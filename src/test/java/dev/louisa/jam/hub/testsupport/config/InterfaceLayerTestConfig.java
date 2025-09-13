package dev.louisa.jam.hub.testsupport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.louisa.jam.hub.infrastructure.mail.EmailService;
import dev.louisa.victor.mock.rest.MockRest;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;

@Configuration
public class InterfaceLayerTestConfig {

    @Bean
    public MockRest mockRest(MockMvc mockMvc, ObjectMapper mapper) {
        return new MockRest(mockMvc, mapper);
    }

    @Bean
    public EmailService emailService() {
        return Mockito.mock(EmailService.class); 
    }
}