package com.altqart.client.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAlias;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pathao_token")
public class PathaoToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name="value")
	private String key;
	
	@JsonAlias("token_type")
	@Column(name = "token_type")
	private String tokenType;

	@JsonAlias("expires_in")
	@Column(name = "expires_in")
	private String expiresIn;

	@JsonAlias("access_token")
	@Column(name = "access_token")
	private String accessToken;

	@JsonAlias("refresh_token")
	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "expires_date")
	private Date expiresDate;
}
