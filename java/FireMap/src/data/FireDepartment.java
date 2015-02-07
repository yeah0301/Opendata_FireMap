package data;

import java.math.BigDecimal;

public class FireDepartment {
	
	
	/**
	*   臺北市政府消防局各分隊座標位置
	*  
	*/
	
	private String name;
	private float TWD67_X;
	private float TWD67_Y;
	private BigDecimal lat;
	private BigDecimal lng;
	private String address;
	private int id;
	
	public FireDepartment(String name,float x,float y,BigDecimal lat,BigDecimal lng,String addr){
		this.name=name;
		this.TWD67_X=x;
		this.TWD67_Y=y;
		this.lat=lat;
		this.lng=lng;
		this.address=addr;
	}
	
	public FireDepartment(int id,String name,BigDecimal lat,BigDecimal lng){
		this.id=id;
		this.name=name;
		this.lat=lat;
		this.lng=lng;
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public float getTWD67_X(){
		return this.TWD67_X;
	}
	
	public float getTWD67_Y(){
		return this.TWD67_Y;
	}
	
	public String getAddress(){
		return address;
	}
	
	public BigDecimal getLat(){
		return lat;
	}

	public BigDecimal getLng(){
		return lng;
	}
	
	@Override
	public String toString(){
		return "name:"+name+" lat:"+lat+" lng:"+lng;
	}

}
