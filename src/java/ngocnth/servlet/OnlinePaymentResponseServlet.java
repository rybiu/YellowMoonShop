/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ngocnth.order.OrderDAO;
import ngocnth.order.OrderDTO;
import ngocnth.payment.PaymentDAO;
import ngocnth.status.StatusDAO;
import ngocnth.status.StatusDTO;
import ngocnth.util.Constant;
import ngocnth.util.EncodeHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "OnlinePaymentResponseServlet", urlPatterns = {"/OnlinePaymentResponseServlet"})
public class OnlinePaymentResponseServlet extends HttpServlet {

    private final String VIEW_ORDER_SERVLET = "trackOrder";
    private final String CHECK_OUT_SUCCCESS_PAGE = "checkOutSuccess.jsp";
    private final String PAGE_404 = "404.html";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(OnlinePaymentResponseServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = ERROR_PAGE;
        try {
            String partnerCode = request.getParameter("partnerCode");
            String accessKey = request.getParameter("accessKey");
            String requestId = request.getParameter("requestId");
            String amount = request.getParameter("amount");
            String orderId = request.getParameter("orderId");
            String orderInfo = request.getParameter("orderInfo");
            String orderType = request.getParameter("orderType");
            String transId = request.getParameter("transId");
            String message = request.getParameter("message");
            String localMessage = request.getParameter("localMessage");
            String responseTime = request.getParameter("responseTime");
            String errorCode = request.getParameter("errorCode");
            String payType = request.getParameter("payType");
            String extraData = request.getParameter("extraData");
            String signature = request.getParameter("signature");
            String signatureStr = "partnerCode=" + partnerCode
                    + "&accessKey=" + accessKey
                    + "&requestId=" + requestId
                    + "&amount=" + amount
                    + "&orderId=" + orderId
                    + "&orderInfo=" + orderInfo
                    + "&orderType=" + orderType
                    + "&transId=" + transId
                    + "&message=" + message
                    + "&localMessage=" + localMessage
                    + "&responseTime=" + responseTime
                    + "&errorCode=" + errorCode
                    + "&payType=" + payType
                    + "&extraData=" + extraData;
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO orderDTO = orderDAO.getOrder(orderId);
            if (orderDTO != null && EncodeHelper.encode(signatureStr, Constant.MOMO_SECRET_KEY).equals(signature)) {
                StatusDAO statusDAO = new StatusDAO();
                if (errorCode.equals("0")) {
                    StatusDTO statusDTO = statusDAO.getStatus(PaymentDAO.STATUS_PAID_SUCCESS);
                    if (statusDTO != null) {
                        orderDAO.updateOrderPayment(orderId, statusDTO.getId(), transId);
                    }
                } else {
                    StatusDTO statusDTO = statusDAO.getStatus(PaymentDAO.STATUS_PAID_FAIL);
                    if (statusDTO != null) {
                        orderDAO.updateOrderPayment(orderId, statusDTO.getId(), transId);
                        orderDAO.rollBackOrder(orderId);
                    }
                }
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("USER") != null) {
                    url = VIEW_ORDER_SERVLET + "?txtOrderId=" + orderId;
                } else {
                    request.setAttribute("ORDER_ID", orderId);
                    url = CHECK_OUT_SUCCCESS_PAGE;
                }
            } else {
                url = PAGE_404;
            }
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("NoSuchAlgorithmException", ex);
        } catch (InvalidKeyException ex) {
            LOGGER.error("InvalidKeyException", ex);
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
        } finally {
            if (url.startsWith(VIEW_ORDER_SERVLET)) {
                response.sendRedirect(url);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
