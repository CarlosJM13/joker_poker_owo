import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;

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
            root.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(880);
        panelPergamino.setMaxHeight(620);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 40;");

        Label titulo = new Label("TIENDA DE MEJORAS — " + jugador.getNombre().toUpperCase());
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#3b220b"));

        textSaldo = new Label("Saldo disponible: $" + jugador.getDolares() + " Dólares");
        textSaldo.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        textSaldo.setTextFill(Color.web("#7a5230"));

        HBox contenedorOpciones = new HBox(25);
        contenedorOpciones.setAlignment(Pos.CENTER);

        // Opción 1: Duplicar mano (Usamos JokerManox2.png)
        VBox op1 = crearOpcionTienda("3 DÓLARES", "Duplicar tu mano\nrecién jugada", "#8b4513", "/assets/jokers/JokerManox2.png");
        op1.setOnMouseClicked(e -> {
            boolean exito = tiendaLogica.comprarDuplicarMano(jugador);
            actualizarSaldo(exito, "¡Mano duplicada!");
        });

        // Opción 2: Comodín Rojo (Usamos JokerCartasR.png)
        VBox op2 = crearOpcionTienda("3 DÓLARES", "Comprar comodín para\ncartas rojas", "#a42a2a", "/assets/jokers/JokerCartasR.png");
        op2.setOnMouseClicked(e -> {
            boolean exito = tiendaLogica.comprarComodin(gestor, numJugador, "rojo");
            actualizarSaldo(exito, "¡Comodín rojo adquirido!");
        });

        // Opción 3: Comodín Negro (Usamos JokerCartasN.png)
        VBox op3 = crearOpcionTienda("3 DÓLARES", "Comprar comodín para\ncartas negras", "#1e1e1e", "/assets/jokers/JokerCartasN.png");
        op3.setOnMouseClicked(e -> {
            boolean exito = tiendaLogica.comprarComodin(gestor, numJugador, "negro");
            actualizarSaldo(exito, "¡Comodín negro adquirido!");
        });

        // Agregamos SOLO las 3 opciones a la pantalla
        contenedorOpciones.getChildren().addAll(op1, op2, op3);


        Button btnContinuar = new Button("Terminar Compras");
        btnContinuar.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnContinuar.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 10 30; -fx-cursor: hand;");
        btnContinuar.setOnAction(e -> {
            if (onTiendaCompleta != null) onTiendaCompleta.run();
        });

        panelPergamino.getChildren().addAll(titulo, textSaldo, contenedorOpciones, btnContinuar);
        root.getChildren().add(panelPergamino);
    }

    private VBox crearOpcionTienda(String precio, String descripcion, String colorHex, String rutaImagen) {
        VBox opcion = new VBox(10);
        opcion.setAlignment(Pos.CENTER);
        opcion.setStyle("-fx-cursor: hand; -fx-padding: 10; -fx-background-color: rgba(255, 250, 240, 0.85); -fx-border-color: " + colorHex + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-border-width: 1.5;");
        opcion.setPrefWidth(220);
        opcion.setPrefHeight(250);

        // Renderizado de la imagen
        javafx.scene.image.ImageView vistaImagen = new javafx.scene.image.ImageView();
        try {
            Image img = new Image(EscenaTienda.class.getResourceAsStream(rutaImagen));
            vistaImagen.setImage(img);
            vistaImagen.setFitHeight(110);
            vistaImagen.setPreserveRatio(true);
        } catch (Exception ex) {
            System.out.println("Imagen no encontrada: " + rutaImagen);
        }

        Label precioText = new Label(precio);
        precioText.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        precioText.setTextFill(Color.web(colorHex));

        Label descText = new Label(descripcion);
        descText.setFont(Font.font("Georgia", javafx.scene.text.FontPosture.ITALIC, 12));
        descText.setTextFill(Color.web("#3b220b"));
        descText.setWrapText(true);
        descText.setAlignment(Pos.CENTER);

        opcion.getChildren().addAll(vistaImagen, precioText, descText);

        // Efecto hover
        opcion.setOnMouseEntered(e -> opcion.setStyle("-fx-cursor: hand; -fx-padding: 10; -fx-background-color: rgba(255, 255, 255, 1); -fx-border-color: " + colorHex + "; -fx-border-width: 2.5; -fx-border-radius: 6; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 6, 0, 0, 3);"));
        opcion.setOnMouseExited(e -> opcion.setStyle("-fx-cursor: hand; -fx-padding: 10; -fx-background-color: rgba(255, 250, 240, 0.85); -fx-border-color: " + colorHex + "; -fx-border-radius: 6; -fx-border-width: 1.5;"));

        return opcion;
    }

    private void actualizarSaldo(boolean exito, String mensajeExito) {
        if (exito) {
            textSaldo.setText("Saldo disponible: $" + jugador.getDolares() + " (" + mensajeExito + ")");
            textSaldo.setTextFill(javafx.scene.paint.Color.web("#2d6a4f"));
        } else {
            textSaldo.setText("Saldo disponible: $" + jugador.getDolares() + " (Fondos insuficientes)");
            textSaldo.setTextFill(javafx.scene.paint.Color.web("#a42a2a"));
        }
    }

    public StackPane getRoot() {
        return root;
    }
}