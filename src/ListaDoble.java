public class ListaDoble {
    NodoDoble cabeza;
    NodoDoble cola;

    public ListaDoble() {
        cabeza = null;
        cola = null;
    }

    public void agregar(String accion) {
        NodoDoble nuevo = new NodoDoble(accion);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
    }

    public void mostrarAdelante() {
        NodoDoble temp = cabeza;
        while (temp != null) {
            System.out.println(temp.accion);
            temp = temp.siguiente;
        }
    }

    public void mostrarAtras() {
        NodoDoble temp = cola;
        while (temp != null) {
            System.out.println(temp.accion);
            temp = temp.anterior;
        }
    }
}