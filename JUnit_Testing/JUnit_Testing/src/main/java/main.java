import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        SQLite sqlite = new SQLite();
        Scanner sc = new Scanner(System.in);
        boolean validadorDni = false;
        String DNI=null;

        // Para crear y validar el dni

        while (validadorDni==false){
            System.out.println("Introduce tu DNI: ");
            DNI = sc.nextLine();
            validadorDni = comprobadorDeDNI(DNI);
        }

        // Añadir el dni a la base de datos

        SQLite.crearUsuario(DNI, 0);

        // Menu principal

        while (true) {
            System.out.println("""
            1. Crear Ingresos
            2. Crear Gastos
            3. Salir
            Seleccione una opción: 
        """);
            int option = sc.nextInt();
            switch (option) {

                // Ingresos

                case 1:
                    int opcionIngreso = 0;
                    String concepto;
                    double cantidad;
                    do{
                        System.out.println("""
                            Introduce el concepto:
                                1. Nomina
                                2. Venta en páginas de segunda mano
                        """);
                        opcionIngreso = sc.nextInt();
                        switch (opcionIngreso){

                            // En caso de que el usuario elija la opcion 1 NOMINA

                            case 1:
                                concepto = "Nomina";
                                System.out.println("Introduce la cantidad: ");
                                cantidad = sc.nextDouble();
                                SQLite.ingresar(DNI, concepto, cantidad);
                                break;

                            // En caso de que el usuario elija la opcion 2 VENTA EN PÁGINAS DE SEGUNDA MANO

                            case 2:
                                concepto = "Venta en páginas de segunda mano";
                                System.out.println("Introduce la cantidad: ");
                                cantidad = sc.nextDouble();
                                SQLite.ingresar(DNI, concepto, cantidad);
                                break;
                            default:
                                System.out.println("Opción inválida, intenta de nuevo.");
                        }
                    }while (opcionIngreso<1 || opcionIngreso>2);
                    break;

                // Gastos

                case 2:
                    int opcionGasto = 0;
                    String conceptoGasto;
                    double cantidadGasto;
                    do {
                        System.out.println("""
                                Introduce el concepto:
                                1. Vacaciones
                                2. Alquiler
                                3. IRPF de su nomina 15%
                                4. Vicios Variados
                        """);
                        opcionGasto = sc.nextInt();
                        switch (opcionGasto) {

                            // En caso de que el usuario elija la opcion 1 VACACIONES

                            case 1:
                                conceptoGasto = "Vacaciones";
                                System.out.println("Introduce la cantidad: ");
                                cantidadGasto = sc.nextDouble();
                                SQLite.gastar(DNI, conceptoGasto, cantidadGasto);
                                break;

                            // En caso de que el usuario elija la opcion 2 ALQUILER

                            case 2:
                                conceptoGasto = "Alquiler";
                                System.out.println("Introduce la cantidad: ");
                                cantidadGasto = sc.nextDouble();
                                SQLite.gastar(DNI, conceptoGasto, cantidadGasto);
                                break;

                            // En caso de que el usuario elija la opcion 3 IRPF DE SU NOMINA 15%

                            case 3:
                                conceptoGasto = "IRPF de su nomina 15%";
                                System.out.println("Introduce la cantidad: ");
                                cantidadGasto = sc.nextDouble();
                                SQLite.gastar(DNI, conceptoGasto, cantidadGasto);
                                break;

                            // En caso de que el usuario elija la opcion 4 VICIOS VARIADOS

                            case 4:
                                conceptoGasto = "Vicios Variados";
                                System.out.println("Introduce la cantidad: ");
                                cantidadGasto = sc.nextDouble();
                                SQLite.gastar(DNI, conceptoGasto, cantidadGasto);
                                break;

                            default:
                                System.out.println("Opción inválida, intenta de nuevo.");
                        }
                    }while (opcionGasto<1 || opcionGasto>4);
                    break;

                // Salir

                case 3:
                    sqlite.cerrar(sqlite.conn);
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opción inválida, intenta de nuevo.");
            }
        }
    }

    // Funcion que me comprueba que el dni sea valido
    public static boolean comprobadorDeDNI(String dni) {

        // Comprobamos que el DNI tenga 9 dígitos

        if (dni.length() != 9) {
            return false;
        }

        // Comprobamos que los primeros 8 dígitos sean numéricos

        for (int i = 0; i < 8; i++) {

            //Mira que los primeros 8 caracteres sean numéricos

            if (!Character.isDigit(dni.charAt(i))) {
                return false;
            }
        }

        // Comprobamos que la letra sea válida

        char letter = dni.charAt(8);
        if (!Character.isLetter(letter)) {
            return false;
        }

        // Comprobamos la división

        int number = Integer.parseInt(dni.substring(0, 8));
        int remainder = number % 23;
        String letters = "TRWAGMYFPDXBNJZSQVHLCKE";
        char expectedLetter = letters.charAt(remainder);

        if (letter != expectedLetter) {
            return false;
        }

        return true;
    }
}
