package ngocnth.role;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import ngocnth.util.DBHelper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class RoleDAO implements Serializable {
    
    public static String ROLE_ADMIN = "ROLE_ADMIN";
    public static String ROLE_USER = "ROLE_USER";
    
    public RoleDTO getRole(int roleId) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name "
                        + "FROM tblRole "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                stm.setInt(1, roleId);
                rs = stm.executeQuery();
                if (rs.next()) {   
                    String name = rs.getString("name");
                    return new RoleDTO(roleId, name);
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
