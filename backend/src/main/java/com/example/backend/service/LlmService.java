package com.example.backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LlmService {

    private final ChatLanguageModel chatLanguageModel;
    private final ObjectMapper objectMapper;

    public LlmService(ChatLanguageModel chatLanguageModel, ObjectMapper objectMapper) {
        this.chatLanguageModel = chatLanguageModel;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> extractCvData(String extractedText) {
        String prompt = generatePrompt(extractedText);

        try {
            String jsonResponse = chatLanguageModel.chat(prompt);

            return objectMapper.readValue(jsonResponse, HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Error processing LLM response", e);
        }


    }


    private String generatePrompt(String extractedText) {
        return """
        You are an AI assistant specializing in resume data extraction. 
        Your task is to accurately extract structured information from a resume.

        ⚠️ **Extraction Rules:**
        1. **Do not make assumptions**—if data is missing, set `"null"`.
        2. **Date Format:** Use `"yyyy-MM-dd"` (e.g., `"1990-05-15"`).
        3. **Email Format:** Must follow `username@domain.com`. If missing, return `"null"`.
        4. **Extract work experience in chronological order** (most recent first).
        5. **Technical skills should be a list of unique, comma-separated values**.
        6. **If multiple positions exist in a company, list them separately.**
        7. **For projects, extract team size, duration, technologies used, and responsibilities.**

        📌 **Expected JSON Output:**
        ```json
        {
          "personal_information": {
            "name": "John Doe",
            "email": "johndoe@example.com",
            "position": "Software Engineer",
            "date_of_birth": "1990-05-15"
          },
          "educational_background": [
            {
              "degree": "Bachelor's in Computer Science",
              "institution": "Stanford University",
              "year_of_graduation": "2016"
            }
          ],
          "work_experience": [
            {
              "company": "Google",
              "position": "Senior Backend Engineer",
              "start_date": "2017-01-01",
              "end_date": "2022-06-30",
              "responsibilities": [
                "Developed scalable microservices using Spring Boot",
                "Optimized database queries in PostgreSQL",
                "Led a backend team of 5 engineers"
              ]
            }
          ],
          "technical_skills": ["Java", "Spring Boot", "AWS", "Docker", "Kubernetes"],
          "certificates": ["AWS Solutions Architect Associate"],
          "professional_summary": "Experienced backend engineer specializing in cloud-native applications...",
          "language_skills": ["English", "Spanish"],
          "project_experience": [
            {
              "name": "E-commerce Platform",
              "duration": "2021-06-01 ~ 2022-05-30",
              "team_size": 6,
              "position": "Lead Backend Engineer",
              "description": "Built a scalable e-commerce system with microservices.",
              "responsibility": [
                "Designed RESTful APIs",
                "Integrated payment processing with Stripe",
                "Ensured high availability using AWS auto-scaling"
              ],
              "technology": ["Spring Boot", "PostgreSQL", "Docker", "AWS"]
            }
          ]
        }
        ```

        **Resume Content:**
        ```
        %s
        ```
        """.formatted(extractedText);
    }
}
