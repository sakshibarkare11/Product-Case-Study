package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;

public interface ProductService {

	Product addProduct(Product product);
	Product viewById(Long productId) throws ProductNotFoundException;
	List<Product> viewAll();
	List<Product> viewByName(String productName);
	Product deleteProduct(Long productId) throws ProductNotFoundException;
	Product updateProductDetails(Long productId,Product product) throws ProductNotFoundException;
}
