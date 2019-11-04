package com.combis.jokeApp.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@Column(unique=true)
	private String username;
	@NotNull
	private String password;
	@NotNull
	private String role;
	@ManyToMany
	@Cascade( value = { CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST } )
	@JoinTable(name = "favouriteJokes",
				joinColumns = @JoinColumn(name = "joke_id"), 
				inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonIgnore
	private Set<Joke> favourites;

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private Collection<Joke> jokes;
	
	public User() {}
	
	public User(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Set<Joke> getFavourites() {
		return favourites;
	}
	public void setFavourites(Set<Joke> favourites) {
		this.favourites = favourites;
	}
	public void addFavourite(Joke joke) {
		favourites.add(joke);
	}
	public void removeFavourite(Joke joke) {
		favourites.remove(joke);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
