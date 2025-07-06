# Desafío Técnico Ualá

[🎥 Ver demo de la app en YouTube](https://youtu.be/NYe0LkEfT1w?si=PIpbkc3UWYxaQEMu)

Esta es una aplicación de Android desarrollada en Kotlin como solución al desafío técnico propuesto. La aplicación permite a los usuarios explorar una lista de aproximadamente 200,000 ciudades, filtrarlas por nombre, marcarlas como favoritas y ver su ubicación en un mapa.  
Dado el gran volumen de datos, se optó por almacenar las ciudades localmente en una base de datos Room. Esto permite que las búsquedas sean rápidas, escalables y persistentes entre sesiones. Además, se utilizó la librería Paging 3 para cargar los resultados de forma paginada, evitando problemas de rendimiento y consumo de memoria. Los favoritos también se guardan localmente en Room, y los datos se cachean luego de la primera descarga, eliminando la necesidad de múltiples descargas.

## Enfoque de la Solución y Decisiones de Arquitectura

El requisito principal del desafío era manejar una lista muy grande de datos (200k+ ciudades) y permitir búsquedas por prefijo de manera rápida y eficiente. Un enfoque ingenuo de descargar y filtrar la lista completa en memoria resultaría en un alto consumo de RAM y una interfaz de usuario lenta o incluso ANRs.

Por lo tanto, la decisión arquitectónica clave fue implementar una estrategia de **persistencia local como única fuente de verdad**, utilizando las siguientes tecnologías y patrones:

### Room Database

- **Problema:** Cargar 200,000 objetos JSON en memoria para cada búsqueda es inviable.
- **Solución:** La aplicación realiza una descarga única del archivo `cities.json` la primera vez que se ejecuta. Estos datos se procesan y se insertan masivamente en una base de datos local Room.
- **Optimización:**
  - Se definió un índice en el campo `name` en la entidad de Room. Esto permite que las consultas del tipo `LIKE` sean mucho más eficientes, especialmente cuando se trabaja con grandes volúmenes de datos.
  - Se implementó una estrategia de inserción en **bloques de 200 registros** (*chunks*) durante la carga inicial de ciudades. Esto permite que Room procese los datos de forma más eficiente, reduce el uso de memoria y evita bloqueos o errores relacionados con operaciones masivas sobre la base de datos.
- **Ventajas:**
  - **Bajo Consumo de Memoria:** La app nunca mantiene la lista completa en RAM. Solo se cargan los datos visibles.
  - **Rendimiento de Búsqueda:** Se delega la lógica de filtrado por prefijo al motor de SQLite, optimizado con índices y ejecutado de forma paginada.
  - **Persistencia:** Los datos, incluyendo los favoritos, se almacenan localmente y persisten entre sesiones de la app.
  
### Jetpack Paging 3

- **Problema:** Aunque la búsqueda en la base de datos es rápida, una consulta podría devolver miles de resultados, lo que volvería a causar problemas de memoria si se cargan todos a la vez.
- **Solución:** Se utiliza la librería **Paging 3** para cargar los datos desde la base de datos en pequeñas "páginas" (ej. de 20 en 20) a medida que el usuario hace scroll.
- **Ventajas:**
    - **UI Fluida:** La lista (`LazyColumn`) es extremadamente eficiente, ya que solo maneja una pequeña cantidad de datos en un momento dado.
    - **Escalabilidad:** La solución funciona igual de bien incluso con muchos más elementos.

## Arquitectura Limpia y Flujo de Datos Reactivo

La aplicación sigue una arquitectura limpia moderna (**MVVM + Repository**).

- **Capas:**
    - **UI (Jetpack Compose):** Vistas declarativas y simples que solo reaccionan al estado.
    - **ViewModel:** Orquesta la lógica de la UI, mantiene el estado y se comunica con la capa de datos.
    - **Repository:** La única fuente de verdad para los ViewModels. Abstrae el origen de los datos (red, base de datos).
    - **Data:** Contiene las fuentes de datos (Room, Retrofit) y los mappers.
- **Flujo de Datos Reactivo (Kotlin Flow):**
    - La UI observa `StateFlow`s del ViewModel.
    - El ViewModel utiliza operadores avanzados como `combine` y `flatMapLatest` para reaccionar a los cambios en los filtros de búsqueda (texto y favoritos), pidiendo al repositorio una nueva fuente de datos paginada de forma eficiente.

## UI Responsive

La app adapta dinámicamente su diseño en función del espacio disponible en pantalla, utilizando un umbral de **600dp de ancho** para decidir si mostrar una o dos vistas simultáneamente. Esta decisión se basa en el **ancho real de la pantalla** en lugar de la orientación del dispositivo.

- En **pantallas angostas** (menores a 600dp, típicamente en portrait), muestra una única vista a la vez: lista, detalle o mapa.
- En **pantallas anchas** (mayores a 600dp, típicamente en landscape, tablets, foldables desplegados o modo pantalla dividida), se muestra simultáneamente la lista de ciudades y el mapa.

> Este enfoque basado en el ancho disponible ofrece mayor flexibilidad y mejor experiencia en dispositivos modernos, como teléfonos plegables o en modo ventana redimensionable, donde la orientación por sí sola no refleja correctamente el espacio útil.

## Integración con Wikipedia

Para enriquecer los detalles de cada ciudad, la app realiza una consulta en tiempo real a la API pública de Wikipedia al abrir el detalle. Se muestra una breve descripción (si existe), sin necesidad de almacenar esos datos localmente.

## Configuración y Buenas Prácticas

- **Base URLs configurables por entorno:**  
  Las URLs base para las APIs (ciudades y Wikipedia) se definen en el archivo `build.gradle` usando `buildConfigField`, permitiendo cambiar fácilmente los endpoints según el tipo de build (`debug`, `release`, etc.).

- **Clave de API protegida:**  
  La API Key de Google Maps se almacena en `local.properties` y **no está versionada en Git**, evitando su exposición pública. Esto permite mantener la seguridad y facilita la configuración del proyecto por parte de otros desarrolladores.

- **Persistencia eficiente:**  
  Se evita el consumo innecesario de red y RAM cacheando los datos localmente y utilizando paginación para cargar solo lo necesario.

- **Indexado en base de datos:**  
  Se definieron índices en campos clave (`name`) de la base de datos Room para optimizar la performance de las búsquedas.

- **Gestión segura de secretos y recursos sensibles:**  
  Se siguen buenas prácticas de exclusión en `.gitignore` para archivos como `local.properties`, y no se hardcodean claves o URLs sensibles en el código fuente.

## Navegación

La navegación se implementó con **Navigation Compose**. Las pantallas disponibles son:

- Lista de ciudades: `cityList`
- Detalle de ciudad: `detail/{cityJson}`
- Mapa: `map/{cityJson}`

## Tecnologías Utilizadas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose
- **Asincronía:** Corrutinas y Flow
- **Inyección de Dependencias:** Hilt
- **Red:** Retrofit y `kotlinx.serialization`
- **Base de Datos:** Room
- **Paginación:** Jetpack Paging 3
- **Navegación:** Jetpack Navigation Compose
- **Testing:** JUnit, MockK, Turbine, Compose Test Rule

## El proyecto incluye:

- **Tests Unitarios:** Para los ViewModels y el Repositorio, verificando la lógica de negocio y el manejo de estado de forma aislada.
- **Tests de UI:** Para los componentes de Compose, verificando que la UI se renderiza correctamente y responde a las interacciones del usuario.

## 🔐 Acceso a claves y configuración privada

Por razones de seguridad, las claves sensibles (Apikey google maps) no están versionados en el repositorio
> 📩 Si necesitás acceder a estas configuraciones para compilar o probar la app, podés solicitar acceso escribiéndome directamente.