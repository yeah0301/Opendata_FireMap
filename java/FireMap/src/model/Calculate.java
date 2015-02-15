package model;

import java.awt.Polygon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import data.*;
import utils.util;

public class Calculate extends CalculateConfig{
	
	String[] sections={"中正區","萬華區","大同區","中山區","松山區","大安區"
			,"信義區","內湖區","南港區","士林區","北投區","文山區"};
	
	//String[] sections={"中山區"};
	
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
	
	
	// ( villag_id ,   village)
	HashMap<Integer, Village> villageID_map_village = new HashMap<Integer, Village>();
	
	

	//( section , linklist(違建))
	HashMap<String, LinkedList<IllegalConstruction>> section_map_IllegalConstruction
	= new HashMap<String, LinkedList<IllegalConstruction>>();
	
	//( section , linklist(消防))
	HashMap<String, LinkedList<FireDepartment>> section_map_firedepartment
	= new HashMap<String, LinkedList<FireDepartment>>();
	
	//( section , linklist(醫院))
	HashMap<String, LinkedList<EmergencyHospital>> section_map_EmergencyHospital
	= new HashMap<String, LinkedList<EmergencyHospital>>();
	
	//( section , linklist(搶救困難地區))
	HashMap<String, LinkedList<LevelDifficultyOfFireRescue>> section_map_LevelDifficultyOfFireRescue
	= new HashMap<String, LinkedList<LevelDifficultyOfFireRescue>>();
	
	
	//( section , linklist(狹窄巷道))
	HashMap<String, LinkedList<NarrowRoadway>> section_map_NarrowRoadway
	= new HashMap<String, LinkedList<NarrowRoadway>>();
	
	
	//( section , linklist(重大不合格地區))
	HashMap<String, LinkedList<SeriousFailureLocation>> section_map_SeriousFailureLocation
	= new HashMap<String, LinkedList<SeriousFailureLocation>>();
	
	
	//( section , linklist(地區火災發生次數))
	HashMap<String, LinkedList<FireCount>> section_map_FireCount
	= new HashMap<String, LinkedList<FireCount>>();
	
	LinkedList<Hydrant> hydrants=new LinkedList<Hydrant>();

	
	public Calculate(){
		sqlServer = new SQLServer();
	}
	
	
	public void init_VillagePolygon(){
		
		for(String section:sections){
			
			LinkedList<Village> villages=sqlServer.select_Village(section);
			HashSet<Integer> idSet = new HashSet<Integer>();
			
			for(Village v:villages){
				
				int id=v.getID();
				double[] lats=util.arraylist_to_array(v.getlats());
				double[] lngs=util.arraylist_to_array(v.getlngs());
				
				idSet.add(id);
				villageID_map_polygon.put(id, new DoublePolygon(lats,lngs));
				villageID_map_village.put(id, v);
			}
			
			section_map_village.put(section, villages);
			section_map_villageID.put(section, idSet);
			

		}
		
		for(String key:section_map_village.keySet())
			System.out.println(key+" : "+section_map_village.get(key).toString());
			

	}
	
	
	
	public void init_IllegalConstruction(){
		
		for(String section:sections)
			section_map_IllegalConstruction.put(section, sqlServer.select_IllegalConstruction(section));

	}
	

	public void init_FireDepartment(){
		
		for(String section:sections)
			section_map_firedepartment.put(section, sqlServer.select_FireDepartment(section));
		
	}
	
	public void init_EmergencyHospital(){
		
		for(String section:sections)
			section_map_EmergencyHospital.put(section, sqlServer.select_EmergencyHospital(section));

	}
	
	public void init_LevelDifficultyOfFireRescue(){
		
		for(String section:sections)
			section_map_LevelDifficultyOfFireRescue.put(section, sqlServer.select_LevelDifficultyOfFireRescue(section));
		
	}
	
	public void init_NarrowRoadway(){
		
		for(String section:sections)
			section_map_NarrowRoadway.put(section, sqlServer.select_NarrowRoadway(section));
		
	}
	
	public void init_SeriousFailureLocation(){
		
		for(String section:sections)
			section_map_SeriousFailureLocation.put(section, sqlServer.select_SeriousFailureLocation(section));
		
	}
	
	
	public void init_FireCount(){
		
		for(String section:sections)
			section_map_FireCount.put(section, sqlServer.select_FireCount(section));
		
	}
	
	
	public void init_Hydrant(){
		
		hydrants = sqlServer.select_Hydrant();
		
		System.out.println("Hydrant list length: "+hydrants.size());
		
	}
	
	
	public void hasContain(){
		
		init_VillagePolygon();
		
		init_FireDepartment();
		init_EmergencyHospital();
		init_LevelDifficultyOfFireRescue();
		init_NarrowRoadway();
		init_SeriousFailureLocation();
		init_IllegalConstruction();
		init_FireCount();
		init_Hydrant();
		
		for(String section:sections){
		
			FireDepartment_calculate(section);
			EmergencyHospital_calculate(section);
			LevelDifficultyOfFireRescue_calculate(section);
			NarrowRoadway_calculate(section);
			SeriousFailureLocation_calculate(section);
			IllegalConstruction_calculate(section);
			FireCount_calculate(section);
		}	
		
		Hydrant_calculate();
		
	}
	
	public void FireDepartment_calculate(String section){
		
		for(FireDepartment f:section_map_firedepartment.get(section)){
			
			double lat=f.getLat().doubleValue();
			double lng=f.getLng().doubleValue();
			//System.out.println(f.getName()+" latlng: "+lat+","+lng);
		
			for(Integer id:section_map_villageID.get(section)){
				if(villageID_map_polygon.get(id).contains(lat,lng)){
					
					if(villageID_map_score.containsKey(id))
						villageID_map_score.put(id, villageID_map_score.get(id)+FIRE_DEPARTMENT_WEIGHT);
					else 
						villageID_map_score.put(id, (float) FIRE_DEPARTMENT_WEIGHT);

				}else 
					continue;
			}
		}
		
	}
	
	
	public void EmergencyHospital_calculate(String section){
		
		for(EmergencyHospital hospital:section_map_EmergencyHospital.get(section)){
			
			//System.out.println(hospital.getName());
			double lat=hospital.getLat().doubleValue();
			double lng=hospital.getLng().doubleValue();
		
			for(Integer id:section_map_villageID.get(section)){
				if(villageID_map_polygon.get(id).contains(lat,lng)){
				
					String category=hospital.getCategory();
					if(category.equals("醫學中心")){
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+MEDICAL_CENTER_WEIGHT);
						else 
							villageID_map_score.put(id, (float) MEDICAL_CENTER_WEIGHT);
						
					}else if(category.equals("區域醫院")){
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+REGION_HOSPITAL_WEIGHT);
						else 
							villageID_map_score.put(id, (float) REGION_HOSPITAL_WEIGHT);
						
						
					}else if(category.equals("地區醫院")){
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+lOCAL_HOSPITAL_WEIGHT);
						else 
							villageID_map_score.put(id, (float) lOCAL_HOSPITAL_WEIGHT);
						
					}else 
						System.err.println("hospital category error!");
					
				}else 
					continue;
			}
		}
		
	}
	
	public void LevelDifficultyOfFireRescue_calculate(String section){
		
		for(LevelDifficultyOfFireRescue diff:section_map_LevelDifficultyOfFireRescue.get(section)){
			
			double lat=diff.getLat().doubleValue();
			double lng=diff.getLng().doubleValue();
			//System.out.println(diff.getName()+" latlng: "+lat+","+lng);
		
			for(Integer id:section_map_villageID.get(section)){
				if(villageID_map_polygon.get(id).contains(lat,lng)){
					
					int level = diff.getLevel();
					boolean hasAisle = diff.getHasAisle();
					
					if(level == 1 && hasAisle){
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+LEVEL_ONE_RESCUE_HAS_AISLE_WEIGHT);
						else 
							villageID_map_score.put(id, (float) LEVEL_ONE_RESCUE_HAS_AISLE_WEIGHT);
						
						
					}else if(level == 1 && !hasAisle){	
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+LEVEL_ONE_RESCUE_NOT_AISLE_WEIGHT);
						else 
							villageID_map_score.put(id, (float) LEVEL_ONE_RESCUE_NOT_AISLE_WEIGHT);
						

					}else if(level == 2 && hasAisle){
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+LEVEL_TWO_RESCUE_HAS_AISLE_WEIGHT);
						else 
							villageID_map_score.put(id, (float) LEVEL_TWO_RESCUE_HAS_AISLE_WEIGHT);
						
						
					}else if(level == 2 && !hasAisle){
						
						if(villageID_map_score.containsKey(id))
							villageID_map_score.put(id, villageID_map_score.get(id)+LEVEL_TWO_RESCUE_NOT_AISLE_WEIGHT);
						else 
							villageID_map_score.put(id, (float) LEVEL_TWO_RESCUE_NOT_AISLE_WEIGHT);
						
					}else 
						System.err.println("LevelDifficultyOfFireRescue level error!");
					
				}else 
					continue;
			}
		}
		
	}
	
	
	public void SeriousFailureLocation_calculate(String section){
		
		for(SeriousFailureLocation failure:section_map_SeriousFailureLocation.get(section)){
			
			double lat=failure.getLat().doubleValue();
			double lng=failure.getLng().doubleValue();
			//System.out.println(failure.getName()+" latlng: "+lat+","+lng);
		
			for(Integer id:section_map_villageID.get(section)){
				if(villageID_map_polygon.get(id).contains(lat,lng)){
					
					if(villageID_map_score.containsKey(id))
						villageID_map_score.put(id, villageID_map_score.get(id)+SERIOUS_FAILURE_LOCATION_WEIGHT);
					else 
						villageID_map_score.put(id, (float) SERIOUS_FAILURE_LOCATION_WEIGHT);
					
				}else 
					continue;
			}
		}
		
	}
	
	
	public void FireCount_calculate(String section){
		
		for(FireCount firecount:section_map_FireCount.get(section)){
			
			int count = firecount.getCount();
			
			for(Integer id:section_map_villageID.get(section)){
				
				if(villageID_map_score.containsKey(id))
					villageID_map_score.put(id, villageID_map_score.get(id)+(count*FIRE_COUNT_WEIGHT));
				else 
					villageID_map_score.put(id, count*FIRE_COUNT_WEIGHT);
			}
		}
	}
	
	
	public void NarrowRoadway_calculate(String section){
		
		for(NarrowRoadway na:section_map_NarrowRoadway.get(section)){
			
			double lat=na.getLat().doubleValue();
			double lng=na.getLng().doubleValue();
			//System.out.println(na.getRoadway()+" latlng: "+lat+","+lng);
		
			for(Integer id:section_map_villageID.get(section)){
				if(villageID_map_polygon.get(id).contains(lat,lng)){

					if(villageID_map_score.containsKey(id))
						villageID_map_score.put(id, villageID_map_score.get(id)+NARROW_ROADWAY_WEIGHT);
					else 
						villageID_map_score.put(id, (float) NARROW_ROADWAY_WEIGHT);
								
				}else 
					continue;
			}
		}
		
	}
	
	
	public void Hydrant_calculate(){
		
		for(Hydrant hydrant:hydrants){
			
			double lat=hydrant.getLat().doubleValue();
			double lng=hydrant.getLng().doubleValue();
			
			
			for(int id:villageID_map_polygon.keySet()){
				if(villageID_map_polygon.get(id).contains(lng, lat)){
					
					if(villageID_map_score.containsKey(id))
						villageID_map_score.put(id, villageID_map_score.get(id)+HYDRANT_WEIGHT);
					else 
						villageID_map_score.put(id, (float) HYDRANT_WEIGHT);
					
				}else 
					continue;
			}
		}
		
	}
	
	
	public void IllegalConstruction_calculate(String section){
		
		for(IllegalConstruction ill:section_map_IllegalConstruction.get(section)){
			
			double lat=ill.getLat().doubleValue();
			double lng=ill.getLng().doubleValue();
			//System.out.println(ill.getID()+" latlng: "+lat+","+lng);
		
			for(Integer id:section_map_villageID.get(section)){
				if(villageID_map_polygon.get(id).contains(lat,lng)){
					
					if(villageID_map_score.containsKey(id))
						villageID_map_score.put(id, villageID_map_score.get(id)+ILLEGAL_CONSTRUCTION_WEIGHT);
					else 
						villageID_map_score.put(id, (float) ILLEGAL_CONSTRUCTION_WEIGHT);
					
				}else 
					continue;
			}
		}
		
	}
	
	
	public void printScore(){
		for(int id:villageID_map_score.keySet())
			System.out.println("id: "+id+" score:"+ villageID_map_score.get(id));
	}
	
	
	public void normalizeScore(){
		
		float max=-1L;
		float min=-1L;
		int num=0;
		float mean=0;
		float sum=0;
		
		
		LinkedList<Result> results = new LinkedList<Result>();
		
		for(int id:villageID_map_score.keySet()){
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
		
		
		
		for(int id:villageID_map_score.keySet()){
			
			float score=(villageID_map_score.get(id)-min)/(max-min);
			//float score = (villageID_map_score.get(id)-mean)/standard_deviation;
			Village v=villageID_map_village.get(id);
			
			System.out.println("id: "+id+" score:"+ score);
			
			//results.add(new Result(id, v.getName(), v.getSection(), v.getJsonPolygon(),score));
			
		}
		
		//sqlServer.insert_Result(results);

	}
	
	
	public float standardDeviation(HashMap<Integer, Float> list,float mean,int num){
		
		float standard_deviation=0;
		float sum=0;
		
		
		for(int id:villageID_map_score.keySet())
			sum+=Math.pow(villageID_map_score.get(id)-mean, 2);
		
		standard_deviation=(float) Math.sqrt(sum/num);
		
		return standard_deviation;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calculate calculate = new Calculate();
		calculate.hasContain();
		calculate.printScore();
		calculate.normalizeScore();
	}

}
