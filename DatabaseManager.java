import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

	private static final String DB_URL = "jdbc:sqlite:inventory.db";

	public static Connection connect() {
	    Connection connection = null;
	    try {
	        Class.forName("org.sqlite.JDBC"); 
	        connection = DriverManager.getConnection(DB_URL);
	        System.out.println("Connection to SQLite has been established.");
	    } catch (ClassNotFoundException e) {
	        System.out.println("SQLite JDBC Driver not found: " + e.getMessage());
	    } catch (SQLException e) {
	        System.out.println("Connection failed: " + e.getMessage());
	    }
	    return connection;
	}

	public static void initializeDatabase() {
	    String createCarsTable = "CREATE TABLE IF NOT EXISTS Cars (" +
	            "car_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	            "brand TEXT NOT NULL," +
	            "model TEXT NOT NULL," +
	            "year INTEGER NOT NULL," +
	            "price REAL NOT NULL," +
	            "fuel_type TEXT," +
	            "transmission TEXT," +
	            "cargo_capacity REAL," +
	            "axle_count INTEGER" +
	            ");";

	    String createSuppliersTable = "CREATE TABLE IF NOT EXISTS Suppliers (" +
	            "supplier_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	            "name TEXT NOT NULL," +
	            "contact_info TEXT" +
	            ");";
	    
	    String createSalesTable = "CREATE TABLE IF NOT EXISTS Sales (" +
	            "sale_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	            "car_id INTEGER NOT NULL," +
	            "supplier_id INTEGER NOT NULL," +
	            "sale_date DATE NOT NULL," +
	            "sale_price REAL NOT NULL," +
	            "customer_name TEXT NOT NULL," +
	            "FOREIGN KEY (car_id) REFERENCES Cars(car_id)," +
	            "FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id)" +
	            ");";

	  


	    try (Connection connection = connect(); Statement statement = connection.createStatement()) {
	        statement.execute(createCarsTable);
	        statement.execute(createSuppliersTable);
	        statement.execute(createSalesTable);
	        System.out.println("Database tables have been initialized.");
	    } catch (SQLException e) {
	        System.out.println("Database initialization failed: " + e.getMessage());
	    }
	}

	public static void addSupplier(String name, String contactInfo) {
	    String sql = "INSERT INTO Suppliers (name, contact_info) VALUES (?, ?)";
	    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, name);
	        pstmt.setString(2, contactInfo);
	        pstmt.executeUpdate();
	        System.out.println("Supplier added successfully.");
	    } catch (SQLException e) {
	        System.out.println("Error adding supplier: " + e.getMessage());
	    }
	}
	
	public static void updateSupplier(int supplierId, String name, String contactInfo) {
	    String sql = "UPDATE Suppliers SET name = ?, contact_info = ? WHERE supplier_id = ?";
	    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, name);
	        pstmt.setString(2, contactInfo);
	        pstmt.setInt(3, supplierId);
	        pstmt.executeUpdate();
	        System.out.println("Supplier updated successfully.");
	    } catch (SQLException e) {
	        System.out.println("Error updating supplier: " + e.getMessage());
	    }
	}
	
	public static void deleteSupplier(int supplierId) {
	    String sql = "DELETE FROM Suppliers WHERE supplier_id = ?";
	    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, supplierId);
	        pstmt.executeUpdate();
	        System.out.println("Supplier deleted successfully.");
	    } catch (SQLException e) {
	        System.out.println("Error deleting supplier: " + e.getMessage());
	    }
	}
	
	public static void addSale(int carId, int supplierId, String customerName, double salePrice) {
	    String sql = "INSERT INTO Sales (car_id, supplier_id, sale_date, sale_price, customer_name) VALUES (?, ?, date('now'), ?, ?)";
	    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, carId);
	        pstmt.setInt(2, supplierId);
	        pstmt.setDouble(3, salePrice);
	        pstmt.setString(4, customerName);
	        pstmt.executeUpdate();
	        System.out.println("Sale added successfully.");
	    } catch (SQLException e) {
	        System.out.println("Error adding sale: " + e.getMessage());
	    }
	}

	public static ResultSet getAllSales() {
	    String sql = "SELECT * FROM Sales";
	    try {
	        Connection conn = connect();
	        Statement stmt = conn.createStatement();
	        return stmt.executeQuery(sql);
	    } catch (SQLException e) {
	        System.out.println("Error retrieving sales: " + e.getMessage());
	        return null;
	    }
	}

	public static double getTotalSalesRevenue() {
	    String sql = "SELECT SUM(sale_price) AS total_revenue FROM Sales";
	    try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
	        if (rs.next()) {
	            return rs.getDouble("total_revenue");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error calculating total revenue: " + e.getMessage());
	    }
	    return 0.0;
	}

	
	public static String getMostSoldBrand() {
	    String sql = "SELECT Cars.brand, COUNT(Sales.sale_id) AS sales_count " +
	                 "FROM Sales " +
	                 "INNER JOIN Cars ON Sales.car_id = Cars.car_id " +
	                 "GROUP BY Cars.brand " +
	                 "ORDER BY sales_count DESC " +
	                 "LIMIT 1";
	    try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
	        if (rs.next()) {
	            return rs.getString("brand") + " (" + rs.getInt("sales_count") + " sales)";
	        }
	    } catch (SQLException e) {
	        System.out.println("Error finding most sold brand: " + e.getMessage());
	    }
	    return "No data available";
	}

	
	public static String getMostActiveSupplier() {
	    String sql = "SELECT Suppliers.name, COUNT(Sales.sale_id) AS sales_count " +
	                 "FROM Sales " +
	                 "INNER JOIN Suppliers ON Sales.supplier_id = Suppliers.supplier_id " +
	                 "GROUP BY Suppliers.name " +
	                 "ORDER BY sales_count DESC " +
	                 "LIMIT 1";
	    try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
	        if (rs.next()) {
	            return rs.getString("name") + " (" + rs.getInt("sales_count") + " sales)";
	        }
	    } catch (SQLException e) {
	        System.out.println("Error finding most active supplier: " + e.getMessage());
	    }
	    return "No data available";
	}

	

	public static void addCar(String brand, String model, int year, double price, String fuelType, String transmission, Double cargoCapacity, Integer axleCount) {
	    String insertCarSQL = "INSERT INTO Cars (brand, model, year, price, fuel_type, transmission, cargo_capacity, axle_count) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

	    try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(insertCarSQL)) {
	        preparedStatement.setString(1, brand);
	        preparedStatement.setString(2, model);
	        preparedStatement.setInt(3, year);
	        preparedStatement.setDouble(4, price);
	        preparedStatement.setString(5, fuelType);
	        preparedStatement.setString(6, transmission);
	        preparedStatement.setObject(7, cargoCapacity); // Nullable field
	        preparedStatement.setObject(8, axleCount); // Nullable field

	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Car added successfully.");
	        } else {
	            System.out.println("Failed to add car.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error adding car: " + e.getMessage());
	    }
	}

	public static void updateCar(int carId, String brand, String model, int year, double price, String fuelType, String transmission, Double cargoCapacity, Integer axleCount) {
	    String updateCarSQL = "UPDATE Cars SET brand = ?, model = ?, year = ?, price = ?, fuel_type = ?, transmission = ?, cargo_capacity = ?, axle_count = ? WHERE car_id = ?;";

	    try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(updateCarSQL)) {
	        preparedStatement.setString(1, brand);
	        preparedStatement.setString(2, model);
	        preparedStatement.setInt(3, year);
	        preparedStatement.setDouble(4, price);
	        preparedStatement.setString(5, fuelType);
	        preparedStatement.setString(6, transmission);
	        preparedStatement.setObject(7, cargoCapacity);
	        preparedStatement.setObject(8, axleCount);
	        preparedStatement.setInt(9, carId);

	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Car updated successfully.");
	        } else {
	            System.out.println("Failed to update car.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error updating car: " + e.getMessage());
	    }
	}

	public static void deleteCar(int carId) {
	    String deleteCarSQL = "DELETE FROM Cars WHERE car_id = ?;";

	    try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(deleteCarSQL)) {
	        preparedStatement.setInt(1, carId);

	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Car deleted successfully.");
	        } else {
	            System.out.println("Failed to delete car.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error deleting car: " + e.getMessage());
	    }
	}

	public static void exportCarsToCSV(String filePath) {
	    String sql = "SELECT * FROM Cars";
	    try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql); FileWriter writer = new FileWriter(filePath)) {
	        writer.write("car_id,brand,model,year,price,fuel_type,transmission,cargo_capacity,axle_count\n");
	        while (resultSet.next()) {
	            writer.write(resultSet.getInt("car_id") + "," +
	                    resultSet.getString("brand") + "," +
	                    resultSet.getString("model") + "," +
	                    resultSet.getInt("year") + "," +
	                    resultSet.getDouble("price") + "," +
	                    resultSet.getString("fuel_type") + "," +
	                    resultSet.getString("transmission") + "," +
	                    (resultSet.getObject("cargo_capacity") != null ? resultSet.getDouble("cargo_capacity") : "") + "," +
	                    (resultSet.getObject("axle_count") != null ? resultSet.getInt("axle_count") : "") + "\n");
	        }
	        System.out.println("Cars exported successfully to " + filePath);
	    } catch (Exception e) {
	        System.out.println("Error exporting cars: " + e.getMessage());
	    }
	}

	public static void importCarsFromCSV(String filePath) {
	    String insertCarSQL = "INSERT INTO Cars (brand, model, year, price, fuel_type, transmission, cargo_capacity, axle_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath)); Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(insertCarSQL)) {
	        String line = reader.readLine(); // Skip header
	        while ((line = reader.readLine()) != null) {
	            String[] fields = line.split(",");
	            preparedStatement.setString(1, fields[1]);
	            preparedStatement.setString(2, fields[2]);
	            preparedStatement.setInt(3, Integer.parseInt(fields[3]));
	            preparedStatement.setDouble(4, Double.parseDouble(fields[4]));
	            preparedStatement.setString(5, fields[5]);
	            preparedStatement.setString(6, fields[6]);
	            preparedStatement.setObject(7, fields[7].isEmpty() ? null : Double.parseDouble(fields[7]));
	            preparedStatement.setObject(8, fields[8].isEmpty() ? null : Integer.parseInt(fields[8]));
	            preparedStatement.executeUpdate();
	        }
	        System.out.println("Cars imported successfully from " + filePath);
	    } catch (Exception e) {
	        System.out.println("Error importing cars: " + e.getMessage());
	    }
	}

	public static void viewAllCars() {
	    String selectAllCarsSQL = "SELECT * FROM Cars;";

	    try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(selectAllCarsSQL)) {
	        while (resultSet.next()) {
	            int carId = resultSet.getInt("car_id");
	            String brand = resultSet.getString("brand");
	            String model = resultSet.getString("model");
	            int year = resultSet.getInt("year");
	            double price = resultSet.getDouble("price");
	            String fuelType = resultSet.getString("fuel_type");
	            String transmission = resultSet.getString("transmission");
	            Double cargoCapacity = resultSet.getObject("cargo_capacity") != null ? resultSet.getDouble("cargo_capacity") : null;
	            Integer axleCount = resultSet.getObject("axle_count") != null ? resultSet.getInt("axle_count") : null;

	            System.out.println("Car ID: " + carId + ", Brand: " + brand + ", Model: " + model + ", Year: " + year + ", Price: " + price + ", Fuel Type: " + fuelType + ", Transmission: " + transmission + ", Cargo Capacity: " + (cargoCapacity != null ? cargoCapacity : "N/A") + ", Axle Count: " + (axleCount != null ? axleCount : "N/A"));
	        }
	    } catch (SQLException e) {
	        System.out.println("Error retrieving cars: " + e.getMessage());
	    }
	}

	public static void main(String[] args) {
	    initializeDatabase();
	}
}
