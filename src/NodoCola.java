public class NodoCola {
    private Carta carta;
    private NodoCola siguiente;

    public NodoCola(Carta carta) {
        this.carta = carta;
        this.siguiente = null;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public NodoCola getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCola siguiente) {
        this.siguiente = siguiente;
    }
}