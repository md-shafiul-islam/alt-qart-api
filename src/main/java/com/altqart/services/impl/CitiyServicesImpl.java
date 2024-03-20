package com.altqart.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.CityMapper;
import com.altqart.model.City;
import com.altqart.model.Product;
import com.altqart.model.Zone;
import com.altqart.repository.CityRepository;
import com.altqart.req.model.CitiesReq;
import com.altqart.req.model.CityReq;
import com.altqart.resp.model.RespCity;
import com.altqart.services.CitiyServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CitiyServicesImpl implements CitiyServices {

	@Autowired
	private CityMapper cityMapper;

	@Autowired
	private CityRepository cityRepository;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public City getCityByKey(String city) {

		Optional<City> optional = cityRepository.getCityByKey(city);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void getAll(Map<String, Object> map, int start, int size, int type) {

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<City> criteriaQuery = criteriaBuilder.createQuery(City.class);

			Root<City> root = criteriaQuery.from(City.class);

			criteriaQuery.select(root);

			TypedQuery<City> typedQuery = session.createQuery(criteriaQuery);

			if (size > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			List<City> cities = typedQuery.getResultList();

			if (type == 1) {
				map.put("response", cityMapper.mapAllRespCity(cities));

			} else if (type == 2) {
				map.put("response", cityMapper.mapAllOptionCity(cities));
			} else {

				map.put("response", cityMapper.mapAllRespCityOnly(cities));
			}

			map.put("status", true);
			map.put("message", cities.size() + " Cities found");
			session.clear();

		} catch (Exception e) {

			log.info("getAllProducts " + e.getMessage());
			e.printStackTrace();
			map.put("status", false);
			map.put("message", e.getMessage());

		}
		session.close();

	}

	@Override
	public void add(CityReq cityReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(CityReq cityReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(CityReq cityReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAll(CitiesReq citiesReq, Map<String, Object> map) {

		List<City> cities = cityMapper.mapAllCities(citiesReq);

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			for (City city : cities) {

				log.info("city Pathao Code " + city.getPathaoCode());

				session.persist(city);

			}
			transaction.commit();
			session.clear();
			map.put("status", true);
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
		}

		session.close();
	}

	@Override
	public City getCityById(int city) {

		if (city > 0) {
			Optional<City> optional = cityRepository.findById(city);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}

		return null;
	}

	@Override
	public City getCityByPathaoCode(int pathaoCode) {
		if (pathaoCode > 0) {
			Optional<City> optional = cityRepository.findByPathaoCode(pathaoCode);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}
		return null;
	}

	@Override
	public void getOne(int id, Map<String, Object> map) {

		if (id > 0) {

			Session session = sessionFactory.openSession();
			try {
				City dbCity = session.get(City.class, id);
				if (dbCity != null) {
					RespCity city = cityMapper.mapRespCity(dbCity);

					map.put("response", city);

				}

				map.put("status", true);
				map.put("message", "City found by Id");
			} catch (Exception e) {

				map.put("status", false);
				map.put("message", e.getMessage());
			}
		}
	}

}
