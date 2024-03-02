package com.altqart.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id", unique = true)
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "store", referencedColumnName = "id")
	private Store store;

	@Column(name = "alias_name")
	private String aliasName;

	@Column(length = 205)
	private String title;

	@Column(name = "bn_title")
	private String bnTitle;

	@OneToMany(mappedBy = "product")
	List<ProductDescription> descriptions;

	@OneToMany(mappedBy = "product")
	private List<Variant> variants;
	
	@OneToMany(mappedBy = "product")
	private List<ItemColor> itemColors;

	@ManyToOne
	@JoinColumn(name = "measurement", referencedColumnName = "id")
	private Measurement measurement;

	@Column(name = "discount_status")
	private boolean discountStatus;

	@Column(name = "is_upcoming")
	private boolean upcoming;

	@Column(name = "video_url")
	private String videoUrl;

	@ManyToOne
	@JoinColumn(name = "category", referencedColumnName = "id")
	private Category category;

	@OneToMany
	private List<CartItem> cartItems;

	@ManyToMany
	@JoinTable(name = "product_images", joinColumns = { @JoinColumn(name = "product") }, inverseJoinColumns = {
			@JoinColumn(name = "image") })
	private Set<ImageGallery> images = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "product_meta", joinColumns = { @JoinColumn(name = "product") }, inverseJoinColumns = {
			@JoinColumn(name = "meta") })
	private Set<MetaData> metaDatas = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "brand", referencedColumnName = "id")
	private Brand brand;

	@ManyToMany
	@JoinTable(name = "product_material", joinColumns = { @JoinColumn(name = "product") }, inverseJoinColumns = {
			@JoinColumn(name = "material") })
	private Set<Material> materials = new HashSet<>();

	@OneToMany(mappedBy = "product")
	private List<Specification> specifications;

	@OneToMany(mappedBy = "product")
	private List<Rating> ratings;

	@Column(name = "avg_rating")
	private double avgRating;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;

}
