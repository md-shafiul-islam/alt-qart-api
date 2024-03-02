package com.altqart.services.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankMapper;
import com.altqart.model.Bank;
import com.altqart.model.BankType;
import com.altqart.repository.BankRepository;
import com.altqart.repository.BankTypeRepository;
import com.altqart.req.model.BankReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankType;
import com.altqart.services.BankServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankServicesImpl implements BankServices {

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private BankMapper bankMapper;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	private SessionFactory sessionFactory;

	@Autowired
	private BankTypeRepository bankTypeRepository;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespBank> getAllBanks(int start, int size) {

		List<RespBank> respBanks = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Bank> criteriaQuery = criteriaBuilder.createQuery(Bank.class);
			Root<Bank> root = criteriaQuery.from(Bank.class);

			CriteriaQuery<Bank> select = criteriaQuery.select(root);
			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Bank> typedQuery = session.createQuery(select);
			typedQuery.setFirstResult(start);
			typedQuery.setMaxResults(size);

			List<Bank> banks = typedQuery.getResultList();

			if (banks != null) {

				respBanks = bankMapper.mapAllRespBank(banks);

			} else {
				System.out.println("Get Banks Not found ");
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllBanks " + e.getMessage());

		}
		session.close();

		return respBanks;

	}

	@Override
	public RespBank getBankByPublicId(String id) {

		RespBank respBank = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Bank> criteriaQuery = criteriaBuilder.createQuery(Bank.class);
			Root<Bank> root = criteriaQuery.from(Bank.class);

			CriteriaQuery<Bank> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));
			TypedQuery<Bank> typedQuery = session.createQuery(select);

			Bank bank = typedQuery.getSingleResult();

			if (bank != null) {

				respBank = bankMapper.mapRespBank(bank);

			} else {
				System.out.println("Get Bank Not found ");
			}

			session.clear();

		} catch (Exception e) {

			log.info("getBankByPublicId " + e.getMessage());

		}
		session.close();

		return respBank;
	}

	@Override
	public boolean save(BankReq bankReq) {
		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			Bank bank = bankMapper.mapBank(bankReq);
			bank.setBankType(bankTypeRepository.getBankTypeByKey(bankReq.getBankType()));
			if (bank != null) {
				session.save(bank);

				transaction.commit();
			}

			session.clear();
			status = true;
		} catch (Exception e) {
			e.printStackTrace();

			if (transaction != null) {
				transaction.rollback();
			}

			status = false;
		}
		session.close();
		return status;
	}

	@Override
	public boolean update(BankReq bankReq) {
		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		Bank bank = getBankById(bankReq.getId());
		try {
			transaction = session.beginTransaction();
			Bank dbBank = session.get(Bank.class, bank.getId());

			if (dbBank != null) {

				if (!helperServices.isNullOrEmpty(bankReq.getDescription())) {
					dbBank.setDescription(bankReq.getDescription());
				}

				if (!helperServices.isNullOrEmpty(bankReq.getName())) {
					dbBank.setName(bankReq.getName());
				}

				if (!helperServices.isNullOrEmpty(bankReq.getLogoUrl())) {
					dbBank.setLogoUrl(bankReq.getLogoUrl());
				}

				session.update(dbBank);

				transaction.commit();
			}

			session.clear();
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}

			status = false;
		}
		session.close();
		return status;
	}

	@Override
	public Bank getBankById(String publicId) {

		Optional<Bank> optional = bankRepository.getBankByPublicId(publicId);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public Bank getBankById(int bank) {

		Optional<Bank> optional = bankRepository.findById(bank);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<RespBankType> getAllBankType() {

		List<RespBankType> respBankTypes = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankType> criteriaQuery = criteriaBuilder.createQuery(BankType.class);
			Root<BankType> root = criteriaQuery.from(BankType.class);

			CriteriaQuery<BankType> select = criteriaQuery.select(root);
			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<BankType> typedQuery = session.createQuery(select);

			List<BankType> bankTypes = typedQuery.getResultList();

			if (bankTypes != null) {

				respBankTypes = bankMapper.mapAllRespBankType(bankTypes);

			} else {
				System.out.println("Get Banks Not found ");
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllBanks " + e.getMessage());

		}
		session.close();

		return respBankTypes;
	}

}
