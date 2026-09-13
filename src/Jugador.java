public class Jugador {
    private String nombre;
    private int dolares;
    private int fichas;
    private Carta[] manoActual;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.dolares = 0;
        this.fichas = 0;
        this.manoActual = new Carta[2];
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

    public int getFichas() {
        return fichas;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
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

    private int mejorasDisponibles = 0;

    public int getMejorasDisponibles() { return mejorasDisponibles; }
    public void agregarMejora() { this.mejorasDisponibles++; }
    public void usarMejora() { this.mejorasDisponibles--; }
}