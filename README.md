
<!-- ================================================== -->
<!-- CARÁTULA INSTITUCIONAL -->
<!-- ================================================== -->

<div align="center">

# INSTITUTO POLITÉCNICO NACIONAL

### ESCUELA SUPERIOR DE CÓMPUTO

<br>

<img src="img/descarga.png" alt="Logo del IPN" width="130">

<br>

## Aplicaciones Moviles nativas

### TAREA 2.- ELEMENTOS BÁSICOS DE INTERFAZ DE USUARIO

<br><br>

**Alumno:**

Caballero Pérez Julio César 

**Grupo:** 7CV4

<br>

**Profesor(a):**

Gabriel Hurtado Avilés

<br>

**Fecha:** 25 de Septiembre del 2026

</div>
 
# Índice
 
1. [Objetivo](#1-objetivo)
2. [Descripción de la aplicación](#2-descripción-de-la-aplicación)
3. [Tecnologías utilizadas](#3-tecnologías-utilizadas)
4. [Estructura del repositorio](#4-estructura-del-repositorio)
5. [Instrucciones de compilación y ejecución](#5-instrucciones-de-compilación-y-ejecución)
6. [Tabla de equivalencias entre tecnologías](#6-tabla-de-equivalencias-entre-tecnologías)
7. [Capturas de pantalla](#7-capturas-de-pantalla)
8. [Reflexión final](#8-reflexión-final)
9. [Referencias](#9-referencias)
---
 
# 1. Objetivo
 
Construir un catálogo interactivo de elementos de interfaz de usuario e implementarlo en tres tecnologías distintas, para identificar los componentes básicos de una interfaz móvil, sus equivalencias entre plataformas y las diferencias entre los enfoques de construcción de interfaces.
 
---
 
# 2. Descripción de la aplicación
 
**Catálogo de elementos** Es una aplicación donde demuestra los componentes más comunes de una interfaz móvil. La misma aplicación se desarrolló tres veces, con un diseño y un comportamiento equivalentes en cada tecnología, para poder compararlas de forma directa.
 
Cada versión tiene una **pantalla principal** y **seis secciones**. Cada elemento se presenta en una tarjeta con tres partes: su **nombre**, una **explicación breve** de para qué sirve y una **demostración interactiva** que responde al usuario.
 
| Sección | Elementos que incluye |
|---|---|
| **1. Entrada de texto** | Campo simple con etiqueta, campo con validación y mensaje de error, contraseña con mostrar/ocultar, teclados numérico, de correo y de teléfono, campo multilínea con contador, campo con sugerencias y barra de búsqueda |
| **2. Botones y acciones** | Botón relleno, con contorno, de texto, tonal y elevado; botones de solo ícono y de ícono con texto; FAB normal y extendido; botón de alternancia; selector segmentado de una y de varias opciones; botón deshabilitado y botón en estado de carga |
| **3. Elementos de selección** | Casilla con estado indeterminado, botones de opción, interruptores, deslizador de valor único y de rango, lista desplegable, selector de fecha, selector de hora y chips de filtro |
| **4. Listas y colecciones** | Lista vertical de más de 15 elementos, cuadrícula, lista con encabezados de sección, detalle al tocar un elemento, deslizar para eliminar, jalar para actualizar, estado vacío con ilustración y pestañas con contenido deslizable |
| **5. Información y retroalimentación** | Textos con distintos estilos y énfasis, imagen local e imagen desde URL con cuatro modos de escalado, indicadores de progreso lineal y circular (determinados e indeterminados), toast, snackbar con acción, diálogo de confirmación, hoja inferior, tarjetas, separadores y badges |
| **6. Contenedores y estructura** | Distribución en fila, en columna y superpuesta, contenedor con desplazamiento vertical, barra superior con título y acciones, barra de navegación inferior, menú lateral y distribución con pesos proporcionales (y con restricciones en la versión de Views) |
 
### Características comunes a las tres versiones
 
- **Navegación:** Cuenta con una fila donde muestra las opciones de *Inicio* y las seis secciones, visible en todo momento. En cada sección, la flecha de la barra superior y el botón *atrás* del teléfono regresan a la pantalla principal.
- **Tema claro y oscuro:** Por defecto las aplicaciónes estaran en el tema que el sistema operativo tenga activo (modo claro o oscuro). Además, cuenta con un botón en la barra superior donde permite elegir las siguientes opciones *Según el sistema*, *Claro* u *Oscuro*.
- **Idioma:** Todos los textos de la interfaz y de la documentación están en español. El calendario, el reloj y los botones de los diálogos del sistema también se muestran en español, aunque el teléfono esté configurado en otro idioma.
- **Conexión entre secciones.** Se implementaron dos:
  1. **Sección 1 → Sección 4:** Un texto capturado en "*Entrada de texto*" se agrega al inicio de la lista de "*Listas y colecciones*", en su propio grupo "Del usuario".
  2. **Sección 3 ↔ Sección 5:** El deslizador de "*Elementos de selección*" cambia el tamaño de todos los textos de "*Información y retroalimentación*". El diálogo de confirmación de la Sección 5 lo restablece al 100 %, y el deslizador de la Sección 3 se actualiza también.
  Ambas conexiones funcionan mediante un **estado compartido**: un `ViewModel` en Android y un `ChangeNotifier` en Flutter.
---
 
# 3. Tecnologías utilizadas
 
### Android nativo con Jetpack Compose
 
Compose es el kit de herramientas moderno y **declarativo** de Android. La interfaz se describe con funciones de Kotlin anotadas con `@Composable`, y la pantalla se vuelve a dibujar automáticamente cuando cambia el estado.
 
- **Lenguaje:** Kotlin
- **Componentes:** Material 3 para Compose
- **Navegación:** Navigation Compose
- **Estado compartido:** `ViewModel` con estado observable de Compose
- **Imágenes desde URL:** Coil
### Android nativo con Views y XML
 
Es el enfoque **clásico e imperativo** de Android. La interfaz se define en archivos de diseño XML y el comportamiento se programa en Kotlin, que accede a las vistas mediante *View Binding*. Cada sección es un **Fragment**.
 
- **Lenguaje:** Kotlin + XML
- **Componentes:** Material Components for Android (Material 3)
- **Navegación:** Navigation Component con `nav_graph.xml`
- **Estado compartido:** `ViewModel` con `LiveData`
- **Listas:** `RecyclerView`, `ListAdapter`, `DiffUtil` y `ConcatAdapter`
- **Imágenes desde URL:** Coil
### Flutter
 
Es el SDK multiplataforma de Google. Usa también un enfoque **declarativo**: la interfaz se construye con un árbol de *widgets* escritos en Dart, y el motor de Flutter dibuja cada píxel sin depender de los componentes nativos de Android.
 
- **Lenguaje:** Dart
- **Componentes:** widgets de Material 3 incluidos en el SDK
- **Navegación:** `TabBar` en la estructura principal
- **Estado compartido:** `ChangeNotifier` distribuido con `InheritedNotifier`
- **Idioma:** paquete `flutter_localizations`
---
 
# 4. Estructura del repositorio
 
```
TAREA 2/
├── README.md                
├── APKs/                     ← aplicaciones listas para instalar
│   ├── CatalogoUI-Compose.apk
│   ├── CatalogoUI-Views.apk
│   └── CatalogoUI-Flutter.apk
├── docs/                     ← capturas de pantalla
│   ├── compose/
│   ├── views/
│   └── flutter/
├── tecnologia-compose/       ← proyecto de Android Studio (Jetpack Compose)
├── tecnologia-views/         ← proyecto de Android Studio (Views + XML)
└── tecnologia-flutter/       ← proyecto de Flutter
```
 
---
 
# 5. Instrucciones de compilación y ejecución
 
## 5.1 Requisitos
 
| Herramienta | Versión utilizada |
|---|---|
| Android Studio | [versión, 1.139.0] |
| Android SDK | API 37 (compilación) · API 24 mínima |
| Kotlin | Integrado en Android Gradle Plugin |
| Flutter SDK | 3.47.2 |
| Dart SDK |  3.13.2 |
| Dispositivo de prueba | [Xiaomi 2511FPC34G, Android 16] o emulador con API 24 o superior |
 
## 5.2 Instalar directamente los APK (sin compilar)
 
1. Descargar el APK deseado de la carpeta [`APKs/](./APKs) del repositorio.
2. Copialo al teléfono y abrelo desde el administrador de archivos.
3. Si el sistema lo solicita, permite **"Instalar apps desconocidas"** para la aplicación con la que se abrió el archivo.
## 5.3 Versión Jetpack Compose
 
**Ejecutar**
1. En Android Studio: **File → Open** → seleccionar la carpeta `tecnologia-compose`.
2. Esperar a que termine el **Gradle Sync**.
3. Conectar el teléfono con la **depuración USB** activada (o iniciar un emulador) y seleccionarlo en la barra superior.
4. Pulsar **Run ▶**.
**Generar el APK**
- **Build → Generate App Bundles or APKs → Generate APKs**
- Archivo resultante: `tecnologia-compose/app/build/outputs/apk/debug/app-debug.apk`
## 5.4 Versión Views + XML
 
**Ejecutar**
1. En Android Studio: **File → Open** → seleccionar la carpeta `tecnologia-views`.
2. Esperar a que termine el **Gradle Sync**.
3. Seleccionar el dispositivo y pulsar **Run ▶**.
**Generar el APK**
- **Build → Generate App Bundles or APKs → Generate APKs**
- Archivo resultante: `tecnologia-views/app/build/outputs/apk/debug/app-debug.apk`
## 5.5 Versión Flutter
 
**Preparación (solo la primera vez)**
1. Instalar los plugins **Flutter** y **Dart** en Android Studio (**Settings → Plugins**).
2. Verificar el entorno:
```bash
   flutter doctor
```
3. Si Flutter no detecta el SDK de Android:
```bash
   flutter config --android-sdk "C:\Users\<usuario>\AppData\Local\Android\Sdk"
   flutter doctor --android-licenses
```
 
**Ejecutar**
```bash
cd tecnologia-flutter
flutter pub get
flutter devices          # confirma que el teléfono aparece
flutter run
```
También se puede abrir la carpeta en Android Studio, elegir el teléfono en el selector de dispositivos y pulsar **Run ▶**.
 
**Generar el APK**
```bash
flutter build apk --release
```
- Archivo resultante: `tecnologia-flutter/build/app/outputs/flutter-apk/app-release.apk`
---
 
# 6. Tabla de equivalencias entre tecnologías
 
## 6.1 Enfoque general
 
| Aspecto | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Paradigma | Declarativo | Imperativo | Declarativo |
| Lenguaje | Kotlin | Kotlin + XML | Dart |
| Unidad de interfaz | Función `@Composable` | Layout XML + `Fragment` | `Widget` |
| Estado local | `remember { mutableStateOf() }` | Variables + `setText()`, `isChecked`… | `StatefulWidget` + `setState()` |
| Estado compartido | `ViewModel` | `ViewModel` + `LiveData` | `ChangeNotifier` + `InheritedNotifier` |
| Acceso a las vistas | No aplica (no hay vistas) | View Binding | No aplica (no hay vistas) |
| Pantallas de la app | Destinos de `NavHost` | Fragments en `nav_graph.xml` | Widgets de pantalla |
| Tema claro/oscuro | `MaterialTheme(darkTheme)` | `Theme.Material3.DayNight` + `AppCompatDelegate` | `theme`, `darkTheme` y `themeMode` |
 
## 6.2 Sección 1: Entrada de texto
 
| Elemento | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Campo de texto | `OutlinedTextField` | `TextInputLayout` + `TextInputEditText` | `TextField` |
| Validación y error | `isError` + `supportingText` | `setError()` + `helperText` | `errorText` + `helperText` |
| Contraseña | `PasswordVisualTransformation` | `endIconMode="password_toggle"` | `obscureText` |
| Tipo de teclado | `KeyboardOptions(keyboardType)` | `android:inputType` | `keyboardType` |
| Multilínea | `minLines` / `maxLines` | `inputType="textMultiLine"` | `minLines` / `maxLines` |
| Sugerencias | `DropdownMenu` + `TextField` | `MaterialAutoCompleteTextView` | `Autocomplete` |
| Barra de búsqueda | `OutlinedTextField` con ícono | `TextInputLayout` redondeado | `SearchBar` |
 
## 6.3 Sección 2: Botones y acciones
 
| Elemento | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Relleno / contorno / texto | `Button` / `OutlinedButton` / `TextButton` | `Button` con estilos `Widget.Material3.Button.*` | `FilledButton` / `OutlinedButton` / `TextButton` |
| Botón de ícono | `IconButton`, `FilledIconButton` | `Widget.Material3.Button.IconButton` | `IconButton`, `IconButton.filled` |
| FAB | `FloatingActionButton` | `FloatingActionButton` | `FloatingActionButton` |
| FAB extendido | `ExtendedFloatingActionButton` | `ExtendedFloatingActionButton` | `FloatingActionButton.extended` |
| Alternancia | `IconToggleButton` | `MaterialButton` con `checkable` | `IconButton` con `isSelected` |
| Segmentado | `SegmentedButton` | `MaterialButtonToggleGroup` | `SegmentedButton` |
| Deshabilitado | `enabled = false` | `isEnabled = false` | `onPressed: null` |
 
## 6.4 Sección 3: Elementos de selección
 
| Elemento | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Casilla indeterminada | `TriStateCheckbox` | `MaterialCheckBox` + `STATE_INDETERMINATE` | `Checkbox(tristate: true)` |
| Botones de opción | `RadioButton` + `selectableGroup` | `RadioGroup` + `MaterialRadioButton` | `RadioListTile` |
| Interruptor | `Switch` | `MaterialSwitch` | `Switch` / `SwitchListTile` |
| Deslizador | `Slider` | `Slider` | `Slider` |
| Deslizador de rango | `RangeSlider` | `RangeSlider` | `RangeSlider` |
| Lista desplegable | `DropdownMenu` | Exposed Dropdown Menu | `DropdownMenu` |
| Fecha / hora | `DatePickerDialog` / `TimePicker` | `MaterialDatePicker` / `MaterialTimePicker` | `showDatePicker()` / `showTimePicker()` |
| Chips de filtro | `FilterChip` | `Chip` en `ChipGroup` | `FilterChip` |
 
## 6.5 Sección 4: Listas y colecciones
 
| Elemento | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Lista vertical | `LazyColumn` | `RecyclerView` + `LinearLayoutManager` | `ListView` |
| Cuadrícula | `LazyVerticalGrid` | `RecyclerView` + `GridLayoutManager` | `GridView` / `SliverGrid` |
| Encabezados de sección | `stickyHeader` | Varios `viewType` en el Adapter | `SliverPersistentHeader` |
| Deslizar para eliminar | `SwipeToDismissBox` | `ItemTouchHelper` | `Dismissible` |
| Jalar para actualizar | `PullToRefreshBox` | `SwipeRefreshLayout` | `RefreshIndicator` |
| Pestañas deslizables | `TabRow` + `HorizontalPager` | `TabLayout` + `ViewPager2` | `TabBar` + `TabBarView` |
 
## 6.6 Sección 5: Información y retroalimentación
 
| Elemento | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Texto con estilos | `Text` + `AnnotatedString` | `TextView` + `SpannableString` | `Text.rich` + `TextSpan` |
| Imagen local | `Image(painterResource())` | `ImageView` | `Image.asset` |
| Imagen desde URL | `AsyncImage` (Coil) | `ImageView.load()` (Coil) | `Image.network` |
| Modo de escalado | `ContentScale` | `scaleType` | `BoxFit` |
| Progreso | `LinearProgressIndicator` / `CircularProgressIndicator` | `LinearProgressIndicator` / `CircularProgressIndicator` | `LinearProgressIndicator` / `CircularProgressIndicator` |
| Toast | `Toast` de Android | `Toast` de Android | No existe; se construyó con `Overlay` |
| Snackbar | `SnackbarHost` | `Snackbar` | `ScaffoldMessenger` + `SnackBar` |
| Diálogo | `AlertDialog` | `MaterialAlertDialogBuilder` | `showDialog()` + `AlertDialog` |
| Hoja inferior | `ModalBottomSheet` | `BottomSheetDialog` | `showModalBottomSheet()` |
| Badge | `BadgedBox` + `Badge` | `BadgeDrawable` | `Badge` |
 
## 6.7 Sección 6: Contenedores y estructura
 
| Elemento | Jetpack Compose | Views + XML | Flutter |
|---|---|---|---|
| Fila / columna | `Row` / `Column` | `LinearLayout` | `Row` / `Column` |
| Superpuesta | `Box` | `FrameLayout` | `Stack` |
| Desplazamiento | `Modifier.verticalScroll` | `NestedScrollView` | `SingleChildScrollView` |
| Barra superior | `TopAppBar` | `MaterialToolbar` | `AppBar` |
| Navegación inferior | `NavigationBar` | `BottomNavigationView` | `NavigationBar` |
| Menú lateral | `ModalNavigationDrawer` | `DrawerLayout` + `NavigationView` | `Drawer` |
| Pesos proporcionales | `Modifier.weight()` | `layout_weight` | `Expanded(flex)` |
| Restricciones | — | `ConstraintLayout` | — |
 
---
 
# 7. Capturas de pantalla
 
Las capturas se encuentran en la carpeta [`docs/`](./docs), organizadas por tecnología.
 
## Pantalla principal
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Inicio.jpeg" width="230"> | <img src="docs/views/Inicio.jpeg" width="230"> | <img src="docs/flutter/Inicio.jpeg" width="230"> |
 
## Sección 1. Entrada de texto
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Texto.jpeg" width="230"> | <img src="docs/views/Texto.jpeg" width="230"> | <img src="docs/flutter/Texto.jpeg" width="230"> |
 
## Sección 2. Botones y acciones
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Botones.jpeg" width="230"> | <img src="docs/views/Botones.jpeg" width="230"> | <img src="docs/flutter/Botones.jpeg" width="230"> |
 
## Sección 3. Elementos de selección
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Seleccion.jpeg" width="230"> | <img src="docs/views/Seleccion.jpeg" width="230"> | <img src="docs/flutter/Seleccion.jpeg" width="230"> |
 
## Sección 4. Listas y colecciones
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Listas.jpeg" width="230"> | <img src="docs/views/Listas.jpeg" width="230"> | <img src="docs/flutter/Listas.jpeg" width="230"> |
 
## Sección 5. Información y retroalimentación
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Informacion.jpeg" width="230"> | <img src="docs/views/Informacion.jpeg" width="230"> | <img src="docs/flutter/Informacion.jpeg" width="230"> |
 
## Sección 6. Contenedores y estructura
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Contenedores.jpeg" width="230"> | <img src="docs/views/Contenedores.jpeg" width="230"> | <img src="docs/flutter/Contenedores.jpeg" width="230"> |
 
## Tema oscuro
 
| Jetpack Compose | Views + XML | Flutter |
|:---:|:---:|:---:|
| <img src="docs/compose/Modo-Oscuro.jpeg" width="230"> | <img src="docs/views/Modo-oscuro.jpeg" width="230"> | <img src="docs/flutter/Modo-oscuro.jpeg" width="230"> |
 
## Conexión entre secciones
 
| Sección 1 →  | Sección 4 |
|:---:|:---:|
| <img src="docs/compose/Seccion1.jpeg" width="230"> |<img src="docs/compose/Seccion4.jpeg" width="230"> |
| El elemento agregado en *Entrada de texto*. |  aparece en el grupo "Del usuario". |
 
---
 
# 8. Reflexión final
 
> ✏️ *Desarrollar estas tres aplicaciones con diferentes tecnologías implicó cierta dificultad,
pero a la vez permitió comprender mejor el tema y el uso de cada lenguaje, así como el
funcionamiento de los elementos básicos de una aplicación móvil que usamos día a día.
.*
 
### ¿En cuál tecnología fue más rápido construir la interfaz?
 
En **Jetpack Compose** y **Views** fueron los más rápidos. En ambos, la interfaz se describe directamente en código y el estado se conecta sin pasos intermedios. Una lista completa se resolvió con `LazyColumn` o `ListView` y unas cuantas líneas. En **Views** la misma lista necesitó un layout XML por fila, un `ViewHolder`, un `Adapter` y un `DiffUtil`. Además, cada sección de Views requirió dos archivos (XML y Fragment) y varios recursos adicionales como íconos vectoriales, estilos y menús.]
 
### ¿Cuál generó el código más legible?
 
**Compose** me pareció el más legible, porque la estructura del código refleja la estructura de la pantalla y todo lo relacionado con un elemento está en un solo lugar. **Flutter** es muy parecido, aunque el anidamiento de widgets puede volverse profundo. **Views** separa el diseño (XML) de la lógica (Kotlin). Eso ordena los archivos, pero obliga a saltar entre ellos para entender un solo elemento.]
 
### Dificultades en cada tecnología
 
**Jetpack Compose**
- [1: El primer *Gradle Sync* falló porque la ruta del proyecto tenía caracteres con acento. Se resolvió renombrando la carpeta.]
- [2: Al agregar dependencias se duplicaron las secciones `[versions]` y `[libraries]` del catálogo `libs.versions.toml`.]
- [3: El recurso `themes.xml` para modo oscuro quedó en la carpeta equivocada y provocó el error *Duplicate resources*.]
**Views + XML**
- [1: se necesitó mucho más código y archivos para lograr lo mismo que en Compose.]
- [2: hubo que resolver a mano el conflicto de gestos entre deslizar un elemento para eliminarlo y cambiar de pestaña en el `ViewPager2`.]
- [3: algunos atributos de color pertenecen a AppCompat y no a Material (`colorError`), lo que provocó un error de compilación.]
**Flutter**
- [1: fue necesario instalar el plugin y configurar la ruta del SDK de Android para que Flutter detectara el teléfono.]
- [2: la imagen local no cargaba hasta registrarla en la sección `assets` de `pubspec.yaml`.]
- [3: Flutter no incluye un *toast* nativo, así que se construyó con un `Overlay`.]
### ¿Con cuál preferiría trabajar?
 
Prefiero trabajar con **Jetpack Compose** para aplicaciones exclusivas de Android, porque es el enfoque que recomienda Google, es más rápido de escribir y se integra con todo el ecosistema de Android. Si el proyecto tuviera que funcionar también en iOS o web, elegiría **Flutter**, porque con un solo código se obtiene una interfaz prácticamente idéntica en todas las plataformas. **Views** sigue siendo importante para mantener aplicaciones existentes.
 
---
 
# 9. Referencias
 
Android Developers. (s. f.-a). *Jetpack Compose*. Google. Recuperado el 25 de septiembre de 2026, de https://developer.android.com/develop/ui/compose/documentation
 
Android Developers. (s. f.-b). *Layouts in views*. Google. Recuperado el 25 de septiembre de 2026, de https://developer.android.com/develop/ui/views/layout/declaring-layout
 
Android Developers. (s. f.-c). *Navigation*. Google. Recuperado el 25 de septiembre de 2026, de https://developer.android.com/guide/navigation
 
Android Developers. (s. f.-d). *ViewModel overview*. Google. Recuperado el 25 de septiembre de 2026, de https://developer.android.com/topic/libraries/architecture/viewmodel
 
Coil Contributors. (s. f.). *Coil: Image loading for Android and Compose Multiplatform*. Recuperado el 25 de septiembre de 2026, de https://coil-kt.github.io/coil/
 
Dart. (s. f.). *Language tour*. Google. Recuperado el 25 de septiembre de 2026, de https://dart.dev/language
 
Flutter. (s. f.-a). *Flutter documentation*. Google. Recuperado el 25 de septiembre de 2026, de https://docs.flutter.dev/
 
Flutter. (s. f.-b). *Material component widgets*. Google. Recuperado el 25 de septiembre de 2026, de https://docs.flutter.dev/ui/widgets/material
 
GitHub. (s. f.). *Administrar lanzamientos en un repositorio*. GitHub Docs. Recuperado el 25 de septiembre de 2026, de https://docs.github.com/es/repositories/releasing-projects-on-github/managing-releases-in-a-repository
 
Google. (s. f.). *Material Design 3*. Recuperado el 25 de septiembre de 2026, de https://m3.material.io/
 
JetBrains. (s. f.). *Kotlin documentation*. Recuperado el 25 de septiembre de 2026, de https://kotlinlang.org/docs/home.html
 
Material Components for Android. (s. f.). *Material Components for Android documentation* [Repositorio de GitHub]. GitHub. Recuperado el 25 de septiembre de 2026, de https://github.com/material-components/material-components-android/tree/master/docs
 