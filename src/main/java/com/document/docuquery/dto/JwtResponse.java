package com.document.docuquery.dto;

import java.util.List;

public class JwtResponse {
    String token;
    String userName;
    List<String> response;

    public JwtResponse(String token, String userName, List<String> response) {
        this.token = token;
        this.userName = userName;
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getResponse() {
        return response;
    }

    public void setResponse(List<String> response) {
        this.response = response;
    }
}
