import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Ojo: esto es la ESCENA de JavaFX. La lógica de negocio real de compra
 * vive en tu clase Tienda.java (constructor Tienda(TablaHash), método
 * comprarDuplicarMano(Jugador)). Por eso el nombre distinto: si esta clase
 * también se llamara "Tienda" chocaría con la tuya.
 *
 * Cubre: "¿El jugador quiere comprar algo?" -> copia de mano (Hash),
 * comodín rojo, comodín negro -> "¿quiere comprar algo más?" en bucle.
 */
public class EscenaTienda {

    private static final int COSTO_COMODIN = 3; // igual que el costo de duplicar mano, según tu mockup

    public static void mostrar(GestorEscenas gestor, int numJugador, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        Tienda tiendaLogica = new Tienda(partida.getBarajaExtra());

        VBox raiz = new VBox(15);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Tienda — Jugador " + numJugador + " (" + jugador.getDolares() + " dólares)"));

        HBox opciones = new HBox(15);
        opciones.setAlignment(Pos.CENTER);

        Button btnCopiaMano = new Button("3 dólares\nCopia de tu mano recién jugada");
        btnCopiaMano.setOnAction(e -> {
            boolean exito = tiendaLogica.comprarDuplicarMano(jugador); // ya valida y descuenta el dinero
            if (exito) {
                mostrar(gestor, numJugador, continuar); // refresca la pantalla con el dinero actualizado
            }
        });

        Button btnComodinRojo = new Button("3 dólares\nComodín cartas de palo rojo");
        btnComodinRojo.setOnAction(e -> {
            if (jugador.gastarDolares(COSTO_COMODIN)) {
                Joker joker = new Joker("Rojo", 10); // TODO: ajustar bonificación real
                (numJugador == 1 ? partida.getJokersJ1() : partida.getJokersJ2()).add(joker);
                mostrar(gestor, numJugador, continuar);
            }
        });

        Button btnComodinNegro = new Button("3 dólares\nComodín cartas de palo negro");
        btnComodinNegro.setOnAction(e -> {
            if (jugador.gastarDolares(COSTO_COMODIN)) {
                Joker joker = new Joker("Negro", 10); // TODO: ajustar bonificación real
                (numJugador == 1 ? partida.getJokersJ1() : partida.getJokersJ2()).add(joker);
                mostrar(gestor, numJugador, continuar);
            }
        });

        opciones.getChildren().addAll(btnCopiaMano, btnComodinRojo, btnComodinNegro);
        raiz.getChildren().add(opciones);

        Button btnSiguiente = new Button("Siguiente");
        btnSiguiente.setOnAction(e -> continuar.run());
        raiz.getChildren().add(btnSiguiente);

        gestor.mostrar(new Scene(raiz, 800, 450));
    }
}
