# Trello: tablero MiPlata | DSY1105 – Evaluación Parcial 2

## Crear y compartir

1. Ingresa a https://trello.com e inicia sesión.
2. En **Create / Crear** elige **Create board / Crear tablero**.
3. Nombre: `MiPlata | DSY1105 – Evaluación Parcial 2`.
4. Selecciona un espacio de trabajo; escoge visibilidad apropiada para compartir con el docente (evita publicar datos personales).
5. Usa **Share / Compartir** para invitar al otro integrante y al docente si corresponde, con correos reales.
6. Agrega las listas descritas abajo con **Add another list / Añadir otra lista**.

## Diseño visual sugerido

```text
┌────────────────────────────────────────────────────────────────────────────────┐
│ MiPlata  ·  DSY1105           [ Compartir ]  [ Actividad ]                     │
├────────────────┬────────────────┬────────────────┬────────────────┬─────────────┤
│ 📥 BACKLOG     │ 📋 POR HACER   │ 🛠 EN PROCESO  │ 🔍 EN REVISIÓN │ ✅ HECHO    │
│                │                │                │                │             │
│ Casos borde    │ Probar cámara  │ Revisar SDK    │ Prueba Room    │ MVVM + Room │
│ Accesibilidad  │ Capturas AVA   │                │ Formulario     │ UI 5 vistas │
│                │                │                │                │ Animaciones │
└────────────────┴────────────────┴────────────────┴────────────────┴─────────────┘
```

**Los estados son solo un ejemplo: ubica cada tarjeta según su progreso REAL.** No marques tareas como concluidas si no las has comprobado.

## Etiquetas

- Verde — Funcionalidad completada y probada
- Azul — Interfaz / experiencia de usuario
- Morado — Room / MVVM / arquitectura
- Amarillo — Pruebas y documentación
- Rojo — Error o impedimento pendiente

## Tarjetas recomendadas

| Tarjeta (título) | Lista inicial sugerida | Evidencia / criterio |
|---|---|---|
| Implementar navegación de cinco pantallas | Hecho si fue verificada | Captura y código `MainActivity.kt`; IE 2.1.1 |
| Validaciones de movimientos y mensajes por campo | En revisión | Pruebas de entradas inválidas; IE 2.1.2/2.2.1 |
| Validaciones de metas desacopladas | En revisión | `Validaciones.kt`, VM y capturas; IE 2.1.2/2.2.1 |
| Animaciones de transición y retroalimentación | En revisión | Video o capturas; IE 2.2.2 |
| Persistencia con Room y MVVM | En revisión | Test tras reiniciar y resultado; IE 2.3.1 |
| Galería de imágenes | En revisión | Video de elección de imagen; IE 2.4.1 |
| Cámara con FileProvider | Por hacer hasta probar | Captura en dispositivo; IE 2.4.1 |
| Recordatorio por notificación Android | Por hacer hasta probar | Captura con aviso visible; IE 2.4.1 |
| README y documentación de entrega | Por hacer hasta completar datos | URL pública GitHub y README; IE 2.3.2 |
| Subir código a AVA y activar AVA individual | Por hacer | Comprobante de envío (si está dentro del plazo) |

## Completa cada tarjeta

- **Descripción**: qué se hizo, aceptación esperada y relación con el indicador.
- **Miembros**: responsable(s) reales del trabajo.
- **Checklist**: analizar → implementar → probar → documentar → revisar.
- **Fecha**: fecha real, sin inventar cronología.
- **Adjuntos**: enlace al commit, pull request, captura o video.
- **Comentarios**: breve evidencia técnica y resultado; registra el trabajo auténtico.

Ejemplo de tarjeta: `Cámara con FileProvider`.

```text
Responsable: [integrante real]
Etiqueta: Nativo
Criterio: IE 2.4.1
Checklist:
[ ] Incorporar TakePicture y FileProvider
[ ] Probar cámara y cancelación en un dispositivo
[ ] Verificar que la imagen se vea después de registrar
[ ] Adjuntar captura y enlace al commit
Fecha: [fecha real de verificación]
```

## Cómo dejar evidencia para la rúbrica

- Deja **tarjetas con fechas de trabajo reales**, asignaciones a ambos integrantes y capturas de las actividades.
- Adjunta enlaces a commits reales por tarjeta; los cambios nuevos no sustituyen los commits distribuidos requeridos durante las semanas del proyecto.
- En el menú del tablero observa la actividad, y proporciona enlace accesible al docente.
- Guarda capturas del tablero, una tarjeta completa y el registro de actividad. **No simules semanas pasadas.**

Fuentes oficiales: https://support.atlassian.com/trello/docs/creating-a-new-board/ ; https://support.atlassian.com/trello/docs/adding-cards ; https://support.atlassian.com/trello/docs/adding-checklists-to-cards/
