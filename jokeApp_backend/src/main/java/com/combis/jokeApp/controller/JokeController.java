package com.combis.jokeApp.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.entity.User;
import com.combis.jokeApp.services.JokeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "com.combis.jokeApp.controller")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class JokeController {
	
	private JokeService service;
	
	@Autowired
	public JokeController(JokeService service) {
		this.service = service;
	}
	@GetMapping("/jokes")
	List<Joke> getJokes(){
		log.info("test");
		log.error("test");
		return service.getJokes();
	}
	@GetMapping("/jokes/{id}")
	Joke getJoke(@PathVariable("id") Integer id) {
		return service.getJoke(id);
	}
	@PostMapping("/jokes")
	Joke addJoke(@RequestBody Joke newJoke) {
		return service.saveJoke(newJoke);
	}
	@DeleteMapping("/jokes/{id}")
	void deleteJoke(@PathVariable Integer id) {
		service.deleteJoke(id);
	}
	@PutMapping("/jokes/{id}/like")
	Joke likeJoke(@PathVariable Integer id) {
		return service.likeJoke(id);
	}
	@PutMapping("/jokes/{id}/dislike")
	Joke dislikeJoke(@PathVariable Integer id) {
		return service.dislikeJoke(id);
	}
	@PutMapping("jokes/{id}")
	Joke editJoke(@PathVariable Integer id, @RequestBody Joke newJoke) {
		return service.editJoke(id, newJoke);
	}
	@GetMapping(value = "/jokes", params = "top")
	List<Joke> getTop5(@RequestParam Integer top){
		return service.getTop(top);
	}
	@GetMapping(value = "/jokes", params = "jotd")
	Joke jokeOfTheDay() {
		return service.jokeOfTheDay();
	}
	@GetMapping(value = "/jokes", params = "random")
	Joke getRandomJoke() {
		return service.getRandomJoke();
	}
	@GetMapping(value = "/jokes", params = "good")
	List<Joke> getGoodJokes() {
		return service.getGoodJokes();
	}
	@GetMapping(value = "/jokes", params = "bad")
	List<Joke> getBadJokes(){
		return service.getBadJokes();
	}
	@GetMapping("/users")
	List<User> getAllUsers(){
		return service.getAllUsers();
	}
	@GetMapping("/users/{id}")
	User getUser(@PathVariable Integer id) {
		return service.getUser(id);
	}
	@GetMapping("/users/{id}/jokes")
	List<Joke> getJokesByUser(@PathVariable Integer id){
		return service.getJokesByUser(id);
	}
	@PutMapping("/users/{id}")
	User editUser(@PathVariable("id") Integer id, @RequestBody User user) {
		return service.editUser(id, user);
	}
	@PutMapping(value = "/jokes/{id}/fav", params = "user")
	User addFavourite(@PathVariable Integer id, @RequestParam Integer user) {
		return service.addFavourite(id, user);
	}
	@PutMapping(value = "/jokes/{id}/unfav", params = "user")
	User removeFavourite(@PathVariable Integer id, @RequestParam Integer user) {
		return service.removeFavourite(id, user);
	}
	@GetMapping("/users/{id}/favs")
	Set<Joke> getFavourites(@PathVariable Integer id) {
		return service.getFavourites(id);
	}
	@GetMapping("/jokes/{id}/favs")
	Set<User> getFavouritedBy(@PathVariable Integer id){
		return service.getFavouritedBy(id);
	}
	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Integer id) {
		service.deleteUser(id);
	}
	@PostMapping("/users")
	User addUser(@RequestBody User user) {
		return service.saveUser(user);
	}
	@GetMapping(value = "/users", params = "username")
	User userByUsername(@RequestParam String username) {
		return service.userByUsername(username);
	}
}
