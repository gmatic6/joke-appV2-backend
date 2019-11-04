package com.combis.jokeApp.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.combis.jokeApp.entity.Joke;

@Repository
@CrossOrigin(origins = "http://localhost:4200")
public interface JokeRepository extends JpaRepository<Joke, Integer>{
	//jokes by user id
	@Query("SELECT joke FROM Joke joke WHERE joke.user.id = ?1")
	List<Joke> getJokesByUser(Integer id);
	//like ratio
	@Query("SELECT joke FROM Joke joke WHERE joke.likes > joke.dislikes")
	List<Joke> getGoodJokes();
	@Query("SELECT joke FROM Joke joke WHERE joke.likes < joke.dislikes")
	List<Joke> getBadJokes();
	//top jokes
	@Query("SELECT joke FROM Joke joke ORDER BY (joke.likes-joke.dislikes) DESC")
	List<Joke> getTop(Pageable pageable);
	@Query("SELECT joke FROM Joke joke ORDER BY random()")
	List<Joke> getRandom(Pageable pageable);
}
