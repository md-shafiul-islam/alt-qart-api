package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestPassowrdReq {

	private String oldPass;

	private String newPassword;

	private String confPassword;

	private String userId;
}
