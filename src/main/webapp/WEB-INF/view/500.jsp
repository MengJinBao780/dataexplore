<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/4/22
  Time: 13:44
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>服务器出错了！</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/metronic/css/error.css">
</head>
<body>
<div class="page-content">
    <h3 class="page-title">
        服务器错误
    </h3>
    <div class="page-bar">
        <ul class="page-breadcrumb">
            <li>
                <i class="fa fa-home"></i>
                <a href="${ctx}/">首页</a>
                <i class="fa fa-angle-right"></i>
            </li>
            <li>
                <a href="#">服务器错误</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 page-500">
            <div class=" number">
                500
            </div>
            <div class=" details">
                <h3>服务器错误！.</h3>
                <p>
                    我们正在尝试修复!<br/>
                    请稍后再试.<br/><br/>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
