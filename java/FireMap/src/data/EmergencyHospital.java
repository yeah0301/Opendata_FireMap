package data;

import java.math.BigDecimal;

/**
 * 臺北市急救責任醫院.csv
 * 
 *
 */

public class EmergencyHospital {
	
	
	private String name;
	private String address_for_display ;
	private String address_for_system ;
	private String telephone;
	private String hospital_evaluation ;
	private String teaching_hospital_evaluation;
	private String category;
	private BigDecimal lat;
	private BigDecimal lng;
	private int id;
	
	
	/**
	 * initial
	 * 
	 * @param name 
	 * @param addr_for_display  
	 * @param addr_for_system
	 * @param telephone 
	 * @param hospital_evaluation
	 * @param tech_hospital_evaluation
	 * @param category
	 * @param lat
	 * @param lng
	 */
	
	public EmergencyHospital(String name,String addr_for_display,String addr_for_system,String telephone
			,String hospital_evaluation,String tech_hospital_evaluation,String category,BigDecimal lat,BigDecimal lng){
		
		this.name=name;
		this.address_for_display=addr_for_display;
		this.address_for_system=addr_for_system;
		this.telephone=telephone;
		this.hospital_evaluation=hospital_evaluation;
		this.teaching_hospital_evaluation=tech_hospital_evaluation;
		this.category=category;
		this.lat=lat;
		this.lng=lng;
	}
	
	
	public EmergencyHospital(int id,String name,String addr_for_display
			,String hospital_evaluation,String tech_hospital_evaluation,String category,BigDecimal lat,BigDecimal lng){
		
		this.id=id;
		this.name=name;
		this.address_for_display=addr_for_display;
		this.hospital_evaluation=hospital_evaluation;
		this.teaching_hospital_evaluation=tech_hospital_evaluation;
		this.category=category;
		this.lat=lat;
		this.lng=lng;

	}
	

	
	/**
	 * 
	 * @return name 醫院名稱
	 */
	public String getName(){
		return this.name;
	}
	
	
	public int getID(){
		return this.id;
	}
	
	
	public String getAddressForDisplay(){
		return this.address_for_display;
	}
	
	public String getAddressForSystem(){
		return this.address_for_system;
	}
	
	public String getTelephone(){
		return this.telephone;
	}
	
	public String getHospitalEvaluation(){
		return this.hospital_evaluation;
	}
	
	public String getTeachingHospitalEvaluation(){
		return this.teaching_hospital_evaluation;
	}
	
	public String getCategory(){
		return this.category;
	}
	
	
	public BigDecimal getLat(){
		return lat;
	}

	public BigDecimal getLng(){
		return lng;
	}

}
