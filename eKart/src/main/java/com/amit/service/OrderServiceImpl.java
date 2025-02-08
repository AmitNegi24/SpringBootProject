package com.amit.service;

import com.amit.dto.OrderDTO;
import com.amit.entity.Order;
import com.amit.entity.CartItem;
import com.amit.entity.OrderStatus;
import com.amit.entity.ShippingInfo;
import com.amit.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(OrderDTO orderDTO) {
        // Convert DTO to Entity
        Order order = new Order();
        order.setTotalAmount(orderDTO.getTotalAmount());

        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setName(orderDTO.getShippingInfo().getName());
        shippingInfo.setAddress(orderDTO.getShippingInfo().getAddress());
        shippingInfo.setCity(orderDTO.getShippingInfo().getCity());
        shippingInfo.setZipCode(orderDTO.getShippingInfo().getZipCode());

        // Set ShippingInfo in the Order
        order.setShippingInfo(shippingInfo);
        // Map CartItemDTO to CartItem entities
        List<CartItem> cartItems = orderDTO.getCartItems().stream()
                .map(cartItemDTO -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setProductId(cartItemDTO.getProductId());
                    cartItem.setTitle(cartItemDTO.getTitle());
                    cartItem.setImage(cartItemDTO.getImage());
                    cartItem.setPrice(cartItemDTO.getPrice());
                    cartItem.setQuantity(cartItemDTO.getQuantity());
                    cartItem.setDescription(cartItemDTO.getDescription());
                    return cartItem;
                })
                .collect(Collectors.toList());

        order.setCartItems(cartItems);
        order.setOrderStatus(OrderStatus.PENDING);

        // Save the order, MongoDB will generate the order ID automatically
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }

}
