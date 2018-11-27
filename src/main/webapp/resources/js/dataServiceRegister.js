/**
 * Created by admin on 2016/4/19.
 */
'use strict';
var isXmlService = false;
var tableColInfo;//数据表的列信息，用于自动填充接口返回参数信息
var initDataTable = function (container, type, cols) {
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
        if (type == '自动封装') {
            jqTds[0].innerHTML = '<select name="paramName" class="form-control input-sm"></select>';
            for (var key in cols) {
                if (cols[key].value == 1)
                    $(jqTds[0]).find('select').append('<option value="' + key + '">' + key + '</option>');
            }
            if (aData[0]) {
                $(jqTds[0]).find('select').append('<option value="' + aData[0] + '">' + aData[0] + '</option>');
                $(jqTds[0]).find('select').val(aData[0]);
            }
        } else
            jqTds[0].innerHTML = '<input type="text" name="paramName" class="form-control input-sm" value="' + aData[0] + '">';
        if (type == '手动封装') {
            var select = '<select name="paramType" class="form-control input-sml">' +
                '<option value="number">数值</option>' +
                '<option value="string">字符串</option>' +
                '<option value="date">日期</option>' +
                '</select>';
            jqTds[1].innerHTML = select;
            if (aData[1])
                $(jqTds[1]).find("input").val(aData[1]);
        }
        jqTds[2].innerHTML = '<input type="text" name="paramOrder" class="form-control input-sm" value="' + aData[2] + '">';
        jqTds[3].innerHTML = '<input type="text" name="paramDescr" class="form-control input-sm" value="' + aData[3] + '">';

        var selectOperator = '<select name="paramOperator" class="form-control input-sml">' +
            '<option value="与">与</option>' +
            '<option value="或">或</option>' +
            '</select>';
        jqTds[4].innerHTML = selectOperator;
        if (aData[4])
            $(jqTds[4]).find("input").val(aData[4]);

        jqTds[5].innerHTML = '<a class="btn btn-xs blue edit" href="">保存</a><a class="btn btn-xs yellow cancel" href="">取消</a>';
    }

    function saveRow(oTable, nRow) {
        var jqInputs = $(':input', nRow);
        if (jqInputs[0].value) {
            if (type == '自动封装') {
                if (!checkInt(jqInputs[1].value)) {
                    $(jqInputs[1]).attr("style", "border-color:red");
                    return false;
                }
            } else {
                if (!checkInt(jqInputs[2].value)) {
                    $(jqInputs[2]).attr("style", "border-color:red");
                    return false;
                }
            }
            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            if (type == '自动封装') {
                oTable.fnUpdate(cols[jqInputs[0].value].type, nRow, 1, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 4, false);
            }
            else {
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
            }
            oTable.fnUpdate('<a class="btn btn-xs blue edit" href="">修改</a><a class="btn btn-xs red delete" href="">删除</a>', nRow, 5, false);
            oTable.fnDraw();
            if (type == '自动封装')
                cols[jqInputs[0].value].value = 0;
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
        oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
        oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 5, false);
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


function getDataSourceList(pageNo) {
    $.ajax({
        type: "GET",
        url: ctx + '/relationalDatabaseList',
        data: {currentPage: pageNo},
        dataType: "json",
        success: function (data) {
            var html = template("dataSourceTmpl", data);
            $('#content .dataSourceList').html(html);
            var totalPages = data.totalPages;
            var currentPage = data.currentPage;
            $("#content .currentPageNo").html(currentPage);
            $("#content .totalPages").html(totalPages);
            $('#content .pagination').bootpag({
                total: totalPages,
                page: currentPage,
                maxVisible: 5,
                leaps: true,
                firstLastUse: true,
                first: '←',
                last: '→',
                wrapClass: 'pagination',
                activeClass: 'active',
                disabledClass: 'disabled',
                nextClass: 'next',
                prevClass: 'prev',
                lastClass: 'last',
                firstClass: 'first'
            }).on('page', function (event, num) {
                getDataSourceList(num);
            });
        }
    });
}

function chooseTable(dsId) {
    var parentObj = $("#dataSourceId_" + dsId);
    var isChangeDS = false;//和上次选取数据源是否有发生变化
    $("#content .dataSourceTable").hide();//隐藏数据源选取table
    if (dsId != $("#content .dataSourceId").val()) { ///和上次选取数据源是否有发生变化，则进入
        $("#content .dataSourceId").val(dsId);
        $("#content .dataSourceName").html(parentObj.children().eq(1).html());//数据源展示值修改
        if (parentObj.children().eq(2).html() != 'xml') {
            isXmlService = false;
            // getTableList(dsId);先注释掉，初始化用户展示表list、选取表的属性、检索条件等等
        }
        else {
            isXmlService = true;
        }
        $('#form_wizard_1').find(".button-save").removeAttr("disabled");
        isChangeDS = true;
    } else {

    }
    if (!isXmlService) {//数据库服务
        $("#content .dataSourceName").show();//数据源名称
        $("#content .selectDS").show();//数据源重选按钮
        $('#content .dataTableNameInputGroup').show();
        $('#content .paramsInputGroup').show();
        dataSourceSelectView(isChangeDS, dsId);
        // $("#content .dataTableNameInputGroup").show(); 用户展示表list，选取表的属性，检索条件等等 注释掉
    } else {//xml服务
        $('#content .dataTableName').empty();
        $('#content .dataTableName').append('<option value="xml" selected>xml</option>');
    }
}

var curIsChangeDS = false;
var curDsId;
var curTablePublicType;

/**
 * 选取关系型的数据源后，进行的操作
 * @param isChangeDS
 * @param dsId
 */
function dataSourceSelectView(isChangeDS, dsId) {
    curIsChangeDS = isChangeDS;
    curDsId = dsId;
    curTablePublicType = "整表发布";
    if ($("#content .tablePublicTypeInputGroup").length == 0) {//插入发布方式 按钮 展示
        $("#content #dataSourceIdFormGroupId").after(template("tablePublicTypeInputGroupTmpl"));
        $(".tablePublicTypeInputGroup input:radio").uniform(); //初始化radio

    }
    $("#tablePublicTableId").attr("checked", "true");
    $.uniform.update();
    $(".tablePublicTypeInputGroup").show(); //展示该页面

    if ($("#content .dataTableNameInputGroup").length == 0) { //选择的数据表展示
        $("#content .tablePublicTypeInputGroup").after(template("dataTableNameInputGroupTmpl"));
    }

    if ($("#content .paramsInputGroup").length == 0) { //数据表的检索条件table展示
        $("#content .dataTableNameInputGroup").after(template("paramsInputGroupTmpl"));
    }
    selectTablePublicType("整表发布");
}

function selectTablePublicType(tablePublicType, isChangeSelectTablePublicType) {
    if (tablePublicType == '整表发布') {
        $('#content .publicSqlGroup').hide();//sql语句input
        if (curIsChangeDS || isChangeSelectTablePublicType) {
            getTableList(curDsId);//先注释掉，初始化用户展示表list、选取表的属性、检索条件等等
        } else {
            // $("#content .dataTableNameInputGroup").show(); //表选取list选择框
            if ($(".paramsInputGroup").length != 0) {//初始化 检索筛选条件table
                $("#content .paramsInputGroup").remove();
            }
            $("#content .dataTableNameInputGroup").after(template("paramsInputGroupTmpl"));
            $('#content .paramsInputGroup').show();

        }
        $("#content .dataTableNameInputGroup").show(); //表选取list选择框

        // $('#content .paramsInputGroup').show(); //表检索条件table
        curTablePublicType = "整表发布";
        // validSqlServiceResult = false;
    } else if (tablePublicType == '自定义发布') {
        curTablePublicType = "自定义发布";
        // validSqlServiceResult = false;
        $('#content .dataTableNameInputGroup').hide();//表选取list选择框
        $('#content .paramsInputGroup').hide();//表检索条件table

        if ($("#content .publicSqlGroup").length == 0) { //sql语句input初始化
            $("#content .tablePublicTypeInputGroup").after(template("publicSqlGroupTmpl"));
        }
        $('#content .publicSqlGroup').show();//sql语句input
    }
    validSqlServiceResult = false;
    curIsChangeDS = false;
}

/**
 *sql形式发布数据服务,字段注释编辑与预览
 */
function publicSqlService() {
    if (!validSqlService()) {
        toastr["warning"]("提示！", "请先检查填写sql语句");
        return;
    }
    var sqlStr = $("#publicSql").val();
    var dataSourceId = $("#dataSourceId").val();
    staticSourceTableChoice(2, null, dataSourceId, sqlStr, "dataService");
}

var validSqlServiceResult = false;

/**
 *sql形式发布数据服务,SQL正确性检测
 */
function validSqlService() {
    var sqlStr = $("#publicSql").val();
    var dataSourceId = $("#dataSourceId").val();
    var result = false;
    $.ajax({
        type: "GET",
        url: ctx + "/sqlValidation",
        async: false,
        data: {sqlStr: sqlStr, dataSourceId: dataSourceId},
        dataType: "json",
        success: function (data) {
            result = data.result;
        }
    });
    validSqlServiceResult = result;
    return validSqlServiceResult;
}

/**
 *
 * @param dsId
 * @param tableName
 */
$(function () {
    $("#editTableFieldComsCloseId").bind("click", closeSqlServiceModel);
    $("#editTableFieldComsSaveId").bind("click", closeSqlServiceModel);
});

/**
 * 关闭编辑预览model后
 */
function closeSqlServiceModel() {
    if (curRefer == null || curRefer != "dataService" || curSQL == null) {
        return;
    }

    var dataResult = null;
    $.ajax({
        type: "GET",
        url: ctx + '/getSQLFieldComsForDataService',
        data: {"dataSourceId": curDataSourceId, "sqlStr": curSQL},
        dataType: "json",
        async: false,
        success: function (dataRes) {
            if (!dataRes || !dataRes.colInfos) {
                return;
            }
            dataResult = dataRes.colInfos;
            if ($(".paramsInputGroup").length != 0) {//初始化 检索筛选条件table
                $("#content .paramsInputGroup").remove();
            }
            $("#content .dataTableNameInputGroup").after(template("paramsInputGroupTmpl"));
            $('#content .paramsInputGroup').show();
            initDataTable($("#content .paramTable"), "自动封装", dataResult);
            tableColInfo = dataResult;
        }
    });
    // curDataSourceId = null;
    // curSQL = null;
    // curRefer = null;

}

function getTableCols(dsId, tableName) {
    $.ajax({
        type: "GET",
        url: ctx + '/tableCols/' + dsId + '/' + tableName,
        dataType: "json",
        success: function (data) {
            {
                if ($(".paramsInputGroup").length != 0) {//初始化 检索筛选条件table
                    $("#content .paramsInputGroup").remove();
                }
                $("#content .dataTableNameInputGroup").after(template("paramsInputGroupTmpl"));
                $('#content .paramsInputGroup').show();
            }
            initDataTable($("#content .paramTable"), "自动封装", data);
            tableColInfo = data;
        }
    });
}

/**
 * 编辑表字段的注释
 * @param dsId
 * @param tableName
 */
function getAndEditTableColsComs(obj, dsId, tableName) {

    staticSourceTableChoice(1, obj, dsId, tableName, "dataService");
}


function getTableList(dsId) { //展示选取数据库的表，下拉框展示
    $.ajax({
        type: "GET",
        url: ctx + '/relationalDatabaseTableList',
        data: {dataSourceId: dsId},
        dataType: "json",
        success: function (data) {
            $('#content .dataTableName').empty();
            for (var i = 0; i < data.list.length; i++) {
                $('#content .dataTableName').append('<option value="' + data.list[i] + '">' + data.list[i] + '</option>');
            }
            changeTable($('#content .dataTableName'));
        }
    });
}

function changeDS(obj) {
    $("#content .dataSourceTable").show();
    $("#content .dataSourceName").hide();
    $("#content .selectDS").hide();
    $('#content .dataTableNameInputGroup').hide();
    $('#content .paramsInputGroup').hide();
}

function changeTable(obj) {
    getTableCols($("#content .dataSourceId").val(), $(obj).val());
    getAndEditTableColsComs(obj, $("#content .dataSourceId").val(), $(obj).val());
    $('#form_wizard_1').find(".button-save").removeAttr("disabled");
}

function changeEnclosureType() {
    if ($(".tablePublicTypeInputGroup").length == 0) {
        $("#content #dataSourceIdFormGroupId").after(template("tablePublicTypeInputGroupTmpl")); //插入发布方式 按钮
        $(".tablePublicTypeInputGroup input:radio").uniform(); //初始化radio
    } else {
        $(".tablePublicTypeInputGroup").hide();
    }
    if ($(".dataTableNameInputGroup").length == 0) { //选择的数据表展示
        $("#content .tablePublicTypeInputGroup").after(template("dataTableNameInputGroupTmpl"));
    } else {
        $(".dataTableNameInputGroup").hide();
    }
    if ($(".paramsInputGroup").length == 0) { //数据表的检索条件table展示
        $("#content .dataTableNameInputGroup").after(template("paramsInputGroupTmpl"));
    } else {
        $(".paramsInputGroup").hide();
    }
    getDataSourceList(1);
    $('#form_wizard_1').find(".button-save").removeAttr("disabled");

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
            } else if (formData[i].name == 'dataSourceId') {
                resource.dataSourceId = formData[i].value;
                continue;
            } else if (formData[i].name == 'dataTableName' && curTablePublicType == "整表发布") {
                dataServiceJson.publicContent = formData[i].value;
            } else if (formData[i].name == 'publicSql' && curTablePublicType == "自定义发布") {
                dataServiceJson.publicContent = formData[i].value;
            } else if (formData[i].name == 'centerCatalogId') {
                resource.centerCatalogId = formData[i].value;
                continue;
            } else if (formData[i].name == 'localCatalogId') {
                resource.localCatalogId = formData[i].value;
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
                param.paramOperator = tds[4].innerHTML;
                params.push(param);
            }
            dataServiceJson.dataServiceParamList = params;
            if (curTablePublicType == "自定义发布") {
                dataServiceJson.publicType = "sql";
                dataServiceJson.fieldComs = $("#publicSql").attr("coms");
            } else {
                dataServiceJson.publicType = "table";
                dataServiceJson.fieldComs = $(".dataTableNameInputGroup select[name=dataTableName]").attr("coms");
            }
        }
        resource.dataResourceServ = dataServiceJson;
        resource.resType = '数据服务';
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
            classId: {
                required: true
            },
            // enclosureType: {
            //     required: true
            // },
            dataSourceId: {
                required: true
            },
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
            classId: {
                required: "请选择分类"
            },
            dataSourceId: {
                required: "请选择数据源"
            },
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
    if (curTablePublicType == "自定义发布" && !validSqlServiceResult
        || ($("#content .paramsInputGroup").length == 0)
        || ($('#content .paramsInputGroup').is(":hidden"))) {
        toastr["error"]("失败！", "请先完成编辑预览sql语句");
        return false;
    }
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
    if ($("#submit_form").find("input[name='enclosureType'][value='自动封装']").parent().hasClass("checked")) {
        var serviceUrl = window.location.protocol + "//" + window.location.host + "/resource/" + $("#resourceId").val();
        $(servicePortNode).find("li[name='portURLFormat']").find(":input").val(serviceUrl);
    } else {
        $(servicePortNode).find("li[name='portURLFormat']").find(":input").val($("#linkUrl").val());
    }
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