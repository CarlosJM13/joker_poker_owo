public class ListaDoble {
    NodoDoble cabeza;
    NodoDoble cola;
    int contadorRondas; // Para numerar las rondas al imprimir

    public ListaDoble() {
        cabeza = null;
        cola = null;
        contadorRondas = 1;
    }

    public void agregar(String nJ1, String mJ1, String cJ1, boolean sC1,
                        String nJ2, String mJ2, String cJ2, boolean sC2, int valorCiega) {

        NodoDoble nuevo = new NodoDoble(nJ1, mJ1, cJ1, sC1, nJ2, mJ2, cJ2, sC2, valorCiega);

        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        contadorRondas++;
    }

    public void mostrarAdelante() {
        NodoDoble temp = cabeza;
        int ronda = 1;
        if (temp == null) System.out.println("historial vacio.");

        while (temp != null) {
            imprimirFormato(temp, ronda);
            temp = temp.siguiente;
            ronda++;
        }
    }

    public void mostrarAtras() {
        NodoDoble temp = cola;
        int ronda = contadorRondas - 1;
        if (temp == null) System.out.println("historial vacio.");

        while (temp != null) {
            imprimirFormato(temp, ronda);
            temp = temp.anterior;
            ronda--;
        }
    }

    // Metodo de apoyo para imprimir replicando el diseño visual de las tarjetas
    private void imprimirFormato(NodoDoble nodo, int ronda) {
        System.out.println("----------------------------------------");
        System.out.println("               RONDA " + ronda);
        System.out.println("----------------------------------------");

        // Bloque Jugador 1
        System.out.println("Jugador 1: " + nodo.nombreJ1);
        System.out.println("Cartas: " + nodo.cartasJ1);
        System.out.println("Mano: " + nodo.manoJ1);
        if(nodo.superoCiegaJ1) {
            System.out.println("Estado: Superó ciega de valor " + nodo.valorCiega);
        } else {
            System.out.println("Estado: Perdió ciega de valor " + nodo.valorCiega);
        }
        System.out.println("----------------------------------------");

        // Bloque Jugador 2
        System.out.println("Jugador 2: " + nodo.nombreJ2);
        System.out.println("Cartas: " + nodo.cartasJ2);
        System.out.println("Mano: " + nodo.manoJ2);
        if(nodo.superoCiegaJ2) {
            System.out.println("Estado: Superó ciega de valor " + nodo.valorCiega);
        } else {
            System.out.println("Estado: Perdió ciega de valor " + nodo.valorCiega);
        }
        System.out.println("-----------------------------------------");
    }
}