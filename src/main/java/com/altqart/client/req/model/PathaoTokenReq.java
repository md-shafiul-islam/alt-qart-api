package com.altqart.client.req.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathaoTokenReq {

	@JsonProperty("client_id")
	private String clientId = "Test";

	@JsonProperty("client_secret")
	private String clientSecret = "client secret";

	private String username;

	private String password;

	@JsonProperty("grant_type")
	private String grantType; //grant_type || password
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	


}
