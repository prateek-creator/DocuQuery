package com.document.docuquery.controller;

import com.document.docuquery.dto.QaResponse;
import com.document.docuquery.entity.Document;
import com.document.docuquery.service.DocumentService;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/documents")
public class DocumentController {
    private  final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file){
        try {
            Document saved=documentService.saveDocument(file);
            return ResponseEntity.ok(saved);
        }
        catch (IOException ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (TikaException ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/v1/qa")
    public List<QaResponse> searchV1(@RequestParam String query) {
        return documentService.searchV1(query);
    }

    @GetMapping("/v2/qa")
    public List<QaResponse> searchV2(@RequestParam String query) {
        return documentService.searchV2(query);
    }
}
