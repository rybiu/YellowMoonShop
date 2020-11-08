/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.product;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Ruby
 */
public class ProductDTO implements Serializable {
    
    private int id;
    private String name;
    private String description;
    private String image;
    private LocalDate createDate;
    private LocalDate expirationDate;
    private int quantity;
    private int price;
    private int categoryId;
    private String userId;
    private int statusId;

    public ProductDTO() {
    }

    public ProductDTO(int id, String name, String description, String image, LocalDate createDate, LocalDate expirationDate, int quantity, int price, int categoryId, String userId, int statusId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.userId = userId;
        this.statusId = statusId;
    }

    public ProductDTO(String name, String description, String image, LocalDate createDate, LocalDate expirationDate, int quantity, int price, int categoryId, String userId) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public ProductDTO(int id, String name, int quantity, int price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductDTO(int id, String name, String description, LocalDate createDate, LocalDate expirationDate, int quantity, int price, int categoryId, int statusId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.statusId = statusId;
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

}
