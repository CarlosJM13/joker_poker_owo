import java.util.List;

public class OrdenadorCartas {

    private static int valorDeRango(Carta carta) {
        String n = carta.getNombre();
        if (n == null) return 0;
        n = n.trim();

        if (n.equalsIgnoreCase("A") || n.equalsIgnoreCase("As")) return 14;
        if (n.equalsIgnoreCase("K")) return 13;
        if (n.equalsIgnoreCase("Q")) return 12;
        if (n.equalsIgnoreCase("J")) return 11;

        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException e) {
            return 0; // no debería pasar con la baraja del juego
        }
    }

    public static void ordenarPorValor(List<Carta> cartas) {
        if (cartas == null) return;
        int n = cartas.size();

        for (int i = 0; i < n - 1; i++) {
            int indiceMenor = i;

            // Buscamos la carta de menor rango en el resto de la lista
            for (int j = i + 1; j < n; j++) {
                if (valorDeRango(cartas.get(j)) < valorDeRango(cartas.get(indiceMenor))) {
                    indiceMenor = j;
                }
            }

            // La colocamos en su posición final (solo si hace falta)
            if (indiceMenor != i) {
                Carta temp = cartas.get(i);
                cartas.set(i, cartas.get(indiceMenor));
                cartas.set(indiceMenor, temp);
            }
        }
    }
}