package com.altqart.services.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.RoleMapper;
import com.altqart.model.Role;
import com.altqart.repository.RoleRepository;
import com.altqart.req.model.RoleReq;
import com.altqart.resp.model.RespRole;
import com.altqart.services.RoleServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServicesImpl implements RoleServices {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleMapper roleMapper;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespRole> getAllRespAccessRole() {
		List<Role> roles = (List<Role>) roleRepository.findAll();
		return roleMapper.mapAllRespRoleOnly(roles);
	}

	@Override
	public void addAccessRole(RoleReq roleReq, Map<String, Object> map) {

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();
			Role role = roleMapper.mapRole(roleReq);

			session.persist(role);

			transaction.commit();
			session.clear();

			map.put("message", "Role Added successfuly");
			map.put("status", true);
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			map.put("message", e.getMessage());
		}

		session.close();

	}

	@Override
	public RespRole getRespAccessRoleById(String id) {

		RespRole respRole = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Role> criteriaQuery = (CriteriaQuery<Role>) criteriaBuilder.createQuery(Role.class);

			Root<Role> root = (Root<Role>) criteriaQuery.from(Role.class);

			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<Role> query = session.createQuery(criteriaQuery);

			Role role = query.getSingleResult();

			if (role != null) {
				respRole = roleMapper.mapRoleDetails(role);
			}

			session.clear();
		} catch (Exception e) {

			log.info("getRespAccessRoleById " + e.getMessage());

			e.printStackTrace();
		}

		session.close();

		return respRole;

	}

}
