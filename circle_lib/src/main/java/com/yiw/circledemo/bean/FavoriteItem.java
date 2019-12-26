package com.yiw.circledemo.bean;

import java.io.Serializable;
/**
 * 
* @ClassName: FavoriteItem
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:44:56 
*
 */
public class FavoriteItem implements Serializable{

	private String user;
	private String full_name;

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
