<%--
  Created by IntelliJ IDEA.
  User: hyj13
  Date: 2018/6/30 0030
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-xs-6 col-xs-offset-3">
            <form class="form-horizontal" action="<c:url value="/register.html"/>" method="post">
                <div class="form-group">
                    <c:if test="${!empty errorMsg}">
                        <p style="color: red">${errorMsg}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="userName" class="col-xs-4 control-label">UserName</label>
                    <div class="col-xs-8">
                        <input type="text" class="form-control" id="userName"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-xs-4 control-label">password</label>
                    <div class="col-xs-8">
                        <input type="text" class="form-control" id="password" />
                    </div>
                </div>
                <button type="button">register</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
