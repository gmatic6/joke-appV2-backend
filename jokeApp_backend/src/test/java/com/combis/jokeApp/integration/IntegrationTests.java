package com.combis.jokeApp.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.entity.User;
import com.combis.jokeApp.repository.JokeRepository;
import com.combis.jokeApp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTests {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JokeRepository repository;
	
	@Test
	public void editUserThroughAllLayers() throws Exception {
		User user = new User("testUser123");
		Integer countBefore = (int) userRepository.count();
		
		objectMapper = new ObjectMapper();
		
		mvc.perform(put("/users/{id}", 1)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		
		List<User> userList = userRepository.findAll();
		assertThat(userList.get(0).getUsername()).isEqualTo(user.getUsername());
		assertThat(userList.size()).isEqualTo(countBefore);
	}
	
	@Test
	public void addJokeThroughAllLayers() throws Exception {
		Joke joke = new Joke("testJoke123", new User("testUser123"));
		Integer countBefore = (int) repository.count();
		
		objectMapper = new ObjectMapper();
		
		mvc.perform(post("/jokes")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(joke)))
				.andExpect(status().isOk());
		
		List<Joke> jokeList = repository.findAll();
		assertThat(jokeList.get(jokeList.size()-1).getJokeText()).isEqualTo(joke.getJokeText());
		assertThat(jokeList.get(jokeList.size()-1).getUser().getUsername()).isEqualTo(joke.getUser().getUsername());
		assertTrue(jokeList.size() > countBefore);
	}
	
	@Test
	public void editJokeThroughAllLayers() throws Exception {
		Joke joke = new Joke("testJoke123", new User("testUser123"));
		Integer countBefore = (int) repository.count();
		
		objectMapper = new ObjectMapper();
		
		mvc.perform(put("/jokes/{id}", 1)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(joke)))
				.andExpect(status().isOk());
		
		List<Joke> jokeList = repository.findAll();
		assertThat(jokeList.get(0).getJokeText()).isEqualTo(joke.getJokeText());
		assertThat(jokeList.get(0).getUser().getUsername()).isEqualTo(joke.getUser().getUsername());
		assertThat(jokeList.size()).isEqualTo(countBefore);
	}
	
	@Test
	public void favouriteJokeThroughAllLayers() throws Exception {
		
		mvc.perform(put("/jokes/{id}/fav", 1)
				.contentType(APPLICATION_JSON)
				.param("user", "1"))
				.andExpect(status().isOk());
		
		Optional<Joke> test = repository.findById(1);
		Joke joke = test.get();
		
		System.out.println(joke.getFavouritedBy().size());
		assertTrue(joke.getFavouritedBy().size() == 1);
		
	}
	
}
