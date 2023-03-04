package cz.mzk.fofola.rest;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.model.dnnt.SugoDataPageDto;
import cz.mzk.fofola.model.dnnt.SugoSessionPageDto;
import cz.mzk.fofola.model.dnnt.SugoTransitionPageDto;
import cz.mzk.fofola.model.dnnt.job.CreateSugoJobDto;
import cz.mzk.fofola.model.dnnt.job.SugoJobDto;
import cz.mzk.fofola.model.dnnt.job.SugoJobPreviewPageDto;
import cz.mzk.fofola.model.dnnt.job.UpdateSugoJobDto;
import cz.mzk.fofola.rest.request.dnnt.SugoDataRequestFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoJobFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoSessionRequestFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoTransitionRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sugo")
@AllArgsConstructor
public class DnntRest {

    private SugoApi sugoApi;

    @GetMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoSessionPageDto getSessions(final SugoSessionRequestFilter requestFilter) {
        return sugoApi.getSessions(requestFilter);
    }

    @GetMapping(value = "/transitions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoTransitionPageDto getTransitions(final SugoTransitionRequestFilter requestFilter) {
        return sugoApi.getTransitions(requestFilter);
    }

    @PostMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoDataPageDto getData(@RequestBody final SugoDataRequestFilter requestFilter) {
        return sugoApi.getData(requestFilter);
    }

    @GetMapping(value = "/job", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoJobPreviewPageDto getAutomaticJobs(final SugoJobFilter filter) {
        return sugoApi.getJobPreviews(filter);
    }

    @GetMapping(value = "/job/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoJobDto getAutomaticJob(@PathVariable final String jobId) {
        return sugoApi.getJob(jobId);
    }

    @PostMapping(value = "/job", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SugoJobDto createAutomaticJob(@RequestBody final CreateSugoJobDto createJob) {
        return sugoApi.createJob(createJob);
    }

    @PutMapping(value = "/job", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SugoJobDto updateAutomaticJob(@RequestBody final UpdateSugoJobDto updateJob) {
        return sugoApi.updateJob(updateJob);
    }

    @DeleteMapping(value = "/job/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoJobDto deleteAutomaticJob(@PathVariable final String jobId) {
        return sugoApi.deleteJob(jobId);
    }

    @PutMapping(value = "/job/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoJobDto toggleAutomaticJobActivity(@PathVariable final String jobId) {
        return sugoApi.toggleJob(jobId);
    }
}
