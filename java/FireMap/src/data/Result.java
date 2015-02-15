package data;

public class Result {
	
	private int id;
	private String name;
	private String section;
	private String json_polygon;
	private float score;
	
	public Result(int id,String name,String section,String json_polygon,float score){
		this.id=id;
		this.name=name;
		this.section=section;
		this.json_polygon=json_polygon;
		this.score=score;
		
	}
	
	public float getScore(){
		return this.score;
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
	
	public String getJson_polygon(){
		return this.json_polygon;
	}
}

