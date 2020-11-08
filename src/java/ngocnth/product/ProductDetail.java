/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.product;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ruby
 */
public class ProductDetail implements Serializable {
    
    private int id;
    private String name;
    private String description;
    private String image;
    private LocalDate createDate;
    private LocalDate expirationDate;
    private int quantity;
    private int price;
    private String categoryName;
    private String userName;
    private String statusName;
    

    public ProductDetail() {
    }

    public ProductDetail(int id, String name, String description, String image, LocalDate createDate, LocalDate expirationDate, int quantity, int price, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.price = price;
        this.categoryName = categoryName;
    }

    public ProductDetail(int id, String name, String description, String image, LocalDate createDate, 
            LocalDate expirationDate, int quantity, int price, String categoryName, String userName, String statusName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.price = price;
        this.categoryName = categoryName;
        this.userName = userName;
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return createDate.format(formatter);
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getExpirationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return expirationDate.format(formatter);
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
 
}
