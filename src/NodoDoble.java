public class NodoDoble {
    String accion;
    NodoDoble siguiente;
    NodoDoble anterior;

    public NodoDoble(String accion) {
        this.accion = accion;
        this.siguiente = null;
        this.anterior = null;
    }
}