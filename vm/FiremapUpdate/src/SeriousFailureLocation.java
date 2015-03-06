

import java.math.BigDecimal;
import java.util.Date;

public class SeriousFailureLocation {
	
	/**
	*   重大不合格場所  
	*  
	*/
	
	private String name;
	private String address;
	private String checkResult;
	private Date date;
	private BigDecimal lat;
	private BigDecimal lng;
	private int id;

	
	public SeriousFailureLocation(String name,String addr,String check,Date date,BigDecimal lat,BigDecimal lng){
		this.name=name;
		this.address=addr;
		this.checkResult=check;
		this.date=date;
		this.lat=lat;
		this.lng=lng;
	}
	
	public SeriousFailureLocation(int id,String name,String addr,String check,BigDecimal lat,BigDecimal lng){
		this.id=id;
		this.name=name;
		this.address=addr;
		this.checkResult=check;
		this.lat=lat;
		this.lng=lng;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getCheckResult(){
		return this.checkResult;
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
	

}
