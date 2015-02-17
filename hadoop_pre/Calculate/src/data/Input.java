package data;

public class Input {
	
	
	private String id;
	private String section;
	private double lat;
	private double lng;
	
	public Input(String id,String section,double lat,double lng){
		this.id=id;
		this.section=section;
		this.lat=lat;
		this.lng=lng;
	}

	public String getID(){
		return this.id;
	}
	
	public String getSection(){
		return this.section;
	}
	
	public double getLat(){
		return this.lat;
	}
	
	public double getLng(){
		return this.lng;
	}

}
