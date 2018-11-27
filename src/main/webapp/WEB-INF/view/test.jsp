<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2018/03/20
  Time: 15:28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>test页面</title>
</head>
<body>
<div class="page-content">

    <div>
        <spring:message code="test_welcome" />
        <br/>
        <spring:message code="test_username" />
        <br/>
        <spring:message code="test_password" />
        <br/><br/><br/>
        ${welcome}
    </div>
</div>
</body>
<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script>
        $(function(){

        });

    </script>
</div>

</html>
