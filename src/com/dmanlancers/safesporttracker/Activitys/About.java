package com.dmanlancers.safesporttracker.Activitys;



import com.dmanlancers.safesporttracker.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class About extends Activity {
    WebView mWebView;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
       RelativeLayout mail=(RelativeLayout)findViewById(R.id.email);
       mail.setOnClickListener(new View.OnClickListener() {
        
        public void onClick(View view) {
        	
        	Intent intent = new Intent(Intent.ACTION_SEND);
        	intent.setType("plain/text");
        	intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "dmanlancers@gmail.com" });
        	intent.putExtra(Intent.EXTRA_SUBJECT, "");
        	intent.putExtra(Intent.EXTRA_TEXT, "");
        	startActivity(Intent.createChooser(intent, ""));
            
    
              
        
        }
            
        
    });

    RelativeLayout webpage=(RelativeLayout)findViewById(R.id.dmanlancersURL);
    webpage.setOnClickListener(new View.OnClickListener() {
       public void onClick(View view) {
    	   Uri uri = Uri.parse("https://www.facebook.com/pages/Dmanlancers/349379121828160");
      	 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      	 startActivity(intent);
          
           
   
             
       
       }
           
       
   });
    
    
    RelativeLayout web=(RelativeLayout)findViewById(R.id.web);
    web.setOnClickListener(new View.OnClickListener() {
       public void onClick(View view) {
    	   Uri uri = Uri.parse("https://developers.facebook.com/apps/495333743830743");
      	 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      	 startActivity(intent);
}
    });
}
}