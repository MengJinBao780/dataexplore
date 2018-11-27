/**
 * Created by liuan on 2016/4/19.
 */
'use strict';
var initNextTab = function (tab, navigation, index, container) {
    if (index == 1 && !$("#coreMetaTree").hasClass("jstree")) {
        if (edit) {
            generateTreeFormWithData(container)
        } else {
            generateTreeForm(container);
        }
    }
};
var generateTreeForm = function (container) {
    $.ajax({
        //url: ctx + "/loadMetaStruct/" + metaStructType,
        //xiajl20171026
        url: ctx + "/loadMetaStruct",
        type: "get",
        dataType: "json",
        beforeSend: function () {
            Metronic.blockUI({
                target: '#tab2',
                boxed: true,
                textOnly: true
            });
        },
        success: function (data) {
            $("#coreMetaTree").empty();
            if (data) {//生成页面
                generateTree($("#coreMetaTree"), data);
                Metronic.unblockUI('#tab2');
                if (jQuery().datepicker) {
                    $('.date-picker').datepicker({
                        rtl: Metronic.isRTL(),
                        orientation: "left",
                        autoclose: true,
                        language: "zh-CN",
                        format: "yyyy-mm-dd"
                    });
                    //$('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                }
                $(".datetime-picker").datetimepicker({
                    locale: "zh-cn",
                    format: "YYYY-MM-DD HH:mm:ss",
                    useCurrent: true
                });
                if ($(".jstree-node .copy"))
                    $(".jstree-node .copy").unbind("click");
                $(".jstree-node .copy").click(function (e) {
                    e.preventDefault();
                    var currentTarget = $(e.currentTarget);
                    copyNode(currentTarget);
                });
                //当必填项文本框失去焦点时，对文本框内容进行校验
                $(".jstree-node :input.required").blur(function (e) {
                    var input = $(e.currentTarget);
                    var li = $(input).closest(".jstree-node");
                    if (checkNodeIsRequired(li))
                        checkRequiredInput(input);
                });
                $(".jstree-node :input[valueType]:not(.required)").change(function (e) {
                    var input = $(e.currentTarget);
                    checkSpecificTypeNoRequired(input);
                });
                $(".jstree-node :input").change(function () {
                    $(container).find(".button-save").removeAttr("disabled");
                });
            } else {//还没有同步schema信息
                Metronic.unblockUI('#tab2');
                bootbox.alert("管理员还没有上传核心元数据规范文件，无法填写核心元数据！");
                $(container).bootstrapWizard("previous");
            }
        },
        error: function () {
            Metronic.unblockUI('#tab2');
            bootbox.alert("网络发生错误！");
            $(container).bootstrapWizard("previous");
        }
    });
};
var generateTreeFormWithData = function (container) {
    $.ajax({
        //url: ctx + "/currentCoreMeta/" + $("#resourceId").val() + "/" + metaStructType,
        //xiajl20171030
        url: ctx + "/currentCoreMeta/" + $("#resourceId").val(),
        type: "get",
        dataType: "json",
        beforeSend: function () {
            Metronic.blockUI({
                target: '#tab2',
                boxed: true,
                textOnly: true
            });
        },
        success: function (result) {
            $("#coreMetaTree").empty();
            if (result.xsdData) {
                var xsdData = result.xsdData;
                generateTree($("#coreMetaTree"), xsdData);
                if (result.metaData)
                    bindTreeData($("#coreMetaTree"), result.metaData);
                Metronic.unblockUI('#tab2');
                if (jQuery().datepicker) {
                    $('.date-picker').datepicker({
                        rtl: Metronic.isRTL(),
                        orientation: "left",
                        autoclose: true,
                        language: "zh-CN",
                        format: "yyyy-mm-dd"
                    });
                    //$('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                }
                if ($(".jstree-node .copy"))
                    $(".jstree-node .copy").unbind("click");
                $(".jstree-node .copy").click(function (e) {
                    e.preventDefault();
                    var currentTarget = $(e.currentTarget);
                    copyNode(currentTarget);
                });
                //当必填项文本框失去焦点时，对文本框内容进行校验
                $(".jstree-node :input.required").blur(function (e) {
                    var input = $(e.currentTarget);
                    checkRequiredInput(input);
                });
                $(".jstree-node :input[valueType]:not(.required)").change(function (e) {
                    var input = $(e.currentTarget);
                    checkSpecificTypeNoRequired(input);
                });
                $(".jstree-node :input").change(function () {
                    $(container).find(".button-save").removeAttr("disabled");
                });
            } else {
                Metronic.unblockUI('#tab2');
                bootbox.alert("核心元数据规范文件丢失，无法修改核心元数据！");
                $(container).bootstrapWizard("previous");
            }
        },
        error: function () {
            Metronic.unblockUI('#tab2');
            bootbox.alert("网络发生错误！");
            $(container).bootstrapWizard("previous");
        }
    });
};
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
var checkCurrentTab = function (tab, navigation, index, container) {
    var r = true;
    if (index == 1) {
        r = checkContentForm();
    }
    if (!r)
        return r;
    if ($(container).find(".button-save").attr("disabled"))
        return true;
    r = saveCurrentPageData(index - 1, container);
    return r;
};
var saveCurrentPageData = function (index, container) {
    if (index == 1) {
        var r;
        if ($("#resourceId").val()) {
            var noError = checkForm($("#coreMetaForm"));
            if (noError) {
                var data = generateTreeFormData($("#coreMetaForm"));
                //xiajl20171030
                console.info("data:" + JSON.stringify(data));

                $.ajax({
                    url: ctx + "/coreMeta/" + $("#resourceId").val(),
                    type: "POST",
                    data: {meta: JSON.stringify(data)},
                    dataType: "json",
                    async: false,
                    beforeSend: function () {
                        Metronic.blockUI({
                            target: '#tab2',
                            boxed: true,
                            message: '保存中...'
                        });
                    },
                    success: function (result) {
                        if (result.head.code == 200) {
                            toastr["success"]("保存成功！", "数据保存");
                            $(container).find(".button-save").attr("disabled", true);
                            r = true;
                        } else {
                            toastr["error"]("保存失败！", "数据保存");
                            r = false;
                        }
                    },
                    error: function () {
                        toastr["error"]("保存失败！", "数据保存");
                        r = false;
                    },
                    complete: function () {
                        Metronic.unblockUI('#tab2');
                    }
                });
            } else {//当有错误时，弹出错误提示，并把光标定位到第一个有错误的提示框内
                toastr["error"]("您提交的表单中有错误，请根据填写框上的提示进行修改！", "表单错误");
                $("#coreMetaForm").find(".jstree-node .form-group.has-error").find(":input")[0].focus();
                r = false;
            }
        } else {
            r = false;
        }
        return r;
    } else if (index == 0) {

        // return true;
        //xiajl20171026
        return saveDataResourcefunc();
    }
};

function viewResource(resourceId) {
    window.location.href = ctx + '/resourceDetail/' + resourceId;
}

function editResource(resourceId) {
    window.location.href = ctx + '/editResource/' + resourceId;
}

function deleteResource(resourceId, callback) {
    bootbox.confirm("确定要删除此资源吗？", function (r) {
        if (r) {
            $.ajax({
                url: ctx + "/resource/" + resourceId,
                type: "delete",
                dataType: "json",
                success: function (result) {
                    if (result.head.code == 200) {
                        toastr["success"]("删除成功！", "数据删除");
                        callback();
                    } else {
                        toastr["error"]("删除失败！", "数据删除");
                    }
                },
                error: function () {
                    toastr["error"]("删除失败！", "数据删除");
                }
            });
        }
    });
}

function publicFunction() {
    $("#unpublicDiv").html("");
    $("#COIList").html("");
}

function unpublicFunction() {
    /*$.ajax({
        type: "POST",
        url: ctx + '/approve/findAllCOIList',
        dataType: "json",
        async: false,
        success: function (data) {
            var coiHtml = "<div class='form-group'> <label class='control-label col-md-3'>选择COI分类 </label> " +
                "<div class='col-md-9'> <select class='form-control select2me' name='coiList' id='coiList' multiple> ";
            for (var r in data) {
                coiHtml += "<option value='" + data[r].COIID + "'>" + data[r].COIName + "</option>";
            }
            coiHtml += "</select> </div> </div>";
            $("#COIList").html(coiHtml);
            coiSelect2 = $('#coiList').select2({
                placeholder: "请选择COI分类",
                allowClear: true
            });
        },
        error: function () {
            toastr["error"]("COI加载失败！", "加载失败");
        }
    });*/
    $.ajax({
        type: "POST",
        url: ctx + '/approve/showGroupAndSecurity',
        dataType: "json",
        async: false,
        success: function (data) {
            var html = "<div class='form-group'> <label class='control-label col-md-3'>选择群组 </label>" +
                " <div class='col-md-9'> " +
                "<select class='form-control select2me' name='authorizedUGroup' id='authorizedUGroup' multiple>";
            for (var g in data.group) {
                html += "<option value='" + data.group[g].groupId + "'>" + data.group[g].groupName + "</option>";
            }
            html += "</select> </div> </div>" +
                "<div class='form-group'> <label class='control-label col-md-3'>可访问人员密级 </label> " +
                "<div class='col-md-9'> <select class='form-control select2me' name='securityLevel' id='securityLevel'> " +
                "<option value='非涉密'>非涉密</option>" +
                "<option value='一般涉密'>一般涉密</option>" +
                "<option value='重要涉密'>重要涉密</option>" +
                "<option value='核心涉密'>核心涉密</option>";
            "</select> </div> </div>";
            $("#unpublicDiv").html(html);
            /*userGroupSelect2 = $('#authorizedUGroup').select2({
                placeholder: "请选择群组",
                allowClear: true
            });
            securitySelect2 = $('#securityLevel').select2({
                placeholder: "请选择人员密集",
                allowClear: true
            });*/
        },
        error: function () {
        }
    });

}

function unpassFunction() {
    var html = "<div class='form-group'> <label class='control-label col-md-3'>审批意见 <span class='required'> * </span> </label> " +
        "<div class='col-md-9'> " +
        "<textarea type='text' class='form-control' name='innerAuditMemo' id='innerAuditMemo' rows='3'></textarea> </div> </div>";
    $("#nopassDiv").html(html);
    $("#passDiv").html("");
    $("#unpublicDiv").html("");
    $("#COIList").html("");
}

function passFunction() {
    var html = "<label class='control-label col-md-3'>是否为公共资源</label> <div class='radio-list col-md-9'> " +
        "<label class='radio-inline' style='margin-left:25px;'> <input type='radio' name='isPublic' onclick='publicFunction();' id='public' value='是'>是</label> " +
        "<label class='radio-inline' style='margin-left:45px;'> <input type='radio' name='isPublic' onclick='unpublicFunction();' id='unpublic' value='否'> 否 </label> </div>";
    $("#passDiv").html(html);
    $("#nopassDiv").html("");
}

function setResTitle(obj) {
    if ($("#coreMetaTree").hasClass("jstree") && $("#coreMetaTree").find("input[name='resTitle']").length > 0) {
        $("#coreMetaTree").find("input[name='resTitle']").val($(obj).val());
    }
}

function updateResource(resourceId) {
    $("#updateInfoForm")[0].reset();
    $("#resourceId").val(resourceId);
    $("#updateInfoModal").modal('show');
}

$("#updateInfoBtn").click(function () {
    $.ajax({
        url: ctx + "/resource/updateInfo",
        type: "post",
        dataType: "json",
        data: $("#updateInfoForm").serialize(),
        success: function (result) {
            if (result.head.code == 200) {
                toastr['success']("更新成功");
                $("#updateInfoModal").modal('hide');
            } else {
                toastr['error']("更新失败！");
            }
        },
        error: function () {
            toastr['error']("更新失败！");
        }
    });
});