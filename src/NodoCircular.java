public class NodoCircular {
    String efecto;
    NodoCircular siguiente;

    public NodoCircular(String efecto) {
        this.efecto = efecto;
        this.siguiente = this;
    }
}