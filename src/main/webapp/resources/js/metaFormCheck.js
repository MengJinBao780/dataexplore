/**
 * Created by admin on 2016/4/12.
 */
//显示输入错误的提示
function showError(element, msg) {
    var icon = $(element).parent('.input-icon').children('i');
    icon.removeClass('fa-check').addClass("fa-warning");
    icon.attr("data-original-title", msg).tooltip({'container': 'body'});
    $(element)
        .closest('.form-group').removeClass("has-success").addClass('has-error'); // set error class to the control group
}
//显示输入正确的提示
function showSuccess(element) {
    var icon = $(element).parent('.input-icon').children('i');
    $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
    icon.removeClass("fa-warning").addClass("fa-check");
    icon.removeAttr("data-original-title");
}
//去除错误提示信息
function removeError(element) {
    var icon = $(element).parent('.input-icon').children('i');
    $(element).closest('.form-group').removeClass('has-error');
    icon.removeClass("fa-warning");
    icon.removeAttr("data-original-title");
}
//验证表单填写内容
function checkForm(form) {
    $(form).find(".has-error :input").each(function () {
        removeError($(this));
    });
    var noError = true;
    $(form).find(":input.required").each(function (index, input) {
        //如果其父节点是非必填项，当父节点下所有子节点都没有填写，不算错误
        var li = $(input).closest(".jstree-node");
        if (checkNodeIsRequired(li))
            if (!checkRequiredInput($(input)))
                noError = false;
        // li = $(li).parent().closest("li.jstree-node");
        // if (li) {
        //     li = $(li);
        //     //如果父节点为必填项
        //     if (li.hasClass("required")) {
        //         if (!checkRequiredInput($(input)))
        //             noError = false;
        //     } else {//如果父节点为非必填项，需要判断此父节点下其他节点是否填写东西
        //         li.find(":input").each(function (index, item) {
        //             if (($(item).is("select") && $(item).val() != "NAN") || (!$(item).is("select") && $(item).val())) {
        //                 if (!checkRequiredInput($(input)))
        //                     noError = false;
        //             }
        //         });
        //     }
        // }
        // else {
        //     if (!checkRequiredInput($(input)))
        //         noError = false;
        // }
    });
    $(form).find(":input[valueType]:not(.required)").each(function (index, input) {
        if (!checkSpecificTypeNoRequired($(input)))
            noError = false;
    });
    return noError;
}
//验证必填项内容
function checkRequiredInput(input) {
    if (("NAN" != input.val() && input.is("select")) || (!input.is("select") && input.val())) {//填写了值
        var isChecked = true;
        var valueTypeName = "";
        //如果不是string类型，需要对其值进行正则表达式判断
        if (input.attr("valueType")) {
            var type = input.attr("valueType");
            if (type == "int") {
                valueTypeName = "整数";
                if (!intRegex.test(input.val()))
                    isChecked = false;
            }
        }
        if (isChecked) {
            showSuccess(input);
        } else {
            showError(input, "数据格式不正确，应该输入" + valueTypeName);
        }
        return isChecked;
    } else {
        showError(input, "这是必填项！");
        return false;
    }
}
//验证此项是否必填
function checkNodeIsRequired(li) {
    if ($(li).hasClass("display-hide"))
        return false;
    if ($(li).parent().hasClass("jstree-container-ul"))
        return ($(li).hasClass("jstree-leaf") && $(li).find(":input").hasClass("required")) || $(li).hasClass("required");
    if (($(li).hasClass("jstree-leaf") && !$(li).find(":input").hasClass("required")) || (!$(li).hasClass("jstree-leaf") && !$(li).hasClass("required")))//节点并不是必填项
        return false;
    else {
        var parentLi = $(li).parent().closest("li.jstree-node");
        //父节点存在，判断父节点此时是否为必填项
        if (parentLi) {
            //如果父节点此时必填
            if (checkNodeIsRequired($(parentLi))) {
                return true;
            } else {//此时父节点不是必填，判断兄弟节点是否已经填写，如果填写了内容，此项也必须填
                var inputs = $(parentLi).find(":input");
                for (var i = 0; i < inputs.length; i++) {
                    if (($(inputs[i]).is("select") && $(inputs[i]).val() != "NAN") || (!$(inputs[i]).is("select") && $(inputs[i]).val())) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            return true;
        }
    }

}
//验证除必填项外，非string类型录入
function checkSpecificTypeNoRequired(input) {
    if (input.val()) {
        var valueTypeName = "";
        var isChecked = true;
        var type = input.attr("valueType");
        if (type == "int") {
            valueTypeName = "整数";
            if (!intRegex.test(input.val()))
                isChecked = false;
        }
        if (isChecked) {
            removeError(input);
        } else {
            showError(input, "数据格式不正确，应该输入" + valueTypeName);
        }
        return isChecked;
    } else {
        removeError(input);
        return true;
    }
}
//提交表单操作
// $("#submitBtn").click(function (e) {
//    
// });
function generateTreeFormData(form) {
    var data = {};
    var root = $(form).find(".jstree");
    if (root.children("ul")) {
        var children = root.children("ul").children("li.jstree-node").not("li[originId]");
        for (var i = 0; i < children.length; i++) {
            getNodeData($(children[i]), data);
        }
        // root.children("ul").children("li.jstree-node").not("li[originId]").each(function(){
        //     getNodeData($(this),data);
        // });
    }

    //xiajl20171101增加模板信息数据
    console.log('aaaa');
    var templateInfo = {};
    if ($("#isTemplate")){
        templateInfo.isTemplate = $("#isTemplate").prop("checked");
    }
    templateInfo.templateName = $("#templateName").val();
    templateInfo.memo = $("#memo").val();
    if ($("#templateId")){
        templateInfo.templateId = $("#templateId").val();

    }
    data.templateInfo = templateInfo;

    return data;
}
function getNodeData(li, nodeData) {
    var countBtn = li.children("a.jstree-anchor").find(".copy");
    if (li.hasClass("jstree-leaf")) {//叶子节点
        var formGroupDiv = li.find("div.form-group");
        var value;
        if (countBtn && countBtn.length != 0 && countBtn.attr("count") != "1") {
            value = getCopiedNodeData(li);
        } else {
            value = formGroupDiv.find(":input").val();
            if (value == 'NAN')
                value = '';
        }
        nodeData[li.attr("name")] = value;
        return nodeData;
    } else {
        var value;
        if (countBtn && countBtn.length != 0 && countBtn.attr("count") != "1") {
            value = getCopiedNodeData(li);
        } else {
            var childData = {};
            var children = li.children("ul.jstree-children").children("li.jstree-node").not("li[originId]");
            for (var i = 0; i < children.length; i++) {
                getNodeData($(children[i]), childData);
            }
            value = childData;
        }
        nodeData[li.attr("name")] = value;
    }
}
function getCopiedNodeData(li) {
    var value = [];
    if (li.hasClass("jstree-leaf")) {
        var formGroupDiv = li.find("div.form-group");
        value.push(formGroupDiv.find(":input").val());
        var id = li.attr("id");
        var siblings = li.siblings("li[originId='" + id + "']");
        for (var i = 0; i < siblings.length; i++) {
            var v = $(siblings[i]).find("div.form-group :input").val();
            if (v == 'NAN')
                v = '';
            value.push(v);
        }
        // li.siblings("li[originId='"+id+"']").each(function(){
        //     value.push($(this).find("div.form-group :input").val());
        // });
    } else {
        var nodeData = {};
        var children = li.children("ul.jstree-children").children("li.jstree-node").not("li[originId]");
        for (var i = 0; i < children.length; i++) {
            getNodeData($(children[i]), nodeData);
        }
        value.push(nodeData);
        var id = li.attr("id");
        var siblings = li.siblings("li[originId='" + id + "']");
        for (var i = 0; i < siblings.length; i++) {
            var siblingData = {};
            var siblingChildren = $(siblings[i]).children("ul.jstree-children").children("li.jstree-node").not("li[originId]");
            for (var j = 0; j < siblingChildren.length; j++) {
                getNodeData($(siblingChildren[j]), siblingData);
            }
            value.push(siblingData);
        }
    }
    return value;
}