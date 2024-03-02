package com.altqart.req.model;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqLoginData implements Serializable{

	private static final long serialVersionUID = 1587283384432333718L;

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
}
