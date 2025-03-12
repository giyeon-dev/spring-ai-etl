package com.example.backend.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ChunkingService {

    private static final int CHUNK_SIZE = 400;
    private static final int OVERLAP_SIZE = 75;

    public Map<String, List<TextSegment>> chunkCv(Map<String, Object> structuredData) {
        Map<String, List<TextSegment>> chunkedSections = new HashMap<>();

        DocumentSplitter splitter = DocumentSplitters.recursive(CHUNK_SIZE, OVERLAP_SIZE);

        structuredData.forEach((section, content) -> {
            String text = content instanceof String ? (String) content : content.toString();
            Document document = Document.from(text);
            List<TextSegment> segments = splitter.split(document);
            chunkedSections.put(section, segments);
        });

        return chunkedSections;
    }



}
