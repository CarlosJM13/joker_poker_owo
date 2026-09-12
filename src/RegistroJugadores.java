import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegistroJugadores {

    public static Scene crearEscena(GestorEscenas gestor, int numeroJugador) {
        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);

        Label etiqueta = new Label("Jugador no registrado\nEscriba nombre del Jugador " + numeroJugador + ":");
        TextField campoNombre = new TextField();

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.setOnAction(e -> {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) return; // TODO: mostrar validación de campo vacío

            if (gestor.getPartida() == null) {
                gestor.setPartida(new Partida());
            }

            Jugador jugador = new Jugador(nombre);
            if (numeroJugador == 1) {
                gestor.getPartida().setJugador1(jugador);
            } else {
                gestor.getPartida().setJugador2(jugador);
            }
            gestor.irA(EstadoJuego.MENU_PRINCIPAL);
        });

        raiz.getChildren().addAll(etiqueta, campoNombre, btnConfirmar);
        return new Scene(raiz, 800, 500);
    }
}
