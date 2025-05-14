
package encryption.decryption;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
/**
 *
 * @author lamaalfreah
 */
public class EncryptionDecryption extends Application { 
    private TextArea outputArea;
    private TextField keyInput;

    public static void main(String[] args) {
        testEncryptText();
        testDecryptText();
        testFullEncryptionDecryption();
        testPerformance();
        launch(args);// Launches the application
        
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Encryption & Decryption");


        // Creating a label to prompt the user for the key
        Label keyLabel = new Label("Enter the number of rails (key):");
        keyLabel.setFont(new Font(14));
        keyLabel.setTextFill(Color.WHITE);
        keyLabel.setStyle("-fx-background-color: transparent;");
        
        // Input field for the user to enter the number of rails        
        keyInput = new TextField();
        keyInput.setPromptText("Enter number of rails");
        keyInput.setMaxWidth(200);

        // Button to attach a file 
        Button fileButton = new Button("Attach File");
        fileButton.setOnAction(e -> attachFile(primaryStage));
        fileButton.setMaxWidth(200);
        fileButton.setStyle("-fx-background-color: #9537FF");
        fileButton.setTextFill(Color.WHITE);

        // Radio button for encryption & Decryption
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton encryptionButton = new RadioButton("Encryption");
        encryptionButton.setToggleGroup(toggleGroup);
        encryptionButton.setTextFill(Color.WHITE);

        RadioButton decryptionButton = new RadioButton("Decryption");
        decryptionButton.setToggleGroup(toggleGroup);
        decryptionButton.setTextFill(Color.WHITE); 
        
        // TextArea to display the output (encrypted text)
        outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(true);
        outputArea.setMaxWidth(900);
        outputArea.setMinWidth(900); 
        outputArea.setPrefHeight(250);
        
        Button saveButton = new Button("Save Encrypted File");
        saveButton.setOnAction(e -> saveEncryptedFile(primaryStage));// Calls the saveEncryptedFile method when clicked
        saveButton.setMaxWidth(200);
        saveButton.setStyle("-fx-background-color: #9537FF");
        saveButton.setTextFill(Color.WHITE);
        


        // VBox layout to arrange components vertically
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(keyLabel, keyInput, fileButton, encryptionButton, decryptionButton, outputArea, saveButton);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    int key = Integer.parseInt(keyInput.getText());
                    if (key < 1) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter key greater than 1 :)");
                        alert.showAndWait();
                        return;
                    }
                    if (key == 1 ||key >= outputArea.getText().length()) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("The output text is identical to the input text\n "
                                + "please enter a small key to protect your data :)");
                        alert.showAndWait();
                        return;
                    }                   
                    if (encryptionButton.isSelected()) {
                        outputArea.setText(encryptText(outputArea.getText(), key));
                    } else if (decryptionButton.isSelected()) {
                        outputArea.setText(decryptText(outputArea.getText(), key));
                    }

                } catch (NumberFormatException e) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a valid key :)");
                        alert.showAndWait();
                }
            }
        });        
        
        // Adding a background image to the layout
        Image bgImage = new Image("background.png"); 
        BackgroundImage backgroundImage = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, 
                                       BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
                                       new BackgroundSize(100, 100, true, true, false, true));
        layout.setBackground(new Background(backgroundImage));
        
        // Set up the scene and window size
        Scene scene = new Scene(layout, 1500, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to attach a file
    private void attachFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                reader.close();
                outputArea.setText(content.toString());
            } catch (IOException | NumberFormatException e) {
                // Display an error message in case of any issues
                outputArea.setText("Error: " + e.getMessage());
            }
        }
        else {
        // Show alert if no file is selected
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please attach the file :)");
        alert.showAndWait();
    }
    }

// Method to encrypt text  
private String encryptText(String OriginalText, int key) {
    String textWithSpacesReplaced = OriginalText.replace(" ", "*");
    char[][] encryptionMatrix = new char[key][OriginalText.length()];
    boolean downwardTrend= false;
    int layer = 0;

    for (int matrixColumn = 0; matrixColumn < OriginalText.length(); matrixColumn++) {
        encryptionMatrix[layer][matrixColumn] = textWithSpacesReplaced.charAt(matrixColumn);
        
        if (layer ==0 ) {
            downwardTrend = true;
        }
        else if(layer== key - 1){
        downwardTrend = false;
        }
        if (downwardTrend) {
            layer++;
        } else {
            layer--;
        }
    }

    StringBuilder textAfterEncryption = new StringBuilder();
    for (int i = 0; i < key; i++) {
        for (int j = 0; j < OriginalText.length(); j++) {
            if (encryptionMatrix[i][j] != '\0') {
                textAfterEncryption.append(encryptionMatrix[i][j]);
            }
        }
    }

    return textAfterEncryption.toString().replace("*"," ");
}

// Method to decrypt text  
private String decryptText(String encryptedText, int key) {
    char[][] decryptionMatrix = new char[key][encryptedText.length()];
    boolean downwardTrend= false;
    int index = 0;
    int layer=0;
 
        for (int matrixColumn = 0; matrixColumn < encryptedText.length(); matrixColumn++) {
        decryptionMatrix[layer][matrixColumn] = '-';
        
        if (layer ==0 ) {
            downwardTrend = true;
        }
        else if(layer== key - 1){
        downwardTrend = false;
        }
        if (downwardTrend) {
            layer++;
        } else {
            layer--;
        }}
    for (int i = 0; i < key; i++) {
        for (int j = 0; j < encryptedText.length(); j++) {
            if (decryptionMatrix[i][j] == '-' && index < encryptedText.length()) {
                decryptionMatrix[i][j] = encryptedText.charAt(index++);
            }}}

    StringBuilder decryptedText = new StringBuilder();
    int row = 0;
    for (int matrixColumn = 0; matrixColumn < encryptedText.length(); matrixColumn++) {
        if (decryptionMatrix[row][matrixColumn] != '\0') {
            decryptedText.append(decryptionMatrix[row][matrixColumn]);
        }
        if (row ==0 ) {
            downwardTrend = true;
        }
        else if(row== key - 1){
        downwardTrend = false;
        }
        if (downwardTrend) {
            row++;
        } else {
            row--;
        } }
    return decryptedText.toString();
}

    // Method for saving file
    private void saveEncryptedFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(outputArea.getText());
                writer.close();
            } catch (IOException e) {
                // Display an error message in case of any issues
                outputArea.setText("Error saving file: " + e.getMessage());
            }
        }
    } 
    public static void testEncryptText() {
        EncryptionDecryption app = new EncryptionDecryption();
        String result = app.encryptText("Rail Fence Cipher", 3);
        String expected = "R ciralFneCpeie h";
        if (result.equals(expected)) {
            System.out.println("Encrypt Text Test Passed");
        } else {
            System.out.println("Encrypt Text Test Failed: Expected " + expected + ", but got " + result);
        }
    }

    public static void testDecryptText() {
        EncryptionDecryption app = new EncryptionDecryption();
        String result = app.decryptText("R ciralFneCpeie h", 3);
        String expected = "Rail Fence Cipher";
        if (result.equals(expected)) {
            System.out.println("Decrypt Text Test Passed");
        } else {
            System.out.println("Decrypt Text Test Failed: Expected " + expected + ", but got " + result);
        }
    }
    
    public static void testFullEncryptionDecryption() {
    EncryptionDecryption app = new EncryptionDecryption();
    String originalText = "Rail Fence Cipher";
    int key = 3;

    String encryptedText = app.encryptText(originalText, key);
    String decryptedText = app.decryptText(encryptedText, key);

    if (originalText.equals(decryptedText)) {
        System.out.println("Integration Test Passed");
    } else {
        System.out.println("Integration Test Failed: Expected " + originalText + ", but got " + decryptedText);
    }
}
    
    public static void testPerformance() {
    EncryptionDecryption app = new EncryptionDecryption();
    StringBuilder longText = new StringBuilder();

    // long text to test
    for (int i = 0; i < 100000; i++) {
        longText.append("Lama ");
    }
    int key = 5;

    long startTime = System.currentTimeMillis();
    String encryptedText = app.encryptText(longText.toString(), key);
    String decryptedText = app.decryptText(encryptedText, key);
    long endTime = System.currentTimeMillis();

    if (longText.toString().equals(decryptedText)) {
        System.out.println("Performance Test Passed in " + (endTime - startTime) + "ms");
    } else {
        System.out.println("Performance Test Failed");
    }
}
}


