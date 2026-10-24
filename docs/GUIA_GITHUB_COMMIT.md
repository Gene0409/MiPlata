# Actualización de GitHub para MiPlata

## Mensaje recomendado para el commit REAL de estas modificaciones

Título (convención Conventional Commits):

```text
feat(android): completar validaciones y recursos nativos de MiPlata
```

Descripción del commit:

```text
- Centraliza las validaciones de metas y movimiento en lógica reutilizable.
- Agrega errores por campo e iconografía Material manteniendo el diseño verde.
- Integra captura de fotografías con cámara y FileProvider.
- Integra avisos de Android con solicitud de permiso de notificaciones.
- Añade pruebas JUnit y de instrumentación para persistencia Room.
- Documenta instalación, funcionalidades y lista de verificación de la entrega.
- Elimina copias duplicadas y artefactos generados del paquete fuente.
```

## Importante: NO pierdas el historial existente

El ZIP de entrega **no lleva `.git`**. No lo reemplaces como carpeta de repositorio ni ejecutes `git init` sobre tu historial anterior.

1. Clona o abre tu repositorio existente.
2. Crea una copia de seguridad antes de sobrescribir archivos.
3. Copia el **contenido de `MiPlata_Entrega/`** (app, gradle, README, docs, etc.) dentro de la raíz del repositorio existente, no encima de su carpeta `.git`.
4. Revisa que Git muestre únicamente los cambios de esta revisión.

```bash
git status
git remote -v
git pull --ff-only origin main
# Copia aquí el contenido de MiPlata_Entrega/ a la raíz del repositorio.
git status
git add .
git diff --cached --stat
git commit -m "feat(android): completar validaciones y recursos nativos de MiPlata"
git push origin main
```

**No publiques:** `.gradle/`, `.idea/`, `local.properties`, `build/`, archivos temporales, tokens ni claves privadas. Los excluye `.gitignore`.

**Comprobaciones después del push:** en GitHub se ven `README.md`, `app/`, `gradle/`, `gradlew`, `gradlew.bat` y `docs/`, y puedes revisar los archivos del commit en la pestaña *Commits*.

**Evidencia de colaboración:** conserva fechas reales, contribuciones y tareas reales; un commit nuevo NO reemplaza la historia distribuida que exige la pauta. Si ya cerró la entrega, no realices pushes antes de tener autorización del docente: la pauta advierte nota 1,0 por modificaciones fuera de plazo.
