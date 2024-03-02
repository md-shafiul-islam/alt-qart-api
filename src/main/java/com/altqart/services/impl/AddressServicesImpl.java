package com.altqart.services.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.mapper.AddressMapper;
import com.altqart.model.Address;
import com.altqart.model.User;
import com.altqart.repository.AddressRepository;
import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.RespAddress;
import com.altqart.resp.model.SearchWordReq;
import com.altqart.services.AddressServices;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AddressServicesImpl implements AddressServices {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

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

		User user = authenticationServices.getCurrentUser();

		if (addressReq != null && user != null) {

			Address address = addressMapper.mapAddress(addressReq);

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				User dbUser = session.get(User.class, user.getId());

				if (dbUser.getStakeholder() != null) {

					address.setStakeholder(dbUser.getStakeholder());
					session.persist(address);
				}

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

							List<RespAddress> addresses = addressMapper
									.mapAllRespAddress(dbUser.getStakeholder().getAddresses());

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

}
