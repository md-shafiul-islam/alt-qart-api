package com.altqart.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@OneToMany(mappedBy = "city")
	private List<Address> address;

	private int code;

	private String name;

	@Column(name = "c_key")
	private String key;

	@Column(name = "pathao_code")
	private int pathaoCode;

	@OneToMany(mappedBy = "city")
	private List<Zone> zones;

}
