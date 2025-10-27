import java.util.*;

public class FriendSuggestionEngine {
    
    private ProfileHashTable profileTable;
    private FriendshipGraph connectionGraph;
    
    public FriendSuggestionEngine(ProfileHashTable profileTable, FriendshipGraph connectionGraph) {
        this.profileTable = profileTable;
        this.connectionGraph = connectionGraph;
    }
    
    public List<FriendSuggestion> sugerirAmigos(String userID, String genderFilter,
                                                 int minAge, int maxAge) {
        Profile userProfile = profileTable.search(userID);
        if (userProfile == null) {
            System.out.println("Usuario no encontrado: " + userID);
            return new ArrayList<>();
        }
        
        // Max-Priority Queue para las sugerencias
        PriorityQueue<FriendSuggestion> suggestionQueue = new PriorityQueue<>();
        
        // Set para evitar sugerencias duplicadas
        Set<String> processedUsers = new HashSet<>();
        processedUsers.add(userID); // No sugerir al mismo usuario
        
        // Añade amigos directos al set de procesados (no sugeridos)
        Map<String, Integer> directFriends = userProfile.getFriendsList();
        processedUsers.addAll(directFriends.keySet());
        
        System.out.println("\nGenerando sugerencias para " + userProfile.getFullName());
        System.out.println("Amigos directos: " + directFriends.size());
        
        // Itera sobre todos los amigos directos del usuario
        for (Map.Entry<String, Integer> friendEntry : directFriends.entrySet()) {
            String friendID = friendEntry.getKey();
            int friendshipQuality = friendEntry.getValue(); // Q = calidad(X, A)
            
            Profile friendProfile = profileTable.search(friendID);
            if (friendProfile == null) continue;
            
            // Iterar sobre los amigos del amigo
            Map<String, Integer> friendsOfFriend = friendProfile.getFriendsList();
            
            for (String fofID : friendsOfFriend.keySet()) {
                // Si B no es X y B no es amigo directo de X
                if (!processedUsers.contains(fofID)) {
                    Profile fofProfile = profileTable.search(fofID);
                    if (fofProfile == null) continue;
                    
                    // Aplicar filtros
                    if (applyFilters(fofProfile, genderFilter, minAge, maxAge)) {
                        // Verificar conectividad con el grafo
                        if (connectionGraph.estaConectados(userID, fofID)) {
                            FriendSuggestion suggestion = new FriendSuggestion(
                                fofProfile, 
                                friendshipQuality,  // Prioridad basada en X-A, no A-B
                                friendProfile.getFullName()
                            );
                            
                            // Verificar si ya existe una sugerencia para este usuario
                            boolean found = false;
                            for (FriendSuggestion existing : suggestionQueue) {
                                if (existing.getSuggestedProfile().getUserID().equals(fofID)) {
                                    // Si existe, actualizar si esta tiene mayor prioridad
                                    if (friendshipQuality > existing.getPriority()) {
                                        suggestionQueue.remove(existing);
                                        suggestionQueue.add(suggestion);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                suggestionQueue.add(suggestion);
                            }
                            processedUsers.add(fofID); // Marcar como procesado
                        }
                    }
                }
            }
        }
        
        // Convertir la cola de prioridad a lista ordenada
        List<FriendSuggestion> sortedSuggestions = new ArrayList<>();
        while (!suggestionQueue.isEmpty()) {
            sortedSuggestions.add(suggestionQueue.poll());
        }
        
        System.out.println("Total de sugerencias generadas: " + sortedSuggestions.size());
        
        // Ordenamiento por prioridad descendente, luego alfabético
        Collections.sort(sortedSuggestions, new Comparator<FriendSuggestion>() {
            @Override
            public int compare(FriendSuggestion s1, FriendSuggestion s2) {
                // Primero por prioridad (descendente)
                if (s1.getPriority() != s2.getPriority()) {
                    return s2.getPriority() - s1.getPriority();
                }
                // Luego alfabéticamente dentro de la misma prioridad
                return s1.getSuggestedProfile().getFullName().compareTo(
                       s2.getSuggestedProfile().getFullName());
            }
        });
        
        return sortedSuggestions;
    }
    
    private boolean applyFilters(Profile profile, String genderFilter, int minAge, int maxAge) {
        // Filtro de género
        if (genderFilter != null && !genderFilter.isEmpty()) {
            if (!profile.getGender().equalsIgnoreCase(genderFilter)) {
                return false;
            }
        }
        // Filtro de edad mínima
        if (minAge > 0 && profile.getAge() < minAge) {
            return false;
        }
        // Filtro de edad máxima
        if (maxAge > 0 && profile.getAge() > maxAge) {
            return false;
        }
        return true;
    }
    
    public void mostrarTopSugerencias(String userID, int topN) {
        List<FriendSuggestion> suggestions = sugerirAmigos(userID, null, -1, -1);
        
        System.out.println("TOP " + topN + " SUGERENCIAS DE AMIGOS");

        int count = 0;
        for (FriendSuggestion suggestion : suggestions) {
            if (count >= topN) break;
            
            Profile profile = suggestion.getSuggestedProfile();
            String stars = "★".repeat(suggestion.getPriority()) + "☆".repeat(5 - suggestion.getPriority());
            
            System.out.printf("%d. %-20s | %s | A través de: %-15s\n",
                    count + 1,
                    profile.getFullName(),
                    stars,
                    suggestion.getThroughFriend());
            System.out.printf("Edad: %d | Género: %s | ID: %-10s\n",
                    profile.getAge(),
                    profile.getGender(),
                    profile.getUserID());
            
            if (count < topN - 1 && count < suggestions.size() - 1) {
            }
            count++;
        }
        if (count == 0) {
            System.out.println("No hay sugerencias disponibles");
        }
    }
    
    public void mostrarSugerenciasConFiltros(String userID, String genderFilter, int minAge, int maxAge) {
        List<FriendSuggestion> suggestions = sugerirAmigos(userID, genderFilter, minAge, maxAge);
        
        System.out.println("\nSUGERENCIAS CON FILTROS");
        if (genderFilter != null) {
            System.out.println("Filtro de género: " + genderFilter);
        }
        if (minAge > 0) {
            System.out.println("Edad mínima: " + minAge);
        }
        if (maxAge > 0) {
            System.out.println("Edad máxima: " + maxAge);
        }
        System.out.println("Total de sugerencias: " + suggestions.size());

        for (int i = 0; i < suggestions.size(); i++) {
            FriendSuggestion suggestion = suggestions.get(i);
            System.out.println((i + 1) + ". " + suggestion);
        }
        
        if (suggestions.isEmpty()) {
            System.out.println("No hay sugerencias que cumplan con los filtros especificados.");
        }
    }
}
