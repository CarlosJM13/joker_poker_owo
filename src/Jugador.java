public class Jugador {
    private String nombre;
    private int dolares;
    private Carta[] manoActual;

    // Inicializa al jugador con su nombre, 0 dólares y espacio para su mano
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.dolares = 0;
        this.manoActual = new Carta[2]; // Espacio para las 2 cartas finales con las que juega
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDolares() {
        return dolares;
    }

    public void setDolares(int dolares) {
        this.dolares = dolares;
    }

    public Carta[] getManoActual() {
        return manoActual;
    }

    public void setManoActual(Carta[] manoActual) {
        this.manoActual = manoActual;
    }

    // MÉTODOS PARA LA ECONOMÍA DE LA TIENDA

    // Añade dólares al ganar una ronda
    public void agregarDolares(int cantidad) {
        this.dolares += cantidad;
    }

    // Intenta gastar dólares en la tienda. Retorna true si le alcanza, false si no.
    public boolean gastarDolares(int cantidad) {
        if (this.dolares >= cantidad) {
            this.dolares -= cantidad;
            return true;
        }
        return false;
    }
}