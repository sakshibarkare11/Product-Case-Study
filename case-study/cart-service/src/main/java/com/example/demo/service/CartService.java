package com.example.demo.service;

import com.example.demo.entities.Cart;
import com.example.demo.exception.CartNotFoundException;
import com.example.demo.exception.ProductNotFoundException;

public interface CartService {

	Cart addProductToCart(String userName,Long productId,int quantity) throws ProductNotFoundException;
	
	Cart viewByCartId(Long cartId) throws CartNotFoundException;
}
