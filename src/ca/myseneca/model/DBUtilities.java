package ca.myseneca.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * @author Mahshid Farrahinia
 * 
 *         The DBUtil class provides the functionalities of loading the
 *         properties file, setting up JDBC connection to Oracle database on
 *         Zenit, retrieve SQLException, SQLWarnings, and BatchUpdateException.
 *         The class have the ability to connect to Oracle database use both
 *         Thin and OCI drivers based on the setting in the properties file
 * 
 */
public class DBUtilities {
	private Properties dbProps;

	public DBUtilities(String propertiesFileName)
			throws FileNotFoundException, IOException, InvalidPropertiesFormatException {
		super();
		this.setProperties(propertiesFileName);
	}

	// Printing warnings
	public static void printWarnings(SQLWarning warning) throws SQLException {
		if (warning != null) {
			System.out.println("\n---Warning---\n");
			while (warning != null) {
				System.out.println("Message: " + warning.getMessage());
				System.out.println("SQLState: " + warning.getSQLState());
				System.out.print("Vendor error code: ");
				System.out.println(warning.getErrorCode());
				System.out.println("");
				warning = warning.getNextWarning();
			}
		}
	}

	// Printing batch update exceptions
	public static void printBatchUpdateException(BatchUpdateException batchException) {
		System.err.println("----BatchUpdateException----");
		System.err.println("SQLState:  " + batchException.getSQLState());
		System.err.println("Message:  " + batchException.getMessage());
		System.err.println("Vendor:  " + batchException.getErrorCode());
		System.err.print("Update counts:  ");
		int[] updateCounts = batchException.getUpdateCounts();
		for (int i = 0; i < updateCounts.length; i++) {
			System.err.print(updateCounts[i] + "   ");
		}
	}

	// Printing SQL exceptions (Trace all exception)
	public static void printSQLException(SQLException exception) {
		for (Throwable e : exception) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable throwable = exception.getCause();
				while (throwable != null) {
					System.out.println("Cause: " + throwable);
					throwable = throwable.getCause();
				}
			}
		}
	}

	// Set the properties and if any exception happen such as file not found. it can
	// handle the exception
	private void setProperties(String fileName)
			throws FileNotFoundException, IOException, InvalidPropertiesFormatException {
		this.dbProps = new Properties();

		// Read the file which is from database.properties file
		FileInputStream inputFile = new FileInputStream(fileName);
		dbProps.load(inputFile);
	}

	// Creating connection. If some exceptions happen it can handle them
	public Connection getConnection() throws SQLException, ClassNotFoundException {

		// It gets driver and URL informations from file for Oracle database
		String driver = dbProps.getProperty("ORACLE_DB_DRIVER");
		String URL = null;

		if (dbProps.getProperty("ORACLE_CONNECTION_TYPE").equals("THIN")) {
			URL = dbProps.getProperty("ORACLE_DB_THIN_DRIVER_CONNECT_DESCRIPTOR_URL");
		} else {
			URL = dbProps.getProperty("ORACLE_DB_OCI_DRIVER_CONNECT_DESCRIPTOR_URL");

			// This file is used for the OCI connection on the Mac. I don't think you need
			// this on Windows.
//			System.load(System.getProperty("user.dir") + "/libocijdbc19.dylib");
		}

		// It gets database properties (User name and password for Oracle database)
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", this.dbProps.getProperty("ORACLE_DB_USERNAME"));
		connectionProperties.put("password", this.dbProps.getProperty("ORACLE_DB_PASSWORD"));

		// Loading driver
		Class.forName(driver);

		// Establishing connection
		Connection connection = null;

		if (dbProps.getProperty("ORACLE_CONNECTION_TYPE").equals("THIN")) {
			connection = DriverManager.getConnection(URL, connectionProperties);
		} else {
			// OCI driver
			Properties properties = new Properties();
			properties.put("user", this.dbProps.getProperty("ORACLE_DB_USERNAME"));
			properties.put("password", this.dbProps.getProperty("ORACLE_DB_PASSWORD"));
			properties.put("defaultRowPrefetch", "20");
			connection = DriverManager.getConnection(URL, properties);
		}

		// Return that connection object
		System.out.println("Connected database successfully");
		return connection;
	}

	// Closing the connection
	public static void closeConnection(Connection connArg) {
		System.out.println("Close all open resources ...");
		try {
			if (connArg != null) {
				connArg.close();
				connArg = null;
			}
		} catch (SQLException sqle) {
			printSQLException(sqle);
		}
	}

}
