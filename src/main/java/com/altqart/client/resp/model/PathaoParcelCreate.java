package com.altqart.client.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathaoParcelCreate {

	private String message;

	private String type;

	private int code;

	private PathaoParcelInf data;
}
