package com.umb.intellidoc.web.service.authentication;

import com.umb.intellidoc.web.client.intellidoc.IntelliDocApiClient;
import com.umb.intellidoc.web.dto.authentication.LoginRequest;
import com.umb.intellidoc.web.dto.authentication.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final IntelliDocApiClient intelliDocApiClient;

    public AuthenticationService(
            IntelliDocApiClient intelliDocApiClient
    ) {
        this.intelliDocApiClient = intelliDocApiClient;
    }

    public LoginResponse login(LoginRequest request) {
        return intelliDocApiClient.login(request);
    }
}