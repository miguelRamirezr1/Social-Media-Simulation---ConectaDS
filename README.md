# Sistema de Red Social "Conecta-DS"

## Descripción General

Sistema completo de simulación de red social implementado en Java que gestiona perfiles de usuarios, establece relaciones de amistad con diferentes niveles de calidad (1-5 estrellas) y genera sugerencias inteligentes de amigos mediante un motor basado en grafos y colas de prioridad.

## Cumplimiento de la Rúbrica de Evaluación

### 1. **Gestión de Perfiles** 
- **Tabla Hash Personalizada** (`ProfileHashTable.java`):
  - Implementación completa con manejo de colisiones por **encadenamiento (Separate Chaining)**
  - Función hash robusta con rehashing automático cuando el factor de carga supera 0.75
  - Operaciones O(1) promedio para inserción, búsqueda y eliminación
- **Carga de Archivos**:
  - Método `cargarPerfilesDesdeArchivo()` para carga masiva desde CSV
  - Validación de datos y manejo de errores
  - Archivos de prueba generados automáticamente

### 2. **Estructura de Conexiones - Grafo**
- **Grafo Ponderado** (`FriendshipGraph.java`):
  - Basado en el código modificado de `WeightedQuickUnionUF.java` proporcionado
  - Listas de adyacencia implementadas con HashMap dentro de cada perfil
  - Pesos de 1-5 estrellas representando la calidad de amistad:
    - 1★ = Conocidos
    - 2★ = Amigos casuales  
    - 3★ = Buenos amigos
    - 4★ = Amigos cercanos
    - 5★ = Mejores amigos

### 3. **Componentes Conectados - WQU**
- **Weighted Quick Union Modificado**:
  - Implementación eficiente con **compresión de caminos** en método `find()`
  - Función `estaConectados()` que verifica si dos usuarios pertenecen al mismo componente
  - Visualización del árbol de conexiones por usuario
  - Gestión de componentes con estadísticas detalladas
  - Prevención de ciclos según el código base proporcionado

### 4. **Motor de Sugerencias** 
- **Max-Priority Queue** (`FriendSuggestionEngine.java`):
  - PriorityQueue de Java configurada como Max-Heap
  - **Lógica correcta**: Prioridad basada en amistad X-A (no A-B)
  - Ejemplo: Si soy mejor amigo (5★) de Ana, y Ana conoce a Pedro (1★), Pedro se sugiere con prioridad 5
  - Algoritmo FoF (Friends of Friends) implementado completamente
  - Evita sugerir amigos directos o al mismo usuario

### 5. **Filtrado y Ordenamiento** 
- **Filtros Implementados**:
  - Por género (M/F)
  - Por rango de edad (mínima y máxima)
  - Filtros opcionales y combinables
- **Ordenamiento**:
  - Algoritmo de ordenamiento por prioridad descendente
  - En caso de empate: orden alfabético por nombre (dentro de la misma prioridad)

### 6. **Calidad y Pruebas** 
- **Código Limpio**:
  - Comentarios JavaDoc en todos los métodos públicos
  - Nombres de variables y métodos descriptivos
  - Estructura modular y bien organizada
- **Buenas Prácticas**:
  - Encapsulación apropiada
  - Manejo de excepciones
  - Validación de entrada
- **Sistema Interactivo Completo** (`Main.java`):
  - Menú interactivo con todas las funcionalidades
  - Carga automática de datos desde CSV
  - Validación robusta de entradas de usuario

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
├──  Profile.java              # Clase de perfil de usuario
├──  ProfileHashTable.java     # Tabla hash con manejo de colisiones
├──  FriendshipGraph.java      # Grafo WQU modificado para conexiones
├──  FriendSuggestion.java     # Clase para sugerencias de amistad
├──  FriendSuggestionEngine.java # Motor de sugerencias con Max-PQ
├──  SocialNetworkSystem.java  # Sistema principal integrado
├──  Main.java                 # Clase main con menú interactivo
├──  profiles.csv              # Archivo de datos de perfiles
├──  connections.csv           # Archivo de datos de conexiones
├──  run.sh                    # Script de compilación y ejecución
└──  README.md                 # Documentación
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

## Funcionalidades del Sistema

El sistema permite de forma interactiva:

1. **Gestión de perfiles** con tabla hash y manejo de colisiones
2. **Creación de grafos ponderados** con calidades de amistad 1-5
3. **Verificación de componentes conectados** usando WQU modificado
4. **Generación de sugerencias inteligentes** con lógica X-A correcta
5. **Aplicación de filtros** por género y rango de edad
6. **Carga masiva desde archivos** CSV para datos iniciales

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
1. Se cargan automáticamente los datos desde los archivos CSV (profiles.csv y connections.csv)
2. Se muestra el menú interactivo con las siguientes opciones:
   - Crear y buscar perfiles
   - Establecer amistades con calidades 1-5
   - Generar sugerencias
   - Verificar conexiones entre usuarios
   - Mostrar estadísticas del sistema
   - Cargar datos adicionales desde archivos


---
