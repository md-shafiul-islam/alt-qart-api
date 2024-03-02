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
import com.altqart.mapper.MaterialMapper;
import com.altqart.model.Material;
import com.altqart.repository.MaterialRepository;
import com.altqart.req.model.MaterialReq;
import com.altqart.resp.model.RespMaterial;
import com.altqart.services.MaterialServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MaterialServicesImpl implements MaterialServices {

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private MaterialMapper materialMapper;

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
	public void getAllMaterial(Map<String, Object> map, int start, int size) {

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Material> criteriaQuery = (CriteriaQuery<Material>) criteriaBuilder
					.createQuery(Material.class);

			Root<Material> root = (Root<Material>) criteriaQuery.from(Material.class);

			criteriaQuery.select(root);

			Query<Material> query = session.createQuery(criteriaQuery);

			List<RespMaterial> materials = materialMapper.mapAllRespMaterial(query.getResultList());

			map.put("response", materials);
			map.put("status", true);
			map.put("message", materials.size() + " Materials found");
			session.clear();
		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public void add(MaterialReq materialReq, Map<String, Object> map) {

		Material material = materialMapper.mapMaterial(materialReq);
		materialRepository.save(material);
		if (material != null) {

			if (material.getId() > 0) {
				map.put("status", false);
				map.put("message", "Material Saved");
			}

		}

	}

	@Override
	public void getOneById(int id, Map<String, Object> map) {

		if (id > 0) {

			Session session = sessionFactory.openSession();

			try {

				RespMaterial respMaterial = materialMapper.mapRespMaterial(session.get(Material.class, id));

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
	public void remove(MaterialReq materialReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MaterialReq materialReq, Map<String, Object> map) {

		if (materialReq != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				Material dbMaterial = session.get(Material.class, materialReq.getId());
				
				if(dbMaterial == null) {
					throw new Exception("Material Not found, Update failed");
				}

				if (helperServices.isNotEqualAndFirstOneIsNotNull(materialReq.getDescription(),
						dbMaterial.getDescription())) {
					dbMaterial.setDescription(materialReq.getDescription());
				}

				if (helperServices.isNotEqualAndFirstOneIsNotNull(materialReq.getName(), dbMaterial.getName())) {
					dbMaterial.setName(materialReq.getName());
				}

				session.merge(dbMaterial);

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
