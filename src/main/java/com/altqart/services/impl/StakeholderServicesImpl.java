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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.initializer.services.TransactionInitServices;
import com.altqart.mapper.PaidMapper;
import com.altqart.mapper.StakeholderMapper;
import com.altqart.model.BankAccount;
import com.altqart.model.BankAccountCredit;
import com.altqart.model.BankAccountCreditRecord;
import com.altqart.model.BankAccountDebit;
import com.altqart.model.BankAccountDebitRecord;
import com.altqart.model.Credential;
import com.altqart.model.EsCredit;
import com.altqart.model.EsCreditDetail;
import com.altqart.model.EsDebit;
import com.altqart.model.EsDebitDetail;
import com.altqart.model.MethodAndTransaction;
import com.altqart.model.Paid;
import com.altqart.model.Receive;
import com.altqart.model.Role;
import com.altqart.model.Stakeholder;
import com.altqart.model.StakeholderType;
import com.altqart.model.Store;
import com.altqart.model.TempStakeholder;
import com.altqart.model.TransactionRecord;
import com.altqart.model.User;
import com.altqart.repository.StakeholderRepository;
import com.altqart.req.model.AddressReq;
import com.altqart.req.model.EsActive;
import com.altqart.req.model.PaidReq;
import com.altqart.req.model.PaymentApproveReq;
import com.altqart.req.model.ReceiveReq;
import com.altqart.req.model.ReqDate;
import com.altqart.req.model.StakeholderReq;
import com.altqart.req.model.UserEmployeeReq;
import com.altqart.resp.model.RespEsStakeholder;
import com.altqart.resp.model.RespStakeholder;
import com.altqart.services.BankAccountServices;
import com.altqart.services.StakeholderServices;
import com.altqart.services.StakeholderTypeServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StakeholderServicesImpl implements StakeholderServices {

	@Autowired
	private StakeholderRepository stakeholderRepository;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private EsInitializerServices esInitializerServices;

	private SessionFactory sessionFactory;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private TransactionInitServices transactionInitServices;

	@Autowired
	private StakeholderMapper stakeholderMapper;

	@Autowired
	private PaidMapper paidMapper;

	@Autowired
	private StakeholderTypeServices stakeholderTypeServices;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private BankAccountServices bankAccountServices;

	private User user;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public boolean save(Stakeholder stakeholder) {
		User user = authenticationServices.getCurrentUser();
		if (user != null) {
			stakeholder.setUser(user);

		}
		if (stakeholder != null) {
			stakeholder.setDate(new Date());
			stakeholder.setGenId(getGenStakeholderId());
			stakeholder.setPublicId(helperServices.getGenPublicId());
			stakeholderRepository.save(stakeholder);

			if (stakeholder.getId() > 0) {
				return true;
			}
		}
		return false;

	}

	@Override
	public List<RespStakeholder> getCheckStakeholderByPhoneNo(String phoneNo) {
		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("phoneNo"), phoneNo));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getCheckStakeholderByPhoneNo " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();
		return respStakeholders;
	}

	@Override
	public Stakeholder getStakeholderById(int id) {
		Optional<Stakeholder> dbStakeholder = stakeholderRepository.findById(id);

		if (dbStakeholder.isPresent() && !dbStakeholder.isEmpty()) {
			return dbStakeholder.get();
		}
		return null;
	}

	@Override
	public Stakeholder getStakeholderByPhoneNo(String phoneNo) {

		return getStakeholderBySearch(phoneNo);
	}

	@Override
	public Stakeholder getStakeholderPublicId(String id) {

		if (!helperServices.isNullOrEmpty(id)) {

			Optional<Stakeholder> optional = stakeholderRepository.getStakeholderByPublicId(id);

			if (!optional.isEmpty() && optional.isPresent()) {
				return optional.get();
			}
		}

		return null;
	}

	@Override
	public Stakeholder getStakeholderByGenId(String id) {

		Optional<Stakeholder> optional = stakeholderRepository.getStakeholderByGenId(id);

		if (!optional.isEmpty() && optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public Stakeholder getStakeholderByName(String name) {
		return getDataByColNameValueString("firstName", name);
	}

	@Override
	public List<RespStakeholder> getAllActiveStakeholder(int start, int size) {

		size = size > 0 ? size : 20;

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("active"), true));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllActiveStakeholder " + e.getMessage());

//			e.printStackTrace();
		}

		session.close();
		return respStakeholders;
	}

	@Override
	public long getCount() {
		return stakeholderRepository.count();
	}

	@Override
	public boolean isUpdateRequestedActive(String id) {

		boolean status = false;

		if (!helperServices.isNullOrEmpty(id)) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
				CriteriaQuery<TempStakeholder> criteriaQuery = (CriteriaQuery<TempStakeholder>) criteriaBuilder
						.createQuery(TempStakeholder.class);
				Root<TempStakeholder> root = (Root<TempStakeholder>) criteriaQuery.from(TempStakeholder.class);
				criteriaQuery.select(root);
				criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("active"), true),
						criteriaBuilder.equal(root.get("publicId"), id)));
				Query<TempStakeholder> query = session.createQuery(criteriaQuery);

				TempStakeholder tempStakeholder = query.getSingleResult();

				if (tempStakeholder != null) {
					status = true;
				}
				transaction.commit();
				session.clear();

			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				status = false;
			}
			session.close();
		}

		return status;
	}

	@Override
	public boolean updateStakeholder(StakeholderReq stakeholder) {

		boolean status = false;

		TempStakeholder tempStakeholder = stakeholderMapper.mapTempStakeholder(stakeholder);
		Stakeholder dbStakeholder = getStakeholderPublicId(stakeholder.getId());
		if (tempStakeholder != null && dbStakeholder != null) {
			if (!dbStakeholder.isUpdateReq()) {
				return createApproveUpdateStakeholder(dbStakeholder, tempStakeholder);
			} else {
				return true;
			}

		}

		return status;
	}

	@Override
	public boolean createApproveUpdateStakeholder(Stakeholder stakeholder, TempStakeholder tempStakeholder) {
		boolean status = false;
		if (stakeholder != null && tempStakeholder != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());

				if (isStakeholderChange(dbStakeholder, tempStakeholder)) {
					dbStakeholder.setUpdateReq(true);
					session.update(dbStakeholder);

					tempStakeholder.setRefStakeholderId(dbStakeholder.getId());
					tempStakeholder.setDate(new Date());

					session.persist(tempStakeholder);
				}

				transaction.commit();
				session.clear();
				status = true;
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				status = false;
			}
			session.close();
		}

		return status;
	}

	private boolean isStakeholderChange(Stakeholder stakeholder, TempStakeholder tempStakeholder) {

		if (helperServices.isNullOrEmpty(tempStakeholder.getName())
				|| helperServices.isNullOrEmpty(tempStakeholder.getPhoneNo())) {
			return false;
		}
		if (!helperServices.isEqual(tempStakeholder.getName(), stakeholder.getName())
				|| !helperServices.isEqual(tempStakeholder.getDescription(), stakeholder.getDescription())
				|| !helperServices.isEqual(tempStakeholder.getEmail(), stakeholder.getEmail())
				|| !helperServices.isEqual(tempStakeholder.getPhoneNo(), stakeholder.getPhoneNo())) {
			return true;
		}

		return false;
	}

	@Override
	public boolean approveUpdateStakeholder(Stakeholder stakeholder) {
		boolean status = false;
		if (stakeholder != null) {

			TempStakeholder tempStakeholder = getActiveTempStakeholderByStakeholderId(stakeholder.getId());

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());
				dbStakeholder.setUpdateReq(false);

				TempStakeholder dbTempStakeholder = session.get(TempStakeholder.class, tempStakeholder.getId());
				dbTempStakeholder.setActive(false);
				dbTempStakeholder.setApprove(1);

				updateStakeholderInfo(dbStakeholder, dbTempStakeholder);
				session.update(dbTempStakeholder);
				session.update(dbStakeholder);
				transaction.commit();
				session.clear();
				status = true;
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			}
			session.close();
		}

		return status;
	}

	@Override
	public RespStakeholder getCreditorStakeholder(String id) {
		RespStakeholder respStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), 1),
					criteriaBuilder.greaterThan(root.get("totalCreditAmount"), root.get("totalDebitAmount"))));
			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			Stakeholder stakeholder = query.getSingleResult();

			respStakeholder = stakeholderMapper.mapRespStakeholderDebitorOrCreditor(stakeholder);

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();

		return respStakeholder;
	}

	@Override
	public boolean payCreditor(Stakeholder stakeHolder, PaidReq payed) {
		boolean status = false;

		User user = authenticationServices.getCurrentUser();
		Paid nPayed = paidMapper.mapPayed(payed);
		if (nPayed != null) {
			nPayed.setStakeholder(stakeHolder);
			nPayed.setUser(user);

			Date date = new Date();
			if (payed.getDate() != null) {
				nPayed.setDate(payed.getDate());
				nPayed.setDateGroup(payed.getDate());
			} else {
				nPayed.setDate(date);
				nPayed.setDateGroup(date);
			}
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				session.persist(nPayed);

				if (nPayed.getMethodAndTransactions() != null) {

					for (MethodAndTransaction methodTransaction : nPayed.getMethodAndTransactions()) {

						methodTransaction.setPaid(nPayed);
						session.persist(methodTransaction);
					}

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
			}

			session.close();
		}

		return status;
	}

	@Override
	public boolean reciveDebtor(Stakeholder stakeHolder, ReceiveReq receive) {
		boolean status = false;

		User user = authenticationServices.getCurrentUser();
		Receive nReceive = paidMapper.mapReceive(receive);
		if (nReceive != null) {
			nReceive.setStakeholder(stakeHolder);
			nReceive.setUser(user);
			Date date = new Date();
			if (receive.getDate() == null) {
				nReceive.setDate(date);
				nReceive.setDateGroup(date);
			}
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				session.persist(nReceive);

				for (MethodAndTransaction methodTransaction : nReceive.getMethodAndTransactions()) {
					methodTransaction.setReceive(nReceive);
					session.persist(methodTransaction);
				}

				transaction.commit();

				session.clear();
				status = true;
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			}

			session.close();
		}

		return status;
	}

	@Override
	public boolean approveReciveDebtor(Receive receive, PaymentApproveReq paymentApprove, Map<String, Object> map) {
		map.put("message", "Receive found. But Approve failed");
		boolean status = false;

		if (receive != null && paymentApprove != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				Receive dbReceive = session.get(Receive.class, receive.getId());

				Stakeholder dbStakeholder = session.get(Stakeholder.class, dbReceive.getStakeholder().getId());

				User cUser = authenticationServices.getCurrentUser();
				Store dbStore = null;

				if (dbStakeholder == null) {
					throw new Exception("Receive Stakholder Or Provider Not found");

				}

				if (dbReceive.getUser() != null) {
					if (dbReceive.getUser().getStore() != null) {
						dbStore = session.get(Store.class, dbReceive.getUser().getStore().getId());
					}
				}

				// Store End

				if (cUser != null && dbStore == null) {
					if (cUser.getStore() != null) {
						dbStore = session.get(Store.class, cUser.getStore().getId());
					}
				}

				if (dbStore == null) {
					throw new Exception("Receive Store Not found");
				}

				Date date = dbReceive.getDate();

				EsCredit esCredit = null;

				if (dbStakeholder.getCredit() != null) {

					esCredit = session.get(EsCredit.class, dbStakeholder.getCredit().getId());

					double nEsCreditAmount = esCredit.getAmount() + dbReceive.getAmount();
					esCredit.setAmount(nEsCreditAmount);

					session.update(esCredit);

				} else {
					esCredit = esInitializerServices.initEsCreditByStakeholder(dbStakeholder, date);
					esCredit.setAmount(dbReceive.getAmount());
					session.persist(esCredit);
				}

				EsCreditDetail creditDetail = esInitializerServices.initEsCreditDetail(esCredit, date);
				creditDetail.setAmount(dbReceive.getAmount());
				creditDetail.setNote("By Cash Account");
				creditDetail.setUser(cUser);

				TransactionRecord transactionRecord = new TransactionRecord();
				transactionRecord.setAmount(dbReceive.getAmount());
				transactionRecord.setDate(date);
				transactionRecord.setDateGroup(date);

				session.persist(transactionRecord);

				// Update DB Stakeholder Receive Debit

				double receiveDebitBalance = 0, prevDebit = 0, currentDebit = 0, rTstkCrAmount = 0, rTstkDeAmount = 0;

				double cCreditAmnt = dbReceive.getAmount() + dbReceive.getDiscount();

				if (dbStakeholder.getTotalCreditAmount() != null) {
					rTstkCrAmount = dbStakeholder.getTotalCreditAmount().doubleValue();
					double nAmnt = dbStakeholder.getTotalCreditAmount().doubleValue() + cCreditAmnt;
					dbStakeholder.setTotalCreditAmount(converterServices.getDoubleToBigDec(nAmnt));

				} else {
					dbStakeholder.setTotalCreditAmount(converterServices.getDoubleToBigDec(cCreditAmnt));

				}

				if (dbStakeholder.getTotalDebitAmount() != null) {
					rTstkDeAmount = dbStakeholder.getTotalDebitAmount().doubleValue();
				}

				receiveDebitBalance = rTstkDeAmount - rTstkCrAmount;

				currentDebit = receiveDebitBalance - cCreditAmnt;

				dbReceive.setPresentDebit(currentDebit);
				dbReceive.setPrevDebit(receiveDebitBalance);

				session.update(dbStakeholder);

				dbReceive.setApprove(1);
				session.update(dbReceive);

				haveMethodAnyAccountTransactions(dbReceive.getMethodAndTransactions(), session, dbReceive.getDate());

				transaction.commit();
				status = true;
				session.clear();
				map.put("message", "Received Approved successfully");
				map.put("status", status);
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				map.put("message", e.getMessage());
				map.put("status", false);
				status = false;
			}

			session.close();

		}
		return status;
	}

	private void haveMethodAnyAccountTransactions(List<MethodAndTransaction> methodAndTransactions, Session session,
			Date date) {

		if (methodAndTransactions != null) {
			for (MethodAndTransaction methodAndTransaction : methodAndTransactions) {

				if (methodAndTransaction != null) {

					if (methodAndTransaction.getSource() != null || methodAndTransaction.getDestination() != null) {
						bankAccountServices.updateBankAccountViaTransaction(methodAndTransaction, session, date);
					}
				}
			}
		}

	}

	@Override
	public boolean rejectPayCreditor(Paid paid, PaymentApproveReq approveReq) {
		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			Paid dbPaid = session.get(Paid.class, paid.getId());
			dbPaid.setApprove(2);
			dbPaid.setRejectNote(approveReq.getRejectNote());
			session.update(dbPaid);

			transaction.commit();
			session.clear();
			status = true;
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			status = false;
		}
		session.close();

		return status;
	}

	@Override
	public boolean approvePayCreditor(Paid paid, PaymentApproveReq paymentApprove, Map<String, Object> map) {

		boolean status = false;
		User cUser = authenticationServices.getCurrentUser();

		if (paid != null && paymentApprove != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Paid dbPayed = session.get(Paid.class, paid.getId());

				Stakeholder dbStakeholder = session.get(Stakeholder.class, dbPayed.getStakeholder().getId());

				if (dbStakeholder == null) {
					throw new Exception("Paid Stakeholder Or Receiver not found");
				}
				Store dbStore = null;

				if (dbPayed.getUser() != null) {
					if (dbPayed.getUser().getStore() != null) {
						dbStore = session.get(Store.class, dbPayed.getUser().getStore().getId());
					}
				}

				if (dbStore == null) {
					throw new Exception("Paid Store not found");
				}

				EsDebit esDebit = null;
				Date date = dbPayed.getDate();

				if (dbStakeholder.getDebit() != null) {

					esDebit = session.get(EsDebit.class, dbStakeholder.getDebit().getId());
					double nDebitAmount = esDebit.getAmount() + dbPayed.getAmount() + dbPayed.getDiscount();
					esDebit.setAmount(nDebitAmount);
					session.update(esDebit);
				} else {
					esDebit = esInitializerServices.initEsDebitByStakeholder(dbStakeholder, date);
					esDebit.setAmount(dbPayed.getAmount() + dbPayed.getDiscount());

					session.persist(esDebit);
				}

				EsDebitDetail debitDetail = esInitializerServices.initEsDebitDetail(esDebit, date);
				debitDetail.setAmount(dbPayed.getAmount());
				debitDetail.setNote("By Cash Account");
				debitDetail.setUser(cUser);

				session.persist(debitDetail);

				double currentCredit = 0, currentDebit = 0, prevCreditBalance = 0, currentCreditBalance = 0;

				if (dbStakeholder.getTotalDebitAmount() != null) {
					currentDebit = dbStakeholder.getTotalDebitAmount().doubleValue();
				}

				if (dbStakeholder.getTotalCreditAmount() != null) {
					currentCredit = dbStakeholder.getTotalCreditAmount().doubleValue();
				}

				prevCreditBalance = currentCredit - currentDebit;
				dbPayed.setPrevCredit(prevCreditBalance);

				if (currentDebit > 0) {
					double nStkhDebitAmount = dbStakeholder.getTotalDebitAmount().doubleValue() + dbPayed.getAmount()
							+ dbPayed.getDiscount();
					dbStakeholder.setTotalDebitAmount(converterServices.getDoubleToBigDec(nStkhDebitAmount));
				} else {
					dbStakeholder.setTotalDebitAmount(
							converterServices.getDoubleToBigDec(dbPayed.getAmount() + dbPayed.getDiscount()));
				}

				double paidDebit = dbPayed.getAmount() + dbPayed.getDiscount();

				if (paidDebit > 0) {
					currentCreditBalance = prevCreditBalance - paidDebit;
					dbPayed.setPresentCredit(currentCreditBalance);
				}

				session.update(dbStakeholder);

				dbPayed.setApprove(1);
				session.update(dbPayed);

				// If have any banking transaction
				updateAllBankAccountTransaction(dbPayed, session);

				transaction.commit();

				session.clear();
				status = true;
				map.put("status", status);
				map.put("message", "Paid Approve successfully");
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				e.printStackTrace();
				status = false;
				map.put("status", status);
				map.put("message", e.getMessage());
			}

			session.close();
		}
		return status;
	}

	private void updateAllBankAccountTransaction(Paid dbPayed, Session session) {

		if (dbPayed != null) {

			if (dbPayed.getMethodAndTransactions() != null) {
				for (MethodAndTransaction methodTransaction : dbPayed.getMethodAndTransactions()) {

					updateBankAccountTransaction(methodTransaction, session, dbPayed.getDate());

				}
			}
		}

	}

	private void updateBankAccountTransaction(MethodAndTransaction methodTransaction, Session session, Date date) {

		if (methodTransaction != null) {

			BankAccount dbSourceAccount = null;
			BankAccountCredit dbSourceCredit = null;

			BankAccount dbDestinationAccount = null;
			BankAccountDebit dbDestinationDebit = null;

			if (methodTransaction.getSource() != null) {
				dbSourceAccount = session.get(BankAccount.class, methodTransaction.getSource().getId());

				if (dbSourceAccount != null) {
					if (dbSourceAccount.getCredit() != null) {
						dbSourceCredit = session.get(BankAccountCredit.class, dbSourceAccount.getCredit().getId());
					}
				}
			}

			if (methodTransaction.getDestination() != null) {
				dbDestinationAccount = session.get(BankAccount.class, methodTransaction.getDestination().getId());
				if (dbDestinationAccount != null) {
					if (dbDestinationAccount.getDebit() != null) {
						dbDestinationDebit = session.get(BankAccountDebit.class,
								dbDestinationAccount.getDebit().getId());
					}
				}
			}

			if (dbSourceAccount != null) {

				double nSourceAcCreditAmount = dbSourceAccount.getTotalCredit() + methodTransaction.getAmount();
				dbSourceAccount.setTotalCredit(nSourceAcCreditAmount);
				session.update(dbSourceAccount);

				if (dbSourceCredit != null) {

					double nSourceCredit = dbSourceCredit.getAmount() + methodTransaction.getAmount();
					dbSourceCredit.setAmount(nSourceCredit);
					session.update(dbSourceCredit);
				} else {
					dbSourceCredit = esInitializerServices.initBankAccountCredit(dbDestinationAccount, date);
					dbSourceCredit.setAmount(methodTransaction.getAmount());
					session.persist(dbSourceCredit);
				}
			}

			if (dbDestinationAccount != null) {
				double nDestinationAcDebit = dbDestinationAccount.getTotalDebit() + methodTransaction.getAmount();
				dbDestinationAccount.setTotalDebit(nDestinationAcDebit);
				session.update(dbDestinationAccount);

				if (dbDestinationDebit != null) {
					double nDestinationDebit = dbDestinationDebit.getAmount() + methodTransaction.getAmount();
					dbDestinationDebit.setAmount(nDestinationDebit);
					session.update(dbDestinationDebit);
				} else {
					dbDestinationDebit = esInitializerServices.initBankAccountDebit(dbDestinationAccount, date);
					dbDestinationDebit.setAmount(methodTransaction.getAmount());
					session.persist(dbDestinationDebit);
				}
			}

			if (dbSourceCredit != null && dbDestinationDebit != null) {
				BankAccountCreditRecord sourceCreditRecord = esInitializerServices.initBankCreditRecord(dbSourceCredit,
						date);
				sourceCreditRecord.setAmount(methodTransaction.getAmount());
				sourceCreditRecord.setNote("Credit Paid. Ref. " + methodTransaction.getRefNo());
				sourceCreditRecord.setMethodTransaction(methodTransaction);
				BankAccountDebitRecord destinationDebitRecord = esInitializerServices
						.initBankDebitRecord(dbDestinationDebit, date);
				destinationDebitRecord.setAmount(methodTransaction.getAmount());
				destinationDebitRecord.setNote("Credit paid. Ref. " + methodTransaction.getRefNo());
				destinationDebitRecord.setMethodAndTransaction(methodTransaction);

				destinationDebitRecord.setAccountCreditRecord(sourceCreditRecord);
				sourceCreditRecord.setAccountDebitRecord(destinationDebitRecord);

				session.persist(destinationDebitRecord);
				session.persist(sourceCreditRecord);
			} else {
				if (dbDestinationDebit != null) {
					BankAccountDebitRecord destinationDebitRecord = esInitializerServices
							.initBankDebitRecord(dbDestinationDebit, date);
					destinationDebitRecord.setAmount(methodTransaction.getAmount());
					destinationDebitRecord.setNote("Credit paid. Ref. " + methodTransaction.getRefNo());
					destinationDebitRecord.setMethodAndTransaction(methodTransaction);
					session.persist(destinationDebitRecord);
				}

				if (dbSourceCredit != null) {

					BankAccountCreditRecord sourceCreditRecord = esInitializerServices
							.initBankCreditRecord(dbSourceCredit, date);
					sourceCreditRecord.setAmount(methodTransaction.getAmount());
					sourceCreditRecord.setNote("Credit Paid. Ref. " + methodTransaction.getRefNo());
					sourceCreditRecord.setMethodTransaction(methodTransaction);
					session.persist(sourceCreditRecord);

				}
			}

		}
	}

	@Override
	public List<RespStakeholder> getAllCreditorStakeholders() {
		List<RespStakeholder> stakeholders = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery
					.where(criteriaBuilder.greaterThan(root.get("totalCreditAmount"), root.get("totalDebitAmount")));
			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			List<Stakeholder> stakeholderList = query.getResultList();

			if (stakeholderList != null) {
				stakeholders = stakeholderMapper.mapAllRespDebitorOrCreditorStakeholder(stakeholderList);
			}

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
			log.info("getAllCreditorStakeholders " + e.getMessage());

		}

		session.close();

		return stakeholders;

	}

	@Override
	public List<RespStakeholder> getAllDebitorStakeholders() {
		List<Stakeholder> stakeholders = null;
		List<RespStakeholder> respStakeholders = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery
					.where(criteriaBuilder.greaterThan(root.get("totalDebitAmount"), root.get("totalCreditAmount")));
			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespDebitorOrCreditorStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {
//			e.printStackTrace();
			log.info("getAllDebitorStakeholders " + e.getMessage());
		}

		session.close();

		return respStakeholders;

	}

	@Override
	public List<RespStakeholder> getRespAllDebitorStakeholders(int start, int size) {
		List<RespStakeholder> respStakeholders = null;

		size = size > 0 ? size : 15;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), 1),
					criteriaBuilder.greaterThan(root.get("totalDebitAmount"), root.get("totalCreditAmount"))));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);

			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			List<Stakeholder> stakeholders = typedQuery.getResultList();

			respStakeholders = stakeholderMapper.mapAllRespDebitorOrCreditorStakeholder(stakeholders);

			session.clear();

		} catch (Exception e) {
//			e.printStackTrace();
			log.info("getRespAllDebitorStakeholders " + e.getMessage());
		}

		session.close();

		return respStakeholders;
	}

	@Override
	public List<RespStakeholder> getRespAllCreditorStakeholders(int start, int size) {

		List<RespStakeholder> respStakeholders = null;

		size = size > 0 ? size : 15;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), 1),
					criteriaBuilder.greaterThan(root.get("totalCreditAmount"), root.get("totalDebitAmount"))));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);

			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			List<Stakeholder> stakeholders = typedQuery.getResultList();

			respStakeholders = stakeholderMapper.mapAllRespDebitorOrCreditorStakeholder(stakeholders);

			session.clear();

		} catch (Exception e) {
//			e.printStackTrace();
			log.info("getRespAllCreditorStakeholders " + e.getMessage());

		}

		session.close();

		return respStakeholders;
	}

	@Override
	public RespStakeholder getStakeholderStatistics(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean activeOrInActiveStakeholder(Stakeholder stakeholder, boolean status) {
		boolean updateStatus = false;
		if (stakeholder != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());

				if (dbStakeholder != null) {
					dbStakeholder.setActive(status);
					session.update(dbStakeholder);
				}
				transaction.commit();
				session.clear();
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				updateStatus = false;
			}

			session.close();
		}

		return updateStatus;
	}

	@Override
	public List<RespStakeholder> getAllRejectedStakeholder(int start, int size) {

		return getAllStakeholderUsingStatus(start, size, 2);
	}

	@Override
	public List<RespStakeholder> getAllStakeholderOnly() {
		return getAllStakeholderUsingStatus(0, 100, 1);
	}

	@Override
	public List<RespStakeholder> getAllStakeholderPending(int start, int size) {

		return getAllStakeholderUsingStatus(start, size, 0);
	}

	@Override
	public List<RespStakeholder> getAllApproveStakeholder(int start, int size) {
		return getAllStakeholderUsingStatus(start, size, 1);
	}

	@Override
	public RespStakeholder getStakeholderPublicIdDetails(String id) {

		RespStakeholder stakeholder = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			Stakeholder data = (Stakeholder) query.getSingleResult();

			if (data != null) {
				stakeholder = stakeholderMapper.mapStakeholderDetails(data);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getStakeholderPublicIdDetails " + e.getMessage());

//			e.printStackTrace();

		}
		session.close();

		return stakeholder;
	}

	@Override
	public RespStakeholder getDebitorOrCreditoStakeholderPublicId(String id) {
		RespStakeholder stakeholder = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			Stakeholder data = (Stakeholder) query.getSingleResult();

			if (data != null) {
				stakeholder = stakeholderMapper.mapRespDebitorOrCreditorStakeholderDetails(data);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getDebitorOrCreditoStakeholderPublicId " + e.getMessage());

		}
		session.close();

		return stakeholder;
	}

	@Override
	public boolean rejectStakeholder(Stakeholder stakeholder, String rejectNote) {

		boolean status = false;

		if (stakeholder != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();

				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());

				if (dbStakeholder != null) {
					dbStakeholder.setApprove(2);
					dbStakeholder.setActive(false);
					dbStakeholder.setRejectNote(rejectNote);

					session.update(dbStakeholder);
					status = true;
				}

				transaction.commit();
				session.clear();

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				status = false;
			}
			session.close();
		}

		return status;
	}

	private Stakeholder getDataByColNameValueString(String colName, String value) {
		Stakeholder stakeholder = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			value = "%" + value + "%";

			criteriaQuery.where(criteriaBuilder.like(root.get(colName), value));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			stakeholder = (Stakeholder) query.getSingleResult();

			session.clear();

		} catch (Exception e) {

			log.info("getDataByColNameValueString " + e.getMessage());

//			e.printStackTrace();

		}
		session.close();
		return stakeholder;

	}

	@Override
	public void saveBySale(Stakeholder nStakeholder) {

		if (nStakeholder != null) {

			if (!helperServices.isNullOrEmpty(nStakeholder.getPhoneNo())) {

				User user = authenticationServices.getCurrentUser();

				nStakeholder.setPublicId(helperServices.getGenPublicId());
				nStakeholder.setGenId(getGenStakeholderId());
				nStakeholder.setNote("This Stakeholder Create or added via Sale");
				nStakeholder.setApprove(1);
				nStakeholder.setDate(new Date());
				nStakeholder.setUser(user);
				stakeholderRepository.save(nStakeholder);
			}

		}

	}

	@Override
	public List<RespStakeholder> getPagingStakeholderByStakeType(String key, int start, int size) {

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
			Join<Stakeholder, StakeholderType> join = root.join("stakeholderTypes");

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(join.get("key"), key),
					criteriaBuilder.equal(root.get("approve"), 1)));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

//			e.printStackTrace();
			log.info("getDataByColNameValueString " + e.getMessage());

		}
		session.close();
		return respStakeholders;

	}

	@Override
	public List<RespStakeholder> getPagingStakeholderByStakeTypeStatus(int status, String key, int start, int size) {

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
			Join<Stakeholder, StakeholderType> join = root.join("stakeholderTypes");

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), status),
					criteriaBuilder.equal(join.get("key"), key)));

			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {

				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getPagingStakeholderByStakeTypeStatus " + e.getMessage());

		}

		session.close();

		return respStakeholders;
	}

	@Override
	public List<RespEsStakeholder> getAllRespEsStakholder(String key, int status) {
		List<RespEsStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
			Join<Stakeholder, StakeholderType> join = root.join("stakeholderTypes");

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), status),
					criteriaBuilder.equal(join.get("key"), key)));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllEsStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllRespEsStakholder " + e.getMessage());

		}

		session.close();

		return respStakeholders;
	}

	@Override
	public List<RespStakeholder> getPendingStakeholderByStakeTypeNotEqualKey(String key, int start, int size) {

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
			Join<Stakeholder, StakeholderType> join = root.join("stakeholderTypes");

			criteriaQuery.where(criteriaBuilder.notEqual(join.get("key"), key));

			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getPendingStakeholderByStakeTypeNotEqualKey " + e.getMessage());
		}

		session.close();
		return respStakeholders;

	}

	@Override
	public RespStakeholder getRespStakeholderCreditorByPublicId(String id) {
		RespStakeholder respStakeholder = getDebitorOrCreditorDetails(id);

		return respStakeholder;
	}

	@Override
	public RespStakeholder getRespStakeholderDebtorByPublicId(String id) {
		return getDebitorOrCreditorDetails(id);
	}

	@Override
	public boolean approveDeleteStakeholder(Stakeholder stakeholder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rejectDeleteStakeholder(Stakeholder stakeholder, String rejectNote) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rejectUpdateStakeholder(Stakeholder stakeholder, String rejectNote) {
		boolean status = false;

		TempStakeholder tempStakeholder = getActiveTempStakeholderByStakeholderId(stakeholder.getId());

		if (tempStakeholder != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());
				TempStakeholder dbTempStakeholder = session.get(TempStakeholder.class, tempStakeholder.getId());

				dbStakeholder.setRejectNote(rejectNote);
				dbStakeholder.setUpdateReq(false);
				session.update(dbStakeholder);

				dbTempStakeholder.setActive(false);
				dbTempStakeholder.setApprove(3);

				session.update(dbTempStakeholder);

				transaction.commit();
				session.clear();
				status = true;
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				status = false;
			}
			session.close();
		}

		return status;
	}

	@Override
	public RespStakeholder getStakeholderCustomerByPublicId(String id) {
		RespStakeholder respStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				respStakeholder = stakeholderMapper.mapCustomerStakeholderDetails(stakeholder, respStakeholder);
			}

			session.clear();

		} catch (Exception e) {
			log.info("getStakeholderCustomerByPublicId " + e.getMessage());
		}

		session.close();
		return respStakeholder;
	}

	@Override
	public boolean updateStakeholderToUserTypee(UserEmployeeReq userEmployee) {
		boolean status = false;
		User authUser = authenticationServices.getCurrentUser();
		if (userEmployee != null && authUser != null) {

			Stakeholder stakeholder = getStakeholderPublicId(userEmployee.getId());
			User user = stakeholderMapper.mapStakeholderToUser(stakeholder);
			if (stakeholder != null && user != null) {
				StakeholderType stakeholderUserType = stakeholderTypeServices.getStakeholderTypeByKey("user");
				StakeholderType stakeholderEmployeeType = stakeholderTypeServices.getStakeholderTypeByKey("employee");

				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					Date date = new Date();

					Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());
					dbStakeholder.getStakeholderTypes().add(stakeholderUserType);
					dbStakeholder.getStakeholderTypes().add(stakeholderEmployeeType);
					user.setStakeholder(dbStakeholder);

					if (authUser.getStore() != null) {
						user.setStore(authUser.getStore());
					}

					Role role = session.get(Role.class, 1);
					user.setRole(role);

					Credential credential = new Credential();
					credential.setPassword(passwordEncoder.encode(userEmployee.getPassword()));
					credential.setStatus(1);
					credential.setUser(user);
					credential.setDate(date);

					session.persist(credential);
					;

					session.persist(user);
					session.update(dbStakeholder);
					transaction.commit();
					status = true;
					session.clear();
				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();

					}
					status = false;
				}
				session.close();

			}

		}

		return status;
	}

	@Override
	public RespStakeholder getRespStakeholderDetailsByPublicId(String id) {
		RespStakeholder respStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				respStakeholder = stakeholderMapper.mapRespStakeholderDetails(stakeholder);
			}

			session.clear();

		} catch (Exception e) {
			log.info("getRespStakeholderDetailsByPublicId " + e.getMessage());
		}

		session.close();
		return respStakeholder;
	}

	@Override
	public RespStakeholder getDebitorOrCreditorDetails(String id) {
		RespStakeholder respStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), 1),
					criteriaBuilder.equal(root.get("publicId"), id)));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				respStakeholder = stakeholderMapper.mapRespDebitorOrCreditorStakeholderDetails(stakeholder);
			}

			session.clear();

		} catch (Exception e) {
			log.info("getDebitorOrCreditorDetails " + e.getMessage());
		}

		session.close();
		return respStakeholder;
	}

	@Override
	public List<RespStakeholder> getAllRepsStakeholderUsingStatusType(int status, String type) {
		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
			Join<Stakeholder, StakeholderType> join = root.join("stakeholderTypes");

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), status),
					criteriaBuilder.equal(join.get("key"), type)));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllRepsStakeholderUsingStatusType " + e.getMessage());

		}

		session.close();

		return respStakeholders;
	}

	@Override
	public List<RespStakeholder> getAllRespStakeholderUsingStatus(int status) {
		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("approve"), status));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllRespStakeholderUsingStatus " + e.getMessage());

		}

		session.close();

		return respStakeholders;
	}

	@Override
	public boolean addTypeToStakeholder(StakeholderType stakeType, Stakeholder stakeholder) {

		boolean status = false;

		if (stakeholder != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();
				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());

				StakeholderType dbStakeholderType = session.get(StakeholderType.class, stakeType.getId());

				dbStakeholder.getStakeholderTypes().add(dbStakeholderType);

				session.update(dbStakeholder);

				transaction.commit();
				session.clear();

				status = true;

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				status = false;
			}
			session.close();
		}

		return status;
	}

	@Override
	public List<RespStakeholder> getAllRespStakeholderUpdatePending() {

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("updateReq"), true));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllRespStakeholderUpdatePending " + e.getMessage());
		}
		session.close();
		return respStakeholders;

	}

	@Override
	public List<RespStakeholder> getPagingStakeholderUpdatePending(int start, int size) {
		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("updateReq"), true));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getPagingStakeholderUpdatePending " + e.getMessage());

		}

		session.close();
		return respStakeholders;
	}

	@Override
	public List<RespStakeholder> getPagingStakeholderByStatus(int status, int start, int size) {

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("approve"), status));

			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {
				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getPagingStakeholderByStatus " + e.getMessage());

		}

		session.close();
		return respStakeholders;
	}

	@Override
	public RespStakeholder getUpdatePendingStakeholderByID(String id) {
		RespStakeholder respStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("updateReq"), true),
					criteriaBuilder.equal(root.get("publicId"), id)));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				respStakeholder = stakeholderMapper.mapRespDebitorOrCreditorStakeholderDetails(stakeholder);
			}

			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();
		return respStakeholder;
	}

	@Override
	public TempStakeholder getActiveTempStakeholderByStakeholder(String id) {
		TempStakeholder tempStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<TempStakeholder> criteriaQuery = (CriteriaQuery<TempStakeholder>) criteriaBuilder
					.createQuery(TempStakeholder.class);
			Root<TempStakeholder> root = (Root<TempStakeholder>) criteriaQuery.from(TempStakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("publicId"), id),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), 0),
							criteriaBuilder.equal(root.get("active"), true))));

			Query<TempStakeholder> query = session.createQuery(criteriaQuery);
			tempStakeholder = query.getSingleResult();
			session.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();

		return tempStakeholder;
	}

	@Override
	public RespStakeholder getStakeholderByPublicIdOnly(String id) {
		RespStakeholder respStakeholder = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				respStakeholder = stakeholderMapper.mapRespStakeholderOnly(stakeholder);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getStakeholderByPublicIdOnly " + e.getMessage());
		}
		session.close();
		return respStakeholder;
	}

	@Override
	public void getStakeholderStatementByPublicId(String id, Map<String, Object> map) {

		map.put("message", "Start creating statement");

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				stakeholderMapper.createAccountStatement(stakeholder, map);
			}

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

			map.put("message", e.getMessage());
			map.put("status", false);
			map.put("response", null);
		}
		session.close();
	}

	@Override
	public void getStakeholderStatementDateByPublicId(String id, Map<String, Object> map, ReqDate reqDate) {

		Stakeholder stakeholder = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			stakeholder = query.getSingleResult();

			if (reqDate != null) {
				stakeholderMapper.createAccountStatementBetweenDate(reqDate, stakeholder, map);
			} else {
				stakeholderMapper.createAccountStatement(stakeholder, map);
			}

			map.put("status", true);
			session.clear();

		} catch (Exception e) {
			log.info("getActiveTempStakeholderByStakeholderId " + e.getMessage());

			map.put("message", e.getMessage());
			map.put("status", false);
		}

		session.close();

	}

	@Override
	public void activeOrInActiveStakeholderById(EsActive esActive, Map<String, Object> map) {

		if (esActive != null) {
			Stakeholder stakeholder = getStakeholderPublicId(esActive.getId());

			if (stakeholder != null) {

				if (stakeholder.getApprove() == 1) {
					boolean status = false;
					if (esActive.isActive() && !stakeholder.isActive()) {
						status = activeOrInActiveStakeholder(stakeholder, esActive.isActive());

						if (status) {
							map.put("message", "Memeber Is activated :)");
							map.put("status", true);
						} else {
							map.put("message", "Memeber failed to activate !!");
						}
					} else {
						map.put("message", "Memeber Already Active :)");
						map.put("status", true);
					}

					if (!esActive.isActive() && stakeholder.isActive()) {
						status = activeOrInActiveStakeholder(stakeholder, esActive.isActive());

						if (status) {
							map.put("message", "Memeber is In-Activate :)");
						} else {
							map.put("message", "Memeber failed to In-Activate !!");
						}

					} else {
						map.put("message", "Memeber Already In-Active :)");
						map.put("status", true);
					}
				} else {
					map.put("message", "This Member not Applicable !!");
				}

			}
		}

	}

	@Override
	public boolean removeTypeToStakeholder(StakeholderType stakeholderType, Stakeholder stakeholder) {

		boolean status = false;

		if (stakeholder != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {

				transaction = session.beginTransaction();
				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());

				StakeholderType dbStakeholderType = session.get(StakeholderType.class, stakeholderType.getId());
				dbStakeholder.getStakeholderTypes().remove(dbStakeholderType);

				session.update(dbStakeholder);

				transaction.commit();
				session.clear();

				status = true;

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				status = false;
				e.printStackTrace();
			}
			session.close();
		}

		return status;

	}

	@Override
	public void getStakeholderAllSaleItems(String id, Map<String, Object> map) {

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Geting Stakeholder Sale Items");

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				map.put("message", "Stakeholder Or Customer found By ID. This Customer has not sale record");

				stakeholderMapper.mapAllSaleItems(stakeholder.getOrders(), map);

			}

			session.clear();

		} catch (Exception e) {
			map.put("message", e.getMessage());
			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public void getStakeholderAllAccount(String id, Map<String, Object> map) {
		map.put("status", false);
		map.put("response", null);
		map.put("message", "Geting Stakeholder Account");

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				map.put("message", "Stakeholder Or Customer found By ID. This Customer has not Bank Accounts ");

				stakeholderMapper.mapAllAccount(stakeholder.getBankAccounts(), map);

			}

			session.clear();

		} catch (Exception e) {
			map.put("message", e.getMessage());
//			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public void getStakeholderAllPaidMethods(String id, Map<String, Object> map) {
		map.put("status", false);
		map.put("response", null);
		map.put("message", "Geting Stakeholder Account");

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);
			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Stakeholder> query = session.createQuery(criteriaQuery);
			Stakeholder stakeholder = query.getSingleResult();

			if (stakeholder != null) {
				map.put("message", "Stakeholder Or Customer found By ID. This Customer has not Bank Accounts ");

				stakeholderMapper.mapAllAccountForOption(stakeholder.getBankAccounts(), map);

			}

			session.clear();

		} catch (Exception e) {
			map.put("message", e.getMessage());
			e.printStackTrace();
		}

		session.close();

	}

	@Override
	public Stakeholder getStakeholderByPublicId(String id) {

		Optional<Stakeholder> optional = stakeholderRepository.getStakeholderByPublicId(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public void getAddStakeholderAddress(Map<String, Object> map, AddressReq addressReq) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllStakeholderAddress(Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStakeholderAddress(Map<String, Object> map, AddressReq addressReq) {
		// TODO Auto-generated method stub

	}

	// Public End

	// Private Start

	private TempStakeholder getActiveTempStakeholderByStakeholderId(int id) {
		TempStakeholder tempStakeholder = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<TempStakeholder> criteriaQuery = (CriteriaQuery<TempStakeholder>) criteriaBuilder
					.createQuery(TempStakeholder.class);
			Root<TempStakeholder> root = (Root<TempStakeholder>) criteriaQuery.from(TempStakeholder.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("refStakeholderId"), id),
					criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), 0),
							criteriaBuilder.equal(root.get("active"), true))));

			Query<TempStakeholder> query = session.createQuery(criteriaQuery);
			tempStakeholder = query.getSingleResult();
			session.clear();

		} catch (Exception e) {
			log.info("getActiveTempStakeholderByStakeholderId " + e.getMessage());
		}

		session.close();

		return tempStakeholder;

	}

	private void updateStakeholderInfo(Stakeholder stakeholder, TempStakeholder tempStakeholder) {

		if (stakeholder != null && tempStakeholder != null) {

			if (!helperServices.isEqual(tempStakeholder.getEmail(), stakeholder.getEmail())) {
				stakeholder.setEmail(tempStakeholder.getEmail());
			}

			if (!helperServices.isEqual(tempStakeholder.getName(), stakeholder.getName())) {
				stakeholder.setName(tempStakeholder.getName());
			}

			if (!helperServices.isEqual(tempStakeholder.getPhoneNo(), stakeholder.getPhoneNo())) {
				stakeholder.setPhoneNo(tempStakeholder.getPhoneNo());
			}

		}

	}

	private String getGenStakeholderId() {
		return helperServices.getRandomNumDateString(6);
	}

	private List<RespStakeholder> getAllStakeholderUsingStatus(int start, int size, int status) {

		List<RespStakeholder> respStakeholders = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Stakeholder> criteriaQuery = (CriteriaQuery<Stakeholder>) criteriaBuilder
					.createQuery(Stakeholder.class);

			Root<Stakeholder> root = (Root<Stakeholder>) criteriaQuery.from(Stakeholder.class);

			criteriaQuery.select(root);
//			Join<Stakeholder, StakeholderType> join = root.join("stakeholderTypes");

			criteriaQuery.where(criteriaBuilder.equal(root.get("approve"), status));

			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Stakeholder> typedQuery = session.createQuery(criteriaQuery);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			Query<Stakeholder> query = session.createQuery(criteriaQuery);

			List<Stakeholder> stakeholders = query.getResultList();

			if (stakeholders != null) {

				respStakeholders = stakeholderMapper.mapAllRespStakeholder(stakeholders);
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllStakeholderUsingStatus " + e.getMessage());

		}
		session.close();
		return respStakeholders;
	}

	private Stakeholder getStakeholderBySearch(String phoneNo) {

		return getDataByColNameValueString("phoneNo", phoneNo);
	}

}