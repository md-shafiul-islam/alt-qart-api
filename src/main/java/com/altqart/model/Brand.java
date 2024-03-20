package com.altqart.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "brand")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;
	
	@Column(name="b_key")
	private String key;

	@Column
	private String name;

	@OneToMany(mappedBy = "brand")
	private List<Product> products;

	@Column(name = "description")
	private String description;

	@Column(name = "tag_line")
	private String tagLine;

	@Column(name = "logo_url")
	private String logoUrl;

	@Column(name = "web_url")
	private String webSite;
	
	private Date date;

}
