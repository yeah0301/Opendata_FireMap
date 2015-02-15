package data;

import java.math.BigDecimal;

public class NarrowRoadway {
	
	/**
	*  臺北市搶救不易狹小巷道清冊.csv
	*  
	*/
	
	private String section;
	private String team;
	private String roadway;
	private float width;
	private BigDecimal lat;
	private BigDecimal lng;
	private String polygon;
	private String level;
	private int id;
	
	
	
	public NarrowRoadway(String sec,String team,String roadway,float width,String level,BigDecimal lat,BigDecimal lng){
		this.section=sec;
		this.team=team;
		this.roadway=roadway;
		this.level = level;
		this.width=width;
		this.lat=lat;
		this.lng=lng;
	}
	
	public NarrowRoadway(String sec,String team,String roadway,float width,String level,BigDecimal lat,BigDecimal lng,String polygon){
		this.section=sec;
		this.team=team;
		this.roadway=roadway;
		this.width=width;
		this.lat=lat;
		this.lng=lng;
		this.polygon=polygon;
		this.level = level;
	}
	
	
	public NarrowRoadway(int id,String sec,String team,String roadway,float width,BigDecimal lat,BigDecimal lng){
		this.id=id;
		this.section=sec;
		this.team=team;
		this.roadway=roadway;
		this.width=width;
		this.lat=lat;
		this.lng=lng;
	}
	
	public String getLevel(){
		return this.level;
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getPolygon(){
		return this.polygon;
	}
	
	public String getSection(){
		return this.section;
	}
	
	public String getTeam(){
		return this.team;
	}
	
	public String getRoadway(){
		return this.roadway;
	}
	
	public float getWidth(){
		return this.width;
	}
	public BigDecimal getLat(){
		return lat;
	}

	public BigDecimal getLng(){
		return lng;
	}
	
	
	

}
