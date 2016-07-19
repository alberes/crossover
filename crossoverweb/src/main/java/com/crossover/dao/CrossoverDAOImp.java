package com.crossover.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.crossover.entity.Artifact;
import com.crossover.entity.User;

public class CrossoverDAOImp implements CrossoverDAO {

	private EntityManager em;
	
	public CrossoverDAOImp(EntityManager em){
		this.em = em;
	}
	
	public Artifact save(Artifact artifact) {
		em.getTransaction().begin();
		em.persist(artifact);
		em.getTransaction().commit();
		return artifact;
	}

	public Artifact find(Long id) {
		Artifact a = em.find(Artifact.class, id);
		return a;
	}

	public List<Artifact> findAll() {
		List<Artifact> artifactList = em.createQuery(
                "SELECT a FROM Artifact a ORDER BY a.id DESC", Artifact.class).getResultList();
		return artifactList;
	}

	public User saveUser(User user) {
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		return user;
	}
	
	public User findUser(String name, String password) {
		TypedQuery<User> query = em.createQuery(
		        "SELECT u FROM User u WHERE u.name = :name AND u.password = :password", User.class);
		    return query.setParameter("name", name).setParameter("password", password).getSingleResult();
	}
	
	public int deleteAllUsers(){
		int deletedCount = em.createQuery("DELETE FROM User").executeUpdate();
		return deletedCount;
	}
	
	public List<User> findAllUsers() {
		List<User> userList = em.createQuery(
                "SELECT u FROM User u ORDER BY u.id DESC", User.class).getResultList();
		return userList;
	}

	
}
