package cz.mzk.fofola.threads;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FofolaThreadEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public FofolaThreadEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    void publishEvent(String type, String msg) {
        FofolaThreadEvent fofolaThreadEvent = new FofolaThreadEvent(this, type, msg);
        applicationEventPublisher.publishEvent(fofolaThreadEvent);
    }
}
