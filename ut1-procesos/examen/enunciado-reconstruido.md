# Examen UT1 (RA1) · Enunciado reconstruido

> **No es el enunciado oficial.** No pude asistir al examen y el zip del aula solo traía el proyecto de la parte práctica. Esto está reconstruido a partir de los esqueletos (`// TODO`, `import`, configuraciones de ejecución, `LEEME.txt`) y de lo que me contaron los compañeros. Lo confirmado por ellos va marcado con ✅; lo deducido, con 🔎.

## Qué hay en esta carpeta

| Carpeta o fichero | Qué es |
|---|---|
| `ut1-examen-alumno/` | Proyecto IntelliJ **tal cual se entregó en el aula**, sin resolver. No tocar: sirve para repetir el examen desde cero |
| `solucion/` | El mismo proyecto con C1 y C2 resueltos y comentados línea a línea |
| `enunciado-reconstruido.md` | Este documento |

## Estructura del examen

1. ✅ **Test.** Difícil y con preguntas «a pillar»; casi la mitad, de procesos frente a hilos. Las 15 preguntas que copié cuando el profesor lo corrigió en clase están en `../apuntes/resumen-rapido-ut1.md`, apartado 18.
2. ✅ **Tres análisis de código**: qué muestra cada `print` (`waitFor` con tiempo, `isAlive`, `destroy`), por qué se cuelga un `sort` (falta `close()`) y por qué se cuelga un hijo con mucha salida (`waitFor()` antes de leer). Están reconstruidos y explicados en el apartado 12 del mismo resumen.
3. ✅ **Parte práctica en el PC** (Linux + IntelliJ): dos ejercicios, **C1** y **C2**.

Entrega: un zip con el proyecto completo, **sin la carpeta `bin`**, y con el nombre escrito en la cabecera de cada clase.

## C1 · Ejecutor de tareas (`PSP.c1.EjecutorTareas`)

✅ Argumentos: `<segundos> <codigo> <esperaMaxima>`.

La clase `Tarea` ya está hecha y no se modifica: recibe `<segundos> <codigoDeSalida>`, escribe una línea de progreso por segundo y termina con `System.exit(codigo)`.

`EjecutorTareas` tiene que:

1. ✅ Lanzar `Tarea` como proceso hijo, pasándole `segundos` y `codigo`.
2. ✅ **Redirigir** la salida del hijo a un fichero (`tarea.log`).
3. ✅ Esperarlo **como mucho** `esperaMaxima` segundos.
4. ✅ Distinguir los tres finales e informar de cada uno:

| Configuración de ejecución | Qué pasa |
|---|---|
| `3 0 5` | Termina bien: código 0 |
| `2 4 5` | Termina con error: código 4 |
| `8 0 3` | No termina a tiempo: hay que **destruirlo** |

5. 🔎 Leer `tarea.log` y mostrarlo por pantalla (el esqueleto importa `FileReader` y `BufferedReader`, pero no `InputStreamReader`: la salida se lee del fichero, no de la tubería).

## C2 · Estadísticas por grupo (`PSP.c2`)

Datos en `datos/`, una nota por línea, con trampas puestas a propósito:

| Fichero | Contenido | Notas válidas |
|---|---|---|
| `grupoA.txt` | `7.5`, `8`, `6.25`, `9`, `5.5` | 5 (media 7,25) |
| `grupoB.txt` | `4`, `siete`, `10`, `11`, `3.5`, `-1`, `6` | 4 (media 5,875) |
| `grupoC.txt` | `no presentado`, línea vacía, `NP` | 0 |

### C2.1 · `HijoEstadistica <nombreDelGrupo>`

- ✅ Lee las notas por su **entrada estándar** y calcula las estadísticas del grupo.
- 🔎 Descarta lo que no es una nota válida (texto, fuera de 0-10, líneas vacías) y avisa por **stderr**.
- 🔎 Escribe el resultado por **stdout** para que lo lea el padre.
- 🔎 Si no hay ninguna nota válida, termina con un código de salida distinto de 0.

### C2.2 · `Padre`

- ✅ Lanza un `HijoEstadistica` **por cada grupo**.
- ✅ Usa **redirecciones** (el esqueleto importa `ProcessBuilder.Redirect`): la entrada de cada hijo viene de `datos/grupoX.txt` y 🔎 sus errores se añaden a `errores.log`.
- ✅ Lee la salida de cada hijo y calcula la **media global**.

Resultado correcto con estos datos: 9 notas válidas, suma 59,75, **media global 6,6389**. La media de las medias (7,25 y 5,875 → 6,5625) es **incorrecta**, porque los grupos no tienen el mismo número de notas.

### C2.4 · Reflexión

✅ En un comentario al principio de `Padre.java`, de 5 a 10 líneas: **¿qué cambiaría con hilos en lugar de procesos?**

> En el esqueleto se salta de C2.2 a C2.4. Los compañeros confirman que solo había C1 y C2, así que el C2.3 o no existía o era un subapartado del padre que no deja rastro en el código.

## Cómo ejecutar la solución

En IntelliJ: `File > Open…` sobre `solucion/` y usar las cuatro configuraciones del desplegable.

En una terminal, desde `solucion/`:

```bash
javac -d bin src/PSP/c1/*.java src/PSP/c2/*.java
java -cp bin PSP.c1.EjecutorTareas 3 0 5
java -cp bin PSP.c1.EjecutorTareas 2 4 5
java -cp bin PSP.c1.EjecutorTareas 8 0 3
java -cp bin PSP.c2.Padre
```

Salida comprobada (JDK 25, en Windows):

```text
RESULTADO: la tarea terminó bien (código 0)
RESULTADO: la tarea terminó con error (código 4)
RESULTADO: la tarea superó los 3 s y se ha destruido (código 1)     ← en Linux, 143

grupoA: 5 notas válidas, media 7.25, máxima 9.0, mínima 5.5
grupoB: 4 notas válidas, media 5.875, máxima 10.0, mínima 3.5
grupoC: sin resultado (código de salida 1)
MEDIA GLOBAL: 6.638888888888889 (9 notas)
```

En Windows, las tildes de `tarea.log` y `errores.log` pueden verse mal al abrirlos como UTF-8: el hijo escribe en la codificación del sistema. En Linux, que es donde se hace el examen, no pasa.

## Decisiones de la solución que habría que saber justificar

- **`waitFor(n, TimeUnit.SECONDS)` devuelve `boolean`**, no el código de salida. El código se pide después con `exitValue()`, y solo si ya ha terminado.
- **`destroy()` no espera.** Por eso detrás hay un `waitFor` corto y, si sigue vivo, `destroyForcibly()`.
- **La salida redirigida a fichero no se puede leer con `getInputStream()`**: hay que abrir el fichero.
- **`redirectErrorStream(true)` en C1**: así no queda ninguna tubería sin leer.
- **`Redirect.appendTo` en C2**: los tres hijos escriben en el mismo `errores.log`; con `new File(...)` cada uno machacaría lo del anterior.
- **Lanzar todos los hijos y después esperarlos**: así trabajan en paralelo.
- **Leer antes de `waitFor()`** (regla de oro), aunque aquí cada hijo solo escriba una línea.
- **stdout para los datos, stderr para los avisos, código de salida para decir si fue bien.**
- **Formato con `;` y sin `String.format`**: en un sistema en español `format` escribe `7,25` con coma y `Double.parseDouble` falla.
