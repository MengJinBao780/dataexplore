<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>个人中心</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <link href="${ctx}/resources/bundles/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/metronic/css/layout.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/css/main.css" rel="stylesheet" type="text/css"/>
    <%--<link href="${ctx}/resources/css/newSdoList.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctx}/resources/bundles/metronic/css/themes/light.css" rel="stylesheet" type="text/css"
          id="style_color"/>
    <link rel="shortcut icon" href="${ctx}/resources/img/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css">

</head>
<body>
<div class="container">
    <div class="nav-index">
        <div class="nav-one col-md-12">
            <div style="position:absolute;right:50px;top:0;">
                <span>个人中心 </span>
                <span> > </span>
                <span> 我的收藏</span>
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <div>
        <div class="col-md-2">
            <div class="sider-bar">
                <h3>个人中心</h3>
                <ul>
                    <%--<li><a href="user.html">个人信息</a></li>--%>
                    <li><a href="javascript:void(0);" id="myFavorite" class="on">我的收藏</a><img
                            src="${ctx}/resources/img/current.png" id="currentArrow"></li>
                    <li><a href="javascript:void(0);" id="myDownload">我的下载</a></li>
                </ul>
            </div>
        </div>

        <div class="collection-main col-md-9">
            <div class="collection-one col-md-12" id="userPortlet">
            </div>
        </div>
    </div>
</div>
<%--个人信息模板--%>
<script type="text/html" id="userInfoTemp">
</script>

<%--我的收藏模板--%>
<script type="text/html" id="myFavoriteTemp">
    {{each favoriteList as value s}}
    <div class="collection-one col-md-12">
        <img class="col-md-3" src="${ctx}{{value.iconPath}}" alt="">
        <div class="collection-right col-md-8">
            <h3 class="col-md-11" style="display:inline;"><a
                    href="${ctx}/sdo/detail/{{value.sdoId}}">{{value.resName}}</a></h3>
            <%--<a href="${ctx}/sdo/detail/{{value.sdoId}}" target="_blank"
               title="查看"><i class="glyphicon glyphicon-eye-open"></i></a>--%>
            &nbsp;&nbsp;<a title="取消收藏" onclick="deleteFavor('{{value.id}}','{{currentPage}}')"><i
                class="glyphicon glyphicon-remove"></i></a>
            <div class="collection-con col-md-12">
                {{value.desc}}
            </div>
            <div class="collection-mess col-md-12">
                <span>{{value.publisher.name}}</span>
                <span>文件量：{{value.toFilesNumber}}</span>
                <span>下载：{{value.downloadCount}}</span>
                <span style="background: #dfb100;color:#ffffff;float:right;font-size:16px;padding-left:8px;padding-right:8px;padding-top:2px;padding-bottom:2px">{{value.dataFormat}}</span>

            </div>

        </div>
    </div>
    {{/each}}

    <div id="myFavoritePagination" style="background:#fff;">
        <div class="row">
            <div style="padding-top: 25px;" class="col-md-5 items-info">&nbsp;&nbsp;&nbsp;&nbsp;当前第<span
                    id="currentPageNo">{{currentPage}}</span>页,共<span
                    id="totalPages">{{totalPage}}</span>页,<span id="totalCount">{{totalNum}}</span>条数据
            </div>
            <div class="col-md-7 ">
                <div id="pagination" style="float: right"></div>
            </div>
        </div>
    </div>

</script>

<%--我的下载模板--%>
<script type="text/html" id="myDownloadTemp">
    {{each downloadList as value s}}
    <div class="collection-one col-md-12">
        <img class="col-md-3" src="${ctx}{{value.iconPath}}" alt="">
        <div class="collection-right col-md-8">
            <h3 class="col-md-11" style="display:inline;"><a
                    href="${ctx}/sdo/detail/{{value.sdoId}}">{{value.resName}}</a></h3>
            <div class="collection-con col-md-12">
                {{value.desc}}
            </div>
            <div class="collection-mess col-md-12">
                <span>{{value.publisher.name}}</span>
                <span>文件量：{{value.toFilesNumber}}</span>
                <span>下载：{{value.downloadCount}}</span>
                <span style="background: #dfb100;color:#ffffff;float:right;font-size:16px;padding-left:8px;padding-right:8px;padding-top:2px;padding-bottom:2px">{{value.dataFormat}}</span>
            </div>
        </div>
    </div>
    {{/each}}

    <div id="myDownloadPagination" style="background:#fff;">
        <div class="row">
            <div style="padding-top: 28px;" class="col-md-5  items-info">&nbsp;&nbsp;&nbsp;&nbsp;当前第<span
                    id="currentPageNoA">{{currentPage}}</span>页,共<span
                    id="totalPagesA">{{totalPage}}</span>页,<span id="totalCountA">{{totalNum}}</span>条数据
            </div>
            <div class="col-md-7 ">
                <div id="pagination_download" style="float: right"></div>
            </div>
        </div>
    </div>

</script>


</body>

<div id="siteMeshJavaScript">

    <script src="${ctx}/resources/bundles/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"
            type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/jquery/jquery.blockui.min.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/jquery/jquery.cokie.min.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/uniform/jquery.uniform.min.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
    <!-- END CORE PLUGINS -->

    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script src="${ctx}/resources/bundles/metronic/global/scripts/metronic.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/metronic/scripts/layout.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <script src="${res}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/js/regex.js"></script>
    <%--<script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.js"></script>--%>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        var ctx = '${ctx}';
        template.helper("dateFormat", convertMilsToDateString);

        $(function () {
            bootbox.setLocale("zh_CN");
            var path = window.location.pathname;
            if (path.indexOf('?') > -1)
                path = path.substring(0, path.indexOf('?'));
            $("ul.page-sidebar-menu a").each(function () {
                var href = $(this).attr("href");
                if (href.indexOf('?') > -1)
                    href = href.substring(0, href.indexOf('?'));
                if (href === path) {
                    $(this).parent().addClass("active");
                    if ($(this).parent().parent().hasClass("sub-menu")) {
                        $(this).parent().parent().parent().children("a").trigger("click");
                        $(this).parent().parent().parent().children("a").append('<span class="selected"></span>');
                        $(this).parent().parent().parent().addClass("active");
                    } else {
                        $(this).parent().children("a").append('<span class="selected"></span>');
                    }
                }
            });
            $("#myFavorite").click();
        });

        $(".nav_text .nav_text02 a").click(function () {
            $(this).parent().parent().find(".nav_text02").css("background", "");
            $(this).parent().css("background", "rgba(244,244,244,0.5)");
        });
        function convertMilsToDateString(mil) {
            var date = new Date(mil);
            return date.Format("yyyy-MM-dd hh:mm:ss");
        }

        //个人信息
        $("#userInfo").click(function () {
            $("#barName1").html("个人信息");
            $("#barName2").html("个人信息");
            $(this).parent().addClass("active");
            $("#myFavorite").parent().removeClass("active");
            $("#myDownload").parent().removeClass("active");
        });

        //我的收藏
        $("#myFavorite").click(function () {
            getMyFavoriteList(1);
            $("#barName1").html("我的收藏");
            $("#barName2").html("我的收藏");
            $(this).parent().addClass("active");
            $("#userInfo").parent().removeClass("active");
            $("#myBrowse").parent().removeClass("active");
            $(this).attr("class", "on");
            $("#myDownload").attr("class", "");
            $("#currentArrow").remove();
            $(this).after('<img src="${ctx}/resources/img/current.png" id="currentArrow"></li>');
        });

        function getMyFavoriteList(pageNo) {
            $.ajax({
                url: "${ctx}/user/myFavorite",
                type: 'post',
                data: {pageNo: pageNo},
                dataType: 'json',
                success: function (data) {
                    if (data.result == "success") {
                        var html = template("myFavoriteTemp", data);
                        $("#userPortlet").empty();
                        $("#userPortlet").append(html);
                        if ($("#pagination .bootpag").length != 0) {
                            $("#pagination").off();
                            $('#pagination').empty();
//                            $("#currentPageNo").html(data.currentPage);
//                            $("#totalPages").html(data.totalPage);
//                            $("#totalCount").html(data.totalNum);
                        }
                        $('#pagination').bootpag({
                            total: data.totalPage,
                            page: data.currentPage,
                            maxVisible: 5,
                            leaps: true,
                            firstLastUse: true,
                            first: '首页',
                            last: '尾页',
                            wrapClass: 'pagination',
                            activeClass: 'active',
                            disabledClass: 'disabled',
                            nextClass: 'next',
                            prevClass: 'prev',
                            lastClass: 'last',
                            firstClass: 'first'
                        }).on('page', function (event, num) {
                            getMyFavoriteList(num);
                        });
                    }
                }
            });
        }

        //我的下载
        $("#myDownload").click(function () {
            getMyDownloadList(1);
            $("#barName1").html("我的下载");
            $("#barName2").html("我的下载");
            $(this).parent().addClass("active");
            $("#userInfo").parent().removeClass("active");
            $("#myDownload").parent().removeClass("active");
            $(this).attr("class", "on");
            $("#myFavorite").attr("class", "");
            $("#currentArrow").remove();
            $(this).after('<img src="${ctx}/resources/img/current.png" id="currentArrow"></li>');
        });

        function getMyDownloadList(pageNo) {
            $.ajax({
                url: "${ctx}/user/myDownload",
                type: 'post',
                data: {pageNo: pageNo},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    if (data.result == "success") {
                        var html = template("myDownloadTemp", data);
                        $("#userPortlet").empty();
                        $("#userPortlet").append(html);
                        if ($("#pagination .bootpag").length != 0) {
                            $("#pagination").off();
                            $('#pagination').empty();
                            //$("#currentPageNo").html(data.currentPage);
                            //$("#totalPages").html(data.totalPage);
                            // $("#totalCount").html(data.totalNum);
                        }
                        $('#pagination_download').bootpag({
                            total: data.totalPage,
                            page: data.currentPage,
                            maxVisible: 5,
                            leaps: true,
                            firstLastUse: true,
                            first: '首页',
                            last: '尾页',
                            wrapClass: 'pagination',
                            activeClass: 'active',
                            disabledClass: 'disabled',
                            nextClass: 'next',
                            prevClass: 'prev',
                            lastClass: 'last',
                            firstClass: 'first'
                        }).on('page', function (event, num) {
                            getMyDownloadList(num);
                        });
                    }
                }
            });
        }


        function deleteFavor(id, currentPage) {
            bootbox.confirm("确定要删除此收藏？", function (r) {
                if (r) {
                    $.ajax({
                        url: "${ctx}/sdo/deleteFavorite",
                        type: "get",
                        dataType: "json",
                        data: {id: id},
                        success: function (data) {
                            console.log(data);
                            if (data == true) {
                                $("#favorite").html('<i class="glyphicon glyphicon-star-empty"></i>收藏');
                                toastr["success"]("取消收藏成功!");
                                getMyFavoriteList(currentPage);
                            } else {
                                toastr["error"]("取消收藏失败！");
                            }
                        }
                    });
                }
            });
        }
    </script>
</div>
</html>
