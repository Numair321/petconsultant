package com.petconsultant.controller;

import com.petconsultant.request.CartRequest;
import com.petconsultant.request.OrderRequest;
import com.petconsultant.response.ApiResponse;
import com.petconsultant.response.CartResponse;
import com.petconsultant.response.OrderResponse;
import com.petconsultant.response.ProductResponse;
import com.petconsultant.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/shop/products")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductResponse> products = shopService.getAllProducts();
        ApiResponse response = new ApiResponse("SUCCESS", "Products fetched", products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/shop/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = shopService.getProductById(id);
        ApiResponse response = new ApiResponse("SUCCESS", "Product found", product);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/shop/products/category/{catId}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable Long catId) {
        List<ProductResponse> products = shopService.getProductsByCategory(catId);
        ApiResponse response = new ApiResponse("SUCCESS", "Products fetched", products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/shop/products/search")
    public ResponseEntity<ApiResponse> searchProducts(@RequestParam String keyword) {
        List<ProductResponse> products = shopService.searchProducts(keyword);
        ApiResponse response = new ApiResponse("SUCCESS", "Search results", products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<ApiResponse> addToCart(@Valid @RequestBody CartRequest request) {
        ApiResponse response = shopService.addToCart(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<ApiResponse> getCart() {
        List<CartResponse> cart = shopService.getCart();
        ApiResponse response = new ApiResponse("SUCCESS", "Cart fetched", cart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/cart/update/{itemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long itemId,
                                                       @Valid @RequestBody CartRequest request) {
        ApiResponse response = shopService.updateCartItem(itemId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/cart/remove/{itemId}")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable Long itemId) {
        ApiResponse response = shopService.removeCartItem(itemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/order/place")
    public ResponseEntity<ApiResponse> placeOrder(@Valid @RequestBody OrderRequest request) {
        ApiResponse response = shopService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/order/history")
    public ResponseEntity<ApiResponse> getOrderHistory() {
        List<OrderResponse> orders = shopService.getOrderHistory();
        ApiResponse response = new ApiResponse("SUCCESS", "Order history fetched", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse order = shopService.getOrderById(orderId);
        ApiResponse response = new ApiResponse("SUCCESS", "Order found", order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
