package com.petconsultant.service;

import com.petconsultant.constants.AppConstants;
import com.petconsultant.entity.*;
import com.petconsultant.exception.BadRequestException;
import com.petconsultant.exception.ResourceNotFoundException;
import com.petconsultant.repository.*;
import com.petconsultant.request.CartRequest;
import com.petconsultant.request.OrderRequest;
import com.petconsultant.response.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    private final ProductRepository   productRepository;
    private final CartItemRepository  cartItemRepository;
    private final OrderRepository     orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository      userRepository;

    public ShopServiceImpl(ProductRepository productRepository,
                            CartItemRepository cartItemRepository,
                            OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            UserRepository userRepository) {
        this.productRepository   = productRepository;
        this.cartItemRepository  = cartItemRepository;
        this.orderRepository     = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository      = userRepository;
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            return userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No user found in DB. Please register first."));
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
    }

    private ProductResponse mapProduct(ProductEntity p) {
        ProductResponse res = new ProductResponse();
        res.setId(p.getId());
        res.setCategoryId(p.getCategoryId());
        res.setName(p.getName());
        res.setDescription(p.getDescription());
        res.setPrice(p.getPrice());
        res.setStock(p.getStock());
        res.setImage(p.getImage());
        res.setIsActive(p.getIsActive());
        res.setCreatedAt(p.getCreatedAt());
        return res;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> products = productRepository.findByIsActiveTrue();
        List<ProductResponse> result = new ArrayList<>();
        for (ProductEntity p : products) result.add(mapProduct(p));
        return result;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        ProductEntity p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapProduct(p);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        List<ProductEntity> products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId);
        List<ProductResponse> result = new ArrayList<>();
        for (ProductEntity p : products) result.add(mapProduct(p));
        return result;
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword);
        List<ProductResponse> result = new ArrayList<>();
        for (ProductEntity p : products) result.add(mapProduct(p));
        return result;
    }

    @Override
    public ApiResponse addToCart(CartRequest request) {
        UserEntity user = getCurrentUser();
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItemEntity cartItem = cartItemRepository
                .findByUserIdAndProductId(user.getId(), request.getProductId())
                .orElseGet(CartItemEntity::new);

        cartItem.setUserId(user.getId());
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Item added to cart");
        response.setData(null);
        return response;
    }

    @Override
    public List<CartResponse> getCart() {
        UserEntity user = getCurrentUser();
        List<CartItemEntity> items = cartItemRepository.findByUserId(user.getId());
        List<CartResponse> result = new ArrayList<>();
        for (CartItemEntity item : items) {
            ProductEntity product = productRepository.findById(item.getProductId()).orElse(null);
            CartResponse res = new CartResponse();
            res.setId(item.getId());
            res.setProductId(item.getProductId());
            res.setQuantity(item.getQuantity());
            res.setUpdatedAt(item.getUpdatedAt());
            if (product != null) {
                res.setProductName(product.getName());
                res.setProductImage(product.getImage());
                res.setProductPrice(product.getPrice());
                res.setTotalPrice(product.getPrice() * item.getQuantity());
            }
            result.add(res);
        }
        return result;
    }

    @Override
    public ApiResponse updateCartItem(Long itemId, CartRequest request) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Cart updated");
        response.setData(null);
        return response;
    }

    @Override
    public ApiResponse removeCartItem(Long itemId) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItemRepository.delete(item);

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Item removed from cart");
        response.setData(null);
        return response;
    }

    @Override
    @Transactional
    public ApiResponse placeOrder(OrderRequest request) {
        UserEntity user = getCurrentUser();
        List<CartItemEntity> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) throw new BadRequestException("Cart is empty");

        double total = 0.0;
        for (CartItemEntity item : cartItems) {
            ProductEntity p = productRepository.findById(item.getProductId()).orElse(null);
            if (p != null) total += p.getPrice() * item.getQuantity();
        }

        double minOrderAmount = 500.0; // User required field for minimum price check
        if (total < minOrderAmount) {
            throw new com.petconsultant.exception.BadRequestException("Order amount must be at least " + minOrderAmount);
        }

        OrderEntity order = new OrderEntity();
        order.setUserId(user.getId());
        order.setTotalAmount(total);
        order.setDeliveryAddress(request.getDeliveryAddress());
        OrderEntity savedOrder = orderRepository.save(order);

        for (CartItemEntity item : cartItems) {
            ProductEntity p = productRepository.findById(item.getProductId()).orElse(null);
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            if (p != null) {
                orderItem.setProductName(p.getName());
                orderItem.setPrice(p.getPrice());
            }
            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByUserId(user.getId());

        ApiResponse response = new ApiResponse();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Order placed successfully");
        response.setData(null);
        return response;
    }

    @Override
    public List<OrderResponse> getOrderHistory() {
        UserEntity user = getCurrentUser();
        List<OrderEntity> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<OrderResponse> result = new ArrayList<>();
        for (OrderEntity o : orders) result.add(buildOrderResponse(o));
        return result;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return buildOrderResponse(order);
    }

    private OrderResponse buildOrderResponse(OrderEntity order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());
        res.setUserId(order.getUserId());
        res.setTotalAmount(order.getTotalAmount());
        res.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        res.setDeliveryAddress(order.getDeliveryAddress());
        res.setCreatedAt(order.getCreatedAt());

        List<OrderItemEntity> items = orderItemRepository.findByOrderId(order.getId());
        List<OrderResponse.OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItemEntity item : items) {
            OrderResponse.OrderItemResponse ir = new OrderResponse.OrderItemResponse();
            ir.setProductId(item.getProductId());
            ir.setProductName(item.getProductName());
            ir.setPrice(item.getPrice());
            ir.setQuantity(item.getQuantity());
            itemResponses.add(ir);
        }
        res.setItems(itemResponses);
        return res;
    }
}
