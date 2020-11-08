/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.order;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ngocnth.order.detail.OrderDetailDTO;

/**
 *
 * @author Ruby
 */
public class OrderDTO implements Serializable {
    
    private String id;
    private String userId;
    private int total;
    private LocalDate date;
    private String name;
    private String phone;
    private String address;
    private int paymentId;
    private int paymentStatusId;
    private List<OrderDetailDTO> products;

    public OrderDTO() {
    }

    public OrderDTO(String id, String userId, int total, LocalDate date, String name, String phone, String address, int paymentId, int paymentStatusId) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentId = paymentId;
        this.paymentStatusId = paymentStatusId;
    }

    public OrderDTO(String id, String userId, int total, LocalDate date, String name, String phone, String address, int paymentId, int paymentStatusId, List<OrderDetailDTO> products) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentId = paymentId;
        this.paymentStatusId = paymentStatusId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate() {
        this.date = LocalDate.now();
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

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }
    
    public int getQuantity(int productId) {
        OrderDetailDTO temp = new OrderDetailDTO(productId);
        if (products != null && products.indexOf(temp) >= 0) {
            return products.get(products.indexOf(temp)).getQuantity();
        } else {
            return 0;
        }
    }

    public List<OrderDetailDTO> getProducts() {
        return products;
    }
    
    public void addProduct(int productId) {
        if (products == null) products = new ArrayList<>();
        int index = products.indexOf(new OrderDetailDTO(productId));
        if (index >= 0) {
            OrderDetailDTO item = products.get(index);
            item.setQuantity(item.getQuantity() + 1);
            products.set(index, item);
        } else {
            OrderDetailDTO item = new OrderDetailDTO(productId, 1);
            products.add(item);
        }
    }
    
    public void removeProduct(int productId) {
        if (products == null) return;
        int index = products.indexOf(new OrderDetailDTO(productId));
        if (index >= 0) {
            products.remove(index);
            if (products.isEmpty()) {
                products = null;
            }
        }
    }
    
    public void updateProduct(int productId, int quantity) {
        if (products == null) return;
        int index = products.indexOf(new OrderDetailDTO(productId));
        if (index >= 0) {
            OrderDetailDTO item = products.get(index);
            item.setQuantity(quantity);
            products.set(index, item);
        }
    }
 
}
