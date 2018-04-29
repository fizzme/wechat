<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-touch-fullscreen" content="yes">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<link rel="stylesheet" href="css/register.css" />
<link rel="bookmark" href="http://m.oppo.com/favicon.ico"/>
<link rel="shortcut icon" href="http://m.oppo.com/favicon.ico" type="image/x-icon" />
<title>用户绑定</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<style>

	@media screen and (max-width: 786px)
		.user-hd .my-avatar {  width: 60px;  height: 60px; }

		.user-hd .my-avatar{width: 110px; height:110px; margin:auto ;position:relative}
		.user-hd .my-avatar img {
			-moz-border-radius: 50%;
			-webkit-border-radius: 50%;
			border-radius: 50%;
			width: 100%;
		}

		.user-hd .my-nickname {
			padding: 20px 0;
			font-size: 16px;
			position: relative;
			width: 100px;
			margin: 0 auto;
		}

		.user-hd .my-nickname {
			padding: 20px 0;
			font-size: 20px;
		}

		.user-hd .my-nickname em {
			font-size: 14px;
			font-style: normal;
		}

		.user-hd .my-nickname span {
			position: relative;
		}
</style>

</head>
<body>

	<header id="header" class="header-topbar">
		<a class="go-back" href="index.html"><img src="http://m.oppo.com/images/home.png"/></a>
		<h1 class="header-title">用户绑定</h1>
	</header>
	<div id="content" class="content-reg">
		<div class="user-hd">
			<div class="my-avatar">

<!-- 				<img src="http://thirdwx.qlogo.cn/mmopen/vi_32/O9ylcAg1qeib3J1ZZUkp7X9hBCkOBzPVp7LfGTIoSiccYdsDWMGToCnic9yvvEFibyUBqjrTwibHWaxmUW5Mtm0UukQ/132" alt=""> -->
				<img src="${requestScope.userinfo.headimgurl}" alt="">
				<span class="update-tip"><i class="i-update-avatar"></i></span>

			</div>
			<div class="my-nickname">
			        <span>
<%--            				 <em>后海大章鱼?</em> --%>
           				 <em>${requestScope.userinfo.nickname}</em>
			        </span>
			</div>

		</div>
		<!-- 使用js来提交表单 -->
		<form action="${pageContext.request.contextPath}/register2" method="POST" id="bindForm" onsubmit="return checkForm();">
		
			<div class="reg-row">
				<div class="reg-row-border"><input type="text" id="telephone" name="telephone" placeholder="手机号" value=""/></div>
			</div>
			<div class="reg-row">
				<div class="reg-row-width40">
					<div class="reg-row-border"><input type="text" id="checkCode" name="checkCode" placeholder="图片验证码" /></div>
				</div>
				<div class="reg-row-width60">
					<div class="reg-row-img"><img src="${pageContext.request.contextPath}/captcha"  onclick="changeCode(this)"/></div>
					<div class="reg-row-tip">&nbsp;点击刷新</div>
				</div>
			</div>
			<!-- 隐藏传递用户的openid -->
			 <input type='hidden' name='openid' id='openid' value='${requestScope.userinfo.openId}'/>
			 
		</form>
		
		<div style="height:auto;line-height:20px;overflow:hidden;margin:0px 20px;margin-bottom:10px;color:#F00;display:none;" id="error-tip">提示</div>

		<div class="reg-row">
		    <span style="color: red">${msg}</span>  
		  
		</div>
		
		<div class="reg-row">
			<span id="submit-loading" class="submit" style="display: none;">正在提交...</span>
			<span id="submit-reg-next" class="submit"><a onclick="document.getElementById('bindForm').submit();">立即注册</a></span>
		</div>
	</div>	
<footer id="footer">
	<ul id="foot-state">
				<li><a href="javascript:User.login();">登录</a></li>
		<li><a href="http://m.oppo.com/reg.html">注册</a></li>
	</ul>
	
	<p class="copyright">Copyright&copy;<a href="http://www.lanrenmb.com/" target="_blank">懒人模板</a> All Rights Reserved 
(2013-2015) 版权所有</p>
	
</body>


<script type="text/javascript">
    
    
     // 用于点击时产生不同的验证码
    function changeCode(node){
        node.src = "${pageContext.request.contextPath}/captcha" ;    
    }
    
    //表单提交时校验绑定参数
    function checkForm(){
    	var telephone = document.getElementById("telephone").value;
    	var checkCode = document.getElementById("checkCode").value;
    	var openid = ${requestScope.userinfo.openId};
    	if(telephone == "" ){
	    	alert("手机号不能为空");
	    	return false;
    	}
    	
    	if(checkCode == "" ){
	    	alert("验证码不能为空");
	    	return false;
    	}
    	alert("当前用户的openid为："+ openid);
    	return true;

    }
    
    
</script>
</html>
