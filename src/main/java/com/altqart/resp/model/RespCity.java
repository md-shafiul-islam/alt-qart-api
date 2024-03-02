package com.altqart.resp.model;

import java.util.List;

import com.altqart.model.Address;
import com.altqart.model.Zone;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespCity {

	private int id;
	
	private int code;

	private String name;

	private String key;

	private int pathaoCode;

	private List<RespZone> zones;
}
