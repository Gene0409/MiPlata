# Verificación de rúbrica DSY1105 (Parcial 2)

Este documento describe **evidencia de implementación**, no constituye una nota del docente ni una prueba completa de ejecución.

| Indicador | Ponderación | Evidencia en código | Pendiente de comprobar |
|---|---:|---|---|
| IE 2.1.1 UI y navegación | 15% | `MainActivity.kt`, cinco pantallas, colores/animaciones y labels compactos | Capturas en móvil compacto y navegación real |
| IE 2.1.2 Formularios | 15% | `AgregarMovimientoScreen`, `MetaAhorroScreen` con iconos, isError y errores por campo | Probar entradas incorrectas en dispositivo |
| IE 2.2.1 Validación desacoplada | 10% | `Validaciones.kt`, `MovimientoViewModel`, `MetaAhorroViewModel` | Ejecutar pruebas JUnit y comprobar errores en interfaz |
| IE 2.2.2 Animaciones | 10% | `AnimatedContent`, `AnimatedVisibility`, transiciones | Video corto o demostración funcional |
| IE 2.3.1 MVVM y Room | 15% | `data/local`, `repository`, ViewModels y test `PersistenciaLocalTest` | Ejecutar test de instrumentación; reabrir app |
| IE 2.3.2 GitHub/Trello | 20% | `README.md`, documentos de Git y Trello | Actividad histórica genuina, integrantes, tarjetas y evidencia pública |
| IE 2.4.1 Recursos nativos | 15% | OpenDocument, TakePicture/FileProvider y notificación/permiso | Probar galería, cámara y notificación en Android |

## Resultado de validación técnica en este entorno

- `Validaciones.kt`: compilado satisfactoriamente con `kotlinc`, comprobadas reglas clave mediante script de ejecución local.
- Build de Android/Gradle: **no ejecutado**. La distribución de Gradle 9.6.0 no está disponible localmente y este entorno no pudo resolver `services.gradle.org`. Tampoco se dispone de Android SDK/emulador para comprobar la ejecución visual.
- **No se debe afirmar que la aplicación está certificada al 100%** hasta ejecutar pruebas y entregar evidencias de colaboración y AVA dentro del plazo.

## Secuencia final de pruebas recomendada

1. Abrir esta carpeta en Android Studio y confirmar **BUILD SUCCESSFUL**.
2. Ejecutar `gradlew.bat testDebugUnitTest`.
3. Ejecutar `gradlew.bat connectedDebugAndroidTest` con emulador abierto.
4. Registrar un gasto e ingreso, reiniciar la app y comprobar saldo.
5. Crear, actualizar y eliminar una meta; comprobar errores por campo.
6. Abrir Galería, guardar imagen y comprobar foto en Movimientos.
7. Tomar una foto de Cámara, guardarla y comprobarla en Movimientos tras reiniciar.
8. Pedir permiso y enviar notificación; revisar la cortina de notificaciones.
9. Ver animaciones y navegación en al menos una pantalla estrecha.
10. Completar integrantes, tablero real, capturas y entrega AVA. No alterar el repositorio después del plazo.
