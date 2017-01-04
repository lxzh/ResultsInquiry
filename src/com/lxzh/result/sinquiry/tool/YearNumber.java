package com.lxzh.result.sinquiry.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class YearNumber {
	private static String FILE_NAME="pets_year_num.xml"; 
	private Context context;
	private int flag;

	public YearNumber(Context context,int flag) {
		this.context = context;
		this.flag=flag;
	}
	
	public String getNumber(String name)
	{
		try {
			if(flag==1)
			{
				FILE_NAME=FILE_NAME.replace("pets", "ncre");
			}
			InputStream input = context.getAssets().open(FILE_NAME);
	    	InputStreamReader inputStreamReader = new InputStreamReader(input,"UTF-8");
	    	BufferedReader in = new BufferedReader(inputStreamReader);
			String content="";
			while (in.ready()) {
				content=in.readLine();
				if(content.contains(name))
				{
					System.out.println(content);
					content=content.substring(content.indexOf("value=\"")+7, content.indexOf("\"><"));
					return content;
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return null;
	}
}
