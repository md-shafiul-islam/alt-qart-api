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
@Table(name = "image_gallery")
public class ImageGallery {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column
	private String name;

	@Column(name = "alt_tag", length = 105)
	private String altTag;

	@Column(length = 105)
	private String title;

	@Column(length = 205)
	private String location;

	@ManyToMany(mappedBy = "images")
	private Set<Product> product = new HashSet<>();

	@ManyToMany(mappedBy = "images")
	private Set<Post> posts = new HashSet<>();

	@ManyToMany(mappedBy = "images")
	private Set<News> news = new HashSet<>();
}
