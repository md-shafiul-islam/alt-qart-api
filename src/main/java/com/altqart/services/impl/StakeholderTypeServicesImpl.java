package com.altqart.services.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.StakeholderMapper;
import com.altqart.model.StakeholderType;
import com.altqart.repository.StakeholderTypeRepository;
import com.altqart.resp.model.RespStakeholderType;
import com.altqart.services.StakeholderTypeServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class StakeholderTypeServicesImpl implements StakeholderTypeServices {

	@Autowired
	private StakeholderTypeRepository stakeholderTypeRepository;
	
	@Autowired
	private StakeholderMapper stakeholderMapper;
	
	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}
	
	@Override
	public StakeholderType getStakeholderTypeByKey(String key) {

		Optional<StakeholderType> optional = stakeholderTypeRepository.getStakeholderByKey(key);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}
	
	@Override
	public List<RespStakeholderType> getAllStakeholderType() {
		List<RespStakeholderType> respStakeholderTypes = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<StakeholderType> criteriaQuery = (CriteriaQuery<StakeholderType>) criteriaBuilder
					.createQuery(StakeholderType.class);
			Root<StakeholderType> root = (Root<StakeholderType>) criteriaQuery.from(StakeholderType.class);
			criteriaQuery.select(root);

			Query<StakeholderType> query = session.createQuery(criteriaQuery);

			List<StakeholderType> stakeholders = query.getResultList();

			respStakeholderTypes = stakeholderMapper.mapAllRespStakeholderType(stakeholders);

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();

		return respStakeholderTypes;
	}
}
