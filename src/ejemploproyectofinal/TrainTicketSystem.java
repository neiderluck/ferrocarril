package ejemploproyectofinal;

import java.util.Scanner;

public class TrainTicketSystem {

    static Scanner sc = new Scanner(System.in);

    // Matrices para guardar los datos de cada entidad
    static String[][] cajeros = new String[5][5];     // id, nombre, apellido, rol, password
    static String[][] usuarios = new String[20][5];   // id, nombre, apellido, origen, destino
    static String[][] trenes = new String[5][3];      // numMaquina, modelo, capacidad
    static String[][] tickets = new String[20][7];    // idTicket, idUsuario, numTren, asiento, origen, destino, precio
    static String[][] ventas = new String[20][5];     // idVenta, idTicket, precio, cantidad, total

    static int totalCajeros = 0;
    static int totalUsuarios = 0;
    static int totalTrenes = 0;
    static int totalTickets = 0;
    static int totalVentas = 0;
    static String cajeroActual = "";

    public static void main(String[] args) {
        cargarDatosIniciales();
        menuInicio();
    }

    // Carga los 3 trenes de la compañia con capacidad de 4 pasajeros y un cajero base
    public static void cargarDatosIniciales() {
        cajeros[0] = new String[]{"1", "Admin", "General", "Cajero", "1234"};
        totalCajeros = 1;

        trenes[0] = new String[]{"101", "Locomotora Antioquia - Ruta Santa Fe", "4"};
        trenes[1] = new String[]{"102", "Locomotora Porce - Ruta Cisneros", "4"};
        trenes[2] = new String[]{"103", "Locomotora Magdalena - Ruta Puerto Berrio", "4"};
        totalTrenes = 3;
    }

    // Menu inicial antes de entrar al dashboard
    public static void menuInicio() {
        int opcion = 0;
        while (opcion != 3) {
            System.out.println("\n=== FERROCARRILES DE ANTIOQUIA ===");
            System.out.println("1. Registrar cajero");
            System.out.println("2. Iniciar sesion");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    registrarCajero();
                    break;
                case 2:
                    if (iniciarSesion()) {
                        dashboard();
                    }
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    // Registro de un nuevo cajero
    public static void registrarCajero() {
        if (totalCajeros >= cajeros.length) {
            System.out.println("No hay espacio para mas cajeros.");
            return;
        }
        System.out.println("\n--- Registro de Cajero ---");
        System.out.print("ID: ");
        cajeros[totalCajeros][0] = sc.nextLine();
        System.out.print("Nombre: ");
        cajeros[totalCajeros][1] = sc.nextLine();
        System.out.print("Apellido: ");
        cajeros[totalCajeros][2] = sc.nextLine();
        System.out.print("Rol: ");
        cajeros[totalCajeros][3] = sc.nextLine();
        System.out.print("Contrasena: ");
        cajeros[totalCajeros][4] = sc.nextLine();
        totalCajeros++;
        System.out.println("Cajero registrado con exito.");
    }

    // Validacion de ingreso del cajero
    public static boolean iniciarSesion() {
        System.out.println("\n--- Iniciar Sesion ---");
        System.out.print("ID del cajero: ");
        String id = sc.nextLine();
        System.out.print("Contrasena: ");
        String pass = sc.nextLine();

        for (int i = 0; i < totalCajeros; i++) {
            if (cajeros[i][0].equals(id) && cajeros[i][4].equals(pass)) {
                cajeroActual = cajeros[i][1] + " " + cajeros[i][2];
                System.out.println("Bienvenido, " + cajeroActual);
                return true;
            }
        }
        System.out.println("Datos incorrectos.");
        return false;
    }

    // Dashboard principal despues del login
    public static void dashboard() {
        int opcion = 0;
        while (opcion != 5) {
            System.out.println("\n=== DASHBOARD (" + cajeroActual + ") ===");
            System.out.println("1. Gestionar Usuarios");
            System.out.println("2. Gestionar Trenes");
            System.out.println("3. Gestionar Tickets");
            System.out.println("4. Gestionar Ventas");
            System.out.println("5. Cerrar sesion");
            System.out.print("Opcion: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1: menuUsuarios(); break;
                case 2: menuTrenes(); break;
                case 3: menuTickets(); break;
                case 4: menuVentas(); break;
                case 5: System.out.println("Sesion cerrada."); break;
                default: System.out.println("Opcion no valida.");
            }
        }
    }

    // Busca la posicion de un registro por su ID en cualquier matriz
    public static int buscarPorId(String[][] matriz, int total, String id) {
        for (int i = 0; i < total; i++) {
            if (matriz[i][0].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    // Elimina una fila corriendo los datos hacia arriba
    public static int eliminarFila(String[][] matriz, int total, int pos) {
        for (int i = pos; i < total - 1; i++) {
            matriz[i] = matriz[i + 1];
        }
        matriz[total - 1] = new String[matriz[0].length];
        return total - 1;
    }

    // ---------------- CRUD USUARIOS ----------------
    public static void menuUsuarios() {
        int op = 0;
        while (op != 6) {
            System.out.println("\n--- MENU USUARIOS ---");
            System.out.println("1. Registrar  2. Actualizar  3. Consultar por ID  4. Consultar todos  5. Eliminar  6. Volver");
            op = Integer.parseInt(sc.nextLine());

            if (op == 1 && totalUsuarios < usuarios.length) {
                System.out.print("ID: "); usuarios[totalUsuarios][0] = sc.nextLine();
                System.out.print("Nombre: "); usuarios[totalUsuarios][1] = sc.nextLine();
                System.out.print("Apellido: "); usuarios[totalUsuarios][2] = sc.nextLine();
                System.out.print("Origen (Medellin / Santa Fe / Cisneros / Puerto Berrio): ");
                usuarios[totalUsuarios][3] = sc.nextLine();
                System.out.print("Destino (Santa Fe / Cisneros / Puerto Berrio): ");
                usuarios[totalUsuarios][4] = sc.nextLine();
                totalUsuarios++;
                System.out.println("Usuario guardado.");
            } else if (op == 2) {
                System.out.print("ID a actualizar: ");
                int pos = buscarPorId(usuarios, totalUsuarios, sc.nextLine());
                if (pos != -1) {
                    System.out.print("Nuevo nombre: "); usuarios[pos][1] = sc.nextLine();
                    System.out.print("Nuevo apellido: "); usuarios[pos][2] = sc.nextLine();
                    System.out.print("Nuevo origen: "); usuarios[pos][3] = sc.nextLine();
                    System.out.print("Nuevo destino: "); usuarios[pos][4] = sc.nextLine();
                    System.out.println("Usuario actualizado.");
                } else System.out.println("No encontrado.");
            } else if (op == 3) {
                System.out.print("ID a buscar: ");
                int pos = buscarPorId(usuarios, totalUsuarios, sc.nextLine());
                if (pos != -1) {
                    System.out.println(usuarios[pos][0] + " - " + usuarios[pos][1] + " " + usuarios[pos][2] + " | " + usuarios[pos][3] + " -> " + usuarios[pos][4]);
                } else System.out.println("No encontrado.");
            } else if (op == 4) {
                for (int i = 0; i < totalUsuarios; i++) {
                    System.out.println(usuarios[i][0] + " - " + usuarios[i][1] + " " + usuarios[i][2] + " | " + usuarios[i][3] + " -> " + usuarios[i][4]);
                }
            } else if (op == 5) {
                System.out.print("ID a eliminar: ");
                int pos = buscarPorId(usuarios, totalUsuarios, sc.nextLine());
                if (pos != -1) {
                    totalUsuarios = eliminarFila(usuarios, totalUsuarios, pos);
                    System.out.println("Usuario eliminado.");
                } else System.out.println("No encontrado.");
            }
        }
    }

    // ---------------- CRUD TRENES ----------------
    public static void menuTrenes() {
        int op = 0;
        while (op != 6) {
            System.out.println("\n--- MENU TRENES ---");
            System.out.println("1. Registrar  2. Actualizar  3. Consultar por # Maquina  4. Consultar todos  5. Eliminar  6. Volver");
            op = Integer.parseInt(sc.nextLine());

            if (op == 1 && totalTrenes < trenes.length) {
                System.out.print("# de maquina: "); trenes[totalTrenes][0] = sc.nextLine();
                System.out.print("Modelo: "); trenes[totalTrenes][1] = sc.nextLine();
                trenes[totalTrenes][2] = "4"; // Capacidad fija de 4 pasajeros
                totalTrenes++;
                System.out.println("Tren guardado con capacidad de 4 pasajeros.");
            } else if (op == 2) {
                System.out.print("# de maquina a actualizar: ");
                int pos = buscarPorId(trenes, totalTrenes, sc.nextLine());
                if (pos != -1) {
                    System.out.print("Nuevo modelo: "); trenes[pos][1] = sc.nextLine();
                    System.out.print("Nueva capacidad (max 4): "); trenes[pos][2] = sc.nextLine();
                    System.out.println("Tren actualizado.");
                } else System.out.println("No encontrado.");
            } else if (op == 3) {
                System.out.print("# de maquina: ");
                int pos = buscarPorId(trenes, totalTrenes, sc.nextLine());
                if (pos != -1) {
                    System.out.println("Maquina: " + trenes[pos][0] + " | Modelo: " + trenes[pos][1] + " | Capacidad: " + trenes[pos][2]);
                } else System.out.println("No encontrado.");
            } else if (op == 4) {
                for (int i = 0; i < totalTrenes; i++) {
                    System.out.println("Maquina: " + trenes[i][0] + " | Modelo: " + trenes[i][1] + " | Capacidad: " + trenes[i][2]);
                }
            } else if (op == 5) {
                System.out.print("# de maquina a eliminar: ");
                int pos = buscarPorId(trenes, totalTrenes, sc.nextLine());
                if (pos != -1) {
                    totalTrenes = eliminarFila(trenes, totalTrenes, pos);
                    System.out.println("Tren eliminado.");
                } else System.out.println("No encontrado.");
            }
        }
    }

    // ---------------- CRUD TICKETS ----------------
    public static void menuTickets() {
        int op = 0;
        while (op != 6) {
            System.out.println("\n--- MENU TICKETS ---");
            System.out.println("1. Registrar  2. Actualizar  3. Consultar por ID  4. Consultar todos  5. Eliminar  6. Volver");
            op = Integer.parseInt(sc.nextLine());

            if (op == 1 && totalTickets < tickets.length) {
                System.out.print("ID Ticket: "); String idT = sc.nextLine();
                System.out.print("ID Usuario: "); String idU = sc.nextLine();
                int posU = buscarPorId(usuarios, totalUsuarios, idU);
                System.out.print("# Maquina Tren (101, 102, 103): "); String idTr = sc.nextLine();
                int posTr = buscarPorId(trenes, totalTrenes, idTr);

                if (posU == -1 || posTr == -1) {
                    System.out.println("Usuario o tren no existe.");
                } else {
                    int ocupados = 0;
                    for (int i = 0; i < totalTickets; i++) {
                        if (tickets[i][2].equals(idTr)) ocupados++;
                    }
                    int cap = Integer.parseInt(trenes[posTr][2]);
                    if (ocupados >= cap) {
                        System.out.println("El tren ya tiene los " + cap + " asientos llenos.");
                    } else {
                        tickets[totalTickets][0] = idT;
                        tickets[totalTickets][1] = idU;
                        tickets[totalTickets][2] = idTr;
                        tickets[totalTickets][3] = "Asiento-" + (ocupados + 1);
                        tickets[totalTickets][4] = usuarios[posU][3];
                        tickets[totalTickets][5] = usuarios[posU][4];
                        System.out.print("Precio del ticket: ");
                        tickets[totalTickets][6] = sc.nextLine();
                        totalTickets++;
                        System.out.println("Ticket asignado en asiento " + (ocupados + 1) + ".");
                    }
                }
            } else if (op == 2) {
                System.out.print("ID Ticket a actualizar: ");
                int pos = buscarPorId(tickets, totalTickets, sc.nextLine());
                if (pos != -1) {
                    System.out.print("Nuevo asiento: "); tickets[pos][3] = sc.nextLine();
                    System.out.print("Nuevo origen: "); tickets[pos][4] = sc.nextLine();
                    System.out.print("Nuevo destino: "); tickets[pos][5] = sc.nextLine();
                    System.out.print("Nuevo precio: "); tickets[pos][6] = sc.nextLine();
                    System.out.println("Ticket actualizado.");
                } else System.out.println("No encontrado.");
            } else if (op == 3) {
                System.out.print("ID Ticket: ");
                int pos = buscarPorId(tickets, totalTickets, sc.nextLine());
                if (pos != -1) {
                    mostrarTicket(pos);
                } else System.out.println("No encontrado.");
            } else if (op == 4) {
                for (int i = 0; i < totalTickets; i++) mostrarTicket(i);
            } else if (op == 5) {
                System.out.print("ID Ticket a eliminar: ");
                int pos = buscarPorId(tickets, totalTickets, sc.nextLine());
                if (pos != -1) {
                    totalTickets = eliminarFila(tickets, totalTickets, pos);
                    System.out.println("Ticket eliminado.");
                } else System.out.println("No encontrado.");
            }
        }
    }

    public static void mostrarTicket(int i) {
        System.out.println("Ticket: " + tickets[i][0] + " | Usuario: " + tickets[i][1] + " | Tren: " + tickets[i][2]
                + " | " + tickets[i][3] + " | " + tickets[i][4] + " -> " + tickets[i][5] + " | $" + tickets[i][6]);
    }

    // ---------------- CRUD VENTAS ----------------
    public static void menuVentas() {
        int op = 0;
        while (op != 6) {
            System.out.println("\n--- MENU VENTAS ---");
            System.out.println("1. Registrar  2. Actualizar  3. Consultar por ID  4. Consultar todas  5. Eliminar  6. Volver");
            op = Integer.parseInt(sc.nextLine());

            if (op == 1 && totalVentas < ventas.length) {
                System.out.print("ID Venta: "); String idV = sc.nextLine();
                System.out.print("ID Ticket: "); String idT = sc.nextLine();
                int posT = buscarPorId(tickets, totalTickets, idT);
                if (posT == -1) {
                    System.out.println("El ticket no existe.");
                } else {
                    double precio = Double.parseDouble(tickets[posT][6]);
                    System.out.print("Cantidad: ");
                    int cant = Integer.parseInt(sc.nextLine());
                    double total = precio * cant;

                    ventas[totalVentas][0] = idV;
                    ventas[totalVentas][1] = idT;
                    ventas[totalVentas][2] = String.valueOf(precio);
                    ventas[totalVentas][3] = String.valueOf(cant);
                    ventas[totalVentas][4] = String.valueOf(total);
                    totalVentas++;
                    System.out.println("Venta registrada. Total a pagar: $" + total);
                }
            } else if (op == 2) {
                System.out.print("ID Venta a actualizar: ");
                int pos = buscarPorId(ventas, totalVentas, sc.nextLine());
                if (pos != -1) {
                    System.out.print("Nueva cantidad: ");
                    int cant = Integer.parseInt(sc.nextLine());
                    double precio = Double.parseDouble(ventas[pos][2]);
                    ventas[pos][3] = String.valueOf(cant);
                    ventas[pos][4] = String.valueOf(precio * cant);
                    System.out.println("Venta actualizada.");
                } else System.out.println("No encontrada.");
            } else if (op == 3) {
                System.out.print("ID Venta: ");
                int pos = buscarPorId(ventas, totalVentas, sc.nextLine());
                if (pos != -1) {
                    System.out.println("Venta: " + ventas[pos][0] + " | Ticket: " + ventas[pos][1] + " | Precio: $" + ventas[pos][2] + " | Cant: " + ventas[pos][3] + " | Total: $" + ventas[pos][4]);
                } else System.out.println("No encontrada.");
            } else if (op == 4) {
                for (int i = 0; i < totalVentas; i++) {
                    System.out.println("Venta: " + ventas[i][0] + " | Ticket: " + ventas[i][1] + " | Precio: $" + ventas[i][2] + " | Cant: " + ventas[i][3] + " | Total: $" + ventas[i][4]);
                }
            } else if (op == 5) {
                System.out.print("ID Venta a eliminar: ");
                int pos = buscarPorId(ventas, totalVentas, sc.nextLine());
                if (pos != -1) {
                    totalVentas = eliminarFila(ventas, totalVentas, pos);
                    System.out.println("Venta eliminada.");
                } else System.out.println("No encontrada.");
            }
        }
    }
}