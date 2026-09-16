package com.umb.intellidoc.web.dto.document;

import com.umb.intellidoc.web.dto.analysis.AnalysisResult;

import java.util.UUID;

public record ProcessDocumentResponse(
        UUID documentId,
        String status,
        String extractedContent,
        AnalysisResult analysis
) {
}