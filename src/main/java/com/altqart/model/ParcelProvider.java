package com.altqart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "base_url")
	private String baseUrl;

	@Column(name = "client_id_key")
	private String idOrKey; // String Or Id

	private String secret;

	private String email;

	private String password;

	@Column(name = "grant_type")
	private String grantType; // password/token

}
