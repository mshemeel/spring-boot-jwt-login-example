package com.example.spring.security.postgresql.payload.response;

import java.util.List;

public class UserFileResponse {
	private String userId;
	private List<FileResponse> files;
	
	
	public UserFileResponse() {
		//default constructor
	}
	
	public UserFileResponse(List<FileResponse> files, String userId) {
		super();
		this.files = files;
		this.userId = userId;
	}
	public List<FileResponse> getFiles() {
		return files;
	}
	public void setFiles(List<FileResponse> files) {
		this.files = files;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
