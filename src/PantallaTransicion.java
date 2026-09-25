import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class PantallaTransicion {

    public static void mostrar(GestorEscenas gestor, String jugadorActual, Runnable onTerminado) {
        StackPane root = new StackPane();
        try {
            Image imagenMesa = new Image(PantallaTransicion.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            BackgroundImage bgMesa = new BackgroundImage(
                    imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bgMesa));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a1a1a;");
        }

        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(650);
        panelPergamino.setMaxHeight(450);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 40;"
        );
        panelPergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0, 0, 0, 0.7)));

        Label lblTurno = new Label("TURNO DE");
        lblTurno.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        lblTurno.setTextFill(Color.web("#5c3a18"));

        double tamanoFuente = jugadorActual.length() > 15 ? 26 : 46;

        Label lblJugador = new Label(jugadorActual);
        lblJugador.setFont(Font.font("Georgia", FontWeight.BOLD, tamanoFuente));
        lblJugador.setTextFill(Color.web("#3b220b"));
        lblJugador.setWrapText(true);
        lblJugador.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lblJugador.setEffect(new DropShadow(3, 2, 2, Color.web("#ffffff"))); // Brillo

        // Animación suave de "latido" en el nombre del jugador
        ScaleTransition st = new ScaleTransition(Duration.millis(800), lblJugador);
        st.setToX(1.05); st.setToY(1.05);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();

        Label lblMensaje = new Label("Cambien la laptop / cede el turno");
        lblMensaje.setFont(Font.font("Georgia", FontPosture.ITALIC, 16));
        lblMensaje.setTextFill(Color.web("#7a5230"));

        panelPergamino.getChildren().addAll(lblTurno, lblJugador, lblMensaje);
        root.getChildren().add(panelPergamino);

        gestor.mostrar(new Scene(root, 1024, 768));

        // Pausa de 2.5 segundos (un poquito más de tiempo para apreciar el efecto)
        PauseTransition pausa = new PauseTransition(Duration.seconds(2.5));
        pausa.setOnFinished(e -> {
            if (onTerminado != null) {
                onTerminado.run();
            }
        });
        pausa.play();
    }
}