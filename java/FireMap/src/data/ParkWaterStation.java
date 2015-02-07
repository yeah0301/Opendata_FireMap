package data;

import java.math.BigDecimal;

public class ParkWaterStation {
	
	
	/**
	*  防災公園緊急維生取水站.csv
	*  
	*/
	
	private String section;
	private String name;
	private String address;
	private String note;
	private BigDecimal lat;
	private BigDecimal lng;
	
	
	public ParkWaterStation(String sec,String name,String addr,String note,BigDecimal lat,BigDecimal lng){
		this.section=sec;
		this.name=name;
		this.address=addr;
		this.note=note;
		this.lat=lat;
		this.lng=lng;
	}
	
	public String getSection(){
		return this.section;
	}
	
	public String getLocation(){
		return this.name;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getNote(){
		return this.note;
	}
	
	public BigDecimal getLat(){
		return lat;
	}

	public BigDecimal getLng(){
		return lng;
	}

}