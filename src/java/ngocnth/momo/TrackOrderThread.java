/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.momo;

import java.sql.SQLException;
import javax.naming.NamingException;
import ngocnth.order.OrderDAO;
import ngocnth.order.OrderDTO;
import ngocnth.payment.PaymentDAO;
import ngocnth.status.StatusDAO;
import ngocnth.status.StatusDTO;
import ngocnth.util.Constant;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
public class TrackOrderThread extends Thread {

    private String orderId = null;
    private final Logger LOGGER = LogManager.getLogger(TrackOrderThread.class);

    public TrackOrderThread(String orderId) {
        this.orderId = orderId;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(Constant.MOMO_TRANS_TIME + 5000);
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO orderDTO = orderDAO.getOrder(orderId);
            if (orderDTO != null) {
                StatusDAO statusDAO = new StatusDAO();
                StatusDTO statusDTO = statusDAO.getStatus(orderDTO.getPaymentStatusId());
                if (statusDTO != null && statusDTO.getName().equals(PaymentDAO.STATUS_UNPAID)) {
                    orderDAO.rollBackOrder(orderId);
                    statusDTO = statusDAO.getStatus(PaymentDAO.STATUS_PAID_FAIL);
                    if (statusDTO != null) {
                        orderDAO.updateOrderPayment(orderId, statusDTO.getId(), null);
                    }
                }
            }
        } catch (InterruptedException ex) {
            LOGGER.error("InterruptedException in orderId=" + orderId, ex);
        } catch (NamingException ex) {
            LOGGER.error("NamingException in orderId=" + orderId, ex);
        } catch (SQLException ex) {
            LOGGER.error("SQLException in orderId=" + orderId, ex);
        }
    }
     
}
