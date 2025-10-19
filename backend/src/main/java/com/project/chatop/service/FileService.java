package com.project.chatop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileService {
	private static final String UPLOAD_DIR = "src/main/resources/static/images/";
	private static final String SERVER_URL = "http://localhost:3001";

    public String save(MultipartFile file) throws IOException {
    	 String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

         Path uploadPath = Paths.get(UPLOAD_DIR);
         if (!Files.exists(uploadPath)) {
             Files.createDirectories(uploadPath);
         }

         Path filePath = uploadPath.resolve(fileName);
         Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

         // Retourne l’URL complète pour que le front puisse charger l’image
         return SERVER_URL + "/images/" + fileName;
    }
}
