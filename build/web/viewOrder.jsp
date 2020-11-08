<%-- 
    Document   : viewOrder
    Created on : Oct 4, 2020, 11:25:09 PM
    Author     : Ruby
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Track Order Page</title>
    </head>
    <body class="bg-dark">
        
        <div>
            <h1 class="pb-5 mb-0 logo">Yellow Moon Shop</h1>
        </div>
        
        <c:set var="user" value="${ sessionScope.USER }" />
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark navbar-fixed-top">
            <div class="container-fluid">
              <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExample08" aria-controls="navbarsExample08" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
              </button> 

              <div class="collapse navbar-collapse justify-content-md-center" id="navbarsExample08">
                <ul class="navbar-nav">
                  <li class="nav-item">
                    <a class="nav-link" href="shop">Shopping</a>
                  </li>
                  <c:if test="${ not empty user }">
                    <c:set var="roleUser" value="ROLE_USER" />
                    <c:if test="${ user.roleName eq roleUser }">
                        <li class="nav-item"><a class="nav-link" href="viewCart">View Cart</a></li>
                        <li class="nav-item"><a class="nav-link" href="checkOut">Check Out</a></li>
                    </c:if>
                    <c:if test="${ user.roleName ne roleUser }">
                        <li class="nav-item"><a class="nav-link" href="addProduct">Add Product</a></li>
                        <li class="nav-item"><a class="nav-link" href="viewProduct">Update Product</a></li>
                    </c:if>            
                    <li class="nav-item active"><a class="nav-link" href="viewOrder">Track Order</a></li>
                    <li class="nav-item"><a class="nav-link" href="logout">Log Out</a></li>
                  </c:if>
                  <c:if test="${ empty user }">
                    <li class="nav-item"><a class="nav-link" href="viewCart">View Cart</a></li>
                    <li class="nav-item"><a class="nav-link" href="checkOut">Check Out</a></li>
                    <li class="nav-item"><a class="nav-link" href="login">Log In</a></li>
                  </c:if>
                </ul>
              </div>
            </div>
        </nav>

        <div class="container-fluid bg-light page-content">
            <c:if test="${ not empty user }">
                <div class="row content-part">
                    <h3 class="col col-4 m-auto mb-0 text-center">Welcome, ${ user.name }!</h3>
                </div>
            </c:if>  

            <div class="row content-part">
                <div class="col col-md-8 m-auto bg-dark text-light p-5 rounded shadow">
                  <h1 class="mb-5">Track order</h1>
                  <form action="trackOrder" method="POST">
                    <div class="form-row">
                      <div class="form-group col">
                        <label for="inputOrderId">Order ID</label>
                        <input type="text" class="form-control" id="inputOrderId" required
                               name="txtOrderId" value="${ param.txtOrderId }" />
                        <c:if test="${ not empty requestScope.VIEW_ORDER_ERROR }">
                            <small id="emailHelp" class="form-text text-danger">${ requestScope.VIEW_ORDER_ERROR }</small>
                        </c:if>
                      </div>
                    </div>
                    <button type="submit" class="btn btn btn btn-outline-light">Search Order</button>
                  </form>
                </div>
            </div>
                        
            <c:if test="${ not empty param.txtOrderId }">
                <c:set var="order" value="${ requestScope.ORDER }" />
                <c:set var="products" value="${ order.products }" />
                <c:if test="${ not empty order }">
                    <div class="row mb-0 content-part">
                        <div class="col col-8 pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                            <h3>We found this</h3>
                        </div>
                    </div>  
                    
                    <div class="row mb-0">
                        <div class="col col-8 p-3 m-auto">
                          <div class="form-row">
                            <div class="form-group col-md-5">
                              <label>Name</label>
                              <input type="text" class="form-control" readonly value="${ order.name }" />
                            </div>
                            <div class="form-group col-md-7">
                              <label>Date</label>
                              <input type="text" class="form-control" readonly value="${ order.date }" />
                            </div>
                          </div>
                          <div class="form-row">
                            <div class="form-group col">
                              <label>Address</label>
                              <input type="text" class="form-control" readonly value="${ order.address }" />
                            </div>
                          </div>
                          <div class="form-row">
                            <div class="form-group col-md-4">
                              <label>Phone</label>
                              <input type="text" class="form-control" readonly value="${ order.phone }" />
                            </div>
                            <div class="form-group col-md-4">
                              <label>Payment</label>
                              <input type="text" class="form-control" readonly value="${ order.paymentName }" />
                            </div>
                            <div class="form-group col-md-4">
                              <label>Payment Status</label>
                              <input type="text" class="form-control" readonly value="${ order.paymentStatus }" />
                            </div>
                          </div>
                        </div>
                    </div>  
 
                      
                    
                    <div class="row pb-5">
                        <table class="col col-8 table table-striped m-auto">
                          <thead>
                            <tr>
                              <th scope="col">#</th>
                              <th scope="col">Name</th>
                              <th scope="col">Price</th>
                              <th scope="col">Quantity</th>
                              <th scope="col">Total</th>
                            </tr>
                          </thead>
                          <tbody>
                            <c:forEach var="dto" items="${ products }" varStatus="counter">
                                <tr>
                                    <td>${ counter.count }</td>
                                    <td>${ dto.productName }</td>
                                    <td><fmt:formatNumber type="number" pattern="###,### đ" value="${ dto.price }" /></td>
                                    <td>${ dto.quantity }</td>
                                    <td><fmt:formatNumber type="number" pattern="###,### đ" value="${ dto.quantity *  dto.price}" /></td>
                                </tr>
                            </c:forEach>
                          </tbody>
                          <tfoot>
                            <tr>
                                <th scope="col" colspan="3"></th>
                                <th scope="col"><h3>Subtotal</h3></th>
                                <th scope="col"><h3><fmt:formatNumber type="number" pattern="###,### đ" value="${ order.total }" /></h3></th>
                            </tr>
                          </tfoot>
                        </table>
                    </div>
                </c:if>
                <c:if test="${ empty order }">
                    <div class="row mb-0 content-part">
                        <div class="col col-8 pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                            <h3>This order id is not existed.</h3>
                        </div>
                    </div>   
                </c:if>
            </c:if>
        </div>

        <div>
            <h6 class="p-4 mb-0 text-light text-center">&copy; 2020 Yellow Moon Shop</h6>
        </div>
        
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>
</html>
