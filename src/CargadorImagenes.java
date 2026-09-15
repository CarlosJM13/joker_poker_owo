import javafx.scene.image.Image;
import java.io.InputStream;

public class CargadorImagenes {

    public static Image cargarCarta(Carta carta) {
        String nombreArchivo = generarNombreArchivo(carta);

        // Intenta cargar PNG dentro de la subcarpeta cartas
        InputStream is = CargadorImagenes.class.getResourceAsStream("/assets/cartas/" + nombreArchivo + ".png");

        if (is == null) {
            // Si no existe PNG, intenta JPG
            is = CargadorImagenes.class.getResourceAsStream("/assets/cartas/" + nombreArchivo + ".jpg");
        }

        if (is == null) {
            // Intento de seguridad para .jpeg (como tus 2P.jpeg, 10T.jpeg, etc.)
            is = CargadorImagenes.class.getResourceAsStream("/assets/cartas/" + nombreArchivo + ".jpeg");
        }

        if (is == null) {
            // Carga el dorso o imagen por defecto si la carta no se encuentra
            is = CargadorImagenes.class.getResourceAsStream("/assets/cartas/Cartas.png");
        }

        return new Image(is);
    }

    private static String generarNombreArchivo(Carta carta) {
        String valor = carta.getNombre();

        // Si el nombre ya incluye la letra del palo al final (ej: "2C", "10T", "AC"),
        // lo devolvemos directamente en mayúsculas para evitar duplicarlos.
        if (valor.length() >= 2) {
            char ultimaLetra = Character.toUpperCase(valor.charAt(valor.length() - 1));
            if (ultimaLetra == 'C' || ultimaLetra == 'D' || ultimaLetra == 'P' || ultimaLetra == 'T') {
                return valor.toUpperCase();
            }
        }

        if (valor.equalsIgnoreCase("As")) {
            valor = "A";
        }

        String palo = carta.getPalo() != null ? carta.getPalo().toUpperCase() : "";
        String letraPalo = "";

        if (palo.startsWith("T")) letraPalo = "T";
        else if (palo.startsWith("D")) letraPalo = "D";
        else if (palo.startsWith("C")) letraPalo = "C";
        else if (palo.startsWith("P")) letraPalo = "P";

        return (valor + letraPalo).toUpperCase();
    }

    public static Image cargarJoker(String tipo) {
        if (tipo.equalsIgnoreCase("rojo")) {
            return new Image(CargadorImagenes.class.getResourceAsStream("/assets/JokerCartasR.png"));
        } else if (tipo.equalsIgnoreCase("negro")) {
            return new Image(CargadorImagenes.class.getResourceAsStream("/assets/JokerCartasN.png"));
        } else {
            return new Image(CargadorImagenes.class.getResourceAsStream("/assets/JokerDebuff.png"));
        }
    }
}