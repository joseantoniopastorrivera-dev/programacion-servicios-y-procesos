# Traspaso: repaso del examen de la UT1 (PSP)

Archivo de contexto para seguir en Claude Code la conversación que empecé en el chat de Claude.
Al abrir Claude Code en `C:\dev\programacion-servicios-y-procesos`, dile:
> Lee `ut1-procesos/traspaso-repaso-examen-ut1.md` y continúa el repaso.

## Actualización (16/11/2026)
No pude ir al examen y me toca **recuperación**. Lo que sigue más abajo es de antes del examen y tiene dos cosas que ya no valen:
- «Nada de la UT2» es falso: en el test cayeron seis preguntas de **procesos frente a hilos** y la reflexión del C2.4.
- El formato real fue test + tres análisis de código + dos ejercicios (C1 y C2).

El material de la recuperación está en `apuntes/resumen-rapido-ut1.md` (apartados 12, 16, 17, 18 y 19) y en `examen/` (proyecto original, solución y enunciado reconstruido).

## Qué quiero
Examen de PSP mañana, **solo UT1 (procesos)**; nada de la UT2 (hilos), ni sockets, ni criptografía.
Actúa como profesor experto de PSP y repasa conmigo de forma interactiva:

1. **Teoría activa:** preguntas de comprensión sobre los apuntes, **de una en una**, sin darme nunca la respuesta por adelantado.
2. **Ejercicios:** problemas parecidos a los del curso. Pídeme que escriba código clave, que explique un fragmento tuyo o que encuentre un bug (bloqueos por buffers, `waitFor` mal colocado, flujos sin cerrar, redirecciones…).
3. **Corrección:** si acierto, confírmalo y añade un matiz de experto. Si fallo, explica el error con claridad y hazme una pregunta de refuerzo sobre ese mismo fallo.
4. **"Simulacro":** cuando lo diga, 5 preguntas (3 de teoría y 2 de código corto) sobre mis puntos más débiles.

## Material (en este repo)
- `ut1-procesos/apuntes/apuntes-ut1-procesos.md` (resumen) y `psp-ut1.pdf`
- `ut1-procesos/apuntes/ejemplos-en-clase/` (Lanzador1-4 y el proyecto `ut1-ejemplos-comunicacion-procesos`: Ejemplo01-08, redirecciones, pipes, inheritIO, bloqueo)
- `ut1-procesos/ejercicios/` (enunciados, `soluciones-ejercicios-processbuilder.md`, `soluciones-comunicacion-procesos.md`)
- `ut1-procesos/soluciones/` y `ut1-procesos/practica/` (SumManager/SumWorker y `guia-defensa-practica-ut1.md`)

## Por dónde íbamos
- Descargas revisada: los 11 archivos de la UT1 que había allí son duplicados exactos (comprobados por hash) de lo que ya está en el repo. Falta borrarlos (no se pudo desde el chat):
  ```powershell
  cd $env:USERPROFILE\Downloads
  Remove-Item Lanzador.java, Lanzador2.java, Lanzador3.java, Lanzador4.java, PSP_UT1.pdf, PSP-UT1_Ejercicios_1.docx, UT1_Ej_ComProcesos.pdf, PSP_Comunicacion_Alumnos.zip, UT1_Comunicacion_Solucion.zip, UT1.procesos.zip, UT1_Ejemplos_Comunicacion_Procesos.zip
  ```
- Examen: 4 pts teoría (test + «qué sale por pantalla») y 6 pts programar en el PC (Linux + IntelliJ). Plan de unas 3 h 30 min: diagnóstico, test, qué sale, práctica, simulacro y la skill `preparar-examen`.
- Hecho: resumen rápido `apuntes/resumen-rapido-ut1.md/.pdf`, con el código verificado. Aviso de IntelliJ: usar `java.home` + `java.class.path` en vez de `-cp bin`.
- Diagnóstico, P1 (`waitFor` antes de leer): **no la sabía**. Explicada. Pendiente la pregunta de refuerzo.
- Puntos débiles detectados:
  - Regla de oro / búfer de la tubería / interbloqueo.
