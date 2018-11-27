/**
 * Created by xiajl on 20171108
 */

$(function () {
    //xiajl20171108
    template.helper("dateFormat", formatDate);
    getData(1);

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
});


$("#isTemplate").change(function(){
    if ($(this).prop("checked") ){
        $("#divTemplate").css("display","");
        //xiajl20171101增加
        $("#templateName").val($("#resTitle").val() +"元数据模板");

        //选择模板失效
        $("#isSelectTemplate").prop("checked",false);
        $("#isSelectTemplate").attr("checked",false);
        $("#isSelectTemplate").attr("disabled","disabled");
    }
    else{
        $("#divTemplate").css("display","none");
        $("#isSelectTemplate").removeAttr("disabled");
    }
});

$("#isSelectTemplate").change(function(){
    if ($(this).prop("checked") ){
        $("#divSelectTemplate").css("display","");

        //xiajl20171101增加
        $("#isTemplate").prop("checked","");
        $("#isTemplate").attr("checked",false);
        $("#isTemplate").attr("disabled","disabled");
    }
    else{
        $("#divSelectTemplate").css("display","none");

        $("#isTemplate").removeAttr("disabled");
    }
});



//xiajl20171105
//获取元数据模板信息数据
function getData(pageNo) {
    $.ajax({
        url: ctx +"/metaTemplate/list",
        type: "get",
        dataType: "json",
        data: {
            "templateName":"",
            "pageNo": pageNo,
            "pageSize": 10
        },
        success: function (data) {
            console.log(data);
            var html = template("systemTmpl", data);
            $("#metaTemplateList").empty();
            $("#metaTemplateList").append(html);
            $("#currentPageNo").html(data.currentPage);
            currentPageNo = data.currentPage;
            $("#totalPages").html(data.totalPages);
            $("#totalCount").html(data.totalCount);
            if ($("#pagination .bootpag").length != 0) {
                $("#pagination").off();
                $('#pagination').empty();
            }
            $('#pagination').bootpag({
                total: data.totalPages,
                page: data.currentPage,
                maxVisible: 5,
                leaps: true,
                firstLastUse: true,
                first: '首页',
                last: '尾页',
                wrapClass: 'pagination',
                activeClass: 'active',
                disabledClass: 'disabled',
                nextClass: 'next',
                prevClass: 'prev',
                lastClass: 'last',
                firstClass: 'first'
            }).on('page', function (event, num) {
                getData(num);
                currentPageNo = num;
            });
        }
    });
}

function viewData(id) {
    window.open(ctx + "/metaTemplate/templateDetailView/" + id, "_blank");
}

//选择模板数据
function selectData(id) {
    $.ajax({
        url: ctx + "/metaTemplate/templateDetail/" + id ,
        type: "get",
        dataType: "json",
        success: function (result) {
            //console.log("20171106:" + JSON.stringify(result));
            //$("#coreMetaTree").empty();
            if (result.xsdData) {
                var xsdData = result.xsdData;
                //generateTree($("#coreMetaTree"), xsdData);

                if (result.metaData)
                    bindTreeData($("#coreMetaTree"), result.metaData);
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
                toastr["success"]("读取模板数据成功！", "读取模板数据成功！");
            } else {
                bootbox.alert("核心元数据规范文件丢失，无法修改核心元数据！");
            }
        },
        error: function () {
            bootbox.alert("网络发生错误！");
        }
    });
}
