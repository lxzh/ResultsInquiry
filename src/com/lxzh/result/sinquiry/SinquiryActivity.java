package com.lxzh.result.sinquiry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.lxzh.result.sinquiry.tool.HttpRequest;
import com.lxzh.result.sinquiry.tool.YearNumber;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SinquiryActivity extends Activity {

	private Button btnBack, btnSinquiry, btnReset;
	private Spinner spiListTime, spiListProvince, spiLevel;
	private EditText etNumber, etUserId, etName;
	private TextView tvResult;
	private ImageView ivLoading;
	private String[] strListTime, strListProvince, strListLevel;
	private ArrayAdapter<String> adapterTime, adapterProvince, adapterLevel;
	private String selectedTime, selectedProvince, selectedLevel;
	private int sinquiryFlag;
	private String URL_CET = "http://www.lxzh123.com/cet";
	private String URL_NCRE = "http://www.lxzh123.com/ncre";
	private Timer timLoading;
	private Animation animLoading;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				// try {
				// Field field = R.drawable.class.getField("icon");
				// int i = field.getInt(new R.drawable());
				// Log.d("icon", i + "");
				// } catch (Exception e) {
				// Log.e("icon", e.toString());
				// }
				Resources res = getResources();
				int resId = res.getIdentifier("loading_0" + msg.what,
						"drawable", getPackageName());
				ivLoading.setImageResource(resId);
			} else if (msg.arg1 == 2) {
				if (msg.what == 0) {
					URL_CET = getString(R.string.url_cet);
					URL_NCRE = getString(R.string.url_ncre);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sinquiryFlag = getIntent().getIntExtra("select_pid", 0);
		Message message = new Message();
		message.arg1 = 2;
		message.what = 0;
		handler.sendMessage(message);
		findId();
		// if (sinquiryFlag == 1) {
		// spiListTime.setSelection(1);
		// spiListProvince.setSelection(16);
		// spiLevel.setSelection(28);
		// }
		animLoading = AnimationUtils.loadAnimation(this, R.anim.anim_loading);
	}

	private void findId() {
		if (sinquiryFlag == 0) {
			setContentView(R.layout.cets);
		} else {
			setContentView(R.layout.ncre);
			spiListTime = (Spinner) findViewById(R.id.sp_select_time);
			spiListProvince = (Spinner) findViewById(R.id.sp_select_province);
			spiLevel = (Spinner) findViewById(R.id.sp_select_level);
			etNumber = (EditText) findViewById(R.id.et_number);
			etUserId = (EditText) findViewById(R.id.et_userid);
			etName = (EditText) findViewById(R.id.et_name);
			initSpinner();
		}
		ivLoading = (ImageView) findViewById(R.id.iv_loading);
		etNumber = (EditText) findViewById(R.id.et_number);
		etName = (EditText) findViewById(R.id.et_name);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnSinquiry = (Button) findViewById(R.id.btn_sinquiry);
		btnReset = (Button) findViewById(R.id.btn_reset);
		tvResult = (TextView) findViewById(R.id.tv_result);
		initButton();
	}

	private void initSpinner() {
		Date now = new Date();
		int year = now.getYear() + 1900;
		int month = now.getMonth() + 1;
		if (sinquiryFlag == 0) {
			int tempMonth = 9;
			if (month < 3) {
				year -= 1;
			} else if (month >= 3 && month < 9) {
				tempMonth = 3;
			}
			int count = 0;
			strListTime = new String[25];
			while (count < 25) {
				strListTime[count] = year + getString(R.string.year)
						+ tempMonth + getString(R.string.month);
				if (tempMonth == 3) {
					tempMonth = 9;
					year--;
				} else {
					tempMonth = 3;
				}
				count++;
			}
			strListProvince = getResources().getStringArray(R.array.pets_bksf);
			strListLevel = getResources().getStringArray(R.array.pets_bkjb);

		} else {
			int tempMonth = 9;
			if (month < 3) {
				year -= 1;
			} else if (month >= 3 && month < 9) {
				tempMonth = 3;
			}
			int count = 0;
			strListTime = new String[2];
			while (count < 2) {
				strListTime[count] = year + getString(R.string.year)
						+ tempMonth + getString(R.string.month);
				if (tempMonth == 3) {
					tempMonth = 9;
					year--;
				} else {
					tempMonth = 3;
				}
				count++;
			}
			strListProvince = getResources().getStringArray(R.array.ncre_bksf);
			strListLevel = getResources().getStringArray(R.array.ncre_bkjb);
		}

		adapterTime = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strListTime);
		adapterTime
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterProvince = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strListProvince);
		adapterProvince
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterLevel = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strListLevel);
		adapterLevel
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spiListTime.setAdapter(adapterTime);
		spiListProvince.setAdapter(adapterProvince);
		spiLevel.setAdapter(adapterLevel);

		spiListTime.setPromptId(R.string.please_select_time);
		spiListProvince.setPromptId(R.string.please_select_province);
		spiLevel.setPromptId(R.string.please_select_level);

		spiListTime.setOnItemSelectedListener(new SpinnerSelectedListener(1));
		spiListProvince
				.setOnItemSelectedListener(new SpinnerSelectedListener(2));
		spiLevel.setOnItemSelectedListener(new SpinnerSelectedListener(3));
	}

	private void initButton() {
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnSinquiry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isReady = false;
				if (sinquiryFlag == 0) {
					if (checkInput(1)) {
						if (checkInput(3)) {
							isReady = true;
						}
					}
				} else {
					if (checkInput(1)) {
						if (checkInput(2)) {
							if (checkInput(3)) {
								isReady = true;
							}
						}
					}
				}
				if (isReady) {
					String param = "";
					if (sinquiryFlag == 0) {
						param = "id=" + etNumber.getText().toString();
						try {
							param += "&name="
									+ URLEncoder.encode(etName.getText()
											.toString(), "GBK");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							System.out.println(e.toString());
						}
					} else {
						YearNumber yearNumber = new YearNumber(
								getBaseContext(), sinquiryFlag);
						param = "state=&opt=queryC&ksnf="
								+ yearNumber.getNumber(selectedTime);
						param += "&sf="
								+ selectedProvince.substring(0,
										selectedProvince.indexOf("-"));
						param += "&bkjb=" + selectedLevel.substring(0, 2);
						param += "&zkzh=" + etNumber.getText().toString();
						try {
							param += "&name="
									+ URLEncoder.encode(etName.getText()
											.toString(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							System.out.println(e.toString());
						}
						param += "&sfzh=" + etUserId.getText().toString();
						param += "&rand=&ksxm=300";
					}
					sinquiry(param);
				}
			}
		});
		btnReset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sinquiryFlag == 1) {
					spiListTime.setSelection(0);
					spiListProvince.setSelection(0);
					spiLevel.setSelection(0);
					etUserId.setText("");
				}
				etNumber.setText("");
				etName.setText("");
				tvResult.setText("");
			}
		});
	}

	private void sinquiry(String param) {
		// timLoading = new Timer();
		// timLoading.schedule(new LoadTimerTask(), 150, 200);
		String URL = (sinquiryFlag == 0 ? URL_CET : URL_NCRE);
		new LoadAsyncTask(URL, param).execute();
	}

	class LoadTimerTask extends TimerTask {
		private int count = 1;

		@Override
		public void run() {
			count++;
			if (count > 6)
				count = 1;
			Message message = new Message();
			message.arg1 = 1;
			message.what = count;
			handler.sendMessage(message);
		}
	}

	class LoadAsyncTask extends AsyncTask {

		private String url;
		private String param;

		public LoadAsyncTask(String url, String param) {
			super();
			this.url = url;
			this.param = param;
		}

		@Override
		protected Object doInBackground(Object... params) {
			String content = HttpRequest.sendPost(url, param, sinquiryFlag);
			return content;
		}

		@Override
		protected void onPreExecute() {
			ivLoading.setVisibility(View.VISIBLE);
			ivLoading.startAnimation(animLoading);
			super.onPreExecute();
		}

		protected void onPostExecute(Object content) {
			if (content != null) {
				String result = content.toString();
				String[] results = null;
				if (sinquiryFlag == 0) {
					if (result.contains("null")) {
						result = result.replace("null", "");
					}
					System.out.println("content=" + result);
					results = result.split(",");
					if (results.length == 8) {
						StringBuffer resultBuffer = new StringBuffer();
						resultBuffer
								.append(getString(R.string.result_sinquiry_title)
										+ "\n");
						resultBuffer.append(getString(R.string.result_school)
								+ results[6] + "\n");
						String type = getString(getResources().getIdentifier(
								"result_cet" + results[0], "string",
								getPackageName()));
						resultBuffer.append(getString(R.string.result_type)
								+ type + "\n\n");
						resultBuffer
								.append(getString(R.string.result_total_result)
										+ results[5] + "\n");
						resultBuffer.append(getString(R.string.result_tingli)
								+ results[1] + "\n");
						resultBuffer.append(getString(R.string.result_yuedu)
								+ results[2] + "\n");
						resultBuffer.append(getString(R.string.result_zonghe)
								+ results[3] + "\n");
						resultBuffer.append(getString(R.string.result_xiezuo)
								+ results[4]);
						tvResult.setText(resultBuffer.toString());
						tvResult.setTextSize(20f);
						try {
							if (Integer.parseInt(results[5]) > 424) {
								Toast.makeText(getBaseContext(),
										getString(R.string.result_past) + type,
										3000).show();
							} else {
								Toast.makeText(getBaseContext(),
										getString(R.string.result_failure),
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
						}
						// Toast.makeText(getBaseContext(), results[7],
						// Toast.LENGTH_LONG).show();
					} else {
						tvResult.setText(getString(R.string.tip_getresult_failure));
					}
				} else {
					tvResult.setText(getResultFromHtml(result));
				}
			} else {
				tvResult.setText(getString(R.string.tip_getresult_failure));
			}
			// timLoading.cancel();
			ivLoading.clearAnimation();
			ivLoading.setVisibility(View.INVISIBLE);
			tvResult.setVisibility(View.VISIBLE);
		}
	}

	public String getResultFromHtml(String content) {
		String result = "";
		// String[] results = new String[1];
		if (content.contains(getString(R.string.tip_empty_result))) {
			int startIndex = content
					.indexOf(getString(R.string.tip_empty_result));
			int endIndex = content.substring(startIndex).indexOf("</div>")
					+ startIndex;
			result = content.substring(startIndex, endIndex);
			result = result.replaceAll(getString(R.string.ch_comma), ".")
					.replaceAll("<br />", "\n")
					.replaceAll("\\&[a-zA-Z]{1,10};", "")
					.replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
		} else {
			String str = content.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
					.replaceAll("</[a-zA-Z]+[1-9]?>", "").replaceAll("	", "");
			// String[] strs=content.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>",
			// "").replaceAll("</[a-zA-Z]+[1-9]?>", "").split("\"he_xi\">");
			// int length=strs.length;
			// int index=strs[length-1].indexOf("<td>");
			// strs[length-1]=strs[length-1].substring(0,
			// strs[length-1].indexOf("</td>", index));
			// for(int i=1;i<strs.length;i++)
			// {
			// result+=strs[i].replaceAll("<[a-zA-Z]+[1-9]?[^><]*>",
			// "").replaceAll("</[a-zA-Z]+[1-9]?>", "")+"\n";
			// }
			str = str.substring(str.indexOf(getString(R.string.html_tag_name)),
					str.indexOf("<!--  pic begin -->"));
			str = insetStrToStr(str,
					str.indexOf(getString(R.string.html_tag_sfzh)), "\n");
			str = insetStrToStr(str,
					str.indexOf(getString(R.string.html_tag_kc)), "\n");
			str = insetStrToStr(str,
					str.indexOf(getString(R.string.html_tag_zkzh)), "\n");
			str = insetStrToStr(str,
					str.indexOf(getString(R.string.html_tag_bscj)), "\n");
			str = insetStrToStr(str,
					str.indexOf(getString(R.string.html_tag_zcj)), "\n");
			result = insetStrToStr(str,
					str.indexOf(getString(R.string.html_tag_sjcj)), "\n");
		}
		System.out.print(result);
		// results[0]=result;
		return result;
	}

	public String insetStrToStr(String longStr, int index, String shortStr) {
		if (longStr.length() < 1 || index < 0 || index > longStr.length())
			return longStr;
		String str1 = longStr.substring(0, index);
		String str2 = longStr.substring(index);
		longStr = str1 + shortStr + str2;
		return longStr;
	}

	public boolean checkInput(int flag) {
		boolean result = false;
		String text = "";
		String toastStr = "";
		if (flag == 1) {
			text = etNumber.getText().toString().trim();
			if (sinquiryFlag == 0) {
				if (text.length() > 15) {
					toastStr = getString(R.string.tip_invalid_zkzh) + "\n"
							+ getString(R.string.tip_siliuji_zkzh);
				} else if (text.length() < 15) {
					toastStr = getString(R.string.tip_zkzh_length_not_enough)
							+ "\n" + getString(R.string.tip_siliuji_zkzh);
				} else {
					result = true;
				}
			} else {
				if (text.length() < 15) {
					toastStr = getString(R.string.tip_zkzh_length_not_enough)
							+ "\n" + getString(R.string.tip_jisuanji_zkzh);
				} else {
					result = true;
				}
			}
		} else if (flag == 2) {
			text = etUserId.getText().toString().trim();
			if (text.length() < 18) {
				toastStr = getString(R.string.tip_sfzh_length_not_enough);
			} else {
				result = true;
			}
		} else {
			text = etName.getText().toString().trim();
			if (text.length() > 0) {
				result = true;
			}
		}
		if (!result) {
			Toast.makeText(getBaseContext(), toastStr, Toast.LENGTH_LONG)
					.show();
		}
		return result;
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public int flag;

		public SpinnerSelectedListener(int flag) {
			this.flag = flag;
		}

		public void onItemSelected(AdapterView<?> arg0, View view,
				int position, long arg3) {
			if (flag == 1) {
				selectedTime = strListTime[position];
			} else if (flag == 2) {
				selectedProvince = strListProvince[position];
			} else {
				selectedLevel = strListLevel[position];
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
