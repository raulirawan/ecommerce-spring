package com.irvan.ecommerce.model;

public class OrderRequest {
    private Integer productId;
    private Integer userId;
    private int qty;

    // getters
    public Integer getProductId() {
        return productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public int getQty() {
        return qty;
    }

    // Setters
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}