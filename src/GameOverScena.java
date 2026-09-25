import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameOverScena {
    private StackPane root; // StackPane para poner fondo
    private Partida partida;
    private GestorEscenas gestor;

    public GameOverScena(Partida partida, GestorEscenas gestor) {
        this.partida = partida;
        this.gestor = gestor;
        crearUI();
    }

    private void crearUI() {
        root = new StackPane();

        // Fondo más oscuro/tenebroso
        try {
            Image imagenMesa = new Image(getClass().getResourceAsStream("/assets/fondo/mesa.jpg"));
            BackgroundImage bgMesa = new BackgroundImage(
                    imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bgMesa));
            // Oscurecemos un poco la mesa para el game over
            Region veloOscuro = new Region();
            veloOscuro.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
            root.getChildren().add(veloOscuro);
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #0f0f1a;");
        }

        VBox panelPergamino = new VBox(30);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(600);
        panelPergamino.setMaxHeight(450);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 40;"
        );
        panelPergamino.setEffect(new DropShadow(30, 0, 0, Color.web("#a42a2a"))); // Sombra rojiza

        Text titulo = new Text("FIN DE LA PARTIDA");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 46));
        titulo.setFill(Color.web("#a42a2a")); // Rojo oscuro
        titulo.setEffect(new DropShadow(2, Color.BLACK));

        VBox resumen = crearResumen();

        HBox botones = new HBox(20);
        botones.setAlignment(Pos.CENTER);

        Button btnMenuPrincipal = crearBotonEstilizado("Menú Principal", false);
        btnMenuPrincipal.setOnAction(e -> gestor.mostrarMenuPrincipal());

        Button btnSalir = crearBotonEstilizado("Salir del Juego", true);
        btnSalir.setOnAction(e -> gestor.salir());

        botones.getChildren().addAll(btnMenuPrincipal, btnSalir);

        panelPergamino.getChildren().addAll(titulo, resumen, botones);
        root.getChildren().add(panelPergamino);
    }

    private VBox crearResumen() {
        VBox resumen = new VBox(15);
        resumen.setAlignment(Pos.CENTER);
        resumen.setStyle(
                "-fx-padding: 25; -fx-background-color: rgba(255, 250, 240, 0.7);" +
                        "-fx-border-radius: 8; -fx-border-color: #5c3a18; -fx-border-width: 2; -fx-background-radius: 8;"
        );
        resumen.setMaxWidth(400);
        resumen.setEffect(new DropShadow(5, Color.color(0,0,0,0.3)));

        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();

        Text j1Info = new Text(j1.getNombre() + ": " + j1.getDolares() + " Dólares Finales");
        j1Info.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        j1Info.setFill(Color.web("#3b220b"));

        Text j2Info = new Text(j2.getNombre() + ": " + j2.getDolares() + " Dólares Finales");
        j2Info.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        j2Info.setFill(Color.web("#3b220b"));

        Text ganador = new Text();
        ganador.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        ganador.setFill(Color.web("#2d6a4f")); // Verde victoria

        boolean perdioJ1 = partida.isUltimaRondaPerdioJ1();
        boolean perdioJ2 = partida.isUltimaRondaPerdioJ2();

        if (perdioJ1 && !perdioJ2) {
            ganador.setText("¡" + j2.getNombre() + " HA GANADO! (llegó más lejos)");
        } else if (perdioJ2 && !perdioJ1) {
            ganador.setText("¡" + j1.getNombre() + " HA GANADO! (llegó más lejos)");
        } else {
            // Ambos se ponchan en la misma ronda -> desempate por fichas de esa ronda
            long fJ1 = partida.getUltimaRondaPuntajeJ1();
            long fJ2 = partida.getUltimaRondaPuntajeJ2();
            if (fJ1 > fJ2) {
                ganador.setText("¡" + j1.getNombre() + " HA GANADO! (más fichas)");
            } else if (fJ2 > fJ1) {
                ganador.setText("¡" + j2.getNombre() + " HA GANADO! (más fichas)");
            } else {
                ganador.setText("¡ES UN EMPATE!");
                ganador.setFill(Color.web("#5c3a18"));
            }
        }

        resumen.getChildren().addAll(j1Info, j2Info, ganador);
        return resumen;
    }

    private Button crearBotonEstilizado(String texto, boolean esRojo) {
        Button boton = new Button(texto);
        boton.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        String colorTexto = esRojo ? "#a42a2a" : "#2c1808";

        boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + colorTexto + ";" +
                        "-fx-padding: 12 30;" +
                        "-fx-cursor: hand;"
        );
        boton.setEffect(new DropShadow(2, Color.web("#ffffff")));

        ScaleTransition st = new ScaleTransition(Duration.millis(150), boton);
        boton.setOnMouseEntered(e -> {
            st.setToX(1.05); st.setToY(1.05); st.play();
            boton.setStyle(boton.getStyle().replace("transparent", "rgba(255, 255, 255, 0.3)"));
        });
        boton.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.play();
            boton.setStyle(boton.getStyle().replace("rgba(255, 255, 255, 0.3)", "transparent"));
        });
        return boton;
    }

    public StackPane getRoot() {
        return root;
    }
}