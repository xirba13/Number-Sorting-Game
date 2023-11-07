package com.myproject;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Tutorial {
    private Menu menu;

    public Tutorial(Menu menu) {
        this.menu = menu;
    }

    public Scene getTutorialScene() {
        Label titleLabel = new Label("Tutorial");
        titleLabel.getStyleClass().add("title-label");

        Label howToPlayLabel = new Label("How to Play:");
        Label step1 = new Label("Click 'Generate Number' to get a random number.");
        Label step2 = new Label("Click on an empty box to place the number.");
        Label step3 = new Label("You must fill all the boxes in ascending order.");

        Label rulesLabel = new Label("Rules:");
        Label rule1 = new Label("You cannot change the numbers once placed.");
        Label rule2 = new Label("You win if the numbers are in order, and you lose if not.");

        Label statsLabel = new Label("Statistics:");
        Label gamesPlayed = new Label("\"Games Played\" shows the total number of games played.");
        Label gamesWon = new Label("\"Games Won\" displays the games won.");
        Label winRate = new Label("\"Win Rate\" is the percentage of games won.");

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> returnToMenu());

        GridPane tutorialLayout = new GridPane();
        tutorialLayout.setAlignment(Pos.CENTER); // Centrar el contenido horizontalmente
        tutorialLayout.setHgap(10);
        tutorialLayout.setVgap(10);
        tutorialLayout.setPadding(new Insets(20, 20, 20, 20));

        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setValignment(titleLabel, VPos.CENTER);
        tutorialLayout.add(titleLabel, 0, 0, 3, 1);

        tutorialLayout.add(howToPlayLabel, 0, 1);
        tutorialLayout.add(step1, 0, 2);
        tutorialLayout.add(step2, 0, 3);
        tutorialLayout.add(step3, 0, 4);
        tutorialLayout.add(rulesLabel, 1, 1);
        tutorialLayout.add(rule1, 1, 2);
        tutorialLayout.add(rule2, 1, 3);
        tutorialLayout.add(statsLabel, 2, 1);
        tutorialLayout.add(gamesPlayed, 2, 2);
        tutorialLayout.add(gamesWon, 2, 3);
        tutorialLayout.add(winRate, 2, 4);
        tutorialLayout.add(backButton, 0, 5, 3, 1);

        GridPane.setHalignment(backButton, HPos.CENTER); // Centrar horizontalmente
        GridPane.setValignment(backButton, VPos.CENTER); // Centrar verticalmente

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tutorialLayout);

        GridPane.setMargin(backButton, new Insets(70, 0, 0, 0));

        Scene tutorialScene = new Scene(borderPane, 1000, 800);
        tutorialScene.getStylesheets().add("/styles.css");


        return tutorialScene;
    }

    private void returnToMenu() {
        Menu.returnToMenu(menu.primaryStage, menu.getMenuScene());
    }
}
