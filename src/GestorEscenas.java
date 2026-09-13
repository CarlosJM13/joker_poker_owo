import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GestorEscenas {
    private Stage stage;
    private Partida partida;

    public GestorEscenas(Stage stage) {
        this.stage = stage;
    }

    // --- NUEVOS MÉTODOS QUE RONDA.JAVA NECESITA ---
    public void mostrar(Scene scene) {
        stage.setScene(scene);
    }

    public void irA(EstadoJuego estado) {
        switch (estado) {
            case MENU_PRINCIPAL: mostrarMenuPrincipal(); break;
            case REGISTRO_JUGADOR_1:
            case REGISTRO_JUGADOR_2: mostrarRegistroJugadores(); break;
            case HISTORIAL_PARTIDAS: mostrarHistorial(); break;
            case PARTIDA_EN_CURSO: iniciarRonda(); break;
            case GAME_OVER: mostrarGameOver(); break;
        }
    }
    // ----------------------------------------------

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
        // Delegado a la llamada estática de Ronda.java
    }

    public void mostrarHistorial() {
        HistorialPartidas historial = new HistorialPartidas(partida.getHistorialRondas(), this);
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