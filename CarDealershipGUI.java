import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarDealershipGUI {

	public static void main(String[] args) {
	    JFrame mainFrame = new JFrame("Car Dealership Management System");
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setSize(600, 400);

	    JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
	    
	    JButton reportsButton = new JButton("Reports");
	    reportsButton.addActionListener(e -> showReportsGUI());
	    panel.add(reportsButton);

	    
	    JButton manageSalesButton = new JButton("Manage Sales");
	    manageSalesButton.addActionListener(e -> showManageSalesGUI());
	    panel.add(manageSalesButton);


	    JButton manageSuppliersButton = new JButton("Manage Suppliers");
	    manageSuppliersButton.addActionListener(e -> showManageSuppliersGUI());
	    panel.add(manageSuppliersButton);

	    JButton addCarButton = new JButton("Add Car");
	    JButton viewCarsButton = new JButton("View Cars");
	    JButton updateCarButton = new JButton("Update Car");
	    JButton deleteCarButton = new JButton("Delete Car");
	    JButton fileIOButton = new JButton("File I/O");

	    addCarButton.addActionListener(e -> showAddCarGUI());
	    viewCarsButton.addActionListener(e -> showCarsTable());
	    updateCarButton.addActionListener(e -> showUpdateCarGUI());
	    deleteCarButton.addActionListener(e -> showDeleteCarGUI());
	    fileIOButton.addActionListener(e -> showFileIOGUI());

	    panel.add(addCarButton);
	    panel.add(viewCarsButton);
	    panel.add(updateCarButton);
	    panel.add(deleteCarButton);
	    panel.add(fileIOButton);

	    mainFrame.add(panel);
	    mainFrame.setVisible(true);
	}

	public static void showManageSalesGUI() {
	    JFrame frame = new JFrame("Manage Sales");
	    frame.setSize(600, 400);

	    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
	    JLabel carIdLabel = new JLabel("Car ID:");
	    JTextField carIdField = new JTextField();
	    JLabel supplierIdLabel = new JLabel("Supplier ID:");
	    JTextField supplierIdField = new JTextField();
	    JLabel customerNameLabel = new JLabel("Customer Name:");
	    JTextField customerNameField = new JTextField();
	    JLabel salePriceLabel = new JLabel("Sale Price:");
	    JTextField salePriceField = new JTextField();

	    JButton addSaleButton = new JButton("Add Sale");
	    JTable salesTable = new JTable(new DefaultTableModel(new Object[]{"Sale ID", "Car ID", "Supplier ID", "Date", "Price", "Customer"}, 0));
	    JButton refreshButton = new JButton("Refresh Table");

	    addSaleButton.addActionListener(e -> {
	        try {
	            int carId = Integer.parseInt(carIdField.getText());
	            int supplierId = Integer.parseInt(supplierIdField.getText());
	            String customerName = customerNameField.getText();
	            double salePrice = Double.parseDouble(salePriceField.getText());

	            DatabaseManager.addSale(carId, supplierId, customerName, salePrice);
	            JOptionPane.showMessageDialog(frame, "Sale added successfully!");
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(frame, "Invalid input. Please check your data.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    refreshButton.addActionListener(e -> {
	        DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
	        model.setRowCount(0); // Clear existing rows
	        try (ResultSet rs = DatabaseManager.getAllSales()) {
	            while (rs.next()) {
	                model.addRow(new Object[]{
	                        rs.getInt("sale_id"),
	                        rs.getInt("car_id"),
	                        rs.getInt("supplier_id"),
	                        rs.getString("sale_date"),
	                        rs.getDouble("sale_price"),
	                        rs.getString("customer_name")
	                });
	            }
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(frame, "Error refreshing table: " + ex.getMessage());
	        }
	    });

	    panel.add(carIdLabel);
	    panel.add(carIdField);
	    panel.add(supplierIdLabel);
	    panel.add(supplierIdField);
	    panel.add(customerNameLabel);
	    panel.add(customerNameField);
	    panel.add(salePriceLabel);
	    panel.add(salePriceField);
	    panel.add(addSaleButton);
	    panel.add(refreshButton);

	    frame.add(new JScrollPane(salesTable), BorderLayout.CENTER);
	    frame.add(panel, BorderLayout.SOUTH);
	    frame.setVisible(true);
	}

	
	public static void showManageSuppliersGUI() {
	    JFrame frame = new JFrame("Manage Suppliers");
	    frame.setSize(600, 400);

	    JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
	    JLabel nameLabel = new JLabel("Name:");
	    JTextField nameField = new JTextField();
	    JLabel contactLabel = new JLabel("Contact Info:");
	    JTextField contactField = new JTextField();

	    JButton addButton = new JButton("Add Supplier");
	    JButton updateButton = new JButton("Update Supplier");
	    JButton deleteButton = new JButton("Delete Supplier");

	    JTable suppliersTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "Contact Info"}, 0));
	    JButton refreshButton = new JButton("Refresh Table");

	    addButton.addActionListener(e -> {
	        String name = nameField.getText();
	        String contactInfo = contactField.getText();
	        DatabaseManager.addSupplier(name, contactInfo);
	        JOptionPane.showMessageDialog(frame, "Supplier added successfully!");
	    });

	    updateButton.addActionListener(e -> {
	        int selectedRow = suppliersTable.getSelectedRow();
	        if (selectedRow >= 0) {
	            int supplierId = (int) suppliersTable.getValueAt(selectedRow, 0);
	            String name = nameField.getText();
	            String contactInfo = contactField.getText();
	            DatabaseManager.updateSupplier(supplierId, name, contactInfo);
	            JOptionPane.showMessageDialog(frame, "Supplier updated successfully!");
	        }
	    });

	    deleteButton.addActionListener(e -> {
	        int selectedRow = suppliersTable.getSelectedRow();
	        if (selectedRow >= 0) {
	            int supplierId = (int) suppliersTable.getValueAt(selectedRow, 0);
	            DatabaseManager.deleteSupplier(supplierId);
	            JOptionPane.showMessageDialog(frame, "Supplier deleted successfully!");
	        }
	    });

	    refreshButton.addActionListener(e -> {
	        DefaultTableModel model = (DefaultTableModel) suppliersTable.getModel();
	        model.setRowCount(0); // Clear existing rows
	        try (Connection conn = DatabaseManager.connect();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT * FROM Suppliers")) {
	            while (rs.next()) {
	                model.addRow(new Object[]{rs.getInt("supplier_id"), rs.getString("name"), rs.getString("contact_info")});
	            }
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(frame, "Error refreshing table: " + ex.getMessage());
	        }
	    });

	    panel.add(nameLabel);
	    panel.add(nameField);
	    panel.add(contactLabel);
	    panel.add(contactField);
	    panel.add(addButton);
	    panel.add(updateButton);
	    panel.add(deleteButton);
	    panel.add(refreshButton);

	    frame.add(new JScrollPane(suppliersTable), BorderLayout.CENTER);
	    frame.add(panel, BorderLayout.SOUTH);
	    frame.setVisible(true);
	}

	
	public static void showAddCarGUI() {
	    JFrame frame = new JFrame("Add Car");
	    frame.setSize(400, 400);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

	    JLabel brandLabel = new JLabel("Brand:");
	    JTextField brandField = new JTextField();
	    JLabel modelLabel = new JLabel("Model:");
	    JTextField modelField = new JTextField();
	    JLabel yearLabel = new JLabel("Year:");
	    JTextField yearField = new JTextField();
	    JLabel priceLabel = new JLabel("Price:");
	    JTextField priceField = new JTextField();
	    JLabel fuelTypeLabel = new JLabel("Fuel Type:");
	    JTextField fuelTypeField = new JTextField();
	    JLabel transmissionLabel = new JLabel("Transmission:");
	    JTextField transmissionField = new JTextField();
	    JLabel cargoCapacityLabel = new JLabel("Cargo Capacity:");
	    JTextField cargoCapacityField = new JTextField();
	    JLabel axleCountLabel = new JLabel("Axle Count:");
	    JTextField axleCountField = new JTextField();

	    JButton saveButton = new JButton("Save");
	    JButton clearButton = new JButton("Clear");

	    saveButton.addActionListener(e -> {
	        try {
	            String brand = brandField.getText();
	            String model = modelField.getText();
	            int year = Integer.parseInt(yearField.getText());
	            double price = Double.parseDouble(priceField.getText());
	            String fuelType = fuelTypeField.getText();
	            String transmission = transmissionField.getText();
	            Double cargoCapacity = cargoCapacityField.getText().isEmpty() ? null : Double.parseDouble(cargoCapacityField.getText());
	            Integer axleCount = axleCountField.getText().isEmpty() ? null : Integer.parseInt(axleCountField.getText());

	            DatabaseManager.addCar(brand, model, year, price, fuelType, transmission, cargoCapacity, axleCount);
	            JOptionPane.showMessageDialog(frame, "Car added successfully!");
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(frame, "Invalid input. Please check your data.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    clearButton.addActionListener(e -> {
	        brandField.setText("");
	        modelField.setText("");
	        yearField.setText("");
	        priceField.setText("");
	        fuelTypeField.setText("");
	        transmissionField.setText("");
	        cargoCapacityField.setText("");
	        axleCountField.setText("");
	    });

	    panel.add(brandLabel);
	    panel.add(brandField);
	    panel.add(modelLabel);
	    panel.add(modelField);
	    panel.add(yearLabel);
	    panel.add(yearField);
	    panel.add(priceLabel);
	    panel.add(priceField);
	    panel.add(fuelTypeLabel);
	    panel.add(fuelTypeField);
	    panel.add(transmissionLabel);
	    panel.add(transmissionField);
	    panel.add(cargoCapacityLabel);
	    panel.add(cargoCapacityField);
	    panel.add(axleCountLabel);
	    panel.add(axleCountField);
	    panel.add(saveButton);
	    panel.add(clearButton);

	    frame.add(panel);
	    frame.setVisible(true);
	}

	public static void showCarsTable() {
	    JFrame frame = new JFrame("View Cars");
	    frame.setSize(800, 500);

	    JPanel filterPanel = new JPanel(new GridLayout(2, 5, 10, 10));
	    JLabel brandLabel = new JLabel("Brand:");
	    JTextField brandField = new JTextField();
	    JLabel modelLabel = new JLabel("Model:");
	    JTextField modelField = new JTextField();
	    JLabel minPriceLabel = new JLabel("Min Price:");
	    JTextField minPriceField = new JTextField();
	    JLabel maxPriceLabel = new JLabel("Max Price:");
	    JTextField maxPriceField = new JTextField();
	    JLabel yearLabel = new JLabel("Year:");
	    JTextField yearField = new JTextField();

	    JButton filterButton = new JButton("Filter");
	    JButton resetButton = new JButton("Reset");

	    String[] columnNames = { "Car ID", "Brand", "Model", "Year", "Price", "Fuel Type", "Transmission", "Cargo Capacity", "Axle Count" };
	    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	    JTable carTable = new JTable(tableModel);

	    filterButton.addActionListener(e -> {
	        String brand = brandField.getText();
	        String model = modelField.getText();
	        String minPrice = minPriceField.getText();
	        String maxPrice = maxPriceField.getText();
	        String year = yearField.getText();

	        StringBuilder query = new StringBuilder("SELECT * FROM Cars WHERE 1=1");
	        if (!brand.isEmpty()) query.append(" AND brand LIKE '%").append(brand).append("%'");
	        if (!model.isEmpty()) query.append(" AND model LIKE '%").append(model).append("%'");
	        if (!minPrice.isEmpty()) query.append(" AND price >= ").append(Double.parseDouble(minPrice));
	        if (!maxPrice.isEmpty()) query.append(" AND price <= ").append(Double.parseDouble(maxPrice));
	        if (!year.isEmpty()) query.append(" AND year = ").append(Integer.parseInt(year));

	        tableModel.setRowCount(0); // Clear table
	        try (Connection connection = DatabaseManager.connect();
	             Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(query.toString())) {
	            while (resultSet.next()) {
	                int carId = resultSet.getInt("car_id");
	                String carBrand = resultSet.getString("brand");
	                String carModel = resultSet.getString("model");
	                int carYear = resultSet.getInt("year");
	                double carPrice = resultSet.getDouble("price");
	                String fuelType = resultSet.getString("fuel_type");
	                String transmission = resultSet.getString("transmission");
	                Double cargoCapacity = resultSet.getObject("cargo_capacity") != null ? resultSet.getDouble("cargo_capacity") : null;
	                Integer axleCount = resultSet.getObject("axle_count") != null ? resultSet.getInt("axle_count") : null;

	                tableModel.addRow(new Object[]{ carId, carBrand, carModel, carYear, carPrice, fuelType, transmission, 
	                        (cargoCapacity != null ? cargoCapacity : "N/A"), 
	                        (axleCount != null ? axleCount : "N/A") });
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    resetButton.addActionListener(e -> {
	        brandField.setText("");
	        modelField.setText("");
	        minPriceField.setText("");
	        maxPriceField.setText("");
	        yearField.setText("");
	        tableModel.setRowCount(0); // Clear table
	    });

	    filterPanel.add(brandLabel);
	    filterPanel.add(brandField);
	    filterPanel.add(modelLabel);
	    filterPanel.add(modelField);
	    filterPanel.add(minPriceLabel);
	    filterPanel.add(minPriceField);
	    filterPanel.add(maxPriceLabel);
	    filterPanel.add(maxPriceField);
	    filterPanel.add(yearLabel);
	    filterPanel.add(yearField);
	    filterPanel.add(filterButton);
	    filterPanel.add(resetButton);

	    frame.add(filterPanel, BorderLayout.NORTH);
	    frame.add(new JScrollPane(carTable), BorderLayout.CENTER);
	    frame.setVisible(true);
	}

	public static void showReportsGUI() {
	    JFrame frame = new JFrame("Reports");
	    frame.setSize(400, 300);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

	    JButton totalRevenueButton = new JButton("Total Sales Revenue");
	    JButton mostSoldBrandButton = new JButton("Most Sold Brand");
	    JButton mostActiveSupplierButton = new JButton("Most Active Supplier");

	    JLabel resultLabel = new JLabel("Results will be displayed here.");
	    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

	    totalRevenueButton.addActionListener(e -> {
	        double totalRevenue = DatabaseManager.getTotalSalesRevenue();
	        resultLabel.setText("Total Sales Revenue: $" + totalRevenue);
	    });

	    mostSoldBrandButton.addActionListener(e -> {
	        String mostSoldBrand = DatabaseManager.getMostSoldBrand();
	        resultLabel.setText("Most Sold Brand: " + mostSoldBrand);
	    });

	    mostActiveSupplierButton.addActionListener(e -> {
	        String mostActiveSupplier = DatabaseManager.getMostActiveSupplier();
	        resultLabel.setText("Most Active Supplier: " + mostActiveSupplier);
	    });

	    panel.add(totalRevenueButton);
	    panel.add(mostSoldBrandButton);
	    panel.add(mostActiveSupplierButton);
	    panel.add(resultLabel);

	    frame.add(panel);
	    frame.setVisible(true);
	}


	public static void showUpdateCarGUI() {
	    JFrame frame = new JFrame("Update Car");
	    frame.setSize(400, 400);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

	    JLabel idLabel = new JLabel("Car ID:");
	    JTextField idField = new JTextField();
	    JLabel brandLabel = new JLabel("Brand:");
	    JTextField brandField = new JTextField();
	    JLabel modelLabel = new JLabel("Model:");
	    JTextField modelField = new JTextField();
	    JLabel yearLabel = new JLabel("Year:");
	    JTextField yearField = new JTextField();
	    JLabel priceLabel = new JLabel("Price:");
	    JTextField priceField = new JTextField();
	    JLabel fuelTypeLabel = new JLabel("Fuel Type:");
	    JTextField fuelTypeField = new JTextField();
	    JLabel transmissionLabel = new JLabel("Transmission:");
	    JTextField transmissionField = new JTextField();
	    JLabel cargoCapacityLabel = new JLabel("Cargo Capacity:");
	    JTextField cargoCapacityField = new JTextField();
	    JLabel axleCountLabel = new JLabel("Axle Count:");
	    JTextField axleCountField = new JTextField();

	    JButton updateButton = new JButton("Update");

	    updateButton.addActionListener(e -> {
	        try {
	            int carId = Integer.parseInt(idField.getText());
	            String brand = brandField.getText();
	            String model = modelField.getText();
	            int year = Integer.parseInt(yearField.getText());
	            double price = Double.parseDouble(priceField.getText());
	            String fuelType = fuelTypeField.getText();
	            String transmission = transmissionField.getText();
	            Double cargoCapacity = cargoCapacityField.getText().isEmpty() ? null : Double.parseDouble(cargoCapacityField.getText());
	            Integer axleCount = axleCountField.getText().isEmpty() ? null : Integer.parseInt(axleCountField.getText());

	            DatabaseManager.updateCar(carId, brand, model, year, price, fuelType, transmission, cargoCapacity, axleCount);
	            JOptionPane.showMessageDialog(frame, "Car updated successfully!");
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(frame, "Invalid input. Please check your data.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    panel.add(idLabel);
	    panel.add(idField);
	    panel.add(brandLabel);
	    panel.add(brandField);
	    panel.add(modelLabel);
	    panel.add(modelField);
	    panel.add(yearLabel);
	    panel.add(yearField);
	    panel.add(priceLabel);
	    panel.add(priceField);
	    panel.add(fuelTypeLabel);
	    panel.add(fuelTypeField);
	    panel.add(transmissionLabel);
	    panel.add(transmissionField);
	    panel.add(cargoCapacityLabel);
	    panel.add(cargoCapacityField);
	    panel.add(axleCountLabel);
	    panel.add(axleCountField);
	    panel.add(updateButton);

	    frame.add(panel);
	    frame.setVisible(true);
	}

	public static void showDeleteCarGUI() {
	    JFrame frame = new JFrame("Delete Car");
	    frame.setSize(400, 200);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

	    JLabel idLabel = new JLabel("Car ID:");
	    JTextField idField = new JTextField();
	    JButton deleteButton = new JButton("Delete");

	    deleteButton.addActionListener(e -> {
	        try {
	            int carId = Integer.parseInt(idField.getText());
	            DatabaseManager.deleteCar(carId);
	            JOptionPane.showMessageDialog(frame, "Car deleted successfully!");
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid Car ID.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    panel.add(idLabel);
	    panel.add(idField);
	    panel.add(deleteButton);

	    frame.add(panel);
	    frame.setVisible(true);
	}

	public static void showFileIOGUI() {
	    JFrame frame = new JFrame("File I/O");
	    frame.setSize(400, 200);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

	    JButton exportButton = new JButton("Export Cars to CSV");
	    JButton importButton = new JButton("Import Cars from CSV");

	    exportButton.addActionListener(e -> {
	        JFileChooser fileChooser = new JFileChooser();
	        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	            DatabaseManager.exportCarsToCSV(filePath);
	        }
	    });

	    importButton.addActionListener(e -> {
	        JFileChooser fileChooser = new JFileChooser();
	        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
	            DatabaseManager.importCarsFromCSV(filePath);
	        }
	    });

	    panel.add(exportButton);
	    panel.add(importButton);

	    frame.add(panel);
	    frame.setVisible(true);
	}
}
