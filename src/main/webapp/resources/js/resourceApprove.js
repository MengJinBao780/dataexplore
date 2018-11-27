/**
 * Created by huangwei on 2016/4/26.
 */
'use strict';
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
function viewResource(resourceId) {
    window.location.href = ctx + '/resourceDetail/' + resourceId;
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
                "<option value='主要涉密'>重要涉密</option>" +
                "<option value='核心涉密'>核心涉密</option>";
            "</select> </div> </div>";
            $("#unpublicDiv").html(html);
            userGroupSelect2 = $('#authorizedUGroup').select2({
                placeholder: "请选择群组",
                allowClear: true
            });
            securitySelect2 = $('#securityLevel').select2({
                placeholder: "请选择角色",
                allowClear: true
            });
        },
        error: function () {
        }
    });

}

function unpassFunction() {
    var html = "<div class='form-group'> <label class='control-label col-md-3'>审批意见 <span class='required'> * </span> </label> " +
        "<div class='col-md-9'> " +
        "<textarea type='text' class='form-control' name='innerAuditMemo'id='innerAuditMemo'rows='3'></textarea> </div> </div>";
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
