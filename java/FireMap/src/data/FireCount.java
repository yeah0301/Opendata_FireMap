package data;

import java.util.Date;

/**
 * 臺北市火災次數分類及時間.xls
 * 
 *
 */

public class FireCount {

	private String section;
	private int count;
	private Date date;
	private int id;

	
	/**
	 * initial 
	 * 
	 * @param section 發生火災地區
	 * @param count 發生火災次數
	 * @param date 發生火災月份
	 */
	public FireCount(String section, int count, Date date) {
		this.section = section;
		this.count = count;
		this.date = date;
	}
	
	public FireCount(int id,String section, int count, Date date) {
		this.id=id;
		this.section = section;
		this.count = count;
		this.date = date;
	}

	/**
	 * 
	 * @return section 發生火災地區
	 */
	public String getSection() {
		return this.section;
	}

	/**
	 * 
	 * @return count count 發生火災次數
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * 
	 * @return date 發生火災月份
	 */

	public Date getDate() {
		return this.date;
	}

}
