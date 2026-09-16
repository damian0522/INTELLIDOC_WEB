package com.umb.intellidoc.web.dto.document;

import java.time.Instant;
import java.util.UUID;

public record HistoryEntryResponse(
        UUID id,
        UUID documentId,
        UUID ownerId,
        Instant date,
        String action
) {
}