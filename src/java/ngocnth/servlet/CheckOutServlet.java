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
import ngocnth.order.OrderCheckOutError;
import ngocnth.order.OrderDAO;
import ngocnth.order.OrderDTO;
import ngocnth.order.detail.OrderDetailDTO;
import ngocnth.order.detail.OrderDetailView;
import ngocnth.payment.PaymentDAO;
import ngocnth.payment.PaymentDTO;
import ngocnth.product.ProductDAO;
import ngocnth.product.ProductDTO;
import ngocnth.user.UserDTO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "CheckOutServlet", urlPatterns = {"/CheckOutServlet"})
public class CheckOutServlet extends HttpServlet {

    private final String VIEW_CART_SERVLET = "viewCart";
    private final String VIEW_ORDER_SERVLET = "trackOrder";
    private final String ONLINE_PAYMENT_REQ_SERVLET = "reqOP";
    private final String CHECK_OUT_PAGE = "checkOut.jsp";
    private final String CHECK_OUT_SUCCCESS_PAGE = "checkOutSuccess.jsp";
    private final String ERROR_PAGE = "error.html";
    
    private static final Logger LOGGER = LogManager.getLogger(CheckOutServlet.class);
    
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
        String name = request.getParameter("txtName");
        String phone = request.getParameter("txtPhone");
        String address = request.getParameter("txtAddress");
        String payment = request.getParameter("txtPayment");
        String url = ERROR_PAGE;
        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("CART") != null) {
                OrderDTO order = (OrderDTO) session.getAttribute("CART");
                List<OrderDetailDTO> products = (List) order.getProducts();
                if (products != null) {
                    boolean checkCart = true;
                    List<OrderDetailView> list = new ArrayList<>();
                    ProductDAO dao = new ProductDAO();
                    int total = 0;
                    for (OrderDetailDTO product : products) {
                        ProductDTO dto = dao.getProductWithStatus(product.getProductId());
                        if (dto != null && product.getQuantity() <= dto.getQuantity()) {
                            product.setPrice(dto.getPrice());
                            OrderDetailView item = new OrderDetailView(product);
                            item.setProductName(dto.getName());
                            list.add(item);
                            total = total + product.getQuantity() * product.getPrice();
                        } else {
                            checkCart = false;
                            url = VIEW_CART_SERVLET;
                            break;
                        }
                    }
                    if (checkCart) {
                        request.setAttribute("PRODUCT_LIST", list);
                        order.setTotal(total);
                        session.setAttribute("CART", order);
                        url = CHECK_OUT_PAGE;
                        if (request.getMethod().equals("POST")) {
                            OrderCheckOutError error = new OrderCheckOutError();
                            boolean fountErr = false;
                            int paymentId = 0;
                            //check input
                            if (name == null || name.trim().isEmpty() || name.trim().length() > 50) {
                                fountErr = true;
                                error.setNameLengthError("Name must be required and its max length is 50 chars");
                            }
                            if (phone == null || phone.trim().isEmpty() || phone.trim().length() > 15) {
                                fountErr = true;
                                error.setPhoneLengthError("Phone number must be required and its max length is 15 chars");
                            }
                            if (address == null || address.trim().isEmpty() || address.trim().length() > 100) {
                                fountErr = true;
                                error.setAddressLengthError("Address must be required and its max length is 100 chars");
                            }
                   
                            PaymentDAO paymentDAO = new PaymentDAO();
                            PaymentDTO paymentDTO = paymentDAO.getPayment(payment);
                            if (paymentDTO == null) {
                                fountErr = true;
                                error.setInvalidPaymentName("Payment method is not valid");
                            } else {
                                paymentId = paymentDTO.getId();
                            }
                            
                            if (fountErr) {
                                request.setAttribute("CHECK_OUT_ERROR", error);
                                url = CHECK_OUT_PAGE;
                            } else {
                                UserDTO user = (UserDTO) session.getAttribute("USER");
                                if (user != null) order.setUserId(user.getId());
                                order.setDate();
                                order.setName(name);
                                order.setPhone(phone);
                                order.setAddress(address);
                                order.setPaymentId(paymentId);
                                OrderDAO orderDAO = new OrderDAO();
                                String orderId = orderDAO.addOrder(order);
                                if (orderId != null){
                                    session.removeAttribute("CART");
                                    if (payment.equals(PaymentDAO.METHOD_ONLINE)) {
                                        url = ONLINE_PAYMENT_REQ_SERVLET + "?txtOrderId=" + orderId;
                                    } else if (user != null){
                                        url = VIEW_ORDER_SERVLET + "?txtOrderId=" + orderId;
                                    } else {
                                        request.setAttribute("ORDER_ID", orderId);
                                        url = CHECK_OUT_SUCCCESS_PAGE;
                                    }
                                }
                            } 
                        }
                    }
                }
            } else {
                url = CHECK_OUT_PAGE;
            }
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } finally {
            if (url.equals(CHECK_OUT_PAGE) || url.equals(CHECK_OUT_SUCCCESS_PAGE) || url.equals(ERROR_PAGE)) {
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
