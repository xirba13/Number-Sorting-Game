package com.myproject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Statistics {
    private Menu menu;

    private int veryEasyPartidasJugadas;
    private int veryEasyPartidasGanadas;
    private int easyPartidasJugadas;
    private int easyPartidasGanadas;
    private int normalPartidasJugadas;
    private int normalPartidasGanadas;
    private int hardPartidasJugadas;
    private int hardPartidasGanadas;

    private double anchoVentana;
    private double altoVentana;

    public Statistics(Menu menu, double anchoVentana, double altoVentana) {
        this.menu = menu;
        this.anchoVentana = anchoVentana;
        this.altoVentana = altoVentana;
        loadStatistics();
    }

    public Scene getStatisticsScene() {
        loadStatistics();
        Label titleLabel = new Label("Statistics");
        titleLabel.getStyleClass().add("title-label");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        gridPane.setAlignment(Pos.CENTER); // Centrar el GridPane en la escena

        gridPane.add(new Label("Mode"), 0, 0);
        gridPane.add(new Label("Games Played"), 1, 0);
        gridPane.add(new Label("Games Won"), 2, 0);
        gridPane.add(new Label("Win Rate"), 3, 0);
        
        gridPane.add(new Label("Very Easy:"), 0, 1);
        gridPane.add(new Label("Easy:"), 0, 2);
        gridPane.add(new Label("Normal:"), 0, 3);
        gridPane.add(new Label("Hard:"), 0, 4);
        
        Label labelVeryEasyGamesPlayed = new Label(Integer.toString(veryEasyPartidasJugadas));
        Label labelEasyGamesPlayed = new Label(Integer.toString(easyPartidasJugadas));
        Label labelNormalGamesPlayed = new Label(Integer.toString(normalPartidasJugadas));
        Label labelHardGamesPlayed = new Label(Integer.toString(hardPartidasJugadas));
        
        Label labelVeryEasyGamesWon = new Label(Integer.toString(veryEasyPartidasGanadas));
        Label labelEasyGamesWon = new Label(Integer.toString(easyPartidasGanadas));
        Label labelNormalGamesWon = new Label(Integer.toString(normalPartidasGanadas));
        Label labelHardGamesWon = new Label(Integer.toString(hardPartidasGanadas));
        
        Label labelVeryEasyWinRate = new Label(calculateWinRate(veryEasyPartidasJugadas, veryEasyPartidasGanadas));
        Label labelEasyWinRate = new Label(calculateWinRate(easyPartidasJugadas, easyPartidasGanadas));
        Label labelNormalWinRate = new Label(calculateWinRate(normalPartidasJugadas, normalPartidasGanadas));
        Label labelHardWinRate = new Label(calculateWinRate(hardPartidasJugadas, hardPartidasGanadas));
        
        // Alinea las etiquetas en el centro de sus respectivas celdas
        GridPane.setHalignment(labelVeryEasyGamesPlayed, HPos.CENTER);
        GridPane.setHalignment(labelEasyGamesPlayed, HPos.CENTER);
        GridPane.setHalignment(labelNormalGamesPlayed, HPos.CENTER);
        GridPane.setHalignment(labelHardGamesPlayed, HPos.CENTER);
        GridPane.setHalignment(labelVeryEasyGamesWon, HPos.CENTER);
        GridPane.setHalignment(labelEasyGamesWon, HPos.CENTER);
        GridPane.setHalignment(labelNormalGamesWon, HPos.CENTER);
        GridPane.setHalignment(labelHardGamesWon, HPos.CENTER);
        GridPane.setHalignment(labelVeryEasyWinRate, HPos.CENTER);
        GridPane.setHalignment(labelEasyWinRate, HPos.CENTER);
        GridPane.setHalignment(labelNormalWinRate, HPos.CENTER);
        GridPane.setHalignment(labelHardWinRate, HPos.CENTER);
        
        gridPane.add(labelVeryEasyGamesPlayed, 1, 1);
        gridPane.add(labelEasyGamesPlayed, 1, 2);
        gridPane.add(labelNormalGamesPlayed, 1, 3);
        gridPane.add(labelHardGamesPlayed, 1, 4);
        
        gridPane.add(labelVeryEasyGamesWon, 2, 1);
        gridPane.add(labelEasyGamesWon, 2, 2);
        gridPane.add(labelNormalGamesWon, 2, 3);
        gridPane.add(labelHardGamesWon, 2, 4);
        
        gridPane.add(labelVeryEasyWinRate, 3, 1);
        gridPane.add(labelEasyWinRate, 3, 2);
        gridPane.add(labelNormalWinRate, 3, 3);
        gridPane.add(labelHardWinRate, 3, 4);
        

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> returnToMenu());

        VBox centerVBox = new VBox(20, titleLabel, gridPane, backButton);
        centerVBox.setAlignment(Pos.CENTER);

        VBox.setMargin(backButton, new Insets(20, 0, 0, 0));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerVBox);

        Scene statisticsScene = new Scene(borderPane, anchoVentana, altoVentana);
        statisticsScene.getStylesheets().add("/styles.css");

        return statisticsScene;
    }

    public void updateStatistics(int veryEasyPartidasJugadas, int veryEasyPartidasGanadas,
            int easyPartidasJugadas, int easyPartidasGanadas,
            int normalPartidasJugadas, int normalPartidasGanadas,
            int hardPartidasJugadas, int hardPartidasGanadas) {
        this.veryEasyPartidasJugadas = veryEasyPartidasJugadas;
        this.veryEasyPartidasGanadas = veryEasyPartidasGanadas;
        this.easyPartidasJugadas = easyPartidasJugadas;
        this.easyPartidasGanadas = easyPartidasGanadas;
        this.normalPartidasJugadas = normalPartidasJugadas;
        this.normalPartidasGanadas = normalPartidasGanadas;
        this.hardPartidasJugadas = hardPartidasJugadas;
        this.hardPartidasGanadas = hardPartidasGanadas;
    }

    private void returnToMenu() {
        Menu.returnToMenu(menu.primaryStage, menu.getMenuScene());
    }

    public void loadStatistics() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    veryEasyPartidasJugadas = Integer.parseInt(parts[4]);
                    veryEasyPartidasGanadas = Integer.parseInt(parts[0]);
                    easyPartidasJugadas = Integer.parseInt(parts[5]);
                    easyPartidasGanadas = Integer.parseInt(parts[1]);
                    normalPartidasJugadas = Integer.parseInt(parts[6]);
                    normalPartidasGanadas = Integer.parseInt(parts[2]);
                    hardPartidasJugadas = Integer.parseInt(parts[7]);
                    hardPartidasGanadas = Integer.parseInt(parts[3]);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAnchoVentana(double anchoVentana) {
        this.anchoVentana = anchoVentana;
    }

    public void setAltoVentana(double altoVentana) {
        this.altoVentana = altoVentana;
    }

    private String calculateWinRate(int gamesPlayed, int gamesWon) {
        if (gamesPlayed > 0) {
            double winRate = (double) gamesWon / gamesPlayed * 100;
            return String.format("%.2f%%", winRate);
        } else {
            return "0.00%";
        }
    }
}
