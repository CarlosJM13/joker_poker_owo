import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.util.Duration;

public class PantallaTransicion {

    public static void mostrar(GestorEscenas gestor, String jugadorActual, Runnable onTerminado) {
        // 1. Fondo de Mesa de Madera cubriendo toda la pantalla
        StackPane root = new StackPane();
        try {
            Image imagenMesa = new Image(PantallaTransicion.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
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

        // 2. Contenedor central con la plantilla de pergamino
        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(650);
        panelPergamino.setMaxHeight(450);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 40;"
        );

        Label lblTurno = new Label("TURNO DE");
        lblTurno.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        lblTurno.setTextFill(Color.web("#5c3a18"));

        double tamanoFuente = jugadorActual.length() > 15 ? 24 : 42;

        Label lblJugador = new Label(jugadorActual);
        lblJugador.setFont(Font.font("Georgia", FontWeight.BOLD, tamanoFuente));
        lblJugador.setTextFill(Color.web("#3b220b"));
        lblJugador.setWrapText(true);
        lblJugador.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label lblMensaje = new Label("Cambien la laptop");
        lblMensaje.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
        lblMensaje.setTextFill(Color.web("#7a5230"));

        panelPergamino.getChildren().addAll(lblTurno, lblJugador, lblMensaje);
        root.getChildren().add(panelPergamino);

        gestor.mostrar(new Scene(root, 1024, 768));

        // Pausa de 2 segundos para dar tiempo de leer y avanza automáticamente en la secuencia
        PauseTransition pausa = new PauseTransition(Duration.seconds(2.0));
        pausa.setOnFinished(e -> {
            if (onTerminado != null) {
                onTerminado.run();
            }
        });
        pausa.play();
    }
}