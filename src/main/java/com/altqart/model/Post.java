package com.altqart.model;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "post")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id", unique = true)
	private String publicId;

	@Column(name = "alias_name", unique = true)
	private String aliasName;

	@Column()
	private String title;

	@Column()
	private String content;

	@ManyToOne()
	@JoinColumn(name = "approve_user", referencedColumnName = "id")
	private User user;

	@ManyToMany
	@JoinTable(name = "post_images", joinColumns = { @JoinColumn(name = "post") }, inverseJoinColumns = {
			@JoinColumn(name = "image") })
	private Set<ImageGallery> images;

	@ManyToOne()
	@JoinColumn(name = "author", referencedColumnName = "id")
	private User author;

	@ManyToMany
	@JoinTable(name = "post_meta", joinColumns = { @JoinColumn(name = "post") }, inverseJoinColumns = {
			@JoinColumn(name = "meta") })
	private Set<MetaData> metaDatas;

	@ManyToOne()
	@JoinColumn(name = "brand", referencedColumnName = "id")
	private Brand brand;

	@ManyToOne()
	@JoinColumn(name = "category", referencedColumnName = "id")
	private Category category;

	@Column(name = "create_date")
	private Date createdDate;

	@Column(name = "update_date")
	private Date updateDate;
}
