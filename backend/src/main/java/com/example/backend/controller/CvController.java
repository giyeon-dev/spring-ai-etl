package com.example.backend.controller;

import com.example.backend.dto.CvUploadResponseDto;
import com.example.backend.service.EmbeddingService;
import com.example.backend.service.FileProcessingService;
import com.example.backend.service.FileStorageService;
import com.example.backend.service.LlmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final FileStorageService fileStorageService;
    private final FileProcessingService fileProcessingService;
    private final LlmService llmService;
    private final EmbeddingService embeddingService;

    public CvController(FileStorageService fileStorageService,
                        FileProcessingService fileProcessingService,
                        LlmService llmService,
                        EmbeddingService embeddingService) {
        this.fileStorageService = fileStorageService;
        this.fileProcessingService = fileProcessingService;
        this.llmService = llmService;
        this.embeddingService = embeddingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<CvUploadResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = null;

        try {

            filePath = fileStorageService.saveFile(file);

            List<String> extractedTextList = fileProcessingService.processFile(file);
            String extractedText = String.join("\n", extractedTextList);


            Map<String, Object> structuredData = llmService.extractCvData(extractedText);

            embeddingService.processAndStore(structuredData);


            CvUploadResponseDto response = new CvUploadResponseDto(filePath, "File processed successfully and embeddings stored");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new CvUploadResponseDto(null, "File upload failed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new CvUploadResponseDto(null, "An error occurred"));
        }
    }
}
