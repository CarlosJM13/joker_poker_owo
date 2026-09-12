import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverScena {

    public static Scene crearEscena(GestorEscenas gestor) {
        VBox raiz = new VBox(15);
        raiz.setAlignment(Pos.CENTER);

        // TODO: reemplazar por el nombre real del ganador/perdedor, calculado
        // en Ronda.pasoEvaluacion (guárdalo en Partida antes de ir a GAME_OVER
        // si lo necesitas aquí).
        raiz.getChildren().add(new Label("¡Partida terminada!"));

        Button btnOtraPartida = new Button("Jugar otra partida");
        btnOtraPartida.setOnAction(e -> gestor.irA(EstadoJuego.MENU_PRINCIPAL));

        Button btnSalir = new Button("Salir del programa");
        btnSalir.setOnAction(e -> gestor.getStage().close());

        raiz.getChildren().addAll(btnOtraPartida, btnSalir);
        return new Scene(raiz, 700, 400);
    }
}
