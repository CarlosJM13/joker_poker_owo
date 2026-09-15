import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Tienda {
    private TablaHash barajaExtra;

    public Tienda(TablaHash barajaExtra) {
        this.barajaExtra = barajaExtra;
    }

    public static void iniciar(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();
        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();

        // Se otorgan exactamente 3 dólares al terminar cada ciega/ronda a ambos jugadores
        int oroGanadoPorRonda = 3;
        j1.setDolares(j1.getDolares() + oroGanadoPorRonda);
        j2.setDolares(j2.getDolares() + oroGanadoPorRonda);

        EscenaTienda.mostrar(gestor, 1, () -> {
            PantallaTransicion.mostrar(gestor, "Cede la computadora al Jugador 2 para la tienda", () -> {
                EscenaTienda.mostrar(gestor, 2, () -> {
                    partida.iniciarNuevaRonda();
                    Ronda.iniciarSecuenciaTurnos(gestor);
                });
            });
        });
    }

    public boolean comprarDuplicarMano(Jugador jugador) {
        int costo = 3;
        if (jugador.getDolares() >= costo) {
            jugador.setDolares(jugador.getDolares() - costo);

            Carta[] mano = jugador.getManoActual();
            for (Carta c : mano) {
                if (c != null && barajaExtra != null) {
                    String claveUnica = c.getNombre() + "_" + c.getPalo() + "_" + System.nanoTime();
                    barajaExtra.insertar(claveUnica, c);
                }
            }
            return true;
        }
        return false;
    }

    // Lógica para comprar comodines de palo rojo o negro
    public boolean comprarComodin(GestorEscenas gestor, int numJugador, String colorObjetivo) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();

        int costo = 3;
        if (jugador.getDolares() >= costo) {
            jugador.setDolares(jugador.getDolares() - costo);

            // Instanciamos el Joker con los 2 argumentos que exige su constructor
            Joker nuevoJoker = new Joker(colorObjetivo, 4); // 4 fichas de bono

            // Lo agregamos a la lista de jokers activa de la partida
            if (numJugador == 1) {
                partida.getJokersJ1().add(nuevoJoker);
            } else {
                partida.getJokersJ2().add(nuevoJoker);
            }

            return true;
        }
        return false;
    }

    // nuevo metodo para poder mejorar la carta directamente
    public boolean comprarMejoraArbol(GestorEscenas gestor, int numJugador, Runnable alTerminarMejora) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();

        int costo = 2; // precio? >:)
        if (jugador.getDolares() >= costo) {
            jugador.setDolares(jugador.getDolares() - costo);

            // Abre la pantalla de mejora y le pasa el llamado a la tienda para regresar a la tienda
            PantallaMejora.mostrar(gestor, numJugador, alTerminarMejora);
            return true;
        }
        return false;
    }
}