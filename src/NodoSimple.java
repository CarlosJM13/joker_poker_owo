public class NodoSimple {
    private Carta carta;
    private NodoSimple siguiente;

    public NodoSimple(Carta carta) {
        this.carta = carta;
        this.siguiente = null;
    }

    public Carta getCarta() { return carta; }
    public void setCarta(Carta carta) { this.carta = carta; }

    public NodoSimple getSiguiente() { return siguiente; }
    public void setSiguiente(NodoSimple siguiente) { this.siguiente = siguiente; }
}
