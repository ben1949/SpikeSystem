<%--
  Created by IntelliJ IDEA.
  User: weihuap
  Date: 2017/6/4
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file = "common/head.jsp"%>
</head>
<body>
<div class="container">
   <div class="panel panel-default text-center">
       <div class="panel-heading">
       <h1>${seckill.name}</h1>
       </div>
   </div>
    <div class="panel-body">
        <h2 class="text-danger">
            <!-- 显示time 图标-->
            <span class="glyphicon glyphicon-time"></span>
            <!-- 展示倒计时-->
            <span class="glyphicon" id="seckill-box"></span>
        </h2>
    </div>
</div>
<div>
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey" placeholder="填手机号" class="form-control"
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <span id="killPhoneMessage" class="glyphicon"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-phone"></span>
                    Submit
                </button>

            </div>
        </div>
    </div>
    </div>
</div>

<!-- 新 Bootstrap 核心 CSS 文件 -->
<link href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

<!-- 可选的Bootstrap主题文件（一般不使用） -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap-theme.min.css"></script>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- 使用CDN 获取公共 js http://www.bootcdn.cn-->
<!-- 获取jquery cookie插件 -->
<!--
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
-->
<script src="/resources/jquery/jquery.cookie.min.js" type="text/javascript"></script>
<!-- 获取jquery count down 插件-->
<!--
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
-->
<script src="/resources/jquery/jquery.countdown.min.js" type="text/javascript"></script>



<!-- 开始编写交互逻辑 -->
<script src="/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        seckill.detail.init({
            seckillId :${seckill.seckillid},
            startTime : ${seckill.startTime.time},//毫秒时间
            endTime : ${seckill.endTime.time}
        });
    });
</script>

</body>
</html>
