package data;

import java.math.BigDecimal;

public class Hydrant {
	
	
    private BigDecimal lat;
    private BigDecimal lng;
    private int id;
    
    
    public Hydrant(){
    	
    	
    }
    
    public Hydrant(int id,BigDecimal lat,BigDecimal lng) {
    	this.id=id;
    	this.lat=lat;
    	this.lng=lng;
    }
    
    public int getID(){
    	return this.id;
    }

    public void setLat(BigDecimal lat){
    	this.lat=lat;
    }
    
    public void setLng(BigDecimal lng){
    	this.lng=lng;
    }
    
    public BigDecimal getLat(){
    	return this.lat;
    }
    
    public BigDecimal getLng(){
    	return this.lng;
    }
   
    public String toString() {
    	return "lat: "+lat+" lng:"+lng;
    }


}