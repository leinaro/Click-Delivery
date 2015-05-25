package co.ines.pruebaclickdelivery.weathertravel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.ines.pruebaclickdelivery.weathertravel.R;
import co.ines.pruebaclickdelivery.weathertravel.persistence.Markers;
import co.ines.pruebaclickdelivery.weathertravel.persistence.UserSession;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;















import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RequestOpenWeatherMap extends AsyncTask<String, String, String>{

	private Context mContext;
	private GoogleMap mMap;
	//	private LatLng latLng;
	private List<Marker> mMarkers;
	private Map<Marker, String> mMarkerRainbowMap;
	private TextView mTopText;

	private boolean iam = false;
	
	private LatLng latLng;
	
	private LinearLayout ln;

	public RequestOpenWeatherMap(Context mContext, GoogleMap mMap, List<Marker> list, Map<Marker, String> mMarkerRainbowMap, boolean iam, LatLng latLng,LinearLayout ln) {
		this.mContext 	= mContext;
		this.mMap 		= mMap;
		this.mMarkers 	= list;
		this.iam = iam;
		this.latLng = latLng;
		this.mMarkerRainbowMap = mMarkerRainbowMap;
		this.ln = ln;

	}
	public RequestOpenWeatherMap(Context mContext, GoogleMap mMap, List<Marker> list, Map<Marker, String> mMarkerRainbowMap, boolean iam, LatLng latLng, TextView mTopText,LinearLayout ln) {
		this.mContext 	= mContext;
		this.mMap 		= mMap;
		this.mMarkers 	= list;
		this.iam = iam;
		this.latLng = latLng;
		this.mMarkerRainbowMap = mMarkerRainbowMap;
		this.mTopText = mTopText;
		this.ln = ln;
	}

	@Override
	protected String doInBackground(String... params) {
		StringBuffer bufferCadena = new StringBuffer("");

		try {
			HttpClient cliente = new DefaultHttpClient();
			HttpGet peticion = new HttpGet(params[0]);
			// ejecuta una petición get
			HttpResponse respuesta = cliente.execute(peticion);

			//lee el resultado
			BufferedReader entrada = new BufferedReader(new InputStreamReader(
					respuesta.getEntity().getContent()));

			String separador = "";
			String NL = System.getProperty("line.separator");
			//almacena el resultado en bufferCadena

			while ((separador = entrada.readLine()) != null) {
				bufferCadena.append(separador + NL);
			}
			entrada.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bufferCadena.toString();
	}

	protected void onPostExecute(String mensaje) {
		try {
			if (iam) {
				JSONObject jb = new JSONObject(mensaje);
				String name = jb.getString("name");
				
				JSONArray weather = jb.getJSONArray("weather");

				JSONObject weatherValue = weather.getJSONObject(0);

				String add = weatherValue.getString("description"); 
//				"http://openweathermap.org/img/w/"
				String iconurl =  weatherValue.getString("icon");
//				Bitmap icon = null;//getBitmapFromURL(iconurl);

				JSONObject location = jb.getJSONObject("coord");

				String lat = location.getString("lat"); 
				String lon = location.getString("lon"); 

				Marker marker = mMap.addMarker(new MarkerOptions()
				.position(latLng)
				.title(name)
				.snippet(add)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				
				mMarkers.add(marker);
				mMarkerRainbowMap.put(marker, iconurl);

				marker = mMap.addMarker(new MarkerOptions()
				.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
				.title(name)
				.snippet(add)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				
				mMarkers.add(marker);
				mMarkerRainbowMap.put(marker, iconurl);
				UserSession.setCITY(marker.getTitle());
				UserSession.setWEATHER(marker.getSnippet());
				mTopText.setText("Te encuentras en "+UserSession.getCITY()+" y el clima esta '"+UserSession.getWEATHER()+"'. \n Selecciona tu proximo destino.");


			}else{

				Log.d("IARL", "---> jb ");

				JSONObject jb = new JSONObject(mensaje);
				Log.d("IARL", "---> jb "+jb.toString());
				//	JSONObject jb1 = jb.getJSONObject("response");
				JSONArray venues = jb.getJSONArray("list");		
				Log.d("IARL", "--->"+venues.toString());

				for(int i=0;i<venues.length();i++){
					JSONObject b = venues.getJSONObject(i);
					String name = b.getString("name");
					Log.i("IARL","--->"+name);
					//				String id = b.getString("id");
					//				Log.i(".......",id);
					JSONObject location = b.getJSONObject("coord");
					//				Log.i("IARL","---> Lo "+location.toString());

					String lat = location.getString("lat"); 
					String lon = location.getString("lon"); 
					//				Log.i("IARL","--->"+lat+" - "+lon);

					JSONArray weather = b.getJSONArray("weather");
					//				Log.i("IARL","---> weather "+weather.toString());

					JSONObject weatherValue = weather.getJSONObject(0);

					String add = weatherValue.getString("description"); 
					String iconurl =  weatherValue.getString("icon");

//					Log.i("IARL","--->"+add);

					int badge = 0;
					if (iconurl.equals("01d")) {
		                badge = R.drawable.unod; // badge_qld;
		            } else if (iconurl.equals("01n")) {
		                badge = R.drawable.unon;
		            } else if (iconurl.equals("02d")) {
		                badge = R.drawable.dosd;
		            } else if (iconurl.equals("02n")) {
		                badge = R.drawable.dosn;
		            } else if (iconurl.equals("03d")) {
		                badge = R.drawable.tresd;
		            } else if (iconurl.equals("03n")) {
		                badge = R.drawable.tresn;
		            } else if (iconurl.equals("04d")) {
		                badge = R.drawable.cuatrod;
		            } else if (iconurl.equals("04n")) {
		                badge = R.drawable.cuatron;
		            } else if (iconurl.equals("09d")) {
		                badge = R.drawable.nueved;
		            } else if (iconurl.equals("09n")) {
		                badge = R.drawable.nueven;
		            } else if (iconurl.equals("10d")) {
		                badge = R.drawable.diezd;
		            } else if (iconurl.equals("10n")) {
		                badge = R.drawable.diezn;
		            } else if (iconurl.equals("50d")) {
		                badge = R.drawable.cincuentad;
		            } else if (iconurl.equals("50n")) {
		                badge = R.drawable.cincuentan;
		            } else if (iconurl.equals("11n")) {
		                badge = R.drawable.oncen;
		            } else if (iconurl.equals("11d")) {
		                badge = R.drawable.onced;
		            } else if (iconurl.equals("13n")) {
		                badge = R.drawable.trecen;
		            } else if (iconurl.equals("13d")) {
		                badge = R.drawable.treced;
		            } else {
		                // Passing 0 to setImageResource will clear the image view.
		                badge = 0;//R.drawable.unod;
		            }
					
					BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
					if (badge != 0) {
						icon = BitmapDescriptorFactory.fromResource(badge);
					}
					
					Marker marker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
					.title(name)
					.snippet(add)
//					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
					.icon(icon));

					mMarkers.add(marker);
					mMarkerRainbowMap.put(marker, iconurl);

				}
				ln.setVisibility(View.GONE);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


//	}
	
}
