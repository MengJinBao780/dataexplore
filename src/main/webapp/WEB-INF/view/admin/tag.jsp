<%--
  Created by IntelliJ IDEA.
  User: sophie
  Date: 2018/5/2
  Time: 16:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>内容采编</title>
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/pub.css">
    <style>
        input{
            box-sizing: border-box;
        }
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
            <li class="list-group-item clearfix active"><a href="${ctx}/tag"><i
                    class="fa fa-angle-right "></i>标签审核</a></li>
            <li class="list-group-item clearfix "><a href="${ctx}/collection/admin/category"><i
                    class="fa fa-angle-right "></i>栏目管理 </a></li>
            <li class="list-group-item clearfix"><a href="${ctx}/collection/"><i
                    class="fa fa-angle-right"></i>内容采编 </a></li>
            <li class="list-group-item clearfix "><a href="${ctx}/admin/log/"><i
                    class="fa fa-angle-right"></i>日志列表 </a></li>
        </ul>
    </div>
    <div class="col-md-9 col-sm-8">
        <ul class="breadcrumb" style="padding: 8px 27px;">
            <li><a href="${ctx}/">首页</a></li>
            <li><a href="${ctx}/collection/">管理中心</a></li>
            <li >标签审核</li>
        </ul>
        <div class="page-content col-md-12">
            <div class="col-md-6" style="margin-bottom: 17px">
                <h3>标签内容列表</h3>
            </div>

            <div class="col-md-12">
                <ul id="ulData" class="nav nav-tabs">
                    <li id ="li1" class="active"><a data-toggle="tab" onclick="showData(0,null,'all',0)">待审核标签</a></li>
                    <li id ="li2"><a data-toggle="tab" onclick="showData(1,null,'all',2)">已审核标签</a></li>
                </ul>
                <div>
                    <div class="form-inline">
                        <label>筛选条件：</label>
                        <div class="form-group">
                            <label class="sr-only">数据集</label>
                            <input class="form-control" id="sdo" placeholder="数据集" >
                        </div>


                        <select id="product" class="form-control">
                                <option value="all">所有产品名称</option>
                            <c:forEach items="${list}" var="item" varStatus="status">
                                <option value="${item.id}">${item.prodName}</option>
                            </c:forEach>
                        </select>


                        <select id="status" class="form-control">
                            <option value="2">所有审核状态</option>
                            <option value="1">已通过审核</option>
                            <option value="-1">被拒绝</option>
                        </select>

                        <%--<a class="btn btn-primary" onclick="search()" role="button">筛选</a>--%>
                        <button onclick="search()" class="btn btn-primary">筛选</button>
                    </div>
                </div>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane fade in active" id="col_list">正在加载数据......</div>
                </div>
            </div>

        </div>
        <div class="row"></div>

        <script type="text/html" id="resourceTmpl">

            <table class="table table-striped table-bordered table-advance table-hover text-center">
                <thead>
                <tr>
                    <th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">序号</th>
                    <th style="width:10%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">标签名称
                    </th>
                    <th style="width:40%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">数据集
                    </th>
                    <th style="width:10%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">
                        产品
                    </th>
                    <th style="width:10%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">标签用户
                    </th>
                    <th style="width:10%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">审核人
                    </th>
                    <th style="width:10%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">审核时间
                    </th>
                    <th style="width:40%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">审核</th>
                </tr>
                </thead>
                <tbody>
                {{each list as value i}}
                <tr>

                    <td>{{i + 1}}{{a}}</td>
                    <td>{{value.tagName}}</td>

                        <td id="sdoTitle"><a href="sdo/detail/{{value.sdoId}}">{{value.sdoTitle}}</a></td>

                    <td id="prodName">{{value.prodName}}</td>
                    <td>{{value.loginId}}</td>
                    <td id="auditor">{{value.auditor}}</td>
                    <td>{{dateFormat(value.auditDate)}}</td>

                    <td id={{value.id}}>
                        <div class="">
                            {{if value.status == 0}}
                            <div>
                                <a href="#" class="btn default btn-xs blue-madison"
                                   onclick="approveTag('{{value.id}}')">
                                    <i class="fa fa-edit"></i> 通过 </a>
                                <a href="#" class="btn default btn-xs " onclick="rejectTag('{{value.id}}')">
                                    <i class=" fa fa-trash-o"></i> 拒绝 </a>
                            </div>
                            {{else if value.status == 1}}
                            <div>已通过审核</div>
                            {{else if value.status == -1}}
                            <div>被拒绝</div>
                            {{/if}}

                        </div>
                    </td>
                </tr>
                {{/each}}
                </tbody>
            </table>
            <div class="review-item clearfix">

                    <div style="padding-top: 25px; float: left"  >
                        当前第<span style="color:blue;" id="currentPageNo"></span>页,共<span style="color:blue;"
                                                                                        id="totalPages"></span>页,<span
                            style="color:blue;" id="totalCount"></span>条数据
                    </div>
                    <div  style="float: right">
                        <div id="pagination" ></div>
                    </div>

            </div>
        </script>

    </div>
</div>
</body>
<div id="siteMeshJavaScript">
    <![endif]-->
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.js"></script>
    <script src="${ctx}/resources/bundles/artTemplate/template.js"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/js/regex.js"></script>
    <script src="${ctx}/resources/js/subStrLength.js"></script>
    <script src="${ctx}/resources/js/jquery.validate.min.js"></script>
    <script src="${ctx}/resources/js/category.js"></script>

    <script type="text/javascript">
        var ctx = '${ctx}';


        template.helper("dateFormat", convertMilsToDateString);
        template.helper("dateTimeFormat", convertMilsToDateTimeMMString);
        template.helper("getSubStr", getSubStr);
        //        template.helper("getProName", getProName);
        $().ready(function () {
            $("#ulData li a").click(function () {
                $(this).parent().parent().find("li").removeClass("active");
                $(this).parent().addClass("active");
            });
            showData(0,null,'all',0);//加载数据
        });
        function search() {
            var sdo = $("#sdo").val();
            var product = $("#product").val();
            var status = $("#status").val();
            if($("#status").is(":hidden"))
            {
                status = 0;
                showData(0,sdo,product,status);
            }
            else
            {showData(1,sdo,product,status);}


        }

        function showData(type,sdo,product,status) {
            if(type==0){
                $("#status").hide();
            }
           else
            {$("#status").show();}
            getTagList(1, type,sdo,product,status);
        }
        function getTagList(pageNo, type,sdo,product,status) {
            $.ajax({
                url: "${ctx}/admin/getTagList",
                type: "get",
                dataType: "json",
                data: {
                    pageSize: 10,
                    pageNo: pageNo,
                    type: type,
                    sdoTitle: sdo,
                    prodId: product,
                    status: status
                },
                success: function (data) {
                    if (data == null) {
                        $("#col_list").empty();
                        $("#col_list").append("暂无数据");
                        return;
                    }
                    getData(data,type,sdo,product,status);
                }
                ,
                error: function (err) {
                    $("#col_list").empty();
                    $("#col_list").append("数据加载失败,请稍后重试");
                }
            })
            ;
        }


        function getData(data, type,sdo,product,status) {
            var html = template("resourceTmpl", data);
            $("#col_list").empty();
            if (data.length != 0) {
                $("#col_list").append(html);
            } else {
                $("#col_list").append("暂无数据");
            }

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
                getTagList(num, type,sdo,product,status);
            });
        }

        function approveTag(id) {
            bootbox.confirm("确定要通过该标签？", function (r) {
                if (r) {
                    $.ajax({
                        url: "${ctx}/tag/approveTag",
                        type: "get",
                        data: {
                            id: id
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data == null) {
                                toastr['success']('失败，请稍后重试!', '提示信息');
                                return;
                            }
                            if (data) {
                                toastr['success']('成功通过!', '提示信息');
//                                alert($("#id"));
//                                $("#id").html("已通过审核");
//                                $("#auditor").html(auditor);
//                                getTagList(1);
                                showData(0,null,'all',0)
//                                getProName(id);
                            }
                        },
                        error: function (err) {
                            toastr['success']('失败，请稍后重试!', '提示信息');
                        }
                    });
                }
            });
        }
        function rejectTag(id) {
            bootbox.confirm("确定要拒绝该标签？", function (r) {
                if (r) {
                    $.ajax({
                        url: "${ctx}/tag/rejectTag",
                        type: "get",
                        data: {
                            id: id
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data == null) {
                                toastr['success']('失败，请稍后重试!', '提示信息');
                                return;
                            }
                            if (data) {
                                toastr['success']('成功拒绝!', '提示信息');
//                                $("#status").html("已拒绝");
//                                getTagList(1);
                                showData(0,null,'all',0)
                            }
                        },
                        error: function (err) {
                            toastr['success']('失败，请稍后重试!', '提示信息');
                        }
                    });
                }
            });
        }

        function getProName(id) {
            $.ajax({
                url: "http://localhost:8080/tag/getProName",
                type: "get",
                data: {
                    id: id
                },
                dataType: "json",
                success: function (data) {
                    var proName = data.proName;
                    alert(proName);
                    return proName;

                },
                error: function () {
                    alert("error")
                }

            });
        }

        function convertMilsToDateTimeMMString(mil) {
            var date = new Date(mil);
            return date.Format("yyyy-MM-dd hh:mm");
        }
        function convertMilsToDateString(mil) {
            var date = new Date(mil);
            return date.Format("yyyy-MM-dd");
        }

    </script>
</div>