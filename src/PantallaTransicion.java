import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Nodo genérico "Se tapan las cartas y se pide ceder la computadora a...".
 * `destino` es solo el texto que se muestra ("Jugador 2", "el centro...", etc.),
 * y `continuar` es lo que sigue en el diagrama después de que confirman.
 */
public class PantallaTransicion {

    public static void mostrar(GestorEscenas gestor, String destino, Runnable continuar) {
        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Cedan la computadora a: " + destino));

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setOnAction(e -> continuar.run());
        raiz.getChildren().add(btnContinuar);

        gestor.mostrar(new Scene(raiz, 700, 400));
    }
}
