public class MotorPuntaje {

    private jokerComunal comunal = new jokerComunal();

    public int calcularJugadaFinal(Carta[] centro, Carta[] cartasJugador, Joker[] jokersActivos, int nivelRecursividad) {
        Carta[] jugadaCompleta = new Carta[7];
        // 7 cartas porque 5 son las de la mesa y 2 son las propias
        System.arraycopy(centro, 0, jugadaCompleta, 0, 5);
        System.arraycopy(cartasJugador, 0, jugadaCompleta, 5, 2);

        int fichasBase = 5;
        int multiBase = 1;

        // 1. Identifica la mano (incluye manos de poker especiales de Joker Poker como 7 flush)
        int maximasIguales = contarMaximasIguales(jugadaCompleta);
        boolean hayColor = comprobarColor(jugadaCompleta);
        boolean hayEscalera = comprobarEscalera(jugadaCompleta);

        // Tabla de puntajes base
        if (hayColor && maximasIguales == 7) { fichasBase = 200; multiBase = 15; } // Seven of a Kind Flush
        else if (maximasIguales == 7) { fichasBase = 150; multiBase = 12; }        // Seven of a Kind
        else if (hayColor && maximasIguales == 6) { fichasBase = 180; multiBase = 14; } // Six of a Kind Flush
        else if (maximasIguales == 6) { fichasBase = 120; multiBase = 10; }        // Six of a Kind
        else if (hayColor && maximasIguales == 5) { fichasBase = 150; multiBase = 12; } // Flush Five
        else if (maximasIguales == 5) { fichasBase = 100; multiBase = 8; }         // Five of a Kind
        else if (hayColor && hayEscalera) { fichasBase = 100; multiBase = 8; }     // Straight Flush
        else if (maximasIguales == 4) { fichasBase = 60; multiBase = 7; }          // Four of a Kind
        else if (esFullHouse(jugadaCompleta)) { fichasBase = 40; multiBase = 4; }  // Full House
        else if (hayColor) { fichasBase = 35; multiBase = 4; }                     // Flush
        else if (hayEscalera) { fichasBase = 30; multiBase = 4; }                  // Straight
        else if (maximasIguales == 3) { fichasBase = 30; multiBase = 3; }          // Three of a kind
        else if (esDoblePar(jugadaCompleta)) { fichasBase = 20; multiBase = 2; }   // Two Pair
        else if (maximasIguales == 2) { fichasBase = 10; multiBase = 2; }          // Pair
        // Carta alta por defecto: 5 x 1 (ya que son las fichas y multi base)

        int totalFichas = fichasBase;
        int totalMulti = multiBase;

        // 2. Encolar las cartas en cola de prioridad para que surta efecto los comodines
        ColaPrioridad calculoPuntaje = new ColaPrioridad();
        // Checamos si no esta vacío y encolamos los comodines
        if (jokersActivos != null) {
            for (Joker j : jokersActivos) {
                if (j != null) calculoPuntaje.encolarJoker(j);
            }
        }
        for (Carta c : jugadaCompleta) {
            calculoPuntaje.encolarCarta(c);
        }

        // 3. sumas y multi
        int bonoRojoActivo = 0;
        int bonoNegroActivo = 0;

        while (!calculoPuntaje.estaVacia()) {
            NodoPrioridad nodo = calculoPuntaje.desencolar();

            if (nodo.esJoker()) {
                Joker j = nodo.getJoker();
                if (j.getColorObjetivo().equalsIgnoreCase("rojo")) {
                    bonoRojoActivo += j.getBonificacionFichas();
                } else if (j.getColorObjetivo().equalsIgnoreCase("negro")) {
                    bonoNegroActivo += j.getBonificacionFichas();
                }
            } else {
                Carta c = nodo.getCarta();
                totalFichas += c.verfichazul();
                totalMulti += c.vermultirojo();

                String palo = c.getPalo();
                if (palo.equalsIgnoreCase("corazon") || palo.equalsIgnoreCase("diamante")) {
                    totalFichas += bonoRojoActivo;
                } else if (palo.equalsIgnoreCase("trebol") || palo.equalsIgnoreCase("pica")) {
                    totalFichas += bonoNegroActivo;
                }
            }
        }

        // 4. Aplicar el Joker Comunal que usa recursividad
        // Se envía el total de fichas como puntaje, el multi como multiplicador, y las veces de recursividad
        int puntajeFinal = comunal.aplicarMultiplicador(totalFichas, totalMulti, nivelRecursividad);

        return puntajeFinal;
    }

    // metodos para manos de 7 cartas teoricamente posibles

    private int contarMaximasIguales(Carta[] cartas) {
        int max = 0;
        for (int i = 0; i < cartas.length; i++) {
            int contador = 0;
            for (int j = 0; j < cartas.length; j++) {
                if (cartas[i].getNombre().equalsIgnoreCase(cartas[j].getNombre())) {
                    contador++;
                }
            }
            if (contador > max) {
                max = contador;
            }
        }
        return max;
    }

    private boolean comprobarColor(Carta[] cartas) {
        // Verifica si al menos 5 cartas o más tienen el mismo palo
        int maxMismoPalo = 0;
        for (int i = 0; i < cartas.length; i++) {
            int contador = 0;
            for (int j = 0; j < cartas.length; j++) {
                if (cartas[i].getPalo().equalsIgnoreCase(cartas[j].getPalo())) {
                    contador++;
                }
            }
            if (contador > maxMismoPalo) maxMismoPalo = contador;
        }
        return maxMismoPalo >= 5;
    }

    private boolean comprobarEscalera(Carta[] cartas) {
        int[] valores = obtenerValoresOrdenadosUnicos(cartas);
        if (valores.length < 5) return false;

        int consecutivas = 1;
        for (int i = 0; i < valores.length - 1; i++) {
            if (valores[i] + 1 == valores[i + 1]) {
                consecutivas++;
                if (consecutivas >= 5) return true;
            } else {
                consecutivas = 1;
            }
        }
        return false;
    }

    private boolean esFullHouse(Carta[] cartas) {
        int maxIguales = contarMaximasIguales(cartas);
        if (maxIguales >= 3) {
            // Buscamos si existe un par para juntar una tercia y un par = fullhouse
            int valorTrio = -1;
            for (int i = 0; i < cartas.length; i++) {
                int count = 0;
                for (Carta c : cartas) if (c.getNombre().equalsIgnoreCase(cartas[i].getNombre())) count++;
                if (count >= 3) { valorTrio = valor(cartas[i]); break; }
            }

            for (int i = 0; i < cartas.length; i++) {
                int count = 0;
                for (Carta c : cartas) if (c.getNombre().equalsIgnoreCase(cartas[i].getNombre())) count++;
                if (count >= 2 && valor(cartas[i]) != valorTrio) return true;
            }
        }
        return false;
    }

    private boolean esDoblePar(Carta[] cartas) {
        int valorPrimerPar = -1;
        for (int i = 0; i < cartas.length; i++) {
            int count = 0;
            for (Carta c : cartas) if (c.getNombre().equalsIgnoreCase(cartas[i].getNombre())) count++;
            if (count >= 2) { valorPrimerPar = valor(cartas[i]); break; }
        }

        if (valorPrimerPar != -1) {
            for (int i = 0; i < cartas.length; i++) {
                int count = 0;
                for (Carta c : cartas) if (c.getNombre().equalsIgnoreCase(cartas[i].getNombre())) count++;
                if (count >= 2 && valor(cartas[i]) != valorPrimerPar) return true;
            }
        }
        return false;
    }

    private int valor(Carta carta) {
        String n = carta.getNombre();
        if (n.equalsIgnoreCase("As")) return 14;
        if (n.equalsIgnoreCase("K")) return 13;
        if (n.equalsIgnoreCase("Q")) return 12;
        if (n.equalsIgnoreCase("J")) return 11;
        return carta.getValorNumerico();
    }

    private int[] obtenerValoresOrdenadosUnicos(Carta[] cartas) {
        // Usamos hash para poder contar la mano de escalera, ya que las cartas duplicadas serían un problema sino
        java.util.HashSet<Integer> set = new java.util.HashSet<>();
        for (Carta c : cartas) set.add(valor(c));
        int[] valores = new int[set.size()];
        int idx = 0;
        for (Integer val : set) valores[idx++] = val;
        java.util.Arrays.sort(valores);
        return valores;
    }
}