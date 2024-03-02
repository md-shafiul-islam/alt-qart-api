package com.altqart.services.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.ReturnTypeMapper;
import com.altqart.model.ReturnType;
import com.altqart.repository.ReturnTypeRepository;
import com.altqart.resp.model.RespReturnType;
import com.altqart.services.ReturnTypeServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReturnTypeServicesImpl implements ReturnTypeServices {

	private SessionFactory sessionFactory;

	@Autowired
	private ReturnTypeMapper returnTypeMapper;
	
	@Autowired
	private ReturnTypeRepository returnTypeRepository;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespReturnType> getAllReturnType() {

		List<RespReturnType> returnTypes = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<ReturnType> criteriaQuery = (CriteriaQuery<ReturnType>) criteriaBuilder
					.createQuery(ReturnType.class);
			Root<ReturnType> root = criteriaQuery.from(ReturnType.class);
			criteriaQuery.select(root);
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<ReturnType> query = session.createQuery(criteriaQuery);

			List<ReturnType> retTypes = query.getResultList();

			if (retTypes != null) {
				returnTypes = returnTypeMapper.mapAllRespReturnType(retTypes);
			}

		} catch (Exception e) {
			log.info("Get All getAllReturnType " + e.getMessage());
			e.printStackTrace();

		}
		session.clear();
		session.close();

		return returnTypes;
	}
	
	@Override
	public ReturnType getReturnTypeById(int id) {
		Optional<ReturnType> optional = returnTypeRepository.findById(id);
		
		if(optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

}
