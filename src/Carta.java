public class Carta {
    private String nombre;
    private String palo;
    private int valorNumerico;
    private boolean comunitaria;

    public Carta(String nombre, String palo, int valorNumerico, boolean comunitaria) {
        this.nombre = nombre;
        this.palo = palo;
        this.valorNumerico = valorNumerico;
        this.comunitaria = comunitaria;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPalo() { return palo; }
    public void setPalo(String palo) { this.palo = palo; }

    public int getValorNumerico() { return valorNumerico; }
    public void setValorNumerico(int valorNumerico) { this.valorNumerico = valorNumerico; }

    public boolean isComunitaria() { return comunitaria; }
    public void setComunitaria(boolean comunitaria) { this.comunitaria = comunitaria; }

    @Override
    public String toString() {
        return nombre + " de " + palo + " (Valor: " + valorNumerico + ")";
    }
}