import java.util.HashMap;
import java.util.Map;

public class FriendshipGraph {
    
    private Map<String, Integer> userIDToIndex;  // se mapea el UserID a índice numérico
    private Map<Integer, String> indexToUserID;  // mapeo inverso
    private int[] parent;                        // parent of i
    private int[] size;                          // número de elementos en el subárbol con raíz en i
    private int count;                           // número de componentes
    private int nextIndex;                       // próximo índice disponible
    private int capacity;                        // capacidad máxima actual
    
    public FriendshipGraph(int initialCapacity) {
        this.capacity = initialCapacity;
        this.userIDToIndex = new HashMap<>();
        this.indexToUserID = new HashMap<>();
        this.parent = new int[capacity];
        this.size = new int[capacity];
        this.count = 0;
        this.nextIndex = 0;
        
        // Inicializar arrays
        for (int i = 0; i < capacity; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }
    
    public FriendshipGraph() {
        this(1000);
    }
    
    public int registerUser(String userID) {
        if (userIDToIndex.containsKey(userID)) {
            return userIDToIndex.get(userID);
        }
        
        if (nextIndex >= capacity) {
            expandCapacity();
        }
        
        userIDToIndex.put(userID, nextIndex);
        indexToUserID.put(nextIndex, userID);
        count++;
        return nextIndex++;
    }
    
    private void expandCapacity() {
        int newCapacity = capacity * 2;
        int[] newParent = new int[newCapacity];
        int[] newSize = new int[newCapacity];
        
        // se copia datos existentes
        System.arraycopy(parent, 0, newParent, 0, capacity);
        System.arraycopy(size, 0, newSize, 0, capacity);
        
        // Inicializar nuevos espacios
        for (int i = capacity; i < newCapacity; i++) {
            newParent[i] = i;
            newSize[i] = 1;
        }
        parent = newParent;
        size = newSize;
        capacity = newCapacity;
        System.out.println("Capacidad del grafo expandida a: " + capacity);
    }
    
    public int getComponentCount() {
        return count;
    }
    
    public int find(int p) {
        validate(p);
        int root = p;
        
        // Encontrar la raíz
        while (root != parent[root]) {
            root = parent[root];
        }
        
        // Compresión de caminos: hacer que todos los nodos apunten directamente a la raíz
        while (p != root) {
            int next = parent[p];
            parent[p] = root;
            p = next;
        }
        
        return root;
    }
    
    public boolean estaConectados(String userID1, String userID2) {
        if (!userIDToIndex.containsKey(userID1) || !userIDToIndex.containsKey(userID2)) {
            return false;
        }
        
        int index1 = userIDToIndex.get(userID1);
        int index2 = userIDToIndex.get(userID2);
        
        return find(index1) == find(index2);
    }
    
    public void union(int p, int q) {
        int rootP = find(p);
        
        // Verificar si ya están en el mismo componente
        if (find(rootP) == find(q)) {
            return;
        }
        
        //verifica si q es descendiente de rootP
        int current = q;
        while (current != parent[current]) {
            if (current == rootP) {
                // si detecta un ciclo, no hacer la unión
                return;
            }
            current = parent[current];
        }
        
        // Unir la raíz de p directamente al nodo q, no a la raíz de q
        parent[rootP] = q;
        
        // Actualiza el tamaño del componente
        int rootQ = find(q);
        size[rootQ] += size[rootP];
        
        count--;
    }
    
    public void conectarUsuarios(String userID1, String userID2) {
        int index1 = registerUser(userID1);
        int index2 = registerUser(userID2);
        
        if (!estaConectados(userID1, userID2)) {
            union(index1, index2);
            System.out.println("Conexión establecida entre " + userID1 + " y " + userID2);
        }
    }
    
    private void validate(int p) {
        if (p < 0 || p >= nextIndex) {
            throw new IllegalArgumentException("Índice " + p + " no está entre 0 y " + (nextIndex - 1));
        }
    }
    
    public int getComponentSize(String userID) {
        if (!userIDToIndex.containsKey(userID)) {
            return 0;
        }
        
        int index = userIDToIndex.get(userID);
        int root = find(index);
        return size[root];
    }
    
    public void printComponentInfo() {
        System.out.println("\nInformación de Componentes Conectados");
        System.out.println("Número total de usuarios: " + nextIndex);
        System.out.println("Número de componentes: " + count);
        
        Map<Integer, Integer> componentSizes = new HashMap<>();
        for (int i = 0; i < nextIndex; i++) {
            int root = find(i);
            componentSizes.put(root, componentSizes.getOrDefault(root, 0) + 1);
        }
        
        System.out.println("Tamaños de componentes:");
        int componentNum = 1;
        for (Map.Entry<Integer, Integer> entry : componentSizes.entrySet()) {
            System.out.println("  Componente " + componentNum++ + ": " + entry.getValue() + " usuarios");
        }
    }

    public void visualizarArbolUsuario(String userID) {
        if (!userIDToIndex.containsKey(userID)) {
            System.out.println("Usuario no encontrado: " + userID);
            return;
        }
        
        int userIndex = userIDToIndex.get(userID);
        int root = find(userIndex);
        
        System.out.println("\nÁrbol de Conexiones para " + userID);
        System.out.println("Raíz del componente: " + indexToUserID.get(root));
        System.out.println("Tamaño del componente: " + size[root]);
        
        // Muestra el camino hasta la raíz
        System.out.print("Camino hasta la raíz: " + userID);
        int current = userIndex;
        while (parent[current] != current) {
            current = parent[current];
            System.out.print(" -> " + indexToUserID.get(current));
        }
    }
}
