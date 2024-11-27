package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cart")
@Data
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartId;
	private String userName;
	private int totalItems;
	private double totalBill;
	
	@OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<CartItem> cartItems;
}
