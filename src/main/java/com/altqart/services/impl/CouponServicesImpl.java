package com.altqart.services.impl;

import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.CouponMapper;
import com.altqart.model.Coupon;
import com.altqart.model.User;
import com.altqart.repository.CouponRepository;
import com.altqart.req.model.CouponActionReq;
import com.altqart.req.model.CouponReq;
import com.altqart.req.model.RespCoupon;
import com.altqart.services.CouponServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CouponServicesImpl implements CouponServices {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private CouponMapper couponMapper;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public Coupon getCouponByCode(String code) {

		Coupon coupon = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
			Root<Coupon> root = criteriaQuery.from(Coupon.class);

			CriteriaQuery<Coupon> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("code"), code));
			Query<Coupon> query = session.createQuery(select);

			coupon = query.getSingleResult();
			
		} catch (Exception e) {
			log.info("getCouponByCode " + e.getMessage());
			e.printStackTrace();
		}

		session.clear();
		session.close();

		return coupon;
	}

	@Override
	public void getRespCouponByCode(String code, Map<String, Object> map) {

		RespCoupon respCoupon = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
			Root<Coupon> root = criteriaQuery.from(Coupon.class);

			CriteriaQuery<Coupon> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("code"), code));
			Query<Coupon> query = session.createQuery(select);

			Coupon coupon = query.getSingleResult();

			if (coupon != null) {
				respCoupon = couponMapper.mapResCoupon(coupon);
			}

			map.put("message", "Coupon found by code");
			map.put("status", true);

			map.put("response", respCoupon);

		} catch (Exception e) {
			log.info("getCouponByCode " + e.getMessage());
			e.printStackTrace();
			map.put("message", e.getMessage());
			map.put("status", false);
		}

		session.clear();
		session.close();

	}

	@Override
	public void action(CouponActionReq actionReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(CouponReq couponReq, Map<String, Object> map) {

		Coupon coupon = couponMapper.mapCoupon(couponReq);
		if (coupon != null) {
			User user = authenticationServices.getCurrentUser();
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				coupon.setUser(user);
				coupon.setPublicId(helperServices.getGenPublicId());
				session.persist(coupon);
				transaction.commit();
				session.clear();

				map.put("status", true);
				map.put("message", "Coupon Saved :)");

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				e.printStackTrace();
				map.put("status", false);
				map.put("message", e.getMessage());

			}
		}
	}

	@Override
	public void getAllCouponByType(int start, int size, int type, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getOne(String id, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

}
