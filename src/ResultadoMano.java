public class ResultadoMano {
    private String nombreMano;
    private int puntajeFinal;

    public ResultadoMano(String nombreMano, int puntajeFinal) {
        this.nombreMano = nombreMano;
        this.puntajeFinal = puntajeFinal;
    }

    public String getNombreMano() {
        return nombreMano;
    }

    public int getPuntajeFinal() {
        return puntajeFinal;
    }
}