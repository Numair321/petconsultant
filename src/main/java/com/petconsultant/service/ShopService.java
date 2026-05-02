package com.petconsultant.service;

import com.petconsultant.request.CartRequest;
import com.petconsultant.request.OrderRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.CartResponse;
import com.petconsultant.response.OrderResponse;
import com.petconsultant.response.ProductResponse;
import java.util.List;

public interface ShopService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    List<ProductResponse> getProductsByCategory(Long categoryId);
    List<ProductResponse> searchProducts(String keyword);
    ApiResponse addToCart(CartRequest request);
    List<CartResponse> getCart();
    ApiResponse updateCartItem(Long itemId, CartRequest request);
    ApiResponse removeCartItem(Long itemId);
    ApiResponse placeOrder(OrderRequest request);
    List<OrderResponse> getOrderHistory();
    OrderResponse getOrderById(Long orderId);
}
