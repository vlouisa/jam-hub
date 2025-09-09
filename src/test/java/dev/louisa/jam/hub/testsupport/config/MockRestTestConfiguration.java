package dev.louisa.jam.hub.testsupport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.louisa.victor.mock.rest.MockRest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
public class MockRestTestConfiguration {
    @Bean
    public MockRest mockRest(MockMvc mockMvc, ObjectMapper mapper) {
        return new MockRest(mockMvc, mapper);
    }
}