public class jokerComunal {
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


