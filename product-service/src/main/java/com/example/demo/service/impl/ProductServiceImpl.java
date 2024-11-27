package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Product addProduct(Product product) {
		
		return productRepository.save(product);
	}

	@Override
	public Product viewById(Long productId) throws ProductNotFoundException {
		
		return productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product Not Found for this id"));
	}

	@Override
	public List<Product> viewAll() {
		
		return productRepository.findAll();
	}

	@Override
	public List<Product> viewByName(String productName) {
		
		return productRepository.findByProductName(productName);
	}

	@Override
	public Product deleteProduct(Long productId) throws ProductNotFoundException {
		
		Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product Not Found for this id"));
		
		productRepository.deleteById(productId);
		return product;
				
	}

	@Override
	public Product updateProductDetails(Long productId, Product product) throws ProductNotFoundException {
		Product p=productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product Not Found for this id"));
		p.setPrice(product.getPrice());
		p.setDescription(product.getDescription());
		p.setProductName(product.getProductName());
		p.setQuantity(product.getQuantity());
		
		return productRepository.save(p);
	}

}
