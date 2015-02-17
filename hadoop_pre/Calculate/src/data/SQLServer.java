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

}
