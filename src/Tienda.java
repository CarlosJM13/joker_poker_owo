public class Tienda {

    private TablaHash barajaExtra;

    public Tienda(TablaHash barajaExtra) {
        this.barajaExtra = barajaExtra;
    }

    // Método que se ejecuta cuando el jugador presiona el botón de comprar la copia de su mano en la Tienda
    public boolean comprarDuplicarMano(Jugador jugador) {
        int costo = 3;

        // Validamos si el jugador tiene suficientes dólares (3 dólares como dictan las reglas del juego)
        if (jugador.getDolares() >= costo) {
            jugador.gastarDolares(costo);

            // Obtenemos la mano actual del jugador y la duplicamos hacia la Tabla Hash
            Carta[] mano = jugador.getManoActual();
            for (Carta c : mano) {
                if (c != null) {
                    // Creamos una copia de la carta conservando sus atributos y evoluciones del árbol
                    Carta copia = new Carta(c.getNombre(), c.getPalo(), c.getValorNumerico(), c.isComunitaria());

                    // Si la carta original ya evolucionó con el árbol, pasamos sus mejoras
                    if (c.nivelActual != null) {
                        copia.setNivelEvolucion(c.nivelActual);
                    }

                    // Generamos una clave única basada en el nombre y un identificador aleatorio
                    String claveUnica = jugador.getNombre() + "_" + copia.getNombre() + "_" + copia.getPalo() + "_" + System.nanoTime();

                    // Insertamos la carta duplicada en tu Tabla Hash (Baraja Extra)
                    barajaExtra.insertar(claveUnica, copia);
                }
            }
            return true; // Compra exitosa
        }

        return false; // No le alcanzó el dinero
    }
}