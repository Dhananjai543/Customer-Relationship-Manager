package com.dhananjai.crm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dhananjai.crm.entity.User;
import com.dhananjai.crm.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1 obtain header that contains jwt

    	String path = request.getRequestURI();
        if (path.equals("/") || path.equals("/resources/**") || path.equals("/error") || path.equals("/processLoginForm")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization"); // Bearer jwt

//        if(authHeader == null || !authHeader.startsWith("Bearer ")){
//            filterChain.doFilter(request, response);
//            return;
//        }

        
        // 2 obtain jwt token
//        String jwt = authHeader.split(" ")[1];
        
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.split(" ")[1];
        } else {
            // If JWT is not found in the Authorization header, check cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                    }
                }
            }
        }
        

        if (jwt != null) {
        	
        	boolean isExpired = jwtService.isTokenExpired(jwt);
            
            if(isExpired) {
            	System.out.println("Token is expired");
            	filterChain.doFilter(request, response);
            	return;
            }
            
            // Obtain subject/username from JWT
            String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsername(username).orElse(null);

                if (user != null && !jwtService.isTokenExpired(jwt) && jwtService.validateToken(jwt, user)) {
                    // Set authenticate object inside our security context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        // 5 execute rest of the filters

        filterChain.doFilter(request, response);

    }
}