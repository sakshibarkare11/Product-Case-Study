package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.client.ProductClient;
import com.example.demo.dto.Product;
import com.example.demo.entities.Cart;
import com.example.demo.exception.CartNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.service.CartService;
import com.example.demo.utl.JwtUtil;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CartService cartService;
	
	@GetMapping("/viewProduct/{productId}")
	public Product viewProduct(@PathVariable Long productId)
	{
		return productClient.viewProductById(productId);
	}
	
	@GetMapping("/view")
	public String view()
	{
		return "hello";
	}
	
	@PostMapping("/addItemToCart")
	public Cart addItemToCart(@RequestHeader("Authorization") String token,@RequestParam("productId") Long productId,@RequestParam("quantity") int quantity) throws ProductNotFoundException
	{
		String username=jwtUtil.extractUserName(token.substring(7));
		return cartService.addProductToCart(username, productId, quantity);
	}
	
	@GetMapping("/viewByCartId/{cartId}")
	public Cart viewByCartId(@PathVariable Long cartId) throws CartNotFoundException
	{
		return cartService.viewByCartId(cartId);
	}
}
