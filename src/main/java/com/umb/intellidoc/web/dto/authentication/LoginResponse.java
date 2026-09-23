package com.umb.intellidoc.web.dto.authentication;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String name,
        String email,
        String role
) {
}