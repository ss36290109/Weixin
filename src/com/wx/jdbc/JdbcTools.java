package com.wx.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.wx.util.LogUtil;

/**
 * SAE的MYSQL服务
 * @author sl
 *
 */
public class JdbcTools {
	private static final String AccessKey = "12xmojmm2j";
	private static final String SecretKey = "24iyi332l45zixhj52x1mljw5wh2l2lyj5j4l0mk";
	private static final String URL = "jdbc:mysql://r.rdc.sae.sina.com.cn:3307/app_slwxapp";

	private JdbcTools() {
	}

	private static Connection getConnection() {
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL, AccessKey, SecretKey);
		} catch (SQLException e) {
			LogUtil.error("mysql获取连接异常", e);
		}
		return con;
	}

	public static Map<String, String> getYelloPageResult(String year,
			String month, String day) {
		Connection connection = getConnection();
		Map<String, String> resuMap = new HashMap<String, String>();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("select good,bad from yellopage where year=? and month=? and day=?");
			preparedStatement.setString(1, year);
			preparedStatement.setString(2, month);
			preparedStatement.setString(3, day);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String good = resultSet.getString("good");
				String bad = resultSet.getString("bad");
				resuMap.put("GOOD", good);
				resuMap.put("BAD", bad);
			}
		} catch (SQLException e) {
			LogUtil.error("mysql select error", e);
		}
		return resuMap;
	}
}
