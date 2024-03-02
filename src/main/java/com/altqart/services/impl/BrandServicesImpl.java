package com.altqart.services.impl;

import java.util.Date;
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
import com.altqart.mapper.BrandMapper;
import com.altqart.model.Brand;
import com.altqart.repository.BrandRepository;
import com.altqart.req.model.ReqBrand;
import com.altqart.resp.model.RespBrand;
import com.altqart.services.BrandServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrandServicesImpl implements BrandServices {

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private HelperServices helperServices;

	private SessionFactory sessionFactory;

	@Autowired
	private BrandMapper brandMapper;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<Brand> getAllBrand() {

		return (List<Brand>) brandRepository.findAll();
	}

	@Override
	public Brand getBrandByPublicId(String id) {

		Optional<Brand> optional = brandRepository.getBrandByPublicId(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public boolean addBrand(ReqBrand brandReq, Map<String, Object> map) {

		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			Brand brand = brandMapper.mapBrand(brandReq);
			session.persist(brand);
			transaction.commit();

			status = true;
			map.put("message", "Brand Saved");
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			map.put("message", e.getMessage());

			status = false;
		}

		map.put("status", status);
		return status;
	}

	@Override
	public void getAllRespBrand(Map<String, Object> map, int start, int size) {

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Brand> criteriaQuery = (CriteriaQuery<Brand>) criteriaBuilder.createQuery(Brand.class);

			Root<Brand> root = (Root<Brand>) criteriaQuery.from(Brand.class);

			criteriaQuery.select(root);
			Query<Brand> query = session.createQuery(criteriaQuery);

			List<Brand> brands = query.getResultList();

			if (brands != null) {
				List<RespBrand> respBrands = brandMapper.mapAllRespBrand(brands);

				if (respBrands != null) {
					map.put("response", respBrands);
					map.put("status", true);
					map.put("message", respBrands.size() + " Brand(s) found ");
				}

			}

			session.clear();
		} catch (Exception e) {

			log.info("getBrandByAliasName " + e.getMessage());
			map.put("status", false);
			map.put("message", "Brand not found ");
			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public void removeBrand(ReqBrand brandReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update(ReqBrand reqBrand, Map<String, Object> map) {

		boolean status = false;

		if (reqBrand != null) {

			if (!helperServices.isNullOrEmpty(reqBrand.getId())) {
				Brand brand = getBrandByPublicId(reqBrand.getId());

				if (brand != null) {
					Session session = sessionFactory.openSession();
					Transaction transaction = null;
					try {
						transaction = session.beginTransaction();
						Brand dbBrand = session.get(Brand.class, brand.getId());

						if (!helperServices.isNullOrEmpty(reqBrand.getName())) {
							dbBrand.setName(reqBrand.getName());

						}

						if (!helperServices.isNullOrEmpty(reqBrand.getDescription())) {
							dbBrand.setDescription(reqBrand.getDescription());
						}

						session.merge(dbBrand);

						transaction.commit();
						session.clear();
						status = true;
						map.put("message", "Brand updated");
					} catch (Exception e) {

						if (transaction != null) {
							transaction.rollback();
						}
						status = false;
						map.put("message", "Brand update failed");
					}
					session.close();
					map.put("status", status);
				}
			}
		}
		map.put("status", status);
		return status;
	}

	@Override
	public Brand getBrandByAliasName(String brandName) {

		if (!helperServices.isNullOrEmpty(brandName)) {

			Brand brand = null;

			if (!helperServices.isNullOrEmpty(brandName)) {

				Session session = sessionFactory.openSession();

				try {

					CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
					CriteriaQuery<Brand> criteriaQuery = (CriteriaQuery<Brand>) criteriaBuilder
							.createQuery(Brand.class);

					Root<Brand> root = (Root<Brand>) criteriaQuery.from(Brand.class);

					criteriaQuery.select(root);
					criteriaQuery.where(criteriaBuilder.equal(root.get("aliasName"), brandName));
					Query<Brand> query = session.createQuery(criteriaQuery);

					brand = query.getSingleResult();

					session.clear();

				} catch (Exception e) {

					log.info("getBrandByAliasName " + e.getMessage());

//					e.printStackTrace();
				}

				session.close();

			}

			return brand;
		}

		return null;
	}

	@Override
	public Brand getBrandByKey(String key) {

		if (!helperServices.isNullOrEmpty(key)) {

			Brand brand = null;

			if (!helperServices.isNullOrEmpty(key)) {

				Session session = sessionFactory.openSession();

				try {

					CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
					CriteriaQuery<Brand> criteriaQuery = (CriteriaQuery<Brand>) criteriaBuilder
							.createQuery(Brand.class);

					Root<Brand> root = (Root<Brand>) criteriaQuery.from(Brand.class);

					criteriaQuery.select(root);
					criteriaQuery.where(criteriaBuilder.equal(root.get("companyKey"), key));
					Query<Brand> query = session.createQuery(criteriaQuery);

					brand = query.getSingleResult();

					session.clear();
				} catch (Exception e) {

					log.info("getBrandByKey " + e.getMessage());
//					e.printStackTrace();
				}

				session.close();

			}

			return brand;
		}

		return null;
	}

	@Override
	public boolean addAllBrand(List<Brand> brands) {

		boolean status = false;

		if (brands != null) {
			Date date = new Date();
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				for (Brand brand : brands) {
					brand.setDate(date);
					session.persist(brand);
				}

				transaction.commit();

				session.clear();
				status = true;
			} catch (Exception e) {
				status = false;
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				log.info("addAllBrand " + e.getMessage());
			}

			session.close();
		}

		return status;
	}

}
