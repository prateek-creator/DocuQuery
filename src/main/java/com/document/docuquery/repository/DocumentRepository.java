package com.document.docuquery.repository;

import com.document.docuquery.dto.QaResponse;
import com.document.docuquery.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    // v1 - Basic ILIKE search
    @Query("SELECT new com.document.docuquery.dto.QaResponse(d.id, d.title, d.author, d.content) " +
            "FROM Document d WHERE LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<QaResponse> searchByContentIlike(@Param("query") String query);

    // v2 - Full-text search using tsvector
    @Query(value = """
        SELECT id, title, author, content
        FROM document
        WHERE content_vector @@ plainto_tsquery('english', :query)
        LIMIT 10
    """, nativeQuery = true)
    List<QaResponse> searchByContentFullText(@Param("query") String query);

}
