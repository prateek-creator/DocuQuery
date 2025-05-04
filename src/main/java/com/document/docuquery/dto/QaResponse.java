package com.document.docuquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Response DTO for document query results")
public class QaResponse {
    @Schema(description = "Document ID", example = "1")
    private Long id;
    @Schema(description = "Title of the document", example = "Spring Boot Guide")
    private String title;
    @Schema(description = "Author of the document", example = "Prateek")
    private String author;
    @Schema(description = "Content or excerpt from the document", example = "Spring Boot makes it easy to create stand-alone...")
    private String content;

    public QaResponse(Long id, String title, String author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public QaResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
