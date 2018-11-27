/**
 * Created by admin on 2016/4/19.
 */
'use strict';
var isXmlService = false;
var tableColInfo;//数据表的列信息，用于自动填充接口返回参数信息

var initDataTable = function (container, cols) {
    function restoreRow(oTable, nRow) {
        var aData = oTable.fnGetData(nRow);
        var jqTds = $('>td', nRow);

        for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
            oTable.fnUpdate(aData[i], nRow, i, false);
        }

        oTable.fnDraw();
    }

    function editRow(oTable, nRow) {
        var aData = oTable.fnGetData(nRow);
        var jqTds = $('>td', nRow);
        jqTds[0].innerHTML = '<input type="text" name="paramName" class="form-control input-sm" value="' + aData[0] + '">';
        var select = '<select name="paramType" class="form-control input-sml">' +
            '<option value="number">数值</option>' +
            '<option value="string">字符串</option>' +
            '<option value="date">日期</option>' +
            '</select>';
        jqTds[1].innerHTML = select;
        if (aData[1])
            $(jqTds[1]).find("input").val(aData[1]);
        jqTds[2].innerHTML = '<input type="text" name="paramOrder" class="form-control input-sm" value="' + aData[2] + '">';
        jqTds[3].innerHTML = '<input type="text" name="paramDescr" class="form-control input-sm" value="' + aData[3] + '">';

        // var selectOperator = '<select name="paramOperator" class="form-control input-sml">' +
        //     '<option value="与">与</option>' +
        //     '<option value="或">或</option>' +
        //     '</select>';
        // jqTds[4].innerHTML = selectOperator;
        // if (aData[4]) {
        //     $(jqTds[4]).find("input").val(aData[4]);
        // }
        jqTds[4].innerHTML = '<a class="btn btn-xs blue edit" href="">保存</a><a class="btn btn-xs yellow cancel" href="">取消</a>';
    }

    function saveRow(oTable, nRow) {
        var jqInputs = $(':input', nRow);
        if (jqInputs[0].value) {

            if (!checkInt(jqInputs[2].value)) {
                $(jqInputs[2]).attr("style", "border-color:red");
                return false;
            }
            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            // oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
            oTable.fnUpdate('<a class="btn btn-xs blue edit" href="">修改</a><a class="btn btn-xs red delete" href="">删除</a>', nRow, 4, false);
            oTable.fnDraw();
            $('#form_wizard_1').find(".button-save").removeAttr("disabled");
            return true;
        }
        else {
            $(jqInputs[0]).attr("style", "border-color:red");
            return false;
        }
    }

    function cancelEditRow(oTable, nRow) {
        var jqInputs = $(':input', nRow);
        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
        oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
        oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
        // oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
        oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 4, false);
        oTable.fnDraw();
    }

    var table = $(container);

    var oTable = table.dataTable({

        // Uncomment below line("dom" parameter) to fix the dropdown overflow issue in the datatable cells. The default datatable layout
        // setup uses scrollable div(table-scrollable) with overflow:auto to enable vertical scroll(see: assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js).
        // So when dropdowns used the scrollable div should be removed.
        //"dom": "<'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>",

        searching: false,
        paging: false,
        "info": false,
        "scrollY": "200px",
        "language": {
            "zeroRecords": "",
            "infoEmpty": "",
            "emptyTable": "没有数据"
        },
        "order": [
            [2, "asc"]
        ] // set first column as a default sort by asc
    });

    var tableWrapper = $(container);

    tableWrapper.find(".dataTables_length select").select2({
        showSearchInput: false //hide search box with special css class
    }); // initialize select2 dropdown

    var nEditing = null;
    var nNew = false;

    $('#content .add').click(function (e) {
        e.preventDefault();

        if (nNew && nEditing) {
            if (confirm("之前添加的数据尚未保存，是否先保存?")) {
                if (saveRow(oTable, nEditing)) {
                    nEditing = null;
                    nNew = false;
                } else
                    return;
            } else {
                oTable.fnDeleteRow(nEditing); // cancel
                nEditing = null;
                nNew = false;

                return;
            }
        }

        var aiNew = oTable.fnAddData(['', '', '', '', '', '', '']);
        var nRow = oTable.fnGetNodes(aiNew[0]);
        editRow(oTable, nRow);
        nEditing = nRow;
        nNew = true;
    });

    table.on('click', '.delete', function (e) {
        e.preventDefault();

        if (confirm("确定要删除这条数据?") == false) {
            return;
        }

        var nRow = $(this).parents('tr')[0];
        cols[$(nRow).children("td")[0].innerHTML].value = 1;
        oTable.fnDeleteRow(nRow);
        $('#form_wizard_1').find(".button-save").removeAttr("disabled");
    });

    table.on('click', '.cancel', function (e) {
        e.preventDefault();
        if (nNew) {
            oTable.fnDeleteRow(nEditing);
            nEditing = null;
            nNew = false;
        } else {
            restoreRow(oTable, nEditing);
            nEditing = null;
        }
    });

    table.on('click', '.edit', function (e) {
        e.preventDefault();

        /* Get the row as a parent of the link that was clicked on */
        var nRow = $(this).parents('tr')[0];

        if (nEditing !== null && nEditing != nRow) {
            /* Currently editing - but not this row - restore the old before continuing to edit mode */
            restoreRow(oTable, nEditing);
            editRow(oTable, nRow);
            nEditing = nRow;
        } else if (nEditing == nRow && this.innerHTML == "保存") {
            /* Editing this row and want to save it */
            if (saveRow(oTable, nEditing)) {
                nEditing = null;
                nNew = false;
            }
        } else {
            /* No edit in progress - let's start one */
            editRow(oTable, nRow);
            nEditing = nRow;
        }
    });
};


var curIsChangeDS = false;
var curDsId;
var curTablePublicType;

function initThird() {

    $("#content").html($("#manual").html());
    initDataTable($("#content .paramTable"), {});
    $('#form_wizard_1').find(".button-save").removeAttr("disabled");
    curTablePublicType = null;

}

function saveDataResourcefunc() {
    if (checkContentForm()) {
        var formData = $("#submit_form").serializeArray();
        var dataServiceJson = {}, resource = {};
        for (var i = 0; i < formData.length; i++) {
            if (formData[i].name == 'classId') {
                resource.localCatalogId = formData[i].value;
            } else if (formData[i].name == 'resState') {
                resource.resState = formData[i].value;
            } else if (formData[i].name == 'resTitle') {
                resource.resName = formData[i].value;
            } else if (formData[i].name == 'license') {
                resource.licId = formData[i].value;
                continue;
            } else if (formData[i].name == 'centerCatalogId') {
                resource.centerCatalogId = formData[i].value;
                continue;
            } else if (formData[i].name == 'localCatalogId') {
                resource.localCatalogId = formData[i].value;
                continue;
            } else if (formData[i].name == 'linkUrl') {
                dataServiceJson.uri = formData[i].value;
                continue;
            } else if (formData[i].name == 'returnType') {
                dataServiceJson.dataType = formData[i].value;
                continue;
            } else if (formData[i].name == 'linkExplain') {
                dataServiceJson.explains = formData[i].value;
                continue;
            } else {
                dataServiceJson[formData[i].name] = formData[i].value;
            }
        }
        if (!isXmlService) {
            var trs = $("#content .paramTable").find("tbody tr");
            var params = [];
            for (var i = 0; i < trs.length; i++) {
                var param = {};
                var tds = $(trs[i]).children("td");
                param.paramName = tds[0].innerHTML;
                param.paramType = tds[1].innerHTML;
                param.paramOrder = tds[2].innerHTML;
                param.paramDescr = tds[3].innerHTML;
                // param.paramOperator = tds[4].innerHTML;
                params.push(param);
            }
            dataServiceJson.dataServiceParamList = params;
        }
        resource.dataResourceThird = dataServiceJson;
        resource.resType = '第三方接口';
        if ($("#resourceId").val())
            resource.resourceId = $("#resourceId").val();
        var r = true;
        var options = {
            url: ctx + '/resource',
            async: false,
            dataType: "json",
            data: {"resource": JSON.stringify(resource)},
            beforeSend: function () {
                Metronic.blockUI({
                    target: '#tab1',
                    boxed: true,
                    message: '保存中...'
                });
            },
            success: function (data) {
                if (data.head.code == 200) {
                    $("#resourceId").val(data.body.resourceId);
                    $("#form_wizard_1").find(".button-save").attr("disabled", true);
                    toastr["success"]("保存成功！", "数据保存");
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
                Metronic.unblockUI("#tab1");
            }
        };
        $('#submit_form').ajaxSubmit(options);
        // $.ajax({
        //     url: ctx + "/resource",
        //     type: "post",
        //     dataType: "json",
        //     data: JSON.stringify(resource),
        //     async: false,
        //     contentType: "application/json;charset=utf-8",
        //     beforeSend: function () {
        //         Metronic.blockUI({
        //             target: '#tab1',
        //             boxed: true,
        //             message: '保存中...'
        //         });
        //     },
        //     success: function (result) {
        //         if (result.head.code == 200) {
        //             $("#resourceId").val(result.body.resourceId);
        //             $("#form_wizard_1").find(".button-save").attr("disabled", true);
        //             toastr["success"]("保存成功！", "数据保存");
        //             r = true;
        //         } else {
        //             toastr["error"]("保存失败！", "数据保存");
        //             r = false;
        //         }
        //     },
        //     error: function () {
        //         toastr["error"]("保存失败！", "数据保存");
        //         r = false;
        //     },
        //     complete: function () {
        //         Metronic.unblockUI('#tab1');
        //     }
        // });
        return r;
    } else {
        toastr["error"]("您提交的表单中有错误，请根据填写框上的提示进行修改！", "表单错误");
        return false;
    }
}

function checkContentForm() {
    $("#submit_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block help-block-error', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        ignore: "", // validate all fields including form hidden input
        rules: {
            resTitle: {
                required: true
            },
            // classId: {
            //     required: true
            // },
            enclosureType: {
                required: true
            },
            // dataSourceId: {
            //     required: true
            // },
            // dataTableName: {
            //     required: true,
            // },
            linkUrl: {
                required: true
            },
            returnType: {
                required: true
            }
        },
        messages: { // custom messages for radio buttons and checkboxes
            resTitle: {
                required: "请输入名称"
            },
            // classId: {
            //     required: "请选择分类"
            // },
            // dataSourceId: {
            //     required: "请选择数据源"
            // },
            // dataTableName: {
            //     required: "请选择数据表"
            // },
            linkUrl: {
                required: "请填写接口地址"
            }
        },

        errorPlacement: function (error, element) { // render error placement for each input type
            if (element.parent(".input-group").size() > 0) {
                error.insertAfter(element.parent(".input-group"));
            } else {
                error.insertAfter(element); // for other inputs, just perform default behavior
            }
        },
        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        }
    });
    var r = $("#submit_form").valid();
    if (!r)
        return r;
    if (!isXmlService) {
        var tbody = $("#content .paramTable").children("tbody");
        if (tbody.find("td.dataTables_empty").length > 0) {
            tbody.find("td.dataTables_empty").html('<span style="color: red">请输入参数列表！</span>');
            return false;
        }
    }
    return true;
}

function initInterfaceInfoInTree(tree) {
    var servicePortNode = $(tree).find("li[name='ServicePort']");
    $(servicePortNode).find("li[name='portURLFormat']").find(":input").val($("#linkUrl").val());
    $(servicePortNode).find("li[name='requestMode']").find(":input").val("GET");
    if (!isXmlService) {
        var trs = $("#content .paramTable").find("tbody tr"), requestParamsNode;
        for (var i = 0; i < trs.length; i++) {
            if (i == 0)
                requestParamsNode = $(servicePortNode).find("li[name='RequestParameter']");
            else {
                requestParamsNode = copyNode($(servicePortNode).find("li[name='RequestParameter']").find("a.copy"));
            }
            var tds = $(trs[i]).children("td");
            $(requestParamsNode).find("li[name='parameterName']").find(":input").val(tds[0].innerHTML);
            $(requestParamsNode).find("li[name='parameterType']").find(":input").val(tds[1].innerHTML);
            $(requestParamsNode).find("li[name='parameterMandatory']").find(":input").val("必选");
            $(requestParamsNode).find("li[name='parameterDescription']").find(":input").val(tds[3].innerHTML);
        }

        if ($("#submit_form").find("input[name='enclosureType'][value='手动封装']").parent().hasClass("checked")) {
            $(servicePortNode).find("li[name='requestExample']").find(":input").val($("#example").val());
            $(servicePortNode).find("li[name='returnExample']").find(":input").val($("#returnDemo").val());
            $(servicePortNode).find("li[name='portDescription']").find(":input").val($("#linkExplain").val());
        } else {
            var i = 0;
            for (var key in tableColInfo) {
                var colInfo = tableColInfo[key], returnParameterNode;
                if (i == 0) {
                    returnParameterNode = $(servicePortNode).find("li[name='ReturnParameter']");
                } else {
                    returnParameterNode = copyNode($(servicePortNode).find("li[name='ReturnParameter']").find("a.copy"));
                }
                $(returnParameterNode).find("li[name='parameterName']").find(":input").val(key);
                $(returnParameterNode).find("li[name='parameterType']").find(":input").val(colInfo["type"]);
                $(returnParameterNode).find("li[name='parameterMandatory']").find(":input").val("必选");
                $(returnParameterNode).find("li[name='parameterDescription']").find(":input").val(colInfo["comment"] ? colInfo["comment"] : "无");
                i++;
            }
        }
    }
    $(servicePortNode).find("a.copy").hide();
    $(servicePortNode).find("a.delete").hide();
    $(servicePortNode).find(":input").attr("readonly", true).attr("disabled", true);
}