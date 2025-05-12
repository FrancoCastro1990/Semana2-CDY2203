package com.duoc.backend;

import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Usar @MockBean en lugar de @MockitoBean + @Autowired
    @MockBean
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        String username = "root";
        String password = "123456";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword); 
        user.setEnabled(true);
        
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        String baseUrl = "http://localhost:" + port + "/login";
        User loginRequest = new User();
        loginRequest.setUsername("root");
        loginRequest.setPassword("123456");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(loginRequest, headers);
        
        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().startsWith("Bearer "));
        
        verify(userDetailsService, times(1)).loadUserByUsername("root");
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange
        String baseUrl = "http://localhost:" + port + "/login";
        User loginRequest = new User();
        loginRequest.setUsername("root"); // Usar un usuario que existe
        loginRequest.setPassword("contraseñaIncorrecta"); // Pero con contraseña incorrecta
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(loginRequest, headers);
        
        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
        
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        
        verify(userDetailsService, times(1)).loadUserByUsername("root");
    }
}