package com.altqart.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.CategoryMapper;
import com.altqart.model.Category;
import com.altqart.repository.CategoryRepository;
import com.altqart.req.model.CategoryReq;
import com.altqart.resp.model.RespCategory;
import com.altqart.services.CategoryServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServicesImpl implements CategoryServices {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private CategoryMapper categoryMapper;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public Iterable<Category> getCategoryList() {

		return categoryRepository.findAll();
	}

	@Override
	public List<Category> getAllCategories() {

		if (categoryRepository != null) {

			Iterable<Category> categories = categoryRepository.findAll();

			if (categories != null) {

				return (List<Category>) categories;

			}
		}
		return null;
	}

	@Override
	public long getCount() {
		return categoryRepository.count();
	}

	@Override
	public void updateCategory(CategoryReq categoryReq, Map<String, Object> map) {

		boolean status = true;

		if (categoryReq != null) {

			if (categoryReq.getId() > 0) {
				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					Category dbCategory = session.get(Category.class, categoryReq.getId());

					if (!helperServices.isNullOrEmpty(categoryReq.getName())) {
						if (!helperServices.isEqual(categoryReq.getName(), dbCategory.getName())) {
							dbCategory.setName(categoryReq.getName());
						}

					}

					if (!helperServices.isNullOrEmpty(categoryReq.getDescription())) {
						if (!helperServices.isEqual(categoryReq.getDescription(), dbCategory.getDescription())) {
							dbCategory.setDescription(categoryReq.getDescription());
						}

					}

					if (categoryReq.getParentId() > 0) {
						Category parentCat = getCategoryById(categoryReq.getParentId());
						dbCategory.setParent(parentCat);
					}

					session.merge(dbCategory);

					transaction.commit();
					status = true;
					session.clear();
					map.put("message", "Category Updated ");

				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();

					}
					log.info("updateCategory " + e.getMessage());
					map.put("message", e.getMessage());
				}

				session.close();
				map.put("status", status);
			}
		}
	}

	private Category getCategoryById(int id) {

		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public Category getCategoryByValue(String value) {
		Category category = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Category> criteriaQuery = (CriteriaQuery<Category>) criteriaBuilder
					.createQuery(Category.class);

			Root<Category> root = (Root<Category>) criteriaQuery.from(Category.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("value"), value));

			Query<Category> query = session.createQuery(criteriaQuery);

			category = query.getSingleResult();

			session.clear();
		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();
		return category;
	}

	@Override
	public RespCategory getRespCategoryByValue(String value) {
		RespCategory respcategory = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Category> criteriaQuery = (CriteriaQuery<Category>) criteriaBuilder
					.createQuery(Category.class);

			Root<Category> root = (Root<Category>) criteriaQuery.from(Category.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("value"), value));

			Query<Category> query = session.createQuery(criteriaQuery);

			respcategory = categoryMapper.mapRespCategory(query.getSingleResult());

			session.clear();
		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();
		return respcategory;
	}

	@Override
	public void addCategory(CategoryReq categoryReq, Map<String, Object> map) {
		Category category = categoryMapper.mapCategory(categoryReq);

		map.put("stats", false);
		map.put("message", "Category save failed");
		if (categoryReq.getParentId() > 0) {
			Category parent = getCategoryById(categoryReq.getParentId());
			category.setParent(parent);
			category.setSub(true);
		} else {
			category.setSub(false);
		}
		categoryRepository.save(category);

		if (category != null) {
			if (category.getId() > 0) {
				map.put("status", true);
				map.put("message", "Category saved");
			}
		}

	}

	@Override
	public List<RespCategory> getAllRespCategory() {

		List<RespCategory> respCategories = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Category> criteriaQuery = (CriteriaQuery<Category>) criteriaBuilder
					.createQuery(Category.class);

			Root<Category> root = (Root<Category>) criteriaQuery.from(Category.class);

			criteriaQuery.select(root);

			Query<Category> query = session.createQuery(criteriaQuery);

			List<Category> categories = query.getResultList();

			if (categories != null) {
				respCategories = categoryMapper.mapAllRespCategory(categories);
			}

			session.clear();

		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

			e.printStackTrace();
		}

		session.close();

		return respCategories;
	}

	@Override
	public RespCategory getRespCategoryById(int id) {

		RespCategory respCategory = null;

		Session session = sessionFactory.openSession();

		try {

			Category category = session.get(Category.class, id);
			respCategory = categoryMapper.mapRespCategory(category);

			session.clear();
		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();

		return respCategory;
	}

	@Override
	public void removeCategory(String id, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<RespCategory> getAllSubRespCategoryById(int id) {

		List<RespCategory> respCategories = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Category> criteriaQuery = (CriteriaQuery<Category>) criteriaBuilder
					.createQuery(Category.class);

			Root<Category> root = (Root<Category>) criteriaQuery.from(Category.class);

			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("parent").get("id"), id));
			Query<Category> query = session.createQuery(criteriaQuery);

			List<Category> categories = query.getResultList();

			if (categories != null) {
				respCategories = categoryMapper.mapAllRespCategory(categories);
			}

			session.clear();

		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

			e.printStackTrace();
		}

		session.close();

		return respCategories;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getCategoryValueIds(String value) {

		List<Integer> ids = new ArrayList<>();

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Category> criteriaQuery = (CriteriaQuery<Category>) criteriaBuilder
					.createQuery(Category.class);

			Root<Category> root = (Root<Category>) criteriaQuery.from(Category.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("value"), value));

			Query<Category> query = session.createQuery(criteriaQuery);

			Category category = query.getSingleResult();

			if (category != null) {
				ids.add(category.getId());
				categoryMapper.mapAllCatAndSubCatIds(category, ids);
			}

			session.clear();
		} catch (Exception e) {

			log.info("Get Category By Value " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();

		return ids;
	}

}
