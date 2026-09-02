import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Estructuras de datos para el juego
        Cola turnos = new Cola();
        Pila pilaDescartesParaJ2 = new Pila();
        Pila pilaDescartesParaJ1 = new Pila();
        ListaCircular efectos = new ListaCircular();
        ListaDoble historial = new ListaDoble();
        Revisar motorPoker = new Revisar();

        ListaSimple mazoJugador1 = new ListaSimple();
        ListaSimple mazoJugador2 = new ListaSimple();

        // Efectos de ronda
        efectos.agregar("El Full House vale la mitad de puntos");
        efectos.agregar("Las Escaleras dan el doble de puntos");
        efectos.agregar("Los Pares valen la mitad de puntos");
        efectos.agregar("Sin debuffo esta ronda");

        String jugador1 = "";
        String jugador2 = "";
        boolean jugadoresRegistrados = false;
        boolean salir = false;

        String[] palos = {"Picas", "Corazones", "Diamantes", "Treboles"};
        String[] nombresCartas = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "As"};

        while (!salir) {
            limpiarConsola();
            System.out.println("========================================");
            System.out.println("             JOKER POKER                ");
            System.out.println("========================================");
            System.out.println("1. Registrar Jugadores y Jugar");
            System.out.println("2. Ver historial hacia adelante");
            System.out.println("3. Ver historial hacia atrás");
            System.out.println("4. Salir del programa");
            System.out.println("----------------------------------------");
            System.out.print("Elige una opción: ");

            // este es el nuevo metodo para evitar errores con la clase validador aqui para las opciones principales (1)
            int opcion = Validador.pedirOpcion("Elige una opción (1-4): ", 1, 4);

            switch (opcion) {
                case 1:
                    // Pedir nombres si es la primera vez que se juega
                    if (!jugadoresRegistrados) {
                        limpiarConsola();
                        System.out.println("=== REGISTRO DE JUGADORES ===");
                        System.out.print("Nombre del Jugador 1: ");
                        jugador1 = scanner.nextLine();
                        System.out.print("Nombre del Jugador 2: ");
                        jugador2 = scanner.nextLine();
                        jugadoresRegistrados = true;

                        turnos.encolar(jugador1);
                        turnos.encolar(jugador2);
                    }

                    limpiarConsola();
                    System.out.println("Barajando y repartiendo cartas...");

                    Carta[] mazoCentral = inicializarBaraja(random, palos, nombresCartas);
                    int indexMazo = 0;

                    // Repartir 3 cartas a cada jugador
                    mazoJugador1.vaciar();
                    mazoJugador1.agregarCarta(mazoCentral[indexMazo++]);
                    mazoJugador1.agregarCarta(mazoCentral[indexMazo++]);
                    mazoJugador1.agregarCarta(mazoCentral[indexMazo++]);

                    mazoJugador2.vaciar();
                    mazoJugador2.agregarCarta(mazoCentral[indexMazo++]);
                    mazoJugador2.agregarCarta(mazoCentral[indexMazo++]);
                    mazoJugador2.agregarCarta(mazoCentral[indexMazo++]);

                    System.out.println("Cartas listas en mano (" + mazoJugador1.getTamaño() + " repartidas a cada uno).");

                    // --- SELECCION INICIAL J1 ---
                    limpiarConsola();
                    String turnoActual = turnos.pasarTurno();
                    System.out.println("========================================");
                    System.out.println("Turno de: " + turnoActual);
                    System.out.println("========================================");
                    System.out.println("Tus cartas disponibles:");
                    mazoJugador1.mostrarCatalogo();
                    System.out.println("----------------------------------------");
                    System.out.print("Selecciona la carta que conservas (1, 2 o 3): ");

                    // este es el nuevo metodo para evitar errores con la clase validador aqui jugador 1 (2)
                    int opJ1 = Validador.pedirOpcion("Selecciona la carta que conservas (1, 2 o 3): ", 1, 3);

                    Carta cartaElegidaJ1 = mazoJugador1.obtenerCarta(opJ1);
                    for (int i = 1; i <= 3; i++) {
                        if (i != opJ1) pilaDescartesParaJ2.apilar(mazoJugador1.obtenerCarta(i));
                    }

                    // --- SELECCION INICIAL J2 ---
                    limpiarConsola();
                    turnoActual = turnos.pasarTurno();
                    System.out.println("========================================");
                    System.out.println("Turno de: " + turnoActual);
                    System.out.println("========================================");
                    System.out.println("Tus cartas disponibles:");
                    mazoJugador2.mostrarCatalogo();
                    System.out.println("----------------------------------------");
                    System.out.print("Selecciona la carta que conservas (1, 2 o 3): ");

                    // este es el nuevo metodo para evitar errores con la clase validador en este caso jugador 2 (3)
                    int opJ2 = Validador.pedirOpcion("Selecciona la carta que conservas (1, 2 o 3): ", 1, 3);

                    Carta cartaElegidaJ2 = mazoJugador2.obtenerCarta(opJ2);
                    for (int i = 1; i <= 3; i++) {
                        if (i != opJ2) pilaDescartesParaJ1.apilar(mazoJugador2.obtenerCarta(i));
                    }

                    // --- REVELAR CARTAS COMUNITARIAS ---
                    limpiarConsola();
                    String efectoRonda = efectos.avanzarEfecto();
                    System.out.println("========================================");
                    System.out.println("Efecto activo: " + efectoRonda);
                    System.out.println("========================================");

                    System.out.println("Cartas comunitarias destapadas:");
                    Carta[] centro = new Carta[5];
                    for(int i = 0; i < 3; i++) {
                        centro[i] = mazoCentral[indexMazo++];
                        centro[i].setComunitaria(true);
                        System.out.println("  [" + (i+1) + "] " + centro[i]);
                    }

                    System.out.println("\nPresiona ENTER para ir a la fase de descartes...");
                    scanner.nextLine();

                    // --- DESCARTES J1 ---
                    limpiarConsola();
                    turnoActual = turnos.pasarTurno();
                    System.out.println("========================================");
                    System.out.println("Turno de: " + turnoActual);
                    System.out.println("Cartas disponibles para tomar:");
                    System.out.println("----------------------------------------");

                    Carta d1_j1 = pilaDescartesParaJ1.desapilar();
                    Carta d2_j1 = pilaDescartesParaJ1.desapilar();
                    Carta extraMazo_j1 = mazoCentral[indexMazo++]; // Siguiente carta del mazo

                    System.out.println("1. " + d1_j1 + " (Pila descartes rival)");
                    System.out.println("2. " + d2_j1 + " (Pila descartes rival)");
                    System.out.println("3. " + extraMazo_j1 + " (Mazo Central)");
                    System.out.println("----------------------------------------");
                    System.out.print("Elige la carta que quieres sumar a tu mano (1, 2 o 3): ");

                    // este es el nuevo metodo para evitar errores con la clase validador, en este caso descartes (4)
                    int descJ1 = Validador.pedirOpcion("Elige la carta que quieres sumar a tu mano (1, 2 o 3): ", 1, 3);

                    Carta cartaDescarteJ1 = (descJ1 == 1) ? d1_j1 : (descJ1 == 2) ? d2_j1 : extraMazo_j1;

                    // --- DESCARTES J2 ---
                    limpiarConsola();
                    turnoActual = turnos.pasarTurno();
                    System.out.println("========================================");
                    System.out.println("Turno de: " + turnoActual);
                    System.out.println("Cartas disponibles para tomar:");
                    System.out.println("----------------------------------------");

                    Carta d1_j2 = pilaDescartesParaJ2.desapilar();
                    Carta d2_j2 = pilaDescartesParaJ2.desapilar();
                    Carta extraMazo_j2 = mazoCentral[indexMazo++]; // Siguiente carta del mazo

                    System.out.println("1. " + d1_j2 + " (Pila descartes rival)");
                    System.out.println("2. " + d2_j2 + " (Pila descartes rival)");
                    System.out.println("3. " + extraMazo_j2 + " (Mazo Central)");
                    System.out.println("----------------------------------------");
                    System.out.print("Elige la carta que quieres sumar a tu mano (1, 2 o 3): ");

                    // este es el nuevo metodo para evitar errores con la clase validador en este caso descartes del p2 (5)
                    int descJ2 = Validador.pedirOpcion("Elige la carta que quieres sumar a tu mano (1, 2 o 3): ", 1, 3);

                    Carta cartaDescarteJ2 = (descJ2 == 1) ? d1_j2 : (descJ2 == 2) ? d2_j2 : extraMazo_j2;

                    // --- EVALUAR MANOS Y SACAR GANADOR ---
                    limpiarConsola();
                    centro[3] = mazoCentral[indexMazo++]; centro[3].setComunitaria(true);
                    centro[4] = mazoCentral[indexMazo++]; centro[4].setComunitaria(true);

                    Carta[] manoJugador1 = { cartaElegidaJ1, cartaDescarteJ1 };
                    Carta[] manoJugador2 = { cartaElegidaJ2, cartaDescarteJ2 };
                    Carta[][] todosLosJugadores = { manoJugador1, manoJugador2 };

                    int indiceGanador = motorPoker.determinarGanador(centro, todosLosJugadores);
                    String ganadorPartida = (indiceGanador == 0) ? jugador1 : jugador2;
                    Resultado resJ1 = motorPoker.evaluar(centro, manoJugador1);
                    Resultado resJ2 = motorPoker.evaluar(centro, manoJugador2);

                    System.out.println("========================================");
                    System.out.println("           RESULTADOS FINALES           ");
                    System.out.println("========================================");
                    System.out.println("CARTAS EN LA MESA:");
                    for (int i = 0; i < centro.length; i++) {
                        System.out.println("  " + (i + 1) + ". " + centro[i]);
                    }
                    System.out.println("----------------------------------------");
                    System.out.println("Mano de " + jugador1 + ":");
                    System.out.println("  Cartas usadas: [" + cartaElegidaJ1 + "] y [" + cartaDescarteJ1 + "]");
                    System.out.println("  Jugada armada: " + resJ1.getNombreMano());
                    System.out.println("----------------------------------------");
                    System.out.println("Mano de " + jugador2 + ":");
                    System.out.println("  Cartas usadas: [" + cartaElegidaJ2 + "] y [" + cartaDescarteJ2 + "]");
                    System.out.println("  Jugada armada: " + resJ2.getNombreMano());
                    System.out.println("========================================");
                    System.out.println("¡GANADOR DE LA RONDA: " + ganadorPartida.toUpperCase() + "!");
                    System.out.println("========================================");

                    // Guardar registro en el historial
                    String detalleHistorial = "Ganador: " + ganadorPartida +
                            " | " + jugador1 + ": [" + cartaElegidaJ1 + ", " + cartaDescarteJ1 + "] -> " + resJ1.getNombreMano() +
                            " vs " + jugador2 + ": [" + cartaElegidaJ2 + ", " + cartaDescarteJ2 + "] -> " + resJ2.getNombreMano();

                    historial.agregar(detalleHistorial);

                    System.out.println("\nPresiona ENTER para regresar al menú...");
                    scanner.nextLine();
                    break;

                case 2:
                    limpiarConsola();
                    System.out.println("=== HISTORIAL DE PARTIDAS (ORDEN CRONOLÓGICO) ===");
                    historial.mostrarAdelante();
                    System.out.println("\nPresiona ENTER para regresar...");
                    scanner.nextLine();
                    break;

                case 3:
                    limpiarConsola();
                    System.out.println("=== HISTORIAL DE PARTIDAS (MÁS RECIENTES PRIMERO) ===");
                    historial.mostrarAtras();
                    System.out.println("\nPresiona ENTER para regresar...");
                    scanner.nextLine();
                    break;

                case 4:
                    salir = true;
                    System.out.println("Saliendo del juego...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    // Inicializa la baraja de 52 cartas y la revuelve
    private static Carta[] inicializarBaraja(Random random, String[] palos, String[] nombres) {
        Carta[] tempBaraja = new Carta[52];
        int cont = 0;

        for (String palo : palos) {
            for (String nombre : nombres) {
                int valorNumerico = 2;
                if (nombre.equalsIgnoreCase("As")) valorNumerico = 14;
                else if (nombre.equalsIgnoreCase("K")) valorNumerico = 13;
                else if (nombre.equalsIgnoreCase("Q")) valorNumerico = 12;
                else if (nombre.equalsIgnoreCase("J")) valorNumerico = 11;
                else {
                    // aqui estaba el try catch anterior que no servia de nada y fue remplazado por este
                    valorNumerico = Integer.parseInt(nombre);
                }

                Carta c = new Carta(nombre, palo, valorNumerico, false);
                c.setNombre(nombre);
                c.setPalo(palo);
                c.setValorNumerico(valorNumerico);

                tempBaraja[cont] = c;
                cont++;
            }
        }

        // Algoritmo para revolver el mazo
        for (int i = tempBaraja.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Carta a = tempBaraja[index];
            tempBaraja[index] = tempBaraja[i];
            tempBaraja[i] = a;
        }
        return tempBaraja;
    }

    // Salto de lineas para limpiar la pantalla de la consola
    private static void limpiarConsola() {
        for (int i = 0; i < 35; i++) System.out.println();
    }
}