package cz.mzk.fofola.rest;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.model.dnnt.SugoSessionPageDto;
import cz.mzk.fofola.rest.request.SugoSessionRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/sugo")
@AllArgsConstructor
public class DnntRest {

    private SugoApi sugoApi;

    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public SugoSessionPageDto getSessions(final SugoSessionRequestFilter requestFilter) {
        return sugoApi.getSessions(requestFilter);
    }
}
