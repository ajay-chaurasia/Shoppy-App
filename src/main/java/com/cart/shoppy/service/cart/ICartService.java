package com.cart.shoppy.service.cart;

import com.cart.shoppy.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);
    Long initializeNewCart();
    Cart getCartByCustomerId(Long customerId);
}
