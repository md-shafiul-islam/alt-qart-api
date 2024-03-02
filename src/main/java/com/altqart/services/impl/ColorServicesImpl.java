package com.altqart.services.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.ColorMapper;
import com.altqart.model.Color;
import com.altqart.repository.ColorRepository;
import com.altqart.req.model.ColorReq;
import com.altqart.resp.model.RespColor;
import com.altqart.services.ColorServices;

import jakarta.persistence.EntityManagerFactory;

@Service
public class ColorServicesImpl implements ColorServices {

	@Autowired
	private ColorRepository colorRepository;

	@Autowired
	private ColorMapper colorMapper;

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
	public void add(ColorReq colorReq, Map<String, Object> map) {

		Color color = colorMapper.mapColor(colorReq);
		if (color != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();
				session.persist(color);
				transaction.commit();
				session.clear();
				map.put("status", true);
				map.put("message", "Color save successfully");
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
			}

			session.close();
		}

	}

	@Override
	public void getAllColor(Map<String, Object> map, int start, int size) {

		List<RespColor> respColors = colorMapper.mapAllRespColor((List<Color>) colorRepository.findAll());
		if (respColors != null) {
			map.put("status", true);
			map.put("message", respColors.size() + " Color's found");
			map.put("response", respColors);
		}

	}

	@Override
	public void getOneById(int id, Map<String, Object> map) {

		if (id > 0) {
			Session session = sessionFactory.openSession();

			try {
				Color color = session.get(Color.class, id);

				if (color != null) {
					RespColor respColor = colorMapper.mapRespColor(color);

					map.put("status", true);
					map.put("message", "Color found By Id");
					map.put("response", respColor);
				}
			} catch (Exception e) {

				e.printStackTrace();
				map.put("status", false);
				map.put("message", "Color not found By Id " + e.getMessage());
			}
		}

	}

	@Override
	public void remove(ColorReq colorReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ColorReq colorReq, Map<String, Object> map) {

		if (colorReq != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();
				Color dbColor = session.get(Color.class, colorReq.getId());
				if (helperServices.isNotEqualAndFirstOneIsNotNull(colorReq.getName(), dbColor.getName())) {
					dbColor.setName(colorReq.getName());
				}

				session.merge(dbColor);
				transaction.commit();
				session.clear();
				map.put("status", true);
				map.put("message", "Color Update successfully");
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				map.put("message", "Color Update failed " + e.getMessage());
				map.put("status", false);
			}
			session.close();
		}

	}
}
