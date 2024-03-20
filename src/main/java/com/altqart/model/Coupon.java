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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	private String code;

	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@Column(name = "apply_amount")
	private double applyAmount; // -1 is any amount;

	@Column(name = "apply_qty")
	private double applyQty; // -1 is any amount;

	private double amount;// 0-any

	private double percentage; // 0-100

	private boolean active;

	private boolean valid;

	private double count;

	@Column(name = "expire_date")
	private Date expireDate;

	@Column(name = "create_date")
	private Date createDate;

}
