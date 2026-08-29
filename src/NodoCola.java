public class NodoCola {
    private String jugador;
    private NodoCola siguiente;

    public NodoCola(String jugador) {
        this.jugador = jugador;
        this.siguiente = null;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public NodoCola getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCola siguiente) {
        this.siguiente = siguiente;
    }
}