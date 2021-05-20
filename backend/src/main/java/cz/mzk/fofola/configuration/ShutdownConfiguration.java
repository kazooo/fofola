package cz.mzk.fofola.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
@Slf4j
@AllArgsConstructor
public class ShutdownConfiguration implements ServletContextListener {

    private final DataSource dataSource;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            log.info("Closing database connection...");
            synchronized (dataSource) {
                if (!dataSource.getConnection().isClosed()) {
                    dataSource.getConnection().close();
                } else {
                    log.info("Database connection is already closed!");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
