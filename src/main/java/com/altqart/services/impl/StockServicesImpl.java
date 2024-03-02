package com.altqart.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.model.Product;
import com.altqart.repository.ProductRepository;
import com.altqart.services.StockServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockServicesImpl implements StockServices {

	@Autowired
	private ProductRepository productRepository;

	private SessionFactory sessionFactory;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<Product> getAllProduct() {
		return (List<Product>) productRepository.findAll();
	}

	
	@Override
	public List<Product> getProductsByItemId(int itemId) {

		List<Product> list = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = (CriteriaQuery<Product>) criteriaBuilder.createQuery(Product.class);

			Root<Product> root = (Root<Product>) criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("item").get("id"), itemId),
					criteriaBuilder.equal(root.get("active"), true)));

			Query<Product> query = session.createQuery(criteriaQuery);

			list = query.getResultList();

		} catch (Exception e) {

//			e.printStackTrace();
			log.info("getProductsByItemId " + e.getMessage());

		}
		session.clear();
		session.close();
		return list;

	}

	@Override
	public double getProductStockPrice() {
		
		double totalStockAmount = 0;
		
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Double> query = builder.createQuery(Double.class);

			Root<Product> root = query.from(Product.class);
			query.where(builder.greaterThan(root.get("qty"), 0));
			query.select(builder.construct(Double.class,
					builder.sum(builder.prod(root.<BigDecimal>get("purchasePrice"), root.<Integer>get("qty")))

			));

			totalStockAmount = session.createQuery(query).getSingleResult();

		} catch (Exception e) {

//			e.printStackTrace();
			log.info("getProductsByItemId " + e.getMessage());

		}
		session.clear();
		session.close();

		return totalStockAmount;
	}
}
