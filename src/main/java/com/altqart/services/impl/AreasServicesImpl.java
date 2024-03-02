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

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AreaMapper;
import com.altqart.model.Area;
import com.altqart.model.Zone;
import com.altqart.repository.AreaRepsoitory;
import com.altqart.req.model.AreaReq;
import com.altqart.req.model.AreasReq;
import com.altqart.req.model.AreasZReq;
import com.altqart.resp.model.RespArea;
import com.altqart.resp.model.RespLocOption;
import com.altqart.services.AreasServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AreasServicesImpl implements AreasServices {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private AreaMapper areaMapper;

	@Autowired
	private AreaRepsoitory areaRepsoitory;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public void update(AreaReq areaReq, Map<String, Object> map) {

		if (areaReq != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				Area dbArea = session.get(Area.class, areaReq.getId());

				if (dbArea != null) {
					if (helperServices.isNotEqualAndFirstOneIsNotNull(areaReq.getName(), dbArea.getName())) {
						dbArea.setName(areaReq.getName());

					}

					if (areaReq.getPathaoCode() > 0 && areaReq.getPathaoCode() != dbArea.getPathaoCode()) {
						dbArea.setPathaoCode(areaReq.getPathaoCode());
					}

					if (dbArea.getZone() != null && areaReq.getZone() > 0) {
						if (dbArea.getZone().getId() != areaReq.getZone()) {

							Zone dbZone = session.get(Zone.class, areaReq.getZone());

							if (dbZone != null) {
								dbArea.setZone(dbZone);
							}
						}
					}

					session.merge(dbArea);
					transaction.commit();

				}
				session.clear();
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				e.printStackTrace();
			}
			session.close();
		}

	}

	@Override
	public void getAll(Map<String, Object> map, int start, int size) {
		List<RespArea> respAreas = null;
		Session session = sessionFactory.openSession();
		try {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Area> criteriaQuery = criteriaBuilder.createQuery(Area.class);
			Root<Area> root = criteriaQuery.from(Area.class);

			CriteriaQuery<Area> select = criteriaQuery.select(root);

			select.orderBy(criteriaBuilder.asc(root.get("name")));

			Query<Area> query = session.createQuery(select);
			List<Area> areas = query.getResultList();

			if (areas != null) {
				respAreas = areaMapper.mapAllRespArea(areas, map);

				if (respAreas != null) {
					map.put("message", respAreas.size() + " Area(s) found");
				}
			}

			session.clear();
			map.put("status", true);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			map.put("status", false);

		}
		map.put("response", respAreas);
		session.close();

	}

	@Override
	public void add(AreaReq areaReq, Map<String, Object> map) {

		if (areaReq != null) {
			Area area = areaMapper.mapArea(areaReq);

			if (area != null) {
				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					session.persist(area);

					transaction.commit();
					session.clear();
					map.put("message", "Area saved");
					map.put("status", true);
				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					map.put("message", e.getMessage());
					map.put("status", false);
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void remove(AreaReq areaReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAll(AreasReq areasReq, Map<String, Object> map) {

		if (areasReq != null) {

			List<Area> areas = areaMapper.mapAllArea(areasReq);

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				for (Area area : areas) {

					session.persist(area);
				}

				transaction.commit();
				session.clear();

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			}

			session.close();
		}
	}

	@Override
	public void getOne(int id, Map<String, Object> map) {

		RespArea respArea = null;
		Session session = sessionFactory.openSession();
		try {
			Area dbArea = session.get(Area.class, id);

			if (dbArea != null) {
				respArea = areaMapper.mapRespArea(dbArea, map);

				if (respArea != null) {
					map.put("message", " Area found by id");
				}
			}

			session.clear();
			map.put("status", true);
		} catch (Exception e) {
			map.put("message", e.getMessage());
			map.put("status", false);

		}
		map.put("response", respArea);
		session.close();

	}
	
	@Override
	public Area getAreaByKey(String key) {
		Optional<Area> optional = areaRepsoitory.getAreaByValue(key);
		
		if(optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public void getAllAreaByZone(int id, Map<String, Object> map) {
	
		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Area> criteriaQuery = criteriaBuilder.createQuery(Area.class);

			Root<Area> root = criteriaQuery.from(Area.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get("zone").get("id"), id));
			
			Query<Area> query = session.createQuery(criteriaQuery);
			
			List<RespLocOption> areas = areaMapper.mapAllRespOptionArea(query.getResultList());
			map.put("response", areas);
			map.put("message", areas.size() + " Area(s) found");
			map.put("status", true);

		} catch (Exception e) {
			log.info("getAllAreaByZone " + e.getMessage());
//			e.printStackTrace();

			map.put("message", e.getMessage());
			map.put("status", false);
		}
		session.clear();
		session.close();
		
	}

	@Override
	public void addZAll(AreasZReq areasReq, Map<String, Object> map) {

		List<Area> areas = areaMapper.mapAllZArea(areasReq);

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			int count = 1;
			for (Area area : areas) {

				session.persist(area);
				count++;
			}

			transaction.commit();
			session.clear();
			map.put("status", true);
			map.put("message", count + " Areas Added");
		} catch (Exception e) {

			e.printStackTrace();
			map.put("status", false);
			map.put("message", e.getMessage());
		}

		session.close();

	}

}
