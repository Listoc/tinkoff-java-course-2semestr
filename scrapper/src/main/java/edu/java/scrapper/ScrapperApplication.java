package edu.java.scrapper;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ClientProperties;
import edu.java.scrapper.configuration.UpdaterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({ApplicationConfig.class, ClientProperties.class, UpdaterProperties.class})
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
