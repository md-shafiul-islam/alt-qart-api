package com.altqart.model.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class Authority implements GrantedAuthority{

	
	private static final long serialVersionUID = 1L;
	
	private final String authority;
	
	public Authority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}
	

}
