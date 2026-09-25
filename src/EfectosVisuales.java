import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

import java.util.List;

/**
 * Efectos visuales reutilizables:
 *  - CRT: overlay tipo pantalla retro (scanlines + viñeta) para toda la pantalla.
 *  - Holográfico: brillo arcoíris animado tipo carta "foil" de Balatro, por carta.
 */
public class EfectosVisuales {

    // ==================== EFECTO CRT (global, por pantalla) ====================

    /** Aplica el efecto CRT a la raíz de una escena. Se llama una vez por Scene. */
    public static void aplicarCRT(Parent root) {
        if (!(root instanceof Pane)) return; // todas las "raiz"/"root" del proyecto son StackPane, que es Pane
        Pane panel = (Pane) root;

        // Ligero tinte y oscurecido general, como monitor viejo
        ColorAdjust tinte = new ColorAdjust();
        tinte.setSaturation(-0.12);
        tinte.setBrightness(-0.03);
        tinte.setContrast(0.08);
        root.setEffect(tinte);

        // Líneas de escaneo (scanlines) más gruesas y marcadas
        Region scanlines = new Region();
        scanlines.setMouseTransparent(true); // para no bloquear clics de botones/cartas
        scanlines.setStyle(
                "-fx-background-color: linear-gradient(from 0px 0px to 0px 3px, repeat, " +
                        "rgba(0,0,0,0.42) 0%, rgba(0,0,0,0.42) 50%, transparent 50%, transparent 100%);"
        );
        scanlines.prefWidthProperty().bind(panel.widthProperty());
        scanlines.prefHeightProperty().bind(panel.heightProperty());

        // Fleco de color en las orillas (imitación de aberración cromática de un tubo CRT)
        Region franjaRoja = new Region();
        franjaRoja.setMouseTransparent(true);
        franjaRoja.setBlendMode(javafx.scene.effect.BlendMode.SCREEN);
        franjaRoja.setStyle("-fx-background-color: linear-gradient(to right, rgba(255,0,60,0.18) 0%, transparent 6%, transparent 94%, rgba(255,0,60,0.18) 100%);");
        franjaRoja.prefWidthProperty().bind(panel.widthProperty());
        franjaRoja.prefHeightProperty().bind(panel.heightProperty());

        Region franjaCian = new Region();
        franjaCian.setMouseTransparent(true);
        franjaCian.setBlendMode(javafx.scene.effect.BlendMode.SCREEN);
        franjaCian.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,220,255,0.14) 0%, transparent 6%, transparent 94%, rgba(0,220,255,0.14) 100%);");
        franjaCian.prefWidthProperty().bind(panel.widthProperty());
        franjaCian.prefHeightProperty().bind(panel.heightProperty());

        // Viñeta oscura en las orillas, como un tubo CRT curvo
        Region vineta = new Region();
        vineta.setMouseTransparent(true);
        vineta.setStyle(
                "-fx-background-color: radial-gradient(center 50% 50%, radius 72%, transparent 45%, rgba(0,0,0,0.55) 100%);"
        );
        vineta.prefWidthProperty().bind(panel.widthProperty());
        vineta.prefHeightProperty().bind(panel.heightProperty());

        panel.getChildren().addAll(franjaRoja, franjaCian, vineta, scanlines);

        // Parpadeo sutil (flicker), como un monitor viejo con el brillo inestable
        Timeline parpadeo = new Timeline(
                new KeyFrame(Duration.seconds(0.0), e -> scanlines.setOpacity(1.0)),
                new KeyFrame(Duration.seconds(0.08), e -> scanlines.setOpacity(0.85)),
                new KeyFrame(Duration.seconds(0.16), e -> scanlines.setOpacity(1.0)),
                new KeyFrame(Duration.seconds(2.5), e -> scanlines.setOpacity(1.0)),
                new KeyFrame(Duration.seconds(2.58), e -> scanlines.setOpacity(0.7)),
                new KeyFrame(Duration.seconds(2.66), e -> scanlines.setOpacity(1.0))
        );
        parpadeo.setCycleCount(Animation.INDEFINITE);
        parpadeo.play();
    }

    // ==================== EFECTO HOLOGRÁFICO (por carta) ====================

    // 3 "cuadros" del gradiente arcoíris; van rotando en un Timeline para simular el brillo en movimiento
    @SuppressWarnings("unchecked")
    private static final List<Stop>[] CUADROS_ARCOIRIS = new List[]{
            List.of(
                    new Stop(0.00, Color.web("#ff5ecb", 0.45)),
                    new Stop(0.25, Color.web("#5ecbff", 0.45)),
                    new Stop(0.50, Color.web("#5eff9d", 0.45)),
                    new Stop(0.75, Color.web("#fff85e", 0.45)),
                    new Stop(1.00, Color.web("#ff5e5e", 0.45))
            ),
            List.of(
                    new Stop(0.00, Color.web("#5ecbff", 0.45)),
                    new Stop(0.25, Color.web("#5eff9d", 0.45)),
                    new Stop(0.50, Color.web("#fff85e", 0.45)),
                    new Stop(0.75, Color.web("#ff5e5e", 0.45)),
                    new Stop(1.00, Color.web("#ff5ecb", 0.45))
            ),
            List.of(
                    new Stop(0.00, Color.web("#5eff9d", 0.45)),
                    new Stop(0.25, Color.web("#fff85e", 0.45)),
                    new Stop(0.50, Color.web("#ff5e5e", 0.45)),
                    new Stop(0.75, Color.web("#ff5ecb", 0.45)),
                    new Stop(1.00, Color.web("#5ecbff", 0.45))
            )
    };

    /**
     * Aplica el brillo holográfico animado a una carta. ancho/alto deben ser
     * los mismos que fitWidth/fitHeight que le pusiste al ImageView.
     */
    public static void aplicarHolografico(ImageView vista, double ancho, double alto) {
        Blend blend = new Blend(BlendMode.COLOR_DODGE);
        blend.setTopInput(crearCapaArcoiris(ancho, alto, 0));
        vista.setEffect(blend);

        Timeline brilloAnimado = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> blend.setTopInput(crearCapaArcoiris(ancho, alto, 0))),
                new KeyFrame(Duration.seconds(1.2), e -> blend.setTopInput(crearCapaArcoiris(ancho, alto, 1))),
                new KeyFrame(Duration.seconds(2.4), e -> blend.setTopInput(crearCapaArcoiris(ancho, alto, 2))),
                new KeyFrame(Duration.seconds(3.6), e -> blend.setTopInput(crearCapaArcoiris(ancho, alto, 0)))
        );
        brilloAnimado.setCycleCount(Animation.INDEFINITE);
        brilloAnimado.play();
    }

    private static ColorInput crearCapaArcoiris(double ancho, double alto, int indiceCuadro) {
        LinearGradient gradiente = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REPEAT, CUADROS_ARCOIRIS[indiceCuadro]);
        return new ColorInput(0, 0, ancho, alto, gradiente);
    }
}