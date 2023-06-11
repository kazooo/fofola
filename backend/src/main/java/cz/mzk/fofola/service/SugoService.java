package cz.mzk.fofola.service;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.model.dnnt.alert.*;
import cz.mzk.fofola.model.doc.SolrDocument;
import cz.mzk.fofola.repository.SolrDocumentRepository;
import cz.mzk.fofola.rest.request.dnnt.SugoAlertFilter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SugoService {

    private SugoApi sugoApi;
    private SolrDocumentRepository solrDocumentRepository;

    public SugoAlertPreviewPageDto getAlertPreviews(final SugoAlertFilter filter) {
        final SugoRawAlertPreviewPageDto rawAlertPreviews = sugoApi.getAlertPreviews(filter);
        return SugoAlertPreviewPageDto.builder()
                .numFound(rawAlertPreviews.getNumFound())
                .entities(rawAlertPreviews.getEntities().stream()
                        .map(this::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    public SugoAlertDto getAlert(final String alertId) {
        return convert(sugoApi.getAlert(alertId));
    }

    public SugoAlertDto solveAlert(final String alertId) {
        return convert(sugoApi.solveAlert(alertId));
    }

    public SugoAlertStats getAlertStats() {
        return sugoApi.getAlertStats();
    }

    private SugoAlertPreviewDto convert(final SugoRawAlertPreviewDto rawAlert) {
        return SugoAlertPreviewDto.builder()
                .id(rawAlert.getId())
                .type(rawAlert.getType())
                .issueType(rawAlert.getIssueType())
                .parameters(rawAlert.getParameters())
                .created(rawAlert.getCreated())
                .build();
    }

    private SugoAlertDto convert(final SugoRawAlertDto rawAlert) {
        return SugoAlertDto.builder()
                .id(rawAlert.getId())
                .type(rawAlert.getType())
                .issueType(rawAlert.getIssueType())
                .created(rawAlert.getCreated())
                .parameters(rawAlert.getParameters())
                .solved(rawAlert.getSolved())
                .session(rawAlert.getSession())
                .document(getDocumentState(rawAlert.getParameters()))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SugoDocumentState getDocumentState(final Map<String, Object> parameters) {
        if (parameters == null || parameters.isEmpty() || !parameters.containsKey(Parameters.UUID)) {
            return null;
        }

        final SolrDocument doc = solrDocumentRepository.getByUuid((String) parameters.get(Parameters.UUID));
        if (doc != null) {
            final SugoDocumentState documentState = new SugoDocumentState();
            documentState.setTitle(doc.getRootTitle());
            documentState.setRoot(doc.getRootPid());
            documentState.setUuid((String) parameters.get(Parameters.UUID));
            if (parameters.containsKey(Parameters.PATH)) {
                final List<String> path = (List<String>) parameters.get(Parameters.PATH);
                documentState.setPath(StringUtils.join(path, "/"));
            }
            documentState.setCurrentLabels((List<String>) parameters.get(Parameters.CURRENT_LABELS));
            documentState.setNextLabels((List<String>) parameters.get(Parameters.NEXT_LABELS));
            return documentState;
        }
        return null;
    }

    private static class Parameters {
        public static String UUID = "uuid";
        public static String PATH = "path";
        public static String CURRENT_LABELS = "currentLabels";
        public static String NEXT_LABELS = "nextLabels";
    }
}
