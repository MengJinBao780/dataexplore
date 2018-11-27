var starNum = 0
$("#star-list").mouseover(function () {
    $("#star-list").delegate("a", "mouseover", function () {
        var num = $(this).index() + 1;
        if (num == 1) {
            $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star2").attr("src", ctx+"/resources/img/star.png")
            $("#star3").attr("src", ctx+"/resources/img/star.png")
            $("#star4").attr("src", ctx+"/resources/img/star.png")
            $("#star5").attr("src", ctx+"/resources/img/star.png")
        } else if (num == 2) {
            $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star3").attr("src", ctx+"/resources/img/star.png")
            $("#star4").attr("src", ctx+"/resources/img/star.png")
            $("#star5").attr("src", ctx+"/resources/img/star.png")
        } else if (num == 3) {
            $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star3").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star4").attr("src", ctx+"/resources/img/star.png")
            $("#star5").attr("src", ctx+"/resources/img/star.png")
        } else if (num == 4) {
            $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star3").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star4").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star5").attr("src", ctx+"/resources/img/star.png")
        } else if (num == 5) {
            $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star3").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star4").attr("src", ctx+"/resources/img/activeStar.png")
            $("#star5").attr("src", ctx+"/resources/img/activeStar.png")
        }
    })
})
$("#star-list").mouseout(function () {
    if (starNum == 1) {
        $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star2").attr("src", ctx+"/resources/img/star.png")
        $("#star3").attr("src", ctx+"/resources/img/star.png")
        $("#star4").attr("src", ctx+"/resources/img/star.png")
        $("#star5").attr("src", ctx+"/resources/img/star.png")
    } else if (starNum == 2) {
        $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star3").attr("src", ctx+"/resources/img/star.png")
        $("#star4").attr("src", ctx+"/resources/img/star.png")
        $("#star5").attr("src", ctx+"/resources/img/star.png")
    } else if (starNum == 3) {
        $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star3").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star4").attr("src", ctx+"/resources/img/star.png")
        $("#star5").attr("src", ctx+"/resources/img/star.png")
    } else if (starNum == 4) {
        $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star3").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star4").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star5").attr("src", ctx+"/resources/img/star.png")
    } else if (starNum == 5) {
        $("#star1").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star2").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star3").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star4").attr("src", ctx+"/resources/img/activeStar.png")
        $("#star5").attr("src", ctx+"/resources/img/activeStar.png")
    } else if (starNum == 0) {
        $("#star1").attr("src", ctx+"/resources/img/star.png")
        $("#star2").attr("src", ctx+"/resources/img/star.png")
        $("#star3").attr("src", ctx+"/resources/img/star.png")
        $("#star4").attr("src", ctx+"/resources/img/star.png")
        $("#star5").attr("src", ctx+"/resources/img/star.png")
    }
})
$("#star-list").delegate("a", "click", function () {
    starNum = $(this).index() + 1;
})