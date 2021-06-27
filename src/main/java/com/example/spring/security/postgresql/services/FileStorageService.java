package com.example.spring.security.postgresql.services;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring.security.postgresql.models.file.Files;
import com.example.spring.security.postgresql.repository.FileRepository;

@Service
public class FileStorageService {

	@Autowired
	private FileRepository fileRepository;
	
	public Files store(MultipartFile file) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    Files Files = new Files(fileName, file.getContentType(), file.getBytes());

	    return fileRepository.save(Files);
	  }

	  public Files getFile(String id) {
	    return fileRepository.findById(id).get();
	  }
	  
	  public Stream<Files> getAllFiles() {
	    return fileRepository.findAll().stream();
	  }
}
