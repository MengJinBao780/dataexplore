<%--
  Created by IntelliJ IDEA.
  User: sophie
  Date: 2017/11/7
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>采编发管理</title>
    <link href="${ctx}/resources/bundles/rateit/src/rateit.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css">
    <style>
        .sidebar .active{
            color: whitesmoke!important;
        }
        .sidebar .active a{
            color: whitesmoke!important;
        }
    </style>
</head>
<body>
<div class="row margin-bottom-40" style="min-height: 500px;margin-right: 15px;">
    <div class="sidebar col-md-2 col-sm-2">
        <ul class="list-group margin-bottom-25 sidebar-menu">
            <li class="list-group-item clearfix"><a href="${ctx}/tag"><i
                    class="fa fa-angle-right"></i>标签审核</a></li>
            <li class="list-group-item clearfix"><a href="${ctx}/collection/admin/category"><i
                    class="fa fa-angle-right"></i>栏目管理 </a></li>
            <li class="list-group-item clearfix active"><a href="${ctx}/collection/"><i
                    class="fa fa-angle-right"></i>内容采编 </a></li>
            <li class="list-group-item clearfix "><a href="${ctx}/admin/log/"><i
                    class="fa fa-angle-right"></i>日志列表 </a></li>
        </ul>
    </div>
    <div class="col-md-9 col-sm-8">
        <ul class="breadcrumb" style="padding: 8px 27px;">
            <li><a href="${ctx}/">首页</a></li>
            <li><a href="${ctx}/collection/">管理中心</a></li>
            <li >内容采编</li>
        </ul>
        <div class="page-content col-md-12" >
            <div class="col-md-6" style="box-sizing: border-box">
                <h3>采编内容列表</h3>
            </div>
            <div class="col-md-6 text-center "style="box-sizing: border-box">
                <button class="btn btn-primary toast-top-right pull-right" onclick="addData()"><i
                        class="fa fa-edit"></i>&nbsp;&nbsp;发布
                </button>
            </div>
            <div class="col-md-12">
                <ul id="ulData" class="nav nav-tabs">
                    <c:forEach items="${list}" var="item" varStatus="status">
                        <li><a data-toggle="tab" data="${item.ctName}"
                               onclick="showData('${item.ctName}');">${item.ctName}</a>
                        </li>
                    </c:forEach>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane fade in active" id="col_list">正在加载数据......</div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="collection">
    <table class="table table-striped table-bordered table-advance table-hover text-center">
        <thead>
        <tr>
            <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">序号</th>
            <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">标题</th>
            <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">类型</th>
            <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">作者</th>
            <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">时间</th>
            <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">操作</th>
        </tr>
        </thead>
        <tbody>

        {{each list}}
        <tr>
            <td style="text-align: center">{{(currentPage-1)*pageSize+$index+1}}</td>
            <td><a href="javascript:viewData('{{$value.id}}');">{{$value.title}}</a>
            </td>
            <td style="text-align: center">{{$value.ctName}}</td>
            <td style="text-align: center">{{$value.author}}</td>
            <td style="text-align: center">{{dateFormat($value.createTime)}}</td>
            <td id="{{$value.colId}}" style="text-align: center">

                    <a href="#" class="btn default btn-xs blue-madison"
                            onclick="viewData('{{$value.id}}')">
                    <i class="fa fa-view"></i> 查看 </a>
                &nbsp;&nbsp;

                <a href="#" class="btn default btn-xs blue-madison"
                   onclick="editData('{{$value.id}}')">
                    <i class="fa fa-edit"></i> 修改 </a>
                &nbsp;&nbsp;

                <a href="#" class="btn default btn-xs blue-madison"
                   onclick="deleteData('{{$value.id}}')">
                    <i class="fa fa-trash"></i> 删除 </a>
            </td>
        </tr>
        {{/each}}

        </tbody>
    </table>
    <div class="review-item clearfix">

            <div style="padding-top: 25px;float: left" >
                当前第<span style="color:blue;" id="currentPageNo"></span>页,共<span style="color:blue;"
                                                                                id="totalPages"></span>页,<span
                    style="color:blue;" id="totalCount"></span>条数据
            </div>
            <div style="float: right">
                <div id="pagination" style="float: right"></div>
            </div>

    </div>
</script>


</body>

<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/bootstrapv3.3/js/bootstrap.js"></script>
    <script src="${ctx}/resources/bundles/artTemplate/template.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.js"></script>
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/js/regex.js"></script>
    <script src="${ctx}/resources/js/subStrLength.js"></script>
    <script type="text/javascript">

        var ctx = '${ctx}';
        template.helper("dateFormat", convertMilsToDateString);
        template.helper("dateTimeFormat", convertMilsToDateTimeMMString);
        template.helper("getSubStr", getSubStr);
        var colType = "新闻";
        $().ready(function () {
            $("#ulData li a").click(function () {
                $(this).parent().parent().find("li").removeClass("active");
                $(this).parent().addClass("active");
                $("#liTitie").html($(this).text() + "列表");
            });

            var type = '${colType}';
            if (type != null && type != '') {
                $("#ulData li a").each(function () {
                    if ($(this).text() == type) {
                        $(this).click();
                    }
                });
            }
            else {
                $("#ulData li:first a").click();
            }
        });
        function showData(type) {
            newsNoticeList(1, type);
        }

        function colNews() {
            colType = "新闻";
            newsNoticeList(1);
        }

        function colNotice() {
            colType = "公告";
            newsNoticeList(1);
        }

        function newsNoticeList(pageNo, type) {
            colType = type;
            $.ajax({
                url: "${ctx}/collection/list",
                type: "get",
                data: {
                    colType: colType,
                    pageSize: 10,
                    pageNo: pageNo
                },
                dataType: "json",
                success: function (data) {
                    if (data == null) {
                        $("#col_list").empty();
                        $("#col_list").append("暂无数据");
                        return;
                    }
                    getData(data);
                },
                error: function (err) {
                    $("#col_list").empty();
                    $("#col_list").append("数据加载失败,请稍后重试");
                }
            });
        }


        function getData(data) {
            var html = template("collection", data);
            $("#col_list").empty();
            $("#col_list").append(html);
//            if (data.content.length != 0) {
//                $("#col_list").append(html);
//            } else {
//                $("#col_list").append("暂无数据");
//            }
            $("#currentPageNo").html(data.currentPage);
            $("#totalPages").html(data.totalPages);
            $("#totalCount").html(data.totalCount);
            if ($("#pagination .bootpag").length != 0) {
                $("#pagination").off();
                $('#pagination').empty();
            }
            $('#pagination').bootpag({
                total: data.totalPages,
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
                newsNoticeList(num);
            });
        }

        function addData() {
            window.location.href = "${ctx}/collection/admin/colId?operate=add";
        }

        function viewData(id) {
            window.location.href = "${ctx}/collection/view/" + id;
        }

        function editData(id) {
            window.location.href = "${ctx}/collection/admin/colId?operate=edit&colId=" + id;
        }


        function deleteData(id) {

            bootbox.confirm("确定要删除此条记录吗？", function (r) {
                if (r) {
                    $.ajax({
                        url: ctx + "/collection/delete/" + id,
                        type: "get",
                        dataType: "json",
                        success: function (data) {

                            if (data == null) {
                                toastr['success']('删除失败，请稍后重试!', '提示信息');
                                return;
                            }
                            if (data) {
//                                alert("删除成功");
                                toastr['success']('删除成功!', '提示信息');
                                getData(1);
                                newsNoticeList(1);
                            }
                        },
                        error: function () {
                            toastr["fail"]("删除失败！", "数据删除");
                        }
                    });
                }
            });
        }
        function convertMilsToDateString(mil) {
            var date = new Date(mil);
            return date.Format("yyyy-MM-dd");
        }

        function convertMilsToDateTimeMMString(mil) {
            var date = new Date(mil);
            return date.Format("yyyy-MM-dd hh:mm");
        }
    </script>
</div>

</html>