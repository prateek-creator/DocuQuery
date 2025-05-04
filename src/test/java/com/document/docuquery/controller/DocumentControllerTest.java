package com.document.docuquery.controller;

import com.document.docuquery.dto.QaResponse;
import com.document.docuquery.entity.Document;
import com.document.docuquery.service.DocumentService;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadDocument_success() throws IOException, TikaException {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "Dummy content".getBytes());
        Document document = new Document();
        document.setId(1L);
        document.setTitle("Test");

        when(documentService.saveDocument(file)).thenReturn(document);

        ResponseEntity<Document> response = documentController.uploadDocument(file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test", response.getBody().getTitle());
    }

    @Test
    void testSearchV1() {
        String query = "test";
        List<QaResponse> mockResponses = List.of(new QaResponse(1L, "Title", "Author", "Content"));

        when(documentService.searchV1(query)).thenReturn(mockResponses);

        List<QaResponse> result = documentController.searchV1(query);
        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void testSearchV2() {
        String query = "test";
        List<QaResponse> mockResponses = List.of(new QaResponse(2L, "Title2", "Author2", "Content2"));

        when(documentService.searchV2(query)).thenReturn(mockResponses);

        List<QaResponse> result = documentController.searchV2(query);
        assertEquals(1, result.size());
        assertEquals("Title2", result.get(0).getTitle());
    }

    @Test
    void testFilterDocuments() {
        Page<Document> mockPage = new PageImpl<>(List.of(new Document()));
        Sort sort = Sort.by(Sort.Direction.DESC, "uploadDate");

        when(documentService.filterDocuments(
                null, null, null, null, 0, 10, sort
        )).thenReturn(mockPage);

        Page<Document> result = documentController.filterDocuments(
                null, null, null, null, 0, 10, "uploadDate,desc"
        );

        assertEquals(1, result.getTotalElements());
    }
}
