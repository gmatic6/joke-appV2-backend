package com.combis.jokeApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.entity.User;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringRunner.class)
@WebMvcTest(JokeController.class)
public class JokeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JokeController jokeController;
	
	@Test
	public void getJokes() throws Exception {
		Joke joke = new Joke("test", new User("user"));
		Joke joke2 = new Joke("test2", new User("user"));
		List<Joke> Jokes = new ArrayList<Joke>();
		Jokes.add(joke);
		Jokes.add(joke2);
		given(jokeController.getJokes()).willReturn(Jokes);
		
		mvc.perform(get("/jokes/")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].jokeText", is(joke.getJokeText())))
				.andExpect(jsonPath("$[1].jokeText", is(joke2.getJokeText())));													
	}
	
	@Test
	public void getJoke() throws Exception {
		Joke joke = new Joke();
		joke.setJokeText("test");
		
		given(jokeController.getJoke(joke.getId())).willReturn(joke);
		
		mvc.perform(get("/jokes/{id}", joke.getId())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("jokeText", is(joke.getJokeText())));
	}
	
	@Test 
	public void randomJoke() throws Exception {
		Joke joke = new Joke();
		
		given(jokeController.getRandomJoke()).willReturn(joke);
		
		mvc.perform(get("/jokes")
				.contentType(APPLICATION_JSON)
				.param("random", ""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(5)));
	}
}