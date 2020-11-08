/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ngocnth.go.GooglePojo;
import ngocnth.user.UserDAO;
import ngocnth.user.UserDTO;
import ngocnth.util.Constant;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Ruby
 */
@WebServlet(name = "LoginWithGoogleServlet", urlPatterns = {"/LoginWithGoogleServlet"})
public class LoginWithGoogleServlet extends HttpServlet {
    
    private static final Logger LOGGER = LogManager.getLogger(LoginWithGoogleServlet.class);

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
        try {
            String idToken = request.getParameter("idToken");
            if (idToken != null && !idToken.isEmpty()) {
                String link = Constant.GOOGLE_LINK_GET_TOKEN + idToken;
                String jsonResponse = Request.Get(link).execute().returnContent().asString();
                GooglePojo googlePojo = new Gson().fromJson(jsonResponse, GooglePojo.class);
                if (googlePojo.isValid()) {
                    String email = googlePojo.getEmail();
                    UserDAO dao = new UserDAO();
                    UserDTO user = dao.checkLoginByGoogle(email);
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("USER", user);
                        out.write("VALID");
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException", ex);
        } catch (NamingException ex) {
            LOGGER.error("NamingException", ex);
        } finally {
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
