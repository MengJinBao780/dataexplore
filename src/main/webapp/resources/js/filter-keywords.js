function filter_keywords(value,keywords) {
    //获取文本输入框中的内容
    //遍历敏感词数组
    var key = {};
    for (var i = 0; i < keywords.length; i++) {
        //全局替换
        /*var reg = new RegExp(keywords[i], "g");*/
        //判断内容中是否包括敏感词
        if (value.indexOf(keywords[i]) != -1) {
            /*var result = value.replace(reg, "**");
            value = result;*/
            key.value = "1";
            key.keyword =keywords[i]
            return key;
        }
    }
    key.value = "0";
    key.keyword =""
    return key
}