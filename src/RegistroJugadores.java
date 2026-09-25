import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
                    imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bgMesa));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a1a1a;");
        }

        // 2. Panel central de pergamino con sombra
        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(600);
        panelPergamino.setMaxHeight(550);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 40;"
        );
        DropShadow sombraPergamino = new DropShadow(20, 10, 10, Color.color(0, 0, 0, 0.7));
        panelPergamino.setEffect(sombraPergamino);

        Text titulo = new Text("Registro de Jugadores");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        titulo.setFill(Color.web("#3b220b"));
        titulo.setEffect(new DropShadow(2, 1, 1, Color.web("#ffffff")));

        // Formulario interno
        VBox formulario = new VBox(20);
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

        HBox botones = new HBox(20, btnComenzar, btnVolver);
        botones.setAlignment(Pos.CENTER);

        panelPergamino.getChildren().addAll(titulo, formulario, botones);
        root.getChildren().add(panelPergamino);
    }

    private HBox crearFilaInput(String etiqueta, String placeholder) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER);

        Label label = new Label(etiqueta);
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        label.setTextFill(Color.web("#3b220b"));
        label.setPrefWidth(100);

        TextField input = new TextField();
        input.setPromptText(placeholder);
        input.setFont(Font.font("Georgia", 14));
        input.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.85);" +
                        "-fx-border-color: #5c3a18; -fx-border-width: 2;" +
                        "-fx-border-radius: 5; -fx-background-radius: 5;" +
                        "-fx-padding: 10;"
        );
        input.setPrefWidth(240);

        // Sombra suave en el input
        DropShadow sombraInput = new DropShadow(3, Color.color(0,0,0,0.2));
        input.setEffect(sombraInput);

        hbox.getChildren().addAll(label, input);
        return hbox;
    }

    private Button crearBotonEstilizado(String texto) {
        Button boton = new Button(texto);
        boton.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2c1808;" +
                        "-fx-padding: 12 25;" +
                        "-fx-cursor: hand;"
        );
        boton.setPrefWidth(200);
        boton.setPrefHeight(50);
        boton.setEffect(new DropShadow(2, Color.web("#ffffff")));

        ScaleTransition st = new ScaleTransition(Duration.millis(150), boton);
        boton.setOnMouseEntered(e -> {
            st.setToX(1.05); st.setToY(1.05); st.play();
            boton.setStyle(boton.getStyle().replace("transparent", "rgba(255, 255, 255, 0.2)"));
        });
        boton.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.play();
            boton.setStyle(boton.getStyle().replace("rgba(255, 255, 255, 0.2)", "transparent"));
        });

        return boton;
    }

    private void iniciarPartida() {
        String nombre1 = inputJugador1.getText().trim();
        String nombre2 = inputJugador2.getText().trim();

        if (nombre1.isEmpty() || nombre2.isEmpty()) {
            inputJugador1.setStyle(inputJugador1.getStyle() + "-fx-border-color: red;");
            inputJugador2.setStyle(inputJugador2.getStyle() + "-fx-border-color: red;");
            return;
        }

        if (nombre1.equals(nombre2)) {
            inputJugador2.setStyle(inputJugador2.getStyle() + "-fx-border-color: red;");
            return;
        }

        gestor.mostrarPartida(nombre1, nombre2);
    }

    public StackPane getRoot() {
        return root;
    }
}