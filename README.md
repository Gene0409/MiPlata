# MiPlata 💚

**Aplicación móvil de finanzas personales para Android.** Permite registrar ingresos y gastos, consultar movimientos, visualizar resúmenes y administrar metas de ahorro en pesos chilenos. Los registros se almacenan localmente en el dispositivo.

**Asignatura:** DSY1105 — Desarrollo de Aplicaciones Móviles  
**Evaluación:** Parcial 2 — Duoc UC  
**Integrantes:** `Gabriel Cáceres` · `Genesis Lincoqueo`  
**Equipo:** `10`  
**Trello:** `[[Pegar enlace real del tablero]](https://trello.com/invite/b/6adbfc8349d7c4f1fd15f4d8/ATTI087bf96f2547868c761959e47c36df315C2EBAC0/miplata-dsy1105-evaluacion-parcial-2)`

## Funcionalidades

- **Inicio:** saldo total, ingresos y gastos; accesos rápidos y notificación voluntaria de prueba.
- **Movimientos:** historial de transacciones y eliminación.
- **Agregar:** ingreso/gasto, categoría, monto y descripción; validaciones visuales independientes de la UI; imagen opcional de galería o cámara.
- **Metas:** creación, visualización de progreso, actualización del ahorro y eliminación; errores individuales en cada campo.
- **Resumen:** indicadores financieros según los movimientos registrados.
- **Persistencia:** Room/SQLite, DAO, repositorios y ViewModel; migración de versión 1 a 2.
- **Animaciones:** transiciones entre secciones, aparición de imágenes y avisos del formulario.
- **Recursos nativos:** selector de documentos de Android, cámara del dispositivo y notificaciones del sistema.

## Tecnología y estructura

- Kotlin, Jetpack Compose, Material 3 y navegación por estado de Compose.
- Room + Flow y arquitectura MVVM: `model`, `data/local`, `data/repository`, `viewmodel`, `ui/screens`.
- Recursos nativos en `nativos/Notificaciones.kt`, selector `OpenDocument`, cámara `TakePicture`, imágenes internas con `FileProvider`.
- JUnit para validaciones y test de instrumentación para persistencia.

```text
app/src/main/java/com/example/miplata/
  MainActivity.kt              # tema, cinco pantallas, transiciones
  Validaciones.kt              # reglas puras
  MovimientoViewModel.kt       # estados de formularios y movimientos
  data/local/                  # Room (base de datos, entidades, DAO)
  data/repository/             # acceso a datos
  model/                       # modelos de dominio
  viewmodel/                   # MetaAhorroViewModel
  ui/screens/                  # Inicio, Movimientos, Agregar, Metas, Resumen
  nativos/Notificaciones.kt    # segundo/tercer recurso nativo
```

## Abrir y ejecutar en Android Studio

1. Instala Android Studio y el SDK compatible con `compileSdk` de `app/build.gradle.kts` (el original usa API 37). Usa el JDK compatible con el Gradle configurado en `gradle/gradle-daemon-jvm.properties`.
2. Abre **esta carpeta raíz**, la que contiene `settings.gradle.kts` y `gradlew.bat`.
3. Permite **Sync Project with Gradle Files** (Android Studio descargará dependencias cuando haya conexión).
4. Inicia un emulador con cámara simulada y notificaciones habilitadas, o conecta un dispositivo físico Android (API 24 o superior).
5. Presiona **Run ▶** y selecciona el dispositivo.
6. Para notificaciones en Android 13 o superior, acepta el permiso cuando presiones «Enviar recordatorio». Si lo rechazas, podrás activarlo en los ajustes de la aplicación.
7. La cámara usa una aplicación externa de captura y guarda las imágenes dentro del espacio privado de MiPlata mediante FileProvider. Si el dispositivo no tiene una app de cámara, usa «Galería».

## Casos de prueba mínimos

- [x] Cambiar entre las cinco pantallas, incluyendo una pantalla pequeña.
- [x] Intentar guardar movimiento vacío, negativo y con categoría vacía: errores por campo.
- [x] Guardar ingreso y gasto, cerrar/reabrir MiPlata: el balance y el historial se mantienen.
- [x] Crear meta vacía, con objetivo 0 y ahorro superior: errores por campo.
- [x] Crear meta válida; actualizar y eliminar; comprobar persistencia tras reinicio.
- [x] Adjuntar imagen desde «Galería», cerrar/reabrir app y verificar el registro.
- [x] Tomar foto con «Cámara» y verificar la vista previa y la imagen guardada.
- [x] Pulsar «Enviar recordatorio», conceder permiso y recibir la notificación.
- [x] Observar transiciones de pantallas y aparición animada de imágenes.
- [x] Ejecutar pruebas automáticas en Android Studio.

**Pruebas:** `./gradlew testDebugUnitTest` (Windows: `gradlew.bat testDebugUnitTest`) y, con emulador/dispositivo activo, `./gradlew connectedDebugAndroidTest`. Los tests de Room y cámara deben verificarse en un dispositivo real/emulador.

## Trabajo colaborativo y entrega

- Repositorio público asociado al archivo original: https://github.com/Gene0409/MiPlata
- Tablero Trello compartido: `[Pegar URL real del tablero]`.
- Cada tarea debe mostrar responsable, fecha real de ejecución y evidencia enlazada (commit, PR, captura o resultado de prueba).
- Se puede documentar honestamente la regularización y el estado actual.
- Entregar también el código en **AVA grupal**
