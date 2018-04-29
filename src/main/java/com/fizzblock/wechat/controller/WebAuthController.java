package com.fizzblock.wechat.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fizzblock.wechat.httpclient.util.WeiXinApis;
import com.fizzblock.wechat.pojo.SNSUserInfo;
import com.fizzblock.wechat.pojo.WebAccessToken;
import com.fizzblock.wechat.util.common.LogUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

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

		LogUtils.info(">>>>>>>>>userAuth,进入授权的redirect_url回调页面，准备获取用户信息");

		ModelAndView modelAndView = new ModelAndView();// 视图与模型类

		String userCode = request.getParameter("code");// 获取code
		LogUtils.info(">>>>>>>>>userAuth," + nowDate + "获取用户code值："+ userCode);

		// 用户取消授权
		if ("authdeny".equals(userCode)) {
			LogUtils.info(">>>>>>>>>userAuth," + nowDate + ",用户取消授权跳到主页面");
			modelAndView.setViewName("index.html");
			return modelAndView;
		}

		WebAccessToken accessToken = WeiXinApis.getWebAccessToken(APP_ID,APP_SECRET, userCode);// 获取网页授权access_token实体类，包含很多字段
		LogUtils.info(">>>>>>>>>userAuth,获取的accessToken信息+"+ JSON.toJSONString(accessToken));
		// 用户同意授权
		LogUtils.info(">>>>>>>>>userAuth,用户同意授权,从session中获取accessToken");
		// 判断是否是超时的code
		JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(accessToken));
		String errcode = jsonObject.getString("errcode");

		if (null != errcode && !"".equals(errcode)) {// 存在错误
			String err_msg = jsonObject.getString("errmsg");
			LogUtils.info(">>>>>>>>>userAuth,"+ String.format("获取accessToken出错 errcode:%s errmsg:%s",errcode + "", err_msg));
			modelAndView.setViewName("userinfo_fail.jsp");// 页面跳转
			modelAndView.addObject("msg", String.format("获取accessToken出错 errcode:%s errmsg:%s", errcode + "",err_msg));
			return modelAndView;
		}

		if (null != accessToken && !"".equals(accessToken)) {
			// 获取访问的token凭证
			String token = accessToken.getAccessToken();
			LogUtils.info(">>>>>>>>>userAuth,获取网页授权token凭证：" + token);
			String openId = accessToken.getOpenId();// 获取用户标识
			LogUtils.info(">>>>>>>>>userAuth,获取用户OpenId：" + openId);
			LogUtils.info(">>>>>>>>>userAuth,从session中获取userInfo");

			SNSUserInfo userInfo = WeiXinApis.fetchSNSUserinfo(token, openId,"zh_CN");
			LogUtils.info(">>>>>>>>>userAuth,将授权信息存储至session中,userinfo信息"+JSON.toJSONString(userInfo));
			session.setAttribute("userinfo", userInfo);
//			session.setAttribute(openId, userInfo);
//			modelAndView.addObject("msg", "用户授权成功！");
//			modelAndView.setViewName("userinfo.jsp");
			modelAndView.setViewName("register.jsp");
			modelAndView.addObject("userinfo", userInfo);
			return modelAndView;
		}

		// 其他情况跳转到主页
		modelAndView.setViewName("index.html");
		return modelAndView;
	}

	
	//存入session中的键是 	public final static String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";
    @Autowired
    private Producer captchaProducer = null;

    
    @RequestMapping(value = "/captcha")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        LogUtils.info(">>>>>>>>>>>>>getKaptchaImage,后端生成的验证码："+capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
	
	
	/**
	 * 用户注册
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/register")
    public String  register(HttpServletRequest request, HttpSession session,String checkCode) throws Exception {
    	String captcher = request.getParameter("checkCode");
    	LogUtils.info(">>>>>>>>>>>>>register,request提交获取的captcher："+captcher);
    	LogUtils.info(">>>>>>>>>>>>>register,参数绑定获取的captcher："+checkCode);
    	
    	String kaptchaPut = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
    	
    	LogUtils.info(">>>>>>>>>>>>>register,session中生成的验证码："+kaptchaPut);
    	
    	//验证通过，跳转主页
    	if(kaptchaPut.equals(captcher)){
    		LogUtils.info(">>>>>>>>>>>>>register,验证码通过，跳转到主页面，更新绑定信息");
    		return "index.html";
    	}
    	//未通过跳转回注册页面
    	
    	LogUtils.info(">>>>>>>>>>>>>register,验证码通过，请重新输入");
    	request.setAttribute("msg", "验证码不正确，请重新输入");
    	
    	return "register.jsp";
    	
    }
    
    
    @RequestMapping(value = "/register2")
    public String  register2(HttpServletRequest request, HttpSession session,HttpServletResponse response,String checkCode,String telephone,String openid) throws Exception {
    	String captcher = request.getParameter("checkCode");
    	
    	LogUtils.info(String.format(">>>>>>>>>>>>>register2,,获取请求参数checkCode:%s telephone:%s openId:%s", checkCode,telephone,openid));
    	
    	String kaptchaPut = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
    	
    	LogUtils.info(">>>>>>>>>>>>>register2,session中生成的验证码："+kaptchaPut);
    	
    	//验证通过，跳转主页
    	if(kaptchaPut.equals(captcher)){
    		LogUtils.info(">>>>>>>>>>>>>register2,验证码通过，跳转到主页面，更新绑定信息");
    		
    		LogUtils.info(">>>>>>>>>>>>>register2,当前用户的openid:"+openid);
    		SNSUserInfo userinfo = (SNSUserInfo) session.getAttribute("userinfo");
    		LogUtils.info(">>>>>>>>>>>>>register2,从session中获取userinfo信息，"+JSON.toJSONString(userinfo));
    		if(null !=userinfo){
    			request.setAttribute("userinfo", userinfo);
    			LogUtils.info(">>>>>>>>>>>>>register2,转发请求");
    			request.getRequestDispatcher("userCenter.jsp").forward(request, response);
    		}else{
    			LogUtils.info(">>>>>>>>>>>>>register2,未获取到userinfo信息,返回注册页面");
    	    	return "register.jsp";
    		}
    		//转发请求
//    		return "index.html";
    	}else{
    		//未通过跳转回注册页面
    		LogUtils.info(">>>>>>>>>>>>>register2,验证码未通过，请重新输入");
    		request.setAttribute("msg", "验证码不正确，请重新输入");
    	}
    	
    	return "register.jsp";
    	
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
