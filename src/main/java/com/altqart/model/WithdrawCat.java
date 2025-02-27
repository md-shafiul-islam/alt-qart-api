package com.altqart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="withdraw_cat")
public class WithdrawCat {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
			
	private String name;
	
	private String description;
	
	
}
