function saveDataResourcefunc() {
    if (checkContentForm()) {
        var r = true;
        var formData = $('#submit_form').serializeArray();
        var resource = {};
        var computerServiceResource = {};
        for (var i = 0; i < formData.length; i++) {
            if (formData[i].name == 'classId') {
                resource.classId = formData[i].value;
                continue;
            } else if (formData[i].name == 'resState') {
                resource.resState = formData[i].value;
                continue;
            } else if (formData[i].name == 'resTitle') {
                resource.resTitle = formData[i].value;
                continue;
            }
            computerServiceResource[formData[i].name] = formData[i].value;
        }
        resource.computeServiceResource = computerServiceResource;
        resource.resType = '计算服务';
        if ($("#resourceId").val())
            resource.resourceId = $("#resourceId").val();
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
        //     type: "post",
        //     url: ctx + '/resource',
        //     data: formData,
        //     dataType: "json",
        //     cache: false,
        //     contentType: false,
        //     async: false,
        //     processData:false,
        //     beforeSend: function () {
        //         Metronic.blockUI({
        //             target: '#tab1',
        //             boxed: true,
        //             message: '保存中...'
        //         });
        //     },
        //     success: function (data) {
        //         if (data.head.code == 200) {
        //             $("#resourceId").val(data.body.resourceId);
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
        //         Metronic.unblockUI("#tab1");
        //     }
        // });
        return r;
    } else {
        toastr["error"]("您提交的表单中有错误，请根据填写框上的提示进行修改！", "表单错误");
        return false;
    }
}


function checkContentForm() {

        var form3 = $('#submit_form');

        form3.validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "", // validate all fields including form hidden input
            rules: {
                resTitle: {
                    required: true
                },
                linkURL: {
                    required: true,
                },
                tags: {
                    required: true
                },
                classId: {
                    required: true
                }
            },
            messages: {
                resTitle: {
                    required: "名称必须填写"
                },
                linkURL: {
                    required: "必填项",
                },
                tags: {
                    required: "必填项"
                },
                classId: {
                    required: "请选择资源分类"
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
    return form3.valid();

}