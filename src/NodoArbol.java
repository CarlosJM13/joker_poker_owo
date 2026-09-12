public class NodoArbol {
    String nivelEvolucion; // tipo de evolucion
    int extraAzules;
    int extraRojas;

    NodoArbol izquierdo; // via para mejorar principalmente fichas azules
    NodoArbol derecho;   // via para mejorar principalmente fichas rojas

    public NodoArbol(String nivelEvolucion, int extraAzules, int extraRojas) {
        this.nivelEvolucion = nivelEvolucion;
        this.extraAzules = extraAzules;
        this.extraRojas = extraRojas;
        this.izquierdo = null;
        this.derecho = null;
    }
}