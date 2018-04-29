<%@page import="com.alibaba.fastjson.JSON"%>
<%@page import="com.fizzblock.wechat.pojo.SNSUserInfo"%>
<%@page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户网页授权</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width,user-scalable=0">
<style type="text/css">
* {
	margin: 8;
	padding: 0
}

table {
	border: 1px dashed #B9B9DD;
	font-size: 12pt
}

td {
	border: 1px dashed #B9B9DD;
	word-break: break-all;
	word-wrap: break-word;
}
</style>


</head>
<body>
	<h1>用户网页授权信息</h1>

	<img alt="用户头像" src="${sessionScope.userinfo.headimgurl}">
	<table width="100%" cellspacing="0" cellpadding="0">
		<tr><td width="20%">属性</td><td width="80%">值</td></tr> 
		<tr><td >openId</td><td >${sessionScope.userinfo.openId}</td></tr> 
		<tr><td >昵称</td><td >${sessionScope.userinfo.nickname}</td></tr> 
		<tr><td >性别</td><td >${sessionScope.userinfo.sex}</td></tr> 
		<tr><td >性别</td><td >
			<c:if test="${sessionScope.userinfo.sex}==1">男</c:if>
			<c:if test="${sessionScope.userinfo.sex}==2">女</c:if>
			<c:if test="${sessionScope.userinfo.sex}==0">未知</c:if>
		</td></tr> 
		<tr><td >国家</td><td >${sessionScope.userinfo.country}</td></tr> 
		<tr><td >省份</td><td >${sessionScope.userinfo.province}</td></tr> 
		<tr><td >城市</td><td >${sessionScope.userinfo.city}</td></tr> 
		<tr><td >头像</td><td >${sessionScope.userinfo.headimgurl}</td></tr> 
		<tr><td >特权</td><td >${sessionScope.userinfo.privilege}</td></tr> 
	
	</table>

<hr><%
//		String openId = (String)session.getAttribute("openId");
		String openId = (String)session.getServletContext().getAttribute("openId");
		System.out.println(">>>>>>>>>>>>>>>>>>从application在jsp页面获取的openID"+openId);
		if(null != openId&& !"".equals(openId)){
		SNSUserInfo user = (SNSUserInfo)session.getServletContext().getAttribute(openId);
		System.out.println(">>>>>>>>>>>>>>>>>>从application在jsp页面获取的UserInfo数据："+JSON.toJSONString(user));
%>
	<img alt="用户头像" src="<%=user.getHeadimgurl() %>">
	<h5>openid值：<%=openId %></h5>
	<table width="100%" cellspacing="0" cellpadding="0">
		<tr><td width="20%">属性</td><td width="80%">值</td></tr> 
		<tr><td >openId</td><td ><%=user.getOpenId()%></td></tr> 
		<tr><td >昵称</td><td ><%=user.getNickname()%></td></tr> 
		<tr><td >性别</td><td ><%=user.getSex()%></td></tr> 
		<%request.setAttribute("sex", user.getSex()); %>
		<tr><td >性别</td><td >
			<c:if test="${requestScope.sex}== 1">男</c:if>
			<c:if test="${requestScope.sex}== 2">女</c:if>
			<c:if test="${requestScope.sex}== 0">未知</c:if>
		</td></tr> 
		<tr><td >国家</td><td ><%=user.getCountry()%></td></tr> 
		<tr><td >省份</td><td ><%=user.getProvince()%></td></tr> 
		<tr><td >城市</td><td ><%=user.getCity()%></td></tr> 
		<tr><td >头像</td><td ><%=user.getHeadimgurl()%></td></tr> 
		<tr><td >特权</td><td ><%=user.getPrivilege()%></td></tr> 
	</table>
	<% } %>
</body>
</html>