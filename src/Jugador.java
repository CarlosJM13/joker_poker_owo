public class Jugador {
    private String nombre;
    private int dolares;
    private Carta[] manoActual;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.dolares = 0;
        this.manoActual = new Carta[2]; // Espacio para las 2 cartas con las que juega en la ronda
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

    public void agregarDolares(int cantidad) {
        this.dolares += cantidad;
    }

    public boolean gastarDolares(int cantidad) {
        if (this.dolares >= cantidad) {
            this.dolares -= cantidad;
            return true;
        }
        return false;
    }
}