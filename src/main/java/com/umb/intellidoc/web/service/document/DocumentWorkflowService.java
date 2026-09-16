package com.umb.intellidoc.web.service.document;

import com.umb.intellidoc.web.client.intellidoc.IntelliDocApiClient;
import com.umb.intellidoc.web.dto.document.DocumentResponse;
import com.umb.intellidoc.web.dto.document.HistoryEntryResponse;
import com.umb.intellidoc.web.dto.document.ProcessDocumentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentWorkflowService {

    private final IntelliDocApiClient intelliDocApiClient;

    public DocumentWorkflowService(
            IntelliDocApiClient intelliDocApiClient
    ) {
        this.intelliDocApiClient = intelliDocApiClient;
    }

    public ProcessDocumentResponse processDocument(
            UUID ownerId,
            MultipartFile file
    ) throws IOException {

        // 1. Crear documento
        DocumentResponse createdDocument =
                intelliDocApiClient.createDocument(
                        ownerId,
                        file
                );

        UUID documentId = createdDocument.id();

        // 2. Validar documento
        intelliDocApiClient.validateDocument(
                documentId,
                ownerId,
                file
        );

        // 3. Almacenar documento
        intelliDocApiClient.storeDocument(
                documentId
        );

        // 4. Procesar documento
        return intelliDocApiClient.processDocument(
                documentId
        );
    }

    public DocumentResponse getDocument(UUID documentId) {
        return intelliDocApiClient.getDocument(documentId);
    }

    public ProcessDocumentResponse getDocumentResult(
            UUID documentId
    ) {
        return intelliDocApiClient.getDocumentResult(documentId);
    }

    public List<DocumentResponse> getDocumentsByOwner(
            UUID ownerId
    ) {
        return intelliDocApiClient.getDocumentsByOwner(ownerId);
    }

    public List<HistoryEntryResponse> getDocumentHistory(
            UUID documentId
    ) {
        return intelliDocApiClient.getDocumentHistory(documentId);
    }

    public byte[] downloadDocumentResult(
            UUID documentId
    ) {
        return intelliDocApiClient.downloadDocumentResult(documentId);
    }
}