package com.combis.jokeApp.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class JokeRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private JokeRepository repository;
	
	@Test
	public void whenFindAll() {
		Joke firstJoke = new Joke("joke1", new User("user1"));
		Joke secondJoke = new Joke("joke2", new User("user2"));
		entityManager.persist(firstJoke);
		entityManager.persist(secondJoke);
		entityManager.flush();
		entityManager.flush();

		List<Joke> jokes = repository.findAll();

		assertThat(jokes.size()).isEqualTo(8);
		assertThat(jokes.get(6)).isEqualTo(firstJoke);
		assertThat(jokes.get(7)).isEqualTo(secondJoke);
	}
	
	@Test
	public void whenGetOne() {
		Joke testJoke = new Joke("joke", new User("testUser"));
		entityManager.persist(testJoke);
		entityManager.flush();
		
		Joke joke = repository.getOne(testJoke.getId());
		
		assertThat(testJoke.getJokeText()).isEqualTo(joke.getJokeText());
	}
	
	@Test
	public void whenCount() {
		List<Joke> jokes = repository.findAll();
		assertThat(jokes.size()).isEqualTo(repository.count());
	}
	
	@Test
	public void goodJokesQuery() {
		//add bad joke and check if it appears
		//4 good jokes already loaded in database
		List<Joke> testList = repository.getGoodJokes();
		int sizeBefore = testList.size();
		
		Joke firstJoke = new Joke("joke1", new User("user1"));
		
		firstJoke.setDislikes(1);
		
		entityManager.persist(firstJoke);
		entityManager.flush();
		
		testList = repository.getGoodJokes();
		int sizeAfter = testList.size();
		assertThat(testList.size()).isEqualTo(4);
		
		//add good joke and make sure it appears
		firstJoke.setLikes(5);
		
		entityManager.persist(firstJoke);
		entityManager.flush();
		
		testList = repository.getGoodJokes();
		sizeAfter = testList.size();
		assertTrue(sizeBefore < sizeAfter);
		
		
	}
}
