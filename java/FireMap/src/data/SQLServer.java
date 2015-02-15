package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.microsoft.sqlserver.jdbc.*;

public class SQLServer {

	String connectionString;

	// The types for the following variables are
	// defined in the java.sql library.
	Connection connection = null; // For making the connection

	Statement statement = null; // For the SQL statement
	ResultSet resultSet = null; // For the result set, if applicable
	PreparedStatement pst = null;

	public SQLServer() {
		connectionString = "jdbc:sqlserver://" + SQLServer_INFO.serverIP + ";"
				+ "database=" + SQLServer_INFO.databaseName + ";" + "user="
				+ SQLServer_INFO.userID + ";" + "password="
				+ SQLServer_INFO.password + ";" + "encrypt=true;"
				+ "hostNameInCertificate=*.database.windows.net;"
				+ "loginTimeout=30";

		try {
			// Ensure the SQL Server driver class is available.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			// Establish the connection.
			connection = DriverManager.getConnection(connectionString);

			if (connection != null && !connection.isClosed()) {
				System.out.println("資料庫連線成功！");
			}

		}

		// Exception handling
		catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void create_FireDepartment() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE FireDepartment("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , name  nvarchar(50) NOT NULL"
					+ "  , TWD67_X  float NOT NULL "
					+ "  , TWD67_Y  float NOT NULL "
					+ "  , lat decimal(10,7) NOT NULL"
					+ "  , lng decimal(10,7) NOT NULL"
					+ "  , address nvarchar(50))");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}
	
	public void create_mobile_record() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE mobile_record ("
					+"datetime datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+"pics varchar(64) NOT NULL,"
					+"x varchar(16) DEFAULT NULL,"
					+"y varchar(16) DEFAULT NULL,"
					+"worker varchar(64) DEFAULT NULL,"
					+"eventid varchar(16) DEFAULT NULL,"
					+"eventDesc varchar(255) DEFAULT NULL,"
					+"PRIMARY KEY (datetime))");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}
	
	public void create_audio_record() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE audio_record ("
					+"datetime datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+"worker varchar(64) NOT NULL,"
					+"mp3_path varchar(64) NOT NULL,"
					+"PRIMARY KEY (datetime,worker))");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}

	public void create_Hydrant() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE Hydrant("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , lat decimal(10,7) NOT NULL"
					+ "  , lng decimal(10,7) NOT NULL)");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}

	public void create_ParkWaterStation() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE ParkWaterStation("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , section  nvarchar(50) NOT NULL"
					+ "  , name  nvarchar(50) NOT NULL "
					+ "  , note  nvarchar(50) NOT NULL "
					+ "  , address nvarchar(50) NOT NULL "
					+ "  , lat decimal(10,7) " + "  , lng decimal(10,7) )");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}
	}

	public void insert_ParkWaterStation(List<ParkWaterStation> list) {

		if (!hasTable("ParkWaterStation"))
			create_ParkWaterStation();

		try {
			pst = connection
					.prepareStatement("insert into ParkWaterStation(section,name,address,note,lat,lng)"
							+ "VALUES (?,?,?,?,?,?)");

			for (ParkWaterStation park : list) {

				pst.setString(1, park.getSection());
				pst.setString(2, park.getLocation());
				pst.setString(3, park.getAddress());
				pst.setString(4, park.getNote());
				pst.setBigDecimal(5, park.getLat());
				pst.setBigDecimal(6, park.getLng());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void insert_Hydrant(List<Hydrant> list) {

		if (!hasTable("Hydrant"))
			create_Hydrant();

		try {
			pst = connection.prepareStatement("insert into Hydrant(lat,lng)"
					+ "VALUES (?,?)");

			for (Hydrant hydrant : list) {

				pst.setBigDecimal(1, hydrant.getLat());
				pst.setBigDecimal(2, hydrant.getLng());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void insert_FireDepartment(List<FireDepartment> list) {

		if (!hasTable("FireDepartment"))
			create_FireDepartment();

		try {
			pst = connection
					.prepareStatement("insert into FireDepartment(name,TWD67_X,TWD67_Y,lat,lng,address)"
							+ "VALUES (?,?,?,?,?,?)");

			for (FireDepartment department : list) {

				pst.setString(1, department.getName());
				pst.setFloat(2, department.getTWD67_X());
				pst.setFloat(3, department.getTWD67_Y());
				pst.setBigDecimal(4, department.getLat());
				pst.setBigDecimal(5, department.getLng());
				pst.setString(6, department.getAddress());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void create_IllegalConstruction() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE IllegalConstruction("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , section  nvarchar(50) NOT NULL"
					+ "  , address  nvarchar(50) NOT NULL "
					+ "  , area  float NOT NULL "
					+ "  , date datetime NOT NULL" + "  , lat decimal(10,7) "
					+ "  , lng decimal(10,7) )");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}

	public void insert_IllegalConstruction(List<IllegalConstruction> list) {

		if (!hasTable("IllegalConstruction"))
			create_IllegalConstruction();

		try {
			pst = connection
					.prepareStatement("insert into IllegalConstruction(section,address,area,date,lat,lng)"
							+ "VALUES (?,?,?,?,?,?)");

			for (IllegalConstruction illegal : list) {

				java.sql.Date sqlDate = new java.sql.Date(illegal.getDate()
						.getTime());

				pst.setString(1, illegal.getSection());
				pst.setString(2, illegal.getAddress());
				pst.setFloat(3, illegal.getArea());
				pst.setDate(4, sqlDate);
				pst.setBigDecimal(5, illegal.getLat());
				pst.setBigDecimal(6, illegal.getLng());
				pst.addBatch();
			}

			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void create_NarrowRoadway() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE NarrowRoadway("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , section  nvarchar(50) NOT NULL"
					+ "  , team  nvarchar(50) NOT NULL "
					+ "  , roadway  nvarchar(50) NOT NULL "
					+ "  , level nvarchar(10)"
					+ "  , width float NOT NULL"
					+ "  , lat decimal(10,7) NOT NULL"
					+ "  , lng decimal(10,7) NOT NULL" + "  , polygon text)");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}

	public void create_Village() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE Village("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , name  nvarchar(50) NOT NULL"
					+ "  , section  nvarchar(10) NOT NULL "
					+ "  , area nvarchar(15) NOT NULL" + "  , polygon text"
					+ "  , json_polygon text)");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}
	
	
	public void create_Result() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE Result("
					+ "    id   INT NOT NULL PRIMARY KEY"
					+ "  , name  nvarchar(50) NOT NULL"
					+ "  , section  nvarchar(10) NOT NULL "
					+"   , score float"
					+ "  , json_polygon text)");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}
	
	
	public void insert_Result(List<Result> list) {

		if (!hasTable("Result"))
			create_Result();
		;

		try {
			pst = connection
					.prepareStatement("insert into Result(id,name,section,score,json_polygon)"
							+ "VALUES (?,?,?,?,?)");

			for (Result r : list) {
				pst.setInt(1, r.getID());
				pst.setString(2, r.getName());
				pst.setString(3, r.getSection());
				pst.setFloat(4, r.getScore());
				pst.setString(5, r.getJson_polygon());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void insert_Village(List<Village> list) {

		if (!hasTable("Village"))
			create_Village();
		;

		try {
			pst = connection
					.prepareStatement("insert into Village(name,section,area,polygon,json_polygon)"
							+ "VALUES (?,?,?,?,?)");

			for (Village v : list) {
				pst.setString(1, v.getName());
				pst.setString(2, v.getSection());
				pst.setString(3, v.getArea());

				String polygon = "";
				String json = "[";

				int size = v.getPolygon().size();

				for (int i = 0; i < size; i++) {
					HashMap map = v.getPolygon().get(i);
					if (i == size - 1) {
						polygon += map.get("lat") + "," + map.get("lng");
						json += "{\"lat\":" + map.get("lat") + ",\"lng\":"
								+ map.get("lng") + "}]";
					} else {
						polygon += map.get("lat") + "," + map.get("lng") + " ";
						json += "{\"lat\":" + map.get("lat") + ",\"lng\":"
								+ map.get("lng") + "},";
					}
				}

				pst.setString(4, polygon);
				pst.setString(5, json);
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void insert_NarrowRoadway(List<NarrowRoadway> list) {

		if (!hasTable("NarrowRoadway"))
			create_NarrowRoadway();

		try {
			pst = connection
					.prepareStatement("insert into NarrowRoadway(section,team,roadway,width,lat,lng,polygon,level)"
							+ "VALUES (?,?,?,?,?,?,?,?)");

			for (NarrowRoadway nr : list) {
				pst.setString(1, nr.getSection());
				pst.setString(2, nr.getTeam());
				pst.setString(3, nr.getRoadway());
				pst.setFloat(4, nr.getWidth());
				pst.setBigDecimal(5, nr.getLat());
				pst.setBigDecimal(6, nr.getLng());
				pst.setString(7, nr.getPolygon());
				pst.setString(8, nr.getLevel());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void create_SeriousFailureLocation() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE SeriousFailureLocation("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , name  nvarchar(50) NOT NULL"
					+ "  , address nvarchar(50) NOT NULL "
					+ "  , checkResult  nvarchar(50) NOT NULL "
					+ "  , date datetime NOT NULL" + "  , lat decimal(10,7) "
					+ "  , lng decimal(10,7) )");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}

	public void insert_SeriousFailureLocation(List<SeriousFailureLocation> list) {

		if (!hasTable("SeriousFailureLocation"))
			create_SeriousFailureLocation();

		try {
			pst = connection
					.prepareStatement("insert into SeriousFailureLocation(name,address,checkResult,date,lat,lng)"
							+ "VALUES (?,?,?,?,?,?)");

			for (SeriousFailureLocation failure : list) {

				java.sql.Date sqlDate = new java.sql.Date(failure.getDate()
						.getTime());

				pst.setString(1, failure.getName());
				pst.setString(2, failure.getAddress());
				pst.setString(3, failure.getCheckResult());
				pst.setDate(4, sqlDate);
				pst.setBigDecimal(5, failure.getLat());
				pst.setBigDecimal(6, failure.getLng());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}
	}

	public void create_LevelDifficultyOfFireRescue() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE LevelDifficultyOfFireRescue("
					+ "    id  INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , level INT NOT NULL" + "  , item INT NOT NULL"
					+ "  , section nvarchar(10) NOT NULL "
					+ "  , address nvarchar(50) NOT NULL "
					+ "  , name nvarchar(50) NOT NULL"
					+ "  , hasAisle bit NOT NULL" + "  , lat decimal(10,7) "
					+ "  , lng decimal(10,7) )");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}

	public void insert_LevelDifficultyOfFireRescue(
			List<LevelDifficultyOfFireRescue> list) {

		if (!hasTable("LevelDifficultyOfFireRescue"))
			create_LevelDifficultyOfFireRescue();

		try {
			pst = connection
					.prepareStatement("insert into LevelDifficultyOfFireRescue(level,item,section,address,name,hasAisle,lat,lng)"
							+ "VALUES (?,?,?,?,?,?,?,?)");

			for (LevelDifficultyOfFireRescue difficulty : list) {

				pst.setInt(1, difficulty.getLevel());
				pst.setInt(2, difficulty.getItem());
				pst.setString(3, difficulty.getSection());
				pst.setString(4, difficulty.getAddress());
				pst.setString(5, difficulty.getName());

				if (difficulty.getHasAisle())
					pst.setInt(6, 1);
				else
					pst.setInt(6, 0);

				pst.setBigDecimal(7, difficulty.getLat());
				pst.setBigDecimal(8, difficulty.getLng());
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// Close();
		}

	}

	public void create_FireCount() {

		try {
			statement = connection.createStatement();
			statement
					.executeUpdate("CREATE TABLE FireCount("
							+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
							+ "  , section  nvarchar(10) NOT NULL"
							+ "  , count INT NOT NULL "
							+ "  , date datetime NOT NULL)");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}

	public void insert_FireCount(List<FireCount> list) {

		if (!hasTable("FireCount"))
			create_FireCount();

		try {
			pst = connection
					.prepareStatement("insert into FireCount(section,count,date)"
							+ "VALUES (?,?,?)");

			for (FireCount fc : list) {

				java.sql.Date sqlDate = new java.sql.Date(fc.getDate()
						.getTime());

				pst.setString(1, fc.getSection());
				pst.setInt(2, fc.getCount());
				pst.setDate(3, sqlDate);
				pst.addBatch();
			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// close();
		}

	}

	/**
	 * check table exists?
	 * 
	 * @param table_name
	 * @return boolean
	 */

	public boolean hasTable(String table_name) {

		DatabaseMetaData meta = null;
		ResultSet rsTables = null;

		try {
			meta = connection.getMetaData();
			rsTables = meta.getTables(null, null, table_name, null);

			if (rsTables.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public void create_EmergencyHospital() {

		try {
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE EmergencyHospital("
					+ "    id   INT IDENTITY NOT NULL PRIMARY KEY"
					+ "  , name  nvarchar(50) NOT NULL"
					+ "  , address_for_display  nvarchar(50) NOT NULL "
					+ "  , address_for_system  nvarchar(50) NOT NULL "
					+ "  , telephone nvarchar(50) NOT NULL"
					+ "  , hospital_evaluation nvarchar(10) NOT NULL"
					+ "  , teaching_hospital_evaluation nvarchar(10) NOT NULL"
					+ "  , category nvarchar(10) NOT NULL"
					+ "  , lat decimal(10,7) NOT NULL"
					+ "  , lng decimal(10,7) NOT NULL)");

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

	}

	public void insert_EmergencyHospital(List<EmergencyHospital> list) {

		String queryString = "insert into EmergencyHospital(name,address_for_display,address_for_system,telephone"
				+ ",hospital_evaluation,teaching_hospital_evaluation,category,lat,lng) VALUES (?,?,?,?,?,?,?,?,?)";

		if (!hasTable("EmergencyHospital"))
			create_EmergencyHospital();

		try {
			pst = connection.prepareStatement(queryString);

			for (EmergencyHospital hospital : list) {

				pst.setString(1, hospital.getName());
				pst.setString(2, hospital.getAddressForDisplay());
				pst.setString(3, hospital.getAddressForSystem());
				pst.setString(4, hospital.getTelephone());
				pst.setString(5, hospital.getHospitalEvaluation());
				pst.setString(6, hospital.getTeachingHospitalEvaluation());
				pst.setString(7, hospital.getCategory());
				pst.setBigDecimal(8, hospital.getLat());
				pst.setBigDecimal(9, hospital.getLng());
				pst.addBatch();

			}
			pst.executeBatch();

		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			// close();
		}

	}

	public void close() {

		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}

	public LinkedList<Village> select_Village(String query_section) {

		LinkedList<Village> list = new LinkedList<Village>();

		String queryString = "select * from Village where section='"
				+ query_section + "'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String section = resultSet.getString("section");
				String polygon = resultSet.getString("polygon");
				
				ArrayList<Double> lats = new ArrayList<Double>();
				ArrayList<Double> lngs = new ArrayList<Double>();

				for (String temp : polygon.split(" ")) {
					String[] latlng = temp.split(",");
					// System.out.println(latlng[0]);
					lats.add(Double.parseDouble(latlng[0]));
					lngs.add(Double.parseDouble(latlng[1]));

				}

				list.add(new Village(id, name, section, lats, lngs,resultSet.getString("json_polygon")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

	/**
	 * 
	 * @return
	 */

	public LinkedList<IllegalConstruction> select_IllegalConstruction(
			String section) {

		LinkedList<IllegalConstruction> list = new LinkedList<IllegalConstruction>();

		String queryString = "select * from IllegalConstruction where section='"
				+ section + "'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {
				// System.out.println(resultSet.getString("lat"));

				list.add(new IllegalConstruction(resultSet.getInt("id"),
						resultSet.getBigDecimal("lat"), resultSet
								.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

	public LinkedList<FireDepartment> select_FireDepartment(String section) {

		LinkedList<FireDepartment> list = new LinkedList<FireDepartment>();

		String queryString = "select * from FireDepartment where address like '%"
				+ section + "%'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new FireDepartment(resultSet.getInt("id"), resultSet
						.getString("name"), resultSet.getBigDecimal("lat"),
						resultSet.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

	public LinkedList<EmergencyHospital> select_EmergencyHospital(String section) {

		LinkedList<EmergencyHospital> list = new LinkedList<EmergencyHospital>();

		String queryString = "select * from EmergencyHospital where address_for_display like '%"
				+ section + "%'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new EmergencyHospital(resultSet.getInt("id"),
						resultSet.getString("name"), resultSet
								.getString("address_for_display"), resultSet
								.getString("hospital_evaluation"), resultSet
								.getString("teaching_hospital_evaluation"),
						resultSet.getString("category"), resultSet
								.getBigDecimal("lat"), resultSet
								.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

	public LinkedList<LevelDifficultyOfFireRescue> select_LevelDifficultyOfFireRescue(
			String section) {

		LinkedList<LevelDifficultyOfFireRescue> list = new LinkedList<LevelDifficultyOfFireRescue>();

		String queryString = "select * from LevelDifficultyOfFireRescue where section = '"
				+ section + "'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new LevelDifficultyOfFireRescue(
						resultSet.getInt("id"), resultSet.getInt("level"),
						resultSet.getInt("item"), resultSet
								.getString("section"), resultSet
								.getString("address"), resultSet
								.getString("name"), resultSet
								.getBoolean("hasAisle"), resultSet
								.getBigDecimal("lat"), resultSet
								.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

	public LinkedList<NarrowRoadway> select_NarrowRoadway(String section) {

		LinkedList<NarrowRoadway> list = new LinkedList<NarrowRoadway>();

		String queryString = "select * from NarrowRoadway where section = '"
				+ section + "'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new NarrowRoadway(resultSet.getInt("id"), resultSet
						.getString("section"), resultSet.getString("team"),
						resultSet.getString("roadway"), resultSet
								.getFloat("width"), resultSet
								.getBigDecimal("lat"), resultSet
								.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

	public LinkedList<SeriousFailureLocation> select_SeriousFailureLocation(
			String section) {

		LinkedList<SeriousFailureLocation> list = new LinkedList<SeriousFailureLocation>();

		String queryString = "select * from SeriousFailureLocation where address like '%"
				+ section + "%'";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new SeriousFailureLocation(resultSet.getInt("id"),
						resultSet.getString("name"), resultSet
								.getString("address"), resultSet
								.getString("checkResult"), resultSet
								.getBigDecimal("lat"), resultSet
								.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}
	
	
	public LinkedList<FireCount> select_FireCount(String section) {

		LinkedList<FireCount> list = new LinkedList<FireCount>();

		String queryString = "select * from FireCount where section ='"+ section + "'";
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new FireCount(resultSet.getInt("id"), resultSet.getString("section")
						, resultSet.getInt("count"), resultSet.getDate("date")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}
	
	
	public LinkedList<Hydrant> select_Hydrant() {

		LinkedList<Hydrant> list = new LinkedList<Hydrant>();

		String queryString = "select * from Hydrant ";
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryString);

			while (resultSet.next()) {

				list.add(new Hydrant(resultSet.getInt("id"), resultSet.getBigDecimal("lat")
						, resultSet.getBigDecimal("lng")));
			}

			return list;

		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			// Close();
		}
		return list;
	}

}
