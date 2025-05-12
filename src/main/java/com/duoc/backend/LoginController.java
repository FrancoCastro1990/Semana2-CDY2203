package com.duoc.backend;
import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;


@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (!passwordEncoder.matches(loginRequest.getPassword(), passwordEncoder.encode(userDetails.getPassword()))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            String token = jwtAuthenticationConfig.getJWTToken(loginRequest.getUsername());
            return ResponseEntity.ok(HtmlUtils.htmlEscape(token));
        } catch (UsernameNotFoundException e) {
            // Mismo tiempo de respuesta para usuario no encontrado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}