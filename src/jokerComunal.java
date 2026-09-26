public class jokerComunal {

    private int puntos = 0;

    // Tope de puntos: con 4 puntos ya llegamos a repetir la mano 5 veces
    // así que no tiene caso seguir sumando después.
    private static final int PUNTOS_MAXIMOS = 4;

    public void sumarPunto() {
        if (puntos < PUNTOS_MAXIMOS) {
            puntos++;
        }
    }

    public int getPuntos() {
        return puntos;
    }


    public int getVecesRepeticion() {
        return puntos + 1;
    }

    public int aplicarMultiplicador(int puntaje, int multiplicador, int veces){
        // tope
        if (veces > 5){
            veces = 5;
        }
        //caso base
        if (veces == 0 ){
            return  puntaje;
        }
        //recursividad
        return  aplicarMultiplicador(puntaje * multiplicador, multiplicador, veces -1);
    }
}