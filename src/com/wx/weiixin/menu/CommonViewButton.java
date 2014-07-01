package com.wx.weiixin.menu;
/**
 * 普通按钮
 * @author sl
 *
 */
public class CommonViewButton extends Button{

	private static final long serialVersionUID = 556881579273325614L;
	private String type;
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
