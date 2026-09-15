import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PantallaMejora {
    private BorderPane root;
    private ArbolMejoras arbolMejoras;
    private Carta cartaSeleccionada;
    private Runnable onMejoraCompleta;

    public PantallaMejora(ArbolMejoras arbolMejoras, Carta cartaSeleccionada, Runnable onMejoraCompleta) {
        this.arbolMejoras = arbolMejoras;
        this.cartaSeleccionada = cartaSeleccionada;
        this.onMejoraCompleta = onMejoraCompleta;
        crearUI();
    }

    public static void mostrar(GestorEscenas gestor, int numJugador, Runnable onMejoraCompleta) {
        Partida partida = gestor.getPartida();
        Jugador jugador = numJugador == 1 ? partida.getJugador1() : partida.getJugador2();

        // Obtiene la primera carta de la mano del jugador para evolucionarla
        Carta carta = jugador.getManoActual()[0];
        if(carta == null) carta = new Carta("As", "Picas", 14, false); // Fallback por si la mano está vacía

        PantallaMejora pantalla = new PantallaMejora(partida.getArbolMejoras(), carta, onMejoraCompleta);
        gestor.mostrar(new Scene(pantalla.getRoot(), 1024, 768));
    }
    // PARRA CHECA ESTO PARA IMPLEMENTAR
    private void aplicarMejora(boolean esIzquierda, java.util.List<Carta> cartasSeleccionadas) {
        // Validar el límite de máximo 2 cartas que pediste
        if (cartasSeleccionadas.size() > 2) {
            System.out.println("Solo puedes mejorar un máximo de 2 cartas a la vez.");
            return;
        }

        for (Carta carta : cartasSeleccionadas) {
            // 1. Si la carta no ha sido mejorada nunca, empieza en la raiz del arbol
            if (carta.nivelActual == null) {
                carta.nivelActual = arbolMejoras.raiz;
            }

            // 2. Navegar el arbol binario según la elección del jugador
            NodoArbol siguienteNodo = esIzquierda ? carta.nivelActual.izquierdo : carta.nivelActual.derecho;

            // 3. Aplicar la evolución si el nodo existe
            if (siguienteNodo != null) {
                carta.setNivelEvolucion(siguienteNodo);
                System.out.println(carta.getNombre() + " subió a: " + siguienteNodo.nivelEvolucion);
            } else {
                System.out.println(carta.getNombre() + " ya está en el nivel máximo de esta rama.");
            }
        }

        // 4. Terminar y ejecutar Y regresar a la tienda
        if (onMejoraCompleta != null) {
            onMejoraCompleta.run();
        }
    }

    private void crearUI() {
        root = new BorderPane();
        root.setStyle("-fx-background: linear-gradient(135deg, #2c3e50 0%, #34495e 50%, #2c3e50 100%);");

        VBox encabezado = new VBox(10);
        encabezado.setAlignment(Pos.CENTER);
        encabezado.setStyle("-fx-padding: 20; -fx-background-color: rgba(0, 0, 0, 0.5);");

        Text titulo = new Text("MEJORA DE CARTA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titulo.setFill(Color.web("#FFD700"));

        Text cartaInfo = new Text("Carta actual: " + cartaSeleccionada.getNombre() + " de " + cartaSeleccionada.getPalo());
        cartaInfo.setFont(Font.font("Arial", 16));
        cartaInfo.setFill(Color.WHITE);

        encabezado.getChildren().addAll(titulo, cartaInfo);
        root.setTop(encabezado);

        VBox centro = crearArbol();
        root.setCenter(centro);
    }

    private VBox crearArbol() {
        VBox arbol = new VBox(40);
        arbol.setAlignment(Pos.TOP_CENTER);
        arbol.setStyle("-fx-padding: 40;");

        HBox raiz = crearNodo("ESTADO BASE", "Fichas Azules: 10\nMultiplicador: 1x", "#FFD700");

        HBox ramaIzq = new HBox(20);
        ramaIzq.setAlignment(Pos.CENTER);
        HBox nodoIzq = crearBotonMejora("RAMA IZQUIERDA\nFICHAS AZULES +5", "#4CAF50", () -> aplicarMejora(true));
        ramaIzq.getChildren().add(nodoIzq);

        HBox ramaDer = new HBox(20);
        ramaDer.setAlignment(Pos.CENTER);
        HBox nodoDer = crearBotonMejora("RAMA DERECHA\nMULTIPLICADOR x1.5", "#F44336", () -> aplicarMejora(false));
        ramaDer.getChildren().add(nodoDer);

        HBox ramas = new HBox(60);
        ramas.setAlignment(Pos.CENTER);

        VBox izq = new VBox(10);
        izq.setAlignment(Pos.CENTER);
        izq.getChildren().addAll(crearLinea(), nodoIzq);

        VBox der = new VBox(10);
        der.setAlignment(Pos.CENTER);
        der.getChildren().addAll(crearLinea(), nodoDer);

        ramas.getChildren().addAll(izq, der);
        arbol.getChildren().addAll(raiz, ramas);
        return arbol;
    }

    private HBox crearNodo(String titulo, String info, String color) {
        HBox nodo = new HBox();
        nodo.setAlignment(Pos.CENTER);

        VBox contenido = new VBox(5);
        contenido.setAlignment(Pos.CENTER);
        contenido.setStyle("-fx-padding: 15; -fx-background-color: " + color + "; -fx-border-radius: 10;");
        contenido.setPrefWidth(200);

        Text tituloText = new Text(titulo);
        tituloText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        tituloText.setFill(Color.BLACK);

        Text infoText = new Text(info);
        infoText.setFont(Font.font("Arial", 12));
        infoText.setFill(Color.BLACK);
        infoText.setWrappingWidth(180);

        contenido.getChildren().addAll(tituloText, infoText);
        nodo.getChildren().add(contenido);
        return nodo;
    }

    private HBox crearBotonMejora(String titulo, String color, Runnable accion) {
        HBox boton = new HBox();
        boton.setAlignment(Pos.CENTER);
        boton.setStyle("-fx-cursor: hand; -fx-padding: 15; -fx-background-color: " + color + "; -fx-border-radius: 10;");
        boton.setPrefWidth(220);
        boton.setPrefHeight(100);

        Text texto = new Text(titulo);
        texto.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        texto.setFill(Color.WHITE);
        texto.setWrappingWidth(200);

        boton.getChildren().add(texto);

        boton.setOnMouseClicked(e -> accion.run());
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-cursor: hand; -fx-padding: 15; -fx-background-color: " + oscurecerColor(color) + "; -fx-border-radius: 10;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-cursor: hand; -fx-padding: 15; -fx-background-color: " + color + "; -fx-border-radius: 10;"));
        return boton;
    }

    private Text crearLinea() {
        Text linea = new Text("|");
        linea.setFont(Font.font("Arial", 30));
        linea.setFill(Color.WHITE);
        return linea;
    }

    private void aplicarMejora(boolean esIzquierda) {
        // Aquí interactúas con tu clase ArbolMejoras (nivelActual.izquierdo/derecho)
        if (onMejoraCompleta != null) {
            onMejoraCompleta.run();
        }
    }

    private String oscurecerColor(String color) {
        return color.equals("#4CAF50") ? "#388E3C" : color.equals("#F44336") ? "#C62828" : color;
    }

    public BorderPane getRoot() {
        return root;
    }
}