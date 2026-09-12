import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Cubre TODO el bloque de una ronda siguiendo tu orden exacto:
 *
 *  J1 escoge -> ceder a J2 -> J2 escoge -> compu al centro ->
 *  mesa común (3) -> ceder a J1 -> J1 toma descarte de J2 ->
 *  ceder a J2 -> J2 toma descarte de J1 -> compu al centro ->
 *  destape -> mesa común (2) -> evaluación
 *
 * Cada paso recibe un Runnable "continuar" que se ejecuta cuando el
 * jugador termina ese paso (equivalente a seguir la flecha del diagrama).
 */
public class Ronda {

    private static final MotorPuntaje motor = new MotorPuntaje();

    public static void iniciar(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();
        partida.iniciarNuevaRonda();

        if (partida.getEfectoRondaActual() != null) {
            mostrarAnuncioDebuff(gestor, () -> iniciarSecuenciaTurnos(gestor));
        } else {
            iniciarSecuenciaTurnos(gestor);
        }
    }

    private static void iniciarSecuenciaTurnos(GestorEscenas gestor) {
        pasoEscogerCartaPropia(gestor, 1, () ->
            PantallaTransicion.mostrar(gestor, "Jugador 2", () ->
                pasoEscogerCartaPropia(gestor, 2, () ->
                    PantallaTransicion.mostrar(gestor, "el centro (ambos jugadores)", () ->
                        pasoMesaComun(gestor, 3, () ->
                            PantallaTransicion.mostrar(gestor, "Jugador 1", () ->
                                pasoEscogerDeDescartes(gestor, 1, () ->
                                    PantallaTransicion.mostrar(gestor, "Jugador 2", () ->
                                        pasoEscogerDeDescartes(gestor, 2, () ->
                                            PantallaTransicion.mostrar(gestor, "el centro (ambos jugadores)", () ->
                                                pasoDestape(gestor, () ->
                                                    pasoMesaComun(gestor, 2, () ->
                                                        pasoEvaluacion(gestor)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    private static void mostrarAnuncioDebuff(GestorEscenas gestor, Runnable continuar) {
        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("¡Ronda especial! Efecto de esta ronda:"));
        raiz.getChildren().add(new Label(gestor.getPartida().getEfectoRondaActual()));

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setOnAction(e -> continuar.run());
        raiz.getChildren().add(btnContinuar);

        gestor.mostrar(new Scene(raiz, 700, 400));
    }

    /**
     * "Jugador N: Escoger una carta de su mazo principal (lista)".
     * Usa tu ListaSimple como el "catálogo" temporal de hasta 3 cartas
     * (por eso ListaSimple trae vaciar(), justo para esto: "vacía la
     * lista para la siguiente ronda").
     */
    private static void pasoEscogerCartaPropia(GestorEscenas gestor, int numJugador, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        List<Carta> mazo = numJugador == 1 ? partida.getMazoPrincipalJ1() : partida.getMazoPrincipalJ2();
        Pila descartesPropios = numJugador == 1 ? partida.getDescartesJ1() : partida.getDescartesJ2();

        ListaSimple opciones = new ListaSimple();
        int cantidadAMostrar = Math.min(3, mazo.size());
        for (int i = 0; i < cantidadAMostrar; i++) {
            // TODO: idealmente robar al azar del mazo, no siempre la primera carta.
            Carta carta = mazo.remove(0);
            opciones.agregarCarta(carta);
        }

        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Jugador " + numJugador + ": escoge una carta de tu mazo principal"));

        int totalOpciones = opciones.getTamaño();
        for (int i = 1; i <= totalOpciones; i++) {
            final int indiceElegido = i;
            Carta carta = opciones.obtenerCarta(i);

            Button btn = new Button(carta.toString());
            btn.setOnAction(e -> {
                jugador.getManoActual()[0] = carta; // primera de las 2 cartas de su mano final

                // Las que no se seleccionaron se van a su propia pila de descartes
                for (int j = 1; j <= totalOpciones; j++) {
                    if (j != indiceElegido) {
                        descartesPropios.apilar(opciones.obtenerCarta(j));
                    }
                }
                opciones.vaciar();
                continuar.run();
            });
            raiz.getChildren().add(btn);
        }

        gestor.mostrar(new Scene(raiz, 900, 550));
    }

    /** "Jugador N: Escoger una carta de entre los descartes del rival". */
    private static void pasoEscogerDeDescartes(GestorEscenas gestor, int numJugador, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        Pila descartesRival = numJugador == 1 ? partida.getDescartesJ2() : partida.getDescartesJ1();

        // Pila solo expone apilar/desapilar, así que sacamos todo para
        // mostrar las opciones; la que no se elija simplemente se descarta
        // (no se vuelve a guardar en ningún lado, tal como indica el diagrama).
        List<Carta> opciones = new ArrayList<>();
        while (!descartesRival.estaVacia()) {
            opciones.add(descartesRival.desapilar());
        }

        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Jugador " + numJugador + ": escoge entre los descartes del rival"));

        for (Carta carta : opciones) {
            Button btn = new Button(carta.toString());
            btn.setOnAction(e -> {
                jugador.getManoActual()[1] = carta; // segunda carta de su mano final
                continuar.run(); // el resto de opciones simplemente se descartan
            });
            raiz.getChildren().add(btn);
        }

        gestor.mostrar(new Scene(raiz, 900, 550));
    }

    /** "Se ponen N cartas sobre la mesa común, visibles para ambos jugadores". */
    private static void pasoMesaComun(GestorEscenas gestor, int cantidad, Runnable continuar) {
        Partida partida = gestor.getPartida();

        // TODO: sacar `cantidad` cartas de la baraja común real y agregarlas
        // a partida.getMesaComun(). No me compartiste esa clase de "baraja
        // completa", así que dejo el punto de enganche.
        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Se ponen " + cantidad + " cartas sobre la mesa común"));
        for (Carta c : partida.getMesaComun()) {
            raiz.getChildren().add(new Label(c.toString()));
        }

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setOnAction(e -> continuar.run());
        raiz.getChildren().add(btnContinuar);

        gestor.mostrar(new Scene(raiz, 900, 550));
    }

    /** "Se voltean las cartas de los jugadores, abajo J1 y arriba J2". */
    private static void pasoDestape(GestorEscenas gestor, Runnable continuar) {
        Partida partida = gestor.getPartida();

        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Se destapan las manos de ambos jugadores"));
        raiz.getChildren().add(new Label("Jugador 1: " + manoComoTexto(partida.getJugador1())));
        raiz.getChildren().add(new Label("Jugador 2: " + manoComoTexto(partida.getJugador2())));

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setOnAction(e -> continuar.run());
        raiz.getChildren().add(btnContinuar);

        gestor.mostrar(new Scene(raiz, 900, 550));
    }

    private static String manoComoTexto(Jugador jugador) {
        Carta[] mano = jugador.getManoActual();
        return (mano[0] != null ? mano[0].toString() : "?") + " + " + (mano[1] != null ? mano[1].toString() : "?");
    }

    /**
     * "Se suman los puntos de cada mano, se juega según la cantidad de
     * puntos de recursividad del joker comunal, y se multiplica para
     * obtener el resultado final."
     */
    private static void pasoEvaluacion(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();

        Carta[] centro = partida.getMesaComun().toArray(new Carta[0]); // debe traer 5 cartas
        int nivelRecursividad = partida.nivelRecursividadJokerComunal();

        // El debuff "El Joker Comunal no aplica esta ronda" se resuelve aquí
        // mismo sin tocar MotorPuntaje: forzamos nivel 1 (el cálculo normal,
        // sin la multiplicación extra de la recursividad).
        if (Partida.DEBUFF_JOKER_COMUNAL_NULO.equals(partida.getEfectoRondaActual())) {
            nivelRecursividad = 1;
        }

        // TODO: los otros 3 debuffs (DEBUFF_COLOR_MITAD, DEBUFF_FULLHOUSE_MITAD,
        // DEBUFF_PAR_INHABILITADO) sí dependen de qué mano de póker salió, así
        // que necesitan que MotorPuntaje.calcularJugadaFinal reciba el efecto
        // activo como parámetro extra. Ver el parche sugerido en el README.
        Joker[] jokersJ1 = partida.getJokersJ1().toArray(new Joker[0]);
        Joker[] jokersJ2 = partida.getJokersJ2().toArray(new Joker[0]);

        int puntajeJ1 = motor.calcularJugadaFinal(centro, partida.getJugador1().getManoActual(), jokersJ1, nivelRecursividad);
        int puntajeJ2 = motor.calcularJugadaFinal(centro, partida.getJugador2().getManoActual(), jokersJ2, nivelRecursividad);

        boolean j1Supero = puntajeJ1 >= partida.getMetaFichas();
        boolean j2Supero = puntajeJ2 >= partida.getMetaFichas();

        // Tu ListaDoble.agregar(...) guarda AMBOS jugadores en un solo nodo por ronda.
        partida.getHistorialRondas().agregar(
                partida.getJugador1().getNombre(), "Puntaje: " + puntajeJ1, manoComoTexto(partida.getJugador1()), j1Supero,
                partida.getJugador2().getNombre(), "Puntaje: " + puntajeJ2, manoComoTexto(partida.getJugador2()), j2Supero,
                partida.getMetaFichas());

        VBox raiz = new VBox(12);
        raiz.setAlignment(Pos.CENTER);
        raiz.getChildren().add(new Label("Meta: " + partida.getMetaFichas()));
        raiz.getChildren().add(new Label("Jugador 1: " + puntajeJ1 + " puntos"));
        raiz.getChildren().add(new Label("Jugador 2: " + puntajeJ2 + " puntos"));

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setOnAction(e -> {
            if (partida.algunJugadorPerdio(puntajeJ1, puntajeJ2)) {
                gestor.irA(EstadoJuego.GAME_OVER);
                return;
            }
            // "Se le otorga cierta cantidad de monedas de oro a cada jugador,
            // siempre la misma cantidad para ambos" -> tienda J1 -> mejora J1
            // -> ceder a J2 -> tienda J2 -> mejora J2 -> nueva ronda
            partida.otorgarMonedasIguales(5); // TODO: ajustar la cantidad real de monedas por ronda
            EscenaTienda.mostrar(gestor, 1, () ->
                PantallaMejora.mostrar(gestor, 1, () ->
                    PantallaTransicion.mostrar(gestor, "Jugador 2", () ->
                        EscenaTienda.mostrar(gestor, 2, () ->
                            PantallaMejora.mostrar(gestor, 2, () ->
                                Ronda.iniciar(gestor)
                            )
                        )
                    )
                )
            );
        });
        raiz.getChildren().add(btnContinuar);

        gestor.mostrar(new Scene(raiz, 900, 550));
    }
}
