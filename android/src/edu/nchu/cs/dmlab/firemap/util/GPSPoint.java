package edu.nchu.cs.dmlab.firemap.util;

import com.google.android.maps.GeoPoint;

public class GPSPoint {
	private final double lat;
	private final double lon;
	GPSPoint(double lon, double lat){
		this.lat = lat;
		this.lon = lon;
	}
	GPSPoint(String lonlat){
		String pos[] = lonlat.split(",");
		this.lon = Double.parseDouble(pos[0]);
		this.lat = Double.parseDouble(pos[1]);
	}
	public static String geoToString(GeoPoint gp){
		return String.valueOf(gp.getLatitudeE6()/1E6)+","+String.valueOf(gp.getLongitudeE6()/1E6);
	}
	public double getLat(){ return lat; }
	public double getLon(){ return lon; }
	public GeoPoint getGeoPoint(){
		int ilat = (int) (lat*1E6);
		int ilon = (int) (lon*1E6);
		return new GeoPoint( ilat, ilon);
	}
}
