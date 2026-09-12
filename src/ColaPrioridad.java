public class ColaPrioridad {
    private NodoPrioridad frente;
    private int tamano;

    public ColaPrioridad() {
        this.frente = null;
        this.tamano = 0;
    }

    private void insertar(NodoPrioridad nuevoNodo) {
        // Si está vacía o el nuevo nodo tiene más prioridad que el primero
        if (estaVacia() || nuevoNodo.getPrioridad() > frente.getPrioridad()) {
            nuevoNodo.setSiguiente(frente);
            frente = nuevoNodo;
        } else {
            // Recorre hasta encontrar el punto donde la prioridad ya no es mayor
            NodoPrioridad actual = frente;
            while (actual.getSiguiente() != null &&
                    actual.getSiguiente().getPrioridad() >= nuevoNodo.getPrioridad()) {
                actual = actual.getSiguiente();
            }
            nuevoNodo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevoNodo);
        }
        tamano++;
    }

    public void encolarJoker(Joker joker) {
        insertar(new NodoPrioridad(joker));
    }

    public void encolarCarta(Carta carta) {
        insertar(new NodoPrioridad(carta));
    }

    public NodoPrioridad desencolar() {
        if (estaVacia()) {
            return null;
        }
        NodoPrioridad nodoActual = frente;
        frente = frente.getSiguiente();
        tamano--;
        return nodoActual;
    }

    public boolean estaVacia() {
        return frente == null;
    }
}