# Soluciones de la UT1 (Linux y Windows)

Tres proyectos de IntelliJ (paquete `PSP`), uno por cada bloque de ejercicios:

| Carpeta | Qué contiene |
|---|---|
| `processbuilder/` | Los 12 ejercicios de *ProcessBuilder y Process* (`Ejercicio1` … `Ejercicio11`, `Padre`/`Hijo`, `GestorProcesos`) |
| `comunicacion/` | Los 15 ejercicios de *Comunicación entre procesos* (`Ejercicio01` … `Ejercicio15` y los hijos), con `datos/` |
| `tareas/` | Las Tareas 1, 2 y 4 del PDF de teoría (`Tarea1`, `Tarea2ProcessCom`, `RunAndWait`, `ModRunAndWait`, `Sumador`) |

Cada ejercicio funciona en **Linux** (el examen) y en **Windows** (casa): la clase `SO` elige el comando según el sistema.
En el examen no hace falta `SO`: se escribe directamente el comando de Linux, que es el primero de cada `SO.comando(linux, windows)`.

## Ejecutar

Abrir la carpeta del proyecto en IntelliJ (File > Open) y ejecutar, o desde la terminal, dentro de la carpeta del proyecto:

```bash
javac -encoding UTF-8 -d bin src/PSP/*.java
java -cp bin PSP.Ejercicio01
```

Los programas que lanzan otro programa Java (`Padre`, `Ejercicio09`, `10`, `14`, `15`, `NodoArbol`, `RunAndWait`…) usan `java -cp bin PSP.Xxx`: hay que ejecutarlos **desde la carpeta del proyecto**, donde está `bin/`.

En Windows, `tr`, `sort`, `wc`, `uniq` y `head` se toman de Git para Windows (`C:/Program Files/Git/usr/bin/`).

Las explicaciones de cada ejercicio están en `../apuntes/` y `../ejercicios/`.
