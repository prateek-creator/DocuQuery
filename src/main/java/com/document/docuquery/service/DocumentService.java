package com.document.docuquery.service;

import com.document.docuquery.dto.QaResponse;
import com.document.docuquery.entity.Document;
import com.document.docuquery.repository.DocumentRepository;
import com.document.docuquery.specification.DocumentSpecification;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service

public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @CacheEvict(value = "qaSearchCache", allEntries = true)
    public Document saveDocument(MultipartFile file) throws IOException, TikaException {
        String text=new Tika().parseToString(file.getInputStream());
        Document doc=Document.builder().title(file.getOriginalFilename()).type(file.getContentType())
                .author("prateek").content(text).build();
        return documentRepository.save(doc);
    }
    @Cacheable(value = "qaSearchCache", key="#query")
    public List<QaResponse> searchV1(String query) {
        return documentRepository.searchByContentIlike(query);
    }

    public List<QaResponse> searchV2(String query) {
        return documentRepository.searchByContentFullText(query);
    }
    public Page<Document> filterDocuments(String author, String type, LocalDate fromDate, LocalDate toDate, int page, int size, Sort sort){
        Pageable pageable= PageRequest.of(page,size,sort);
        Specification<Document> spec= DocumentSpecification.filterBy(author,type,fromDate,toDate);
        return documentRepository.findAll(spec,pageable);
    }
}
