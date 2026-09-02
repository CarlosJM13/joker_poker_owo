import java.util.Scanner;
public class Validador {
    // el escaner de la libreria de arriba
    private static Scanner scanner = new Scanner(System.in);

    // clase para hacer los trycatch y evitar erore como poner texto en donde van numeros, un numero que no es del 1 al 5, etc.

    public static int pedirOpcion(String mensaje, int min, int max) {
        int opcion = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print(mensaje);
            String input = scanner.nextLine(); // escanear la siguiente linea

            try {
                opcion = Integer.parseInt(input); // esto hace que la opcion se convierta a numero

                //validamos el rango
                if (opcion >= min && opcion <= max) {
                    entradaValida = true;
                } else { //yo agregue la carita al final para que se vea mejor
                    System.out.println("\nError: ingrese un numero del " + min + " al " + max + " :)");
                }
            } catch (NumberFormatException e) {
                // en caso de usar letras o algun simbolo
                System.out.println("Error: ingrese un numero entero\n");
            }
        }
        // mientras la entrada sea valida regresa la opcion
        return opcion;
    }
}