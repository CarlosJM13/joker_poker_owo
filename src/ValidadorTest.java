package packageName.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import packageName.Validador;
// Importa tu clase Validador aquí si está en otro paquete
// import packageName.Validador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidadorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        // Arrange global: Limpiar y redirigir la salida de consola para leer los System.out.println
        outContent.reset();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        // Restaurar los streams originales del sistema para no afectar otras pruebas
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /**
     * Método auxiliar para simular la entrada del usuario por teclado.
     * Dado que el Scanner de Validador es 'static', se inicializa con el System.in original
     * antes de que corran las pruebas. Usamos Reflection para inyectar nuestro mock.
     */
    private void simularEntradaUsuario(String data) {
        try {
            ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
            System.setIn(testIn);

            Field scannerField = Validador.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(null, new Scanner(System.in));
        } catch (Exception e) {
            throw new RuntimeException("Error al inyectar el mock en el Scanner estático", e);
        }
    }

    // ==========================================
    // Categoria: Happy Path
    // ==========================================

    @Test
    void testTC01_HappyPath_IngresarNumeroValido() {
        // Arrange
        simularEntradaUsuario("3\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(3, resultado, "El método debe retornar el número válido ingresado (3).");
    }

    // ==========================================
    // Categoria: Edge Cases
    // ==========================================

    @Test
    void testTC02_EdgeCase_LimiteInferiorExacto() {
        // Arrange
        simularEntradaUsuario("1\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(1, resultado, "El método debe aceptar el valor exacto del límite inferior (1).");
    }

    @Test
    void testTC03_EdgeCase_LimiteSuperiorExacto() {
        // Arrange
        simularEntradaUsuario("5\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(5, resultado, "El método debe aceptar el valor exacto del límite superior (5).");
    }

    @Test
    void testTC04_EdgeCase_RangoDeUnSoloElemento() {
        // Arrange
        simularEntradaUsuario("2\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 2, 2);

        // Assert
        assertEquals(2, resultado, "El método debe funcionar cuando el mínimo y el máximo son el mismo valor.");
    }

    @Test
    void testTC05_EdgeCase_RangoNumerosNegativos() {
        // Arrange
        simularEntradaUsuario("-3\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", -5, -1);

        // Assert
        assertEquals(-3, resultado, "El método debe procesar y validar correctamente números negativos.");
    }

    // ==========================================
    // Categoria: Error / Negative Cases
    // ==========================================

    @Test
    void testTC06_ErrorCase_NumeroMenorAlLimite() {
        // Arrange: Enviamos '0' (fuera de rango) seguido de '1' (válido para romper el bucle While)
        simularEntradaUsuario("0\n1\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(1, resultado, "Al final debe retornar el número válido ingresado.");
        assertTrue(outContent.toString().contains("Error: ingrese un numero del 1 al 5 :)"),
                "Debe imprimir el mensaje de error de rango.");
    }

    @Test
    void testTC07_ErrorCase_NumeroMayorAlLimite() {
        // Arrange: Enviamos '6' (fuera de rango) seguido de '1' (válido)
        simularEntradaUsuario("6\n1\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(1, resultado, "Al final debe retornar el número válido ingresado.");
        assertTrue(outContent.toString().contains("Error: ingrese un numero del 1 al 5 :)"),
                "Debe imprimir el mensaje de error de rango.");
    }

    @Test
    void testTC08_ErrorCase_LetrasOCaracteresEspeciales() {
        // Arrange: Enviamos letras y símbolos (inválidos) seguido de '2' (válido)
        simularEntradaUsuario("abc\n#$%\n2\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(2, resultado);
        assertTrue(outContent.toString().contains("Error: ingrese un numero entero"),
                "Debe imprimir el mensaje del bloque catch NumberFormatException.");
    }

    @Test
    void testTC09_ErrorCase_NumeroDecimal() {
        // Arrange: Enviamos decimal '3.5' (inválido para int) seguido de '3' (válido)
        simularEntradaUsuario("3.5\n3\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(3, resultado);
        assertTrue(outContent.toString().contains("Error: ingrese un numero entero"),
                "Debe atrapar la excepción de casteo e imprimir error de tipo entero.");
    }

    @Test
    void testTC10_ErrorCase_StringVacioYEspacios() {
        // Arrange: Enviamos un enter vacío ('\n'), espacios ('   \n') y luego '4' (válido)
        simularEntradaUsuario("\n   \n4\n");

        // Act
        int resultado = Validador.pedirOpcion("Ingrese opción: ", 1, 5);

        // Assert
        assertEquals(4, resultado);
        assertTrue(outContent.toString().contains("Error: ingrese un numero entero"),
                "El input vacío debe disparar la excepción e imprimir el mensaje de error.");
    }
}