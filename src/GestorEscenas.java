import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.List;
import java.util.ArrayList;

public class GestorEscenas {
    private Stage stage;
    private Partida partida;

    public GestorEscenas(Stage stage) {
        this.stage = stage;
    }

    public void mostrar(Scene scene) {
        // 1. Preparamos el efecto de transicion
        javafx.scene.Parent root = scene.getRoot();
        root.setOpacity(0); // Inicia invisible

        EfectosVisuales.aplicarCRT(root); // Efecto de pantalla retro en TODAS las escenas

        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(500), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0); // Termina 100% visible
        ft.play();

        // 2. Mostramos la escena
        stage.setScene(scene);
    }

    public void mostrarMenuPrincipal() {
        MenuPrincipal menu = new MenuPrincipal(this);
        mostrar(new Scene(menu.getRoot(), 1024, 768));
    }

    public void mostrarRegistroJugadores() {
        RegistroJugadores registro = new RegistroJugadores(this);
        mostrar(new Scene(registro.getRoot(), 1024, 768));
    }

    public void mostrarPartida(String nombre1, String nombre2) {
        partida = new Partida();
        partida.setJugador1(new Jugador(nombre1));
        partida.setJugador2(new Jugador(nombre2));

        // --- CARTAS DE PRUEBA CON EL FORMATO REAL DE TUS PNGs ---
        String[] cartasPrueba = {"2C", "3D", "4P", "5T", "6C"};
        for (String nombre : cartasPrueba) {
            // Extraemos el valor numérico básico para que funcione la lógica
            int val = Character.isDigit(nombre.charAt(0)) ? Character.getNumericValue(nombre.charAt(0)) : 10;
            String paloCarta = nombre.endsWith("C") ? "Corazon" : nombre.endsWith("D") ? "Diamante" : nombre.endsWith("P") ? "Pica" : "Trebol";

            partida.getMazoPrincipalJ1().add(new Carta(nombre, paloCarta, val, false));
            partida.getMazoPrincipalJ2().add(new Carta(nombre, paloCarta, val, false));
        }

        iniciarRonda();
    }

    public void iniciarRonda() {
        if (partida.getNumeroRonda() >= 13) {
            mostrarGameOver();
        } else {
            Ronda.iniciar(this);
        }
    }

    public void mostrarTienda() {
        // Delegado a la llamada estática de la clase ronda.java
    }

    public void mostrarHistorial() {
        // Si la partida todavia no se juega nada, creamos una ListaDoble vacía para que no truene
        ListaDoble datosHistorial;
        if (partida != null && partida.getHistorialRondas() != null) {
            datosHistorial = partida.getHistorialRondas();
        } else {
            datosHistorial = new ListaDoble();
        }

        // Nota el orden: primero la ListaDoble, luego el gestor
        HistorialPartidas historial = new HistorialPartidas(datosHistorial, this);
        mostrar(new Scene(historial.getRoot(), 1024, 768));
    }

    public void mostrarGameOver() {
        GameOverScena gameOver = new GameOverScena(partida, this);
        mostrar(new Scene(gameOver.getRoot(), 1024, 768));
    }

    public void salir() {
        stage.close();
    }

    public Partida getPartida() {
        return partida;
    }

}