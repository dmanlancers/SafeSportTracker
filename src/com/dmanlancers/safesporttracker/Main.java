package com.dmanlancers.safesporttracker;




import java.lang.ref.WeakReference;

import com.adsdk.sdk.banner.BannerAdView;
import com.appbrain.AppBrain;
import com.dmanlancers.safesporttracker.R;
import com.dmanlancers.safesporttracker.Activitys.About;
import com.dmanlancers.safesporttracker.Activitys.Bike;
import com.pad.android.iappad.AdController;
import com.dmanlancers.safesporttracker.Activitys.Mountaineering;
import com.dmanlancers.safesporttracker.Activitys.Run;
import com.dmanlancers.safesporttracker.Activitys.SnowBoard;
import com.dmanlancers.safesporttracker.Activitys.Walk;
import com.dmanlancers.safesporttracker.GpsServices.DefGps;
import com.google.ads.Ad;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.mobclix.android.sdk.MobclixAdView;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.mopub.mobileads.MoPubView;
import com.paDGfonLEQ.CZVyKcbSdu130599.Airpush;
import com.pad.android.iappad.AdController;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pad.android.iappad.AdController;


public class Main extends Activity {
	
	//private AdView adView;
	Airpush airpush;
	
	AdController myController;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.main);
		 airpush = new Airpush(this);
		 airpush.startDialogAd();
			//airpush.startLandingPageAd();
			airpush.startPushNotification(false);
			//airpush.startDialogAd();
			airpush.startIconAd();		
		 myController = new AdController(this, "904972433");
	    
	   //myController.loadAd();
	   
		
	    
	    String fontPath = "fonts/BillboardCo.ttf";

		// text view label
		TextView txtGhost = (TextView) findViewById(R.id.txt1);
		TextView txtGhost1 = (TextView) findViewById(R.id.txt4);
		TextView txtGhost2= (TextView) findViewById(R.id.txt3);
		TextView txtGhost3= (TextView) findViewById(R.id.txt2);
		TextView txtGhost4= (TextView) findViewById(R.id.txt5);
		TextView txtGhost5= (TextView) findViewById(R.id.titleText);
		TextView txtGhost6= (TextView) findViewById(R.id.choose);

		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

		// Applying font
		txtGhost.setTypeface(tf);
		txtGhost1.setTypeface(tf);
		txtGhost2.setTypeface(tf);
		txtGhost3.setTypeface(tf);
		txtGhost4.setTypeface(tf);
		txtGhost5.setTypeface(tf);
		txtGhost6.setTypeface(tf);

		final Vibrator vibe = (Vibrator) Main.this.getSystemService(Context.VIBRATOR_SERVICE);


		RelativeLayout  imgRun = (RelativeLayout) findViewById(R.id.col3);
		imgRun.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);

				startActivity(new Intent(Main.this,Run.class));
				


			}
		});
		RelativeLayout  imgMountain = (RelativeLayout) findViewById(R.id.col2);
		imgMountain.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);

				startActivity(new Intent(Main.this,Mountaineering.class));


			}
		});

		RelativeLayout  imgBike = (RelativeLayout) findViewById(R.id.col1);
		imgBike.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);
				startActivity(new Intent(Main.this, Bike.class));


			}
		});
		RelativeLayout  imgSnow = (RelativeLayout) findViewById(R.id.col5);
		imgSnow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);

				startActivity(new Intent(Main.this, SnowBoard.class));


			}
		});

		RelativeLayout  imgWalk = (RelativeLayout) findViewById(R.id.col4);
		imgWalk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);


				startActivity(new Intent(Main.this, Walk.class));
			}

		});

		Button btnMediaP = (Button) findViewById(R.id.btnMediaP);
		btnMediaP.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				vibe.vibrate(100);
				Intent myActivity2 = 
						new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
				startActivity(myActivity2);
				
				myController.loadAd();


			}
		});



		 
		

		Button btnWifi = (Button) findViewById(R.id.btnWifi);
		btnWifi.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			
				vibe.vibrate(100);
				startActivity(new Intent(Main.this, About.class));
				//AdController myController = new AdController(this, "904972433");
				myController.loadAd();
			}
		});
		Button btnDef = (Button) findViewById(R.id.btnDef);
		btnDef.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				vibe.vibrate(100);
				startActivity(new Intent(Main.this, DefGps.class));
				myController.loadAd();

			}
		});
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
//Airpush airpush=new Airpush (this);

		
		//airpush.startIconAd();//start icon.
		//airpush.startDialogAd();
		//start random smart wall ad.
		//airpush.startLandingPageAd();
		//airpush.startPushNotification(false);
		//airpush.startSmartWallAd();

	   
	    // Normal case behavior follows
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {			
			//use smart wall on app exit. 
			airpush.startIconAd();	
			airpush.startDialogAd();
			//airpush.startLandingPageAd();
			
		}
		return super.onKeyDown(keyCode, event);
	}
			
			
	
	
	public void criarAtalho(){
	    Intent atalhointent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	    atalhointent.putExtra("duplicate", false);
	    atalhointent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Safe Sport Tracker");
	    Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.iconapp);
	    atalhointent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	    atalhointent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , Main.class));
	    sendBroadcast(atalhointent);
		
	}

	
	//airpush.startSmartWallAd();


}













