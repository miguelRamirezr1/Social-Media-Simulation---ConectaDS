# Sistema de Red Social "ConectaDS"
### 1. **Gestión de Perfiles**
- **Tabla Hash Personalizada** (`ProfileHashTable.java`):
  - Implementación con manejo de colisiones por **encadenamiento (Separate Chaining)**
  - Función hash con rehashing automático cuando el factor de carga supera 0.75
  - Operaciones O(1) promedio para inserción, búsqueda y eliminación
- **Carga de Archivos**:
  - Método `cargarPerfilesDesdeArchivo()` para carga masiva desde CSV
  - Validación de datos y manejo de errores
  - Archivos de prueba generados automáticamente

### 2. **Estructura de Conexiones - Grafo**
- **Grafo Ponderado** (`FriendshipGraph.java`):
  - Basado en el código modificado de `WeightedQuickUnionUF.java`
  - Listas de adyacencia implementadas con HashMap dentro de cada perfil
  - Pesos de 1-5 estrellas representando la calidad de amistad:
    - 1★ = Conocidos
    - 2★ = Amigos casuales  
    - 3★ = Buenos amigos
    - 4★ = Amigos cercanos
    - 5★ = Mejores amigos

### 3. **Componentes Conectados - WQU**
- **Weighted Quick Union Modificado**:
  - Implementación con **compresión de caminos** en método `find()`
  - Función `estaConectados()` que verifica si dos usuarios pertenecen al mismo componente
  - Visualización del árbol de conexiones por usuario
  - Gestión de componentes con estadísticas
  - Prevención de ciclos

### 4. **Motor de Sugerencias**
- **Max-Priority Queue** (`FriendSuggestionEngine.java`):
  - PriorityQueue de Java configurada como Max-Heap
  - Prioridad basada en amistad X-A (no A-B)
  - Algoritmo FoF (Friends of Friends) implementado completamente
  - Evita sugerir amigos directos o al mismo usuario

### 5. **Filtrado y Ordenamiento**
- **Filtros**:
  - Por género (M/F)
  - Por rango de edad (mínima y máxima)
  - Filtros opcionales y combinables
- **Ordenamiento**:
  - Algoritmo de ordenamiento por prioridad descendente
  - En caso de empate: orden alfabético por nombre dentro de la misma prioridad

## Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    SocialNetworkSystem                   │
│                    (Clase Principal)                     │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┼────────────┬───────────────┐
        ▼            ▼            ▼               ▼
┌──────────┐ ┌──────────┐ ┌──────────────┐ ┌─────────────┐
│ Profile  │ │HashTable │ │FriendshipGraph│ │Suggestion  │
│  Class   │ │          │ │    (WQU)      │ │  Engine     │
└──────────┘ └──────────┘ └──────────────┘ └─────────────┘
```

## Estructura de Archivos

```
Sistema Red Social
├── Profile.java              # Clase de perfil de usuario
├── ProfileHashTable.java     # Tabla hash con manejo de colisiones
├── FriendshipGraph.java      # Grafo WQU modificado para conexiones
├── FriendSuggestion.java     # Clase para sugerencias de amistad
├── FriendSuggestionEngine.java # Motor de sugerencias con Max-PQ
├── SocialNetworkSystem.java  # Sistema principal integrado
├── Main.java                 # Clase main con pruebas
└── README.md                 # Documentación
```

## Compilación y Ejecución

### Compilar todos los archivos:
```bash
javac *.java
```

### Ejecutar el programa:
```bash
java Main
```

## Funcionalidades Principales

### 1. Gestión de Perfiles
```java
// Crear perfil
system.crearPerfil("U001", "Ana García", 25, "F");

// Buscar perfil
Profile profile = system.buscarPerfil("U001");

// Cargar desde archivo CSV
system.cargarPerfilesDesdeArchivo("profiles.csv");
```

### 2. Establecer Amistades
```java
// Crear amistad con calidad 1-5
system.generarLazo("U001", "U002", 5); // Mejores amigos
```

### 3. Verificar Conexiones
```java
// Verificar si dos usuarios están conectados
system.verificarConexion("U001", "U002");
```

### 4. Generar Sugerencias
```java
// Sin filtros
system.generarSugerencias("U001", 10);

// Con filtros
system.generarSugerenciasConFiltros("U001", "F", 20, 30);
```

## Complejidad Algorítmica

| Operación | Complejidad | Estructura |
|-----------|------------|------------|
| Insertar perfil | O(1) promedio | Tabla Hash |
| Buscar perfil | O(1) promedio | Tabla Hash |
| Union | O(log n)* | WQU con compresión |
| Find | O(log n)* | WQU con compresión |
| estaConectados | O(log n)* | WQU con compresión |
| Generar sugerencias | O(n·m·log k) | Max-PQ |

*Casi O(1) con compresión de caminos

## Pruebas Incluidas

El sistema incluye una suite completa de pruebas que demuestran:

1. **Inserción y búsqueda en tabla hash** con manejo de colisiones
2. **Creación de grafos ponderados** con calidades 1-5
3. **Verificación de componentes conectados** usando WQU
4. **Generación de sugerencias priorizadas** con lógica X-A
5. **Aplicación de filtros** por género y edad
6. **Carga masiva desde archivos** CSV

## Formato de Archivos CSV

### profiles.csv
```csv
userID,fullName,age,gender
U001,Ana García,25,F
U002,Carlos López,30,M
```

### connections.csv
```csv
userID_A,userID_B,calidad
U001,U002,5
U001,U003,4
```

## Ejemplo de Uso

Al ejecutar el programa:
1. Se ejecutan automáticamente las 6 pruebas de validación
2. Se genera un conjunto de datos de ejemplo
3. Se muestra el menú interactivo para pruebas manuales
