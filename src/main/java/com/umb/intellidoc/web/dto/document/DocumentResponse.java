package com.umb.intellidoc.web.dto.document;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        UUID ownerId,
        String originalFilename,
        String contentType,
        Long sizeInBytes,
        String status,
        String storagePath,
        Instant createdAt,
        Instant updatedAt
) {
}