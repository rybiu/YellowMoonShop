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
import ngocnth.category.CategoryDAO;
import ngocnth.category.CategoryDTO;
import ngocnth.product.ProductDAO;
import ngocnth.product.ProductDetail;
import ngocnth.product.ProductSearchError;
import ngocnth.util.Constant;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {

    private final String SEARCH_PAGE = "search.jsp";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(SearchServlet.class);
    
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
        String minStr = request.getParameter("txtMinRange");
        String maxStr = request.getParameter("txtMaxRange");
        String categoryName = request.getParameter("txtCategory");
        String pageIndexStr = request.getParameter("txtPageIndex");
        String url = ERROR_PAGE;
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
            request.setAttribute("CATEGORY_LIST", categoryList);
            int min = -1, max = -1, categoryId = -1, pageIndex;
            boolean hasInput;
            boolean foundError = false;
            ProductSearchError error = new ProductSearchError();
            // check search value
            hasInput = searchValue != null && !searchValue.isEmpty();
            // check min price
            try {
                if (minStr != null && !minStr.isEmpty()) {
                    min = Integer.parseInt(minStr);
                    if (min < 0) {
                        foundError = true;
                        error.setInvalidMin("Min price must not be a negative number.");
                    } else {
                        hasInput = true;
                    }
                }
            } catch (NumberFormatException e) {
                error.setInvalidMin("Min price must not be a negative number.");
            }
            // check max price
            try {
                if (maxStr != null && !maxStr.isEmpty()) {
                    max = Integer.parseInt(maxStr);
                    if (max < 0) {
                        foundError = true;
                        error.setInvalidMax("Max price must not be a negative number.");
                    } else if (error.getInvalidMin() == null && max < min) {
                        foundError = true;
                        error.setInvalidMax("Max value must be a greater than min value.");
                    } else {
                        hasInput = true;
                    }
                }
            } catch (NumberFormatException e) {
                foundError = true;
                error.setInvalidMax("Max price must not be a negative number.");
            }
            // check category id
            if (categoryName != null && !categoryName.isEmpty()) {
                CategoryDTO category = categoryDAO.getCategory(categoryName);
                if (category == null) {
                    foundError = true;
                    error.setInvalidCategory("This category does not exist.");
                } else {
                    categoryId = category.getId();
                    hasInput = true;
                }
            }
            // check page index
            try {
                pageIndex = Integer.parseInt(pageIndexStr);
            } catch (NumberFormatException ex) {
                pageIndex = 1;
            }
            
            if (foundError) {
                request.setAttribute("SEARCH_ERROR", error);
            } else if (hasInput) {
                ProductDAO dao = new ProductDAO();
                request.setAttribute("SEARCH_RESULT_PAGE_SIZE", Constant.PAGING_SIZE);
                int count = dao.getProductListCount(searchValue, min, max, categoryId);
                int maxIndex = (int) Math.ceil((double)count / Constant.PAGING_SIZE);
                request.setAttribute("SEARCH_RESULT_MAX_INDEX", maxIndex);
                if (pageIndex <= 0 || pageIndex > maxIndex) pageIndex = 1;
                request.setAttribute("SEARCH_RESULT_PAGE_INDEX", pageIndex);
                int start = (pageIndex - 1) * Constant.PAGING_SIZE;
                List<ProductDetail> products = dao.getProductList(searchValue, min, max, categoryId, start, Constant.PAGING_SIZE);
                request.setAttribute("SEARCH_RESULT", products);
            }
            url = SEARCH_PAGE;
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
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
