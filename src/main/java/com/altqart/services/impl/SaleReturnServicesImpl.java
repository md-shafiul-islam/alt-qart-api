package com.altqart.services.impl;

import java.util.ArrayList;
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

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperConverterServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.mapper.SaleReturnInvoiceMapper;
import com.altqart.model.EsCredit;
import com.altqart.model.EsCreditDetail;
import com.altqart.model.EsDebit;
import com.altqart.model.EsDebitDetail;
import com.altqart.model.MethodAndTransaction;
import com.altqart.model.Product;
import com.altqart.model.ReturnType;
import com.altqart.model.SaleReturnInvoice;
import com.altqart.model.SaleReturnItem;
import com.altqart.model.Stakeholder;
import com.altqart.model.Store;
import com.altqart.model.User;
import com.altqart.repository.SaleReturnInvoiceRepository;
import com.altqart.req.model.EsApprove;
import com.altqart.req.model.SaleReturnReq;
import com.altqart.resp.model.RespSaleReturn;
import com.altqart.services.BankAccountServices;
import com.altqart.services.SaleReturnServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SaleReturnServicesImpl implements SaleReturnServices {

	@Autowired
	private SaleReturnInvoiceRepository saleReturnInvoiceRepository;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private SaleReturnInvoiceMapper saleReturnMapper;

	@Autowired
	private EsInitializerServices esInitializerServices;

	@Autowired
	private HelperConverterServices converterServices;

	private SessionFactory sessionFactory;

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
	public List<RespSaleReturn> getAllSaleRetun(int start, int size) {

		return getAllSaleReturnByStatus(start, size, 1);
	}

	@Override
	public List<RespSaleReturn> getAllPendingSaleRetun(int start, int size) {

		return getAllSaleReturnByStatus(start, size, 0);
	}

	@Override
	public List<RespSaleReturn> getAllRejectedSaleRetun(int start, int size) {
		return getAllSaleReturnByStatus(start, size, 2);
	}

	private List<RespSaleReturn> getAllSaleReturnByStatus(int start, int pageSize, int status) {

		List<RespSaleReturn> saleReturns = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<SaleReturnInvoice> criteriaQuery = criteriaBuilder.createQuery(SaleReturnInvoice.class);
			Root<SaleReturnInvoice> root = criteriaQuery.from(SaleReturnInvoice.class);

			CriteriaQuery<SaleReturnInvoice> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("approve"), status));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			List<SaleReturnInvoice> saleReturnInvoice = new ArrayList<>();
			if (pageSize > 0) {
				TypedQuery<SaleReturnInvoice> typedQuery = session.createQuery(select);
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(pageSize);

				saleReturnInvoice = typedQuery.getResultList();
			} else {
				Query<SaleReturnInvoice> typedQuery = session.createQuery(select);
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(pageSize);

				saleReturnInvoice = typedQuery.getResultList();
			}

			log.info("saleReturnInvoice " + saleReturnInvoice.size());
			if (saleReturnInvoice != null) {

				saleReturns = saleReturnMapper.mapAllSaleReturnInvoice(saleReturnInvoice);
			}

		} catch (Exception e) {
			log.info("getOrdersByStatus " + e.getMessage());
		}

		session.clear();
		session.close();
		return saleReturns;
	}

	@Override
	public SaleReturnInvoice getSaleRetunById(String id) {

		Optional<SaleReturnInvoice> optional = saleReturnInvoiceRepository.getSaleReturnInvoiceByPublicId(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public RespSaleReturn getRespSaleRetunById(String id) {

		log.info("getRespSaleRetunById " + id);
		RespSaleReturn saleReturn = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<SaleReturnInvoice> criteriaQuery = (CriteriaQuery<SaleReturnInvoice>) criteriaBuilder
					.createQuery(SaleReturnInvoice.class);
			Root<SaleReturnInvoice> root = criteriaQuery.from(SaleReturnInvoice.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<SaleReturnInvoice> query = session.createQuery(criteriaQuery);

			SaleReturnInvoice saleReturnInvoice = query.getSingleResult();

			if (saleReturnInvoice != null) {
				saleReturn = saleReturnMapper.mapSaleReturnInvoice(saleReturnInvoice);
			}

		} catch (Exception e) {
			log.info("Get Order Status " + e.getMessage());
			e.printStackTrace();

		}
		session.clear();
		session.close();

		return saleReturn;
	}

	@Override
	public void addRejectSaleReturnInvoice(EsApprove approve, Map<String, Object> map) {

		SaleReturnInvoice invoice = getSaleRetunById(approve.getStId());

		if (invoice != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				if (invoice.getApprove() == 2) {
					throw new Exception("Sale Return invoice already Rejected :)");
				} else if (invoice.getApprove() > 0) {
					throw new Exception(
							"Sale Return invoice reject request failed. Please, contact the administrator :)");
				}

				transaction = session.beginTransaction();

				SaleReturnInvoice dbInvoice = session.get(SaleReturnInvoice.class, invoice.getId());
				dbInvoice.setApprove(2);
				dbInvoice.setRejectNote(approve.getNarration());

				session.update(dbInvoice);

				transaction.commit();
				map.put("status", true);
				map.put("message", "Sale Return Invoice Rejectde success :)");
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				map.put("status", false);
				map.put("message", e.getMessage());
			}
			session.clear();
			session.close();
		}

	}

	@Override
	public void addSaleReturnInvoice(SaleReturnReq saleReturnReq, Map<String, Object> map) {
		map.put("message", "Save Sale Return Failed. Please, contact administrator");
		User user = authenticationServices.getCurrentUser();

		if (saleReturnReq != null && user != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				SaleReturnInvoice returnInvoice = saleReturnMapper.mapSaleReturnInvoice(saleReturnReq, map);
				transaction = session.beginTransaction();

				ReturnType returnType = session.get(ReturnType.class, saleReturnReq.getReturnType());
				returnInvoice.setReturnType(returnType);
				session.persist(returnInvoice);

				if (returnInvoice.getReturnItems() != null) {

					for (SaleReturnItem item : returnInvoice.getReturnItems()) {
						item.setSaleReturnInvoice(returnInvoice);
						session.persist(item);
					}
				}

				if (returnInvoice.getMethodAndTransactions() != null) {

					for (MethodAndTransaction method : returnInvoice.getMethodAndTransactions()) {

						method.setSaleReturnInvoice(returnInvoice);
						session.persist(method);
					}
				}

				transaction.commit();

				map.put("status", true);
				map.put("message", "Save Sale Return success :)");
				session.clear();
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				map.put("status", false);
				map.put("message", "Save Sale Return Failed. Please, contact administrator");
			}

			session.close();
		}

	}

	@Override
	public void addApproveSaleReturnInvoice(EsApprove approve, Map<String, Object> map) {

		if (approve != null) {

			SaleReturnInvoice invoice = getSaleRetunById(approve.getStId());

			if (invoice != null) {
				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					if (invoice.getApprove() == 1) {
						throw new Exception("Sale Return invoice already Approved :)");
					} else if (invoice.getApprove() == 2) {
						throw new Exception("Sale Return invoice already Rejected :)");
					} else if (invoice.getApprove() > 0) {
						throw new Exception(
								"Sale Return invoice approve request failed. Please, contact the administrator :)");
					}

					SaleReturnInvoice dbInvoice = session.get(SaleReturnInvoice.class, invoice.getId());

					if (dbInvoice == null) {
						throw new Exception("Sale Return invoice not found !!");
					}

					Stakeholder dbStakeholder = null;
					Store dbStore = null;

					EsDebit dbEsDebit = null;
					EsCredit dbEsCredit = null;

					Date date = dbInvoice.getDate();

					if (dbInvoice.getStakeholder() != null) {
						dbStakeholder = session.get(Stakeholder.class, dbInvoice.getStakeholder().getId());
					}

					if (dbStakeholder == null) {
						throw new Exception("Sale Return invoice 'Stakeholder Or Customer' not found !!");
					}

					if (dbInvoice.getStore() != null) {
						dbStore = session.get(Store.class, dbInvoice.getStore().getId());
					}

					if (dbStore == null) {
						throw new Exception("Sale Return invoice 'Business' not found !!");
					}

					if (dbStakeholder.getDebit() != null) {
						dbEsDebit = session.get(EsDebit.class, dbStakeholder.getDebit().getId());
					}

					double nStkDebtAmount = dbInvoice.getReturnFees() + dbInvoice.getPaidAmount()
							+ dbInvoice.getLessAdustment();
					if (dbEsDebit != null) {
						double nEsDebitAmount = dbEsDebit.getAmount() + nStkDebtAmount;
						dbEsDebit.setAmount(nEsDebitAmount);
						session.update(dbEsDebit);
					} else {
						dbEsDebit = esInitializerServices.initEsDebitByStakeholder(dbStakeholder, date);
						dbEsDebit.setAmount(nStkDebtAmount);
						session.persist(dbEsDebit);
					}

					dbInvoice.setNote(approve.getNarration());
					dbInvoice.setApprove(1);

					double nStkTotalCredit = 0, nStkTotalDebit = 0;

					if (dbStakeholder.getTotalCreditAmount() != null) {
						nStkTotalCredit = dbStakeholder.getTotalCreditAmount().doubleValue()
								+ dbInvoice.getGrandTotal();
					}

					if (dbStakeholder.getTotalDebitAmount() != null) {
						nStkTotalDebit = dbStakeholder.getTotalDebitAmount().doubleValue() + dbInvoice.getPaidAmount();
					}

					dbStakeholder.setTotalCreditAmount(converterServices.getDoubleToBigDec(nStkTotalCredit));
					dbStakeholder.setTotalDebitAmount(converterServices.getDoubleToBigDec(nStkTotalDebit));

					bankAccountServices.updateHaveAnyAccountTransactions(dbInvoice.getMethodAndTransactions(), session,
							date);

					session.update(dbStakeholder);
					session.update(dbInvoice);

					transaction.commit();

					map.put("status", true);
					map.put("message", "Sale Return Invoice Approved :)");

				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}

					e.printStackTrace();
					map.put("status", false);
					map.put("message", e.getMessage());
				}

				session.clear();
				session.close();
			}
		}

	}

}
