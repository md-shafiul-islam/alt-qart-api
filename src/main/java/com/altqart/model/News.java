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
@Table(name = "news")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "alias_name")
	private String aliasName;

	@Column()
	private String title;

	@ManyToOne()
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@Column
	private String content;
	
	@ManyToMany
	@JoinTable(name = "news_images", joinColumns = { @JoinColumn(name = "news") }, inverseJoinColumns = {
			@JoinColumn(name = "image") })
	private Set<ImageGallery> images;

	@ManyToMany
	@JoinTable(name = "news_meta", joinColumns = { @JoinColumn(name = "news") }, inverseJoinColumns = {
			@JoinColumn(name = "meta") })
	private Set<MetaData> metaDatas;

	@ManyToOne()
	@JoinColumn(name = "category", referencedColumnName = "id")
	private Category category;

	@Column(name = "create_date")
	private Date crateDate;

	@Column(name = "update_date")
	private Date updateDate;
}
