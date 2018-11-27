<%--
  Created by IntelliJ IDEA.
  User: sophie
  Date: 2017/11/14
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>内容采编编辑</title>
    <link href="${ctx}/resources/editor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="page-content  col-md-12">
    <ul class="breadcrumb" style="padding: 8px 27px;">
        <li><a href="${ctx}/">首页</a></li>
        <li><a href="${ctx}/collection/">管理中心</a></li>
        <li >内容采编编辑</li>
    </ul>
    <%--<h3>内容采编编辑</h3>--%>
    <div class="row padding-top-10">
        <div class="col-md-12">
            <!-- BEGIN PORTLET-->
            <div class="portlet box blue-madison">
                <div class="portlet-title">
                    <%--<div class="caption">内容编辑表单--%>
                    <%--</div>--%>
                    <div class="tools">
                    </div>
                </div>
                <div class="portlet-body form">
                    <!-- BEGIN FORM-->
                    <form action="<c:choose><c:when test="${operate=='edit'}">${ctx}/collection/admin/edit</c:when><c:otherwise>${ctx}/collection/admin/add</c:otherwise></c:choose>"
                          class="form-horizontal margin-bottom-0" method="POST" id="edit_collection" target="_blank">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="control-label col-md-1  col-sm-3">栏目:</label>

                                <div class="col-md-6  col-sm-4">
                                    <div class="radio-list " id="colType">
                                        <c:forEach items="${list}" var="item">
                                            <label class="radio-inline  " style="padding-left: 20px">
                                                <input type="radio" name="ctName" value="${item.ctName}"
                                                       <c:if test="${collection.ctName eq item.ctName}">checked</c:if>
                                                >
                                                    ${item.ctName}</label>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-1  col-sm-3">作者:</label>

                                <div class="col-md-3  col-sm-4">
                                    <input type="text" class="form-control" maxlength="25" name="author"
                                           value="${collection.author}"
                                           id="colAuthor">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-1  col-sm-3">标题:</label>

                                <div class="col-md-6  col-sm-8">
                                    <input type="text" class="form-control" maxlength="25" name="title"
                                           value="${collection.title}"
                                           id="colTitle">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-1 col-sm-3">正文:</label>

                                <div class="col-md-9 col-sm-9">
                                            <textarea id="myEditor" name="content" rows="15" class="xxxxx"
                                                      style="width: 100%" ></textarea>
                                </div>
                            </div>
                            <input type="hidden" class="form-control" maxlength="25" name="colId"
                                   id="colId" value="${colId}">

                        </div>
                        <div class="form-actions">
                            <div class="row">
                                <div class="col-md-offset-8 col-md-4">
                                    <button type="submit" class="btn green" onclick="previewAdd()"><i
                                            class="fa fa-check"></i> 预览
                                    </button>
                                    &nbsp;&nbsp;
                                    <button type="submit" class="btn green" onclick="submitAdd()"><i
                                            class="fa fa-check"></i> 提交
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- END FORM-->
                </div>
            </div>
            <!-- END PORTLET-->
        </div>
    </div>
</div>
<div class="row"></div>
</body>

<div id="siteMeshJavaScript">
    <script type="text/javascript">
        var ctx = '${ctx}';
    </script>
    <script src="${ctx}/resources/js/jquery.validate.1.14.0.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/resources/editor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/resources/editor/umeditor.js"></script>
    <script type="text/javascript" src="${ctx}/resources/editor/lang/zh-cn/zh-cn.js"></script>


    <script type="text/javascript">

        var submitUrl = '<c:choose><c:when test="${operate=='edit'}">${ctx}/collection/admin/edit</c:when><c:otherwise>${ctx}/collection/admin/add</c:otherwise></c:choose>';
        var previewUrl = '${ctx}/collection/admin/add/preview';
        var um;
        $().ready(function () {
            um = UM.getEditor('myEditor');
            // alert('${collection.content}');
            um.setContent('${collection.content}', true);
            validateForm();
        });
        function validateForm() {
            var validator = $("#edit_collection").validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error_element help-block-error', // default input error message class
//                focusInvalid: false, // do not focus the last invalid input
                ignore: ".xxxxx",
                rules: {
                    colType: {
                        required: true
                    },
                    colAuthor: {
                        required: true
                    },
                    colTitle: {
                        required: true
                    }
                },
                messages: {
                    colType: {
                        required: "必选"
                    },
                    colAuthor: {
                        required: "请填写作者"
                    },
                    colTitle: {
                        required: "请填写标题"
                    }
                },
                highlight: function (element) { // hightlight error inputs
                    $(element).closest('.form-group').addClass('has-error');   // set error class to the control group
                },
                unhighlight: function (element) { // revert the change done by hightlight
                    $(element).closest('.form-group').removeClass('has-error'); // set error class to the control group
                },
                submitHandler: function (form) {
                    um.sync();
                    var content = um.getContent();
                    if (content && content.length > 0) {
                        return true;
                    }
                    return false;
                },
                errorPlacement: function (label, element) {
                    label.insertAfter(element.is("textarea") ? element.next() : element);
                }
            });
            validator.focusInvalid = function () {
                if (this.settings.focusInvalid) {
                    try {
                        var toFocus = $(this.findLastActive() || this.errorList.length && this.errorList[0].element || []);
                        if (toFocus.is("textarea")) {
                            um.focus()
                        } else {
                            toFocus.filter(":visible").focus();
                        }
                    } catch (e) {
                    }
                }
            }
        }
        function submitAdd() {
            $("#edit_collection").attr("action", submitUrl);
            $("#edit_collection").attr("target", "");
            return true;
        }
        function previewAdd() {
            $("#edit_collection").attr("action", previewUrl);
            $("#edit_collection").attr("target", "_blank");
            return true;
        }

    </script>
</div>

</html>