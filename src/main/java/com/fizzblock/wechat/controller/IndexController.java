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

@Controller
public class IndexController {
		
		public String APP_ID = "wx416c5da1eef311e6";
	
		public String APP_SECRET = "30d7fdede88444ff927934f50b367669";
		
		public String REDIRECT_URI = "http://fizzblock.bceapp.com/getSNSUserinfo";
		
		public boolean SNSAPI_USERINFO = true;
		
		public String STATE_PARAM = null; 
		
	
		@RequestMapping(value = "/home")
		public String index(){
			return "index.html";
		}
		
		
		@RequestMapping(value = "/getSNSUserinfo" ,method=RequestMethod.GET)
		public ModelAndView getWeiXinUserInfo(HttpServletRequest request,HttpServletResponse response,String code) throws IOException {
			
			ServletContext session = request.getServletContext();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String nowDate =df.format(new Date());
			
			System.out.println(nowDate+">>>>>>>>>>>>>进入授权的redirect_url回调页面，准备获取用户信息");
			
			//视图与模型类
			ModelAndView modelAndView = new ModelAndView();
			
			//获取code
			String userCode = request.getParameter("code");
			
			//存入code判断code是不是已存在，已存在就跳转到详情页面，不存在就是可以拉取用户信息
			System.out.println(nowDate+"获取用户code值："+userCode);
//			System.out.println(nowDate+"springMVC参数绑定获取用户code值："+code);
			
			//用户同意授权
			if(!"authdeny".equals(userCode)){
				System.out.println(nowDate+">>>>>>用户同意授权");
				
				String useFlage = (String)session.getAttribute(userCode);
				
				//session中存在code，需判断code，拿到的code与session中code比较
				//code未被使用过
				if(!"used".equals(useFlage)){
					System.out.println(nowDate + ">>>>>>>>>>code未被使用过");
					//拉取用户信息
					System.out.println(nowDate+">>>>>>>>>从session中获取accessToken");
						WebAccessToken accessToken = (WebAccessToken)session.getAttribute("accessToken");
						//获取网页授权access_token实体类，包含很多字段
						if(null== accessToken){
							
							
							accessToken = WeiXinApis.getWebAccessToken(APP_ID, APP_SECRET, userCode);
							//判断是否是超时的code
							JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(accessToken));
							String errcode = jsonObject.getString("errcode");
							
							if(null != errcode&&!"".equals(errcode)){
								String err_msg = jsonObject.getString("errmsg");
								System.out.println(">>>>>>>>>"+String.format("获取accessToken出错 errcode:%s errmsg:%s", errcode+"",err_msg));
								
								//页面跳转
								modelAndView.setViewName("userinfo_fail.jsp");
								modelAndView.addObject("msg", String.format("获取accessToken出错 errcode:%s errmsg:%s", errcode+"",err_msg));
								return modelAndView;
							}else{
								session.setAttribute("accessToken", accessToken);
								System.out.println(nowDate+">>>>>>>>>未获取到accessToken，从微信拉取accessToken并存入session中");
								System.out.println(nowDate+">>>>>>>accessToken获取结果："+JSON.toJSONString(accessToken));
								
							}
							
						}
						
						if(null !=accessToken&&!"".equals(accessToken)){
							System.out.println(nowDate+">>>>>>>>>>web授权的accessToken不为空待用");
								//获取访问的token凭证
								String token = accessToken.getAccessToken();
								System.out.println(nowDate+">>>>>>>>>>获取token凭证："+token);
								//获取用户标识
								String openId = accessToken.getOpenId();
								System.out.println(nowDate+">>>>>>>>>>获取用户OpenId："+openId);
								
								System.out.println(nowDate+">>>>>>>>>>从session中获取userInfo");
								
								SNSUserInfo userInfo = (SNSUserInfo)session.getAttribute(openId);
								
								if(null == userInfo){
									
									System.out.println(nowDate+">>>>>>>>>>session中userInfo不存在，从微信拉取用户数据");
									//拉取用户信息
									userInfo = WeiXinApis.fetchSNSUserinfo(token, openId, "zh_CN");
									System.out.println(nowDate+">>>>>>>>>>>拉取用户信息："+JSON.toJSONString(userInfo));
									System.out.println(nowDate+">>>>>>>>>>将userinfo信息存入到Session中");
									session.setAttribute(openId, userInfo);
								}
								
								modelAndView.addObject("msg", "用户授权成功！");
								modelAndView.setViewName("userinfo.jsp");
								modelAndView.addObject("userinfo", userInfo);
								return modelAndView;
						}else{
							System.out.println(">>>>>>>>>>>>accessToken获取失败！");
							modelAndView.setViewName("userinfo_fail.jsp");
							modelAndView.addObject("msg", "用户授权失败！");
						}
						
				System.out.println(">>>>>>>>>>>>>>将已使用过的code存入session中待下次判断");
				session.setAttribute(userCode, "used");
				
			}else{//code被使用过，且已经拉取过用户信息，直接跳转到用户详情页面
				System.out.println(nowDate+">>>>>>code已经获取过用户授权信息，无需重复获取！");
				modelAndView.addObject("msg", "code已经获取过用户授权信息，无需重复获取！");
				modelAndView.setViewName("userinfo.jsp");
			}
				
			}else{
				System.out.println(nowDate+">>>>>>>>>>>>>>>用户授权拒绝，无法获取code信息");
				//页面跳转
				modelAndView.setViewName("userinfo_fail.jsp");
				modelAndView.addObject("msg", "用户授权失败！");
			}
			
			return modelAndView;
		}
		
		
		/**
		 * 方法业务逻辑重构
		 * @param session
		 * @param code
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(value = "/getSNSUserinfo2.do" ,method=RequestMethod.GET)
		public ModelAndView getWeiXinUserInfo2(HttpSession session,String code) throws IOException {
			
			System.out.println(getNowDate()+">>>>>>>>>>>>>>>进入回调url");
			
			ModelAndView modelAndView = new ModelAndView();

			//用户授权通过
			if(userAuthAcess(code)){
				return userAuthResponse(code,session,modelAndView);
			}
			
			//用户授权被拒绝
			if(userAuthDeny(code)){
				System.out.println(getNowDate()+">>>>>>>>>>>>>>>用户授权拒绝，无法获取code信息");
				//页面跳转
				modelAndView.setViewName("userinfo_fail.jsp");
				modelAndView.addObject("msg", "用户授权失败！");
//				modelAndView.addObject("msg", "绑定用户成功！");
			}
			
			return modelAndView;
		}

		
		//获取当前时间
		private String getNowDate() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//设置日期格式
			return df.format(new Date());
		}


		//用户授权并可以获取code
		private ModelAndView userAuthResponse(String code,HttpSession session,ModelAndView modelAndView) {
			//判断code
//			String flag = (String)session.getAttribute(code);
			System.out.println(getNowDate()+">>>>>>回调请求的code："+code);
			String flag = (String)session.getServletContext().getAttribute(code);
			System.out.println(getNowDate()+">>>>>>回调请求的code使用标志："+flag);
			
			//code使用过了,直接返回，用户详情页面
			if(codeUsed(flag)){
				System.out.println(getNowDate()+">>>>>>code已经获取过用户授权信息，无需重复获取！");
//				modelAndView.addObject("msg", "code已经被使用！");
				//人性化显示
				modelAndView.addObject("msg", "用户绑定成功！");
				modelAndView.setViewName("userinfo_fail.jsp");
				return modelAndView;
			}
			
			//code未被使用
			if(codeUnused(flag)){
				
				System.out.println(getNowDate()+">>>>>>code未被使用"+code);
				//使用code获取accessToken
				WebAccessToken  accessToken =  WeiXinApis.getWebAccessToken(APP_ID, APP_SECRET, code);
				
				//accessToken获取失败,跳转到失败界面，打印日志信息
				if(accessTokenFail(accessToken)){
					System.out.println(getNowDate()+">>>>>>accessToken获取失败,跳转到失败界面"+JSON.toJSONString(accessToken));
					modelAndView.setViewName("userinfo_fail.jsp");
					modelAndView.addObject("msg", getNowDate()+"获取accessToken出错 "+JSON.toJSONString(accessToken));
					return modelAndView;
				}
				
				//accessToken获取成功，拉取用户信息
				if(accessTokenSuccess(accessToken)){
					//获取访问的token凭证
					System.out.println(getNowDate()+">>>>>>accessToken获取成功，准备拉取用户信息"+JSON.toJSONString(accessToken));
					String token = accessToken.getAccessToken();
					String openId = accessToken.getOpenId();
					System.out.println(String.format(getNowDate()+">>>>>>>>>>获取token凭证：%s 获取用户OpenId：%s", token,openId));
					
					//拉取用户信息
					SNSUserInfo userInfo = WeiXinApis.fetchSNSUserinfo(token, openId, "zh_CN");
					System.out.println(getNowDate()+">>>>>>>>>>>拉取用户信息："+JSON.toJSONString(userInfo));
					
					//存入用户信息到application，以openId作为键
					saveUserInfo(openId,userInfo,session);
					//信息存入request中
					modelAndView.addObject("userinfo", userInfo);
					modelAndView.addObject("openId", openId);
					modelAndView.setViewName("userinfo.jsp");
					//标记code已经被使用了
					//原来的方式
//					session.setAttribute(code, "used");
					session.getServletContext().setAttribute(code, "used");
					System.out.println(getNowDate()+">>>>>>>>>>>标记code已用");
					
					//保存用户信息到数据库--模拟
				}
				
			}
			
			return modelAndView;
		}

		
		
		/**
		 * 获取绑定的用户信息，暂时从application中去取
		 * @param session
		 * @param code
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(value = "/getUserInfoBind.do" ,method=RequestMethod.GET)
		public ModelAndView getUserInfoBind(HttpSession session,String code) throws IOException {
			System.out.println(getNowDate()+">>>>>>进入获取用户信息界面,code："+code);
			ModelAndView modelAndView = new ModelAndView();
			//判断code
//			String flag = (String)session.getAttribute(code);
			String flag = (String)session.getServletContext().getAttribute(code);
			
			//code使用过了,直接返回，用户详情页面
			if(codeUsed(flag)){
				System.out.println(getNowDate()+">>>>>>code已失效code:"+code);
//				modelAndView.addObject("msg", "code已经被使用！");
//				modelAndView.setViewName("userinfo_fail.jsp");
				
			//由于多次回调，最终的地址会落在这里，这里来进行跳转，由于数据存在session中，不担心获取不到数据
				modelAndView.setViewName("userinfo.jsp");
				
			}
			
			//code未被使用
			if(codeUnused(flag)){
				System.out.println(getNowDate()+">>>>>>code未失效，code:"+code);
				WebAccessToken webAccessToken = WeiXinApis.getWebAccessToken(APP_ID, APP_SECRET, code);
				//accessToken获取成功，拉取用户信息
				if(accessTokenSuccess(webAccessToken)){
					System.out.println(getNowDate()+">>>>>>获取base_info的accessToken获取成功，拉取用户信息 accessToken:"+JSON.toJSONString(webAccessToken));
					String openId = webAccessToken.getOpenId();
					
					if(null!=openId  &&!"".equals(openId)){
						
						//从服务端获取用户信息，根据openId
						
						SNSUserInfo userInfo = getUserInfo(openId, session);
						
						//设置openId否则jsp为空指针
//						modelAndView.addObject("openId", openId);
						//我看request上面已经就没用了，多次回调，这一次成功在第一次上面
						System.out.println(getNowDate()+">>>>>>>>>>>>>>>将获取到的已绑定用户数据，存储到session的作用域");
						
						//设置为session级别
						System.out.println(getNowDate()+">>>>>>>>>>>>>>>存储openId到session:"+openId);
						session.setAttribute("openId", openId);
						session.setAttribute("userinfo", userInfo);
						System.out.println(getNowDate()+">>>>>>>>>>>>>>>存储openId到application:"+openId);

						session.getServletContext().setAttribute("openId", openId);
						
						
						//第一次才会有值
/*						modelAndView.addObject("userinfo", userInfo);
						modelAndView.setViewName("userinfo.jsp");*/
						//标记code已用被使用
						session.getServletContext().setAttribute(code, "used");
						System.out.println(getNowDate()+">>>>>>>>>>>标记code已用");
					}else{
						System.out.println(getNowDate()+"baseInfo_获取openId失败,accessToken:"+JSON.toJSONString(webAccessToken));
						modelAndView.addObject("msg", getNowDate()+"获取openId失败,accessToken:"+JSON.toJSONString(webAccessToken));
						modelAndView.setViewName("userinfo_fail.jsp");
						return modelAndView;
					}
					
				}
			}
			
			return modelAndView;
		}
		
		
		private SNSUserInfo getUserInfo(String openId, HttpSession session) {
			System.out.println(getNowDate()+">>>>>>>>>>>>>>>从application中获取绑定的用户信息,用户openId为:"+openId);
			SNSUserInfo userinfo = (SNSUserInfo) session.getServletContext().getAttribute(openId);
			System.out.println(getNowDate()+">>>>>>>>>>>>>>>从application中获取绑定用户信息,用户信息为:"+JSON.toJSONString(userinfo));
			return userinfo;
		}


		private void saveUserInfo(String openId, SNSUserInfo userInfo,
				HttpSession session) {
			System.out.println(getNowDate()+">>>>>>>>>>>>>>>开始从服务端存储用户信息,用户openId为:"+openId);
			session.getServletContext().setAttribute(openId, userInfo);
			System.out.println(getNowDate()+">>>>>>>>>>>>>>>开始从服务端存储用户信息,用户信息为为:"+JSON.toJSONString(userInfo));
		}


		//webAccessToken获取成功
		private boolean accessTokenSuccess(WebAccessToken accessToken) {
			
			boolean flag = false;
			//判断是否是超时的code的存在的errcode信息
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(accessToken));
			String errcode = jsonObject.getString("errcode");
			
			//如果存在错误码且不为空，打印返回方法
			if(null != errcode&&!"".equals(errcode)){
				String err_msg = jsonObject.getString("errmsg");
				System.out.println(getNowDate()+">>>>>>>>>error信息--->"+String.format("获取accessToken出错 errcode:%s errmsg:%s", errcode+"",err_msg));
				return false;
			}else{
				//不存在错误码
				flag = true;
			}
			return flag;
		}

		//webAccessToken获取失败
		private boolean accessTokenFail(WebAccessToken accessToken) {
			return !accessTokenSuccess(accessToken);
		}


		//code已使用
		private boolean codeUsed(String flag) {
			return !codeUnused(flag);
		}

		//code未使用
		private boolean codeUnused(String flag) {
			return !"used".equals(flag);
		}


		//用户授权被拒
		private boolean userAuthDeny(String code) {
		
			return !userAuthAcess(code);
		}


		//用户同意授权
		private boolean userAuthAcess(String code) {
			return !"authdeny".equals(code);
		}
		
}
