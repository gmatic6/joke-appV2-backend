package com.combis.jokeApp.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.Test;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.entity.User;
import com.combis.jokeApp.repository.JokeRepository;
import com.combis.jokeApp.repository.UserRepository;

public class JokeServiceTest {

	private JokeRepository repository = Mockito.mock(JokeRepository.class);
	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private JokeService JokeService;
	private PasswordEncoder passwordEncoder;
	
	@Before
	public void setup() {
		this.JokeService = new JokeService(repository, userRepository, passwordEncoder);
	}
	
	@Test
	public void findAllTest() {
		List<Joke> testlist = new ArrayList<Joke>();
		testlist.add(new Joke("test123", new User("testUser")));
		when(repository.findAll()).thenReturn(testlist);
		List<Joke> result = JokeService.getJokes();
		assertEquals(testlist, result);
	}
	
	@Test
	public void jokeOfTheDayTest() {
		
		List<Joke> testlist = new ArrayList<Joke>();
		testlist.add(new Joke("test1", new User("testUser")));
		testlist.add(new Joke("test2", new User("testUser")));
		testlist.add(new Joke("test3", new User("testUser")));
		testlist.add(new Joke("test4", new User("testUser")));
		testlist.add(new Joke("test5", new User("testUser")));
		testlist.add(new Joke("test6", new User("testUser")));

		for(int id = 0; id < testlist.size(); id++) {
			when(repository.findById(id)).thenReturn(Optional.of(testlist.get(id)));
			testlist.get(id).setId(id);
		}
		when(repository.count()).thenReturn((long) testlist.size());
	
		Joke result = JokeService.jokeOfTheDay();
		Joke result2 = JokeService.jokeOfTheDay();
		
		assertEquals(result.getId(), result2.getId());
	}

	@Test
	public void likeJokeTest() {
		Joke testJoke = new Joke();
		when(repository.findById(0)).thenReturn(Optional.of(testJoke));
		Integer oldLikes = testJoke.getLikes();
		testJoke = JokeService.likeJoke(testJoke.getId());
		Integer newLikes = testJoke.getLikes();
		assertTrue(newLikes > oldLikes);
	}
	
	@Test
	public void dislikeJokeTest() {
		Joke testJoke = new Joke();
		when(repository.findById(0)).thenReturn(Optional.of(testJoke));
		Integer oldDislikes = testJoke.getDislikes();
		testJoke = JokeService.dislikeJoke(testJoke.getId());
		Integer newDislikes = testJoke.getDislikes();
		assertTrue(newDislikes > oldDislikes);
	}
}