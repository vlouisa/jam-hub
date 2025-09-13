package dev.louisa.jam.hub.testsupport.config;

import dev.louisa.victor.mail.pit.docker.MailPitContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MailPitTestConfiguration {
    @Bean
    public MailPitContainer mailPitContainer() {
        return new MailPitContainer();
    }
}