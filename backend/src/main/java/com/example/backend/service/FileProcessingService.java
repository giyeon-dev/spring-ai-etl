package com.example.backend.service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {

    public List<String> processFile(MultipartFile file) {
        try(InputStream inputStream = file.getInputStream()) {
            ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes());

            List<Document> extractedDocuments;

            if (file.getOriginalFilename().endsWith(".pdf")) {
                extractedDocuments = new ParagraphPdfDocumentReader(resource).read();
            } else {
                extractedDocuments = new TikaDocumentReader(resource).read();
            }

            return extractedDocuments.stream()
                    .map(Document::getFormattedContent)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + file.getOriginalFilename(), e);
        }

    }

}
