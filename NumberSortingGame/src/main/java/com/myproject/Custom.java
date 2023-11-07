    package com.myproject;

    import javafx.application.Application;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.layout.*;
    import javafx.stage.Stage;

    public class Custom extends Application {
        private Stage stage;
        private Modes modes;
        private double anchoVentana;
        private double altoVentana;
        private TextField customTextField;

        public Custom() {
            // Constructor, you can add any initialization logic here if needed.
        }

        @Override
        public void start(Stage primaryStage) {
            stage = primaryStage;

            // Create UI elements
            Label titleLabel = new Label("Select Mode");
            titleLabel.getStyleClass().add("title-label");

            customTextField = new TextField();
            customTextField.setPromptText("Number of fields");
            customTextField.setMaxWidth(200);
            customTextField.setEditable(false);
            customTextField.setId("customTextField");

            customTextField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (!newValue.matches("\\d*") || newValue.equals("0")) {
                    customTextField.setText(oldValue);
                }
            });            

            customTextField.setOnMouseClicked(event -> {
                customTextField.setEditable(true);
                customTextField.setStyle("");
            });

            Button backButton = new Button("Back");
            backButton.setOnAction(event -> returnToModes());

            // Botón "Play" para iniciar App.java
            Button playButton = new Button("Play");
            playButton.setOnAction(event -> startGame());

            // Create layout for the Custom mode
            StackPane inputPane = new StackPane(customTextField);
            VBox customBox = new VBox(20, titleLabel, inputPane, playButton, backButton);
            customBox.setAlignment(Pos.CENTER);

            // Create the scene for the Custom mode
            Scene customScene = new Scene(customBox, anchoVentana, altoVentana);
            customScene.getStylesheets().add("/styles.css");

            stage.setScene(customScene);
            stage.show();
        }

        public void setAnchoVentana(double anchoVentana) {
            this.anchoVentana = anchoVentana;
        }

        public void setAltoVentana(double altoVentana) {
            this.altoVentana = altoVentana;
        }

        public void setModes(Modes modes) {
            this.modes = modes;
        }

        private void returnToModes() {
            stage.setScene(modes.getModesScene());
        }

        private void startGame() {
            String userInput = customTextField.getText();
            if (!userInput.isEmpty()) {
                int numFields = Integer.parseInt(userInput);
                App app = new App(numFields, false); // Desactivar los contadores
                app.setModes(modes); // Puedes utilizar el objeto "modes" de Custom
                app.start(stage); // Utiliza el objeto "stage" de Custom
            }
        }
    }
