package cz.mzk.fofola.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    /**
     * Creates custom executor for PDFGen with thread pool size of 2.
     * Reason: Kramerius PDFResource allows max 2 concurrent sessions
     * @return custom executor with max pool size of 2.
     */
    @Bean(name = "pdfServiceThreadExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("PdfService-");
        executor.initialize();
        return executor;
    }
}
