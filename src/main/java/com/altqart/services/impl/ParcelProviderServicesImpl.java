package com.altqart.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.ParcelProviderMapper;
import com.altqart.model.Order;
import com.altqart.model.ParcelProvider;
import com.altqart.repository.ParcelProviderRepository;
import com.altqart.req.model.ParcelProviderReq;
import com.altqart.resp.model.RespOrder;
import com.altqart.resp.model.RespParcelProvider;
import com.altqart.services.ParcelProviderServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

@Service
public class ParcelProviderServicesImpl implements ParcelProviderServices {

	@Autowired
	private ParcelProviderRepository parcelProviderRepository;

	@Autowired
	private ParcelProviderMapper parcelProviderMapper;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public ParcelProvider getParcelProviderByKey(String key) {

		Optional<ParcelProvider> optional = parcelProviderRepository.getParcelProviderByKey(key);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public ParcelProvider getParcelProviderByWebHookSecret(String givenKey) {
		Optional<ParcelProvider> optional = parcelProviderRepository.getParcelProviderByWebHookSecret(givenKey);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public boolean save(ParcelProviderReq parcelProviderReq) {

		if (parcelProviderReq != null) {
			ParcelProvider parcelProvider = parcelProviderMapper.mapParcelProvider(parcelProviderReq);

			if (parcelProvider != null) {
				parcelProviderRepository.save(parcelProvider);

				if (parcelProvider.getId() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public RespParcelProvider getParcelProviderById(String id) {

		RespParcelProvider parcelProvider = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<ParcelProvider> criteriaQuery = criteriaBuilder.createQuery(ParcelProvider.class);

			Root<ParcelProvider> root = criteriaQuery.from(ParcelProvider.class);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<ParcelProvider> query = session.createQuery(criteriaQuery);

			ParcelProvider provider = query.getSingleResult();

			if (query != null) {

				parcelProvider = parcelProviderMapper.mapRespParcelProvider(provider);

			}

		} catch (Exception e) {
//			e.printStackTrace();
		}
		session.clear();
		session.close();
		return parcelProvider;

	}

	@Override
	public void getAllProvider(Map<String, Object> map, int start, int size) {

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<ParcelProvider> criteriaQuery = criteriaBuilder.createQuery(ParcelProvider.class);

			Root<ParcelProvider> root = criteriaQuery.from(ParcelProvider.class);

			Query<ParcelProvider> query = session.createQuery(criteriaQuery);

			List<ParcelProvider> providers = query.getResultList();

			if (query != null) {

				List<ParcelProvider> parcelProviders = parcelProviderMapper.mapAllRespParcelProvider(providers);

				map.put("response", parcelProviders);
				map.put("message", parcelProviders.size() + " Provider found");
				map.put("status", true);
			}

		} catch (Exception e) {
//			e.printStackTrace();

			map.put("status", false);
		}
		session.clear();
		session.close();

	}

}
