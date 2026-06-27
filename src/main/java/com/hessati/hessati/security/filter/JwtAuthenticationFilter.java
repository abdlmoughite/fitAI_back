package com.hessati.hessati.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        // Disable this filter's default /login processing — auth is handled via REST controller
        setFilterProcessesUrl("/api/auth/login-disabled");
    }
}
