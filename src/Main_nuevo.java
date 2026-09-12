import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GestorEscenas.inicializar(primaryStage);
        primaryStage.setTitle("Joker Poker");
        GestorEscenas.getInstancia().irA(EstadoJuego.MENU_PRINCIPAL);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
