<%-- 
    Document   : checkOut
    Created on : Oct 5, 2020, 10:14:32 AM
    Author     : Ruby
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Check Out</title>
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
                        <li class="nav-item active"><a class="nav-link" href="checkOut">Check Out</a></li>
                    </c:if>
                    <c:if test="${ user.roleName ne roleUser }">
                        <li class="nav-item"><a class="nav-link" href="addProduct">Add Product</a></li>
                        <li class="nav-item"><a class="nav-link" href="viewProduct">Update Product</a></li>
                    </c:if>            
                    <li class="nav-item"><a class="nav-link" href="viewOrder">Track Order</a></li>
                    <li class="nav-item"><a class="nav-link" href="logout">Log Out</a></li>
                  </c:if>
                  <c:if test="${ empty user }">
                    <li class="nav-item"><a class="nav-link" href="viewCart">View Cart</a></li>
                    <li class="nav-item active"><a class="nav-link" href="checkOut">Check Out</a></li>
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
            
            <c:set var="products" value="${ requestScope.PRODUCT_LIST }" />
            <c:if test="${ not empty products }">
                <c:set var="error" value="${ requestScope.CHECK_OUT_ERROR }" />
                <div class="row content-part">
                    <form action="checkOut" method="POST" class="col col-md-8 m-auto bg-dark text-light p-5 rounded shadow">
                      <h1 class="mb-5">Check Out</h1>
                      <div class="form-row">
                        <div class="form-group col-md-6">
                          <label for="inputName">Name (1 - 50 chars)</label>
                          <input type="text" class="form-control" id="inputName" name="txtName" required
                                <c:if test="${ not empty param.txtName }">
                                    value="${ param.txtName }"
                                </c:if>
                                <c:if test="${ empty param.txtName }">
                                    value="${ user.name }"
                                </c:if> />
                          <c:if test="${ not empty error.nameLengthError }">
                            <small class="form-text text-danger">${ error.nameLengthError }</small>
                          </c:if>
                        </div>
                        <div class="form-group col-md-6">
                          <label for="inputPhone">Phone Number (1 - 15 chars)</label>
                          <input type="text" class="form-control" id="inputPhone" name="txtPhone" required
                                <c:if test="${ not empty param.txtPhone }">
                                    value="${ param.txtPhone }"
                                </c:if>
                                <c:if test="${ empty param.txtPhone }">
                                    value="${ user.phone }"
                                </c:if> />
                          <c:if test="${ not empty error.phoneLengthError }">
                            <small class="form-text text-danger">${ error.phoneLengthError }</small>
                          </c:if>
                        </div>
                      </div>
                      <div class="form-row">
                        <div class="form-group col-7">
                          <label for="inputAddress">Address (1 - 100 chars)</label>
                          <input type="text" class="form-control" id="inputAddress" name="txtAddress" required
                                <c:if test="${ not empty param.txtAddress }">
                                    value="${ param.txtAddress }"
                                </c:if>
                                <c:if test="${ empty param.txtAddress }">
                                    value="${ user.address }"
                                </c:if> />
                          <c:if test="${ not empty error.addressLengthError }">
                            <small class="form-text text-danger">${ error.addressLengthError }</small>
                          </c:if>
                        </div>
                        <div class="form-group col">
                          <label for="inputAddress">Payment</label>
                          <div class="input-group col">
                            <div class="input-group-prepend">
                              <div class="input-group-text">
                              <input type="radio" name="txtPayment" value="CASH" checked>
                              </div>
                            </div>
                            <input type="text" class="form-control" value="Cash" readonly>
                          </div>
                          <div class="input-group col mt-2">
                            <div class="input-group-prepend">
                              <div class="input-group-text">
                              <input type="radio" name="txtPayment" value="ONLINE" >
                              </div>
                            </div>
                            <input type="text" class="form-control" value="Online (by Momo)" readonly>
                          </div>
                          <c:if test="${ not empty error.invalidPaymentName }">
                            <small class="form-text text-danger">${ error.invalidPaymentName }</small>
                          </c:if>
                        </div>
                      </div>
                      <button type="submit" class="btn btn btn btn-outline-light">Check Out</button>
                    </form>
                </div>
                
                <div class="row content-part">
                    <div class="col col-8 pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                        <h3>Your cart</h3>
                    </div>
                </div>    

                <div class="row pb-5">
                    <table class="col col-8 table table-striped m-auto text-center">
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
                                <td class="text-left">${ dto.productName }</td>
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
                            <th scope="col">
                                <h3><fmt:formatNumber type="number" pattern="###,### đ" value="${ sessionScope.CART.total }" /></h3>
                            </th>
                        </tr>
                      </tfoot>
                    </table>
                </div>
            </c:if>
            <c:if test="${ empty products }">
                <div class="row content-part">
                    <div class="col col-8 pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                        <h3>There is no products in your cart.</h3>
                    </div>
                </div>   
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