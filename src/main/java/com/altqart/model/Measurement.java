package com.altqart.model;

import java.util.List;

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
@Table(name = "measurement")
public class Measurement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@OneToMany(mappedBy = "measurement")
	private List<Product> products;

	private double weight;

	private double lenght;

	private double width;

	private double height;
}
