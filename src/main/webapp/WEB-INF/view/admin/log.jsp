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
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>内容采编</title>
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css">
    <%--<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/log.css">--%>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/datepicker.css">
    <style>
        input{box-sizing: border-box;}
        .sidebar .active{
            color: whitesmoke!important;
        }
        .sidebar .active a{
            color: whitesmoke!important;
        }
        .ri-list{
            background: #FFF;
            border-left: #FFF 12px solid;
            padding-left: 20px;
            padding-right: 20px;
        }
        .tospan{
            display: inline-block;
            padding: 6px 12px;
            font-size: 14px;
            font-weight: 400;
            line-height: 1;
            color: #555;
            text-align: center;
            background-color: #eee;
            border: 1px solid #ccc;
            white-space: nowrap;
            vertical-align: middle
        }
        td{
            text-align: center;
        }
    </style>
</head>
<body>
<div class="row margin-bottom-40" style="min-height: 500px;margin-right: 15px;" >
    <div class="sidebar col-md-2 col-sm-2" >
        <ul class="list-group margin-bottom-25 sidebar-menu">
            <li class="list-group-item clearfix "><a href="${ctx}/tag"><i
                    class="fa fa-angle-right "></i>标签审核</a></li>
            <li class="list-group-item clearfix "><a href="${ctx}/collection/admin/category"><i
                    class="fa fa-angle-right "></i>栏目管理 </a></li>
            <li class="list-group-item clearfix"><a href="${ctx}/collection/"><i
                    class="fa fa-angle-right"></i>内容采编 </a></li>
            <li class="list-group-item clearfix active" style="color: rgba(25, 158, 216, 1)"><a href="${ctx}/admin/log/"><i
                    class="fa fa-angle-right" ></i>日志列表 </a></li>
        </ul>
    </div>
    <div class="col-md-9 col-sm-8 ri-list" >
        <ul class="breadcrumb">
            <li><a href="${ctx}/">首页</a></li>
            <li><a href="${ctx}/collection/">管理中心</a></li>
            <li >日志审核</li>
        </ul>
        <div class="page-content col-md-12" style="margin-bottom: 17px">
            <h3>日志内容列表</h3>
        </div>
        <div class="row"></div>
        <div style="padding: 0 12px">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#browse-log" aria-controls="browse-log" role="tab" data-toggle="tab">浏览日志</a></li>
                <li role="presentation"><a href="#search-log" aria-controls="search-log" role="tab" data-toggle="tab">搜索日志</a></li>
                <li role="presentation"><a href="#collection-log" aria-controls="collection-log" role="tab" data-toggle="tab">收藏日志</a></li>
                <li role="presentation"><a href="#download-log" aria-controls="download-log" role="tab" data-toggle="tab">下载日志</a></li>
            </ul>
            <!-- Tab panes -->
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="browse-log">
                    <div >
                        <div style="margin: 10px 0px">
                            <form class="form-inline">
                                <label>筛选条件：</label>
                                <div class="form-group">
                                    <label class="sr-only">用户名</label>
                                    <input class='form-control'  placeholder='用户名' value="" id="browse-loginID">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">标题</label>
                                    <input class="form-control" id="browse-title" placeholder="标题" value="">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">时间</label>
                                    <input type='text' class='form-control selectData' id="browse-inputDate-start"
                                             data-date-format="yyyy-mm-dd" placeholder="开始时间" readonly  >
                                    <span class="tospan">to</span>
                                    <input type='text' class='form-control selectData' id="browse-inputDate-end"
                                            data-date-format="yyyy-mm-dd" placeholder="结束时间" readonly  >
                                </div>
                                <input type="button"  class="btn btn-primary" id="browse-btn" value="筛选"></input>
                            </form>
                        </div>
                        <div style="background: grey;">
                                <div class="caption">
                                    <i class="icon-cogs"></i>
                                    <font color="white">浏览日志</font>
                                </div>
                            </div>
                        <table class="table table-hover table-bordered">
                            <thead>
                                <tr>
                                    <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">用户名</th>
                                    <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">标题</th>
                                    <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">时间</th>
                                </tr>
                            </thead>
                            <tbody id="browse-tab">
                            <div class="col-md-12">
                                <div id="myTabContent" class="tab-content">
                                    <div id="col_list">正在加载数据......</div>
                                </div>
                            </div>
                            </tbody>
                        </table>
                        <div class="page-list" style="float: right" id="browse-page-list"></div>
                        <div class="page-span" style="height: 74px;line-height: 74px"></div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="search-log">
                    <div >
                        <div style="margin: 10px 0px" >
                            <form class="form-inline">
                                <label>筛选条件：</label>
                                <div class="form-group">
                                    <label class="sr-only">用户名</label>
                                    <input class='form-control'  placeholder='loginID' value="" id="search-loginID">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">关键词</label>
                                    <input class="form-control" id="search-title" placeholder="关键词" value="">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">时间</label>
                                    <input type='text' class='form-control selectData' id="search-inputDate-start"
                                           data-date-format="yyyy-mm-dd" placeholder="起始时间" readonly>
                                    <span class="tospan">to</span>
                                    <input type='text' class='form-control selectData'  id="search-inputDate-end"
                                           data-date-format="yyyy-mm-dd" placeholder="结束时间" readonly>
                                </div>
                                <input type="button"  class="btn btn-primary" id="search-btn" value="筛选"></input>
                            </form>
                        </div>
                        <div style="background: grey;">
                            <div class="caption">
                                <i class="icon-cogs"></i>
                                <font color="white">搜索日志</font>
                            </div>
                        </div>
                        <table class="table table-hover table-bordered">
                            <thead>
                                <tr>
                                    <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">用户名</th>
                                    <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">关键词</th>
                                    <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">时间</th>
                                </tr>
                            </thead>
                            <tbody id="search-tab">

                            </tbody>
                        </table>
                        <div class="page-list" style="float: right" id="search-page-list"></div>
                        <div class="page-span" style="height: 74px;line-height: 74px"></div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="collection-log">
                    <div>
                        <div  style="margin: 10px 0px">
                            <form class="form-inline">
                                <label>筛选条件：</label>
                                <div class="form-group">
                                    <label class="sr-only">用户名</label>
                                    <input class='form-control'  placeholder='loginID' value="" id="collection-loginID">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">标题</label>
                                    <input class="form-control" id="collection-title" placeholder="名称" value="">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">时间</label>
                                    <input type='text' class='form-control selectData' id="collection-inputDate-start"
                                           data-date-format="yyyy-mm-dd" placeholder="起始时间" readonly>
                                    <span class="tospan">to</span>
                                    <input type='text' class='form-control selectData' id="collection-inputDate-end"
                                           data-date-format="yyyy-mm-dd" placeholder="结束时间" readonly>
                                </div>
                                <input type="button"  class="btn btn-primary" id="collection-btn" value="筛选"></input>

                            </form>
                        </div>
                        <div style="background: grey;">
                            <div class="caption">
                                <i class="icon-cogs"></i>
                                <font color="white">收藏日志</font>
                            </div>
                        </div>
                        <table class="table table-hover table-bordered">
                            <thead>
                            <tr>
                                <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">用户名</th>
                                <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">标题</th>
                                <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">时间</th>
                            </tr>
                            </thead>
                            <tbody id="collection-tab">

                            </tbody>
                        </table>
                        <div class="page-list" style="float: right" id="collection-page-list"></div>
                        <div class="page-span" style="height: 74px;line-height: 74px"></div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane" id="download-log">
                    <div >
                        <div style="margin: 10px 0px">
                            <form class="form-inline">
                                <label>筛选条件：</label>
                                <div class="form-group">
                                    <label class="sr-only">用户名</label>
                                    <input class='form-control'  placeholder='loginID' value="" id="download-loginID">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">标题</label>
                                    <input class="form-control" id="download-title" placeholder="名称" value="">
                                </div>
                                <div class="form-group">
                                    <label class="sr-only">时间</label>
                                    <input type='text' class='form-control selectData' id="download-inputDate-start"
                                           data-date-format="yyyy-mm-dd" placeholder="起始时间" readonly>
                                    <span class="tospan">to</span>
                                    <input type='text' class='form-control selectData' id="download-inputDate-end"
                                           data-date-format="yyyy-mm-dd" placeholder="结束时间" readonly>
                                </div>
                                <input type="button"  class="btn btn-primary" id="download-btn" value="筛选"></input>
                            </form>
                        </div>
                        <div style="background: grey;">
                            <div class="caption">
                                <i class="icon-cogs"></i>
                                <font color="white">下载日志</font>
                            </div>
                        </div>
                        <table class="table table-hover table-bordered">
                            <thead>
                            <tr>
                                <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">用户名</th>
                                <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">标题</th>
                                <th style= "background: #a6c8e6;color: #FFF;font-weight: bold;text-align: center">时间</th>
                            </tr>
                            </thead>
                            <tbody id="download-tab">

                            </tbody>
                        </table>
                        <div class="page-list" style="float: right" id="download-page-list"></div>
                        <div class="page-span" style="height: 74px;line-height: 74px"></div>
                    </div>
                </div>
            </div>
        </div>


    </div>
</div>
</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/js/bootstrap-datepicker.js" ></script>
    <script src="${ctx}/resources/js/dateSetTime.js" ></script>
    <script type="text/javascript">
        var ctx = '${ctx}';
        var SubId="";
        var activeIndex=0;
        var url ="";
        var data={};
        var dataSh={};

        $().ready(function () {
            initData();

        });

        $("#download-inputDate-start").bind('input propertychange', function() {
            alert("aaaa");
        });

        $('.selectData').datepicker();
        <!-- 切换tab页-->
        $("[role='presentation']").on("click",function () {
            var num = $(this).index()
            if(activeIndex == num){
                return;
            }
            if($(this).index() ==0){
                url="${ctx}/admin/log/getSdoVisit";
                data.loginId =$("#browse-loginID").val() ;
                data.title = $("#browse-title").val();
                /*data.beginTime = "2010-05-16";*/
                data.beginTime = $("#browse-inputDate-start").val();
                data.endTime = $("#browse-inputDate-end").val();
                requestLists(1,url,data,0);
                activeIndex =0;
            }else if($(this).index() ==1){
                url="${ctx}/admin/log/getSdoSearch";
                dataSh.loginId =$("#search-loginID").val() ;
                dataSh.keyword = $("#search-title").val();
                data.beginTime = $("#search-inputDate-start").val();
                data.endTime = $("#search-inputDate-end").val();
                requestLists(1,url,dataSh,1);
                activeIndex =1;
            }else if($(this).index() ==2){
                url="${ctx}/admin/log/getSdoFavorites";
                data.loginId =$("#collection-loginID").val() ;
                data.title = $("#collection-title").val();
                data.beginTime = $("#collection-inputDate-start").val();

                data.endTime = $("#collection-inputDate-end").val();
                requestLists(1,url,data,2);
                activeIndex =2;
            }else {
                url="${ctx}/admin/log/getSdoDownload";
                data.loginId =$("#download-loginID").val() ;
                data.title = $("#download-title").val();
                data.beginTime = $("#download-inputDate-start").val();
                data.endTime = $("#download-inputDate-end").val();
                requestLists(1,url,data,3);
                activeIndex =3;
            }

        })


        <!-- 四个筛选按钮-->
        $("#browse-btn").click(function () {
            url="${ctx}/admin/log/getSdoVisit";
            data.loginId =$("#browse-loginID").val() ;
            data.title = $("#browse-title").val();
            data.beginTime = $("#browse-inputDate-start").val();
            data.endTime = $("#browse-inputDate-end").val();
            /*data.endTime = "2018-05-16";*/
            requestLists(1,url,data,0);
        })
        $("#search-btn").click(function () {
            url="${ctx}/admin/log/getSdoSearch";
            dataSh.loginId =$("#search-loginID").val() ;
            dataSh.keyword = $("#search-title").val();
            dataSh.beginTime = $("#search-inputDate-start").val();
            dataSh.endTime = $("#search-inputDate-end").val();
            requestLists(1,url,dataSh,1);
        })
        $("#collection-btn").click(function () {
            url="${ctx}/admin/log/getSdoFavorites";
            data.loginId =$("#collection-loginID").val() ;
            data.title = $("#collection-title").val();
            data.beginTime = $("#collection-inputDate-start").val();
            data.endTime = $("#collection-inputDate-end").val();
            requestLists(1,url,data,2);
        })
        $("#download-btn").click(function () {
            url="${ctx}/admin/log/getSdoDownload";
            data.loginId =$("#download-loginID").val() ;
            data.title = $("#download-title").val();
            data.beginTime = $("#download-inputDate-start").val();
            data.endTime = $("#download-inputDate-end").val();
            requestLists(1,url,data,3);
        })
        <!-- 初始化页面加载-->
        function initData() {
            url="${ctx}/admin/log/getSdoVisit";
            data.loginId =$("#browse-loginID").val() ;
            data.title = $("#browse-title").val();
            data.beginTime = $("#browse-inputDate-start").val();
            data.endTime = $("#browse-inputDate-end").val();
            requestLists(1,url,data,0);

        }
        <!-- 四个tag的页面请求-->
        function requestLists(pageNum,url,data,nameNum) {
            data.pageNum = pageNum;
            var conData = data;
            var conurl =url;
            $.ajax({
                url:url,
                type:"GET",
                data: data,
                success:function (data) {
                    var dataSdo = JSON.parse(data);
                    var href="";
                    $("#col_list").hide();
                    switch (nameNum){
                        case 0:
                            $("#browse-tab").empty();
                            if(data =="{}" ||dataSdo.result.length ==0){
                                $('.page-list').empty();
                                $('.page-span').empty();
                                $("#browse-tab").append("<tr><td colspan='3'>暂时没有数据</td></tr>")
                                return
                            }
                            var str="";
                            for(var i =0;i<dataSdo.result.length;i++){
                                href = "${ctx}/sdo/detail/"+dataSdo.result[i].sdoId;
                                str+= "<tr> <td>"+ dataSdo.result[i].loginId+"</td> <td><a href="+href +">"+dataSdo.result[i].title +"</a></td>"+
                                    "<td>"+  convertMilsToDateString(dataSdo.result[i].createTime)+"</td> </tr>"
                            }
                            $("#browse-tab").append(str);
                            break;
                        case 1:
                            $("#search-tab").empty();
                            if(data =="{}" ||dataSdo.result.length ==0){
                                $('.page-list').empty();
                                $('.page-span').empty();
                                $("#search-tab").append("<tr><td colspan='3'>暂时没有数据</td></tr>")
                                return
                            }
                            var str="";
                            for(var i =0;i<dataSdo.result.length;i++){
                                str+= "<tr> <td>"+ dataSdo.result[i].loginId+"</td> <td>"+dataSdo.result[i].keyword +"</td>"+
                                    "<td>"+  convertMilsToDateString(dataSdo.result[i].createTime)+"</td> </tr>"
                            }
                            $("#search-tab").append(str);
                            break;
                        case 2:
                            $("#collection-tab").empty();
                            if(data =="{}" ||dataSdo.result.length ==0){
                                $('.page-list').empty();
                                $('.page-span').empty();
                                $("#collection-tab").append("<tr><td colspan='3'>暂时没有数据</td></tr>")
                                return
                            }
                            var str="";
                            for(var i =0;i<dataSdo.result.length;i++){
                                href = "${ctx}/sdo/detail/"+dataSdo.result[i].sdoId;
                                str+= "<tr> <td>"+ dataSdo.result[i].loginId+"</td> <td><a href="+href +">"+dataSdo.result[i].title +"</a></td>"+
                                    "<td>"+  convertMilsToDateString(dataSdo.result[i].updateTime)+"</td> </tr>"
                            }
                            $("#collection-tab").append(str);
                            break;
                        case 3:
                            $("#download-tab").empty();
                            if(data =="{}" ||dataSdo.result.length ==0){
                                $('.page-list').empty();
                                $('.page-span').empty();
                                $("#download-tab").append("<tr><td colspan='3'>暂时没有数据</td></tr>")
                                return
                            }
                            var str="";
                            for(var i =0;i<dataSdo.result.length;i++){
                                href = "${ctx}/sdo/detail/"+dataSdo.result[i].sdoId;
                                str+= "<tr> <td>"+ dataSdo.result[i].loginId+"</td> <td><a href="+ href+">"+dataSdo.result[i].title +"</a></td>"+
                                    "<td>"+  convertMilsToDateString(dataSdo.result[i].createTime)+"</td> </tr>"
                            }
                            $("#download-tab").append(str);
                            break;
                    }

                    /*table分页请求*/
                    if ($(".page-list .bootpag").length != 0) {
                        $(".page-list").off();
                        $('.page-list').empty();
                        $(".page-span").empty();
                    }
                    $(".page-span")[nameNum].append("当前第"+dataSdo.pageNum +"页,共"+dataSdo.totalPage +"页,"+dataSdo.totalCount+"条数据")
                    var $ele;
                    switch (nameNum){
                        case 0:
                            $ele= $("#browse-page-list")
                            break;
                        case 1:
                            $ele= $("#search-page-list")
                            break;
                        case 2:
                            $ele =$("#collection-page-list")
                            break;
                        case 3:
                            $ele =$("#download-page-list")
                            break;
                    }
                    $ele.bootpag({
                        total: dataSdo.totalPage,
                        page: dataSdo.pageNum,
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
                        requestLists(num,conurl,conData,nameNum);
                    });
                },error:function () {
                    alert("服务器请求失败");
                }
            })
        }
        <!-- 时间转化-->
        function convertMilsToDateString(mil) {
            var date = new Date(mil);
            return date.Format("yyyy-MM-dd hh:mm:ss");
        }


    </script>

</div>