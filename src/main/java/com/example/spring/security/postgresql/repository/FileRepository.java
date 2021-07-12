package com.example.spring.security.postgresql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.security.postgresql.models.User;
import com.example.spring.security.postgresql.models.file.Files;

@Repository
public interface FileRepository extends JpaRepository<Files, String>{

	List<Files> findByUser(User user);
	
}
