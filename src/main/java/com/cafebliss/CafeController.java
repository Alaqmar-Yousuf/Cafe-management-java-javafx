package com.cafebliss;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class CafeController {
    @FXML private ListView<MenuItem> menuListView;
    @FXML private TextField quantityField;
    @FXML private Label totalLabel;
    @FXML private Button addButton;
    @FXML private Button placeOrderButton;
    @FXML private VBox rootPane;
    @FXML private TableView<OrderItem> orderTableView;
    @FXML private TableColumn<OrderItem, String> itemNameColumn;
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML private TableColumn<OrderItem, Double> priceColumn;
    @FXML private TableColumn<OrderItem, Double> totalColumn;
    @FXML private TableColumn<OrderItem, Void> actionsColumn;
    @FXML private Button logoutButton;
    @FXML private Button addItemButton;
    @FXML private Button editItemButton;
    @FXML private Button deleteItemButton;
    @FXML private Button inventoryButton;
    @FXML private Button analyticsButton;
    @FXML private Button tableButton;

    private ObservableList<MenuItem> menuItems;
    private ObservableList<OrderItem> orderItems;
    private Order order;

    public static class OrderItem {
        private final MenuItem menuItem;
        private int quantity;

        public OrderItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }

        public String getName() { return menuItem.getName(); }
        public double getPrice() { return menuItem.getPrice(); }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getTotal() { return menuItem.getPrice() * quantity; }
        public MenuItem getMenuItem() { return menuItem; }
    }

    @FXML
    public void initialize() {
        // Initialize menu items
        menuItems = FXCollections.observableArrayList(
            new MenuItem("Espresso", 3.50),
            new MenuItem("Cappuccino", 4.00),
            new MenuItem("Latte", 4.50),
            new MenuItem("Croissant", 2.50),
            new MenuItem("Muffin", 3.00)
        );
        menuListView.setItems(menuItems);
        order = new Order();
        orderItems = FXCollections.observableArrayList();

        // Set up TableView
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Make quantity column editable
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            OrderItem item = event.getRowValue();
            int newQuantity = event.getNewValue();
            if (newQuantity > 0 && newQuantity <= 100) {
                item.setQuantity(newQuantity);
                updateOrderFromTable();
            } else {
                showAlert("Error", "Quantity must be between 1 and 100");
                orderTableView.refresh();
            }
        });

        // Add remove button to actions column
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");
            {
                removeButton.setOnAction(event -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    orderItems.remove(item);
                    updateOrderFromTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });

        orderTableView.setItems(orderItems);

        // Set cell factory for menu ListView
        menuListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.toString());
            }
        });

        // Initialize UI components
        quantityField.setText("1");
        // Add input validation for quantity field
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        updateOrderSummary();
    }

    private void updateOrderFromTable() {
        order = new Order();
        for (OrderItem item : orderItems) {
            order.addItem(item.getMenuItem(), item.getQuantity());
        }
        updateOrderSummary();
    }

    @FXML
    private void handleAddItem() {
        MenuItem selectedItem = menuListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item from the menu.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert("Error", "Quantity must be a positive number.");
                return;
            }
            if (quantity > 100) {
                showAlert("Error", "Maximum quantity allowed is 100.");
                return;
            }

            // Check if item already exists in order
            for (OrderItem item : orderItems) {
                if (item.getMenuItem().getName().equals(selectedItem.getName())) {
                    int newQuantity = item.getQuantity() + quantity;
                    if (newQuantity > 100) {
                        showAlert("Error", "Total quantity cannot exceed 100.");
                        return;
                    }
                    item.setQuantity(newQuantity);
                    orderTableView.refresh();
                    updateOrderFromTable();
                    return;
                }
            }

            // Add new item
            orderItems.add(new OrderItem(selectedItem, quantity));
            updateOrderFromTable();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid quantity.");
        }
    }

    @FXML
    private void handlePlaceOrder() {
        if (order.isEmpty()) {
            showAlert("Error", "No items in the order.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Order");
        confirmDialog.setHeaderText("Place Order");
        confirmDialog.setContentText("Are you sure you want to place this order?");

        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            try {
                order.generateReceipt();
                if (order.isReceiptGenerated()) {
                    showAlert("Success", "Order placed successfully! Receipt generated as 'receipt.txt'.");
                    order = new Order();
                    orderItems.clear();
                    updateOrderSummary();
                }
            } catch (RuntimeException e) {
                showAlert("Error", "Failed to generate receipt: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
            Scene scene = new Scene(root, 400, 350);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Login");
        } catch (Exception e) {
            showAlert("Error", "Failed to return to login screen.");
        }
    }

    @FXML
    private void handleAddMenuItem() {
        Dialog<Pair<String, Double>> dialog = new Dialog<>();
        dialog.setTitle("Add Menu Item");
        dialog.setHeaderText("Enter item details");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    if (name.isEmpty() || price <= 0) {
                        showAlert("Error", "Name cannot be empty and price must be positive.");
                        return null;
                    }
                    return new Pair<>(name, price);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Price must be a valid number.");
                    return null;
                }
            }
            return null;
        });
        dialog.showAndWait().ifPresent(result -> {
            MenuItem newItem = new MenuItem(result.getKey(), result.getValue());
            menuItems.add(newItem);
        });
    }

    @FXML
    private void handleEditMenuItem() {
        MenuItem selectedItem = menuListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to edit.");
            return;
        }
        Dialog<Pair<String, Double>> dialog = new Dialog<>();
        dialog.setTitle("Edit Menu Item");
        dialog.setHeaderText("Edit item details");
        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField nameField = new TextField(selectedItem.getName());
        TextField priceField = new TextField(String.valueOf(selectedItem.getPrice()));
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    if (name.isEmpty() || price <= 0) {
                        showAlert("Error", "Name cannot be empty and price must be positive.");
                        return null;
                    }
                    return new Pair<>(name, price);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Price must be a valid number.");
                    return null;
                }
            }
            return null;
        });
        dialog.showAndWait().ifPresent(result -> {
            selectedItem.setName(result.getKey());
            selectedItem.setPrice(result.getValue());
            menuListView.refresh();
        });
    }

    @FXML
    private void handleDeleteMenuItem() {
        MenuItem selectedItem = menuListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to delete.");
            return;
        }
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Menu Item");
        confirmDialog.setContentText("Are you sure you want to delete " + selectedItem.getName() + "?");
        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            menuItems.remove(selectedItem);
        }
    }

    @FXML
    private void handleInventory() {
        try {
            Stage stage = (Stage) inventoryButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/InventoryView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Inventory Management");
        } catch (Exception e) {
            showAlert("Error", "Failed to open inventory management.");
        }
    }

    @FXML
    private void handleAnalytics() {
        try {
            Stage stage = (Stage) analyticsButton.getScene().getWindow();
            
            // Explicitly check if the resource is found
            java.net.URL fxmlUrl = getClass().getResource("/AnalyticsView.fxml");
            if (fxmlUrl == null) {
                System.err.println("Error: AnalyticsView.fxml not found in classpath.");
                showAlert("Error", "Failed to open analytics view: FXML file not found.");
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Analytics & Reporting");
        } catch (Exception e) {
            System.err.println("Error loading AnalyticsView.fxml: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open analytics view.");
        }
    }

    @FXML
    private void handleTableManagement() {
        try {
            Stage stage = (Stage) tableButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/TableView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Table Management");
        } catch (Exception e) {
            showAlert("Error", "Failed to open table management view.");
        }
    }

    private void updateOrderSummary() {
        totalLabel.setText("Total: $" + String.format("%.2f", order.getTotal()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}