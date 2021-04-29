package ca.myseneca.model;

/**
* @author Chao Chen
*
* The DAManager.java is a data access manager class provides the interface
* that is used to manipulate data in the database without exposing details of the
* database. The bean of Employee class is requested to be used in the data
* operations for the Employees table. The code of loading the properties file,
* setting up JDBC connection is not allowed in the class.
* 
*/

import java.io.File;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class DAManager {
	private static Connection conn;

	public DAManager(Connection connArg) {
		// super();
		DAManager.conn = connArg;
	}

	// call the PL/SQL function F_SECURITY in the P_SECURITY package; the method
	// will return a 0 value for unauthorized user
	@SuppressWarnings("finally")
	public static int getEmployeeID(String user, String password) throws SQLException {
		CallableStatement stmt = null;
		ResultSet rset = null;
		int auth = 1;
		try {
			stmt = conn.prepareCall("{ ? = call P_SECURITY.F_SECURITY(?,?)}");
			stmt.setString(2, user);
			stmt.setString(3, password);
			stmt.registerOutParameter(1, Types.INTEGER);
			rset = stmt.executeQuery();
			auth = stmt.getInt(1);

		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			// finally block used to close resources
			if (stmt != null) {
				stmt.close();
			}
			return auth;
		}
	}

	// store a new employee to employee table
	public int addEmployee(Employee emp) throws SQLException {
		PreparedStatement stmt = null;
		int userID = 0;

		try {
			String sql = "insert into EMPLOYEES (EMPLOYEE_ID,FIRST_NAME,LAST_NAME,EMAIL,PHONE_NUMBER,HIRE_DATE,JOB_ID,SALARY,COMMISSION_PCT,MANAGER_ID,DEPARTMENT_ID) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);

			// set up query parameters
			stmt.setInt(1, emp.getEmployee_ID());
			stmt.setString(2, emp.getFirst_Name());
			stmt.setString(3, emp.getLast_Name());
			stmt.setString(4, emp.getEmail());
			stmt.setString(5, emp.getPHONE_NUMBER());
			stmt.setDate(6, emp.getHIRE_DATE());
			stmt.setString(7, emp.getJOB_ID());
			stmt.setDouble(8, emp.getSALARY());
			stmt.setDouble(9, emp.getCOMMISSION_PCT());
			stmt.setInt(10, emp.getMANAGER_ID());
			stmt.setInt(11, emp.getDEPARTMENT_ID());

			// Execute the update statement
			stmt.executeUpdate();

			userID = emp.getEmployee_ID();
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			// finally block used to close resources
			if (stmt != null) {
				stmt.close();
			}
		}

		return userID;
	}

	// return an array of all employees
	@SuppressWarnings("finally")
	public static ArrayList<Employee> getAllEmployees() throws SQLException {
		ArrayList<Employee> eArray = new ArrayList<Employee>();
		Statement stmt = null;
		String query = "select * from EMPLOYEES";
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Employee emp = new Employee();
				emp.setEmployee_ID(rs.getInt("EMPLOYEE_ID"));
				emp.setFirst_Name(rs.getString("FIRST_NAME"));
				emp.setLast_Name(rs.getString("LAST_NAME"));
				emp.setEmail(rs.getString("EMAIL"));
				emp.setPHONE_NUMBER(rs.getString("PHONE_NUMBER"));
				emp.setHIRE_DATE(rs.getDate("HIRE_DATE"));
				emp.setJOB_ID(rs.getString("JOB_ID"));
				emp.setSALARY(rs.getDouble("SALARY"));
				emp.setCOMMISSION_PCT(rs.getDouble("COMMISSION_PCT"));
				emp.setMANAGER_ID(rs.getInt("MANAGER_ID"));
				emp.setDEPARTMENT_ID(rs.getInt("DEPARTMENT_ID"));
				eArray.add(emp);
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			return eArray;
		}
	}

	// return an array of employees meet the DepartmentID
	@SuppressWarnings("finally")
	public static ArrayList<Employee> getEmployeesByDepartmentID(int depid) throws SQLException {
		ArrayList<Employee> eArray = new ArrayList<Employee>();
		Statement stmt = null;
		String query = "select * from EMPLOYEES where DEPARTMENT_ID = " + depid;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Employee emp = new Employee();
				emp.setEmployee_ID(rs.getInt("EMPLOYEE_ID"));
				emp.setFirst_Name(rs.getString("FIRST_NAME"));
				emp.setLast_Name(rs.getString("LAST_NAME"));
				emp.setEmail(rs.getString("EMAIL"));
				emp.setPHONE_NUMBER(rs.getString("PHONE_NUMBER"));
				emp.setHIRE_DATE(rs.getDate("HIRE_DATE"));
				emp.setJOB_ID(rs.getString("JOB_ID"));
				emp.setSALARY(rs.getDouble("SALARY"));
				emp.setCOMMISSION_PCT(rs.getDouble("COMMISSION_PCT"));
				emp.setMANAGER_ID(rs.getInt("MANAGER_ID"));
				emp.setDEPARTMENT_ID(rs.getInt("DEPARTMENT_ID"));
				eArray.add(emp);
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			return eArray;
		}
	}

	// return an employee meet the employeeID
	@SuppressWarnings("finally")
	public static Employee getEmployeeByID(int empid) throws SQLException {
		Employee emp = new Employee();
		OracleCallableStatement stmt = null;
//	    String query = "select * from EMPLOYEES where EMPLOYEE_ID="+empid;
		try {
			stmt = (OracleCallableStatement) conn.prepareCall("{call P_SECURITY.P_EMP_INFO(?,?)}");
			stmt.setInt(1, empid);
			stmt.registerOutParameter(2, OracleTypes.CURSOR);

			stmt.executeUpdate();
			OracleResultSet ors = (OracleResultSet) stmt.getCursor(2);

			while (ors.next()) {
				emp.setEmployee_ID(ors.getInt("EMPLOYEE_ID"));
				emp.setFirst_Name(ors.getString("FIRST_NAME"));
				emp.setLast_Name(ors.getString("LAST_NAME"));
				emp.setEmail(ors.getString("EMAIL"));
				emp.setPHONE_NUMBER(ors.getString("PHONE_NUMBER"));
				emp.setHIRE_DATE(ors.getDate("HIRE_DATE"));
				emp.setJOB_ID(ors.getString("JOB_ID"));
				emp.setSALARY(ors.getDouble("SALARY"));
				emp.setCOMMISSION_PCT(ors.getDouble("COMMISSION_PCT"));
				emp.setMANAGER_ID(ors.getInt("MANAGER_ID"));
				emp.setDEPARTMENT_ID(ors.getInt("DEPARTMENT_ID"));
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			return emp;
		}
	}

	// update data of an employee in db, return result as integer
	@SuppressWarnings("finally")
	public static int updateEmployee(Employee emp) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE  Employees SET"
					+ " EMPLOYEE_ID=?,FIRST_NAME=?,LAST_NAME=?,EMAIL=?,PHONE_NUMBER=?,HIRE_DATE=?,"
					+ "JOB_ID=?,SALARY=?,COMMISSION_PCT=?,MANAGER_ID=?,DEPARTMENT_ID=? " + " where EMPLOYEE_ID = "
					+ emp.getEmployee_ID();
			stmt = conn.prepareStatement(sql);

			// set up query parameters
			stmt.setInt(1, emp.getEmployee_ID());
			stmt.setString(2, emp.getFirst_Name());
			stmt.setString(3, emp.getLast_Name());
			stmt.setString(4, emp.getEmail());
			stmt.setString(5, emp.getPHONE_NUMBER());
			stmt.setDate(6, emp.getHIRE_DATE());
			stmt.setString(7, emp.getJOB_ID());
			stmt.setDouble(8, emp.getSALARY() + 1); // Add 1 to salary, to see the difference
			stmt.setDouble(9, emp.getCOMMISSION_PCT());
			stmt.setInt(10, emp.getMANAGER_ID());
			stmt.setInt(11, emp.getDEPARTMENT_ID());

			// Execute the update statement
			stmt.executeUpdate();
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (final SQLException se) {
			} // do nothing
			return 0;
		}
	}

	// delete data of an employee in db, return how many rows deleted from db as an
	// integer result
	@SuppressWarnings("finally")
	public static int deleteEmployeeByID(int empid) throws SQLException {
//		Employee emp = new Employee();
		PreparedStatement stmt = null;
		System.out.println("1");
		int rows = 0;

		try {
			String sql = "DELETE FROM EMPLOYEES WHERE EMPLOYEE_ID = ?" ;
			stmt = conn.prepareStatement(sql);

			// set up query parameters
			stmt.setInt(1, empid);
			
			rows = stmt.executeUpdate();
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			return rows;
		}
	}

	// The batch update should be executed inside a transaction to make sure that
	// either all updates are executed, or none are.
	// Any successful updates can be rolled back, in case of the update fail.
	@SuppressWarnings("finally")
	public static boolean batchUpdate(String[] SQLs) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;

		try {
			conn.setAutoCommit(false);
			Savepoint save = conn.setSavepoint();
			for (int i = 0; i < SQLs.length; i++) {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

				if (!stmt.execute(SQLs[i])) {
					System.out.println("Could not find entry");
					System.out.println("\ntable after rollback:");
					conn.rollback(save);
					return false;
				} else {
					rs = stmt.getResultSet();
					rs.first();// first row
					int empid = rs.getInt("EMPLOYEE_ID");
				}
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			conn.setAutoCommit(true);
			return true;
		}
	}
}
