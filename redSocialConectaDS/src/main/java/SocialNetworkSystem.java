import java.io.*;
import java.util.*;

//Integra todos los componentes como perfiles, conexiones y sugerencias
//Permite la carga de datos desde archivos

public class SocialNetworkSystem {
    
    private ProfileHashTable profileTable;
    private FriendshipGraph connectionGraph;
    private FriendSuggestionEngine suggestionEngine;
    
    public SocialNetworkSystem() {
        this.profileTable = new ProfileHashTable(50);
        this.connectionGraph = new FriendshipGraph(100);
        this.suggestionEngine = new FriendSuggestionEngine(profileTable, connectionGraph);
        
        System.out.println("Sistema de Red Social iniciado exitosamente");
    }
    
    public boolean crearPerfil(String userID, String fullName, int age, String gender) {
        if (profileTable.search(userID) != null) {
            System.out.println("Error: El usuario " + userID + " ya existe.");
            return false;
        }
        
        Profile newProfile = new Profile(userID, fullName, age, gender);
        profileTable.insert(newProfile);
        connectionGraph.registerUser(userID);
        
        System.out.println("Perfil creado exitosamente: " + fullName + " (ID: " + userID + ")");
        return true;
    }
    
    public boolean generarLazo(String userID_A, String userID_B, int calidad) {
        if (calidad < 1 || calidad > 5) {
            System.out.println("Error: La calidad debe estar entre 1 y 5");
            return false;
        }
        
        Profile profileA = profileTable.search(userID_A);
        Profile profileB = profileTable.search(userID_B);
        
        if (profileA == null || profileB == null) {
            System.out.println("Error: Uno o ambos usuarios no existen");
            return false;
        }
        
        // Crear amistad en ambos sentidos
        profileA.addFriend(userID_B, calidad);
        profileB.addFriend(userID_A, calidad);
        
        // Actualizar el grafo
        connectionGraph.conectarUsuarios(userID_A, userID_B);
        
        String calidadStr = getCalidadString(calidad);
        System.out.println("Amistad establecida: " + profileA.getFullName() +
                          " <-> " + profileB.getFullName() +
                          " [" + calidadStr + "]");
        return true;
    }
    
    private String getCalidadString(int calidad) {
        String[] niveles = {
            "Conocidos",
            "Amigos casuales",
            "Buenos amigos",
            "Amigos cercanos",
            "Mejores amigos"
        };
        return niveles[calidad - 1];
    }
    
    public int cargarPerfilesDesdeArchivo(String filename) {
        int loadedCount = 0;
        System.out.println("\n> Iniciando carga de perfiles desde: " + filename);
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Saltar la línea de encabezados si existe
                if (isFirstLine) {
                    if (line.toLowerCase().contains("userid") || 
                        line.toLowerCase().contains("id")) {
                        isFirstLine = false;
                        continue;
                    }
                    isFirstLine = false;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String userID = parts[0].trim();
                    String fullName = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String gender = parts[3].trim().toUpperCase();
                    
                    if (crearPerfil(userID, fullName, age, gender)) {
                        loadedCount++;
                    }
                }
            }
            
            System.out.println("Carga completada: " + loadedCount + " perfiles cargados exitosamente");
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado - " + filename);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Formato de edad inválido en el archivo");
        }
        
        return loadedCount;
    }
    
    public int cargarConexionesDesdeArchivo(String filename) {
        int loadedCount = 0;
        System.out.println("\n> Iniciando carga de conexiones desde: " + filename);
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Saltar la línea de encabezados si existe
                if (isFirstLine) {
                    if (line.toLowerCase().contains("userid") || 
                        line.toLowerCase().contains("calidad")) {
                        isFirstLine = false;
                        continue;
                    }
                    isFirstLine = false;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String userID_A = parts[0].trim();
                    String userID_B = parts[1].trim();
                    int calidad = Integer.parseInt(parts[2].trim());
                    
                    if (generarLazo(userID_A, userID_B, calidad)) {
                        loadedCount++;
                    }
                }
            }
            
            System.out.println("Carga completada: " + loadedCount + " conexiones establecidas");
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado - " + filename);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Formato de calidad inválido en el archivo");
        }
        
        return loadedCount;
    }
    
    public void mostrarPerfil(String userID) {
        Profile profile = profileTable.search(userID);
        if (profile == null) {
            System.out.println("Usuario no encontrado: " + userID);
            return;
        }
        
        System.out.println("INFORMACIÓN DE PERFIL");
        System.out.printf("ID: %-34s \n", profile.getUserID());
        System.out.printf("Nombre: %-30s \n", profile.getFullName());
        System.out.printf("Edad: %-32d \n", profile.getAge());
        System.out.printf("Género: %-30s \n", profile.getGender());

        System.out.println("LISTA DE AMIGOS");

        Map<String, Integer> friends = profile.getFriendsList();
        if (friends.isEmpty()) {
            System.out.println("No tiene amigos registrados");
        } else {
            for (Map.Entry<String, Integer> entry : friends.entrySet()) {
                Profile friendProfile = profileTable.search(entry.getKey());
                if (friendProfile != null) {
                    String stars = "★".repeat(entry.getValue()) + "☆".repeat(5 - entry.getValue());
                    System.out.printf("• %-20s %s\n",
                            friendProfile.getFullName(), stars);
                }
            }
        }
    }
    
    public void verificarConexion(String userID1, String userID2) {
        boolean connected = connectionGraph.estaConectados(userID1, userID2);
        
        Profile profile1 = profileTable.search(userID1);
        Profile profile2 = profileTable.search(userID2);
        
        if (profile1 == null || profile2 == null) {
            System.out.println("Uno o ambos usuarios no existen.");
            return;
        }
        
        System.out.println("\nVerificacion de Conexion:");
        System.out.println("Usuario 1: " + profile1.getFullName());
        System.out.println("Usuario 2: " + profile2.getFullName());
        
        if (connected) {
            System.out.println("Estado: CONECTADOS");
            
            // Verificar si son amigos directos
            if (profile1.isFriend(userID2)) {
                int quality = profile1.getFriendshipQuality(userID2);
                System.out.println("Tipo: Amigos directos");
                System.out.println("Calidad de amistad: " + getCalidadString(quality));
            } else {
                System.out.println("Tipo: Conectados indirectamente (mismo componente)");
            }
            
            System.out.println("Tamaño del componente: " + 
                    connectionGraph.getComponentSize(userID1));
        } else {
            System.out.println("Estado: NO CONECTADOS");
            System.out.println("Los usuarios están en componentes diferentes");
        }
    }
    
    public void mostrarEstadisticas() {
        System.out.println("ESTADÍSTICAS DEL SISTEMA");
        System.out.println("Total de usuarios: " +
                String.format("%-20d", profileTable.size()));
        System.out.println("Componentes conectados: " +
                String.format("%-15d", connectionGraph.getComponentCount()));
        
        // Calcular estadísticas de amistades
        int totalFriendships = 0;
        int maxFriends = 0;
        String mostPopular = "";
        
        for (Profile profile : profileTable.getAllProfiles()) {
            int friendCount = profile.getFriendsList().size();
            totalFriendships += friendCount;
            if (friendCount > maxFriends) {
                maxFriends = friendCount;
                mostPopular = profile.getFullName();
            }
        }
        
        double avgFriends = profileTable.size() > 0 ? 
                (double) totalFriendships / profileTable.size() : 0;
        
        System.out.println("Promedio de amigos: " +
                String.format("%-19.2f", avgFriends));
        System.out.println("Usuario más popular: " +
                String.format("%-18s", mostPopular.length() > 18 ? 
                mostPopular.substring(0, 15) + "..." : mostPopular));
        System.out.println("Máximo de amigos: " +
                String.format("%-21d", maxFriends));

        profileTable.printStatistics();
        connectionGraph.printComponentInfo();
    }
    
    public void generarSugerencias(String userID, int topN) {
        suggestionEngine.mostrarTopSugerencias(userID, topN);
    }
    
    public void generarSugerenciasConFiltros(String userID, String genderFilter, int minAge, int maxAge) {
        suggestionEngine.mostrarSugerenciasConFiltros(userID, genderFilter, minAge, maxAge);
    }
    public void visualizarArbol(String userID) {
        connectionGraph.visualizarArbolUsuario(userID);
    }
}
