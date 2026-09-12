public class NodoPrioridad {
    private Joker joker;
    private Carta carta;
    private int prioridad; // 1 = Joker (sale primero), 0 = Carta (sale después)
    private NodoPrioridad siguiente;

    // Constructor si se encola un Joker
    public NodoPrioridad(Joker joker) {
        this.joker = joker;
        this.carta = null;
        this.prioridad = 1;
        this.siguiente = null;
    }

    // Constructor si se encola una Carta
    public NodoPrioridad(Carta carta) {
        this.joker = null;
        this.carta = carta;
        this.prioridad = 0;
        this.siguiente = null;
    }

    public boolean esJoker() { return joker != null; }

    public Joker getJoker() { return joker; }
    public Carta getCarta() { return carta; }
    public int getPrioridad() { return prioridad; }

    public NodoPrioridad getSiguiente() { return siguiente; }
    public void setSiguiente(NodoPrioridad siguiente) { this.siguiente = siguiente; }
}