/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import com.google.gson.Gson;
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
import ngocnth.momo.CaptureWalletRes;
import ngocnth.momo.CaptureWalletReq;
import ngocnth.momo.TrackOrderThread;
import ngocnth.order.OrderDAO;
import ngocnth.order.OrderDTO;
import ngocnth.util.Constant;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "OnlinePaymentServlet", urlPatterns = {"/OnlinePaymentRequestServlet"})
public class OnlinePaymentRequestServlet extends HttpServlet {

    private final String PAGE_404 = "404.html";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(OnlinePaymentRequestServlet.class);
    
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
        String orderId = request.getParameter("txtOrderId");
        String url = ERROR_PAGE;
        try {
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO orderDTO = orderDAO.getOrder(orderId);
            if (orderDTO != null) {
                CaptureWalletReq data = new CaptureWalletReq(Constant.MOMO_RETURN_URL, orderId, orderDTO.getTotal());
                String jsonResponse = Request.Post(Constant.MOMO_API_ENDPOINT)
                        .bodyString(new Gson().toJson(data), ContentType.APPLICATION_JSON)
                        .execute().returnContent().asString();
                CaptureWalletRes capture = new Gson().fromJson(jsonResponse, CaptureWalletRes.class);
                if (capture.isValid()) {
                    url = capture.getPayUrl();
                    new TrackOrderThread(orderId).start();
                }
            } else {
                url = PAGE_404;
            }
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("NoSuchAlgorithmException", ex);
        } catch (InvalidKeyException ex) {
            LOGGER.error("InvalidKeyException", ex);
        } finally {
            if (url.equals(PAGE_404) || url.equals(ERROR_PAGE)) {
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            } else {
                response.sendRedirect(url);
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
