<%-- 
    Document   : search
    Created on : Jun 10, 2020, 1:22:23 PM
    Author     : Ruby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%--@page session="false" --%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Shopping Page</title>
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
                  <li class="nav-item active">
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
                        <li class="nav-item"><a class="nav-link" href="viewUser">View User</a></li>
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
                <c:set var="error" value="${ requestScope.SEARCH_ERROR }" />
                <form class="col col-md-8 m-auto bg-dark text-light p-5 rounded shadow" action="shop">
                  <h1 class="mb-5">Search product</h1>
                  <div class="form-group">
                    <label for="inputSearchValue">Search Value</label>
                    <input type="text" class="form-control" id="inputSearchValue" 
                           name="txtSearchValue" value="${ param.txtSearchValue }">
                  </div>
                 
                  <div class="form-row">
                    <div class="form-group col-md-4">
                      <label for="inputMinPrice">Min Price</label>
                      <input type="number" class="form-control" id="inputMinPrice" min="0"
                             name="txtMinRange" value="${ param.txtMinRange }" />
                      <c:if test="${ not empty error.invalidMin }">
                        <small class="form-text text-danger">${ error.invalidMin }</small>
                      </c:if>
                    </div>
                      
                    <div class="form-group col-md-4">
                      <label for="inputMaxPrice">Max Price</label>
                      <input type="number" class="form-control" id="inputMaxPrice" min="0"
                             name="txtMaxRange" value="${ param.txtMaxRange }" />
                      <c:if test="${ not empty error.invalidMax }">
                        <small class="form-text text-danger">${ error.invalidMax }</small>
                      </c:if>
                    </div>
                      
                    <div class="form-group col-md-4">
                      <label for="inputCategory">Category</label>
                      <c:set var="categories" value="${ requestScope.CATEGORY_LIST }" />
                      <select id="inputCategory" class="form-control" name="txtCategory">
                        <option></option>
                        <c:forEach var="category" items="${ categories }">
                            <c:if test="${ category.name eq param.txtCategory }">
                                <option selected>${ category.name }</option>
                            </c:if>
                            <c:if test="${ category.name ne param.txtCategory }">
                                <option>${ category.name }</option>
                            </c:if>
                        </c:forEach>
                      </select>
                      <c:if test="${ not empty error.invalidCategory }">
                        <small class="form-text text-danger">${ error.invalidCategory }</small>
                      </c:if>
                    </div>
                      
                  </div>
                  <button type="submit" class="btn btn btn btn-outline-light">Search</button>
                </form>
            </div>
                      
        <c:set var="pageSize" value="${ requestScope.SEARCH_RESULT_PAGE_SIZE }" />
        <c:set var="maxIndex" value="${ requestScope.SEARCH_RESULT_MAX_INDEX }" />
        <c:set var="pageIndex" value="${ requestScope.SEARCH_RESULT_PAGE_INDEX }" />
        <c:set var="dtos" value="${ requestScope.SEARCH_RESULT }" />

        <c:if test="${ not empty pageSize}">
            <c:if test="${ empty dtos }">
            <div class="row mb-0 content-part">
                <div class="col col-8 p-5 m-auto bg-dark text-light rounded shadow">
                    <h3>No products are matched!</h3>
                </div>
            </div>   
            </c:if>
            <c:if test="${ not empty dtos }">
                
            <div class="row mt-5 ml-5 mr-5 mb-0 content-part">
                <div class="col pl-5 p-3 m-auto bg-dark text-light rounded shadow">
                    <h3>Product List</h3>
                </div>
            </div>            

            <div class="row ml-5 mr-5 pb-5">
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">#</th>
                      <th scope="col">Name</th>
                      <th scope="col">Description</th>
                      <th scope="col">Image</th>
                      <th scope="col">Create Date</th>
                      <th scope="col">Expiration Date</th>
                      <th scope="col">Quantity</th>
                      <th scope="col">Price</th>
                      <th scope="col">Category</th>
                      <c:if test="${ empty user or user.roleName eq roleUser }">
                        <th scope="col">Action</th>
                      </c:if>
                    </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="dto" items="${ dtos }" varStatus="counter">
                    <tr>
                      <th scope="row">${ counter.count + (pageIndex - 1) * pageSize }</th>
                      <td>${ dto.name }</td>
                      <td>${ dto.description }</td>
                      <td>
                          <c:if test="${ not empty dto.image }">
                            <img src="uploads/${ dto.image }" />
                          </c:if>
                      </td>
                      <td>${ dto.createDate }</td>
                      <td>${ dto.expirationDate }</td>
                      <td>${ dto.quantity }</td>
                      <td><fmt:formatNumber type="number" pattern="###,### đ" value="${ dto.price }" /></td>
                      <td>${ dto.categoryName }</td>
                      <c:if test="${ empty user or user.roleName eq roleUser }">
                        <td>
                          <c:url var="addToCartLink" value="addCart">
                              <c:param name="txtSearchValue" value="${ param.txtSearchValue }"></c:param>
                              <c:param name="txtMinRange" value="${ param.txtMinRange }"></c:param>
                              <c:param name="txtMaxRange" value="${ param.txtMaxRange }"></c:param>
                              <c:param name="txtCategory" value="${ param.txtCategory }"></c:param>
                              <c:param name="txtPageIndex" value="${ param.txtPageIndex }"></c:param>
                              <c:param name="txtProductId" value="${ dto.id }"></c:param>
                          </c:url>
                            <a href="${ addToCartLink }"><button class="btn btn-outline-dark">Add To Cart</button></a>
                        </td>
                      </c:if>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
                <c:set var="maxIndex" value="${ requestScope.SEARCH_RESULT_MAX_INDEX }" />
                <c:url var="pageUrl" value="shop">
                    <c:param name="txtSearchValue" value="${ param.txtSearchValue }"></c:param>
                    <c:param name="txtMinRange" value="${ param.txtMinRange }"></c:param>
                    <c:param name="txtMaxRange" value="${ param.txtMaxRange }"></c:param>
                    <c:param name="txtCategory" value="${ param.txtCategory }"></c:param>
                    <c:param name="txtPageIndex" value=""></c:param>
                </c:url>
                <nav aria-label="Page navigation" class="m-auto">
                    <ul class="pagination justify-content-center m-0">
                      <li class="page-item 
                        <c:if test="${ pageIndex eq 1 }">disabled</c:if>">
                        <a class="page-link" href="${ pageUrl }${ pageIndex - 1 }">Previous</a>
                      </li>
                      <li class="page-item active">
                          <a class="page-link" href="${ pageUrl }${ pageIndex }">${ pageIndex }</a>
                      </li>
                      <li class="page-item 
                        <c:if test="${ pageIndex eq maxIndex }">disabled</c:if>">
                        <a class="page-link" href="${ pageUrl }${ pageIndex + 1 }">Next</a>
                      </li>
                    </ul>
                </nav>
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

