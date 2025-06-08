package com.amit.api;

import com.amit.dto.RazorpayVerificationRequest;
import com.amit.entity.Order;
import com.amit.entity.OrderStatus;
import com.amit.service.OrderService;
import com.amit.utility.RazorpaySignatureVerifier;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-api")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "Authorization")
public class PaymentAPI {

    @Value("${razorpay.key_secret}")
    private String keySecret;


    @Autowired
    private OrderService orderService;

    @Autowired
    private RazorpayClient razorpayClient;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmUPIPayment(
            @RequestParam String orderId,
            @RequestParam String upiId
    ) {
        try {
            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                return ResponseEntity.status(404).body("Order not found.");
            }

            // Simulated UPI validation logic
            if (upiId == null || !upiId.contains("@")) {
                return ResponseEntity.badRequest().body("Invalid UPI ID.");
            }

            // Confirm order
            order.setOrderStatus(OrderStatus.CONFIRMED);
            order.setUpiId(upiId); // save UPI used
            orderService.saveOrder(order);

            return ResponseEntity.ok("UPI payment confirmed successfully. Order marked as CONFIRMED.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Payment confirmation failed: " + e.getMessage());
        }
    }

@PostMapping("/create-razorpay-order")
public ResponseEntity<?> createRazorpayOrder(@RequestParam String orderId) {
    try {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return ResponseEntity.status(404).body("Order not found.");
        }

        // Prepare order details for Razorpay
        JSONObject options = new JSONObject();
        int amountInPaise = (int)(Double.parseDouble(order.getTotalAmount()) * 100); // Convert INR to paise
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", orderId);
        options.put("payment_capture", 1);

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);
//      comment krdi h taaki baad may dekhu
        order.setRazorpayOrderId(razorpayOrder.get("id").toString());
        orderService.saveOrder(order);

        // Save razorpayOrder.get("id") to your DB if you want to track Razorpay order ID

        return ResponseEntity.ok(razorpayOrder.toString());

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error creating Razorpay order: " + e.getMessage());
    }
}

    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody RazorpayVerificationRequest request) {
        String razorpayOrderId = request.getRazorpayOrderId();
        try {
            System.out.println("Received ekartOrderId: " + request.getEkartOrderId());

            // Prepare payload for signature verification
            String payload = razorpayOrderId + "|" + request.getRazorpayPaymentId();

            // Verify the Razorpay signature
            boolean isValid = RazorpaySignatureVerifier.verifySignature(payload, request.getRazorpaySignature(), keySecret);

            if (isValid) {
                // Use the new service method to find order by razorpayOrderId
                Order order = orderService.getOrderByRazorpayOrderId(razorpayOrderId);

                if (order == null) {
                    return ResponseEntity.status(404).body("Order not found.");
                }

                // Update order details on successful payment
                order.setOrderStatus(OrderStatus.CONFIRMED);
                order.setPaymentMethod("RAZORPAY");
                order.setRazorpayOrderId(razorpayOrderId);
                order.setRazorpayPaymentId(request.getRazorpayPaymentId());
                order.setRazorpaySignature(request.getRazorpaySignature());

                orderService.saveOrder(order);

                return ResponseEntity.ok("Payment verified successfully, order confirmed.");
            } else {
                return ResponseEntity.status(400).body("Invalid payment signature.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying payment: " + e.getMessage());
        }
    }

}

