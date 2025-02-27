package com.altqart.resp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBankType {

	private int id;

	private String name;

	private String description;

	private String key;

	private List<RespBank> banks;
}
