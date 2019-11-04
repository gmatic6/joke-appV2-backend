package com.combis.jokeApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.combis.jokeApp.entity.User;

@Repository
@CrossOrigin(origins = "http://localhost:4200")
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("SELECT user FROM User user WHERE user.username = ?1")
	User findbyUsername(String username);
	
}
