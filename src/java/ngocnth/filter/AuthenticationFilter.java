/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ngocnth.role.RoleDAO;
import ngocnth.user.UserDAO;
import ngocnth.user.UserDTO;
import ngocnth.util.Constant;

/**
 *
 * @author Ruby
 */
public class AuthenticationFilter implements Filter {
    
    private static final boolean debug = true;
    private final String SEARCH_PAGE = "SearchServlet";

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public AuthenticationFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            //log("AuthenticationFilter:DoBeforeProcessing");
        }

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log items on the request object,
        // such as the parameters.
        /*
	for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    String values[] = request.getParameterValues(name);
	    int n = values.length;
	    StringBuffer buf = new StringBuffer();
	    buf.append(name);
	    buf.append("=");
	    for(int i=0; i < n; i++) {
	        buf.append(values[i]);
	        if (i < n-1)
	            buf.append(",");
	    }
	    log(buf.toString());
	}
         */
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            //log("AuthenticationFilter:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
        /*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
         */
        // For example, a filter might append something to the response.
        /*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (debug) {
            //log("AuthenticationFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        
        Throwable problem = null;
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        boolean checkLogin;
        try {
            int lastIndex = uri.lastIndexOf("/");
            String resource = uri.substring(lastIndex + 1);
            
            ServletContext servletContext = request.getServletContext();
            Map<String, String> nonAuthSiteMap = 
                    (Map<String, String>) servletContext.getAttribute("NON_AUTH_SITE_MAP");
            
            checkLogin = nonAuthSiteMap.containsKey(resource);
            
            UserDTO dto = null;
            
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("USER") == null) {
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    Cookie lastCookie;
                    for (int i = cookies.length - 1; i >= 0; i--) {
                        lastCookie = cookies[i];
                        String key = lastCookie.getName();
                        String value = lastCookie.getValue();
                        if (key.equals("Authentication")) {
                            if (value.trim().length() > 0) {
                                int delimiterIndex = value.indexOf("-");
                                String username = value.substring(0, delimiterIndex);
                                String password = value.substring(delimiterIndex + 1);
                                UserDAO dao = new UserDAO();
                                dto = dao.checkLogin(username, password);
                                if (dto != null) {
                                    session = req.getSession();
                                    session.setAttribute("USER", dto);
                                    checkLogin = true;
                                } 
                            }
                            break;
                        }
                    }
                } 
            } else {
                dto = (UserDTO) session.getAttribute("USER");
                checkLogin = true;
            }
            
            if (dto != null && dto.getRoleName().endsWith(RoleDAO.ROLE_ADMIN)) {
                checkLogin = !resource.equals("addCart") 
                        && !resource.equals("updateCart")
                        && !resource.equals("removeCart")
                        && !resource.equals("viewCart")
                        && !resource.equals("checkOut")
                        && !resource.equals("reqOP")
                        && !resource.equals("resOP");
            }
            
            if (dto != null && dto.getRoleName().endsWith(RoleDAO.ROLE_USER)) {
                checkLogin = !resource.equals("addProduct") 
                        && !resource.equals("viewProduct")
                        && !resource.equals("updateProduct")
                        && !resource.equals("removeImage")
                        && !resource.equals("viewUser");;
            }
            
            if (checkLogin) {
                chain.doFilter(request, response);
            } else if (uri.contains(Constant.UPLOAD_DIR)) {
                chain.doFilter(request, response);
            } else if (resource.endsWith(".css") || resource.endsWith(".js") || resource.endsWith(".ttf")) {
                chain.doFilter(request, response);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(SEARCH_PAGE);
                rd.forward(request, response);
            }
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            //log("AuthenticationFilter_doFilter_" + t.getMessage());
        }
        
        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                //log("AuthenticationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenticationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthenticationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
    
}
