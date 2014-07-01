package com.wx.weiixin.menu;

import java.io.Serializable;
/**
 * 微信菜单基类
 * @author sl
 *
 */
public class Button implements Serializable {
	
	private static final long serialVersionUID = 7579453084842600760L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
