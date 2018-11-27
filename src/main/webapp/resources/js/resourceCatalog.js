/**
 * Created by liuan on 2016/4/26.
 */
function initLocalResourceCatalogTree(container, classId) {
    $.ajax({
        url: ctx + "/getLocalResCatalogList",
        type: "get",
        dataType: "json",
        async:false,
        success: function (data) {
            if (data.length == 0) {
                bootbox.alert("注册员还没有编辑分类信息，暂时不能注册数据");
                window.location.href = ctx;
            } else {
                var jstreeData = parseDataToJstreeData(data);
                $(container).jstree({
                    "core": {
                        'multiple': false,
                        'force_text': true,
                        "expand_selected_onload": true,
                        'dblclick_toggle': false,
                        'check_callback': false,
                        'data': jstreeData
                    }
                }).bind("select_node.jstree", function (event, selected) {
                    $(".button-save").removeAttr("disabled");
                    $("#localCatalogId").val(selected.node.id);
                }).bind("ready.jstree", function () {
                    if (classId) {
                        var jstree = $.jstree.reference("#localjstree-demo");
                        var node = jstree.get_node(classId, true);
                        jstree.select_node(node);
                    }
                    $(container).find(".jstree-anchor").each(function(){
                        $(this).attr("style","width:200px");
                    });
                });

            }
        }
    });

}

function initCenterResourceCatalogTree(container, classId) {
    $.ajax({
        url: ctx + "/getCenterResCatalogList",
        type: "get",
        dataType: "json",
        async:false,
        success: function (data) {
            if (data.length == 0) {
                bootbox.alert("注册员还没有编辑分类信息，暂时不能注册数据");
                window.location.href = ctx;
            } else {
                var jstreeData = parseDataToJstreeData(data);
                $(container).jstree({
                    "core": {
                        'multiple': false,
                        'force_text': true,
                        "expand_selected_onload": true,
                        'dblclick_toggle': false,
                        'check_callback': false,
                        'data': jstreeData
                    }
                }).bind("select_node.jstree", function (event, selected) {
                    $(".button-save").removeAttr("disabled");
                    $("#centerCatalogId").val(selected.node.id);
                }).bind("ready.jstree", function () {
                    if (classId) {
                        var jstree = $.jstree.reference("#jstree-demo");
                        var node = jstree.get_node(classId, true);
                        jstree.select_node(node);
                    }
                    $("#jstree-demo").find(".jstree-anchor").each(function(){
                        $(this).attr("style","width:200px");
                    });
                });


            }

        }
    });


}


function parseDataToJstreeData(data) {
    var jstreeData = [];
    for (var i = 0; i < data.length; i++) {
        var node = {};
        node.id = data[i].id;
        node.icon = "jstree-folder";
        node.text = data[i].name;
        if (data[i].parentid == 0) {
            node.parent = '#';
            rootId = node.id;
        } else {
            node.parent = data[i].parentid;
        }
        var state = {};
        state.opened = true;
        node.state = state;
        jstreeData.push(node);
    }


    if (data.length == 1)
        selectedId = data[0].id;
    return jstreeData;
}