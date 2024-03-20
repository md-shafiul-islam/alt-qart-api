package com.altqart.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.resource.jdbc.LogicalConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.OrderBy;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.ProductMapper;
import com.altqart.model.Brand;
import com.altqart.model.Category;
import com.altqart.model.Color;
import com.altqart.model.ImageGallery;
import com.altqart.model.ItemColor;
import com.altqart.model.Material;
import com.altqart.model.Measurement;
import com.altqart.model.MetaData;
import com.altqart.model.Product;
import com.altqart.model.ProductDescription;
import com.altqart.model.Size;
import com.altqart.model.SpecKey;
import com.altqart.model.Specification;
import com.altqart.model.User;
import com.altqart.model.Variant;
import com.altqart.repository.ProductRepository;
import com.altqart.req.model.ItemColorReq;
import com.altqart.req.model.ProductCatReq;
import com.altqart.req.model.ProductReq;
import com.altqart.req.model.SpecificationReq;
import com.altqart.req.model.VariantReq;
import com.altqart.resp.model.RespDetailProduct;
import com.altqart.resp.model.RespMinProduct;
import com.altqart.resp.model.RespProduct;
import com.altqart.services.BrandServices;
import com.altqart.services.CategoryServices;
import com.altqart.services.ProductServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServicesImpl implements ProductServices {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductMapper productMapper;

	private SessionFactory sessionFactory;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private BrandServices brandServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespProduct> getAllProducts(int start, int size) {

		List<RespProduct> respProducts = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			TypedQuery<Product> typedQuery = session.createQuery(criteriaQuery);

			if (size > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			products = typedQuery.getResultList();

			if (products != null) {
				respProducts = productMapper.mapAllRespProductDetals(products);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllProducts " + e.getMessage());
			e.printStackTrace();

		}
		session.close();

		return respProducts;
	}

	@Override
	public List<RespMinProduct> getAllMinRespProduct(int start, int size) {

		List<RespMinProduct> respProducts = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			TypedQuery<Product> typedQuery = session.createQuery(criteriaQuery);

			if (size > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			List<Product> products = typedQuery.getResultList();

			if (products != null) {
				respProducts = productMapper.mapAllMinRespProductDetals(products);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllProducts " + e.getMessage());
			e.printStackTrace();

		}
		session.close();

		return respProducts;

	}

	@Override
	public List<Product> getAllLowStockQtyProduct() {

		List<Product> date = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.le(root.get("qty"), 5));

			Query<Product> query = session.createQuery(criteriaQuery);

			date = query.getResultList();

			session.clear();

		} catch (Exception e) {

			log.info("getAllLowStockQtyProduct " + e.getMessage());
		}
		session.close();
		return date;

	}

	@Override
	public List<Product> getAllAvailableProduct() {

		List<Product> products = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.greaterThan(root.get("qty"), 0));

			Query<Product> query = session.createQuery(criteriaQuery);

			products = query.getResultList();

			session.clear();

		} catch (Exception e) {

			log.info("getAllAvailableProduct " + e.getMessage());

		}
		session.close();
		return products;
	}

	@Override
	public List<Product> getAllAvailableProductsByItemId(String id) {

		List<Product> products = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("item").get("id"), id),
					criteriaBuilder.greaterThan(root.get("qty"), 0)));

			Query<Product> query = session.createQuery(criteriaQuery);

			products = query.getResultList();

			session.clear();

		} catch (Exception e) {

			log.info("getAllAvailableProductsByItemId " + e.getMessage());

		}
		session.close();
		return products;
	}

	@Override
	public Product getProductById(int id) {

		Optional<Product> dbProduct = productRepository.findById(id);

		if (dbProduct.isPresent() && !dbProduct.isEmpty()) {
			return dbProduct.get();
		}

		return null;
	}

	@Override
	public List<Product> getProductsByCategory(Category category) {

		List<Product> products = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = (CriteriaQuery<Product>) criteriaBuilder.createQuery(Product.class);

			Root<Product> root = (Root<Product>) criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("category").get("id"), category.getId()));

			Query<Product> query = session.createQuery(criteriaQuery);

			products = query.getResultList();

			session.clear();

		} catch (Exception e) {

			log.info("getProductsByCategory " + e.getMessage());
		}

		session.close();

		return products;
	}

	@Override
	public List<Product> getProductByCatId(int id) {

		List<Product> products = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = (CriteriaQuery<Product>) criteriaBuilder.createQuery(Product.class);

			Root<Product> root = (Root<Product>) criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("category").get("id"), id));

			Query<Product> query = session.createQuery(criteriaQuery);

			products = query.getResultList();

			session.clear();

		} catch (Exception e) {

			log.info("getProductByCatId " + e.getMessage());

		}

		session.close();
		return products;

	}

	@Override
	public long getCount() {
		return productRepository.count();
	}

	@Override
	public RespProduct getProductByPublicId(String id) {
		RespProduct product = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = (CriteriaQuery<Product>) criteriaBuilder.createQuery(Product.class);

			Root<Product> root = (Root<Product>) criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Product> query = session.createQuery(criteriaQuery);

			product = productMapper.mapRespProduct(query.getSingleResult());

			session.clear();

		} catch (Exception e) {

			log.info("getProductByCatId " + e.getMessage());

		}

		session.close();
		return product;
	}

	@Override
	public RespDetailProduct getDetailsProductByPublicId(String id) {

		RespDetailProduct product = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = (CriteriaQuery<Product>) criteriaBuilder.createQuery(Product.class);

			Root<Product> root = (Root<Product>) criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Product> query = session.createQuery(criteriaQuery);

			product = productMapper.mapRespDetailsProduct(query.getSingleResult());

			session.clear();

		} catch (Exception e) {

			log.info("getProductByCatId " + e.getMessage());

		}

		session.close();
		return product;
	}

	@Override
	public void add(ProductReq productReq, Map<String, Object> map) {

		if (productReq != null) {
			User user = authenticationServices.getCurrentUser();

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Date date = new Date();

				User dbUser = null;

				if (user != null) {
					dbUser = session.get(User.class, user.getId());
				} else {
					dbUser = session.get(User.class, 1);
				}

				Product product = inittProduct(productReq, session);
				product.setCreateDate(date);
				product.setUpdateDate(date);

				if (dbUser != null) {
					product.setStore(dbUser.getStore());
				}

				session.persist(product);

				if (product.getDescriptions() != null) {
					for (ProductDescription description : product.getDescriptions()) {
						description.setProduct(product);
						session.persist(description);
					}
				}

				if (product.getMaterials() != null) {
					for (Material material : product.getMaterials()) {

						if (material != null) {

							if (material.getId() > 0) {
								Material dbMaterial = session.get(Material.class, material.getId());
								dbMaterial.getProducts().add(product);
								product.getMaterials().add(dbMaterial);
								session.merge(dbMaterial);
							} else {
								material.getProducts().add(product);
								product.getMaterials().add(material);
								session.persist(material);
							}
						}

					}
				}

				if (product.getImages() != null) {

					for (ImageGallery gallery : product.getImages()) {

						if (gallery != null) {

							if (gallery.getId() > 0) {
								ImageGallery imageGallery = session.get(ImageGallery.class, gallery.getId());

								imageGallery.getProduct().add(product);
								product.getImages().add(gallery);
								session.merge(imageGallery);
							} else {

								gallery.getProduct().add(product);
								product.getImages().add(gallery);
								session.persist(gallery);
							}
						}

					}
				}

				if (product.getMetaDatas() != null) {

					for (MetaData metaData : product.getMetaDatas()) {
						metaData.getProducts().add(product);
						product.getMetaDatas().add(metaData);
						session.persist(metaData);
					}
				}

				for (Variant variant : product.getVariants()) {

					variant.setProduct(product);

					if (variant.getDicountPrice() > 0) {
						variant.setDicount(true);
					} else {
						variant.setDicount(false);
					}

					session.persist(variant);
				}

				if (product.getSpecifications() != null) {
					for (Specification specification : product.getSpecifications()) {
						specification.setProduct(product);
						session.persist(specification);
					}
				}

				transaction.commit();
				map.put("status", true);
				map.put("message", "Prodduct Save successfully");

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();

				map.put("status", false);
				map.put("message", e.getMessage());
			} finally {
				session.close();
			}
		}

	}

	private Product inittProduct(ProductReq productReq, Session session) throws Exception {

		Product product = productMapper.mapProductReq(productReq);
		Brand brand = brandServices.getBrandByPublicId(productReq.getBrand());

		if (product != null) {

			product.setPublicId(helperServices.getGenPublicId());

			if (helperServices.isNullOrEmpty(product.getAliasName())) {
				String aliasName = productReq.getTitle().toLowerCase().replaceAll(" ", "-");
				product.setAliasName(aliasName);
			}

			product.setAvgRating(converterServices.getRandomBetweenTwoNumbers(4.2, 5));

			Brand dbBrand = null;

			if (brand != null) {
				dbBrand = session.get(Brand.class, brand.getId());
			}

			if (dbBrand == null) {
				throw new Exception("Product Brand not found");
			}

			product.setBrand(dbBrand);

			Category dbCategory = session.get(Category.class, productReq.getCategory());

			if (dbCategory == null) {
				throw new Exception("Category not found");
			}
			product.setCategory(dbCategory);

			Measurement dbMeasurement = session.get(Measurement.class, productReq.getMeasurement());
			if (dbMeasurement == null) {
				throw new Exception("Product Package/Box Measurement not found");
			}

			product.setMeasurement(dbMeasurement);

			if (productReq.getMaterials() != null) {

				Set<Material> materials = new HashSet<>();

				for (Integer mId : productReq.getMaterials()) {

					Material dbMaterial = session.get(Material.class, mId);
					dbMaterial.getProducts().add(product);

					product.getMaterials().add(dbMaterial);

					materials.add(dbMaterial);
				}

				product.setMaterials(materials);
			}

			if (productReq.getItemColorReqs() != null) {
				List<ItemColor> itemColors = new ArrayList<>();
				for (ItemColorReq itemColorReq : productReq.getItemColorReqs()) {

					ItemColor itemColor = new ItemColor();
					Color dbItColor = session.get(Color.class, itemColorReq.getId());
					itemColor.setColor(dbItColor);
					itemColor.setUrl(itemColorReq.getUrl());
					itemColor.setProduct(product);

					session.persist(itemColor);
					itemColors.add(itemColor);
				}

				product.setItemColors(itemColors);
			}

			String originBarCode = helperServices.getProductOriginBarCode(productReq.getOriginCode());
			List<Variant> variants = new ArrayList<>();
			if (productReq.getVariants() != null) {

				for (VariantReq variantReq : productReq.getVariants()) {
					Color dbColor = null;
					if (variantReq.getColor() > 0) {
						dbColor = session.get(Color.class, variantReq.getColor());
					}

					if (dbColor == null) {
						throw new Exception("Product Variant color not set. Please, Set Color & send again !!");
					}

					Size dbSize = session.get(Size.class, variantReq.getSize());
					Variant variant = productMapper.mapVariant(variantReq);
					if (variant != null) {
						String barCode = helperServices.getProductBarCodeByOrigin(originBarCode,
								" S " + dbSize.getName() + " C " + dbColor.getName());
						variant.setBarCode(barCode);
						variant.setSize(dbSize);
						variant.setColor(dbColor);
					}

					variants.add(variant);
				}

			}
			product.setVariants(variants);

			if (productReq.getSpecifications() != null) {
				List<Specification> specifications = new ArrayList<>();

				for (SpecificationReq specificationReq : productReq.getSpecifications()) {
					Specification specification = productMapper.mapSpecification(specificationReq);

					SpecKey dbSpecKey = session.get(SpecKey.class, specificationReq.getSpecKey());
					specification.setKey(dbSpecKey);
					specification.setProduct(product);
					specifications.add(specification);
				}

				product.setSpecifications(specifications);

			}

		}

		return product;
	}

	@Override
	public void update(ProductReq productReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public Product getProductByStrId(String product) {

		Optional<Product> optional = productRepository.getProductByPublicId(product);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();

		}

		return null;
	}

	// Start Variant

	@Override
	public Variant getVariantById(String id) {

		Variant variant = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Variant> criteriaQuery = (CriteriaQuery<Variant>) criteriaBuilder.createQuery(Variant.class);

			Root<Variant> root = (Root<Variant>) criteriaQuery.from(Variant.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Variant> query = session.createQuery(criteriaQuery);

			variant = query.getSingleResult();

			session.clear();

		} catch (Exception e) {

			log.info("Get variant By Id" + e.getMessage());

		}

		session.close();

		return variant;
	}

	// End Variant

	@Override
	public void getRespProductExceptCategory(ProductCatReq catReq, Map<String, Object> map) {

		if (catReq != null) {

			if (catReq.isStatus()) {
				map.put("message", "Product not found By Except Category");
			} else {
				map.put("message", "Product not found By Category");
			}
		}

		List<RespMinProduct> respProducts = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);

			criteriaQuery.select(root);

			if (catReq.isStatus()) {
				criteriaQuery.where(criteriaBuilder.notEqual(root.get("category").get("id"), catReq.getId()));
			} else {
				criteriaQuery.where(criteriaBuilder.equal(root.get("category").get("id"), catReq.getId()));
			}

			TypedQuery<Product> typedQuery = session.createQuery(criteriaQuery);

			if (catReq.getSize() > 0) {
				typedQuery.setFirstResult(catReq.getStart());
				typedQuery.setMaxResults(catReq.getSize());
			}

			List<Product> products = typedQuery.getResultList();

			if (products != null) {
				respProducts = productMapper.mapAllMinRespProductDetals(products);

				map.put("response", respProducts);
				map.put("status", true);

				if (catReq.isStatus()) {
					map.put("message", respProducts.size() + " Except Category Product(s) found");
				} else {
					map.put("message", respProducts.size() + " Product(s) found By Category");
				}
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllProducts " + e.getMessage());
			e.printStackTrace();

		}
		session.close();

	}

	@Override
	public void getAllRespMinProductByCategory(String value, Map<String, Object> map) {

		List<Integer> categoryIds = categoryServices.getCategoryValueIds(value);

		log.info("Categories " + categoryIds);

		List<RespMinProduct> respProducts = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

			Root<Product> root = criteriaQuery.from(Product.class);
			In<Integer> inClause = criteriaBuilder.in(root.get("category").get("id"));
			for (Integer id : categoryIds) {
				inClause.value(id);
			}
			criteriaQuery.select(root).where(inClause);

			Query<Product> query = session.createQuery(criteriaQuery);
			List<Product> products = query.getResultList();

			if (products != null) {
				List<RespMinProduct> minProducts = productMapper.mapAllMinRespProductDetals(products);

				if (minProducts != null) {
					map.put("message", minProducts.size() + " Product found by category");
					map.put("response", minProducts);
					map.put("status", true);
				}
			}
			session.clear();

		} catch (Exception e) {

			log.info("getAllProducts " + e.getMessage());
			e.printStackTrace();

		}
		session.close();

	}

	@Override
	public List<RespMinProduct> getRandomMinRespProduct(int start, int size) {
		List<RespMinProduct> respProducts = new ArrayList<>();
		Session session = sessionFactory.openSession();

		if (size <= 0) {
			size = 6;
		}
		try {
//			SELECT * FROM product ORDER BY rand() LIMIT 3
			List<Product> products = session.createNativeQuery("select * from product ORDER BY rand() ", Product.class)
					.setMaxResults(size).getResultList();

			if (products != null) {

				respProducts = productMapper.mapAllMinRespProductDetals(products);

			}
			session.clear();

		} catch (Exception e) {

			log.info("getAllProducts " + e.getMessage());
			e.printStackTrace();

		}
		session.close();

		return respProducts;
	}

}
