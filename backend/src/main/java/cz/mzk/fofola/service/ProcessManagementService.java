package cz.mzk.fofola.service;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.configuration.AppProperties;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessDTO;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.ProcessType;
import cz.mzk.fofola.model.solr.ProcessingDoc;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.FProcessRepository;
import cz.mzk.fofola.process.ProcessEventNotifier;
import cz.mzk.fofola.repository.SolrRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


@Service
@AllArgsConstructor
@Slf4j
public class ProcessManagementService {

    private final ProcessEventNotifier eventNotifier;
    private final FProcessRepository processRepository;
    private final AppProperties appProperties;
    private final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    private final KrameriusApi krameriusApi;
    private final SugoApi sugoApi;
    private final SolrRepository<SearchDoc> solrSearchRepository;
    private final SolrRepository<ProcessingDoc> solrProcessingRepository;

    public String start(ProcessType type, Map<String, Object> data)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String pid = UUID.randomUUID().toString();

        ProcessParams params = new ProcessParams();
        params.setId(pid);
        params.setConfig(appProperties);
        params.setData(data);
        params.setEventNotifier(eventNotifier);
        params.setType(type);
        params.setKrameriusApi(krameriusApi);
        params.setSugoApi(sugoApi);
        params.setSolrSearchRepository(solrSearchRepository);
        params.setSolrProcessingRepository(solrProcessingRepository);

        Process process = instantiate(type, params);
        threadPoolExecutor.execute(() -> {
            Thread.currentThread().setName(pid);
            process.run();
        });
        return pid;
    }

    public void terminate(String id) {
        Thread thread = getThreadByName(id);
        if (thread != null) {
            log.info("Terminating process " + id + "...");
            thread.interrupt();
            log.info("Process " + id + " is terminated!");
        }
    }

    public List<ProcessDTO> findAllProcess() {
        return processRepository.findAll(Sort.by(Sort.Direction.DESC, "start"));
    }

    public void removeInfoFromDB(String id) {
        processRepository.deleteById(id);
    }

    private Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Process instantiate(ProcessType type, ProcessParams params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class processClass = type.getProcessClass();
        if (processClass == null)
            throw new IllegalStateException("No defined process class for type \"" + type.toString() + "\"!");
        Constructor<?> ctor = processClass.getConstructor(params.getClass());
        Object object = ctor.newInstance(params);
        return (Process) object;
    }
}
