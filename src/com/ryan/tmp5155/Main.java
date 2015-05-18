package com.ryan.tmp5155;

import java.text.SimpleDateFormat;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private Handler handler = new Handler(); // 時間
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm"); // 時間
	private BluetoothAdapter mBluetoothAdapter  = BluetoothAdapter.getDefaultAdapter(); //  BlueTooth
//	IntentFilter intentFilter = new IntentFilter();	// 監聽廣播	

	TextView txt_nowtime, txt_ver;
	Button btn_area, btn_console, btn_other, btn_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		PackageManager msgVer = this.getPackageManager();

		btn_area=(Button)findViewById(R.id.btn_Area);
		btn_console=(Button)findViewById(R.id.btn_Console);
		btn_other=(Button)findViewById(R.id.btn_Other);
		btn_exit=(Button)findViewById(R.id.btn_Exit);
		txt_nowtime=(TextView)findViewById(R.id.txt_NowTime);
		txt_ver=(TextView)findViewById(R.id.txtVer);

		btn_area.setOnClickListener(btnArea_listener);
		btn_console.setOnClickListener(btnConsole_listener);
		btn_other.setOnClickListener(btnOther_listener);
		btn_exit.setOnClickListener(btnExit_listener);

		handler.post(updateTimerThread);	// 執行 updateTimerThread，顯示目前時間
		getWirelessStatus();

		try { PackageInfo info = msgVer.getPackageInfo(this.getPackageName(), 0);
			txt_ver.setText(getString(R.string.strVer)+" "+info.versionName);	// 版本
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			test1
		}
	}

	// 顯示目前時間
	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			// 利用 System.currentTimeMillis() 讀取系統時間
			Long nowTime = System.currentTimeMillis();
			txt_nowtime.setText(getString(R.string.strNowTime)+format.format(nowTime));
			// 調用 Handler 的 postDelayed 方法
			// 將 要執行的線程對象 updateTimerThread 放入隊列中, 當等待時間 (5000毫秒)結束後, 執行線程對象 updateTimerThread
			handler.postDelayed(this, 5000);
		}
	};

	// 取得網路狀態
	private void getWirelessStatus(){
		ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();	// 偵測 3G 是否連線
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();	// 偵測 WiFi 是否連線
		if(is3g == true)
			Toast.makeText(this, "3g上網中", Toast.LENGTH_SHORT).show();
		else if(isWifi == true)
			Toast.makeText(this, "WiFi上網中", Toast.LENGTH_SHORT).show();
		else if (mBluetoothAdapter.isEnabled())
			Toast.makeText(this, "藍芽已開啟，請配對藍芽", Toast.LENGTH_SHORT).show();
		else{
			Toast.makeText(this, "無網路連線，即將開啟藍芽", Toast.LENGTH_SHORT).show();
			mBluetoothAdapter.enable();	//將 BlueTooth 開啟
		}
	}

	/* 切換至區域畫面 */
	private Button.OnClickListener btnArea_listener = new Button.OnClickListener(){
		public void onClick(View v){
			Intent i = new Intent();
			i.setClass(Main. this, Area.class );
			startActivity(i);
//            Main. this.finish();
		}};

	/* Console */
	private Button.OnClickListener btnConsole_listener = new Button.OnClickListener(){
		public void onClick(View v){
			Intent i = new Intent();
			i.setClass(Main. this, Setting.class );
			startActivity(i);
		}};

	/* Other */
	private Button.OnClickListener btnOther_listener = new Button.OnClickListener(){
		public void onClick(View v){
//			WifiManager wifiMsg = (WifiManager) getSystemService(Context.WIFI_SERVICE); // WiFi
//			if (wifiMsg.isWifiEnabled()) //判斷目前WiFi狀態
//				wifiMsg.setWifiEnabled(false);   //把WiFi關閉
//			else
//				wifiMsg.setWifiEnabled(true); //把WiFi開啟
			handler.removeCallbacks(updateTimerThread);
		}};

	/* Exit */
	private Button.OnClickListener btnExit_listener = new Button.OnClickListener(){
		public void onClick(View v){
			// 調用 Handler 的 removeCallbacks 方法, 刪除隊列中未執行的線程對象
			handler.removeCallbacks(updateTimerThread);
			android.os.Process.killProcess(android.os.Process.myPid());
		}};

	// 監聽 BlueTooth/WiFi 狀態改變事件
//	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//	    @Override
//	    public void onReceive(Context context, Intent intent) {
//	        final String action = intent.getAction();
//
//	        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))	// 收到 BlueTooth 狀態改變
//	        {
//	        	if (mBluetoothAdapter.isEnabled()){
//	        		Toast.makeText(context, "BlueTooth 已開啟", Toast.LENGTH_SHORT).show();
//	        	}
//	        	else{
//	        		Toast.makeText(context, "BlueTooth 已關閉", Toast.LENGTH_SHORT).show();
//	        	}
//	        }
////	        if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))	// 收到 WiFi 狀態改變
////	        {   
////	        	 Toast.makeText(context, "請確認網路狀態", Toast.LENGTH_SHORT).show();
////	        }
//	    }
//	};

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);	// BlueTooth 狀態改變事件
////        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);	// WiFi 狀態改變事件
////        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);	// WiFi 狀態改變事件
//        registerReceiver(mReceiver, intentFilter);
//    }

}
