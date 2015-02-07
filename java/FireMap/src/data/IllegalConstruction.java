package data;

import java.math.BigDecimal;
import java.util.Date;

public class IllegalConstruction {
	
	/**
	*  屋頂違建隔出3個使用單元以上.csv
	*  
	*/
	
	private String section;
	private String address;
	private float area;
	private Date date;
	private BigDecimal lat ;
	private BigDecimal lng;
	private int id;
	
	public IllegalConstruction(String section,String address,float area,Date date,BigDecimal lat,BigDecimal lng){
		this.section=section;
		this.address=address;
		this.area=area;
		this.date=date;
		this.lat=lat;
		this.lng=lng;
	}
	
	public IllegalConstruction(int id,BigDecimal lat,BigDecimal lng){
		this.id=id;
		this.lat=lat;
		this.lng=lng;
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getSection(){
		return this.section;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public float getArea(){
		return this.area;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public BigDecimal getLat(){
		return lat;
	}

	public BigDecimal getLng(){
		return lng;
	}
	
	@Override
	public String toString(){
		return "id:"+id+" lat:"+lat+" lng:"+lng;
	}

}
