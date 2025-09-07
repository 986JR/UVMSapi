package com.uvms.apiuvms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads/contracts");

    public String save(MultipartFile file) {
        try {
            // Ensure directory exists
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                System.out.println("Created directory: " + root.toAbsolutePath());
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = root.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File saved at: " + filePath.toAbsolutePath());

            return filePath.toString(); // store this in DB
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed", e);
        }
    }
}

