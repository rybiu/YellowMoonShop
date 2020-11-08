<%-- 
    Document   : viewCart
    Created on : Oct 4, 2020, 11:25:49 PM
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
        <title>View Cart Page</title>
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
                        <li class="nav-item active"><a class="nav-link" href="viewCart">View Cart</a></li>
                        <li class="nav-item"><a class="nav-link" href="checkOut">Check Out</a></li>
                    </c:if>
                    <c:if test="${ user.roleName ne roleUser }">
                        <li class="nav-item"><a class="nav-link" href="addProduct">Add Product</a></li>
                        <li class="nav-item"><a class="nav-link" href="viewProduct">Update Product</a></li>
                    </c:if>            
                    <li class="nav-item"><a class="nav-link" href="viewOrder">Track Order</a></li>
                    <li class="nav-item"><a class="nav-link" href="logout">Log Out</a></li>
                  </c:if>
                  <c:if test="${ empty user }">
                    <li class="nav-item active"><a class="nav-link" href="viewCart">View Cart</a></li>
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
            <c:set var="products" value="${ requestScope.PRODUCT_LIST }" />
            <c:if test="${ not empty products }">
                <div class="row mb-0 content-part">
                    <div class="col col-8 pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                        <h3>View Cart</h3>
                    </div>
                </div>    

                <div class="row pb-5">
                    <table class="col col-8 table table-striped m-auto text-center">
                      <thead>
                        <tr>
                          <th scope="col">#</th>
                          <th scope="col">Name</th>
                          <th scope="col">Price</th>
                          <th scope="col" class="text-center">Quantity</th>
                          <th scope="col">Total</th>
                          <th scope="col" colspan="2">Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:set var="errorProductId" value="${ requestScope.CART_ERROR_ID }" />
                        <c:set var="total" value="0" />
                        <c:forEach var="dto" items="${ products }" varStatus="counter">
                            <c:set var="total" value="${ total + dto.price * dto.quantity }" />
                            <form action="updateCart" method="POST">
                                <tr>
                                    <th scope="row">${ counter.count }</td>
                                    <td class="text-left">${ dto.productName }</td>
                                    <td><fmt:formatNumber type="number" pattern="###,### đ" value="${ dto.price }" /></td>
                                    <td>
                                        <div class="form-group">
                                            <input type="hidden" name="txtProductId" value="${ dto.productId }" />
                                            <input type="number" class="form-control text-center" required min="1"
                                                   name="txtProductQuantity" value="${ dto.quantity }" />
                                            <c:if test="${ not empty error.invalidMax }">
                                              <small class="form-text text-danger">${ error.invalidMax }</small>
                                            </c:if>
                                        </div>
                                        <c:choose>
                                            <c:when test="${ dto.quantity gt dto.maxQuantity }">
                                                <small class="form-text text-danger">The quantity of this product is left at ${ dto.maxQuantity }.</small>
                                            </c:when>
                                            <c:when test="${ errorProductId eq dto.productId }">
                                                <small class="form-text text-danger">${ requestScope.CART_ERROR_MES }</small>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatNumber type="number" pattern="###,### đ" value="${ dto.price * dto.quantity }" /></td>
                                    <td>
                                        <input type="submit" value="Update" class="btn btn-outline-dark" />
                                    </td>
                                    <td>
                                        <c:url var="removeFromCartLink" value="removeCart">
                                            <c:param name="txtProductId" value="${ dto.productId }"></c:param>
                                        </c:url>
                                        <button type="button" class="btn btn-outline-dark" data-toggle="modal" 
                                            data-target="#productModal${ counter.count }">Remove</button>
                                        <!-- Modal -->
                                        <div class="modal fade" id="productModal${ counter.count }" tabindex="-1" role="dialog">
                                          <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                              <div class="modal-header">
                                                <h5 class="modal-title" id="commentModalLabel">Remove Product</h5>
                                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                  <span aria-hidden="true">&times;</span>
                                                </button>
                                              </div>
                                              <div class="modal-body text-left">
                                                  Are you sure to remove this product?
                                              </div>
                                              <div class="modal-footer">
                                                <button type="button" class="btn btn-outline-dark" data-dismiss="modal">Close</button>
                                                <a class="btn btn-outline-dark" href="${ removeFromCartLink }">Remove</a>
                                              </div>
                                            </div>
                                          </div>
                                        </div>
                                    </td>
                                </tr>
                            </form>
                        </c:forEach>
                      </tbody>
                      <tfoot>
                        <tr>
                            <th scope="col" colspan="3"></th>
                            <th scope="col"><h3>Subtotal</h3></th>
                            <th scope="col"><h3><fmt:formatNumber type="number" pattern="###,### đ" value="${ total }" /></h3></th>
                            <th scope="col" colspan="2"></th>
                        </tr>
                      </tfoot>
                    </table>
                </div>
                <c:if test="${ not empty requestScope.VIEW_CART_MESSAGE }">
                  <div class="row content-part">
                    <div class="col col-8 pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                        <p class="mb-0">${ requestScope.VIEW_CART_MESSAGE }</p>
                    </div>
                  </div>  
                </c:if> 
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
