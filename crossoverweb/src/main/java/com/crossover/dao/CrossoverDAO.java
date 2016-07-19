package com.crossover.dao;

import java.util.List;

import com.crossover.entity.Artifact;
import com.crossover.entity.User;

public interface CrossoverDAO {

	public Artifact save(Artifact artifact);
	
	public Artifact find(Long id);
	
	public List<Artifact> findAll();
	
	public User saveUser(User user);
	
	public User findUser(String name, String password);
	
	public int deleteAllUsers();
	
	public List<User> findAllUsers();
	
}
