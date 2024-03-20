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
@Table(name = "comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "content")
	private String content;

	@ManyToOne()
	@JoinColumn(name = "author", referencedColumnName = "id")
	private User author;

	@ManyToOne()
	@JoinColumn(name = "approve_user", referencedColumnName = "id")
	private User approveUser;

	@ManyToOne()
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;

	@ManyToOne()
	@JoinColumn(name = "news", referencedColumnName = "id")
	private News news;

	@ManyToOne()
	@JoinColumn(name = "post", referencedColumnName = "id")
	private Post post;

//	  @ManyToOne
//	  @JoinTable(inverseJoinColumns = @JoinTable(name="comment_sub", ))
////	  Comment parent;
//	  Set<Comment> comments;
//
//	  @OneToMany


	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;

}
