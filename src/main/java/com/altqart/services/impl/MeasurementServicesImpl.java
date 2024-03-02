package com.altqart.services.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.MeasurementMapper;
import com.altqart.model.Measurement;
import com.altqart.repository.MeasurementRepository;
import com.altqart.req.model.MeasurementReq;
import com.altqart.resp.model.RespMeasurement;
import com.altqart.services.MeasurementServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeasurementServicesImpl implements MeasurementServices {

	@Autowired
	private MeasurementRepository measurementRepository;

	@Autowired
	private MeasurementMapper measurementMapper;

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
			CriteriaQuery<Measurement> criteriaQuery = (CriteriaQuery<Measurement>) criteriaBuilder
					.createQuery(Measurement.class);

			Root<Measurement> root = (Root<Measurement>) criteriaQuery.from(Measurement.class);

			criteriaQuery.select(root);

			Query<Measurement> query = session.createQuery(criteriaQuery);

			List<RespMeasurement> measurements = measurementMapper.mapAllRespMeasurement(query.getResultList());

			map.put("response", measurements);
			map.put("status", true);
			map.put("message", measurements.size() + " Measurements found");
			session.clear();
		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public void add(MeasurementReq measurementReq, Map<String, Object> map) {

		Measurement measurement = measurementMapper.mapMeasurement(measurementReq);
		measurementRepository.save(measurement);
		if (measurement != null) {

			if (measurement.getId() > 0) {
				map.put("status", true);
				map.put("message", "Material Saved");
			}

		}

	}

	@Override
	public void getOneById(int id, Map<String, Object> map) {

		if (id > 0) {

			Session session = sessionFactory.openSession();

			try {

				RespMeasurement respMaterial = measurementMapper.mapRespMeasurement(session.get(Measurement.class, id));

				map.put("response", respMaterial);
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
	public void remove(MeasurementReq measurementReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MeasurementReq measurementReq, Map<String, Object> map) {

		if (measurementReq != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				Measurement dbMeasurement = session.get(Measurement.class, measurementReq.getId());

				if (dbMeasurement == null) {
					throw new Exception("Measurement Not found, Update failed");
				}

				if (measurementReq.getHeight() > 0) {
					dbMeasurement.setHeight(measurementReq.getHeight());
				}

				if (measurementReq.getLenght() > 0) {
					dbMeasurement.setLenght(measurementReq.getLenght());
				}

				if (measurementReq.getWeight() > 0) {
					dbMeasurement.setWeight(measurementReq.getWeight());
				}

				if (measurementReq.getWidth() > 0) {
					dbMeasurement.setWidth(measurementReq.getWidth());
				}

				session.merge(dbMeasurement);

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

}
