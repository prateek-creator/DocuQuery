package com.document.docuquery.service;

import com.document.docuquery.dto.QaResponse;
import com.document.docuquery.entity.Document;
import com.document.docuquery.repository.DocumentRepository;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service

public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document saveDocument(MultipartFile file) throws IOException, TikaException {
        String text=new Tika().parseToString(file.getInputStream());
        Document doc=Document.builder().title(file.getOriginalFilename()).type(file.getContentType())
                .author("prateek").content(text).build();
        return documentRepository.save(doc);
    }
    public List<QaResponse> searchV1(String query) {
        return documentRepository.searchByContentIlike(query);
    }

    public List<QaResponse> searchV2(String query) {
        return documentRepository.searchByContentFullText(query);
    }
}
