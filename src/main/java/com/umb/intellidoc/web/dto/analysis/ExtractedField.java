package com.umb.intellidoc.web.dto.analysis;

public record ExtractedField(
        String value,
        Double confidence,
        String sourceText
) {
}