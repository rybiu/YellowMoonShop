/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.product;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import ngocnth.status.StatusDAO;
import ngocnth.status.StatusDTO;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class ProductDAO implements Serializable {
    
    private final String STATUS_ACTIVE = "PRODUCT_ACTIVE";
    
    public boolean addProduct(ProductDTO dto)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "INSERT INTO tblProduct "
                        + "(name, description, image, createDate, expirationDate, quantity, price, categoryId, userId, statusId) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stm = con.prepareCall(sql);
                stm.setString(1, dto.getName());
                stm.setString(2, dto.getDescription());
                stm.setString(3, dto.getImage());
                stm.setString(4, Date.valueOf(dto.getCreateDate()).toString());
                stm.setString(5, Date.valueOf(dto.getExpirationDate()).toString());
                stm.setInt(6, dto.getQuantity());
                stm.setInt(7, dto.getPrice());
                if (dto.getCategoryId() != -1) {
                    stm.setInt(8, dto.getCategoryId());
                } else {
                    stm.setNull(8, Types.INTEGER);
                }
                stm.setString(9, dto.getUserId());
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(10, statusDTO.getId());
                return stm.executeUpdate() > 0;
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return false;
    }
    
    public boolean updateProduct(ProductDTO dto)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "UPDATE tblProduct "
                        + "SET name = ?, "
                        + "description = ?, "
                        + "createDate = ?, "
                        + "expirationDate = ?, "
                        + "quantity = ?, "
                        + "price = ?, "
                        + "categoryId = ?, "
                        + "statusId = ? "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                stm.setString(1, dto.getName());
                stm.setString(2, dto.getDescription());
                stm.setString(3, Date.valueOf(dto.getCreateDate()).toString());
                stm.setString(4, Date.valueOf(dto.getExpirationDate()).toString());
                stm.setInt(5, dto.getQuantity());
                stm.setInt(6, dto.getPrice());
                if (dto.getCategoryId() == -1) {
                    stm.setNull(7, Types.INTEGER);
                } else {
                    stm.setInt(7, dto.getCategoryId());
                }
                stm.setInt(8, dto.getStatusId());
                stm.setInt(9, dto.getId());
                return stm.executeUpdate() > 0;
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return false;
    }
    
    public List<ProductDetail> getProductList(String searchValue, int start, int row)
            throws NamingException, SQLException {
        List<ProductDetail> list = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT p.id AS id, p.name AS name, p.description AS description, image, createDate, expirationDate, "
                        + "quantity, price, c.name AS category_name, u.name AS user_name, s.description AS status_name "
                        + "FROM ("
                            + "SELECT "
                            + "id, name, description, image, createDate, expirationDate, quantity, price, categoryId, userId, statusId "
                            + "FROM tblProduct "
                            + "WHERE name LIKE ? "
                            + "ORDER BY createDate " 
                            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                        + ") p LEFT JOIN tblCategory c ON p.categoryId = c.id "
                        + "JOIN tblStatus s ON p.statusId = s.id "
                        + "JOIN tblUser u ON p.userId = u.id";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + searchValue + "%");
                stm.setInt(2, start);
                stm.setInt(3, row);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String image = rs.getString("image");
                    LocalDate createDate = Date.valueOf(rs.getString("createDate")).toLocalDate().plusDays(2);
                    LocalDate expirationDate = Date.valueOf(rs.getString("expirationDate")).toLocalDate().plusDays(2);
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    String categoryName = rs.getString("category_name");
                    String userName = rs.getString("user_name");
                    String statusName = rs.getString("status_name");
                    ProductDetail product = new ProductDetail(id, name, description, image, createDate, expirationDate, quantity, price, categoryName, userName, statusName);
                    if (list == null) list = new ArrayList<>();
                    list.add(product);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return list;
    }
    
    public List<ProductDetail> getProductList(String searchName, int minRange, int maxRange, 
            int categoryId, int start, int row) throws NamingException, SQLException {
        List<ProductDetail> list = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT p.id AS id, p.name AS name, description, image, createDate, "
                        + "expirationDate, quantity, price, c.name AS category_name "
                        + "FROM ("
                            + "SELECT "
                            + "id, name, description, image, createDate, expirationDate, quantity, price, categoryId "
                            + "FROM tblProduct "
                            + "WHERE name LIKE ? "
                            + "AND (price >= ? OR -1 = ?) "
                            + "AND (price <= ? OR -1 = ?) "
                            + "AND (categoryId = ? OR -1 = ?) "
                            + "AND statusId = ? "
                            + "AND quantity > 0 "
                            + "AND expirationDate > getdate() "
                            + "ORDER BY createDate " 
                            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                        + ") p LEFT JOIN tblCategory c ON p.categoryId = c.id";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + searchName + "%");
                stm.setInt(2, minRange);
                stm.setInt(3, minRange);
                stm.setInt(4, maxRange);
                stm.setInt(5, maxRange);
                stm.setInt(6, categoryId);
                stm.setInt(7, categoryId);
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(8, statusDTO.getId());
                stm.setInt(9, start);
                stm.setInt(10, row);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String image = rs.getString("image");
                    LocalDate createDate = Date.valueOf(rs.getString("createDate")).toLocalDate().plusDays(2);
                    LocalDate expirationDate = Date.valueOf(rs.getString("expirationDate")).toLocalDate().plusDays(2);
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    String categoryName = rs.getString("category_name");
                    ProductDetail detail = new ProductDetail(id, name, description, image, 
                            createDate, expirationDate, quantity, price, categoryName);
                    if (list == null) list = new ArrayList<>();
                    list.add(detail);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return list;
    }
    
    public int getProductListCount(String searchValue) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT COUNT(*) as count "
                        + "FROM tblProduct "
                        + "WHERE name LIKE ?";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + searchValue + "%");
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getInt("count");
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return 0;
    }
    
    public int getProductListCount(String searchName, int minRange, int maxRange, int categoryId)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT COUNT(*) as count "
                        + "FROM tblProduct "
                        + "WHERE name LIKE ? "
                        + "AND (price >= ? OR -1 = ? ) "
                        + "AND (price <= ? OR -1 = ?) "
                        + "AND (categoryId = ? OR -1 = ?) "
                        + "AND statusId = ? "
                        + "AND quantity > 0 "
                        + "AND expirationDate > getdate()";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + searchName + "%");
                stm.setInt(2, minRange);
                stm.setInt(3, minRange);
                stm.setInt(4, maxRange);
                stm.setInt(5, maxRange);
                stm.setInt(6, categoryId);
                stm.setInt(7, categoryId);
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(8, statusDTO.getId());
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getInt("count");
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return 0;
    }
    
    public ProductDTO getProduct(int productId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name, description, image, createDate, expirationDate, quantity, price, categoryId, statusId "
                        + "FROM tblProduct "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                stm.setInt(1, productId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String image = rs.getString("image");
                    LocalDate createDate = rs.getDate("createDate").toLocalDate().plusDays(2);
                    LocalDate expirationDate = rs.getDate("expirationDate").toLocalDate().plusDays(2);
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    int categoryId = rs.getInt("categoryId");
                    int statusId = rs.getInt("statusId");
                    return new ProductDTO(productId, name, description, image, createDate, expirationDate, quantity, price, categoryId, sql, statusId);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public ProductDTO getProductWithStatus(int productId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name, quantity, price "
                        + "FROM tblProduct "
                        + "WHERE id = ? "
                        + "AND statusId = ? "
                        + "AND quantity > 0 "
                        + "AND expirationDate > getdate()";
                stm = con.prepareCall(sql);
                stm.setInt(1, productId);
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(2, statusDTO.getId());
                rs = stm.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    return new ProductDTO(productId, name, quantity, price);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public int getProductQuantity(Connection con, int productId) throws NamingException, SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            if (con != null) {
                String sql = "SELECT quantity "
                        + "FROM tblProduct "
                        + "WHERE id = ? AND statusId = ?";
                stm = con.prepareCall(sql);
                stm.setInt(1, productId);
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(2, statusDTO.getId());
                rs = stm.executeQuery();
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
        }
        return -1;
    }
    
    public boolean updateProductQuantity(Connection con, int productId, int productQuantity) 
            throws NamingException, SQLException {
        PreparedStatement stm = null;
        try {
            if (con != null) {
                String sql = "UPDATE tblProduct "
                        + "SET quantity = quantity - ? "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                stm.setInt(1, productQuantity);
                stm.setInt(2, productId);
                return stm.executeUpdate() > 0;
            } 
        } finally {
            if (stm != null) stm.close();
        }
        return false;
    }
    
    public boolean updateProductImage(int productId, String image) 
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "UPDATE tblProduct "
                        + "SET image = ? "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                if (image == null) {
                    stm.setNull(1, Types.NVARCHAR);
                } else {
                    stm.setString(1, image);
                }
                stm.setInt(2, productId);
                return stm.executeUpdate() > 0;
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return false;
    }
    
}
