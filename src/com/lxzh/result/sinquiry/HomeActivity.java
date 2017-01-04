package com.lxzh.result.sinquiry;

import com.lxzh.result.sinquiry.tool.WebAccessTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		try {
			new Thread(){
				@Override
				public void run() {
					String str=new WebAccessTools(HomeActivity.this).getWebContentPost(getString(R.string.url_count));
					super.run();
				}
			};
//			Toast.makeText(getBaseContext(), "count="+str, 3000).show();
		} catch (Exception e) {

		}
	}
	public void onBtnClick(View v)
	{
		Intent intent=new Intent(this,SinquiryActivity.class);
		if(v.getId()==R.id.btnpets)
		{
			intent.putExtra("select_pid", 0);
		}else
		{
			intent.putExtra("select_pid", 1);
		}
		startActivity(intent);
	}
}
