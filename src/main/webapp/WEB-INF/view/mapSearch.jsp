<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/5/2
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>欢迎访问CASEARTH广目系统</title>
    <META HTTP-EQUIV="pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <META HTTP-EQUIV="expires" CONTENT="0">
    <link href="${ctx}/resources/bundles/nouislider/nouislider.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/css/newMapSearch.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button{
            -webkit-appearance: none !important;
            margin: 0;
        }
        input[type="number"]{-moz-appearance:textfield;}
    </style>
</head>
<body>
<div id="container"></div>
<img id="showPanel" class="pulldown" src="${ctx}/resources/img/pulldown.png" alt="">
<div id="searchbar">
    <input type="text" name="searchText" id="keyword"/>
    <button id="sdoSearch">搜索</button>
    <img id="hiddenPanel" src="${ctx}/resources/img/pullup.png" alt="">
</div>
<div id="content">
    <div class="content-main">
        <div class="content_range">
            <span>内容范围</span>
            <select id="fileType" name="fileType" style="width: 164px;margin-right: 55px"></select>
            <select id="publisher" name="fileType" style="margin-right: 40px"></select>
        </div>
        <div class="coordinate-wrap">
            <div class="coordinate-tit">坐标</div>
            <div class="coordinate">
                <span>最小经度</span><input type="text" id="x1" value="0" name="coordinates"/>
                <span>最小纬度</span><input type="text" id="y1" value="0" name="coordinates"/>
                <span>最大经度</span><input type="text" id="x2" value="0" name="coordinates"/>
                <span>最大纬度</span><input type="text" id="y2" value="0" name="coordinates"/>
            </div>
        </div>
        <div>
            <span class="time_range">时间范围</span>
            <div>
                <input id="tr_starttime" type="number" >
                <div id="timeSlider" style="width:290px;"></div>
                <input id="tr_endtime" type="number" >
                <div class="clear"></div>
            </div>
        </div>
    </div>
    <div class="btn-wrap" id="search-list-one" style="display: none">
        <button class="btn-default-wrap map-active" role="presentation" id="data-one">数据集</button>
        <button class="btn-default-wrap" role="presentation" id="data-two">文件</button>
        <div class="clear"></div>
    </div>
    <div class="map-conwrap" id="search-list-two" style="display: none">
        <div class="search-data"  name="search-con">
            <div class="con-default one" id="file-list-sdo">
                <%--<div class="con-one">
                    <span class="num">1</span>
                    <img class="map-con-img" src="${ctx}/resources/img/pro-img.jpg">
                    <div class="map-con-right">
                        <h3>数据集</h3>
                        <div class="data-con">数含有...</div>
                        <div class="collection-mess">
                            <span>李国庆数据中心</span>
                            <span>2018-01-08</span>
                        </div>
                        <div class="files-type">
                            <span class="file-type-one">CSV</span>
                            <span class="file-type-two">GeoJSON</span>
                            <span class="file-type-three">SHP</span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>--%>
            </div>
            <div id="sdo-page-list" style="float: right"></div>
            <div id="sdo-page-span" style="height: 74px;line-height: 74px"></div>
        </div>
        <div class="search-file"  name="search-con" style="display: none;">
            <div class="con-default two" id="file-list-file">
                <%--<div class='con-one'>
                    <span class='num'>1</span>
                    <div class='map-con-list-right'>
                        <h3>filename</h3>
                        <div class="content-see">
                            <ul>
                                <li>查看</li>
                                <li>|</li>
                                <li>下载</li>
                            </ul>
                        </div>
                        <div class="collection-mess">
                            <span class="file-type-one">filetype</span>
                            <span>publisher</span>
                            <span>updatetime</span>
                            <span>45M</span>
                            <span class="datas-name">来源<a href="sdotitle">sdotitle</a></span>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>--%>
            </div>
            <div id="file-page-list" style="float: right"></div>
            <div id="file-page-span" style="height: 74px;line-height: 74px"></div>
        </div >
    </div>
</div>
<div id="right-panel">
    <div id="rectangular" style="height: 50px;">
        <img src="${ctx}/resources/img/rect_icon.png" class="activeImg">
        <img src="${ctx}/resources/img/rect_icon_active.png" >
    </div>
</div>
<div class="modal fade  " id="myModalThree" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header" style="background-color: #337ab7">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close" name="close-metadata"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabelThree">图片预览</h4>
            </div>
            <div class="modal-body" id="img-pos">
                <img src=""  id="imgcontent" style="width: 100%">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"
                        name="close-metadata">关闭
                </button>
            </div>
        </div>
    </div>
</div>
</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/nouislider/nouislider.min.js"></script>
    <script src="${ctx}/resources/js/subStrLength.js"></script>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.6&dev-Map=74dbb914c763808d47998ac6e45d706a&plugin=AMap.MouseTool"></script>
    <script src="//webapi.amap.com/ui/1.0/main.js?v=1.4.6&dev-Map=74dbb914c763808d47998ac6e45d706a"></script>
    <script src="http://webapi.amap.com/ui/1.0/ui/overlay/SimpleMarker.js?v=1.4.6&dev-Map=74dbb914c763808d47998ac6e45d706a"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/js/mapSearch.js"></script>
    <script>

        var data={
            keyword:"",
            fileType:"",
            publisher:"",
            x1:0,
            x2:0,
            y1:0,
            y2:0,
            beginTime:1905,
            endTime:2020,
        }
        var conListDraw=[];
        var conList =[];
        var Exp = /^(-|\+)?[0-9]+.?[0-9]*$/;
        var showDataNum = 0;
        var showHideList= 0;
        $("#first").removeClass("active");
        $("#second").removeClass("active");
        $("#third").addClass("active");
     /*   $('#hiddenPanel').click(function() {
            $("#searchbar").css("left","-830px");
            $("#content").css("left","-726px");
        });
        $('#showPanel').click(function() {
            $("#searchbar").css("left","100px");
            $("#content").css("left","100px");
        });*/
        $("#data-one").click(function(){
            $(".search-data").css("display","block");
            $(".search-file").css("display","none");
            $("#data-one").addClass("map-active");
            $("#data-two").removeClass("map-active");
            showDataNum =0;
            updateDate();
            sdoFun(1,data);
        })
        $("#data-two").click(function(){
            $(".search-data").css("display","none");
            $(".search-file").css("display","block");
            $("#data-two").addClass("map-active");
            $("#data-one").removeClass("map-active");
            showDataNum=1;
            updateDate();
            FileInfoFun(1,data)
        })
        $("#sdoSearch").on("click",function () {
            $("#search-list-one").show();
            $("#search-list-two").show();
            showHideList = 1;
            var className = $("[name='search-con']:hidden").attr("class");
            if(className == "search-file"){
                updateDate();
                sdoFun(1,data)
            }else {
                updateDate();
                FileInfoFun(1,data)
            }

        })
        $("#rectangular").on("click",function () {
            if($(this).children(".activeImg").index() == 1){
                $("#x1").val("0")
                $("#x2").val("0")
                $("#y1").val("0")
                $("#y2").val("0")
                $(this).children().first().addClass("activeImg");
                $(this).children().last().removeClass("activeImg");
                mainMap.clearMap();
                sendGetDataReq();
                return
            }
            $(this).children().first().removeClass("activeImg")
            $(this).children().last().addClass("activeImg")
            mainMap.clearMap();
            mouseTools.rectangle()
        })

        $(function () {
            initFun();
        })
        function initFun(){
            $.ajax({
                url:"${ctx}/mapSearch/getInitData",
                type:"GET",
                success:function (data) {
                    var data =JSON.parse(data);
                    var dataTypeStr ="<option value='文件类型' selected>文件类型</option>"
                    var publisherStr="<option value='数据提供者' selected>数据提供者</option>"
                    for(var i=0;i<data.dataType.length;i++){
                        dataTypeStr+="<option value="+data.dataType[i] +">"+data.dataType[i] +"</option>"
                    }
                    for (var i=0;i<data.publisher.length;i++){
                        publisherStr+="<option value="+data.publisher[i] +">"+data.publisher[i] +"</option>"
                    }
                    $("#fileType").append(dataTypeStr);
                    $("#publisher").append(publisherStr);
                    slider.noUiSlider.on('change.one', function(values, handle, unencoded, tap, positions) {
                        var startTime = parseInt(values[0]);
                        var endTime = parseInt(values[1]);
                        TBstartTime.value = startTime;
                        TBendTime.value = endTime;
                        if(showHideList == 0){
                            sendGetDataReq();
                            showHideList =1;
                            return
                        }
                        sendGetDataReq();
                    });
                    TBstartTime.onblur = function() {
                        if(showHideList == 0){
                            changeTime(true);

                            sendGetDataReq();
                            showHideList =1;
                            return
                        }
                        if (TBstartTime.value > TBendTime.value) {
                            TBstartTime.value = TBendTime.value;
                        }
                        changeTime(true);
                        sendGetDataReq();
                    }
                    TBendTime.onblur = function() {
                        if(showHideList == 0){
                            changeTime(false);
                            sendGetDataReq();
                            showHideList =1;
                            return
                        }
                        changeTime(false);
                        sendGetDataReq();
                    }
                    $("[name='fileType']").on("change",function () {
                        if(showHideList == 0){
                            sendGetDataReq();
                            showHideList =1;
                            return
                        }
                        sendGetDataReq();
                    });
                    $("[name='coordinates']").on("focusout",function () {
                        if(showHideList == 0){
                            sendGetDataReq();
                            showHideList =1;
                            return
                        }
                        sendGetDataReq();
                    });
                    $("#keyword").bind({
                        focusout:function () {
                            if(showHideList == 0){
                                sendGetDataReq();
                                showHideList =1;
                                return
                            }
                            sendGetDataReq();
                        },
                        keypress:function (ev) {
                            var event = ev || window.event;
                            if(showHideList == 0){
                                if (event.keyCode == 13){
                                    sendGetDataReq();
                                }
                                showHideList =1;
                                return
                            }
                            if (event.keyCode == 13){
                                sendGetDataReq();
                            }
                        }
                    })

                }
            })
        }
        function updateDate() {
            data.keyword = $("#keyword").val();
            if($("#fileType").val() == "文件类型"){
                data.fileType= "";
            }else {
                data.fileType= $("#fileType").val();
            }
            if($("#publisher").val() == "数据提供者"){
                data.publisher= "";
            }else {
                data.publisher= $("#publisher").val();
            }
            if(!Exp.test($("#x1").val()) || $("#x1").val() == ""){
                $("#x1").val(0)
            }
            if (!Exp.test($("#x2").val()) ||$("#x2").val() == ""){
                $("#x2").val(0)
            }
            if(!Exp.test($("#y1").val()) || $("#y1").val() == ""){
                $("#y1").val(0)
            }if(!Exp.test($("#y2").val()) || $("#y2").val() == ""){
                $("#y2").val(0)
            }

            data.x1= parseFloat($("#x1").val());
            data.x2= parseFloat($("#x2").val());
            data.y1= parseFloat($("#y1").val());
            data.y2= parseFloat($("#y2").val());

            var H=$("#tr_starttime").val();
            var D= $("#tr_endtime").val();

            data.beginTime= H;
            data.endTime= D;
        }
        function sendGetDataReq() {
            if(showHideList == 0){
                $("#search-list-one").show();
                $("#search-list-two").show();
                updateDate();
                sdoFun(1,data)
                showHideList == 1;
            }else{
                if(showDataNum ==0){
                    updateDate();
                    sdoFun(1,data)
                }else {
                    updateDate();
                    FileInfoFun(1,data)
                }
            }
        }
        function isDrawRect() {
            var x1= parseFloat($("#x1").val());
            var x2= parseFloat($("#x2").val());
            var y1= parseFloat($("#y1").val());
            var y2= parseFloat($("#y2").val());
            var num = x1+x2+y1+y2;
            if(!num == 0 && x2>x1 && y2>y1 ){
                var southWest = new AMap.LngLat(x1, y1)
                var northEast = new AMap.LngLat(x2, y2)
                var bounds = new AMap.Bounds(southWest, northEast);
                new AMap.Rectangle({
                    map:mainMap,
                    bounds: bounds,
                    strokeColor:'#1779cc',
                    strokeOpacity:1,
                    strokeWeight:0,
                    fillColor:'#1779cc',
                    fillOpacity:0.5,
                    zIndex:10,
                    bubble:true,
                    cursor:'pointer',
                });

                var numMax =  Math.max((x2 - x1),(y2 - y1));
                numMax = Math.ceil(numMax*10);
                if(numMax == 0){
                    mainMap.setCenter([(x1+x2)/2,(y1+y2)/2]);
                    mainMap.setZoom(14);
                }else {
                    var numZoom = 12.5 - (numMax * 0.3);
                    if(numZoom <= 3){
                        numZoom=3
                    }
                    mainMap.setCenter([(x1+x2)/2,(y1+y2)/2]);
                    mainMap.setZoom(numZoom);
                }
            }else {
                mainMap.clearMap();
            }
        }
        <!-- 页面数据集请求-->
        function sdoFun(num ,data){
            data.pageNum=num;
            var conData = data;
            $.ajax({
                url:"${ctx}/mapSearch/getSdoBy",
                type:"GET",
                data: conData,
                success:function (data) {
                    $("#file-list-sdo").empty();
                    mainMap.clearMap();
                    isDrawRect();
                    var dataSdo = JSON.parse(data);
                    if(data =="{}" ||dataSdo.sdo.length ==0){
                        $('#sdo-page-list').empty();
                        $('#sdo-page-span').empty();
                        $("#file-list-sdo").append("<h1 >暂时没有数据</h1>")
                        return
                    }
                    var neamStr = "";
                    var sdolist = dataSdo.sdo;
                    var imgHref=""
                    var imgSrc = ""
                    var nameHref=""
                    for(var i=0;i<sdolist.length;i++){
                        imgHref="${ctx}/sdo/detail/"+sdolist[i].sdoid;
                        imgSrc="${ctx}"+sdolist[i].img;
                        nameHref=imgHref
                        neamStr+="<div class='con-one' name='file-sdo' > <span class='num'>"+ (i+1)+"</span> <img class='map-con-img' src="+ imgSrc+">"+
                            "<div class='map-con-right'> <h3><a href="+nameHref +">"+sdolist[i].title+"</a></h3> <div class='data-con'>"+ getSubStr(sdolist[i].desc,150)+"</div>"+
                        "<div class='collection-mess'><span>"+sdolist[i].publisher+"</span> <span>"+ sdolist[i].publisherPublishTime+"</span> </div> <div class='files-type'>"+
                            "<span class='file-type-one'>"+sdolist[i].dataFormat+"</span> </div> </div> <div class='clear'></div> </div>"
                    }
                    $("#file-list-sdo").append(neamStr);
                    var rect;
                    var infoWindow = new AMap.InfoWindow();
                    conListDraw.length = 0;
                    conList.length=0;
                    conList = $("[name='file-sdo']");
                    for(var i=0;i<sdolist.length;i++){
                        imgHref="${ctx}/sdo/detail/"+sdolist[i].sdoid;
                        imgSrc="${ctx}"+sdolist[i].img;
                        nameHref=imgHref;
                        if(sdolist[i].center !== null){
                            var southWest = new AMap.LngLat(sdolist[i].lowLeft[0], sdolist[i].lowLeft[1])
                            var northEast = new AMap.LngLat(sdolist[i].upRight[0], sdolist[i].upRight[1])
                            var bounds = new AMap.Bounds(southWest, northEast);
                            rect =  new AMap.Rectangle({
                                map:mainMap,
                                bounds: bounds,
                                strokeColor:'blue',
                                strokeOpacity:0.5,
                                strokeWeight:0,
                                fillColor:'blue',
                                fillOpacity:0,
                                zIndex:10,
                                bubble:true,
                                cursor:'pointer',
                            });
                            rect.content="<div class='con-one'  style='width:660px'> <span class='num'>"+(i+1)+"</span> <img class='map-con-img' src="+ imgSrc+">"+
                                "<div class='map-con-right'> <h3><a href="+nameHref +">"+sdolist[i].title+"</a></h3> <div class='data-con'>"+ getSubStr(sdolist[i].desc,150)+"</div>"+
                                "<div class='collection-mess'><span>"+sdolist[i].publisher+"</span> <span>"+ sdolist[i].publisherPublishTime+"</span> </div> <div class='files-type'>"+
                                "<span class='file-type-one'>"+sdolist[i].dataFormat+"</span> </div> </div> <div class='clear'></div> </div>"
                            rect.drawId=i;
                            rect.drawActive=false;
                            rect.center=sdolist[i].center;
                            rect.numMax =Math.ceil(Math.max((sdolist[i].lowRight[0] - sdolist[i].lowLeft[0]),(sdolist[i].upLeft[1] - sdolist[i].lowLeft[1])) * 10);
                            conListDraw.push(rect);
                            rect.on('click', markerClick);
                        }
                    }
                    function markerClick(e){
                        if(!this.drawActive){
                            $(conList[this.drawId]).addClass("file-list-con-select")
                            this.setOptions({
                                fillOpacity:0.5,
                                fillColor:'red'
                            });
                            this.drawActive=true
                        }else {
                            $(conList[this.drawId]).removeClass("file-list-con-select")
                            this.setOptions({
                                fillOpacity:0,
                                fillColor:'blue'
                            });
                            this.drawActive=false;
                        }
                        infoWindow.setContent(e.target.content);
                        infoWindow.open(mainMap, e.lnglat);

                    }
                    $("[name='file-sdo']").hover(
                        function () {
                            $(this).addClass("con-hover");
                        },
                        function () {
                            $(this).removeClass("con-hover");
                        }
                    );
                    $("[name='file-sdo']").click(function () {
                        for(var i=0;i<conListDraw.length;i++){
                            if($(this).index() ==conListDraw[i].drawId ){
                                if($(this).hasClass("file-list-con-select")){
                                    conListDraw[i].setOptions({
                                        fillColor:'blue',
                                        fillOpacity:0,
                                    });
                                    if(conListDraw[i].numMax == 0){
                                        mainMap.setZoom(14);
                                    }else {
                                        var numZoom = 12.5 - (conListDraw[i].numMax * 0.3);
                                        if(numZoom <= 3){
                                            numZoom=3
                                        }
                                        mainMap.setZoom(numZoom);
                                    }
                                    mainMap.setCenter(conListDraw[i].center);
                                    $(this).removeClass("file-list-con-select")
                                }else {
                                    conListDraw[i].setOptions({
                                        fillColor:'red',
                                        fillOpacity:0.5,
                                    });
                                    if(conListDraw[i].numMax == 0){
                                        mainMap.setZoom(14);
                                    }else {
                                        var numZoom = 12.5 - (conListDraw[i].numMax * 0.3);
                                        if(numZoom <= 3){
                                            numZoom=3
                                        }
                                        mainMap.setZoom(numZoom);
                                    }
                                    mainMap.setCenter(conListDraw[i].center);
                                    $(this).addClass("file-list-con-select")
                                }
                            }
                        }

                    })
                    /*table分页请求*/
                    if ($("#sdo-page-list .bootpag").length != 0) {
                        $("#sdo-page-list").off();
                        $('#sdo-page-list').empty();
                        $("#sdo-page-span").empty();
                    }
                    $("#sdo-page-span").append("当前第"+dataSdo.pageNum +"页,共"+dataSdo.totalPage +"页,共"+dataSdo.totalNum+"条数据")
                    $('#sdo-page-list').bootpag({
                        total: dataSdo.totalPage,
                        page: dataSdo.pageNum,
                        maxVisible: 6,
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
                        sdoFun(num,conData);
                    });
                },error:function () {
                    console.log("请求失败")
                }
            })
        }
        <!-- 页面文件请求-->
        function FileInfoFun(num,data) {
            data.pageNum=num;
            var conData = data;
            console.log(conData)
            $.ajax({
                url:"${ctx}/mapSearch/getFileInfoBy",
                type:"GET",
                data: conData,
                success:function (data) {
                    $("#file-list-file").empty();
                    mainMap.clearMap();
                    isDrawRect();
                    var dataFile = JSON.parse(data);
                    if(data =="{}" ||dataFile.fileinfo.length ==0){
                        $('#file-page-list').empty();
                        $("#file-page-span").empty();
                        $("#file-list-file").append("<h1 >暂时没有数据</h1>")
                        return
                    }
                    var fileList =dataFile.fileinfo;
                    var fileStr="";
                    var publisherHref="";
                    var downHref="";
                    var fileImgSrc="";
                    var sourceSrc="";
                    var fileImgHref="";
                    for(var i=0;i<fileList.length;i++){
                        publisherHref="";
                        sourceSrc="${ctx}/sdo/detail/"+fileList[i].sdoid;
                        downHref="${ctx}/sdo/downloadOneFile?id="+fileList[i].fileinfoid;
                        fileImgSrc="${ctx}/resources/img/index/u0.jpg";
                        fileImgHref=sourceSrc;
                        fileStr+="<div class='con-one' name='file-list'> <span class='num'>"+(i+1)+"</span> <div class='map-con-list-right'> <h3 title="+fileList[i].filename +">"+fileList[i].filename+"</h3>"+
                            "<div class='content-see'> <ul> <li class='tab-preview' onlyID="+fileList[i].fileinfoid +">查看</li> <li>|</li> <li><a href='javascript:;' class='downLoadOne' onlyID="+downHref +">下载</a></li> </ul> </div> <div class='collection-mess'>"+
                            "<span class='file-type-one'>"+fileList[i].filetype+"</span> <span>"+fileList[i].publisher+"</span> <span>"+fileList[i].updatetime+"</span>"+
                        "<span class='datas-name'>来源:<a title='"+fileList[i].sdotitle+"' href="+sourceSrc+">"+fileList[i].sdotitle+"</a></span> </div> </div> <div class='clear'></div> </div>"
                    }

                    $("#file-list-file").append(fileStr);
                    $("[name='file-list']").hover(
                        function () {
                            $(this).addClass("con-hover");
                        },
                        function () {
                            $(this).removeClass("con-hover");
                        }
                    )

                    var rect;
                    var infoWindow = new AMap.InfoWindow();
                    conListDraw.length = 0;
                    conList.length=0;
                    conList = $("[name='file-list']");
                    for(var i=0;i<fileList.length;i++){
                        publisherHref="";
                        sourceSrc="${ctx}/sdo/detail/"+fileList[i].sdoid;
                        downHref="${ctx}/sdo/downloadOneFile?id="+fileList[i].fileinfoid;
                        fileImgSrc="${ctx}/resources/img/index/u0.jpg";
                        fileImgHref=sourceSrc;
                        if(fileList[i].center !==null){
                            var southWest = new AMap.LngLat(fileList[i].lowLeft[0], fileList[i].lowLeft[1])
                            var northEast = new AMap.LngLat(fileList[i].upRight[0], fileList[i].upRight[1])
                            var bounds = new AMap.Bounds(southWest, northEast);
                            rect =  new AMap.Rectangle({
                                map:mainMap,
                                bounds: bounds,
                                strokeColor:'blue',
                                strokeOpacity:0.5,
                                strokeWeight:0,
                                fillColor:'blue',
                                fillOpacity:0,
                                zIndex:10,
                                bubble:true,
                                cursor:'pointer',
                            });
                            rect.content="<div class='con-one' style='width:660px'> <span class='num'>"+(i+1)+"</span> <div class='map-con-list-right'> <h3 title="+fileList[i].filename +">"+fileList[i].filename+"</h3>"+
                                "<div class='content-see'> <ul> <li class='tab-preview' onlyID="+fileList[i].fileinfoid +">查看</li> <li>|</li> <li><a href='javascript:;' class='downLoadOne' onlyID="+downHref +">下载</a></li> </ul> </div> <div class='collection-mess'>"+
                                "<span class='file-type-one'>"+fileList[i].filetype+"</span> <span>"+fileList[i].publisher+"</span> <span>"+fileList[i].updatetime+"</span>"+
                                "<span class='datas-name'>来源:<a title='"+fileList[i].sdotitle+"' href="+sourceSrc+">"+fileList[i].sdotitle+"</a></span> </div> </div> <div class='clear'></div> </div>"
                            rect.drawId=i;
                            rect.drawActive=false;
                            rect.center = fileList[i].center;
                            rect.numMax =Math.ceil(Math.max((fileList[i].lowRight[0] - fileList[i].lowLeft[0]),(fileList[i].upLeft[1] - fileList[i].lowLeft[1])) * 10);
                            conListDraw.push(rect);
                            rect.on('click', markerClick);
                        }
                    }
                    $(".downLoadOne").on("click",function () {
                        if(loginId ==""){
                            toastr["error"]("请先登录！");
                            return;
                        }
                        var url = $(this).attr("onlyID");
                        window.location.href=url;
                    })
                    function markerClick(e){
                        if(!this.drawActive){
                            $(conList[this.drawId]).addClass("file-list-con-select")
                            this.setOptions({
                                fillColor:'red',
                                fillOpacity:0.5
                            });
                            this.drawActive=true
                        }else {
                            $(conList[this.drawId]).removeClass("file-list-con-select")
                            this.setOptions({
                                fillColor:'blue',
                                fillOpacity:0

                            });
                            this.drawActive=false;
                        }
                        infoWindow.setContent(e.target.content);
                        infoWindow.open(mainMap, e.lnglat);
                    }
                    $("[name='file-list']").click(function () {
                        for(var i=0;i<conListDraw.length;i++){
                            if($(this).index() ==conListDraw[i].drawId ){
                                if($(this).hasClass("file-list-con-select")){
                                    conListDraw[i].setOptions({
                                        fillColor:'blue',
                                        fillOpacity:0
                                    });
                                    if(conListDraw[i].numMax == 0){
                                        mainMap.setZoom(14);
                                    }else {
                                        var numZoom = 12.5 - (conListDraw[i].numMax * 0.3);
                                        if(numZoom <= 3){
                                            numZoom=3
                                        }
                                        mainMap.setZoom(numZoom);
                                    }
                                    mainMap.setCenter(conListDraw[i].center);

                                    $(this).removeClass("file-list-con-select")
                                }else {
                                    conListDraw[i].setOptions({
                                        fillColor:'red',
                                        fillOpacity:0.5
                                    });
                                    if(conListDraw[i].numMax == 0){
                                        mainMap.setZoom(14);
                                    }else {
                                        var numZoom = 12.5 - (conListDraw[i].numMax * 0.3);
                                        if(numZoom <= 3){
                                            numZoom=3
                                        }
                                        mainMap.setZoom(numZoom);
                                    }
                                    mainMap.setCenter(conListDraw[i].center);
                                    $(this).addClass("file-list-con-select")
                                }
                            }
                        }
                    })
                    if ($("#file-page-list .bootpag").length != 0) {
                        $("#file-page-list").off();
                        $('#file-page-list').empty();
                        $("#file-page-span").empty();
                    }
                    $("#file-page-span").append("当前第"+dataFile.pageNum +"页,共"+dataFile.totalPage +"页,共"+dataFile.totalNum+"条数据")
                    $('#file-page-list').bootpag({
                        total: dataFile.totalPage,
                        page: dataFile.pageNum,
                        maxVisible: 6,
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
                        FileInfoFun(num,conData);
                    });
                },
                error:function () {
                    console.log("请求失败")
                }
            })
        }
        $("body").delegate('.tab-preview', 'click', function () {
            var id = $(this).attr("onlyID");
            $.ajax({
                url: "${ctx}/sdo/getPreview?id=" + id,
                type: "GET",
                success: function (data) {
                    var data = JSON.parse(data);
                    if (data.previewType == "img") {
                        $("#imgcontent").attr("src", "${ctx}"+data.previewFile);
                        $("#myModalThree").modal('show')
                    } else if (data.previewType == "datatable") {
                        window.open("${ctx}/sdo/readExcel?filePath="+ data.filePath+"&fileType="+data.fileType)
                    }else if(data.previewType =="openoffice"){
                        window.open("${ctx}/sdo/office?filePath="+data.filePath+"&fileType="+data.fileType)
                    }
                },
                error: function () {
                    alert("下载失败");
                }
            })
        })
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
    </script>
</div>
</html>
