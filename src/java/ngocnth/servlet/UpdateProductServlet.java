/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import ngocnth.category.CategoryDAO;
import ngocnth.category.CategoryDTO;
import ngocnth.log.LogDAO;
import ngocnth.log.LogDTO;
import ngocnth.product.ProductDAO;
import ngocnth.product.ProductDTO;
import ngocnth.product.ProductSetError;
import ngocnth.status.StatusDAO;
import ngocnth.status.StatusDTO;
import ngocnth.user.UserDTO;
import ngocnth.util.Constant;
import ngocnth.util.FileHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "UpdateProductServlet", urlPatterns = {"/updateProduct"})
@MultipartConfig()
public class UpdateProductServlet extends HttpServlet {
    
    private final String UPDATE_PRODUCT_PAGE = "updateProduct.jsp";
    private final String PAGE_404 = "404.html";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(UpdateProductServlet.class);

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
        String url = ERROR_PAGE;
        try {
            int productId = Integer.parseInt(productIdStr);
            ProductDAO productDAO = new ProductDAO();
            ProductDTO productDTO = productDAO.getProduct(productId);
            if (productDTO == null) {
                url = PAGE_404;
            } else {
                CategoryDAO categoryDAO = new CategoryDAO();
                List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
                request.setAttribute("CATEGORY_LIST", categoryList);
                StatusDAO statusDAO = new StatusDAO();
                List<StatusDTO> statusList = statusDAO.getStatusWithPrefix("PRODUCT");
                request.setAttribute("STATUS_LIST", statusList);
                request.setAttribute("PRODUCT", productDTO);
                if (request.getMethod().equals("POST")) {
                    String name = request.getParameter("txtName");
                    String description = request.getParameter("txtDescription");
                    String createDateStr = request.getParameter("txtCreateDate");
                    String expirationDateStr = request.getParameter("txtExpirationDate");
                    String quantityStr = request.getParameter("txtQuantity");
                    String priceStr = request.getParameter("txtPrice");
                    String categoryName = request.getParameter("slCategory");
                    String statusName = request.getParameter("slStatus");
                    Part filePart = request.getPart("imgFile");
                    LocalDate createDate = null, expirationDate = null;
                    int quantity = 0, price = 0, categoryId = -1, statusId = 0;
                    String realPath = request.getServletContext().getRealPath("/") + Constant.UPLOAD_DIR;
                    String fileName = null;
                    boolean foundError = false;
                    ProductSetError error = new ProductSetError();
                    if (name == null || name.trim().isEmpty() || name.trim().length() > 50) {
                        foundError = true;
                        error.setNameLengthError("The product name must be required and its max length is 50 chars.");
                    }
                    if (description != null && description.length() > 300) {
                        foundError = true;
                        error.setDescriptionLengthError("The product description max length is 300 chars.");
                    }
                    try {
                        createDate = LocalDate.parse(createDateStr);
                    } catch (DateTimeParseException e) {
                        foundError = true;
                        error.setCreateDateFormatError("The create date must be required and follow the correct format.");
                    }
                    try {
                        expirationDate = LocalDate.parse(expirationDateStr);
                        if (expirationDate.isBefore(createDate)) {
                            foundError = true;
                            error.setExpirationDateFormatError("The expiration date must be after the create date.");
                        }
                    } catch (DateTimeParseException e) {
                        foundError = true;
                        error.setExpirationDateFormatError("The expiration date must be required and follow the correct format.");
                    }
                    try {
                        quantity = Integer.parseInt(quantityStr);
                        if (quantity < 0) {
                            foundError = true;
                            error.setQuantityValueError("The product quantity must not be a negative number.");
                        }
                    } catch (NumberFormatException e) {
                        foundError = true;
                        error.setQuantityValueError("The product quantity must not be a negative number.");
                    }
                    try {
                        price = Integer.parseInt(priceStr);
                        if (price < 0) {
                            foundError = true;
                            error.setPriceValueError("The product price must not be a negative number.");
                        }
                    } catch (NumberFormatException e) {
                        foundError = true;
                        error.setPriceValueError("The product price must not be a negative number.");
                    }
                    if (categoryName != null && !categoryName.isEmpty()) {
                        CategoryDTO categoryDTO = categoryDAO.getCategory(categoryName);
                        if (categoryDTO == null) {
                            foundError = true;
                            error.setCategoryValueError("The category name is not valid.");
                        } else {
                            categoryId = categoryDTO.getId();
                        }
                    }
                    StatusDTO statusDTO = statusDAO.getStatus(statusName);
                    if (statusDTO == null) {
                        foundError = true;
                        error.setStatusIdValueError("The status name is not valid.");
                    } else {
                        statusId = statusDTO.getId();
                    }
                    if (foundError) {
                        request.setAttribute("UPDATE_PRODUCT_ERROR", error);
                    } else {
                        if (filePart != null && filePart.getSize() > 0) {
                            File image = FileHelper.uploadFile(filePart, realPath);
                            fileName = image.getName();
                        }
                        HttpSession session = request.getSession();
                        UserDTO user = (UserDTO) session.getAttribute("USER");
                        ProductDTO temp = new ProductDTO(productId, name, description, createDate,
                                expirationDate, quantity, price, categoryId, statusId);
                        boolean result = productDAO.updateProduct(temp);
                        if (fileName != null) {
                            if (productDTO.getImage() != null && !productDTO.getImage().isEmpty())
                                FileHelper.deleteFile(productDTO.getImage(), realPath);
                            result = result && productDAO.updateProductImage(productId, fileName);
                        }
                        if (result) {
                            temp = productDAO.getProduct(productId);
                            request.setAttribute("PRODUCT", temp);
                            request.setAttribute("UPDATE_PRODUCT_RESULT", "The product has been edited successfully.");
                            LogDAO logDAO = new LogDAO();
                            logDAO.addLog(new LogDTO(user.getId(), productId));
                        } else {
                            request.setAttribute("UPDATE_PRODUCT_RESULT", "The product has been edited failed.");
                        }
                    }
                }
                url = UPDATE_PRODUCT_PAGE;
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
        }  catch (IOException ex) {
            LOGGER.error("IOException", ex);
        } catch (NumberFormatException ex) {
            url = PAGE_404;
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
