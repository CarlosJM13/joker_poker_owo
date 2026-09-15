import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
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
        PantallaTransicion.mostrar(gestor, "Turno del Jugador 1: Escoger carta de la baraja", () -> {
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
        PantallaTransicion.mostrar(gestor, "Turno del Jugador 1: Escoger entre descartes del rival", () -> {
            pasoEscogerDeDescartes(gestor, 1, () -> ejecutarPaso5(gestor));
        });
    }

    private static void ejecutarPaso5(GestorEscenas gestor) {
        PantallaTransicion.mostrar(gestor, "Turno del Jugador 2: Escoger entre descartes del rival", () -> {
            pasoEscogerDeDescartes(gestor, 2, () -> ejecutarPaso6(gestor));
        });
    }

    private static void ejecutarPaso6(GestorEscenas gestor) {
        pasoMesaComun(gestor, 2, () -> ejecutarPaso7(gestor));
    }

    private static void ejecutarPaso7(GestorEscenas gestor) {
        pasoDestape(gestor, () -> pasoEvaluacion(gestor));
    }

    private static void mostrarAnuncioDebuff(GestorEscenas gestor, String efecto, Runnable continuar) {
        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox pergamino = new VBox(20);
        pergamino.setAlignment(Pos.CENTER);
        pergamino.setMaxWidth(750);
        pergamino.setMaxHeight(450);
        pergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 40;");

        Label titulo = new Label("¡ADVERTENCIA: CIEGA ESPECIAL!");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#a42a2a"));

        Label desc = new Label(efecto);
        desc.setFont(Font.font("Georgia", FontPosture.ITALIC, 16));
        desc.setTextFill(Color.web("#3b220b"));
        desc.setWrapText(true);
        desc.setStyle("-fx-text-alignment: center;");

        Button btnContinuar = new Button("Aceptar Desafío");
        btnContinuar.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnContinuar.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 10 30; -fx-cursor: hand;");
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
            Carta carta = barajaCompartida.remove(0);
            opciones.agregarCarta(carta);
        }

        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            BackgroundImage bgMesa = new BackgroundImage(
                    imagenMesa,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            raiz.setBackground(new Background(bgMesa));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(750);
        panelPergamino.setMaxHeight(550);
        panelPergamino.setStyle(
                "-fx-background-image: url('/assets/fondo/mplantilla2.jpg');" +
                        "-fx-background-size: stretch;" +
                        "-fx-padding: 30;"
        );

        Label titulo = new Label("Jugador " + numJugador + ": Escoge UNA carta para conservar");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web("#3b220b"));

        HBox contenedorCartas = new HBox(25);
        contenedorCartas.setAlignment(Pos.CENTER);

        int totalOpciones = opciones.getTamaño();
        for (int i = 1; i <= totalOpciones; i++) {
            final int indiceElegido = i;
            Carta carta = opciones.obtenerCarta(i);

            VBox tarjetaOpcion = new VBox(10);
            tarjetaOpcion.setAlignment(Pos.CENTER);

            ImageView vistaCarta = new ImageView();
            try {
                Image imgCarta = CargadorImagenes.cargarCarta(carta);
                if (imgCarta != null) {
                    vistaCarta.setImage(imgCarta);
                }
            } catch (Exception ex) {}

            vistaCarta.setFitWidth(110);
            vistaCarta.setFitHeight(160);
            vistaCarta.setPreserveRatio(true);

            Button btnElegir = new Button("Elegir");
            btnElegir.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
            btnElegir.setStyle(
                    "-fx-background-image: url('/assets/fondo/boton_borde.png');" +
                            "-fx-background-size: stretch;" +
                            "-fx-background-color: transparent;" +
                            "-fx-text-fill: #2c1808;" +
                            "-fx-padding: 8 20;" +
                            "-fx-cursor: hand;"
            );

            btnElegir.setOnAction(e -> {
                if (carta != null) {
                    jugador.getManoActual()[0] = carta;
                }

                for (int j = 1; j <= totalOpciones; j++) {
                    if (j != indiceElegido) {
                        Carta cartaDescarte = opciones.obtenerCarta(j);
                        if (cartaDescarte != null && descartesPropios != null) {
                            descartesPropios.apilar(cartaDescarte);
                        }
                    }
                }
                opciones.vaciar();
                continuar.run();
            });

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
        Pila descartesActivos = (numJugador == 1) ? partida.getDescartesJ1() : partida.getDescartesJ2();
        Pila descartesRival = numJugador == 1 ? partida.getDescartesJ2() : partida.getDescartesJ1();
        List<Carta> barajaCompartida = partida.getBarajaComun();

        ListaSimple opciones = new ListaSimple();

        for (int i = 0; i < 2; i++) {
            if (descartesRival != null && !descartesRival.estaVacia()) {
                opciones.agregarCarta(descartesRival.desapilar());
            } else if (!barajaCompartida.isEmpty()) {
                opciones.agregarCarta(barajaCompartida.remove(0));
            }
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

        VBox panelPergamino = new VBox(20);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(850);
        panelPergamino.setMaxHeight(550);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");

        Label titulo = new Label("Jugador " + numJugador + ": Escoge tu segunda carta");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.web("#3b220b"));

        HBox contenedorCartas = new HBox(25);
        contenedorCartas.setAlignment(Pos.CENTER);

        int totalOpciones = opciones.getTamaño();
        for (int i = 1; i <= totalOpciones; i++) {
            final int indiceElegido = i;
            Carta carta = opciones.obtenerCarta(i);

            VBox tarjetaOpcion = new VBox(10);
            tarjetaOpcion.setAlignment(Pos.CENTER);

            ImageView vistaCarta = new ImageView();
            try {
                if (carta != null) {
                    vistaCarta.setImage(CargadorImagenes.cargarCarta(carta));
                }
            } catch (Exception ex) {}

            vistaCarta.setFitWidth(100);
            vistaCarta.setFitHeight(145);
            vistaCarta.setPreserveRatio(true);

            Button btnElegir = new Button("Elegir");
            btnElegir.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
            btnElegir.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 8 20; -fx-cursor: hand;");

            btnElegir.setOnAction(e -> {
                if (carta != null) {
                    jugador.getManoActual()[1] = carta;
                }
                Pila descartesPropios = numJugador == 1 ? partida.getDescartesJ1() : partida.getDescartesJ2();
                for (int j = 1; j <= totalOpciones; j++) {
                    if (j != indiceElegido) {
                        descartesPropios.apilar(opciones.obtenerCarta(j));
                    }
                }
                opciones.vaciar();
                continuar.run();
            });

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

        StackPane raiz = new StackPane();
        try {
            Image imgMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imgMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox pergamino = new VBox(20);
        pergamino.setAlignment(Pos.CENTER);
        pergamino.setMaxWidth(800);
        pergamino.setMaxHeight(550);
        pergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");

        Label texto = new Label("Cartas en la Mesa Común");
        texto.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        texto.setTextFill(Color.web("#3b220b"));

        HBox contenedorCartasMesa = new HBox(15);
        contenedorCartasMesa.setAlignment(Pos.CENTER);

        List<Carta> mesa = partida.getMesaComun();
        for (Carta c : mesa) {
            ImageView vistaCarta = new ImageView();
            try {
                vistaCarta.setImage(CargadorImagenes.cargarCarta(c));
            } catch (Exception ex) {}
            vistaCarta.setFitWidth(95);
            vistaCarta.setFitHeight(140);
            vistaCarta.setPreserveRatio(true);
            contenedorCartasMesa.getChildren().add(vistaCarta);
        }

        Button btn = new Button("Continuar");
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btn.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 10 25; -fx-cursor: hand;");
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

        VBox panelPergamino = new VBox(15);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(920);
        panelPergamino.setMaxHeight(650);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 25;");

        Label titulo = new Label("¡DESTAPE Y EVALUACIÓN DE LA MESA!");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web("#3b220b"));

        Label labelMesa = new Label("Mesa Común");
        labelMesa.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        labelMesa.setTextFill(Color.web("#5c3a18"));

        HBox seccionMesa = new HBox(10);
        seccionMesa.setAlignment(Pos.CENTER);
        List<Carta> centroLista = partida.getMesaComun();
        if (centroLista != null) {
            for (Carta c : centroLista) {
                if (c != null) {
                    ImageView imgView = new ImageView();
                    try {
                        imgView.setImage(CargadorImagenes.cargarCarta(c));
                    } catch (Exception ex) {}
                    imgView.setFitWidth(80);
                    imgView.setFitHeight(115);
                    imgView.setPreserveRatio(true);
                    seccionMesa.getChildren().add(imgView);
                }
            }
        }

        HBox manosJugadoresBox = new HBox(40);
        manosJugadoresBox.setAlignment(Pos.CENTER);

        VBox boxJ1 = new VBox(5);
        boxJ1.setAlignment(Pos.CENTER);
        Label lblJ1 = new Label(j1.getNombre() + " (Mano)");
        lblJ1.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        lblJ1.setTextFill(Color.web("#3b220b"));
        HBox cartasJ1 = new HBox(10);
        cartasJ1.setAlignment(Pos.CENTER);
        if (j1.getManoActual() != null) {
            for (Carta c : j1.getManoActual()) {
                if (c != null) {
                    ImageView imgView = new ImageView();
                    try { imgView.setImage(CargadorImagenes.cargarCarta(c)); } catch (Exception ex) {}
                    imgView.setFitWidth(80); imgView.setFitHeight(115); imgView.setPreserveRatio(true);
                    cartasJ1.getChildren().add(imgView);
                }
            }
        }
        boxJ1.getChildren().addAll(lblJ1, cartasJ1);

        VBox boxJ2 = new VBox(5);
        boxJ2.setAlignment(Pos.CENTER);
        Label lblJ2 = new Label(j2.getNombre() + " (Mano)");
        lblJ2.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        lblJ2.setTextFill(Color.web("#3b220b"));
        HBox cartasJ2 = new HBox(10);
        cartasJ2.setAlignment(Pos.CENTER);
        if (j2.getManoActual() != null) {
            for (Carta c : j2.getManoActual()) {
                if (c != null) {
                    ImageView imgView = new ImageView();
                    try { imgView.setImage(CargadorImagenes.cargarCarta(c)); } catch (Exception ex) {}
                    imgView.setFitWidth(80); imgView.setFitHeight(115); imgView.setPreserveRatio(true);
                    cartasJ2.getChildren().add(imgView);
                }
            }
        }
        boxJ2.getChildren().addAll(lblJ2, cartasJ2);

        manosJugadoresBox.getChildren().addAll(boxJ1, boxJ2);

        Button btnMejorarJ1 = new Button("Usar Joker (" + j1.getNombre() + ")");
        btnMejorarJ1.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
        btnMejorarJ1.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-cursor: hand; -fx-padding: 8 15;");
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

        Button btnMejorarJ2 = new Button("Usar Joker (" + j2.getNombre() + ")");
        btnMejorarJ2.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
        btnMejorarJ2.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-cursor: hand; -fx-padding: 8 15;");
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

        HBox botonesMejoraBox = new HBox(15, btnMejorarJ1, btnMejorarJ2);
        botonesMejoraBox.setAlignment(Pos.CENTER);

        Button btnContinuar = new Button("Evaluar Ronda");
        btnContinuar.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnContinuar.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 10 25; -fx-cursor: hand;");
        btnContinuar.setOnAction(e -> continuar.run());

        panelPergamino.getChildren().addAll(titulo, labelMesa, seccionMesa, manosJugadoresBox, botonesMejoraBox, btnContinuar);
        raiz.getChildren().add(panelPergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }

    private static void pasoEvaluacion(GestorEscenas gestor) {
        Partida partida = gestor.getPartida();
        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();

        Carta[] mesaComunArr = partida.getMesaComun().toArray(new Carta[0]);

        List<Carta> listaManoJ1 = new ArrayList<>();
        for (Carta c : j1.getManoActual()) {
            if (c != null) listaManoJ1.add(c);
        }
        Carta[] manoJ1Arr = listaManoJ1.toArray(new Carta[0]);

        List<Carta> listaManoJ2 = new ArrayList<>();
        for (Carta c : j2.getManoActual()) {
            if (c != null) listaManoJ2.add(c);
        }
        Carta[] manoJ2Arr = listaManoJ2.toArray(new Carta[0]);

        Joker[] jokersJ1Arr = partida.getJokersJ1().toArray(new Joker[0]);
        Joker[] jokersJ2Arr = partida.getJokersJ2().toArray(new Joker[0]);
        int nivelJoker = partida.nivelRecursividadJokerComunal();

        MotorPuntaje motor = new MotorPuntaje();
        ResultadoMano resJ1 = motor.calcularJugadaConDetalle(mesaComunArr, manoJ1Arr, jokersJ1Arr, nivelJoker);
        ResultadoMano resJ2 = motor.calcularJugadaConDetalle(mesaComunArr, manoJ2Arr, jokersJ2Arr, nivelJoker);

        long puntajeFinalJ1 = resJ1.getPuntajeFinal();
        long puntajeFinalJ2 = resJ2.getPuntajeFinal();

        StackPane raiz = new StackPane();
        try {
            Image imagenMesa = new Image(Ronda.class.getResourceAsStream("/assets/fondo/mesa.jpg"));
            raiz.setBackground(new Background(new BackgroundImage(imagenMesa, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            raiz.setStyle("-fx-background-color: #2b1d0c;");
        }

        VBox panelPergamino = new VBox(15);
        panelPergamino.setAlignment(Pos.CENTER);
        panelPergamino.setMaxWidth(920);
        panelPergamino.setMaxHeight(620);
        panelPergamino.setStyle("-fx-background-image: url('/assets/fondo/mplantilla2.jpg'); -fx-background-size: stretch; -fx-padding: 30;");

        Label titulo = new Label("EVALUACIÓN DE LA RONDA " + partida.getNumeroRonda());
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#3b220b"));

        // Obtenemos en qué ronda vamos
        int rondaActual = partida.getNumeroRonda();

        // Hacemos que la meta escale (150 en ronda 1, 300 en ronda 2, 450 en ronda 3...)
        long metaFichas = 150L * rondaActual;

        Label lblMeta = new Label("Meta de Fichas Requerida: " + metaFichas);
        lblMeta.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        lblMeta.setTextFill(Color.web("#7a5230"));
        HBox cuerpoPrincipal = new HBox(30);
        cuerpoPrincipal.setAlignment(Pos.CENTER);

        VBox guiaManosBox = new VBox(6);
        guiaManosBox.setAlignment(Pos.CENTER_LEFT);
        guiaManosBox.setStyle("-fx-background-color: rgba(92, 58, 24, 0.1); -fx-padding: 15; -fx-background-radius: 5;");

        Label lblGuiaTitulo = new Label("VALORES DE MANOS");
        lblGuiaTitulo.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        lblGuiaTitulo.setTextFill(Color.web("#3b220b"));

        String[] infoManos = {
                "• Escalera Real: 100 Fichas x 8 Multi",
                "• Full House: 40 Fichas x 4 Multi",
                "• Color (Flush): 35 Fichas x 4 Multi",
                "• Escalera: 30 Fichas x 4 Multi",
                "• Trío: 30 Fichas x 3 Multi",
                "• Doble Par: 20 Fichas x 2 Multi",
                "• Par: 10 Fichas x 2 Multi",
                "• Carta Alta: 5 Fichas x 1 Multi"
        };

        guiaManosBox.getChildren().add(lblGuiaTitulo);
        for (String textoMano : infoManos) {
            Label lblM = new Label(textoMano);
            lblM.setFont(Font.font("Georgia", FontPosture.ITALIC, 11));
            lblM.setTextFill(Color.web("#5c3a18"));
            guiaManosBox.getChildren().add(lblM);
        }

        VBox resultadosBox = new VBox(20);
        resultadosBox.setAlignment(Pos.CENTER);

        VBox desgloseBox = new VBox(12);
        desgloseBox.setAlignment(Pos.CENTER);

        Label labelJ1 = new Label(j1.getNombre() + " hizo " + resJ1.getNombreMano() + " ➔ " + puntajeFinalJ1 + " fichas");
        labelJ1.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        labelJ1.setTextFill(Color.web("#3b220b"));

        Label labelJ2 = new Label(j2.getNombre() + " hizo " + resJ2.getNombreMano() + " ➔ " + puntajeFinalJ2 + " fichas");
        labelJ2.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        labelJ2.setTextFill(Color.web("#3b220b"));


        boolean perdioJ1 = puntajeFinalJ1 < metaFichas;
        boolean perdioJ2 = puntajeFinalJ2 < metaFichas;

        String resultadoPartida = (perdioJ1 || perdioJ2)
                ? "¡Alerta! Un jugador no alcanzó la meta."
                : "¡Excelente! Ambos superaron la meta.";

        Label lblResultado = new Label(resultadoPartida);
        lblResultado.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
        lblResultado.setTextFill(Color.web("#7a5230"));

        desgloseBox.getChildren().addAll(labelJ1, labelJ2, lblResultado);
        resultadosBox.getChildren().add(desgloseBox);

        cuerpoPrincipal.getChildren().addAll(guiaManosBox, resultadosBox);

        // --- BOTONES FINALES ---
        Button btnMenuPrincipal = new Button("Volver al Menú");
        btnMenuPrincipal.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnMenuPrincipal.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 10 25; -fx-cursor: hand;");
        btnMenuPrincipal.setOnAction(e -> gestor.mostrarMenuPrincipal());

        Button btnSiguienteRonda = new Button("Siguiente Ronda / Tienda");
        btnSiguienteRonda.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnSiguienteRonda.setStyle("-fx-background-image: url('/assets/fondo/boton_borde.png'); -fx-background-size: stretch; -fx-background-color: transparent; -fx-text-fill: #2c1808; -fx-padding: 10 25; -fx-cursor: hand;");

        btnSiguienteRonda.setOnAction(e -> {
            if (perdioJ1 || perdioJ2) {
                System.out.println("Fin del juego por no pasar la meta.");
                // gestor.mostrarGameOver();
            } else {
                Tienda.iniciar(gestor);
            }
        });

        HBox botonesBox = new HBox(20, btnMenuPrincipal, btnSiguienteRonda);
        botonesBox.setAlignment(Pos.CENTER);

        if (partida.getHistorialRondas() != null) {
            partida.getHistorialRondas().agregar(
                    j1.getNombre(),
                    resJ1.getNombreMano(),
                    String.valueOf(puntajeFinalJ1),
                    !perdioJ1,
                    j2.getNombre(),
                    resJ2.getNombreMano(),
                    String.valueOf(puntajeFinalJ2),
                    !perdioJ2,
                    (int) metaFichas
            );
        }

        panelPergamino.getChildren().addAll(titulo, lblMeta, cuerpoPrincipal, botonesBox);

        raiz.getChildren().add(panelPergamino);
        gestor.mostrar(new Scene(raiz, 1024, 768));
    }
}