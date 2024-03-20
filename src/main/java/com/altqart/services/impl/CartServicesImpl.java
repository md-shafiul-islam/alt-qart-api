package com.altqart.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperDateServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.mapper.CartMapper;
import com.altqart.model.Cart;
import com.altqart.model.CartItem;
import com.altqart.model.Coupon;
import com.altqart.model.Product;
import com.altqart.model.Stakeholder;
import com.altqart.model.User;
import com.altqart.model.Variant;
import com.altqart.repository.CartRepository;
import com.altqart.req.model.CartChooseReq;
import com.altqart.req.model.CartItemReq;
import com.altqart.req.model.CartReq;
import com.altqart.req.model.CouponApplyReq;
import com.altqart.resp.model.RespCart;
import com.altqart.services.CartServices;
import com.altqart.services.CouponServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServicesImpl implements CartServices {

	@Autowired
	private CouponServices couponServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private HelperDateServices dateServices;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private EsInitializerServices esInitializerServices;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public void getAllCart(Map<String, Object> map, int start, int size) {

		map.put("message", "Cart's not found");
		map.put("status", false);
		map.put("response", null);

		List<Cart> carts = null;

		log.info("Public Id " + helperServices.getGenPublicId());

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Cart> criteriaQuery = criteriaBuilder.createQuery(Cart.class);
			Root<Cart> root = criteriaQuery.from(Cart.class);

			CriteriaQuery<Cart> select = criteriaQuery.select(root);

			select.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Cart> query = session.createQuery(select);
			carts = query.getResultList();

			if (carts != null) {
				cartMapper.mapAllCart(carts, map);
			}

			session.clear();

		} catch (Exception e) {
			map.put("message", e.getMessage());
			map.put("status", false);
			map.put("response", null);
		}

		session.close();

	}

	@Override
	public void addCart(CartReq cartReq, Map<String, Object> map) {

		if (cartReq != null) {
			Cart cart = cartMapper.mapCart(cartReq);
			User user = authenticationServices.getCurrentUser();
			if (cart != null) {

				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();
					User dbUser = session.get(User.class, user.getId());
					Date date = new Date();

					Stakeholder stakeholder = null;
					if (dbUser.getStakeholder() == null) {
						stakeholder = esInitializerServices.initStakeholderViaUser(dbUser, date);
						session.persist(stakeholder);
					} else {
						stakeholder = dbUser.getStakeholder();
					}
					cart.setStakeholder(stakeholder);
					cart.setPublicId(helperServices.getGenPublicId());
					session.persist(cart);

					for (CartItem item : cart.getCartItems()) {
						item.setCart(cart);
						session.persist(item);
					}
					transaction.commit();
					session.clear();
				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();

					}
				}

				session.close();
			}
		}

	}

	@Override
	public void updateCart(CartReq cart, Map<String, Object> map) {

	}

	@Override
	public void addCartItem(CartItemReq cartItem, Map<String, Object> map) {

		log.info("Services Add TO Cart Item ...");

		if (cartItem != null) {

			Cart cart = null;

			User user = authenticationServices.getCurrentUser();

			if (!helperServices.isNullOrEmpty(cartItem.getCart())) {
				cart = getCartById(cartItem.getCart());
			}

			Session session = sessionFactory.openSession();
			Transaction transaction = null;
			Date date = new Date();
			try {
				transaction = session.beginTransaction();

				Stakeholder dbStakeholder = null;
				User dbUser = null;
				Cart dbCart = null;

				boolean isCreate = false;

				if (user != null) {

					dbUser = session.get(User.class, user.getId());

					if (dbUser != null) {
						if (dbUser.getStakeholder() != null) {
							dbStakeholder = session.get(Stakeholder.class, dbUser.getStakeholder().getId());

							if (dbStakeholder != null) {

								if (dbStakeholder.getCart() != null) {
									dbCart = session.get(Cart.class, dbStakeholder.getCart().getId());
								}
							}
						}
					}
				}

				if (dbStakeholder == null) {
					dbStakeholder = esInitializerServices.initStakeholderViaUser(dbUser, date);
					session.persist(dbStakeholder);
				}

				if (dbCart == null) {

					if (cart != null) {
						dbCart = session.get(Cart.class, cart.getId());
					} else {
						dbCart = esInitializerServices.initCart(date);
						dbCart.setStakeholder(dbStakeholder);
						session.persist(dbCart);
						isCreate = true;
					}
				}

				CartItem item = cartMapper.mapCartItem(cartItem);

				double totalQty = 0, totalAmount = 0, discount = 0, totalChooseAmount = 0, chooseQty = 0;

				if (isCreate) {
					if (dbCart.getCartItems() == null) {
						dbCart.setCartItems(new ArrayList<>());
					}
					item.setCart(dbCart);
					dbCart.getCartItems().add(item);

					totalAmount = item.getSubTotal();
					totalChooseAmount = item.getSubTotal();
					totalQty = item.getQty();
					chooseQty = item.getQty();
					discount = discount + (item.getPrice() - item.getDiscountPrice());

					log.info("Cart Item Variant ID " + item.getVariant().getId());

					session.persist(item);
					map.put("message", "Cart Item Added");
				} else {
					boolean isExist = false;

					Product dbProduct = session.get(Product.class, item.getProduct().getId());
					Variant dbVariant = session.get(Variant.class, item.getVariant().getId());

					if (dbVariant != null && dbProduct != null) {

						if (dbCart.getCartItems() != null) {
							for (CartItem dbItem : dbCart.getCartItems()) {

								discount = discount + (dbItem.getPrice() - dbItem.getDiscountPrice());

								if (dbItem.getVariant() != null && dbItem.getProduct() != null) {

									if (dbItem.getVariant().getId() == item.getVariant().getId()) {
										isExist = true;

										if (dbItem.getQty() == item.getQty()) {
											dbItem.setQty(dbItem.getQty() + 1);
										} else {
											double iQty = item.getQty() > 0 ? item.getQty() : 1;
											dbItem.setQty(dbItem.getQty() + iQty);

										}

										double subTotal = dbItem.getDiscountPrice() > 0
												? dbItem.getDiscountPrice() * dbItem.getQty()
												: dbItem.getPrice() * dbItem.getQty();

										dbItem.setSubTotal(subTotal);
										session.merge(dbItem);
									}
								}
								totalQty = dbItem.getQty() + totalQty;
								totalAmount = totalAmount + dbItem.getSubTotal();

								if (dbItem.isChoose()) {
									totalChooseAmount += dbItem.getSubTotal();
									chooseQty += dbItem.getQty();
								}
							}
						}

					}

					if (!isExist) {

						discount = discount + (item.getPrice() - item.getDiscountPrice());
						totalQty = totalQty + item.getQty();
						totalAmount = totalAmount + item.getSubTotal();

						item.setCart(dbCart);

						session.persist(item);
						dbCart.getCartItems().add(item);

						if (item.isChoose()) {
							totalChooseAmount += item.getSubTotal();
							chooseQty += item.getQty();
						}
					}
					if (isExist) {
						map.put("message", "Cart Item Updated");
					} else {
						map.put("message", "Cart Item Added");
					}
				}

				dbCart.setDiscount(discount);
				dbCart.setTotalAmount(totalAmount);
				dbCart.setTotalQty(totalQty);
				dbCart.setChooseAmount(totalChooseAmount);
				dbCart.setChooseQty(chooseQty);

				dbCart.setGrandTotal(totalAmount);

				if (dbCart.getChooseQty() > 0) {
					dbCart.setGrandTotal(totalChooseAmount);
				}

				session.merge(dbCart);
				log.info("Services Add TO Cart Item Cart Creating ... Befor MapRespCart");
				map.put("response", cartMapper.mapRespCart(dbCart));
				log.info("Services Add TO Cart Item Cart Creating ... Befor Commit");
				transaction.commit();
				session.clear();

				map.put("status", true);

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				map.put("status", false);
				map.put("response", null);
				map.put("message", e.getMessage());
			}

			session.close();

		}

	}

	@Override
	public Cart getCartById(String id) {

		Cart cart = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Cart> criteriaQuery = criteriaBuilder.createQuery(Cart.class);
			Root<Cart> root = criteriaQuery.from(Cart.class);

			CriteriaQuery<Cart> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Cart> query = session.createQuery(select);
			cart = query.getSingleResult();

		} catch (Exception e) {
			log.info("getOrdersByStatus " + e.getMessage());
			e.printStackTrace();
		}

		session.clear();
		session.close();

		return cart;
	}

	@Override
	public RespCart getRespCartById(String id) {

		RespCart respCart = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Cart> criteriaQuery = criteriaBuilder.createQuery(Cart.class);
			Root<Cart> root = criteriaQuery.from(Cart.class);

			CriteriaQuery<Cart> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Cart> query = session.createQuery(select);

			respCart = cartMapper.mapRespCart(query.getSingleResult());

		} catch (Exception e) {
			log.info("getRespCartById " + e.getMessage());
//			e.printStackTrace();
		}

		session.clear();
		session.close();

		return respCart;
	}

	@Override
	public RespCart getRespCartOnlyById(String id) {

		RespCart respCart = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Cart> criteriaQuery = criteriaBuilder.createQuery(Cart.class);
			Root<Cart> root = criteriaQuery.from(Cart.class);

			CriteriaQuery<Cart> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Cart> query = session.createQuery(select);
			Cart cart = query.getSingleResult();

			if (cart != null) {
				respCart = cartMapper.mapRespCartOnly(cart);
			}

		} catch (Exception e) {
			log.info("getOrdersByStatus " + e.getMessage());
			e.printStackTrace();
		}

		session.clear();
		session.close();

		return respCart;
	}

	@Override
	public void removeCartItem(CartItemReq cartItem, Map<String, Object> map) {

		if (cartItem != null) {

			Cart cart = getCartById(cartItem.getCart());

			if (cart != null) {
				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();
					Cart dbCart = session.get(Cart.class, cart.getId());
					if (dbCart.getCartItems() != null) {
						for (CartItem dbItem : dbCart.getCartItems()) {
							if (helperServices.isEqualAndFirstOneIsNotNull(dbItem.getPublicId(), cartItem.getId())) {
								dbCart.getCartItems().remove(dbItem);
								session.remove(dbItem);
							}
						}
					}

					session.merge(dbCart.getCartItems());
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

	@Override
	public void updateCartItem(CartItemReq cartItem, Map<String, Object> map) {

		if (cartItem != null) {
			Cart cart = getCartById(cartItem.getCart());

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Cart dbCart = session.get(Cart.class, cart.getId());

				for (CartItem item : dbCart.getCartItems()) {
					if (helperServices.isEqualAndFirstOneIsNotNull(cartItem.getId(), item.getPublicId())) {
						item.setQty(cartItem.getQty());

						if (item.getVariant() != null) {

							if (item.getVariant().isDicount()) {
								item.setPrice(item.getVariant().getDicountPrice());
							} else {
								item.setPrice(item.getVariant().getPrice());
							}

							item.setSubTotal(item.getPrice() * item.getQty());
						}

						session.merge(cartItem);

					}
				}

				session.merge(dbCart);

				transaction.commit();
				session.clear();

				map.put("status", true);
				map.put("message", "Cart Item Updated");

			} catch (Exception e) {
				map.put("status", false);
				map.put("message", e.getMessage());

			}
			session.close();

		}

	}

	@Override
	public void decrementCartItem(CartItemReq cartItemReq, Map<String, Object> map) {

		if (cartItemReq != null) {
			Cart cart = getCartById(cartItemReq.getCart());

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();
				Date date = new Date();
				Cart dbCart = session.get(Cart.class, cart.getId());

				double totalAmount = 0, discount = 0, totalQty = 0, totalChooseAmount = 0, chooseQty = 0;

				for (CartItem item : dbCart.getCartItems()) {

					if (helperServices.isEqualAndFirstOneIsNotNull(cartItemReq.getId(), item.getPublicId())) {
						item.setQty(item.getQty() - 1);
					}

					double localDiscount = 0, price = item.getPrice();

					if (item.getDiscountPrice() > 0) {
						localDiscount = item.getPrice() - item.getDiscountPrice();

						discount = discount + (localDiscount * item.getQty());

						price = item.getDiscountPrice();
					}

					double subTotal = price * item.getQty();

					item.setSubTotal(subTotal);
					totalAmount += subTotal;
					totalQty += item.getQty();
					if (item.isChoose()) {
						chooseQty += item.getQty();
						totalChooseAmount += item.getSubTotal();
					}
					session.merge(item);
				}

				dbCart.setChooseQty(chooseQty);
				dbCart.setDiscount(discount);
				dbCart.setTotalQty(totalQty);
				dbCart.setTotalAmount(totalAmount);
				dbCart.setUpdateDate(date);
				dbCart.setChooseAmount(totalChooseAmount);
				dbCart.setGrandTotal(totalAmount);
				if (dbCart.getChooseQty() > 0) {
					dbCart.setGrandTotal(totalChooseAmount);
				}
				session.merge(dbCart);

				transaction.commit();
				session.clear();

				map.put("status", true);
				map.put("message", "Cart Item decrement");
				map.put("response", cartMapper.mapRespCart(dbCart));

			} catch (Exception e) {
				map.put("status", false);
				map.put("message", e.getMessage());

			}

			session.close();

		}

	}

	@Override
	public void incrementCartItem(CartItemReq cartItem, Map<String, Object> map) {

		if (cartItem != null) {
			Cart cart = getCartById(cartItem.getCart());
			Date date = new Date();
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Cart dbCart = session.get(Cart.class, cart.getId());

				double totalAmount = 0, discount = 0, totalQty = 0, totalChooseAmount = 0, chooseQty = 0;
				for (CartItem item : dbCart.getCartItems()) {
					if (helperServices.isEqualAndFirstOneIsNotNull(cartItem.getId(), item.getPublicId())) {

						item.setQty(item.getQty() + 1);
					}

					double localDiscount = 0, price = item.getPrice();

					if (item.getDiscountPrice() > 0) {
						localDiscount = item.getPrice() - item.getDiscountPrice();

						discount = discount + (localDiscount * item.getQty());

						price = item.getDiscountPrice();
					}

					double subTotal = price * item.getQty();
					item.setSubTotal(subTotal);
					totalAmount = totalAmount + subTotal;
					totalQty = totalQty + item.getQty();

					if (item.isChoose()) {
						totalChooseAmount += item.getSubTotal();
						chooseQty += item.getQty();
					}

					session.merge(item);
				}

				dbCart.setDiscount(discount);
				dbCart.setUpdateDate(date);
				dbCart.setTotalAmount(totalAmount);
				dbCart.setTotalQty(totalQty);

				dbCart.setChooseAmount(totalChooseAmount);
				dbCart.setChooseQty(chooseQty);

				dbCart.setGrandTotal(totalAmount);

				if (dbCart.getChooseQty() > 0) {
					dbCart.setGrandTotal(totalChooseAmount);
				}

				session.merge(dbCart);

				transaction.commit();
				session.clear();

				map.put("status", true);
				map.put("message", "Cart Item increment");
				map.put("response", cartMapper.mapRespCart(dbCart));

			} catch (Exception e) {
				map.put("status", false);
				map.put("message", e.getMessage());

			}

			session.close();

		}

	}

	@Override
	public void deleteCartItem(CartItemReq cartItem, Map<String, Object> map) {

		if (cartItem != null) {
			Cart cart = getCartById(cartItem.getCart());
			Date date = new Date();
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Cart dbCart = session.get(Cart.class, cart.getId());
				CartItem targetedItem = null;

				if (dbCart.getCartItems() != null) {
					for (CartItem item : dbCart.getCartItems()) {
						if (helperServices.isEqualAndFirstOneIsNotNull(cartItem.getId(), item.getPublicId())) {

							targetedItem = item;

						}

					}
				}

				if (targetedItem != null) {
					CartItem dbCartItem = session.get(CartItem.class, targetedItem.getId());

					session.remove(dbCartItem);
					dbCart.getCartItems().remove(targetedItem);
				}
				session.merge(dbCart);

				dbCart = session.get(Cart.class, dbCart.getId());

				double totalAmount = 0, discount = 0, totalQty = 0, totalChooseAmount = 0, chooseQty = 0;
				for (CartItem item : dbCart.getCartItems()) {

					double lDiscount = item.getPrice() - item.getDiscountPrice();

					totalQty += item.getQty();
					totalAmount += item.getSubTotal();
					discount = discount + (lDiscount * item.getQty());

					if (item.isChoose()) {
						chooseQty += item.getQty();
						totalChooseAmount += item.getSubTotal();
					}
				}

				dbCart.setChooseQty(chooseQty);
				dbCart.setDiscount(discount);
				dbCart.setUpdateDate(date);
				dbCart.setTotalAmount(totalAmount);
				dbCart.setTotalQty(totalQty);
				dbCart.setGrandTotal(totalAmount);
				dbCart.setChooseAmount(totalChooseAmount);
				if (dbCart.getChooseQty() > 0) {
					dbCart.setGrandTotal(totalChooseAmount);
				}

				session.merge(dbCart);
				transaction.commit();
				session.clear();

				map.put("status", true);
				map.put("message", "Cart Item Deleted Successfully");
				map.put("response", cartMapper.mapRespCart(dbCart));

			} catch (Exception e) {
				map.put("status", false);
				map.put("message", e.getMessage());
				e.printStackTrace();

			}

			session.close();

		}

	}

	@Override
	public String getUserCartId() {

		String cartId = null;
		User user = authenticationServices.getCurrentUser();

		if (user != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				User dbUser = session.get(User.class, user.getId());
				if (dbUser != null) {

					if (dbUser.getStakeholder() != null) {

						Stakeholder dbStakeholder = session.get(Stakeholder.class, dbUser.getStakeholder().getId());

						if (dbStakeholder.getCart() != null) {
							cartId = dbStakeholder.getCart().getPublicId();
						} else {
							Cart cart = esInitializerServices.initCart(new Date());
							cart.setStakeholder(dbStakeholder);
							session.persist(cart);

							cartId = cart.getPublicId();
						}
					}

				}

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

		return cartId;
	}

	@Override
	public void getCartByUser(User user, Map<String, Object> map) {

		RespCart respCart = null;

		if (user != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				User dbUser = session.get(User.class, user.getId());
				if (dbUser != null) {
					Cart cart = null;
					if (dbUser.getStakeholder() != null) {

						Stakeholder dbStakeholder = session.get(Stakeholder.class, dbUser.getStakeholder().getId());

						if (dbStakeholder.getCart() != null) {
							cart = dbStakeholder.getCart();
						} else {
							cart = esInitializerServices.initCart(new Date());
							cart.setStakeholder(dbStakeholder);
							session.persist(cart);

						}
					}
					respCart = cartMapper.mapRespCart(cart);
				}

				transaction.commit();
				session.clear();
				map.put("status", true);
				map.put("message", "User Cart found");
				map.put("response", respCart);
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

	@Override
	public void toggleCartItem(CartChooseReq cartChoose, Map<String, Object> map) {

		if (cartChoose != null) {
			Cart cart = getCartById(cartChoose.id);

			if (cart != null) {

				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					Cart dbCart = session.get(Cart.class, cart.getId());

					boolean chooseAll = true;

					double chooseQty = 0, chooseTotalAmount = 0;

					if (dbCart.getCartItems() != null) {

						for (CartItem item : dbCart.getCartItems()) {

							if (helperServices.isEqualAndFirstOneIsNotNull(cartChoose.getCartItem(),
									item.getPublicId())) {

								if (item.isChoose()) {
									map.put("message", "Cart Item deselect");
								} else {
									map.put("message", "Cart Item select");
								}

								item.setChoose(!item.isChoose());
								session.merge(item);
							}

							if (!item.isChoose()) {
								chooseAll = false;
							}

							if (item.isChoose()) {
								chooseQty += item.getQty();
								chooseTotalAmount += item.getSubTotal();
							}
						}
					}

					dbCart.setChoose(chooseAll);
					dbCart.setChooseAmount(chooseTotalAmount);
					dbCart.setGrandTotal(chooseTotalAmount);
					dbCart.setChooseQty(chooseQty);

					session.merge(dbCart);
					transaction.commit();
					session.clear();
					map.put("status", true);
					map.put("response", cartMapper.mapRespCart(dbCart));
				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					map.put("status", false);
					map.put("message", "Cart Item tggle failed");
				}

				session.close();

			}
		}

	}

	@Override
	public void toggleAllCartItem(CartChooseReq cartChoose, Map<String, Object> map) {

		if (cartChoose != null) {
			Cart cart = getCartById(cartChoose.getId());

			if (cart != null) {

				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					Cart dbCart = session.get(Cart.class, cart.getId());

					for (CartItem item : dbCart.getCartItems()) {
						item.setChoose(!dbCart.isChoose());
						session.merge(item);
					}

					dbCart.setChoose(!dbCart.isChoose());

					if (dbCart.isChoose()) {
						dbCart.setChooseQty(dbCart.getTotalQty());
						dbCart.setChooseAmount(dbCart.getTotalAmount());
						dbCart.setGrandTotal(dbCart.getTotalAmount());
					} else {
						dbCart.setChooseQty(0);
						dbCart.setChooseAmount(0);
						dbCart.setGrandTotal(0);
					}

					session.merge(dbCart);
					transaction.commit();
					session.clear();

					map.put("response", cartMapper.mapRespCart(dbCart));
					map.put("status", true);
					map.put("message", "Cart Items toggle successfully");

				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					map.put("status", false);
					map.put("message", "Cart All items toggle failed");
				}

				session.clear();
			}
		}

	}

	@Override
	public void getApplyCoupon(CouponApplyReq couponReq, Map<String, Object> map) {

		if (couponReq != null) {

			Coupon coupon = couponServices.getCouponByCode(couponReq.getCode());

			Cart cart = getCartById(couponReq.getId());
			double couponAmount = 0, couponPar = 0, discountAmount = 0;

			if (cart != null && coupon != null) {

				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();
					Cart dbCart = session.get(Cart.class, cart.getId());

					if (!coupon.isValid()) {
						throw new Exception("Coupon is In-Valid");
					}

					if (!coupon.isActive()) {
						throw new Exception("Coupon usage exited");
					}

					Date date = new Date();
					if (dateServices.isGreater(coupon.getExpireDate(), date)) {
						throw new Exception("Coupon is Expireed");
					}

					// Check Amount ant Quantity
					if (coupon.getApplyAmount() < 0 && coupon.getApplyQty() < 0) {
						couponAmount = coupon.getAmount();
						couponPar = coupon.getPercentage();

					} else if (coupon.getApplyAmount() < 0 && coupon.getApplyQty() > 0) {

						if (cart.getChooseQty() >= coupon.getApplyQty()) {
							couponAmount = coupon.getAmount();
							couponPar = coupon.getPercentage();
						}

					} else if (coupon.getApplyAmount() > 0 && coupon.getApplyQty() < 0) {
						if (cart.getChooseAmount() >= coupon.getApplyAmount()) {
							couponAmount = coupon.getAmount();
							couponPar = coupon.getPercentage();
						}
					}

					if (couponAmount > 0) {
						discountAmount = couponAmount;
					} else {

						if (couponPar > 0) {

							discountAmount = converterServices.getAmountUsingPercentageTotal(couponPar,
									cart.getChooseAmount());
						}
					}

					if (discountAmount > 0) {

						dbCart.setCouponCode(couponReq.getCode());
						dbCart.setCouponDiscount(discountAmount);
						dbCart.setCouponPar(couponPar);
						dbCart.setGrandTotal(dbCart.getChooseAmount() - discountAmount);

					} else {
						dbCart.setCouponCode(couponReq.getCode());
						dbCart.setCouponDiscount(0);
						dbCart.setCouponPar(0);
						dbCart.setGrandTotal(dbCart.getChooseAmount());
					}

					session.merge(dbCart);

					map.put("response", cartMapper.mapRespCart(dbCart));

					transaction.commit();
					session.clear();
					map.put("status", true);
					map.put("message", " Coupon Applyed");
				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					map.put("status", false);
					map.put("message", e.getMessage());
					e.printStackTrace();
					if (cart != null) {
						restCartCoupon(cart.getId());
					}

				}

			}

		}

	}

	@Override
	public Cart getForOrderPlace(String id) {

		Cart cart = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Cart> criteriaQuery = criteriaBuilder.createQuery(Cart.class);
			Root<Cart> root = criteriaQuery.from(Cart.class);

			CriteriaQuery<Cart> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Cart> query = session.createQuery(select);
			cart = query.getSingleResult();

			if (cart != null) {

				if (cart.getCartItems() != null) {
					for (CartItem cartItem : cart.getCartItems()) {
						if (cartItem.getVariant() != null) {
							cartItem.getVariant().getId();
						}
					}
				}

			}

			cart.getStakeholder();

		} catch (Exception e) {
			log.info("getOrdersByStatus " + e.getMessage());
			e.printStackTrace();
		}

		session.clear();
		session.close();

		return cart;
	}

	private void restCartCoupon(int id) {

		if (id > 0) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();
				Cart dbCart = session.get(Cart.class, id);

				dbCart.setCouponDiscount(0);
				dbCart.setCouponPar(0);
				dbCart.setGrandTotal(dbCart.getChooseAmount());
				session.merge(dbCart);
				transaction.commit();
				session.clear();

			} catch (Exception e) {
				e.printStackTrace();
			}

			session.close();
		}

	}

}
