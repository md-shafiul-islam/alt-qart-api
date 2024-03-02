package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespRank {

	private int id;

	private RespRange accessRange;

	private String name;

	private String description;

}
