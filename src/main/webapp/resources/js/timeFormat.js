(function (window) {
        function timeFormat(date) {
                //date是整数，否则要parseInt转换
                var time = new Date(date);
                var y = time.getFullYear();
                var m = time.getMonth()+1;
                var d = time.getDate();
                var h = time.getHours();
                var mm = time.getMinutes();
                var s = time.getSeconds();
                /*返回年月日*/
                return y+'-'+add0(m)+'-'+add0(d);
                /*返回年月日时分秒*/
               /* return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);*/
            };
            function add0(m){return m<10?'0'+m:m }
            window.mytime={
                timeFormat:timeFormat
            };
    })(window)