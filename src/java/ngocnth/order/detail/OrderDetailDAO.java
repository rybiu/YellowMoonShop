/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.order.detail;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import ngocnth.product.ProductDAO;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class OrderDetailDAO implements Serializable {
    
    public boolean addOrderDetail(Connection con, String orderId, List<OrderDetailDTO> dtos)
            throws NamingException, SQLException {
        PreparedStatement stm = null;
        boolean result = true;
        try {
            if (con != null) {
                String sql = "INSERT INTO tblOrderDetail (orderId, productId, quantity, price) "
                            + "VALUES (?, ?, ?, ?)";
                stm = con.prepareCall(sql);
                ProductDAO productDAO = new ProductDAO();
                for (OrderDetailDTO dto : dtos) {
                    stm.setString(1, orderId);
                    stm.setInt(2, dto.getProductId());
                    stm.setInt(3, dto.getQuantity());
                    stm.setInt(4, dto.getPrice());
                    result = result && stm.executeUpdate() > 0;
                    result = result && productDAO.updateProductQuantity(con, dto.getProductId(), dto.getQuantity());
                    int left = productDAO.getProductQuantity(con, dto.getProductId());
                    result = result &&  left >= 0;
                    if (!result) break;
                }
            } 
        } finally {
            if (stm != null) stm.close();
        }
        return result;
    }
    
    public List<OrderDetailDTO> getOrderDetail(String orderId)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<OrderDetailDTO> list = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT productId, quantity, price "
                            + "FROM tblOrderDetail "
                            + "WHERE orderId = ?";
                stm = con.prepareCall(sql);
                stm.setString(1, orderId);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int productId = rs.getInt("productId");
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    OrderDetailDTO dto = new OrderDetailDTO(orderId, productId, quantity, price);
                    if (list == null) list = new ArrayList<>();
                    list.add(dto); 
                }
                
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return list;
    }
    
    public List<OrderDetailView> getOrderDetailView(String orderId)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<OrderDetailView> list = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT productId, o.quantity as quantity, o.price AS price, name, p.quantity as max_quantity "
                        + "FROM ("
                            + "SELECT productId, quantity, price "
                            + "FROM tblOrderDetail "
                            + "WHERE orderId = ?"
                        + ") o JOIN tblProduct p ON o.productId = p.id";
                stm = con.prepareCall(sql);
                stm.setString(1, orderId);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int productId = rs.getInt("productId");
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    String name = rs.getString("name");
                    int maxQuantity = rs.getInt("max_quantity");
                    OrderDetailView dto = new OrderDetailView(productId, name, quantity, price, maxQuantity);
                    if (list == null) list = new ArrayList<>();
                    list.add(dto); 
                }
                
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return list;
    }
    
}
