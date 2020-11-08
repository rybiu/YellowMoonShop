/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.payment;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class PaymentDAO implements Serializable {
    
    public static final String METHOD_CASH = "CASH";
    public static final String METHOD_ONLINE = "ONLINE";
    public static final String STATUS_UNPAID = "PAYMENT_UNPAID";
    public static final String STATUS_PAID_SUCCESS = "PAYMENT_PAID_SUCCESS";
    public static final String STATUS_PAID_FAIL = "PAYMENT_PAID_FAILED";
    
    public PaymentDTO getPayment(String paymentName)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT id "
                        + "FROM tblPayment "
                        + "WHERE name = ?";
                stm = con.prepareCall(sql);
                stm.setString(1, paymentName);
                rs = stm.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return new PaymentDTO(id, paymentName);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public PaymentDTO getPayment(int paymentId)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name "
                        + "FROM tblPayment "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                stm.setInt(1, paymentId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    return new PaymentDTO(paymentId, name);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
}
