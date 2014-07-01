package com.wx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wx.weiixin.menu.AccessToken;
import com.wx.weiixin.menu.Button;
import com.wx.weiixin.menu.CommonButton;
import com.wx.weiixin.menu.CommonViewButton;
import com.wx.weiixin.menu.ComplexButton;
import com.wx.weiixin.menu.Menu;

/**
 * 公众平台通用接口工具类
 * 
 * @author sl
 * 
 */
public class WeixinUtil {

	private static final Logger LOGGER = Logger.getLogger(WeixinUtil.class);
	// 获取access_token的接口地址（GET） 限200（次/天）
	private final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建（POST） 限100（次/天）
	private final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单删除
	private final static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		HttpsURLConnection httpUrlConn = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			bufferedReader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream(), "utf-8"));
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			jsonObject = new JSONObject(buffer.toString());
		} catch (Exception e) {
			LOGGER.error("WeixinUtil--->httpRequest error", e);
		} finally {
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error("WeixinUtil--->httpRequest close io error", e);
				}
			if (httpUrlConn != null)
				httpUrlConn.disconnect();
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (Exception e) {
				accessToken = null;
				// 获取token失败
				LOGGER.error(
						"获取token失败 errcode:{} errmsg:{}"
								+ jsonObject.getInt("errcode")
								+ jsonObject.getString("errmsg"), e);
			}
		}
		return accessToken;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;
		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = new JSONObject(menu).toString();
		System.out.println(jsonMenu);
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				LOGGER.debug("创建菜单失败 errcode:{} errmsg:{}"
						+ jsonObject.getInt("errcode")
						+ jsonObject.getString("errmsg"));
			}
		}

		return result;
	}

	public static int deleteMenu(String accessToken) {
		int result = 0;
		String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				LOGGER.debug("删除菜单失败 errcode:{} errmsg:{}"
						+ jsonObject.getInt("errcode")
						+ jsonObject.getString("errmsg"));
			}
		}
		return result;
	}

	public static void main(String[] args) {
		deleteMenu(WeixinUtil.getAccessToken("wx07759361c950160a",
				"9d5896778ffcae549cae92cb27a5a226").getToken());
		com.wx.weiixin.menu.Menu menu = new com.wx.weiixin.menu.Menu();
		ComplexButton complexButton1 = new ComplexButton();
		complexButton1.setName("周边服务");

		CommonButton commonButton10 = new CommonButton();
		commonButton10.setKey("ZBMS");
		commonButton10.setName("周边美食");
		commonButton10.setType("click");

		CommonButton commonButton11 = new CommonButton();
		commonButton11.setKey("ZBXC");
		commonButton11.setName("周边小吃");
		commonButton11.setType("click");

		CommonButton commonButton12 = new CommonButton();
		commonButton12.setKey("ZBYL");
		commonButton12.setName("周边娱乐");
		commonButton12.setType("click");

		CommonButton commonButton13 = new CommonButton();
		commonButton13.setKey("ZBJD");
		commonButton13.setName("周边酒店");
		commonButton13.setType("click");

		CommonButton commonButton14 = new CommonButton();
		commonButton14.setKey("ZBJD");
		commonButton14.setName("周边优惠");
		commonButton14.setType("click");

		complexButton1
				.setSub_button(new Button[] { commonButton10, commonButton11,
						commonButton12, commonButton13, commonButton14 });

		// ************************************************************
		ComplexButton complexButton2 = new ComplexButton();
		complexButton2.setName("生活服务");

		CommonButton commonButton20 = new CommonButton();
		commonButton20.setKey("TQYB");
		commonButton20.setName("天气预报");
		commonButton20.setType("click");

		// CommonViewButton commonButton21 = new CommonViewButton();
		// commonButton21.setType("view");
		// commonButton21.setName("搞笑漫画");
		// commonButton21.setUrl("http://slwxapp.sinaapp.com/directComic.jsp");
		//
		// CommonButton commonButton22 = new CommonButton();
		// commonButton22.setKey("XH");
		// commonButton22.setName("笑话");
		// commonButton22.setType("click");

		CommonButton commonButton21 = new CommonButton();
		commonButton21.setKey("XZYS");
		commonButton21.setName("星座运势");
		commonButton21.setType("click");

		CommonButton commonButton22 = new CommonButton();
		commonButton22.setKey("RMTG");
		commonButton22.setName("热门团购");
		commonButton22.setType("click");

		CommonButton commonButton23 = new CommonButton();
		commonButton23.setKey("XLCK");
		commonButton23.setName("黄历查看");
		commonButton23.setType("click");

		CommonButton commonButton24 = new CommonButton();
		commonButton24.setKey("KDCX");
		commonButton24.setName("快递查询");
		commonButton24.setType("click");

		complexButton2
				.setSub_button(new Button[] { commonButton20, commonButton21,
						commonButton22, commonButton23, commonButton24 });

		// ************************************************************
		ComplexButton complexButton3 = new ComplexButton();
		complexButton3.setName("互动社区");
		CommonViewButton commonButton31 = new CommonViewButton();
		commonButton31.setType("view");
		commonButton31.setName("一街坊");
		commonButton31.setUrl("http://wx.wsq.qq.com/255773530");

		CommonButton commonButton32 = new CommonButton();
		commonButton32.setKey("LTLK");
		commonButton32.setName("聊天唠嗑");
		commonButton32.setType("click");

		CommonButton commonButton33 = new CommonButton();
		commonButton33.setKey("XH");
		commonButton33.setName("来个笑话");
		commonButton33.setType("click");

		CommonViewButton commonButton34 = new CommonViewButton();
		commonButton34.setType("view");
		commonButton34.setName("搞笑漫画");
		commonButton34.setUrl("http://slwxapp.sinaapp.com/directComic.jsp");

		complexButton3.setSub_button(new Button[] { commonButton31,
				commonButton32, commonButton33, commonButton34 });
		menu.setButton(new Button[] { complexButton1, complexButton2,
				complexButton3 });
		//
		createMenu(
				menu,
				WeixinUtil.getAccessToken("wx07759361c950160a",
						"9d5896778ffcae549cae92cb27a5a226").getToken());
	}
}
