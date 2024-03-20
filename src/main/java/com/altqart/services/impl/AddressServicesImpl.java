package com.altqart.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AddressMapper;
import com.altqart.model.Address;
import com.altqart.model.Order;
import com.altqart.model.Stakeholder;
import com.altqart.model.User;
import com.altqart.repository.AddressRepository;
import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.RespMinAddress;
import com.altqart.resp.model.RespOrder;
import com.altqart.resp.model.SearchWordReq;
import com.altqart.services.AddressServices;
import com.altqart.services.StakeholderServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AddressServicesImpl implements AddressServices {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private StakeholderServices stakeholderServices;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public void getAll(Map<String, Object> map, int start, int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(AddressReq addressReq, Map<String, Object> map) {

		Stakeholder stakeholder = stakeholderServices.getStakeholderByPublicId(addressReq.getStakeholder());

		if (addressReq != null && stakeholder != null) {

			Address address = addressMapper.mapAddress(addressReq);
			address.setStakeholder(stakeholder);

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				Stakeholder dbStakeholder = session.get(Stakeholder.class, stakeholder.getId());

				if (!helperServices.isNullOrEmpty(dbStakeholder.getName())) {
					dbStakeholder.setName(address.getFullName());
					dbStakeholder.setPhoneNo(address.getPhoneNo());

					session.merge(dbStakeholder);

				}

				if (dbStakeholder.getUser() == null) {
					Date date = new Date();
					User user = new User();
					user.setPhoneNo(address.getPhoneNo());
					user.setEnabled(1);
					user.setName(dbStakeholder.getName());
					user.setPublicId(helperServices.getGenPublicId());
					user.setCode(helperServices.getUserGenId());
					user.setStakeholder(dbStakeholder);
					user.setUdate(date);
					user.setDate(date);
					session.persist(user);
				}
				session.persist(address);

				transaction.commit();
				session.clear();

				map.put("status", true);
				map.put("message", "Address added successfully");

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
				map.put("status", false);
				map.put("message", "Address added failed");
			}

			session.close();
		}

	}

	@Override
	public void getStakeholderAddress(Map<String, Object> map) {

		User user = authenticationServices.getCurrentUser();

		if (user != null) {

			Session session = sessionFactory.openSession();

			try {
				User dbUser = session.get(User.class, user.getId());
				if (dbUser != null) {

					if (dbUser.getStakeholder() != null) {
						if (dbUser.getStakeholder().getAddresses() != null) {

							List<RespMinAddress> addresses = addressMapper
									.mapAllRespAddressMin(dbUser.getStakeholder().getAddresses());

							map.put("status", true);
							map.put("message", "Stakeholder Address found");
							map.put("response", addresses);
						}
					}
				}
			} catch (Exception e) {
				map.put("status", false);
				map.put("message", "Stakeholder Address not found");
			}
		}

	}

	@Override
	public void remove(AddressReq addressReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(AddressReq addressReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllSearchSuggestion(SearchWordReq words, Map<String, Object> map) {

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			log.info("Search Words " + words);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public Address getAddressById(String address) {

		Optional<Address> optional = addressRepository.getAddressByPublicId(address);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public void getStakeholderAddressById(String id, Map<String, Object> map) {

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Address> criteriaQuery = criteriaBuilder.createQuery(Address.class);
			Root<Address> root = criteriaQuery.from(Address.class);

			CriteriaQuery<Address> select = criteriaQuery.select(root);

			select.where(criteriaBuilder.equal(root.get("stakeholder").get("publicId"), id));

			select.orderBy(criteriaBuilder.desc(root.get("id")));
			TypedQuery<Address> typedQuery = session.createQuery(select);

			List<Address> addresses = typedQuery.getResultList();

			if (addresses != null) {

				map.put("response", addressMapper.mapAllRespAddressMin(addresses));
				map.put("status", true);
				map.put("message", " Address found by Stakeholder");
			}

		} catch (Exception e) {

			map.put("status", false);
			map.put("message", e.getMessage());
		}

		session.clear();
		session.close();

	}

}
