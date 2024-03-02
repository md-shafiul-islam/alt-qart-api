package com.altqart.resp.model;

import com.altqart.model.Zone;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespArea {

	private int id;

	private RespZone zone;

	private String name;

	private String value;

	private int pathaoCode;
	
}
