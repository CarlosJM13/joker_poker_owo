import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Carta {
    private String nombre;
    private String palo;
    private int valorNumerico;
    private boolean comunitaria;
    private int fichaAzules;
    private int multiRojo;

    // Atributo para rastrear el nivel de evolucion en el arbol
    public NodoArbol nivelActual;

    //Fichas azules agregan fichas al puntaje.
    //Multiplicadores rojos aumentan el multiplicador para consultar los multiplicadores
    public int verfichazul(){return fichaAzules;}
    public int vermultirojo(){return multiRojo;}

    public Carta(String nombre, String palo, int valorNumerico, boolean comunitaria) {
        this.nombre = nombre;
        this.palo = palo;
        this.valorNumerico = valorNumerico;
        this.comunitaria = comunitaria;

        // Se inicializan en 0 como indica el estado base del diagrama
        this.fichaAzules = 0;
        this.multiRojo = 0;
    }
    // Metodo para asignar evolución y sumar los atributos del árbol
    public void setNivelEvolucion(NodoArbol nuevoNivel) {
        this.nivelActual = nuevoNivel;

        // Sumamos las estadísticas que otorga este nuevo nodo del árbol a la carta
        this.fichaAzules += nuevoNivel.extraAzules;
        this.multiRojo += nuevoNivel.extraRojas;
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
        //Ahora muestra las fichas azules y rojas actuales de la carta para se pueda ver el efecto en consola
        return nombre + " de " + palo + " (Valor: " + valorNumerico + " | Azules: +" + fichaAzules + ", Rojas: +" + multiRojo + ")";
    }

    public boolean esRoja() {
        return palo.equalsIgnoreCase("corazon") || palo.equalsIgnoreCase("diamante");
    }

    public boolean esNegra() {
        return palo.equalsIgnoreCase("trebol") || palo.equalsIgnoreCase("pica");
    }
}