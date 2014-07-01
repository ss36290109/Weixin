package com.wx.weiixin.menu;
/**
 * 普通按钮
 * @author sl
 *
 */
public class CommonButton extends Button{

	private static final long serialVersionUID = 556881579273325614L;
	private String type;
	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
