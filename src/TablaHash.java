public class TablaHash {
    private NodoTablaHash[] tabla;
    private int capacidad;
    private int cantidadElementos; // Lleva la cuenta de cuántas cartas extra hay

    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new NodoTablaHash[capacidad];
        this.cantidadElementos = 0;
    }

    // Función Hash para decidir en qué posición del arreglo va la carta
    private int funcionHash(String clave) {
        return Math.abs(clave.hashCode()) % capacidad;
    }

    // Metodo para guardar una carta duplicada en la tabla
    public void insertar(String clave, Carta carta) {
        int indice = funcionHash(clave);
        NodoTablaHash actual = tabla[indice];

        // Si la clave ya existe, solo actualizamos la carta
        while (actual != null) {
            if (actual.getClave().equals(clave)) {
                actual.setCarta(carta);
                return;
            }
            actual = actual.getSiguiente();
        }

        // Si no existe, creamos el nuevo nodo y lo ponemos al inicio de la lista de ese índice
        NodoTablaHash nuevoNodo = new NodoTablaHash(clave, carta);
        nuevoNodo.setSiguiente(tabla[indice]);
        tabla[indice] = nuevoNodo;
        cantidadElementos++;
    }

    // Metodo para buscar una carta específica si conoces su clave
    public Carta buscar(String clave) {
        int indice = funcionHash(clave);
        NodoTablaHash actual = tabla[indice];

        while (actual != null) {
            if (actual.getClave().equals(clave)) {
                return actual.getCarta();
            }
            actual = actual.getSiguiente();
        }
        return null; // No se encontró
    }

    public Carta[] obtenerTodasLasCartas() {
        Carta[] todas = new Carta[cantidadElementos];
        int index = 0;

        for (int i = 0; i < capacidad; i++) {
            NodoTablaHash actual = tabla[i];
            while (actual != null) {
                todas[index] = actual.getCarta();
                index++;
                actual = actual.getSiguiente();
            }
        }
        return todas;
    }

    // Saber cuántas cartas extras hay en total
    public int getCantidadElementos() {
        return cantidadElementos;
    }
}