import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HistorialPartidas {
    private StackPane root;
    private ListaDoble historial;
    private GestorEscenas gestor;

    public HistorialPartidas(ListaDoble historial, GestorEscenas gestor) {
        this.historial = historial;
        this.gestor = gestor;
        crearUI();
    }

    private void crearUI() {
        root = new StackPane();

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

        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(850);
        panelPergamino.setMaxHeight(650);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 40;"
        );
        panelPergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0, 0, 0, 0.7)));

        Text titulo = new Text("HISTORIAL DE PARTIDAS");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 38));
        titulo.setFill(Color.web("#3b220b"));
        titulo.setEffect(new DropShadow(2, 1, 1, Color.web("#ffffff")));

        javafx.scene.Node contenidoLista = crearListadoRondas();

        Button btnVolver = new Button("Volver al Menú");
        btnVolver.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        btnVolver.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2c1808;" +
                        "-fx-padding: 12 30;" +
                        "-fx-cursor: hand;"
        );
        btnVolver.setEffect(new DropShadow(2, Color.web("#ffffff")));

        ScaleTransition stBtn = new ScaleTransition(Duration.millis(150), btnVolver);
        btnVolver.setOnMouseEntered(e -> { stBtn.setToX(1.05); stBtn.setToY(1.05); stBtn.play(); });
        btnVolver.setOnMouseExited(e -> { stBtn.setToX(1.0); stBtn.setToY(1.0); stBtn.play(); });
        btnVolver.setOnAction(e -> gestor.mostrarMenuPrincipal());

        panelPergamino.getChildren().addAll(titulo, contenidoLista, btnVolver);
        root.getChildren().add(panelPergamino);
    }

    private javafx.scene.Node crearListadoRondas() {
        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);
        contenido.setStyle("-fx-padding: 10;");

        Text infoText = new Text("Total de rondas jugadas: " + (historial != null ? historial.getTamaño() : 0));
        infoText.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        infoText.setFill(Color.web("#5c3a18"));
        contenido.getChildren().add(infoText);

        if (historial != null && historial.cabeza != null) {
            VBox contenedorRondas = new VBox(15);
            contenedorRondas.setAlignment(Pos.CENTER);
            contenedorRondas.setStyle("-fx-padding: 5 15;");

            NodoDoble actual = historial.cabeza;
            int numRonda = 1;

            while (actual != null) {
                VBox cardRonda = new VBox(8);
                cardRonda.setAlignment(Pos.CENTER);
                cardRonda.setStyle(
                        "-fx-background-color: rgba(255, 250, 240, 0.6);" +
                                "-fx-border-color: #5c3a18; -fx-border-width: 1.5;" +
                                "-fx-padding: 15; -fx-background-radius: 8; -fx-border-radius: 8;"
                );
                DropShadow dsCard = new DropShadow(5, Color.color(0,0,0,0.2));
                cardRonda.setEffect(dsCard);

                Text lblTituloRonda = new Text("--- RONDA " + numRonda + " (Meta: " + actual.valorCiega + ") ---");
                lblTituloRonda.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
                lblTituloRonda.setFill(Color.web("#3b220b"));

                String estadoJ1 = actual.superoCiegaJ1 ? "✅ Superó" : "❌ Perdió";
                Text lblJ1 = new Text(actual.nombreJ1 + " (" + actual.manoJ1 + ") ➔ " + actual.cartasJ1 + " pts | " + estadoJ1);
                lblJ1.setFont(Font.font("Georgia", 14));
                lblJ1.setFill(Color.web("#5c3a18"));

                String estadoJ2 = actual.superoCiegaJ2 ? "✅ Superó" : "❌ Perdió";
                Text lblJ2 = new Text(actual.nombreJ2 + " (" + actual.manoJ2 + ") ➔ " + actual.cartasJ2 + " pts | " + estadoJ2);
                lblJ2.setFont(Font.font("Georgia", 14));
                lblJ2.setFill(Color.web("#5c3a18"));

                cardRonda.getChildren().addAll(lblTituloRonda, lblJ1, lblJ2);

                // Efecto hover sutil en las tarjetas del historial
                cardRonda.setOnMouseEntered(e -> cardRonda.setStyle(cardRonda.getStyle().replace("0.6", "0.9")));
                cardRonda.setOnMouseExited(e -> cardRonda.setStyle(cardRonda.getStyle().replace("0.9", "0.6")));

                contenedorRondas.getChildren().add(cardRonda);

                actual = actual.siguiente;
                numRonda++;
            }

            ScrollPane scroll = new ScrollPane(contenedorRondas);
            scroll.setPrefHeight(350);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            return scroll;

        } else {
            Text vacio = new Text("No hay partidas registradas aún.");
            vacio.setFont(Font.font("Georgia", FontPosture.ITALIC, 16));
            vacio.setFill(Color.web("#7a5230"));
            return vacio;
        }
    }

    public StackPane getRoot() {
        return root;
    }
}