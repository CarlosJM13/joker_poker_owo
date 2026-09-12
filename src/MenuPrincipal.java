import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPrincipal {

    public static Scene crearEscena(GestorEscenas gestor) {
        VBox raiz = new VBox(15);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("JOKER POKER"));

        boolean jugador1Listo = gestor.getPartida() != null && gestor.getPartida().getJugador1() != null;
        boolean jugador2Listo = gestor.getPartida() != null && gestor.getPartida().getJugador2() != null;

        if (jugador1Listo && jugador2Listo) {
            Button btnNuevaPartida = new Button("Nueva partida");
            btnNuevaPartida.setOnAction(e -> {
                gestor.getPartida().reiniciarParaNuevaPartida();
                gestor.irA(EstadoJuego.PARTIDA_EN_CURSO);
            });

            Button btnHistorial = new Button("Historial jugadas");
            btnHistorial.setOnAction(e -> gestor.irA(EstadoJuego.HISTORIAL_PARTIDAS));

            raiz.getChildren().addAll(btnNuevaPartida, btnHistorial);
        } else {
            Button btnJugador1 = new Button(jugador1Listo ? "Jugador 1 ✓" : "Registrar Jugador 1");
            btnJugador1.setDisable(jugador1Listo);
            btnJugador1.setOnAction(e -> gestor.irA(EstadoJuego.REGISTRO_JUGADOR_1));

            Button btnJugador2 = new Button(jugador2Listo ? "Jugador 2 ✓" : "Registrar Jugador 2");
            btnJugador2.setDisable(jugador2Listo);
            btnJugador2.setOnAction(e -> gestor.irA(EstadoJuego.REGISTRO_JUGADOR_2));

            Button btnHistorial = new Button("Historial jugadas");
            btnHistorial.setOnAction(e -> gestor.irA(EstadoJuego.HISTORIAL_PARTIDAS));

            raiz.getChildren().addAll(btnJugador1, btnJugador2, btnHistorial);
        }

        return new Scene(raiz, 800, 500);
    }
}
