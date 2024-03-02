package com.altqart.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.mapper.ReceiveMapper;
import com.altqart.mapper.StoreMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Receive;
import com.altqart.repository.ReceiveRepository;
import com.altqart.resp.model.RespReceive;
import com.altqart.services.ReceiveServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceiveServicesImpl implements ReceiveServices {

	@Autowired
	private ReceiveRepository receiveRepository;

	@Autowired
	private ReceiveMapper receiveMapper;

	private SessionFactory sessionFactory;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public Receive getReceiveById(String id) {

		Optional<Receive> optional = receiveRepository.getReceiveByPublicId(id);

		if (!optional.isEmpty() && optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<RespReceive> getAllApprove(int start, int size) {
		return getAllReceiveByStatus(1, start, size);
	}

	@Override
	public List<RespReceive> getAllPending(int start, int size) {
		return getAllReceiveByStatus(0, start, size);
	}

	@Override
	public List<RespReceive> getAllRejected(int start, int size) {
		return getAllReceiveByStatus(2, start, size);
	}

	@Override
	public RespReceive getReceiveByPublicId(String id) {

		RespReceive respReceive = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Receive> criteriaQuery = (CriteriaQuery<Receive>) criteriaBuilder.createQuery(Receive.class);

			Root<Receive> root = (Root<Receive>) criteriaQuery.from(Receive.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<Receive> query = session.createQuery(criteriaQuery);

			Receive receive = query.getSingleResult();

			if (receive != null) {
				respReceive = receiveMapper.mapRespReceive(receive);
			}

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();
		return respReceive;
	}

	@Override
	public void getReceiveInvoiceByPublicId(String id, Map<String, Object> map) {

		boolean status = false;

		Map<String, Object> response = new HashMap<>();

		RespReceive respReceive = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Receive> criteriaQuery = (CriteriaQuery<Receive>) criteriaBuilder.createQuery(Receive.class);

			Root<Receive> root = (Root<Receive>) criteriaQuery.from(Receive.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<Receive> query = session.createQuery(criteriaQuery);

			Receive receive = query.getSingleResult();

			if (receive != null) {
				respReceive = receiveMapper.mapRespReceive(receive);

				response.put("receive", respReceive);

				if (receive.getUser() != null) {
					response.put("user", userMapper.mapRespEsUser(authenticationServices.getCurrentUser()));

					if (receive.getUser().getStore() != null) {

						response.put("store", storeMapper.mapRespStore(receive.getUser().getStore()));
						
					}
				}

			} else {
				throw new Exception("Receive cash not found by id. Please, contact administrator");
			}

			session.clear();
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
			map.put("message", e.getMessage());
		}

		session.close();
		if (status) {
			map.put("status", true);
			map.put("response", response);
			map.put("message", "Receive found");
		}

	}

	private List<RespReceive> getAllReceiveByStatus(int status, int start, int size) {

		List<RespReceive> respReceives = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Receive> criteriaQuery = (CriteriaQuery<Receive>) criteriaBuilder.createQuery(Receive.class);

			Root<Receive> root = (Root<Receive>) criteriaQuery.from(Receive.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("approve"), status));
			TypedQuery<Receive> typedQuery = session.createQuery(criteriaQuery);

			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			List<Receive> receives = typedQuery.getResultList();

			if (receives != null) {
				respReceives = receiveMapper.mapAllRespReceive(receives);
			}

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();
		return respReceives;
	}
}
