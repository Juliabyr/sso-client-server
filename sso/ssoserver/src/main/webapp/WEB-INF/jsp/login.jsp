<%--
  Created by IntelliJ IDEA.
  User: zhuye2
  Date: 2018/7/25
  Time: 17:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
    <script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
    <meta name="keywords" content="Flat Dark Web Login Form Responsive Templates, Iphone Widget Template, Smartphone login forms,Login form, Widget Template, Responsive Templates, a Ipad 404 Templates, Flat Responsive Templates" />
    <link href="${pageContext.request.contextPath}/css/style.css" rel='stylesheet' type='text/css' />
</head>
<body>
<script>$(document).ready(function(c) {
    $('.close').on('click', function(c){
        $('.login-form').fadeOut('slow', function(c){
            $('.login-form').remove();
        });
    });
});
</script>

<div class="login-form">
    <div class="close"> </div>
    <div class="head-info">
        <label class="lbl-1"> </label>
        <label class="lbl-2"> </label>
        <label class="lbl-3"> </label>
    </div>
    <div class="clear"> </div>
    <div class="avtar">
        <img src="${pageContext.request.contextPath}/images/avtar.png" />
    </div>
    <form id="loginform" class="form-horizontal" role="form" method="post" id="fm1" action="${pageContext.request.contextPath}/server/auth/login">
        <input type="text" id="username" class="form-control" name="username" value="" placeholder="Username">
        <div class="key">
            <input type="password" id="password" class="form-control" name="password" value="" placeholder="Password">
        </div>
        <div class="signin">
            <input class="btn btn-primary" name="submit" type="submit" value="Login" style="min-width: 200px">
            <input class="btn btn-primary" name="reset" type="reset" value="Clear" style="min-width: 200px">
        </div>
    </form>

</div>

<%--<form id="loginform" class="form-horizontal" role="form" method="post" id="fm1" action="${pageContext.request.contextPath}/server/auth/login">--%>
<%--<div style="margin-bottom: 25px" class="form-group">--%>
<%--<label for="username" class="control-label col-md-3 required">Username</label>--%>
<%--<div class="col-md-9">--%>
<%--<input type="text" id="username" class="form-control" name="username" value="" placeholder="Username">--%>
<%--</div>--%>
<%--</div>--%>
<%--<div style="margin-bottom: 25px" class="form-group">--%>
<%--<label for="password" class="control-label col-md-3 required">Password</label>--%>
<%--<div class="col-md-9">--%>
<%--<input type="password" id="password" class="form-control" name="password" value="" placeholder="Password">--%>
<%--</div>--%>
<%--</div>--%>
<%--<div style="margin-top: 20px" class="form-group">--%>
<%--<div class=" col-md-offset-3 col-md-9">--%>
<%--<input class="btn btn-primary" name="submit" type="submit" value="Login" style="min-width: 200px">--%>
<%--<input class="btn btn-default" name="reset" type="reset" value="Clear" style="min-width: 200px">--%>
<%--</div>--%>
<%--</div>--%>
<%--</form>--%>
</body>
</html>
