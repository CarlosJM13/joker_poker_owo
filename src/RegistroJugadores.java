import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RegistroJugadores {
    private StackPane root;
    private GestorEscenas gestor;
    private TextField inputJugador1;
    private TextField inputJugador2;

    public RegistroJugadores(GestorEscenas gestor) {
        this.gestor = gestor;
        crearUI();
    }

    private void crearUI() {
        root = new StackPane();

        // 1. Fondo de Mesa de Madera
        try {
            Image imagenMesa = new Image(getClass().getResourceAsStream("/assets/fondo/mesa.jpg"));
            BackgroundImage bgMesa = new BackgroundImage(
                    imagenMesa,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bgMesa));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #2b1d0c;");
        }

        // 2. Panel central de pergamino estilo Isaac
        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(600);
        panelPergamino.setMaxHeight(500);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 40;"
        );

        Text titulo = new Text("Registro de Jugadores");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        titulo.setFill(Color.web("#3b220b"));

        // Formulario interno
        VBox formulario = new VBox(15);
        formulario.setAlignment(Pos.CENTER);
        formulario.setMaxWidth(400);

        HBox hbox1 = crearFilaInput("Jugador 1:", "Nombre J1");
        inputJugador1 = (TextField) hbox1.getChildren().get(1);

        HBox hbox2 = crearFilaInput("Jugador 2:", "Nombre J2");
        inputJugador2 = (TextField) hbox2.getChildren().get(1);

        formulario.getChildren().addAll(hbox1, hbox2);

        // Botones
        Button btnComenzar = crearBotonEstilizado("Comenzar Partida");
        btnComenzar.setOnAction(e -> iniciarPartida());

        Button btnVolver = crearBotonEstilizado("Volver");
        btnVolver.setOnAction(e -> gestor.mostrarMenuPrincipal());

        HBox botones = new HBox(15, btnComenzar, btnVolver);
        botones.setAlignment(Pos.CENTER);

        panelPergamino.getChildren().addAll(titulo, formulario, botones);
        root.getChildren().add(panelPergamino);
    }

    private HBox crearFilaInput(String etiqueta, String placeholder) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER);

        Label label = new Label(etiqueta);
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        label.setTextFill(Color.web("#3b220b"));
        label.setPrefWidth(100);

        TextField input = new TextField();
        input.setPromptText(placeholder);
        input.setFont(Font.font("Georgia", 14));
        input.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7);" +
                        "-fx-border-color: #5c3a18;" +
                        "-fx-border-radius: 4;" +
                        "-fx-padding: 8;"
        );
        input.setPrefWidth(220);

        hbox.getChildren().addAll(label, input);
        return hbox;
    }

    private Button crearBotonEstilizado(String texto) {
        Button boton = new Button(texto);
        boton.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2c1808;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        );
        boton.setPrefWidth(280);
        boton.setPrefHeight(45);

        boton.setOnMouseEntered(e -> boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: #000000;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        ));
        boton.setOnMouseExited(e -> boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2c1808;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        ));

        return boton;
    }

    private void iniciarPartida() {
        String nombre1 = inputJugador1.getText().trim();
        String nombre2 = inputJugador2.getText().trim();

        if (nombre1.isEmpty() || nombre2.isEmpty()) {
            System.out.println("Por favor, ingresa ambos nombres.");
            return;
        }

        if (nombre1.equals(nombre2)) {
            System.out.println("Los nombres deben ser diferentes.");
            return;
        }

        gestor.mostrarPartida(nombre1, nombre2);
    }

    public StackPane getRoot() {
        return root;
    }
}