<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/5/4
  Time: 8:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>Excel</title>
    <link href="${ctx}/resources/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<input type="button" class="btn btn-primary" value="下载筛选后数据" onclick="javascript:download();"  />
<div style="margin-bottom: 5px">
    <ul class="nav nav-tabs">
        <c:forEach items="${sheetNames}" varStatus="status" var="name">
            <li role="presentation" name="tabName"><a href="#">${name}</a></li>
        </c:forEach>
    </ul>
</div>
<c:forEach items="${sheets}" var="lists" varStatus="status">
<c:if test="${lists.size()>1}">
    <div style="width: 100%;overflow: auto;display: none" class="excel-wrap">
        <table class="excel"  style="border: 1px solid black">
            <thead>
                <tr>
                    <c:forEach items="${lists.get(0)}" var="data">
                        <th style="border:1px solid #AAAAAA">${data}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:forEach  var="i" begin="1" end="${lists.size()-1}" step="1">
                    <tr>
                        <c:forEach items="${lists.get(i)}" var="data">
                            <td style="border:1px solid #AAAAAA;padding: 2px" >${data}</td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
</c:forEach>
</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript">
        var tabsNum = 0;
        $(".excel-wrap").height(document.body.clientHeight -273);
        $(document).ready(function() {
         $("[name='tabName']:eq(0)").addClass("active");
         $(".excel-wrap:eq(0)").show();
         $('.excel').DataTable({
             "autoWidth":false,
             "ordering":false,
             "bPaginate" : false,
             "bInfo" : false
         });
         $("[name='tabName']").on("click",function () {
             var newNum = $(this).index()
             if(newNum == tabsNum){
                 return
             }else {
                 $("[name='tabName']:eq("+tabsNum+")").removeClass("active");
                 $(this).addClass("active");
                 $(".excel-wrap:eq("+tabsNum+")").hide();
                 $(".excel-wrap:eq("+newNum+")").show();
                 tabsNum = newNum;
             }
         })
        });
        $.extend({
            StandardPost:function(data){
                var form = $("<form method='post' action='${ctx}/sdo/downloadDataInExcel'></form>"),
                    input;
                    input = $("<input type='hidden' name='data'>");
                    input.val(data);
                        form.append(input);
                form.appendTo(document.body);
                form.submit();
            }
        });
        function download() {
            var table = $(".excel:visible");
            var rows =table.get(0).rows;
            var all=new Array();
            for(var i=0;i<rows.length;i++){
                var cells = new Array();
                for (var j=0 ;j<rows[i].cells.length;j++){
                    cells.push(rows[i].cells[j].innerHTML);
                }
                all.push(cells);
            }
            $.StandardPost(JSON.stringify(all));
        }
    </script>
</div>
</html>
