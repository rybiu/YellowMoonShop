/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.status;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class StatusDAO implements Serializable {
    
    public StatusDTO getStatus(String statusName) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT id, description "
                        + "FROM tblStatus "
                        + "WHERE name = ?";
                stm = con.prepareCall(sql);
                stm.setString(1, statusName);
                rs = stm.executeQuery();
                if (rs.next()) {   
                    int id = rs.getInt("id");
                    String description = rs.getString("description");
                    return new StatusDTO(id, statusName, description);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public StatusDTO getStatus(int statusId) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT name, description "
                        + "FROM tblStatus "
                        + "WHERE id = ?";
                stm = con.prepareCall(sql);
                stm.setInt(1, statusId);
                rs = stm.executeQuery();
                if (rs.next()) {   
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    return new StatusDTO(statusId, name, description);
                }
            } 
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return null;
    }
    
    public List<StatusDTO> getStatusWithPrefix(String prefix) 
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<StatusDTO> list = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "SELECT id, name, description "
                        + "FROM tblStatus "
                        + "WHERE name LIKE ?";
                stm = con.prepareCall(sql);
                stm.setString(1, prefix + "%");
                rs = stm.executeQuery();
                while (rs.next()) {   
                    int id = rs.getInt("id");
                    String statusName = rs.getString("name");
                    String description = rs.getString("description");
                    if (list == null) list = new ArrayList<>();
                    list.add(new StatusDTO(id, statusName, description));
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
