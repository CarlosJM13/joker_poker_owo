public class Pila {
    private NodoPila tope;

    //pa que al inicio tenga null
    public Pila() {
        tope = null;
    }
    public boolean estaVacia() {
        return tope == null;
    }
    //Luis hizo la clase carta, aqui meto una carta en la pila de tres que los jugadores van a elegir
    public void apilar(Carta carta) {
        //caja con carta
        NodoPila NNUEVO = new NodoPila(carta);
        NNUEVO.siguiente = tope;
        tope = NNUEVO;
    }

    //este es cuando llega la segunda ronda
    public Carta desapilar() {
        if (estaVacia()) {
            return null;
        }
        //crea una variable llamada carta de tipo carta y ahi mete la cartaque esta en el tope
        Carta carta = tope.carta;
        tope = tope.siguiente;
        return carta;
    }

    public void mostrar() {
        NodoPila actual = tope;
        while (actual != null) {
            System.out.println(actual.carta);
            actual = actual.siguiente;
        }
    }
}
