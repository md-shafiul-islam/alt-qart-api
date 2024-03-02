package com.altqart.services.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankBranchMapper;
import com.altqart.model.Bank;
import com.altqart.model.BankBranch;
import com.altqart.repository.BankBranchRepository;
import com.altqart.req.model.BankBranchReq;
import com.altqart.resp.model.RespBankBranch;
import com.altqart.services.BankBranchServices;
import com.altqart.services.BankServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankBranchServicesImpl implements BankBranchServices {

	@Autowired
	private BankBranchRepository bankBranchRepository;

	@Autowired
	private BankBranchMapper bankBranchMapper;

	@Autowired
	private BankServices bankServices;

	@Autowired
	private HelperServices helperServices;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespBankBranch> getAllBankBranchs() {
		List<RespBankBranch> respBankBranchs = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankBranch> criteriaQuery = criteriaBuilder.createQuery(BankBranch.class);
			Root<BankBranch> root = criteriaQuery.from(BankBranch.class);

			CriteriaQuery<BankBranch> select = criteriaQuery.select(root);

			TypedQuery<BankBranch> typedQuery = session.createQuery(select);

			List<BankBranch> bankBranchs = typedQuery.getResultList();

			if (bankBranchs != null) {
				respBankBranchs = bankBranchMapper.mapAllRespBankBranch(bankBranchs);

			} else {
				System.out.println("Get Banks Not found ");
			}

			session.clear();

		} catch (Exception e) {

			e.printStackTrace();

		}
		session.close();

		return respBankBranchs;
	}

	@Override
	public BankBranch getBankBranchByKey(String branch) {

		Optional<BankBranch> optional = bankBranchRepository.getBankBranchByKey(branch);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public RespBankBranch getBankById(int id) {

		RespBankBranch respBankBranch = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankBranch> criteriaQuery = criteriaBuilder.createQuery(BankBranch.class);
			Root<BankBranch> root = criteriaQuery.from(BankBranch.class);

			CriteriaQuery<BankBranch> select = criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
			TypedQuery<BankBranch> typedQuery = session.createQuery(select);

			BankBranch bankBranch = typedQuery.getSingleResult();

			if (bankBranch != null) {

				respBankBranch = bankBranchMapper.mapRespBankBranch(bankBranch);

			} else {
				System.out.println("Get Banks Not found ");
			}

			session.clear();

		} catch (Exception e) {
			log.info("getBankById " + e.getMessage());

		}
		session.close();

		return respBankBranch;
	}

	@Override
	public boolean save(BankBranchReq bankBranchReq) {

		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		Bank bank = bankServices.getBankById(bankBranchReq.getBank());
		if (bank != null) {
			try {
				transaction = session.beginTransaction();
				Bank dbBank = session.get(Bank.class, bank.getId());
				if (dbBank != null) {
					BankBranch bankBranch = new BankBranch();
					bankBranch.setAddress(bankBranchReq.getAddress());
					bankBranch.setBank(dbBank);

					if (helperServices.isNullOrEmpty(bankBranchReq.getKey())) {
						bankBranch.setKey(
								helperServices.getStringReplaceAll(bankBranchReq.getName(), "_", " ").toLowerCase());
					} else {
						bankBranch.setKey(bankBranchReq.getKey());
					}

					bankBranch.setName(bankBranchReq.getName());
					bankBranch.setPhoneNo(bankBranchReq.getPhoneNo());

					session.persist(bankBranch);

					transaction.commit();
					session.clear();
					status = true;
				}

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}

				status = false;
				e.printStackTrace();
			}
		}

		session.close();

		return status;
	}

	@Override
	public boolean update(BankBranchReq bankBranchReq) {

		boolean status = false;

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			BankBranch bankBranch = session.get(BankBranch.class, bankBranchReq.getBank());

			if (!helperServices.isNullOrEmpty(bankBranchReq.getAddress())) {
				bankBranch.setAddress(bankBranchReq.getAddress());
			}

			if (!helperServices.isNullOrEmpty(bankBranchReq.getName())) {
				bankBranch.setName(bankBranchReq.getName());
			}

			if (!helperServices.isNullOrEmpty(bankBranchReq.getPhoneNo())) {
				bankBranch.setPhoneNo(bankBranchReq.getPhoneNo());
			}

			if (bankBranch.getBank().getPublicId() != bankBranchReq.getBank()) {
				Bank bank = session.get(Bank.class, bankBranchReq.getBank());

				if (bank != null) {
					bankBranch.setBank(bank);
				}
			}

			session.update(bankBranch);

			transaction.commit();
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
	public BankBranch getBankBranchById(int branch) {

		Optional<BankBranch> optional = bankBranchRepository.findById(branch);

		if (optional.isPresent() && !optional.isEmpty()) {

			return optional.get();
		}
		return null;
	}

}
