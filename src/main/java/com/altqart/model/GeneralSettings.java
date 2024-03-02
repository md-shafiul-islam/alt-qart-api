package com.altqart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="general_setting")
public class GeneralSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
}
