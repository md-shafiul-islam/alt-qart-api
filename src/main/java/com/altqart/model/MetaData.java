package com.altqart.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "meta_data")
public class MetaData {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(length = 75)
	private String name;

	@Column()
	private String content;

	@ManyToMany(mappedBy = "metaDatas")
	private Set<Product> products = new HashSet<>();

	@ManyToMany()
	private Set<Post> posts = new HashSet<>();

	@ManyToMany()
	private Set<News> news = new HashSet<>();
}
