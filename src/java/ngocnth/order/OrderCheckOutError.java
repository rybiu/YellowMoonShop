/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.order;

import java.io.Serializable;

/**
 *
 * @author Ruby
 */
public class OrderCheckOutError implements Serializable {
    
    private String nameLengthError;
    private String phoneLengthError;
    private String addressLengthError;
    private String invalidPaymentName;

    public String getNameLengthError() {
        return nameLengthError;
    }

    public void setNameLengthError(String nameLengthError) {
        this.nameLengthError = nameLengthError;
    }

    public String getPhoneLengthError() {
        return phoneLengthError;
    }

    public void setPhoneLengthError(String phoneLengthError) {
        this.phoneLengthError = phoneLengthError;
    }

    public String getAddressLengthError() {
        return addressLengthError;
    }

    public void setAddressLengthError(String addressLengthError) {
        this.addressLengthError = addressLengthError;
    }

    public String getInvalidPaymentName() {
        return invalidPaymentName;
    }

    public void setInvalidPaymentName(String invalidPaymentName) {
        this.invalidPaymentName = invalidPaymentName;
    }
    
}
