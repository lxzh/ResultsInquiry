package com.lxzh.result.sinquiry;

import java.util.Timer;
import java.util.TimerTask;

import com.lxzh.result.sinquiry.tool.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity{
	private Button btnBack;
	private TextView tvResult,tvLoading;
	private ImageView ivLoading;
	private int sinquiryFlag;
	private Timer timLoading;
	private String URL_CET="http://cet.99sushe.com/search";
	private String URL_NCRE="http://chaxun.neea.edu.cn/examcenter/query.cn?op=doQueryResults";
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.arg1==1)
			{
				Resources res=getResources(); 
				int resId=res.getIdentifier("loading_0"+msg.what,"drawable",getPackageName()); 
				Log.i("res",Integer.toString(resId));
				ivLoading.setImageResource(resId);
			}else if(msg.arg1==2)
			{
				Bundle bundle=msg.getData();
				String content=bundle.getString("content");
				Toast.makeText(getBaseContext(), content, Toast.LENGTH_LONG).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		btnBack=(Button)findViewById(R.id.btn_back);
		tvResult=(TextView)findViewById(R.id.tv_result);
		tvLoading=(TextView)findViewById(R.id.tv_loading_tip);
		ivLoading=(ImageView)findViewById(R.id.iv_loading);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ResultActivity.this.finish();
			}
		});
		Intent intent=getIntent();
		sinquiryFlag=intent.getIntExtra("select_pid", 0);
		String param=intent.getStringExtra("param");
		Toast.makeText(getBaseContext(), param, Toast.LENGTH_LONG).show();
	}
	
	
	public String getResultFromHtml(String content)
	{
		String result="";
		if(content.contains(getString(R.string.tip_empty_result)))
		{
			int startIndex=content.indexOf(getString(R.string.tip_empty_result));
			int endIndex=content.substring(startIndex).indexOf("</div>")+startIndex;
			result=content.substring(startIndex, endIndex);
			result=result.replaceAll(getString(R.string.ch_comma), ".").replaceAll("<br />", "\n").replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
		}else
		{
			String str=content.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "").replaceAll("	", "");
//			String[] strs=content.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "").split("\"he_xi\">");
//			int length=strs.length;
//			int index=strs[length-1].indexOf("<td>");
//			strs[length-1]=strs[length-1].substring(0, strs[length-1].indexOf("</td>", index));
//			for(int i=1;i<strs.length;i++)
//			{
//				result+=strs[i].replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "")+"\n";
//			}
			str=str.substring(str.indexOf(getString(R.string.html_tag_name)), str.indexOf("<!--  pic begin -->"));
			str=insetStrToStr(str,str.indexOf(getString(R.string.html_tag_sfzh)),"\n");
			str=insetStrToStr(str,str.indexOf(getString(R.string.html_tag_kc)),"\n");
			str=insetStrToStr(str,str.indexOf(getString(R.string.html_tag_zkzh)),"\n");
			str=insetStrToStr(str,str.indexOf(getString(R.string.html_tag_bscj)),"\n");
			str=insetStrToStr(str,str.indexOf(getString(R.string.html_tag_zcj)),"\n");
			result=insetStrToStr(str,str.indexOf(getString(R.string.html_tag_sjcj)),"\n");
		}
		return result;
	}
	
	public String insetStrToStr(String longStr,int index,String shortStr)
	{
		if(longStr.length()<1||index<0||index>longStr.length())
			return longStr;
		String str1=longStr.substring(0, index);
		String str2=longStr.substring(index);
		longStr=str1+shortStr+str2;
		return longStr;
	}
}
