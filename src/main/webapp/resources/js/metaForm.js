/**
 * Created by admin on 2016/4/14.
 */
function generateTree(container, xsdData) {
    $("#rootDescription").html(xsdData.description);
    var ul = $("<ul/>");
    if (xsdData.childNodes) {
        $.each(xsdData.childNodes, function (index, item) {
            // if(item.nodeName!="ServicePort")
            ul.append(generateTreeNode(item));
        });
    }
    $(container).append(ul);
    $(container).jstree({
        "core": {
            "themes": {
                "responsive": false,
                "icons": false,
                "stripes": false,
                "animation": 0
            },
            "multiple ": false,
            "dblclick_toggle": false,
            "check_callback": true
        }
    }).bind("select_node.jstree", function (event, selected) {
        $("#" + selected.selected[0]).find(":input").focus();
    }).bind("ready.jstree", function () {
        $(container).find(".jstree-leaf .jstree-anchor").removeAttr("href");
        //if (metaStructType == 'AFDMService')
        //    initInterfaceInfoInTree($(container));
    });
}
function bindTreeData(tree, metaData) {
    var childern = $(tree).children("ul").children("li.jstree-node");
    for (var i = 0; i < childern.length; i++) {
        bindTreeNodeData($(childern[i]), metaData);
    }
}
function bindTreeNodeData(node, nodeData) {
    var nodeName = node.attr("name");
    if (nodeName == "ServicePort")
        return;
    nodeData = nodeData[nodeName];
    if (nodeData) {
        //如果是叶子节点
        if ($(node).hasClass("jstree-leaf")) {
            if (isArrayFn(nodeData)) {//这个叶子节点是可重复的
                var copyLink = $(node).children("a.jstree-anchor").find(".copy");
                if (copyLink.length == 0)
                    return;
                $(node).find(":input").val(nodeData[0]);
                for (var i = 1; i < nodeData.length; i++) {
                    var nodeCopy = copyNode($(copyLink));
                    $(nodeCopy).find(":input").val(nodeData[i]);
                }
            } else {
                if (nodeName == 'resTitle' && $("#resTitle").length > 0) {
                    $(node).find(":input").val($("#resTitle").val());
                    if ($("#resTitle").val() != nodeData) {
                        $(".button-save").removeAttr("disabled");
                    }
                }
                else
                    $(node).find(":input").val(nodeData);
            }
        } else {
            if (isArrayFn(nodeData)) {//这个节点是可以复制的
                var copyLink = $(node).children("a.jstree-anchor").find(".copy");
                if (copyLink.length == 0)
                    return;
                var nodeInFactData = {};
                nodeInFactData[nodeName] = nodeData[0];
                bindTreeNodeData($(node), nodeInFactData);
                for (var i = 1; i < nodeData.length; i++) {
                    var nodeCopy = copyNode($(copyLink));
                    nodeInFactData[nodeName] = nodeData[i];
                    bindTreeNodeData($(nodeCopy), nodeInFactData);
                }

            } else {
                var children = $(node).children("ul.jstree-children").children("li.jstree-node");
                for (var i = 0; i < children.length; i++) {
                    bindTreeNodeData($(children[i]), nodeData);
                }
            }
        }
    }
}
function copyNode(currentTarget) {
    var originNode = $(currentTarget).parent().parent().parent();
    var isCloneFunc = true;
    if ($("#" + originNode.attr("id")).hasClass("jstree-leaf"))
        isCloneFunc = false;
    if ($("#" + originNode.attr("id")).find(".datetime-picker").length > 0) {
        $(".datetime-picker").data("DateTimePicker").destroy();
    }
    if ($("#" + originNode.attr("id")).find(".date-picker").length > 0) {
        $(".date-picker").datepicker("destroy");
    }
    var nodeCopy = $("#" + originNode.attr("id")).clone(isCloneFunc);
    //修改复制节点的按钮类别
    nodeCopy.children("a.jstree-anchor").find(".copy").attr("class", "delete");
    nodeCopy.children("a.jstree-anchor").find(".delete .fa").attr("class", "fa fa-minus-square");
    if (nodeCopy.attr("aria-expanded") || nodeCopy.attr("aria-expanded") === "false") {
        nodeCopy.children("i.jstree-ocl").addClass("copied");
    }
    //增加节点计数
    var count = Number($(currentTarget).attr("count"));
    count++;
    $(currentTarget).attr("count", "" + count + "");
    //修改各个子节点的id
    if (isCloneFunc) {
        nodeCopy.find(".jstree-node").each(function (index, item) {
            var originId = $(item).attr("id");
            $(item).attr("id", originId + "_copy");
            var oldOriginId = $(item).attr("originId");
            if (oldOriginId) {
                // $(item).attr("originId", oldOriginId + "_copy");
                $("#" + oldOriginId + "_copy").children("a.jstree-anchor").find(".copy").attr("count", "1");
                $(item).remove();
            }
        });
    } else {//修改复制节点内input名称和节点的id
        var inputName = nodeCopy.find(":input").attr("name");
        nodeCopy.find(":input").attr("name", inputName + "-" + count);
    }
    nodeCopy.find(":input").val("");
    //去除拷贝节点上的输入正确和错误提示
    nodeCopy.find(":input").each(function (index, item) {
        var icon = $(item).parent('.input-icon').children('i');
        $(item).closest('.form-group').removeClass('has-error').removeClass('has-success');
        icon.removeClass("fa-warning").removeClass("fa-check");
        icon.removeAttr("data-original-title");
    });
    var nodeId = nodeCopy.attr("id");
    nodeCopy.attr("id", nodeId + "-" + count);
    nodeCopy.attr("originId", nodeId);
    if (nodeCopy.find(".jstree-anchor").hasClass("jstree-hovered"))
        nodeCopy.find(".jstree-anchor").removeClass("jstree-hovered");
    var insertPosition;
    var siblings = $("#" + originNode.attr("id")).siblings("li[originId='" + originNode.attr("id") + "']")
    if (siblings && siblings.length != 0)
        insertPosition = $(siblings[siblings.length - 1]);
    else
        insertPosition = $("#" + originNode.attr("id"));
    if (insertPosition.hasClass("jstree-last")) {
        insertPosition.removeClass("jstree-last");
        nodeCopy.addClass("jstree-last");
    }
    var select = nodeCopy.find(".form-group select");
    if (select && select.length != 0)
        select.val("NAN");
    nodeCopy.insertAfter(insertPosition);
    if ($("#" + originNode.attr("id")).find(".datetime-picker").length > 0) {
        $(".datetime-picker").datetimepicker({
            locale: "zh-cn",
            format: "YYYY-MM-DD HH:mm:ss",
            useCurrent: true
        });
    }
    if ($("#" + originNode.attr("id")).find(".date-picker").length > 0) {
        $(".date-picker").datepicker({
            rtl: Metronic.isRTL(),
            orientation: "left",
            autoclose: true,
            language: "zh-CN",
            format: "yyyy-mm-dd",
            todayHighlight: true
        });
    }
    if ($(".jstree-node .delete"))
        $(".jstree-node .delete").unbind("click");
    $(".jstree-node .delete").click(function (e) {
        e.preventDefault();
        var li = $(e.currentTarget).parent().parent().parent();
        var originId = li.attr("originId");
        var count = $("#" + originId).find(".copy").attr("count");
        count--;
        $("#" + originId).children("a.jstree-anchor").find(".copy").attr("count", count);
        if (li.hasClass("jstree-last")) {
            if (count == 1) {
                $("#" + originId).addClass("jstree-last");
            } else {
                $("#" + originId + "-" + count).addClass("jstree-last");
            }
        }
        deleteNode(li);
    });
    if ($(".jstree-node .jstree-ocl.copied"))
        $(".jstree-node .jstree-ocl.copied").unbind("click");
    $(".jstree-node .jstree-ocl.copied").click(function (e) {
        var li = $(e.currentTarget).parent();
        if (li.attr("aria-expanded") === "true") {
            li[0].className = li[0].className.replace('jstree-open', 'jstree-closed');
            li.attr("aria-expanded", false).children('.jstree-children').addClass("display-hide");
        } else {
            li[0].className = li[0].className.replace('jstree-closed', 'jstree-open');
            li.children('.jstree-children').removeClass("display-hide");
            li[0].setAttribute("aria-expanded", true);
        }
    });
    return nodeCopy;
}
function deleteNode(obj) {
    $(obj).remove();
}
function generateTreeNode(xsdNode) {
    var li = $('<li name="' + xsdNode.nodeName + '"/>');
    var mustSingnalSpan = $('<span class="required" style="color: #e02222">*</span>');
    if (xsdNode.multiple) {
        li.append('<div style="float:right;margin-top: 5px"><a href="#" class="copy" count="1"><i class="fa fa-plus-square"></i></a></div>');
    }
    if (xsdNode.childNodes) {//有子节点
        var span = $("<span/>");
        span.append(xsdNode.nodeTitle || xsdNode.description);
        span.attr("style", "font-size:14px;font-weight:bold");
        li.append(span);
        if (xsdNode.must) {
            li.append(mustSingnalSpan);
            li.addClass("required");
        }
        li.attr("data-jstree", '{ "opened" : true }');
        var ul = $("<ul/>");
        $.each(xsdNode.childNodes, function (index, item) {
            ul.append(generateTreeNode(item));
        });
        li.append(ul);
    } else {//叶子节点
        var formGroupDiv = $('<div/>');
        formGroupDiv.attr("class", "form-group");
        var label = $('<label/>');
        label.attr("class", "control-label col-md-3");

        if (xsdNode.must) {
            label.append(xsdNode.nodeTitle);
            label.append(mustSingnalSpan);
        } else {
            label.append(xsdNode.nodeTitle);
        }
        formGroupDiv.append(label);
        var inputDiv = $('<div class="col-md-8"></div>');
        var iconInputDiv = $('<div class="input-icon right"></div>');
        iconInputDiv.append('<i class="fa"></i>');
        if (xsdNode.options) {//下拉框
            var select = $('<select name="' + xsdNode.nodeName + '" class="form-control input-sm"></select>');
            select.append('<option value="NAN" selected>--请选择--</option>');
            $.each(xsdNode.options, function (index, item) {
                select.append('<option value="' + item + '">' + item + '</option>');
            });
            iconInputDiv.append(select);
        } else if (xsdNode.nodeType == "date") {
            iconInputDiv.append('<input class="form-control form-control-inline input-sm date-picker" name="' + xsdNode.nodeName + '" size="16" type="text" value=""/>');
        } else if (xsdNode.nodeType == "text") {
            iconInputDiv.append('<textarea type="text" class="form-control input-sm" valueType="' + xsdNode.nodeType + '" name="' + xsdNode.nodeName + '" title="' + xsdNode.description + '"></textarea>');
        } else if (xsdNode.nodeType == "datetime") {
            iconInputDiv.append('<input type="text" class="form-control form-control-inline input-sm datetime-picker" name="' + xsdNode.nodeName + '" title="' + xsdNode.description + '"/>');
        } else {
            var resTitle = '';
            if (xsdNode.nodeName == 'resTitle' && $("#resTitle").length > 0) {
                resTitle = $("#resTitle").val();
                iconInputDiv.append('<input type="text" class="form-control input-sm" name="' + xsdNode.nodeName +
                    '" title="' + xsdNode.description + '" value="' + resTitle + '" readonly/>');
            } else
                iconInputDiv.append('<input type="text" class="form-control input-sm" name="' + xsdNode.nodeName +
                '" title="' + xsdNode.description + '"/>');
        }
        if (xsdNode.must) {
            iconInputDiv.find(":input").addClass("required");
        }
        inputDiv.append(iconInputDiv);
        formGroupDiv.append(inputDiv);
        li.append(formGroupDiv);
        //资源id和元数据id通过程序自动生成
        if (xsdNode.nodeName == 'mdID' || xsdNode.nodeName == 'resID') {
            li.addClass("display-hide");
        }
    }
    return li;
}