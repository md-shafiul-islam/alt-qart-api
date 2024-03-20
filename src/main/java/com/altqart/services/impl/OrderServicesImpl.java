package com.altqart.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperDateServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.mapper.SaleMapper;
import com.altqart.mapper.StoreMapper;
import com.altqart.model.Address;
import com.altqart.model.Cart;
import com.altqart.model.CartItem;
import com.altqart.model.MethodAndTransaction;
import com.altqart.model.NamePhoneNo;
import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.Parcel;
import com.altqart.model.ParcelProvider;
import com.altqart.model.ShippingAddress;
import com.altqart.model.Status;
import com.altqart.model.User;
import com.altqart.model.Variant;
import com.altqart.repository.OrderRepository;
import com.altqart.repository.StatusRepository;
import com.altqart.req.model.OrderPlaceReq;
import com.altqart.req.model.ReqDate;
import com.altqart.resp.model.RespMinOrder;
import com.altqart.resp.model.RespOrder;
import com.altqart.resp.model.RespStore;
import com.altqart.services.BankAccountServices;
import com.altqart.services.CartServices;
import com.altqart.services.OrderServices;
import com.altqart.services.ParcelProviderServices;
import com.altqart.services.ParcelServices;
import com.altqart.services.StakeholderServices;
import com.altqart.services.StatusServices;
import com.altqart.services.StoreServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServicesImpl implements OrderServices {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartServices cartServices;

	@Autowired
	private StatusServices statusServices;

	@Autowired
	private HelperDateServices dateServices;

	@Autowired
	private StakeholderServices stakeholderServices;

	@Autowired
	private HelperAuthenticationServices helperAuthenticationServices;

	private SessionFactory sessionFactory;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperDateServices helperDateServices;

	@Autowired
	private StoreServices StoreServices;

	@Autowired
	private SaleMapper saleMapper;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private EsInitializerServices esInitializerServices;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private BankAccountServices bankAccountServices;

	@Autowired
	private ParcelProviderServices parcelProviderServices;

	@Autowired
	private ParcelServices parcelServices;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespOrder> getAll(int start, int size) {

		return getOrderByStatus(-1, start, size);
	}

	@Override
	public List<RespMinOrder> getAllMin(int start, int size) {
		return getRepMinOrderByStatus(-1, start, size);
	}

	@Override
	public Order getOrderByPublicId(String id) {

		Optional<Order> optional = orderRepository.getOrderByPublicId(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Order getOrderById(int id) {

		Optional<Order> dbOrder = orderRepository.findById(id);
		if (dbOrder.isPresent() && !dbOrder.isEmpty()) {
			return dbOrder.get();
		}
		return null;
	}

	@Override
	public List<RespMinOrder> getAllOrderPending(int startAt, int pageSize) {
		return getRepMinOrderByStatus(1, startAt, pageSize);
	}

	@Override
	public List<RespMinOrder> getAllProcessesOrder(int startAt, int pageSize) {

		return getRepMinOrderByStatus(2, startAt, pageSize);
	}

	@Override
	public List<RespMinOrder> getAllOrderShipped(int start, int size) {
		return getRepMinOrderByStatus(3, start, size);
	}

	@Override
	public List<RespMinOrder> getAllOrderDelivered(int startAt, int pageSize) {

		return getRepMinOrderByStatus(4, startAt, pageSize);
	}

	@Override
	public List<RespMinOrder> getAllOrderCanceled(int start, int size) {
		return getRepMinOrderByStatus(5, start, size);
	}

	@Override
	public List<RespMinOrder> getAllOrderFailedDelivery(int start, int size) {
		return getRepMinOrderByStatus(6, start, size);
	}

	@Override
	public List<RespMinOrder> getAllOrderReturns(int start, int size) {
		return getRepMinOrderByStatus(7, start, size);
	}

	@Override
	public RespOrder getOrderDetailsByPublicId(String id) {

		Order order = getOrderByPublicId(id);
		RespOrder respOrder = null;

		if (order == null) {
			return null;
		}
		Session session = sessionFactory.openSession();

		try {

			Order dbOrder = session.get(Order.class, order.getId());

			if (dbOrder != null) {

				respOrder = saleMapper.mapOrderDetails(dbOrder);
			}

		} catch (Exception e) {

			log.info("getOrderDetailsByPublicId " + e.getMessage());

		}

		session.close();

		return respOrder;

	}

	@Override
	public void getSaleInvoiceDetailsByPublicId(String id, Map<String, Object> map) {

		Order order = getOrderByPublicId(id);
		RespOrder respOrder = null;

		Session session = sessionFactory.openSession();

		try {

			if (order == null) {
				throw new Exception("Sale invoice not found by ID");
			}

			Order dbOrder = session.get(Order.class, order.getId());

			if (dbOrder != null) {

				respOrder = saleMapper.mapSaleInvoiceDetails(dbOrder);

				RespStore respStore = storeMapper.mapRespStore((dbOrder.getStore()));

				Map<String, Object> response = new HashMap<>();
				response.put("sale", respOrder);
				response.put("store", respStore);

				map.put("response", response);
				map.put("message", "Sale Invoice found");
				map.put("status", true);

			} else {
				throw new Exception("Sale invoice not found by ID");
			}
			session.clear();
		} catch (Exception e) {

			log.info("getOrderDetailsByPublicId " + e.getMessage());
			map.put("message", e.getMessage());
			map.put("status", false);

		}

		session.close();

	}

	@Override
	public void getOrderPlace(OrderPlaceReq orderPlaceReq, Map<String, Object> map) {

		boolean status = true;

		if (orderPlaceReq != null) {
			Order order = saleMapper.mapPlaceOrder(orderPlaceReq);

			Cart cart = cartServices.getCartById(orderPlaceReq.getCart());
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				if (cart == null) {
					throw new Exception("Cart not found, Order place failed. Please try again");
				}

				if (order == null) {
					throw new Exception("Corrupted, Order place failed. Please try again");
				}

				Date date = new Date();

				if (order.getDate() == null) {
					order.setDate(date);
					order.setGrupeDate(date);
				}

				order.setStatus(session.get(Status.class, 1));

				order.setStakeholder(cart.getStakeholder());

				session.persist(order);

				if (order.getMethodTransactions() != null) {
					for (MethodAndTransaction methodTransaction : order.getMethodTransactions()) {
						methodTransaction.setOrder(order);
						session.persist(methodTransaction);
					}
				}

				if (order.getOrderItems() != null) {

					for (OrderItem item : order.getOrderItems()) {
						item.setDate(date);
						item.setGroupDate(date);
						item.setOrder(order);

						session.persist(item);
					}
				}

				if (order.getShippingAddress() != null) {
					ShippingAddress shippingAddress = order.getShippingAddress();
					shippingAddress.setOrder(order);
					order.setShippingAddress(shippingAddress);
					session.persist(shippingAddress);
				}

				if (cart != null) {
					Cart dbCart = session.get(Cart.class, cart.getId());
					List<CartItem> nCatCartItems = new ArrayList<>();

					double totalAmount = 0, totalQty = 0;
					if (dbCart.getCartItems() != null) {

						for (CartItem cartItem : dbCart.getCartItems()) {

							if (cartItem.isChoose()) {

								CartItem dbCartItem = session.get(CartItem.class, cartItem.getId());
								session.remove(dbCartItem);
							} else {
								nCatCartItems.add(cartItem);

								double price = cartItem.getDiscountPrice() > 0 ? cartItem.getDiscountPrice()
										: cartItem.getPrice();
								totalAmount += price;
								totalQty += cartItem.getQty();

							}
						}
					}

					dbCart.setCartItems(nCatCartItems);
					dbCart.setChooseAmount(0);
					dbCart.setChooseQty(0);
					dbCart.setCouponDiscount(0);
					dbCart.setCouponCode("");
					dbCart.setCouponPar(0);
					dbCart.setDiscount(0);
					dbCart.setTotalAmount(totalAmount);
					dbCart.setTotalQty(totalQty);

					session.merge(dbCart);
				}

				transaction.commit();
				status = true;
				map.put("response", order.getInvNo());
				map.put("message", "Order Place successfully");
			} catch (Exception e) {
				if (transaction != null) {
					log.info("Order Roll Back !!");
					transaction.rollback();
				}
				e.printStackTrace();
				status = false;

			}

			session.clear();
			session.close();
		}

		map.put("status", status);

	}

	@Override
	public Map<String, Object> approveOrder(String orderId) {

		User user = helperAuthenticationServices.getCurrentUser();
		ParcelProvider parcelProvider = parcelProviderServices.getParcelProviderByKey("pathao");
		Parcel parcel = null;
		boolean status = false;

		Map<String, Object> approve = new HashMap<>();
		String message = null;
		Order order = getOrderByPublicId(orderId);
		Date date = new Date();
		if (orderId != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Order dbOrder = session.get(Order.class, order.getId());

				User dbUser = session.get(User.class, user.getId());

				if (dbUser == null) {
					throw new Exception("User Not found, Please, contact administrator");
				}
				if (dbOrder != null) {

					if (dbOrder.getStatus() != null) {

						if (dbOrder.getStatus().getId() == 1) {

							Status ordertatus = session.get(Status.class, 2);
							dbOrder.setStatus(ordertatus);

						} else {
							throw new Exception("Order Already Approved Or Shipped :)");
						}
					}

					dbOrder.setUser(user);

					session.merge(dbOrder);

					updateProductStockByVariant(dbOrder.getOrderItems(), session);

				}

				Address address = null;
				if (dbOrder.getShippingAddress() != null) {

					if (dbOrder.getShippingAddress().getAddress() != null) {
						address = dbOrder.getShippingAddress().getAddress();

					}
				}

				if (address == null) {
					throw new Exception("Order Address not found");
				}

				parcel = new Parcel();
				parcel.setAmountToCollect((int) dbOrder.getCodAmount());

				parcel.setDate(date);
				parcel.setDateGroup(date);

				String itemDescription = "";
				double totalWeight = 0;
				for (OrderItem orderItem : dbOrder.getOrderItems()) {
					String name = "";
					if (orderItem.getVariant() != null) {
						if (orderItem.getVariant().getProduct() != null) {
							name = orderItem.getVariant().getProduct().getTitle();
							if (orderItem.getVariant().getProduct().getMeasurement() != null) {
								totalWeight += orderItem.getVariant().getProduct().getMeasurement().getWeight();
							}

						}
					}

					itemDescription += "Title: " + name + " Qty: " + orderItem.getQty() + " Price: "
							+ orderItem.getPrice() + " Item Total:" + orderItem.getSubTotal() + "\n";
				}

				itemDescription += "Total Quantity: " + dbOrder.getTotalQty() + " Total Price: "
						+ dbOrder.getSubTotal();
				parcel.setItemDescription(itemDescription);
				parcel.setItemQuantity(dbOrder.getTotalQty());
				parcel.setItemWeight(totalWeight);
				parcel.setMerchantOrderId(dbOrder.getInvNo());
				parcel.setOrder(dbOrder);
				parcel.setParcelProvider(parcelProvider);
				parcel.setRecipientAddress(address.getFullAddress() + " (Phone No2.): " + address.getPhoneNo2());
				parcel.setRecipientArea(address.getArea().getPathaoCode());
				parcel.setRecipientCity(address.getCity().getPathaoCode());
				parcel.setRecipientName(address.getFullName());
				parcel.setRecipientPhone(address.getPhoneNo());
				parcel.setRecipientZone(address.getZone().getPathaoCode());
				parcel.setSenderName(user.getStore().getName());
				parcel.setPublicId(helperServices.getGenPublicId());
				parcel.setItemType(2);
				String senderPhone = "01602507785";
				int count = 0;
				if (dbUser.getStore().getNamePhoneNos() != null) {

					for (NamePhoneNo namePhone : dbUser.getStore().getNamePhoneNos()) {
						if (namePhone != null) {

							if (count == 0) {
								senderPhone = namePhone.getPhoneNo();
							}
						}
					}

				}

				parcel.setSenderPhone(senderPhone);
				parcel.setSpecialInstruction(dbOrder.getShippingAddress().getNote());
				parcel.setStoreId(dbUser.getStore().getPathaoId());
				parcel.setParcelProvider(parcelProvider);
				session.persist(parcel);

				if (parcel != null) {
					dbOrder.setParcelCreate(true);
					session.merge(dbOrder);
				}

				transaction.commit();
				session.clear();

				status = true;
				message = "Sale Approve & Stock Calculate Success";
			} catch (Exception e) {
				status = false;
				if (transaction != null) {
					transaction.rollback();
				}

				log.info("Sale Approve failed " + e.getMessage());

				e.printStackTrace();

				message = e.getMessage();
			}

			session.close();
		}

		approve.put("status", status);
		approve.put("message", message + " Parcel Create Failed");
		if (parcel != null) {
			parcelServices.createPathaoParcel(parcel, approve);
		}

		return approve;

	}

	@Override
	public void updateOrderStatus(String id, String statusKey, Map<String, Object> map) {

		Status status = getStatusByKey(statusKey);

		if (status != null && !helperServices.isNullOrEmpty(id)) {

			Order order = getOrderByPublicId(id);

			if (order != null) {
				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {

					transaction = session.beginTransaction();

					Status dbStatus = session.get(Status.class, status.getId());
					Order dbOrder = session.get(Order.class, order.getId());

					if (helperServices.isEqual(statusKey, "cancel") && dbOrder.getStatus().getId() != 1) {
						throw new Exception("This Order cant't cancel please contact administrator");
					}

					dbOrder.setStatus(dbStatus);

					session.merge(dbOrder);

					transaction.commit();

					map.put("status", true);
					map.put("message", "Update Order to " + dbStatus.getName());
					map.put("response", null);

				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					e.printStackTrace();
					map.put("status", false);
					map.put("message", "Update Order Status failed " + e.getMessage());
				}
			}
		}

	}

	private Status getStatusByKey(String statusKey) {

		Optional<Status> optional = statusRepository.getStatusByValue(statusKey);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public void getSaleReturnApprove(String id, Map<String, Object> map) {

		Order order = getOrderByPublicId(id);

		if (order != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Order dbOrder = session.get(Order.class, order.getId());

				if (dbOrder.getStatus() != null) {
					if (helperServices.isEqual(dbOrder.getStatus().getValue(), "return")) {
						dbOrder.setReturnStatus(1);

						updateOrderStockViaReturn(dbOrder.getOrderItems(), session);
					}
				}

				session.merge(dbOrder);

				transaction.commit();

				session.clear();

				map.put("status", true);
				map.put("message", "Order Return Approved");
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				e.printStackTrace();

				map.put("status", false);
				map.put("message", e.getMessage());
			}

			session.close();
		}

	}

	@Override
	public void getOrderFaileldDeliveryApprove(String id, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	private void updateOrderStockViaReturn(List<OrderItem> orderItems, Session session) throws Exception {

		if (orderItems != null) {

			for (OrderItem orderItem : orderItems) {

				Variant dbVariant = session.get(Variant.class, orderItem.getVariant().getId());

				if (orderItem.isReturn()) {
					if (orderItem.getQty() <= orderItem.getReturnQty()) {
						dbVariant.setQty(dbVariant.getQty() + orderItem.getReturnQty());
					} else {
						throw new Exception("Returnt Order item Quntity Not Match");
					}

				}

				session.merge(dbVariant);

			}
		}

	}

	private void updateProductStockByVariant(List<OrderItem> orderItems, Session session) {

		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {

				if (orderItem != null) {

					if (orderItem.getVariant() != null) {
						Variant dbVariant = session.get(Variant.class, orderItem.getVariant().getId());

						if (orderItem.getQty() > 0) {
							dbVariant.setQty(dbVariant.getQty() - orderItem.getQty());
						}

						session.merge(dbVariant);
					}

				}

			}
		}

	}

	@Override
	public List<RespOrder> getThisMonthOrAnyOrderByStatus(int status, Date date) {
		List<RespOrder> Order = new ArrayList<>();

		Session session = sessionFactory.openSession();
		try {
			Calendar calender = helperDateServices.getCalenderByDate(date);

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

			Root<Order> root = criteriaQuery.from(Order.class);
			ParameterExpression<Integer> monthExp = criteriaBuilder.parameter(Integer.class);
			ParameterExpression<Integer> yearExp = criteriaBuilder.parameter(Integer.class);

			criteriaQuery.where(criteriaBuilder.and(
					criteriaBuilder.equal(monthExp,
							criteriaBuilder.function("month", Integer.class, root.get("grupeDate"))),
					criteriaBuilder.equal(yearExp,
							criteriaBuilder.function("year", Integer.class, root.get("grupeDate")))));

			int month = calender.get(Calendar.MONTH) + 1;
			log.info("This Get Current Month " + month + " And Year " + calender.get(Calendar.YEAR));
			TypedQuery<Order> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setParameter(yearExp, calender.get(Calendar.YEAR));
			typedQuery.setParameter(monthExp, month);

			List<Order> lOrder = typedQuery.getResultList();

			if (lOrder != null) {
				log.info("Order By This Month Size " + lOrder.size());

				Order = saleMapper.mapAllOrderByStatus(lOrder, status);

			} else {
				System.out.println("Get Order Not found ");
			}

		} catch (Exception e) {
			log.info("getThisMonthOrAnyOrderByStatus " + e.getMessage());
//			e.printStackTrace();
		}
		session.clear();
		session.close();
		return Order;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RespOrder> getOrderByBetweenDateAndStatus(ReqDate reqDate) {

		List<RespOrder> Order = new ArrayList<>();

		if (reqDate != null) {
			Session session = sessionFactory.openSession();
			try {

				CriteriaBuilder cb = session.getCriteriaBuilder();
				CriteriaQuery<Order> query = cb.createQuery(Order.class);
				Root<Order> root = query.from(Order.class);

				CriteriaQuery<Order> select = query.select(root);

				select.where(cb.between(root.get("grupeDate"), reqDate.getDate(), reqDate.getEndDate()));

				TypedQuery<Order> typedQuery = session.createQuery(select);

				List<Order> lOrder = typedQuery.getResultList();

				if (lOrder != null) {

					Order = saleMapper.mapAllOrderByStatus(lOrder, reqDate.getStatus());

				} else {
					log.info("Get Order Not found ");
				}

			} catch (Exception e) {

				log.info("getOrderByBetweenDateAndStatus " + e.getMessage());
//				e.printStackTrace();
			}
			session.clear();
			session.close();
		}

		return Order;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RespOrder> getOrderByMonthAndStatus(int status, Date date) {
		List<RespOrder> Order = new ArrayList<>();

		Session session = sessionFactory.openSession();
		try {
			Calendar calender = helperDateServices.getCalenderByDate(date);

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

			Root<Order> root = criteriaQuery.from(Order.class);
			ParameterExpression<Integer> monthExp = criteriaBuilder.parameter(Integer.class);
			ParameterExpression<Integer> yearExp = criteriaBuilder.parameter(Integer.class);

			criteriaQuery.where(criteriaBuilder.and(
					criteriaBuilder.equal(monthExp,
							criteriaBuilder.function("month", Integer.class, root.get("grupeDate"))),
					criteriaBuilder.equal(yearExp,
							criteriaBuilder.function("year", Integer.class, root.get("grupeDate")))));

			int month = calender.get(Calendar.MONTH) + 1;
			log.info("Get By Month " + month + " And Year " + calender.get(Calendar.YEAR));
			TypedQuery<Order> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setParameter(yearExp, calender.get(Calendar.YEAR));
			typedQuery.setParameter(monthExp, month);

			List<Order> lOrder = typedQuery.getResultList();

			if (lOrder != null) {
				log.info("Order By Month Size " + lOrder.size());

				Order = saleMapper.mapAllOrderByStatus(lOrder, status);

			} else {
				System.out.println("Get Order Not found ");
			}

		} catch (Exception e) {

			log.info("getOrderByMonthAndStatus " + e.getMessage());
//			e.printStackTrace();
		}
		session.clear();
		session.close();
		return Order;
	}

	@Override
	public List<RespOrder> getThisDayOrAnyOrderByStatus(int status, Date date) {

		List<RespOrder> Order = new ArrayList<>();

		Session session = sessionFactory.openSession();
		try {
			Calendar calender = helperDateServices.getCalenderByDate(date);

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

			Root<Order> root = criteriaQuery.from(Order.class);

			criteriaQuery.where(criteriaBuilder.equal(root.get("grupeDate"), calender.getTime()));

			TypedQuery<Order> typedQuery = session.createQuery(criteriaQuery);

			List<Order> lOrder = typedQuery.getResultList();

			if (lOrder != null) {
				log.info("Order By Month Size " + lOrder.size());

				Order = saleMapper.mapAllOrderByStatus(lOrder, status);

			} else {
				System.out.println("Get Order Not found ");
			}

		} catch (Exception e) {

			log.info("getThisDayOrAnyOrderByStatus " + e.getMessage());
			e.printStackTrace();
		}
		session.clear();
		session.close();
		return Order;
	}

	@Override
	public int getThisDayOrderCount(Date date) {
		int size = 0;

		Session session = sessionFactory.openSession();
		try {
			Calendar calender = helperDateServices.getCalenderByDate(date);

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

			Root<Order> root = criteriaQuery.from(Order.class);

			criteriaQuery.where(criteriaBuilder.equal(root.get("grupeDate"), calender.getTime()));

			TypedQuery<Order> typedQuery = session.createQuery(criteriaQuery);

			List<Order> lOrder = typedQuery.getResultList();
			if (lOrder != null) {
				size = lOrder.size();
			}

		} catch (Exception e) {

			log.info("getThisDayOrderCount " + e.getMessage());
//			e.printStackTrace();
		}
		session.clear();
		session.close();
		return size;
	}

	@Override
	public List<RespOrder> getThisOrAnyWeekOrderByStatus(int status, Date date) {

		List<RespOrder> Order = new ArrayList<>();

		Session session = sessionFactory.openSession();
		try {
			if (date == null) {
				date = new Date();
			}

			Date weekStartDate = helperDateServices.getWeekFirstDate(date);
			Date weekLastDate = helperDateServices.getWeekLastDate(date);

			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Order> query = cb.createQuery(Order.class);
			Root<Order> root = query.from(Order.class);

			CriteriaQuery<Order> select = query.select(root);

			select.where(cb.between(root.get("grupeDate"), weekStartDate, weekLastDate));

			TypedQuery<Order> typedQuery = session.createQuery(select);

			List<Order> orderList = typedQuery.getResultList();

			if (orderList != null) {
				log.info("Order By Month Size " + orderList.size());

				Order = saleMapper.mapAllOrderByStatus(orderList, status);

			} else {
				System.out.println("Get Order Not found ");
			}

		} catch (Exception e) {

			log.info("getThisOrAnyWeekOrderByStatus " + e.getMessage());
//			e.printStackTrace();
		}
		session.clear();
		session.close();

		return Order;

	}

	@Override
	public OrderItem getOrderItemById(int id) {
		OrderItem orderItem = null;

		Session session = sessionFactory.openSession();
		try {

			orderItem = session.get(OrderItem.class, id);

			if (orderItem != null) {
				orderItem.getVariant();

			} else {
				System.out.println("Get Order Item By ID");
			}

		} catch (Exception e) {

			log.info("getOrderItemById " + e.getMessage());
			e.printStackTrace();
		}
		session.clear();
		session.close();

		return orderItem;
	}

	@Override
	public long getSaleCount() {

		return orderRepository.count();
	}

	// Private Start

	/**
	 * @param offset   0
	 * @param pageSize 10
	 * @param isAdmin
	 * @param status
	 * @return
	 */
	private List<RespOrder> getOrderByStatus(int staus, int offset, int pageSize) {

		pageSize = pageSize > 0 ? pageSize : 500;

		List<Order> Order = null;
		List<RespOrder> respOrder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
			Root<Order> root = criteriaQuery.from(Order.class);

			CriteriaQuery<Order> select = criteriaQuery.select(root);

			if (staus >= 0) {
				select.where(criteriaBuilder.equal(root.get("status").get("id"), staus));
			}

			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Order> typedQuery = session.createQuery(select);

			if (pageSize > 0) {
				typedQuery.setFirstResult(offset);
				typedQuery.setMaxResults(pageSize);
			}

			Order = typedQuery.getResultList();

			if (Order != null) {

				respOrder = saleMapper.mapAllOrder(Order);

			} else {
				System.out.println("Get Order Not found ");
			}

		} catch (Exception e) {
			log.info("getOrderByStatus " + e.getMessage());
		}

		session.clear();
		session.close();
		return respOrder;
	}

	/**
	 * @param offset   0
	 * @param pageSize 10
	 * @param isAdmin
	 * @param status
	 * @return
	 */
	private List<RespMinOrder> getRepMinOrderByStatus(int staus, int offset, int pageSize) {

		pageSize = pageSize > 0 ? pageSize : 500;

		List<Order> orders = null;
		List<RespMinOrder> respOrders = null;

		Session session = sessionFactory.openSession();
		
		log.info("getRepMinOrderByStatus "+ staus);


		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
			Root<Order> root = criteriaQuery.from(Order.class);

			CriteriaQuery<Order> select = criteriaQuery.select(root);

			if (staus >= 0) {
				select.where(criteriaBuilder.equal(root.get("status").get("id"), staus));
			}

			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Order> typedQuery = session.createQuery(select);

			if (pageSize > 0) {
				typedQuery.setFirstResult(offset);
				typedQuery.setMaxResults(pageSize);
			}

			orders = typedQuery.getResultList();

			if (orders != null) {

				log.info("Order By Status Befor Map Size: " + orders.size());

				respOrders = saleMapper.mapAllRespMinOrder(orders);

			} else {
				System.out.println("Get Order Not found ");

			}

		} catch (Exception e) {
			log.info("getOrderByStatus " + e.getMessage());
			e.printStackTrace();
		}

		session.clear();
		session.close();
		return respOrders;
	}

	@Override
	public List<Status> getAllOrdertatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status getOrdertatusById(int statusId) {
		// TODO Auto-generated method stub
		return null;
	}

}
