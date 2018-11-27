/**
 * Created by admin on 2016/4/15.
 */
'use strict';
function initWizard(container) {

    $(container).bootstrapWizard({
        'nextSelector': '.button-next',
        'previousSelector': '.button-previous',
        onTabClick: function (tab, navigation, index, clickedIndex) {
            return false;
        },
        onNext: function (tab, navigation, index) {
            var result = checkCurrentTab(tab, navigation, index, container);
            if (result) {
                initNextTab(tab, navigation, index, container);
                handleTitle(tab, navigation, index, container);
                $(container).find(".button-save").attr("disabled", true);
            } else {
                return result;
            }
        },
        onPrevious: function (tab, navigation, index) {
            $(container).find(".button-save").attr("disabled", true);
            handleTitle(tab, navigation, index, container);
        },
        onTabShow: function (tab, navigation, index) {
            var total = navigation.find('li').length;
            var current = index + 1;
            var $percent = (current / total) * 100;
            $(container).find('.progress-bar').css({
                width: $percent + '%'
            });
        }
    });
    $(container).find('.button-previous').hide();
    $(container).find('.button-submit').click(function () {
        var r = true;
        if ($(container).find(".button-save").attr("disabled")) {
            var noError = checkForm($("#coreMetaForm"));
            if (!noError) {
                toastr["error"]("您提交的表单中有错误，请根据填写框上的提示进行修改！", "表单错误");
                $("#coreMetaForm").find(".jstree-node .form-group.has-error").find(":input")[0].focus();
                return;
            }
        } else {
            var index = $(container).bootstrapWizard("currentIndex");
            r = saveCurrentPageData(index, container);
        }
        if (r) {
            $.ajax({
                url: ctx + "/resource/" + $("#resourceId").val() + "/resState",
                type: "put",
                dataType: "json",
                data: "本地审核中",
                async: false,
                beforeSend: function () {
                    Metronic.blockUI({
                        target: '#tab2',
                        boxed: true,
                        message: '注册中...'
                    });
                },
                success: function (result) {
                    if (result.head.code == 200) {
                        toastr["success"]("注册成功！", "数据注册");
                        $(container).find(".button-save").attr("disabled", true);
                        //window.location.href = ctx + "/resourceDetail/" + $("#resourceId").val();
                        window.location.href = ctx + "/resource/manager";
                    } else {
                        toastr["error"]("注册失败！", "数据注册");
                    }
                },
                error: function () {
                    toastr["error"]("注册失败！", "数据注册");
                },
                complete: function () {
                    Metronic.unblockUI('#tab2');
                }
            });
        }
    }).hide();
    $(container).find(".button-save").click(function () {
        var index = $(container).bootstrapWizard("currentIndex");
        saveCurrentPageData(index, container);
    }).attr("disabled", true);
    $(container).find(":input").change(function () {
        $(container).find(".button-save").removeAttr("disabled");
    });
}
var handleTitle = function (tab, navigation, index, container) {
    var total = navigation.find('li').length;
    var current = index + 1;
    // set wizard title
    $('.step-title', $(container)).text('第 ' + (index + 1) + ' 步，共 ' + total + ' 步');
    // set done steps
    jQuery('li', $(container)).removeClass("done");
    var li_list = navigation.find('li');
    for (var i = 0; i < index; i++) {
        jQuery(li_list[i]).addClass("done");
    }

    if (current == 1) {
        $(container).find('.button-previous').hide();
    } else {
        $(container).find('.button-previous').show();
    }

    if (current >= total) {
        $(container).find('.button-next').hide();
        $(container).find('.button-submit').show();
    } else {
        $(container).find('.button-next').show();
        $(container).find('.button-submit').hide();
    }
    Metronic.scrollTo($('.page-title'));
};