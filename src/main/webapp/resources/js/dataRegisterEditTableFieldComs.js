/**
 * Created by Administrator on 2017/4/26 0026.
 */

var curEditIsChoiceTableOrSql = 0; //1:table;2:sql
var curSourceTableChoice = null;
var curDataSourceId = null;
var curTableName = null;
var curSQL = null;
var curSQLStrIndex = 0;
var sqlNum = 1;
var curRefer = null;
/**
 *
 * @param obj
 * @param dataSourceId
 * @param tableName
 */
function staticSourceTableChoice(editIsChoiceTableOrSql, obj, dataSourceId, tableNameOrSql, refer) {
    if (refer == "dataService" || !obj || obj.checked) {
        $('#editTableFieldComsId').html("");
        $('#previewTableDataAndComsId').html("");

        $('#editTableDataAndComsButtonId').parent().removeClass("active");
        $('#previewTableDataAndComsButtonId').parent().removeClass("active");

        $('#editTableFieldComsId').removeClass("active");
        $('#previewTableDataAndComsId').removeClass("active");

        $('#editTableDataAndComsButtonId').parent().addClass("active");
        $('#editTableFieldComsId').addClass("active");
        var tableInfosList = null;
        if (editIsChoiceTableOrSql == 1) {
            var tableInfos = getTableFieldComs(dataSourceId, tableNameOrSql);
            tableInfosList = [];
            tableInfosList[0] = {tableName: tableNameOrSql, tableInfos: tableInfos};
        } else if (editIsChoiceTableOrSql == 2) {
            var tableInfosMap = getSqlFieldComs(dataSourceId, tableNameOrSql);
            var i = 0;
            tableInfosList = [];
            for (var key in tableInfosMap) {
                tableInfosList [i++] = {tableName: key, tableInfos: tableInfosMap[key]};
            }
        }
        if (!tableInfosList || tableInfosList.length == 0) {
            if (editIsChoiceTableOrSql == 2) {
                toastr["warning"]("提示！", "请先检查填写sql语句");
            }
            return;
        }
        $("#staticSourceTableChoiceModal").modal("show");
        var html = template("editTableFieldComsTmpl", {"tableInfosList": tableInfosList});
        $('#editTableFieldComsId').html(html);
        curSourceTableChoice = obj;
        curDataSourceId = dataSourceId;
        curEditIsChoiceTableOrSql = editIsChoiceTableOrSql;
        curRefer = refer;
        if (editIsChoiceTableOrSql == 1) {
            curTableName = tableNameOrSql;
        } else if (editIsChoiceTableOrSql == 2) {
            curSQL = tableNameOrSql;
        }
        preSaveEditTableFieldComs();// 页面与保存coms信息
    } else {
        $(obj).removeAttr("coms");
    }
    $("#form_wizard_1").find(".button-save").removeAttr("disabled");

}

function getTableFieldComs(dataSourceId, tableName) {
    var dataResult = null;
    $.ajax({
        type: "GET",
        url: ctx + '/getTableFieldComs',
        data: {"dataSourceId": dataSourceId, "tableName": tableName, "timestamp": Date.parse(new Date())},
        dataType: "json",
        async: false,
        success: function (data) {
            if (!data || !data.tableInfos) {
                return;
            }
            dataResult = data.tableInfos;
        }
    });
    return dataResult;
}

function getSqlFieldComs(dataSourceId, sqlStr) {
    var dataResult = null;
    $.ajax({
        type: "GET",
        url: ctx + '/getSQLFieldComs',
        data: {"dataSourceId": dataSourceId, "sqlStr": sqlStr},
        dataType: "json",
        async: false,
        success: function (data) {
            if (!data || !data.tableInfos) {
                return;
            }
            dataResult = data.tableInfos;
        }
    });
    return dataResult;
}

/**
 * 页面与保存coms信息
 */
function preSaveEditTableFieldComs() {
    if (curTableName || curSQL) {
        var fieldComsList = getEditTableOrSqlFieldComs();
        var tableInfos = $.toJSON(fieldComsList);
        if (curEditIsChoiceTableOrSql == 1) {
            $(curSourceTableChoice).attr("coms", tableInfos.toString());
        } else if (curEditIsChoiceTableOrSql == 2) {
            if (curSQLStrIndex == 0) {
                $("#sqlStr").attr("coms", tableInfos.toString());
                if ($("#publicSql").length == 1) {
                    $("#publicSql").attr("coms", tableInfos.toString());
                }
            } else {
                $("#sqlStr" + curSQLStrIndex).attr("coms", tableInfos.toString());
            }
        }
    }
}

/**
 * 保存coms信息
 */
function saveEditTableFieldComs() {
    if (curTableName || curSQL) {
        var fieldComsList = getEditTableOrSqlFieldComs();
        var tableInfos = $.toJSON(fieldComsList);
        if (curEditIsChoiceTableOrSql == 1) {
            $(curSourceTableChoice).attr("coms", tableInfos.toString());
            saveTableFieldComs(curDataSourceId, fieldComsList);
        } else if (curEditIsChoiceTableOrSql == 2) {
            if (curSQLStrIndex == 0) {
                $("#sqlStr").attr("coms", tableInfos.toString());
            } else {
                $("#sqlStr" + curSQLStrIndex).attr("coms", tableInfos.toString());
            }
            saveSQLFieldComs(curDataSourceId, curSQL, fieldComsList);
        }
        curSourceTableChoice = null;
        curTableName = null;
        curEditIsChoiceTableOrSql = 0;
        // if (curRefer != "dataService") {
        curDataSourceId = null;
        curSQL = null;
        curRefer = null;
        // }

    }
}

$(function () {


    $("#previewTableDataAndComsButtonId").bind("click", function () {
        if (curEditIsChoiceTableOrSql == 1) {
            var tableInfos = getEditTableOrSqlFieldComs();
            previewTableDataAndComs(curDataSourceId, tableInfos);
        } else if (curEditIsChoiceTableOrSql == 2) {
            var tableInfos = getEditTableOrSqlFieldComs();
            previewSqlDataAndComs(curDataSourceId, tableInfos);
        }
    });

    // $("#editTableFieldComsCancelId").bind("click", function () {
    //     if (curEditIsChoiceTableOrSql == 1) {
    //         $(curSourceTableChoice).attr("checked", false);
    //     }
    //     curSourceTableChoice = null;
    //     curDataSourceId = null;
    //     curTableName = null;
    //     curRefer = null;
    // });
    $("#editTableFieldComsSaveId").bind("click", saveEditTableFieldComs);
    $("#editTableFieldComsCloseId").bind("click", saveEditTableFieldComs
        // function () {
        // if (curEditIsChoiceTableOrSql == 1) {
        //     $(curSourceTableChoice).attr("checked", false);
        // }
        // curSourceTableChoice = null;
        // curDataSourceId = null;
        // curTableName = null;
        // curRefer = null;
        // }
    );
});

function getEditTableOrSqlFieldComs() {
    var tableInfosList = [];
    var tableNameVar = null;
    var tableInfos = null;
    var tableInfosListIndex = 0;
    var tableInfosIndex = 0;
    if ($(".fieldComsKey")) {
        $(".fieldComsKey").each(function (index, value, array) {
            var tableName = $(value).attr("tablename");
            if (tableNameVar != tableName) {
                tableInfos = [];
                var tableInfosObj = {tableName: tableName, tableInfos: tableInfos};
                tableInfosList[tableInfosListIndex++] = tableInfosObj;
                tableInfosIndex = 0;
                tableNameVar = tableName;
            }
            // var fieldName = $(value).text();
            var fieldName = $(value).attr("fieldName");
            var columnNameLabel = $(value).attr("columnNameLabel");
            var dataType = $(value).attr("dataType");
            var fieldComs = $($(".fieldComsValue")[index]).val();
            tableInfos[tableInfosIndex++] = {
                columnName: fieldName,
                columnNameLabel: columnNameLabel,
                columnComment: fieldComs,
                dataType: dataType
            };

        });
    }
    return tableInfosList;
}

function saveTableFieldComs(dataSourceId, tableInfos) {
    if (tableInfos && tableInfos.length == 1) {
        $.ajax({
            type: "POST",
            url: ctx + '/saveTableFieldComs',
            data: {
                "dataSourceId": dataSourceId,
                "tableName": tableInfos[0].tableName,
                "tableInfos": $.toJSON(tableInfos[0].tableInfos)
            },
            dataType: "json",
            success: function () {
            }
        });
    }
}

function saveSQLFieldComs(dataSourceId, sqlStr, tableInfos) {
    $.ajax({
        type: "POST",
        url: ctx + '/saveSQLFieldComs',
        data: {
            "dataSourceId": dataSourceId, "sqlStr": sqlStr,
            "tableInfos": $.toJSON(tableInfos)
        },
        dataType: "json",
        success: function () {
        }
    });
}

function previewTableDataAndComs(dataSourceId, tableInfosList) {
    $.ajax({
        type: "POST",
        url: ctx + '/previewRelationalDatabaseByTableName',
        data: {
            "dataSourceId": dataSourceId, "tableInfosList": $.toJSON(tableInfosList)
        },
        dataType: "json",
        success: function (data) {
            if (!data || !data.datas) {
                return;
            }
            var columnTitleList = [];
            tableInfosList.forEach(function (tableInfos, index1, array1) {
                tableInfos.tableInfos.forEach(function (value, index2, array2) {
                    // var columnTitle = value.columnName + "(" + value.columnComment + ")";
                    columnTitleList.push({columnName:value.columnName,columnComment:value.columnComment});
                });
            });
            data.datas.unshift(columnTitleList);
            var html = template("previewTableDataAndComsTmpl", {"datas": data.datas});
            $('#previewTableDataAndComsId').html(html);
        }
    });
}

function previewSqlDataAndComs(dataSourceId, tableInfosList) {
    var sqlStr;
    if (curRefer == "dataService") {
        sqlStr = $("#publicSql").val();
    } else {
        var sqlStrId = "sqlStr";
        if (curSQLStrIndex < 0) {
            return;
        }
        if (curSQLStrIndex != 0) {
            sqlStrId += curSQLStrIndex;
        }
        sqlStr = $("#" + sqlStrId).val();
    }
    $.ajax({
        type: "POST",
        url: ctx + '/previewRelationalDatabaseBySQL',
        data: {
            "dataSourceId": dataSourceId,
            "sqlStr": sqlStr,
            "tableInfosList": $.toJSON(tableInfosList)
        },
        dataType: "json",
        success: function (data) {
            if (!data || !data.datas) {
                return;
            }
            var columnTitleList = [];
            tableInfosList.forEach(function (tableInfos, index1, array1) {
                tableInfos.tableInfos.forEach(function (value, index2, array2) {
                    // var columnTitle = value.columnNameLabel + "<br>(" + value.columnComment + ")";
                    // columnTitleList.push(columnTitle);
                    columnTitleList.push({columnName:value.columnName,columnComment:value.columnComment});
                });
            });
            data.datas.unshift(columnTitleList);
            var html = template("previewTableDataAndComsTmpl", {"datas": data.datas});
            $('#previewTableDataAndComsId').html(html);
        }
    });
}

function editSqlFieldComs(sqlNum) {
    var sqlStrId = "sqlStr";
    if (sqlNum < 0) {
        return;
    }
    if (sqlNum != 0) {
        sqlStrId += sqlNum;
    }
    curSQLStrIndex = sqlNum;
    var sqlStr = $("#" + sqlStrId).val();
    var dataSourceId = $("#dataSourceId").val();
    console.log("sqlStrxxxxx" + sqlStr);

    staticSourceTableChoice(2, null, dataSourceId, sqlStr, "dataResource");
}
