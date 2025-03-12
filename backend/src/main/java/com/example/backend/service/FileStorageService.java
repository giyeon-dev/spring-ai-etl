package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = this.uploadDir + fileName;

        File targetFile = new File(filePath);
        file.transferTo(targetFile);

        return filePath;
    }
}
