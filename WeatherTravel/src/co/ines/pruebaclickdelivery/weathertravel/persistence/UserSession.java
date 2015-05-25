package co.ines.pruebaclickdelivery.weathertravel.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import com.google.android.gms.maps.model.LatLng;


public class UserSession {

	private static String ID = "";
	private static String NAME = "";
	private static String CITY = "";
	private static String WEATHER = "";
	private static LatLng latLngGps;
	private static LatLng latLngSelect;


	public static String getWEATHER() {
		return WEATHER;
	}

	public static void setWEATHER(String wEATHER) {
		WEATHER = wEATHER;
	}

	public UserSession(String id, String  name) {
		UserSession.ID = id;
		UserSession.NAME = name;
		latLngGps = null;
		latLngSelect = null;
	}	

	public static String getCITY() {
		return CITY;
	}

	public static void setCITY(String cITY) {
		CITY = cITY;
	}

	public static void cleanAll() {
		ID = "";
		NAME = "";
	}
	public static void setID(String iD) {
		ID = iD;
	}
	public static void setNAME(String nAME) {
		NAME = nAME;
	}
	public static String getID() {
		return ID;
		//return currentUser.getObjectId();
	}
	public static String getNAME() {
		return NAME;
		//return currentUser.getString("Name");
	}

	public static LatLng getLatLngGps() {
		return latLngGps;
	}

	public static void setLatLngGps(LatLng latLngGps) {
		UserSession.latLngGps = latLngGps;
	}

	public static LatLng getLatLngSelect() {
		return latLngSelect;
	}

	public static void setLatLngSelect(LatLng latLngSelect) {
		UserSession.latLngSelect = latLngSelect;
	}


}
