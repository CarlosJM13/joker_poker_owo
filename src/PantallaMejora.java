import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * "¿El jugador quiere mejorar sus cartas?" / "¿Tiene dinero suficiente?".
 * Deja elegir cuál de sus 2 cartas mejorar, y entre fichas azules o rojas
 * (las 2 ramas del ArbolMejoras). Si no hay dinero o la carta ya llegó a
 * un nodo hoja (legendaria), los botones quedan deshabilitados.
 */
public class PantallaMejora {

    private static final int COSTO_MEJORA = 5; // TODO: ajustar el costo real de mejorar una carta

    public static void mostrar(GestorEscenas gestor, int numJugador, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        ArbolMejoras arbol = partida.getArbolMejoras();

        VBox raiz = new VBox(15);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Jugador " + numJugador + ": ¿mejorar tus cartas? (" + jugador.getDolares() + " dólares)"));

        for (Carta carta : jugador.getManoActual()) {
            if (carta == null) continue;

            HBox fila = new HBox(10);
            fila.setAlignment(Pos.CENTER);
            fila.getChildren().add(new Label(carta.toString()));

            NodoArbol actual = (carta.nivelActual != null) ? carta.nivelActual : arbol.raiz;
            boolean tieneDinero = jugador.getDolares() >= COSTO_MEJORA;

            Button btnAzul = new Button("+ fichas azules");
            btnAzul.setDisable(!tieneDinero || actual.izquierdo == null);
            btnAzul.setOnAction(e -> {
                if (jugador.gastarDolares(COSTO_MEJORA)) {
                    carta.setNivelEvolucion(actual.izquierdo);
                    mostrar(gestor, numJugador, continuar); // refresca con el nuevo estado
                }
            });

            Button btnRoja = new Button("+ multiplicador rojo");
            btnRoja.setDisable(!tieneDinero || actual.derecho == null);
            btnRoja.setOnAction(e -> {
                if (jugador.gastarDolares(COSTO_MEJORA)) {
                    carta.setNivelEvolucion(actual.derecho);
                    mostrar(gestor, numJugador, continuar);
                }
            });

            fila.getChildren().addAll(btnAzul, btnRoja);
            raiz.getChildren().add(fila);
        }

        Button btnContinuar = new Button("Continuar la ronda");
        btnContinuar.setOnAction(e -> continuar.run());
        raiz.getChildren().add(btnContinuar);

        gestor.mostrar(new Scene(raiz, 800, 450));
    }
}
