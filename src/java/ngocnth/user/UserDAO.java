/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import ngocnth.role.RoleDAO;
import ngocnth.status.StatusDAO;
import ngocnth.status.StatusDTO;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class UserDAO implements Serializable {
    
    private final String STATUS_ACTIVE = "USER_ACTIVE";
    
    public UserDTO checkLogin(String email, String password) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name, phone, address, roleId "
                        +   "FROM tblUser "
                        +   "WHERE id = ? "
                        +   "AND password = ? "
                        +   "AND statusId = ?";
                stm = con.prepareCall(sql);
                stm.setString(1, email);
                stm.setString(2, password);
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(3, statusDTO.getId());
                rs = stm.executeQuery();
                if (rs.next()) {   
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    int roleId = rs.getInt("roleId");
                    String roleName = new RoleDAO().getRole(roleId).getName();
                    UserDTO dto = new UserDTO(email, name, password, phone, address, roleName);
                    return dto;
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public UserDTO checkLoginByGoogle(String email) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name, phone, address, roleId "
                        +   "FROM tblUser "
                        +   "WHERE id = ? "
                        +   "AND password IS NULL "
                        +   "AND statusId = ?";
                stm = con.prepareCall(sql);
                stm.setString(1, email);
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(STATUS_ACTIVE);
                stm.setInt(2, statusDTO.getId());
                rs = stm.executeQuery();
                if (rs.next()) {   
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    int roleId = rs.getInt("roleId");
                    String roleName = new RoleDAO().getRole(roleId).getName();
                    UserDTO dto = new UserDTO(email, name, null, phone, address, roleName);
                    return dto;
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
