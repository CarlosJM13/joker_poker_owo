import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Ronda {

    private static final MotorPuntaje motor = new MotorPuntaje();

    public static void iniciar(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();
        partida.iniciarNuevaRonda();
        iniciarSecuenciaTurnos(gestor);
    }

    public static void iniciarSecuenciaTurnos(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();
        String efecto = partida.getEfectoRondaActual();
        if (efecto != null) {
            mostrarAnuncioDebuff(gestor, efecto, () -> ejecutarPaso1(gestor));
        } else {
            ejecutarPaso1(gestor);
        }
    }

    private static void ejecutarPaso1(GestorEscenas gestor) {
        PantallaTransicion.mostrar(gestor, "Turno del Jugador 1: Escoger carta", () -> {
            pasoEscogerCartaPropia(gestor, 1, () -> ejecutarPaso2(gestor));
        });
    }

    private static void ejecutarPaso2(GestorEscenas gestor) {
        PantallaTransicion.mostrar(gestor, "Cede la computadora al Jugador 2", () -> {
            pasoEscogerCartaPropia(gestor, 2, () -> ejecutarPaso3(gestor));
        });
    }

    private static void ejecutarPaso3(GestorEscenas gestor) {
        pasoMesaComun(gestor, 3, () -> ejecutarPaso4(gestor));
    }

    private static void ejecutarPaso4(GestorEscenas gestor) {
        PantallaTransicion.mostrar(gestor, "Turno del Jugador 1: Descartes", () -> {
            pasoEscogerDeDescartes(gestor, 1, () -> ejecutarPaso5(gestor));
        });
    }

    private static void ejecutarPaso5(GestorEscenas gestor) {
        PantallaTransicion.mostrar(gestor, "Turno del Jugador 2: Descartes", () -> {
            pasoEscogerDeDescartes(gestor, 2, () -> ejecutarPaso6(gestor));
        });
    }

    private static void ejecutarPaso6(GestorEscenas gestor) {
        pasoMesaComun(gestor, 2, () -> ejecutarPaso7(gestor));
    }

    private static void ejecutarPaso7(GestorEscenas gestor) {
        pasoDestape(gestor, () -> pasoEvaluacion(gestor));
    }

    // --- MÉTODOS VISUALES REUTILIZABLES ---
    private static Button crearBotonPremium(String texto) {
        Button boton = new Button(texto);
        boton.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        String estiloBase = "-fx-background-color: linear-gradient(#5c3a18, #2b1d0c); -fx-text-fill: #FFD700; -fx-padding: 10 25; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: linear-gradient(#8b4513, #5c3a18); -fx-text-fill: #FFFFFF; -fx-padding: 10 25; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";

        boton.setStyle(estiloBase);
        boton.setEffect(new DropShadow(5, Color.BLACK));

        ScaleTransition st = new ScaleTransition(Duration.millis(150), boton);
        boton.setOnMouseEntered(e -> { st.setToX(1.05); st.setToY(1.05); st.play(); boton.setStyle(estiloHover); });
        boton.setOnMouseExited(e -> { st.setToX(1.0); st.setToY(1.0); st.play(); boton.setStyle(estiloBase); });
        return boton;
    }

    private static void aplicarEfectoCarta(ImageView vistaCarta) {
        DropShadow sombraNormal = new DropShadow(10, 5, 5, Color.color(0,0,0,0.6));
        DropShadow sombraHover = new DropShadow(20, Color.web("#FFD700"));
        vistaCarta.setEffect(sombraNormal);

        ScaleTransition st = new ScaleTransition(Duration.millis(150), vistaCarta);
        vistaCarta.setOnMouseEntered(e -> {
            st.setToX(1.1); st.setToY(1.1); st.play();
            vistaCarta.setEffect(sombraHover);
            vistaCarta.setCursor(javafx.scene.Cursor.HAND);
        });
        vistaCarta.setOnMouseExited(e -> {
            st.setToX(1.0); st.setToY(1.0); st.play();
            vistaCarta.setEffect(sombraNormal);
        });
    }

    // 🌟 LA MAGIA DE LA REPARTICIÓN (Cartas Voladoras) 🌟
    private static void animarEntradaCarta(ImageView vistaCarta, int delayMillis) {
        // Empiezan 300 pixeles arriba y transparentes
        vistaCarta.setTranslateY(-300);
        vistaCarta.setOpacity(0);

        TranslateTransition caida = new TranslateTransition(Duration.millis(600), vistaCarta);
        caida.setToY(0);
        caida.setDelay(Duration.millis(delayMillis));

        FadeTransition aparicion = new FadeTransition(Duration.millis(600), vistaCarta);
        aparicion.setToValue(1.0);
        aparicion.setDelay(Duration.millis(delayMillis));

        caida.play();
        aparicion.play();
    }
    // ----------------------------------------

    private static void mostrarAnuncioDebuff(GestorEscenas gestor, String efecto, Runnable continuar) {
        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox pergamino = new VBox(25);
        pergamino.setAlignment(Pos.CENTER);
        pergamino.setMaxWidth(750);
        pergamino.setMaxHeight(450);
        pergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 40;");
        pergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0,0,0,0.8)));

        Label titulo = new Label("¡CIEGA ESPECIAL!");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        titulo.setTextFill(Color.web("#a42a2a"));
        titulo.setEffect(new DropShadow(2, Color.web("#ffffff")));

        Label desc = new Label(efecto);
        desc.setFont(Font.font("Georgia", FontPosture.ITALIC, 20));
        desc.setTextFill(Color.web("#3b220b"));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-alignment: center;");

        Button btnContinuar = crearBotonPremium("Aceptar Desafío");
        btnContinuar.setOnAction(e -> continuar.run());

        pergamino.getChildren().addAll(titulo, desc, btnContinuar);
        raiz.getChildren().add(pergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }

    private static void pasoEscogerCartaPropia(GestorEscenas gestor, int numJugador, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();
        List<Carta> barajaCompartida = partida.getBarajaComun();
        Pila descartesPropios = numJugador == 1 ? partida.getDescartesJ1() : partida.getDescartesJ2();

        ListaSimple opciones = new ListaSimple();
        int cantidadAMostrar = Math.min(3, barajaCompartida.size());
        for (int i = 0; i < cantidadAMostrar; i++) {
            opciones.agregarCarta(barajaCompartida.remove(0));
        }

        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(800);
        panelPergamino.setMaxHeight(600);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");
        panelPergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0,0,0,0.8)));

        Label titulo = new Label("Jugador " + numJugador + ": Escoge UNA carta");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.web("#3b220b"));
        titulo.setEffect(new DropShadow(2, Color.web("#ffffff")));

        HBox contenedorCartas = new HBox(40);
        contenedorCartas.setAlignment(Pos.CENTER);
        contenedorCartas.setStyle("-fx-padding: 20;");

        int totalOpciones = opciones.getTamaño();
        int delayAnimacion = 0; // Para que caigan una por una

        for (int i = 1; i <= totalOpciones; i++) {
            final int indiceElegido = i;
            Carta carta = opciones.obtenerCarta(i);

            VBox tarjetaOpcion = new VBox(15);
            tarjetaOpcion.setAlignment(Pos.CENTER);

            ImageView vistaCarta = new ImageView();
            vistaCarta.setFitWidth(130);
            vistaCarta.setFitHeight(180);
            vistaCarta.setPreserveRatio(true);
            try {
                CargadorImagenes.cargarCartaEnVista(vistaCarta, carta);
            } catch (Exception ex) {}

            aplicarEfectoCarta(vistaCarta);
            animarEntradaCarta(vistaCarta, delayAnimacion); // Llama a la animación
            delayAnimacion += 150; // La siguiente carta tarda 150ms más en caer

            Button btnElegir = crearBotonPremium("Elegir Carta");
            btnElegir.setOnAction(e -> {
                if (carta != null) jugador.getManoActual()[0] = carta;
                for (int j = 1; j <= totalOpciones; j++) {
                    if (j != indiceElegido) {
                        Carta cartaDescarte = opciones.obtenerCarta(j);
                        if (cartaDescarte != null && descartesPropios != null) descartesPropios.apilar(cartaDescarte);
                    }
                }
                opciones.vaciar();
                continuar.run();
            });

            vistaCarta.setOnMouseClicked(e -> btnElegir.fire());

            tarjetaOpcion.getChildren().addAll(vistaCarta, btnElegir);
            contenedorCartas.getChildren().add(tarjetaOpcion);
        }

        panelPergamino.getChildren().addAll(titulo, contenedorCartas);
        raiz.getChildren().add(panelPergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }

    private static void pasoEscogerDeDescartes(GestorEscenas gestor, int numJugador, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador jugador = (numJugador == 1) ? partida.getJugador1() : partida.getJugador2();
        Pila descartesRival = numJugador == 1 ? partida.getDescartesJ2() : partida.getDescartesJ1();
        List<Carta> barajaCompartida = partida.getBarajaComun();

        ListaSimple opciones = new ListaSimple();
        for (int i = 0; i < 2; i++) {
            if (descartesRival != null && !descartesRival.estaVacia()) opciones.agregarCarta(descartesRival.desapilar());
            else if (!barajaCompartida.isEmpty()) opciones.agregarCarta(barajaCompartida.remove(0));
        }
        while (opciones.getTamaño() < 3 && !barajaCompartida.isEmpty()) {
            opciones.agregarCarta(barajaCompartida.remove(0));
        }

        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(25);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(850);
        panelPergamino.setMaxHeight(600);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");
        panelPergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0,0,0,0.8)));

        Label titulo = new Label("Jugador " + numJugador + ": Escoge tu segunda carta");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        titulo.setTextFill(Color.web("#3b220b"));
        titulo.setEffect(new DropShadow(2, Color.web("#ffffff")));

        HBox contenedorCartas = new HBox(40);
        contenedorCartas.setAlignment(Pos.CENTER);

        int totalOpciones = opciones.getTamaño();
        int delayAnimacion = 0; // Animación de caída

        for (int i = 1; i <= totalOpciones; i++) {
            final int indiceElegido = i;
            Carta carta = opciones.obtenerCarta(i);

            VBox tarjetaOpcion = new VBox(15);
            tarjetaOpcion.setAlignment(Pos.CENTER);

            ImageView vistaCarta = new ImageView();
            vistaCarta.setFitWidth(130);
            vistaCarta.setFitHeight(180);
            vistaCarta.setPreserveRatio(true);
            try {
                CargadorImagenes.cargarCartaEnVista(vistaCarta, carta);
            } catch (Exception ex) {}

            aplicarEfectoCarta(vistaCarta);
            animarEntradaCarta(vistaCarta, delayAnimacion);
            delayAnimacion += 150;

            Button btnElegir = crearBotonPremium("Elegir Carta");
            btnElegir.setOnAction(e -> {
                if (carta != null) jugador.getManoActual()[1] = carta;
                Pila descartesPropios = numJugador == 1 ? partida.getDescartesJ1() : partida.getDescartesJ2();
                for (int j = 1; j <= totalOpciones; j++) {
                    if (j != indiceElegido) descartesPropios.apilar(opciones.obtenerCarta(j));
                }
                opciones.vaciar();
                continuar.run();
            });
            vistaCarta.setOnMouseClicked(e -> btnElegir.fire());

            tarjetaOpcion.getChildren().addAll(vistaCarta, btnElegir);
            contenedorCartas.getChildren().add(tarjetaOpcion);
        }

        panelPergamino.getChildren().addAll(titulo, contenedorCartas);
        raiz.getChildren().add(panelPergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }

    private static void pasoMesaComun(GestorEscenas gestor, int cantidad, Runnable continuar) {
        Partida partida = gestor.getPartida();
        List<Carta> barajaCompartida = partida.getBarajaComun();

        for (int i = 0; i < cantidad; i++) {
            if (!barajaCompartida.isEmpty() && partida.getMesaComun().size() < 5) {
                partida.getMesaComun().add(barajaCompartida.remove(0));
            }
        }

        // Cuando ya están las 5 cartas comunitarias sobre la mesa, las ordenamos
        // de menor a mayor (Selection Sort) para que los jugadores identifiquen
        // más fácil escaleras, pares, tríos, etc. y decidan si usar sus jokers.
        if (partida.getMesaComun().size() == 5) {
            OrdenadorCartas.ordenarPorValor(partida.getMesaComun());
        }

        StackPane raiz = new StackPane();
        try {
            Image imgMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imgMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox pergamino = new VBox(30);
        pergamino.setAlignment(Pos.CENTER);
        pergamino.setMaxWidth(900);
        pergamino.setMaxHeight(550);
        pergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");
        pergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0,0,0,0.8)));

        Label texto = new Label("Cartas en la Mesa Común");
        texto.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        texto.setTextFill(Color.web("#3b220b"));
        texto.setEffect(new DropShadow(2, Color.web("#ffffff")));

        HBox contenedorCartasMesa = new HBox(20);
        contenedorCartasMesa.setAlignment(Pos.CENTER);

        int delayAnimacion = 0;
        for (Carta c : partida.getMesaComun()) {
            ImageView vistaCarta = new ImageView();
            vistaCarta.setFitWidth(120);
            vistaCarta.setFitHeight(170);
            vistaCarta.setPreserveRatio(true);
            try { CargadorImagenes.cargarCartaEnVista(vistaCarta, c); } catch (Exception ex) {}

            aplicarEfectoCarta(vistaCarta);
            animarEntradaCarta(vistaCarta, delayAnimacion);
            delayAnimacion += 150; // Efecto Crupier

            contenedorCartasMesa.getChildren().add(vistaCarta);
        }

        Button btn = crearBotonPremium("Continuar a la siguiente fase");
        btn.setOnAction(e -> continuar.run());

        pergamino.getChildren().addAll(texto, contenedorCartasMesa, btn);
        raiz.getChildren().add(pergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }

    private static void pasoDestape(GestorEscenas gestor, Runnable continuar) {
        Partida partida = gestor.getPartida();
        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();

        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(950);
        panelPergamino.setMaxHeight(700);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 25;");
        panelPergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0,0,0,0.8)));

        Label titulo = new Label("¡DESTAPE DE LA MESA!");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        titulo.setTextFill(Color.web("#a42a2a"));
        titulo.setEffect(new DropShadow(2, Color.web("#ffffff")));

        int delayAnimacion = 0; // Aquí llevaremos la cuenta de TODAS las cartas de la pantalla

        VBox seccionCentral = new VBox(10);
        seccionCentral.setAlignment(Pos.CENTER);
        Label labelMesa = new Label("Mesa Común");
        labelMesa.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        labelMesa.setTextFill(Color.web("#5c3a18"));

        HBox seccionMesa = new HBox(15);
        seccionMesa.setAlignment(Pos.CENTER);
        for (Carta c : partida.getMesaComun()) {
            if (c != null) {
                ImageView imgView = new ImageView();
                imgView.setFitWidth(90); imgView.setFitHeight(130); imgView.setPreserveRatio(true);
                try { CargadorImagenes.cargarCartaEnVista(imgView, c); } catch (Exception ex) {}

                aplicarEfectoCarta(imgView);
                animarEntradaCarta(imgView, delayAnimacion);
                delayAnimacion += 100; // Caen un poco más rápido aquí

                seccionMesa.getChildren().add(imgView);
            }
        }
        seccionCentral.getChildren().addAll(labelMesa, seccionMesa);

        HBox manosJugadoresBox = new HBox(60);
        manosJugadoresBox.setAlignment(Pos.CENTER);
        manosJugadoresBox.setStyle("-fx-padding: 10 0;");

        VBox boxJ1 = new VBox(10);
        boxJ1.setAlignment(Pos.CENTER);
        Label lblJ1 = new Label(j1.getNombre() + " (Mano)");
        lblJ1.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblJ1.setTextFill(Color.web("#3b220b"));
        HBox cartasJ1 = new HBox(10);
        cartasJ1.setAlignment(Pos.CENTER);
        for (Carta c : j1.getManoActual()) {
            if (c != null) {
                ImageView imgView = new ImageView();
                imgView.setFitWidth(90); imgView.setFitHeight(130); imgView.setPreserveRatio(true);
                try { CargadorImagenes.cargarCartaEnVista(imgView, c); } catch (Exception ex) {}

                aplicarEfectoCarta(imgView);
                animarEntradaCarta(imgView, delayAnimacion);
                delayAnimacion += 100;

                cartasJ1.getChildren().add(imgView);
            }
        }
        boxJ1.getChildren().addAll(lblJ1, cartasJ1);

        VBox boxJ2 = new VBox(10);
        boxJ2.setAlignment(Pos.CENTER);
        Label lblJ2 = new Label(j2.getNombre() + " (Mano)");
        lblJ2.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblJ2.setTextFill(Color.web("#3b220b"));
        HBox cartasJ2 = new HBox(10);
        cartasJ2.setAlignment(Pos.CENTER);
        for (Carta c : j2.getManoActual()) {
            if (c != null) {
                ImageView imgView = new ImageView();
                imgView.setFitWidth(90); imgView.setFitHeight(130); imgView.setPreserveRatio(true);
                try { CargadorImagenes.cargarCartaEnVista(imgView, c); } catch (Exception ex) {}

                aplicarEfectoCarta(imgView);
                animarEntradaCarta(imgView, delayAnimacion);
                delayAnimacion += 100;

                cartasJ2.getChildren().add(imgView);
            }
        }
        boxJ2.getChildren().addAll(lblJ2, cartasJ2);

        manosJugadoresBox.getChildren().addAll(boxJ1, boxJ2);

        Button btnMejorarJ1 = crearBotonPremium("Usar Joker (" + j1.getNombre() + ")");
        btnMejorarJ1.setOnAction(e -> {
            List<Joker> jokersJ1 = partida.getJokersJ1();
            if (jokersJ1 != null && !jokersJ1.isEmpty()) {
                NodoArbol nuevaMejora = partida.getArbolMejoras().raiz;
                for (Carta c : j1.getManoActual()) {
                    if (c != null && !c.isComunitaria()) c.setNivelEvolucion(nuevaMejora);
                }
                jokersJ1.remove(0);
                btnMejorarJ1.setText("¡J1 Mejorado!");
                btnMejorarJ1.setDisable(true);
            } else {
                btnMejorarJ1.setText("Sin Jokers");
            }
        });

        Button btnMejorarJ2 = crearBotonPremium("Usar Joker (" + j2.getNombre() + ")");
        btnMejorarJ2.setOnAction(e -> {
            List<Joker> jokersJ2 = partida.getJokersJ2();
            if (jokersJ2 != null && !jokersJ2.isEmpty()) {
                NodoArbol nuevaMejora = partida.getArbolMejoras().raiz;
                for (Carta c : j2.getManoActual()) {
                    if (c != null && !c.isComunitaria()) c.setNivelEvolucion(nuevaMejora);
                }
                jokersJ2.remove(0);
                btnMejorarJ2.setText("¡J2 Mejorado!");
                btnMejorarJ2.setDisable(true);
            } else {
                btnMejorarJ2.setText("Sin Jokers");
            }
        });

        HBox botonesMejoraBox = new HBox(20, btnMejorarJ1, btnMejorarJ2);
        botonesMejoraBox.setAlignment(Pos.CENTER);

        Button btnContinuar = crearBotonPremium("Evaluar Ronda");
        btnContinuar.setOnAction(e -> continuar.run());

        panelPergamino.getChildren().addAll(titulo, seccionCentral, manosJugadoresBox, botonesMejoraBox, btnContinuar);
        raiz.getChildren().add(panelPergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }

    private static void pasoEvaluacion(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();
        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();

        Carta[] mesaComunArr = partida.getMesaComun().toArray(new Carta[0]);
        List<Carta> listaManoJ1 = new ArrayList<>();
        for (Carta c : j1.getManoActual()) if (c != null) listaManoJ1.add(c);

        List<Carta> listaManoJ2 = new ArrayList<>();
        for (Carta c : j2.getManoActual()) if (c != null) listaManoJ2.add(c);

        int nivelJoker = partida.nivelRecursividadJokerComunal();
        if (Partida.DEBUFF_JOKER_COMUNAL_NULO.equals(partida.getEfectoRondaActual())) {
            nivelJoker = 1;
        }

        MotorPuntaje motor = new MotorPuntaje();
        String efectoActual = partida.getEfectoRondaActual();
        ResultadoMano resJ1 = motor.calcularJugadaConDetalle(mesaComunArr, listaManoJ1.toArray(new Carta[0]), partida.getJokersJ1().toArray(new Joker[0]), nivelJoker, efectoActual);
        ResultadoMano resJ2 = motor.calcularJugadaConDetalle(mesaComunArr, listaManoJ2.toArray(new Carta[0]), partida.getJokersJ2().toArray(new Joker[0]), nivelJoker, efectoActual);
        long puntajeFinalJ1 = resJ1.getPuntajeFinal();
        long puntajeFinalJ2 = resJ2.getPuntajeFinal();

        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(920);
        panelPergamino.setMaxHeight(650);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");
        panelPergamino.setEffect(new DropShadow(20, 10, 10, Color.color(0,0,0,0.8)));

        Label titulo = new Label("EVALUACIÓN RONDA " + partida.getNumeroRonda());
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.web("#3b220b"));

        long metaFichas = partida.getMetaFichas();
        Label lblMeta = new Label("Meta de Fichas Requerida: " + metaFichas);
        lblMeta.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        lblMeta.setTextFill(Color.web("#a42a2a"));

        HBox cuerpoPrincipal = new HBox(40);
        cuerpoPrincipal.setAlignment(Pos.CENTER);

        VBox guiaManosBox = new VBox(8);
        guiaManosBox.setAlignment(Pos.CENTER_LEFT);
        guiaManosBox.setStyle("-fx-background-color: rgba(92, 58, 24, 0.1); -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #5c3a18; -fx-border-radius: 8;");

        Label lblGuiaTitulo = new Label("VALORES DE MANOS");
        lblGuiaTitulo.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        lblGuiaTitulo.setTextFill(Color.web("#3b220b"));
        guiaManosBox.getChildren().add(lblGuiaTitulo);

        String[] infoManos = {
                "• Escalera Real: 100 pts x 8", "• Full House: 40 pts x 4",
                "• Color (Flush): 35 pts x 4", "• Escalera: 30 pts x 4",
                "• Trío: 30 pts x 3", "• Doble Par: 20 pts x 2",
                "• Par: 10 pts x 2", "• Carta Alta: 5 pts x 1"
        };
        for (String textoMano : infoManos) {
            Label lblM = new Label(textoMano);
            lblM.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
            lblM.setTextFill(Color.web("#5c3a18"));
            guiaManosBox.getChildren().add(lblM);
        }

        VBox resultadosBox = new VBox(25);
        resultadosBox.setAlignment(Pos.CENTER);
        resultadosBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #5c3a18; -fx-border-radius: 8;");
        resultadosBox.setEffect(new DropShadow(5, Color.color(0,0,0,0.2)));

        Label labelJ1 = new Label(j1.getNombre() + " hizo " + resJ1.getNombreMano() + "\n➔ " + puntajeFinalJ1 + " fichas");
        labelJ1.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        labelJ1.setTextFill(Color.web("#3b220b"));
        labelJ1.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label labelJ2 = new Label(j2.getNombre() + " hizo " + resJ2.getNombreMano() + "\n➔ " + puntajeFinalJ2 + " fichas");
        labelJ2.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        labelJ2.setTextFill(Color.web("#3b220b"));
        labelJ2.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        boolean perdioJ1 = puntajeFinalJ1 < metaFichas;
        boolean perdioJ2 = puntajeFinalJ2 < metaFichas;

        // Las rondas jefe son las que traen un efecto/debuff especial (cada
        // 3 rondas). Si se gana una, el Joker Comunal sube 1 punto y desde
        // la SIGUIENTE ronda repetirá la mano una vez más (tope de 5 veces).
        boolean esRondaJefe = efectoActual != null;
        if (esRondaJefe && !perdioJ1 && !perdioJ2) {
            partida.getJokerComunal().sumarPunto();
        }

        String resultadoPartida = (perdioJ1 || perdioJ2) ? "¡Alerta! Un jugador no alcanzó la meta." : "¡Excelente! Ambos superaron la meta.";

        Label lblResultado = new Label(resultadoPartida);
        lblResultado.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblResultado.setTextFill((perdioJ1 || perdioJ2) ? Color.web("#a42a2a") : Color.web("#2d6a4f"));

        resultadosBox.getChildren().addAll(labelJ1, labelJ2, lblResultado);

        if (partida.jokerComunalVisible()) {
            VBox jokerComunalBox = new VBox(8);
            jokerComunalBox.setAlignment(Pos.CENTER);
            jokerComunalBox.setMaxWidth(150);
            jokerComunalBox.setStyle("-fx-background-color: rgba(122, 30, 30, 0.15); -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #7a1e1e; -fx-border-radius: 8;");

            ImageView imgComunal = new ImageView();
            try {
                imgComunal.setImage(CargadorImagenes.cargarJoker("comunal"));
            } catch (Exception ex) {}
            imgComunal.setFitWidth(70);
            imgComunal.setPreserveRatio(true);

            Label lblComunalTitulo = new Label("JOKER COMUNAL");
            lblComunalTitulo.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
            lblComunalTitulo.setTextFill(Color.web("#3b220b"));
            lblComunalTitulo.setWrapText(true);
            lblComunalTitulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            Label lblComunalInfo = new Label("Repitió esta mano x" + nivelJoker + "\n(" + partida.getJokerComunal().getPuntos() + " ronda(s) jefe superada(s))");
            lblComunalInfo.setFont(Font.font("Georgia", FontPosture.ITALIC, 11));
            lblComunalInfo.setTextFill(Color.web("#5c3a18"));
            lblComunalInfo.setWrapText(true);
            lblComunalInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            jokerComunalBox.getChildren().addAll(imgComunal, lblComunalTitulo, lblComunalInfo);
            cuerpoPrincipal.getChildren().add(jokerComunalBox); // a la izquierda de todo lo demás
        }

        cuerpoPrincipal.getChildren().addAll(guiaManosBox, resultadosBox);

        Button btnMenuPrincipal = crearBotonPremium("Volver al Menú");
        btnMenuPrincipal.setOnAction(e -> gestor.mostrarMenuPrincipal());

        Button btnSiguienteRonda = crearBotonPremium("Siguiente Ronda / Tienda");
        btnSiguienteRonda.setOnAction(e -> {
            partida.setResultadoUltimaRonda(perdioJ1, perdioJ2, puntajeFinalJ1, puntajeFinalJ2);
            if (perdioJ1 || perdioJ2) gestor.mostrarGameOver();
            else Tienda.iniciar(gestor);
        });

        HBox botonesBox = new HBox(20, btnMenuPrincipal, btnSiguienteRonda);
        botonesBox.setAlignment(Pos.CENTER);

        if (partida.getHistorialRondas() != null) {
            partida.getHistorialRondas().agregar(
                    j1.getNombre(), resJ1.getNombreMano(), String.valueOf(puntajeFinalJ1), !perdioJ1,
                    j2.getNombre(), resJ2.getNombreMano(), String.valueOf(puntajeFinalJ2), !perdioJ2, (int) metaFichas
            );
        }

        panelPergamino.getChildren().addAll(titulo, lblMeta, cuerpoPrincipal, botonesBox);
        raiz.getChildren().add(panelPergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }
}