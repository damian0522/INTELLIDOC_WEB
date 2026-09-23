package com.umb.intellidoc.web.dto.authentication;

public record LoginRequest(
        String email,
        String password
) {
}