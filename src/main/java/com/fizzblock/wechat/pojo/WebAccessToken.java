package com.fizzblock.wechat.pojo;

import java.io.Serializable;

/**
 * 微信授权的token实体类封装
 * 用于网页授权等
 * @author glen
 *
 */
public class WebAccessToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//网页授权接口凭证
	private String accessToken;
	
	//凭证的有效时长
	private int expiresIn;
	
	//刷新凭证
	private String refreshToken ;
	
	//用户标识
	private String openId;
	
	//用户授权作用域
	private String scope;

	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
