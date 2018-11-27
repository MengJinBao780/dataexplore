<%--
  Created by IntelliJ IDEA.
  User: ajian
  Date: 2018/7/3
  Time: 20:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>资源关联</title>
</head>
<body>
<div class="page-content">
    <%--<div class="">--%>
        <%--<h2 class="common-tit">数据关联</h2>--%>
        <div  id="relateSdoId" class="container" style="width: 80%;height: 100%"></div>
    <%--</div>--%>
</div>

</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.js"></script>
    <script type="text/javascript"
            src="http://webapi.amap.com/maps?v=1.4.6&dev-Map=74dbb914c763808d47998ac6e45d706a&plugin=AMap.MouseTool"></script>
    <script>
        /*关联图谱*/
        $().ready(function () {
            $.ajax({
                url: '${ctx}/sdo/relation/${id}',
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
                return cutstr(title, 10);//显示字符个数

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
                    animationDurationUpdate: 1200,
                    animationEasingUpdate: 'quinticInOut',
                    series: [
                        {
                            type: 'graph',
                            layout: 'force',
                            symbolSize: 70,//圆圈大小
                            roam: true,
                            // edgeSymbol: ['circle', 'arrow'],
                            edgeSymbolSize: [10, 10],
                            edgeLabel: {
                                normal: {
                                    textStyle: {
                                        fontSize: 15 //字体大小
                                    }
                                }
                            },
                            force: {
                                repulsion: 1000,
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
                var curSdoName = getShortName('${title}');
                var curSdo = {
                    name: curSdoName, des: '${title}', id: 0, sdoId: '${id}'
                };//symbolSize: 80
                var dataMap = new Map();
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
                                show: true, //true显示分值，false不显示
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
                    <%--window.open('${ctx}/sdo/detail/' + handler.data.sdoId);--%>
//                    return;
                    $.ajax({
                        url: '${ctx}/sdo/relation/' + handler.data.sdoId,
                        type: "get",
                        dataType: "json",
                        success: function (relateList2) {
                            if (!relateList2 || relateList2.length == 0) {
                                return;
                            }
                            relationMap2(handler, relateList2);
                        },
                        error: function () {
                        }
                    });
                }

                function relationMap2(handler, relateList2) {
                    for (var i = 0; i < relateList2.length; i++) {
                        var cur2 = relateList2[i];
                        if (dataMap.get(cur2.sdoId)) {
                            var tar = dataMap.get(cur2.sdoId);
                            var curLink = {
                                source: handler.data.id,
                                target: tar.id,
                                value: 1,
                                score: cur.score,
                                label: {
                                    normal: {
                                        show: true, //true显示分值，false不显示
                                        formatter: cur.score.toFixed(3) + '',
                                        fontSize: 9
                                    }
                                },
                                lineStyle: {
//                                    normal: {curveness: 0.2}
                                },
                                itemStyle: {
                                    normal: {
                                        color: "#ffffff"
                                    }
                                }
                            };
                            linkList.push(curLink);
                        } else {
                            var curName = getShortName(cur2.sdoTitle);
                            var curRelate = {
                                name: curName, des: cur2.sdoTitle, id: indexId, sdoId: cur2.sdoId
                            };
                            dataMap.set(cur2.sdoId, curRelate);
                            dataList.push(curRelate);
                            var curLink = {
                                source: handler.data.id,
                                target: indexId++,
                                value: 1,
                                score: cur.score,
                                label: {
                                    normal: {
                                        show: true, //true显示分值，false不显示
                                        formatter: cur.score.toFixed(3) + '',
                                        fontSize: 9
                                    }
                                },
                                lineStyle: {
//                                    normal: {curveness: 0.2}
                                }
                            };
                            linkList.push(curLink);
                        }
                    }
//                    myChart.setOption(option, true);
                    myChart.setOption({
                        series: [{
                            roam: true,
                            data: dataList,
                            edges: linkList
                        }]
                    });
                    myChart.on('click', clickEvent);
                }
            }
        });
    </script>
</div>
</html>
