package dev.louisa.jam.hub.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import wiremock.org.eclipse.jetty.http.HttpHeader;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class MockRest {
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    public RequestBuilder post(String uri, Object... uriVars) {
        return new RequestBuilder(mockMvc, mapper, MockMvcRequestBuilders.post(resolveUri(uri, uriVars)));
    }

    public RequestBuilder get(String uri, Object... uriVars) {
        return new RequestBuilder(mockMvc, mapper, MockMvcRequestBuilders.get(resolveUri(uri, uriVars)));
    }

    public RequestBuilder put(String uri, Object... uriVars) {
        return new RequestBuilder(mockMvc, mapper, MockMvcRequestBuilders.put(resolveUri(uri, uriVars)));
    }

    public RequestBuilder patch(String uri, Object... uriVars) {
        return new RequestBuilder(mockMvc, mapper, MockMvcRequestBuilders.patch(resolveUri(uri, uriVars)));
    }

    public RequestBuilder delete(String uri, Object... uriVars) {
        return new RequestBuilder(mockMvc, mapper, MockMvcRequestBuilders.delete(resolveUri(uri, uriVars)));
    }

    private String resolveUri(String uri, Object... uriVars) {
        return org.springframework.web.util.UriComponentsBuilder
                .fromUriString(uri)
                .buildAndExpand(uriVars)
                .toUriString();
    }

    // --- Nested fluent RequestBuilder ---
    @RequiredArgsConstructor
    public static class RequestBuilder {
        private final MockMvc mockMvc;
        private final ObjectMapper mapper;
        private final MockHttpServletRequestBuilder request;

        private HttpStatus expectedStatus = null;
        private RequestPostProcessor userPostProcessor;

        public RequestBuilder body(Object body) throws Exception {
            request.content(mapper.writeValueAsString(body));
            request.contentType(MediaType.APPLICATION_JSON);
            return this;
        }

        public RequestBuilder withRequestHeader(String name, String value) {
            request.header(name, value);
            return this;
        }

        public RequestBuilder withJwt(String token) {
            this.userPostProcessor = addTokenToRequest(token);
            
        return this;
        }

        private RequestPostProcessor addTokenToRequest(String token) {
            return request -> {
                request.addHeader(HttpHeader.AUTHORIZATION.lowerCaseName(), "Bearer " + token);
                return request;
            };
        }


        public RequestBuilder expectResponseStatus(HttpStatus status) {
            this.expectedStatus = status;
            return this;
        }

        private ResultActions exchange() throws Exception {
            ResultActions actions;
            if (userPostProcessor != null) {
                actions = mockMvc.perform(request.with(userPostProcessor));
            } else {
                actions = mockMvc.perform(request);
            }

            if (expectedStatus != null) {
                actions.andExpect(status().is(expectedStatus.value()));
            }
            return actions;
        }

        public void expectNoResponseBody() throws Exception {
            ResultActions actions = exchange();
            MvcResult result = actions.andReturn();
            String content = result.getResponse().getContentAsString();
            if (content != null && !content.isBlank()) {
                throw new AssertionError("Expected no body, but response contained: " + content);
            }
        }

        public <T> T expectResponseBody(Class<T> type) throws Exception {
            ResultActions actions = exchange(); // ensures status check runs if configured
            MvcResult result = actions.andReturn();
            String json = result.getResponse().getContentAsString();
            return mapper.readValue(json, type);
        }
    }
}