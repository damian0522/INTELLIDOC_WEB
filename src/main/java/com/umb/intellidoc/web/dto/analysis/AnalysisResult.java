package com.umb.intellidoc.web.dto.analysis;

import java.util.Map;

public record AnalysisResult(
        String documentType,
        Map<String, ExtractedField> requestedFields,
        Map<String, ExtractedField> additionalFindings
) {
}