package com.document.docuquery.controller;

import com.document.docuquery.dto.QaResponse;
import com.document.docuquery.entity.Document;
import com.document.docuquery.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.tika.exception.TikaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/documents")
public class DocumentController {
    private  final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @Operation(summary = "Upload a document (PDF, Word, etc.)", description = "Accepts a multipart file and stores it along with metadata.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded document",
                    content = @Content(schema = @Schema(implementation = Document.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Query documents using simple keyword search (v1)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QaResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/v1/qa")
    public List<QaResponse> searchV1(@RequestParam String query) {
        return documentService.searchV1(query);
    }
    @Operation(summary = "Query documents using enhanced search (v2)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = QaResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/v2/qa")
    public List<QaResponse> searchV2(@RequestParam String query) {
        return documentService.searchV2(query);
    }
    @Operation(summary = "Filter documents by metadata with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered documents list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Document.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/filter")
    public Page<Document> filterDocuments(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate uploadDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate uploadDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "uploadDate,desc") String sort
    ){
        String[] sortParams=sort.split(",");
        Sort.Direction direction=Sort.Direction.fromString(sortParams[1]);
        Sort sortObj=Sort.by(direction,sortParams[0]);
        return documentService.filterDocuments(author, type, uploadDateFrom, uploadDateTo, page, size, sortObj);
    }
}
