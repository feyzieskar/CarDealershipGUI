# CarDealershipGUI
Project Report: Inventory Management System

1.	Project Overview
1.1 Objective

The aim of our project is to develop an Inventory Management System (IMS) for a small car dealership. This system will enable the dealership to manage its vehicle inventory, sales records, and supplier information effectively.

Using Java Swing for the graphical user interface (GUI), the system will provide user-friendly navigation for tasks such as adding, updating, and deleting vehicles, tracking sales, and managing supplier data. A relational database will ensure secure data storage and support CRUD operations. Additionally, the system will include file import/export functionality to enhance data portability and ensure seamless information management.

1.2 Store Type

Our team selected a car dealership as the store type for this project. Car dealerships face unique challenges in managing inventory, which includes high-value items with detailed specifications.
	•	Why a Car Dealership?
A car dealership requires an advanced IMS due to the complexity of its operations, which include:
	•	Detailed Inventory Management: Each car has unique attributes, such as make, model, year, engine type, fuel type, color, mileage, and price. The IMS will track these attributes efficiently.
	•	Supplier Coordination: Managing relationships with multiple suppliers and tracking vehicle delivery schedules is critical.
	•	Sales Integration: The system will monitor sales records, including customer details, transaction dates, and payment methods.
	•	Customized Filtering and Search: Users will be able to search for vehicles by make, model, price range, and other criteria, making inventory management and customer service more efficient.

The system’s customization for a car dealership will include features such as a visually organized GUI, a database schema designed for vehicle attributes, and tools to analyze sales trends. This tailored approach will provide the dealership with a practical solution for its inventory management needs.

2.	Design and Architecture
2.1	System Architecture
System Layers
1.	Graphical User Interface (GUI):
o	Purpose: Enables users to interact with the system.
o	Technology: Java Swing.
o	Functions:
	Vehicle Operations: Add, update, delete vehicles.
	Supplier Management: Add new suppliers, edit or delete existing ones.
	Sales Management: Record vehicle sales and view sales history.
	Search and Filter: Search for vehicles based on criteria like brand, model, or price range.
2.	Business Logic Layer:
o	Purpose:
	Validates user inputs and manages CRUD (Create, Read, Update, Delete) operations.
	Acts as a bridge between the GUI and the database.
o	Features:
	Prepares and executes SQL queries for database operations.
	Captures errors and sends appropriate error messages to the GUI.
	Input validation (e.g., ensuring vehicle price is not negative).
3.	Database Layer:
o	Purpose: Safely stores all data.
o	Technology: SQLite integrated with JDBC.
o	Structure:
	Cars Table: Stores vehicle information (e.g., brand, model, price).
	Suppliers Table: Stores supplier information (e.g., name, contact details).
	Sales Table: Stores sales records (e.g., sale date, vehicle, price).
o	Relationships:
	The Sales table has a Foreign Key relationship with the Cars table.



Workflow
1.	Adding a Vehicle:
o	The user fills out a form in the GUI (e.g., brand: Toyota, model: Corolla, price: 500,000 TL).
o	The business logic layer validates the input (e.g., ensures price is not negative).
o	If valid, an SQL query is prepared and executed to add the vehicle to the database.
o	A success message is returned to the user.
2.	Searching and Filtering Vehicles:
o	The user filters vehicles based on criteria like brand or price range.
o	The business logic layer runs the corresponding SQL query.
o	Results are displayed in the GUI as a table.
3.	Recording a Sale:
o	The user completes a sales form (e.g., vehicle ID: 3, sale price: 480,000 TL).
o	The business logic layer ensures the sale is linked to a valid vehicle ID.
o	Once validated, the sale record is added to the Sales table.

System Diagram
[ User ]
|
v
[ GUI - Java Swing ]
- Add Vehicle
  - View Cars
  - Update Vehicle
  - Delete Vehicle
  - Manage Suppliers
  - Manage Sales
  - Search and Filter
  - Reports
|
v
[ Business Logic Layer ]
- CRUD Operations
- Input Validation
- Error Handling
|
v
[ Database Layer - SQLite ]
- Cars Table
- Suppliers Table
- Sales Table


2.2 Class Diagram
Key Classes and Their Roles:
1.	DatabaseManager
•	Methods:
o	connect()
o	initializeDatabase()
o	addCar(...)
o	updateCar(...)
o	deleteCar(...)
o	addSupplier(...)
o	updateSupplier(...)
o	deleteSupplier(...)
o	addSale(...)
o	getAllSales()
o	authenticateUser(...)
2.	CarDealershipGUI
•	Methods:
o	main(...)
o	showLoginGUI(...)
o	showMainMenu(...)
o	showCarsTable(...)
o	showManageSalesGUI(...)
o	showManageSuppliersGUI(...)

 
Class Diagram Description:
The class diagram for the Car Dealership Management System illustrates the primary classes and their relationships within the project. It is structured as follows:
1.	DatabaseManager:
o	This class is responsible for all interactions with the SQLite database.
o	It provides methods for CRUD operations across various tables (Cars, Suppliers, Sales) and manages database connections.
o	It includes specialized methods for tasks like user authentication and reporting (e.g., calculating total revenue, identifying the most active supplier).
2.	CarDealershipGUI:
o	This class serves as the primary interface between the user and the system.
o	It handles user interactions and provides a graphical user interface using Java Swing components.
o	It uses the DatabaseManager for database operations.
o	The GUI provides multiple screens for managing vehicles, suppliers, sales, and generating reports.



Purpose:
The class diagram captures the modular design of the system, highlighting the separation of concerns:
•	DatabaseManager encapsulates database logic.
•	CarDealershipGUI focuses on user interaction.


2.3 Database Schema 
Tables and Attributes


Cars
•	Purpose: Stores information about vehicles available in the dealership.
•	Attributes:
o	car_id (Primary Key) : INTEGER
o	brand : TEXT
o	model : TEXT
o	year : INTEGER
o	price : REAL
o	fuel_type : TEXT
o	transmission : TEXT
o	cargo_capacity : REAL (optional for trucks)
o	axle_count : INTEGER (optional for trucks)


Suppliers
•	Purpose: Stores details about vehicle suppliers.
•	Attributes:
o	supplier_id (Primary Key) : INTEGER
o	name : TEXT
o	contact_info : TEXT
Sales
•	Purpose: Records information about sales transactions.
•	Attributes:
o	sale_id (Primary Key) : INTEGER
o	car_id (Foreign Key referencing Cars.car_id) : INTEGER
o	supplier_id (Foreign Key referencing Suppliers.supplier_id) : INTEGER
o	sale_date : DATE
o	sale_price : REAL
o	customer_name : TEXT



Relationships
1.	One-to-Many Relationship:
o	A supplier can supply multiple cars:
	Suppliers.supplier_id → Cars.supplier_id
2.	One-to-One Relationship:
o	Each sale is associated with one car:
	Sales.car_id → Cars.car_id
3.	Many-to-One Relationship:
o	Multiple sales can involve the same supplier:
	Sales.supplier_id → Suppliers.supplier_id








Entity-Relationship Diagram (ERD) Description
Entities:
•	Cars: Contains vehicle details.
•	Suppliers: Represents suppliers providing the vehicles.
•	Sales: Tracks sales transactions.
Attributes:
•	Primary keys uniquely identify each row in their respective tables.
•	Foreign keys establish relationships among tables.

 

Entities and Relationships
Cars Table:
•	Tracks vehicle details.
•	Related to Sales through car_id.
Suppliers Table:
•	Stores supplier information.
•	Related to both Cars and Sales through supplier_id.
Sales Table:
•	Records transaction details.
•	Related to both Cars and Suppliers via foreign keys.


3. Features and Functionality
3.1. Key Features
•	Adding, updating, deleting, and viewing vehicle inventory.
•	Managing supplier details and linking suppliers to vehicle purchases.
•	Recording sales transactions and linking them to vehicles and suppliers.
•	Advanced search and filtering options for inventory management.
•	Generating reports, including:
•	Total sales revenue.
•	Most sold vehicle brand.
•	Most active supplier based on sales.
•	User authentication and role-based access (admin and regular users).
•	File I/O functionality for importing and exporting inventory data in CSV format.
3.2. Customization
Our system is tailored for a car dealership. This customization is evident in:
•	The inclusion of specific attributes like fuel type, transmission, cargo capacity, and axle count to address car-specific requirements.
•	A GUI designed for easy data entry and retrieval relevant to car inventory management.

4. Application Walkthrough
4.1. GUI Overview


 
•	Main Menu: Provides access to inventory, suppliers, sales, and reports.


 
•	Reports: Generate statistical insights, such as total revenue and top-performing suppliers.


 
•	Sales Management: Record sales transactions and generate sales reports.
 
•	Supplier Management: Manage supplier details and associate them with vehicles.
 
•	Vehicle Management: Add, update, delete, and view vehicles with detailed attributes.


 

•	File I/O: Export inventory data to CSV for backup or import data from CSV.

 
•	Search and Filter: Allows users to filter vehicles based on brand, model, price range, and year.





4.2. Sample Workflow
Adding a New Car to Inventory:
1.	Navigate to the "Add Car" screen.
2.	Enter the required details, such as brand, model, year, price, etc.
3.	Click the "Save" button to add the car to the inventory.
4.	Navigate to the "View Cars" screen to verify the addition.
Filtering Vehicles:
•	Use the "View Cars" screen.
•	Enter filter criteria such as price range or model.
•	Click "Filter" to view the results.

Recording a Sale:
•	Navigate to the "Manage Sales" screen.
•	Enter the vehicle ID, supplier ID, customer name, and sale price.
•	Click "Add Sale" to record the transaction.
Generating a Report:
•	Open the "Reports" screen.
•	Click on any report option (e.g., Total Revenue).
•	View the generated result.


5. Object-Oriented Principles
5.1. Use of Classes and Objects
•	DatabaseManager class manages all database-related operations such as connecting to the database and executing SQL queries.
•	CarDealershipGUI class provides the graphical user interface for user interaction.

5.2. Inheritance and Polymorphism
•	The structure supports inheritance by designing reusable methods in the DatabaseManager class.
•	Polymorphism is applied through methods like addCar and connect where the behavior adapts based on parameters or usage.
5.3. Interfaces and Abstract Classes
•	JDBC is used for database connectivity, leveraging interfaces provided by Java.

6. Database Integration
6.1. Database Operations
CRUD operations implemented:
•	Create: Adding a new car using the addCar method.
•	Read: Viewing all cars in a table using SQL SELECT queries.
•	Update: Modifying car details by specifying a car ID.
•	Delete: Removing a car from the inventory by its ID.

6.2. Sample Queries
Create:
INSERT INTO Cars (brand, model, year, price, fuel_type, transmission, cargo_capacity, axle_count)
VALUES ('Toyota', 'Corolla', 2021, 20000.0, 'Petrol', 'Automatic', NULL, NULL);

Read:
SELECT * FROM Cars;

Update:
UPDATE Cars SET price = 21000.0 WHERE car_id = 1;

Delete:
DELETE FROM Cars WHERE car_id = 1;

7. File I/O
7.1. Import/Export Functionality
The system includes features for exporting vehicle inventory to a CSV file and importing inventory data from a CSV file. These features enhance data portability and allow dealerships to maintain backups or migrate data efficiently.
Example use case:
•	A dealership wants to create a backup of their vehicle inventory. They can click on the "Export Cars to CSV" button, and all inventory data will be saved as a CSV file.
•	In case of database failure or migration to a new system, they can use the "Import Cars from CSV" button to reload their data.

7.2. Error Handling
•	The application uses try-catch blocks to handle SQLExceptions and input validation errors gracefully.
•	User-friendly error messages are displayed in the GUI.

8. Challenges and Solutions
Challenge: Ensuring seamless integration between GUI and database. 
Solution: We modularized the application into separate classes for GUI and database management.
Challenge: Handling null values in optional fields. 
Solution: Used nullable checks in methods and SQL statements.
9. Future Improvements
•	Adding advanced analytics and visualization tools.
•	Supporting additional file formats like JSON or XML for File I/O.
•	Expanding the system to include more granular user roles and permissions.
•	Implementing a mobile-friendly version of the application.

10. Conclusion
This project provided hands-on experience with database integration, GUI development, and object-oriented principles. It demonstrated the importance of modular design and teamwork in software development. The final product is a robust and user-friendly management system tailored to the needs of car dealerships.

Appendix
A. User Manual
Adding a Vehicle
1.	Navigate to the "Add Vehicle" screen from the main menu.
2.	Fill in the required fields, including:
o	Brand
o	Model
o	Year
o	Price
o	Optional fields: Fuel Type, Transmission, Cargo Capacity, Axle Count.
3.	Click "Save" to add the vehicle to the database.
Viewing and Filtering Vehicles
1.	Navigate to the "View Cars" screen.
2.	Enter filtering criteria (e.g., brand, price range, year) and click "Filter."
3.	To reset filters, click "Reset."
Managing Suppliers
1.	Navigate to the "Manage Suppliers" screen.
2.	Add a new supplier:
o	Enter the supplier's name and contact information.
o	Click "Add Supplier."
3.	Update or delete an existing supplier by selecting it from the table and using the respective buttons.
Recording Sales
1.	Navigate to the "Manage Sales" screen.
2.	Enter the details of the sale, including:
o	Vehicle ID
o	Supplier ID
o	Customer Name
o	Sale Price.
3.	Click "Add Sale" to record the transaction.
Generating Reports
1.	Navigate to the "Reports" screen.
2.	Select a report option:
o	Total Sales Revenue
o	Most Sold Brand
o	Most Active Supplier.
3.	View the report results displayed on the screen.
Exporting and Importing Data
1.	Navigate to the "File I/O" screen.
2.	Export data:
o	Click "Export to CSV."
o	Choose a location and save the file.
3.	Import data:
o	Click "Import from CSV."
o	Select a CSV file to upload.
Error Handling
•	If an invalid input is provided (e.g., non-numeric data in a numeric field), an error message will guide you to correct it.
•	For database-related errors, the application provides detailed messages to ensure you can resolve the issue.

B. References
•	SQLite JDBC Driver: https://github.com/xerial/sqlite-jdbc
•	Java Swing Documentation: https://docs.oracle.com/javase/tutorial/uiswing/







