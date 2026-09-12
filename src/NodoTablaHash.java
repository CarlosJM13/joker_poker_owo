public class NodoTablaHash {
    private String clave;
    private Carta carta;
    private NodoTablaHash siguiente;

    public NodoTablaHash(String clave, Carta carta) {
        this.clave = clave;
        this.carta = carta;
        this.siguiente = null;
    }

    // Getters
    public String getClave() { return clave; }
    public Carta getCarta() { return carta; }
    public NodoTablaHash getSiguiente() { return siguiente; }

    // Setters
    public void setCarta(Carta carta) { this.carta = carta; }
    public void setSiguiente(NodoTablaHash siguiente) { this.siguiente = siguiente; }
}