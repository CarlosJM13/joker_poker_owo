import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HistorialPartidas {
    private BorderPane root;
    private ListaDoble historial;
    private GestorEscenas gestor;
    
    public HistorialPartidas(ListaDoble historial, GestorEscenas gestor) {
        this.historial = historial;
        this.gestor = gestor;
        crearUI();
    }
    
    private void crearUI() {
        root = new BorderPane();
        root.setStyle("-fx-background: linear-gradient(135deg, #1a237e 0%, #512da8 25%, #6a1b9a 50%, #7b1fa2 75%, #512da8 100%);");
        
        // Encabezado
        VBox encabezado = new VBox(10);
        encabezado.setAlignment(Pos.CENTER);
        encabezado.setStyle("-fx-padding: 20; -fx-background-color: rgba(0, 0, 0, 0.5);");
        
        Text titulo = new Text("HISTORIAL DE PARTIDAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titulo.setFill(Color.web("#FFD700"));
        
        encabezado.getChildren().add(titulo);
        root.setTop(encabezado);
        
        // Centro: listado de rondas
        VBox contenido = crearListadoRondas();
        root.setCenter(contenido);
        
        // Pie
        HBox pie = new HBox(10);
        pie.setAlignment(Pos.CENTER);
        pie.setStyle("-fx-padding: 15; -fx-background-color: rgba(0, 0, 0, 0.5);");
        
        Button btnVolver = new Button("Volver al Menú");
        btnVolver.setStyle("-fx-font-size: 14; -fx-padding: 10 30; -fx-background-color: #2196F3; -fx-text-fill: white;");
        btnVolver.setOnAction(e -> gestor.mostrarMenuPrincipal());
        
        pie.getChildren().add(btnVolver);
        root.setBottom(pie);
    }
    
    private VBox crearListadoRondas() {
        VBox contenido = new VBox(10);
        contenido.setStyle("-fx-padding: 20;");
        
        Text infoText = new Text("Total de rondas jugadas: " + (historial != null ? historial.getTamaño() : 0));
        infoText.setFont(Font.font("Arial", 16));
        infoText.setFill(Color.WHITE);
        
        contenido.getChildren().add(infoText);
        
        // Aquí puedes iterar sobre el historial y mostrar las rondas
        // Por ahora, mostraremos un mensaje simple
        if (historial != null && historial.getTamaño() > 0) {
            Text detalles = new Text("El historial contiene información de cada ronda jugada.");
            detalles.setFont(Font.font("Arial", 14));
            detalles.setFill(Color.web("#B3E5FC"));
            contenido.getChildren().add(detalles);
        } else {
            Text vacio = new Text("No hay partidas registradas aún.");
            vacio.setFont(Font.font("Arial", 14));
            vacio.setFill(Color.web("#FFB74D"));
            contenido.getChildren().add(vacio);
        }
        
        return contenido;
    }
    
    public BorderPane getRoot() {
        return root;
    }
}
