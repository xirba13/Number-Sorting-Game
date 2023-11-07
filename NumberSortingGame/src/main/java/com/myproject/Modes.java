package com.myproject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Modes {
    private Menu menu;
    private double anchoVentana;
    private double altoVentana;

    public Modes(Menu menu, double anchoVentana, double altoVentana) {
        this.menu = menu;
        this.anchoVentana = anchoVentana;
        this.altoVentana = altoVentana;
    }

    public Scene getModesScene() {
        Label titleLabel = new Label("Select Mode");
        titleLabel.getStyleClass().add("title-label");

        // Create mode buttons
        Button playWithTwoFieldsButton = createModeButton("Very Easy (2 numbers)");
        Button playWithFiveFieldsButton = createModeButton("Easy (5 numbers)");
        Button playWithTenFieldsButton = createModeButton("Normal (10 numbers)");
        Button playWithTwentyFieldsButton = createModeButton("Hard (20 numbers)");
        Button playWithCustomFieldsButton = createModeButton("Custom ( No Statistics)");

        playWithTwoFieldsButton.setOnAction(event -> startGame(2));
        playWithFiveFieldsButton.setOnAction(event -> startGame(5));
        playWithTenFieldsButton.setOnAction(event -> startGame(10));
        playWithTwentyFieldsButton.setOnAction(event -> startGame(20));
        playWithCustomFieldsButton.setOnAction(event -> startCustomMode());

        VBox modeButtonsVBox = new VBox(20, playWithTwoFieldsButton, playWithFiveFieldsButton, playWithTenFieldsButton,
                playWithTwentyFieldsButton, playWithCustomFieldsButton);
        modeButtonsVBox.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> returnToMenu());

        VBox centerVBox = new VBox(20, titleLabel, modeButtonsVBox, backButton);
        centerVBox.setAlignment(Pos.CENTER);

        VBox.setMargin(backButton, new Insets(20, 0, 0, 0)); // Add top margin to the "Back to Menu" button

        VBox buttonsVBox = new VBox();
        buttonsVBox.setAlignment(Pos.TOP_LEFT);
        buttonsVBox.setPadding(new Insets(10, 10, 10, 10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerVBox);
        borderPane.setLeft(buttonsVBox);

        Scene modesScene = new Scene(borderPane, anchoVentana, altoVentana);
        modesScene.getStylesheets().add("/styles.css");

        return modesScene;
    }

    public void setAnchoVentana(double anchoVentana) {
        this.anchoVentana = anchoVentana;
    }

    public void setAltoVentana(double altoVentana) {
        this.altoVentana = altoVentana;
    }

    // Function to create mode buttons with a fixed width
    private Button createModeButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(200); // Set the desired fixed width
        return button;
    }

    private void startGame(int numFields) {
        App app = new App(numFields, true); // Habilitar los contadores
        app.setModes(this);
        app.start(menu.primaryStage);
    }

    private void startCustomMode() {
        Custom customMode = new Custom();
        customMode.setModes(this);
        customMode.start(menu.primaryStage);
    }

    private void returnToMenu() {
        Menu.returnToMenu(menu.primaryStage, menu.getMenuScene());
    }
}
