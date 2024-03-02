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
import com.altqart.model.EsCredit;
import com.altqart.model.EsCreditDetail;
import com.altqart.model.EsDebit;
import com.altqart.model.EsDebitDetail;
import com.altqart.model.MethodAndTransaction;
import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.Product;
import com.altqart.model.Stakeholder;
import com.altqart.model.Status;
import com.altqart.model.Store;
import com.altqart.model.TransactionRecord;
import com.altqart.model.User;
import com.altqart.repository.OrderRepository;
import com.altqart.req.model.ReqDate;
import com.altqart.resp.model.RespOrder;
import com.altqart.resp.model.RespStore;
import com.altqart.services.BankAccountServices;
import com.altqart.services.OrderServices;
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

	private User user;

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
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
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
	public List<RespOrder> getAllOrderDelivered(int startAt, int pageSize) {

		return getOrderByStatus(3, startAt, pageSize);
	}

	@Override
	public List<RespOrder> getAllOrderPending(int startAt, int pageSize) {
		return getOrderByStatus(1, startAt, pageSize);
	}

	@Override
	public List<RespOrder> getAllProcessesOrder(int startAt, int pageSize) {

		return getOrderByStatus(2, startAt, pageSize);
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
	public boolean saveOrderViaAPI(Order order) {

		boolean status = false;
		log.info("Save Order Via API !!");

		if (order != null) {
//			if (order.getStore() == null) {
//				order.setStore(StoreServices.getStoreByPublicId(order.getStoreId()));
//			}
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				Date date = new Date();

				if (order.getDate() == null) {
					order.setDate(date);
					order.setGrupeDate(date);
				}
				transaction = session.beginTransaction();

				order.setStatus(session.get(Status.class, 1));

				order.setPublicId(helperServices.getGenPublicId());

				session.persist(order);

				if (order.getMethodTransactions() != null) {
					for (MethodAndTransaction methodTransaction : order.getMethodTransactions()) {
						methodTransaction.setOrder(order);
						session.persist(methodTransaction);
					}
				}
				transaction.commit();

				status = true;

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

		return status;
	}

	@Override
	public Map<String, Object> approveOrder(Order order, int i) {
		user = helperAuthenticationServices.getCurrentUser();
		boolean status = false;
		Map<String, Object> approve = new HashMap<>();
		String message = null;
		if (order != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Order dbOrder = session.get(Order.class, order.getId());
				Status Ordertatus = session.get(Status.class, i);

				Date date = dbOrder.getDate();

				Stakeholder dbCustomer = null;

				if (dbOrder != null) {

					// Stakeholder Credit and Debit
					if (dbOrder.getStakeholder() != null) {

						dbCustomer = session.get(Stakeholder.class, dbOrder.getStakeholder().getId());
					}

					if (dbCustomer == null) {// Unknown Customer Sale Or Not Exist Customer Start

						// Unknown Customer Sale Or Not Exist Customer End

					} else {// Exist Customer Or Known Customer Sale Calculation Start

						double nCustomerTotalDebit = 0, nCustometTotalCredit = 0, prevCredit = 0, prevDebit = 0;

						if (dbCustomer.getTotalDebitAmount() != null) {
							prevDebit = dbCustomer.getTotalDebitAmount().doubleValue();
//							nCustomerTotalDebit = dbCustomer.getTotalDebitAmount().doubleValue()
//									+ dbOrder.getNetSaleAmount();
						} else {
//							nCustomerTotalDebit = dbOrder.getNetSaleAmount();
						}

						if (dbCustomer.getTotalCreditAmount() != null) {
							prevCredit = dbCustomer.getTotalCreditAmount().doubleValue();
//							nCustometTotalCredit = dbCustomer.getTotalCreditAmount().doubleValue()
//									+ dbOrder.getPayAmount();
						} else {
//							nCustometTotalCredit = dbOrder.getPayAmount();
						}

						dbCustomer.setTotalCreditAmount(converterServices.getDoubleToBigDec(nCustometTotalCredit));
						dbCustomer.setTotalDebitAmount(converterServices.getDoubleToBigDec(nCustomerTotalDebit));

						if (nCustomerTotalDebit > nCustometTotalCredit) {
//							dbOrder.setTotalDebit(nCustomerTotalDebit - nCustometTotalCredit);
						} else {
//							dbOrder.setTotalCredit(nCustometTotalCredit - nCustomerTotalDebit);
						}

						if (prevCredit > prevDebit) {
							prevCredit = prevCredit - prevDebit;
							prevDebit = 0;

						} else {
							prevDebit = prevDebit - prevCredit;
							prevCredit = 0;
						}

						double netGrandTotal = 0;
						log.info("Prev Cr " + prevCredit + " Prev Dr " + prevDebit + " Grand Total "
								+ dbOrder.getGrandTotal());
						if (prevCredit > 0) {
							netGrandTotal = dbOrder.getGrandTotal() - prevCredit;
						} else if (prevDebit > 0) {
							netGrandTotal = dbOrder.getGrandTotal() + prevDebit;
						}

						log.info("Order Net Grand Total: " + netGrandTotal);

						dbOrder.setNetGrandTotal(netGrandTotal);

						if (dbCustomer.getDebit() != null) {
//							stkDebit = session.get(EsDebit.class, dbCustomer.getDebit().getId());
						}

						if (dbCustomer.getCredit() != null) {
//							stkCredit = session.get(EsCredit.class, dbCustomer.getCredit().getId());
						}

						// Calculating Sale Account Credit to Stakholder Debit Start

//						if (stkDebit != null) {
//							double nStkDebitAmount = stkDebit.getAmount() + dbOrder.getNetSaleAmount();
//							stkDebit.setAmount(nStkDebitAmount);
//							session.update(stkDebit);
//						} else {
//							stkDebit = esInitializerServices.initEsDebitByStakeholder(dbCustomer, date);
//							stkDebit.setAmount(dbOrder.getNetSaleAmount());
//							session.persist(stkDebit);
//						}
						// Calculating Sale Account Credit to Stakholder Debit End

						// Stakeholder Debit & Sale Credit Not Null Start
//						if (stkDebit != null && saleAccountCredit != null) {
//
//							insertBasicSaleAndCustomerRecord(session, dbOrder, date, saleAccountCredit, dbCustomer,
//									stkDebit, saleCreditRecord);
//							// Stakeholder Debit & Sale Credit Not Null Start
//						} else {
//							throw new Exception("Customer Debit & Sale Credit Not found !!");
//						}

//						if (dbOrder.getPayAmount() > 0) {// If Paid Any amount
//
//							cashDebit = initCashKnownCustomer(session, dbOrder, date, dbStore, cashAccount,
//									cashDebit);
//
//							if (stkCredit != null) {
//								double nStkCreditAmount = stkCredit.getAmount() + dbOrder.getPayAmount();
//								stkCredit.setAmount(nStkCreditAmount);
//								session.update(stkCredit);
//							} else {
//								stkCredit = esInitializerServices.initEsCreditByStakeholder(dbCustomer, date);
//								stkCredit.setAmount(dbOrder.getPayAmount());
//								session.persist(stkCredit);
//							}
//
//							if (stkCredit != null && cashDebit != null) {
//
//								knownCustomerSaleRecord(session, dbOrder, date, dbCustomer, stkCredit, cashDebit);
//							} else {
//								throw new Exception("Customer Paid Credit & Cash Debit not found");
//							}
//
//						}

						//
//						TransactionRecord saleTransactionRecord = esInitializerServices
//								.initSaleTransactionRecord(saleAccount, stkDebit, date);
//						saleTransactionRecord.setAmount(dbOrder.getNetSaleAmount());
//						saleTransactionRecord.setNarration("Using Sale Inv. No. " + dbOrder.getInvNo());
//						session.persist(saleTransactionRecord);

						session.update(dbCustomer);
					}
					// Exist Customer Or Known Customer Sale Calculation End
					// Update Stock Status
					// Stock Status End

					// Capital Update using sale profit and loss start

					// Capital Update using sale profit and loss end

					// Bank Transaction If Have
					bankAccountServices.updateHaveAnyAccountTransactions(dbOrder.getMethodTransactions(), session,
							date);
					session.update(dbOrder);

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
				e.printStackTrace();
				log.info("Sale Approve " + e.getMessage());
				message = e.getMessage();
			}

			session.close();
		}

		approve.put("status", status);
		approve.put("message", message);

		return approve;

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
				orderItem.getProduct();

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

			select.where(criteriaBuilder.equal(root.get("status").get("id"), staus));
			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Order> typedQuery = session.createQuery(select);
			typedQuery.setFirstResult(offset);
			typedQuery.setMaxResults(pageSize);

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
