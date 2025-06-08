package com.amit.dto;

import jakarta.validation.constraints.NotNull;

public class ShippingInfoDTO {
    @NotNull(message = "{shippinginfo.name.absent}")
    private String name;
    @NotNull(message = "{shippinginfo.address.absent}")
    private String address;
   @NotNull(message = "{shippinginfo.city.absent}")
    private String city;
    @NotNull(message = "{shippinginfo.zipcode.absent}")
    private String zipCode;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
