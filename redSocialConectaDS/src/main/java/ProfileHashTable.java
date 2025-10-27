import java.util.LinkedList;

public class ProfileHashTable {
    
    private static class HashNode {
        String key;
        Profile value;
        
        public HashNode(String key, Profile value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private LinkedList<HashNode>[] bucketArray;
    private int numBuckets;
    private int size;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    public ProfileHashTable(int initialCapacity) {
        this.numBuckets = initialCapacity;
        this.bucketArray = new LinkedList[numBuckets];
        this.size = 0;
        
        for (int i = 0; i < numBuckets; i++) {
            bucketArray[i] = new LinkedList<>();
        }
    }
    
    public ProfileHashTable() {
        this(16);
    }
    
    private int getBucketIndex(String key) {
        int hashCode = key.hashCode();
        // Asegura que el índice sea positivo
        return Math.abs(hashCode) % numBuckets;
    }
    
    public void insert(Profile profile) {
        String key = profile.getUserID();
        int bucketIndex = getBucketIndex(key);
        LinkedList<HashNode> bucket = bucketArray[bucketIndex];
        
        // Verifica si el perfil ya existe y actualiza si es necesario
        for (HashNode node : bucket) {
            if (node.key.equals(key)) {
                node.value = profile;
                System.out.println("Perfil actualizado: " + key);
                return;
            }
        }
        
        // Agrega nuevo nodo al bucket
        bucket.add(new HashNode(key, profile));
        size++;
        System.out.println("Perfil insertado: " + key + " en bucket " + bucketIndex);
        
        // Verificar si es necesario rehashing
        if ((double) size / numBuckets >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
    }
    
    public Profile search(String userID) {
        int bucketIndex = getBucketIndex(userID);
        LinkedList<HashNode> bucket = bucketArray[bucketIndex];
        
        for (HashNode node : bucket) {
            if (node.key.equals(userID)) {
                return node.value;
            }
        }
        return null;
    }
    
    public boolean delete(String userID) {
        int bucketIndex = getBucketIndex(userID);
        LinkedList<HashNode> bucket = bucketArray[bucketIndex];
        
        for (HashNode node : bucket) {
            if (node.key.equals(userID)) {
                bucket.remove(node);
                size--;
                System.out.println("Perfil eliminado: " + userID);
                return true;
            }
        }
        return false;
    }
    
    private void rehash() {
        System.out.println("Rehashing... Tamaño anterior: " + numBuckets);
        
        LinkedList<HashNode>[] oldBucketArray = bucketArray;
        numBuckets = numBuckets * 2;
        size = 0;
        bucketArray = new LinkedList[numBuckets];
        
        for (int i = 0; i < numBuckets; i++) {
            bucketArray[i] = new LinkedList<>();
        }
        
        // Reinsertar todos los elementos
        for (LinkedList<HashNode> bucket : oldBucketArray) {
            for (HashNode node : bucket) {
                insert(node.value);
            }
        }
        
        System.out.println("Rehashing completado. Nuevo tamaño: " + numBuckets);
    }
    
    public LinkedList<Profile> getAllProfiles() {
        LinkedList<Profile> allProfiles = new LinkedList<>();
        
        for (LinkedList<HashNode> bucket : bucketArray) {
            for (HashNode node : bucket) {
                allProfiles.add(node.value);
            }
        }
        return allProfiles;
    }
    
    public int size() {
        return size;
    }

    public void printStatistics() {
        System.out.println("\nEstadísticas de la Tabla Hash");
        System.out.println("Número de buckets: " + numBuckets);
        System.out.println("Número de elementos: " + size);
        System.out.println("Factor de carga: " + String.format("%.2f", (double) size / numBuckets));
        
        int maxChainLength = 0;
        int nonEmptyBuckets = 0;
        
        for (LinkedList<HashNode> bucket : bucketArray) {
            if (!bucket.isEmpty()) {
                nonEmptyBuckets++;
                maxChainLength = Math.max(maxChainLength, bucket.size());
            }
        }
        
        System.out.println("Buckets ocupados: " + nonEmptyBuckets);
        System.out.println("Longitud máxima de cadena: " + maxChainLength);
    }
}
