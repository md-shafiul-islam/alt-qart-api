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
@Table(name = "color")
public class Color {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@OneToMany(mappedBy = "color")
	private List<Variant> variants;

	private String name;

	@Column(name = "c_key")
	private String cKey;

}
