package com.altqart.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id", unique = true)
	private String publicId;

	@ManyToOne()
	@JoinColumn(name = "author", referencedColumnName = "id")
	private User author;

	@ManyToOne()
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;

	@ManyToOne()
	@JoinColumn(name = "approve_user", referencedColumnName = "id")
	private User approveUser;

	@Column(name = "rate_score")
	private double rateScore;

	@Column
	private String content;

	@Temporal(TemporalType.DATE)
	private Date date;

}
