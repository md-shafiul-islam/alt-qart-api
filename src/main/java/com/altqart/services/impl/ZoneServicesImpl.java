package com.altqart.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.ZoneMapper;
import com.altqart.model.Zone;
import com.altqart.repository.ZoneRepository;
import com.altqart.req.model.ZoneReq;
import com.altqart.req.model.ZonesCityReq;
import com.altqart.req.model.ZonesReq;
import com.altqart.resp.model.RespLocOption;
import com.altqart.resp.model.RespZone;
import com.altqart.services.ZoneServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZoneServicesImpl implements ZoneServices {

	@Autowired
	private ZoneMapper zoneMapper;

	@Autowired
	private ZoneRepository zoneRepository;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public void getAll(Map<String, Object> map, int start, int size) {

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Zone> criteriaQuery = criteriaBuilder.createQuery(Zone.class);

			Root<Zone> root = criteriaQuery.from(Zone.class);

			TypedQuery<Zone> typedQuery = session.createQuery(criteriaQuery);
			List<Zone> zones = typedQuery.getResultList();

			if (size > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}
			map.put("response", zoneMapper.mapAllRespZoneOnly(zones));
			map.put("message", zones.size() + " Zone(s) found");
			map.put("status", true);

		} catch (Exception e) {
			log.info("getThisMonthOrAnyOrderByStatus " + e.getMessage());
//			e.printStackTrace();

			map.put("message", e.getMessage());
			map.put("status", false);
		}
		session.clear();
		session.close();

	}

	@Override
	public Zone getZoneByKey(String zone) {

		Optional<Zone> optional = zoneRepository.getZoneByValue(zone);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void getOne(int id, Map<String, Object> map) {

		Session session = sessionFactory.openSession();
		try {

			Zone dbZone = session.get(Zone.class, id);

			if (dbZone != null) {
				RespZone respZone = zoneMapper.mapRespZoneOnly(dbZone);
				map.put("response", respZone);
			}

			session.clear();
			map.put("status", true);
			map.put("message", "Zone Found By Id");
		} catch (Exception e) {

			e.printStackTrace();
			map.put("status", false);
			map.put("message", e.getMessage());
		}

	}

	@Override
	public void getAllByCity(int id, Map<String, Object> map) {

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Zone> criteriaQuery = criteriaBuilder.createQuery(Zone.class);

			Root<Zone> root = criteriaQuery.from(Zone.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get("city").get("id"), id));

			Query<Zone> query = session.createQuery(criteriaQuery);

			List<RespLocOption> zones = zoneMapper.mapAllRespZoneOption(query.getResultList());
			map.put("response", zones);
			map.put("message", zones.size() + " Zone(s) found");
			map.put("status", true);

		} catch (Exception e) {
			log.info("getThisMonthOrAnyOrderByStatus " + e.getMessage());
//			e.printStackTrace();

			map.put("message", e.getMessage());
			map.put("status", false);
		}
		session.clear();
		session.close();

	}

	@Override
	public void add(ZoneReq zoneReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(ZoneReq zoneReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ZoneReq zoneReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAll(ZonesReq zonesReq, Map<String, Object> map) {

		List<Zone> zones = zoneMapper.mapAllZone(zonesReq);

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			for (Zone zone : zones) {

				session.persist(zone);

			}

			transaction.commit();
			session.clear();
			map.put("status", true);
			map.put("message", zones.size() + " Zone Added Successfully");
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			map.put("status", false);
			map.put("message", e.getMessage());
		}

		session.close();

	}

	@Override
	public Zone getZoneById(int zone) {

		if (zone > 0) {
			Optional<Zone> optional = zoneRepository.findById(zone);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}

		return null;
	}

	@Override
	public Zone getZoneByPathaoId(int pathaoCode) {

		if (pathaoCode > 0) {
			Optional<Zone> optional = zoneRepository.findByPathaoCode(pathaoCode);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}

		return null;
	}

	@Override
	public void addCityZoneAll(ZonesCityReq zonesReq, Map<String, Object> map) {
		List<Zone> zones = zoneMapper.mapZoneAll(zonesReq);

		if (zones != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				for (Zone zone : zones) {
					
					if(zone.getCity() == null) {
						throw new Exception("Zone City not found");
					}
					session.persist(zone);
				}

				transaction.commit();
				session.clear();

				map.put("message", zones.size() + " Zone(s) Added :)");
				map.put("status", true);
			} catch (Exception e) {

				e.printStackTrace();
				map.put("message", "All Zone(s) Added Failed");
				map.put("status", false);
			}

			session.close();
		}

	}

}
