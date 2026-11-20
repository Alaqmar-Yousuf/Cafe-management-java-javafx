package com.cafebliss;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if ("admin".equals(username) && "admin".equals(password)) {
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/CafeView.fxml"));
                if (stage.getScene() != null) {
                    stage.getScene().setRoot(root);
                } else {
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                    stage.setScene(scene);
                }
                stage.setTitle("Cafe Bliss - Management System");
                stage.setMaximized(true);
            } catch (Exception e) {
                errorLabel.setText("Failed to load main app.");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Invalid username or password.");
            errorLabel.setVisible(true);
        }
    }
}
