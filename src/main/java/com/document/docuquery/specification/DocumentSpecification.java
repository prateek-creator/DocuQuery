package com.document.docuquery.specification;

import com.document.docuquery.entity.Document;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DocumentSpecification {
    public static Specification<Document> filterBy(String author, String type, LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (author != null && !author.isEmpty()) {
                predicates.add(cb.equal(root.get("author"), author));
            }

            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("uploadDate"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("uploadDate"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
