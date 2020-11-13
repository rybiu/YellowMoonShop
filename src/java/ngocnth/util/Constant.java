/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.util;

/**
 *
 * @author Ruby
 */
public class Constant {

    public static final String UPLOAD_DIR = "uploads";

    public static final int PAGING_SIZE = 20;

    public static String GOOGLE_CLIENT_ID = "YOUR-GOOGLE-CLIENT-ID";
    public static String GOOGLE_LINK_GET_TOKEN = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    public static String MOMO_PARTNER_CODE = "MOMO";
    public static String MOMO_ACCESS_KEY = "F8BBA842ECF85";
    public static String MOMO_SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    public static String MOMO_API_ENDPOINT = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
    public static String MOMO_RETURN_URL = "http://localhost:8080/YellowMoonShop/resOP";
    public static String MOMO_REQ_TYPE_CAPTURE_WALLET = "captureMoMoWallet";
    public static int MOMO_TRANS_TIME = 100 * 60 * 1000;

}
