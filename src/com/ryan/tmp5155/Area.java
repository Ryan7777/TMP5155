package com.ryan.tmp5155;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Area extends Activity {
       Button btnBack;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
              setContentView(R.layout.area);
              
              btnBack=(Button)findViewById(R.id.area_btnBack);
              
              btnBack.setOnClickListener(btnBack_listener);
       }
        
    	/* Back */
    	private Button.OnClickListener btnBack_listener = new Button.OnClickListener(){
    		public void onClick(View v){
    			Area. this.finish();
    		}};
}
