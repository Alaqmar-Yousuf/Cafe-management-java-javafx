package com.cafebliss;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.Optional;

public class InventoryController {
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML private TableColumn<InventoryItem, String> unitColumn;
    @FXML private TableColumn<InventoryItem, Integer> minStockColumn;
    @FXML private TableColumn<InventoryItem, Double> priceColumn;
    @FXML private TableColumn<InventoryItem, String> statusColumn;
    @FXML private Label statusLabel;

    private ObservableList<InventoryItem> inventoryItems;

    @FXML
    public void initialize() {
        inventoryItems = FXCollections.observableArrayList();
        
        // Initialize with some sample data
        inventoryItems.add(new InventoryItem("Coffee Beans", 50, 20, "kg", 15.99));
        inventoryItems.add(new InventoryItem("Milk", 100, 30, "liters", 2.99));
        inventoryItems.add(new InventoryItem("Sugar", 75, 25, "kg", 1.99));
        
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        minStockColumn.setCellValueFactory(new PropertyValueFactory<>("minimumStock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        
        // Custom cell factory for status column
        statusColumn.setCellValueFactory(cellData -> {
            InventoryItem item = cellData.getValue();
            String status = item.isLowStock() ? "Low Stock" : "OK";
            return new SimpleStringProperty(status);
        });
        
        // Set cell factory for status column to show different colors
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("Low Stock".equals(status)) {
                        setStyle("-fx-text-fill: #FF6B6B;");
                    } else {
                        setStyle("-fx-text-fill: #4CAF50;");
                    }
                }
            }
        });

        inventoryTable.setItems(inventoryItems);
        updateStatusLabel();
    }

    @FXML
    private void handleAddItem() {
        Dialog<Pair<InventoryItem, Boolean>> dialog = new Dialog<>();
        dialog.setTitle("Add Inventory Item");
        dialog.setHeaderText("Enter item details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField quantityField = new TextField();
        TextField minStockField = new TextField();
        TextField unitField = new TextField();
        TextField priceField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Minimum Stock:"), 0, 2);
        grid.add(minStockField, 1, 2);
        grid.add(new Label("Unit:"), 0, 3);
        grid.add(unitField, 1, 3);
        grid.add(new Label("Price per Unit:"), 0, 4);
        grid.add(priceField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    int minStock = Integer.parseInt(minStockField.getText());
                    String unit = unitField.getText();
                    double price = Double.parseDouble(priceField.getText());

                    if (name.isEmpty() || quantity < 0 || minStock < 0 || unit.isEmpty() || price < 0) {
                        showAlert("Error", "Please enter valid values for all fields.");
                        return null;
                    }

                    return new Pair<>(new InventoryItem(name, quantity, minStock, unit, price), true);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter valid numbers for quantity, minimum stock, and price.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            inventoryItems.add(result.getKey());
            updateStatusLabel();
        });
    }

    @FXML
    private void handleEditItem() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to edit.");
            return;
        }

        Dialog<Pair<InventoryItem, Boolean>> dialog = new Dialog<>();
        dialog.setTitle("Edit Inventory Item");
        dialog.setHeaderText("Edit item details");

        ButtonType editButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(selectedItem.getName());
        TextField quantityField = new TextField(String.valueOf(selectedItem.getQuantity()));
        TextField minStockField = new TextField(String.valueOf(selectedItem.getMinimumStock()));
        TextField unitField = new TextField(selectedItem.getUnit());
        TextField priceField = new TextField(String.valueOf(selectedItem.getPricePerUnit()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Minimum Stock:"), 0, 2);
        grid.add(minStockField, 1, 2);
        grid.add(new Label("Unit:"), 0, 3);
        grid.add(unitField, 1, 3);
        grid.add(new Label("Price per Unit:"), 0, 4);
        grid.add(priceField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                try {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    int minStock = Integer.parseInt(minStockField.getText());
                    String unit = unitField.getText();
                    double price = Double.parseDouble(priceField.getText());

                    if (name.isEmpty() || quantity < 0 || minStock < 0 || unit.isEmpty() || price < 0) {
                        showAlert("Error", "Please enter valid values for all fields.");
                        return null;
                    }

                    selectedItem.setName(name);
                    selectedItem.setQuantity(quantity);
                    selectedItem.setMinimumStock(minStock);
                    selectedItem.setUnit(unit);
                    selectedItem.setPricePerUnit(price);

                    return new Pair<>(selectedItem, true);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter valid numbers for quantity, minimum stock, and price.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            inventoryTable.refresh();
            updateStatusLabel();
        });
    }

    @FXML
    private void handleDeleteItem() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to delete.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Inventory Item");
        confirmDialog.setContentText("Are you sure you want to delete " + selectedItem.getName() + "?");

        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            inventoryItems.remove(selectedItem);
            updateStatusLabel();
        }
    }

    @FXML
    private void handleRestock() {
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to restock.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Restock Item");
        dialog.setHeaderText("Restock " + selectedItem.getName());
        dialog.setContentText("Enter quantity to add:");

        dialog.showAndWait().ifPresent(quantityStr -> {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity > 0) {
                    selectedItem.addStock(quantity);
                    inventoryTable.refresh();
                    updateStatusLabel();
                } else {
                    showAlert("Error", "Please enter a positive quantity.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.");
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) inventoryTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CafeView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Management System");
        } catch (Exception e) {
            showAlert("Error", "Failed to return to main screen.");
        }
    }

    private void updateStatusLabel() {
        int lowStockCount = (int) inventoryItems.stream().filter(InventoryItem::isLowStock).count();
        statusLabel.setText(String.format("Total Items: %d | Low Stock Items: %d", 
            inventoryItems.size(), lowStockCount));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 