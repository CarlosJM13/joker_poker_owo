import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.text.FontPosture;

public class PantallaMejora {
    private StackPane root;
    private ArbolMejoras arbolMejoras;
    private Carta[] manoJugador;
    private Runnable onMejoraCompleta;
    private Label lblContador; // Para mostrar cuántas mejoras llevas aplicadas

    // Esta compra de $2 (comodín 4 / "Evolucionar cartas") da 2 usos, y cada
    // uso se asigna a UNA carta con SU propio camino: por ejemplo, 1 carta
    // para mejora azul y otra distinta para mejora roja, en vez de forzar
    // el mismo camino a las 2 cartas como antes.
    private static final int USOS_TOTALES = 3;
    private int usosRestantes = USOS_TOTALES;

    private Carta cartaSeleccionada; // la carta resaltada en este momento (o null)
    private final java.util.Map<Carta, VBox> cajasPorCarta = new java.util.HashMap<>();

    public PantallaMejora(ArbolMejoras arbolMejoras, Carta[] manoJugador, Runnable onMejoraCompleta) {
        this.arbolMejoras = arbolMejoras;
        this.manoJugador = manoJugador;
        this.onMejoraCompleta = onMejoraCompleta;
        crearUI();
    }

    public static void mostrar(GestorEscenas gestor, int numJugador, Runnable onMejoraCompleta) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        Carta[] mano = jugador.getManoActual();
        PantallaMejora pantalla = new PantallaMejora(partida.getArbolMejoras(), mano, onMejoraCompleta);
        gestor.mostrar(new Scene(pantalla.getRoot(), 1024, 768));
    }

    private void aplicarMejora(boolean esIzquierda) {
        if (usosRestantes <= 0) {
            return; // Ya no quedan usos, los botones deberían estar deshabilitados
        }

        if (cartaSeleccionada == null) {
            lblContador.setText("¡Selecciona primero una carta!");
            lblContador.setTextFill(Color.web("#a42a2a")); // Rojo error
            return;
        }

        Carta carta = cartaSeleccionada;
        if (carta.nivelActual == null) {
            carta.nivelActual = arbolMejoras.raiz;
        }
        NodoArbol siguienteNodo = esIzquierda ? carta.nivelActual.izquierdo : carta.nivelActual.derecho;

        if (siguienteNodo != null) {
            carta.setNivelEvolucion(siguienteNodo);
            System.out.println(carta.getNombre() + " subió a: " + siguienteNodo.nivelEvolucion + " (" + (esIzquierda ? "mejora azul" : "mejora roja") + ")");
        } else {
            System.out.println(carta.getNombre() + " ya está en el nivel máximo.");
        }

        // Deja la carta marcada como "ya usada" en esta visita y libera la selección
        VBox caja = cajasPorCarta.get(carta);
        if (caja != null) {
            caja.setStyle("-fx-border-color: " + (esIzquierda ? "#1e4c7a" : "#7a1e1e") + "; -fx-border-width: 3; -fx-border-radius: 5; -fx-padding: 5; -fx-opacity: 0.5;");
            caja.setEffect(null);
            caja.setDisable(true);
        }
        cartaSeleccionada = null;
        usosRestantes--;

        if (usosRestantes <= 0) {
            lblContador.setText("¡Mejoras completas!");
            lblContador.setTextFill(Color.web("#2d6a4f"));
            finalizar();
        } else {
            lblContador.setText("Mejoras aplicadas: " + (USOS_TOTALES - usosRestantes) + " / " + USOS_TOTALES + " (elige otra carta, por ejemplo para la otra rama)");
            lblContador.setTextFill(Color.web("#5c3a18"));
        }
    }

    private void finalizar() {
        if (onMejoraCompleta != null) {
            onMejoraCompleta.run();
        }
    }

    private void crearUI() {
        root = new StackPane();

        // 1. Fondo de Mesa de Madera con sombra interna oscura
        try {
            Image imagenMesa = new Image(PantallaMejora.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            BackgroundImage bgMesa = new BackgroundImage(
                    imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true)
            );
            root.setBackground(new Background(bgMesa));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a1a1a;");
        }

        // 2. Contenedor del Pergamino con Sombra 3D
        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(880);
        panelPergamino.setMaxHeight(650);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 30;"
        );

        // ✨ APLICAR CURVATURA AL PERGAMINO (sombra muy pronunciada + escala 3D)
        EfectosVisuales.aplicarCurvatura(panelPergamino);

        // ✨ APLICAR CLARIDAD SUPER FUERTE AL FONDO (mesa)
        EfectosVisuales.aplicarClaridad(root);

        // Título con sombra
        Text titulo = new Text("EVOLUCIÓN DE CARTAS");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 38));
        titulo.setFill(Color.web("#3b220b"));
        DropShadow sombraTexto = new DropShadow(3, 2, 2, Color.web("#ffffff"));
        titulo.setEffect(sombraTexto);

        lblContador = new Label("Elige una carta y luego una rama (azul o roja). Tienes " + USOS_TOTALES + " mejoras disponibles.");
        lblContador.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblContador.setTextFill(Color.web("#5c3a18"));
        lblContador.setWrapText(true);
        lblContador.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // --- ZONA DE SELECCIÓN DE CARTAS CON ANIMACIONES ---
        HBox zonaCartas = new HBox(40);
        zonaCartas.setAlignment(Pos.CENTER);
        zonaCartas.setStyle("-fx-padding: 10;");

        for (Carta c : manoJugador) {
            if (c != null) {
                VBox cajaCarta = new VBox(5);
                cajaCarta.setAlignment(Pos.CENTER);

                ImageView vistaCarta = new ImageView();
                try {
                    vistaCarta.setImage(CargadorImagenes.cargarCarta(c));
                } catch (Exception ex) {}
                vistaCarta.setFitWidth(120);
                vistaCarta.setFitHeight(170);
                vistaCarta.setPreserveRatio(true);

                // Sombra base para las cartas
                DropShadow sombraCarta = new DropShadow(10, 5, 5, Color.color(0,0,0,0.5));
                cajaCarta.setEffect(sombraCarta);
                cajaCarta.setStyle("-fx-border-color: transparent; -fx-border-width: 3; -fx-padding: 5; -fx-cursor: hand;");

                // Animación de escala preparada
                ScaleTransition st = new ScaleTransition(Duration.millis(150), cajaCarta);

                cajaCarta.setOnMouseEntered(e -> {
                    if (cartaSeleccionada != c) {
                        st.setToX(1.05); st.setToY(1.05); st.play();
                    }
                });
                cajaCarta.setOnMouseExited(e -> {
                    if (cartaSeleccionada != c) {
                        st.setToX(1.0); st.setToY(1.0); st.play();
                    }
                });

                cajaCarta.setOnMouseClicked(e -> {
                    if (cajaCarta.isDisable()) return; // ya se usó en esta visita

                    if (cartaSeleccionada == c) {
                        // Vuelve a hacer click en la misma carta: la deselecciona
                        cartaSeleccionada = null;
                        cajaCarta.setStyle("-fx-border-color: transparent; -fx-border-width: 3; -fx-padding: 5; -fx-cursor: hand;");
                        cajaCarta.setEffect(sombraCarta);
                        st.setToX(1.0); st.setToY(1.0); st.play();
                    } else {
                        // Quita el resaltado de la carta que estuviera seleccionada antes
                        if (cartaSeleccionada != null) {
                            VBox cajaAnterior = cajasPorCarta.get(cartaSeleccionada);
                            if (cajaAnterior != null) {
                                cajaAnterior.setStyle("-fx-border-color: transparent; -fx-border-width: 3; -fx-padding: 5; -fx-cursor: hand;");
                                cajaAnterior.setEffect(sombraCarta);
                            }
                        }
                        cartaSeleccionada = c;
                        // Borde brillante y crece cuando se selecciona
                        cajaCarta.setStyle("-fx-border-color: #FFD700; -fx-border-width: 3; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand; -fx-background-color: rgba(255, 215, 0, 0.15);");
                        DropShadow glow = new DropShadow(20, Color.web("#FFD700"));
                        cajaCarta.setEffect(glow);
                        st.setToX(1.1); st.setToY(1.1); st.play();
                    }
                });

                cajasPorCarta.put(c, cajaCarta);
                cajaCarta.getChildren().add(vistaCarta);
                zonaCartas.getChildren().add(cajaCarta);
            }
        }

        VBox centro = crearArbol();

        Button btnTerminar = new Button("Terminar mejoras");
        btnTerminar.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnTerminar.setStyle("-fx-background-color: linear-gradient(#5c3a18, #2b1d0c); -fx-text-fill: #FFD700; -fx-padding: 8 20; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        btnTerminar.setOnAction(e -> finalizar());

        panelPergamino.getChildren().addAll(titulo, lblContador, zonaCartas, centro, btnTerminar);
        root.getChildren().add(panelPergamino);
    }

    private VBox crearArbol() {
        VBox arbol = new VBox(20);
        arbol.setAlignment(Pos.TOP_CENTER);
        arbol.setStyle("-fx-padding: 10;");

        // Nodo Base usando un diseño más estilizado
        VBox raiz = new VBox(5);
        raiz.setAlignment(Pos.CENTER);
        raiz.setStyle("-fx-padding: 10 30; -fx-background-color: rgba(255, 215, 0, 0.2); -fx-border-color: #5c3a18; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        Text txtRaizTit = new Text("ESTADO ACTUAL");
        txtRaizTit.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        txtRaizTit.setFill(Color.web("#3b220b"));
        Text txtRaizInfo = new Text("Selecciona un camino evolutivo abajo");
        txtRaizInfo.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
        txtRaizInfo.setFill(Color.web("#5c3a18"));
        raiz.getChildren().addAll(txtRaizTit, txtRaizInfo);

        HBox ramaIzq = new HBox(20);
        ramaIzq.setAlignment(Pos.CENTER);
        // Usamos la textura de tus botones con colores adaptados
        HBox nodoIzq = crearBotonMejora("✦ RAMA AZUL ✦\n+5 Fichas Base\n(a la carta seleccionada)", "#1e4c7a");
        nodoIzq.setOnMouseClicked(e -> aplicarMejora(true));

        HBox ramaDer = new HBox(20);
        ramaDer.setAlignment(Pos.CENTER);
        HBox nodoDer = crearBotonMejora("✦ RAMA ROJA ✦\nx1.5 Multiplicador\n(a la carta seleccionada)", "#7a1e1e");
        nodoDer.setOnMouseClicked(e -> aplicarMejora(false));

        HBox ramas = new HBox(40);
        ramas.setAlignment(Pos.CENTER);

        VBox izq = new VBox(5);
        izq.setAlignment(Pos.CENTER);
        izq.getChildren().addAll(crearFlecha("#1e4c7a"), nodoIzq);

        VBox der = new VBox(5);
        der.setAlignment(Pos.CENTER);
        der.getChildren().addAll(crearFlecha("#7a1e1e"), nodoDer);

        ramas.getChildren().addAll(izq, der);
        arbol.getChildren().addAll(raiz, ramas);
        return arbol;
    }

    private HBox crearBotonMejora(String titulo, String colorFondoHex) {
        HBox boton = new HBox();
        boton.setAlignment(Pos.CENTER);
        // Usamos boton_borde.png de fondo, pero le ponemos un color de fondo semitransparente para diferenciar azul/rojo
        boton.setStyle(
                "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                        "-fx-background-size: stretch;" +
                        "-fx-background-color: " + colorFondoHex + "cc;" + // El 'cc' al final le da 80% de opacidad
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #2c1808; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;"
        );
        boton.setPrefWidth(240);
        boton.setPrefHeight(75);

        Text texto = new Text(titulo);
        texto.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        texto.setFill(Color.WHITE);
        texto.setWrappingWidth(220);
        texto.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        DropShadow shadowText = new DropShadow(2, Color.BLACK);
        texto.setEffect(shadowText);

        boton.getChildren().add(texto);

        ScaleTransition st = new ScaleTransition(Duration.millis(150), boton);
        boton.setOnMouseEntered(e -> {
            st.setToX(1.05); st.setToY(1.05); st.play();
            boton.setStyle(boton.getStyle().replace(colorFondoHex + "cc", colorFondoHex + "ff")); // Se hace más sólido
        });
        boton.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.play();
            boton.setStyle(boton.getStyle().replace(colorFondoHex + "ff", colorFondoHex + "cc")); // Vuelve a transparente
        });

        return boton;
    }

    private Text crearFlecha(String colorHex) {
        Text flecha = new Text("▼");
        flecha.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        flecha.setFill(Color.web(colorHex));
        DropShadow ds = new DropShadow(2, Color.web("#3b220b"));
        flecha.setEffect(ds);
        return flecha;
    }

    public StackPane getRoot() {
        return root;
    }
}