package nchu.dmlab.firemap;

public class CalculateConfig {
	
	protected final static float LENGTH_TOLERANT=(float) 1.05;//容錯率
	
	protected final static int MEDICAL_CENTER_WEIGHT=4;
	
	protected final static int REGION_HOSPITAL_WEIGHT=3;
	
	protected final static int lOCAL_HOSPITAL_WEIGHT=2;
	
	protected final static int FIRE_DEPARTMENT_WEIGHT=5;
	
	protected final static float FIRE_DEPARTMENT_LENGTH=(float) 0.01;//1km
	
	protected final static float FIRE_COUNT_WEIGHT=(float) -0.5;
	
	protected final static int SERIOUS_FAILURE_LOCATION_WEIGHT=-5;
	
	protected final static int LEVEL_ONE_RESCUE_NOT_AISLE_WEIGHT=-4;
	
	protected final static int LEVEL_ONE_RESCUE_HAS_AISLE_WEIGHT=-3;
	
	protected final static int LEVEL_TWO_RESCUE_NOT_AISLE_WEIGHT=-5;
	
	protected final static int LEVEL_TWO_RESCUE_HAS_AISLE_WEIGHT=-4;
	
	protected final static int ILLEGAL_CONSTRUCTION_WEIGHT=-4;
	
	protected final static float ILLEGAL_CONSTRUCTION_LENGTH=(float) 0.0005;
	
	protected final static int NARROW_ROADWAY_WEIGHT=-5;
	
	protected final static float NARROW_ROADWAY_LENGTH=(float) 0.0005;
	
	protected final static float HYDRANT_WEIGHT=(float) 0.3;
	
	protected final static int PARK_WATER_STATION_WEIGHT=2;

}