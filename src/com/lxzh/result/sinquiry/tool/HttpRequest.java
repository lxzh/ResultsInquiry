package com.lxzh.result.sinquiry.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequest {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "gzip, deflate");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection
					.setRequestProperty("Cookie",
							"usertype=xs; JSESSIONID=0000XhkdI6viIRVX9vhpATqBtvl:167iomm50;");
			connection
					.setRequestProperty(
							"hub_service",
							"cjrgeLM+5k/n8+9XXgIw+dpmaj1+eZt1ICOi5fNIdzUKXMJa4v7PTY+P1ZHkcBdT/tsX95eX7TLmlw==");
			connection
					.setRequestProperty("user-agent",
							"Mozilla/5.0 (Windows NT 6.1; rv:19.0) Gecko/20100101 Firefox/19.0");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, int id) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = null;
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(5000);
			if (id == 0) {
				conn.setRequestProperty("Host", "cet.99sushe.com");
				conn.setRequestProperty(
						"Cookies",
						"CNZZDATA30023677=cnzz_eid%3D537858816-1376719409-http%253A%252F%252Fcet.99sushe.com%26ntime%3D1377053402%26cnzz_a%3D0%26retime%3D1377053403035%26sin%3D%26ltime%3D1377053403035%26rtime%3D1");
				conn.setRequestProperty("Referer", "http://cet.99sushe.com/");
			} else {
				conn.setRequestProperty("Host", "chaxun.neea.edu.cn");
			}
			conn.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language",
					"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.getOutputStream().write(param.getBytes());
			out = new PrintWriter(conn.getOutputStream());
			out.flush();
			if (id == 0) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gbk"));
				String line;
//				result=new String("".getBytes(),"gbk");
				while ((line = in.readLine()) != null) {
					line = new String(line.getBytes(), "utf-8");
					result += line;
				}
//				result=new String(gbToUtf8(result));
			}else
			{
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String gbToUtf8(String source)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < source.length(); i++) {
			String s = source.substring(i, i + 1);
			if (s.charAt(0) > 0x80) {
				byte[] bytes = s.getBytes("Unicode");
				String binaryStr = "";
				for (int j = 2; j < bytes.length; j += 2) {
					// the first byte
					String hexStr = getHexString(bytes[j + 1]);
					String binStr = getBinaryString(Integer.valueOf(hexStr, 16));
					binaryStr += binStr;

					// the second byte
					hexStr = getHexString(bytes[j]);
					binStr = getBinaryString(Integer.valueOf(hexStr, 16));

					binaryStr += binStr;
				}

				String s1 = "1110" + binaryStr.substring(0, 4);
				String s2 = "10" + binaryStr.substring(4, 10);
				String s3 = "10" + binaryStr.substring(10, 16);

				byte[] bs = new byte[3];
				bs[0] = Integer.valueOf(s1, 2).byteValue();
				bs[1] = Integer.valueOf(s2, 2).byteValue();
				bs[2] = Integer.valueOf(s3, 2).byteValue();

				String ss = new String(bs, "UTF-8");
				sb.append(ss);
			}
		}
		return sb.toString();
	}

	public static String getHexString(byte b) {
		String hexStr = Integer.toHexString(b);
		int m = hexStr.length();
		if (m < 2) {
			hexStr = "0" + hexStr;
		} else {
			hexStr = hexStr.substring(m - 2);
		}
		return hexStr;
	}

	private static String getBinaryString(int i) {
		String binaryStr = Integer.toBinaryString(i);
		int length = binaryStr.length();
		for (int k = 0; k < 8 - length; k++) {
			binaryStr = "0" + binaryStr;
		}
		return binaryStr;
	}
}