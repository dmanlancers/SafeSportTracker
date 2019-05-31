package com.dmanlancers.safesporttracker.facebook;



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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class InitialRunPost extends Activity  {





	public static String currentFileName ;

	public static Float dist;

	public final String images[] = {"http://t2.gstatic.com/images?q=tbn:ANd9GcRuzUJ5dcWJvHZ-3a2u0HmHe9-flbOI3RfirNDnQxqDrJA3xk6a"};
	public int i = 0;




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

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		String data = sdf.format(new Date());
		String time = sdf1.format(new Date());
		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		String iniciar_corrida = getString(R.string.iniciar_corrida);
		String acompanhe = getString(R.string.acompanhe);
		String dia = getString(R.string.dia);

		if (facebookMessage == null){ /* Envio da msg para o face */
			Intent postOnFacebookWallIntent = new Intent(this, InitialRunPost.class);
			postOnFacebookWallIntent.putExtra("facebookMessage", "");
			startActivity(postOnFacebookWallIntent);
			facebookMessage = iniciar_corrida+":"+time+"\n"+dia+":"+data+"\n"+acompanhe+":"+"\n"
					+"http://maps.google.com/?q=http://safesport.site40.net/"+currentFileName;
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
		InitialRunPost.currentFileName = filename;

		return filename;
	}









}
