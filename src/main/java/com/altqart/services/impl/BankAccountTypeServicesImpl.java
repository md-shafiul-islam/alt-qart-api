package com.altqart.services.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.BankAccountTypeMapper;
import com.altqart.model.BankAccountType;
import com.altqart.repository.BankAccountTypeRepository;
import com.altqart.req.model.BankAccountTypeReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankAccountType;
import com.altqart.services.BankAccountTypeServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankAccountTypeServicesImpl implements BankAccountTypeServices {

	private SessionFactory sessionFactory;

	@Autowired
	private BankAccountTypeRepository bankAccountTypeRepository;

	@Autowired
	private BankAccountTypeMapper bankAccountTypeMapper;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespBankAccountType> getAllBankAccountTypes() {

		List<RespBankAccountType> bankAccountTypes = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<BankAccountType> criteriaQuery = criteriaBuilder.createQuery(BankAccountType.class);
			Root<BankAccountType> root = criteriaQuery.from(BankAccountType.class);

			CriteriaQuery<BankAccountType> select = criteriaQuery.select(root);
			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<BankAccountType> typedQuery = session.createQuery(select);

			List<BankAccountType> accountTypes = typedQuery.getResultList();

			if (accountTypes != null) {

				bankAccountTypes = bankAccountTypeMapper.mapAllRespBankAccountType(accountTypes);

			} else {
				System.out.println("Get Banks Not found ");
			}

			session.clear();

		} catch (Exception e) {

			log.info("getAllBanks " + e.getMessage());

		}
		session.close();

		return bankAccountTypes;
	}

	@Override
	public RespBank getBankAccountTypeById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(BankAccountTypeReq bankAccountTypeReq) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(BankAccountTypeReq bankAccountTypeReq) {
		// TODO Auto-generated method stub
		return false;
	}

}
