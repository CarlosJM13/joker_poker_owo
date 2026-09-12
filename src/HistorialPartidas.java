import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Tu ListaDoble guarda AMBOS jugadores en un solo NodoDoble por ronda
 * (nombreJ1/manoJ1/cartasJ1/superoCiegaJ1 + su espejo para J2), así que
 * aquí solo se recorre una vez y se pintan las 2 columnas por fila,
 * igual que en tu mockup.
 *
 * Los campos de NodoDoble no tienen modificador (package-private), y como
 * ninguna de nuestras clases usa `package`, están todas en el paquete por
 * defecto -> se puede acceder directo a actual.nombreJ1, actual.siguiente, etc.
 */
public class HistorialPartidas {

    public static Scene crearEscena(GestorEscenas gestor) {
        VBox raiz = new VBox(15);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Historial jugadas"));

        Partida partida = gestor.getPartida();
        if (partida != null) {
            VBox lista = construirLista(partida.getHistorialRondas());
            ScrollPane scroll = new ScrollPane(lista);
            scroll.setFitToWidth(true);
            raiz.getChildren().add(scroll);
        }

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> gestor.irA(EstadoJuego.MENU_PRINCIPAL));
        raiz.getChildren().add(btnVolver);

        return new Scene(raiz, 900, 550);
    }

    private static VBox construirLista(ListaDoble historial) {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);

        NodoDoble actual = historial.cabeza;
        int ronda = 1;
        if (actual == null) {
            contenedor.getChildren().add(new Label("Historial vacío."));
        }

        while (actual != null) {
            HBox fila = new HBox(40);
            fila.setAlignment(Pos.CENTER);
            fila.getChildren().add(construirColumnaJugador("Ronda " + ronda + " — " + actual.nombreJ1,
                    actual.cartasJ1, actual.manoJ1, actual.superoCiegaJ1, actual.valorCiega));
            fila.getChildren().add(construirColumnaJugador(actual.nombreJ2,
                    actual.cartasJ2, actual.manoJ2, actual.superoCiegaJ2, actual.valorCiega));
            contenedor.getChildren().add(fila);

            actual = actual.siguiente;
            ronda++;
        }
        return contenedor;
    }

    private static VBox construirColumnaJugador(String titulo, String cartas, String mano, boolean supero, int valorCiega) {
        VBox columna = new VBox(4);
        columna.getChildren().add(new Label(titulo));
        columna.getChildren().add(new Label("Cartas: " + cartas));
        columna.getChildren().add(new Label("Mano: " + mano));
        columna.getChildren().add(new Label(supero
                ? "Superó ciega de valor " + valorCiega
                : "Perdió ciega de valor " + valorCiega));
        return columna;
    }
}
