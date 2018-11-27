/**
 * Created by admin on 2016/4/15.
 */
'use strict';

function checkContentForm() {
    var form3 = $('#submit_form');
    var validation = form3.validate({
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
            // description: {
            //     required: true
            // },
            // dataSize: {
            //     required: true,
            // },
            // packType: {
            //     required: true,
            // },
            // content: {
            //     required: true,
            //     maxlength: 100
            // },
            resourceType: {
                required: true,
            },
            catalogCheckbox: {
                required: true
            },
            // generateType: {
            //     required: true,
            // },
            /*choosefile: {
                required: true,
            }*/
            sqlStr: {
                remote: {
                    url: ctx + "/sqlValidation",
                    type: "get",
                    data: {
                        tables: function () {
                            var mapTableStr = '';
                            var checked = $("#resourceChooseDiv").find("input[type='checkbox']");
                            for (var i = 0; i < checked.length; i++) {
                                if ($(checked[i]).attr("checked")) {
                                    if (mapTableStr == '')
                                        mapTableStr = $(checked[i]).val();
                                    else
                                        mapTableStr = mapTableStr + ',' + $(checked[i]).val();
                                }
                            }
                            return mapTableStr;
                        },
                        dataSourceId: form3.find(":input[name='dataSourceId']").val()
                    }
                }
            }
        },
        messages: { // custom messages for radio buttons and checkboxes
            resTitle: {
                required: "请输入名称"
            },
            resourceType: {
                required: "请选择数据资源类型"
            },
            catalogCheckbox: {
                required: "请选择"
            },
            // packType: {
            //     required: "请选择封装类型"
            // },
            // generateType: {
            //     required: "请选择生成关系数据表方式"
            // },
            /*choosefile: {
                required: "请选择或上传文件"
            },*/
            sqlStr: {
                validSqlStr: "sql语句不正确"
             }
        },

        errorPlacement: function (error, element) { // render error placement for each input type
            if (element.parent(".input-group").size() > 0) {
                error.insertAfter(element.parent(".input-group"));
            } else if (element.attr("data-error-container")) {
                error.appendTo(element.attr("data-error-container"));
            } else if (element.parents('.radio-list').size() > 0) {
                error.appendTo(element.parents('.radio-list').attr("data-error-container"));
            } else if (element.parents('.radio-inline').size() > 0) {
                error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
            } else if (element.parents('.checkbox-list').size() > 0) {
                error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
            } else if (element.parents('.checkbox-inline').size() > 0) {
                error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
            } else {
                error.insertAfter(element); // for other inputs, just perform default behavior
            }
        },
        invalidHandler: function (event, validator) { //display error alert on form submit
        },
        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },
        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },
        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        },
        submitHandler: function (form) {
        }
    });

    //apply validation on select2 dropdown value change, this only needed for chosen dropdown integration.
    $('.select2me', form3).change(function () {
        form3.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
    });

    // initialize select2 tags
    $("#select2_tags").change(function () {
        form3.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
    }).select2({
        tags: ["red", "green", "blue", "yellow", "pink"]
    });

    //initialize datepicker
    $('.date-picker').datepicker({
        rtl: Metronic.isRTL(),
        autoclose: true
    });
    $('.date-picker .form-control').change(function () {
        form3.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
    });
    var r = form3.valid();
    if (!r)
        return r;
    var jstree = $.jstree.reference("#jstree");
    var errorElement = $("<span/>");
    errorElement.addClass('help-block help-block-error');
    /*if (jstree) {
        if (jstree.get_top_checked(false).length > 0) {
            $("#filePath")
                .closest('.form-group').removeClass('has-error');
            r = true;
        } else {
            $("#filePath")
                .closest('.form-group').addClass('has-error');
            errorElement.html("请选择或上传文件");
            errorElement.insertAfter($("#filePath"));
            r = false;
            return r;
        }
    }*/

    var filePathStr = "";
    $("#filePath").val("");
    var jstree = $.jstree.reference("#jstree");
    if($("#fileSource").is(':checked')){
        if (jstree) {
            var selectedFile = jstree.get_top_checked(false);
            for (var i = 0; i < selectedFile.length; i++) {
                filePathStr = filePathStr + selectedFile[i] + ',';
            }
        }
        if($("#tags").val()!=""&&$("#tags").val()!=undefined){
            filePathStr = filePathStr + $("#tags").val();
        }
        if (filePathStr != "") {
            $("#filePath").val(filePathStr);
        }
        var dTable = $('#uploadedFilesTable').dataTable();
        var nTrs = dTable.fnGetNodes();
        for(var i = 0; i < nTrs.length; i++){
            filePathStr = filePathStr + dTable.fnGetData(nTrs[i])[0] + ',';
        }
    }

    if($("#fileSource").is(':checked')){
        if(filePathStr!=""&&filePathStr!=undefined){
            $("#filePath")
                .closest('.form-group').removeClass('has-error');
            r = true;
        }else{
            $("#filePath")
                .closest('.form-group').addClass('has-error');
            errorElement.html("请选择或上传文件");
            errorElement.insertAfter($("#filePath"));
            r = false;
            return r;
        }
    }else{
        if(!$("#dataSourceId").val()){
            errorElement.html("请选择数据库");
            $("#resourceChooseDiv")
                .closest('.form-group').addClass('has-error');
            errorElement.insertAfter($("#resourceChooseDiv"));
            r = false;
            return r;
        }

    }

    if(!$("#localCheckbox").prop("checked")&&!$("#centerCheckbox").prop("checked")){
        errorElement.html("请选择");
        $("#centerCheckbox")
            .closest('.form-group').addClass('has-error');
        errorElement.insertAfter($("#localCheckbox"));
    }

    if ($("#sqlStr").val()) {
        return r;
    } else if ($("#resourceChooseDiv").find("input[type='checkbox']").length > 0) {
        if ($("#resourceChooseDiv").find("input[type='checkbox']:checked").length == 0) {
            errorElement.html("请选择数据表");
            $("#maptableinput")
                .closest('.form-group').addClass('has-error');
            errorElement.insertAfter($("#maptableinput"));
            r = false;
            return r;
        } else {
            $("#maptableinput").closest('.form-group').removeClass('has-error');
        }
    }
    return r;
}

