import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameOverScena {
    private BorderPane root;
    private Partida partida;
    private GestorEscenas gestor;
    
    public GameOverScena(Partida partida, GestorEscenas gestor) {
        this.partida = partida;
        this.gestor = gestor;
        crearUI();
    }
    
    private void crearUI() {
        root = new BorderPane();
        root.setStyle("-fx-background: linear-gradient(135deg, #1a1a2e 0%, #16213e 25%, #0f3460 50%, #16213e 75%, #1a1a2e 100%);");
        
        // Centro
        VBox centro = new VBox(30);
        centro.setAlignment(Pos.CENTER);
        centro.setStyle("-fx-padding: 50;");
        
        // Título
        Text titulo = new Text("FIN DE LA PARTIDA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        titulo.setFill(Color.web("#FF6B6B"));
        
        // Resumen
        VBox resumen = crearResumen();
        
        // Botones
        HBox botones = new HBox(15);
        botones.setAlignment(Pos.CENTER);
        
        Button btnMenuPrincipal = new Button("Menú Principal");
        btnMenuPrincipal.setStyle("-fx-font-size: 14; -fx-padding: 12 40; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnMenuPrincipal.setOnAction(e -> gestor.mostrarMenuPrincipal());
        
        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-font-size: 14; -fx-padding: 12 40; -fx-background-color: #F44336; -fx-text-fill: white;");
        btnSalir.setOnAction(e -> gestor.salir());
        
        botones.getChildren().addAll(btnMenuPrincipal, btnSalir);
        
        centro.getChildren().addAll(titulo, resumen, botones);
        root.setCenter(centro);
    }
    
    private VBox crearResumen() {
        VBox resumen = new VBox(15);
        resumen.setAlignment(Pos.CENTER);
        resumen.setStyle("-fx-padding: 30; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10;");
        resumen.setPrefWidth(400);
        
        Jugador j1 = partida.getJugador1();
        Jugador j2 = partida.getJugador2();
        
        Text j1Info = new Text(j1.getNombre() + ": " + j1.getFichas() + " fichas");
        j1Info.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        j1Info.setFill(Color.web("#FFD700"));
        
        Text j2Info = new Text(j2.getNombre() + ": " + j2.getFichas() + " fichas");
        j2Info.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        j2Info.setFill(Color.web("#FFD700"));
        
        Text ganador = new Text();
        ganador.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ganador.setFill(Color.web("#4CAF50"));
        
        if (j1.getFichas() > j2.getFichas()) {
            ganador.setText("¡" + j1.getNombre() + " es el GANADOR!");
        } else {
            ganador.setText("¡" + j2.getNombre() + " es el GANADOR!");
        }
        
        resumen.getChildren().addAll(j1Info, j2Info, ganador);
        return resumen;
    }
    
    public BorderPane getRoot() {
        return root;
    }
}
