import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MenuPrincipal {
    private StackPane root;
    private GestorEscenas gestor;

    public MenuPrincipal(GestorEscenas gestor) {
        this.gestor = gestor;
        crearUI();
    }

    private void crearUI() {
        root = new StackPane();

        // Fondo de Mesa
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

        // Pergamino con sombra 3D profunda
        VBox panelPergamino = new VBox(30);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(650);
        panelPergamino.setMaxHeight(550);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 50;"
        );
        panelPergamino.setEffect(new DropShadow(25, 10, 10, Color.color(0, 0, 0, 0.8)));

        // Título con brillo dorado
        Text titulo = new Text("JOKER POKER");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 58));
        titulo.setFill(Color.web("#2b1d0c"));
        DropShadow sombraTexto = new DropShadow(5, Color.web("#FFD700"));
        titulo.setEffect(sombraTexto);

        Text subtitulo = new Text("¡BIENVENIDO AL JUEGO!");
        subtitulo.setFont(Font.font("Georgia", FontPosture.ITALIC, 18));
        subtitulo.setFill(Color.web("#5c3a18"));

        // Botones construidos con CSS Premium
        Button btnJugar = crearBotonPremium("Jugar");
        btnJugar.setOnAction(e -> gestor.mostrarRegistroJugadores());

        Button btnHistorial = crearBotonPremium("Historial de Partidas");
        btnHistorial.setOnAction(e -> gestor.mostrarHistorial());

        Button btnSalir = crearBotonPremium("Salir");
        btnSalir.setOnAction(e -> gestor.salir());

        VBox contenedorBotones = new VBox(20, btnJugar, btnHistorial, btnSalir);
        contenedorBotones.setAlignment(Pos.CENTER);

        panelPergamino.getChildren().addAll(titulo, subtitulo, contenedorBotones);
        root.getChildren().add(panelPergamino);
    }

    // Nuevo método generador de botones con gradientes de madera y oro
    private Button crearBotonPremium(String texto) {
        Button boton = new Button(texto);
        boton.setFont(Font.font("Georgia", FontWeight.BOLD, 16));

        String estiloBase = "-fx-background-color: linear-gradient(#5c3a18, #2b1d0c); " +
                "-fx-text-fill: #FFD700; " +
                "-fx-padding: 12 30; " +
                "-fx-border-color: #FFD700; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;";

        String estiloHover = "-fx-background-color: linear-gradient(#8b4513, #5c3a18); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-padding: 12 30; " +
                "-fx-border-color: #FFD700; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;";

        boton.setStyle(estiloBase);
        boton.setEffect(new DropShadow(5, Color.BLACK));

        ScaleTransition st = new ScaleTransition(Duration.millis(150), boton);
        boton.setOnMouseEntered(e -> {
            st.setToX(1.05); st.setToY(1.05); st.play();
            boton.setStyle(estiloHover);
        });

        boton.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.play();
            boton.setStyle(estiloBase);
        });

        boton.setPrefWidth(280);
        return boton;
    }

    public StackPane getRoot() {
        return root;
    }
}