package com.project.chatop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileService {
	private static final String UPLOAD_DIR = "uploads/images/";
	
	@Value("${server.port}")
	private int serverPort;
	
	@Value("${server.address}")
	private String serverAddress;

	/*
     Sauvegarde un fichier sur le serveur et retourne son URL publique.
    */
    public String save(MultipartFile file) throws IOException {
    	 String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

         Path uploadPath = Paths.get(UPLOAD_DIR);
         if (!Files.exists(uploadPath)) {
             Files.createDirectories(uploadPath);
         }

         Path filePath = uploadPath.resolve(fileName);
         Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

         String serverUrl = "http://" + serverAddress + ":" + serverPort;
         
         return serverUrl + "/images/" + fileName;
    }
}
