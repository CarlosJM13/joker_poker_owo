public class Joker {
    private String colorObjetivo; // "Rojo" o "Negro"
    private int bonificacionFichas;

    public Joker(String colorObjetivo, int bonificacionFichas) {
        this.colorObjetivo = colorObjetivo;
        this.bonificacionFichas = bonificacionFichas;
    }

    public String getColorObjetivo() { return colorObjetivo; }
    public int getBonificacionFichas() { return bonificacionFichas; }
}