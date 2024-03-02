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
import com.altqart.mapper.PaidMapper;
import com.altqart.mapper.StoreMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Paid;
import com.altqart.repository.PayedRepository;
import com.altqart.resp.model.RespPaid;
import com.altqart.resp.model.RespStore;
import com.altqart.services.PayedServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaidServicesImpl implements PayedServices {

	@Autowired
	private PayedRepository payedRepository;

	private SessionFactory sessionFactory;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private PaidMapper paidMapper;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<Paid> getAllPaid() {

		List<Paid> paids = (List<Paid>) payedRepository.findAll();

		return paids;
	}

	@Override
	public List<RespPaid> getAllApprovePayed(int start, int size) {

		size = size > 0 ? size : 15;

		return getAllPaidByStatus(1, start, size);

	}

	@Override
	public List<RespPaid> getAllPendingPayed(int start, int size) {
		size = size > 0 ? size : 15;

		return getAllPaidByStatus(0, start, size);
	}

	@Override
	public List<RespPaid> getAllRejectedPayed(int start, int size) {
		size = size > 0 ? size : 15;

		return getAllPaidByStatus(2, start, size);
	}

	@Override
	public Paid getPaidById(String id) {

		Optional<Paid> optional = payedRepository.getPaidByPublicId(id);

		if (!optional.isEmpty() && optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public RespPaid getPaidByPublicId(String id) {
		RespPaid respPaid = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Paid> criteriaQuery = (CriteriaQuery<Paid>) criteriaBuilder.createQuery(Paid.class);

			Root<Paid> root = (Root<Paid>) criteriaQuery.from(Paid.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<Paid> typedQuery = session.createQuery(criteriaQuery);

			Paid paid = typedQuery.getSingleResult();

			if (paid != null) {
				respPaid = paidMapper.mapRespPaid(paid);
			}

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();
		return respPaid;
	}

	@Override
	public void getPaidInvoiceByPublicId(String id, Map<String, Object> map) {

		Map<String, Object> response = new HashMap<>();

		boolean status = false;

		RespPaid respPaid = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Paid> criteriaQuery = (CriteriaQuery<Paid>) criteriaBuilder.createQuery(Paid.class);

			Root<Paid> root = (Root<Paid>) criteriaQuery.from(Paid.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<Paid> typedQuery = session.createQuery(criteriaQuery);

			Paid paid = typedQuery.getSingleResult();

			if (paid != null) {
				respPaid = paidMapper.mapRespPaid(paid);
				if (paid.getUser() != null) {

					RespStore respStore = storeMapper.mapRespStoreOnly(paid.getUser().getStore());
					response.put("store", respStore);
					response.put("user", userMapper.mapRespUserOnly(authenticationServices.getCurrentUser()));

				}

				response.put("paid", respPaid);
			} else {
				throw new Exception("Paid not found by ID. Please, contact administrator");
			}

			session.clear();
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", e.getMessage());
			status = false;
		}

		session.close();

		if (status) {
			map.put("status", true);
			map.put("response", response);
			map.put("message", "Payable found");
		}

	}

	private List<RespPaid> getAllPaidByStatus(int status, int start, int size) {

		List<RespPaid> respPaids = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Paid> criteriaQuery = (CriteriaQuery<Paid>) criteriaBuilder.createQuery(Paid.class);

			Root<Paid> root = (Root<Paid>) criteriaQuery.from(Paid.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("approve"), status));
			TypedQuery<Paid> typedQuery = session.createQuery(criteriaQuery);

			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			List<Paid> paids = typedQuery.getResultList();

			if (paids != null) {
				log.info("Paid Found ", paids.size());
				respPaids = paidMapper.mapAllRespPaid(paids);
			}

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();
		return respPaids;
	}

}
