package co.ines.pruebaclickdelivery.weathertravel.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import com.google.android.gms.maps.model.LatLng;

public class Markers {
	
	
	private static ArrayList<Markers> markers;
	
	private static String ID = "";
	private static String NAME = "";
	private static String ADD = "";
	private static LatLng LOCATION = null;
	private static boolean isHospital = false;

	
	public Markers(String id, String  name) {
		Markers.ID = id;
		Markers.NAME = name;
	}	
	
	public static void cleanAll() {
		ID = "";
		NAME = "";
		ADD = "";
	}

	public static String getID() {
		return ID;
	}

	public static void setID(String iD) {
		ID = iD;
	}

	public static String getNAME() {
		return NAME;
	}

	public static void setNAME(String nAME) {
		NAME = nAME;
	}

	public static String getADD() {
		return ADD;
	}

	public static void setADD(String aDD) {
		ADD = aDD;
	}

	public static LatLng getLOCATION() {
		return LOCATION;
	}

	public static void setLOCATION(LatLng lOCATION) {
		LOCATION = lOCATION;
	}

	public static boolean isHospital() {
		return isHospital;
	}

	public static void setHospital(boolean isHospital) {
		Markers.isHospital = isHospital;
	}

	public static ArrayList<Markers> getMarkers() {
		return markers;
	}

	public static void setMarkers(ArrayList<Markers> markers) {
		Markers.markers = markers;
	}
	
}
