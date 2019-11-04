package com.combis.jokeApp.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CascadeType;

import org.hibernate.annotations.Cascade;

@Entity
public class Joke{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	private String jokeText;
	@NotNull
	private int likes;
	@NotNull
	private int dislikes;
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade( value = { CascadeType.MERGE } )
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "favourites")
	@Cascade( value = { CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST } )
	private Set<User> favouritedBy;
	
	public Joke() {}
	
	public Joke(String jokeText, User user){
		this.jokeText = jokeText;
		this.user = user;
		likes = 0;
		dislikes = 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJokeText() {
		return jokeText;
	}
	public void setJokeText(String jokeText) {
		this.jokeText = jokeText;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<User> getFavouritedBy() {
		return favouritedBy;
	}
	public void setFavouritedBy(Set<User> favouritedBy) {
		this.favouritedBy = favouritedBy;
	}
}
