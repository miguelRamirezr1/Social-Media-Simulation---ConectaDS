
public class FriendSuggestion implements Comparable<FriendSuggestion> {
    private Profile suggestedProfile;
    private int priority; // Basada en la calidad de amistad X-A (no A-B)
    private String throughFriend; // ID del amigo a través del cual se sugiere
    
    public FriendSuggestion(Profile suggestedProfile, int priority, String throughFriend) {
        this.suggestedProfile = suggestedProfile;
        this.priority = priority;
        this.throughFriend = throughFriend;
    }
    
    @Override
    public int compareTo(FriendSuggestion other) {
        // Primero comparar por prioridad en orden descendente
        if (this.priority != other.priority) {
            return other.priority - this.priority; // Orden inverso para max heap
        }
        // En caso de empate, ordena alfabéticamente por nombre
        return this.suggestedProfile.getFullName().compareTo(
                other.suggestedProfile.getFullName());
    }
    
    public Profile getSuggestedProfile() {
        return suggestedProfile;
    }
    public int getPriority() {
        return priority;
    }
    public String getThroughFriend() {
        return throughFriend;
    }
    
    @Override
    public String toString() {
        return String.format("Sugerencia: %s (ID: %s) - Prioridad: %d - A través de: %s",
                suggestedProfile.getFullName(),
                suggestedProfile.getUserID(),
                priority,
                throughFriend);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FriendSuggestion that = (FriendSuggestion) obj;
        return suggestedProfile.getUserID().equals(that.suggestedProfile.getUserID());
    }
    
    @Override
    public int hashCode() {
        return suggestedProfile.getUserID().hashCode();
    }
}
