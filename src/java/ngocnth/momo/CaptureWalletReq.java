/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.momo;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import ngocnth.util.Constant;
import ngocnth.util.EncodeHelper;

/**
 *
 * @author Ruby
 */
public class CaptureWalletReq {
    
    private String accessKey;
    private String partnerCode;
    private String requestType;
    private String notifyUrl;
    private String returnUrl;
    private String orderId;
    private String amount;
    private String orderInfo;
    private String requestId;
    private String extraData;
    private String signature;

    public CaptureWalletReq() { 
    }

    public CaptureWalletReq(String notifyUrl, String orderId, int amount) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        this.accessKey = Constant.MOMO_ACCESS_KEY;
        this.partnerCode = Constant.MOMO_PARTNER_CODE;
        this.requestType = Constant.MOMO_REQ_TYPE_CAPTURE_WALLET;
        this.notifyUrl = notifyUrl;
        this.returnUrl = notifyUrl;
        this.orderId = orderId;
        this.amount = amount + "";
        this.orderInfo = "You are paying for the Yellow Moon Shop";
        this.requestId = orderId;
        this.extraData = "";
        this.signature = this.generateSignature();
    }
    
    private String generateSignature() 
            throws NoSuchAlgorithmException, InvalidKeyException {
        String signatureStr = "partnerCode=" + this.partnerCode
            + "&accessKey=" + this.accessKey
            + "&requestId=" + this.orderId
            + "&amount=" + this.amount
            + "&orderId=" + this.orderId
            + "&orderInfo=" + this.orderInfo
            + "&returnUrl=" + this.returnUrl
            + "&notifyUrl=" + this.notifyUrl
            + "&extraData=";
        return EncodeHelper.encode(signatureStr, Constant.MOMO_SECRET_KEY);
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

}
