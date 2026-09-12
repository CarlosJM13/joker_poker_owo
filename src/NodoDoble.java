public class NodoDoble {
    // Datos del Jugador 1
    String nombreJ1;
    String manoJ1;
    String cartasJ1;
    boolean superoCiegaJ1;

    // Datos del Jugador 2
    String nombreJ2;
    String manoJ2;
    String cartasJ2;
    boolean superoCiegaJ2;

    int valorCiega; // El valor que deben superar osea la ciega pe

    NodoDoble siguiente;
    NodoDoble anterior;

    public NodoDoble(String nombreJ1, String manoJ1, String cartasJ1, boolean superoCiegaJ1,
                     String nombreJ2, String manoJ2, String cartasJ2, boolean superoCiegaJ2,
                     int valorCiega) {

        this.nombreJ1 = nombreJ1;
        this.manoJ1 = manoJ1;
        this.cartasJ1 = cartasJ1;
        this.superoCiegaJ1 = superoCiegaJ1;

        this.nombreJ2 = nombreJ2;
        this.manoJ2 = manoJ2;
        this.cartasJ2 = cartasJ2;
        this.superoCiegaJ2 = superoCiegaJ2;

        this.valorCiega = valorCiega;

        this.siguiente = null;
        this.anterior = null;
    }
}