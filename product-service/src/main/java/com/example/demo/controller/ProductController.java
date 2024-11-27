package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/addProduct")
	public ResponseEntity<Product> addProduct(@RequestBody Product product)
	{
		return ResponseEntity.ok(productService.addProduct(product));
	}
	
	@GetMapping("/viewProductById/{productId}")
	public ResponseEntity<Product> viewProductById(@PathVariable Long productId) throws ProductNotFoundException
	{
		return ResponseEntity.ok(productService.viewById(productId));
	}
	
	@GetMapping("/viewAll")
	public ResponseEntity<List<Product>> viewAll()
	{
		return ResponseEntity.ok(productService.viewAll());
	}
	
	@GetMapping("/viewByName/{productName}")
	public ResponseEntity<List<Product>> viewByName(@PathVariable String productName)
	{
		return ResponseEntity.ok(productService.viewByName(productName));
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) throws ProductNotFoundException
	{
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}
	
	@PutMapping("/updateProductDetails/{productId}")
	public ResponseEntity<Product> updateProductDetails(@PathVariable Long productId,@RequestBody Product product) throws ProductNotFoundException
	{
		return ResponseEntity.ok(productService.updateProductDetails(productId, product));
	}
}
