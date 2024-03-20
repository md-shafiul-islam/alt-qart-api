package com.altqart.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parcel_provider")
public class ParcelProvider {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToMany(mappedBy = "parcelProvider", fetch = FetchType.LAZY)
	private List<Parcel> parcels;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "base_url")
	private String baseUrl;

	@Column(name = "client_id")
	private String clientId;

	private String name;

	@Column(name = "value")
	private String key;

	@Column(name = "client_id_key")
	private String idOrKey; // String Or Id

	@Column(name = "store_id")
	private int storeId;

	private String secret;

	private String email;

	private String password;

	private String username;

	@Column(name = "grant_type")
	private String grantType; // password/token

	@Column(name = "webhook")
	private String webHookSecret;

	@Column(name = "token_link")
	private String tokenLink = "/aladdin/api/v1/issue-token";

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "oredre_link")
	private String oredreLink;

	@Column(name = "bulk_oredre_link")
	private String bulkOredreLink;

	@Column(name = "short_link")
	private String shortLink;

	@Column(name = "store_link")
	private String storeLink = "/aladdin/api/v1/stores";

	@Column(name = "priceing_link")
	private String priceingLink;

}
