import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteTest {

    SQLite sqlite;

    @BeforeEach
    void setUp() {
        sqlite = new SQLite();
        sqlite.crearTablas(); // Asegúrate de que las tablas estén creadas antes de cada test
    }

    @Test
    void conectar() {
        Connection connection = SQLite.conectar();
        assertNotNull(connection, "La conexión a la base de datos debería ser exitosa.");
        SQLite.cerrar(connection);
    }

    @Test
    void cerrar() {
        Connection connection = SQLite.conectar();
        assertDoesNotThrow(() -> SQLite.cerrar(connection), "Cerrar la conexión no debería lanzar excepciones.");
    }

    @Test
    void crearTablas() {
        // No esperamos errores al crear tablas si ya existen
        assertDoesNotThrow(() -> sqlite.crearTablas(), "La creación de tablas no debería lanzar excepciones.");
    }

    @Test
    void crearUsuario() {
        SQLite sqlite = new SQLite();
        String dni = "12345678Z"; // DNI único para la prueba
        double saldoEsperado = 1000.0;

        // Limpia la base de datos antes de la prueba
        try (Connection conn = SQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM usuarios WHERE dni = ?")) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
        } catch (SQLException e) {
            fail("Error al limpiar la base de datos: " + e.getMessage());
        }

        // Crear usuario
        sqlite.crearUsuario(dni, saldoEsperado);

        // Verificar saldo inicial
        double saldoReal = -1;
        try (Connection conn = SQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT saldo FROM usuarios WHERE dni = ?")) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                saldoReal = rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            fail("Error al consultar la base de datos: " + e.getMessage());
        }

        // Comprobar si el saldo inicial es el esperado
        assertEquals(saldoEsperado, saldoReal, "El saldo inicial debería ser " + saldoEsperado + ".");
    }


    @Test
    void ingresar() {
        SQLite sqlite = new SQLite();
        String dni = "12345678Z"; // DNI único para la prueba
        String conceptoEsperado = "Salario";
        double cantidad = 2000.0;

        // Asegurarse de que el usuario existe
        sqlite.crearUsuario(dni, 1000.0);

        // Registrar un ingreso
        sqlite.ingresar(dni, conceptoEsperado, cantidad);

        // Verificar que el ingreso se ha registrado correctamente
        String conceptoReal = null;
        try (Connection conn = SQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT concepto FROM ingresos WHERE dni = ? ORDER BY id DESC LIMIT 1")) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                conceptoReal = rs.getString("concepto");
            }
        } catch (SQLException e) {
            fail("Error al consultar la base de datos: " + e.getMessage());
        }

        // Comparar valores esperados y reales
        assertEquals(conceptoEsperado, conceptoReal, "El concepto debería ser 'Salario'.");
    }


    @Test
    void gastar() {
        SQLite sqlite = new SQLite();
        String dni = "12345678Z"; // DNI válido para la prueba
        String conceptoEsperado = "Renta";
        double cantidad = 150.0;

        // Asegurarse de que el usuario existe
        sqlite.crearUsuario(dni, 1000.0);

        // Registrar un gasto
        sqlite.gastar(dni, conceptoEsperado, cantidad);

        // Verificar que el gasto se ha registrado correctamente
        String conceptoReal = null;
        try (Connection conn = SQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT concepto FROM gastos WHERE dni = ? ORDER BY id DESC LIMIT 1")) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                conceptoReal = rs.getString("concepto");
            }
        } catch (SQLException e) {
            fail("Error al consultar la base de datos: " + e.getMessage());
        }

        // Comparar valores esperados y reales
        assertEquals(conceptoEsperado, conceptoReal, "El concepto debería ser 'Renta'.");
    }

}
