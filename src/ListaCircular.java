public class ListaCircular {
    NodoCircular cabeza;
    NodoCircular actual;

    public ListaCircular() {
        cabeza = null;
        actual = null;
    }

    public void agregar(String efecto) {
        NodoCircular nuevo = new NodoCircular(efecto);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
            actual = cabeza;
        } else {
            NodoCircular temp = cabeza;
            while (temp.siguiente != cabeza) {
                temp = temp.siguiente;
            }
            temp.siguiente = nuevo;
            nuevo.siguiente = cabeza;
        }
    }

    // cada que se inicie una ronda nueva, se manda llamar esta funcion para elegir los efectos
    public String avanzarEfecto() {
        if (actual != null) {
            String efectoActual = actual.efecto;
            actual = actual.siguiente;
            return efectoActual;
        }
        return "Sin efecto";
    }
}