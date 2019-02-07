package com.macbook.adminlaundry.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("service")
    @Expose
    private MenuLaundry service;
    @SerializedName("jumlah")
    @Expose
    private Integer customer;

    public MenuLaundry getService() {
        return service;
    }

    public void setService(MenuLaundry service) {
        this.service = service;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }
}
