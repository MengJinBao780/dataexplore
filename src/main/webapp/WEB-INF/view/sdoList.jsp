<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2018/04/26
  Time: 15:28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>欢迎访问CASEARTH广目系统</title>
    <%--<link href="${ctx}/resources/css/sdoList.css" rel="stylesheet" type="text/css"/>--%>
    <%--<script src="${ctx}/resources/bundles/nouislider/nouislider.min.css"></script>--%>
    <link href="${ctx}/resources/css/newSdoList.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/jquery-range/jquery.range.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/jquery-tags-input/jquery.tagsinput.css" rel="stylesheet" type="text/css"/>
    <%--<link href="${ctx}/resources/bundles/jquery-tagcloud/style.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctx}/resources/bundles/jquery-tagcloud/normalize.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctx}/resources/bundles/nouislider/nouislider.min.css">
    <style type="text/css">
        mark2 {
            color: #ff0000;
            /*font-weight: bold;*/
            background-color:#FFFFff;
        }
        div.tagsinput {
            background: #FFF;
            padding: 5px;
            width: 300px;
            height: 100px;
        }
        .data-filter select{
            float:left;
            display: block;
            width:120px;
            height:28px;
            border:1px solid #ccc;
            margin-right:20px;
            margin-top:4px;
        }
        .data-right h3{
            margin-bottom:16px;
        }
        #tr_starttime, #tr_endtime{
            margin-top:7px;
            border:none;
        }
        .data-filter select,.data-filter option{
            border:1px solid #ccc;
        }
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button{
            -webkit-appearance: none !important;
            margin: 0;
        }
        input[type="number"]{-moz-appearance:textfield;}
        .cloud-tag2{
            position: relative;
            height: 500px;
            overflow: hidden;
            box-shadow: 0 0 50px #8e8e8e;
            -webkit-box-shadow: 0 0 50px #8e8e8e;
            -moz-box-shadow: 0 0 50px #8e8e8e;
        }
        .cloud-tag2 a {
            position: absolute;
            color: #8e8e8e;
            text-decoration: none;
            top: 500px;
            display: block;
            border: #8e8e8e 1px solid;
            box-shadow: 0 0 5px #8e8e8e;
            -webkit-box-shadow: 0 0 5px #8e8e8e;
            -moz-box-shadow: 0 0 5px #8e8e8e;
            background: #fff;
            filter: alpha(opacity: 30);
            opacity: 0.5;
            font-size: 14px;
            padding: 3px 5px;
            font-family: arial;
        }
        .cloud-tag2 a:hover {
            filter: alpha(opacity: 100);
            opacity: 1;
            font-weight: bold;
            font-size: 16px;
        }
    </style>
</head>
<body>
<div class="page-content">
    <div class="search">
        <div class="search-box">
            <div class="search-tips" id="search-tips-id">
                <p id="proName"></p>
                <img id="closeProName" src="${ctx}/resources/img/cancel.png" alt="">
            </div>
            <div class="search-main">
                <input class="search-write" type="text">
                <button class="search-btn1">搜索DataExplore</button>
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
            <div class="key-wrap"></div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="container">
        <div class="data-list">
            <div class="data-filter">
                <img src="${ctx}/resources/img/filter-icon.png" alt="">
                <select name="fileType" id="fileType" onchange="getData();">
                    <%--<option value="文件类型">文件类型</option>
                    <option value="">文件类型</option>
                    <option value="">文件类型</option>--%>
                    <option value="" selected>文件类型</option>
                    <c:forEach items="${dataTypeList}"  var="item">
                        <option value="${item}">${item}</option>
                    </c:forEach>
                </select>
                <select name="provider" id="provider" onchange="getData();">
                    <option value="">数据提供者</option>
                    <c:forEach items="${publisherList}"  var="item">
                        <option value="${item}">${item}</option>
                    </c:forEach>
                </select>
                <div class="time-part">
                    <input id="tr_starttime" type="number" >
                    <div id="timeSlider" style="width:290px;margin-top:8px;margin-right:30px;"></div>
                    <input id="tr_endtime" type="number" >
                    <div class="clear"></div>
                </div>
                <div class="clear"></div>
            </div>
            <div class="sort-type">
                <ul >
                    <li name="sortDiv" id="title" data="title" >名称</li>
                    <li>|</li>
                    <li name="sortDiv" id="visitCount" data="visitCount">访问量</li>
                    <li>|</li>
                    <li name="sortDiv" id="downloadCount" data="downloadCount">下载量</li>
                    <li>|</li>
                    <li name="sortDiv" id="publisherPublishTime" data="publisherPublishTime">更新时间</li>
                </ul>
                <label>排序方式：</label>
                <div class="clear"></div>
            </div>
            <div class="dataset-list">
            </div>
            <span id="page-span" style="line-height: 76px"></span>
            <div class="page-list"></div>
        </div>
        <div class="data-main-right">
            <div class="tag tag-data">
                <h3 class="tag-tit">关键词标签云</h3>
                <div class="cloud-tag" id="mainTag">
                    <%--<img style="display:block;" src="${ctx}/resources/img/tag.jpg" alt="">--%>
                </div>
                <%--<div class="cloud-tag2" id="mainTag2">

                </div>--%>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>



<script type="text/html" id="resourceTmpl">
    {{each list as value i}}
    <div class="collection-one">
        <img class="collection-img" src="${ctx}{{value.iconPath}}" alt="">
        <div class="data-right">
            <a href="${ctx}/sdo/detail/{{value.sdoid}}"><h3>{{value.title}}</h3></a>
            <div class="data-con" title="{{value.desc}}">{{getSubStr(value.desc,250)}}</div>
            <div class="collection-mess">
                <span>{{value.publisher}}</span>
                <span>{{dateFormat(value.publisherPublishTime)}}</span>
                <span></span>
                <label>文件量：</label>
                <span>{{value.toFilesNumber}}</span>
                <label>下载：</label>
                <span>{{value.downloadCount}}</span>
            </div>
            <div class="files-type">
                <span class="file-type-one">{{value.dataFormat}}</span>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    {{/each}}
</script>

</body>
<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/nouislider/nouislider.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery.svg3dtagcloud/jquery.svg3dtagcloud.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-range/jquery.range.min.js"></script>
    <script src="${ctx}/resources/js/regex.js"></script>
    <script src="${ctx}/resources/js/subStrLength.js"></script>
    <script src="${ctx}/resources/bundles/jquery-tags-input/jquery.tagsinput.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-tagcloud/tagcloud.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script>
        var tagNames=new Array();
        var tagStr="";
        var prodName="${param.prodName}";
        var tag = "${param.tag}";
        var sortName="title";
        var prodId = "${param.prodId}";
        var searchKey = "${param.searchKey}";
        var timerangeStr =""

        var slider = document.getElementById('timeSlider');
        var date = new Date;
        var year = date.getFullYear() + 2;
        var timeMin = year - 50;
        var timeMax = year;
        var listSelects = {};
        $("#first").removeClass("active");
        $("#second").addClass("active");
        $("#third").removeClass("active");



        //如果搜索关键词不为空，则回填关键词
        if (searchKey != '')
            $(".search-write").val(searchKey);

        if (prodName != "" && prodName != 'undefine'){
            $("#proName").text(prodName);
            $("#search-tips-id").show();
        }
        $("#closeProName").on("click",function () {
            $("#search-tips-id").hide();
            $("#proName").text("");
            prodId="";
            getData(sortName);
        })
        if (tag != "" && tag != 'undefine'){
            $(".key-wrap").append("<div class='key-word'> <p>"+tag+"</p> <span class='closeWord'>×</span> </div>");
            tagNames.push(tag);
        }
        $(".key-wrap").delegate(".closeWord","click",function () {
            var removeIndex = $(".closeWord").index(this);
            $(".key-word").eq(removeIndex).remove();
            tagNames.splice(removeIndex,1);
            /*发送请求*/
            tagStr =  tagNames.join(",")
            getData(sortName);
        })

        /*排序方式*/
        sortName = $("[name='sortDiv']:first").attr("id");
        $("[name='sortDiv']:first").addClass("sort-type-active");

        $("[name='sortDiv']").click(function () {
            sortName = ($(this).attr("id"));
            $("[name='sortDiv']").removeClass("sort-type-active");
            $(this).addClass("sort-type-active");
            getData(sortName);
        });

        $.fn.tagcloud.defaults = {
            size: {start: 15, end: 30, unit: 'px'},
            color: {start: '#0099FF', end: '#6e1919'}
        };
        $(".search-btn1").click(function () {
            getData(sortName);
        });

/*------------------------------------------------*/

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
        var dateRange = ${dateRange};

        $(function(){
            showSliderBar();
            var TBstartTime = document.getElementById("tr_starttime");
            var TBendTime = document.getElementById("tr_endtime");
            timerangeStr = $("#tr_starttime").val() + ","+ $("#tr_endtime").val()
            slider.noUiSlider.on('change.one', function(values, handle, unencoded, tap, positions) {
                var startrate = parseInt(values[0]);
                var endrate = parseInt(values[1]);
                $("#startrate").val(startrate);
                $("#stoprate").val(endrate);
                timerangeStr = startrate+","+endrate;
                getData(sortName)
            });
            TBstartTime.onblur = function() {
                changeTime(true);
                timerangeStr = $("#tr_starttime").val()+","+$("#tr_endtime").val()
                getData(sortName)
            }
            TBendTime.onblur = function() {
                changeTime(false);
                timerangeStr = $("#tr_starttime").val()+","+$("#tr_endtime").val()
                getData(sortName)
            }
            var dataFormat= $("#fileType").val();
            var publisher = $("#provider").val();
            getResourceData(prodId, searchKey, tag, dataFormat,publisher,timerangeStr, sortName,1, 10);

        });

        function getData(sortName){
            var dataFormat= $("#fileType").val();
            var publisher = $("#provider").val();
            var timerange = timerangeStr;
            var searchKey = $(".search-write").val();
            /*listSelects.dataFormat = dataFormat;
            listSelects.publisher=publisher;
            sessionStorage.setItem('listSelect', JSON.stringify(listSelects));*/
            getResourceData(prodId,searchKey,tagStr,dataFormat,publisher,timerange, sortName, 1,10);
        }

        function getResourceData(prodId, searchKey, tags, dataType,publisher,timerange, sortName,pageNo,pageSize) {
            $.ajax({
                url: "${ctx}/sdo/getData",
                type: "get",
                data: {
                    prodId: prodId,
                    searchKey: searchKey,
                    tags: tags,
                    dataFormat:dataType,
                    publisher:publisher,
                    timerange:timerange,
                    sortName:sortName,
                    pageNo: pageNo,
                    pageSize: pageSize,
                },
                dataType: "json",
                success: function (data) {

                    showMainTags(data.tagList);
                    var html = template("resourceTmpl", data);
                    $(".dataset-list").empty();
                    $(".dataset-list").append(html);
                    $("#page-span").html("当前第"+(data.currentPage + 1) +"页,共"+data.totalPages +"页,共"+data.totalCount+"条数据")
                    if (data.totalCount == 0) {
                        $(".dataset-list").append("&nbsp;&nbsp;没有数据");
                    }
                    $("#currentPageNo").html(pageNo);
                    $("#totalPages").html(data.totalPages);
                    $("#totalCount").html(data.totalCount);
                    if ($(".page-list .bootpag").length != 0) {
                        $(".page-list").off();
                        $('.page-list').empty();
                    }

                    $('.page-list').bootpag({
                        total: data.totalPages,
                        page: pageNo,
                        maxVisible: 5,
                        leaps: true,
                        firstLastUse: true,
                        first: '首页',
                        last: '尾页',
                        wrapClass: 'page-list',
                        activeClass: 'active',
                        disabledClass: 'disabled',
                        nextClass: 'next',
                        prevClass: 'prev',
                        lastClass: 'last',
                        firstClass: 'first'
                    }).on('page', function (event, num) {
                        getResourceData(prodId, searchKey, tags, dataType,publisher,timerange,sortName, num,pageSize);
                    });

                },
                error:function () {
                    console.log("请求失败")
                }
            });
        }


        function changeTag(prodId,tagName) {
            var flag = true
            for(var i=0;i<tagNames.length;i++){
                if(tagNames[i] ==tagName ){
                    flag =false;
                }
            }
            if ( !flag)
            {
                toastr["warning"]("标签已经存在");
            }
            else {
                $(".key-wrap").append("<div class='key-word'> <p>"+tagName+"</p> <span class='closeWord'>×</span> </div>")
                tagNames.push(tagName);
                tagStr = tagNames.join(",");
                getData(sortName)
            }
        }
        //jquery标签云
        function showMainTags(list) {
            $("#mainTag").html("");
            var str="";
            for ( var i = 0; i<list.length; i++){
                str += '<a href="#" rel= "' + list[i].num  + '" onclick="changeTag(\''+ list[i].prodId +'\',\'' + list[i].tagName + '\');">' + list[i].tagName +'</a>';
            }
            $("#mainTag").append(str);
            /*$("#mainTag2").append(str);*/
            /*showPropTags();*/
            $("#mainTag a").tagcloud();
        }

        //显示时间滑动条控件
        function showSliderBar() {
            var TBstartTime = document.getElementById("tr_starttime");
            var TBendTime = document.getElementById("tr_endtime");

            noUiSlider.create(slider, {
                start: [year - 20, year],
                connect: true,
                step: 1,
                range: {
                    'min': timeMin, //查询近50年范围的
                    'max': timeMax
                }
            });
            TBstartTime.value = year - 20;
            TBendTime.value = year;

            slider.noUiSlider.on('change.one', function(values, handle, unencoded, tap, positions) {
                var startTime = parseInt(values[0]);
                var endTime = parseInt(values[1]);

                TBstartTime.value = startTime;
                TBendTime.value = endTime;
            });
        }
        //同步验证时间输入框和滑动条一致
        function changeTime(isStartTime) {
            var TBstartTime = document.getElementById("tr_starttime");
            var TBendTime = document.getElementById("tr_endtime");


            if (isStartTime) {
                if (TBstartTime.value < timeMin) {
                    TBstartTime.value = timeMin;
                }
                if (TBstartTime.value > TBendTime.value) {
                    TBstartTime.value = TBendTime.value;
                }
            } else {
                if (TBendTime.value < TBstartTime.value) {
                    TBendTime.value = TBstartTime.value;
                }
                if (TBendTime.value > timeMax) {
                    TBendTime.value = timeMax;
                }
            }

            slider.noUiSlider.set([TBstartTime.value, TBendTime.value]);
        }
        function showPropTags() {
            var oDiv = document.getElementById('mainTag2');
            var aA = document.querySelectorAll('#mainTag2 a');
            var i = 0;
            for (i = 0; i < aA.length; i++) {
                aA[i].pause = 1;
                aA[i].time = null;
                initialize(aA[i]);
                aA[i].onmouseover = function() {
                    this.pause = 0;
                };
                aA[i].onmouseout = function() {
                    this.pause = 1;
                };
            }
            setInterval(starmove, 50);

            function starmove() {
                for (i = 0; i < aA.length; i++) {
                    if (aA[i].pause) {
                        domove(aA[i]);
                    }
                }
            }

            function domove(obj) {
                if (obj.offsetTop <= -obj.offsetHeight) {
                    obj.style.top = oDiv.offsetHeight + "px";
                    initialize(obj);
                } else {
                    obj.style.top = obj.offsetTop - obj.ispeed + "px";
                }
            }

            function initialize(obj) {
                var iLeft = parseInt(Math.random() * oDiv.offsetWidth);
                var scale = Math.random() * 1 + 1;
                var iTimer = parseInt(Math.random() * 1500);
                obj.pause = 0;

                obj.style.fontSize = 12 * scale + 'px';

                if ((iLeft - obj.offsetWidth) > 0) {
                    obj.style.left = iLeft - obj.offsetWidth + "px";
                } else {
                    obj.style.left = iLeft + "px";
                }
                clearTimeout(obj.time);
                obj.time = setTimeout(function() {
                    obj.pause = 1;
                }, iTimer);
                obj.ispeed = Math.ceil(Math.random() * 4) + 1;
            }
        }
    </script>
</div>

</html>
