package com.example.demo.dto;

import lombok.Data;

@Data
public class Product {

	private Long productId;
	private String productName;
	private String description;
	private double price;
	private int quantity;
}
