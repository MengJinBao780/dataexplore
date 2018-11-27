<%--
  Created by IntelliJ IDEA.
  User: sophie
  Date: 2017/11/9
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>新闻预览</title>
    <link href="${ctx}/resources/bundles/rateit/src/rateit.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css">
    <style type="text/css">
        li{ display:inline;}
        .mid-div{
            margin: 0 auto;
        }
        h3{
            font-size: 24px;
        }
    </style>
</head>
<body>

<div style="width: 980px" class="mid-div">
     <h3  style="text-align: center;padding-top: 10px">${collection.title}</h3>

     <ul class="blog-info col-md-12 col-sm-12 text-center" style="margin: 10px 0">

                            <li class=" text-center "><i class="fa fa-comments"></i> 作者：${collection.author}</li>

                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                            <li class=" text-center"><i class="fa fa-calendar"></i>时间：<fmt:formatDate value="${collection.createTime}" type="time" pattern="yyyy-MM-dd HH:mm"/></li>
                        </ul>

                        <p>${collection.content}</p>

</div>
</body>
</html>
