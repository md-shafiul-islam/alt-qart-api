package com.altqart.services.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.EsCreditMapper;
import com.altqart.model.EsCreditDetail;
import com.altqart.resp.model.RespEsCreditDetail;
import com.altqart.services.CreditServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreditServicesImpl implements CreditServices {

	@Autowired
	private EsCreditMapper creditMapper;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespEsCreditDetail> getAllRespCreditDetails() {

		List<RespEsCreditDetail> respCredits = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<EsCreditDetail> criteriaQuery = (CriteriaQuery<EsCreditDetail>) criteriaBuilder
					.createQuery(EsCreditDetail.class);

			Root<EsCreditDetail> root = (Root<EsCreditDetail>) criteriaQuery.from(EsCreditDetail.class);

			criteriaQuery.select(root);

			Query<EsCreditDetail> query = session.createQuery(criteriaQuery);

			List<EsCreditDetail> creditsDetails = query.getResultList();

			respCredits = creditMapper.mapAllCreditDetals(creditsDetails);

		} catch (Exception e) {

			e.printStackTrace();
			log.info("getAllRespCreditDetails "+e.getMessage());


		}

		session.clear();
		session.close();

		return respCredits;

	}
}
