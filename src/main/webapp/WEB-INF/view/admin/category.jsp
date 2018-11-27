<%--
  Created by IntelliJ IDEA.
  User: sophie
  Date: 2017/11/20
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <li class="list-group-item clearfix"><a href="${ctx}/tag"><i
                    class="fa fa-angle-right"></i>标签审核</a></li>
            <li class="list-group-item clearfix active">
                <a href="${ctx}/collection/admin/category">
                    <i class="fa fa-angle-right active"></i>
                    栏目管理
                </a>
            </li>
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
            <li >栏目管理</li>
        </ul>
        <div class="page-content col-md-12">
            <div class="col-md-6" style="box-sizing: border-box">
                <h3>栏目内容列表</h3>
            </div>
            <div class="col-md-6 text-center " style="box-sizing: border-box">
                <button type="button" onclick="showForm();" class="btn btn-primary btn-sm pull-right">增加栏目
                </button>
            </div>
            <div class="col-md-12">
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
                    <%--<th style="background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">序号</th>--%>
                    <th style="width:40%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">栏目名称
                    </th>
                    <th style="width:20%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">栏目顺序
                    </th>
                    <th style="width:40%; background: #a6c8e6;color: #FFF;font-weight: bold" class="text-center">操作</th>
                </tr>
                </thead>
                <tbody>
                {{each list as value i}}
                <tr>
                    <%--<td>{{i + 1}}</td>--%>
                    <td>{{value.ctName}}</td>
                    <td>{{value.order}}</td>
                    <td>
                        <a href="#" onclick="editCategory('{{value.id}}')"
                           class="btn default btn-xs blue-madison">
                            <i class="fa fa-edit"></i> 编辑 </a>
                        <a href="#" class="btn default btn-xs green" onclick="deleteCategory('{{value.id}}')">
                            <i class=" fa fa-trash-o"></i> 删除 </a>
                    </td>
                </tr>
                {{/each}}
                </tbody>
            </table>
        </script>


        <div id="addInfoModal" class="modal fade" tabindex="-1" data-width="500">
            <div class="modal-dialog">
                <form class="form-horizontal" role="form" id="addInfoForm" accept-charset="utf-8" method="post"
                      action="${ctx}/collection/admin/addCategory">
                    <div class="modal-content">
                        <div class="modal-header bg-primary">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                            <h4 class="modal-title">增加栏目</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-12 form">
                                    <div class="form-body">
                                        <input type="hidden" name="resourceId" id="resourceId">
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">栏目名称:<span class="required"
                                                                                             aria-required="true"
                                                                                             style="color:red;"> * </span></label>
                                            <div class="col-md-8">
                                                <input type="text" name="ctName" id="ctName"
                                                       class="form-control"></input>
                                            </div>
                                        </div>
                                        <div style="height: 15px;"></div>
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">栏目顺序:<span class="required"
                                                                                             aria-required="true"> * </span></label>
                                            <div class="col-md-8">
                                                <input type="text" name="order" id="order" class="form-control"></input>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" data-dismiss="modal" class="btn">取消</button>
                            <button type="submit" onclick="checkForm();" id="addInfoBtn" class="btn blue">确定</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>


        <div id="updateInfoModal" class="modal fade" tabindex="-1" data-width="500">
            <div class="modal-dialog">
                <form class="form-horizontal" role="form" id="updateInfoForm" accept-charset="utf-8" method="post"
                      action="${ctx}/collection/admin/updateCategory">

                    <div class="modal-content">
                        <div class="modal-header bg-primary">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                            <h4 class="modal-title">修改栏目</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-12 form">
                                    <div class="form-body">
                                        <input type="hidden" name="id" id="id">
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">栏目名称:<span class="required"
                                                                                             aria-required="true"
                                                                                             style="color:red;"> * </span></label>
                                            <div class="col-md-8">
                                                <input type="text" name="ctName" id="name1"
                                                       class="form-control"></input>
                                            </div>
                                        </div>
                                        <div style="height: 15px;"></div>
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">栏目顺序:<span class="required"
                                                                                             aria-required="true"> * </span></label>
                                            <div class="col-md-8">
                                                <input type="text" name="order" id="order1"
                                                       class="form-control"></input>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" data-dismiss="modal" class="btn">取消</button>
                            <button type="submit" onclick="checkForm();" id="updateInfoBtn" class="btn blue">确定</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

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
        $().ready(function () {
            getCategoryList();//加载数据
        });


        function showForm() {
            $("#name").val("");
            $("#sort").val("");
            $("#addInfoModal").modal("show");
        }


        function editCategory(id) {
//            alert(id);
            $.ajax({
                url: "${ctx}/collection/admin/getCategoryById",
                type: "get",
                dataType: "json",
                data: {"id": id},
                success: function (data) {
                    console.log("data:" + data.category);
                    if (data != null) {
                        $("#id").val(data.category.id);
                        $("#name1").val(data.category.ctName);
                        $("#order1").val(data.category.order);
                    }
                }
            });

            $("#updateInfoModal").modal("show");
        }


        function getCategoryList() {
            $.ajax({
                url: "${ctx}/collection/admin/getCategoryList",
                type: "get",
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
            var html = template("resourceTmpl", data);
            $("#col_list").empty();
            if (data.length != 0) {
                $("#col_list").append(html);
            } else {
                $("#col_list").append("暂无数据");
            }
        }

        function deleteCategory(categoryId) {
            bootbox.confirm("确定要删除此条记录？", function (r) {
                if (r) {
                    $.ajax({
                        url: "${ctx}/collection/admin/deleteCategory",
                        type: "get",
                        data: {
                            id: categoryId
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data == null) {
                                toastr['success']('删除失败，请稍后重试!', '提示信息');
                                return;
                            }
                            if (data) {
                                toastr['success']('删除成功!', '提示信息');
                                getCategoryList();
                            }
                        },
                        error: function (err) {
                            toastr['success']('删除失败，请稍后重试!', '提示信息');
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