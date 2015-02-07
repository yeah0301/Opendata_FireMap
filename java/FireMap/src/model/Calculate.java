package model;

import java.awt.Polygon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import data.SQLServer;
import data.Village;
import data.*;
import utils.util;

public class Calculate {
	
	String[] sections={"中正區","萬華區","大同區","中山區","松山區","大安區"
			,"信義區","內湖區","南港區","士林區","北投區","文山區"};
	
	SQLServer sqlServer;
	util util=new util();
	
	//all villages
	LinkedList<Village> villages= new LinkedList<Village>(); 
	
	//( section , linklist(villag) )
	HashMap<String, LinkedList<Village>> section_map_village
	= new HashMap<String, LinkedList<Village>>();
	
	//( section , set(villag_id) )
	HashMap<String, HashSet<Integer>> section_map_villageID
	= new HashMap<String, HashSet<Integer>>();
	
	
	//( villag_id , polygon )
	HashMap<Integer, DoublePolygon> villageID_map_polygon = new HashMap<Integer, DoublePolygon>();
	
	// ( villag_id , score )
	HashMap<Integer, Float> villageID_map_score = new HashMap<Integer, Float>();
	
	
	

	//("區",linklist(違建))
	HashMap<String, LinkedList<IllegalConstruction>> section_map_illegal
	= new HashMap<String, LinkedList<IllegalConstruction>>();
	
	//("區",linklist(消防))
	HashMap<String, LinkedList<FireDepartment>> section_map_firedepartment
	= new HashMap<String, LinkedList<FireDepartment>>();
	
	

	public Calculate(){
		sqlServer = new SQLServer();
	}
	
	
	public void init_VillagePolygon(){
		
		//for(String section:sections){
			String section = "中正區";
			LinkedList<Village> villages=sqlServer.select_Village(section);
			HashSet<Integer> idSet = new HashSet<Integer>();
			
			for(Village v:villages){
				
				int id=v.getID();
				double[] lats=util.arraylist_to_array(v.getlats());
				double[] lngs=util.arraylist_to_array(v.getlngs());
				
				idSet.add(id);
				villageID_map_polygon.put(id, new DoublePolygon(lats,lngs));	
			}
			
			section_map_village.put(section, villages);
			section_map_villageID.put(section, idSet);
			

		//}
		
		for(String key:section_map_village.keySet())
			System.out.println(key+" : "+section_map_village.get(key).toString());
			

	}
	
	
	/**
	 * lat overflow
	 */
	public void init_IllegalConstruction(){
		
		for(String section:sections)
			section_map_illegal.put(section, sqlServer.select_IllegalConstruction(section));
		
		for(String key:section_map_illegal.keySet())
			System.out.println(key+":"+section_map_illegal.get(key).toString());
	}
	
	
	
	public void init_FireDepartment(){
		
		//for(String section:sections)
			section_map_firedepartment.put("中正區", sqlServer.select_FireDepartment("中正區"));
		
		//for(String key:section_map_firedepartment.keySet())
			//System.out.println(key+":"+section_map_firedepartment.get(key).toString());
		
	}
	
	
	public void hasContain(){
		
		for(FireDepartment f:section_map_firedepartment.get("中正區")){
			
			System.out.println(f.getName());
			double lat=f.getLat().doubleValue();
			double lng=f.getLng().doubleValue();
			
			for(Integer id:section_map_villageID.get("中正區")){
				if(villageID_map_polygon.get(id).contains(lat,lng))
					System.out.println(id);
			}

		}
			
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calculate calculate = new Calculate();
		
		
		//calculate.init_FireDepartment();
		calculate.init_VillagePolygon();
		calculate.init_FireDepartment();
		calculate.hasContain();
	}

}
