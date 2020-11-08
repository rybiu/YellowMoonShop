/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ngocnth.product.ProductDAO;
import ngocnth.product.ProductDetail;
import ngocnth.util.Constant;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "ViewProductServlet", urlPatterns = {"/ViewProductServlet"})
public class ViewProductServlet extends HttpServlet {
    
    private final String VIEW_PRODUCT_PAGE = "viewProduct.jsp";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(ViewProductServlet.class);

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
        String searchValue = request.getParameter("txtSearchValue");
        String pageIndexStr = request.getParameter("txtPageIndex");
        int pageIndex;
        try {
            pageIndex = Integer.parseInt(pageIndexStr);
        } catch (NumberFormatException e) {
            pageIndex = 1;
        }
        String url = ERROR_PAGE;
        try {
            if (searchValue == null) searchValue = "";
            ProductDAO dao = new ProductDAO();
            request.setAttribute("SEARCH_RESULT_PAGE_SIZE", Constant.PAGING_SIZE);
            int count = dao.getProductListCount(searchValue);
            int maxIndex = (int) Math.ceil((double)count / Constant.PAGING_SIZE);
            request.setAttribute("SEARCH_RESULT_MAX_INDEX", maxIndex);
            if (pageIndex <= 0 || pageIndex > maxIndex) pageIndex = 1;
            request.setAttribute("SEARCH_RESULT_PAGE_INDEX", pageIndex);
            int start = (pageIndex - 1) * Constant.PAGING_SIZE;
            List<ProductDetail> products = dao.getProductList(searchValue, start, Constant.PAGING_SIZE);
            request.setAttribute("SEARCH_RESULT", products);
            url = VIEW_PRODUCT_PAGE;
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
