public class Revisar {
    public Resultado evaluar(Carta[] centro, Carta[] cartasJugador) {
        Carta[] sieteCartas = new Carta[7];
        // Copiar las 5 cartas del centro
        for (int i = 0; i < 5; i++) {
            sieteCartas[i] = centro[i];
        }
        // Copiar las 2 cartas del jugador
        for (int i = 0; i < 2; i++) {
            sieteCartas[i + 5] = cartasJugador[i];
        }
        Resultado mejor = null;

        for (int a = 0; a < 7; a++) {
            for (int b = a + 1; b < 7; b++) {
                for (int c = b + 1; c < 7; c++) {
                    for (int d = c + 1; d < 7; d++) {
                        for (int e = d + 1; e < 7; e++) {
                            Carta[] mano = {
                                    sieteCartas[a],
                                    sieteCartas[b],
                                    sieteCartas[c],
                                    sieteCartas[d],
                                    sieteCartas[e]
                            };
                            Resultado resultado = evaluarCinco(mano);
                            if (mejor == null || comparar(resultado, mejor) > 0) {
                                mejor = resultado;
                            }
                        }
                    }
                }
            }
        }
        return mejor;
    }

    private Resultado evaluarCinco(Carta[] cartas) {
        boolean color = esColor(cartas);
        int valorEscalera = valorEscalera(cartas);
        boolean escalera = valorEscalera != -1;

        // ESCALERA REAL
        if (color && esEscaleraReal(cartas)) {
            return new Resultado(1000, "Escalera real", new int[]{14});
        }
        // ESCALERA COLOR
        if (color && escalera) {
            return new Resultado(900, "Escalera de color", new int[]{valorEscalera});
        }
        // POKER
        int valorPoker = encontrarCantidad(cartas, 4);
        if (valorPoker != -1) {
            int cartaRestante =
                    encontrarMayorDistinta(cartas, valorPoker);
            return new Resultado(800, "Poker", new int[]{valorPoker,cartaRestante});//qué valor tiene el póker y qué carta sobra
        }
        // FUL HOUSE
        int valorTrio = encontrarCantidad(cartas, 3);
        if (valorTrio != -1) {
            int valorPar = encontrarParDistinto(cartas, valorTrio);
            if (valorPar != -1) {
                return new Resultado(700, "Full house", new int[]{valorTrio,valorPar});
            }
        }
        // COLOR
        if (color) {
            return new Resultado(600, "Color", obtenerValoresOrdenados(cartas));
        }
        // ESCALERA
        if (escalera) {
            return new Resultado(500, "Escalera", new int[]{valorEscalera});
        }
        // TRIO
        if (valorTrio != -1) {
            return new Resultado(400, "Trio", obtenerValoresOrdenados(cartas));
        }
        // DOBLE PAR
        int primerPar = encontrarCantidad(cartas, 2);
        //-1 es no encontre
        if (primerPar != -1) {
            int segundoPar = encontrarParDistinto(cartas, primerPar);
            if (segundoPar != -1) {
                //pa saber cuál de dos números es el mayor o el menor
                int mayorPar = Math.max(primerPar, segundoPar);
                int menorPar = Math.min(primerPar, segundoPar);
                int cartaRestante = encontrarMayorDistinta(cartas, mayorPar, menorPar);
                return new Resultado(300, "Doble par", new int[]{mayorPar, menorPar,cartaRestante});
            }
        }
        // PAR
        int valorPar = encontrarCantidad(cartas, 2);
        if (valorPar != -1) {
            return new Resultado(200, "Par", obtenerValoresOrdenados(cartas));
        }

        // CARTA ALTA
        return new Resultado(100, "Carta alta", obtenerValoresOrdenados(cartas));
    }

    // Comprueba si las 5 cartas son del mismo palo.
    private boolean esColor(Carta[] cartas) {
        String palo = cartas[0].getPalo();
        for (int i = 1; i < cartas.length; i++) {
            if (!cartas[i].getPalo().equalsIgnoreCase(palo)) { //comparar dos textos sin importar si están escritos con may o min
                return false;
            }
        }
        return true;
    }
    // Comprueba si las cartas son 10 J Q K y As.
    private boolean esEscaleraReal(Carta[] cartas) {
        boolean as = false;
        boolean diez = false;
        boolean jota = false;
        boolean reina = false;
        boolean rey = false;

        for (Carta carta : cartas) {
            String nombre = carta.getNombre();
            if (nombre.equalsIgnoreCase("As")) {
                as = true;
            }
            if (nombre.equalsIgnoreCase("10")) {
                diez = true;
            }
            if (nombre.equalsIgnoreCase("J")) {
                jota = true;
            }
            if (nombre.equalsIgnoreCase("Q")) {
                reina = true;
            }
            if (nombre.equalsIgnoreCase("K")) {
                rey = true;
            }
        }
        return as && diez && jota && reina && rey;
    }
    //Devuelve el valor de la escalera.
    //Si no hay, -1.
    private int valorEscalera(Carta[] cartas) {
        int[] valores = new int[5];
        for (int i = 0; i < 5; i++) {
            valores[i] = valor(cartas[i]);
        }

        // Ordenar de menor a mayor
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 5; j++) {
                if (valores[i] > valores[j]) {
                    int temporal = valores[i];
                    valores[i] = valores[j];
                    valores[j] = temporal;
                }
            }
        }

        // Escalera normal
        boolean consecutivas = true;
        for (int i = 0; i < 4; i++) {
            if (valores[i] + 1 != valores[i + 1]) {
                consecutivas = false;
            }
        }
        if (consecutivas) {
            return valores[4];
        }
        return -1;
    }

    private int encontrarCantidad(Carta[] cartas, int cantidad) {
        for (int i = 0; i < 5; i++) {
            int contador = 0;
            for (int j = 0; j < 5; j++) {
                if (mismoValor(cartas[i], cartas[j])) {
                    contador++;
                }
            }
            if (contador == cantidad) {
                return valor(cartas[i]);
            }
        }
        return -1;
    }

    private int encontrarParDistinto(
            Carta[] cartas,
            int valorAnterior) {
        for (int i = 0; i < 5; i++) {
            int contador = 0;
            for (int j = 0; j < 5; j++) {
                if (mismoValor(cartas[i], cartas[j])) {
                    contador++;
                }
            }
            if (contador == 2 &&
                    valor(cartas[i]) != valorAnterior) {
                return valor(cartas[i]);
            }
        }
        return -1;
    }
    //carta altA que aun no usamos
    private int encontrarMayorDistinta(Carta[] cartas, int... valoresExcluidos) {
        int mayor = -1;
        for (int i = 0; i < 5; i++) {
            boolean excluida = false;
            for (int j = 0; j < valoresExcluidos.length; j++) {
                if (valor(cartas[i]) == valoresExcluidos[j]) {
                    excluida = true;
                }
            }
            if (!excluida && valor(cartas[i]) > mayor) {
                mayor = valor(cartas[i]);
            }
        }
        return mayor;
    }

    private int[] obtenerValoresOrdenados(Carta[] cartas) {
        int[] valores = new int[5];
        for (int i = 0; i < 5; i++) {
            valores[i] = valor(cartas[i]);
        }

        // Ordenar de mayor a menor
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 5; j++) {
                if (valores[i] < valores[j]) {
                    int temporal = valores[i];
                    valores[i] = valores[j];
                    valores[j] = temporal;
                }
            }
        }
        return valores;
    }

    private boolean mismoValor(Carta carta1, Carta carta2) {
        return carta1.getNombre().equalsIgnoreCase(
                carta2.getNombre()
        );
    }

    private int valor(Carta carta) {
        if (carta.getNombre().equalsIgnoreCase("As")) {
            return 14;
        }
        if (carta.getNombre().equalsIgnoreCase("K")) {
            return 13;
        }
        if (carta.getNombre().equalsIgnoreCase("Q")) {
            return 12;
        }
        if (carta.getNombre().equalsIgnoreCase("J")) {
            return 11;
        }
        return carta.getValorNumerico();
    }

    //1 mano1 gana
    //-1 mano2 gana
    // 0 empate
    public int comparar(Resultado mano1, Resultado mano2) {
        // Primero se comparan los puntos
        if (mano1.getPuntos() > mano2.getPuntos()) {
            return 1;
        }
        if (mano1.getPuntos() < mano2.getPuntos()) {
            return -1;
        }
        int[] desempate1 = mano1.getDesempate();
        int[] desempate2 = mano2.getDesempate();

        int cantidad = Math.min(desempate1.length, desempate2.length);
        for (int i = 0; i < cantidad; i++) {
            if (desempate1[i] > desempate2[i]) {
                return 1;
            }
            if (desempate1[i] < desempate2[i]) {
                return -1;
            }
        }
        return 0;
    }

    public int determinarGanador(Carta[] centro, Carta[][] jugadores) {
        int ganador = 0;
        Resultado mejorMano = evaluar(centro, jugadores[0]);
        for (int i = 1; i < jugadores.length; i++) {
            Resultado manoActual =
                    evaluar(centro, jugadores[i]);
            if (comparar(manoActual, mejorMano) > 0) {
                mejorMano = manoActual;
                ganador = i;
            }
        }
        return ganador;
    }
}