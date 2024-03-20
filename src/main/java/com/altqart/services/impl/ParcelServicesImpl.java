package com.altqart.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.altqart.client.model.PathaoToken;
import com.altqart.client.model.PathaoWebHook;
import com.altqart.client.req.model.PathaoWebHookReq;
import com.altqart.client.resp.model.PathaoParcelCreate;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.ParcelMapper;
import com.altqart.model.Order;
import com.altqart.model.Parcel;
import com.altqart.model.ParcelProvider;
import com.altqart.model.ParcelStatus;
import com.altqart.repository.ParcelProviderRepository;
import com.altqart.repository.ParcelRepository;
import com.altqart.repository.ParcelStatusRepository;
import com.altqart.repository.PathaoTokenRepository;
import com.altqart.req.model.ParcelReq;
import com.altqart.resp.model.RespParcel;
import com.altqart.services.ParcelProviderServices;
import com.altqart.services.ParcelServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParcelServicesImpl implements ParcelServices {

	@Autowired
	private PathaoTokenRepository pathaoTokenRepository;

	@Autowired
	private ParcelProviderServices parcelProviderServices;

	@Autowired
	private ParcelStatusRepository parcelStatusRepository;

	private SessionFactory sessionFactory;

	private PathaoToken pathaoToken;

	private RestClient restClient = RestClient.create();

	@Autowired
	private ParcelMapper parcelMapper;

	@Autowired
	private ParcelRepository parcelRepository;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public void getAllParcel(Map<String, Object> map, int start, int size, String type) {

		List<RespParcel> parcels = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Parcel> criteriaQuery = criteriaBuilder.createQuery(Parcel.class);
			Root<Parcel> root = criteriaQuery.from(Parcel.class);

			CriteriaQuery<Parcel> select = criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("parcelStatus").get("slug"), type));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			TypedQuery<Parcel> typedQuery = session.createQuery(select);

			if (start > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			List<Parcel> parcelList = typedQuery.getResultList();

			if (parcelList != null) {

				parcels = parcelMapper.mapAllParcel(parcelList);

				map.put("response", parcels);
				map.put("status", true);
				map.put("message", "Parcel found");

			} else {
				System.out.println("Get Parcels Not found ");
			}

		} catch (Exception e) {

			e.printStackTrace();
			map.put("status", false);
			map.put("message", "Parcel not found");
		}

		session.close();
	}

	@Override
	public void addParcel(ParcelReq parcelReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateParcel(ParcelReq parcelReq, Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getParcelById(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPathaoParcel(Parcel parcel, Map<String, Object> map) {

		initPathaoToke();

		if (parcel != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();

				Parcel dbParcel = session.get(Parcel.class, parcel.getId());

				if (dbParcel == null) {
					throw new Exception("Parcel not found");
				}

				if (dbParcel.getStatus() > 0) {
					throw new Exception("Parcel Already " + dbParcel.getParcelStatus().getLabel());
				}

				Parcel tempParcel = parcelMapper.mapTempParcel(dbParcel);
				if (dbParcel != null) {
					if (dbParcel.getParcelProvider() != null) {
						PathaoParcelCreate create = (PathaoParcelCreate) restClient.post()
								.uri(dbParcel.getParcelProvider().getBaseUrl()
										+ dbParcel.getParcelProvider().getOredreLink())
								.headers(httpHeaders -> {
									httpHeaders.add("Authorization",
											(pathaoToken.getTokenType() + " " + pathaoToken.getAccessToken()));

								}).accept(MediaType.APPLICATION_JSON).body(tempParcel).retrieve()
								.body(PathaoParcelCreate.class);
						map.put("parcel", create);
						if (create != null) {

							if (create.getCode() == 200 && create.getData() != null) {
								dbParcel.setDeliveryFee(create.getData().getDeliveryFee());
								dbParcel.setConsignmentId(create.getData().getConsignmentId());
								ParcelStatus parcelStatus = getParcelStatusByPathaoKey(
										create.getData().getOrderStatus());
								dbParcel.setParcelStatus(parcelStatus);
								if (parcelStatus != null) {
									dbParcel.setStatus(parcelStatus.getId());
								}

								log.info("Parcel Track ID " + create.getData().getConsignmentId());

								map.put("message", "Order Approved & Parcel Created");
							} else {
								map.put("message", "Order Approved & Parcel Create failed");
							}
						}
					}

					log.info("Parcel Status " + dbParcel.getStatus());

					if (dbParcel.getStatus() > 0) {
						if (dbParcel.getOrder() != null) {
							Order dbOrder = session.get(Order.class, dbParcel.getOrder().getId());
							dbOrder.setTrackingNo(dbParcel.getConsignmentId());
							session.merge(dbOrder);
						}
					}

					session.merge(dbParcel);
				}

				transaction.commit();
				session.clear();
			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				map.put("message", "Order Approved Parce Exc. " + e.getMessage());
				e.printStackTrace();
			}

			session.close();
		}
	}

	@Override
	public Map<String, Object> updatePathaoParcelViaWebHook(PathaoWebHookReq webHookReq, String givenKey) {

		log.info("Service Web hooke ... ");

		ParcelProvider parcelProvider = parcelProviderServices.getParcelProviderByWebHookSecret(givenKey);

		if (webHookReq != null) {
			PathaoWebHook pathaoWebHook = parcelMapper.mapPathaoWebHook(webHookReq);

		}

		return null;
	}

	@Override
	public void getAllDeliveredParcel(Map<String, Object> map, int start, int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllDeliveryFailedParcel(Map<String, Object> map, int start, int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllPaymentInvoiceParcel(Map<String, Object> map, int start, int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllPendingParcel(Map<String, Object> map, int start, int size) {

		List<RespParcel> parcels = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Parcel> criteriaQuery = criteriaBuilder.createQuery(Parcel.class);
			Root<Parcel> root = criteriaQuery.from(Parcel.class);

			CriteriaQuery<Parcel> select = criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("status"), 0));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			TypedQuery<Parcel> typedQuery = session.createQuery(select);

			if (start > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			List<Parcel> parcelList = typedQuery.getResultList();

			if (parcelList != null) {

				parcels = parcelMapper.mapAllParcel(parcelList);

				map.put("response", parcels);
				map.put("status", true);
				map.put("message", parcelList.size() + " Pending Parcel found");

			} else {
				System.out.println("Get Parcels Not found ");
			}

		} catch (Exception e) {

			e.printStackTrace();
			map.put("status", false);
			map.put("message", "Pending Parcel not found");
		}

		session.close();

	}

	@Override
	public void getAllReturnParcel(Map<String, Object> map, int start, int size) {

		List<RespParcel> parcels = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Parcel> criteriaQuery = criteriaBuilder.createQuery(Parcel.class);
			Root<Parcel> root = criteriaQuery.from(Parcel.class);

			CriteriaQuery<Parcel> select = criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.equal(root.get("parcelStatus").get("slug"), "return"));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			TypedQuery<Parcel> typedQuery = session.createQuery(select);

			if (start > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			List<Parcel> parcelList = typedQuery.getResultList();

			if (parcelList != null) {

				parcels = parcelMapper.mapAllParcel(parcelList);

				map.put("response", parcels);
				map.put("status", true);
				map.put("message", "Parcel found");

			} else {
				System.out.println("Get Parcels Not found ");
			}

		} catch (Exception e) {

			e.printStackTrace();
			map.put("status", false);
			map.put("message", "Parcel not found");
		}

		session.close();

	}

	@Override
	public void getAllSendParcel(Map<String, Object> map, int start, int size) {

		List<RespParcel> parcels = null;

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Parcel> criteriaQuery = criteriaBuilder.createQuery(Parcel.class);
			Root<Parcel> root = criteriaQuery.from(Parcel.class);

			CriteriaQuery<Parcel> select = criteriaQuery.select(root);

			criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get("status"), 1));
			select.orderBy(criteriaBuilder.desc(root.get("id")));

			TypedQuery<Parcel> typedQuery = session.createQuery(select);

			if (start > 0) {
				typedQuery.setFirstResult(start);
				typedQuery.setMaxResults(size);
			}

			List<Parcel> parcelList = typedQuery.getResultList();

			if (parcelList != null) {

				parcels = parcelMapper.mapAllParcel(parcelList);

				map.put("response", parcels);
				map.put("status", true);
				map.put("message", parcelList.size() + " Send Parcel found");

			} else {
				System.out.println("Get Parcels Not found ");
			}

		} catch (Exception e) {

			e.printStackTrace();
			map.put("status", false);
			map.put("message", "Send Parcel not found");
		}

		session.close();

	}

	private ParcelStatus getParcelStatusByPathaoKey(String orderStatus) {

		Optional<ParcelStatus> optional = parcelStatusRepository.getParcelStatusByPathaoKey(orderStatus);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public void sendParcel(String id, Map<String, Object> map) {
		initPathaoToke();

		Parcel parcel = getParcelByPubId(id);

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			if (parcel == null) {
				throw new Exception("Parcel not found by Id");
			}
			Parcel dbParcel = session.get(Parcel.class, parcel.getId());

			Parcel tempParcel = parcelMapper.mapTempParcel(dbParcel);

			if (dbParcel != null) {
				if (dbParcel.getParcelProvider() != null) {
					PathaoParcelCreate create = restClient.post().uri(
							dbParcel.getParcelProvider().getBaseUrl() + dbParcel.getParcelProvider().getOredreLink())
							.headers(httpHeaders -> {
								httpHeaders.add("Authorization",
										(pathaoToken.getTokenType() + " " + pathaoToken.getAccessToken()));

							}).accept(MediaType.APPLICATION_JSON).body(tempParcel).retrieve()
							.body(PathaoParcelCreate.class);

					if (create.getData() != null && dbParcel.getOrder() != null) {

						if (!helperServices.isNullOrEmpty(create.getData().getConsignmentId())) {
							Order dbOrder = session.get(Order.class, dbParcel.getOrder().getId());
							dbOrder.setTrackingNo(create.getData().getConsignmentId());
							session.merge(dbOrder);
							dbParcel.setConsignmentId(create.getData().getConsignmentId());
							dbParcel.setDeliveryFee(create.getData().getDeliveryFee());

							ParcelStatus parcelStatus = getParcelStatusByPathaoKey(create.getData().getOrderStatus());
							dbParcel.setParcelStatus(parcelStatus);
							dbParcel.setStatus(parcelStatus.getId());
						} else {
							throw new Exception("Client Parcel Create failed");
						}
					}
				}

			}

			session.merge(dbParcel);

			transaction.commit();
			session.clear();
			map.put("message", "Parcel Send successfully");
			map.put("status", true);
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			map.put("message", "Parcel Send Failed Exc. " + e.getMessage());
		}

		session.close();
	}

	private Parcel getParcelByPubId(String id) {

		Optional<Parcel> optional = parcelRepository.getParcelByPublicId(id);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	private ParcelStatus getParcelStatusByKey(String orderStatus) {

		Optional<ParcelStatus> optional = parcelStatusRepository.getParcelStatusBySlug(orderStatus);

		if (optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}

		return null;
	}

	private void initPathaoToke() {

		Optional<PathaoToken> optional = pathaoTokenRepository.getPathaoTokenByKey("pathao");

		if (optional.isPresent() && !optional.isEmpty()) {
			pathaoToken = optional.get();
		}

	}

}
