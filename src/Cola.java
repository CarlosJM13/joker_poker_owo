public class Cola {
    private NodoCola frente;
    private NodoCola ultimo;
    private int tamano;

    public Cola() {
        this.frente = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    // Agrega un jugador a la fila
    public void encolar(String jugador) {
        NodoCola nuevoNodo = new NodoCola(jugador);
        if (estaVacia()) {
            frente = nuevoNodo;
            ultimo = nuevoNodo;
        } else {
            ultimo.setSiguiente(nuevoNodo);
            ultimo = nuevoNodo;
        }
        tamano++;
    }

    // Saca al jugador que le toca jugar
    public String desencolar() {
        if (estaVacia()) {
            System.out.println("No hay jugadores en la cola.");
            return null;
        }
        String turnoActual = frente.getJugador();
        frente = frente.getSiguiente();
        if (frente == null) {
            ultimo = null;
        }
        tamano--;
        return turnoActual;
    }

    // Pasa el turno al siguiente jugador (lo saca del frente y lo forma atrás)
    public String pasarTurno() {
        String jugadorActual = desencolar();
        if (jugadorActual != null) {
            encolar(jugadorActual);
        }
        return jugadorActual;
    }

    public boolean estaVacia() {
        return frente == null;
    }
}