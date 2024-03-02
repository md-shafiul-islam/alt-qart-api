package com.altqart.services.impl;

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
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankAccountMapper;
import com.altqart.mapper.StoreMapper;
import com.altqart.model.InvoiceSetting;
import com.altqart.model.NamePhoneNo;
import com.altqart.model.Store;
import com.altqart.model.StoreType;
import com.altqart.model.User;
import com.altqart.repository.NamePhoneRepository;
import com.altqart.repository.StoreRepository;
import com.altqart.repository.StoreTypeRepository;
import com.altqart.req.model.BankAccountReq;
import com.altqart.req.model.EsApprove;
import com.altqart.req.model.NamePhoneNoReq;
import com.altqart.req.model.StoreReq;
import com.altqart.resp.model.RespBankAccount;
import com.altqart.resp.model.RespInvoiceSetting;
import com.altqart.resp.model.RespNamePhoneNo;
import com.altqart.resp.model.RespStore;
import com.altqart.resp.model.RespStoreType;
import com.altqart.services.BankAccountServices;
import com.altqart.services.StoreServices;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreServicesImpl implements StoreServices {

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StoreTypeRepository storeTypeRepository;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private StoreMapper storeMapper;

	private SessionFactory sessionFactory;

	@Autowired
	private BankAccountServices bankAccountServices;

	@Autowired
	private BankAccountMapper accountMapper;

	@Autowired
	private NamePhoneRepository namePhoneRepository;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public Store getStoreById(int id) {

		Optional<Store> dbStore = storeRepository.findById(id);

		if (!dbStore.isEmpty() && dbStore.isPresent()) {
			return dbStore.get();
		}

		return null;
	}

	@Override
	public Store getDefaultStore() {

		Optional<Store> optional = storeRepository.findById(1);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public void updateStore(Store store) {

	}

	@Override
	public void addStore(Store store) {
		storeRepository.save(store);

	}

	@Override
	public void addApprove(EsApprove approve, Map<String, Object> map) {

		if (approve != null) {
			// TODO Auto-generated method stub
		}

	}

	@Override
	public void addStore(StoreReq storeReq, Map<String, Object> map) {

		if (storeReq != null) {

			Store store = storeMapper.mapStore(storeReq);

			if (store != null) {
				store.setPublicId(helperServices.getGenPublicId());
				storeRepository.save(store);

				if (store != null) {

					if (store.getId() > 0) {
						map.put("status", true);
						map.put("message", "Business Saved :)");
					} else {
						map.put("status", false);
						map.put("message", "Business Save failed");
					}
				}
			} else {
				map.put("status", false);
				map.put("message", "Business mapping failed");
			}
		}

	}

	@Override
	public List<RespStore> getAllStore(int i, int start, int size) {
		List<RespStore> respStores = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Store> criteriaQuery = criteriaBuilder.createQuery(Store.class);

			Root<Store> root = criteriaQuery.from(Store.class);
			CriteriaQuery<Store> select = criteriaQuery.select(root);

			Query<Store> query = session.createQuery(select);

			List<Store> stores = query.getResultList();

			if (stores != null) {
				respStores = storeMapper.mapAllRespStore(stores);
			}

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();
		}

		session.clear();
		session.close();

		return respStores;
	}

	@Override
	public RespStore getRespStoreById(String id) {

		RespStore respStore = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Store> criteriaQuery = criteriaBuilder.createQuery(Store.class);

			Root<Store> root = criteriaQuery.from(Store.class);
			CriteriaQuery<Store> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Store> query = session.createQuery(select);

			Store store = query.getSingleResult();

			if (store != null) {

				respStore = storeMapper.mapRespStore(store);
			}

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();
		}

		session.clear();
		session.close();

		return respStore;
	}

	@Override
	public void update(StoreReq storeReq, Map<String, Object> map) {

		Store store = getStoreByPublicId(storeReq.getId());

		if (store != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();

				Store dbStore = session.get(Store.class, storeReq.getId());

				if (isStoreCheckChangeAndSet(dbStore, storeReq, session)) {

					session.update(dbStore);
					map.put("status", true);
					map.put("message", "Store information updated");

				} else {
					throw new Exception("Store information update failed");
				}

				transaction.commit();
				session.clear();
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();

				map.put("status", false);
				map.put("message", e.getMessage());
			}

		} else {
			map.put("message", "Business not found by ID");
		}

	}

	private Store getStoreByPublicId(String id) {
		if (!helperServices.isNullOrEmpty(id)) {
			Optional<Store> optional = storeRepository.getStoreByPublicId(id);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}
		return null;
	}

	@Override
	public void addBankAccount(BankAccountReq bankAccountReq, Map<String, Object> map) {

		Store store = getStoreByPublicId(bankAccountReq.getStore());
		map.put("message", "Business not found. Bank account add failed!!");
		if (bankAccountReq != null && store != null) {
			bankAccountServices.saveBankAccountViaBusiness(bankAccountReq, store, map);
		}

	}

	@Override
	public void getAllBankAccountByStore(String id, Map<String, Object> map) {

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Store> criteriaQuery = criteriaBuilder.createQuery(Store.class);

			Root<Store> root = criteriaQuery.from(Store.class);
			CriteriaQuery<Store> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Store> query = session.createQuery(select);

			Store store = query.getSingleResult();
			if (store != null) {

				List<RespBankAccount> respBankAccounts = accountMapper
						.mapAllBusinessRespBankAccount(store.getBanAccounts());

				map.put("status", true);
				map.put("message", "Business Account");
				map.put("response", respBankAccounts);
			}

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();
			map.put("status", false);
			map.put("message", e.getMessage());
		}

		session.clear();
		session.close();

	}

	@Override
	public List<RespStoreType> getAllRespStoreType() {

		List<RespStoreType> respStoreTypes = null;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<StoreType> criteriaQuery = criteriaBuilder.createQuery(StoreType.class);

			Root<StoreType> root = criteriaQuery.from(StoreType.class);
			CriteriaQuery<StoreType> select = criteriaQuery.select(root);

			Query<StoreType> query = session.createQuery(select);

			List<StoreType> storeTypes = query.getResultList();

			if (storeTypes != null) {
				respStoreTypes = storeMapper.mapAllRespStoreType(storeTypes);

			}

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");
			e.printStackTrace();

		}

		session.clear();
		session.close();

		return respStoreTypes;

	}

	private boolean isStoreCheckChangeAndSet(Store dbStore, StoreReq storeReq, Session session) throws Exception {

		boolean status = false;

		if (helperServices.isNotEqualAndFirstOneIsNotNull(storeReq.getName(), dbStore.getName())) {

			String roleName = null;
			boolean autStatus = false;

			User dbUser = session.get(User.class, authenticationServices.getCurrentUser().getId());
			if (dbUser != null) {
				if (dbUser.getRole() != null) {
					roleName = dbUser.getRole().getName();
					autStatus = dbUser.getRole().isSupper();

				}
			}

			if (helperServices.isEqual(roleName, "Administrator") || helperServices.isEqual(roleName, "Supper Admin")
					|| helperServices.isEqual(roleName, "System Administrator")) {

				if (autStatus) {
					dbStore.setName(dbStore.getName());
					status = true;
				} else {
					throw new Exception("Can't Change Or Update Business Name. Please Contact Administrator");
				}

			} else {
				throw new Exception("Can't Change Or Update Business Name. Please Contact Administrator");
			}

		}

		if (helperServices.isNotEqualAndFirstOneIsNotNull(storeReq.getStartLine(), dbStore.getStartLine())) {
			dbStore.setStartLine(storeReq.getStartLine());
			status = true;
		}

		if (helperServices.isNotEqualAndFirstOneIsNotNull(storeReq.getEmail(), dbStore.getEmail())) {
			dbStore.setEmail(storeReq.getEmail());
			status = true;
		}

		if (helperServices.isNotEqualAndFirstOneIsNotNull(storeReq.getProprietor(), dbStore.getProprietor())) {
			dbStore.setProprietor(storeReq.getProprietor());
			status = true;
		}

		if (helperServices.isNotEqualAndFirstOneIsNotNull(storeReq.getDescription(), dbStore.getDescription())) {
			dbStore.setDescription(storeReq.getDescription());
			status = true;
		}

		if (!helperServices.isNullOrEmpty(storeReq.getBusinessType())) {

			StoreType storeType = getRespStoreTypeByValue(storeReq.getBusinessType());
			StoreType dbStoreType = null;
			if (storeType != null) {
				dbStoreType = session.get(StoreType.class, storeReq.getId());
			} else {
				throw new Exception("BusinessType Not found By ID ");

			}

			if (dbStore.getStoreType() != null) {

				if (dbStore.getStoreType().getId() != dbStoreType.getId()) {
					dbStore.setStoreType(dbStoreType);
					status = true;
				}
			} else {
				dbStore.setStoreType(dbStoreType);
				status = true;
			}

		}

		return status;
	}

	private StoreType getRespStoreTypeByValue(String businessType) {
		Optional<StoreType> optional = storeTypeRepository.getStoreTypeByValue(businessType);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public boolean updateApprove(EsApprove approve) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getAllStoreNamePhoneNo(String id, Map<String, Object> map) {

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Store> criteriaQuery = criteriaBuilder.createQuery(Store.class);

			Root<Store> root = criteriaQuery.from(Store.class);
			CriteriaQuery<Store> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<Store> query = session.createQuery(select);

			Store store = query.getSingleResult();

			if (store != null) {

				List<RespNamePhoneNo> namePhoneNos = storeMapper.mapAllRespNamePhoneNo(store.getNamePhoneNos());

				if (namePhoneNos != null) {
					map.put("status", true);
					map.put("response", namePhoneNos);
					map.put("message", " Business Name Phone No " + namePhoneNos.size() + "(s) found");
				}
			}

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();
			map.put("status", false);
			map.put("message", " Business Name Phone No. not found");
		}

		session.clear();
		session.close();

	}

	@Override
	public void addStoreNamePhoneNo(String id, NamePhoneNoReq namePhoneNo, Map<String, Object> map) {

		Store store = getStoreById(id);

		if (store != null) {

			NamePhoneNo nNamePhoneNo = storeMapper.mapNameAndPhoneNo(namePhoneNo);

			if (nNamePhoneNo != null) {
				nNamePhoneNo.setStore(store);
				namePhoneRepository.save(nNamePhoneNo);

				if (nNamePhoneNo.getId() > 0) {
					map.put("status", true);
					map.put("message", "Name And Phone No. Is save.");
				}
			}

		}

	}

	@Override
	public void updateStoreNamePhoneNo(String id, NamePhoneNoReq namePhoneNo, Map<String, Object> map) {

		User user = authenticationServices.getCurrentUser();

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			User dbUser = session.get(User.class, user.getId());
			if (dbUser != null) {
				log.info("dbUser.getRole().getName() " + dbUser.getRole().getName());

				if (helperServices.isEqual(dbUser.getRole().getName(), "Administrator")
						|| helperServices.isEqual(dbUser.getRole().getName(), "administrator")
						|| helperServices.isEqual(dbUser.getRole().getName(), "owner")
						|| helperServices.isEqual(dbUser.getRole().getName(), "manager")) {
					NamePhoneNo dbNamePhoneNo = session.get(NamePhoneNo.class, namePhoneNo.getId());
					if (!isCheckkChangeAnyFieldAndSet(namePhoneNo, dbNamePhoneNo, session)) {

						session.update(dbNamePhoneNo);

					}
					transaction.commit();
					session.clear();
					map.put("message", "Phone Name is updated");
					map.put("status", true);
				} else {
					throw new Exception("You can't change Phone & Name. Please, Contact Administrator");
				}
			} else {
				throw new Exception("You can't change Phone & Name. Please, Contact Administrator");
			}

		} catch (Exception e) {

			map.put("message", e.getMessage());
			e.printStackTrace();
			map.put("status", false);
		}

	}

	@Override
	public RespNamePhoneNo getStoreByPhoneId(int phoneId) {

		RespNamePhoneNo namePhoneNo = null;

		Session session = sessionFactory.openSession();

		try {

			NamePhoneNo dbNamePhoneNo = session.get(NamePhoneNo.class, phoneId);

			if (dbNamePhoneNo != null) {

				namePhoneNo = storeMapper.mapRespNamePhoneNo(dbNamePhoneNo);
			}

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();

		}

		session.clear();
		session.close();

		return namePhoneNo;
	}

	@Override
	public void removeStoreNamePhone(String id, NamePhoneNoReq namePhoneNo, Map<String, Object> map) {

		if (helperServices.isValidAndLenghtCheck(id, 32) && namePhoneNo != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				NamePhoneNo dbNamePhoneNo = session.get(NamePhoneNo.class, namePhoneNo.getId());

				if (dbNamePhoneNo.getStore() != null) {

					if (helperServices.isEqual(dbNamePhoneNo.getStore().getPublicId(), id)) {
						map.put("message", dbNamePhoneNo.getName() + " No." + dbNamePhoneNo.getPhoneNo()
								+ " এই নাম ও নাম্বারটি মুছে ফেলা হয়েছে");
						session.remove(dbNamePhoneNo);
					}
				}

				transaction.commit();
				session.clear();
				map.put("status", true);

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();

				map.put("message", e.getMessage());
			}
			session.clear();
		}

	}

	private boolean isCheckkChangeAnyFieldAndSet(NamePhoneNoReq namePhoneNo, NamePhoneNo dbNamePhoneNo,
			Session session) {

		boolean status = false;
		if (helperServices.isNotEqualAndFirstOneIsNotNull(namePhoneNo.getName(), dbNamePhoneNo.getName())) {
			dbNamePhoneNo.setName(namePhoneNo.getName());
			status = true;
		}

		if (helperServices.isNotEqualAndFirstOneIsNotNull(namePhoneNo.getPhoneNo(), dbNamePhoneNo.getPhoneNo())) {
			dbNamePhoneNo.setPhoneNo(namePhoneNo.getPhoneNo());
			status = true;
		}

		if (namePhoneNo.isOffice() != dbNamePhoneNo.isOffice()) {
			dbNamePhoneNo.setOffice(namePhoneNo.isOffice());
			status = true;
		}

		return status;
	}

	@Override
	public Store getStoreById(String id) {

		if (!helperServices.isNullOrEmpty(id)) {
			Optional<Store> optional = storeRepository.getStoreByPublicId(id);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}
		return null;
	}

	@Override
	public void addOrUpdaPhoneNoAll(List<NamePhoneNoReq> namePhoneNoReqs, String store, Map<String, Object> map) {
		if (namePhoneNoReqs != null) {
			Session session = sessionFactory.openSession();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();

				transaction.commit();
				session.clear();
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			}

		}

	}

}
