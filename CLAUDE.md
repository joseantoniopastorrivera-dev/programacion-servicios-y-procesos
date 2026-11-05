# Programacion-Servicios-Y-Procesos (PSP, 2º DAM)

Programación concurrente en Java: procesos, hilos y (más adelante) sockets y servicios.

## Estructura
- `ut0-repaso/` — repaso de Java y streams (PDF/PPTX).
- `ut1-procesos/apuntes/` — `psp-ut1.pdf` y `ejemplos-en-clase/` (Lanzador*.java y el proyecto IntelliJ `ut1-ejemplos-comunicacion-procesos`, paquete `PSP`: ProcessBuilder, redirecciones, pipes, inheritIO).
- `ut1-procesos/ejercicios/` — enunciados y proyecto IntelliJ `ut1-procesos` (configuraciones en `run/`).
- `ut1-procesos/practica/`
- `ut2-hilos/apuntes/` — unidad en curso.

## Compilar y ejecutar
- Proyectos IntelliJ (`.iml`, sin Maven): abrir la carpeta del proyecto en IntelliJ, o `javac -d out src/PSP/*.java && java -cp out PSP.Clase`.
- Los ejemplos de procesos lanzan otros programas Java: compila también la clase hija (p. ej. `HijoCuadrado`) antes de ejecutar el padre.

## Notas
- Los `.zip` de Ejercicios son material del profesor (enunciado y solución): no los modifiques.
