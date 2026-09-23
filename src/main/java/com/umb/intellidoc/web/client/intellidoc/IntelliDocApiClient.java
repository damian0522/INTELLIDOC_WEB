package com.umb.intellidoc.web.client.intellidoc;

import com.umb.intellidoc.web.dto.authentication.LoginRequest;
import com.umb.intellidoc.web.dto.authentication.LoginResponse;
import com.umb.intellidoc.web.dto.document.DocumentResponse;
import com.umb.intellidoc.web.dto.document.ProcessDocumentResponse;
import com.umb.intellidoc.web.dto.document.HistoryEntryResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class IntelliDocApiClient {

    private final RestClient restClient;

    public IntelliDocApiClient(RestClient intelliDocRestClient) {
        this.restClient = intelliDocRestClient;
    }

    public DocumentResponse createDocument(
            UUID ownerId,
            MultipartFile file
    ) throws IOException {

        ByteArrayResource fileResource =
                new ByteArrayResource(file.getBytes()) {

                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("ownerId", ownerId.toString());
        body.add("file", fileResource);

        return restClient.post()
                .uri("/api/v1/documents")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(DocumentResponse.class);
    }

    public DocumentResponse validateDocument(
            UUID documentId,
            UUID ownerId,
            MultipartFile file
    ) throws IOException {

        ByteArrayResource fileResource =
                new ByteArrayResource(file.getBytes()) {

                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("ownerId", ownerId.toString());
        body.add("file", fileResource);

        return restClient.post()
                .uri(
                        "/api/v1/documents/{documentId}/validation",
                        documentId
                )
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(DocumentResponse.class);
    }

    public DocumentResponse storeDocument(
            UUID documentId
    ) {

        return restClient.post()
                .uri(
                        "/api/v1/documents/{documentId}/storage",
                        documentId
                )
                .retrieve()
                .body(DocumentResponse.class);
    }

    public ProcessDocumentResponse processDocument(
            UUID documentId
    ) {

        return restClient.post()
                .uri(
                        "/api/v1/documents/{documentId}/process",
                        documentId
                )
                .retrieve()
                .body(ProcessDocumentResponse.class);
    }

    public DocumentResponse getDocument(UUID documentId) {

        return restClient.get()
                .uri("/api/v1/documents/{documentId}", documentId)
                .retrieve()
                .body(DocumentResponse.class);
    }

    public ProcessDocumentResponse getDocumentResult(UUID documentId) {

        return restClient.get()
                .uri(
                        "/api/v1/documents/{documentId}/result",
                        documentId
                )
                .retrieve()
                .body(ProcessDocumentResponse.class);
    }

    public List<DocumentResponse> getDocumentsByOwner(
            UUID ownerId
    ) {

        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/documents")
                                .queryParam("ownerId", ownerId)
                                .build()
                )
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<>() {});
    }

    public List<HistoryEntryResponse> getDocumentHistory(
            UUID documentId
    ) {

        return restClient.get()
                .uri(
                        "/api/v1/documents/{documentId}/history",
                        documentId
                )
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<>() {});
    }

    public byte[] downloadDocumentResult(
            UUID documentId
    ) {

        return restClient.get()
                .uri(
                        "/api/v1/documents/{documentId}/result/download",
                        documentId
                )
                .retrieve()
                .body(byte[].class);
    }

    public LoginResponse login(LoginRequest request) {

        return restClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(LoginResponse.class);
    }
}