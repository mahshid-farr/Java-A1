package ca.myseneca.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import ca.myseneca.model.DAManager;
import ca.myseneca.model.DBUtilities;
import ca.myseneca.model.Employee;

/**
*
* The HRManagement class is a test class that mimics an application. Prompt
* user input a username and a password from the console. If pass the credential
* check, show the user’s info as employee. Then test your JDBC data access
* code by calling each method in HRManagement class for Employee. If not
* pass, stop the program.
* 
*/
public class HRManagement {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBUtilities myJDBCTutorialUtilities;
		Connection myConnection = null;

		// Get DB configuration
		try {
			myJDBCTutorialUtilities = new DBUtilities("database.properties");
		} catch (Exception e) {
			System.err.println("Problem reading properties file db.properties");
			e.printStackTrace();
			return;
		}

		try {
			myConnection = myJDBCTutorialUtilities.getConnection();

			DAManager myManager = new DAManager(myConnection);
			Scanner inputReader = new Scanner(System.in);
			
			// Ask user for credentials
			System.out.println("Please enter username: ");
			String username = inputReader.next();
			System.out.println("Please enter password: ");
			String password = inputReader.next();

			// Check authorization
			if (myManager.getEmployeeID(username, password) == 0) {
				System.out.println("Sorry, your credentials are not correct. Program will be stopped.");
				System.exit(0);
			} else {
				System.out.println("Function 'getEmployeeID' successfully tested!\n");
			}

			// Testing
			
			// Get all Employees
			System.out.println("Get all Employees");
			ArrayList<Employee> allEmpArray = myManager.getAllEmployees();
			System.out.println("Function 'getAllEmployees' successfully tested!\n");

			// Get Employees By Department ID
			System.out.println("Get Employees By Department ID");
			System.out.println("Please enter department ID: (for example 60)");
			int depid = inputReader.nextInt();
			ArrayList<Employee> depEmpArray = myManager.getEmployeesByDepartmentID(depid);
			System.out.println("Function 'getEmployeesByDepartmentID' successfully tested!\n");
			
			// Add a new Employee
			System.out.println("Add a new Employee");
			Employee newEmp = new Employee(true);
			int userID = myManager.addEmployee(newEmp);
			System.out.println("Function 'addEmployee' successfully tested! User with ID "+userID+" was created.\n");
			
			// Get Employee By ID
			System.out.println("Get Employee By ID");
			System.out.println("Please enter valid employee ID: (for example 115)");
			int uid = inputReader.nextInt();
			Employee empArray = myManager.getEmployeeByID(uid);
			
			// Update Employee
			System.out.println("Update Employee");
			int updateResult = myManager.updateEmployee(empArray);
			System.out.println("Function 'updateEmployee' successfully tested!\n");	
			
			// Delete Employee By ID
			System.out.println("Delete Employee By ID");
			System.out.println("Please enter valid employee ID: (for example 105)");
			int did = inputReader.nextInt();
			myManager.deleteEmployeeByID(did);
			System.out.println("Function 'deleteEmployeeByID' successfully tested!\n");

			inputReader.close();
			
			System.out.println("Program successfully completed!");
			
		} catch (ClassNotFoundException ce) {
			System.err.println("JDBC driver class not found.");
			ce.printStackTrace();
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			DBUtilities.closeConnection(myConnection);
		}
	}
	
}