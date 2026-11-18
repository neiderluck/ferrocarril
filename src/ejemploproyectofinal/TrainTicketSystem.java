package ejemploproyectofinal;

import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;




public class TrainTicketSystem {




        // Crear las variables para las entidades
        /* Ferrocarriles de Antioquia requiere un sistema que permita asignar
         * un ticket en un tren a un usuario a través de una venta.
         * Del Usuario requiere: id, nombre , apellido, origen , destino.
         * Del cajero requiere conocer los mismos datos (id, nombre, apellido) y adicional
         * el rol y la contraseña.
         * Del tren requiere conocer: # de máquina , módelo, capacidad.
         * Del Ticket requiere conocer: Id del ticket , usuario, tren , asiento , origen , destino, precio.
         * De la venta requiere conocer: id de la venta , id del ticket, precio, cantidad , total
         * El sistema debe permitir registrar, actualizar , consultar por id , consultar todos y eliminar, para cada
         * entidad del sistema (Usuarios, trenes, tickets , ventas)
         * El sistema debe permitir navegar las opciones a través de menús
         * El sistema debe permitir registrar un cajero e iniciar sesión
         * El sistema debe permitir al cajero contar con un dashboard con los menus a las diferentes opciones.
         * después de iniciar sesión.
         * La compañia cuenta con tres trenes cada uno con capacidad de 4 pasajeros.
         * Cada tren hace un viaje de ida y vuelta, en un día , a tres destinos: Santa Fe , Cisneros y Puerto Berrio  */

        // Scanner para capturar los datos
        static Scanner sc = new Scanner(System.in);

        // Usuario tren
        static int id;
        static String nombre;
        static String apellido;
        static String origen;
        static String destino;

        // Usuario cajero
        static String rol;
        static String password;

        // Tren
        static int trenId;
        static String modelo;
        static int capacidad;

        // Ticket
        static int ticketId;
        static int usuario;
        static String origenRuta;
        static String destinoRuta;
        static String silla;
        static int tren;
        static String fecha;
        static String hora;
        static double precio;

        // Venta
        static int ventaId;
        static int ticketIdVenta;
        static int cantidad;
        static double precioVentaUnitario;
        static double totalVenta;

        // Contadores para generar los id automáticamente (1, 2, 3...)
        static int contadorUsuarios = 0;
        static int contadorCajeros = 0;
        static int contadorTrenes = 0;
        static int contadorTickets = 0;
        static int contadorVentas = 0;


        public static void main(String[] args) {
            // Carga los datos guardados anteriormente y recupera el último id de cada tabla
            contadorCajeros = cargarMatriz("cajeros.txt", cajeros);
            contadorUsuarios = cargarMatriz("usuarios.txt", usuarios);
            contadorTrenes = cargarMatriz("trenes.txt", trenes);
            contadorTickets = cargarMatriz("tickets.txt", tickets);
            contadorVentas = cargarMatriz("ventas.txt", ventas);

            iniciarApp();
        }

        // Arrays y Matrices
        // Cada fila es un registro y la columna 0 siempre es el id.
        // Si el id de una fila es null, esa fila está vacía.
        static String[][] usuarios = new String[20][5];
        static String[][] cajeros = new String[5][5];
        static String[][] trenes = new String[3][3];      // la compañía tiene 3 trenes
        static String[][] tickets = new String[50][9];
        static String[][] ventas = new String[50][5];

        static String[] encabezadosUsuario = {"Id", "Nombre", "Apellido", "Origen", "Destino"};
        static String[] encabezadosCajero = {"Id", "Nombre", "Apellido", "Rol", "Contraseña"};
        static String[] encabezadosTren = {"# Máquina", "Modelo", "Capacidad"};
        static String[] encabezadosTicket = {"Id", "Usuario", "Tren", "Asiento", "Origen", "Destino", "Fecha", "Hora", "Precio"};
        static String[] encabezadosVenta = {"Id", "Ticket", "Precio", "Cantidad", "Total"};


        // Métodos para guardar y cargar en archivos de texto

        // Guarda todos los registros activos de una matriz en un archivo .txt separados por ;
        public static void guardarMatriz(String nombreArchivo, String[][] matriz){
            try(PrintWriter escritor = new PrintWriter(new OutputStreamWriter(new FileOutputStream(nombreArchivo), StandardCharsets.UTF_8))){
                for(int i = 0; i < matriz.length; i++){
                    if(matriz[i][0] != null){
                        String linea = "";
                        for(int j = 0; j < matriz[i].length; j++){
                            linea += matriz[i][j];
                            if(j < matriz[i].length - 1){
                                linea += ";";
                            }
                        }
                        escritor.println(linea);
                    }
                }
            } catch(Exception e){
                System.out.println("Error al guardar el archivo " + nombreArchivo);
            }
        }

        // Lee un archivo .txt al iniciar el programa, llena la matriz y devuelve el id más alto
        public static int cargarMatriz(String nombreArchivo, String[][] matriz){
            int maxId = 0;
            File archivo = new File(nombreArchivo);

            if(!archivo.exists()){
                return 0;
            }

            try(Scanner lector = new Scanner(archivo, StandardCharsets.UTF_8)){
                int fila = 0;
                while(lector.hasNextLine() && fila < matriz.length){
                    String linea = lector.nextLine();
                    if(!linea.trim().isEmpty()){
                        String[] datos = linea.split(";", -1);
                        if(datos.length == matriz[fila].length){
                            for(int j = 0; j < matriz[fila].length; j++){
                                matriz[fila][j] = datos[j];
                            }
                            int idActual = Integer.parseInt(matriz[fila][0]);
                            if(idActual > maxId){
                                maxId = idActual;
                            }
                            fila++;
                        }
                    }
                }
            } catch(Exception e){
                System.out.println("Error al cargar el archivo " + nombreArchivo);
            }

            return maxId;
        }


        // Métodos de ayuda (los usan todas las entidades)

        // Lee un texto, evita que quede vacío y reemplaza ; para no dañar el archivo
        public static String leerTexto(String mensaje){
            String texto;
            do{
                System.out.print(mensaje);
                texto = sc.nextLine().trim().replace(";", ",");
                if(texto.isEmpty()){
                    System.out.println("Este campo no puede quedar vacío");
                }
            } while(texto.isEmpty());
            return texto;
        }

        // Lee y valida una fecha con formato exacto DD/MM/AAAA
        public static String leerFecha(String mensaje){
            while(true){
                String texto = leerTexto(mensaje);
                if(texto.length() == 10 && texto.charAt(2) == '/' && texto.charAt(5) == '/'){
                    try{
                        int dia = Integer.parseInt(texto.substring(0, 2));
                        int mes = Integer.parseInt(texto.substring(3, 5));
                        int anio = Integer.parseInt(texto.substring(6, 10));
                        if(dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12 && anio >= 2000 && anio <= 2100){
                            return texto;
                        }
                    } catch(NumberFormatException e){
                        // Continúa y muestra el mensaje de formato inválido
                    }
                }
                System.out.println("Fecha inválida. Use el formato DD/MM/AAAA (ejemplo: 18/11/2026)");
            }
        }

        // Lee y valida una hora con formato exacto HH:MM (24 horas)
        public static String leerHora(String mensaje){
            while(true){
                String texto = leerTexto(mensaje);
                if(texto.length() == 5 && texto.charAt(2) == ':'){
                    try{
                        int hh = Integer.parseInt(texto.substring(0, 2));
                        int mm = Integer.parseInt(texto.substring(3, 5));
                        if(hh >= 0 && hh <= 23 && mm >= 0 && mm <= 59){
                            return texto;
                        }
                    } catch(NumberFormatException e){
                        // Continúa y muestra el mensaje de formato inválido
                    }
                }
                System.out.println("Hora inválida. Use el formato HH:MM de 24 horas (ejemplo: 08:30)");
            }
        }

        // Lee un número entero sin dejar basura en el Scanner
        public static int leerEntero(){
            while(true){
                try{
                    return Integer.parseInt(sc.nextLine().trim());
                } catch(NumberFormatException e){
                    System.out.print("Valor inválido, intente de nuevo: ");
                }
            }
        }

        // Lee un número decimal (para los precios)
        public static double leerDecimal(){
            while(true){
                try{
                    return Double.parseDouble(sc.nextLine().trim());
                } catch(NumberFormatException e){
                    System.out.print("Valor inválido, intente de nuevo: ");
                }
            }
        }

        // Devuelve la posición de la fila que tiene ese id, o -1 si no existe
        public static int buscarFila(String[][] matriz, int idBuscado){
            for(int i = 0; i < matriz.length; i++){
                if(matriz[i][0] != null && matriz[i][0].equals(String.valueOf(idBuscado))){
                    return i;
                }
            }
            return -1;
        }

        // Devuelve la primera fila vacía, o -1 si la matriz está llena
        public static int buscarFilaLibre(String[][] matriz){
            for(int i = 0; i < matriz.length; i++){
                if(matriz[i][0] == null){
                    return i;
                }
            }
            return -1;
        }

        // Revisa si un id está siendo usado en otra matriz (para evitar borrar registros en uso o duplicar ventas)
        public static boolean existeReferencia(String[][] matriz, int columna, int idBuscado){
            for(int i = 0; i < matriz.length; i++){
                if(matriz[i][0] != null && matriz[i][columna].equals(String.valueOf(idBuscado))){
                    return true;
                }
            }
            return false;
        }

        // Muestra una fila con sus encabezados
        public static void mostrarFila(String[][] matriz, String[] encabezados, int fila){
            for(int j = 0; j < encabezados.length; j++){
                System.out.println(encabezados[j] + ": " + matriz[fila][j]);
            }
            System.out.println("----------------------");
        }

        // Consultar todos
        public static void mostrarTodos(String[][] matriz, String[] encabezados){
            boolean hayDatos = false;

            for(int i = 0; i < matriz.length; i++){
                if(matriz[i][0] != null){
                    mostrarFila(matriz, encabezados, i);
                    hayDatos = true;
                }
            }

            if(!hayDatos){
                System.out.println("No hay registros");
            }
        }

        // Consultar por id
        public static void mostrarPorId(String[][] matriz, String[] encabezados){
            System.out.print("Indique el id a consultar: ");
            int fila = buscarFila(matriz, leerEntero());

            if(fila == -1){
                System.out.println("No se encontró ningún registro con ese id");
            } else {
                mostrarFila(matriz, encabezados, fila);
            }
        }

        // Borra los datos de una fila
        public static void limpiarFila(String[][] matriz, int fila){
            for(int j = 0; j < matriz[fila].length; j++){
                matriz[fila][j] = null;
            }
            System.out.println("Registro eliminado");
        }

        // Menú para escoger uno de los tres destinos de la compañía
        public static String elegirDestino(){
            int opcion;

            do{
                System.out.println("Destino: 1. Santa Fe  2. Cisneros  3. Puerto Berrio");
                System.out.print("Seleccione destino: ");
                opcion = leerEntero();
            } while(opcion < 1 || opcion > 3);

            if(opcion == 1){
                return "Santa Fe";
            } else if(opcion == 2){
                return "Cisneros";
            } else {
                return "Puerto Berrio";
            }
        }


        // Métodos Usuario

        public static void crearUsuario(){

            int fila = buscarFilaLibre(usuarios);

            if(fila == -1){
                System.out.println("No hay espacio para más usuarios");
                return;
            }

            nombre = leerTexto("Nombre: ");
            apellido = leerTexto("Apellido: ");
            origen = leerTexto("Origen: ");
            destino = elegirDestino();

            if(origen.equalsIgnoreCase(destino)){
                System.out.println("El origen y el destino no pueden ser el mismo lugar");
                return;
            }

            contadorUsuarios++;
            id = contadorUsuarios;

            usuarios[fila][0] = String.valueOf(id);
            usuarios[fila][1] = nombre;
            usuarios[fila][2] = apellido;
            usuarios[fila][3] = origen;
            usuarios[fila][4] = destino;

            guardarMatriz("usuarios.txt", usuarios);
            System.out.println("Usuario registrado con id " + id);
        }

        public static void consultarUsuarios(){
            mostrarTodos(usuarios, encabezadosUsuario);
        }

        public static void consultarUsuarioPorId(){
            mostrarPorId(usuarios, encabezadosUsuario);
        }

        public static void actualizarUsuario(){

            System.out.print("Ingrese el id del usuario a actualizar: ");
            id = leerEntero();

            int fila = buscarFila(usuarios, id);

            if(fila == -1){
                System.out.println("No se encontró el usuario");
                return;
            }

            System.out.println("Seleccione el dato a actualizar 1. Nombre 2. Apellido 3. Origen 4. Destino");
            int opcion = leerEntero();

            switch (opcion){
                case 1:
                    usuarios[fila][1] = leerTexto("Actualizar nombre: ");
                    break;
                case 2:
                    usuarios[fila][2] = leerTexto("Actualizar apellido: ");
                    break;
                case 3:
                    String nuevoOrigen = leerTexto("Actualizar origen: ");
                    if(nuevoOrigen.equalsIgnoreCase(usuarios[fila][4])){
                        System.out.println("El origen y el destino no pueden ser el mismo lugar");
                        return;
                    }
                    usuarios[fila][3] = nuevoOrigen;
                    break;
                case 4:
                    String nuevoDestino = elegirDestino();
                    if(usuarios[fila][3].equalsIgnoreCase(nuevoDestino)){
                        System.out.println("El origen y el destino no pueden ser el mismo lugar");
                        return;
                    }
                    usuarios[fila][4] = nuevoDestino;
                    break;
                default:
                    System.out.println("Opción inválida");
                    return;
            }

            guardarMatriz("usuarios.txt", usuarios);
            System.out.println("Usuario actualizado");
        }

        public static void eliminarUsuario(){
            System.out.print("Indique el id del usuario a eliminar: ");
            int idEliminar = leerEntero();
            int fila = buscarFila(usuarios, idEliminar);

            if(fila == -1){
                System.out.println("No se encontró ningún usuario con ese id");
                return;
            }

            // Bloquea el borrado si el usuario tiene tickets creados
            if(existeReferencia(tickets, 1, idEliminar)){
                System.out.println("No se puede eliminar: el usuario tiene tickets registrados");
                return;
            }

            limpiarFila(usuarios, fila);
            guardarMatriz("usuarios.txt", usuarios);
        }


        // Métodos del Tren

        public static void crearTren(){

            int fila = buscarFilaLibre(trenes);

            if(fila == -1){
                System.out.println("La compañía solo cuenta con 3 trenes");
                return;
            }

            modelo = leerTexto("Modelo: ");

            do{
                System.out.print("Capacidad (mayor a 0): ");
                capacidad = leerEntero();
                if(capacidad <= 0){
                    System.out.println("La capacidad debe ser mayor a 0");
                }
            } while(capacidad <= 0);

            contadorTrenes++;
            trenId = contadorTrenes;

            trenes[fila][0] = String.valueOf(trenId);
            trenes[fila][1] = modelo;
            trenes[fila][2] = String.valueOf(capacidad);

            guardarMatriz("trenes.txt", trenes);
            System.out.println("Tren registrado con # de máquina " + trenId);
        }

        public static void consultarTrenes(){
            mostrarTodos(trenes, encabezadosTren);
        }

        public static void consultarTrenPorId(){
            mostrarPorId(trenes, encabezadosTren);
        }

        // Verifica que ningún ticket ya creado en ese tren tenga un asiento mayor a la nueva capacidad
        public static boolean capacidadCompatibleConTickets(int trenBuscado, int nuevaCapacidad){
            for(int i = 0; i < tickets.length; i++){
                if(tickets[i][0] != null && tickets[i][2].equals(String.valueOf(trenBuscado))){
                    int asientoOcupado = Integer.parseInt(tickets[i][3]);
                    if(asientoOcupado > nuevaCapacidad){
                        return false;
                    }
                }
            }
            return true;
        }

        public static void actualizarTren(){

            System.out.print("Ingrese el # de máquina del tren a actualizar: ");
            trenId = leerEntero();

            int fila = buscarFila(trenes, trenId);

            if(fila == -1){
                System.out.println("No se encontró el tren");
                return;
            }

            System.out.println("Seleccione el dato a actualizar 1. Modelo 2. Capacidad");
            int opcion = leerEntero();

            switch (opcion){
                case 1:
                    trenes[fila][1] = leerTexto("Nuevo modelo: ");
                    break;
                case 2:
                    int nuevaCapacidad;
                    do{
                        System.out.print("Nueva capacidad (mayor a 0): ");
                        nuevaCapacidad = leerEntero();
                        if(nuevaCapacidad <= 0){
                            System.out.println("La capacidad debe ser mayor a 0");
                        }
                    } while(nuevaCapacidad <= 0);

                    if(!capacidadCompatibleConTickets(trenId, nuevaCapacidad)){
                        System.out.println("No se puede bajar la capacidad: hay tickets con asientos mayores a " + nuevaCapacidad);
                        return;
                    }

                    trenes[fila][2] = String.valueOf(nuevaCapacidad);
                    break;
                default:
                    System.out.println("Opción inválida");
                    return;
            }

            guardarMatriz("trenes.txt", trenes);
            System.out.println("Tren actualizado");
        }

        public static void eliminarTren(){
            System.out.print("Indique el # de máquina del tren a eliminar: ");
            int idEliminar = leerEntero();
            int fila = buscarFila(trenes, idEliminar);

            if(fila == -1){
                System.out.println("No se encontró ningún tren con ese # de máquina");
                return;
            }

            // Bloquea el borrado si el tren tiene tickets asociados
            if(existeReferencia(tickets, 2, idEliminar)){
                System.out.println("No se puede eliminar: el tren tiene tickets registrados");
                return;
            }

            limpiarFila(trenes, fila);
            guardarMatriz("trenes.txt", trenes);
        }


        // Métodos registro Cajero

        public static void crearCajero(){

            int fila = buscarFilaLibre(cajeros);

            if(fila == -1){
                System.out.println("No hay espacio para más cajeros");
                return;
            }

            nombre = leerTexto("Nombre: ");
            apellido = leerTexto("Apellido: ");
            rol = leerTexto("Rol: ");
            password = leerTexto("Contraseña: ");

            contadorCajeros++;
            id = contadorCajeros;

            cajeros[fila][0] = String.valueOf(id);
            cajeros[fila][1] = nombre;
            cajeros[fila][2] = apellido;
            cajeros[fila][3] = rol;
            cajeros[fila][4] = password;

            guardarMatriz("cajeros.txt", cajeros);
            System.out.println("Cajero registrado. Su id para iniciar sesión es " + id);
        }


        // Métodos de Creación del ticket

        // Revisa que el tren no tenga ya un viaje programado hacia otro destino en esa misma fecha
        public static boolean destinoValidoParaTren(int trenBuscado, String destinoBuscado, String fechaViaje, int filaIgnorar){
            for(int i = 0; i < tickets.length; i++){
                if(i == filaIgnorar){
                    continue;
                }
                if(tickets[i][0] != null
                        && tickets[i][2].equals(String.valueOf(trenBuscado))
                        && tickets[i][6].equals(fechaViaje)){
                    if(!tickets[i][5].equalsIgnoreCase(destinoBuscado)){
                        System.out.println("El tren " + trenBuscado + " ya viaja a " + tickets[i][5] + " en la fecha " + fechaViaje);
                        return false;
                    }
                }
            }
            return true;
        }

        // Revisa que el tren exista, que el asiento esté dentro de la capacidad
        // y que nadie más lo tenga en ese tren y en esa fecha
        public static boolean asientoValido(int trenBuscado, int asiento, String fechaViaje, int filaIgnorar){

            int filaTren = buscarFila(trenes, trenBuscado);

            if(filaTren == -1){
                System.out.println("El tren no existe");
                return false;
            }

            if(asiento < 1 || asiento > Integer.parseInt(trenes[filaTren][2])){
                System.out.println("Asiento fuera de la capacidad del tren (1 a " + trenes[filaTren][2] + ")");
                return false;
            }

            for(int i = 0; i < tickets.length; i++){
                if(i == filaIgnorar){
                    continue;
                }
                if(tickets[i][0] != null
                        && tickets[i][2].equals(String.valueOf(trenBuscado))
                        && tickets[i][3].equals(String.valueOf(asiento))
                        && tickets[i][6].equals(fechaViaje)){
                    System.out.println("Ese asiento ya está ocupado en ese tren para la fecha " + fechaViaje);
                    return false;
                }
            }

            return true;
        }

        public static void crearTicket(){

            int fila = buscarFilaLibre(tickets);

            if(fila == -1){
                System.out.println("No hay espacio para más tickets");
                return;
            }

            // Usuario
            System.out.print("Id del usuario: ");
            usuario = leerEntero();
            int filaUsuario = buscarFila(usuarios, usuario);

            if(filaUsuario == -1){
                System.out.println("El usuario no existe, regístrelo primero");
                return;
            }

            // Tren
            System.out.print("# de máquina del tren: ");
            tren = leerEntero();

            if(buscarFila(trenes, tren) == -1){
                System.out.println("El tren no existe, regístrelo primero");
                return;
            }

            // El origen y el destino se toman de los datos del usuario
            origenRuta = usuarios[filaUsuario][3];
            destinoRuta = usuarios[filaUsuario][4];

            if(origenRuta.equalsIgnoreCase(destinoRuta)){
                System.out.println("El origen y el destino del usuario no pueden ser iguales");
                return;
            }

            // Fecha y hora del viaje con formato validado
            fecha = leerFecha("Fecha del viaje (DD/MM/AAAA): ");
            hora = leerHora("Hora del viaje (HH:MM): ");

            // Validar que el tren no vaya a dos destinos distintos el mismo día
            if(!destinoValidoParaTren(tren, destinoRuta, fecha, -1)){
                return;
            }

            // Asiento
            System.out.print("Número de asiento: ");
            int asiento = leerEntero();

            if(!asientoValido(tren, asiento, fecha, -1)){
                return;
            }
            silla = String.valueOf(asiento);

            // Precio mayor a 0
            do{
                System.out.print("Precio (mayor a 0): ");
                precio = leerDecimal();
                if(precio <= 0){
                    System.out.println("El precio debe ser mayor a 0");
                }
            } while(precio <= 0);

            contadorTickets++;
            ticketId = contadorTickets;

            tickets[fila][0] = String.valueOf(ticketId);
            tickets[fila][1] = String.valueOf(usuario);
            tickets[fila][2] = String.valueOf(tren);
            tickets[fila][3] = silla;
            tickets[fila][4] = origenRuta;
            tickets[fila][5] = destinoRuta;
            tickets[fila][6] = fecha;
            tickets[fila][7] = hora;
            tickets[fila][8] = String.valueOf(precio);

            guardarMatriz("tickets.txt", tickets);
            System.out.println("Ticket creado con id " + ticketId);
        }

        public static void consultarTickets(){
            mostrarTodos(tickets, encabezadosTicket);
        }

        public static void consultarTicketPorId(){
            mostrarPorId(tickets, encabezadosTicket);
        }

        public static void actualizarTicket(){

            System.out.print("Ingrese el id del ticket a actualizar: ");
            ticketId = leerEntero();

            int fila = buscarFila(tickets, ticketId);

            if(fila == -1){
                System.out.println("No se encontró el ticket");
                return;
            }

            // Evita alterar precios o rutas de un ticket que ya fue facturado
            if(existeReferencia(ventas, 1, ticketId)){
                System.out.println("No se puede actualizar: el ticket ya tiene una venta asociada");
                return;
            }

            System.out.println("Seleccione el dato a actualizar 1. Tren 2. Asiento 3. Origen 4. Destino 5. Fecha y hora 6. Precio");
            int opcion = leerEntero();

            int trenActual = Integer.parseInt(tickets[fila][2]);
            int asientoActual = Integer.parseInt(tickets[fila][3]);
            String origenActual = tickets[fila][4];
            String destinoActual = tickets[fila][5];
            String fechaActual = tickets[fila][6];

            switch (opcion){
                case 1:
                    System.out.print("Nuevo # de máquina del tren: ");
                    int nuevoTren = leerEntero();
                    if(buscarFila(trenes, nuevoTren) == -1){
                        System.out.println("El tren no existe");
                        return;
                    }
                    if(!destinoValidoParaTren(nuevoTren, destinoActual, fechaActual, fila)){
                        return;
                    }
                    if(!asientoValido(nuevoTren, asientoActual, fechaActual, fila)){
                        return;
                    }
                    tickets[fila][2] = String.valueOf(nuevoTren);
                    break;
                case 2:
                    System.out.print("Nuevo asiento: ");
                    int nuevoAsiento = leerEntero();
                    if(!asientoValido(trenActual, nuevoAsiento, fechaActual, fila)){
                        return;
                    }
                    tickets[fila][3] = String.valueOf(nuevoAsiento);
                    break;
                case 3:
                    String nuevoOrigen = leerTexto("Nuevo origen: ");
                    if(nuevoOrigen.equalsIgnoreCase(destinoActual)){
                        System.out.println("El origen y el destino no pueden ser el mismo lugar");
                        return;
                    }
                    tickets[fila][4] = nuevoOrigen;
                    break;
                case 4:
                    String nuevoDestino = elegirDestino();
                    if(origenActual.equalsIgnoreCase(nuevoDestino)){
                        System.out.println("El origen y el destino no pueden ser el mismo lugar");
                        return;
                    }
                    if(!destinoValidoParaTren(trenActual, nuevoDestino, fechaActual, fila)){
                        return;
                    }
                    tickets[fila][5] = nuevoDestino;
                    break;
                case 5:
                    String nuevaFecha = leerFecha("Nueva fecha (DD/MM/AAAA): ");
                    if(!destinoValidoParaTren(trenActual, destinoActual, nuevaFecha, fila)){
                        return;
                    }
                    if(!asientoValido(trenActual, asientoActual, nuevaFecha, fila)){
                        return;
                    }
                    String nuevaHora = leerHora("Nueva hora (HH:MM): ");
                    tickets[fila][6] = nuevaFecha;
                    tickets[fila][7] = nuevaHora;
                    break;
                case 6:
                    double nuevoPrecio;
                    do{
                        System.out.print("Nuevo precio (mayor a 0): ");
                        nuevoPrecio = leerDecimal();
                        if(nuevoPrecio <= 0){
                            System.out.println("El precio debe ser mayor a 0");
                        }
                    } while(nuevoPrecio <= 0);
                    tickets[fila][8] = String.valueOf(nuevoPrecio);
                    break;
                default:
                    System.out.println("Opción inválida");
                    return;
            }

            guardarMatriz("tickets.txt", tickets);
            System.out.println("Ticket actualizado");
        }

        public static void eliminarTicket(){
            System.out.print("Indique el id del ticket a eliminar: ");
            int idEliminar = leerEntero();
            int fila = buscarFila(tickets, idEliminar);

            if(fila == -1){
                System.out.println("No se encontró ningún ticket con ese id");
                return;
            }

            // Bloquea el borrado si el ticket ya tiene una venta registrada
            if(existeReferencia(ventas, 1, idEliminar)){
                System.out.println("No se puede eliminar: el ticket ya tiene una venta asociada");
                return;
            }

            limpiarFila(tickets, fila);
            guardarMatriz("tickets.txt", tickets);
        }


        // Registro de la venta

        // Imprime la factura completa en español
        public static void imprimirFactura(int filaVenta){
            int idTicketFactura = Integer.parseInt(ventas[filaVenta][1]);
            int filaTicket = buscarFila(tickets, idTicketFactura);

            System.out.println("\n========= FACTURA DE VENTA =========");
            System.out.println("Ferrocarriles de Antioquia");
            System.out.println("Venta #: " + ventas[filaVenta][0]);
            System.out.println("Ticket #: " + ventas[filaVenta][1]);

            if(filaTicket != -1){
                int idPasajero = Integer.parseInt(tickets[filaTicket][1]);
                int filaPasajero = buscarFila(usuarios, idPasajero);

                if(filaPasajero != -1){
                    System.out.println("Pasajero: " + usuarios[filaPasajero][1] + " " + usuarios[filaPasajero][2]);
                }
                System.out.println("Tren #: " + tickets[filaTicket][2] + " | Asiento: " + tickets[filaTicket][3]);
                System.out.println("Ruta: " + tickets[filaTicket][4] + " -> " + tickets[filaTicket][5]);
                System.out.println("Fecha y hora: " + tickets[filaTicket][6] + " - " + tickets[filaTicket][7]);
            }

            System.out.println("Precio unitario: $" + ventas[filaVenta][2]);
            System.out.println("Cantidad: " + ventas[filaVenta][3]);
            System.out.println("Total a pagar: $" + ventas[filaVenta][4]);
            System.out.println("====================================\n");
        }

        public static void crearVenta(){

            int fila = buscarFilaLibre(ventas);

            if(fila == -1){
                System.out.println("No hay espacio para más ventas");
                return;
            }

            System.out.print("Id del ticket a vender: ");
            ticketIdVenta = leerEntero();
            int filaTicket = buscarFila(tickets, ticketIdVenta);

            if(filaTicket == -1){
                System.out.println("El ticket no existe, créelo primero");
                return;
            }

            // Evita vender el mismo ticket dos veces
            if(existeReferencia(ventas, 1, ticketIdVenta)){
                System.out.println("Este ticket ya fue vendido");
                return;
            }

            // El precio sale de la columna 8 del ticket
            precioVentaUnitario = Double.parseDouble(tickets[filaTicket][8]);

            System.out.print("Cantidad: ");
            cantidad = leerEntero();

            if(cantidad <= 0){
                System.out.println("La cantidad debe ser mayor a 0");
                return;
            }

            totalVenta = precioVentaUnitario * cantidad;

            contadorVentas++;
            ventaId = contadorVentas;

            ventas[fila][0] = String.valueOf(ventaId);
            ventas[fila][1] = String.valueOf(ticketIdVenta);
            ventas[fila][2] = String.valueOf(precioVentaUnitario);
            ventas[fila][3] = String.valueOf(cantidad);
            ventas[fila][4] = String.valueOf(totalVenta);

            guardarMatriz("ventas.txt", ventas);
            System.out.println("Venta registrada con id " + ventaId);
            imprimirFactura(fila);
        }

        public static void consultarVentas(){
            mostrarTodos(ventas, encabezadosVenta);
        }

        public static void consultarVentaPorId(){
            System.out.print("Indique el id de la venta a consultar: ");
            int fila = buscarFila(ventas, leerEntero());

            if(fila == -1){
                System.out.println("No se encontró ninguna venta con ese id");
            } else {
                imprimirFactura(fila);
            }
        }

        // Se puede cambiar la cantidad y el total se recalcula
        public static void actualizarVenta(){

            System.out.print("Ingrese el id de la venta a actualizar: ");
            ventaId = leerEntero();

            int fila = buscarFila(ventas, ventaId);

            if(fila == -1){
                System.out.println("No se encontró la venta");
                return;
            }

            System.out.print("Nueva cantidad: ");
            cantidad = leerEntero();

            if(cantidad <= 0){
                System.out.println("La cantidad debe ser mayor a 0");
                return;
            }

            precioVentaUnitario = Double.parseDouble(ventas[fila][2]);
            totalVenta = precioVentaUnitario * cantidad;

            ventas[fila][3] = String.valueOf(cantidad);
            ventas[fila][4] = String.valueOf(totalVenta);

            guardarMatriz("ventas.txt", ventas);
            System.out.println("Venta actualizada");
            imprimirFactura(fila);
        }

        public static void eliminarVenta(){
            System.out.print("Indique el id de la venta a eliminar: ");
            int idEliminar = leerEntero();
            int fila = buscarFila(ventas, idEliminar);

            if(fila == -1){
                System.out.println("No se encontró ninguna venta con ese id");
                return;
            }

            limpiarFila(ventas, fila);
            guardarMatriz("ventas.txt", ventas);
        }


        // Método de la App

        public static void iniciarApp(){

            int opcion;

            do{
                System.out.println("===== FERROCARRILES DE ANTIOQUIA =====");
                System.out.println("1. Registrar cajero");
                System.out.println("2. Iniciar sesión");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = leerEntero();

                switch (opcion){
                    case 1:
                        crearCajero();
                        break;
                    case 2:
                        if(login()){
                            menuApp();
                        }
                        break;
                    case 0:
                        System.out.println("Hasta pronto");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

            } while(opcion != 0);
        }

        // Inicio de sesión

        public static boolean login(){

            System.out.print("Id del cajero: ");
            id = leerEntero();
            password = leerTexto("Contraseña: ");

            int fila = buscarFila(cajeros, id);

            if(fila != -1 && cajeros[fila][4].equals(password)){
                System.out.println("Bienvenido " + cajeros[fila][1] + " " + cajeros[fila][2]);
                return true;
            }

            System.out.println("Id o contraseña incorrectos");
            return false;
        }

        // Menú principal (panel del cajero)

        public static void menuApp(){

            int opcion;

            do{
                System.out.println("===== PANEL PRINCIPAL =====");
                System.out.println("1. Usuarios");
                System.out.println("2. Trenes");
                System.out.println("3. Tickets");
                System.out.println("4. Ventas");
                System.out.println("0. Cerrar sesión");
                System.out.print("Seleccione una opción: ");
                opcion = leerEntero();

                switch (opcion){
                    case 1:
                        menuUsuario();
                        break;
                    case 2:
                        menuTren();
                        break;
                    case 3:
                        menuTicket();
                        break;
                    case 4:
                        menuVenta();
                        break;
                    case 0:
                        System.out.println("Sesión cerrada");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

            } while(opcion != 0);
        }

        // Opciones que se repiten en los menús de cada entidad
        public static void mostrarMenuCrud(String titulo){
            System.out.println("===== MENÚ " + titulo + " =====");
            System.out.println("1. Registrar");
            System.out.println("2. Actualizar");
            System.out.println("3. Consultar por id");
            System.out.println("4. Consultar todos");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
        }

        // Menú de Usuario

        public static void menuUsuario(){

            int opcion;

            do{
                mostrarMenuCrud("USUARIOS");
                opcion = leerEntero();

                switch (opcion){
                    case 1:
                        crearUsuario();
                        break;
                    case 2:
                        actualizarUsuario();
                        break;
                    case 3:
                        consultarUsuarioPorId();
                        break;
                    case 4:
                        consultarUsuarios();
                        break;
                    case 5:
                        eliminarUsuario();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

            } while(opcion != 0);
        }

        // Menú de Tren

        public static void menuTren(){

            int opcion;

            do{
                mostrarMenuCrud("TRENES");
                opcion = leerEntero();

                switch (opcion){
                    case 1:
                        crearTren();
                        break;
                    case 2:
                        actualizarTren();
                        break;
                    case 3:
                        consultarTrenPorId();
                        break;
                    case 4:
                        consultarTrenes();
                        break;
                    case 5:
                        eliminarTren();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

            } while(opcion != 0);
        }

        // Menú tickets

        public static void menuTicket(){

            int opcion;

            do{
                mostrarMenuCrud("TICKETS");
                opcion = leerEntero();

                switch (opcion){
                    case 1:
                        crearTicket();
                        break;
                    case 2:
                        actualizarTicket();
                        break;
                    case 3:
                        consultarTicketPorId();
                        break;
                    case 4:
                        consultarTickets();
                        break;
                    case 5:
                        eliminarTicket();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

            } while(opcion != 0);
        }

        // Menú venta

        public static void menuVenta(){

            int opcion;

            do{
                mostrarMenuCrud("VENTAS");
                opcion = leerEntero();

                switch (opcion){
                    case 1:
                        crearVenta();
                        break;
                    case 2:
                        actualizarVenta();
                        break;
                    case 3:
                        consultarVentaPorId();
                        break;
                    case 4:
                        consultarVentas();
                        break;
                    case 5:
                        eliminarVenta();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida");
                }

            } while(opcion != 0);
        }

    }