import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.FontPosture;
import javafx.scene.image.Image;

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

        // 2. Contenedor central simulando el pergamino/menú
        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(800);
        panelPergamino.setMaxHeight(600);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 50;"
        );

        // Título Estético
        Text titulo = new Text("HISTORIAL DE PARTIDAS");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        titulo.setFill(Color.web("#3b220b"));

        javafx.scene.Node contenidoLista = crearListadoRondas();

        // Botón para volver al menú
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
        btnVolver.setOnAction(e -> gestor.mostrarMenuPrincipal());

        // Juntar todo en el pergamino
        panelPergamino.getChildren().addAll(titulo, contenidoLista, btnVolver);

        // Agregar el pergamino al StackPane principal
        root.getChildren().add(panelPergamino);
    }

    private javafx.scene.Node crearListadoRondas() {
        VBox contenido = new VBox(10);
        contenido.setAlignment(Pos.CENTER);
        contenido.setStyle("-fx-padding: 10;");

        Text infoText = new Text("Total de rondas jugadas: " + (historial != null ? historial.getTamaño() : 0));
        infoText.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        infoText.setFill(Color.web("#5c3a18"));
        contenido.getChildren().add(infoText);

        if (historial != null && historial.cabeza != null) {
            // Contenedor para las rondas con barra de desplazamiento por si son muchas
            VBox contenedorRondas = new VBox(12);
            contenedorRondas.setAlignment(Pos.CENTER);

            NodoDoble actual = historial.cabeza;
            int numRonda = 1;

            while (actual != null) {
                // Tarjeta visual para cada ronda
                VBox cardRonda = new VBox(4);
                cardRonda.setAlignment(Pos.CENTER);
                cardRonda.setStyle("-fx-background-color: rgba(92, 58, 24, 0.08); -fx-padding: 10; -fx-background-radius: 5;");

                Text lblTituloRonda = new Text("--- RONDA " + numRonda + " (Meta: " + actual.valorCiega + ") ---");
                lblTituloRonda.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
                lblTituloRonda.setFill(Color.web("#3b220b"));

                // Estado Jugador 1
                String estadoJ1 = actual.superoCiegaJ1 ? "✅ Superó la meta" : "❌ Perdió (No llegó a la meta)";
                Text lblJ1 = new Text(actual.nombreJ1 + " (" + actual.manoJ1 + ") ➔ " + actual.cartasJ1 + " fichas | " + estadoJ1);
                lblJ1.setFont(Font.font("Georgia", 12));
                lblJ1.setFill(Color.web("#5c3a18"));

                // Estado Jugador 2
                String estadoJ2 = actual.superoCiegaJ2 ? "✅ Superó la meta" : "❌ Perdió (No llegó a la meta)";
                Text lblJ2 = new Text(actual.nombreJ2 + " (" + actual.manoJ2 + ") ➔ " + actual.cartasJ2 + " fichas | " + estadoJ2);
                lblJ2.setFont(Font.font("Georgia", 12));
                lblJ2.setFill(Color.web("#5c3a18"));

                cardRonda.getChildren().addAll(lblTituloRonda, lblJ1, lblJ2);
                contenedorRondas.getChildren().add(cardRonda);

                actual = actual.siguiente;
                numRonda++;
            }

            javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(contenedorRondas);
            scroll.setPrefHeight(300);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            return scroll;

        } else {
            Text vacio = new Text("No hay partidas registradas aún.");
            vacio.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
            vacio.setFill(Color.web("#7a5230"));
            return vacio;
        }
    }

    public StackPane getRoot() {
        return root;
    }
}