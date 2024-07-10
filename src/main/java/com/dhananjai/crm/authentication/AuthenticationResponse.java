package com.dhananjai.crm.authentication;

import lombok.Builder;

@Builder
public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String gettoken() {
        return token;
    }

    public void settoken(String token) {
        this.token = token;
    }
}