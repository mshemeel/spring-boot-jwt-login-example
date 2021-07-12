package com.example.spring.security.postgresql.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.spring.security.postgresql.models.file.Files;
import com.example.spring.security.postgresql.payload.response.FileResponse;
import com.example.spring.security.postgresql.payload.response.MessageResponse;
import com.example.spring.security.postgresql.payload.response.UserDeailsResponse;
import com.example.spring.security.postgresql.payload.response.UserFileResponse;
import com.example.spring.security.postgresql.security.services.UserDetailsImpl;
import com.example.spring.security.postgresql.security.services.UserDetailsServiceImpl;
import com.example.spring.security.postgresql.services.FileStorageService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/files")
public class FilesController {

	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private UserDetailsServiceImpl userService;

	@PostMapping("/upload")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		/* get current user */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String message = "";
		try {
			fileStorageService.store(file, userDetails);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}

	@GetMapping("/files")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<FileResponse>> getListFiles() {
		List<FileResponse> files = fileStorageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getId()).toUriString();

			return new FileResponse(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		Files fileDB = fileStorageService.getFile(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}

	@GetMapping("/files/currentUserFiles")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<UserFileResponse> getListFilesOfCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<FileResponse> files = fileStorageService.getAllFilesByUserId(userDetails).map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getUser().getId() + "/" + dbFile.getId()).toUriString();
			return new FileResponse(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		UserFileResponse resp = new UserFileResponse();
		resp.setFiles(files);
		resp.setUserId(userDetails.getId().toString());

		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	@GetMapping("/files/user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserFileResponse> getListFilesByUserId(@PathVariable String id) {

		Long userID = Long.valueOf(id);
		UserDetailsImpl userIdObj = new UserDetailsImpl(userID, null, null, null, null); 
		List<FileResponse> files = fileStorageService.getAllFilesByUserId(userIdObj).map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/")
					.path(dbFile.getId()).toUriString();
			return new FileResponse(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		UserFileResponse resp = new UserFileResponse();
		resp.setFiles(files);
		resp.setUserId(userIdObj.getId().toString());

		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
}
