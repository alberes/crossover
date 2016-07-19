package com.crossover.bo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.crossover.dao.CrossoverDAO;
import com.crossover.dao.CrossoverDAOImp;
import com.crossover.entity.Artifact;
import com.crossover.entity.User;

public class CrossoverBO {

	private EntityManagerFactory emf;
	
	private EntityManager em;

	private CrossoverDAO dao;
	
	public CrossoverBO(EntityManagerFactory emf){
		this.emf = emf;
		this.em = this.emf.createEntityManager();
		dao = new CrossoverDAOImp(this.em);
	}
	
	public Artifact saveArtifact(Artifact artifact){
		artifact = dao.save(artifact);
		return artifact;
	}
	
	public Artifact getArtifact(Long id){
		Artifact artifact = dao.find(id);
		return artifact;
	}
	
	public List<Artifact> getAllArtifact(){
		List<Artifact> list = dao.findAll();
		
		return list;
	}

	public User saveUser(User user){
		user = dao.saveUser(user);
		return user;
	}

	public User getUser(String name, String password){
		User user = dao.findUser(name, password);
		return user;
	}

	public int deleteAllUsers(){
		return dao.deleteAllUsers();
	}

	public List<User> getAllUser(){
		List<User> list = dao.findAllUsers();
		
		return list;
	}

	public void close(){
		em.close();		
	}
	
}
