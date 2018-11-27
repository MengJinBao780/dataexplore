<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/4/12
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set value="${pageContext.session.getAttribute('loginId')}" var="loginId"/>
<%@ page import="cn.csdb.utils.CasURLCode" %>
<%--<%@ page import="cn.csdb.commons.utils.*" %>--%>

<html>
<head>
    <title>CASEARTH广目</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <link href="${ctx}/resources/bundles/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrapv3.3/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <%--<link rel="stylesheet" href="${ctx}/resources/css/headAndFoot.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctx}/resources/css/public.css" rel="stylesheet" type="text/css">--%>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctx}/resources/css/main.css" rel="stylesheet" type="text/css">
    <!--BEGIN PAGE STYLES-->
    <sitemesh:write property="head"/>
    <%-- <link href="${ctx}/resources/css/customer.css" rel="stylesheet" type="text/css"/>--%>
    <link rel="shortcut icon" href="${ctx}/resources/img/favicon.ico"/>
</head>
<body>


    <!-- BEGIN HEADER -->

    <%--<div class="main-wrap">
        <div class="main-header">
            <a href="javascript:;" class="wrap-img">
                <img src="${ctx}/resources/img/index/u210.png">
            </a>
            <a href="javascript:;">
                <span class="header-des">CASEARTH广目</span>
            </a>
            &lt;%&ndash;<div class="main-header-switch1 "><span >English</span></div>
            <div class="main-header-switch2 "><span >中文</span></div>&ndash;%&gt;
        </div>
        <div class="main-nav">
            <ul class="main-nav-ul">
                <li><span><a style="color: darkblue;font-size: 18px;" href="${ctx}/">首页</a></span></li>
                <li><span><a style="color: darkblue;font-size: 18px;" href="${ctx}/sdo/list">搜索数据集</a></span></li>
                <li><span><a style="color: darkblue;font-size: 18px;" href="${ctx}/mapSearch/">地图搜索</a></span></li>
            </ul>
            <div class="login">
                <div>
                    <shiro:guest>
                        <a style="color: darkblue;" href="${applicationScope.systemPro['cas.url.prefix']}/login?service=${applicationScope.systemPro['dataexplore.url']}/shiro-cas" id="sign">登录</a> &nbsp;&nbsp;
                    </shiro:guest>
                    <shiro:user>

                        <a target='_blank' style="color: darkblue;"
                           href="${applicationScope.systemPro['cas.url.prefix']}/reg01004Action.do?userID=<%=CasURLCode.encode(session.getAttribute("loginId").toString())%>">
                                ${sessionScope.loginId}</a></span>


                        <a style="color: darkblue;" href="${ctx}/user">个人中心</a>&nbsp;&nbsp;
                        <shiro:hasRole name="admin">
                            <a style="color: darkblue;" href="${ctx}/tag">管理员入口</a>&nbsp;&nbsp;
                        </shiro:hasRole>
                        <a style="color: darkblue;" href="${ctx}/logout">退出</a>&nbsp;&nbsp;
                    </shiro:user>
                </div>
            </div>
        </div>
    </div>--%>
    <header>
        <div class="header-wrap">
            <div class="header-left">
                <img class="logo" src="${ctx}/resources/img/logo.png">
                <img class="logo-tit" src="${ctx}/resources/img/logo-tit.png">
            </div>
            <%--<div class="header-right">
                <a href="#">中文版</a>
                <span>|</span>
                <a href="#">ENGLISH</a>
            </div>--%>
        </div>
    </header>
    <nav>
        <div class="nav-wrap">
            <div class="nav-left">
                <ul id="nav-title">
                    <li><a href="${ctx}/" id="first">首页</a></li>
                    <li><a href="${ctx}/sdo/list" id="second">搜索数据集</a></li>
                    <li><a href="${ctx}/mapSearch/" id="third">地图搜索</a></li>
                </ul>
            </div>
            <div class="nav-right">
                <%--<label class="user-mess">用户：</label>
                <span class="user-mess">Roosea</span>
                <button class="logout-btn">退出</button>--%>
                <div>
                    <shiro:guest>
                        <a href="${applicationScope.systemPro['cas.url.prefix']}/login?service=${applicationScope.systemPro['dataexplore.url']}/shiro-cas" id="sign">登录</a> &nbsp;&nbsp;
                    </shiro:guest>
                    <shiro:user>

                        <a target='_blank'
                           href="${applicationScope.systemPro['cas.url.prefix']}/reg01004Action.do?userID=<%=CasURLCode.encode(session.getAttribute("loginId").toString())%>">
                                ${sessionScope.loginId}</a></span>


                        <a href="${ctx}/user">个人中心</a>&nbsp;&nbsp;
                        <shiro:hasRole name="admin">
                            <a href="${ctx}/tag">管理员入口</a>&nbsp;&nbsp;
                        </shiro:hasRole>
                        <a href="${ctx}/logout">退出</a>&nbsp;&nbsp;
                    </shiro:user>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </nav>
    <!-- END HEADER -->
    <div class="clearfix"></div>

    <%-- <div class="container neirong_div" >--%>
    <div id="main-content" style="min-height: 500px">
        <sitemesh:write property="body"/>
    </div>

    <%-- </div>--%>

    <!-- BEGIN FOOTER -->
    <%--<div class="foot_div" >--%>
        <%--<div class="main-footer">

        <div>
            地球大数据-示范平台© Company 2018 This line of text is meant to be treated as fine print.
        </div>
        <div>
            版权所有：中国科学院计算机网络信息中心 备案序号：京ICP备09112257-61
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
            &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 电话：010-58812534&nbsp;
            &nbsp; 邮箱：data@cnic.cn&nbsp;
        </div>
    </div>--%>
    <div class="clear"></div>
    <footer>
        <p>地球大数据-示范平台 © Company 2018 This line of text is meant to be treated as fine print.</p>
        <p>版权所有：中国科学院计算机网络信息中心 备案序号：京ICP备09112257-61 电话：010-58812534 邮箱：data@cnic.cn </p>
    </footer>
    <%--</div>--%>

<script src="${ctx}/resources/bundles/jquery/jquery1.12.4.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/bundles/jquery/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="${ctx}/resources/bundles/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/bundles/bootstrapv3.3/js/bootstrap.min.js" type="text/javascript"></script>

<script src="${ctx}/resources/js/regex.js"></script>
<script src="${ctx}/resources/js/subStrLength.js"></script>
<script src="${ctx}/resources/bundles/artTemplate/template.js"></script>
<script src="${ctx}/resources/js/echarts.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>

    ctx = '${ctx}';
    loginId='${loginId}';
    /*var  pageHei = document.body.clientHeight;
    $("#main-content").css("min-height",(pageHei - 290) +"px");
    $("#main-content").css("overflow","hidden");*/
    template.defaults.escape = false;
    template.helper("dateFormat", convertMilsToDateString);
    template.helper("dateTimeFormat", convertMilsToDateTimeString);
    template.helper("getSubStr", getSubStr);
    if(sessionStorage.getItem("keyWords") == null){
        $.getJSON("${ctx}/resources/data/CensorWords.json",function (data) {
            sessionStorage.setItem('keyWords', JSON.stringify(data));
        })
    }
    function convertMilsToDateTimeString(mil) {
        var date = new Date(mil);
        return date.Format("yyyy-MM-dd hh:mm:ss");
    }
    function convertMilsToDateString(mil) {
        var date = new Date(mil);
        return date.Format("yyyy-MM-dd");
    }
    function convertMilsToDateTimeYYString(mil) {
        var date = new Date(mil);
        return date.Format("yyyy-MM-dd");
    }
    $("#logout").click(
        function () {
            window.location.href = "${ctx}/logout";
        }
    )



</script>
<!-- END JAVASCRIPTS -->
<sitemesh:write property="div.siteMeshJavaScript"/>
</body>
</html>