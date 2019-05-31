package com.dmanlancers.safesporttracker.facebook;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.dmanlancers.safesporttracker.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class FinalWalkPost extends Activity  {




	public static String kmlFile ;
	public static String currentFileName ;
	public static Float dist;
	public static Float Dist;
	public static double Speed;
	public static double MaxSp;
	public static double Avg;
	public static double Avgsp;
	public static double Altitude;
	public static double Alt;
	public static double THours;
	public static double TH;
	public static double TMinutes;
	public static double TM;
	public static double TSeconds;
	public static double TS;



	private static final String APP_ID = "495333743830743";//269876589726953
	private static final String[] PERMISSIONS = new String[] {"publish_stream"};

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";

	private Facebook facebook;
	private String messageToPost;

	public boolean saveCredentials(Facebook facebook) {
		Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook);
		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);



		//loginAndPostToWall();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		String data = sdf.format(new Date());
		String time = sdf1.format(new Date());
		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		String terminar_caminhada = getString(R.string.terminar_caminhada);
		String dia = getString(R.string.dia);
		String percurso = getString(R.string.percurso);
		String distancia_percorrida = getString(R.string.distancia_percorrida);
		String tempo_total = getString(R.string.tempo_total);
		String velocidade_maxima = getString(R.string.velocidade_m_x_);		
		String velocidade_media = getString(R.string.velocidade_m_dia_);
		String alt_ganha = getString(R.string.altitude);
		java.text.DecimalFormat nft = new  
				java.text.DecimalFormat("#00.###");  
		nft.setDecimalSeparatorAlwaysShown(false);  
		DecimalFormat dec2 = new DecimalFormat("#.##");
		DecimalFormat dec1 = new DecimalFormat("#.###");

		String ConversorM;
		ConversorM = prefs.getString("m_k", "Km");
		if (facebookMessage == null && ConversorM.equals("Km") ){ /* Envio da msg para o face */
			Intent postOnFacebookWallIntent = new Intent(this, FinalWalkPost.class);
			postOnFacebookWallIntent.putExtra("facebookMessage", "");
			startActivity(postOnFacebookWallIntent);
			facebookMessage = terminar_caminhada+":"+time+"\n"+dia+":"+data+"\n"+percurso+":"+"\n"
					+"http://maps.google.com/?q=http://safesport.site40.net/"+currentFileName+"\n"+
					distancia_percorrida+":"+dec1.format(Dist)+" km"+"\n"
					+velocidade_maxima+dec2.format(MaxSp)+" Km/h"+"\n"+velocidade_media+dec2.format(Avgsp)+" Km/h"+"\n"
					+alt_ganha+":"+dec2.format(Alt)+" m"+"\n"+tempo_total+":"+nft.format(TH)+":"+""+nft.format(TM)+":"+""+nft.format(TS)+"\n";
		}

		else {

			if (facebookMessage == null && ConversorM.equals("M") ){ /* Envio da msg para o face */
				Intent postOnFacebookWallIntent = new Intent(this, FinalWalkPost.class);
				postOnFacebookWallIntent.putExtra("facebookMessage", "");
				startActivity(postOnFacebookWallIntent);
				facebookMessage = terminar_caminhada+":"+time+"\n"+dia+":"+data+"\n"+percurso+":"+"\n"
						+"http://maps.google.com/?q=http://safesport.site40.net/"+currentFileName+"\n"+
						distancia_percorrida+":"+dec1.format(Dist * 0.62)+" mi"+"\n"
						+velocidade_maxima+dec2.format(MaxSp * 0.62)+" mph"+"\n"+velocidade_media+dec2.format(Avgsp * 0.62)+" mph"+"\n"
						+alt_ganha+dec2.format(Alt)+" m"+"\n"+tempo_total+":"+""+nft.format(TH)+":"+""+nft.format(TM)+":"+""+nft.format(TS)+"\n";


			}


		}
		messageToPost = facebookMessage;



	}

	public void doNotShare(View button){
		finish();
	}
	public void share(View button){
		if (! facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			postToWall(messageToPost);
		}
	}

	public void loginAndPostToWall(){
		facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, (DialogListener) new LoginDialogListener());
	}

	public void postToWall(String message){
		String sucesso = getString(R.string.sucesso);
		String erro = getString(R.string.erro);
		Bundle parameters = new Bundle();
		parameters.putString("message", message);
		parameters.putString("description", "topic share");
		long  id=489339014422899l;
		try {
			facebook.request("me");
			String response = facebook.request("me/feed", parameters, "POST");
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") ||
					response.equals("false")) {
				showToast("Blank response.");
			}
			else {
				showToast(sucesso);
				finish();
			}

		} catch (Exception e) {
			showToast(erro);
			e.printStackTrace();
			finish();
		}
	}

	class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			saveCredentials(facebook);
			if (messageToPost != null)
			{
				postToWall(messageToPost);
			}
		}

		public void onCancel() {
			String cancelar_autenticacao = getString(R.string.cancelar_autenticacao);
			showToast(cancelar_autenticacao);
		}
		public void onFacebookError(FacebookError e) {
			String falha_autenticacao = getString(R.string.falha_autenticacao);
			showToast(falha_autenticacao);
			finish();
		}
		public void onError(DialogError e) {
			String falha_autenticacao = getString(R.string.falha_autenticacao);
			showToast(falha_autenticacao);
			finish();

		}
	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	public String setFileName(String filename) {
		// TODO Auto-generated method stub
		FinalWalkPost.currentFileName = filename;

		return filename;
	}







	public  void setDistance(float distance) {
		// TODO Auto-generated method stub
		dist= distance;	
		FinalWalkPost.Dist=dist;
	}
	public  void setMaxSpeed(double Mkph) {
		// TODO Auto-generated method stub
		Speed = Mkph;	
		FinalWalkPost.MaxSp = Speed;
	}
	public  void setAvgSpeed(double avgSpeed) {
		// TODO Auto-generated method stub
		Avg = avgSpeed;	
		FinalWalkPost.Avgsp = Avg;
	}
	public  void setAltGain(double altitude) {
		// TODO Auto-generated method stub
		Altitude = altitude;	
		FinalWalkPost.Alt = Altitude;
	}


	public  void setHours(double TotalHours) {
		// TODO Auto-generated method stub
		THours = TotalHours;	
		FinalWalkPost.TH = THours;
	}
	public  void setMinutes(double TotalMinutes) {
		// TODO Auto-generated method stub
		TMinutes = TotalMinutes;	
		FinalWalkPost.TM = TMinutes;
	}
	public  void setSeconds(double TotalSeconds) {
		// TODO Auto-generated method stub
		TSeconds = TotalSeconds;	
		FinalWalkPost.TS = TSeconds;
	}







}