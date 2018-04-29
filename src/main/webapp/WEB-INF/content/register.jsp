<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>

<script type="text/javascript">
    function changeR(node){
        // 用于点击时产生不同的验证码
//         node.src = "randomcode.jpg?time="+new Date().getTime() ;    
        node.src = "${pageContext.request.contextPath}/captcha" ;    
    }
</script>


</head>
<body>
<h1>hello word</h1>

<table>
<tr>
	<td><input type="text" id="code" class="form-control" placeholder="验证码" data-toggle="tooltip" data-placement="top"></td>
	<td> <img id="codeImg" src="http://127.0.0.1:8080/wechat/captcha" class="img-responsive" style="display: block;width:100%;height: 32px"></td>
</tr>
<tr>
	<td>登陆</td>
	<td>取消</td>
</tr>
</table>

<div style="text-align: center;">
<h2>验证码之kaptcha</h2>
<img alt="random" src="${pageContext.request.contextPath}/captcha" onclick="changeR(this)" style="cursor: pointer;"><br/><br/>
    <form action="${pageContext.request.contextPath}/register">
        <input type="text" name="checkCode">
        <input type="submit" value="提交">
    </form> 
</div>
<h3  style="color: red">${msg}</h3>

</body>
</html>