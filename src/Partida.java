import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor de todo el estado que varias escenas necesitan leer y
 * modificar durante una partida. NO es una escena JavaFX.
 *
 * Nota sobre el Joker comunal: es un comodín que el jugador SIEMPRE
 * tiene puesto por default (no se compra en la tienda). Cada vez que se
 * gana una ronda jefe (las rondas múltiplo de 3, las que traen un
 * "efectoRondaActual") se le suma 1 punto -- ver sumarPunto() llamado
 * desde Ronda.pasoEvaluacion(). Cada punto hace que la mano se repita
 * una vez más al calcular el puntaje final:
 *   0 puntos (rondas 1-3, aún no se gana ninguna ronda jefe) -> repite x1 (sin efecto, no se le muestra al jugador)
 *   1 punto  (ganada la 1a ronda jefe, ronda 4 en adelante)  -> repite x2
 *   2 puntos (ganada la 2a ronda jefe)                       -> repite x3
 *   3 puntos (ganada la 3a ronda jefe)                       -> repite x4
 *   4 puntos (ganada la 4a ronda jefe en adelante, tope)     -> repite x5 (máximo)
 * jokerComunal.aplicarMultiplicador(puntaje, multi, veces) hace
 * puntaje * multi^veces por recursión, y "veces" es exactamente
 * jokerComunal.getVecesRepeticion().
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
    // Baraja única compartida entre ambos jugadores
    private List<Carta> barajaComun = new ArrayList<>();

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

    // Instancia ÚNICA y persistente para toda la partida: guarda cuántas
    // rondas jefe se han ganado (ver sumarPunto() en Ronda.pasoEvaluacion).
    private jokerComunal jokerComunal = new jokerComunal();

    public Partida() {
        this.numeroRonda = 0;

        // Inicializamos las pilas de descartes para evitar errores en rondas posteriores
        this.descartesJ1 = new Pila();
        this.descartesJ2 = new Pila();

        this.listaEfectos = new ListaCircular();
        this.listaEfectos.agregar(DEBUFF_COLOR_MITAD);
        this.listaEfectos.agregar(DEBUFF_FULLHOUSE_MITAD);
        this.listaEfectos.agregar(DEBUFF_PAR_INHABILITADO);
        this.listaEfectos.agregar(DEBUFF_JOKER_COMUNAL_NULO);

        this.historialRondas = new ListaDoble();
        this.arbolMejoras.inicializarRutas();

        inicializarBarajaCompartida();
    }

    public void reiniciarParaNuevaPartida() {
        this.numeroRonda = 0;
        this.jokersJ1.clear();
        this.jokersJ2.clear();
        this.jokerComunal = new jokerComunal();
        jugador1.setDolares(0);
        jugador2.setDolares(0);
        // TODO: rebarajar/reconstruir mazoPrincipalJ1 y mazoPrincipalJ2 desde cero
    }

    /** "Se inicia una nueva ronda, se barajean las cartas y se muestra la meta de fichas". */
    public void iniciarNuevaRonda() {
        this.numeroRonda++;
        this.mesaComun.clear();
        this.metaFichas = calcularMetaFichas(this.numeroRonda);

        if (numeroRonda % 3 == 0) {
            this.efectoRondaActual = listaEfectos.avanzarEfecto();
        } else {
            this.efectoRondaActual = null;
        }

        // Rellenar la baraja común con las cartas base usando tu método existente
        inicializarBarajaCompartida();

        // Agregamos las cartas compradas/duplicadas en la tienda (TablaHash)
        if (this.barajaExtra != null) {
            Carta[] extras = this.barajaExtra.obtenerTodasLasCartas();
            if (extras != null) {
                for (Carta c : extras) {
                    if (c != null) {
                        this.barajaComun.add(c);
                    }
                }
            }
        }

        // Se mezclan todas las cartas (base + extras) como en Balatro
        java.util.Collections.shuffle(this.barajaComun);
    }

    /**
     * Curva de dificultad de la meta de fichas ("el mínimo que pide" la
     * ronda para no perder). Antes era lineal (+25 fijo por ronda), lo cual
     * se queda muy corto contra el Joker Comunal: a partir de la ronda 4
     * este empieza a repetir la mano varias veces (multiplicador al
     * cuadrado, al cubo...), así que el puntaje de los jugadores crece
     * MUCHO más rápido que una recta. Por eso ahora la meta crece de forma
     * geométrica (28% más por ronda) y además da un salto extra del 50%
     * en cada ronda jefe (múltiplo de 3), justo cuando el Joker Comunal
     * también sube de nivel.
     */
    private int calcularMetaFichas(int ronda) {
        double base = 40.0;
        double meta = base * Math.pow(1.28, ronda - 1);

        if (ronda % 3 == 0) {
            meta *= 1.5; // ronda jefe: dificultad extra
        }

        // Redondeado a múltiplos de 5 para que se vea limpio en pantalla
        return (int) (Math.round(meta / 5.0) * 5);
    }

    /**
     * Nivel de recursividad del Joker Comunal según cuántas rondas jefe se
     * han ganado ya en esta partida. Se le pasa TAL CUAL a
     * MotorPuntaje.calcularJugadaConDetalle(...), salvo que la ronda tenga
     * el debuff DEBUFF_JOKER_COMUNAL_NULO (ver Ronda.java).
     */
    public int nivelRecursividadJokerComunal() {
        return jokerComunal.getVecesRepeticion();
    }

    /** El comodín comunal se le muestra al jugador desde que gana su primera ronda jefe. */
    public boolean jokerComunalVisible() {
        return jokerComunal.getPuntos() > 0;
    }

    public jokerComunal getJokerComunal() {
        return jokerComunal;
    }

    /** "¿Algún jugador no paso la meta?" */
    public boolean algunJugadorPerdio(int puntajeJ1, int puntajeJ2) {
        return puntajeJ1 < metaFichas || puntajeJ2 < metaFichas;
    }

    public void otorgarMonedasIguales(int cantidad) {
        jugador1.agregarDolares(cantidad);
        jugador2.agregarDolares(cantidad);
    }

    private void inicializarBarajaCompartida() {
        barajaComun.clear();
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] palos = {"Corazon", "Diamante", "Pica", "Trebol"};

        for (String palo : palos) {
            for (String val : valores) {
                barajaComun.add(new Carta(val, palo, 10, false));
            }
        }
        java.util.Collections.shuffle(barajaComun);
    }

    public List<Carta> getBarajaComun() {
        return barajaComun;
    }

    // --- getters y setters ---

    private boolean ultimaRondaPerdioJ1;
    private boolean ultimaRondaPerdioJ2;
    private long ultimaRondaPuntajeJ1;
    private long ultimaRondaPuntajeJ2;

    public void setResultadoUltimaRonda(boolean perdioJ1, boolean perdioJ2, long puntajeJ1, long puntajeJ2) {
        this.ultimaRondaPerdioJ1 = perdioJ1;
        this.ultimaRondaPerdioJ2 = perdioJ2;
        this.ultimaRondaPuntajeJ1 = puntajeJ1;
        this.ultimaRondaPuntajeJ2 = puntajeJ2;
    }

    public boolean isUltimaRondaPerdioJ1() { return ultimaRondaPerdioJ1; }
    public boolean isUltimaRondaPerdioJ2() { return ultimaRondaPerdioJ2; }
    public long getUltimaRondaPuntajeJ1() { return ultimaRondaPuntajeJ1; }
    public long getUltimaRondaPuntajeJ2() { return ultimaRondaPuntajeJ2; }



    public Jugador getJugador1() { return jugador1; }
    public void setJugador1(Jugador jugador1) { this.jugador1 = jugador1; }

    public Jugador getJugador2() { return jugador2; }
    public void setJugador2(Jugador jugador2) { this.jugador2 = jugador2; }

    public int getNumeroRonda() { return numeroRonda; }
    public int getMetaFichas() { return metaFichas; }
    public String getEfectoRondaActual() { return efectoRondaActual; }

    public List<Carta> getMazoPrincipalJ1() { return barajaComun; }
    public List<Carta> getMazoPrincipalJ2() { return barajaComun; }
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