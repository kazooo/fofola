package cz.mzk.fofola.rest;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.model.dnnt.SugoDataPageDto;
import cz.mzk.fofola.model.dnnt.SugoSessionDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sugo")
@AllArgsConstructor
@Slf4j
public class DnntRest {

    private SugoApi sugoApi;

    @GetMapping(value = "/session", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PutMapping(value = "/session/{sessionId}/pause", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SugoSessionDto pauseSession(@PathVariable final String sessionId) {
        log.info("Pausing session {}", sessionId);
        return sugoApi.pauseSession(sessionId);
    }

    @PutMapping(value = "/session/{sessionId}/launch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SugoSessionDto launchSession(@PathVariable final String sessionId) {
        log.info("Launching session {}", sessionId);
        return sugoApi.launchSession(sessionId);
    }

    @PutMapping(value = "/session/{sessionId}/terminate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SugoSessionDto terminateSession(@PathVariable final String sessionId) {
        log.info("Terminating session {}", sessionId);
        return sugoApi.terminateSession(sessionId);
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
        log.info("Creating job {}", createJob.getTitle());
        return sugoApi.createJob(createJob);
    }

    @PutMapping(value = "/job", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SugoJobDto updateAutomaticJob(@RequestBody final UpdateSugoJobDto updateJob) {
        log.info("Updating job {}", updateJob.getId());
        return sugoApi.updateJob(updateJob);
    }

    @DeleteMapping(value = "/job/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoJobDto deleteAutomaticJob(@PathVariable final String jobId) {
        log.info("Deleting job {}", jobId);
        return sugoApi.deleteJob(jobId);
    }

    @PutMapping(value = "/job/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SugoJobDto toggleAutomaticJobActivity(@PathVariable final String jobId) {
        log.info("Toggling job {}", jobId);
        return sugoApi.toggleJob(jobId);
    }

    @PutMapping(value = "/job/{jobId}/trigger", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SugoJobDto triggerAutomaticJob(@PathVariable final String jobId) {
        log.info("Triggering job {}", jobId);
        return sugoApi.triggerJob(jobId);
    }
}
