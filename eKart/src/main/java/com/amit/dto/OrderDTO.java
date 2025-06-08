package com.amit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class OrderDTO {
    @NotEmpty(message="{order.cart.empty}")
    @Valid
    private List<CartItemDTO> cartItems;

    private String paymentMethod;

    private String upiId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    @Valid
    @NotNull(message = "{order.shippinginfo.absent}")
    private ShippingInfoDTO shippingInfo;

    @NotNull(message = "{order.totalamount.absent}")
    private String totalAmount;  // Update to String to match payload

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }


    // Getters and Setters

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

    public ShippingInfoDTO getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoDTO shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}

