import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture; // <-- Importación correcta
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuPrincipal {
    private StackPane root;
    private GestorEscenas gestor;

    public MenuPrincipal(GestorEscenas gestor) {
        this.gestor = gestor;
        crearUI();
    }

    private void crearUI() {
        root = new StackPane();

        // 1. Fondo de Mesa de Madera (`mesa.jpg`) cubriendo toda la pantalla
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

        // 2. Contenedor central simulando el pergamino/menú estilo Isaac
        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(600);
        panelPergamino.setMaxHeight(500);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 50;"
        );

        // Título del juego
        Text titulo = new Text("JOKER POKER");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 54));
        titulo.setFill(Color.web("#3b220b"));

        Text subtitulo = new Text("¡BIENVENIDO AL JUEGO!");
        subtitulo.setFont(Font.font("Georgia", FontPosture.ITALIC, 16)); // <-- Uso correcto de FontPosture
        subtitulo.setFill(Color.web("#5c3a18"));

        // Botones del menú
        Button btnJugar = crearBotonEstilizado("Jugar");
        btnJugar.setOnAction(e -> gestor.mostrarRegistroJugadores());

        Button btnHistorial = crearBotonEstilizado("Historial de Partidas");
        btnHistorial.setOnAction(e -> gestor.mostrarHistorial());

        Button btnSalir = crearBotonEstilizado("Salir");
        btnSalir.setOnAction(e -> gestor.salir());

        VBox contenedorBotones = new VBox(15, btnJugar, btnHistorial, btnSalir);
        contenedorBotones.setAlignment(Pos.CENTER);

        panelPergamino.getChildren().addAll(titulo, subtitulo, contenedorBotones);

        root.getChildren().add(panelPergamino);
    }

    private Button crearBotonEstilizado(String texto) {
        Button boton = new Button(texto);
        boton.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" + // <-- Nombre actualizado sin espacios
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2c1808;" +
                        "-fx-padding: 12 30;" +
                        "-fx-cursor: hand;"
        );
        boton.setPrefWidth(280);
        boton.setPrefHeight(50);

        boton.setOnMouseEntered(e -> boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: #000000;" +
                        "-fx-padding: 12 30;" +
                        "-fx-cursor: hand;"
        ));
        boton.setOnMouseExited(e -> boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2c1808;" +
                        "-fx-padding: 12 30;" +
                        "-fx-cursor: hand;"
        ));

        return boton;
    }

    public StackPane getRoot() {
        return root;
    }
}