package com.altqart.services.impl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.services.SessionServices;

import jakarta.persistence.EntityManagerFactory;

@Service
public class SessionServicesImpl implements SessionServices{

	
	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {
		
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		
		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}
	
	@Override
	public SessionFactory getHBSessionFactory() {
		if (sessionFactory != null) {
			return sessionFactory;
		}
		
		return null;
	}
}
