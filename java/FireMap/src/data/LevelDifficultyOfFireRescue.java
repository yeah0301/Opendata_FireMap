package data;

import java.math.BigDecimal;


/**
 * 一、二級火災搶救困難地區.xls
 * 
 * 
 *
 */

public class LevelDifficultyOfFireRescue {

	private int level;
	private int item;
	private String section;
	private String address;
	private String name;
	private boolean hasAisle;
	private BigDecimal lat;
	private BigDecimal lng;
	
	/**
	 * initial
	 * 
	 * @param level 評分等級
	 * @param item 認定項目
	 * @param sec 行政區
	 * @param addr 地址
	 * @param name 場所名稱
	 * @param hasAisle 消防通道
	 */
	
	public LevelDifficultyOfFireRescue(int level, int item, String sec,
			String addr, String name, boolean hasAisle,BigDecimal lat,BigDecimal lng) {
		
		this.level = level;
		this.item = item;
		this.section = sec;
		this.address = addr;
		this.name = name;
		this.hasAisle = hasAisle;
		this.lat=lat;
		this.lng=lng;

	}

	public int getLevel() {
		return this.level;
	}

	public int getItem() {
		return this.item;
	}

	public String getSection() {
		return this.section;
	}

	public String getAddress() {
		return this.address;
	}

	public String getName() {
		return this.name;
	}

	public boolean getHasAisle() {
		return this.hasAisle;
	}

	public String toString() {
		return "Level:" + level + " item:" + item + " section:" + section
				+ " address:" + address + " name:" + name + " hasAisle:"
				+ hasAisle;
	}
	
	public BigDecimal getLat(){
		return lat;
	}

	public BigDecimal getLng(){
		return lng;
	}
}
