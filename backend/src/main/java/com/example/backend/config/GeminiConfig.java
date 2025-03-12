package com.example.backend.config;


import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    @Bean
    public GoogleAiGeminiChatModel geminiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .modelName("gemini-1.5-flash")
                .apiKey(geminiApiKey)
                .temperature(0.2)
                .maxOutputTokens(1024)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return GoogleAiEmbeddingModel.builder()
                .apiKey(geminiApiKey)
                .modelName("text-embedding-004")
                .build();
    }
}
