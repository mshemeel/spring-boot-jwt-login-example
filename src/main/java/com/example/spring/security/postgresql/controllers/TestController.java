package com.example.spring.security.postgresql.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.security.postgresql.payload.response.UserDeailsResponse;
import com.example.spring.security.postgresql.security.services.UserDetailsServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@Autowired
	private UserDetailsServiceImpl userService;
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
	@GetMapping("users/fetchAll")
	@PreAuthorize("hasRole('ADMIN')")
	 @ApiOperation(value = "${AuthController.me}", response = UserDeailsResponse.class, authorizations = { @Authorization(value="apiKey") })
	  @ApiResponses(value = {//
	      @ApiResponse(code = 400, message = "Something went wrong"), //
	      @ApiResponse(code = 403, message = "Access denied"), //
	      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
	public ResponseEntity<List<UserDeailsResponse>> fetchAllUsers(){ 
		List<UserDeailsResponse> users = userService.getAllUsers().map(user -> { 
			List<String> roles = user.getRoles().stream().map(item -> item.getName().name())
					.collect(Collectors.toList());
			return new UserDeailsResponse(user.getId(), user.getUsername(), user.getEmail(), (List<String>) roles);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
}
