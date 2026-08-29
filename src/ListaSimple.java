public class ListaSimple {
    private NodoSimple cabeza;
    private int tamaño;

    public ListaSimple() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    public void agregarCarta(Carta carta) {
        NodoSimple nuevoNodo = new NodoSimple(carta);

        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoSimple actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamaño++;
    }

    public void mostrarCatalogo() {
        NodoSimple actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getCarta().toString());
            actual = actual.getSiguiente();
        }
    }

    public int getTamaño() { return tamaño; }
}
