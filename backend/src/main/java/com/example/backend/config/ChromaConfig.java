package com.example.backend.config;


import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChromaConfig {

    private static final String CHROMA_URL = "http://localhost:8000";
    private static final String COLLECTION_NAME = "cv_embeddings";


    @Bean
    public ChromaEmbeddingStore chromaEmbeddingStore() {
        try {

            ChromaEmbeddingStore store = new ChromaEmbeddingStore(CHROMA_URL, COLLECTION_NAME, Duration.ofSeconds(30), true, true);
            System.out.println("Chroma collection exists or has been created: " + COLLECTION_NAME);
            return store;
        } catch (Exception e) {
            System.err.println("Failed to create Chroma collection: " + e.getMessage());
            throw new RuntimeException("Could not initialize ChromaEmbeddingStore", e);
        }
    }
}
