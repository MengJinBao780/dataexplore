//AMap
var mainMap;
var slider = document.getElementById('timeSlider');

var date = new Date;
var year = 2020;
var timeMin = year - 50;
var timeMax = year;
var TBstartTime = document.getElementById("tr_starttime");
var TBendTime = document.getElementById("tr_endtime");



//显示高德地图
function showMap(centerX, centerY) {
    mainMap = new AMap.Map("container", {
        zoom: 5, //地图图层级别，PC默认3-18
        viewMode: '3D',
        center: [centerX, centerY], //地图中心点
        //layers: [new AMap.TileLayer.Satellite()],     //固定设置为卫星图
        resizeEnable: true,
        rotateEnable: false
    });

    //设置可切换图层(卫星图和2D模型图)
    mainMap.plugin(["AMap.MapType","AMap.MouseTool"], function() {
        //地图类型切换
        var type = new AMap.MapType({
            defaultType: 0 //使用卫星地图
        });
        mainMap.addControl(type);
        mouseTools = new AMap.MouseTool(mainMap);
        AMap.event.addListener(mouseTools,'draw',function(e){
            if(e.obj.getPath == undefined){
                return
            }
            var paths = e.obj.getPath();
            console.log(paths)
            if(paths.length == 4){
                if(paths[0].lng>paths[2].lng){
                    $("#x1").val(paths[2].lng)
                    $("#x2").val(paths[0].lng)
                }else {
                    $("#x1").val(paths[0].lng)
                    $("#x2").val(paths[2].lng)
                }
                if(paths[0].lat>paths[2].lat){
                    $("#y1").val(paths[2].lat)
                    $("#y2").val(paths[0].lat)
                }else {
                    $("#y1").val(paths[0].lat)
                    $("#y2").val(paths[2].lat)
                }
            }
            sendGetDataReq();
            mouseTools.close();

            /* AMapUI.loadUI(['overlay/SimpleMarker'], function(SimpleMarker) {
                 for(var i=0;i<paths.length;i++){
                     new SimpleMarker({
                         //普通文本
                         iconLabel: i + 1,
                         map: map,
                         position: paths[i]
                     });
                 };
             });*/
            /*setTimeout(function () {
                map.clearMap();
            },50)*/
        });
    });

    //显示标尺
    mainMap.plugin(["AMap.Scale"], function() {
        var scale = new AMap.Scale({
            position: "LB"
        });
        mainMap.addControl(scale);
    });

    //加载工具条
    mainMap.plugin(["AMap.ToolBar"], function() {
        var tool = new AMap.ToolBar({
            position: "RB"
        });
        mainMap.addControl(tool);
    });
}

//在指定位置显示矩形
function SetRetanglePlace(southWestX, southWestY, northEastX, northEastY, num) {
    var southWest = new AMap.LngLat(southWestX, southWestY);
    var northEast = new AMap.LngLat(northEastX, northEastY);

    var bounds = new AMap.Bounds(southWest, northEast);

    var rectangle = new AMap.Rectangle({
        map: mainMap,
        bounds: bounds,
        strokeColor: 'yellow',
        strokeWeight: 5,
        strokeOpacity: 0.5,
        strokeDasharray: [30, 10],
        strokeStyle: [0, 0, 0],
        //fillColor: 'blue',
        fillOpacity: 0.0,
        zIndex: 10,
        cursor: 'pointer',
        bubble: false,
    })

    //在矩形区域中间添加字符 --begin
    var posX = (southWestX + northEastX) / 2;
    var posY = (southWestY + northEastY) / 2;
    var textPlace = new AMap.Text({
        text: num,
        textAlign: 'center', // 'left' 'right', 'center',
        verticalAlign: 'middle', //middle 、bottom
        draggable: false,
        cursor: 'pointer',
        angle: 0,
        style: {
            //此处可以适当调整为 背景透明而文字不透明
            'color': 'yellow',
            'background-color': "transparent",
            'border': 'none',
            "font-size": "22px"
            //'padding':'10px 20px'
        },
        position: [posX, posY]
    });
    textPlace.setMap(mainMap);
    //end

    //添加新的矩形后将中心点平移,并设置地图显示级别
    mainMap.setZoomAndCenter(14, [posX, posY]);
}

//显示时间滑动条控件
function showSliderBar() {


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


    // console.log("TBstartTime.value:" + TBstartTime.value);
    // console.log("TBendTime.value" + TBendTime.value);

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

window.onload = function() {
    showSliderBar();

    var TBstartTime = document.getElementById("tr_starttime");
    var TBendTime = document.getElementById("tr_endtime");
    TBstartTime.onblur = function() {
        changeTime(true);
    }
    TBendTime.onblur = function() {
        changeTime(false);
    }

    $('#hiddenPanel').click(function() {
        $('#searchbar').animate({ left: "-830px" }, "slow");
        $('#content').animate({ left: "-830px" }, "slow");

    });

    $('#showPanel').click(function() {
        $('#searchbar').animate({ left: "0px" }, "slow");
        $('#content').animate({ left: "0px" }, "slow");
    });

    console.log("ready to show AMap.");

    //测试默认显示以天安门为中心的地图
    showMap(116.40, 39.92);

    //测试天安门附近的矩形
    /*SetRetanglePlace(116.389533, 39.910878, 116.405124, 39.925799, "1");*/
}