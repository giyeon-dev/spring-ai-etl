package com.example.backend.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final ChromaEmbeddingStore chromaEmbeddingStore;
    private final ChunkingService chunkingService; //

    public EmbeddingService(EmbeddingModel embeddingModel, ChromaEmbeddingStore chromaEmbeddingStore, ChunkingService chunkingService) {
        this.embeddingModel = embeddingModel;
        this.chromaEmbeddingStore = chromaEmbeddingStore;
        this.chunkingService = chunkingService;
    }

    public void processAndStore(Map<String, Object> structuredData) {

        List<TextSegment> allChunks = chunkingService.chunkCv(structuredData)
                .values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<Embedding> embeddings = allChunks.stream()
                .map(chunk -> embeddingModel.embed(chunk).content())
                .collect(Collectors.toList());

        chromaEmbeddingStore.addAll(embeddings, allChunks);


    }

}
