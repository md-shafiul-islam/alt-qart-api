package com.altqart.services.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.StatusMapper;
import com.altqart.model.Status;
import com.altqart.repository.StatusRepository;
import com.altqart.resp.model.RespStatus;
import com.altqart.services.StatusServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusServicesImpl implements StatusServices {

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private StatusMapper statusMapper;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public Status getStatusById(int id) {

		Optional<Status> dbStatus = statusRepository.findById(id);

		if (dbStatus.isPresent() && !dbStatus.isEmpty()) {
			return dbStatus.get();
		}

		return null;
	}

	@Override
	public List<Status> getAllStatus() {
		return (List<Status>) statusRepository.findAll();
	}

	@Override
	public List<RespStatus> getAllRespStatus() {
		List<RespStatus> respStatuses = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Status> criteriaQuery = (CriteriaQuery<Status>) criteriaBuilder.createQuery(Status.class);

			Root<Status> root = (Root<Status>) criteriaQuery.from(Status.class);

			criteriaQuery.select(root);

			Query<Status> query = session.createQuery(criteriaQuery);

			List<Status> statuses = query.getResultList();

			if (statuses != null) {

				respStatuses = statusMapper.mapAllRespStatusOnly(statuses);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllRespStatus " + e.getMessage());

		}
		return respStatuses;
	}

	@Override
	public RespStatus getRespStatusById(int id) {
		RespStatus respStatus = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Status> criteriaQuery = (CriteriaQuery<Status>) criteriaBuilder.createQuery(Status.class);

			Root<Status> root = (Root<Status>) criteriaQuery.from(Status.class);

			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));

			Query<Status> query = session.createQuery(criteriaQuery);

			Status status = query.getSingleResult();

			if (status != null) {

				respStatus = statusMapper.mapRespStatus(status);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getRespStatusById " + e.getMessage());

		}
		return respStatus;
	}

}
