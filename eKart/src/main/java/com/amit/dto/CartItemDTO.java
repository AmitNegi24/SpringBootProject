package com.amit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CartItemDTO {

    @NotBlank(message = "{cart.invalid.productId}")
    private String productId;

    @NotBlank(message = "{cart.invalid.title}")
    private String title;

    @NotBlank(message = "Image URL is required")
    private String image;

    @Positive(message = "{cart.invalid.price}")
    private double price;

    @Min(value = 1, message = "{cart.invalid.quantity}")
    private int quantity;

    @Size(max = 200, message = "Description should not exceed 500 characters")
    private String description;

    // Getters and Setters

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
