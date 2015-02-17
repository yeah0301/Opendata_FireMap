package data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Village {
	
	String name;
	String section;
	String area;
	LinkedList<HashMap<String, BigDecimal>> polygon;
	ArrayList<Double> lats;
	ArrayList<Double> lngs;
	int id;
	String json_polygon;
	
	
	public Village(String name,String section,String area,LinkedList<HashMap<String, BigDecimal>> poly){
		this.name = name;
		this.section = section;
		this.area = area;
		this.polygon = poly;
	}
	
	public Village(int id,String name,String section,ArrayList<Double> lats,ArrayList<Double> lngs,String json_polygon){
		this.id=id;
		this.name = name;
		this.section = section;
		this.lats=lats;
		this.lngs=lngs;
		this.json_polygon=json_polygon;
	}
	
	
	public String getJsonPolygon(){
		return this.json_polygon;
	}
	
	public int getID(){
		return this.id;
	}

	public String getName(){
		return this.name;
	}
	
	public String getSection(){
		return this.section;
	}
	
	public String getArea(){
		return this.area;
	}
	
	public ArrayList<Double> getlats(){
		return lats;
	}
	
	public ArrayList<Double> getlngs(){
		return lngs;
	}
	
	public LinkedList<HashMap<String, BigDecimal>> getPolygon(){
		return this.polygon;
	}
	
	@Override
	public String toString(){
		return "name: "+name;
	}
}