/**
 * Estados de nivel superior únicamente. El detalle fino de la ronda
 * (escoger carta, descartes, mesa común, destape...) no vive aquí como
 * un estado por nodo -- con más de 12 micro-pasos el enum se vuelve
 * inmanejable. En vez de eso, Ronda.java encadena esos pasos con
 * callbacks (Runnable) en el orden exacto que diste:
 *
 *   J1 escoge -> ceder a J2 -> J2 escoge -> compu al centro ->
 *   mesa común (3) -> ceder a J1 -> J1 toma descarte de J2 ->
 *   ceder a J2 -> J2 toma descarte de J1 -> compu al centro ->
 *   destape -> mesa común (2) -> evaluación -> tienda/mejora -> repetir
 *
 * Este enum solo cubre las pantallas "de menú" que sí tienen sentido
 * como destino fijo de navegación.
 */
public enum EstadoJuego {
    MENU_PRINCIPAL,
    REGISTRO_JUGADOR_1,
    REGISTRO_JUGADOR_2,
    HISTORIAL_PARTIDAS,
    PARTIDA_EN_CURSO, // dispara Ronda.iniciar(gestor)
    GAME_OVER
}
