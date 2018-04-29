<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

   <title>个人中心</title>
    <meta name="keyword" content="个人中心"/>
    <meta name="description" content="个人中心"/>

    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="HandheldFriendly" content="true">
    <meta name="MobileOptimized" content="320">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <meta name="full-screen" content="no">
    <meta name="x5-fullscreen" content="true">
    <meta name="x5-page-mode" content="app">
    <meta name="msapplication-tap-highlight" content="no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <!--[if IE 8]><meta http-equiv="X-UA-Compatible" content="IE=8"><![endif]-->
    <link rel="shortcut icon" href="http://statics.oneplus.cn/img/oneplus.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="css/common_pc.css?v=20150718">
    <script src="http://statics.oneplus.cn/v2/core.js?v=20150718" id="oneplusjs" updatelist="" update=""></script>
	<script>
    document.documentElement.className +=  op.isMobile ? ' mobile ' : ' pc ';
	</script>
    <!--[if IE 8]>
        <script type="text/javascript" src="http://statics.oneplus.cn/v2/lib/PIE.js?v=20150718"></script>
    <![endif]-->
<link rel="stylesheet" type="text/css" href="css/m-index.css?v=20150718">

<style type="text/css">
.user-hd .my-avatar {
    width: 80px;
    height: 80px;
    margin-top: 30px;
}
</style>
</head>
<body>
<div id="op-wrap">
        <div id="op-aside"></div>
        <div id="op-wrap-mask"></div>
        <div id="op-content">

<script type="text/template" id="tpl-mobSuccess">
    <div class="op-register-success op-mob-success">
        <h3><span class="titleico">注册成功</span></h3>
        <div class="ico"><span class="success"></span></div>
        <div class="txt">
            <p><span class="mob"></span>$userName</p>
            <p>忘记密码或丢失账户可通过您的手机号码找回</p>
        </div>
        <div class="goto"><a href="javascript:;" class="loginNow"><i></i>立即登录</a></div>
    </div>
</script>
<script type="text/template" id="tpl-mailSuccess">
    <div class="op-register-success op-mail-success">
        <h3><span class="titleico">邮箱注册激活</span></h3>
        <div class="txt">
            <p>系统已向您的邮箱 $userName</p>
            <p>发送了一封激活邮件</p>
        </div>
        <div class="goto"><a href="$mailUrl" target="_blank"><i></i>登录邮箱</a> 点击链接激活你的账户</div>
        <div class="if"><span class="lamp"></span>如果没有收到激活邮件  <a href="javascript:;"><i></i>重新发送</a></div>
    </div>
</script>
<script type="text/template" id="tpl-userCenter">
     <div class="user-hd">
    <div class="my-avatar">
        <a href="" onclick="_opq.push(['trackEvent','onepluscn_A003','修改头相'])">
            <img src="http://statics.oneplus.cn/v2/img/user/avatar-default.png" alt="">
            <span class="update-tip"><i class="i-update-avatar"></i></span>
        </a>
    </div>
    <div class="my-nickname">
        <span>
			<a href="http://account.oneplus.cn" onclick="_opq.push(['trackEvent','onepluscn_A004','个人账户'])"><em></em></a>
        </span>
    </div>
    <ul class="my-stuffs">
        <li>
            <a href="http://my.oneplus.cn/coupon" onclick="_opq.push(['trackEvent','onepluscn_A005','优惠'])">
                <span class="my-coupon">0</span>
                <em>优惠券</em>
            </a>
        </li>
        <li>
            <a href="http://my.oneplus.cn/point" onclick="_opq.push(['trackEvent','onepluscn_A006','积分'])">
                <span class="my-point">0</span>
                <em>积分</em>
            </a>
        </li>
        <li>
            <a href="http://account.oneplus.cn#mobile" onclick="_opq.push(['trackEvent','onepluscn_A007','修改手机'])">
                <span class="my-usemob"><i class="i-account-mobile-bind"></i></span>
                <em>修改手机</em>
            </a>
        </li>
        <li>
            <a href="http://account.oneplus.cn#email" onclick="_opq.push(['trackEvent','onepluscn_A009','修改邮箱'])">
                <span class="my-usemail"><i class="i-account-email-bind"></i></span>
                <em>修改邮箱</em>
            </a>
        </li>
    </ul>
</div>
    <ul class="mylink">
        <li><a href="http://my.oneplus.cn/order" class="order" onclick="_opq.push(['trackEvent','onepluscn_A011','我的订单'])"><i></i>我的订单</a> <span class="op-circle">5</span> <span class="bracket"></span></li>
        <li><a href="http://account.oneplus.cn" class="my" onclick="_opq.push(['trackEvent','onepluscn_A012','我的账户'])"><i></i>我的账户</a><span class="bracket"></span></li>
    </ul>
	<div class="links">
        <span class="home" onclick="_opq.push(['trackEvent','onepluscn_A016','进入个人中心']);location.href=op.URL.my.index;"><i></i>进入个人中心</span>
        <span class="logout" onclick="_opq.push(['trackEvent','onepluscn_A017','退出登录']);location.href=op.URL.account.logout;"><i></i>退出登录</span>
    </div>
</script>
<script type="text/template" id="tpl-userLogin">
    <div class="op-login mini">
        <h3><span class="titleico"></span></h3>
        <div class="userbox">
            <input type="text" id="loginName" placeholder="手机、邮箱或用户名" />
            <span class="tip"></span>
        </div>
        <div class="passbox">
            <input type="password" id="passWord" placeholder="密码" />
            <span class="find"></span>
        </div>
        <div class="codebox">
            <input type="text" id="verifyCode" placeholder="验证码"/>
            <img class="codeimg" src="http://account.oneplus.cn/getVerifyImage" alt="图片验证码" />
        </div>
		<div class="err_message"></div>
        <div class="underbox"><div class="loginbtn" onclick="_opq.push(['trackEvent','onepluscn_A019','登 录'])"> 登 录 </div></div>
        <div class="ft-operate">
			<a href="http://account.oneplus.cn/login/forget" class="link find-pwd"><i class="i-find"></i>忘记密码</a>
			<a href="http://account.oneplus.cn/regist" class="link"><i class="arrow"></i>注册</a>
		</div>
		<div class="otherlogin">
            <a href="http://account.oneplus.cn/qqlogin?state=1" class="qq-login" onclick="_opq.push(['trackEvent','onepluscn_A013','QQ登录'])"></a>
            <a href="http://account.oneplus.cn/wblogin?scope=1" class="sina-login" onclick="_opq.push(['trackEvent','onepluscn_A014','微博登录'])"></a>
        </div>
    </div>
</script>
<script type="text/template" id="tpl-userRegister">
    <div class="op-register mini">
			<form action="" onsubmit="return false;">
                        <h3><span class="titleico">注 册</span></h3>
                        <div class="userbox">
                            <div class=""></div>
                            <input type="text" class="userId" placeholder="手机或邮箱" />
                            <span class="tip"></span>
                        </div>
                        <div class="codebox">
                            <input type="text" class="verifyCode" placeholder="图片验证码"/>
                            <img class="codeimg" src="http://account.oneplus.cn/getVerifyImage" alt="图片验证码"/>
                        </div>
                        <div class="codebox getSMS">
                            <input type="text" class="smsCode" placeholder="短信验证码"/>
                            <span class="getSMSBtn"><i></i>获取验证码</span>
                            <span class="time"><i></i><em>120</em>s</span>
                        </div>
                        <div class="passbox">
                            <input type="password" class="passWord" placeholder="密码6~16位，数字/字母/字符至少两种" /><span class="tip"></span>
                        </div>
                        <div class="passbox">
                            <input type="password" class="passWord2" placeholder="确认密码" />
                            <span class="tip"></span>
                        </div>
                        <div class="note">注册一加，即表示同意一加的<a href="http://account.oneplus.cn/agreement" target="_blank" class="user" onclick="_opq.push(['trackEvent','onepluscn_A019','用户协议'])">用户协议</a>。
                        </div>
                        <button class="btn registerBtn" type="submit" onclick="_opq.push(['trackEvent','onepluscn_A020','确 定'])">确 定</button>
           				<div class="ft-operate"><a href="http://account.oneplus.cn/login" class="link"><i class="arrow"></i>登录</a></div>
			 </form>
	</div>
</script>

<script>
window.op = window.op || {};
op.startTime = new Date();
op.URL = {
		staticUrl: {
	        staticHead: "http://statics.oneplus.cn"
	    },
	    cartUrl: {
	        query: 'http://store.oneplus.cn/cart/query',
	        add: 'http://store.oneplus.cn/cart/add',
	        del: 'http://store.oneplus.cn/cart/del',
	        update: 'http://store.oneplus.cn/cart/update',
	        getCartNum: 'http://store.oneplus.cn/cart/ajaxCartNum',
	        cartPage: 'http://store.oneplus.cn/cart',
	        orderPage: 'http://store.oneplus.cn/order/tocheckout',
	        storePage: 'http://store.oneplus.cn',
	        wxPay: 'http://store.oneplus.cn/pay/wxpayment',
	        notice:'http://store.oneplus.cn/arrival/notice/add'  //到货提醒
	    }
	    ,account:{
	        login:"/onepluslogin"
	        ,logout:"http://account.oneplus.cn/logout"
	        ,get:"http://account.oneplus.cn/ajaxGetUserInfo"
	        ,reg:"/register"
	        ,find:"http://account.oneplus.cn/login/forget"
	        ,sendCode:"http://account.oneplus.cn/sendCode"
	        ,phplogin:"http://account.oneplus.cn/login/phplogin"
	        ,loginPage:"http://account.oneplus.cn/login"
	        ,regPage:"http://account.oneplus.cn/regist"
	    }
	    ,xxoo:"register|login|passwordFormatVerify|forgetSendCode|resetPassWord|checkVerifyCode|updatePassWord"
	    ,my:{
	        index:"http://my.oneplus.cn"
		    //静态资源服务器
		    ,noPhoto:"http://statics.oneplus.cn/v2/img/user/avatar-default-s.png"
		    ,bindIMEI:"http://my.oneplus.cn/bindImei"
	    }
	    ,check:{
	         opid:"http://account.oneplus.cn/verify"
	        ,sendSMS:"http://account.oneplus.cn/sendSmsCode"
	    	//验证码
	    	,getVerifyImg:"http://account.oneplus.cn/getVerifyImage"
	    }
	    ,agreement:{
	         JDB:"http://www.oneplus.cn/jdb/agreement"
	        ,privacy:"http://account.oneplus.cn/agreement_privacy"
	        ,user:"http://account.oneplus.cn/agreement"
	    }
	    ,userAddress:{
	        addressList: 'http://www.oneplus.cn/user/addr/region',
	        query: 'http://www.oneplus.cn/user/addr/query',
	        add: 'http://www.oneplus.cn/user/addr/add',
	        edit: 'http://www.oneplus.cn/user/addr/modify',
	        del: 'http://www.oneplus.cn/user/addr/del',
	        mlist:'http://store.oneplus.cn/pcas/mlist',
	        setDefault: 'http://www.oneplus.cn/user/addr/default',
	        addressDetail: 'http://www.oneplus.cn/user/addr',
	        addressCenter: 'http://store.oneplus.cn/order/addressCenter'
	    }
	    ,service:{
	    	verifyIMEI: 'http://service.oneplus.cn/verifyIMEI'
	    	,findKnowledge: 'http://service.oneplus.cn/findKnowledge'
	    	,addFeedback: 'http://service.oneplus.cn/addFeedback'
	    	,getPoint: 'http://service.oneplus.cn/getPoint'
	    	,serarchKnowledge: 'http://service.oneplus.cn/serarchKnowledge'
	    	,myInsurance: 'http://service.oneplus.cn/queryInsurance'
	    }
	 	// 上传头像flash路径(域名和打开的页面一致)
	    ,uploadSwf: {
	       flash: 'http://account.oneplus.cn/account'
	    }

};
seajs.use('http://statics.oneplus.cn/v2/js/g/top-head.js',function(topHead){
    topHead.init({});
});
</script>
			 <div class="user-hd">
			    <div class="my-avatar">

<!-- 			        		<img src="http://thirdwx.qlogo.cn/mmopen/vi_32/O9ylcAg1qeib3J1ZZUkp7X9hBCkOBzPVp7LfGTIoSiccYdsDWMGToCnic9yvvEFibyUBqjrTwibHWaxmUW5Mtm0UukQ/132" alt=""> -->
			        		<img src="${requestScope.userinfo.headimgurl}" alt=""/>
			            <span class="update-tip"><i class="i-update-avatar"></i></span>

			    </div>                
			    <div class="my-nickname">
			        <span>
<!--            				 <em>后海大章鱼?</em> -->
           				 <em>${requestScope.userinfo.nickname}</em>
			        </span>
			    </div>    
			    <ul class="my-stuffs">

			        <li>
			            <a href="http://my.oneplus.cn/point">
			                <span class="my-point">5</span>
			                <em>积分</em>
			            </a>
			        </li>
			        <li>
			            <!-- 已绑定时显示 -->
			             <a href="http://account.oneplus.cn#mobile">
			                <span><i class="i-account-mobile-unbind"></i></span>
			                <em>绑定手机</em>
			            </a>
			        </li>

			    </ul>
			</div>  
			
            <div class="user-bd">
				<div class="user-aside">
				    <dl class="user-menu">
				        <dt>个人中心</dt>
				        <dd><a href="http://my.oneplus.cn/order"><i class="i-my-order"></i>我的订单</a></dd>
				        <dd><a href="http://my.oneplus.cn/coupon"><i class="i-my-cunpon"></i>优惠券</a></dd>
				        <dd><a href="http://my.oneplus.cn/address"><i class="i-my-address"></i>收货地址</a></dd>
				        <dd><a href="http://my.oneplus.cn/award"><i class="i-my-prize"></i>我的奖品</a></dd>
				    </dl>

				</div> 

<footer class="op-footer clearfix" id="op-footer">

	<div class="op-copyright">
		<p>Copyright&copy;<a href="http://www.lanrenmb.com/" target="_blank">懒人模板</a> All Rights Reserved 
(2013-2015) 版权所有
</p>
		<span class="icon-kx">
		    <script type="text/javascript">
		    document.write('<scri'+'pt src="http://kxlogo.knet.cn/seallogo.dll?sn=e14063044030050707awmq000000&size=0"></scri'+'pt>');
		    </script>
		</span>
		<span class="icon-smrz">
				<a key ="54c85a40c274e71c5865cbf7"  logo_size="83x30"  logo_type="realname"  href="http://www.anquan.org"><script src="http://static.anquan.org/static/outer/js/aq_auth.js"></script></a>
		</span>
	</div>
</footer>
 <script>
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?e3c7f7f17054135e8f86f4c943459d8b";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
</script>
<script type="text/javascript">
    (function() {
        var a = document.createElement("script");
        a.type = "text/javascript";
        a.async = true;
        a.src = "http://analytics.oneplus.cn/opdc.js";
        var b = document.getElementsByTagName("script")[0x0];
        b.parentNode.insertBefore(a, b)
    })();
</script>		    </div>
        </div>
</div>    
<!--[if lte IE 9]>
<script src="http://statics.oneplus.cn/v2/lib/placeholder.min.js"></script>
<![endif]-->
</body>

</html>