package com.document.docuquery.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String type;

    @CreationTimestamp
    private LocalDateTime uploadDate;
    @Column(columnDefinition = "tsvector", insertable = false, updatable = false)
    private String contentVector;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Constructors
    public Document() {}
    public Document(Long id, String title, String author, String type, LocalDateTime uploadDate, String content, String contentVector) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
        this.uploadDate = uploadDate;
        this.content = content;
        this.contentVector=contentVector;
    }

    public String getContentVector() {
        return contentVector;
    }

    public void setContentVector(String contentVector) {
        this.contentVector = contentVector;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    // Manual Builder
    public static class Builder {
        private Long id;
        private String title;
        private String author;
        private String type;
        private LocalDateTime uploadDate;
        private String content;
        private String contentVector;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder uploadDate(LocalDateTime uploadDate) {
            this.uploadDate = uploadDate;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder contentVector(String contentVector){
            this.contentVector=contentVector;
            return this;
        }

        public Document build() {
            return new Document(id, title, author, type, uploadDate, content, contentVector);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
