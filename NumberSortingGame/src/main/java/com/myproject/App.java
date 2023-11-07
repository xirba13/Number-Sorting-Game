package com.myproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class App extends Application {
    private Stage stage;
    private int numFields = 5;
    private int veryEasyWins = 0;
    private int easyWins = 0;
    private int normalWins = 0;
    private int hardWins = 0;
    private int veryEasyPartidasJugadas = 0;
    private int easyPartidasJugadas = 0;
    private int normalPartidasJugadas = 0;
    private int hardPartidasJugadas = 0;
    private static final String SAVE_FILE_NAME = "save.txt";
    private Label veryEasyWinsLabel = new Label("Very Easy Wins: 0");
    private Label veryEasyPartidasLabel = new Label("Very Easy Partidas: 0");
    private Label easyWinsLabel = new Label("Easy Wins: 0");
    private Label easyPartidasLabel = new Label("Easy Partidas: 0");
    private Label normalWinsLabel = new Label("Normal Wins: 0");
    private Label normalPartidasLabel = new Label("Normal Partidas: 0");
    private Label hardWinsLabel = new Label("Hard Wins: 0");
    private Label hardPartidasLabel = new Label("Hard Partidas: 0");
    private Modes modes;
    boolean todosCamposLlenos = false;
    private boolean enableCounters; // Nueva variable booleana para habilitar/deshabilitar los contadores
    private boolean[] numeroGuardado;
    private TextField[] textFields;
    private Label resultadoLabel = new Label();
    private Button generarButton;
    private Label etiquetaNumero;
    private Button reiniciarButton;
    private Button backToMenuButton;
    private Label contadorPartidasLabel;
    private Label contadorVictoriasLabel;
    private Label winRateLabel;
    private GridPane gridPane = new GridPane();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        numeroGuardado = new boolean[numFields]; // Inicializar el array con el número correcto de campos
        textFields = new TextField[numFields]; // Inicializar el array de textFields
        leerContadorPartidas(); // Lee el contador desde el archivo
        setupUI(primaryStage);
        setEventHandlers();
        primaryStage.show();
    }

    public App(int numFields, boolean enableCounters) {
        this.numFields = numFields;
        numeroGuardado = new boolean[numFields];
        textFields = new TextField[numFields];
        this.enableCounters = enableCounters; // Establece el estado de los contadores
    }

    // Getter for the App's main scene
    public Scene getScene() {
        return stage.getScene();
    }

    // Setter for the menu reference
    public void setModes(Modes modes) {
        this.modes = modes;
    }

    private void setupUI(Stage stage) {
        etiquetaNumero = new Label();
        etiquetaNumero.setStyle("-fx-font-size: 100px; -fx-font-weight: bold;");

        // Configura el GridPane
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        int columns = 5; // Cantidad de columnas por fila
        int rowIndex = 0;
        int colIndex = 0;

        for (int i = 0; i < numFields; i++) {
            if (colIndex == columns) {
                colIndex = 0;
                rowIndex++;
            }
            textFields[i] = createTextField(i);
            gridPane.add(textFields[i], colIndex, rowIndex);
            colIndex++;
        }

        generarButton = createButton("Generate Number");
        reiniciarButton = createButton("Restart Game");
        reiniciarButton.setDisable(true);

        HBox topRow = new HBox(10);
        topRow.getChildren().addAll(etiquetaNumero, resultadoLabel);
        topRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(50);
        root.getChildren().addAll(topRow, generarButton);

        // Coloca las etiquetas en un VBox
        VBox statsVBox = new VBox(
                veryEasyWinsLabel, veryEasyPartidasLabel,
                easyWinsLabel, easyPartidasLabel,
                normalWinsLabel, normalPartidasLabel,
                hardWinsLabel, hardPartidasLabel);

        // Configura el diseño y la posición del VBox en la interfaz
        statsVBox.setAlignment(Pos.CENTER);
        statsVBox.setSpacing(10);

        if (numFields > 5) {
            gridPane.setVgap(25); // Ajusta el espaciado vertical
            HBox centeredGridPane = new HBox(gridPane);
            centeredGridPane.setAlignment(Pos.CENTER);
            root.getChildren().add(centeredGridPane);
        } else {
            gridPane.setVgap(10); // Restaura el espaciado vertical predeterminado si hay 5 o menos campos
            HBox fieldSection = new HBox(gridPane);
            fieldSection.setAlignment(Pos.CENTER);
            root.getChildren().add(fieldSection);
        }

        if (numFields <= 20) {
            gridPane.getStyleClass().add("grid-pane-with-border");
        } else if (numFields > 20) {
            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            // Establece la altura del ScrollPane para mostrar un número específico de
            // campos en pantalla
            int visibleFields = 10; // Ajusta este valor según tus necesidades
            double rowHeight = 50; // Ajusta la altura de cada fila
            scrollPane.setPrefViewportHeight(visibleFields * rowHeight);

            HBox centeredScrollPane = new HBox(scrollPane); // Envuelve el ScrollPane en un HBox
            centeredScrollPane.setAlignment(Pos.CENTER); // Alinea el HBox al centro

            root.getChildren().add(centeredScrollPane); // Agrega el HBox centrado en lugar del ScrollPane
        }

        root.setAlignment(Pos.CENTER);

        backToMenuButton = createButton("Back to Menu");
        backToMenuButton.setDisable(true); // Estado inicial deshabilitado

        Region leftRegion = new Region();
        Region rightRegion = new Region();
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        HBox.setHgrow(rightRegion, Priority.ALWAYS);

        HBox buttonBox = new HBox(leftRegion, backToMenuButton, reiniciarButton, rightRegion);
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setPadding(new Insets(80, 10, 0, 10));

        contadorPartidasLabel = new Label();
        setModeDependentLabelsJugadas();
        contadorPartidasLabel.setStyle("-fx-font-size: 16px;");

        contadorVictoriasLabel = new Label();
        setModeDependentLabelsGanadas();
        contadorVictoriasLabel.setStyle("-fx-font-size: 16px;");

        winRateLabel = new Label("Win Rate: N/A");
        winRateLabel.setStyle("-fx-font-size: 16px;");

        HBox bottomLeftPartidas = new HBox(contadorPartidasLabel, contadorVictoriasLabel, winRateLabel);
        bottomLeftPartidas.setAlignment(Pos.BOTTOM_LEFT);

        HBox.setMargin(contadorPartidasLabel, new Insets(0, 10, 10, 10));
        HBox.setMargin(contadorVictoriasLabel, new Insets(0, 10, 10, 10));
        HBox.setMargin(winRateLabel, new Insets(0, 10, 10, 10));

        HBox bottomLeftLayout = new HBox(bottomLeftPartidas);
        bottomLeftLayout.setAlignment(Pos.BOTTOM_LEFT);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);
        borderPane.setTop(buttonBox);

        HBox bottomLayout = new HBox(10); // Añade un espacio entre los elementos
        bottomLayout.getChildren().addAll(bottomLeftLayout);

        // Configura alineación para cada elemento en la esquina correspondiente
        HBox.setHgrow(bottomLeftLayout, Priority.ALWAYS);

        if (enableCounters) {
            setModeDependentLabelsJugadas();
            setModeDependentLabelsGanadas();
            updateWinRateLabel();
        } else {
            // Desactiva los contadores
            contadorPartidasLabel.setText("");
            contadorVictoriasLabel.setText("");
            winRateLabel.setText("");
        }

        bottomLayout.setAlignment(Pos.BOTTOM_CENTER); // Alinea el HBox en la parte inferior

        borderPane.setBottom(bottomLayout);

        // Calcula el Win Rate y actualiza la etiqueta
        updateWinRateLabel();

        Scene scene = new Scene(borderPane, 1200, 800);
        scene.getStylesheets().add("/styles.css"); // Agrega el archivo CSS
        stage.setScene(scene);
    }

    private TextField createTextField(int index) {
        TextField textField = new TextField();
        textField.setStyle("-fx-alignment: center;");

        textField.setPromptText(String.valueOf(index + 1));
        textField.setEditable(false);
        textField.setMouseTransparent(true);
        textField.setFocusTraversable(false);
        return textField;
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setMinWidth(100);
        return button;
    }

    private void setBackToMenuButtonEnabled(boolean enabled) {
        backToMenuButton.setDisable(!enabled);
    }

    private void generateNumber() {
        int nuevoAleatorio = (int) (Math.random() * 1000 + 1);
        etiquetaNumero.setText(String.valueOf(nuevoAleatorio));

        for (int i = 0; i < numFields; i++) {
            if (!numeroGuardado[i]) {
                textFields[i].clear();
                textFields[i].setMouseTransparent(false);
            }
        }

        resultadoLabel.setText("");
        todosCamposLlenos = false;
        reiniciarButton.setDisable(true);

        // Desactivar el botón "Back to Menu" al generar un nuevo número
        setBackToMenuButtonEnabled(false);

        generarButton.setDisable(true); // Desactiva el botón después de generar el número

        // Verifica si todos los campos están llenos después de desactivar el botón
        todosCamposLlenos = true;
        for (int i = 0; i < numFields; i++) {
            if (!numeroGuardado[i]) {
                todosCamposLlenos = false;
                break;
            }
        }
    }

    private boolean checkIfSorted() {
        int lastNumber = -1;

        for (int i = 0; i < numFields; i++) {
            if (numeroGuardado[i]) {
                int currentNumber = Integer.parseInt(textFields[i].getText());
                if (currentNumber < lastNumber) {
                    return false; // Si los números no están en orden ascendente, es una derrota
                }
                lastNumber = currentNumber;
            }
        }

        return true; // Si los números están en orden ascendente, es una victoria.
    }

    private void saveNumber(int index) {
        if (!numeroGuardado[index]) {
            String numeroGenerado = etiquetaNumero.getText();
            if (!numeroGenerado.isEmpty() && numeroGenerado.matches("\\d+")) {
                textFields[index].setText(numeroGenerado);
                numeroGuardado[index] = true;
                etiquetaNumero.setText("");

                boolean allFieldsFilled = true;
                boolean perder = false;

                for (int i = 0; i < numFields; i++) {
                    if (!numeroGuardado[i]) {
                        allFieldsFilled = false;
                        break;
                    }
                }
                if (!checkIfSorted()) {
                    resultadoLabel.setStyle("-fx-font-size: 100px; -fx-font-weight: bold;");
                    resultadoLabel.setText("YOU LOSE");
                    perder = true;
                    switch (numFields) {
                        case 2:
                            veryEasyPartidasJugadas++;
                            veryEasyWinsLabel.setText("Very Easy Wins: " + veryEasyWins);
                            veryEasyPartidasLabel.setText("Very Easy Partidas: " + veryEasyPartidasJugadas);
                            break;
                        case 5:
                            easyPartidasJugadas++;
                            easyWinsLabel.setText("Easy Wins: " + easyWins);
                            easyPartidasLabel.setText("Easy Partidas: " + easyPartidasJugadas);
                            break;
                        case 10:
                            normalPartidasJugadas++;
                            normalWinsLabel.setText("Normal Wins: " + normalWins);
                            normalPartidasLabel.setText("Normal Partidas: " + normalPartidasJugadas);
                            break;
                        case 20:
                            hardPartidasJugadas++;
                            hardWinsLabel.setText("Hard Wins: " + hardWins);
                            hardPartidasLabel.setText("Hard Partidas: " + hardPartidasJugadas);
                            break;
                    }
                } else if (allFieldsFilled && checkIfSorted()) {
                    resultadoLabel.setStyle("-fx-font-size: 100px; -fx-font-weight: bold;");
                    resultadoLabel.setText("YOU WIN");
                    switch (numFields) {
                        case 2:
                            veryEasyWins++;
                            veryEasyPartidasJugadas++;
                            veryEasyWinsLabel.setText("Very Easy Wins: " + veryEasyWins);
                            veryEasyPartidasLabel.setText("Very Easy Partidas: " + veryEasyPartidasJugadas);
                            break;
                        case 5:
                            easyWins++;
                            easyPartidasJugadas++;
                            easyWinsLabel.setText("Easy Wins: " + easyWins);
                            easyPartidasLabel.setText("Easy Partidas: " + easyPartidasJugadas);
                            break;
                        case 10:
                            normalWins++;
                            normalPartidasJugadas++;
                            normalWinsLabel.setText("Normal Wins: " + normalWins);
                            normalPartidasLabel.setText("Normal Partidas: " + normalPartidasJugadas);
                            break;
                        case 20:
                            hardWins++;
                            hardPartidasJugadas++;
                            hardWinsLabel.setText("Hard Wins: " + hardWins);
                            hardPartidasLabel.setText("Hard Partidas: " + hardPartidasJugadas);
                            break;
                    }
                }
                if (enableCounters) {
                    guardarContadorPartidas();
                    setModeDependentLabelsJugadas();
                    setModeDependentLabelsGanadas();
                    updateWinRateLabel();
                }

                if (allFieldsFilled || perder) {
                    reiniciarButton.setDisable(false);
                    generarButton.setDisable(true);
                    setBackToMenuButtonEnabled(true);
                } else {
                    reiniciarButton.setDisable(true);
                    generarButton.setDisable(false);
                    setBackToMenuButtonEnabled(false);
                }
            } else {
                generarButton.setDisable(false);
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < numFields; i++) {
            textFields[i].clear();
            textFields[i].setMouseTransparent(false);
            numeroGuardado[i] = false;
        }
        resultadoLabel.setText("");
        etiquetaNumero.setText("");
        generarButton.setDisable(false);
        reiniciarButton.setDisable(true);
        setBackToMenuButtonEnabled(false);
    }

    private void guardarContadorPartidas() {
        try (FileWriter writer = new FileWriter(SAVE_FILE_NAME)) {
            writer.write(
                    veryEasyWins + "," + easyWins + "," + normalWins + "," + hardWins + "," +
                            veryEasyPartidasJugadas + "," + easyPartidasJugadas + "," + normalPartidasJugadas + ","
                            + hardPartidasJugadas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void leerContadorPartidas() {
        File saveFile = new File(SAVE_FILE_NAME);

        try {
            if (saveFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
                    String line = reader.readLine();

                    if (line != null) {
                        String[] partes = line.split(",");
                        if (partes.length == 8) {
                            veryEasyWins = Integer.parseInt(partes[0]);
                            easyWins = Integer.parseInt(partes[1]);
                            normalWins = Integer.parseInt(partes[2]);
                            hardWins = Integer.parseInt(partes[3]);
                            veryEasyPartidasJugadas = Integer.parseInt(partes[4]);
                            easyPartidasJugadas = Integer.parseInt(partes[5]);
                            normalPartidasJugadas = Integer.parseInt(partes[6]);
                            hardPartidasJugadas = Integer.parseInt(partes[7]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setEventHandlers() {
        generarButton.setOnAction(event -> generateNumber());
        backToMenuButton.setOnAction(event -> returnToMenu(stage));

        for (int i = 0; i < numFields; i++) {
            final int finalI = i;
            textFields[i].setOnMouseClicked(event -> saveNumber(finalI));
        }

        reiniciarButton.setOnAction(event -> resetGame()); // Manejador para el botón "Restart"
    }

    private void returnToMenu(Stage stage) {
        stage.setScene(modes.getModesScene());
    }

    private void setModeDependentLabelsJugadas() {
        if (enableCounters) {
            switch (numFields) {
                case 2:
                    contadorPartidasLabel.setText("Games Played (Very Easy): " + veryEasyPartidasJugadas);
                    break;
                case 5:
                    contadorPartidasLabel.setText("Games Played (Easy): " + easyPartidasJugadas);
                    break;
                case 10:
                    contadorPartidasLabel.setText("Games Played (Normal): " + normalPartidasJugadas);
                    break;
                case 20:
                    contadorPartidasLabel.setText("Games Played (Hard): " + hardPartidasJugadas);
                    break;
                // Agrega más casos para otros modos de juego si es necesario
                default:
                    contadorPartidasLabel.setText("Games Played: 0");
            }
        } else {
            // Desactiva la etiqueta de Partidas Jugadas
            contadorPartidasLabel.setText("");
        }
    }

    private void setModeDependentLabelsGanadas() {
        if (enableCounters) {
            switch (numFields) {
                case 2:
                    contadorVictoriasLabel.setText("Games Won (Very Easy): " + veryEasyWins);
                    break;
                case 5:
                    contadorVictoriasLabel.setText("Games Won (Easy): " + easyWins);
                    break;
                case 10:
                    contadorVictoriasLabel.setText("Games Won (Normal): " + normalWins);
                    break;
                case 20:
                    contadorVictoriasLabel.setText("Games Won (Hard): " + hardWins);
                    break;
                // Agrega más casos para otros modos de juego si es necesario
                default:
                    contadorVictoriasLabel.setText("Games Won: 0");
            }
        } else {
            // Desactiva la etiqueta de Partidas Ganadas
            contadorVictoriasLabel.setText("");
        }
    }

    private void updateWinRateLabel() {
        if (enableCounters) {
            double winRate = 0.0;

            switch (numFields) {
                case 2:
                    if (veryEasyPartidasJugadas > 0) {
                        winRate = ((double) veryEasyWins / veryEasyPartidasJugadas) * 100.0;
                    }
                    break;
                case 5:
                    if (easyPartidasJugadas > 0) {
                        winRate = ((double) easyWins / easyPartidasJugadas) * 100.0;
                    }
                    break;
                case 10:
                    if (normalPartidasJugadas > 0) {
                        winRate = ((double) normalWins / normalPartidasJugadas) * 100.0;
                    }
                    break;
                case 20:
                    if (hardPartidasJugadas > 0) {
                        winRate = ((double) hardWins / hardPartidasJugadas) * 100.0;
                    }
                    break;
            }

            if (winRate > 0) {
                winRateLabel.setText("Win Rate: " + String.format("%.2f%%", winRate));
            } else {
                winRateLabel.setText("Win Rate: N/A");
            }
        } else {
            // Desactiva la etiqueta del Win Rate
            winRateLabel.setText("");
        }

    }
}