package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBankAccountType {

	private int id;

	private List<RespBankAccount> accounts = new ArrayList<>();

	private String name, value;

	private String description;


}
