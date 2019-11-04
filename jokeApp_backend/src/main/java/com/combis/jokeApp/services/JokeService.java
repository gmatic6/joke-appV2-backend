package com.combis.jokeApp.services;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.combis.jokeApp.entity.Joke;
import com.combis.jokeApp.entity.User;
//import com.combis.jokeApp.logger.HealthLogger;
//import com.combis.jokeApp.logger.PerformanceLogger;
import com.combis.jokeApp.repository.JokeRepository;
import com.combis.jokeApp.repository.UserRepository;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j(topic = "com.combis.jokeApp.services")
@Service
@CrossOrigin(origins = "http://localhost:4200")
public class JokeService {
	/*
	Logger logger = LoggerFactory.getLogger(JokeService.class);
	Logger logger2 = LoggerFactory.getLogger("com.combis.jokeApp.services.JokeService2");
	Logger logger3 = LoggerFactory.getLogger("com.combis.jokeApp.services.JokeService3");
	*/
	private JokeRepository repository;
	private UserRepository userRepository;
	private int date;
	private int jotd;
	private boolean jotdUsed;
	private Calendar cal = Calendar.getInstance();
	@SuppressWarnings("unused")
	private PasswordEncoder passwordEncoder;
	//@Autowired
	//private PerformanceLogger logger;
	//@Autowired
	//private HealthLogger logger2;
	@Autowired
	JokeService(JokeRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder){
		
		this.repository = repository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
										//month starts at zero!
		date = cal.get(Calendar.YEAR) * cal.get(Calendar.MONTH) * cal.get(Calendar.DATE);
		jotd = (int) (date % (repository.count() - 1) + 1);
		jotdUsed = false;
		List<User> preloadedUsers = userRepository.findAll();
		for(User user : preloadedUsers) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
		}
	}
	
	public List<Joke> getJokes(){
		/*
		logger.trace("Trace Message!");
		logger.debug("Debug Message!");
		logger.info("Info Message!");
		logger.warn("Warn Message!");
		logger.error("Error Message!");
		logger2.trace("Trace Message!");
		logger2.debug("Debug Message!");
		logger2.info("Info Message!");
		logger2.warn("Warn Message!");
		logger2.error("Error Message!");
		*/
		//log.info("test");
		//log.error("test");
		return repository.findAll();
	}
	
	public Joke getJoke(Integer id) {
		if (repository.findById(id).isPresent()) {
			return repository.findById(id).get();
		} else {
			return null;
		}
	}
	
	public Joke saveJoke(Joke newJoke) {
		repository.save(newJoke);
		return newJoke;
	}
	
	public void deleteJoke(Integer id) {
		jotdUsed = false;
		Joke joke = repository.findById(id).get();
		for(User user : joke.getFavouritedBy()) {
			user.getFavourites().remove(joke);
		}
		repository.delete(repository.findById(id).get());
	}
	
	public Joke likeJoke(Integer id) {
		Joke joke = getJoke(id);
		joke.setLikes(joke.getLikes() + 1);
		repository.save(joke);
		return joke;
	}
	
	public Joke dislikeJoke(Integer id) {
		Joke joke = getJoke(id);
		joke.setDislikes(joke.getDislikes() + 1);
		repository.save(joke);
		return joke;
	}
	
	public List<Joke> getTop(Integer top){
		return repository.getTop(PageRequest.of(0, top));
	}
	
	public Joke jokeOfTheDay() {
		//int i = (int) new Date().getDate(); //deprecated
											//month starts at zero!
		int dateNew = cal.get(Calendar.YEAR) * cal.get(Calendar.MONTH) * cal.get(Calendar.DATE);
		if(getJoke(jotd) == null) {
			jotdUsed = false;
		}
		if(jotdUsed == true && date == dateNew) {
			return getJoke(jotd);
		} else {
			jotd = (int) (dateNew % repository.count()) + 1;
			jotdUsed = true;
			if (!repository.findById(jotd).isPresent()) {
				jotd = getRandomJoke().getId();
			}
			return getJoke(jotd);
		}
	}
	
	public Joke getRandomJoke() {
		Joke random = repository.getRandom(PageRequest.of(0, 1)).get(0);
		return random;
	}
	
	public List<Joke> getGoodJokes(){
		return repository.getGoodJokes();
	}
	
	public List<Joke> getBadJokes(){
		return repository.getBadJokes();
	}
	
	public Joke editJoke(Integer id, Joke newJoke) {
		Joke joke = repository.findById(id).get();
		joke.setJokeText(newJoke.getJokeText());
		repository.save(joke);
		return joke;
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public User getUser(Integer id) {
		return userRepository.findById(id).get();
	}
	
	public List<Joke> getJokesByUser(Integer id){
		return repository.getJokesByUser(id);
	}
	
	public User editUser(Integer id, User newUser) {
		User user = userRepository.findById(id).get();
		user.setUsername(newUser.getUsername());
		user.setRole(newUser.getRole());
		userRepository.save(user);
		return user;
	}
	
	public User addFavourite(Integer jokeId, Integer userId) {
		User user = userRepository.findById(userId).get();
		user.addFavourite(repository.findById(jokeId).get());
		userRepository.save(user);
		return user;
	}
	
	public User removeFavourite(Integer jokeId, Integer userId) {
		User user = userRepository.findById(userId).get();
		user.removeFavourite(repository.findById(jokeId).get());
		userRepository.save(user);
		return user;
	}
	
	public Set<Joke> getFavourites(Integer id) {
		return userRepository.findById(id).get().getFavourites();
	}
	
	public Set<User> getFavouritedBy(Integer id) {
		return repository.findById(id).get().getFavouritedBy();
	}
	
	public void deleteUser(Integer id) {
		User user = userRepository.findById(id).get();
		user.setFavourites(new HashSet<Joke>());
		userRepository.save(user);
		userRepository.delete(userRepository.findById(id).get());
	}
	public User saveUser(User newUser) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		newUser.setPassword(encoder.encode(newUser.getPassword()));
		newUser.setRole("USER");
		userRepository.save(newUser);
		return newUser;
	}
	
	public User userByUsername(String username) {
		return userRepository.findbyUsername(username);
	}
}
