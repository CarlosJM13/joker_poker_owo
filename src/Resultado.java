public class Resultado {
    private int puntos;
    private String nombreMano;
    private int[] desempate;

    public Resultado(int puntos, String nombreMano, int[] desempate) {
        this.puntos = puntos;
        this.nombreMano = nombreMano;
        this.desempate = desempate;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getNombreMano() {
        return nombreMano;
    }

    public int[] getDesempate() {
        return desempate;
    }
}