<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/8 0008
  Time: 上午 9:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>数据上传</title>
    <link href="${ctx}/resources/bundles/jqeury-file-upload/css/jquery.fileupload.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/jqeury-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet"
          type="text/css">
</head>
<body>
<div class="col-md-12">
    <div class="portlet box blue col-md-6">
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-gift"></i>资源上传
            </div>
            <div class="tools">
            </div>
        </div>
        <div class="portlet-body form">
            <form role="form">
                <div class="form-body" id="form_id">
                    <%--<div class="form-group has-warning">--%>
                    <%--<label class="control-label">Input with warning</label>--%>
                    <%--<input type="text" class="form-control" id="inputWarning">--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <span class="btn btn-success fileinput-button" id="file-btn-id">
                            <i class="glyphicon glyphicon-plus"></i><span>选择文件</span>
                           <input type="file" name="files" id="fileupload_input" value="xxx" multiple/>
                         </span>
                        <span id="file-name-id"></span>
                    </div>
                    <%--<div class="form-group has-success ">--%>
                        <%--<label class="control-label">可视化插件</label>--%>
                        <%--<select class="form-control" name="viewType" id="viewTypeId">--%>
                            <%--<option value="">自动选择</option>--%>
                            <%--<option value="office">office</option>--%>
                            <%--<option value="pdf">pdf</option>--%>
                            <%--<option value="text">文本</option>--%>
                            <%--<option value="html">网页</option>--%>
                            <%--<option value="image">图片</option>--%>
                            <%--<option value="video">视频</option>--%>
                            <%--<option value="radio">音频</option>--%>
                            <%--<option value="json">json</option>--%>
                            <%--<option value="shape">shp.zip</option>--%>
                            <%--<option value="geojson">shp</option>--%>
                            <%--<option value="geotiff">Geotiff</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                </div>
                <div class="form-actions">
                    <button type="button" class="btn default" id="cancelId">取消</button>
                    <button type="button" class="btn red" id="submitId">提交</button>
                </div>
            </form>
        </div>
    </div>
    <div class="col-md-9" id="resListViewId">

    </div>
</div>
<script type="text/html" id="resListViewTmp">
    <div>
        <table class="table table-hover">
            <thead>
            <tr class="info">
                <td style="width: 50px">序号</td>
                <td>资源id</td>
                <td>文件名称</td>
                <%--<td>可视化插件</td>--%>
                <%--<td>预览</td>--%>
            </tr>
            </thead>
            <tbody id="content">
            {{each resList as value i}}
            <tr>
                <td>{{ i + 1 }}</td>
                <td> {{value.id}}</td>
                <td>{{value.fileName}}</td>
                <%--<td>{{value.viewType}}</td>--%>
                <td>
                    <a target="_blank" href="${ctx}/file/download?fileId={{value.id}}">预览</a>
                </td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
</script>
</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/jqeury-file-upload/js/jquery.ui.widget.js"></script>
    <script src="${ctx}/resources/bundles/jqeury-file-upload/js/jquery.iframe-transport.js"></script>
    <script src="${ctx}/resources/bundles/jqeury-file-upload/js/jquery.fileupload.js"></script>

    <script type="text/javascript">
        var ctx = '${ctx}';
        var fileUploadData = null;
        $().ready(function () {
//            var viewType;
            var upload = $("#fileupload_input").fileupload({
                url: ctx + "/file/upload",//文件上传地址
                autoUpload: false,
//                formData: {viewType: viewType},
                add: function (e, data) {
                    fileUploadData = data;
                    $.each(data.files, function (index, file) {
                        $("#file-name-id").html(file.name);
                    });
                },
                done: function (e, result) {
                    console.log(JSON.stringify(result.result));
                    fileUploadData = null;
                    $("#file-name-id").html("");
                    resListView();
                }
            });

            $("#submitId").click(function () {
                if (fileUploadData) {
//                    fileUploadData.formData = {viewType: $("#viewTypeId").val()};
                    fileUploadData.submit();
                }
                fileUploadData = null;
            });

            function resListView() {
                $.ajax({
                    url: "${ctx}/file/list",
                    dataType: "json",
                    success: function (data) {
                        var html = template("resListViewTmp", {resList: data});
                        $("#resListViewId").empty();
                        $("#resListViewId").append(html);
                    }
                });
            }

            resListView();

        });
    </script>

</div>
</html>
