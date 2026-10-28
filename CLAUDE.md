# Programacion-Servicios-Y-Procesos (PSP, 2º DAM)

Programación concurrente en Java: procesos, hilos y (más adelante) sockets y servicios.

## Estructura
- `UT0-Repaso/` — repaso de Java y streams (PDF/PPTX).
- `UT1-Procesos/Apuntes/` — `PSP_UT1.pdf` y `Ejemplos-En-Clase/` (Lanzador*.java y el proyecto IntelliJ `UT1_Ejemplos_Comunicacion_Procesos`, paquete `PSP`: ProcessBuilder, redirecciones, pipes, inheritIO).
- `UT1-Procesos/Ejercicios/` — enunciados y proyecto IntelliJ `UT1.procesos` (configuraciones en `.run/`).
- `UT1-Procesos/Practica/`
- `UT2-Hilos/Apuntes/` — unidad en curso.

## Compilar y ejecutar
- Proyectos IntelliJ (`.iml`, sin Maven): abrir la carpeta del proyecto en IntelliJ, o `javac -d out src/PSP/*.java && java -cp out PSP.Clase`.
- Los ejemplos de procesos lanzan otros programas Java: compila también la clase hija (p. ej. `HijoCuadrado`) antes de ejecutar el padre.

## Notas
- Los `.zip` de Ejercicios son material del profesor (enunciado y solución): no los modifiques.
