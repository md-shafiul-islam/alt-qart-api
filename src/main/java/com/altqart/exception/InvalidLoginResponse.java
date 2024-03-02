package com.altqart.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidLoginResponse {

	private String username;

	private String password;

	private String message;

	public InvalidLoginResponse() {

		username = "User not valid";
		password = "Password invalid";

	}

}
