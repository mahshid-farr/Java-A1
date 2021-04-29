package ca.myseneca.model;

import java.io.Serializable;
import java.util.Random;
import java.sql.Date;

/**
 * @author Mahshid Farrahinia This public class creates a Serializable Employee
 *         class that has same attributes stored in the Database. It meets the
 *         required conventions of JavaBeans. So it implement the
 *         java.io.Serializable interface, It contains private variables with
 *         default (zero-argument) constructor, the setters, and the getters
 *         Also it has another constructor with related fields
 */
public class Employee implements Serializable {

	// To remove warning
	private static final long serialVersionUID = 1L;

	private int Employee_ID;
	private String First_Name;
	private String Last_Name;
	private String Email;
	private String PHONE_NUMBER;
	private Date HIRE_DATE;
	private String JOB_ID;
	private double SALARY;
	private double COMMISSION_PCT;
	private int MANAGER_ID;
	private int DEPARTMENT_ID;
	
	// Default Constructor for Employees
	public Employee() {
	}

	// Default Constructor for Employees
	public Employee(boolean test) {
		super();
		Random rand = new Random(); 
		
		Employee_ID = rand.nextInt(999999);
		First_Name = "TEST NAME"+String.valueOf(Employee_ID);
		Last_Name = "TEST LASTNAME"+String.valueOf(Employee_ID);
		Email = "email@"+String.valueOf(Employee_ID)+".com";
		PHONE_NUMBER = String.valueOf(Employee_ID);
		HIRE_DATE = new Date(2010, 1, 3);
		JOB_ID = "SA_MAN";
		SALARY = 14000;
		COMMISSION_PCT = 0.4;
		MANAGER_ID = 100;
		DEPARTMENT_ID = 80;
	}

	// Default using fields for Employees
	public Employee(int employee_ID, String first_Name, String last_Name, String email, String pHONE_NUMBER,
			Date hIRE_DATE, String jOB_ID, double sALARY, double cOMMISSION_PCT, int mANAGER_ID, int dEPARTMENT_ID) {
		super();
		Employee_ID = employee_ID;
		First_Name = first_Name;
		Last_Name = last_Name;
		Email = email;
		PHONE_NUMBER = pHONE_NUMBER;
		HIRE_DATE = hIRE_DATE;
		JOB_ID = jOB_ID;
		SALARY = sALARY;
		COMMISSION_PCT = cOMMISSION_PCT;
		MANAGER_ID = mANAGER_ID;
		DEPARTMENT_ID = dEPARTMENT_ID;
	}

	// Getter and Setters
	public int getEmployee_ID() {
		return Employee_ID;
	}

	public void setEmployee_ID(int employee_ID) {
		Employee_ID = employee_ID;
	}

	public String getFirst_Name() {
		return First_Name;
	}

	public void setFirst_Name(String first_Name) {
		First_Name = first_Name;
	}

	public String getLast_Name() {
		return Last_Name;
	}

	public void setLast_Name(String last_Name) {
		Last_Name = last_Name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPHONE_NUMBER() {
		return PHONE_NUMBER;
	}

	public void setPHONE_NUMBER(String pHONE_NUMBER) {
		PHONE_NUMBER = pHONE_NUMBER;
	}

	public Date getHIRE_DATE() {
		return HIRE_DATE;
	}

	public void setHIRE_DATE(Date hIRE_DATE) {
		HIRE_DATE = hIRE_DATE;
	}

	public String getJOB_ID() {
		return JOB_ID;
	}

	public void setJOB_ID(String jOB_ID) {
		JOB_ID = jOB_ID;
	}

	public double getSALARY() {
		return SALARY;
	}

	public void setSALARY(double sALARY) {
		SALARY = sALARY;
	}

	public double getCOMMISSION_PCT() {
		return COMMISSION_PCT;
	}

	public void setCOMMISSION_PCT(double cOMMISSION_PCT) {
		COMMISSION_PCT = cOMMISSION_PCT;
	}

	public int getMANAGER_ID() {
		return MANAGER_ID;
	}

	public void setMANAGER_ID(int mANAGER_ID) {
		MANAGER_ID = mANAGER_ID;
	}

	public int getDEPARTMENT_ID() {
		return DEPARTMENT_ID;
	}

	public void setDEPARTMENT_ID(int dEPARTMENT_ID) {
		DEPARTMENT_ID = dEPARTMENT_ID;
	}

	// Print Employee's Information
	@Override
	public String toString() {
		return "Employee_ID:" + Employee_ID + "|FIRST_NAME:" + First_Name + "|LAST_NAME:" + Last_Name + "|EMAIL:"
				+ Email + "|PHONE_NUMBER:" + PHONE_NUMBER + "|HIRE_DATE:" + HIRE_DATE + "|JOB_ID" + JOB_ID + "|SALARY"
				+ SALARY + "|COMMISSION_PCT" + COMMISSION_PCT + "|MANAGER_ID" + MANAGER_ID + "|DEPARTMENT_ID"
				+ DEPARTMENT_ID;
	}
}