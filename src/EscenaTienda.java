import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class EscenaTienda {
    private StackPane root;
    private Partida partida;
    private GestorEscenas gestor;
    private Jugador jugador;
    private int numJugador;
    private Runnable onTiendaCompleta;
    private Tienda tiendaLogica;
    private Label textSaldo;

    public EscenaTienda(Partida partida, GestorEscenas gestor, int numJugador, Runnable onTiendaCompleta) {
        this.partida = partida;
        this.gestor = gestor;
        this.numJugador = numJugador;
        this.jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        this.onTiendaCompleta = onTiendaCompleta;
        this.tiendaLogica = new Tienda(partida.getBarajaExtra());
        crearUI();
    }

    public static void mostrar(GestorEscenas gestor, int numJugador, Runnable onTiendaCompleta) {
        EscenaTienda pantalla = new EscenaTienda(gestor.getPartida(), gestor, numJugador, onTiendaCompleta);
        gestor.mostrar(new Scene(pantalla.getRoot(), 1024, 768));
    }

    private void crearUI() {
        root = new StackPane();
        try {
            Image imagenMesa = new Image(EscenaTienda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            root.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a1a1a;");
        }

        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(950);
        panelPergamino.setMaxHeight(650);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 40;");

        // Sombra del pergamino
        DropShadow sombraPergamino = new DropShadow(20, 10, 10, Color.color(0, 0, 0, 0.7));
        panelPergamino.setEffect(sombraPergamino);

        Label titulo = new Label("TIENDA DE MEJORAS — " + jugador.getNombre().toUpperCase());
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        titulo.setTextFill(Color.web("#3b220b"));
        DropShadow sombraTexto = new DropShadow(2, 1, 1, Color.web("#ffffff"));
        titulo.setEffect(sombraTexto);

        textSaldo = new Label("Saldo disponible: $" + jugador.getDolares() + " Dólares");
        textSaldo.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        textSaldo.setTextFill(Color.web("#7a5230"));

        HBox contenedorOpciones = new HBox(20);
        contenedorOpciones.setAlignment(Pos.CENTER);

        VBox op1 = crearOpcionTienda("3 DÓLARES", "Duplicar tu mano\nrecién jugada", "#8b4513", "/assets/jokers/JokerManox2.png");
        op1.setOnMouseClicked(e -> {
            boolean exito = tiendaLogica.comprarDuplicarMano(jugador);
            actualizarSaldo(exito, "¡Mano duplicada!");
        });

        VBox op2 = crearOpcionTienda("3 DÓLARES", "Comprar comodín para\ncartas rojas", "#a42a2a", "/assets/jokers/JokerCartasR.png");
        op2.setOnMouseClicked(e -> {
            boolean exito = tiendaLogica.comprarComodin(gestor, numJugador, "rojo");
            actualizarSaldo(exito, "¡Comodín rojo adquirido!");
        });

        VBox op3 = crearOpcionTienda("3 DÓLARES", "Comprar comodín para\ncartas negras", "#1e1e1e", "/assets/jokers/JokerCartasN.png");
        op3.setOnMouseClicked(e -> {
            boolean exito = tiendaLogica.comprarComodin(gestor, numJugador, "negro");
            actualizarSaldo(exito, "¡Comodín negro adquirido!");
        });

        VBox op4 = crearOpcionTienda("3 DÓLARES", "Evolucionar cartas\nen el Árbol", "#2e8b57", "/assets/jokers/JokerCartasN.png");
        op4.setOnMouseClicked(e -> {
            Runnable volverATienda = () -> EscenaTienda.mostrar(gestor, numJugador, onTiendaCompleta);
            boolean exito = tiendaLogica.comprarMejoraArbol(gestor, numJugador, volverATienda);
            if (!exito) {
                actualizarSaldo(false, "Fondos insuficientes");
            }
        });

        contenedorOpciones.getChildren().addAll(op1, op2, op3, op4);

        Button btnContinuar = new Button("Terminar Compras");
        btnContinuar.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        btnContinuar.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 12 40; -fx-cursor: hand;");
        btnContinuar.setEffect(sombraTexto);

        ScaleTransition stBtn = new ScaleTransition(Duration.millis(150), btnContinuar);
        btnContinuar.setOnMouseEntered(e -> { stBtn.setToX(1.05); stBtn.setToY(1.05); stBtn.play(); });
        btnContinuar.setOnMouseExited(e -> { stBtn.setToX(1.0); stBtn.setToY(1.0); stBtn.play(); });

        btnContinuar.setOnAction(e -> {
            if (onTiendaCompleta != null) onTiendaCompleta.run();
        });

        panelPergamino.getChildren().addAll(titulo, textSaldo, contenedorOpciones, btnContinuar);
        root.getChildren().add(panelPergamino);
    }

    private VBox crearOpcionTienda(String precio, String descripcion, String colorHex, String rutaImagen) {
        VBox opcion = new VBox(10);
        opcion.setAlignment(Pos.CENTER);
        opcion.setStyle("-fx-cursor: hand; -fx-padding: 15; -fx-background-color: rgba(255, 250, 240, 0.9); -fx-border-color: " + colorHex + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 2;");
        opcion.setPrefWidth(200);
        opcion.setPrefHeight(260);

        // Sombra suave para la tarjeta
        DropShadow sombraTarjeta = new DropShadow(5, 2, 2, Color.color(0,0,0,0.3));
        opcion.setEffect(sombraTarjeta);

        ImageView vistaImagen = new ImageView();
        try {
            Image img = new Image(EscenaTienda.class.getResourceAsStream(rutaImagen));
            vistaImagen.setImage(img);
            vistaImagen.setFitHeight(110);
            vistaImagen.setPreserveRatio(true);
            DropShadow sombraImg = new DropShadow(5, Color.color(0,0,0,0.4));
            vistaImagen.setEffect(sombraImg);
        } catch (Exception ex) {}

        Label precioText = new Label(precio);
        precioText.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        precioText.setTextFill(Color.web(colorHex));

        Label descText = new Label(descripcion);
        descText.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
        descText.setTextFill(Color.web("#3b220b"));
        descText.setWrapText(true);
        descText.setAlignment(Pos.CENTER);
        descText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        opcion.getChildren().addAll(vistaImagen, precioText, descText);

        // Efecto hover y escala
        ScaleTransition st = new ScaleTransition(Duration.millis(150), opcion);

        opcion.setOnMouseEntered(e -> {
            st.setToX(1.05); st.setToY(1.05); st.play();
            opcion.setStyle("-fx-cursor: hand; -fx-padding: 15; -fx-background-color: rgba(255, 255, 255, 1); -fx-border-color: " + colorHex + "; -fx-border-width: 3; -fx-border-radius: 8; -fx-background-radius: 8;");
            opcion.setEffect(new DropShadow(15, Color.web(colorHex))); // Brillo del color de la carta
        });

        opcion.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.play();
            opcion.setStyle("-fx-cursor: hand; -fx-padding: 15; -fx-background-color: rgba(255, 250, 240, 0.9); -fx-border-color: " + colorHex + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 2;");
            opcion.setEffect(sombraTarjeta);
        });

        return opcion;
    }

    private void actualizarSaldo(boolean exito, String mensajeExito) {
        if (exito) {
            textSaldo.setText("Saldo disponible: $" + jugador.getDolares() + " (" + mensajeExito + ")");
            textSaldo.setTextFill(Color.web("#2d6a4f"));
        } else {
            textSaldo.setText("Saldo disponible: $" + jugador.getDolares() + " (Fondos insuficientes)");
            textSaldo.setTextFill(Color.web("#a42a2a"));
        }
    }

    public StackPane getRoot() {
        return root;
    }
}