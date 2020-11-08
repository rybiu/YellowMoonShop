/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ngocnth.order.OrderDTO;
import ngocnth.product.ProductDAO;
import ngocnth.product.ProductDTO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "RemoveFromCartServlet", urlPatterns = {"/RemoveFromCartServlet"})
public class RemoveFromCartServlet extends HttpServlet {

    private final String VIEW_CART_SERVLET = "viewCart";
    private final String PAGE_404 = "404.html";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(RemoveFromCartServlet.class);
    
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
        String productIdStr = request.getParameter("txtProductId");
        String url = VIEW_CART_SERVLET;
        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("CART") != null) {
                OrderDTO order = (OrderDTO) session.getAttribute("CART");
                int productId = Integer.parseInt(productIdStr);
                ProductDAO productDAO = new ProductDAO();
                ProductDTO productDTO = productDAO.getProductWithStatus(productId);
                if (productDTO != null) {
                    order.removeProduct(productId);
                    if (order.getProducts() == null) {
                        session.removeAttribute("CART");
                    } else {
                        session.setAttribute("CART", order);
                    }
                } else {
                    url = PAGE_404;
                }
            }
        } catch (NumberFormatException e) {
            url = PAGE_404;
        } catch (NamingException ex) {
            url = ERROR_PAGE;
            LOGGER.error("NamingException", ex);
        } catch (SQLException ex) {
            url = ERROR_PAGE;
            LOGGER.error("SQLException", ex);
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
