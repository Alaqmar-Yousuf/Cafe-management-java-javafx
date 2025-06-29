package com.cafebliss;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsController {
    @FXML private ComboBox<String> periodComboBox;
    @FXML private TableView<SalesReport> salesTable;
    @FXML private TableColumn<SalesReport, String> dateColumn;
    @FXML private TableColumn<SalesReport, Double> revenueColumn;
    @FXML private TableColumn<SalesReport, Integer> itemsSoldColumn;
    @FXML private BarChart<String, Number> popularItemsChart;
    @FXML private TextArea reportTextArea;

    private ObservableList<SalesReport> salesReports;
    private List<Order> allOrders;

    @FXML
    public void initialize() {
        // Initialize period options
        periodComboBox.setItems(FXCollections.observableArrayList(
            "Today", "This Week", "This Month", "Last Month", "All Time"
        ));
        periodComboBox.getSelectionModel().selectFirst();

        // Initialize table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        itemsSoldColumn.setCellValueFactory(new PropertyValueFactory<>("totalItemsSold"));

        // Initialize data
        salesReports = FXCollections.observableArrayList();
        allOrders = new ArrayList<>(); // This would typically be loaded from a database

        // Add some sample data
        addSampleData();

        salesTable.setItems(salesReports);
    }

    private void addSampleData() {
        // Add sample orders and sales reports
        LocalDateTime now = LocalDateTime.now();
        
        // Create sample orders for the last 30 days
        for (int i = 0; i < 30; i++) {
            Order order = new Order();
            order.addItem(new MenuItem("Coffee", 3.50), 2);
            order.addItem(new MenuItem("Sandwich", 5.99), 1);
            allOrders.add(order);

            SalesReport report = new SalesReport(now.minusDays(i));
            report.addItem(new MenuItem("Coffee", 3.50), 2);
            report.addItem(new MenuItem("Sandwich", 5.99), 1);
            salesReports.add(report);
        }
    }

    @FXML
    private void handleGenerateReport() {
        String period = periodComboBox.getValue();
        LocalDateTime startDate = getStartDate(period);
        
        // Filter sales reports based on period
        List<SalesReport> filteredReports = salesReports.stream()
            .filter(report -> report.getDate().isAfter(startDate))
            .collect(Collectors.toList());

        // Update table
        salesTable.setItems(FXCollections.observableArrayList(filteredReports));

        // Update popular items chart
        updatePopularItemsChart(filteredReports);

        // Generate report text
        generateReportText(filteredReports);
    }

    private LocalDateTime getStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        switch (period) {
            case "Today":
                return now.truncatedTo(ChronoUnit.DAYS);
            case "This Week":
                return now.minusDays(7);
            case "This Month":
                return now.minusDays(30);
            case "Last Month":
                return now.minusDays(60);
            default:
                return now.minusYears(1);
        }
    }

    private void updatePopularItemsChart(List<SalesReport> reports) {
        // Clear existing data
        popularItemsChart.getData().clear();

        // Calculate total quantities for each item
        Map<String, Integer> itemQuantities = new HashMap<>();
        for (SalesReport report : reports) {
            report.getItemsSold().forEach((item, quantity) -> 
                itemQuantities.merge(item.getName(), quantity, Integer::sum));
        }

        // Create chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Items Sold");

        // Sort items by quantity and take top 5
        itemQuantities.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        popularItemsChart.getData().add(series);
    }

    private void generateReportText(List<SalesReport> reports) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sales Report - ").append(periodComboBox.getValue()).append("\n\n");

        double totalRevenue = reports.stream().mapToDouble(SalesReport::getTotalRevenue).sum();
        int totalItems = reports.stream().mapToInt(SalesReport::getTotalItemsSold).sum();

        sb.append("Summary:\n");
        sb.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
        sb.append("Total Items Sold: ").append(totalItems).append("\n");
        sb.append("Average Order Value: $").append(String.format("%.2f", totalRevenue / reports.size())).append("\n\n");

        sb.append("Detailed Report:\n");
        reports.forEach(report -> sb.append(report.toString()).append("\n"));

        reportTextArea.setText(sb.toString());
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("sales_report.txt");

        File file = fileChooser.showSaveDialog(reportTextArea.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(reportTextArea.getText());
                showAlert("Success", "Report exported successfully!");
            } catch (IOException e) {
                showAlert("Error", "Failed to export report: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlePrint() {
        // In a real application, this would integrate with the system's print dialog
        showAlert("Print", "Print functionality would be implemented here.");
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) reportTextArea.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CafeView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Management System");
        } catch (Exception e) {
            showAlert("Error", "Failed to return to main screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 