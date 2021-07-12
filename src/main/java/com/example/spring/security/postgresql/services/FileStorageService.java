package com.example.spring.security.postgresql.services;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring.security.postgresql.models.User;
import com.example.spring.security.postgresql.models.file.Files;
import com.example.spring.security.postgresql.repository.FileRepository;
import com.example.spring.security.postgresql.repository.UserRepository;
import com.example.spring.security.postgresql.security.services.UserDetailsImpl;

@Service
@Transactional 
//Unable to access lob stream; nested exception is org.hibernate.HibernateException: Added @Transactional
public class FileStorageService {

	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Files store(MultipartFile file,UserDetailsImpl userDetails) throws IOException { 
	    Optional<User> user = userRepository.findById(userDetails.getId());
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    Files files = new Files(fileName, file.getContentType(), file.getBytes(),user.get()); 
	    return fileRepository.save(files);
	  }

	  public Files getFile(String id) {
	    return fileRepository.findById(id).get();
	  }
	  
	  public Stream<Files> getAllFiles() {
	    return fileRepository.findAll().stream();
	  }
	  
	  public Stream<Files> getAllFilesByUserId(UserDetailsImpl userDetails){
		  Optional<User> user = userRepository.findById(userDetails.getId());
		  return fileRepository.findByUser(user.get()).stream();
	  }
	  
}
