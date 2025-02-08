package com.amit.api;

import com.amit.dto.OrderDTO;
import com.amit.entity.Order;
import com.amit.entity.OrderStatus;
import com.amit.service.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout-api")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "Authorization")
public class CheckOutAPI {

    @Autowired
    private OrderService orderService;

    static Log logger = LogFactory.getLog(CheckOutAPI.class);

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderDTO orderDTO) {
        try {
            logger.info("Received a request to create an order");

            // Create order
            Order order = orderService.createOrder(orderDTO);

            // Return response with order ID and status
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Order created successfully with ID: " + order.getOrderId() + " and Status: " + order.getOrderStatus());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order creation failed: " + e.getMessage());
        }
    }
}
