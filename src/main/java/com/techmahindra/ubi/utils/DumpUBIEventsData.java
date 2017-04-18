package com.techmahindra.ubi.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DumpUBIEventsData {
	
	private static Connection connection = null;
	
	public DumpUBIEventsData() throws Exception {
		Properties props = new Properties();
		try {
			props.load(DumpUBIEventsData.class.getClassLoader().getResourceAsStream("ubi.properties"));
			connection = getDBConnection(props);
	        connection.setAutoCommit(false);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void dumpEventsData() {
		Statement stmt = null;
		try {
	        stmt = connection.createStatement();
	        stmt.execute("CREATE TABLE IF NOT EXISTS UBI(vin varchar(20),"+
								"model varchar(30),"+
								"timestamp timestamp,"+
								"outsideTemp real,"+
								"engineTemp real,"+
								"speed real,"+
								"fuel real,"+
								"engineOil real,"+
								"tirePressure real,"+
								"odometer bigint,"+
								"city varchar(20),"+
								"accPedalPos int,"+
								"parkBrakeStatus int,"+
								"headlampStatus int,"+
								"brakePedalStatus int,"+
								"transGearPosition varchar(10),"+
								"ignitionStatus int,"+
								"windshieldWiperStatus int,"+
								"abs int,"+
								"tripId varchar(28),"+
								"incidentType varchar(20))");
		        
		        String csvPath = "D:\\Sample_Data\\careventsdata\\rawcareventstream";
		        String[] csvFiles = new String[]{
		        		"824507016_30dc7f8019f24402a2df1a49c21689bf_2015-03-30.csv",
						"824507016_30dc7f8019f24402a2df1a49c21689bf_2015-04-28.csv",
						"824507016_77fddce51ace464f8dcd963cad9c796b_4.csv",
						"824507016_77fddce51ace464f8dcd963cad9c796b_2016-08-10.csv"
		        };
		        
		        for(String csvFile: csvFiles) {
		        	int count = stmt.executeUpdate("INSERT INTO UBI SELECT * FROM CSVREAD('" + csvPath + "\\" + csvFile + "', null, null)");
		        	System.out.println("Inserted " + count + " records from " + csvFile);
		        }
		        
		        connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void testConnection() {
		Statement stmt = null;
		try {
	        stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT count(*) FROM UBI");
	        rs.next();
	        System.out.println(rs.getInt(1) + " records available in DB");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void closeConnection() {
		if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	private static Connection getDBConnection(Properties props) throws ClassNotFoundException, SQLException {
        Connection dbConnection;
        try {
            Class.forName(props.getProperty("JDBC_DRIVER"));
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
			throw e;
        }
        try {
            dbConnection = DriverManager.getConnection(props.getProperty("JDBC_CONN_STRING"), 
            											props.getProperty("JDBC_USER"), 
            											props.getProperty("JDBC_PASSWORD"));
        } catch (SQLException e) {
        	e.printStackTrace();
			throw e;
        }
        return dbConnection;
    }
	
	public static void main(String[] args) throws Exception {
		DumpUBIEventsData dump = new DumpUBIEventsData();
		dump.dumpEventsData();
		dump.closeConnection();
	}
}
