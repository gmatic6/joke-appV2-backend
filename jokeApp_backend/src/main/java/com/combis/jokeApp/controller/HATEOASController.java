package com.combis.jokeApp.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.services.JokeService;

@RestController
public class HATEOASController {

	private JokeService service;
	@Autowired
	public HATEOASController(JokeService service) {
		this.service = service;
	}
	
	@GetMapping("/HATEOAS/jokes")
	public Resources<Resource<Joke>> getJokes() {
		List<Resource<Joke>> jokes = service.getJokes().stream()
				.map(joke -> new Resource<>(joke,
				linkTo(methodOn(HATEOASController.class).getJoke(joke.getId())).withSelfRel(),
				linkTo(methodOn(HATEOASController.class).getJokes()).withRel("jokes")))
				.collect(Collectors.toList());
		
		return new Resources<>(jokes,
				linkTo(methodOn(HATEOASController.class).getJokes()).withSelfRel());
	}
	
	@GetMapping("/HATEOAS/jokes/{id}")
	public Resource<Joke> getJoke(@PathVariable Integer id) {
		Joke joke = service.getJoke(id);
		return new Resource<>(joke,
				linkTo(methodOn(HATEOASController.class).getJoke(id)).withSelfRel(),
				linkTo(methodOn(HATEOASController.class).getJokes()).withRel("jokes"));
	}
	
	@PostMapping("/HATEOAS/jokes")
	public ResponseEntity<?> newJoke(@RequestBody Joke newJoke) throws URISyntaxException {
			Resource<Joke> resource = new Resource<>(newJoke,
					linkTo(methodOn(HATEOASController.class).getJoke(newJoke.getId())).withSelfRel(),
					linkTo(methodOn(HATEOASController.class).getJokes()).withRel("jokes"));
			return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}
}
