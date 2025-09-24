package dev.louisa.jam.hub.infrastructure.security.jwt;

import dev.louisa.jam.hub.infrastructure.security.SecurityLevelResolver;
import dev.louisa.jam.hub.infrastructure.security.jwt.authenticator.JwtAuthenticationService;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest extends BaseInfraStructureTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    @Mock
    private SecurityLevelResolver securityLevelResolver;
    
    @Mock
    private JwtAuthenticationService jwtAuthenticationService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtAuthenticationService, securityLevelResolver);
    }
    
    @Test
    void shouldAuthenticateRequest() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer some-jwt-token");
     
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
    
        verify(jwtAuthenticationService).authenticate("some-jwt-token");
        verify(filterChain).doFilter(request, response);
        
    }
}