import java.sql.*;


public class SQLite {

    public static String url = null; // archivo de base de datos
    public static Connection conn = null;

    // Constructor que al usar la clase crea la base de datos
    public SQLite() {
        url = "jdbc:sqlite:finanzas.db";
        conectar();
        crearTablas();
    }

    // Método para establecer la conexión con la base de datos
    public static Connection conectar() {
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conn;
    }

    // Método para cerrar la conexión con la base de datos
    public static void cerrar(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Conexión cerrada con la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la base de datos: " + e.getMessage());
        }
    }

    //Tablas
    public static void crearTablas() {
        String UsuariosTabla = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "dni TEXT NOT NULL UNIQUE,"
                + "saldo REAL NOT NULL); ";

        String GastosTabla = "CREATE TABLE IF NOT EXISTS gastos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "dni TEXT NOT NULL, "
                + "concepto TEXT NOT NULL, "
                + "cantidad REAL NOT NULL, "
                + "FOREIGN KEY (dni) REFERENCES usuarios(dni));";

        String IngresosTabla = "CREATE TABLE IF NOT EXISTS ingresos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "dni TEXT NOT NULL, "
                + "concepto TEXT NOT NULL, "
                + "cantidad REAL NOT NULL, "
                + "FOREIGN KEY (dni) REFERENCES usuarios(dni));";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(UsuariosTabla);
            stmt.execute(GastosTabla);
            stmt.execute(IngresosTabla);
            System.out.println("Tablas creadas con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    // Metodos

    // Metodo para registrar un nuevo usuario
    public static void crearUsuario(String dni, double saldo) {
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO usuarios (dni, saldo) VALUES (?, ?)")) {
            stmt.setString(1, dni);
            stmt.setDouble(2, saldo);
            stmt.executeUpdate();
            System.out.println("Usuario registrado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }

    // Metodo para registrar un nuevo ingreso

    public static void ingresar(String dni, String concepto, double cantidad) {
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO ingresos (dni, concepto, cantidad) VALUES (?, ?, ?)")) {
            stmt.setString(1, dni);
            stmt.setString(2, concepto);
            stmt.setDouble(3, cantidad);
            stmt.executeUpdate();
            System.out.println("Ingreso registrado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al registrar el ingreso: " + e.getMessage());
        }

        // Pillo el sueldo del usuario

        double saldoNuevo=0;

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("SELECT saldo FROM usuarios WHERE dni = ?")) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                saldoNuevo = rs.getDouble("saldo");
            } else {
                System.out.println("No se encontro el usuario.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el saldo: " + e.getMessage());
        }

        saldoNuevo += cantidad;

        // Ahora actualizaremos el sueldo de la base de datos

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET saldo =  ? WHERE dni = ?")) {
            stmt.setDouble(1, saldoNuevo);
            stmt.setString(2, dni);
            stmt.executeUpdate();
            System.out.println("Sueldo actualizado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el saldo: " + e.getMessage());
        }
    }

    public static void gastar(String dni, String concepto, double cantidad) {
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO gastos (dni, concepto, cantidad) VALUES (?, ?, ?)")) {
            stmt.setString(1, dni);
            stmt.setString(2, concepto);
            stmt.setDouble(3, cantidad);
            stmt.executeUpdate();
            System.out.println("Gasto registrado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al registrar el gasto: " + e.getMessage());
        }

        // Pillo el sueldo del usuario

        double saldoNuevo=0;

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("SELECT saldo FROM usuarios WHERE dni = ?")) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                saldoNuevo = rs.getDouble("saldo");
            } else {
                System.out.println("No se encontro el usuario.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el saldo: " + e.getMessage());
        }

        saldoNuevo -= cantidad;

        // Ahora actualizaremos el sueldo de la base de datos

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET saldo = ? WHERE dni = ?")) {
            stmt.setDouble(1, saldoNuevo);
            stmt.setString(2, dni);
            stmt.executeUpdate();
            System.out.println("Sueldo actualizado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el saldo: " + e.getMessage());
        }
    }
}
