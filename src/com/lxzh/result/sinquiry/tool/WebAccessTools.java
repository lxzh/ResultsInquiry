package com.lxzh.result.sinquiry.tool;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @author lxzh
 * 网站访问工具类，用于Android的网络访问
 */
public class WebAccessTools {
	
	/**
	 * 当前的Context上下文对象
	 */
	private Context context;
	/**
	 * 构造一个网站访问工具类
	 * @param context 记录当前Activity中的Context上下文对象
	 */
	public WebAccessTools(Context context) {
		this.context = context;
	}
	
	/**
	 * 根据给定的url地址访问网络，得到响应内容(这里为GET方式访问)
	 * @param url 指定的url地址
	 * @return web服务器响应的内容，为<code>String</code>类型，当访问失败时，返回为null
	 */
	public String getWebContent(String url) {
		Log.d("getWebContent", "1");
		Log.d("getWebContent", "url="+url);
		//创建一个http请求对象
		HttpGet request = new HttpGet(url);
		//创建HttpParams以用来设置HTTP参数
		HttpParams params=new BasicHttpParams();
		//设置连接超时或响应超时
		//HttpConnectionParams.setConnectionTimeout(params, 3000);
		//HttpConnectionParams.setSoTimeout(params, 5000);
		//创建一个网络访问处理对象
		HttpClient httpClient = new DefaultHttpClient(params);
		try{
			//执行请求参数项
			HttpResponse response = httpClient.execute(request);
			//判断是否请求成功
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				//获得响应信息
				String content = EntityUtils.toString(response.getEntity());
				return content;
			} else {
				//网连接失败，使用Toast显示提示信息
				Toast.makeText(context, "网络访问失败，请检查您机器的联网设备!", Toast.LENGTH_LONG).show();
			}
			
		}catch(Exception e) {
			Log.d("getWebContent", "6");
			System.out.println(e.toString());
		} finally {
			Log.d("getWebContent", "7");
			//释放网络连接资源
			httpClient.getConnectionManager().shutdown();
		}
		Log.d("getWebContent", "8");
		return null;
	}
	
	public String getWebContentPost(String url) {
		Log.d("getWebContent", "1");
		Log.d("getWebContent", "url="+url);
		//创建一个http请求对象
		HttpPost request = new HttpPost(url);
		//创建HttpParams以用来设置HTTP参数
		HttpParams params=new BasicHttpParams();
		//设置连接超时或响应超时
		//HttpConnectionParams.setConnectionTimeout(params, 3000);
		//HttpConnectionParams.setSoTimeout(params, 5000);
		//创建一个网络访问处理对象
		HttpClient httpClient = new DefaultHttpClient(params);
		try{
			//执行请求参数项
			HttpResponse response = httpClient.execute(request);
			//判断是否请求成功
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				//获得响应信息
				String content = EntityUtils.toString(response.getEntity());
				return content;
			} else {
				//网连接失败，使用Toast显示提示信息
				Toast.makeText(context, "网络访问失败，请检查您机器的联网设备!", Toast.LENGTH_LONG).show();
			}
			
		}catch(Exception e) {
			Log.d("getWebContent", "6");
			System.out.println(e.toString());
		} finally {
			Log.d("getWebContent", "7");
			//释放网络连接资源
			httpClient.getConnectionManager().shutdown();
		}
		Log.d("getWebContent", "8");
		return null;
	}
}