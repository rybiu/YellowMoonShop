/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.product;

import java.io.Serializable;

/**
 *
 * @author Ruby
 */
public class ProductSearchError implements Serializable {
    
    private String invalidMin;
    private String invalidMax;
    private String invalidCategory;

    public String getInvalidMin() {
        return invalidMin;
    }

    public void setInvalidMin(String invalidMin) {
        this.invalidMin = invalidMin;
    }

    public String getInvalidMax() {
        return invalidMax;
    }

    public void setInvalidMax(String invalidMax) {
        this.invalidMax = invalidMax;
    }

    public String getInvalidCategory() {
        return invalidCategory;
    }

    public void setInvalidCategory(String invalidCategory) {
        this.invalidCategory = invalidCategory;
    }
    
}
