package com.example.demo.client;


import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.Product;



@FeignClient(value = "productClient",url = "http://localhost:8083/products")
public interface ProductClient {

	@GetMapping("/viewProductById/{productId}")
	Product viewProductById(@PathVariable Long productId);
}
