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

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.mapper.BankAccountMapper;
import com.altqart.model.BankAccount;
import com.altqart.model.BankAccountCredit;
import com.altqart.model.BankAccountCreditRecord;
import com.altqart.model.BankAccountDebit;
import com.altqart.model.BankAccountDebitRecord;
import com.altqart.model.BankAccountType;
import com.altqart.model.BankBranch;
import com.altqart.model.BankPaid;
import com.altqart.model.BankReceive;
import com.altqart.model.BankWithdraw;
import com.altqart.model.MethodAndTransaction;
import com.altqart.model.Stakeholder;
import com.altqart.model.Store;
import com.altqart.model.TempBankAccount;
import com.altqart.repository.BankAccountRepository;
import com.altqart.repository.BankDepositRepository;
import com.altqart.repository.BankPaidRepository;
import com.altqart.repository.BankWithdrawRepository;
import com.altqart.req.model.BankAccountApprove;
import com.altqart.req.model.BankAccountReq;
import com.altqart.resp.model.RespBankAccount;
import com.altqart.services.BankAccountServices;
import com.altqart.services.StakeholderServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankAccountServicesImpl implements BankAccountServices {

	@Autowired
	private BankAccountMapper bankAccountMapper;

	@Autowired
	private EsInitializerServices esInitializerServices;

	private SessionFactory sessionFactory;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private StakeholderServices stakeholderServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private BankDepositRepository bankDepositRepository;

	@Autowired
	private BankWithdrawRepository bankWithdrawRepository;

	@Autowired
	private BankPaidRepository bankPaidRepository;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespBankAccount> getAllBanksAccountByStatus(int i, int start, int size) {

		List<RespBankAccount> accounts = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankAccount> criteriaQuery = criteriaBuilder.createQuery(BankAccount.class);
			Root<BankAccount> root = criteriaQuery.from(BankAccount.class);

			CriteriaQuery<BankAccount> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("approve"), i),
					criteriaBuilder.equal(root.get("owner"), false)));
			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<BankAccount> typedQuery = session.createQuery(select);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			List<BankAccount> bankAccounts = typedQuery.getResultList();

			if (bankAccounts != null) {

				accounts = bankAccountMapper.mapAllRespBankAccount(bankAccounts);

			} else {
				System.out.println("Get Banks Not found ");
			}

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

		}
		session.close();

		return accounts;
	}

	@Override
	public RespBankAccount getRespBanksAccountById(String id) {
		RespBankAccount account = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankAccount> criteriaQuery = criteriaBuilder.createQuery(BankAccount.class);
			Root<BankAccount> root = criteriaQuery.from(BankAccount.class);

			CriteriaQuery<BankAccount> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			TypedQuery<BankAccount> typedQuery = session.createQuery(select);

			BankAccount bankAccount = typedQuery.getSingleResult();

			if (bankAccount != null) {

				account = bankAccountMapper.mapRespBankAccount(bankAccount);

			}

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();
			log.info("Withdraw Not found " + e.getMessage());

		}
		session.close();

		return account;
	}

	public BankAccount getBanksAccountByAccountNo(String accountNo) {

		Optional<BankAccount> optional = bankAccountRepository.getBankAccountByAccountNo(accountNo);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public boolean update(BankAccountReq bankAccountReq) {
		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		BankAccount bankAccount = getBanksAccountById(bankAccountReq.getId());

		if (bankAccount != null) {
			try {
				transaction = session.beginTransaction();

				BankAccount dbBankAccount = session.get(BankAccount.class, bankAccount.getId());

				if (dbBankAccount != null) {

					dbBankAccount.setUpdateReq(true);
				}
				session.update(dbBankAccount);

				TempBankAccount tempBankAccount = bankAccountMapper.mapTempBankAccount(bankAccountReq);
				session.save(tempBankAccount);
				transaction.commit();
				session.clear();

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				status = false;
			}
		}

		session.close();

		return status;
	}

	@Override
	public boolean updateApprove(BankAccountApprove approve) {
		boolean status = false;

		if (approve.getStatus() == 1) {
			BankAccount account = null;
			TempBankAccount tempBankAccount = getBankTempActiveAccount(approve.getId());
			if (tempBankAccount != null) {
				account = getBanksAccountById(approve.getId());
			}

			if (account != null) {
				Session session = sessionFactory.openSession();
				Transaction transaction = null;

				try {
					transaction = session.beginTransaction();

					BankAccount dbAccount = session.get(BankAccount.class, account.getId());

					if (!helperServices.isNullOrEmpty(tempBankAccount.getName())) {
						dbAccount.setName(tempBankAccount.getName());

					}

					if (tempBankAccount.getAccountType() > 0) {

						if (tempBankAccount.getAccountType() != dbAccount.getType().getId()) {
							BankAccountType accountType = session.get(BankAccountType.class,
									tempBankAccount.getAccountType());

							if (accountType != null) {
								dbAccount.setType(accountType);
							}
						}
					}

					dbAccount.setUpdateReq(false);
					session.update(dbAccount);

					TempBankAccount tmpAccount = session.get(TempBankAccount.class, tempBankAccount.getId());
					tmpAccount.setActive(false);
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

		}

		return status;
	}

	@Override
	public void save(BankAccountReq bankAccountReq, Map<String, Object> map) {

		boolean status = false;

		if (!isAccountExist(bankAccountReq.getAccountNo())) {
			Date date = new Date();

			Stakeholder stakeholder = stakeholderServices.getStakeholderPublicId(bankAccountReq.getStakeholder());
			if (stakeholder != null) {

				Session session = sessionFactory.openSession();
				Transaction transaction = null;
				try {
					transaction = session.beginTransaction();

					BankAccount bankAccount = bankAccountMapper.mapAccount(bankAccountReq);
					BankAccountType accountType = session.get(BankAccountType.class, bankAccountReq.getAccountType());

					Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());
					BankBranch bdBankBranch = session.get(BankBranch.class, bankAccountReq.getBranch());

					if (accountType != null && bankAccount != null) {
						bankAccount.setType(accountType);
						bankAccount.setStakeholder(dbStakeholder);
						bankAccount.setBankBranch(bdBankBranch);

						bankAccount.setDate(date);
						bankAccount.setDateGroup(date);
						bankAccount.setUser(authenticationServices.getCurrentUser());

						session.persist(bankAccount);

						BankAccountCredit accountCredit = esInitializerServices.initBankAccountCredit(bankAccount,
								date);
						session.persist(accountCredit);

						BankAccountDebit accountDebit = esInitializerServices.initBankAccountDebit(bankAccount, date);
						session.persist(accountDebit);

						transaction.commit();
						session.clear();
						status = true;
						map.put("message", "Bank Account Added :)");
					} else {
						throw new Exception("Please, fulfilling Bank Account Request.");

					}

				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					e.printStackTrace();
					status = false;
					map.put("message", e.getMessage());
				}

				session.close();
			} else {
				map.put("message", "Bank Account Save failed. The account holder is not found");

			}

		} else {
			map.put("message", "This Bank Account already added. Ac.No." + bankAccountReq.getAccountNo());
		}
		map.put("status", status);
		map.put("response", null);

	}

	@Override
	public void saveBankAccountViaBusiness(BankAccountReq bankAccountReq, Store store, Map<String, Object> map) {
		boolean status = false;

		if (!isAccountExist(bankAccountReq.getAccountNo())) {
			Date date = new Date();

			if (store != null) {

				Session session = sessionFactory.openSession();
				Transaction transaction = null;
				try {
					transaction = session.beginTransaction();

					BankAccount bankAccount = bankAccountMapper.mapAccount(bankAccountReq);
					BankAccountType accountType = session.get(BankAccountType.class, bankAccountReq.getAccountType());

					BankBranch bdBankBranch = session.get(BankBranch.class, bankAccountReq.getBranch());
					bankAccount.setStore(store);
					bankAccount.setOwner(true);
					if (accountType != null && bankAccount != null) {
						bankAccount.setType(accountType);
						bankAccount.setBankBranch(bdBankBranch);

						bankAccount.setDate(date);
						bankAccount.setDateGroup(date);
						bankAccount.setUser(authenticationServices.getCurrentUser());

						session.persist(bankAccount);

						BankAccountCredit accountCredit = esInitializerServices.initBankAccountCredit(bankAccount,
								date);
						session.persist(accountCredit);

						BankAccountDebit accountDebit = esInitializerServices.initBankAccountDebit(bankAccount, date);
						session.persist(accountDebit);

						transaction.commit();
						session.clear();
						status = true;
						map.put("message", "Bank Account Added :)");
					} else {
						throw new Exception("Please, fulfilling Bank Account Request.");

					}

				} catch (Exception e) {

					if (transaction != null) {
						transaction.rollback();
					}
					e.printStackTrace();
					status = false;
					map.put("message", e.getMessage());
				}

				session.close();
			} else {
				map.put("message", "Bank Account Save failed. The account holder is not found");

			}

		} else {
			map.put("message", "This Bank Account already added. Ac.No." + bankAccountReq.getAccountNo());
		}
		map.put("status", status);
		map.put("response", null);

	}

	@Override
	public BankAccount getBanksAccountById(String id) {

		Optional<BankAccount> optional = bankAccountRepository.getBankAccountByPublicId(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public void updateHaveAnyAccountTransactions(List<MethodAndTransaction> methodAndTransactions, Session session,
			Date date) {

		if (methodAndTransactions != null) {
			for (MethodAndTransaction methodAndTransaction : methodAndTransactions) {

				if (methodAndTransaction != null) {
					if (methodAndTransaction.getSource() != null || methodAndTransaction.getDestination() != null) {
						updateBankAccountViaTransaction(methodAndTransaction, session, date);
					}
				}
			}
		}

	}

	@Override
	public void updateBankAccountViaTransaction(MethodAndTransaction methodAndTransaction, Session session, Date date) {

		if (methodAndTransaction != null) {

			if (methodAndTransaction.getSource() != null && methodAndTransaction.getDestination() != null) {

				BankAccount sourceAccount = session.get(BankAccount.class, methodAndTransaction.getSource().getId());
				BankAccount destinationAccount = session.get(BankAccount.class,
						methodAndTransaction.getDestination().getId());

				BankAccountCredit sourceAccountCredit = null;
				BankAccountDebit destinationAccountDebit = null;

				if (sourceAccount != null) {

					double nSourchCredit = sourceAccount.getTotalCredit() + methodAndTransaction.getAmount();
					sourceAccount.setTotalCredit(nSourchCredit);
					session.update(sourceAccount);

					if (sourceAccount.getCredit() != null) {
						sourceAccountCredit = session.get(BankAccountCredit.class, sourceAccount.getCredit().getId());
					}

				}

				if (destinationAccount != null) {

					double destinationAcDebit = destinationAccount.getTotalDebit() + methodAndTransaction.getAmount();
					destinationAccount.setTotalDebit(destinationAcDebit);
					session.update(destinationAccount);

					if (destinationAccount.getDebit() != null) {
						destinationAccountDebit = session.get(BankAccountDebit.class,
								destinationAccount.getDebit().getId());
					}

				}

				if (sourceAccountCredit != null) {
					double nSourceCredit = sourceAccountCredit.getAmount() + methodAndTransaction.getAmount();
					sourceAccountCredit.setAmount(nSourceCredit);
					session.update(sourceAccountCredit);
				} else {
					sourceAccountCredit = esInitializerServices.initBankAccountCredit(sourceAccount, date);
					sourceAccountCredit.setAmount(methodAndTransaction.getAmount());
					session.persist(sourceAccountCredit);
				}

				if (destinationAccountDebit != null) {
					double nDestinationDebit = destinationAccountDebit.getAmount() + methodAndTransaction.getAmount();
					destinationAccountDebit.setAmount(nDestinationDebit);
					session.update(destinationAccountDebit);
				} else {
					destinationAccountDebit = esInitializerServices.initBankAccountDebit(destinationAccount, date);
					destinationAccountDebit.setAmount(methodAndTransaction.getAmount());
					session.persist(destinationAccountDebit);
				}

				BankAccountCreditRecord creditRecord = esInitializerServices.initBankCreditRecord(sourceAccountCredit,
						date);
				creditRecord.setAmount(methodAndTransaction.getAmount());
				creditRecord.setMethodTransaction(methodAndTransaction);
				creditRecord.setNote("Added this Transaction Ref. " + methodAndTransaction.getRefNo());

				BankAccountDebitRecord debitRecord = esInitializerServices.initBankDebitRecord(destinationAccountDebit,
						date);
				debitRecord.setAmount(methodAndTransaction.getAmount());
				debitRecord.setMethodAndTransaction(methodAndTransaction);
				debitRecord.setNote("Added this Transaction Ref. " + methodAndTransaction.getRefNo());

				debitRecord.setAccountCreditRecord(creditRecord);
				creditRecord.setAccountDebitRecord(debitRecord);

				session.persist(debitRecord);
				session.persist(creditRecord);

			} else {

				if (methodAndTransaction.getSource() != null) {

					BankAccount sourceAccount = session.get(BankAccount.class,
							methodAndTransaction.getSource().getId());

					BankAccountCredit sourceAccountCredit = null;

					if (sourceAccount != null) {

						double nSourceCredit = sourceAccount.getTotalCredit() + methodAndTransaction.getAmount();
						sourceAccount.setTotalCredit(nSourceCredit);
						session.persist(sourceAccount);

						if (sourceAccount.getCredit() != null) {
							sourceAccountCredit = session.get(BankAccountCredit.class,
									sourceAccount.getCredit().getId());
						}

					}

					if (sourceAccountCredit != null) {
						double nSourceCredit = sourceAccountCredit.getAmount() + methodAndTransaction.getAmount();
						sourceAccountCredit.setAmount(nSourceCredit);
						session.update(sourceAccountCredit);
					} else {
						sourceAccountCredit = esInitializerServices.initBankAccountCredit(sourceAccount, date);
						sourceAccountCredit.setAmount(methodAndTransaction.getAmount());
						session.persist(sourceAccountCredit);
					}

					BankAccountCreditRecord creditRecord = esInitializerServices
							.initBankCreditRecord(sourceAccountCredit, date);
					creditRecord.setAmount(methodAndTransaction.getAmount());
					creditRecord.setMethodTransaction(methodAndTransaction);
					creditRecord.setNote("Added this Transaction Ref. " + methodAndTransaction.getRefNo());

					session.persist(creditRecord);

				}

				if (methodAndTransaction.getDestination() != null) {

					BankAccount destinationAccount = session.get(BankAccount.class,
							methodAndTransaction.getDestination().getId());

					BankAccountDebit destinationAccountDebit = null;
					if (destinationAccount != null) {

						double destinationAcDebit = destinationAccount.getTotalDebit()
								+ methodAndTransaction.getAmount();
						destinationAccount.setTotalDebit(destinationAcDebit);
						session.update(destinationAccount);

						if (destinationAccount.getDebit() != null) {
							destinationAccountDebit = session.get(BankAccountDebit.class,
									destinationAccount.getDebit().getId());
						}

					}

					if (destinationAccountDebit != null) {
						double nDestinationDebit = destinationAccountDebit.getAmount()
								+ methodAndTransaction.getAmount();
						destinationAccountDebit.setAmount(nDestinationDebit);
						session.update(destinationAccountDebit);
					} else {
						destinationAccountDebit = esInitializerServices.initBankAccountDebit(destinationAccount, date);
						destinationAccountDebit.setAmount(methodAndTransaction.getAmount());
						session.persist(destinationAccountDebit);
					}

					BankAccountDebitRecord debitRecord = esInitializerServices
							.initBankDebitRecord(destinationAccountDebit, date);
					debitRecord.setAmount(methodAndTransaction.getAmount());
					debitRecord.setMethodAndTransaction(methodAndTransaction);
					debitRecord.setNote("Added this Transaction Ref. " + methodAndTransaction.getRefNo());

					session.persist(debitRecord);
				}

			}

		}

	}

	private TempBankAccount getBankTempActiveAccount(String id) {
		TempBankAccount bankAccount = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<TempBankAccount> criteriaQuery = criteriaBuilder.createQuery(TempBankAccount.class);
			Root<TempBankAccount> root = criteriaQuery.from(TempBankAccount.class);

			CriteriaQuery<TempBankAccount> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("refId"), id),
					criteriaBuilder.equal(root.get("active"), true)));
			TypedQuery<TempBankAccount> typedQuery = session.createQuery(select);

			bankAccount = typedQuery.getSingleResult();

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

		}
		session.close();

		return bankAccount;
	}

	private BankWithdraw getBankWithdrawById(String id) {

		BankWithdraw bankWithdraw = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankWithdraw> criteriaQuery = criteriaBuilder.createQuery(BankWithdraw.class);
			Root<BankWithdraw> root = criteriaQuery.from(BankWithdraw.class);

			CriteriaQuery<BankWithdraw> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<BankWithdraw> query = session.createQuery(select);

			bankWithdraw = query.getSingleResult();

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

		}
		session.close();

		return bankWithdraw;

	}

	private BankReceive getBankReceiveById(String id) {

		BankReceive bankReceive = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankReceive> criteriaQuery = criteriaBuilder.createQuery(BankReceive.class);
			Root<BankReceive> root = criteriaQuery.from(BankReceive.class);

			CriteriaQuery<BankReceive> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<BankReceive> query = session.createQuery(select);

			bankReceive = query.getSingleResult();

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

		}
		session.close();

		return bankReceive;
	}

	private BankPaid getBankPaidById(String id) {

		BankPaid bankPaid = null;

		Session session = sessionFactory.openSession();
		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankPaid> criteriaQuery = criteriaBuilder.createQuery(BankPaid.class);
			Root<BankPaid> root = criteriaQuery.from(BankPaid.class);

			CriteriaQuery<BankPaid> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			Query<BankPaid> query = session.createQuery(select);

			bankPaid = query.getSingleResult();

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

		}
		session.close();

		return bankPaid;

	}

	private boolean isAccountExist(String accountNo) {

		BankAccount account = getBanksAccountByAccountNo(accountNo);

		if (account != null) {
			return true;
		}

		return false;
	}

}
