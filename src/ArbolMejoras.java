public class ArbolMejoras {
    public NodoArbol raiz;

    public ArbolMejoras() {
        // El estado base empieza con 0 fichas azules y 0 rojas
        this.raiz = new NodoArbol("Estado Base", 0, 0);
    }

    // Metodo para las vias de evolucion
    public void inicializarRutas() {
        // Nivel 1 - Vía Izquierda (Mejora Azul): Muchas azules, poquitas rojas
        raiz.izquierdo = new NodoArbol("Mejora Azul", 5, 1);

        // Nivel 1 - derecha mas al multplicador, muchas rojas, una azul
        raiz.derecho = new NodoArbol("Mejora Roja", 1, 5);

        // Nivel 2 de mejoras
        raiz.izquierdo.izquierdo = new NodoArbol("Legendaria full azul", 12, 2);
        raiz.izquierdo.derecho = new NodoArbol("Legendaria híbrida", 8, 5);

        raiz.derecho.izquierdo = new NodoArbol("Legendaria híbrida", 5, 8);
        raiz.derecho.derecho = new NodoArbol("Legendaria full roja", 2, 12);
    }

    // Recorrido para ver el orden del arbol conforme evoluciono
    public void recorrerArbol(NodoArbol nodo, String prefijo) {
        if (nodo != null) {
            System.out.println(prefijo + nodo.nivelEvolucion + " (+ " + nodo.extraAzules + " azules, + " + nodo.extraRojas + " rojas)");
            recorrerArbol(nodo.izquierdo, prefijo + "  [Izq] -> ");
            recorrerArbol(nodo.derecho, prefijo + "  [Der] -> ");
        }
    }
}