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
public class ProductSetError implements Serializable {
    
    private String nameLengthError;
    private String descriptionLengthError;
    private String createDateFormatError;
    private String expirationDateFormatError;
    private String quantityValueError;
    private String priceValueError;
    private String categoryValueError;
    private String statusIdValueError;

    public String getNameLengthError() {
        return nameLengthError;
    }

    public void setNameLengthError(String nameLengthError) {
        this.nameLengthError = nameLengthError;
    }

    public String getDescriptionLengthError() {
        return descriptionLengthError;
    }

    public void setDescriptionLengthError(String descriptionLengthError) {
        this.descriptionLengthError = descriptionLengthError;
    }

    public String getCreateDateFormatError() {
        return createDateFormatError;
    }

    public void setCreateDateFormatError(String createDateFormatError) {
        this.createDateFormatError = createDateFormatError;
    }

    public String getExpirationDateFormatError() {
        return expirationDateFormatError;
    }

    public void setExpirationDateFormatError(String expirationDateFormatError) {
        this.expirationDateFormatError = expirationDateFormatError;
    }

    public String getQuantityValueError() {
        return quantityValueError;
    }

    public void setQuantityValueError(String quantityValueError) {
        this.quantityValueError = quantityValueError;
    }

    public String getPriceValueError() {
        return priceValueError;
    }

    public void setPriceValueError(String priceValueError) {
        this.priceValueError = priceValueError;
    }

    public String getCategoryValueError() {
        return categoryValueError;
    }

    public void setCategoryValueError(String categoryValueError) {
        this.categoryValueError = categoryValueError;
    }

    public String getStatusIdValueError() {
        return statusIdValueError;
    }

    public void setStatusIdValueError(String statusIdValueError) {
        this.statusIdValueError = statusIdValueError;
    }

}
