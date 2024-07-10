package com.dhananjai.crm.authentication;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dhananjai.crm.config.JwtService;
import com.dhananjai.crm.entity.User;
import com.dhananjai.crm.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private JwtService jwtService;

   private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public AuthenticationResponse register(User request){
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        System.out.println(token);
        return  new AuthenticationResponse(token);
    }




    public AuthenticationResponse login(AuthenticationRequest authenticationRequest,  HttpServletResponse response){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        );
        
        Authentication authentication = authenticationManager.authenticate(authToken);
//        User user = userRepository.findByUsername(authenticationRequest.getUsername()).get();
//        if(authentication.isAuthenticated()) {
//        	String refreshToken = jwtService.generateToken(user, generateExtraClaims(user));
//        	AuthenticationResponse authResponse = new AuthenticationResponse(refreshToken);
//            // set accessToken to cookie header
//        	
//            ResponseCookie cookie = ResponseCookie.from("accessToken", refreshToken)
//                    .httpOnly(true)
//                    .secure(false)
//                    .path("/")
//                    .maxAge(Duration.ofMinutes(Long.valueOf("60")))
//                    .build();
//            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//            return new AuthenticationResponse(refreshToken);
//        }else {
//        	throw new UsernameNotFoundException("invalid user request..!!");
//        }
        
        
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(jwt);
    }




    private Map<String, Object> generateExtraClaims(User user) {
    	Object s = user.getRole();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", s);
        return extraClaims;
    }
}