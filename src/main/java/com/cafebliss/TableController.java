package com.cafebliss;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TableController {
    @FXML private GridPane tableGrid;
    @FXML private Button backButton;
    @FXML private Button addTableButton;
    @FXML private Button editTableButton;
    @FXML private Button deleteTableButton;
    @FXML private Button makeReservationButton;
    @FXML private Button viewReservationsButton;

    private ObservableList<Table> tables;
    private static final int GRID_COLUMNS = 4;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        tables = FXCollections.observableArrayList();
        // Add some sample tables
        for (int i = 1; i <= 12; i++) {
            tables.add(new Table(i, i <= 4 ? 2 : (i <= 8 ? 4 : 6)));
        }
        updateTableGrid();
    }

    private void updateTableGrid() {
        tableGrid.getChildren().clear();
        int row = 0;
        int col = 0;

        for (Table table : tables) {
            VBox tableBox = createTableBox(table);
            tableGrid.add(tableBox, col, row);
            
            col++;
            if (col >= GRID_COLUMNS) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createTableBox(Table table) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: " + getStatusColor(table.getStatus()) + "; -fx-padding: 10; -fx-border-color: #666; -fx-border-width: 1;");
        box.setPrefSize(150, 100);

        Label numberLabel = new Label("Table " + table.getTableNumber());
        numberLabel.setStyle("-fx-font-weight: bold;");
        
        Label capacityLabel = new Label(table.getCapacity() + " seats");
        Label statusLabel = new Label(table.getStatus().toString());
        
        box.getChildren().addAll(numberLabel, capacityLabel, statusLabel);
        
        if (table.getStatus() == Table.TableStatus.RESERVED) {
            Label reservationLabel = new Label(table.getReservationName() + "\n" + 
                table.getReservationTime().format(TIME_FORMATTER));
            box.getChildren().add(reservationLabel);
        }

        box.setOnMouseClicked(e -> handleTableClick(table));
        return box;
    }

    private String getStatusColor(Table.TableStatus status) {
        switch (status) {
            case FREE: return "#90EE90"; // Light green
            case OCCUPIED: return "#FFB6C1"; // Light red
            case RESERVED: return "#87CEEB"; // Light blue
            case CLEANING: return "#D3D3D3"; // Light gray
            default: return "#FFFFFF";
        }
    }

    private void handleTableClick(Table table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Table " + table.getTableNumber());
        dialog.setHeaderText("Table Status: " + table.getStatus());

        ButtonType freeButton = new ButtonType("Mark as Free");
        ButtonType occupyButton = new ButtonType("Mark as Occupied");
        ButtonType reserveButton = new ButtonType("Make Reservation");
        ButtonType closeButton = ButtonType.CLOSE;

        dialog.getDialogPane().getButtonTypes().addAll(freeButton, occupyButton, reserveButton, closeButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get() == freeButton) {
                table.markAsFree();
            } else if (result.get() == occupyButton) {
                table.setStatus(Table.TableStatus.OCCUPIED);
            } else if (result.get() == reserveButton) {
                showReservationDialog(table);
            }
            updateTableGrid();
        }
    }

    private void showReservationDialog(Table table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Make Reservation");
        dialog.setHeaderText("Reserve Table " + table.getTableNumber());

        TextField nameField = new TextField();
        nameField.setPromptText("Customer Name");
        
        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 12);
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0);
        
        HBox timeBox = new HBox(5, new Label("Time:"), hourSpinner, new Label(":"), minuteSpinner);
        timeBox.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(new VBox(10, nameField, timeBox));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText();
            if (!name.isEmpty()) {
                LocalDateTime time = LocalDateTime.now()
                    .withHour(hourSpinner.getValue())
                    .withMinute(minuteSpinner.getValue());
                table.reserve(name, time);
                updateTableGrid();
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CafeView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Cafe Bliss - Order Management");
        } catch (Exception e) {
            showAlert("Error", "Failed to return to main view.");
        }
    }

    @FXML
    private void handleAddTable() {
        Dialog<Table> dialog = new Dialog<>();
        dialog.setTitle("Add Table");
        dialog.setHeaderText("Enter Table Details");

        TextField numberField = new TextField();
        numberField.setPromptText("Table Number");
        
        Spinner<Integer> capacitySpinner = new Spinner<>(2, 12, 4);
        capacitySpinner.setEditable(true);

        dialog.getDialogPane().setContent(new VBox(10, 
            new Label("Table Number:"), numberField,
            new Label("Capacity:"), capacitySpinner));
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<Table> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int number = Integer.parseInt(numberField.getText());
                int capacity = capacitySpinner.getValue();
                tables.add(new Table(number, capacity));
                updateTableGrid();
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter valid numbers.");
            }
        }
    }

    @FXML
    private void handleEditTable() {
        // Similar to handleAddTable but with pre-filled values
        // Implementation left as an exercise
    }

    @FXML
    private void handleDeleteTable() {
        // Implementation left as an exercise
    }

    @FXML
    private void handleMakeReservation() {
        // Implementation left as an exercise
    }

    @FXML
    private void handleViewReservations() {
        // Implementation left as an exercise
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 