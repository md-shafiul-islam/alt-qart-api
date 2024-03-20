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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Access;
import com.altqart.model.Cart;
import com.altqart.model.Credential;
import com.altqart.model.Stakeholder;
import com.altqart.model.User;
import com.altqart.repository.UserRepositry;
import com.altqart.req.model.UserReq;
import com.altqart.resp.model.EsInitInf;
import com.altqart.resp.model.RespEsUser;
import com.altqart.resp.model.RespUser;
import com.altqart.services.UserServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServicesImpl implements UserServices {

	@Autowired
	private UserRepositry userRepository;

	private SessionFactory sessionFactory;

	@Autowired
	private HelperServices helperServices;

	private PasswordEncoder passwordEncoder;

	@Autowired
	private EsInitializerServices esInitializerServices;

	private User cUser;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	private void initPasswordEncoder() {

		if (passwordEncoder == null) {
			passwordEncoder = new BCryptPasswordEncoder();
		}

	}

	@Override
	public User getUserByPublicID(String userId) {

		return userRepository.getUserByPublicId(userId);
	}

	@Override
	public User getUserById(int id) {
		User user = null;
		if (id > 0) {
			Session session = sessionFactory.openSession();
			try {

				user = session.get(User.class, id);
				if (user != null) {
					user.getAuthorities();
					if (user.getRole() != null) {
						if (user.getRole().getAccesses() != null) {
							for (Access access : user.getRole().getAccesses()) {
								if (access != null) {
									if (access.getAccessType() != null) {
										access.getAccessType().getName();
									}

								}
							}
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	@Override
	public User getOnlyUserByPublicID(String userId) {
		if (helperServices.isValidAndLenghtCheck(userId, 30)) {
			Optional<User> optional = userRepository.findUserByPublicId(userId);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}
		return null;
	}

	@Override
	public User getCurrentUser() {
		return cUser;
	}

	@Override
	public List<User> getAllUser() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public void signUpUser(UserReq userReq, Map<String, Object> map) {
		initPasswordEncoder();
		if (userReq != null) {
			initPasswordEncoder();
			User user = userMapper.getUser(userReq);

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				Date date = new Date();
				transaction = session.beginTransaction();
				user.setEnabled(1);
				user.setUdate(date);
				user.setDate(date);

				session.persist(user);

				Stakeholder stakeholder = esInitializerServices.initStakeholderViaUser(user, date);
				session.persist(stakeholder);

				Cart cart = esInitializerServices.initCart(date);
				cart.setStakeholder(stakeholder);
				session.persist(cart);

				if (!helperServices.isNullOrEmpty(userReq.getPassword())) {
					Credential credential = new Credential();
					credential.setDate(date);
					credential.setPassword(passwordEncoder.encode(userReq.getPassword()));
					credential.setStatus(1);
					credential.setUser(user);
					session.persist(credential);
				}

				transaction.commit();
				session.clear();

				map.put("message", "User Saved");
				map.put("status", true);

			} catch (Exception e) {

				if (transaction != null) {

					transaction.rollback();
				}
				map.put("message", e.getMessage());
				map.put("status", false);
			}

			session.close();
		}

	}

	@Override
	public boolean saveUser(User user) {

		if (user.getId() > 0) {
			return false;
		} else {

			user.setCredentials(new ArrayList<>());

			Credential credential = new Credential();
			credential.setDate(new Date());
			credential.setStatus(1);
			credential.setUser(user);

			credential.setPassword(getIncription("123456789"));
			user.setPublicId(getUnicId());

			user.getCredentials().add(credential);

			return false;

		}

	}

	@Override
	public String getUnicId() {

		boolean status = true;

		String key = null;
		while (status) {
			key = helperServices.getUnicId();
			if (!isKeyOrIdExist(key)) {
				status = false;
			}
		}

		return key;
	}

	@Override
	public boolean updatePassword(Credential credential) {

		String encPass = this.getIncription(credential.getPassword());
		credential.setPassword(encPass);

		if (credential.getUser() != null) {

			return updateCredential(credential);
		}

		return false;
	}

	@Override
	public long getCount() {
		return userRepository.count();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = getUserByString(username);

		if (user == null) {

			System.out.println("User Name not found: " + username);
			throw new UsernameNotFoundException(username + " User Name not found");
		} else {
			System.out.println("User Found:  " + user.getName());
		}

		return user;
	}

	private User getUserByString(String username) {

		return getUserByUsername(username);
	}

	@Override
	public User getUserByUsername(String userName) {

		User user = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

			Root<User> root = criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("username"), userName));

			Query<User> query = session.createQuery(criteriaQuery);

			user = query.getSingleResult();

			if (user.getStore() != null) {
				user.getStore().getPublicId();
			}

			session.clear();
		} catch (Exception e) {

			log.info("getUserByUsername " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();
		cUser = user;
		return user;
	}

	@Override
	public User getUserByUserNameAndPass(String name, String password) {

		if (name != null && password != null) {
			// TODO: Get User By password and userName
		}
		return null;
	}

	@Override
	public User getUserByPhoneNo(String phoneNo) {
		User user = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

			Root<User> root = criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("phoneNo"), phoneNo));

			Query<User> query = session.createQuery(criteriaQuery);

			user = query.getSingleResult();

			if (user.getStore() != null) {
				user.getStore().getPublicId();
			}

			session.clear();
		} catch (Exception e) {

			log.info("getUserByUsername " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();
		cUser = user;
		return user;
	}

	@Override
	public User getUserByEmail(String email) {

		User user = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

			Root<User> root = criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("email"), email));

			Query<User> query = session.createQuery(criteriaQuery);

			user = query.getSingleResult();

			if (user.getStore() != null) {
				user.getStore().getPublicId();
			}

			session.clear();
		} catch (Exception e) {

			log.info("getUserByUsername " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();
		cUser = user;
		return user;

	}

	@Override
	public List<RespUser> getAllRespUserOnly() {
		List<RespUser> respUsers = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

			Root<User> root = criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			Query<User> query = session.createQuery(criteriaQuery);

			List<User> users = query.getResultList();

			if (users != null) {
				respUsers = userMapper.mapAllRespUser(users);
			}

			session.clear();
		} catch (Exception e) {

			log.info("getUserByUsername " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();

		return respUsers;
	}

	@Override
	public List<RespEsUser> getAllRespEsUserOnly() {
		List<RespEsUser> respUsers = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

			Root<User> root = criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			Query<User> query = session.createQuery(criteriaQuery);

			List<User> users = query.getResultList();

			if (users != null) {
				respUsers = userMapper.mapAllRespEsUser(users);
			}

			session.clear();
		} catch (Exception e) {

			log.info("getUserByUsername " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();

		return respUsers;
	}

	@Override
	public RespUser getUserDetailsByPublicID(String id) {
		RespUser respUser = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

			Root<User> root = criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("publicId"), id));

			Query<User> query = session.createQuery(criteriaQuery);

			User user = query.getSingleResult();

			if (user != null) {
				respUser = userMapper.mapRespUserDetails(user);
			}

			session.clear();
		} catch (Exception e) {

			log.info("getUserByUsername " + e.getMessage());

//			e.printStackTrace();
		}
		session.close();
		return respUser;
	}

	@Override
	public boolean updateUserLocked(int locked, User user) {

		boolean status = false;

		if (user != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				User dbUser = session.get(User.class, user.getId());
				if (dbUser.getLocked() != locked) {
					dbUser.setLocked(locked);
					session.update(dbUser);
				}

				transaction.commit();
				session.clear();
				status = true;
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				status = false;
			}

			session.clear();
		}

		return status;
	}

	@Override
	public boolean updateUserEnabled(int enabled, User user) {
		boolean status = false;

		if (user != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				User dbUser = session.get(User.class, user.getId());
				if (dbUser.getEnabled() != enabled) {
					dbUser.setEnabled(enabled);
					session.update(dbUser);
				}

				transaction.commit();
				session.clear();
				status = true;
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				status = false;
			}

			session.clear();
		}

		return status;
	}
	// Public Method End

	// Private Method
	@SuppressWarnings("unchecked")
	private boolean updateCredential(Credential credential) {

		boolean status = false;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaUpdate<Credential> criteriaQuery = criteriaBuilder.createCriteriaUpdate(Credential.class);
			Root<Credential> root = criteriaQuery.from(Credential.class);
			criteriaQuery.set(root.get("status"), 0);
			criteriaQuery.where(criteriaBuilder.equal(root.get("user").get("id"), credential.getUser().getId()));

			Query<Credential> query = session.createQuery(criteriaQuery);

			int affectedRow = query.executeUpdate();

			if (affectedRow >= 0) {
				session.save(credential);
			}

			System.out.println("After Session Clear and close !!");

			transaction.commit();

			if (credential.getId() > 0) {
				status = true;
			}
			session.clear();

		} catch (Exception e) {
			status = false;
			if (transaction != null) {

				transaction.rollback();
			}
		}
		session.close();

		return status;
	}

	@Override
	public void createEsPlaceholderUser(String empty, Map<String, Object> map) {

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			Date date = new Date();
			transaction = session.beginTransaction();
			User user = new User();
			user.setCode(helperServices.getUserGenId());
			user.setPlaceToken(helperServices.getGenPublicId());
			user.setAuthUser(helperServices.getProductPublicId());
			user.setDate(date);
			user.setEnabled(1);
			user.setUdate(date);
			user.setPublicId(helperServices.getGenPublicId());

			Stakeholder stakeholder = new Stakeholder();
			stakeholder.setActive(true);
			stakeholder.setApprove(1);
			stakeholder.setDate(date);
			stakeholder.setPublicId(helperServices.getGenPublicId());

			Cart cart = new Cart();
			cart.setStakeholder(stakeholder);
			cart.setDate(date);
			cart.setUpdateDate(date);
			cart.setPublicId(helperServices.getGenPublicId());

			session.persist(user);
			session.persist(stakeholder);
			session.persist(cart);

			transaction.commit();
			session.clear();

			EsInitInf esInitInf = new EsInitInf();
			esInitInf.setEsInf(user.getPlaceToken());
			esInitInf.setId(user.getAuthUser());

			map.put("status", true);
			map.put("message", "Initialize successfully");
			map.put("response", esInitInf);
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Override
	public void getEsTokenBySequence(String id, Map<String, Object> map) {

		boolean status = false;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = (CriteriaQuery<User>) criteriaBuilder.createQuery(User.class);

			Root<User> root = (Root<User>) criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("phoneNo"), id));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

			Query<User> query = session.createQuery(criteriaQuery);

			User user = query.getSingleResult();

			session.clear();

		} catch (Exception e) {
			status = false;
			if (transaction != null) {

				transaction.rollback();
			}
		}
		session.close();

	}

	@Override
	public User getUserByEsUserAuth(String id) {

		log.info("getUserByEsUserAuth ....");

		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = (CriteriaQuery<User>) criteriaBuilder.createQuery(User.class);

			Root<User> root = (Root<User>) criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("authUser"), id));

			Query<User> query = session.createQuery(criteriaQuery);

			user = query.getSingleResult();

			log.info("User Found By Token" + user.getCode());

			session.clear();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
			e.printStackTrace();
		}
		session.close();

		return user;
	}

	@Override
	public User getUserByPlsToken(String esInf) {

		log.info("getUserByPlsToken ...");

		User user = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = (CriteriaQuery<User>) criteriaBuilder.createQuery(User.class);

			Root<User> root = (Root<User>) criteriaQuery.from(User.class);

			criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("placeToken"), esInf));

			Query<User> query = session.createQuery(criteriaQuery);

			user = query.getSingleResult();

			log.info("Get By Token " + user.getCode());

			session.clear();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
			e.printStackTrace();
		}
		session.close();

		return user;

	}

	private boolean isKeyOrIdExist(String key) {

		return userRepository.getUserByPublicId(key) != null;
	}

	private String getIncription(String getnPass) {
		initPasswordEncoder();
		return passwordEncoder.encode(getnPass);
	}

}
