package model;


import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import data.*;

public class CalculateNEW extends CalculateConfig{
	
	public class Latlngs{
		
		double[] lats,lngs;
		
		public Latlngs(double[] lats,double[] lngs){
			this.lats=lats;
			this.lngs=lngs;
		}
		
		public double[] getLat(){ return this.lats;}
		public double[] getLng(){ return this.lngs;}

	}

	
	//(villagID => polygon)
	HashMap<String, Latlngs> villageID_map_latlng = new HashMap<String, Latlngs>();
	
	// (villagID => score)
	HashMap<String, Float> villageID_map_score = new HashMap<String, Float>();
	
	// (inputID => input)
	HashMap<String, Input> inputID_map_input = new HashMap<String, Input>();
	
	
	//MapReduce mapping後Key,Value ( villageID => Set(inputID) )
	HashMap<String,  HashSet<String>> villageID_map_inputID = new HashMap<String,  HashSet<String>>();

	
	public CalculateNEW() throws IOException{
		
		
		
		readVillage("village.txt");
		readInput("input.txt");
		
		//計算
		calculateInput();
		
		
		//print mapping
		System.out.println("\n============Mapping===================");
		for(String villageID:villageID_map_inputID.keySet())
			System.out.println(villageID+" => "+villageID_map_inputID.get(villageID));
		
		
		//原始分數
		System.out.println("\n============Score===================");
		printScore();
		
		
		//正規化後
		System.out.println("\n============Normalize========================");
		normalizeScore();
		
		
		
	
	}
	
	
	public double[] StringToArray(String str){
		
		String[] items = str.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
		double[] results = new double[items.length];
		int i=0;
		
		for(String item:items){
			results[i]=Double.parseDouble(item.trim());
			i++;
		}
		
		return results;
	}
	
	
	public void readVillage(String path) throws IOException{
		
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		while (br.ready()) {
			
			String[] subString=br.readLine().split(" ");
			String id = subString[0];
			String section = subString[1];
			double[] lats = StringToArray(subString[2]);
			double[] lngs = StringToArray(subString[3]);
			
			villageID_map_latlng.put(id, new Latlngs(lats, lngs));

		}
		
		fr.close();

	}
	
	
	public void readInput(String path) throws IOException{
		
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		while (br.ready()) {
			
			String[] subString=br.readLine().split(" ");
			String id = subString[0];
			String section = subString[1];
			double lat = Double.parseDouble(subString[2]);
			double lng = Double.parseDouble(subString[3]);
			
			inputID_map_input.put(id, new Input(id, section, lat, lng));
			
		}
		
		fr.close();
		
		
	}

	
	public void calculateInput(){
		
		for(String inputID:inputID_map_input.keySet()){
			Input input = inputID_map_input.get(inputID);
			for(String villageID:villageID_map_latlng.keySet()){
				
				Latlngs villageLatlng = villageID_map_latlng.get(villageID);
						
				if(isContain(input.getLat(), input.getLng(), villageLatlng)){
					
					if(inputID.contains("firedepartment"))
						firedepartment_part(villageID,inputID);
					
					else if(inputID.contains("illegal"))
						illegal_part(villageID,inputID);
					
					else if(inputID.contains("narrow"))
						narrow_part(villageID,inputID);
					
				}else{
					
					//double min_distance=centerDistance(input.getLat(), input.getLng(), villageLatlng);
					double min_distance=minBoundDistance(input.getLat(), input.getLng(), villageLatlng);
					
					if(inputID.contains("firedepartment") && min_distance <= FIRE_DEPARTMENT_LENGTH*LENGTH_TOLERANT)
						firedepartment_part(villageID,inputID);
					
					else if(inputID.contains("illegal") && min_distance <= ILLEGAL_CONSTRUCTION_LENGTH*LENGTH_TOLERANT)
						illegal_part(villageID,inputID);
					
					else if(inputID.contains("narrow") && min_distance <= NARROW_ROADWAY_LENGTH*LENGTH_TOLERANT)
						narrow_part(villageID,inputID);
					
				}
			}
		}
	}
	
	public void narrow_part(String villageID,String inputID){
		
		mapping(villageID,inputID);
		
		if(villageID_map_score.containsKey(villageID))
			villageID_map_score.put(villageID, villageID_map_score.get(villageID)+NARROW_ROADWAY_WEIGHT);
		else 
			villageID_map_score.put(villageID, (float) NARROW_ROADWAY_WEIGHT);
		
	}
	
	public void firedepartment_part(String villageID,String inputID){
		
		mapping(villageID,inputID);
		
		if(villageID_map_score.containsKey(villageID))
			villageID_map_score.put(villageID, villageID_map_score.get(villageID)+FIRE_DEPARTMENT_WEIGHT);
		else 
			villageID_map_score.put(villageID, (float) FIRE_DEPARTMENT_WEIGHT);
		
	}
	
	public void illegal_part(String villageID,String inputID){
		
		mapping(villageID,inputID);
		
		if(villageID_map_score.containsKey(villageID))
			villageID_map_score.put(villageID, villageID_map_score.get(villageID)+ILLEGAL_CONSTRUCTION_WEIGHT);
		else 
			villageID_map_score.put(villageID, (float) ILLEGAL_CONSTRUCTION_WEIGHT);
		
	}
	
	
	public void mapping(String villageID,String inputID){
		
		try{
			villageID_map_inputID.get(villageID).add(inputID);
			
		}catch(java.lang.NullPointerException n){
			villageID_map_inputID.put(villageID, new HashSet<String>(){{add(inputID);}});
			
		}
		
	}
	
	
	//檢查點是否落在polygon
	public boolean isContain(double inputLat,double inputLng,Latlngs polygon_latlngs){
		
		DoublePolygon polygon = new DoublePolygon(polygon_latlngs.getLat(), polygon_latlngs.getLng());
		
		if(polygon.contains(inputLat, inputLng))
			return true;
		else
			return false;
		
	}
	
	
	//計算點和polygon邊上所有點距離
	public double minBoundDistance(double inputLat,double inputLng,Latlngs polygon_latlngs){
		
		double distance=0.0;
		double min_distance=9999.0;
		int i=0;
		
		while (i<polygon_latlngs.getLat().length) {
			
			distance=Math.sqrt(Math.pow(polygon_latlngs.getLat()[i]-inputLat, 2)
					+Math.pow(polygon_latlngs.getLng()[i]-inputLng, 2));
			
			min_distance = Math.min(min_distance, distance);
			//System.out.println(min_distance);
			i++;
		}

		return min_distance;
		
	}
	
	//計算點和polygon重心距離
	public double centerDistance(double inputLat,double inputLng,Latlngs polygon_latlngs){
		
		DoublePolygon polygon = new DoublePolygon(polygon_latlngs.getLat(), polygon_latlngs.getLng());
		Point centerPoint=polygon.polygonCenterOfMass();
		double distance=0.0;

		distance=Math.sqrt(Math.pow(centerPoint.getX()-inputLat, 2)
				+Math.pow(centerPoint.getY()-inputLng, 2));
		
		return distance;
		
	}
	
	
	public void printScore(){
		for(String id:villageID_map_score.keySet())
			System.out.println("id: "+id+" score:"+ villageID_map_score.get(id));
		
		
	}
	
	
	public void normalizeScore(){
		
		float max=-1L;
		float min=-1L;
		int num=0;
		float mean=0;
		float sum=0;
		
		
		for(String id:villageID_map_score.keySet()){
			float current=villageID_map_score.get(id);
			
			//initialize max and min
			if(max == -1L && min == -1L){
				max=current;min=current;
			}
			max=Math.max(max, current);
			
			min=Math.min(min, current);
			
			sum+=current;
			num++;
		}
		
		mean=(float)sum/num;
		
		float standard_deviation=standardDeviation(villageID_map_score, mean, num);
		
		System.out.println("standard_deviation:"+standard_deviation);
		System.out.println("max: "+max+" min: "+min);
		
		
		for(String id:villageID_map_score.keySet()){
			float score = (villageID_map_score.get(id)-mean)/standard_deviation;
			System.out.println("id: "+id+" score:"+ score);	
		}
		

	}
	
	public float standardDeviation(HashMap<String, Float> list,float mean,int num){
		
		float standard_deviation=0;
		float sum=0;

		for(String id:villageID_map_score.keySet())
			sum+=Math.pow(villageID_map_score.get(id)-mean, 2);
		
		standard_deviation=(float) Math.sqrt(sum/num);
		
		return standard_deviation;
	}
	
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		CalculateNEW calculateNEW = new CalculateNEW();
		
	}

}