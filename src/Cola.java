public class Cola {
    private NodoCola frente;
    private NodoCola ultimo;
    private int tamano;

    public Cola() {
        this.frente = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    // Agrega una carta al final de la cola (por ejemplo, al armar/barajar el mazo)
    public void encolar(Carta carta) {
        NodoCola nuevoNodo = new NodoCola(carta);
        if (estaVacia()) {
            frente = nuevoNodo;
            ultimo = nuevoNodo;
        } else {
            ultimo.setSiguiente(nuevoNodo);
            ultimo = nuevoNodo;
        }
        tamano++;
    }

    // Saca y devuelve la carta que esta al frente (la siguiente en repartirse)
    public Carta desencolar() {
        if (estaVacia()) {
            System.out.println("La cola esta vacia, no hay cartas para repartir.");
            return null;
        }
        Carta cartaSalida = frente.getCarta();
        frente = frente.getSiguiente();
        if (frente == null) {
            ultimo = null;
        }
        tamano--;
        return cartaSalida;
    }

    // Permite ver la carta del frente sin sacarla todavia de la cola
    public Carta verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.getCarta();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamano() {
        return tamano;
    }

    // Metodo extra util para depurar: muestra todas las cartas que quedan
    public void mostrarCola() {
        NodoCola actual = frente;
        System.out.print("Cola actual: ");
        while (actual != null) {
            System.out.print("[" + actual.getCarta() + "] ");
            actual = actual.getSiguiente();
        }
        System.out.println();
    }
}