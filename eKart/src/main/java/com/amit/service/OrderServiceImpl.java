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

@Service(value ="orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(OrderDTO orderDTO) {
        // Convert DTO to Entity
        Order order = new Order();
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setUpiId(orderDTO.getUpiId());
        System.out.println("Payment Method: " + orderDTO.getPaymentMethod());
        if ("COD".equalsIgnoreCase(orderDTO.getPaymentMethod())) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        } else {
            order.setOrderStatus(OrderStatus.PENDING);
        }

        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setName(orderDTO.getShippingInfo().getName());
        shippingInfo.setAddress(orderDTO.getShippingInfo().getAddress());
        shippingInfo.setCity(orderDTO.getShippingInfo().getCity());
        shippingInfo.setZipCode(orderDTO.getShippingInfo().getZipCode());
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

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByRazorpayOrderId(String razorpayOrderId) {
        return orderRepository.findByRazorpayOrderId(razorpayOrderId);
    }


}
