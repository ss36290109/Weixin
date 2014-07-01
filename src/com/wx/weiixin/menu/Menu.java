package com.wx.weiixin.menu;

import java.io.Serializable;

/**
 * 
 * @author sl
 * 
 */
public class Menu implements Serializable {

	private static final long serialVersionUID = 2034107202826512105L;

	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
}
