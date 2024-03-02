package com.altqart.resp.model;

import com.altqart.model.Product;
import com.altqart.model.SpecKey;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespSpecification {

	private RespSpecKey key;

	private String value;

	private String description;

	private RespProduct product;

	private boolean isFeature;
}
