<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/4/22
  Time: 13:50
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>NOT FOUND</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/metronic/css/error.css">
</head>
<body>
<div class="page-content">
    <h3 class="page-title">
        404 NOT FOUND
    </h3>
    <div class="page-bar">
        <ul class="page-breadcrumb">
            <li>
                <i class="fa fa-home"></i>
                <a href="${ctx}/">首页</a>
                <i class="fa fa-angle-right"></i>
            </li>
            <li>
                <a href="#">404</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 page-404">
            <div class="number">
                404
            </div>
            <div class="details">
                <h3>您访问的资源不存在.</h3>
                <p>
                    您要访问的资源或页面可能已经被删除或不存在。<br/>
                    <a href="${ctx}/">
                        返回首页 </a>
                </p>
            </div>
        </div>
    </div>
</div>

</body>
</html>
