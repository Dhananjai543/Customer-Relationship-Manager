package com.dhananjai.crm.authentication;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dhananjai.crm.entity.User;

@RestController
@RequestMapping("/")
@Tag(name = "Auth APIs", description = "APIs for user authentication and registration")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
//        return ResponseEntity.ok(authenticationService.login(request));
//    }
}