<%--
  Created by IntelliJ IDEA.
  User: huangwei
  Date: 2018/4/20
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>统计详细</title>
</head>
<body >
<div id="productStatDiv" style="height: 400px;margin-top:20px"></div>
</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/jquery.svg3dtagcloud/jquery.svg3dtagcloud.min.js"></script>
    <script src="${ctx}/resources/js/echarts.min.js" type="text/javascript"></script>
    <script>
        //echarts统计图
        $.ajax({
            url: "${ctx}/getProductStatDetail",
            type: "get",
            dataType: "json",
            //            data: {fid: params.data.fid},
            success: function (data) {
                //                            console.log(result);
                var chart = echarts.init(document.getElementById('productStatDiv'));

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
                            magicType: {show: true, type: ['line', 'bar']}
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
    </script>
</div>
</html>
