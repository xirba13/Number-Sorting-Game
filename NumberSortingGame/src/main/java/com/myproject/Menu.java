package com.myproject;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Menu extends Application {
    Stage primaryStage;
    private Scene menuScene;
    private Tutorial tutorial;
    private Statistics statistics;
    private Modes modes; // Agrega una instancia de la clase Modes
    private double anchoVentana = 1200;
    private double altoVentana = 800;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        createSaveFileIfNotExists();
        this.primaryStage = primaryStage;
        tutorial = new Tutorial(this);
        statistics = new Statistics(this, anchoVentana, altoVentana);
        modes = new Modes(this, anchoVentana, altoVentana);

        primaryStage.setWidth(anchoVentana); // Establece el ancho de la ventana
        primaryStage.setHeight(altoVentana); // Establece la altura de la ventana

        Label titleLabel = new Label("Number Sorting Game");
        titleLabel.getStyleClass().add("title-label"); // Aplica una clase de estilo al título

        Button play = createMenuButton("Play");
        Button statisticsButton = createMenuButton("Statistics");
        Button tutorialButton = createMenuButton("Tutorial");
        Button exitButton = createMenuButton("Exit");

        VBox buttonsLayout = new VBox(20);
        buttonsLayout.getChildren().addAll(titleLabel, play, statisticsButton, tutorialButton, exitButton);
        buttonsLayout.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(buttonsLayout);

        // Mantén las dimensiones actuales de la ventana
        anchoVentana = primaryStage.getWidth();
        altoVentana = primaryStage.getHeight();

        menuScene = new Scene(borderPane, anchoVentana, altoVentana);
        menuScene.getStylesheets().add("/styles.css");
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Number Sorting Game");

        Image appIcon = new Image(getClass().getResourceAsStream("/icon.png"));
        primaryStage.getIcons().add(appIcon);

        Label versionLabel = new Label("Made By: Xabier Cendón");
        versionLabel.getStyleClass().add("version-label");

        VBox versionLayout = new VBox(versionLabel);
        VBox.setMargin(versionLayout, new Insets(0, 10, 10, 0));
        versionLayout.setAlignment(Pos.BOTTOM_RIGHT);

        versionLabel.setPadding(new Insets(0, 10, 10, 0));
        titleLabel.setPadding(new Insets(0, 0, 100, 0));

        borderPane.setBottom(versionLayout);

        primaryStage.show();
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(100); // Establece el ancho fijo deseado para los botones
        button.setOnAction(event -> {
            if (text.equals("Play")) {
                selectMode();
            } else if (text.equals("Statistics")) {
                showStatistics();
            } else if (text.equals("Tutorial")) {
                startTutorial();
            } else if (text.equals("Exit")) {
                primaryStage.close();
            }
        });
        return button;
    }

    public void setAnchoVentana(double anchoVentana) {
        this.anchoVentana = anchoVentana;
    }

    public void setAltoVentana(double altoVentana) {
        this.altoVentana = altoVentana;
    }

    private void selectMode() {
        modes.setAnchoVentana(primaryStage.getWidth()); // Guarda las dimensiones actuales
        modes.setAltoVentana(primaryStage.getHeight());
        primaryStage.setScene(modes.getModesScene());
    }

    private void showStatistics() {
        statistics.setAnchoVentana(primaryStage.getWidth()); // Guarda las dimensiones actuales
        statistics.setAltoVentana(primaryStage.getHeight());
        primaryStage.setScene(statistics.getStatisticsScene());
    }

    private void startTutorial() {
        primaryStage.setScene(tutorial.getTutorialScene());
    }

    public Scene getMenuScene() {
        return menuScene;
    }

    private static final String SAVE_FILE_NAME = "save.txt";

    private void createSaveFileIfNotExists() {
        File saveFile = new File(SAVE_FILE_NAME);
        if (!saveFile.exists()) {
            try {
                if (saveFile.createNewFile()) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void returnToMenu(Stage primaryStage, Scene menuScene) {
        primaryStage.setScene(menuScene);
    }
}
