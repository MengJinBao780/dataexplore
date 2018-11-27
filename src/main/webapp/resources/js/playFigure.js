var moving = false;//标识是否正在移动

var index = 2;//当前显示图片的下标
/*var indexT = 3;*/
var intervalId;
var $listDiv = $(".move");
var $spans = $('.contentaaa>div');
var $containerDiv =$(".lunplay");
$('#prev').click(function () {
    //翻到上一页
    page(false);
});
$('#next').click(function () {
    //翻到下一页
    page(true);
});
autoPage();
//实现鼠标移入移出的功能
$containerDiv.hover(function () {
    //停止自动翻页
    clearInterval(intervalId);
}, function () {
    //开启自动翻页
    autoPage();
});
$(".contentaaa").hover(function () {
    //停止自动翻页
    clearInterval(intervalId);
}, function () {
    //开启自动翻页
    autoPage();
});
function page(value){
    if(moving) {
        return;
    }
    moving = true;
    var targetLeft = 0;
    var left = $listDiv.position().left;
    targetLeft = left + (value?-100:100);

    $listDiv.animate({
        left : targetLeft
    }, function () {
        //更新标识
        moving = false;
        //处理最左边和最右边
        if(targetLeft==0) {
            targetLeft = -900;
        } else if(targetLeft==-1000) {
            targetLeft = -100;
        }
        //重新定位
        $listDiv.css('left' , targetLeft);
    });
    updatePoints(value);
}
function updatePoints(value) {

    //移除index所对应的span的class
    //$spans[index].className = '';
    /*  $(".lunplay li").eq(indexT).first().css("background","");*/
    $spans.eq(index).removeClass('active');
    //$spans.eq(index).removeAttr('class');
    //计算目标index
    var targetIndex = 0;
    targetIndex = index + (value?1:-1);
    /* targetIndexT = indexT+(value?1:-1);*/
    if(targetIndex==-1) {
        targetIndex = 8;
    } else if(targetIndex==9) {
        targetIndex = 0;
    }
    /*if(targetIndexT==2) {
        targetIndexT = 11;
    } else if(targetIndexT==12) {
        targetIndexT = 3;
    }*/
    //更新对应的span的class
    $spans.eq(targetIndex).addClass('active');
    /* $(".lunplay li").eq(targetIndexT).first().css("background","red");*/
    //更新index
    index = targetIndex;
    /*indexT = targetIndexT;*/
}
function autoPage() {
    intervalId = setInterval(function () {
        page(true);
    }, 1000);
}