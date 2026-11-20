# ⚙️ Programación de Servicios y Procesos (PSP)

Programación multihilo, procesos concurrentes, sockets de red y comunicación segura / Multithreading, concurrent processes, network sockets, and secure communication coursework.

---

## 🗂️ Estructura del Repositorio

* **`ut0-repaso/`**: Ejercicios y conceptos base iniciales. Las soluciones en Java están en `ejercicios-resueltos/`.
* **`ut1-procesos/`**: Ejercicios, teoría y práctica sobre la creación, gestión y comunicación de procesos.
  * **`apuntes/apuntes-ut1-procesos.md`**: apuntes completos de la UT1 (ProcessBuilder, Process, ProcessHandle, redirecciones, comunicación y diferencias Linux/Windows).
  * **`ejercicios/soluciones-*.md`**: los 12 ejercicios de ProcessBuilder y los 15 de comunicación explicados línea a línea, con sus reflexiones.
  * **`apuntes/resumen-rapido-ut1.md`** (y `.pdf`): resumen rápido para repasar antes del examen, con las preguntas del test real.
  * **`soluciones/`**: código de todas las soluciones y de las Tareas 1, 2 y 4, válido en Linux y en Windows (ver `LEEME.md`).
  * **`examen/`**: examen real de la UT1 (09/11/2026) transcrito con sus soluciones (`examen-ut1.md`), el proyecto tal como se entrega al alumno (`ut1-examen-alumno/`) y su resolución (`solucion/`).
* **`ut2-hilos/`**:
  * **`apuntes/`**: Teoría, documentación y ejemplos sobre programación multihilo y sincronización. **`resumen-rapido-ut2.md`**: resumen para el examen, con plantillas para la práctica, un simulacro de test de 20 preguntas y un banco de 45.
  * **`simulacro-examen/`**: simulacro práctico del examen de la UT2 con el formato del de la UT1: enunciado con soluciones (`enunciado-simulacro-ut2.md`), proyecto del alumno con `// TODO` (`ut2-simulacro-alumno/`) y su resolución (`solucion/`).
  * **`entrega/`**: entrega de los ejercicios de la UT2: versión IntelliJ y versión VS Code con un menú para elegir el ejercicio, y el PDF de respuestas (`ut2-psp-JoseAntonio_PastorRivera.pdf`).
  * **`ejercicios/`**: relación de ejercicios (`ejercicios-ut2.docx`), proyecto IntelliJ `bloque1-4` sin resolver y `soluciones-ejercicios-ut2.md` con las respuestas.
  * **`soluciones/bloque1-4/`**: los 14 ejercicios resueltos (hilos, condiciones de carrera, volatile, synchronized, Lock, atómicos, interbloqueo), explicados línea a línea. Igual en Linux y en Windows. El ejercicio 1.2 (tarea pesada, contar primos) tiene además una solución B con `extends Thread` (`ConHilosB.java` y `ContarPrimosHilo.java`).

---

## 🛠️ Tecnologías y Conceptos
* **Lenguaje Principal**: Java
* **Áreas**: Threads, Multiproceso, Sincronización, Concurrencia.
* **Control de Versiones**: Git / GitHub.

---
&copy; 2026-2027 José Antonio Pastor Rivera | IES Isidra de Guzmán (DAM)
