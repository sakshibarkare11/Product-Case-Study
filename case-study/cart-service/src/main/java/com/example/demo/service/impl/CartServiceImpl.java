package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.ProductClient;
import com.example.demo.dto.Product;
import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.exception.CartNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public Cart addProductToCart(String userName,Long productId,int quantity) throws ProductNotFoundException {
		
		Cart cart = cartRepository.findByUserName(userName);
	    
	    // If cart doesn't exist, create a new cart
	    if (cart == null) {
	        cart = createNewCart(userName);
	    }
	    
	    // Get the product details
	    Product product = productClient.viewProductById(productId);
	    if (product == null) {
	        throw new ProductNotFoundException("Product not found");
	    }
	    
	    // Create a new CartItem
	    CartItem cartItem = new CartItem();
	    cartItem.setCart(cart);
	    cartItem.setProductId(productId);
	    cartItem.setProductName(product.getProductName());
	    cartItem.setProductQuantity(quantity);
	    cartItem.setTotalPrice(product.getPrice() * quantity);
	    
	    // Initialize the cart's items list if it's null
	    List<CartItem> items = cart.getCartItems();
	    if (items == null) {
	        items = new ArrayList<>();  // Initialize the list if it's null
	    }
	    
	    // Add the new cart item to the list
	    items.add(cartItem);
	    cart.setCartItems(items); // Save updated cart items back to the cart
	    
	    // Update the cart's total items count
	    cart.setTotalItems(cart.getTotalItems()+cartItem.getProductQuantity());
	    
	    // Calculate the total bill
	    double totalBill = 0;
	    for (CartItem c : items) {
	        totalBill += c.getTotalPrice();
	    }
	    cart.setTotalBill(totalBill);
	    
	    // Save the cart and cart item
	    cartRepository.save(cart);
	    //cartItemRepository.save(cartItem);
	    
	    return cart;
	}

	private Cart createNewCart(String userName) {
	
		Cart cart=new Cart();
		cart.setUserName(userName);
		
		return cart;
	}

	@Override
	public Cart viewByCartId(Long cartId) throws CartNotFoundException {
		// TODO Auto-generated method stub
		return cartRepository.findById(cartId).orElseThrow(()->new CartNotFoundException("Cart not found for the given id"));
	}

}
