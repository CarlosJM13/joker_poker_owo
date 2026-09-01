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
        int contador = 1;
        while (actual != null) {
            System.out.println(contador + ". " + actual.getCarta().toString());
            actual = actual.getSiguiente();
            contador++;
        }
    }

    public int getTamaño() { return tamaño; }

    // Obtiene una carta específica según su posición (1, 2 o 3)
    public Carta obtenerCarta(int indice) {
        NodoSimple actual = cabeza;
        int contador = 1;
        while (actual != null) {
            if (contador == indice) {
                return actual.getCarta();
            }
            actual = actual.getSiguiente();
            contador++;
        }
        return null;
    }

    // Vacía la lista para la siguiente ronda
    public void vaciar() {
        this.cabeza = null;
        this.tamaño = 0;
    }
}