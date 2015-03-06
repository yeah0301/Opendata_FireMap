
import java.sql.*;
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

	public void insert_SeriousFailureLocation(List<SeriousFailureLocation> list) {

		if (!hasTable("SeriousFailureLocation"))
			create_SeriousFailureLocation();
		else
			TRUNCATE_SeriousFailureLocation();
		
			
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

	
	
	public void TRUNCATE_SeriousFailureLocation() {
		try {
			statement = connection.createStatement();
			statement.executeUpdate("TRUNCATE TABLE SeriousFailureLocation");
		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			//Close();
		}
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
	
	

}
