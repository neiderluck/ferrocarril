package ejemploproyectofinal;

import java.util.Scanner;

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

    //Usuario cajero

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

        iniciarApp();

    }

    // Arrays y Matrices
    // Cada fila es un registro y la columna 0 siempre es el id.
    // Si el id de una fila es null, esa fila está vacía.

    static String[][] usuarios = new String[20][5];
    static String[][] cajeros = new String[5][5];
    static String[][] trenes = new String[3][3];      // la compañía tiene 3 trenes
    static String[][] tickets = new String[50][7];
    static String[][] ventas = new String[50][5];

    static String[] encabezadosUsuario = {"Id" , "Nombre" , "Apellido" , "Origen" , "Destino"};
    static String[] encabezadosCajero = {"Id" , "Nombre" , "Apellido" , "Rol" , "Password"};
    static String[] encabezadosTren = {"# Maquina" , "Modelo" , "Capacidad"};
    static String[] encabezadosTicket = {"Id" , "Usuario" , "Tren" , "Asiento" , "Origen" , "Destino" , "Precio"};
    static String[] encabezadosVenta = {"Id" , "Ticket" , "Precio" , "Cantidad" , "Total"};


    // Métodos de ayuda (los usan todas las entidades)

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

    // Eliminar: deja la fila vacía
    public static void eliminarPorId(String[][] matriz){
        System.out.print("Indique el id a eliminar: ");
        int fila = buscarFila(matriz, leerEntero());

        if(fila == -1){
            System.out.println("No se encontró ningún registro con ese id");
        } else {
            for(int j = 0; j < matriz[fila].length; j++){
                matriz[fila][j] = null;
            }
            System.out.println("Registro eliminado");
        }
    }

    // Menú para escoger uno de los tres destinos de la compañía
    public static String elegirDestino(){
        int opcion;

        do{
            System.out.println("Destino: 1. Santa Fe  2. Cisneros  3. Puerto Berrio");
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

        contadorUsuarios++;
        id = contadorUsuarios;

        System.out.print("Nombre: ");
        nombre = sc.nextLine();
        System.out.print("Apellido: ");
        apellido = sc.nextLine();
        System.out.print("Origen: ");
        origen = sc.nextLine();
        destino = elegirDestino();

        usuarios[fila][0] = String.valueOf(id);
        usuarios[fila][1] = nombre;
        usuarios[fila][2] = apellido;
        usuarios[fila][3] = origen;
        usuarios[fila][4] = destino;

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
                System.out.print("Actualizar nombre: ");
                usuarios[fila][1] = sc.nextLine();
                break;
            case 2:
                System.out.print("Actualizar apellido: ");
                usuarios[fila][2] = sc.nextLine();
                break;
            case 3:
                System.out.print("Actualizar origen: ");
                usuarios[fila][3] = sc.nextLine();
                break;
            case 4:
                usuarios[fila][4] = elegirDestino();
                break;
            default:
                System.out.println("Opción inválida");
                return;
        }

        System.out.println("Usuario actualizado");
    }

    public static void eliminarUsuario(){
        eliminarPorId(usuarios);
    }



    // Métodos del Tren

    public static void crearTren(){

        int fila = buscarFilaLibre(trenes);

        if(fila == -1){
            System.out.println("La compañía solo cuenta con 3 trenes");
            return;
        }

        System.out.print("Modelo: ");
        modelo = sc.nextLine();

        do{
            System.out.print("Capacidad: ");
            capacidad = leerEntero();
        } while(capacidad <= 0);

        contadorTrenes++;
        trenId = contadorTrenes;

        trenes[fila][0] = String.valueOf(trenId);
        trenes[fila][1] = modelo;
        trenes[fila][2] = String.valueOf(capacidad);

        System.out.println("Tren registrado con # de máquina " + trenId);
    }

    public static void consultarTrenes(){
        mostrarTodos(trenes, encabezadosTren);
    }

    public static void consultarTrenPorId(){
        mostrarPorId(trenes, encabezadosTren);
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
                System.out.print("Nuevo modelo: ");
                trenes[fila][1] = sc.nextLine();
                break;
            case 2:
                System.out.print("Nueva capacidad: ");
                trenes[fila][2] = String.valueOf(leerEntero());
                break;
            default:
                System.out.println("Opción inválida");
                return;
        }

        System.out.println("Tren actualizado");
    }

    public static void eliminarTren(){
        eliminarPorId(trenes);
    }


    //Metodos registro Cajero

    public static void crearCajero(){

        int fila = buscarFilaLibre(cajeros);

        if(fila == -1){
            System.out.println("No hay espacio para más cajeros");
            return;
        }

        contadorCajeros++;
        id = contadorCajeros;

        System.out.print("Nombre: ");
        nombre = sc.nextLine();
        System.out.print("Apellido: ");
        apellido = sc.nextLine();
        System.out.print("Rol: ");
        rol = sc.nextLine();
        System.out.print("Password: ");
        password = sc.nextLine();

        cajeros[fila][0] = String.valueOf(id);
        cajeros[fila][1] = nombre;
        cajeros[fila][2] = apellido;
        cajeros[fila][3] = rol;
        cajeros[fila][4] = password;

        System.out.println("Cajero registrado. Su id para iniciar sesión es " + id);
    }


    // Metodo de Creación del ticket

    // Revisa que el tren exista, que el asiento esté dentro de la capacidad
    // y que nadie más lo tenga en ese tren
    public static boolean asientoValido(int trenBuscado, int asiento){

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
            if(tickets[i][0] != null
                    && tickets[i][2].equals(String.valueOf(trenBuscado))
                    && tickets[i][3].equals(String.valueOf(asiento))){
                System.out.println("Ese asiento ya está ocupado");
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

        // Asiento
        System.out.print("Número de asiento: ");
        int asiento = leerEntero();

        if(!asientoValido(tren, asiento)){
            return;
        }
        silla = String.valueOf(asiento);

        // El origen y el destino se toman de los datos del usuario
        origenRuta = usuarios[filaUsuario][3];
        destinoRuta = usuarios[filaUsuario][4];

        System.out.print("Precio: ");
        precio = leerDecimal();

        contadorTickets++;
        ticketId = contadorTickets;

        tickets[fila][0] = String.valueOf(ticketId);
        tickets[fila][1] = String.valueOf(usuario);
        tickets[fila][2] = String.valueOf(tren);
        tickets[fila][3] = silla;
        tickets[fila][4] = origenRuta;
        tickets[fila][5] = destinoRuta;
        tickets[fila][6] = String.valueOf(precio);

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

        System.out.println("Seleccione el dato a actualizar 1. Asiento 2. Origen 3. Destino 4. Precio");
        int opcion = leerEntero();
        int nuevoAsiento;

        switch (opcion){
            case 1:
                System.out.print("Nuevo asiento: ");
                nuevoAsiento = leerEntero();
                if(!asientoValido(Integer.parseInt(tickets[fila][2]), nuevoAsiento)){
                    return;
                }
                tickets[fila][3] = String.valueOf(nuevoAsiento);
                break;
            case 2:
                System.out.print("Nuevo origen: ");
                tickets[fila][4] = sc.nextLine();
                break;
            case 3:
                tickets[fila][5] = elegirDestino();
                break;
            case 4:
                System.out.print("Nuevo precio: ");
                tickets[fila][6] = String.valueOf(leerDecimal());
                break;
            default:
                System.out.println("Opción inválida");
                return;
        }

        System.out.println("Ticket actualizado");
    }

    public static void eliminarTicket(){
        eliminarPorId(tickets);
    }


    // Registro de la venta

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

        // El precio sale del ticket
        precioVentaUnitario = Double.parseDouble(tickets[filaTicket][6]);

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

        System.out.println("Venta registrada con id " + ventaId + ". Total: " + totalVenta);
    }

    public static void consultarVentas(){
        mostrarTodos(ventas, encabezadosVenta);
    }

    public static void consultarVentaPorId(){
        mostrarPorId(ventas, encabezadosVenta);
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

        System.out.println("Venta actualizada. Nuevo total: " + totalVenta);
    }

    public static void eliminarVenta(){
        eliminarPorId(ventas);
    }


    // Metodo de la App

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

    // login

    public static boolean login(){

        System.out.print("Id del cajero: ");
        id = leerEntero();
        System.out.print("Password: ");
        password = sc.nextLine();

        int fila = buscarFila(cajeros, id);

        if(fila != -1 && cajeros[fila][4].equals(password)){
            System.out.println("Bienvenido " + cajeros[fila][1] + " " + cajeros[fila][2]);
            return true;
        }

        System.out.println("Id o password incorrectos");
        return false;
    }

    // menu de app (dashboard del cajero)

    public static void menuApp(){

        int opcion;

        do{
            System.out.println("===== DASHBOARD =====");
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

    // menu de Usuario

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

    // menu de Tren

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

    // menu tickets

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

    // menu venta

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
