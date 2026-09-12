import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor de todo el estado que varias escenas necesitan leer y
 * modificar durante una partida. NO es una escena JavaFX.
 *
 * Nota sobre el Joker comunal: en tu MotorPuntaje.java vive DENTRO de
 * MotorPuntaje (campo privado) y solo se activa pasándole un
 * "nivelRecursividad" (int) a calcularJugadaFinal(...). Por eso aquí NO
 * guardamos una instancia de jokerComunal: solo calculamos qué nivel le
 * toca a la ronda actual con nivelRecursividadJokerComunal(), según tu regla:
 *   rondas 1-3   -> 1 (sin efecto, no se muestra al jugador)
 *   rondas 4-6   -> 2
 *   rondas 7-9   -> 3
 *   rondas 10-12 -> 4
 *   ronda 13+    -> 5 (tope máximo)
 * jokerComunal.aplicarMultiplicador(puntaje, multi, veces) hace
 * puntaje * multi^veces por recursión, así que "veces = 1" ya es el
 * cálculo normal de siempre -- por eso rondas 1-3 (nivel 1) no tienen
 * ningún efecto extra visible.
 */
public class Partida {

    // --- Debuffs de rondas especiales (múltiplo de 3), como constantes para
    // poder compararlos por nombre en Ronda.java sin usar texto suelto ---
    public static final String DEBUFF_COLOR_MITAD = "Color vale la mitad de puntos";
    public static final String DEBUFF_FULLHOUSE_MITAD = "Full House vale la mitad de puntos";
    public static final String DEBUFF_PAR_INHABILITADO = "Par está inhabilitado";
    public static final String DEBUFF_JOKER_COMUNAL_NULO = "El Joker Comunal no aplica esta ronda";

    private Jugador jugador1;
    private Jugador jugador2;

    private int numeroRonda;
    private int metaFichas;

    // TODO: idealmente el mazo completo (52+ cartas) también sería una
    // estructura propia tuya en vez de ArrayList; no me compartiste una
    // clase de "baraja completa" así que la dejo como List<Carta>. Lo que
    // SÍ ya usa tu estructura real es el "catálogo de 3 para escoger" de
    // cada turno, que se arma con tu ListaSimple dentro de Ronda.java.
    private List<Carta> mazoPrincipalJ1 = new ArrayList<>();
    private List<Carta> mazoPrincipalJ2 = new ArrayList<>();

    private List<Carta> mesaComun = new ArrayList<>(); // se llena a 5 cartas: 3 + 2

    private Pila descartesJ1;
    private Pila descartesJ2;
    private Cola colaTurnos; // guarda "J1" / "J2" para saber a quién le toca ceder la compu

    // Comodines comprados por cada jugador en la tienda. Jugador.java no
    // tiene este campo, así que lo llevamos aquí en vez de tocar tu clase.
    private List<Joker> jokersJ1 = new ArrayList<>();
    private List<Joker> jokersJ2 = new ArrayList<>();

    private ListaCircular listaEfectos;   // rota los 4 debuffs de rondas especiales
    private String efectoRondaActual;     // null si esta ronda no es especial

    // OJO: tu ListaDoble.agregar(...) guarda AMBOS jugadores en un solo nodo
    // por ronda -- no es "una lista por jugador" como yo había asumido antes.
    // Por eso aquí hay UNA sola lista para toda la partida.
    private ListaDoble historialRondas;

    private TablaHash barajaExtra = new TablaHash(101); // TODO: ajustar capacidad si esperas muchas copias/mejoras

    // Árbol de mejoras: es UNA sola estructura compartida por todas las
    // cartas del juego (describe las rutas de evolución posibles). Cada
    // Carta solo guarda un puntero (nivelActual) a un nodo de este árbol.
    private ArbolMejoras arbolMejoras = new ArbolMejoras();

    public Partida() {
        this.numeroRonda = 0;

        this.listaEfectos = new ListaCircular();
        this.listaEfectos.agregar(DEBUFF_COLOR_MITAD);
        this.listaEfectos.agregar(DEBUFF_FULLHOUSE_MITAD);
        this.listaEfectos.agregar(DEBUFF_PAR_INHABILITADO);
        this.listaEfectos.agregar(DEBUFF_JOKER_COMUNAL_NULO);

        this.historialRondas = new ListaDoble();
        this.arbolMejoras.inicializarRutas();
    }

    public void reiniciarParaNuevaPartida() {
        this.numeroRonda = 0;
        this.jokersJ1.clear();
        this.jokersJ2.clear();
        jugador1.setDolares(0);
        jugador2.setDolares(0);
        // TODO: rebarajar/reconstruir mazoPrincipalJ1 y mazoPrincipalJ2 desde cero
    }

    /** "Se inicia una nueva ronda, se barajean las cartas y se muestra la meta de fichas". */
    public void iniciarNuevaRonda() {
        numeroRonda++;
        metaFichas = calcularMetaFichas(numeroRonda);
        mesaComun = new ArrayList<>();
        descartesJ1 = new Pila();
        descartesJ2 = new Pila();

        colaTurnos = new Cola();
        colaTurnos.encolar("J1");
        colaTurnos.encolar("J2");

        // Ronda especial cada 3 rondas: se anuncia el debuff de la ListaCircular.
        efectoRondaActual = (numeroRonda % 3 == 0) ? listaEfectos.avanzarEfecto() : null;

        // TODO: barajear mazoPrincipalJ1 / mazoPrincipalJ2 (Fisher-Yates o similar)
    }

    private int calcularMetaFichas(int ronda) {
        // TODO: definir la curva de dificultad real del juego
        return 300 + (ronda * 50);
    }

    /**
     * Nivel de recursividad del Joker Comunal según la ronda actual.
     * Se le pasa TAL CUAL a MotorPuntaje.calcularJugadaFinal(...), salvo
     * que la ronda tenga el debuff DEBUFF_JOKER_COMUNAL_NULO (ver Ronda.java).
     */
    public int nivelRecursividadJokerComunal() {
        int nivel = 1 + (numeroRonda - 1) / 3;
        return Math.min(nivel, 5);
    }

    /** El comodín comunal solo se le muestra al jugador a partir de la ronda 4. */
    public boolean jokerComunalVisible() {
        return numeroRonda >= 4;
    }

    /** "¿Algún jugador no paso la meta?" */
    public boolean algunJugadorPerdio(int puntajeJ1, int puntajeJ2) {
        return puntajeJ1 < metaFichas || puntajeJ2 < metaFichas;
    }

    public void otorgarMonedasIguales(int cantidad) {
        jugador1.agregarDolares(cantidad);
        jugador2.agregarDolares(cantidad);
    }

    // --- getters y setters ---

    public Jugador getJugador1() { return jugador1; }
    public void setJugador1(Jugador jugador1) { this.jugador1 = jugador1; }

    public Jugador getJugador2() { return jugador2; }
    public void setJugador2(Jugador jugador2) { this.jugador2 = jugador2; }

    public int getNumeroRonda() { return numeroRonda; }
    public int getMetaFichas() { return metaFichas; }
    public String getEfectoRondaActual() { return efectoRondaActual; }

    public List<Carta> getMazoPrincipalJ1() { return mazoPrincipalJ1; }
    public List<Carta> getMazoPrincipalJ2() { return mazoPrincipalJ2; }
    public List<Carta> getMesaComun() { return mesaComun; }

    public Pila getDescartesJ1() { return descartesJ1; }
    public Pila getDescartesJ2() { return descartesJ2; }
    public Cola getColaTurnos() { return colaTurnos; }

    public List<Joker> getJokersJ1() { return jokersJ1; }
    public List<Joker> getJokersJ2() { return jokersJ2; }

    public TablaHash getBarajaExtra() { return barajaExtra; }
    public ArbolMejoras getArbolMejoras() { return arbolMejoras; }

    public ListaDoble getHistorialRondas() { return historialRondas; }
}
