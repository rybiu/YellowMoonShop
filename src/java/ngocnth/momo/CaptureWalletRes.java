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
public class CaptureWalletRes {
    
    private String requestId;
    private String errorCode;
    private String orderId;
    private String message;
    private String localMessage;
    private String requestType;
    private String payUrl;
    private String signature;
    
    public boolean isValid() throws NoSuchAlgorithmException, InvalidKeyException {
        String signatureStr = "requestId=" + requestId
                + "&orderId=" + orderId
                + "&message=" + message
                + "&localMessage=" + localMessage
                + "&payUrl=" + payUrl
                + "&errorCode=" + errorCode
                + "&requestType=" + requestType;
        return EncodeHelper.encode(signatureStr, Constant.MOMO_SECRET_KEY).equals(signature) && errorCode.equals("0");
    }

    public String getPayUrl() {
        return payUrl;
    }
    
}
