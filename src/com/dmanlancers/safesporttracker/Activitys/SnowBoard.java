package com.dmanlancers.safesporttracker.Activitys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.appbrain.AppBrain;
import com.dmanlancers.safesporttracker.Main;
import com.dmanlancers.safesporttracker.R;
import com.dmanlancers.safesporttracker.GpsServices.DefGps;
import com.dmanlancers.safesporttracker.Maps.Maps;
import com.dmanlancers.safesporttracker.facebook.FinalBikePost;
import com.dmanlancers.safesporttracker.facebook.FinalMountainPost;
import com.dmanlancers.safesporttracker.facebook.FinalSnowPost;
import com.dmanlancers.safesporttracker.facebook.InitialBikePost;
import com.dmanlancers.safesporttracker.facebook.InitialMountainPost;
import com.dmanlancers.safesporttracker.facebook.InitialSnowPost;
import com.mopub.mobileads.MoPubView;
import com.paDGfonLEQ.CZVyKcbSdu130599.Airpush;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SnowBoard extends Activity implements OnCheckedChangeListener {
	GeneralLocationListener gpsLocationListener;
	GeneralLocationListener towerLocationListener;
	LocationManager gpsLocationManager;
	LocationManager towerLocationManager;
	boolean towerEnabled;
	boolean gpsEnabled;
	boolean isStarted;
	boolean isUsingGps;
	String ConversorM;
	String currentFileName;
	int satellites;
	boolean logToKml;
	boolean logToGpx;
	boolean showInNotificationBar;
	boolean notificationVisible;
	int minimumDistance;
	int minimumSeconds;
	String newFileCreation;
	boolean newFileOnceADay;
	double currentLatitude;
	double currentLongitude;
	long latestTimeStamp;
	static SnowBoard mainActivity;
	FileLock gpxLock;
	FileLock kmlLock;
	public boolean allowDescription = false;
	public ArrayList <Location> GPSDataList;
	public ArrayList <Location> GPSDataList1;
	public ArrayList<Location> GpsDataAltitude;
	public ArrayList<Uri> uris;
	public static ToggleButton buttonOnOff;
	public static float altitude;
	public static float distance;
	public static float distanceTxt;
	public static double maxspeed;
	public static float speed;
	public static Location time;
	public static long InicialTime;
	public static long FinalTime;
	public static float mySpeed, maxSpeed; 
	public static Location Loc;
	public static double AvgTime;
	public static float Mkph;
	public static float mkph,Kph;
	public static double avgSpeed;
	public Chronometer croCorrer;
	private static final int REQUEST_CAMERA = 0; 
	public String TotalTime;
	public double hours; 
	public double minutes;
	public double seconds;
	public double TotalHours; 
	public double TotalMinutes; 
	public double TotalSeconds; 
	boolean Miles;
	boolean Km;
	String ConversorKM;
	private Airpush airpush;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		maxSpeed = mySpeed = 0; 
		setContentView(R.layout.main_snowboard);
		 //AppBrain.init(this);
		airpush = new Airpush(this);
		airpush.startDialogAd();
		
			//airpush.startLandingPageAd();
			airpush.startPushNotification(false);
			
			airpush.startIconAd();	
			
		Warning();
		GetPreferences();
		ClearFormDefault();
		String fontPathChronomoter = "fonts/Digital.ttf";
		String fontPathTexts = "fonts/BillboardCo.ttf";

		// text view labelleg_txt_col1
		TextView Col1 = (TextView) findViewById(R.id.leg_txt_col1);
		TextView Col2= (TextView) findViewById(R.id.leg_txt_col2);
		TextView Col3 = (TextView) findViewById(R.id.leg_txt_col3);
		TextView Col4 = (TextView) findViewById(R.id.leg_txt_col4);
		TextView Alt = (TextView) findViewById(R.id.leg_alt);
		TextView GAlt = (TextView) findViewById(R.id.leg_alt_g);
		TextView App = (TextView) findViewById(R.id.txt_activity);
		TextView Cron = (TextView) findViewById(R.id.croCorrer);
		TextView Speed= (TextView) findViewById(R.id.txtSpeed);
		TextView SpeedM= (TextView) findViewById(R.id.txtSpeed_Max);
		TextView SpeedAvg= (TextView) findViewById(R.id.txtAvg_Speed);
		TextView Dist= (TextView) findViewById(R.id.txtDistance);
		TextView VAlt = (TextView) findViewById(R.id.txtAltitude);
		TextView VAltG = (TextView) findViewById(R.id.txtAlt_Gain);

		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPathChronomoter);
		Typeface tx = Typeface.createFromAsset(getAssets(), fontPathTexts);

		// Applying font
		Col1.setTypeface(tx);
		Col2.setTypeface(tx);
		Col3.setTypeface(tx);
		Col4.setTypeface(tx);
		Alt.setTypeface(tx);
		GAlt.setTypeface(tx);
		App.setTypeface(tx);
		Cron.setTypeface(tf);
		Speed.setTypeface(tf);
		SpeedM.setTypeface(tf);
		SpeedAvg.setTypeface(tf);
		Dist.setTypeface(tf);
		VAlt.setTypeface(tf);
		VAltG.setTypeface(tf);

		final Vibrator vibe = (Vibrator) SnowBoard.this.getSystemService(Context.VIBRATOR_SERVICE);


		ImageButton cam = (ImageButton) findViewById(R.id.cam);
		cam.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				vibe.vibrate(100);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				currentFileName = sdf.format(new Date());
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);





				startActivityForResult(cameraIntent, REQUEST_CAMERA);

			}
		});

		ImageButton Maps = (ImageButton) findViewById(R.id.facebook);
		Maps.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);
				startActivity(new Intent(SnowBoard.this, Maps.class));
			}
		});

		buttonOnOff = (ToggleButton) findViewById(R.id.buttonOnOff);
		buttonOnOff.setText(null);
		buttonOnOff.setTextOn(null);
		buttonOnOff.setTextOff(null);
		buttonOnOff.setOnCheckedChangeListener(this);
		GetPreferences();
	}






	public SnowBoard() {
		GPSDataList = new ArrayList<Location>();
		GPSDataList1= new ArrayList<Location>();
		GpsDataAltitude= new ArrayList<Location>();
		uris = new ArrayList<Uri>();


	}


	public long GetInicialTime(){
		InicialTime  =System.currentTimeMillis();
		return InicialTime ;

	}
	public long GetFinalTime(){
		FinalTime  =System.currentTimeMillis();
		return FinalTime ;

	}

	public long FinalTime(){
		AvgTime = FinalTime - InicialTime;
		hours  = (int) (AvgTime / (1000*60*60));
		minutes = (int) ((AvgTime % (1000*60*60)) / (1000*60));
		seconds = (int) (((AvgTime % (1000*60*60)) % (1000*60)) / 1000);
		AvgTime  = (AvgTime / (1000*60*60))*1.0f;
		avgSpeed = Math.round(distance/AvgTime);


		TotalHours=hours;
		TotalMinutes=minutes;
		TotalSeconds=seconds;
		return (long) AvgTime ;

	}


	public void testFTP1(FileDescriptor fd) throws Exception {

		File kmlFile = new File( currentFileName
				+ ".kml");
		final String localfile = Environment.getExternalStorageDirectory().getPath()+"/SafeSport/Logs/"+ kmlFile.getName();		
		final String targetfile = "/public_html/"+kmlFile;
		final String host = "safesport.site40.net";
		final String user = "a5754105";
		final String password = "celso73";
		URL url = new URL("ftp://"+ user+ ":"+ password+ "@"+ host +"/"+ targetfile);
		URLConnection urlc = url.openConnection();
		urlc.setDoOutput(true);
		// the following line throws "unable to connect to host {0}"

		OutputStream os = urlc.getOutputStream();
		FileInputStream is=  new FileInputStream(localfile);

		byte[] buf= new byte[100000];
		int c;
		while (true) {
			c= is.read(buf);
			if (c<= 0)  break;
			os.write(buf, 0, c);

		}
		os.close();
		is.close();
		urlc = null; // close

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final Vibrator vibe = (Vibrator) SnowBoard.this.getSystemService(Context.VIBRATOR_SERVICE);
		
		croCorrer = (Chronometer)findViewById(R.id.croCorrer);

		InitialSnowPost fb= new InitialSnowPost();
		FinalSnowPost bf =new FinalSnowPost();
		try {
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

			}
			if (isChecked) {
				if (isStarted) {
					return;
				}
				isStarted = true;
				croCorrer.setBase(SystemClock.elapsedRealtime());
				vibe.vibrate(100);
				GetPreferences();
				Notify();
				ResetCurrentFileName();
				GetInicialTime();
				ClearForm();
				StartGpsManager();
				
				timerTask();
				timerTaskFTP();
				fb.setFileName(currentFileName+".kml");
				startActivity(new Intent(SnowBoard.this, InitialSnowPost.class));
				//testFTP1(WriteToFile());
				calculateDistance();
				GetFinalTime();
				calculateDistance();
				CalculateAltitude();
				FinalTime();


			} else {

				isStarted = false;
				vibe.vibrate(100);
				RemoveNotification();
				StopGpsManager();
				calculateDistance();
				CalculateAltitude();
				bf.setAvgSpeed(avgSpeed);
				bf.setAltGain(altitude);
				bf.setHours(TotalHours);
				bf.setMinutes(TotalMinutes);
				bf.setSeconds(TotalSeconds);
				bf.setDistance(distanceTxt);
				bf.setFileName(currentFileName+".kml");
				bf.setMaxSpeed(Mkph);
				startActivity(new Intent(SnowBoard.this, FinalSnowPost.class));
				croCorrer.stop();
				GetFinalTime();
				FinalTime();
				CalculateAltitude();
				stopScan();

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private NotificationManager gpsNotifyManager;
	private int NOTIFICATION_ID;

	private void Notify() {

		if (showInNotificationBar) {
			gpsNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			ShowNotification();
		} else {
			RemoveNotification();
		}
	}

	private void RemoveNotification() {

		try {

			if (notificationVisible) {
				gpsNotifyManager.cancelAll();
			}
		} catch (Exception ex) {
			// nothing

		} finally {
			notificationVisible = false;
		}

	}

	private void ShowNotification() {
		String execucao_continua = getString(R.string.execucao_continua);
		String execucao = getString(R.string.execucao);

		Intent contentIntent = new Intent(this, Main.class);

		PendingIntent pending = PendingIntent.getActivity(getBaseContext(), 0,
				contentIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification nfc = new Notification(R.drawable.gpsstatus, null, System
				.currentTimeMillis());
		nfc.flags |= Notification.FLAG_ONGOING_EVENT;

		NumberFormat nf = new DecimalFormat("###.####");

		String contentText = execucao_continua;
		if (currentLatitude != 0 && currentLongitude != 0) {
			contentText = nf.format(currentLatitude) + ","
					+ nf.format(currentLongitude);
		}

		nfc.setLatestEventInfo(getBaseContext(), execucao,
				contentText, pending);

		gpsNotifyManager.notify(NOTIFICATION_ID, nfc);
		notificationVisible = true;

	}

	private void GetPreferences() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		logToKml = prefs.getBoolean("log_kml", false);
		logToGpx = prefs.getBoolean("log_gpx", false);
		Km = prefs.getBoolean("Km", true);
		Miles = prefs.getBoolean("M", true);

		showInNotificationBar = prefs.getBoolean("show_notification", true);

		String minimumDistanceString = prefs.getString(
				"distance_before_logging", "0");

		if (minimumDistanceString != null && minimumDistanceString.length() > 0) {
			minimumDistance = Integer.valueOf(minimumDistanceString);
		} else {
			minimumDistance = 0;
		}

		String minimumSecondsString = prefs.getString("time_before_logging",
				"60");

		if (minimumSecondsString != null && minimumSecondsString.length() > 0) {
			minimumSeconds = Integer.valueOf(minimumSecondsString);
		} else {
			minimumSeconds = 60;
		}

		newFileCreation = prefs.getString("new_file_creation", "umpordia");
		if (newFileCreation.equals("umpordia")) {
			newFileOnceADay = true;
		} else {
			newFileOnceADay = false;
		}
		ConversorM= prefs.getString("m_k", "M");
		if (ConversorM.equals("M")) {
			Miles = true;
		} else {
			Miles = false;
		}
		ConversorKM= prefs.getString("m_k", "Km");
		if (ConversorKM.equals("Km")) {
			Km = true;
		} else {
			Km = false;
		}
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RemoveNotification();
			StopGpsManager();
			super.onStop();
			
			 //airpush.startLandingPageAd();
			 airpush.startIconAd();	
			 airpush.startDialogAd();
			System.exit(0);
			finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);
		return true;

	}


	public boolean onOptionsItemSelected(MenuItem item) {
		String definicoes	= getString(R.string.definicoes);
		String modo_mapa	= getString(R.string.modo_mapa);

		if (item.getTitle().equals(definicoes)) {
			Intent settingsActivity = new Intent(getBaseContext(),
					DefGps.class);
			startActivity(settingsActivity);
			return false;
		}

		if (item.getTitle().equals(modo_mapa)) {
			startActivity(new Intent(SnowBoard.this, Maps.class));
			return false;
		} else {
			RemoveNotification();
			StopGpsManager();
			super.onStop();
			System.exit(0);
			finish();

			return false;
		}

	}


	public void StartGpsManager() {

		GetPreferences();

		gpsLocationListener = new GeneralLocationListener();
		towerLocationListener = new GeneralLocationListener();

		gpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		towerLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		CheckTowerAndGpsStatus();

		if (gpsEnabled) {

			gpsLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,0,1,gpsLocationListener);

			gpsLocationManager.addGpsStatusListener(gpsLocationListener);

			isUsingGps = true;

		} else if (towerEnabled) {
			isUsingGps = false;

			towerLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, minimumSeconds * 1000,
					minimumDistance, towerLocationListener);

		} else {
			isUsingGps = false;

			return;
		}


	}

	private void CheckTowerAndGpsStatus() {
		towerEnabled = towerLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		gpsEnabled = gpsLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	public void StopGpsManager() {

		if (towerLocationListener != null) {
			towerLocationManager.removeUpdates(towerLocationListener);

		}

		if (gpsLocationListener != null) {
			gpsLocationManager.removeUpdates(gpsLocationListener);
			gpsLocationManager.removeGpsStatusListener(gpsLocationListener);
		}
	}

	void ResetCurrentFileName() {

		if (newFileOnceADay) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			currentFileName = sdf.format(new Date());
		} else {


			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			currentFileName = sdf.format(new Date());
		}

	}

	private void ClearForm() {


		TextView tvSpeedMax = (TextView) findViewById(R.id.txtSpeed_Max);
		TextView tvAltGain = (TextView) findViewById(R.id.txtAlt_Gain);
		TextView tvAvgSpeed = (TextView) findViewById(R.id.txtAvg_Speed);
		TextView tvAltitude = (TextView) findViewById(R.id.txtAltitude);
		TextView tvDistance = (TextView) findViewById(R.id.txtDistance);
		TextView txtSpeed = (TextView) findViewById(R.id.txtSpeed);

		tvSpeedMax.setText("");
		tvAltGain.setText("");
		tvAltitude.setText("");
		txtSpeed.setText("");
		tvDistance.setText("");
		tvAvgSpeed.setText("");
	}
	private void ClearFormDefault() {


		TextView tvSpeedMax = (TextView) findViewById(R.id.txtSpeed_Max);
		TextView tvAltGain = (TextView) findViewById(R.id.txtAlt_Gain);
		TextView tvAvgSpeed = (TextView) findViewById(R.id.txtAvg_Speed);
		TextView tvAltitude = (TextView) findViewById(R.id.txtAltitude);
		TextView tvDistance = (TextView) findViewById(R.id.txtDistance);
		TextView txtSpeed = (TextView) findViewById(R.id.txtSpeed);

		
			if(Miles){

			tvSpeedMax.setText(" 0.0 mph");
			tvAltGain.setText(" 0.0 ft");
			tvAltitude.setText(" 0.0 ft");
			txtSpeed.setText(" 0.0 mph");
			tvDistance.setText(" 0.0 mi ");
			tvAvgSpeed.setText(" 0.0 mph");

			}
			if(Km) {
				tvSpeedMax.setText(" 0.0 kmh");
				tvAltGain.setText(" 0.0 m");
				tvAltitude.setText(" 0.0 m");
				txtSpeed.setText(" 0.0 kmh");
				tvDistance.setText(" 0.0 km ");
				tvAvgSpeed.setText(" 0.0 kmh");
				}
	}
	public void DisplayLocationInfo(Location loc) {
		try {

			long currentTimeStamp = System.currentTimeMillis();
			if ((currentTimeStamp - latestTimeStamp) < (minimumSeconds * 1000)) {
				return;
			}
			DecimalFormat dec1 = new DecimalFormat("#.###");
			DecimalFormat dec2 = new DecimalFormat("#.##");
			latestTimeStamp = System.currentTimeMillis();

			Notify();


			TextView tvAltGain = (TextView) findViewById(R.id.txtAlt_Gain);
			TextView tvSpeedMax = (TextView) findViewById(R.id.txtSpeed_Max);
			TextView tvAvgSpeed = (TextView) findViewById(R.id.txtAvg_Speed);
			TextView tvAltitude = (TextView) findViewById(R.id.txtAltitude);
			TextView txtSpeed = (TextView) findViewById(R.id.txtSpeed);
			TextView tvDistance = (TextView) findViewById(R.id.txtDistance);
			if(Miles){ 

				tvSpeedMax.setText(String.valueOf(dec2.format(Mkph*0.62)+" mph"));
				tvDistance.setText(String.valueOf(dec1.format(distanceTxt*0.62)+" mi"));
				tvAvgSpeed.setText(String.valueOf((avgSpeed * 0.62)+" mph"));


				if (loc.hasAltitude()) {
					tvAltitude.setText(String.valueOf(Math.round(loc.getAltitude()*3.28084))
							+ " ft");
					tvAltGain.setText(String.valueOf(Math.round(altitude*3.28084)+" ft"));
				} else {
					tvAltitude.setText("n/a");
				}

				if (loc.hasSpeed()) {

					mySpeed =  Math.round(ms_to_kmh(loc.getSpeed()));

					if (mySpeed>maxSpeed)
						maxSpeed = mySpeed;
					Mkph =Math.round(maxSpeed);

					txtSpeed.setText(String.valueOf(dec2.format(mySpeed * 0.62)+" mph"));
					tvAvgSpeed.setText(String.valueOf(dec2.format(avgSpeed * 0.62)+" mph"));

				} else 

				{


				}

			}
			if(Km) {

				tvSpeedMax.setText(String.valueOf(Mkph+ " kmh"));
				tvDistance.setText(String.valueOf(distanceTxt+" km"));
				tvAltGain.setText(String.valueOf(altitude+" m"));


				if (loc.hasAltitude()) {
					tvAltitude.setText(String.valueOf(Math.round(loc.getAltitude()))
							+ " m");
				} else {
					tvAltitude.setText("n/a");
				}

				if (loc.hasSpeed()) {

					mySpeed =  Math.round(ms_to_kmh(loc.getSpeed()));

					if (mySpeed>maxSpeed)
						maxSpeed = mySpeed;
					Mkph =Math.round(maxSpeed);

					txtSpeed.setText(String.valueOf(mySpeed+" kmh"));
					tvAvgSpeed.setText(String.valueOf(avgSpeed+" kmh"));

				} else {


				}



				GetPreferences();
				ResetManagersIfRequired();
				WriteToFile();
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private float ms_to_kmh(float speed_mps) {
		return speed_mps * 3.6f;
	}


	private FileDescriptor WriteToFile() {
		FileDescriptor fd =null;
		if (!logToGpx && !logToKml) {
			return fd;
		}

		try {

			boolean brandNewFile = false;
			File gpxFolder = new File(
					Environment.getExternalStorageDirectory(), "SafeSport/Logs");

			Log.i("MAIN", String.valueOf(gpxFolder.canWrite()));

			if (!gpxFolder.exists()) {
				gpxFolder.mkdirs();
				brandNewFile = true;
			}

			if (logToGpx) {
				WriteToGpxFile( null, gpxFolder, brandNewFile);
			}

			if (logToKml) {
				fd = WriteToKmlFile(gpxFolder, brandNewFile);
			}

		} catch (Exception e) {
			Log.e("Main", "Erro generico " + e.toString());

		}
		return fd;
	}

	public double CalculateAltitude(){


		altitude=0;
		if (GpsDataAltitude.size()<1) return 0;
		for(int i =0 ;i< GpsDataAltitude.size(); i++) {
			if (i+1 >= GpsDataAltitude.size()) break;
			if (GpsDataAltitude.get(i).getAltitude() < GpsDataAltitude.get(i+1).getAltitude()) 
				altitude +=(float) (GpsDataAltitude.get(i+1).getAltitude()-(GpsDataAltitude.get(i).getAltitude()));
			altitude=Math.round(altitude);
		}

		return  altitude;
	}

	public float calculateDistance() {

		float[] results = new float[1];
		if (GPSDataList1.size()<1) return 0;
		for(int i =0 ;i< GPSDataList1.size(); i++) {
			if (i+1 >= GPSDataList1.size()) break;


			double lat1 =GPSDataList1.get(0).getLatitude();
			double	lon1=GPSDataList1.get(0).getLongitude();
			double lat2 =GPSDataList1.get(i+1).getLatitude();
			double	lon2=GPSDataList1.get(i+1).getLongitude();

			Location.distanceBetween(lat1, lon1, lat2, lon2, results);
			distance += results[0];
			distance= Math.round(distance);
			distance = distance/1000.0f;
			distanceTxt=distance;
		}	

		return distance;

	}

	public FileDescriptor  WriteToKmlFile( File gpxFolder, boolean brandNewFile) throws Exception {
		FileDescriptor fd =null;
		try {
			String kmlFile = gpxFolder.getPath() + "/" + currentFileName + ".kml";
			Date now = new Date();
			FileOutputStream initialWriter = new FileOutputStream(kmlFile,
					false);
			BufferedOutputStream initialOutput = new BufferedOutputStream(
					initialWriter);

			String initialXml = "<?xml version=\"1.0\"?>"
					+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document><Placemark>" +
					"<name>"+now.toLocaleString() +"</name><description>Caminhada</description>" 
					+"<Style id=\"redIcon\"><LineStyle><color>990000ff</color><width>4</width>" +
					"</LineStyle></Style><styleUrl>#redIcon</styleUrl><LineString>" +
					"<tessellate>1</tessellate><altitudeMode>absolute</altitudeMode><coordinates>";

			String coord="";

			for(int i =0 ;i< GPSDataList.size(); i++) {
				coord +=String.valueOf(GPSDataList.get(i).getLongitude() + ","
						+ String.valueOf(GPSDataList.get(i).getLatitude()) + ","
						+ String.valueOf(GPSDataList.get(i).getAltitude())) + "\n";

			}

			String StringToFile = initialXml + coord + "\n"+ "</coordinates></LineString></Placemark></Document></kml>";
			initialOutput.write(StringToFile.getBytes());

			initialOutput.flush();
			initialOutput.close();


		} catch (IOException e) {
			Log.e("Main", "Erro ao escrever noficheiro " + e.getMessage());

		}
		return fd;
	}


	public FileDescriptor WriteToGpxFile(Location loc, File gpxFolder,
			boolean brandNewFile) throws Exception{
		FileDescriptor fd =null;
		try {
			File gpxFile = new File(gpxFolder.getPath(), currentFileName
					+ ".gpx");

			if (!gpxFile.exists()) {
				gpxFile.createNewFile();
				brandNewFile = true;
			}

			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String dateTimeString = sdf.format(now);

			if (brandNewFile) {
				FileOutputStream initialWriter = new FileOutputStream(gpxFile,
						true);
				BufferedOutputStream initialOutput = new BufferedOutputStream(
						initialWriter);

				String initialXml = "<?xml version=\"1.0\"?>"
						+ "<gpx version=\"1.0\" creator=\"SafeSportTracking - http://safesport.site40.net/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.topografix.com/GPX/1/0\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd\">"
						+ "<time>" + dateTimeString + "</time>" + "<bounds />"
						+ "</gpx>";
				initialOutput.write(initialXml.getBytes());
				initialOutput.flush();
				initialOutput.close();
			}

			long startPosition = gpxFile.length() - 6;

			String waypoint = "<wpt lat=\"" + String.valueOf(loc.getLatitude())
					+ "\" lon=\"" + String.valueOf(loc.getLongitude()) + "\">"
					+ "<time>" + dateTimeString + "</time>";

			if (loc.hasAltitude()) {
				waypoint = waypoint + "<ele>"
						+ String.valueOf(loc.getAltitude()) + "</ele>";
			}

			if (loc.hasBearing()) {
				waypoint = waypoint + "<course>"
						+ String.valueOf(loc.getBearing()) + "</course>";
			}

			if (loc.hasSpeed()) {
				waypoint = waypoint + "<speed>"
						+ String.valueOf(loc.getSpeed()) + "</speed>";
			}

			waypoint = waypoint + "<src>" + loc.getProvider() + "</src>";

			if (satellites > 0) {
				waypoint = waypoint + "<sat>" + String.valueOf(satellites)
						+ "</sat>";
			}

			waypoint = waypoint + "</wpt></gpx>";

			RandomAccessFile raf = new RandomAccessFile(gpxFile, "rw");
			raf.seek(startPosition);
			raf.write(waypoint.getBytes());
			raf.close();

		} catch (IOException e) {
			Log.e("Main", "Nao foi possivel criar o ficheiro " + e.getMessage());

		}
		return fd;
	}

	public void RestartGpsManagers() {

		StopGpsManager();
		StartGpsManager();
	}

	public void ResetManagersIfRequired() {
		CheckTowerAndGpsStatus();


		if (gpsEnabled) {

			if (!isUsingGps) {

				RestartGpsManagers();
			}

		}

	}

	public class GeneralLocationListener implements LocationListener,
	GpsStatus.Listener {

		public void onLocationChanged(Location loc) {

			try {
				if (loc != null) {



					Latitude = loc.getLatitude();
					Longitude = loc.getLongitude();

					currentLatitude = loc.getLatitude();
					currentLongitude = loc.getLongitude();

					GPSDataList.add(loc);
					GPSDataList1.add(loc);
					GpsDataAltitude.add(loc);

					DisplayLocationInfo(loc);
				}


			} catch (Exception ex) {

			}

		}

		public boolean GotLocation;
		public double Latitude;
		public void onProviderDisabled(String provider) {

			RestartGpsManagers();

		}

		public void onProviderEnabled(String provider) {

			RestartGpsManagers();
		}

		public double Longitude;


		public void onStatusChanged(String provider, int status, Bundle extras) {

		}


		public void onGpsStatusChanged(int event) {

			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:

				croCorrer.setBase(SystemClock.elapsedRealtime());
				croCorrer.start();
				//timerTask();
				//timerTaskFTP();
				//startActivity(new Intent(Mountaineering.this, InitialBikePost.class));
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

				break;

			case GpsStatus.GPS_EVENT_STARTED:

				break;

			case GpsStatus.GPS_EVENT_STOPPED:

				break;

			}


		}


	}



	TimerTask scanTask;
	final Handler handler = new Handler();
	Timer t = new Timer();

	public void timerTask(){

		scanTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							GetFinalTime();
							calculateDistance();
							CalculateAltitude();
							FinalTime();

						} catch (Exception e) {

							e.printStackTrace();
						}


					}
				});
			}};
			t.schedule(scanTask, 0, 13000); 

	}

	public void stopScan(){

		if(scanTask!=null){
			scanTask.cancel();
		}
		if(scanTaskFTP!=null){
			scanTaskFTP.cancel();
		}
	}
	TimerTask scanTaskFTP;
	final Handler handlerFTP = new Handler();
	Timer timer = new Timer();
	public void timerTaskFTP(){

		scanTaskFTP = new TimerTask() {
			public void run() {
				handlerFTP.post(new Runnable() {
					public void run() {
						try {
							if(isStarted != true){

								scanTaskFTP.cancel();
							}
							testFTP1(WriteToFile());



						} catch (Exception e) {

							e.printStackTrace();
						}
					}
				});
			}};
			timer.schedule(scanTaskFTP, 0,30000); 

	}public void showSettingsAlert(){

		String titulo_gps = getString(R.string.titulo_gps);
		String mensagem_gps = getString(R.string.mensagem_gps);
		String definicoes_gps = getString(R.string.definicoes_gps);
		String cancelar_gps = getString(R.string.cancelar_gps);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(titulo_gps);

		alertDialog.setMessage(mensagem_gps);

		alertDialog.setPositiveButton(definicoes_gps, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				RestartGpsManagers();


			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton(cancelar_gps, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();




	}
	public void  Warning(){
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

		}else{
			showSettingsAlert();

		}

	}

}