package com.fizzblock.wechat.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fizzblock.wechat.httpclient.util.WeiXinApis;
import com.fizzblock.wechat.pojo.SNSUserInfo;
import com.fizzblock.wechat.pojo.WebAccessToken;

/**
 * 网页授权控制器
 * 
 * @author glen
 *
 */
@Controller
public class WebAuthController {

	public String APP_ID = "wx416c5da1eef311e6";

	public String APP_SECRET = "30d7fdede88444ff927934f50b367669";


	public boolean SNSAPI_USERINFO = true;

	public String STATE_PARAM = null;


	@RequestMapping(value = "/userAuth.action", method = RequestMethod.GET)
	public ModelAndView userAuth(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,
			String code) throws IOException {

		// ServletContext session = request.getServletContext();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String nowDate = df.format(new Date());

		System.out.println(nowDate
				+ ">>>>>>>>>userAuth,进入授权的redirect_url回调页面，准备获取用户信息");

		ModelAndView modelAndView = new ModelAndView();// 视图与模型类

		String userCode = request.getParameter("code");// 获取code
		System.out.println(">>>>>>>>>userAuth," + nowDate + "获取用户code值："+ userCode);

		// 用户取消授权
		if ("authdeny".equals(userCode)) {
			System.out.println(">>>>>>>>>userAuth," + nowDate + ",用户取消授权跳到主页面");
			modelAndView.setViewName("index.html");
			return modelAndView;
		}

		WebAccessToken accessToken = WeiXinApis.getAccessToken(APP_ID,APP_SECRET, userCode);// 获取网页授权access_token实体类，包含很多字段
		System.out.println(">>>>>>>>>userAuth,获取的accessToken信息+"+ JSON.toJSONString(accessToken));
		// 用户同意授权
		System.out.println(nowDate + ">>>>>>用户同意授权,从session中获取accessToken");
		// 判断是否是超时的code
		JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(accessToken));
		String errcode = jsonObject.getString("errcode");

		if (null != errcode && !"".equals(errcode)) {// 存在错误
			String err_msg = jsonObject.getString("errmsg");
			System.out.println(">>>>>>>>>"+ String.format("获取accessToken出错 errcode:%s errmsg:%s",errcode + "", err_msg));
			modelAndView.setViewName("userinfo_fail.jsp");// 页面跳转
			modelAndView.addObject("msg", String.format("获取accessToken出错 errcode:%s errmsg:%s", errcode + "",err_msg));
			return modelAndView;
		}

		if (null != accessToken && !"".equals(accessToken)) {
			// 获取访问的token凭证
			String token = accessToken.getAccessToken();
			System.out.println(nowDate + ">>>>>>>>>>获取网页授权token凭证：" + token);
			String openId = accessToken.getOpenId();// 获取用户标识
			System.out.println(nowDate + ">>>>>>>>>>获取用户OpenId：" + openId);
			System.out.println(nowDate + ">>>>>>>>>>从session中获取userInfo");

			SNSUserInfo userInfo = WeiXinApis.fetchUserinfo(token, openId,"zh_CN");

			System.out.println(nowDate + ">>>>>>>>>>>拉取用户信息："+ JSON.toJSONString(userInfo));// 拉取用户信息
			System.out.println(nowDate + ">>>>>>>>>>将userinfo信息存入到Session中");
//			session.setAttribute(openId, userInfo);
			modelAndView.addObject("msg", "用户授权成功！");
			modelAndView.setViewName("userinfo.jsp");
			modelAndView.addObject("userinfo", userInfo);
			return modelAndView;
		}

		// 其他情况跳转到主页
		modelAndView.setViewName("index.html");
		return modelAndView;
	}

	// 获取当前时间
	private String getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");// 设置日期格式
		return df.format(new Date());
	}





	// webAccessToken获取成功
	private boolean accessTokenSuccess(WebAccessToken accessToken) {

		boolean flag = false;
		// 判断是否是超时的code的存在的errcode信息
		JSONObject jsonObject = JSON
				.parseObject(JSON.toJSONString(accessToken));
		String errcode = jsonObject.getString("errcode");

		// 如果存在错误码且不为空，打印返回方法
		if (null != errcode && !"".equals(errcode)) {
			String err_msg = jsonObject.getString("errmsg");
			System.out.println(getNowDate()+ ">>>>>>>>>error信息--->"+ String.format("获取accessToken出错 errcode:%s errmsg:%s",errcode + "", err_msg));
			return false;
		} else {
			// 不存在错误码
			flag = true;
		}
		return flag;
	}

	// webAccessToken获取失败
	private boolean accessTokenFail(WebAccessToken accessToken) {
		return !accessTokenSuccess(accessToken);
	}

	// code已使用
	private boolean codeUsed(String flag) {
		return !codeUnused(flag);
	}

	// code未使用
	private boolean codeUnused(String flag) {
		return !"used".equals(flag);
	}

	// 用户授权被拒
	private boolean userAuthDeny(String code) {

		return !userAuthAcess(code);
	}

	// 用户同意授权
	private boolean userAuthAcess(String code) {
		return !"authdeny".equals(code);
	}

}
