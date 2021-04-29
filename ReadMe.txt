CJV805 – Assignment 1 – Summer 2020 - Group 2

Lab Assignment Submission Form
==========================================================================

We declare that the attached assignment is our own work in accordance with Seneca Academic Policy. No
part of this assignment has been copied manually or electronically from any other source (including web sites) or
distributed to other students.

Group members:
	Name 1: Yevhen Salitrynskyi
	Student ID: 131945198
	
	Name 2: Mahshid Farrahinia
	Student ID: 144091196
	
	Name 3: Chao Chen
	Student ID: 120162193


Explanation:
	DBAccessHelper.java:
		Empty file, was created according to the assignment task.
		
	DBUtilities.java:
		Class which provide functionality of loading properties file,
        printing warnings, exceptions and also build the Oracle database connection through thin and OCI based on informations on database.properties file.
        
    Employee.java:    
        Employee class which is related to employees table in database. It meets the requirements for JavaBeans
        like implementing serializable interface and containing private attributes with a zero argument constructor and including related getters and setters.
		
	DAManager.java:
		The DAManager.java is a data access manager class provides the interface that is used to manipulate data in the database without exposing details of the
		database. 
		
	HRManagement.java:
		The HRManagement class is a test class that mimics an application. You can run it to test all methods from the DAManager.java class.