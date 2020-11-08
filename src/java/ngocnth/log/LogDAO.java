/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.log;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import ngocnth.util.DBHelper;

/**
 *
 * @author Ruby
 */
public class LogDAO implements Serializable {
    
    public boolean addLog(LogDTO dto)
            throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                String sql = "INSERT INTO tblLog (userId, productId, date) "
                        + "VALUES (?, ?, ?)";
                stm = con.prepareCall(sql);
                stm.setString(1, dto.getUserId());
                stm.setInt(2, dto.getProductId());
                stm.setString(3, Date.valueOf(dto.getDate()).toString());
                return stm.executeUpdate() > 0;
            } 
        } finally {
            if (stm != null) stm.close();
            if (con != null) con.close();
        }
        return false;
    }
    
}
