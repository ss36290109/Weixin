package com.wx.weiixin.menu;

/**
 * 复杂按钮
 * 
 * @author sl
 * 
 */
public class ComplexButton extends Button {

	private static final long serialVersionUID = 4341970969730458323L;
	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}
}
