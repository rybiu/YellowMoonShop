/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.order;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import ngocnth.order.detail.OrderDetailView;

/**
 *
 * @author Ruby
 */
public class OrderView implements Serializable {
    
    private String id;
    private String userId;
    private int total;
    private LocalDate date;
    private String name;
    private String phone;
    private String address;
    private String paymentName;
    private String paymentStatus;
    private List<OrderDetailView> products;

    public OrderView() {
    }

    public OrderView(String id, String userId, int total, LocalDate date, String name, String phone, String address, String paymentName, String paymentStatus, List<OrderDetailView> products) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentName = paymentName;
        this.paymentStatus = paymentStatus;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<OrderDetailView> getProducts() {
        return products;
    }

    public void setProducts(List<OrderDetailView> products) {
        this.products = products;
    }
   
}
