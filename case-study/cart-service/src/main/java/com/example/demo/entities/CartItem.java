package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cartitems")
@Data
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartItemId;
	private Long productId;
	private String productName;
	private double totalPrice;
	private int productQuantity;
	
	@ManyToOne
	@JoinColumn(name = "cart_id")
	@JsonIgnore
	private Cart cart;
}
