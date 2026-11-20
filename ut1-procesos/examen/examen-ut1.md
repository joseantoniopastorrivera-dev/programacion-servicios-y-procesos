# Examen UT1 (RA1) · PSP · 09/11/2026

> **Enunciado real**, transcrito de las fotos del examen (8 páginas) y de la **solución oficial del test** que publicó el profesor. Sustituye al antiguo `enunciado-reconstruido.md`.
> Úsalo como **simulacro**: haz las partes A y B en papel, la práctica en `ut1-examen-alumno/` y **después** mira las soluciones del final.
> Proyecto del profesor sin tocar: [`ut1-examen-alumno/`](ut1-examen-alumno/) · Solución completa y comentada: [`solucion/`](solucion/)

## Índice

- [Normas y puntuación](#normas-y-puntuación)
- [Parte A · Tipo test (2,5 puntos)](#parte-a--tipo-test-25-puntos)
- [Parte B · Análisis de código (2 puntos)](#parte-b--análisis-de-código-2-puntos)
- [Parte práctica (5,5 puntos)](#parte-práctica-55-puntos)
- [Soluciones](#soluciones)
- [Lo que este examen enseña para el de la UT2](#lo-que-este-examen-enseña-para-el-de-la-ut2)

---

## Normas y puntuación

**Cabecera:** Ciclo DAM · Módulo Programación de Servicios y Procesos · Examen RA 1 · Curso 2026/2027 · Grupo DAM2 · Fecha 09/11/2026.

| Parte | Puntos | Qué es |
|---|---|---|
| **A** · Tipo test | 2,5 | 20 preguntas, una sola respuesta correcta |
| **B** · Análisis de código | 2 | B1 (0,5) · B2 (0,75) · B3 (0,75). No se escriben programas |
| **Práctica** | 5,5 | C1 (2) · C2 (3,5) en el PC, con IntelliJ |
| **Total** | **10** | Para aprobar el examen y el RA, teoría + práctica **≥ 5** |

**Test:** cada acierto suma **1**, cada error **resta 1/3** y las preguntas sin responder **no suman ni restan**. La nota se pondera sobre 2,5:

```
nota del test = (aciertos − errores / 3) / 20 × 2,5      (mínimo 0)
ejemplo: 11 aciertos, 3 errores, 6 en blanco → (11 − 1) / 20 × 2,5 = 1,25
```

**Parte práctica · instrucciones del examen:**
- **Estructura:**
  - **C1 · Ejecutor de tareas (2 puntos):** lanzar un proceso, redirigir su salida, esperarlo con un tiempo límite e interpretar su código de salida.
  - **C2 · Analizador de notas en paralelo (3,5 puntos):** lanzar varios procesos en paralelo, comunicarse con ellos y combinar sus resultados.
- **Criterios de evaluación:** correcta compilación y ejecución · cumplimiento de los requisitos funcionales · código claro y comentado · legibilidad, mensajes claros y manejo de errores.
- **Proyecto de partida:** el proyecto de IntelliJ `UT1_Examen_Alumno` facilitado por el profesor. Incluye la clase `Tarea` (ya hecha), los ficheros de datos y las clases que hay que completar, agrupadas por ejercicio en los paquetes `PSP.c1` y `PSP.c2`. El proyecto compila en la carpeta `bin`.
- **Entrega:** todos los `.java` correctamente nombrados y guardados en la carpeta del proyecto, comprimido en **`Apellidos_Nombre.zip`**, **sin la carpeta `bin`**. Se sube al aula virtual a través del **cuestionario** habilitado.

**Otras consideraciones:**
- Prohibidos los aparatos electrónicos (móviles, smartwatch…), los materiales no permitidos y copiar: la calificación sería un **0**.
- **Sin acceso a Internet ni herramientas de inteligencia artificial.**
- Cada alumno usa su propio material; no se presta nada.
- No se puede hablar durante el examen.

---

## Parte A · Tipo test (2,5 puntos)

> 20 preguntas con una sola respuesta correcta. Apunta tus respuestas en la tabla del final de esta parte.

**1.** ¿Qué es la programación concurrente?
- a) Varios procesos que avanzan a la vez y colaboran en una tarea
- b) Ejecutar un programa más rápido con un procesador más potente
- c) Programar una aplicación combinando varios lenguajes de programación
- d) Ejecutar los procesos uno detrás de otro, esperando a que acabe el anterior

**2.** Un ordenador con un solo núcleo reproduce música mientras descarga un fichero. ¿Cómo es posible?
- a) Los dos procesos usan el núcleo en el mismo instante
- b) La descarga se ejecuta en un servidor externo
- c) El sistema operativo alterna los procesos mediante cambios de contexto
- d) No es posible: hacen falta al menos dos núcleos

**3.** ¿Cuál de estos es un ámbito típico de la programación concurrente?
- a) Un programa que suma dos números introducidos por teclado y termina
- b) Un fichero de texto con la lista de la compra
- c) Una calculadora que realiza una única operación
- d) Un servidor web que atiende a muchos usuarios a la vez

**4.** Un editor de vídeo usa los 8 núcleos del ordenador para exportar un vídeo. Es un ejemplo de programación…
- a) distribuida
- b) paralela
- c) secuencial
- d) cooperativa

**5.** Un proyecto científico reparte un cálculo entre miles de ordenadores conectados por Internet. Es programación…
- a) paralela en un sistema fuertemente acoplado
- b) distribuida
- c) concurrente en un solo procesador
- d) secuencial

**6.** Compilas `Hijo.java` y ejecutas dos veces a la vez `java -cp bin PSP.Hijo`. ¿Cuántos procesos de `Hijo` hay?
- a) 2
- b) 1
- c) 3
- d) Ninguno: un `.class` no se puede ejecutar

**7.** ¿Qué ejecuta realmente el código de `Hijo.class`?
- a) El procesador, directamente, igual que un fichero `.exe` de Windows
- b) El compilador `javac`, que lo vuelve a compilar en cada ejecución
- c) El sistema operativo, que lo carga en memoria sin intermediarios
- d) La máquina virtual de Java (`java`)

**8.** Un programa está parado en `sc.nextLine()` esperando a que el usuario escriba. ¿En qué estado está?
- a) Listo
- b) En ejecución
- c) Bloqueado
- d) Terminado

**9.** El planificador de tiempo compartido le quita la CPU a un proceso porque se le ha acabado su cuanto. ¿A qué estado pasa?
- a) Listo
- b) Bloqueado
- c) Terminado
- d) Nuevo

**10.** ¿Qué guarda el sistema operativo al hacer un cambio de contexto?
- a) Una copia del código fuente del programa
- b) El estado del proceso, los registros del procesador y la información de memoria
- c) El PID y el nombre del programa, para volver a encontrarlo
- d) Nada: el proceso vuelve a empezar desde el principio

**11.** ¿Por qué se dice que el cambio de contexto tiene un coste?
- a) Porque borra la memoria del proceso y hay que volver a cargarla desde el disco
- b) Porque obliga a recompilar el programa antes de devolverle la CPU
- c) Porque reinicia el proceso y se pierde el trabajo hecho hasta entonces
- d) Mientras se hace, la CPU no avanza en ningún proceso

**12.** En la planificación apropiativa…
- a) solo se cambia de proceso cuando el que se ejecuta termina o se queda bloqueado
- b) todos los procesos tienen la misma prioridad y se turnan por cuantos de tiempo
- c) el usuario decide qué proceso se ejecuta
- d) un proceso más prioritario puede quitar la CPU al que se está ejecutando

**13.** ¿Quién crea un proceso nuevo?
- a) El propio proceso, que se crea a sí mismo al ejecutarse
- b) El hardware del ordenador, sin que intervenga el sistema operativo
- c) Siempre el SO, a petición de otro proceso (el padre)
- d) El compilador, al generar el `.class`

**14.** ¿Qué contiene la cola de procesos preparados?
- a) Los procesos listos que esperan a que se les asigne la CPU
- b) Los procesos que esperan una operación de entrada/salida
- c) Los procesos que ya han terminado
- d) Los programas en disco que aún no se han ejecutado

**15.** ¿Qué comparten los hilos de un mismo proceso?
- a) La pila de llamadas, que es común a todos ellos
- b) La memoria del proceso
- c) El código fuente, pero no los datos
- d) El teclado y la pantalla, pero no la memoria

**16.** Un programa Java en ejecución tiene 3 hilos trabajando. ¿Cuántos PID tiene?
- a) 3
- b) 4
- c) 1
- d) Ninguno

**17.** Comparado con crear un proceso, crear un hilo es…
- a) más ligero
- b) más costoso
- c) igual de costoso
- d) más costoso, aunque solo en Windows

**18.** Un hilo modifica por error una variable compartida. ¿A quién afecta?
- a) Al hilo que la ha modificado, porque cada hilo trabaja con su copia
- b) A los demás procesos del sistema
- c) A ninguno, porque Java protege automáticamente las variables compartidas
- d) A todos los hilos del proceso

**19.** ¿Por qué los navegadores ejecutan cada pestaña en un proceso distinto y no en un hilo?
- a) Porque un proceso consume menos memoria que un hilo
- b) Por aislamiento: si una pestaña falla, el resto sigue funcionando
- c) Porque entre procesos es más fácil compartir datos
- d) Porque el SO no deja crear hilos

**20.** ¿Cómo se pasan datos normalmente procesos e hilos?
- a) Procesos: la memoria compartida; hilos: ficheros
- b) Los dos, mediante la memoria compartida del sistema operativo
- c) Procesos: flujos, ficheros o tuberías; hilos: la memoria compartida
- d) Los dos, mediante el código de salida

**Hoja de respuestas**

| Pregunta | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 |
|---|---|---|---|---|---|---|---|---|---|---|
| Respuesta | | | | | | | | | | |
| **Pregunta** | **11** | **12** | **13** | **14** | **15** | **16** | **17** | **18** | **19** | **20** |
| Respuesta | | | | | | | | | | |

---

## Parte B · Análisis de código (2 puntos)

> En esta parte **no tienes que escribir programas**: lee el código, explica qué ocurre y, cuando se pida, indica qué línea cambiarías.

### B1 · Predecir la salida (0,5 puntos)

¿Qué imprime este programa? **Justifica cada línea.**

```java
import java.util.concurrent.TimeUnit;

public class B1 {
    public static void main(String[] args) throws Exception {
        Process p = new ProcessBuilder("sleep", "3").start();
        boolean fin = p.waitFor(1, TimeUnit.SECONDS);
        System.out.println("A: " + fin);
        System.out.println("B: " + p.isAlive());
        p.destroy();
        System.out.println("C: " + p.waitFor());
        System.out.println("D: " + p.isAlive());
    }
}
```

### B2 · Encontrar el error (0,75 puntos)

Este programa debería mostrar las tres frutas ordenadas, pero al ejecutarlo **se queda colgado y no muestra nada**.

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class B2 {
    public static void main(String[] args) throws Exception {
        Process p = new ProcessBuilder("sort").start();

        PrintWriter pw = new PrintWriter(p.getOutputStream());
        pw.println("pera");
        pw.println("kiwi");
        pw.println("manzana");
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        System.out.println("Código: " + p.waitFor());
    }
}
```

1. ¿Por qué se queda colgado? ¿Qué está esperando cada proceso?
2. ¿Por qué no es suficiente el `flush()`?
3. ¿Qué línea cambiarías y por cuál? Basta con indicar la línea.

### B3 · El bloqueo (0,75 puntos)

`Hablador` es un programa Java que escribe en su salida el número de líneas que recibe por argumento. Con `10` el programa funciona, pero con `100000` **se queda colgado para siempre**.

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class B3 {
    public static void main(String[] args) throws Exception {
        Process p = new ProcessBuilder("java", "-cp", "bin", "PSP.Hablador", args[0]).start();
        int codigo = p.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        int lineas = 0;
        while (br.readLine() != null) {
            lineas++;
        }
        System.out.println(lineas + " líneas, código " + codigo);
    }
}
```

1. Explica por qué se cuelga con 100.000 líneas y no con 10.
2. ¿Cómo se llama esta situación?
3. ¿Qué línea moverías, y a dónde, para solucionarlo? No hace falta reescribir el programa.

---

## Parte práctica (5,5 puntos)

### C1 · Ejecutor de tareas (2 puntos)

La clase `Tarea` **ya está hecha y no hay que modificarla**. Simula una tarea que tarda varios segundos: escribe una línea de progreso por segundo y termina con el código de salida que se le indique. Recibe dos argumentos: los segundos que tarda y el código de salida con el que termina. Por ejemplo, con `3` y `0` tarda 3 segundos y termina con código 0.

Completa la clase **`EjecutorTareas`**, que recibe tres argumentos: los segundos que tardará la tarea, el código con el que terminará y el **tiempo máximo** que el ejecutor está dispuesto a esperar. El programa debe:

1. Lanzar la `Tarea` con los dos primeros argumentos, haciendo que **su salida se guarde en el fichero `tarea.log`** (sobrescribiéndolo si existe). Mostrar el **PID** de la tarea.
2. Esperar a la tarea **como máximo** el tiempo indicado en el tercer argumento. Si no ha terminado, destruirla y avisar.
3. Mostrar el código de salida: «Tarea correcta» si es 0, o «La tarea ha fallado» con el código en otro caso.
4. Mostrar el contenido de `tarea.log`, cada línea precedida de `[TAREA]`.

**Ejemplo de ejecución** (argumentos `8 0 3`: la tarea tarda 8 s y solo se esperan 3):

```text
Tarea lanzada con PID 44511
La tarea no ha terminado en 3 s: se destruye
La tarea ha fallado (código 143)
[TAREA] Tarea iniciada: durará 8 s
[TAREA] Paso 1 de 8 completado
[TAREA] Paso 2 de 8 completado
```

Pruébalo con las tres configuraciones preparadas: `3 0 5` (termina bien), `2 4 5` (termina con error) y `8 0 3` (se destruye). El código que obtengas al destruir la tarea puede variar según el sistema operativo.

### C2 · Analizador de notas en paralelo (3,5 puntos)

En la carpeta `datos` hay tres ficheros (`grupoA.txt`, `grupoB.txt` y `grupoC.txt`) con una nota por línea. Algunas líneas pueden no ser notas válidas.

**C2.1 · `HijoEstadistica` (0,75 puntos)**

Programa que recibe por `args` el nombre del grupo y lee las notas de su **entrada estándar**, una por línea. Una nota es válida si es un número entre 0 y 10 (con punto decimal, por ejemplo `6.25`).

- Si una línea no es una nota válida, escribe un aviso en su **salida de error**, con el nombre del grupo y el número de línea, y la ignora.
- Al terminar de leer, escribe en su **salida estándar** exactamente estas cuatro líneas:

```text
CANTIDAD <número de notas válidas>
MEDIA <media con 2 decimales>
MAXIMA <nota máxima>
MINIMA <nota mínima>
```

- Si no hay ninguna nota válida, no escribe resultados, avisa por la salida de error y termina con **código de salida 2**.

**C2.2 · `Padre` (2 puntos)**

1. Lanza un `HijoEstadistica` por cada fichero y muestra el **PID** de cada uno. Los tres deben ejecutarse **en paralelo**: lánzalos todos primero y recoge los resultados después, sin esperar a que termine uno para lanzar el siguiente.
2. Cada hijo debe leer las notas **directamente de su fichero, como si las escribiera alguien por teclado**: configura la entrada estándar del hijo para que sea el fichero. **El padre no debe abrir ni leer los ficheros de notas.**
3. Los errores de todos los hijos se **añaden** al fichero `errores.log`, sin borrar lo que hubiera.
4. Recoge la salida de cada hijo y muestra sus resultados.
5. Si un hijo termina con un código distinto de 0, lo indica y no tiene en cuenta ese grupo.
6. Al final, muestra la **media global**: la media de todas las notas válidas de los grupos que han terminado bien. Como cada grupo tiene un número distinto de notas, **no vale hacer la media de las medias**. Por ejemplo, si el grupo X tiene 2 notas con media 10 y el grupo Y tiene 8 notas con media 5, la media global es (2·10 + 8·5) / 10 = **6**, y no (10 + 5) / 2 = 7,5. Usa los datos `CANTIDAD` y `MEDIA` de cada hijo para calcularla.

**C2.3 · Documentación (0,25 puntos)**

Comenta las clases y los métodos: qué hacen, qué reciben y qué devuelven.

**C2.4 · Reflexión: procesos o hilos (0,5 puntos)**

En un comentario al principio de la clase `Padre`, explica en 5-10 líneas qué cambiaría si, en lugar de lanzar tres procesos `HijoEstadistica`, hicieras el cálculo con tres **hilos** dentro del mismo programa. Indica al menos una ventaja y un inconveniente.

---

## Soluciones

> ⛔ **Para.** Si estás haciendo el simulacro, no sigas leyendo hasta terminar.

### Parte A · plantilla oficial

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| a | c | d | b | b | a | d | c | a | b | d | d | c | a | b | c | a | d | b | c |

| Nº | Respuesta | Por qué (y la trampa) |
|---|---|---|
| 1 | a | Concurrente = varios procesos que progresan a la vez (real o aparente) y **colaboran**. La d es la secuencial |
| 2 | c | Un núcleo no da paralelismo real: el SO reparte la CPU en turnos cortos con **cambios de contexto** y parece simultáneo. La a es imposible con un núcleo |
| 3 | d | Un servidor web atiende muchas peticiones a la vez. El resto son programas secuenciales sencillos (o ni siquiera programas) |
| 4 | b | Varios núcleos de **un mismo ordenador**, memoria común = **paralela** (fuertemente acoplado) |
| 5 | b | Ordenadores independientes que colaboran por la **red** = **distribuida**. Trampa: «fuertemente acoplado» es memoria compartida en una máquina |
| 6 | a | Cada ejecución crea **un proceso distinto**, aunque usen el mismo `.class` |
| 7 | d | El `.class` es **bytecode**: lo ejecuta la **JVM** (el proceso `java`), no el procesador directamente |
| 8 | c | Espera una **E/S** (que el usuario escriba): **bloqueado**. Aunque tuviera la CPU no podría avanzar. Trampa: «listo» |
| 9 | a | Agotar el cuanto no bloquea: solo espera la CPU, así que vuelve a **listos**. Trampa: «bloqueado» es para quien espera E/S |
| 10 | b | El **contexto**: estado, registros (contador de programa incluido) e información de memoria, para continuar donde se quedó |
| 11 | d | Guardar y restaurar el contexto es trabajo del SO: **mientras tanto ningún proceso avanza**. No se borra memoria, ni se recompila, ni se reinicia |
| 12 | d | **Apropiativa** = el SO puede **expulsar** al que se ejecuta (por ejemplo, llega uno más prioritario). La a describe la **no apropiativa** |
| 13 | c | Solo el **SO** crea procesos, cuando otro proceso (el **padre**) se lo pide (`ProcessBuilder.start()`) |
| 14 | a | Cola de **preparados** = procesos **listos** que solo esperan la CPU. Los que esperan E/S están en las colas de dispositivos |
| 15 | b | Comparten la **memoria** (datos y código). Cada hilo tiene **su propia pila** y registros. Trampa: la a |
| 16 | c | El PID es **del proceso**: una JVM = un PID, tenga los hilos que tenga |
| 17 | a | Un hilo **no necesita espacio de memoria propio**: crearlo y cambiar entre hilos es más **ligero** |
| 18 | d | Comparten memoria: el cambio lo ven **todos los hilos del proceso** (por eso hay que sincronizar). Los otros procesos no se enteran |
| 19 | b | **Aislamiento**: cada proceso tiene su memoria; si una pestaña falla, las demás siguen |
| 20 | c | Procesos (sin memoria común): **flujos, ficheros o tuberías**. Hilos: la **memoria compartida** |

> **Estrategia con la penalización de −1/3:** si dudas entre las cuatro, déjala en blanco (contestar al azar da de media 1/4 · 1 − 3/4 · 1/3 = **0**). Si puedes **descartar al menos una**, contesta: entre tres opciones el azar ya da +0,11 de media, y entre dos, +0,33.

### Parte B

**B1 · Salida** (en Linux, donde se hizo el examen):

```text
A: false
B: true
C: 143
D: false
```

| Línea | Por qué |
|---|---|
| `A: false` | `waitFor(1, SECONDS)` espera **como máximo** 1 s y devuelve un **boolean** (¿terminó?). `sleep 3` dura 3 s: no ha terminado → `false`. El padre **no** se queda esperando más |
| `B: true` | Ha pasado 1 s de 3: el hijo **sigue vivo** |
| `C: 143` | `destroy()` envía **SIGTERM** y **no espera**. `waitFor()` **sin argumentos** sí bloquea hasta que muere y devuelve el **código de salida**: 128 + 15 (SIGTERM) = **143**. Distinto de 0 porque no terminó por sí mismo. (En Windows sería 1, y además allí no existe `sleep`) |
| `D: false` | Tras el `waitFor()` el proceso ya ha terminado seguro |

> ⚠️ La clave es distinguir los dos `waitFor`: **con tiempo → `boolean`** y no espera más de lo indicado; **sin argumentos → `int`** (código de salida) y espera lo que haga falta.

**B2 · `sort` sin cerrar**

1. **Interbloqueo:** `sort` necesita **toda** la entrada para ordenar, y solo sabe que se ha acabado cuando recibe el **fin de fichero (EOF)**, que llega al **cerrar** su entrada. Mientras tanto no escribe nada. El padre está bloqueado en `br.readLine()` esperando la salida de `sort`, y `sort` está bloqueado esperando más entrada. Cada uno espera al otro.
2. `flush()` **envía** las tres líneas que había en el búfer, pero **deja el flujo abierto**: `sort` no recibe EOF y sigue esperando por si llegan más.
3. Cambiar **`pw.flush();`** por **`pw.close();`** (cerrar envía lo pendiente y además manda el EOF). Saldría `kiwi`, `manzana`, `pera` y `Código: 0`.

> ⚠️ **Error típico:** crear el `PrintWriter` con autoflush, `new PrintWriter(p.getOutputStream(), true)`. **No arregla nada**: es otro `flush()` (después de cada `println`), y sigue sin haber EOF. El problema no es enviar los datos, es **cerrar** el canal.

**B3 · `waitFor()` antes de leer**

1. La salida del hijo va a una **tubería** con un **búfer limitado** (unos 64 KB en Linux). Con 10 líneas todo cabe: el hijo escribe, termina, `waitFor()` vuelve y luego se leen las líneas. Con 100.000 líneas el búfer **se llena**: el hijo se **bloquea** al escribir hasta que alguien lea, pero el padre no lee porque está bloqueado en `waitFor()` esperando a que el hijo termine.
2. **Interbloqueo** (*deadlock*): el padre espera al hijo y el hijo espera al padre. **No es un problema de tiempo**: por mucho que se espere, ninguno avanza.
3. Mover **`int codigo = p.waitFor();`** a **después del bucle `while`** (justo antes del `println`). Regla de oro: **primero leer toda la salida, después esperar**.

### Parte práctica

La solución completa, comentada y con Javadoc está en [`solucion/`](solucion/). Cumple el enunciado al pie de la letra (mensajes exactos, formato de cuatro líneas, código 2, PID, `appendTo` y documentación). Para ejecutarla, desde `solucion/`:

```bash
javac -d bin src/PSP/c1/*.java src/PSP/c2/*.java
java -cp bin PSP.c1.EjecutorTareas 3 0 5
java -cp bin PSP.c1.EjecutorTareas 2 4 5
java -cp bin PSP.c1.EjecutorTareas 8 0 3
java -cp bin PSP.c2.Padre
```

En IntelliJ, con las configuraciones del desplegable (`C1_correcta`, `C1_error`, `C1_destruida` y `C2_padre`).

**Salidas comprobadas** (JDK 25 compilando para Java 11, en Windows; en Linux el código del destruido es **143**):

```text
=== 3 0 5
Tarea lanzada con PID 16044
Tarea correcta
[TAREA] Tarea iniciada: durará 3 s
[TAREA] Paso 1 de 3 completado
[TAREA] Paso 2 de 3 completado
[TAREA] Paso 3 de 3 completado
[TAREA] Tarea terminada con código 0

=== 2 4 5
Tarea lanzada con PID 23796
La tarea ha fallado (código 4)
[TAREA] …
[TAREA] Tarea terminada con código 4

=== 8 0 3
Tarea lanzada con PID 3664
La tarea no ha terminado en 3 s: se destruye
La tarea ha fallado (código 1)            ← 143 en Linux
[TAREA] Tarea iniciada: durará 8 s
[TAREA] Paso 1 de 8 completado
[TAREA] Paso 2 de 8 completado

=== Padre
Lanzado grupoA con PID 13224
Lanzado grupoB con PID 11556
Lanzado grupoC con PID 13920
grupoA: CANTIDAD 5 · MEDIA 7.25 · MAXIMA 9.0 · MINIMA 5.5
grupoB: CANTIDAD 4 · MEDIA 5.88 · MAXIMA 10.0 · MINIMA 3.5
grupoC: el hijo terminó con código 2 → no se tiene en cuenta
MEDIA GLOBAL: 6,64 (9 notas válidas)
```

`errores.log` (se **añade** a lo que hubiera):

```text
grupoC, línea 1: no es una nota válida: "no presentado"
grupoC, línea 2: no es una nota válida: ""
grupoC, línea 3: no es una nota válida: "NP"
grupoC: no hay ninguna nota válida
grupoB, línea 2: no es una nota válida: "siete"
grupoB, línea 4: no es una nota válida: "11"
grupoB, línea 6: no es una nota válida: "-1"
```

Los avisos de los grupos salen **mezclados y en distinto orden** en cada ejecución: los tres hijos escriben a la vez en el mismo fichero. Es la prueba de que van en paralelo.

**Datos de prueba:**

| Fichero | Contenido | Válidas | Resultado |
|---|---|---|---|
| `grupoA.txt` | `7.5`, `8`, `6.25`, `9`, `5.5` | 5 | media 7,25 · máx 9 · mín 5,5 |
| `grupoB.txt` | `4`, `siete`, `10`, `11`, `3.5`, `-1`, `6` | 4 | media 5,875 → «5.88» · máx 10 · mín 3,5 |
| `grupoC.txt` | `no presentado`, línea vacía, `NP` | 0 | código 2 |

Media global con `CANTIDAD × MEDIA`: (5 · 7,25 + 4 · 5,88) / 9 = **6,64**. La media de las medias, (7,25 + 5,88) / 2 = 6,57, sería **incorrecta**.

**Decisiones que hay que saber justificar (puntúan en «requisitos», «comentado» y «manejo de errores»):**

| Decisión | Por qué |
|---|---|
| `waitFor(n, TimeUnit.SECONDS)` en C1 | Devuelve **boolean**; el código se pide después con `waitFor()` (o `exitValue()`) |
| `destroy()` + `waitFor()` | `destroy()` **no espera**; el `waitFor()` posterior da el código real (143). Con margen de 2 s y `destroyForcibly()` por si ignora el SIGTERM |
| `redirectOutput(new File("tarea.log"))` | `new File` = **sobrescribe** (lo que pide el enunciado) |
| `redirectErrorStream(true)` | Los errores del hijo también van al log; ninguna tubería queda sin leer |
| Leer `tarea.log` con `FileReader` | La salida fue al **fichero**: `getInputStream()` no traería nada |
| `redirectInput(new File("datos", grupo + ".txt"))` | El hijo lee el fichero **como si fuera el teclado**; el padre no lo abre |
| `Redirect.appendTo(errores.log)` | **Añade**; con `new File` cada hijo borraría lo del anterior. Y **no** se borra al empezar («sin borrar lo que hubiera») |
| Bucle de `start()` y **después** bucle de lectura + `waitFor()` | Así los tres van **en paralelo** |
| Leer toda la salida **antes** de `waitFor()` | Regla de oro (justo lo de B3) |
| Validar con `matches("\\d+(\\.\\d+)?")` y rango 0-10 | `Double.parseDouble` aceptaría `NaN`, `1e1` o `7d`, que no son notas; `11` y `-1` se rechazan por rango o por el signo |
| `String.format(Locale.ROOT, "%.2f", media)` | En un sistema en español `%.2f` escribe `7,25` y el `Double.parseDouble` del padre fallaría |
| Código **2** si no hay notas, **1** si falta el argumento | Lo pide el enunciado y distingue los dos fallos |
| Avisos por **stderr**, datos por **stdout**, éxito por el **código de salida** | Cada canal para lo suyo: el padre lee solo datos limpios |
| Javadoc en cada clase y método (qué hace, `@param`, `@return`, `@throws`) | Es el C2.3 |
| `if/else` y no `switch` con flechas | El proyecto pide **JDK 11 o superior**; las flechas son de Java 14 |

**C2.4 · Reflexión modelo** (está en la cabecera de `Padre.java`): con hilos todo iría en una sola JVM y se esperaría con `join()`; comparten memoria, así que sobran las redirecciones y el formato de texto. **Ventaja:** un hilo es mucho más ligero que lanzar una JVM y compartir datos es directo. **Inconvenientes:** condiciones de carrera (hay que sincronizar con `synchronized`) y pérdida de aislamiento (un fallo grave o un `System.exit` en un hilo tumba todo, y no hay códigos de salida).

---

## Lo que este examen enseña para el de la UT2

- **Mismo formato de test:** 20 preguntas, +1 / −1/3 / 0, sobre 2,5. Muchas son **escenarios** («un editor de vídeo usa 8 núcleos…», «un programa parado en `nextLine()`…») en vez de definiciones.
- **La práctica parte de un proyecto IntelliJ del profesor** con las clases a completar (`// TODO`), una ya hecha que **no se toca**, configuraciones de ejecución preparadas y paquetes por ejercicio (`PSP.c1`, `PSP.c2`…).
- **El enunciado fija la salida exacta** (mensajes, formato de líneas, códigos): hay que copiarla **literalmente**.
- **Siempre hay documentación** (comentar clases y métodos: qué hacen, qué reciben y qué devuelven) y una **reflexión escrita** en un comentario.
- **Se evalúa** compilación, requisitos, código comentado y **manejo de errores** (argumentos incorrectos, datos no válidos).
- **Entrega:** `Apellidos_Nombre.zip` **sin `bin`**, por el cuestionario del aula. Nombre en la cabecera de cada clase.
- Sin Internet ni IA: las plantillas hay que **sabérselas de memoria**.
