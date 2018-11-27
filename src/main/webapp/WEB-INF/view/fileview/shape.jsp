<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/1 0007
  Time: 上午 9:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="${ctx}/resources/resview/leaflet/leaflet.css"/>
    <link rel="stylesheet" href="${ctx}/resources/bundles/semantic/semantic.min.css" />
    <link rel="stylesheet" href="${ctx}/resources/bundles/semantic/semantic.css"/>

    <link rel="stylesheet" href="${ctx}/resources/resview/shape.css"/>

</head>
<body>
<div>
    <div id="wrap" class="wrap">
        <div id="map"></div>
     <div id="attr">
            <div class="tableDisplay">
                <table id="attribute" class="ui small celled unstackable table">
                    <thead id="attrHead">
                    <tr class="center aligned">
                        <th>Attribute</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                    <tbody class="tbodyContent"></tbody>
                </table>
            </div>
            <div class="ui red icon button" id="cancelAttr">Close</div>
        </div>
    </div>
    <div class="overlay"></div>
</div>
</body>


<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/js/proj4.js"></script>
    <script src="${ctx}/resources/js/jquery-2.1.4.js"></script>
    <script src="${ctx}/resources/bundles/jquery-ui-1.11.4/jquery-ui.js"></script>
    <script src="${ctx}/resources/resview/leaflet/leaflet.js"></script>
    <script src="${ctx}/resources/bundles/jszip/jszip.js"></script>
    <script src="${ctx}/resources/bundles/jszip/jszip-utils.js"></script>
    <!--[if IE]>
    <script type="text/javascript" src="${ctx}/resources/bundles/jszip/jszip-utils-ie.js"></script>
    <![endif]-->
    <script src="${ctx}/resources/bundles/semantic/semantic.min.js"></script>
    <script src="${ctx}/resources/resview/shp2geojson/preprocess.js"></script>
    <script src="${ctx}/resources/resview/shp2geojson/preview.js"></script>
    <script type="text/javascript" charset="UTF-8">
        var ctx = '${ctx}';
        $(document).ready(function () {
            var map = L.map('map').setView([0, 0], 5),
                vector;
            L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {maxZoom: 13}).addTo(map);
            function initVector() {
                vector = L.geoJson([], {
                    style: function (feature) {
                        return feature.properties.style;
                    },
                    onEachFeature: function (feature, layer) {

                        layer.on({
                            click: function (e) {
                                vector.eachLayer(function (l) {
                                    vector.resetStyle(l);
                                });

                                $('.tbodyContent').remove();
                                var tbody = '<tbody class="tbodyContent">';
                                for (var key in e.target.feature.properties) {
                                    tbody +=
                                        ('<tr class="center aligned"><td>' + key + '</td><td>' + e.target.feature.properties[key] + '</td></tr>');
                                }
                                $('#attribute').append(tbody + '</tbody>');
                                $('#attr').fadeIn(300);
                                map.panTo(e.latlng);

                                if ('setStyle' in e.target) e.target.setStyle({
                                    fillColor: '#FF0',
                                    fillOpacity: 0.6
                                });
                            }
                        });
                    }
                }).addTo(map);
            }
            //读取地图数据
            function loadShpZip() {
                loadshp({
                    url: '${ctx}/sdo/file/view/shape/data?fileId=${fileId}',
                    encoding: "GB2312",
                    EPSG: 4326
                }, function (data) {
                    $('.overlay').toggleClass('effect');
                    $('#wrap').toggleClass('blur');

                    vector.addData(data);
                    map.fitBounds([
                        [data.bbox[1], data.bbox[0]], [data.bbox[3], data.bbox[2]]
                    ]);
                });
            }
            initVector();
            loadShpZip();
//            $('.shp-modal').toggleClass('effect');
            $('.overlay').toggleClass('effect');
            $('#wrap').toggleClass('blur');
//            $('#encoding').dropdown();
            $("#attr").draggable({containment: $(this).parent().parent(), scroll: false, cursor: "move"});
            $('#cancelAttr').click(function () {
                $('#attr').slideUp(300);
            });
        });
        var pdfFileName = '${fileName}';
        document.title = pdfFileName;
    </script>
</div>
</html>
