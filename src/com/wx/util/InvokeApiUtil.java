package com.wx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
/**
 * url 调用 api
 * @author sl
 *
 */
public class InvokeApiUtil {

	private InvokeApiUtil() {
	}

	public static String getApiResult(String apiUrl) throws IOException {
		LogUtil.debug("apiUrl:::"+apiUrl);
		URL u = new URL(apiUrl);
		URLConnection conn = u.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF8"));
		String data = "";
		String line;
		while ((line = br.readLine()) != null) {
			data += line;
		}
		br.close();
		return data;
	}
}
