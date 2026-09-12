import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestorEscenas {

    private static GestorEscenas instancia;

    private final Stage stage;
    private EstadoJuego estadoActual;
    private Partida partida; // null hasta que se registran los 2 jugadores

    private GestorEscenas(Stage stage) {
        this.stage = stage;
        this.estadoActual = EstadoJuego.MENU_PRINCIPAL;
    }

    public static void inicializar(Stage stage) {
        instancia = new GestorEscenas(stage);
    }

    public static GestorEscenas getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("GestorEscenas no inicializado. Llama a inicializar(stage) en Main.");
        }
        return instancia;
    }

    public Stage getStage() { return stage; }
    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }
    public EstadoJuego getEstadoActual() { return estadoActual; }

    /**
     * Navegación de alto nivel: menú, registro, historial, arrancar
     * partida, y game over. Todo lo que ocurre DENTRO de una ronda
     * (Ronda.java) usa mostrar(Scene) directamente, sin pasar por aquí.
     */
    public void irA(EstadoJuego siguiente) {
        this.estadoActual = siguiente;
        switch (siguiente) {
            case MENU_PRINCIPAL -> mostrar(MenuPrincipal.crearEscena(this));
            case REGISTRO_JUGADOR_1 -> mostrar(RegistroJugadores.crearEscena(this, 1));
            case REGISTRO_JUGADOR_2 -> mostrar(RegistroJugadores.crearEscena(this, 2));
            case HISTORIAL_PARTIDAS -> mostrar(HistorialPartidas.crearEscena(this));
            case PARTIDA_EN_CURSO -> Ronda.iniciar(this);
            case GAME_OVER -> mostrar(GameOverScena.crearEscena(this));
        }
    }

    /** Usado por Ronda, EscenaTienda y PantallaMejora para cambiar de pantalla sin pasar por el enum. */
    public void mostrar(Scene escena) {
        stage.setScene(escena);
        stage.show();
    }
}
