package cz.mzk.fofola.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

        log.warn("Exception Cause - " + throwable.getMessage());
        log.warn("Method name - " + method.getName());
        for (Object param : obj) {
            log.warn("Parameter value - " + param);
        }
    }
}
