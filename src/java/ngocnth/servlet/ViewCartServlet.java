/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ngocnth.order.OrderDTO;
import ngocnth.order.detail.OrderDetailDTO;
import ngocnth.order.detail.OrderDetailView;
import ngocnth.product.ProductDAO;
import ngocnth.product.ProductDTO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "ViewCartServlet", urlPatterns = {"/ViewCartServlet"})
public class ViewCartServlet extends HttpServlet {
    
    private final String VIEW_CART_PAGE = "viewCart.jsp";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(ViewCartServlet.class);

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
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("CART") != null) {
                OrderDTO order = (OrderDTO) session.getAttribute("CART");
                List<OrderDetailDTO> orderDetailList = (List) order.getProducts();
                if (orderDetailList != null) {
                    List<OrderDetailView> list = new ArrayList<>();
                    ProductDAO productDAO = new ProductDAO();
                    boolean removeFlag = false;
                    for (int i = 0; i < orderDetailList.size(); i++) {
                        OrderDetailDTO orderDetail = orderDetailList.get(i);
                        ProductDTO productDTO = productDAO.getProductWithStatus(orderDetail.getProductId());
                        if (productDTO != null) {
                            orderDetail.setPrice(productDTO.getPrice());
                            OrderDetailView item = new OrderDetailView(orderDetail);
                            item.setProductName(productDTO.getName());
                            item.setMaxQuantity(productDTO.getQuantity());
                            list.add(item);
                        } else {
                            orderDetailList.remove(orderDetail);
                            removeFlag = true;
                            i--;
                        }
                    }
                    if (removeFlag) {
                        request.setAttribute("VIEW_CART_MESSAGE", "We have removed some products that no longer exist. Please check it!");
                    }
                    request.setAttribute("PRODUCT_LIST", list);
                    session.setAttribute("CART", order);
                }
            }
            url = VIEW_CART_PAGE;
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
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
