import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GestorAudio {
    private static MediaPlayer mediaPlayer;

    public static void reproducirMusicaFondo(String rutaArchivo) {
        if (mediaPlayer != null) {
            return;
        }
        try {
            String ruta = GestorAudio.class.getResource(rutaArchivo).toExternalForm();
            Media media = new Media(ruta);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.4);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error al reproducir la música de fondo: " + e.getMessage());
        }
    }
}