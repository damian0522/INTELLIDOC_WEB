package com.umb.intellidoc.web.controller.document;

import com.umb.intellidoc.web.dto.document.ProcessDocumentResponse;
import com.umb.intellidoc.web.service.document.DocumentWorkflowService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.umb.intellidoc.web.dto.document.DocumentResponse;
import com.umb.intellidoc.web.dto.document.HistoryEntryResponse;

import java.util.Comparator;
import java.util.List;

import java.util.UUID;

@Controller
public class DocumentController {

    private final DocumentWorkflowService documentWorkflowService;
    private static final UUID DEMO_OWNER_ID =
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    public DocumentController(
            DocumentWorkflowService documentWorkflowService
    ) {
        this.documentWorkflowService = documentWorkflowService;
    }

    @GetMapping("/")
    public String home(Model model) {

        UUID ownerId = UUID.fromString(
                "550e8400-e29b-41d4-a716-446655440000"
        );

        List<DocumentResponse> documents =
                documentWorkflowService.getDocumentsByOwner(ownerId);

        long totalDocuments = documents.size();

        long processedDocuments = documents.stream()
                .filter(document ->
                        "PROCESSED".equals(document.status()))
                .count();

        long pendingDocuments = documents.stream()
                .filter(document ->
                        !"PROCESSED".equals(document.status())
                                && !"FAILED".equals(document.status()))
                .count();

        List<DocumentResponse> recentDocuments =
                documents.stream()
                        .sorted(
                                Comparator.comparing(
                                        DocumentResponse::createdAt,
                                        Comparator.nullsLast(
                                                Comparator.reverseOrder()
                                        )
                                )
                        )
                        .limit(5)
                        .toList();

        model.addAttribute(
                "totalDocuments",
                totalDocuments
        );

        model.addAttribute(
                "processedDocuments",
                processedDocuments
        );

        model.addAttribute(
                "pendingDocuments",
                pendingDocuments
        );

        model.addAttribute(
                "recentDocuments",
                recentDocuments
        );

        return "home/index";
    }

    @PostMapping("/documents/process")
    public String processDocument(
            @RequestParam("file") MultipartFile file,
            Model model
    ) {

        try {

            UUID ownerId = UUID.fromString(
                    "550e8400-e29b-41d4-a716-446655440000"
            );

            ProcessDocumentResponse result =
                    documentWorkflowService.processDocument(
                            ownerId,
                            file
                    );

            model.addAttribute("result", result);

            return "result";

        } catch (Exception exception) {

            model.addAttribute(
                    "error",
                    exception.getMessage()
            );

            return "index";
        }
    }

    @GetMapping("/documents/{documentId}/result/download")
    public ResponseEntity<byte[]> downloadResult(
            @PathVariable UUID documentId
    ) {

        byte[] content =
                documentWorkflowService.downloadDocumentResult(
                        documentId
                );

        String filename =
                "resultado-" + documentId + ".json";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\""
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(content);
    }

    @GetMapping("/documents/upload")
    public String uploadPage() {
        return "documents/upload";
    }

    @GetMapping("/documents")
    public String documents(Model model) {

        List<DocumentResponse> documents =
                documentWorkflowService.getDocumentsByOwner(
                        DEMO_OWNER_ID
                );

        model.addAttribute("documents", documents);

        return "documents";
    }

    @GetMapping("/documents/{documentId}")
    public String documentDetail(
            @PathVariable UUID documentId,
            Model model
    ) {

        DocumentResponse document =
                documentWorkflowService.getDocument(documentId);

        model.addAttribute("document", document);

        try {

            ProcessDocumentResponse result =
                    documentWorkflowService.getDocumentResult(
                            documentId
                    );

            model.addAttribute("result", result);

        } catch (Exception exception) {

            model.addAttribute("result", null);

        }

        return "document-detail";
    }

    @GetMapping("/documents/{documentId}/history")
    public String documentHistory(
            @PathVariable UUID documentId,
            Model model
    ) {

        DocumentResponse document =
                documentWorkflowService.getDocument(documentId);

        List<HistoryEntryResponse> history =
                documentWorkflowService.getDocumentHistory(
                        documentId
                );

        model.addAttribute("document", document);
        model.addAttribute("history", history);

        return "history";
    }
}