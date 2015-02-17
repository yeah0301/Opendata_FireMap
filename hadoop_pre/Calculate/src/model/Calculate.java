package model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;




import data.*;


public class Calculate extends CalculateConfig{
	
	String[] sections={"中正區","萬華區","大同區","中山區","松山區","大安區"
			,"信義區","內湖區","南港區","士林區","北投區","文山區"};
	
	//(section => Set(villagID))
	HashMap<String, HashSet<String>> section_map_villageID = new HashMap<String, HashSet<String>>();
	
	//(villagID => polygon)
	HashMap<String, DoublePolygon> villageID_map_polygon = new HashMap<String, DoublePolygon>();
	
	// (villagID => score)
	HashMap<String, Float> villageID_map_score = new HashMap<String, Float>();
	
	// (inputID => input)
	HashMap<String, Input> inputID_map_input = new HashMap<String, Input>();
	
	//(section => Set(inputID))
	HashMap<String, HashSet<String>> section_map_inputID = new HashMap<String, HashSet<String>>();
	
	//MapReduce mapping後Key,Value ( villageID => Set(inputID) )
	HashMap<String,  HashSet<String>> villageID_map_inputID = new HashMap<String,  HashSet<String>>();

	
	public Calculate() throws IOException{
		
		
		System.out.println("\n============Village===================");
		readVillage("village.txt");
		
		
		System.out.println("\n============Input===================");
		readInput("input.txt");
		
		//計算
		for(String section:sections)
			calculateInput(section);
		
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
			
			
			villageID_map_polygon.put(id, new DoublePolygon(lats,lngs));
			
			try{
				section_map_villageID.get(section).add(id);
			}catch(java.lang.NullPointerException n){
				section_map_villageID.put(section, new HashSet<String>(){{add(id);}});
			}
			
		}
		
		fr.close();

		for(String key:section_map_villageID.keySet())	
			System.out.println(key+" => "+section_map_villageID.get(key));
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
			

			try{
				section_map_inputID.get(section).add(id);
			}catch(java.lang.NullPointerException n){
				section_map_inputID.put(section, new HashSet<String>(){{add(id);}});
			}
			
		}
		
		
		fr.close();
		
		for(String key:section_map_inputID.keySet())	
			System.out.println(key+" => "+section_map_inputID.get(key));
	}
	
	public void mapping(String villageID,String inputID){
		
		try{
			villageID_map_inputID.get(villageID).add(inputID);
			
		}catch(java.lang.NullPointerException n){
			villageID_map_inputID.put(villageID, new HashSet<String>(){{add(inputID);}});
			
		}
		
	}

	
	public void calculateInput(String section){
		
		for(String inputID :section_map_inputID.get(section)){
			
			Input input = inputID_map_input.get(inputID);
			
			for(String villageID:section_map_villageID.get(section)){
				
				if(villageID_map_polygon.get(villageID).contains(input.getLat(),input.getLng())){
					
					mapping(villageID,inputID);
					
					if(inputID.contains("firedepartment")){
						
						if(villageID_map_score.containsKey(villageID))
							villageID_map_score.put(villageID, villageID_map_score.get(villageID)+FIRE_DEPARTMENT_WEIGHT);
						else 
							villageID_map_score.put(villageID, (float) FIRE_DEPARTMENT_WEIGHT);
						
					}
					
					else if(inputID.contains("illegal")){
						
						if(villageID_map_score.containsKey(villageID))
							villageID_map_score.put(villageID, villageID_map_score.get(villageID)+ILLEGAL_CONSTRUCTION_WEIGHT);
						else 
							villageID_map_score.put(villageID, (float) ILLEGAL_CONSTRUCTION_WEIGHT);
						
					}
					
					else if(inputID.contains("narrow")){
						
						if(villageID_map_score.containsKey(villageID))
							villageID_map_score.put(villageID, villageID_map_score.get(villageID)+NARROW_ROADWAY_WEIGHT);
						else 
							villageID_map_score.put(villageID, (float) NARROW_ROADWAY_WEIGHT);
						
					}
					
					

				}else 
					continue;
			}
		}
		
		
		
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
		
		Calculate calculate = new Calculate();
		
	}

}