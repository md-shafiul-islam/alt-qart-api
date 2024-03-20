package com.altqart.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "store")
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", referencedColumnName = "id")
	private User user;

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<BankAccount> banAccounts;

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<Product> products;

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<Order> orders;

	@Column(name = "start_line")
	private String startLine;

	@Column(name = "logo_url")
	private String logoUrl;
	
	@Column(name="pathao_id")
	private int pathaoId;

	private String name;

	private String description;

	private String proprietor;

//	@OneToMany(mappedBy = "")
//	private List<Address> addresses;

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<DailyStatistics> dailyStatistics;

	private String email;

	@ManyToOne
	@JoinColumn(name = "store_type")
	private StoreType storeType;

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<NamePhoneNo> namePhoneNos;
	
	

}
