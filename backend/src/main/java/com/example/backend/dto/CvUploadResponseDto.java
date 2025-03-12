package com.example.backend.dto;
import lombok.Getter;

@Getter
public class CvUploadResponseDto {
    private final String filePath;
    private final String message;

    public CvUploadResponseDto(String filePath, String message) {
        this.filePath = filePath;
        this.message = message;
    }
}
