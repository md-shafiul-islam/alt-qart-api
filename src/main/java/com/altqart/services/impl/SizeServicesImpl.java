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
import com.altqart.mapper.MaterialMapper;
import com.altqart.mapper.SizeMapper;
import com.altqart.model.Material;
import com.altqart.model.MeasurementStandard;
import com.altqart.model.Size;
import com.altqart.repository.MaterialRepository;
import com.altqart.repository.MeasurementStandardRepository;
import com.altqart.repository.SizeRepository;
import com.altqart.req.model.MaterialReq;
import com.altqart.req.model.MeasurementStandardReq;
import com.altqart.req.model.SizeReq;
import com.altqart.resp.model.RespMaterial;
import com.altqart.resp.model.RespMeasurementStandard;
import com.altqart.resp.model.RespSize;
import com.altqart.services.SizeServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SizeServicesImpl implements SizeServices {

	@Autowired
	private SizeRepository sizeRepository;

	@Autowired
	private MeasurementStandardRepository measurementStandardRepository;

	@Autowired
	private SizeMapper sizeMapper;

	@Autowired
	private HelperServices helperServices;

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
			CriteriaQuery<Size> criteriaQuery = (CriteriaQuery<Size>) criteriaBuilder.createQuery(Size.class);

			Root<Size> root = (Root<Size>) criteriaQuery.from(Size.class);

			criteriaQuery.select(root);

			Query<Size> query = session.createQuery(criteriaQuery);

			List<RespSize> respSizes = sizeMapper.mapAllRespSize(query.getResultList());

			map.put("response", respSizes);
			map.put("status", true);
			map.put("message", respSizes.size() + " Size's found");
			session.clear();
		} catch (Exception e) {

			log.info("Get Size By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public void add(SizeReq sizeReq, Map<String, Object> map) {

		Size size = sizeMapper.mapSize(sizeReq);
		sizeRepository.save(size);
		if (size != null) {

			if (size.getId() > 0) {
				map.put("status", true);
				map.put("message", "Size Saved");
			}

		}

	}

	@Override
	public void getOneById(int id, Map<String, Object> map) {

		if (id > 0) {

			Session session = sessionFactory.openSession();

			try {

				RespSize respSize = sizeMapper.mapRespSize(session.get(Size.class, id));

				map.put("response", respSize);
				map.put("status", true);
			} catch (Exception e) {

				e.printStackTrace();

				map.put("message", e.getMessage());
			}
			session.clear();
			session.close();
		}

	}

	@Override
	public void remove(SizeReq sizeReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SizeReq sizeReq, Map<String, Object> map) {

		if (sizeReq != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				Size dbSize = session.get(Size.class, sizeReq.getId());

				if (dbSize == null) {
					throw new Exception("Size Not found, Update failed");
				}

				if (helperServices.isNotEqualAndFirstOneIsNotNull(sizeReq.getName(), dbSize.getName())) {
					dbSize.setName(sizeReq.getName());
				}

				if (helperServices.isNotEqualAndFirstOneIsNotNull(sizeReq.getValue(), dbSize.getSKey())) {
					dbSize.setSKey(sizeReq.getValue());
				}

				if (sizeReq.getCount() > 0) {
					dbSize.setCount(sizeReq.getCount());
				}

				if (sizeReq.getStandard() > 0) {
					MeasurementStandard standard = session.get(MeasurementStandard.class, sizeReq.getStandard());

					if (standard != null) {
						dbSize.setStandard(standard);
					}
				}

				session.merge(dbSize);
				transaction.commit();
				session.clear();

			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			} finally {
				session.close();
			}
		}

	}

	@Override
	public void addMeasurementStandard(MeasurementStandardReq standard, Map<String, Object> map) {

		if (standard != null) {
			MeasurementStandard measurementStandard = sizeMapper.mapMeasurementStandard(standard);

			if (measurementStandard != null) {
				measurementStandardRepository.save(measurementStandard);

				if (measurementStandard != null) {
					if (measurementStandard.getId() > 0) {
						map.put("status", true);
						map.put("message", "Measurement Standard Saved :)");
					}
				}

			}
		}

	}

	@Override
	public List<RespMeasurementStandard> getAllRespMeasurementStandard() {

		return sizeMapper.mapAllStandard((List<MeasurementStandard>) measurementStandardRepository.findAll());
	}

	@Override
	public RespMeasurementStandard getOneMeasurementStandard(int id) {

		if (id > 0) {
			Optional<MeasurementStandard> optional = measurementStandardRepository.findById(id);
			if (optional.isPresent() && !optional.isEmpty()) {
				return sizeMapper.mapRespStandard(optional.get());
			}
		}

		return null;
	}

	@Override
	public void updateMeasurementStandard(MeasurementStandardReq standard, Map<String, Object> map) {

		if (standard != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				MeasurementStandard dbStandard = session.get(MeasurementStandard.class, standard.getId());
				if (!helperServices.isNotEqualAndFirstOneIsNotNull(standard.getName(), dbStandard.getName())) {
					dbStandard.setName(standard.getName());
				}

				session.merge(dbStandard);

				transaction.commit();

				map.put("status", true);
				map.put("message", "Measurement Standard saved");
			} catch (Exception e) {
				map.put("status", false);
				map.put("message", e.getMessage());
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			} finally {
				session.clear();
				session.close();
			}
		}

	}

}
