/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.order.detail;

import java.io.Serializable;

/**
 *
 * @author Ruby
 */
public class OrderDetailView implements Serializable {
    
    private int productId;
    private String productName;
    private int quantity;
    private int price;
    private int maxQuantity;

    public OrderDetailView() {
    }

    public OrderDetailView(int productId, String productName, int quantity, int price, int maxQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.maxQuantity = maxQuantity;
    }
    
    public OrderDetailView(OrderDetailDTO dto) {
        this.productId = dto.getProductId();
        this.quantity = dto.getQuantity();
        this.price = dto.getPrice();
    }
    
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
    
}
