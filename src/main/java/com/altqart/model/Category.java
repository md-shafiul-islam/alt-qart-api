package com.altqart.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;

	private String value;

	private String description;

	@ManyToOne
	@JoinColumn(name = "parent", referencedColumnName = "id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	private List<Category> subCategories;

	@OneToMany(mappedBy = "category")
	private List<Product> products;

	@Column(name = "is_sub", columnDefinition = "boolean default false")
	private boolean isSub;

}
