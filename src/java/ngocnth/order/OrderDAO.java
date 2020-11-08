/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.order;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.naming.NamingException;
import ngocnth.order.detail.OrderDetailDAO;
import ngocnth.order.detail.OrderDetailDTO;
import ngocnth.order.detail.OrderDetailView;
import ngocnth.payment.PaymentDAO;
import ngocnth.payment.PaymentDTO;
import ngocnth.product.ProductDAO;
import ngocnth.status.StatusDAO;
import ngocnth.status.StatusDTO;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class OrderDAO implements Serializable {
    
    public String addOrder(OrderDTO dto)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                con.setAutoCommit(false);
                String sql = "INSERT INTO tblOrder "
                        + "(userId, total, date, name, phone, address, paymentId, paymentStatusId) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                stm = con.prepareCall(sql);
                stm.setString(1, dto.getUserId());
                stm.setInt(2, dto.getTotal());
                stm.setString(3, Date.valueOf(dto.getDate()).toString());
                stm.setString(4, dto.getName());
                stm.setString(5, dto.getPhone());
                stm.setString(6, dto.getAddress());
                stm.setInt(7, dto.getPaymentId());
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(PaymentDAO.STATUS_UNPAID);
                stm.setInt(8, statusDTO.getId());
                if (stm.executeUpdate() > 0) {
                    String lastId = this.getLastOrderId(con);
                    OrderDetailDAO dao = new OrderDetailDAO();
                    if (!dao.addOrderDetail(con, lastId, dto.getProducts())) {
                        con.rollback();
                        return null;
                    } else {
                        con.commit();
                        return lastId;
                    }
                } else {
                    con.rollback();
                    return null;
                }
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public boolean updateOrderPayment(String orderId, int paymentStatusId, String transId)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "UPDATE tblOrder "
                        + "SET paymentStatusId = ?, "
                        + "transId = ? "
                        + "WHERE id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, paymentStatusId);
                stm.setString(2, transId);
                stm.setString(3, orderId);
                return stm.executeUpdate() > 0;
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return false;
    }
    
    public boolean rollBackOrder(String orderId)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        boolean check = true;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                con.setAutoCommit(false);
                ProductDAO productDAO = new ProductDAO();
                OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                List<OrderDetailDTO> list = orderDetailDAO.getOrderDetail(orderId);
                for (OrderDetailDTO item : list) {
                    check = check && productDAO.updateProductQuantity(con, item.getProductId(), -item.getQuantity());
                    if (!check) break;
                }
                if (check) {
                    con.commit();
                } else {
                    con.rollback();
                }  
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return check;
    }
    
    public String getLastOrderId(Connection con)
            throws NamingException, SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            if (con != null) {
                String sql = "SELECT MAX(id) AS last_id "
                        + "FROM tblOrder";
                stm = con.prepareCall(sql);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getString("last_id");
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
        }
        return null;
    }
    
    public OrderView getOrder(String orderId, String userId, boolean isAdmin)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT total, date, name, phone, address, paymentId, paymentStatusId "
                        + "FROM tblOrder "
                        + "WHERE id = ? "
                        + "AND (userId = ? OR userId IS NULL OR 1 = ?)";
                stm = con.prepareCall(sql);
                stm.setString(1, orderId);
                stm.setString(2, userId);
                stm.setBoolean(3, isAdmin);
                rs = stm.executeQuery();
                if (rs.next()) {
                    int total = rs.getInt("total");
                    LocalDate date = rs.getDate("date").toLocalDate().plusDays(2);
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    int paymentId = rs.getInt("paymentId");
                    PaymentDAO paymentDAO = new PaymentDAO();
                    PaymentDTO paymentDTO = paymentDAO.getPayment(paymentId);
                    int paymentStatusId = rs.getInt("paymentStatusId");
                    StatusDAO statusDAO = new StatusDAO();
                    StatusDTO statusDTO = statusDAO.getStatus(paymentStatusId);
                    OrderDetailDAO dao = new OrderDetailDAO();
                    List<OrderDetailView> products = dao.getOrderDetailView(orderId);
                    return new OrderView(orderId, userId, total, date, name, phone, address, 
                            paymentDTO.getName(), statusDTO.getDescription(), products);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public OrderDTO getOrder(String orderId)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT total, userId, date, name, phone, address, paymentId, paymentStatusId "
                        + "FROM tblOrder "
                        + "WHERE id = ? ";
                stm = con.prepareCall(sql);
                stm.setString(1, orderId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    int total = rs.getInt("total");
                    String userId = rs.getString("userId");
                    LocalDate date = rs.getDate("date").toLocalDate().plusDays(2);
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    int paymentId = rs.getInt("paymentId");
                    int paymentStatusId = rs.getInt("paymentStatusId");
                    return new OrderDTO(orderId, userId, total, date, name, phone, address, paymentId, paymentStatusId);
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
