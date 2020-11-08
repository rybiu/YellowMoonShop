/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.log;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Ruby
 */
public class LogDTO implements Serializable {
    
    private int id;
    private String userId;
    private int productId;
    private LocalDate date;

    public LogDTO() {
    }

    public LogDTO(int id, String userId, int productId, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.date = date;
    }

    public LogDTO(String userId, int productId) {
        this.userId = userId;
        this.productId = productId;
        this.date = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}
