package com.wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wx.service.CoreService;
import com.wx.util.SignUtil;

/**
 * 核心请求处理类
 * 
 * @author sl
 */
public class WxCoreServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8567941011390335530L;

	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (checkSignature(request)) {
			PrintWriter out = response.getWriter();
			// 随机字符串
			String echostr = request.getParameter("echostr");
			out.println(echostr);
			out.flush();
			out.close();
			out = null;
		}

	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		if (checkSignature(request)) {
			// 调用核心业务类接收消息、处理消息  
	        String respMessage = CoreService.processRequest(request);  
	        // 响应消息  
	        PrintWriter out = response.getWriter();  
	        out.println(respMessage);  
	        out.flush();
	        out.close();  
		}
	}

	/**
	 * 验证微信签名
	 * 
	 * @param request
	 * @return
	 */
	private boolean checkSignature(HttpServletRequest request) {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		return SignUtil.checkSignature(signature, timestamp, nonce);
	}
}
