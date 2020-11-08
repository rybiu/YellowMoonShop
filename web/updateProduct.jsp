<%-- 
    Document   : addProduct
    Created on : Oct 6, 2020, 10:26:37 AM
    Author     : Ruby
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Update Product Page</title>
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
                        <li class="nav-item active"><a class="nav-link" href="viewProduct">Update Product</a></li>
                    </c:if>            
                    <li class="nav-item"><a class="nav-link" href="viewOrder">Track Order</a></li>
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
                <c:set var="product" value="${ requestScope.PRODUCT }" />
                <c:set var="error" value="${ requestScope.UPDATE_PRODUCT_ERROR }" />
                <form action="updateProduct" method="POST" enctype="multipart/form-data"
                        class="col col-md-8 m-auto bg-dark text-light p-5 rounded shadow">
                    
                  <h1 class="mb-5">Edit product</h1>
                  
                  <input type="hidden" name="txtProductId" value="${ product.id }" />
                  
                  <div class="form-row">
                    <div class="form-group col-6">
                      <label for="inputName">Product Name (1 - 50 chars)</label>
                      <input type="text" class="form-control" id="inputName" 
                             name="txtName" value="${ product.name }" required>
                      <c:if test="${ not empty error.nameLengthError }">
                        <small class="form-text text-danger">${ error.nameLengthError }</small>
                      </c:if>
                    </div>
                    <div class="form-group col-6">
                      <label for="inputPrice">Product Price</label>
                      <input type="number" class="form-control" id="inputPrice" min="0"
                             name="txtPrice" value="${ product.price }" required>
                      <c:if test="${ not empty error.priceValueError }">
                        <small class="form-text text-danger">${ error.priceValueError }</small>
                      </c:if>
                    </div>
                  </div>

                  <div class="form-group">
                    <label for="inputDescription">Product Description (0 - 300 chars)</label>
                    <textarea class="form-control" id="inputDescription" rows="3" name="txtDescription">${ product.description }</textarea>
                    <c:if test="${ not empty error.descriptionLengthError }">
                        <small class="form-text text-danger">${ error.descriptionLengthError }</small>
                    </c:if>
                  </div>

                  <div class="form-row">
                    <div class="form-group col-md-4">
                      <label for="inputQuantity">Quantity</label>
                      <input type="number" class="form-control" id="inputQuantity" min="0"
                             name="txtQuantity" value="${ product.quantity }" required >
                      <c:if test="${ not empty error.quantityValueError }">
                        <small class="form-text text-danger">${ error.quantityValueError }</small>
                      </c:if>
                    </div>
                    
                    <div class="form-group col-md-4">
                      <label for="inputCategory">Category</label>
                      <select id="inputCategory" class="form-control" name="slCategory">
                        <option></option>
                        <c:forEach var="category" items="${ requestScope.CATEGORY_LIST }">
                            <c:if test="${ product.categoryId eq category.id }">
                                <option selected>${ category.name }</option>
                            </c:if>
                            <c:if test="${ product.categoryId ne category.id }">
                                <option>${ category.name }</option>
                            </c:if>
                        </c:forEach>
                      </select>
                      <c:if test="${ not empty error.categoryValueError }">
                        <small class="form-text text-danger">${ error.categoryValueError }</small>
                      </c:if>
                    </div>
                    <div class="form-group col-md-4">
                      <label for="inputStatus">Status</label>
                      <select id="inputStatus" class="form-control" name="slStatus">
                        <c:forEach var="status" items="${ requestScope.STATUS_LIST }">
                            <c:if test="${ product.statusId eq status.id }">
                                <option value="${ status.name }" selected>${ status.description }</option>
                            </c:if>
                            <c:if test="${ product.statusId ne status.id }">
                                <option value="${ status.name }">${ status.description }</option>
                            </c:if>
                        </c:forEach>
                      </select>
                      <c:if test="${ not empty error.statusIdValueError }">
                        <small class="form-text text-danger">${ error.statusIdValueError }</small>
                      </c:if>
                    </div>
                  </div>

                  <div class="form-row">
                    <div class="form-group col-6">
                      <label for="inputCreateDate">Create Date</label>
                      <input type="date" class="form-control" id="inputCreateDate" 
                             name="txtCreateDate" value="${ product.createDate }" required />
                      <c:if test="${ not empty error.createDateFormatError }">
                        <small class="form-text text-danger">${ error.createDateFormatError }</small>
                      </c:if>
                    </div>
                    <div class="form-group col-6">
                      <label for="inputExpirationDate">Expiration Date</label>
                      <input type="date" class="form-control" id="inputExpirationDate" 
                             name="txtExpirationDate" value="${ product.expirationDate }" required />
                      <c:if test="${ not empty error.expirationDateFormatError }">
                        <small class="form-text text-danger">${ error.expirationDateFormatError }</small>
                      </c:if>
                    </div>
                  </div>
                    
                  <div class="form-group">
                    <label for="customFile" class="mr-3">Choose Article Image</label>
                    <input type="file" id="customFile" name="imgFile" accept="image/*">
                  </div>
                  
                  <div class="form-row">
                    <div class="form-group col-4">
                       <button type="submit" class="btn btn btn btn-outline-light mr-3">Edit Product</button>
                       <a href="removeImage?txtProductId=${ product.id }">
                         <button type="button" class="btn btn btn btn-outline-light">Remove Image</button>
                       </a>
                    </div>  
                    <div class="form-group col">
                         <c:if test="${ not empty product.image }">
                           <image src="uploads/${ product.image }" />
                       </c:if>
                    </div>
                  </div>
                  
                </form>
            </div>
            
            <c:if test="${ not empty requestScope.UPDATE_PRODUCT_RESULT }">
            <div class="row mb-0 content-part">
                <div class="col col-8 p-5 m-auto bg-dark text-light rounded shadow">
                  <h5>${ requestScope.UPDATE_PRODUCT_RESULT }</h5> 
                </div>
            </div>  
            </c:if>

        <div>
            <h6 class="p-4 mb-0 text-light text-center">&copy; 2020 Yellow Moon Shop</h6>
        </div>
        
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </body>
</html>
