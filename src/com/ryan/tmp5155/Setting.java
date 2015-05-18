package com.ryan.tmp5155;

import com.ryan.btntest.view.CheckSwitchButton;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setting extends Activity {

	//	WifiManager wifiMsg1 = (WifiManager) getSystemService(Context.WIFI_SERVICE); // WiFi
	BluetoothAdapter mBluetoothAdapter  = BluetoothAdapter.getDefaultAdapter(); //  BlueTooth

	private Handler wifihandler = new Handler(); // update WiFi information 
	CheckSwitchButton csbtn_wifi, csbtn_bt, csbtn_lock;
	private LinearLayout wifi_layout, bt_layout;
	TextView tv_WiFi01, tv_WiFi02, tv_WiFi03, tv_WiFi04;
	Button btn_BTsetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		findView();

		wifi_layout.setVisibility(View.GONE);
		bt_layout.setVisibility(View.GONE);

		getWirelessStatus();
//          wifihandler.post(updateWifiInfoThread);	// 執行 updateWifiInfoThread，更新 WiFi 資訊

	}


	private void findView(){
		csbtn_wifi = (CheckSwitchButton)findViewById(R.id.csbtn_wifi);
		csbtn_bt = (CheckSwitchButton)findViewById(R.id.csbtn_bt);
		csbtn_lock = (CheckSwitchButton)findViewById(R.id.csbtn_lock);
		wifi_layout = (LinearLayout) findViewById(R.id.wlayout);
		bt_layout = (LinearLayout) findViewById(R.id.blayout);
		tv_WiFi01 = (TextView) findViewById(R.id.tv_w01);
		tv_WiFi02 = (TextView) findViewById(R.id.tv_w02);
		tv_WiFi03 = (TextView) findViewById(R.id.tv_w03);
		tv_WiFi04 = (TextView) findViewById(R.id.tv_w04);
		btn_BTsetting=(Button)findViewById(R.id.btn_btsetting);

		csbtn_wifi.setOnCheckedChangeListener(csbtnWiFi_listener);
		csbtn_bt.setOnCheckedChangeListener(csbtnBlueTooth_listener);
		csbtn_lock.setOnCheckedChangeListener(csbtnLock_listener);
		btn_BTsetting.setOnClickListener(btnBTset_listener);
	}

	private void getWirelessStatus(){
		ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE); // WiFi
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();	// 偵測 WiFi 是否連線
		if(isWifi == true && mBluetoothAdapter.isEnabled()){	// WiFi & BlueTooth 皆開啟
			csbtn_wifi.setChecked(true);
			csbtn_bt.setChecked(true);
		}
		else if(isWifi == true){	// 只有 WiFi 開啟
			csbtn_wifi.setChecked(true);
			csbtn_bt.setChecked(false);
		}
		else if (mBluetoothAdapter.isEnabled()){	// 只有 BlueTooth 開啟
			csbtn_wifi.setChecked(false);
			csbtn_bt.setChecked(true);
		}
		else{	// WiFi & BlueTooth 皆關閉
			csbtn_wifi.setChecked(false);
			csbtn_bt.setChecked(false);
		}
	}

	private void getWiFiStatus(){

		ConnectivityManager CM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		WifiManager wifiManager1 = (WifiManager) getSystemService(WIFI_SERVICE);
		if(wifiManager1 != null){
			WifiInfo wifiInfo = wifiManager1.getConnectionInfo();
			tv_WiFi01.setText(info.getTypeName().toString());
			tv_WiFi02.setText(info.getState().toString());
			tv_WiFi03.setText(wifiInfo.getSSID());
			tv_WiFi04.setText(Formatter.formatIpAddress(wifiInfo.getIpAddress()));	// formatIpAddress(int) 方法即將棄用
		}
		else{
			tv_WiFi01.setText(null);
			tv_WiFi02.setText(null);
			tv_WiFi03.setText(null);
			tv_WiFi04.setText(null);
		}

	}

	/* WiFi */
	private CheckSwitchButton.OnCheckedChangeListener csbtnWiFi_listener = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
//			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			if(isChecked){
				wifiManager.setWifiEnabled(true);
				Toast.makeText(Setting.this, "WiFi 已開啟", Toast.LENGTH_SHORT).show();
				wifi_layout.setVisibility(View.VISIBLE);
				getWiFiStatus();
				wifihandler.post(updateWifiInfoThread);	// 執行 updateWifiInfoThread，更新 WiFi 資訊
			}else{
				if(wifihandler != null)
					wifihandler.removeCallbacks(updateWifiInfoThread);
				wifiManager.setWifiEnabled(false);
				Toast.makeText(Setting.this, "WiFi 已關閉", Toast.LENGTH_SHORT).show();
				wifi_layout.setVisibility(View.GONE);
			}

		}};

	/* BlueTooth */
	private CheckSwitchButton.OnCheckedChangeListener csbtnBlueTooth_listener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			if(isChecked){
				mBluetoothAdapter.enable();
				Toast.makeText(Setting.this, "BlueTooth 已開啟", Toast.LENGTH_SHORT).show();
				bt_layout.setVisibility(View.VISIBLE);
			}else{
				mBluetoothAdapter.disable();
				Toast.makeText(Setting.this, "BlueTooth 已關閉", Toast.LENGTH_SHORT).show();
				bt_layout.setVisibility(View.GONE);
			}
		}};

	/* Lock */
	private CheckSwitchButton.OnCheckedChangeListener csbtnLock_listener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			if(isChecked){
				//選中  
				csbtn_wifi.setEnabled(false);
				csbtn_bt.setEnabled(false);
			}else{
				//未選中  
				csbtn_wifi.setEnabled(true);
				csbtn_bt.setEnabled(true);
			}
		}};

	/* BlueTooth Setting */
	private Button.OnClickListener btnBTset_listener = new Button.OnClickListener(){
		public void onClick(View v){
			Intent settintIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			startActivity(settintIntent);
		}};

	// update WiFi information
	private Runnable updateWifiInfoThread = new Runnable() {
		public void run() {
			getWiFiStatus();
			wifihandler.postDelayed(updateWifiInfoThread, 1000);
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (wifihandler != null) {
			wifihandler.removeCallbacks(updateWifiInfoThread);
		}
	}
}
