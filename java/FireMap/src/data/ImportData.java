package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import utils.ReadXMLFileUsingSaxparser;
import utils.util;

public class ImportData {

	SQLServer sql;

	public ImportData() {

	}

	/**
	 * Capture 防災公園緊急維生取水站.csv and insert SQL Server
	 * 
	 */

	public void load_ParkWaterStation(String path) {
		BufferedReader br = null;
		FileInputStream fin = null;
		List<ParkWaterStation> list = new LinkedList<ParkWaterStation>();

		try {
			fin = new FileInputStream(new File(path));
			br = new BufferedReader(new InputStreamReader(fin, "BIG5"));

			while (br.ready()) {
				String line = br.readLine();
				System.out.println(line);
				if (!line.contains("name")) {
					String temp[] = line.split(",");
					String sec = temp[0];
					String name = temp[1];
					String addr = temp[2];
					String note = temp[3];
					LatLng latLng = queryAddressToLatlng("台北市 " + name);

					list.add(new ParkWaterStation(sec, name, addr, note, latLng
							.getLat(), latLng.getLng()));

				} else
					continue;

				System.out.flush();
			}
			
			sql = new SQLServer();
			//sql.insert_ParkWaterStation(list);
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * Capture 臺北市政府消防局各分隊座標位置.csv and insert SQL Server
	 * 
	 */
	public void load_FireDepartment(String path) {
		BufferedReader br = null;
		FileInputStream fin = null;
		List<FireDepartment> list = new LinkedList<FireDepartment>();

		try {
			fin = new FileInputStream(new File(path));
			br = new BufferedReader(new InputStreamReader(fin, "BIG5"));

			while (br.ready()) {
				String line = br.readLine();
				System.out.println(line);
				if (!line.contains("name")) {
					String temp[] = line.split(",");
					String name = temp[0];
					float TWD67_X = Float.parseFloat(temp[1]);
					float TWD67_Y = Float.parseFloat(temp[2]);

					String latlng[] = TWD67_To_WGS84(TWD67_X, TWD67_Y).split(
							",");
					BigDecimal lat = new BigDecimal(latlng[1]);
					BigDecimal lng = new BigDecimal(latlng[0]);

					System.out.println(lat + "," + lng);

					list.add(new FireDepartment(name, TWD67_X, TWD67_X, lat,
							lng, queryLatlngToAddress(lat, lng)));
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else
					continue;

				System.out.flush();
			}

			sql.insert_FireDepartment(list);
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * Capture 屋頂違建隔出3個使用單元以上.csv and insert SQL Server
	 * 
	 */

	public void load_IllegalConstructionTable(String path) {

		BufferedReader br = null;
		FileInputStream fin = null;
		List<IllegalConstruction> list = new LinkedList<IllegalConstruction>();

		try {
			fin = new FileInputStream(new File(path));
			br = new BufferedReader(new InputStreamReader(fin, "BIG5"));

			while (br.ready()) {
				String line = br.readLine();
				System.out.println(line);
				if (!line.contains("no")) {
					String temp[] = line.split(",");
					int yy = Integer.parseInt(temp[1]);
					yy += 1911;
					String mm = temp[2];
					String dd = temp[3];
					String sec = temp[4];
					String addr = temp[5];
					float area = Float.parseFloat(temp[6]);

					String query_addr = addr.replaceAll("(.*號).*", "台北市$1");
					// System.out.println(query_addr);

					LatLng latLng = queryAddressToLatlng(query_addr);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					new util();
					list.add(new IllegalConstruction(sec, addr, area, util
							.strToDatetime(yy + "-" + mm + "-" + dd
									+ " 00:00:00"), latLng.getLat(), latLng
							.getLng()));

				} else
					continue;

				System.out.flush();
			}

			sql.insert_IllegalConstruction(list);
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sql.close();
		}

	}

	/**
	 * Capture 臺北市搶救不易狹小巷道清冊.csv and insert SQL Server
	 * 
	 */
	public void load_NarrowRoadwayTable(String path) {

		FileInputStream file = null;
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		List<NarrowRoadway> list = new LinkedList<NarrowRoadway>();
		int currentSheet = 0;

		try {
			file = new FileInputStream(new File(path));
			workbook = new HSSFWorkbook(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (currentSheet < 3) {
			sheet = workbook.getSheetAt(currentSheet);
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (!row.getCell(0).toString().contains("編號")
						&& !row.getCell(0).toString().contains("臺北市搶救不易")) {
					String sec = row.getCell(1).getStringCellValue();
					String team = row.getCell(2).getStringCellValue();
					String roadway = row.getCell(3).getStringCellValue();
					float width = Float.parseFloat(row.getCell(4).toString());
					String str = null;
					if(currentSheet==2)
						 str = row.getCell(7).getStringCellValue();
					else
						str = row.getCell(6).getStringCellValue();
					
					str = str.replaceAll("paths:(.|\n)*\\],(.|\n)*\\[(.*)",
							"$3");
					str = str.replaceAll("\\]\\]\\}\\);", "");

					String polygon = "[";

					String[] splitString = str.split(",\n");

					for (int i = 0; i < splitString.length; i++) {
						if (i == splitString.length - 1)
							polygon += splitString[i].replaceAll(
									"new google.maps.LatLng\\((.*), (.*)\\)",
									"{\"lat\":$1,\"lng\":$2}]");
						else
							polygon += splitString[i].replaceAll(
									"new google.maps.LatLng\\((.*), (.*)\\)",
									"{\"lat\":$1,\"lng\":$2},");

					}

					System.out.println(polygon);
					System.out.println("============================");

					LatLng latLng = queryAddressToLatlng("台北市 " + roadway);

					String level = null;

					if (currentSheet == 0)
						level = "red";
					else if (currentSheet == 1)
						level = "yellow";
					else if (currentSheet == 2)
						level = "blue";

					list.add(new NarrowRoadway(sec, team, roadway, width,
							level, latLng.getLat(), latLng.getLng(), polygon));

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else
					continue;
			}
			currentSheet++;
		}

		sql = new SQLServer();
		sql.insert_NarrowRoadway(list);
		sql.close();

		try {
			workbook.close();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Capture 重大不合格場所 and insert SQL Server
	 * 
	 * URL:http://trans.nfa.gov.tw/nfaweb/danger/%E5%8F%B0%E5%8C%97%E5%B8%82%E5%
	 * 9C%B0%E5%8D%80.htm
	 * 
	 */
	public void load_SeriousFailureLocation() {
		String URL = "http://trans.nfa.gov.tw/nfaweb/danger/%E5%8F%B0%E5%8C%97%E5%B8%82%E5%9C%B0%E5%8D%80.htm";
		Document doc = null;
		List<SeriousFailureLocation> list = new LinkedList<SeriousFailureLocation>();

		try {
			doc = Jsoup.connect(URL).timeout(1000).get();

			for (org.jsoup.nodes.Element element : doc.select("td.text")
					.select("tr")) {
				if (!element.hasAttr("background")) {

					String name = element.child(0).text();

					if (!name.equals("以下空白")) {

						String addr = element.child(1).text();
						String check = element.child(2).text();
						String dateString = element.child(3).text();
						LatLng latLng = queryAddressToLatlng(addr);

						int yy = Integer.parseInt((String) dateString
								.subSequence(0, 3));
						yy += 1911;

						new util();
						java.util.Date date = util.strToDatetime(yy + "-"
								+ dateString.substring(3, 5) + "-"
								+ dateString.substring(5, 7) + " 00:00:00");

						list.add(new SeriousFailureLocation(name, addr, check,
								date, latLng.getLat(), latLng.getLng()));
					} else
						continue;
				} else
					continue;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql.insert_SeriousFailureLocation(list);
		sql.close();

	}

	/**
	 * Capture 一、二級火災搶救困難地區.xls and insert SQL Server
	 * 
	 */

	public void load_LevelDifficultyOfFireRescue(String path) {

		FileInputStream file = null;
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		List<LevelDifficultyOfFireRescue> list = new LinkedList<LevelDifficultyOfFireRescue>();

		try {
			file = new FileInputStream(new File(path));

			// Get the workbook instance for XLS file
			workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			sheet = workbook.getSheetAt(0);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int maxRow = sheet.getLastRowNum();

		for (int i = 18; i <= maxRow; i++) {

			Row row = sheet.getRow(i);
			int level = (int) row.getCell(1).getNumericCellValue();
			int item = 0;

			switch (row.getCell(2).getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				item = (int) row.getCell(2).getNumericCellValue();
				break;
			case Cell.CELL_TYPE_STRING:
				item = Integer.parseInt(row.getCell(2).getStringCellValue());
				break;
			}

			String sec = row.getCell(3).toString();
			String addr = row.getCell(4).toString();
			String name = row.getCell(5).toString();

			String aisle = null;
			boolean hasAisle = false;

			try {
				aisle = row.getCell(6).toString();

				if (aisle.equals("有"))
					hasAisle = true;
				else
					hasAisle = false;

			} catch (NullPointerException n) {
				hasAisle = false;
			}

			System.out.println(level + " " + item + " " + sec + " " + addr
					+ " " + name + " " + hasAisle);

			LatLng latLng = queryAddressToLatlng("台北市 " + addr);

			list.add(new LevelDifficultyOfFireRescue(level, item, sec, addr,
					name, hasAisle, latLng.getLat(), latLng.getLng()));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		sql.insert_LevelDifficultyOfFireRescue(list);
		sql.close();

		try {
			workbook.close();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Capture 臺北市火災次數分類及時間.xls and insert SQL Server
	 * 
	 * URL:http://www.tfd.gov.tw/upload/time/1419824794_1.xls
	 * 
	 */
	public void load_FireCount() {

		InputStream file = null;
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		URL url = null;
		List<FireCount> list = new LinkedList<FireCount>();

		try {

			url = new URL("http://www.tfd.gov.tw/upload/time/1422234440_1.xls");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.connect();
			file = con.getInputStream();

			// Get the workbook instance for XLS file
			workbook = new HSSFWorkbook(file);

			int numberOfSheets = workbook.getNumberOfSheets();

			// 1 month to 12 month
			for (int i = 1; i <= numberOfSheets - 1; i++) {
				// Get first sheet from the workbook
				sheet = workbook.getSheetAt(i - 1);

				for (int j = 7; j < 19; j++) {
					Row row = sheet.getRow(j);
					String sec = row.getCell(0).getStringCellValue();
					int count = (int) row.getCell(1).getNumericCellValue();
					System.out.println(sec + " " + count);

					new util();
					Date date = null;

					if (i < 10)
						date = util
								.strToDatetime("2014-0" + i + "-15 00:00:00");
					else
						date = util.strToDatetime("2014-" + i + "-15 00:00:00");

					list.add(new FireCount(sec, count, date));
				}
			}

			sql.insert_FireCount(list);

			sql.close();
			workbook.close();
			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Capture 臺北市急救責任醫院.xls and insert SQL Server
	 * 
	 * @param path
	 *            檔案路徑
	 * 
	 */
	public void load_EmergencyHospital(String path) {

		BufferedReader br = null;
		FileInputStream fin = null;

		List<EmergencyHospital> list = new LinkedList<EmergencyHospital>();

		try {
			fin = new FileInputStream(new File(path));
			br = new BufferedReader(new InputStreamReader(fin, "BIG5"));

			while (br.ready()) {
				String line = br.readLine();
				System.out.println(line);

				if (!line.contains("name")) {
					String temp[] = line.split(",");
					String name = temp[0];
					String addr_dis = temp[1];
					String addr_sys = temp[2];
					String telephone = temp[3];
					String hospital_eval = temp[4];
					String tech_hospital_eval = temp[5];
					String category = temp[6];
					LatLng latLng = queryAddressToLatlng(addr_sys);

					list.add(new EmergencyHospital(name, addr_dis, addr_sys,
							telephone, hospital_eval, tech_hospital_eval,
							category, latLng.getLat(), latLng.getLng()));
				} else
					continue;

				System.out.flush();
			}

			sql.insert_EmergencyHospital(list);
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// sql.close();
		}

	}

	/**
	 * Capture 臺北市里界圖.kml and insert SQL Server
	 * 
	 * @param path
	 *            檔案路徑
	 * 
	 * 
	 */
	public void load_Village(String path) {

		org.dom4j.Document document = null;
		SAXReader reader = new SAXReader();
		LinkedList<Village> list = new LinkedList<Village>();

		try {
			document = reader.read(new File(path));

			for (Element element : (List<Element>) document
					.selectNodes("kml/Document/Placemark")) {

				String name = element.selectSingleNode("name").getText();
				String description = element.selectSingleNode("description")
						.getText();
				String section = description.replaceAll(".*區名</td><td>(.*)區.*",
						"$1區");
				String area = description.replaceAll(
						".*面積</td><td>([\\d|.]*).*", "$1");
				LinkedList<HashMap<String, BigDecimal>> poly = new LinkedList<HashMap<String, BigDecimal>>();

				System.out.println(area);
				String coordinates = element.selectSingleNode(
						"Polygon/outerBoundaryIs/LinearRing/coordinates")
						.getText();

				coordinates = coordinates.replaceAll("\\s*", "");
				for (String latlng : coordinates.split(",0")) {

					String[] temp = latlng.split(",");

					HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

					map.put("lat", new BigDecimal(temp[1]));
					map.put("lng", new BigDecimal(temp[0]));

					poly.add(map);
				}

				list.add(new Village(name, section, area, poly));

				System.out.println("============================");

			}
			sql.insert_Village(list);
			sql.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void load_Hydrant(String path) {

		SAXParserFactory spfac = SAXParserFactory.newInstance();
		ReadXMLFileUsingSaxparser handler = new ReadXMLFileUsingSaxparser();
		SAXParser sp;

		try {
			sp = spfac.newSAXParser();

			sp.parse(new File(path), handler);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		handler.readList();

		sql = new SQLServer();

		sql.insert_Hydrant(handler.getHydrantList());

	}

	public String TWD67_To_WGS84(float TWD67_X, float TWD67_Y) {

		String urlString = "http://map.happyman.idv.tw/~happyman/index/proj.php?go=1&api=0&q=1&x="
				+ TWD67_X + "&y=" + TWD67_Y;

		Document document = null;
		try {
			document = Jsoup.connect(urlString).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document.select("b").get(0).text();

	}

	public String queryLatlngToAddress(BigDecimal lat, BigDecimal lng) {

		Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = null;
		GeocodeResponse geocoderResponse = null;

		geocoderRequest = new GeocoderRequestBuilder()
				.setLocation(new LatLng(lat, lng)).setLanguage("zh-TW")
				.getGeocoderRequest();

		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = null;
		try {
			address = geocoderResponse.getResults().get(0)
					.getFormattedAddress();
			System.out.println(address);
			return address;

		} catch (java.lang.IndexOutOfBoundsException e) {
			System.out.println("address not find");
			;
			return null;
		}
	}

	private LatLng queryAddressToLatlng(String address) {

		Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = null;
		GeocodeResponse geocoderResponse = null;
		LatLng latLng = new LatLng("0", "0");

		BigDecimal lat = null;
		BigDecimal lng = null;

		geocoderRequest = new GeocoderRequestBuilder().setAddress(address)
				.setLanguage("en").getGeocoderRequest();

		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (geocoderResponse.getStatus().value().equals("OK")) {
			for (GeocoderResult result : geocoderResponse.getResults()) {
				lat = result.getGeometry().getLocation().getLat();
				lng = result.getGeometry().getLocation().getLng();
				System.out.println(lat + "," + lng);
				return result.getGeometry().getLocation();
			}
		} else
			return latLng;

		return latLng;
	}

	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub

		ImportData importData = new ImportData();

		importData.load_ParkWaterStation("D:\\消防\\防災公園緊急維生取水站.csv");//完成上半部

		// -------------------------finish---------------------------------------------

		// importData.load_IllegalConstructionTable("D:\\消防\\「屋頂違建隔出3個使用單元以上」清冊.csv");
		// importData.load_EmergencyHospital("D:\\消防\\臺北市急救責任醫院.csv");
		// importData.load_Village("D:\\消防\\臺北市里界圖.xml");
		// importData.load_FireDepartment("D:\\消防\\臺北市政府消防局各分隊座標位置.csv");
		// importData.load_LevelDifficultyOfFireRescue("D:\\消防\\一、二級火災搶救困難地區.xls");
		//importData.load_NarrowRoadwayTable("D:\\消防\\臺北市搶救不易狹小巷道清冊(改).xls");
		// importData.load_SeriousFailureLocation();//重大不安全場所
		// importData.load_FireCount();//臺北市火災次數分類及時間

		// -------------------------bug--------------------------------------------

		// Cannot insert the value NULL into column 'lat', table
		// 'dmlab.dbo.Hydrant'; column does not allow nulls. INSERT fails.
		// importData.load_Hydrant("D:\\消防\\大臺北地區消防栓分布點位圖.kml");
	}

}
