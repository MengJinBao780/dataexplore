var mainMap;
var mapRectangle;
var slider = document.getElementById('rateSlider');

//显示默认的卫星图
function showAMap(centerX, centerY) {
    mainMap = new AMap.Map("mapContioner", {
        zoom: 4, //地图图层级别，PC默认3-18
        /*viewMode: '3D',*/
        center: [centerX, centerY], //地图中心点
        /*layers: [new AMap.TileLayer.Satellite({zIndex:0})], //固定设置为卫星图*/
        resizeEnable: true,
        rotateEnable: false,
        // dragEnable: false
    });
    mainMap.plugin(["AMap.MouseTool"], function() {
        mouseTools = new AMap.MouseTool(mainMap);
    });
    /*AMap.event.addListener(mouseTools, 'draw', function (e) { //����¼�\
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

        for (var i = 0; i < paths.length; i++) {
            new AMap.Marker({
                position: paths[i],
                title: paths[i],
                map: mainMap
            });
        }
        ;
        $(".rect-box").removeClass('rect-active');

    });*/
}

//显示可拖动的范围矩形
function showMapRetangle(southWestX, southWestY, northEastX, northEastY) {
    var southWest = new AMap.LngLat(southWestX, southWestY);
    var northEast = new AMap.LngLat(northEastX, northEastY);

    var bounds = new AMap.Bounds(southWest, northEast);

    mapRectangle = new AMap.Rectangle({
        map: mainMap,
        bounds: bounds,
        strokeColor: 'Red',
        strokeWeight: 5,
        strokeOpacity: 0.5,
        strokeDasharray: [30, 10],
        strokeStyle: [0, 0, 0],
        fillColor: 'white',
        fillOpacity: 0.3,
        zIndex: 10,
        // cursor: 'hand',
        bubble: false
    })

    refreshRangeBox();

    mainMap.plugin(['AMap.RectangleEditor'], function() {
        var RectangleEditor = new AMap.RectangleEditor(mainMap, mapRectangle);
        RectangleEditor.open();

        AMap.event.addListener(RectangleEditor, "adjust", function() {
            refreshRangeBox();
        });
    })
}

//根据地图中的矩形刷新输入框中的经纬度
function refreshRangeBox() {
    var currentSouthWest = mapRectangle.getBounds().getSouthWest();
    var currentNorthEast = mapRectangle.getBounds().getNorthEast();

    var curSWLg = currentSouthWest.getLng();
    var curSWLt = currentSouthWest.getLat();
    var curNELg = currentNorthEast.getLng();
    var curNELt = currentNorthEast.getLat();

    //test
    // console.log("curSWLg:" + curSWLg);
    // console.log("curSWLt:" + curSWLt);
    // console.log("curNELg:" + curNELg);
    // console.log("curNELt:" + curNELt);

    $("#eastLng").val(curNELg);
    $("#westLng").val(curSWLg);
    $("#northLat").val(curNELt);
    $("#southLat").val(curSWLt);
}

//校验范围输入框中的经纬度，并刷新地图中的矩形区域
function updateRangeBox(type) {
    // console.log(type);
    var elg = $("#eastLng").val();
    var wlg = $("#westLng").val();
    var nlt = $("#northLat").val();
    var slt = $("#southLat").val();

    if (type === "east") {
        if (elg < wlg) {
            $("#eastLng").val(wlg);
        }
    }
    if (type === "west") {
        if (elg < wlg) {
            $("#westLng").val(elg);
        }
    }
    if (type === "north") {
        if (nlt < slt) {
            $("#northLat").val(slt);
        }
    }
    if (type == "south") {
        if (nlt < slt) {
            $("#southLat").val(nlt);
        }
    }

    if (elg < 0) {
        $("#eastLng").val(0);
    }
    if (elg > 180) {
        $("#eastLng").val(180);
    }
    if (wlg < 0) {
        $("#westLng").val(0);
    }
    if (wlg > 180) {
        $("#westLng").val(180);
    }
    if (nlt < 0) {
        $("#northLat").val(0);
    }
    if (nlt > 90) {
        $("#northLat").val(90);
    }
    if (slt < 0) {
        $("#southLat").val(0);
    }
    if (slt > 90) {
        $("#southLat").val(90);
    }

    /*changeMapRetangle($("#westLng").val(), $("#southLat").val(), $("#eastLng").val(), $("#northLat").val());*/
}

//重设矩形位置
function changeMapRetangle(southWestX, southWestY, northEastX, northEastY) {
    var southWest = new AMap.LngLat(southWestX, southWestY);
    var northEast = new AMap.LngLat(northEastX, northEastY);

    var bounds = new AMap.Bounds(southWest, northEast);
    mapRectangle.setBounds(bounds);
}


//显示云覆盖率滑动条
function showRateSlider() {
    noUiSlider.create(slider, {
        start: [0, 100],
        connect: true,
        step: 1,
        range: {
            'min': 0,
            'max': 100
        }
    });

    $("#startrate").val(0);
    $("#stoprate").val(100);

    slider.noUiSlider.on('change.one', function(values, handle, unencoded, tap, positions) {
        var startrate = parseInt(values[0]);
        var endrate = parseInt(values[1]);

        $("#startrate").val(startrate);
        $("#stoprate").val(endrate);
    });
}

//检验覆盖率输入范围并刷新滑动条
function updateRate(isStartRate) {

    var TBstartRate = $("#startrate").val();
    var TBstopRate = $("#stoprate").val();
    TBstartRate = parseInt(TBstartRate);
    TBstopRate = parseInt(TBstopRate);

    if (isStartRate) {
        if (TBstartRate < 0) {
            TBstartRate = 0;
        }
        if (TBstartRate > TBstopRate) {
            TBstartRate = TBstopRate;
        }
    } else {
        if (TBstopRate < TBstartRate) {
            TBstopRate = TBstartRate;
        }
        if (TBstopRate > 100) {
            TBstopRate = 100;
        }
    }

    $("#startrate").val(TBstartRate);
    $("#stoprate").val(TBstopRate);

    slider.noUiSlider.set([$("#startrate").val(), $("#stoprate").val()]);
}


$("document").ready(function() {
    showRateSlider();

/*
    $("#eastLng").blur(function() {
        updateRangeBox("east");
    });
    $("#westLng").blur(function() {
        updateRangeBox("west");
    });
    $("#northLat").blur(function() {
        updateRangeBox("north");
    });
    $("#southLat").blur(function() {
        updateRangeBox("south");
    });
*/

    $("#startrate").blur(function() {
        updateRate(true);
    });
    $("#stoprate").blur(function() {
        updateRate(false);
    });

    //测试默认显示以天安门为中心的地图
    showAMap(116.40, 39.92);

    //测试天安门附近的矩形
    /*showMapRetangle(100.389533, 32.910878, 120.405124, 39.925799);*/

});