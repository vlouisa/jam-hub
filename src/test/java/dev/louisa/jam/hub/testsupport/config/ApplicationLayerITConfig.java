package dev.louisa.jam.hub.testsupport.config;

import dev.louisa.victor.mail.pit.docker.MailPitContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("application-it")
public class ApplicationLayerITConfig {

    @Bean
    public MailPitContainer mailPitContainer() {
        return new MailPitContainer();
    }
}