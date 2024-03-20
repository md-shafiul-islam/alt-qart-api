package com.altqart.services.impl;

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
import com.altqart.client.req.model.ParcelPriceReq;
import com.altqart.client.req.model.PathaoTokenReq;
import com.altqart.client.resp.model.RespPathaoPrice;
import com.altqart.helper.services.HelperServices;
import com.altqart.model.Parcel;
import com.altqart.model.ParcelProvider;
import com.altqart.repository.PathaoTokenRepository;
import com.altqart.services.ParcelClientService;
import com.altqart.services.ParcelProviderServices;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParcelClientServiceImpl implements ParcelClientService {

	@Autowired
	private ParcelProviderServices parcelProviderServices;

	@Autowired
	private HelperServices helperServices;

	RestClient restClient = RestClient.create();

	private String validToken;

	private ParcelProvider parcelProvider;

	private PathaoToken pathaoToken;

	@Autowired
	private PathaoTokenRepository pathaoTokenRepository;

	private SessionFactory sessionFactory;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public Object initParcelPricing(String baseUrl, String uri) {

		restClient = RestClient.builder().baseUrl(baseUrl).build();

		PathaoTokenReq pathaoTokenReq = new PathaoTokenReq();

		restClient.post().body(pathaoTokenReq);

		return null;
	}

	@Override
	public void getToken() {

		initParcelProvider();
		PathaoTokenReq pathaoTokenReq = new PathaoTokenReq();

		pathaoTokenReq.setClientId(parcelProvider.getClientId());
		pathaoTokenReq.setClientSecret(parcelProvider.getSecret());
		pathaoTokenReq.setGrantType("password");
		pathaoTokenReq.setPassword(parcelProvider.getPassword());
		pathaoTokenReq.setUsername(parcelProvider.getUsername());

		PathaoToken respToken = restClient.post().uri(parcelProvider.getBaseUrl() + parcelProvider.getTokenLink())
				.accept(MediaType.APPLICATION_JSON).body(pathaoTokenReq).retrieve().body(PathaoToken.class);

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			if (respToken == null) {
				throw new Exception("Geting Pathao Token failed");
			}

			PathaoToken dbPathaoToken = null;
			boolean isCreate = false;
			if (pathaoToken != null) {
				dbPathaoToken = session.get(PathaoToken.class, pathaoToken.getId());
			} else {
				dbPathaoToken = new PathaoToken();
				dbPathaoToken.setKey("pathao");
				isCreate = true;
			}

			dbPathaoToken.setAccessToken(respToken.getAccessToken());
			dbPathaoToken.setExpiresDate(respToken.getExpiresDate());
			dbPathaoToken.setExpiresIn(respToken.getExpiresIn());
			dbPathaoToken.setRefreshToken(respToken.getRefreshToken());
			dbPathaoToken.setTokenType(respToken.getTokenType());

			if (isCreate) {
				session.persist(dbPathaoToken);
			} else {
				session.merge(dbPathaoToken);
			}
			transaction.commit();
			session.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initParcelProvider() {

		if (parcelProvider == null) {
			parcelProvider = parcelProviderServices.getParcelProviderByKey("pathao");
		}

	}

	public Object getCreateOrRefreshToken(Map<String, Object> map) {

//		log.info("Creating Or Updating Validate Token");

		initParcelProvider();
		PathaoTokenReq pathaoTokenReq = new PathaoTokenReq();

		pathaoTokenReq.setClientId(parcelProvider.getClientId());
		pathaoTokenReq.setClientSecret(parcelProvider.getSecret());
		pathaoTokenReq.setGrantType("password");
		pathaoTokenReq.setPassword(parcelProvider.getPassword());
		pathaoTokenReq.setUsername(parcelProvider.getUsername());

		PathaoToken respToken = restClient.post().uri(parcelProvider.getBaseUrl() + parcelProvider.getTokenLink())
				.accept(MediaType.APPLICATION_JSON).body(pathaoTokenReq).retrieve().body(PathaoToken.class);

		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			if (respToken == null) {
				throw new Exception("Geting Pathao Token failed");
			}

			PathaoToken dbPathaoToken = null;
			boolean isCreate = false;
			if (pathaoToken != null) {
				dbPathaoToken = session.get(PathaoToken.class, pathaoToken.getId());
			} else {
				dbPathaoToken = new PathaoToken();
				dbPathaoToken.setKey("pathao");
				isCreate = true;
			}

			dbPathaoToken.setAccessToken(respToken.getAccessToken());
			dbPathaoToken.setExpiresDate(respToken.getExpiresDate());
			dbPathaoToken.setExpiresIn(respToken.getExpiresIn());
			dbPathaoToken.setRefreshToken(respToken.getRefreshToken());
			dbPathaoToken.setTokenType(respToken.getTokenType());

			if (isCreate) {
				session.persist(dbPathaoToken);
				map.put("message", "Token Created");

			} else {
				session.merge(dbPathaoToken);
				map.put("message", "Token Updated via create Or Update");
			}
			transaction.commit();
			session.clear();
			map.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respToken;
	}

	@Override
	public Object getCreateParcel(String baseUrl, String uri, String token) {

		String bearToken = !helperServices.isNullOrEmpty(token) ? token : this.validToken;
		String stText = "ygydbiadiuadia";
		Parcel parcel = new Parcel();
		parcel.setAmountToCollect(350);
//		parcel.setDeliveryFee(110);
		parcel.setItemDescription("One pic T-Shirt");
		parcel.setItemQuantity(1);
		parcel.setItemType(2);
		parcel.setItemWeight(.5);
		parcel.setMerchantOrderId("AQ 14578 21547 32541 12436");
		parcel.setRecipientAddress("dokkhin hossainpur, madrasha para. Mohadevpur, naogaon");
		parcel.setRecipientArea(9740);
		parcel.setRecipientCity(46);
		parcel.setRecipientName("Md Shafiul Islam");
		parcel.setRecipientPhone("01839619264");
		parcel.setRecipientZone(761);
		parcel.setSpecialInstruction("Handel with care");
		parcel.setSenderPhone("01725686029");
		parcel.setSenderName("Md Shahidul Islam");
		parcel.setStoreId(130744);

		log.info("Header stText " + stText);
		log.info("Header bearToken " + bearToken);

		return restClient.post().uri(baseUrl + uri).headers(httpHeaders -> {
			httpHeaders.add("Authorization", (bearToken));

		}).accept(MediaType.APPLICATION_JSON).body(parcel).retrieve().body(Object.class);
	}

	@Override
	public void getValidateToken(Map<String, Object> map) {

		boolean status = false;

		initParcelProvider();
		initPathaoToken();
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();
			PathaoTokenReq pathaoTokenReq = new PathaoTokenReq();

			pathaoTokenReq.setClientId(parcelProvider.getClientId());
			pathaoTokenReq.setClientSecret(parcelProvider.getSecret());
			pathaoTokenReq.setGrantType("refresh_token");
			pathaoTokenReq.setRefreshToken(pathaoToken.getRefreshToken());

			PathaoToken respToken = restClient.post().uri(parcelProvider.getBaseUrl() + parcelProvider.getTokenLink())
					.accept(MediaType.APPLICATION_JSON).body(pathaoTokenReq).retrieve().body(PathaoToken.class);

			if (respToken == null) {
				throw new Exception("Geting Refresh Pathao Token failed");
			}

			if (pathaoToken != null) {

				PathaoToken dbPathaoToken = session.get(PathaoToken.class, pathaoToken.getId());
				dbPathaoToken.setAccessToken(respToken.getAccessToken());
				dbPathaoToken.setExpiresDate(respToken.getExpiresDate());
				dbPathaoToken.setExpiresIn(respToken.getExpiresIn());
				dbPathaoToken.setRefreshToken(respToken.getRefreshToken());
				dbPathaoToken.setTokenType(respToken.getTokenType());
				session.merge(dbPathaoToken);
			}

			transaction.commit();
			session.clear();
			map.put("message", "Pathao Token Updated");
			map.put("status", true);
			status = true;
			map.put("refreshStatus", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", e.getMessage());

			status = false;
		}
		session.close();

		if (!status) {
			map.put("refreshStatus", false);
			Object nToken = getCreateOrRefreshToken(map);
			map.put("response", nToken);
		}
	}

	@Override
	public void getParcelPrice(ParcelPriceReq parcelPriceReq, Map<String, Object> map) {

		initParcelProvider();
		initPathaoToken();

		log.info("Store Id: " + parcelProvider.getStoreId());

		if (parcelPriceReq != null) {
			parcelPriceReq.setStoreId(parcelProvider.getStoreId());
			parcelPriceReq.setDeliveryType(48);
			parcelPriceReq.setItemType(2);

			if (0 >= parcelPriceReq.getItemWeight()) {
				parcelPriceReq.setItemWeight(0.280);
			}

//			map.put("priceReq", parcelPriceReq);

			RespPathaoPrice respPrice = restClient.post()
					.uri(parcelProvider.getBaseUrl() + parcelProvider.getPriceingLink()).headers(httpHeaders -> {
						httpHeaders.add("Authorization",
								(pathaoToken.getTokenType() + " " + pathaoToken.getAccessToken()));

					}).accept(MediaType.APPLICATION_JSON).body(parcelPriceReq).retrieve().body(RespPathaoPrice.class);

			map.put("response", respPrice.getData());
			map.put("status", true);
			map.put("message", "Get Parcel Price");

		}

	}

	private void initPathaoToken() {

		if (pathaoToken == null) {
			Optional<PathaoToken> optional = pathaoTokenRepository.getPathaoTokenByKey("pathao");

			if (optional.isPresent() && !optional.isEmpty()) {
				pathaoToken = optional.get();
			}
		}

	}

}
