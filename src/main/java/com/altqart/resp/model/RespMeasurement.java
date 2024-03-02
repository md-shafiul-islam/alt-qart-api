package com.altqart.resp.model;

import java.util.List;

import com.altqart.model.Product;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespMeasurement {

	private int id;

	private List<RespProduct> products;

	private double weight;

	private double lenght;

	private double width;

	private double height;

}
