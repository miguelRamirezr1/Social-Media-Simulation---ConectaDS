import java.util.HashMap;
import java.util.Map;

public class Profile {
    private String userID;
    private String fullName;
    private int age;
    private String gender; // "M" o "F"
    private Map<String, Integer> friendsList; // FriendID -> Calidad (1-5)
    
    public Profile(String userID, String fullName, int age, String gender) {
        this.userID = userID;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.friendsList = new HashMap<>();
    }
    
    public void addFriend(String friendID, int quality) {
        if (quality >= 1 && quality <= 5) {
            friendsList.put(friendID, quality);
        } else {
            throw new IllegalArgumentException("La calidad de amistad debe estar entre 1 y 5");
        }
    }
    
    public int getFriendshipQuality(String friendID) {
        return friendsList.getOrDefault(friendID, 0);
    }
    public boolean isFriend(String friendID) {
        return friendsList.containsKey(friendID);
    }
    public String getUserID() {
        return userID;
    }
    public String getFullName() {
        return fullName;
    }
    public int getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public Map<String, Integer> getFriendsList() {
        return new HashMap<>(friendsList); // Retorna una copia para evitar modificaciones externas
    }
    
    @Override
    public String toString() {
        return String.format("Profile[ID=%s, Name=%s, Age=%d, Gender=%s, Friends=%d]",
                userID, fullName, age, gender, friendsList.size());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Profile profile = (Profile) obj;
        return userID.equals(profile.userID);
    }
    
    @Override
    public int hashCode() {
        return userID.hashCode();
    }
}
