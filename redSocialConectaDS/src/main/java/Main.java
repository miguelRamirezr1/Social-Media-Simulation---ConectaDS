import java.util.Scanner;

public class Main {
    
    private static SocialNetworkSystem system;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        system = new SocialNetworkSystem();
        scanner = new Scanner(System.in);
        
        System.out.println("RED SOCIAL CONECTA-DS");

        cargarDatosIniciales();
        mostrarMenuInteractivo();
        scanner.close();
    }
    
    //Carga los datos iniciales desde archivos CSV
    private static void cargarDatosIniciales() {
        System.out.println("CARGANDO DATOS INICIALES");

        System.out.println("Cargando perfiles...");
        int perfilesCargados = system.cargarPerfilesDesdeArchivo("/home/bee/Documents/University/UPB/estructuraDyA/CuartoParcial/IdeaProject/redSocialConectaDS/src/main/resources/profiles.csv");
        
        if (perfilesCargados > 0) {
            System.out.println(perfilesCargados + " perfiles cargados exitosamente\n");
            
            System.out.println("Cargando conexiones de amistad...");
            int conexionesCargadas = system.cargarConexionesDesdeArchivo("/home/bee/Documents/University/UPB/estructuraDyA/CuartoParcial/IdeaProject/redSocialConectaDS/src/main/resources/connections.csv");
            
            if (conexionesCargadas > 0) {
                System.out.println(conexionesCargadas + " conexiones establecidas exitosamente\n");
            } else {
                System.out.println("No se encontró el archivo connections.csv\n");
            }
        } else {
            System.out.println("No se encontró el archivo profiles.csv");
            System.out.println("  El sistema iniciará vacío.\n");
        }
    }
    
    //menú interactivo
    private static void mostrarMenuInteractivo() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("MENÚ PRINCIPAL");
            System.out.println("1. Crear perfil");
            System.out.println("2. Buscar perfil");
            System.out.println("3. Establecer amistad");
            System.out.println("4. Generar sugerencias");
            System.out.println("5. Verificar conexión");
            System.out.println("6. Mostrar estadísticas");
            System.out.println("7. Cargar perfiles desde archivo");
            System.out.println("8. Cargar conexiones desde archivo");
            System.out.println("9. Visualizar árbol de usuario");
            System.out.println("0. Salir");
            System.out.print("\nSeleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    crearPerfilInteractivo();
                    break;
                case 2:
                    buscarPerfilInteractivo();
                    break;
                case 3:
                    establecerAmistadInteractivo();
                    break;
                case 4:
                    generarSugerenciasInteractivo();
                    break;
                case 5:
                    verificarConexionInteractivo();
                    break;
                case 6:
                    system.mostrarEstadisticas();
                    break;
                case 7:
                    cargarPerfilesInteractivo();
                    break;
                case 8:
                    cargarConexionesInteractivo();
                    break;
                case 9:
                    visualizarArbolInteractivo();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\nGracias por usar Conecta-DS!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }
    
    // Métodos interactivos para el menú
    
    private static void crearPerfilInteractivo() {
        System.out.print("ID de usuario: ");
        String id = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Género (M/F): ");
        String genero = scanner.nextLine().toUpperCase();
        
        system.crearPerfil(id, nombre, edad, genero);
    }
    
    private static void buscarPerfilInteractivo() {
        System.out.print("ID de usuario a buscar: ");
        String id = scanner.nextLine();
        system.mostrarPerfil(id);
    }
    
    private static void establecerAmistadInteractivo() {
        System.out.print("ID del primer usuario: ");
        String id1 = scanner.nextLine();
        System.out.print("ID del segundo usuario: ");
        String id2 = scanner.nextLine();
        System.out.print("Calidad de amistad (1-5): ");
        int calidad = scanner.nextInt();
        scanner.nextLine();
        
        system.generarLazo(id1, id2, calidad);
    }
    
    private static void generarSugerenciasInteractivo() {
        System.out.print("ID del usuario: ");
        String id = scanner.nextLine();
        System.out.print("¿Aplicar filtros? (S/N): ");
        String aplicarFiltros = scanner.nextLine();
        
        if (aplicarFiltros.equalsIgnoreCase("S")) {
            System.out.print("Filtro de género (M/F o vacío para ninguno): ");
            String genero = scanner.nextLine();
            System.out.print("Edad mínima (-1 para sin filtro): ");
            int minEdad = scanner.nextInt();
            System.out.print("Edad máxima (-1 para sin filtro): ");
            int maxEdad = scanner.nextInt();
            scanner.nextLine();
            
            system.generarSugerenciasConFiltros(id, 
                genero.isEmpty() ? null : genero, minEdad, maxEdad);
        } else {
            System.out.print("Número de sugerencias a mostrar: ");
            int topN = scanner.nextInt();
            scanner.nextLine();
            system.generarSugerencias(id, topN);
        }
    }
    
    private static void verificarConexionInteractivo() {
        System.out.print("ID del primer usuario: ");
        String id1 = scanner.nextLine();
        System.out.print("ID del segundo usuario: ");
        String id2 = scanner.nextLine();
        
        system.verificarConexion(id1, id2);
    }
    
    private static void cargarPerfilesInteractivo() {
        System.out.print("Nombre del archivo de perfiles: ");
        String archivo = scanner.nextLine();
        system.cargarPerfilesDesdeArchivo(archivo);
    }
    
    private static void cargarConexionesInteractivo() {
        System.out.print("Nombre del archivo de conexiones: ");
        String archivo = scanner.nextLine();
        system.cargarConexionesDesdeArchivo(archivo);
    }
    
    private static void visualizarArbolInteractivo() {
        System.out.print("ID del usuario: ");
        String id = scanner.nextLine();
        system.visualizarArbol(id);
    }
}
