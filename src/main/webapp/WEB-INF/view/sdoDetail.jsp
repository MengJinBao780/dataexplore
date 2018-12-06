<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2018/04/18
  Time: 15:32
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>欢迎访问CASEARTH广目系统</title>
    <META HTTP-EQUIV="pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <META HTTP-EQUIV="expires" CONTENT="0">
    <%-- <link href="${ctx}/resources/css/sdoDetail.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctx}/resources/css/newSdoDetail.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctx}/resources/bundles/nouislider/nouislider.min.css">
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="page-content">
    <!--[if lt IE 7]>
    <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="#">upgrade your
        browser</a> to improve your experience.</p>
    <![endif]-->
    <div class="detail-main">
        <div class="detail-left">
            <div class="detail-introduce">
                <div class="introduce-top">
                    <h2 style="font-weight: bold" id="header-title"></h2>
                    <div class="introduce-top-right">
                        <c:if test="${favorite=='0'}">
                            <a href="javascript:;" id="favorite" isValid="0">收藏数据集</a>
                        </c:if>
                        <c:if test="${favorite=='1'}">
                            <a href="javascript:;" id="favorite" isValid="1">已收藏</a>
                        </c:if>
                    </div>
                </div>
                <div class="introduce-main">
                    <div class="introduce-one">
                        <label>创 建 人：</label>
                        <span id="header-creator"></span>
                    </div>
                    <div class="introduce-one">
                        <label>最近更新：</label>
                        <span id="header-time"></span>
                    </div>
                    <div class="introduce-one">
                        <label>标 识符 ：</label>
                        <span id="header-pid"></span>
                    </div>
                    <div class="introduce-one">
                        <label>引用地址：</label>
                        <span id="header-quote" style="float:none;padding-left: 70px"></span>
                    </div>
                    <div class="introduce-one">
                        <label>文 件 量 ：</label>
                        <span id="header-fNumber"></span>
                    </div>
                    <div class="introduce-one">
                        <label>存 储 量：</label>
                        <span id="herder-mSize"></span>
                    </div>
                    <div class="introduce-one">
                        <label>浏 览 量：</label>
                        <span id="herder-sSize"></span>
                    </div>
                    <div class="introduce-one">
                        <label>下 载 量：</label>
                        <span id="herder-dSize"></span>
                    </div>
                    <div class="clear"></div>
                    <label>数据简介：</label>
                    <p class="data-introduce" id="header-desc"></p>
                    <div class="data-file">
                        <label>数据文件：</label>
                        <div class="files-type">
                            <span class="file-type-one"></span>
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div class="data-tags">
                        <label>数据标签：</label>
                    </div>
                    <div class="data-tag-remove"></div>
                    <input class="add-tag" type="text" name="" placeholder="追加标签，回车确认" id="searchAddTab">
                    <div class="clear"></div>
                </div>
                <div class="data-links">
                    <a href="javascript:;" id="show-metadata">元数据详情</a>
                    <a href="javascript:;" id="down-metadata">下载元数据</a>
                </div>
                <div class="clear"></div>
            </div>
            <div class="line"></div>
            <div id="table-form" style="display: none">
                <div class="map-detail">
                    <%--<label>关键词：</label>
                    <input style="width:302px;" type="text" name="">--%>
                    <label>标识符：</label>
                    <input style="width:141px;" type="text" name="" id="formLabel">
                    <label>条代号：</label>
                    <input style="width:141px;margin-right:0" type="text" name="" id="formAttribute">
                    <%--<label>提供者：</label>
                    <input type="text" name="">
                    <label>文件格式：</label>
                    <div class="files-type">
                        <span class="file-type-one">SHP</span>
                        <span class="file-type-two">CSV</span>
                        <span class="file-type-three">KML</span>
                    </div>--%>
                    <div class="clear"></div>
                </div>
                <!-- 范围 -->
                <div class="map-range">
                    <label>范 围：</label>
                    <input style="margin-left:8px;" id="eastLng" placeholder="最大经度" name="formRange"/>
                    <input id="westLng" placeholder="最小经度" name="formRange"/>
                    <input id="northLat" placeholder="最大纬度" name="formRange"/>
                    <input id="southLat" placeholder="最小纬度" name="formRange"/>
                </div>
                <%--<div class="map-range">
                    <label>高  度：</label>
                    <input style="margin-left:8px;" id="lowHeight" type="text" placeholder="最低海拔" />
                    <input id="longHeight" type="text" placeholder="最高海拔" />
                </div>--%>
                <!-- 云覆盖率 -->
                <div class="map-cover">
                    <label>云覆盖率</label>
                    <input id="startrate" type="text"/><label>%</label>
                    <div id="rateSlider"
                         style="width:290px;float:left;margin-top:6px;margin-right:26px;margin-left: 7px"></div>
                    <input id="stoprate" type="text"/><label>%</label>
                </div>
                <div id="mapContioner">
                    <div id="right-panel">
                        <div id="rectangular" style="height: 50px;">
                            <img src="${ctx}/resources/img/rect_icon.png" class="activeImg">
                            <img src="${ctx}/resources/img/rect_icon_active.png">
                        </div>
                    </div>
                </div>
            </div>
            <div id="table-form2" style="display: none">
                <div class="map-detail" id="map-detail">
                    <%--<div style='width: 293px;overflow: hidden;float: left'>
                        <label>标识符：</label>
                        <input style='width:150px;' type='text' name='updateExcel' >
                    </div>--%>
                </div>
            </div>
            <table id="table-th" class="table table-bordered">
                <caption>
                    <input class="select-download" type="button" value="批量下载" id="download-all">
                </caption>
                <thead></thead>
                <tbody></tbody>
            </table>
            <div style="overflow: hidden">
                <div id="page-table" style="float: right;line-height: 76px"></div>
                <div style="margin: 0px 0;line-height: 76px">
                    <span id="total-data"></span>
                </div>
            </div>
            <div class="tab-wrap">
                <div class="tab-tit">
                    <div class="tit-one comm tit-active">
                        <p>用户评论</p>
                        <span id="totalCount"></span>
                    </div>
                    <div class="tit-one api">API</div>
                    <%--<div class="tit-one software">软件工具</div>--%>
                    <div class="score">
                        <span id="avgScore"></span>
                        <div class="score-img">
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="tab-main">
                    <!-- 用户评论 -->
                    <div class="tab-one">
                        <div id="user-lists">
                        </div>

                        <div style="float: left;line-height: 76px">
                            <span id="get-total"></span>
                        </div>
                        <div id="contents-list" style="float: right"></div>
                        <div class="clear"></div>
                        <div class="clear"></div>
                        <div class="write-comment">
                            <div style="margin-bottom: 15px;font-size: large">用户评分</div>
                            <textarea style="resize: none;display: none" name="text-comments"></textarea>
                            <div id="star-list" class="star" style="margin-left: 77px">
                                <a href="javascript:;"><img src="${ctx}/resources/img/star.png"
                                                            id="star1"/></a>
                                <a href="javascript:;"><img src="${ctx}/resources/img/star.png"
                                                            id="star2"/></a>
                                <a href="javascript:;"><img src="${ctx}/resources/img/star.png"
                                                            id="star3"/></a>
                                <a href="javascript:;"><img src="${ctx}/resources/img/star.png"
                                                            id="star4"/></a>
                                <a href="javascript:;"><img src="${ctx}/resources/img/star.png"
                                                            id="star5"/></a>
                            </div>
                            <div class="write-btn">
                                <img src="${ctx}/resources/img/write.jpg">
                                <span id="editor-pull">发评论</span>
                            </div>
                        </div>
                    </div>
                    <div class="tab-two" style="display: none;">
                        <div <%--style="width: 880px"--%>>
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th class="col-md-3">API</th>
                                    <th class="col-md-6">URL</th>
                                    <th class="col-md-1">格式</th>
                                    <th class="col-md-1">HTTP</th>
                                    <%--<th class="col-md-1"> 需要登录</th>--%>
                                </tr>
                                </thead>
                                <tbody>
                                <%
                                    String path = request.getContextPath();
                                    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
                                %>
                                <tr>
                                    <td class="api-head api-data">通过PID获取文件列表 &nbsp;getFileListBySdoPid</td>
                                    <td><%=basePath%>/api/getFileListBySdoPid?sdoPid=${sdo.pid}&pageNum=10&curPage=1</td>
                                    <td>JSON</td>
                                    <td>GET</td>
                                    <%--<td>否</td>--%>
                                </tr>
                                <tr>
                                    <td class="api-head api-data">通过ID获取文件列表 &nbsp;getFileListBySdoId</td>
                                    <td><%=basePath%>/api/getFileListBySdoId?sdoId=${sdo.id}&pageNum=10&curPage=1</td>
                                    <td>JSON</td>
                                    <td>GET</td>
                                    <%--<td>否</td>--%>
                                </tr>
                                <tr>
                                    <td class="api-head api-data">文件单个下载 &nbsp;downloadOneFile</td>
                                    <td><%=basePath%>/sdo/downloadOneFile?id=fileId</td>
                                    <td>文件</td>
                                    <td>GET</td>
                                    <%--<td>否</td>--%>
                                </tr>
                                <tr>
                                    <td class="api-head api-data">文件批量下载 &nbsp;downloadFiles</td>
                                    <td><%=basePath%>/sdo/downloadFiles?listId=fileId1,fileId2,fileId3</td>
                                    <td>文件</td>
                                    <td>GET</td>
                                    <%--<td>否</td>--%>
                                </tr>
                                <tr>
                                    <td class="api-head api-data">获取元数据 &nbsp;sdoMetadata</td>
                                    <td><%=basePath%>/api/sdoMetadata/${sdo.id}</td>
                                    <td>JSON</td>
                                    <td>GET</td>
                                    <%--<td>否</td>--%>
                                </tr>

                                <tr>
                                    <td class="api-head api-data">获取文件元数据 &nbsp;fileMetadata</td>
                                    <td><%=basePath%>/api/fileMetadata/fileId</td>
                                    <td>JSON</td>
                                    <td>GET</td>
                                    <%--<td>否</td>--%>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="tab-three" style="display: none;">
                        <p>软件工具</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="detail-right">
            <div class="dataset-person">
                <h2 class="common-tit" id="related-peo-name"></h2>
                <ul class="contact">
                    <li>
                        <label>联系人：</label>
                        <span id="related-peo-pName"></span>
                    </li>
                    <li>
                        <label style="float: left">电 &nbsp;话 ：</label>
                        <span id="related-peo-pTel" style="display: block;padding-left: 70px"></span>
                    </li>
                    <li>
                        <label style="float: left">邮 &nbsp;件 ：</label>
                        <span id="related-peo-pEmail" style="display: block;padding-left: 70px"></span>
                    </li>
                </ul>
            </div>
            <div class="data-relative">
                <h2 class="common-tit"><a href="${ctx}/sdo/relation/map/${id}" target="_blank">数据关联 >>></a></h2>
                <div class="relative-wrap" id="relateSdoId"></div>
            </div>
            <div class="data-recommend">
                <h2 class="common-tit">数据推荐</h2>
                <ul class="recommend-one">
                    <c:forEach items="${recList}" var="item">
                        <li>
                            <i>·</i>
                            <a title="${item.sdoTitle}" href="${ctx}/sdo/detail/${item.sdoId}">
                                <c:if test="${fn:length(item.sdoTitle) > 19}">
                                    ${fn:substring(item.sdoTitle,0,19)}
                                    <span style="text-align:right">...</span>
                                </c:if>
                                <c:if test="${fn:length(item.sdoTitle) <= 19}">
                                    ${item.sdoTitle}
                                </c:if>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="modal fade " id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header" style="background-color: #337ab7">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">详细元数据</h4>
                </div>
                <div style="width: 100%;height: 600px;overflow: auto">
                    <div class="modal-body">
                        <div>
                            <table class="table table-bordered">
                                <tbody>
                                <tr>
                                    <td rowspan="8" style="width: 15%">基本信息</td>
                                    <td style="width: 25% !important;">数据对象</td>
                                    <td style="width: 65%" id="data-title"></td>
                                </tr>
                                <tr>
                                    <td>简介</td>
                                    <td id="data-desc"></td>
                                </tr>
                                <tr>
                                    <td>资源关键词</td>
                                    <td id="data-keyword"></td>
                                </tr>
                                <tr>
                                    <td>科学分类</td>
                                    <td id="data-catalog"></td>
                                </tr>
                                <tr>
                                    <td>产品分类</td>
                                    <td id="data-productName"></td>
                                </tr>
                                <tr>
                                    <td>访问限制</td>
                                    <td id="data-visitLimit"></td>
                                </tr>
                                <tr>
                                    <td>格式</td>
                                    <td id="data-ftName"></td>
                                </tr>
                                <tr>
                                    <td>范围说明</td>
                                    <td id="data-rangeDescription"></td>
                                </tr>
                                <tr>
                                    <td rowspan="5">地理范围</td>
                                    <td>地理中心点坐标</td>
                                    <td id="data-center"></td>
                                </tr>
                                <tr>
                                    <td>地理左上角坐标</td>
                                    <td id="data-upLeft"></td>
                                </tr>
                                <tr>
                                    <td>地理右上角坐标</td>
                                    <td id="data-upRight"></td>
                                </tr>
                                <tr>
                                    <td>地理右下角坐标</td>
                                    <td id="data-lowLeft"></td>
                                </tr>
                                <tr>
                                    <td>地理左下角坐标</td>
                                    <td id="data-lowRight"></td>
                                </tr>
                                <tr>
                                    <td rowspan="2">时间范围</td>
                                    <td>开始时间</td>
                                    <td id="data-startTime"></td>
                                </tr>
                                <tr>
                                    <td>结束时间</td>
                                    <td id="data-endTime"></td>
                                </tr>
                                <tr>
                                    <td rowspan="2">分辨率</td>
                                    <td>空间分辨率</td>
                                    <td id="data-rSpatial"></td>
                                </tr>
                                <tr>
                                    <td>时间分辨率</td>
                                    <td id="data-rTime"></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>版权声明</td>
                                    <td id="data-rightstatement"></td>
                                </tr>
                                <tr>
                                    <td rowspan="3">创建信息</td>
                                    <td>创建机构</td>
                                    <td id="data-cOrganization"></td>
                                </tr>
                                <tr>
                                    <td>创建人员</td>
                                    <td id="data-cName"></td>
                                </tr>
                                <tr>
                                    <td>创建日期</td>
                                    <td id="data-cTime"></td>
                                </tr>
                                <tr>
                                    <td rowspan="4">发布信息</td>
                                    <td>发布机构</td>
                                    <td id="data-pOrganization"></td>
                                </tr>
                                <tr>
                                    <td>邮箱</td>
                                    <td id="data-email"></td>
                                </tr>
                                <tr>
                                    <td>电话</td>
                                    <td id="data-tel"></td>
                                </tr>
                                <tr>
                                    <td>最新发布日期</td>
                                    <td id="data-pTime"></td>
                                </tr>
                                <tr>
                                    <td rowspan="3">文件信息</td>
                                    <td>总存储量</td>
                                    <td id="data-mSize"></td>
                                </tr>
                                <tr>
                                    <td>总文件数</td>
                                    <td id="data-toFilesNumber"></td>
                                </tr>
                                <tr>
                                    <td>总记录数</td>
                                    <td id="data-toRecordNumber"></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade " id="myModalTwo" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header" style="background-color: #337ab7">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close" name="close-metadata"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabelTwo">元数据</h4>
                </div>
                <div class="modal-body">
                    <div class="data-wrap">
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <td rowspan="6" style="width: 15%">基本信息</td>
                                <td style="width: 25% !important;">标识</td>
                                <td style="width: 65%" id="file-pid"></td>
                            </tr>
                            <tr>
                                <td>条代号</td>
                                <td id="file-number"></td>
                            </tr>
                            <tr>
                                <td>云量</td>
                                <td id="file-cloudiness"></td>
                            </tr>
                            <tr>
                                <td>创建时间</td>
                                <td id="file-createTime"></td>
                            </tr>
                            <tr>
                                <td>更新时间</td>
                                <td id="file-updateTime"></td>
                            </tr>
                            <tr>
                                <td>状态码</td>
                                <td id="file-status"></td>
                            </tr>

                            <tr>
                                <td rowspan="3">文件</td>
                                <td>文件名称</td>
                                <td id="file-fileName"></td>
                            </tr>
                            <tr>
                                <td>文件类型ID</td>
                                <td id="file-ftId"></td>
                            </tr>
                            <tr>
                                <td>文件类型名称</td>
                                <td id="file-ftName"></td>
                            </tr>
                            <tr>
                                <td>可视化</td>
                                <td>可视化插件</td>
                                <td id="file-previewType"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            name="close-metadata">关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade  " id="myModalThree" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel">
        <div class="modal-dialog" style="width: 500px" role="document">
            <div class="modal-content">
                <div class="modal-header" style="background-color: #337ab7">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close" name="close-metadata"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabelThree">图片预览</h4>
                </div>
                <div class="modal-body" style="height: 368px;position: relative" id="img-pos">
                    <img src="" id="imgcontent"
                         style="width: 250px;height: 250px;position: absolute;top:0px;bottom: 0px;left: 0px;right: 0px;margin: auto">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            name="close-metadata">关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.js"></script>
    <script type="text/javascript"
            src="http://webapi.amap.com/maps?v=1.4.6&dev-Map=74dbb914c763808d47998ac6e45d706a&plugin=AMap.MouseTool"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <script src="${ctx}/resources/js/commentsStar.js"></script>
    <script src="${ctx}/resources/bundles/nouislider/nouislider.min.js"></script>
    <script src="${ctx}/resources/js/dataset.js"></script>


    <script>
        var sdoId = "${id}";
        var tagsNum = 0;
        var HDFData = {};
        var EXCELData = {};
        var Exp = /^(-|\+)?[0-9]+.?[0-9]*$/;
        var hdfUrl = "${ctx}/sdo/getFileByHDF";
        $("#first").removeClass("active");
        $("#second").addClass("active");
        $("#third").removeClass("active");
        var keyWords = JSON.parse(sessionStorage.getItem("keyWords"));
        $(function () {
            visitSdo();
            getScoreList(1);
        })
        function visitSdo() {
            $.ajax({
                url: "${ctx}/sdo/visitSdo?id=" + sdoId,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    var constTataType = data.fileType;
                    $("#header-creator").html(data.creator[0]);
                    /*$("#header-creator").attr("title", data.creator[0]);*/
                    $("#header-desc").html(data.desc);
                    $("#header-fNumber").html(data.fNumber);
                    $("#herder-mSize").html(data.mSize);

                    //20180625修改 todo 先暂时用假数据
                    //$("#header-pid").html(data.pid);
                    $("#header-pid").html("CSTR:00000.11.01.rINuDqM1Hr");
                    //引用地址
                    //$("#header-quote").html(data.quote);
                    $("#header-quote").html(data.creator[0] + "." + data.title + "." + data.time);

                    $("#header-time").html(data.time);
                    $("#header-title").html(data.title);
                    $("#herder-sSize").html(data.vCount);
                    $("#herder-dSize").html(data.dCount);
                    $(".file-type-one").html(data.fileType);
                    for (var i = 0; i < data.tags.length; i++) {
                        $(".data-tags").append("<span>" + data.tags[i] + "</span>")
                    }
                    $("#searchAddTab").bind("keypress", function (ev) {
                        var event = ev || window.event;
                        if (event.keyCode == 13) {
                            if (loginId == "") {
                                toastr["error"]("请先登录！");
                                $(this).val("");
                                return;
                            }
                            if (tagsNum >= 5) {
                                toastr["error"]("最多存在5个自定义标签");
                                $(this).val("");
                                return;
                            }
                            var tagContent = $.trim($(this).val());
                            var flag =  filter_keywords(tagContent,keyWords);
                            if(flag.value =="1"){
                                toastr["error"]("标签存在敏感词！"+flag.keyword);
                                $(this).val("");
                                return
                            }

                            /*tagContent =  filter_keywords(tagContent);
                            tagContent = filter_keywords(tagContent);*/

                            $.ajax({
                                url: "${ctx}/addTagOfSdo",
                                type: "get",
                                data: {
                                    tagName: tagContent,
                                    sdoId: sdoId
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (data.result == 1) {
                                        tagsNum += 1;
                                        $(".data-tag-remove").append("<div class='data-tags1' id=" + data.tagId + "> <span>" + htmlEncodeJQ(tagContent) + "</span> <span class='delete'>×</span> </div>")
                                        /*$(".data-tag-remove").append("<div class='data-tags1' id="+data.tagId+"> <span>"+tagContent+"</span> <span class='delete'>×</span> </div>")*/
                                        $("#searchAddTab").val("");
                                    } else {
                                        toastr["error"]("标签已经存在！");
                                        $("#searchAddTab").val("");
                                        return
                                    }

                                }
                            });
                        }
                    });
                    $(".data-tag-remove").delegate('.delete', 'click', function () {
                        var closeTag = $(this).parent();
                        var tagId = closeTag.attr("id");
                        $.ajax({
                            url: "${ctx}/deleteTagOfSdo",
                            type: "get",
                            data: {
                                tagId: tagId
                            },
                            dataType: "json",
                            success: function (data) {
                                closeTag.remove();
                                tagsNum -= 1;
                            }
                        });

                    });
                    $("#down-metadata").attr("href", "${ctx}/sdo/downloadExcel?id=" + data.id)
                    $("#show-metadata").attr("onlyID", data.id);
                    $("#show-metadata").on("click", function () {
                        var id = $(this).attr("onlyID");
                        $.ajax({
                            url: "${ctx}/sdo/getSdoDetails?id=" + sdoId,
                            type: "GET",
                            success: function (data) {
                                var data = JSON.parse(data);
                                $("#data-title").html(data.title)
                                $("#data-desc").html(data.desc)
                                $("#data-keyword").html(data.keyword)
                                $("#data-catalog").html(data.catalog)
                                $("#data-productName").html(data.productName)
                                $("#data-visitLimit").html(data.visitLimit)
                                $("#data-ftName").html(data.ftName)
                                $("#data-rangeDescription").html(data.rangeDescription)
                                $("#data-center").html(data.center)
                                $("#data-upLeft").html(data.upLeft)
                                $("#data-upRight").html(data.upRight)
                                $("#data-lowLeft").html(data.lowLeft)
                                $("#data-lowRight").html(data.lowRight)
                                $("#data-startTime").html(data.startTime)
                                $("#data-endTime").html(data.endTime)
                                $("#data-rSpatial").html(data.rSpatial)
                                $("#data-rTime").html(data.rTime)
                                $("#data-rightstatement").html(data.rightstatement)
                                $("#data-cOrganization").html(data.cOrganization)
                                $("#data-cName").html(data.cName)
                                $("#data-cTime").html(data.cTime)
                                $("#data-pOrganization").html(data.pOrganization)
                                $("#data-email").html(data.email)
                                $("#data-tel").html(data.tel)
                                $("#data-pTime").html(data.pTime)
                                $("#data-mSize").html(data.mSize)
                                $("#data-toFilesNumber").html(data.toFilesNumber)
                                $("#data-toRecordNumber").html(data.toRecordNumber)
                                $("#myModal").modal('show');

                            },
                            error: function () {
                                alert("下载失败");
                            }
                        })
                    });
                    $("#related-peo-name").html(data.pOrganization);
                    $("#related-peo-pName").html(data.pOrganization);
                    $("#related-peo-pTel").append(data.tel + "<br>");
                    $("#related-peo-pEmail").append(data.email + "<br>");
                    /*var arrpTel = data.pTel.split(";");
                    for (var i = 0; i < arrpTel.length; i++) {
                        $("#related-peo-pTel").append(arrpTel[i] + "<br>");
                    }
                    var arrEmail = data.pEmail.split(";");
                    for (var i = 0; i < arrEmail.length; i++) {
                        $("#related-peo-pEmail").append(arrEmail[i] + "<br>");
                    }*/

                    if (constTataType != "XLSX" && constTataType != "ARC/GRID") {
                        $("#table-form").show();
                        var url = "${ctx}/sdo/getFileByHDF";
                        updateHDF();
                        getMidData(1, HDFData, constTataType, url);

                        slider.noUiSlider.on('change.one', function (values, handle, unencoded, tap, positions) {
                            var startrate = parseInt(values[0]);
                            var endrate = parseInt(values[1]);
                            $("#startrate").val(startrate);
                            $("#stoprate").val(endrate);
                            updateHDF();
                            getMidData(1, HDFData, constTataType, url);
                        });
                        $("[name='formRange']").on("focusout", function () {
                            updateHDF();
                            isDrawRect();
                            getMidData(1, HDFData, constTataType, url);
                        });
                        $("#formLabel").on("focusout", function () {
                            updateHDF();
                            getMidData(1, HDFData, constTataType, url);
                        })
                        $("#formAttribute").on("focusout", function () {
                            updateHDF();
                            getMidData(1, HDFData, constTataType, url);
                        })
                        $("[name='formRange']").on("keypress", function (ev) {
                            var event = ev || window.event;
                            if (event.keyCode == 13) {
                                updateHDF();
                                /*isDrawRect();*/
                                getMidData(1, HDFData, constTataType, url);
                            }
                        });
                        $("#formLabel").on("keypress", function (ev) {
                            var event = ev || window.event;
                            if (event.keyCode == 13) {
                                updateHDF();
                                /*isDrawRect();*/
                                getMidData(1, HDFData, constTataType, url);
                            }
                        });
                        $("#formAttribute").on("keypress", function (ev) {
                            var event = ev || window.event;
                            if (event.keyCode == 13) {
                                updateHDF();
                                /*isDrawRect();*/
                                getMidData(1, HDFData, constTataType, url);
                            }
                        });
                        $("#rectangular").on("click", function () {
                            if ($(this).children(".activeImg").index() == 1) {
                                $("#westLng").val("0")
                                $("#eastLng").val("0")
                                $("#northLat").val("0")
                                $("#southLat").val("0")
                                $(this).children().first().addClass("activeImg");
                                $(this).children().last().removeClass("activeImg");
                                mainMap.clearMap();
                                updateHDF();
                                getMidData(1, HDFData, constTataType, url);
                                return
                            }
                            $(this).children().first().removeClass("activeImg")
                            $(this).children().last().addClass("activeImg")
                            mainMap.clearMap();
                            mouseTools.rectangle()
                            AMap.event.addListener(mouseTools, 'draw', function (e) { //����¼�\
                                var paths = e.obj.getPath();
                                if (paths[0].lng > paths[2].lng) {
                                    $("#westLng").val(paths[2].lng)
                                    $("#eastLng").val(paths[0].lng)
                                } else {
                                    $("#westLng").val(paths[0].lng)
                                    $("#eastLng").val(paths[2].lng)
                                }
                                if (paths[0].lat > paths[2].lat) {
                                    $("#southLat").val(paths[2].lat)
                                    $("#northLat").val(paths[0].lat)
                                } else {
                                    $("#southLat").val(paths[0].lat)
                                    $("#northLat").val(paths[2].lat)
                                }
                                updateHDF();

                                getMidData(1, HDFData, constTataType, hdfUrl);
                                mouseTools.close();

                                /*for (var i = 0; i < paths.length; i++) {
                                    new AMap.Marker({
                                        position: paths[i],
                                        title: paths[i],
                                        map: mainMap
                                    });
                                };*/
                                $(".rect-box").removeClass('rect-active');

                            });
                        })
                    }
                    else {
                        var ExStr = "";
                        for (var i = 0; i < data.condition.length; i++) {
                            ExStr += "<div style='width: 293px;overflow: hidden;float: left'> <label for=" + data.condition[i].fieldName + ">" + data.condition[i].fieldTitle + "</label>" +
                                "<input style='width:150px;' type='text' name='updateExcel' id=" + data.condition[i].fieldName + "> </div>"
                        }
                        var dataArr = data.condition;

                        function updateEXCEL(arr) {
                            EXCELData.sdoId = sdoId;
                            for (var i = 0; i < arr.length; i++) {
                                var strName = arr[i].fieldName;
                                EXCELData[strName] = $("#" + strName + "").val();
                            }
                        }

                        $("#map-detail").append(ExStr);
                        $("#table-form2").show();

                        var url = "${ctx}/sdo/getFileByCERN";
                        updateEXCEL(dataArr);
                        getMidData(1, EXCELData, constTataType, url);
                        $("[name='updateExcel']").on('focusout', function () {
                            updateEXCEL(dataArr);
                            getMidData(1, EXCELData, constTataType, url);
                        })
                        $("[name='updateExcel']").on("keypress", function (ev) {
                            var event = ev || window.event;
                            if (event.keyCode == 13) {
                                console.log("aaaa")
                                updateEXCEL(dataArr);
                                /*isDrawRect();*/
                                getMidData(1, EXCELData, constTataType, url);
                            }
                        });
                    }
                }
            })
        }
        function getScoreList(pageNo) {
            $.ajax({
                url: "${ctx}/sdo/getScoreList",
                type: "post",
                data: {
                    sdoId: sdoId,
                    pageNo: pageNo
                },
                dataType: "json",
                success: function (data) {
                    $("#get-total").html("");
                    $("#get-total").html("当前第" + data.currentPage + "页，共" + data.totalCount + "条数据")
                    $(".score-img").empty()
                    $("#avgScore").html(data.avgScore.toFixed(1));
                    data.avgScore = Math.ceil(data.avgScore)
                    $("#totalCount").html("(" + data.totalCount + ")");
                    $(".score-img").append("<img src='${ctx}/resources/img/" + data.avgScore + "star.png'>");
                    $("#user-lists").empty();
                    var str = "";
                    for (var i = 0; i < data.scoreList.length; i++) {
                        str += "<div class='user-one'> <div class='name'>" + data.scoreList[i].loginId + "</div> <div class='comment'>" +
                            "<div class='star'> <img src='${ctx}/resources/img/" + data.scoreList[i].score + "star.png'> </div>" +
                            "<span class='comment-time'>" + convertMilsToDateString(data.scoreList[i].createTime) + "</span> <p>" + data.scoreList[i].content + "</p>";

                        if (data.scoreList[i].loginId == loginId) {
                            str += "<div class='delete-user'onlyId=" + data.scoreList[i].id + ">删除</div>";
                        }
                        str += "</div> <div class='clear'></div> </div>";
                    }
                    $("#user-lists").append(str);
                    if ($("#contents-list .bootpag").length != 0) {
                        $("#contents-list").off();
                        $('#contents-list').empty();
                    }
                    $("#contents-list").bootpag({
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
                        getScoreList(num);
                    })
                }
            });
        }
        function updateHDF() {
            HDFData.pid = $("#formLabel").val();
            HDFData.number = $("#formAttribute").val();
            if (!Exp.test($("#westLng").val()) || $("#westLng").val() == "") {
                $("#westLng").val("")
            }
            if (!Exp.test($("#eastLng").val()) || $("#eastLng").val() == "") {
                $("#eastLng").val("")
            }
            if (!Exp.test($("#northLat").val()) || $("#northLat").val() == "") {
                $("#northLat").val("")
            }
            if (!Exp.test($("#southLat").val()) || $("#southLat").val() == "") {
                $("#southLat").val("")
            }
            HDFData.x1 = $("#westLng").val();
            HDFData.x2 = $("#eastLng").val();
            HDFData.y2 = $("#northLat").val();
            HDFData.y1 = $("#southLat").val();
            HDFData.cloudiness1 = $("#startrate").val();
            HDFData.cloudiness2 = $("#stoprate").val();
            HDFData.sdoId = sdoId;
        }
        function isDrawRect() {
            var x1 = parseFloat($("#westLng").val());
            var x2 = parseFloat($("#eastLng").val());
            var y1 = parseFloat($("#southLat").val());
            var y2 = parseFloat($("#northLat").val());
            var num = x1 + x2 + y1 + y2;
            if (x2 < x1) {
                toastr["error"]("经度大小输入错误");
                return
            }
            if (y2 < y1) {
                toastr["error"]("纬度大小输入错误");
                return
            }
            if (!isNaN(num) && x2 > x1 && y2 > y1) {
                var southWest = new AMap.LngLat(x1, y1)
                var northEast = new AMap.LngLat(x2, y2)
                var bounds = new AMap.Bounds(southWest, northEast);
                mainMap.clearMap();
                new AMap.Rectangle({
                    map: mainMap,
                    bounds: bounds,
                    strokeColor: '#1779cc',
                    strokeOpacity: 1,
                    strokeWeight: 0,
                    fillColor: '#1779cc',
                    fillOpacity: 0.5,
                    zIndex: 10,
                    bubble: true,
                    cursor: 'pointer',
                });
                mainMap.setZoom(7);
                mainMap.setCenter([(x1 + x2) / 2, (y1 + y2) / 2]);
            } else {
                mainMap.clearMap();
            }
        }

        function getMidData(pageNo, data, type, url) {
            var consData = data;
            consData.fileType = type;
            consData.pageNum = pageNo;
            console.log(consData)
            $.ajax({
                url: url,
                type: "GET",
                data: consData,
                success: function (data) {
                    var list = JSON.parse(data);
                    console.log(list)
                    $("#total-data").html("");
                    $("#total-data").html("当前第" + list.pageNum + "页，共" + list.totalCount + "条数据")
                    $("#table-th thead").empty();
                    $("#table-th tbody").empty();
                    var headStr = "<th><input type='checkbox' id='checkedAll'></th>";
                    var bodyStr = "";
                    for (var i = 0; i < list.topTitle.length; i++) {
                        headStr += "<th name=" + list.topTitle[i].fieldName + ">" + list.topTitle[i].fieldTitle + "</th>"
                    }
                    headStr += "<th style='width:22%'>操作</th>";

                    for (var i = 0; i < list.data.length; i++) {
                        var downurl = "${ctx}/sdo/downloadOneFile?id=" + list.data[i].id;
                        bodyStr += "<tr onlyID=" + list.data[i].id + "><td><input type='checkbox' name='items'></td>"
                        for (var j = 0; j < list.topTitle.length; j++) {
                            var val = list.topTitle[j].fieldName;
                            bodyStr += "<td>" + list.data[i][val] + "</td>"
                        }
                        bodyStr += "<td> <button  class='operation-btn tab-preview' > 预览 </button>" +
                            " <button  class='operation-btn tab-metadata' data-toggle='modal' data-target='#myModalTwo'name='tab-metadata' > 元数据 </button>" +
                            "<button  class='operation-btn tab-download'> 下载 </button> " +
//                            "<button  class='operation-btn tab-copyid' > 复制id </button>" +
                            "<button type='button' class='operation-btn tab-copyid' data-toggle='popover' title='文件id' data-content='"+list.data[i].id+"'>文件id</button>" +
                            "</td></tr>"
                    }

                    $("#table-th thead").append(headStr);
                    $("#table-th tbody").append(bodyStr);

                    $("#checkedAll").on("click", function () {
                        $("[name='items']").prop("checked", $(this).prop('checked'))
                    })
                    if ($("#page-table .bootpag").length != 0) {
                        $("#page-table").off();
                        $('#page-table').empty();
                    }
                    $("[data-toggle='popover']").popover();
                    /*table分页请求*/
                    $('#page-table').bootpag({
                        total: list.pageSum,
                        page: list.pageNum,
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
                        getMidData(num, consData, type, url);
                    });
                }
            })
        }

        function htmlEncodeJQ(str) {
            return $('<span/>').text(str).html();
        }
        function htmlDecodeJQ(str) {
            return $('<span/>').html(str).text();
        }
        function filter_keywords(value,keywords) {
            //获取文本输入框中的内容
            //遍历敏感词数组
            var key = {};
            for (var i = 0; i < keywords.length; i++) {
                //全局替换
                /*var reg = new RegExp(keywords[i], "g");*/
                //判断内容中是否包括敏感词
                if (value.indexOf(keywords[i]) != -1) {
                    /*var result = value.replace(reg, "**");
                    value = result;*/
                    key.value = "1";
                    key.keyword =keywords[i];
                    return key;
                }
            }
            key.value = "0";
            key.keyword =""
            return key
        }
        $(".comm").click(function () {
            $(".tab-one").show();
            $(".tab-two").hide();
            $(".tab-three").hide();
            $(".score").show();
            $(".comm").addClass("tit-active");
            $(".api").removeClass("tit-active");
            $(".software").removeClass("tit-active");
        })
        $(".api").click(function () {
            $(".tab-two").show();
            $(".tab-one").hide();
            $(".tab-three").hide();
            $(".score").hide();
            $(".api").addClass("tit-active");
            $(".comm").removeClass("tit-active");
            $(".software").removeClass("tit-active");
        })
        $(".software").click(function () {
            $(".tab-three").show();
            $(".tab-one").hide();
            $(".tab-two").hide();
            $(".software").addClass("tit-active");
            $(".comm").removeClass("tit-active");
            $(".api").removeClass("tit-active");
        })
        /*table预览*/
        $("#table-th").delegate('.tab-preview', 'click', function () {
            var id = $(this).parent().parent().attr("onlyID");
            var indexImgNum = $(".tab-preview").index(this)
            $.ajax({
                url: "${ctx}/sdo/getPreview?id=" + id,
                type: "GET",
                success: function (data) {
                    var data = JSON.parse(data);
                    if (data.previewType == "img") {
                        $("#imgcontent").attr("src", "${ctx}/resources/img/hdf/files/file" + (indexImgNum + 1) + ".jpeg");
                        $("#myModalThree").modal('show');
                    } else if (data.previewType == "datatable") {
                        window.open("${ctx}/sdo/readExcel?filePath=" + data.filePath + "&fileType=" + data.fileType);
                    } else if (data.previewType == "openoffice") {
                        window.open("${ctx}/sdo/office?filePath=" + data.filePath + "&fileType=" + data.fileType);
                    } else if (data.previewType == "shape") {
                        window.open("${ctx}/sdo/file/view?fileId=" + id);
                    }
                },
                error: function () {
                    alert("下载失败");
                }
            })


        })
        /*table元数据*/
        $("#table-th").delegate('.tab-metadata', 'click', function () {
            var id = $(this).parent().parent().attr("onlyID");
            $.ajax({
                url: "${ctx}/sdo/getFileInfo?id=" + id,
                type: "GET",
                success: function (data) {
                    var data = JSON.parse(data);
                    $("#file-pid").html(data.pid)
                    $("#file-number").html((data.number === "") ? "--" : data.number)
                    $("#file-cloudiness").html(data.cloudiness)
                    $("#file-createTime").html(convertMilsToDateTimeString(data.createTime))
                    $("#file-updateTime").html(convertMilsToDateTimeString(data.updateTime))
                    $("#file-status").html(data.status)
                    $("#file-fileName").html(data.fileName)
                    /*$("#file-filePath").html(data.filePath)*/
                    $("#file-ftId").html(data.ftId)
                    $("#file-ftName").html(data.ftName)
                    /*$("#file-previewFile").html(data.previewFile)*/
                    $("#file-previewType").html(data.previewType)
                },
                error: function () {
                    alert("下载失败");
                }
            })
        })
        /*table下载*/
        $("#table-th").delegate('.tab-download', 'click', function () {
            if (loginId == "") {
                toastr["error"]("请先登录！");
                return;
            }
            var id = $(this).parent().parent().attr("onlyID");
            window.location.href = "${ctx}/sdo/downloadOneFile?id=" + id;
        })
        /*全部下载*/
        $("#download-all").on("click", function () {
            if (loginId == "") {
                toastr["error"]("请先登录！");
                return;
            }
            var $eleChecked = $("[name='items']:checked")
            var numChecked = $eleChecked.size();
            if (numChecked == 0) {
                toastr["warning"]("请选择需要下载项");
                return
            }
            var list = new Array();
            $eleChecked.each(function () {
                list.push($(this).parent().parent().attr("onlyID"))
            });
            var url = "${ctx}/sdo/downloadFiles?listId=" + list.toString();
            window.location.href = url;
        })


        $("#favorite").on("click", function () {
            if (loginId == "") {
                toastr["error"]("请先登录！");
                return;
            }
            if ($(this).attr("isValid") == 0) {
                $.ajax({
                    url: "${ctx}/sdo/favorite",
                    type: "get",
                    dataType: "json",
                    data: {
                        sdoId: '${id}',
                        isValid: 1,
                        randomParam: Math.random()
                    },
                    success: function (isOK) {
                        if (isOK == true) {
                            $("#favorite").html('已收藏');
                            $("#favorite").attr("isValid", "1");
                            toastr["success"]("收藏成功!");
                        } else {
                            toastr["error"]("收藏失败！");
                        }
                    }
                });
            } else {
                $.ajax({
                    url: "${ctx}/sdo/favorite",
                    type: "get",
                    dataType: "json",
                    data: {
                        sdoId: '${id}',
                        isValid: 0,
                        randomParam: Math.random()
                    },
                    success: function (isOK) {
                        if (isOK == true) {
                            $("#favorite").html('收藏数据集');
                            $("#favorite").attr("isValid", "0");
                            toastr["success"]("取消收藏成功!");
                        } else {
                            toastr["error"]("取消收藏失败！");
                        }
                    }
                });
            }
        });
        /*关闭预览元数据*/
        $("[name='close-metadata']").on('click', function () {
            $(".data-wrap tr").each(function () {
                $(this).children(":last").html("")
            })
        });
        $("#editor-pull").click(function () {
            $.ajax({
                url: "${ctx}/sdo/scoresubmit",
                type: 'post',
                data: {
                    sdoId: sdoId,
                    score: starNum,
                    comment: ""
                },
                dataType: 'json',
                success: function (data) {
                    if (data.result == "success") {
                        /*$("[name='text-comments']").val("");*/
                        getScoreList(1);
                    }
                    if (data.result == "fail") {
                        /*$("[name='text-comments']").val("");*/
                        toastr["error"]("请先登录！");
                    }
                }
            });
            starNum = 0;
            /*$("[name='text-comments']").val("");*/
            $("#star1").attr("src", "${ctx}/resources/img/star.png")
            $("#star2").attr("src", "${ctx}/resources/img/star.png")
            $("#star3").attr("src", "${ctx}/resources/img/star.png")
            $("#star4").attr("src", "${ctx}/resources/img/star.png")
            $("#star5").attr("src", "${ctx}/resources/img/star.png")
        });
        <!-- 删除评论 -->
        $("#user-lists").delegate(".delete-user", "click", function () {
            deleteScore($(this).attr("onlyID"));
        });
        function deleteScore(id) {
            bootbox.setLocale("zh_CN");
            bootbox.confirm("确定要删除此条记录吗？", function (r) {
                if (r) {
                    $.ajax({
                        url: ctx + "/sdo/deleteScore/" + id,
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            if (data) {
                                toastr["success"]("删除成功！", "数据删除");
                                getScoreList(1);
                            } else {
                                toastr["error"]("删除失败！", "数据删除");
                            }
                        },
                        error: function () {
                            toastr["error"]("删除失败！", "数据删除");
                        }
                    });
                }
            });
        }
        <!-- byCql-->
        <!-- 收藏数据-start-->
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "1000",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        <!-- 收藏数据-end-->
        function MyMap() {

            this.keys = new Array();
            this.data = new Object();

            this.set = function(key, value) {
                if (this.data[key] == null) {
                    if (this.keys.indexOf(key) == -1) {
                        this.keys.push(key);
                    }
                }
                this.data[key] = value;
            }

            this.get = function(key) {
                return this.data[key];
            }
        }
        /*关联图谱*/
        $().ready(function () {
            $.ajax({
                url: '${ctx}/sdo/relation/page/${id}',
                type: "get",
                dataType: "json",
                success: function (relateList) {
                    if (!relateList || relateList.length == 0) {
                        return;
                    }
                    relationMap(relateList);
                },
                error: function () {
                }
            });
            function cutstr(str, len) {
                var str_length = 0;
                var str_len = 0;
                var str_cut = new String();
                str_len = str.length;
                for (var i = 0; i < str_len; i++) {
                    var a = str.charAt(i);
                    str_length++;
                    if (escape(a).length > 4) {
                        //中文字符的长度经编码之后大于4
                        str_length++;
                    }
                    str_cut = str_cut.concat(a);
                    if (str_length >= len) {
                        str_cut = str_cut.concat("..");
                        return str_cut;
                    }
                }

                //如果给定字符串小于指定长度，则返回源字符串；
                if (str_length < len) {
                    return str;
                }
            }


            function getShortName(title) {
                if (!title) {
                    return "";
                }
                return cutstr(title, 8);

            }

            function relationMap(relateList) {
                var dataList = [];
                var linkList = [];
                var dom = document.getElementById("relateSdoId");
                var myChart = echarts.init(dom);
                var option = {
                    title: {
//                text: 'Graph 简单示例'
                    },
                    tooltip: {
                        formatter: function (x) {
                            return x.data.des ? x.data.des : x.data.score.toFixed(3);
                        }
                    },
                    animationDurationUpdate: 400,
                    animationEasingUpdate: 'quinticInOut',
                    series: [
                        {
                            type: 'graph',
                            layout: 'force',
                            symbolSize: 50,
                            roam: true,
                            // edgeSymbol: ['circle', 'arrow'],
                            edgeSymbolSize: [10, 10],
                            edgeLabel: {
                                normal: {
                                    textStyle: {
                                        fontSize: 15
                                    }
                                }
                            },
                            force: {
                                repulsion: 120,
                                edgeLength: [60, 70]
                            },
                            draggable: true,
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true//如果设置为true，节点文字则一直显示
                                    },
                                    color: '#3FA7DC'
                                }
                            },
                            lineStyle: {
                                normal: {
                                    width: 2,
                                    color: '#4b565b'
                                }
                            },
                            data: dataList,
                            links: linkList
                        }
                    ]
                };
                var itemColor = ["", "", "#AED6F1", "#85C1E9", "#5DADE2", "#3498DB", "#2E86C1", "#2874A6", "#21618C", "#1B4F72"];
                var curSdoName = getShortName('${sdo.title}');
                var curSdo = {
                    name: curSdoName, des: '${sdo.title}', id: 0, sdoId: '${id}'
                };//symbolSize: 80
                var dataMap = new MyMap();
                var indexId = 1;
                dataMap.set('${id}', curSdo);
                dataList.push(curSdo);
                if ((!Array.isArray(relateList)) || relateList.length == 0) {
                    return;
                }
                for (var i = 0; i < relateList.length; i++) {
                    var cur = relateList[i];
                    var curName = getShortName(cur.sdoTitle);
                    var curRelate = {
                        name: curName, des: cur.sdoTitle, id: indexId, sdoId: cur.sdoId,
                        itemStyle: {
                            normal: {
                                color: itemColor[parseInt(cur.score * 10) % itemColor.length] //"#00FFFF"
                            }
                        }
                    };
                    dataList.push(curRelate);
                    dataMap.set(cur.sdoId, curRelate);
//                    var curveness = Math.random() / 5;
//                    curveness = Math.random() > 0.5 ? (-curveness) : curveness;
                    var curLink = {
                        source: 0,
                        target: indexId++,
                        value: 1,
                        score: cur.score,
                        label: {
                            normal: {
                                show: true,
                                formatter: cur.score.toFixed(3) + '',
                                fontSize: 9
                            }
                        },
                        lineStyle: {
//                            normal: {curveness: curveness}//曲线连接线
                        }
                    };
                    linkList.push(curLink);
                }
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                    myChart.on('click', clickEvent);
                }
                function clickEvent(handler) {
                    if (handler.dataType != 'node' || handler.data.id == 0) {
                        return;
                    }
                    //获取节点点击的数组序号
//                        var arrayIndex = handler.dataIndex;
                    //获取数据
//                        var urlParam = handler.data.name;
                    window.open('${ctx}/sdo/detail/' + handler.data.sdoId);
                    return;
                }
            }
        });

    </script>
</div>
</html>
