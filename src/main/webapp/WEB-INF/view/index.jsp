<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2018/04/12
  Time: 15:28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>欢迎访问CASEARTH广目系统</title>

    <link href="${ctx}/resources/css/newIndex.css" rel="stylesheet" type="text/css"/>
   <%-- <link href="${ctx}/resources/css/index.css" rel="stylesheet" type="text/css"/>--%>
    <link rel="stylesheet" href="${ctx}/resources/bundles/swiper/swiper.css">
</head>
<body>
<div class="banner">
    <div class="search-wrap">
        <input type="text" placeholder="学科主题，研究领域，地名，类型" name="" id="search">
        <button class="search-btn" id="searchBtn">搜索</button>
        <div class="word-wrap">
            <p>推荐搜索词：</p>
            <div class="search-word">
            </div>
        </div>
    </div>
    <%--<div class="img-wrap">
        <div class="img-one">
            <img id="img1" src="${ctx}/resources/img/play0.png" alt="">
            <p>一带一路</p>
        </div>
        <div class="img-one">
            <img id="img2" src="${ctx}/resources/img/play1.png" alt="">
            <p>美丽中国</p>
        </div>
        <div class="img-one">
            <img id="img3" src="${ctx}/resources/img/play2.png" alt="">
            <p>生物多样性</p>
        </div>
        <div class="img-one">
            <img id="img4" src="${ctx}/resources/img/play3.png" alt="">
            <p>时空三级</p>
        </div>
        <div class="img-one">
            <img id="img5" src="${ctx}/resources/img/play4.png" alt="">
            <p>近海海洋</p>
        </div>
        <div class="img-one">
            <img id="img6" src="${ctx}/resources/img/play5.png" alt="">
            <p>大数据云服务</p>
        </div>
        <div class="clear"></div>
    </div>--%>
   <%-- <div id="img-sign" class="img-sign">
        <!-- 标签显示 -->
    </div>--%>
    <div style="width: 1200px;height: 130px;margin:0 auto" id="swiper-wrap">
        <div class="swiper-container">
            <div class="swiper-wrapper">

            </div>
            <!-- Add Button -->
            <div class="swiper-button-next"  style="outline:none"></div>
            <div class="swiper-button-prev" style="outline:none"></div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>
    </div>
    <div class="contentaaa">

    </div>
</div>
<div class="about">
    <div class="about-left">
        <img src="${ctx}/resources/img/about.png">
    </div>
    <div class="about-right">
        <h2>关于DataExplore</h2>
        <p>DataExplore是一个社区驱动的项目，提供跨多个成员资源库的数据访问，支持对地球和环境数据的增强搜索和发现。DataExplore通过响应式教育资源和材料促进数据管理的最佳实践支持对地球和环境数据的增强搜索和发现通过响应式教育资源和材料促进数据管理的最佳实践...</p>
    </div>
    <div class="clear"></div>
</div>
<div class="container">
    <div class="con-list">
        <div class="list-tit" id="list-tit">
            <ul>
                <li class="active">最近更新</li>
                <li>最受欢迎</li>
            </ul>
            <div class="list-type">
                <img id="list-one" src="${ctx}/resources/img/list.png">
                <img id="list-two" src="${ctx}/resources/img/thumbnail.png">
            </div>
        </div>
        <div class="con-main">
            <div class="list-model">
                <ul class="table-tit" id="list-model-title-last" style="display: none;">
                    <li>数据名称</li>
                    <li>数据来源</li>
                    <li>访问量</li>
                    <li>最后更新</li>
                </ul>
                <div class="table-con" id="list-model-last" style="display: none;">
                   <%-- <ul class="row-one">
                        <li>Landsat4-5 MSS 卫星数字产品内容内容内容</li>
                        <li>李国庆数据中心</li>
                        <li>2018-01-08</li>
                    </ul>
                    <ul class="row-one">
                        <li>Landsat4-5 MSS 卫星数字产品</li>
                        <li>李国庆数据中心</li>
                        <li>2018-01-08</li>
                    </ul>
                    <ul class="row-one">
                        <li>Landsat4-5 MSS 卫星数字产品</li>
                        <li>李国庆数据中心</li>
                        <li>2018-01-08</li>
                    </ul>--%>
                </div>
                <div class="thumbnail-model" id="thumbnail-model-last" >
                </div>
            </div>
        </div>
        <div class="con-main" style="display:none;">
            <ul class="table-tit" id="list-model-title-welcome" style="display: none;">
                <li>数据名称</li>
                <li>数据来源</li>
                <li>访问量</li>
                <li>最后更新</li>
            </ul>
            <div class="table-con" id="list-model-welcome" style="display: none;">
                <%--<ul class="row-one">
                    <li>最受欢迎</li>
                    <li>李国庆数据中心</li>
                    <li>2018-01-08</li>
                </ul>--%>
            </div>

            <div class="thumbnail-model" id="thumbnail-model-welcome" >
                <%--<div class="product-one">
                    <img src="${ctx}/resources/img/pro-img.jpg">
                    <p>Landsat4-5 MSS 最受欢迎</p>
                    <span>李国庆数据中心 2018.01.08</span>
                </div>--%>
            </div>
        </div>
    </div>
    <div class="con-right">
        <div class="data">
            <h3 class="right-tit">数据情况</h3>
            <span class="cover"></span>
            <div class="data-main">
                <div class="data-one">
                    <img src="${ctx}/resources/img/user.png">
                    <span>总用户：</span>
                    <span>${stat.userNum}</span>
                </div>
                <div class="data-one">
                    <img src="${ctx}/resources/img/database.png">
                    <span>数据量：</span>
                    <span>${stat.dataSize}&nbsp;GB</span>
                </div>
                <div class="data-one">
                    <img src="${ctx}/resources/img/file.png">
                    <span>文件数：</span>
                    <span>${stat.fileNum}</span>
                </div>
                <div class="data-one">
                    <img src="${ctx}/resources/img/visit.png">
                    <span>访问量：</span>
                    <span>${stat.visitNum}</span>
                </div>
                <div class="data-one">
                    <img src="${ctx}/resources/img/data.png">
                    <span>数据集：</span>
                    <span>${stat.datasetNum}</span>
                </div>
                <div class="data-one">
                    <img src="${ctx}/resources/img/download.png">
                    <span>下载量：</span>
                    <span>${stat.downloadNum}</span>
                </div>
            </div>
        </div>
        <div class="tag">
            <h3 class="right-tit">全站标签云</h3>
            <span class="cover"></span>
            <div class="tag-main"></div>
        </div>
        <div class="statistics">
            <h3 class="right-tit">服务统计</h3>
            <%--<a class="more" href="#">更多…</a>--%>
            <span class="cover"></span>
            <div class="statistics-main" id="datashow">
                <%--<img style="margin-top:18px;margin-left:6px;" src="${ctx}/resources/img/project.jpg">--%>
                <!-- <div id="MyChart" style="width: 350px; height: 300px;"></div> -->
            </div>
        </div>
        <div class="news">
            <h3 class="right-tit">新闻动态</h3>
            <%--<a class="more" href="#">更多…</a>--%>
            <span class="cover"></span>
            <div class="news-main">
                <c:forEach items="${newsList}" var="item">
                    <div class="news-one">
                        <a href="${ctx}/collection/view/${item.id}" title="${item.title}">
                            <c:if test="${fn:length(item.title) > 17}">
                                ${fn:substring(item.title,0,17)}......
                            </c:if>
                            <c:if test="${fn:length(item.title) <= 17 }">
                                ${item.title}
                            </c:if>
                    </a>
                        <span><fmt:formatDate type="date" value="${item.createTime}" pattern="yyyy-MM-dd"/></span>
                    </div>
                </c:forEach>
                <%--<div class="news-one">
                    <a href="#">让战略科技力量宣示“复兴之力”</a>
                    <span>2018-04-09</span>
                </div>
                <div class="news-one">
                    <a href="#">让战略科技力量宣示“复兴之力”</a>
                    <span>2018-04-09</span>
                </div>
                <div class="news-one">
                    <a href="#">让战略科技力量宣示“复兴之力”让战略科技力量宣示“复兴之力”</a>
                    <span>2018-04-09</span>
                </div>
                <div class="news-one">
                    <a href="#">让战略科技力量宣示“复兴之力”</a>
                    <span>2018-04-09</span>
                </div>--%>
            </div>
        </div>
    </div>
</div>





<%--<div class="page-content">
    <div class="main-content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-8 del-padding eq-high ">
                    &lt;%&ndash;<div class="lun-play">
                        <div class="search">
                            <input type="text" placeholder="  搜索学科主题、研究领域、地名、资源类型或其他关键词..." >
                            <button type="submit" >搜索DataExplore</button>
                        </div>
                        <div class="key-words">
                        </div>
                        <div style="width: 675px;height: 140px;margin-left: 105px;margin-top: 20px" id="swiper-wrap">

                        </div>
                       <div class="contentaaa">
                            &lt;%&ndash;<div class="aaa" name="雷达"></div>
                            <div class="aaa" name="大气"></div>
                            <div class="aaa active" name="陆地"></div>
                            <div class="aaa" name="卫星"></div>
                            <div class="aaa" name="气候"></div>
                            <div class="aaa" name="6th专项"></div>
                            <div class="aaa" name="7th专项"></div>
                            <div class="aaa" name="8th专项"></div>
                            <div class="aaa" name="9th专项"></div>&ndash;%&gt;
                        </div>
                    </div>&ndash;%&gt;
                    &lt;%&ndash;<div class="con-nav">
                        <div class="nav-cent">
                            <a href="javascript:;"><span id="clk1">最近更新</span></a>
                            <a href="javascript:;"><span id="clk2">最受欢迎</span></a>
                        </div>
                    </div>&ndash;%&gt;
                    &lt;%&ndash;<div id="clk11" class="container-fluid del-padding">
                        <div class="row" style="padding: 0 10px 0 0;" id="rec" >
                        </div>
                    </div>
                    <div id="clk22" class="container-fluid del-padding" style="display: none">
                        <div class="row" style="padding: 0 10px 0 0;" id="pop">
                        </div>
                    </div>&ndash;%&gt;
                </div>
                <div class="col-xs-4 del-padding eq-high" >
                    <!--<div style="width: 100%;height: 2000px"></div>-->
                    &lt;%&ndash;<div class="describe">
                        <h3 style="color:  rgba(25, 158, 216, 1);">什么是DataExplore</h3>
                        <div>DataExplore是一个社区驱动的项目，提供跨多个成员资源库的数据访问，
                            支持对地球和环境数据的增强搜索和发现。DataExplore通过响应式教育资源和材料促进数据管理的最佳实践...
                        </div>
                        <a href="#">更多关于DataExplore的信息</a>
                    </div>&ndash;%&gt;
                    <div class="jqCloud"></div>
                    &lt;%&ndash;<div class="total-data">
                        <div style="background-color: #C0E9C1"><h4><span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;&nbsp;用户总数：${stat.userNum}</h4></div>
                        <div style="background-color: #efd5cb"><h4><span class="glyphicon glyphicon-cloud" aria-hidden="true"></span>&nbsp;&nbsp;总数据量：${stat.dataSize}</h4></div>
                        <div style="background-color: #afe6e0"><h4><span class="glyphicon glyphicon-file" aria-hidden="true"></span>&nbsp;&nbsp;总文件数：${stat.fileNum}</h4></div>
                        <div style="background-color: #C0E9C1"><h4><span class="glyphicon glyphicon-hand-up" aria-hidden="true"></span>&nbsp;&nbsp;访问人次：${stat.visitNum}</h4></div>
                        <div style="background-color: #efd5cb"><h4><span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>&nbsp;&nbsp;据集数：&nbsp;&nbsp;&nbsp;&nbsp;${stat.datasetNum}</h4></div>
                        <div style="background-color: #afe6e0"><h4><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>&nbsp;&nbsp;下载量：&nbsp;&nbsp;&nbsp;&nbsp;${stat.downloadNum}</h4></div>
                    </div>&ndash;%&gt;
                    <div class="data-draw">
                       &lt;%&ndash; <div id="datashow" style="height: 400px;margin-top:20px"></div>&ndash;%&gt;
                    </div>
                    &lt;%&ndash;<div>
                        <div>新闻资讯</div>
                        <img src="/resources/img/index/u175.png" alt="#">
                        <div>
                            <c:forEach items="${newsList}" var="item">
                                <div class=news-ul">
                                    <div class="col-md-8" style="padding-left: 0px; padding-right: 0px;">
                                        <a TARGET="_blank" title="${item.title}" href="${ctx}/collection/view/${item.id}">
                                            <c:if test="${fn:length(item.title) > 13}">
                                                ${fn:substring(item.title,0,13)}......
                                            </c:if>
                                            <c:if test="${fn:length(item.title) <= 13 }">
                                                ${item.title}
                                            </c:if>
                                        </a>
                                    </div>
                                    <div class="col-md-4" style="padding-left: 0px; padding-right: 0px;">
                                        <span><fmt:formatDate type="date" value="${item.createTime}" pattern="yyyy-MM-dd"/></span>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                    </div>&ndash;%&gt;
                </div>
            </div>
        </div>
    </div>
</div>--%>
</body>
<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/swiper/swiper.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery.svg3dtagcloud/jquery.svg3dtagcloud.min.js"></script>
    <%--<script src="${ctx}/resources/js/playFigure.js"></script>--%>
    <script>
        var strList ="";
        var strtHumbnail ="";

        var recentUrl = "${ctx}/getInitDates?type=recent";
        var popularUrl = "${ctx}/getInitDates?type=popular";
        var TagsByProdUrl = "${ctx}/getTagsByProd";
        var Top40TagsUrl = "${ctx}/getTop40Tags";

        var oLi = $("#list-tit li");
        var con = $(".con-main");
        var listOne = $("#list-one");
        var listTwo = $("#list-two");
        var listModelLast = $("#list-model-last");
        var thumbnailModelLast=$("#thumbnail-model-last");
        var listModelWelcome = $("#list-model-welcome");
        var thumbnailModelWelcome = $("#thumbnail-model-welcome");
        var listModelTitleLast = $("#list-model-title-last");
        var listModelTitleWelcome = $("#list-model-title-welcome");
        $("#first").addClass("active");
        $("#second").removeClass("active");
        $("#third").removeClass("active");
        $(".swiper-wrapper").delegate("img","click",function () {
            var url = $(this).attr("data-url");
            window.location.href="${ctx}"+encodeURI(url);

        })
        listOne.click(function () {
            listModelLast.show();
            thumbnailModelLast.hide();
            listModelWelcome.show();
            thumbnailModelWelcome.hide();
            listModelTitleLast.show();
            listModelTitleWelcome.show();
        });
        listTwo.click(function () {
            listModelLast.hide();
            thumbnailModelLast.show();
            listModelWelcome.hide();
            thumbnailModelWelcome.show();
            listModelTitleLast.hide();
            listModelTitleWelcome.hide();
        });
        //搜索框搜索回车
        $("#search").keydown(function (ev) {
            ev =window.event ||ev;
            if(ev.keyCode == 13) {
                //等待预设搜索接口
                window.location.href="${ctx}/sdo/list?searchKey="+$(this).val();
            }
        })
        $("#searchBtn").click(function () {
            window.location.href= "${ctx}/sdo/list?searchKey="+$("#search").val();
        })
        //最近更新和最受欢迎请求
        function getData(url,ele1,ele2) {
            $.ajax({
                url:url,
                type:'GET',
                success:function(list){
                    var recentlist =JSON.parse(list);
                    var href = "";
                    strList="";
                    strtHumbnail=""
                    for(var i=0;i<recentlist.length;i++){
                        href = "${ctx}/sdo/detail/"+recentlist[i].id;
                        strList+="<ul class='row-one'> <li><a href="+ href+" title="+recentlist[i].title +">"+recentlist[i].title+"</a></li> <li>"+
                            recentlist[i].publishOrganization +"</li><li>"+ recentlist[i].visitCount+"</li> <li>"+ recentlist[i].publisherPublishTime+"</li> </ul>";
                        strtHumbnail+= "<div class='product-one'> <a href="+ href+"><img src=${ctx}"+recentlist[i].iconPaht +
                            "></a> <p title="+recentlist[i].title +">"+recentlist[i].title+"</p> <span>"+recentlist[i].publishOrganization +recentlist[i].publisherPublishTime  +"</span> </div>"
                    }
                    ele1.append(strList);
                    ele2.append(strtHumbnail)
                },
                error:function(XMLHttpRequest, textStatus, errorThrown) {
                    alert(XMLHttpRequest.status);
                    alert(XMLHttpRequest.readyState);
                    alert(textStatus);
                }});
        }

        $(function(){
            TagsByProd(TagsByProdUrl);
            SearchWord();
            function TagsByProd(url){
                $.ajax({
                    url:url,
                    type:"GET",
                    success:function (data) {
                        var TagsByProdList =  JSON.parse(data);
                        var playStr="";
                        var href="";
                        var imgUrl="";
                        for (var i =0;i<TagsByProdList.length;i++){
                            href= "/sdo/list?prodId="+TagsByProdList[i].prodId+"&prodName="+TagsByProdList[i].prodName;
                            imgUrl = "${ctx}"+TagsByProdList[i].img;
                            playStr+="<div class='swiper-slide'><img src="+imgUrl+" data-url="+href +"><p>"+TagsByProdList[i].prodName+"</p></div>"
                        }
                        $(".swiper-wrapper").append(playStr);

                        var playTagStr = ""
                        for(var i=0;i<TagsByProdList.length;i++){
                            playTagStr+="<div class='img-sign' name="+TagsByProdList[i].prodName +">"
                            for(var j=0;j<TagsByProdList[i].tagNames.length;j++){
                                var tagsHref = "${ctx}/sdo/list?tag="+encodeURI(TagsByProdList[i].tagNames[j]) + "&prodId="+TagsByProdList[i].prodId;
                                playTagStr+="<a href="+tagsHref +"><span >"+TagsByProdList[i].tagNames[j]+"</span></a>"
                            }
                            playTagStr+="</div>"
                        }
                        $(".contentaaa").append(playTagStr);
                        autoPlay();
                    },
                    error:function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(XMLHttpRequest.status);
                        alert(XMLHttpRequest.readyState);
                        alert(textStatus);
                    }
                })
            };
            getData(recentUrl,listModelLast,thumbnailModelLast);
            oLi.on("click",function () {
                if($(this).attr("class")){
                    return
                }
                if($(this).index() == 0){
                    getData(recentUrl,listModelLast,thumbnailModelLast);
                    oLi.eq(0).addClass("active");
                    oLi.eq(1).removeClass("active")
                    setTimeout(function () {
                        con.eq(0).show();
                        con.eq(1).hide();
                        listModelWelcome.empty();
                        thumbnailModelWelcome.empty();
                    },50)
                }else {
                    getData(popularUrl,listModelWelcome,thumbnailModelWelcome);
                    oLi.eq(1).addClass("active");
                    oLi.eq(0).removeClass("active");
                    setTimeout(function () {
                        con.eq(1).show();
                        con.eq(0).hide();
                        listModelLast.empty();
                        thumbnailModelLast.empty();
                    },50)
                }

            })
            //云标签
            Top40Tags(Top40TagsUrl);
        });

            //echarts统计图
        $.ajax({
                url: "${ctx}/getIndexProductStat",
                type: "get",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datashow'));

                    var option = {
                        title : {
                            text: '资源服务情况',
                            left: 'center'
                        },
                        tooltip : {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                dataView : {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                                myTool2: {
                                    show: true,
                                    title: '详细',
                                    icon: 'image://http://echarts.baidu.com/images/favicon.png',
                                    onclick: function (){
                                        window.location.href="${ctx}/getMoreStat";
                                    }
                                }
                            }
                        },
                        calculable : true,
                        legend: {
                            data:['数据集','存储量','文件量'],
                            x: 'center',
                            top:'bottom'
                        },
                        grid: {
                            top: '12%',
                            left: '1%',
                            right: '10%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type : 'category',
                                data : data.productName
                            }
                        ],
                        yAxis: [
                            {
                                type : 'value'
                            }
                        ],
                        series : [
                            {
                                name: '数据集',
                                type: 'bar',
                                data: data.datasetNumList
                            },
                            {
                                name: '存储量',
                                type: 'bar',
                                data: data.storageCapacityList
                            },
                            {
                                name: '文件量',
                                type: 'bar',
                                data: data.fileNumList
                            }
                        ]
                    };
                    chart.setOption(option);
                }
            });
            //云标签
        function Top40Tags(url){
                $.ajax({
                    url:url,
                    type:"GET",
                    success:function (data) {
                        var cloudTab1=new Array();
                        var tabList =  JSON.parse(data);
                        for(var i=0;i<tabList.length;i++){
                            cloudTab1.push({
                                label:tabList[i].tagName,
                                url:"${ctx}/sdo/list?tag=" + encodeURI(tabList[i].tagName),

                            })
                        }
                        var settings = {
                            entries: cloudTab1,
                            width: 420,
                            height: 272,
                            radius: '65%',
                            radiusMin: 75,
                            bgDraw: true,
                            bgColor: '#eff7fa',
                            opacityOver: 1.00,
                            opacityOut: 0.05,
                            opacitySpeed: 6,
                            fov: 800,
                            speed: 0.5,
                            fontFamily: 'Oswald, Arial, sans-serif',
                            fontSize: '15',
                            fontColor: '#000',
                            fontWeight: 'normal',//bold
                            fontStyle: 'normal',//italic
                            fontStretch: 'normal',//wider, narrower, ultra-condensed, extra-condensed, condensed, semi-condensed, semi-expanded, expanded, extra-expanded, ultra-expanded
                            fontToUpperCase: true

                        };
                        $(".tag-main").svg3DTagCloud(settings);
                        $(".tag-main a").css("cursor","pointer");
                    },
                    error:function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(XMLHttpRequest.status);
                        alert(XMLHttpRequest.readyState);
                        alert(textStatus);
                    }
                })
            };

        function SearchWord() {
                $.ajax({
                    url:"${ctx}/getSearchWord",
                    type:"GET",
                    success:function (data) {
                       var data = JSON.parse(data);
                       var str = "";
                       var url = ""
                       for(var i=0;i<data.length;i++){
                           url = "${ctx}/sdo/list?searchKey="+data[i].searchWord;
                           str+="<a href="+ url+"><span >"+data[i].searchWord +"</span></a>"
                       }
                        $(".search-word").append(str);
                    }
                })
            }
        function autoPlay() {
                var oldIndex = "";
                var flag =true;
                var num =4;
                var swiperHeight = $(".swiper-slide").height();
                var myswiper = new Swiper('.swiper-container', {
                   on: {
                       init: function(){
                           //Swiper初始化了
                           num = $(".swiper-slide").eq(this.activeIndex +2).attr("data-swiper-slide-index")
                           $(".swiper-slide").eq(this.activeIndex +2).addClass("active-swiper");
                           $(".img-sign").eq(num).show()
                       },
                       slideChangeTransitionStart: function(){
                           $(".swiper-slide img").css("height",swiperHeight+"px")
                           $(".swiper-slide").eq(oldIndex).removeClass("active-swiper");
                           if(!num == ""){
                                $(".img-sign").eq(num).hide()
                            }
                            num = $(".swiper-slide").eq(this.activeIndex +2).attr("data-swiper-slide-index")
                            oldIndex = this.activeIndex +2
                            if(flag){
                                $(".swiper-slide").eq(this.activeIndex +2).addClass("active-swiper");
                                $(".img-sign").eq(num).show()
                                flag =false;
                                return
                            }
                           $(".swiper-slide").eq(this.activeIndex +2).addClass("active-swiper");
                           $(".img-sign").eq(num).show()
                        },
                    },
                    allowTouchMove: false,
                    loop:true,
                    speed:1000,
                    slidesPerView: 6,
                    spaceBetween: 30,
                    autoplay:{
                        delay:1000
                    },
                    navigation: {
                        nextEl: '.swiper-button-next',
                        prevEl: '.swiper-button-prev',
                    },
                });
                $("#swiper-wrap").on("mouseenter",function () {
                    myswiper.autoplay.stop();
                })
                $("#swiper-wrap").on("mouseleave",function () {
                    myswiper.autoplay.start();
                })
                $(".img-sign").on("mouseenter",function () {
                    myswiper.autoplay.stop();
                })
                $(".img-sign").on("mouseleave",function () {
                    myswiper.autoplay.start();
                })
            }
    </script>
</div>

</html>
