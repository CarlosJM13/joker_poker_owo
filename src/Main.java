import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Creamos las estructuras para los turnos, pilas de descarte y el historial
        Cola turnos = new Cola();
        Pila pilaJugador1 = new Pila();
        Pila pilaJugador2 = new Pila();
        ListaCircular debuffos = new ListaCircular();
        ListaDoble historial = new ListaDoble();
        Revisar motorPoker = new Revisar();

        // Efectos que cambian los puntos de las manos cada ronda
        debuffos.agregar("El Full House vale la mitad de puntos");
        debuffos.agregar("Las Escaleras dan el doble de puntos");
        debuffos.agregar("Los Pares valen la mitad de puntos");
        debuffos.agregar("Sin debuffo esta ronda (Puntaje normal)");

        String jugador1 = "";
        String jugador2 = "";
        boolean jugadoresRegistrados = false;
        boolean salir = false;

        String[] palos = {"Picas", "Corazones", "Diamantes", "Treboles"};
        String[] nombresCartas = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "As"};

        while (!salir) {
            limpiarConsola();
            System.out.println("========================================");
            System.out.println("     MENÚ PRINCIPAL - JOKER POKER       ");
            System.out.println("========================================");
            System.out.println("1. Jugar una nueva partida");
            System.out.println("2. Ver historial de partidas (Lista Doble)");
            System.out.println("3. Salir del programa");
            System.out.print("Elige una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    // Pedimos los nombres de los jugadores la primera vez
                    if (!jugadoresRegistrados) {
                        limpiarConsola();
                        System.out.print("Ingresa el nombre del Jugador 1: ");
                        jugador1 = scanner.nextLine();
                        System.out.print("Ingresa el nombre del Jugador 2: ");
                        jugador2 = scanner.nextLine();
                        jugadoresRegistrados = true;
                        historial.agregar("Se registraron los jugadores: " + jugador1 + " y " + jugador2);
                    }

                    limpiarConsola();
                    System.out.println("=== ¡INICIANDO PARTIDA TIPO BALATRO! ===");

                    // Baraja de 52 cartas revuelta para que no salgan repetidas
                    List<Carta> barajaActual = inicializarBaraja(random, palos, nombresCartas);

                    turnos.encolar(jugador1);
                    turnos.encolar(jugador2);

                    // Repartimos 3 cartas iniciales para cada uno desde el mazo
                    Carta m1_c1 = barajaActual.remove(0);
                    Carta m1_c2 = barajaActual.remove(0);
                    Carta m1_c3 = barajaActual.remove(0);

                    Carta m2_c1 = barajaActual.remove(0);
                    Carta m2_c2 = barajaActual.remove(0);
                    Carta m2_c3 = barajaActual.remove(0);

                    // --- Turno Jugador 1: Elige de su mazo principal ---
                    limpiarConsola();
                    String turnoActual = turnos.desencolar();
                    System.out.println("--- Turno de " + turnoActual + " (Mazo Principal) ---");
                    System.out.println("1. " + m1_c1);
                    System.out.println("2. " + m1_c2);
                    System.out.println("3. " + m1_c3);
                    System.out.print("Elige una carta (1, 2 o 3): ");
                    int opJ1 = scanner.nextInt();
                    scanner.nextLine();

                    Carta cartaElegidaJ1 = (opJ1 == 1) ? m1_c1 : (opJ1 == 2 ? m1_c2 : m1_c3);

                    // Las cartas que no eligió se van a la pila del rival
                    if (opJ1 != 1) pilaJugador2.apilar(m1_c1);
                    if (opJ1 != 2) pilaJugador2.apilar(m1_c2);
                    if (opJ1 != 3) pilaJugador2.apilar(m1_c3);

                    turnos.encolar(turnoActual);

                    // --- Turno Jugador 2: Elige de su mazo principal ---
                    limpiarConsola();
                    turnoActual = turnos.desencolar();
                    System.out.println("--- Turno de " + turnoActual + " (Mazo Principal) ---");
                    System.out.println("1. " + m2_c1);
                    System.out.println("2. " + m2_c2);
                    System.out.println("3. " + m2_c3);
                    System.out.print("Elige una carta (1, 2 o 3): ");
                    int opJ2 = scanner.nextInt();
                    scanner.nextLine();

                    Carta cartaElegidaJ2 = (opJ2 == 1) ? m2_c1 : (opJ2 == 2 ? m2_c2 : m2_c3);

                    // Las que no eligió se van a la pila del Jugador 1
                    if (opJ2 != 1) pilaJugador1.apilar(m2_c1);
                    if (opJ2 != 2) pilaJugador1.apilar(m2_c2);
                    if (opJ2 != 3) pilaJugador1.apilar(m2_c3);

                    turnos.encolar(turnoActual);

                    // --- Anunciamos el efecto y sacamos las primeras 3 cartas comunitarias ---
                    limpiarConsola();
                    String efectoRonda = debuffos.avanzarEfecto();
                    System.out.println("[LISTA CIRCULAR] Efecto de mano para esta ronda: " + efectoRonda);

                    System.out.println("\n[MESA COMÚN] Primeras 3 cartas comunitarias:");
                    Carta[] centro = new Carta[5];
                    centro[0] = barajaActual.remove(0);
                    centro[1] = barajaActual.remove(0);
                    centro[2] = barajaActual.remove(0);

                    mostrarMesaParcial(centro, 3);

                    // --- Turno J1: Escoge entre descartes del rival o una carta random del mazo ---
                    System.out.println("\nPresiona ENTER para continuar con el turno de " + jugador1 + "...");
                    scanner.nextLine();
                    limpiarConsola();

                    turnoActual = turnos.desencolar();
                    System.out.println("--- Turno de " + turnoActual + " (Descartes + Extra Random) ---");
                    mostrarMesaParcial(centro, 3);

                    Carta d1_j2 = pilaJugador1.desapilar();
                    Carta d2_j2 = pilaJugador1.desapilar();
                    Carta cartaRandomJ1 = barajaActual.remove(0); // Sacamos una carta limpia del mazo

                    System.out.println("\nOpciones disponibles:");
                    System.out.println("1. " + d1_j2 + " (Reciclada del rival)");
                    System.out.println("2. " + d2_j2 + " (Reciclada del rival)");
                    System.out.println("3. " + cartaRandomJ1 + " (Carta Extra de la Baraja)");
                    System.out.print("Elige tu carta definitiva (1, 2 o 3): ");
                    int selDescJ1 = scanner.nextInt();
                    scanner.nextLine();

                    Carta cartaDescarteJ1 = (selDescJ1 == 1) ? d1_j2 : (selDescJ1 == 2 ? d2_j2 : cartaRandomJ1);
                    turnos.encolar(turnoActual);

                    // --- Turno J2: Escoge entre descartes del rival o una carta random del mazo ---
                    System.out.println("\nPresiona ENTER para continuar con el turno de " + jugador2 + "...");
                    scanner.nextLine();
                    limpiarConsola();

                    turnoActual = turnos.desencolar();
                    System.out.println("--- Turno de " + turnoActual + " (Descartes + Extra Random) ---");
                    mostrarMesaParcial(centro, 3);

                    Carta d1_j1 = pilaJugador2.desapilar();
                    Carta d2_j1 = pilaJugador2.desapilar();
                    Carta cartaRandomJ2 = barajaActual.remove(0); // Sacamos otra carta limpia

                    System.out.println("\nOpciones disponibles:");
                    System.out.println("1. " + d1_j1 + " (Reciclada del rival)");
                    System.out.println("2. " + d2_j1 + " (Reciclada del rival)");
                    System.out.println("3. " + cartaRandomJ2 + " (Carta Extra de la Baraja)");
                    System.out.print("Elige tu carta definitiva (1, 2 o 3): ");
                    int selDescJ2 = scanner.nextInt();
                    scanner.nextLine();

                    Carta cartaDescarteJ2 = (selDescJ2 == 1) ? d1_j1 : (selDescJ2 == 2 ? d2_j1 : cartaRandomJ2);
                    turnos.encolar(turnoActual);

                    // --- Destapamos las últimas 2 cartas comunitarias para completar las 5 ---
                    limpiarConsola();
                    System.out.println("[MESA COMÚN] Se revelan las 5 cartas comunitarias completas:");
                    centro[3] = barajaActual.remove(0);
                    centro[4] = barajaActual.remove(0);
                    mostrarMesaParcial(centro, 5);

                    Carta[] manoJugador1 = new Carta[] { cartaElegidaJ1, cartaDescarteJ1 };
                    Carta[] manoJugador2 = new Carta[] { cartaElegidaJ2, cartaDescarteJ2 };

                    Carta[][] todosLosJugadores = new Carta[][] { manoJugador1, manoJugador2 };

                    int indiceGanador = motorPoker.determinarGanador(centro, todosLosJugadores);
                    String ganadorPartida = (indiceGanador == 0) ? jugador1 : jugador2;

                    Resultado resJ1 = motorPoker.evaluar(centro, manoJugador1);
                    Resultado resJ2 = motorPoker.evaluar(centro, manoJugador2);

                    // Preparamos las cartas específicas que eligió el ganador para guardarlas
                    Carta cartaElegidaGanador1 = (indiceGanador == 0) ? cartaElegidaJ1 : cartaElegidaJ2;
                    Carta cartaElegidaGanador2 = (indiceGanador == 0) ? cartaDescarteJ1 : cartaDescarteJ2;
                    String manoGanadoraNombre = (indiceGanador == 0) ? resJ1.getNombreMano() : resJ2.getNombreMano();

                    // Guardamos en el historial solo las 5 de la mesa y las 2 del ganador
                    String detalleHistorial = "Ganador: " + ganadorPartida +
                            " | Mano: " + manoGanadoraNombre +
                            " | Sus cartas: [" + cartaElegidaGanador1 + ", " + cartaElegidaGanador2 + "]" +
                            " | Comunitarias: [" + centro[0] + ", " + centro[1] + ", " + centro[2] + ", " + centro[3] + ", " + centro[4] + "]";

                    historial.agregar(detalleHistorial);

                    System.out.println("\n========================================");
                    System.out.println("       RESULTADOS FINALES DE LA RONDA    ");
                    System.out.println("========================================");
                    System.out.println("Efecto aplicado: " + efectoRonda);
                    System.out.println("----------------------------------------");
                    System.out.println(jugador1 + " escogió: [" + cartaElegidaJ1 + "] y [" + cartaDescarteJ1 + "]");
                    System.out.println(jugador1 + " armó: " + resJ1.getNombreMano());
                    System.out.println("----------------------------------------");
                    System.out.println(jugador2 + " escogió: [" + cartaElegidaJ2 + "] y [" + cartaDescarteJ2 + "]");
                    System.out.println(jugador2 + " armó: " + resJ2.getNombreMano());
                    System.out.println("----------------------------------------");
                    System.out.println(">>> ¡EL GANADOR ES: " + ganadorPartida.toUpperCase() + " ! <<<");
                    System.out.println("========================================");

                    System.out.println("\nPresiona ENTER para regresar al Menú Principal...");
                    scanner.nextLine();
                    break;

                case 2:
                    limpiarConsola();
                    System.out.println("--- HISTORIAL DE PARTIDAS (Lista Doble Adelante) ---");
                    historial.mostrarAdelante();
                    System.out.println("\n--- HISTORIAL EN REVERSA (Lista Doble Atrás) ---");
                    historial.mostrarAtras();
                    System.out.println("\nPresiona ENTER para regresar...");
                    scanner.nextLine();
                    break;

                case 3:
                    salir = true;
                    System.out.println("Saliendo del programa. ¡Mucho éxito con el proyecto!");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }

    // Armamos la baraja de 52 cartas y la revolvemos para que no se repitan
    private static List<Carta> inicializarBaraja(Random random, String[] palos, String[] nombres) {
        List<Carta> baraja = new ArrayList<>();
        for (String palo : palos) {
            for (String nombre : nombres) {
                int valorNumerico = 2;
                if (nombre.equalsIgnoreCase("As")) {
                    valorNumerico = 11;
                } else if (nombre.equalsIgnoreCase("K") || nombre.equalsIgnoreCase("Q") || nombre.equalsIgnoreCase("J")) {
                    valorNumerico = 10;
                } else {
                    try {
                        valorNumerico = Integer.parseInt(nombre);
                    } catch (NumberFormatException e) {
                        valorNumerico = 2;
                    }
                }
                baraja.add(new Carta(nombre, palo, valorNumerico, false));
            }
        }
        Collections.shuffle(baraja, random);
        return baraja;
    }

    // Ponemos muchos saltos de línea para limpiar la pantalla y que no se vea el turno pasado
    private static void limpiarConsola() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    // Imprimimos las cartas que van saliendo en la mesa para tenerlas a la vista
    private static void mostrarMesaParcial(Carta[] centro, int cantidad) {
        System.out.println("----------------------------------------");
        for (int i = 0; i < cantidad; i++) {
            System.out.println("  [Mesa] Carta " + (i + 1) + ": " + centro[i]);
        }
        System.out.println("----------------------------------------");
    }
}