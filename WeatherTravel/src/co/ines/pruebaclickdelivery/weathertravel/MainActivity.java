package co.ines.pruebaclickdelivery.weathertravel;

import co.ines.pruebaclickdelivery.weathertravel.R;
import co.ines.pruebaclickdelivery.weathertravel.persistence.UserSession;
import co.ines.pruebaclickdelivery.weathertravel.util.RequestOpenWeatherMap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements 
OnMapReadyCallback, 
OnMarkerClickListener,
OnInfoWindowClickListener,
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener,
OnMyLocationButtonClickListener,
OnMarkerDragListener,
OnCameraChangeListener{

	private LatLng latLngTemp;
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	private GoogleApiClient mGoogleApiClient;
	private LinearLayout lnProgressBar;
	private String cityTemp = "";
	private String weatherTemp = "";

	/** These settings are the same as the settings for the map. They will in fact give you updates
	 *  at the maximal rates currently possible.
	 */
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {
		// These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
		// "title" and "snippet".
		private final View mWindow;
		private final View mContents;

		CustomInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
			mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow);
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			render(marker, mContents);
			return mContents;
		}

		private void render(Marker marker, View view) {
			int badge;
			// Use the equals() method on a Marker to check for equals.  Do not use ==.
			String iconWeather = mMarkerRainbowMap.get(marker);
			if (iconWeather.equals("01d")) {
                badge = R.drawable.unod; // badge_qld;
            } else if (iconWeather.equals("01n")) {
                badge = R.drawable.unon;
            } else if (iconWeather.equals("02d")) {
                badge = R.drawable.dosd;
            } else if (iconWeather.equals("02n")) {
                badge = R.drawable.dosn;
            } else if (iconWeather.equals("03d")) {
                badge = R.drawable.tresd;
            } else if (iconWeather.equals("03n")) {
                badge = R.drawable.tresn;
            } else if (iconWeather.equals("04d")) {
                badge = R.drawable.cuatrod;
            } else if (iconWeather.equals("04n")) {
                badge = R.drawable.cuatron;
            } else if (iconWeather.equals("09d")) {
                badge = R.drawable.nueved;
            } else if (iconWeather.equals("09n")) {
                badge = R.drawable.nueven;
            } else if (iconWeather.equals("10d")) {
                badge = R.drawable.diezd;
            } else if (iconWeather.equals("10n")) {
                badge = R.drawable.diezn;
            } else if (iconWeather.equals("50d")) {
                badge = R.drawable.cincuentad;
            } else if (iconWeather.equals("50n")) {
                badge = R.drawable.cincuentan;
            } else if (iconWeather.equals("11n")) {
                badge = R.drawable.oncen;
            } else if (iconWeather.equals("11d")) {
                badge = R.drawable.onced;
            } else if (iconWeather.equals("13n")) {
                badge = R.drawable.trecen;
            } else if (iconWeather.equals("13d")) {
                badge = R.drawable.treced;
            } else {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;//R.drawable.unod;
            }
			((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				// Spannable string allows us to edit the formatting of the text.
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}

			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}
	}

	/**
	 * Keeps track of the last selected marker (though it may no longer be selected).  This is
	 * useful for refreshing the info window.
	 */
	private Marker mLastSelectedMarker;

	private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
	private Map<Marker, String> mMarkerRainbowMap = new HashMap<Marker, String>();


	private TextView mTopText;
	//	private RadioGroup mOptions;

	private final Random mRandom = new Random();


	private static final String HOST_SEARCH 		= "http://api.openweathermap.org/data/2.5/find?"; 
	private static final String LAT					= "lat=";
	private static final String LON					= "&lon=";
	private static final String CNT					= "&cnt=10";
	private static final String LANG				= "&lang=sp";
	private static final String UNIT				= "&units=metric";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lnProgressBar = (LinearLayout) findViewById(R.id.ln1);

		mTopText = (TextView) findViewById(R.id.top_text);
		if (mLastSelectedMarker != null && mLastSelectedMarker.isInfoWindowShown()) {
			// Refresh the info window when the info window's content has changed.
			mLastSelectedMarker.showInfoWindow();
		}

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addApi(LocationServices.API)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.build();

	}


	@Override
	protected void onResume() {
		super.onResume();
		mGoogleApiClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;

		//		mMap.setMyLocationEnabled(true);
		mUiSettings = mMap.getUiSettings();

		// Keep the UI Settings state in sync with the checkboxes.
		mUiSettings.setZoomControlsEnabled(true);
		mUiSettings.setCompassEnabled(true);
		mUiSettings.setMyLocationButtonEnabled(true);
		mUiSettings.setScrollGesturesEnabled(true);
		mUiSettings.setZoomGesturesEnabled(true);
		mUiSettings.setTiltGesturesEnabled(true);
		mUiSettings.setRotateGesturesEnabled(true);
		mUiSettings.setMapToolbarEnabled(false);

		// Setting an info window adapter allows us to change the both the contents and look of the
		// info window.
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events.  See the bottom of this class for their behavior.
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);
		mMap.setOnCameraChangeListener(this);
		// Override the default content description on the view, for accessibility mode.
		// Ideally this string would be localised.
		map.setContentDescription("Map with lots of markers.");
		map.setOnMyLocationButtonClickListener(this);
		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation") // We use the new method when supported
				@SuppressLint("NewApi") // We check which build version we are using.
				@Override
				public void onGlobalLayout() {
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					} else {
						mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
				}
			});
		}
		showMyLocation();
	}



	/**
	 * Button to get current Location. This demonstrates how to get the current Location as required
	 * without needing to register a LocationListener.
	 */
	public void showMyLocation() {
		if (mGoogleApiClient.isConnected()) {
			Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

			LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
			UserSession.setLatLngGps(latLng);

			RequestOpenWeatherMap oWM1 =  new RequestOpenWeatherMap(this, mMap, mMarkerRainbow,mMarkerRainbowMap, true,latLng,mTopText,lnProgressBar);
			oWM1.execute("http://api.openweathermap.org/data/2.5/weather?lat="+latLng.latitude+"&lon="+latLng.longitude+LANG);

			RequestOpenWeatherMap oWM =  new RequestOpenWeatherMap(this, mMap, mMarkerRainbow,mMarkerRainbowMap, false,null,lnProgressBar);
			//			oWM.execute(HOST_SEARCH+LAT+latLng.latitude+LON+latLng.longitude+CNT+LANG+UNIT);
			oWM.execute("http://api.openweathermap.org/data/2.5/find?lat="+latLng.latitude+"&lon="+latLng.longitude+"&cnt=100000&lang=sp&units=metric");

			//			oWM.execute("http://api.openweathermap.org/data/2.5/box/city?bbox=-160,90,0,0,10&cluster=yes");

		}
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {}//in this app the location is a trial version

	/**
	 * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnectionSuspended(int cause) {
		// Do nothing
	}

	/**
	 * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, REQUEST, this);  // LocationListener
		showMyLocation();
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	/**
	 * Checks if the map is ready (which depends on whether the Google Play services APK is
	 * available. This should be called prior to calling any methods on GoogleMap.
	 */
	private boolean checkReady() {
		if (mMap == null) {
			Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/** Called when the update button is clicked. */
	public void updateMap(View view) {
		showMyLocation();
	}

	/** Called when the teleport button is clicked. */
	public void teleport(View view) {
		UserSession.setLatLngSelect(latLngTemp);
		UserSession.setCITY(cityTemp);
		UserSession.setWEATHER(weatherTemp);
		UserSession.setLatLngSelect(latLngTemp);
		mTopText.setText("Te encuentras en "+UserSession.getCITY()+" y el clima esta '"+UserSession.getWEATHER()+"'. \n Selecciona tu proximo destino.");

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setMessage("Teletransportacion completa!");
		alertDialogBuilder.setNeutralButton("Continuar",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	//
	// Marker related listeners.
	//
	@Override
	public boolean onMarkerClick(final Marker marker) {
		latLngTemp = marker.getPosition();
		cityTemp = marker.getTitle();
		weatherTemp = marker.getSnippet();
		mTopText.setText("En este momento el clima esta '"+marker.getSnippet()+"' en "+cityTemp+".Selecciona 'TELETRASPORTAR' para viajar.");
		mLastSelectedMarker = marker;
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {}

	@Override
	public void onMarkerDragStart(Marker marker) {}

	@Override
	public void onMarkerDragEnd(Marker marker) {}

	@Override
	public void onMarkerDrag(Marker marker) {}



	//Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onCameraChange(CameraPosition arg0) {
//		mMap.clear();
		lnProgressBar.setVisibility(View.VISIBLE);
		RequestOpenWeatherMap oWM =  new RequestOpenWeatherMap(MainActivity.this, mMap, mMarkerRainbow,mMarkerRainbowMap, false,null,lnProgressBar);
		//			oWM.execute(HOST_SEARCH+LAT+latLng.latitude+LON+latLng.longitude+CNT+LANG+UNIT);
		oWM.execute("http://api.openweathermap.org/data/2.5/find?lat="+arg0.target.latitude+"&lon="+arg0.target.longitude+"&cnt=100000&lang=sp&units=metric");
	}


}
