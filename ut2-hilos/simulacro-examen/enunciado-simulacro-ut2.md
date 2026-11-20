# Simulacro de examen UT2 (hilos) · PSP

> Hecho con el **mismo formato que el examen real de la UT1** ([`../../ut1-procesos/examen/examen-ut1.md`](../../ut1-procesos/examen/examen-ut1.md)) y con ejercicios del estilo de los que proponen los apuntes de la UT2 (Despliegue, Corredores, Vocales).
> **Cómo hacerlo:** test en papel (20 min) y práctica en el PC (2 h) **sin mirar nada**. Copia [`ut2-simulacro-alumno/`](ut2-simulacro-alumno/) a otra carpeta, ábrela en IntelliJ y completa los `// TODO`. Al terminar, compara con [`solucion/`](solucion/) y con las soluciones del final de este documento.

## Índice

- [Normas y puntuación](#normas-y-puntuación)
- [Parte A · Tipo test](#parte-a--tipo-test)
- [Parte práctica (5,5 puntos)](#parte-práctica-55-puntos)
- [Soluciones](#soluciones)

---

## Normas y puntuación

| Parte | Puntos | Qué es |
|---|---|---|
| **A** · Tipo test | 4,5 | 20 preguntas · acierto +1, error −1/3, en blanco 0. Nota = (aciertos − errores / 3) / 20 × 4,5 |
| **Práctica** | 5,5 | C1 (2) · C2 (1,5) · C3 (2), en el PC con IntelliJ |
| **Total** | **10** | |

> El reparto real del examen de la UT2 no lo sabemos. En la UT1 la teoría fue test (2,5) + análisis de código (2); como en la UT2 no habrá análisis de código, aquí el test vale esos 4,5 puntos.

**Instrucciones de la parte práctica** (como en el examen real):
- **Proyecto de partida:** `UT2_Simulacro_Alumno` (carpeta `ut2-simulacro-alumno/`). Incluye la clase `Corredor` (ya hecha, **no se modifica**), el fichero `datos/texto.txt` y las clases que hay que completar, en los paquetes `PSP.c1`, `PSP.c2` y `PSP.c3`. El proyecto compila en la carpeta `bin`.
- **Criterios de evaluación:** correcta compilación y ejecución · cumplimiento de los requisitos funcionales · código claro y comentado · legibilidad, mensajes claros y manejo de errores.
- **Entrega:** todos los `.java` en la carpeta del proyecto, comprimido en `Apellidos_Nombre.zip`, **sin la carpeta `bin`**. Nombre y apellidos en la cabecera de cada clase.
- Sin Internet, sin IA y sin apuntes.

---

## Parte A · Tipo test

Usa el **simulacro de 20 preguntas** del resumen: [`../apuntes/resumen-rapido-ut2.md`, apartado 11](../apuntes/resumen-rapido-ut2.md#11-test-simulacro-de-20-preguntas-y-banco-de-45). Tiene hoja de respuestas y la plantilla oculta. **20 minutos.**

---

## Parte práctica (5,5 puntos)

### C1 · Despliegue con dependencias (2 puntos)

Un proceso de despliegue tiene siete tareas. Cada tarea es un **hilo** y **ninguna puede empezar antes de que terminen las tareas de las que depende**:

| Tarea | Duración | Depende de |
|---|---|---|
| T1 · Descargar el código | 1000 ms | — |
| T2 · Compilar el backend | 2000 ms | T1 |
| T3 · Compilar el frontend | 1500 ms | T1 |
| T4 · Tests del backend | 1500 ms | T2 |
| T5 · Tests del frontend | 1000 ms | T3 |
| T6 · Crear la imagen Docker | 1000 ms | T4 y T5 |
| T7 · Desplegar en el servidor | 500 ms | T6 |

**C1.1 · `Tarea` (0,75 puntos).** Clase que **extiende `Thread`**, con el constructor `Tarea(String nombre, int ms, Thread... dependencias)`. En `run()`:
- Espera con `join()` a **todas** sus dependencias.
- Muestra `Empieza <nombre> (<ms> ms)`, trabaja con `sleep()` el tiempo indicado y muestra `Termina <nombre> (<ms> ms)`, donde `<ms>` son los milisegundos transcurridos desde el inicio del despliegue.
- Si el hilo es interrumpido, muestra `<nombre> interrumpida: no se completa` y termina.
- Método `getMs()`, que devuelve la duración de la tarea.

**C1.2 · `Despliegue` (0,75 puntos).** Programa principal:
1. Recibe un argumento opcional: `normal` (por defecto) o `inverso`. Con cualquier otro valor, o con más de un argumento, muestra por la **salida de error** `Uso: Despliegue [normal|inverso]` y termina con **código 1**.
2. Crea las siete tareas con sus dependencias y muestra `Despliegue en orden <orden>`.
3. Las arranca: de T1 a T7, o de T7 a T1 si el orden es `inverso`.
4. Espera a que termine el despliegue y muestra `Despliegue completado en <total> ms (en secuencial serían <suma> ms)`, donde `<suma>` es la suma de las duraciones (calculada con `getMs()`, no escrita a mano).

**C1.3 · Reflexión (0,5 puntos).** En un comentario al principio de `Despliegue`, explica en 5-10 líneas si el despliegue sigue funcionando cuando las tareas se arrancan en orden inverso, y por qué. Si no está garantizado, di cómo lo harías seguro.

**Ejemplo de ejecución** (los milisegundos varían un poco):

```text
Despliegue en orden normal
Empieza T1 · Descargar el código (0 ms)
Termina T1 · Descargar el código (1020 ms)
Empieza T3 · Compilar el frontend (1020 ms)
Empieza T2 · Compilar el backend (1020 ms)
Termina T3 · Compilar el frontend (2522 ms)
Empieza T5 · Tests del frontend (2522 ms)
Termina T2 · Compilar el backend (3023 ms)
Empieza T4 · Tests del backend (3023 ms)
Termina T5 · Tests del frontend (3524 ms)
Termina T4 · Tests del backend (4523 ms)
Empieza T6 · Crear la imagen Docker (4523 ms)
Termina T6 · Crear la imagen Docker (5525 ms)
Empieza T7 · Desplegar en el servidor (5525 ms)
Termina T7 · Desplegar en el servidor (6028 ms)
Despliegue completado en 6028 ms (en secuencial serían 8500 ms)
```

Pruébalo con las configuraciones `C1 · Despliegue (orden normal)` y `C1 · Despliegue inverso`.

### C2 · Carrera con tiempo límite (1,5 puntos)

La clase `Corredor` **ya está hecha y no hay que modificarla**. Es un `Runnable` que da 10 pasos (entre 100 y 400 ms cada uno), mostrando `Corredor N avanza al paso P`, y al terminar llama a `meta.cruzar(nombre)`. Si le interrumpen, muestra `Corredor N se retira: tiempo agotado` y termina.

**C2.1 · `Meta` (0,5 puntos).** Objeto **compartido** por todos los corredores:
- `int cruzar(String nombre)`: registra la llegada, muestra `<nombre> cruza la meta en posición <p>` y devuelve la posición (1 = primero). Dos corredores **nunca** pueden recibir la misma posición.
- `List<String> getClasificacion()`: devuelve los nombres en orden de llegada.

**C2.2 · `Carrera` (0,75 puntos).** Recibe dos argumentos: el número de corredores (entre 3 y 5) y el tiempo máximo de la carrera en segundos (mayor que 0).
1. Si los argumentos no son correctos (no son dos, no son números o están fuera de rango), muestra un mensaje claro por la **salida de error** y termina con **código 1**.
2. Muestra `Salida de <n> corredores (tiempo máximo: <t> s)` y lanza un hilo por corredor (`Corredor 1`, `Corredor 2`…), todos con la **misma** meta.
3. Espera a los corredores **como máximo el tiempo indicado en total** (no ese tiempo para cada uno).
4. Los que no hayan llegado se retiran: **interrúmpelos** y espera a que terminen.
5. Muestra `CLASIFICACIÓN`, una línea `<posición>. <nombre>` por cada corredor que ha llegado y `Retirados: <n>`.

**C2.3 · Documentación (0,25 puntos).** Comenta las clases `Meta` y `Carrera` y sus métodos: qué hacen, qué reciben y qué devuelven.

**Ejemplo de ejecución** (argumentos `4 2`; se omiten las líneas `avanza al paso`):

```text
Salida de 4 corredores (tiempo máximo: 2 s)
Corredor 2 cruza la meta en posición 1
Corredor 1 se retira: tiempo agotado
Corredor 4 se retira: tiempo agotado
Corredor 3 se retira: tiempo agotado
CLASIFICACIÓN
1. Corredor 2
Retirados: 3
```

Pruébalo con `C2 · Carrera 4 5`, `C2 · Carrera 4 2` y `C2 · Carrera 7 3` (esta última debe dar error).

### C3 · Recuento de vocales (2 puntos)

Cinco hilos cuentan las vocales de un fichero de texto: **cada hilo se encarga de una vocal**, pero **todos actualizan el mismo recuento**. Se pasa el texto a minúsculas y solo cuentan `a`, `e`, `i`, `o` y `u` (las vocales con tilde no cuentan).

**C3.1 · `Recuento` (0,5 puntos).** Objeto **compartido** con el total de vocales y el número de cada una:
- `void sumar(char vocal)`: suma una aparición de esa vocal.
- `int getCantidad(char vocal)` y `int getTotal()`.

**C3.2 · `ContadorVocal` (0,5 puntos).** `Runnable` que recibe el texto, su vocal y el recuento, recorre el texto y **llama a `sumar` cada vez que encuentra su vocal**.

**C3.3 · `ContarVocales` (0,5 puntos).** Recibe por argumento la ruta del fichero.
1. Sin argumento: `Uso: ContarVocales <fichero>` por la salida de error y código 1. Si el fichero no se puede leer: `No se puede leer el fichero: <ruta>` por la salida de error y código 1.
2. Lanza los cinco hilos, espera a todos y muestra, exactamente:

```text
a: <n>
e: <n>
i: <n>
o: <n>
u: <n>
TOTAL: <n>
Comprobación secuencial: <n> → CORRECTO
```

   La última línea compara el total con una cuenta hecha en un solo hilo; si no coinciden, dice `INCORRECTO`.

**C3.4 · Reflexión (0,5 puntos).** En un comentario al principio de `ContarVocales`, explica en 5-10 líneas qué pasaría sin sincronización, si bastaría con `volatile` y qué otra forma correcta conoces. Indica al menos una ventaja y un inconveniente.

**Resultado con `datos/texto.txt`:**

```text
a: 17200
e: 13200
i: 6000
o: 15600
u: 6000
TOTAL: 58000
Comprobación secuencial: 58000 → CORRECTO
```

---

## Soluciones

> ⛔ **Para.** Si estás haciendo el simulacro, no sigas hasta terminar.

Código completo, con Javadoc y comentarios, en [`solucion/`](solucion/). Todo compilado con `--release 11` y ejecutado con las seis configuraciones.

### C1 · Despliegue

**`Tarea.run()`, lo importante:**

```java
for (Thread dependencia : dependencias) {
    esperarA(dependencia);                 // join() a CADA dependencia (T6 tiene dos)
}
System.out.printf("Empieza %s (%d ms)%n", getName(), transcurrido());
Thread.sleep(ms);                          // simula el trabajo
System.out.printf("Termina %s (%d ms)%n", getName(), transcurrido());
```

- `Thread... dependencias` (varargs) permite crear `new Tarea("T1…", 1000)` sin dependencias y `new Tarea("T6…", 1000, t4, t5)` con dos. Dentro, `dependencias` es un `Thread[]`.
- `super(nombre)` en el constructor: el nombre de la tarea es el nombre del hilo, y `getName()` lo devuelve (por eso conviene `extends Thread`).
- **El `join()` va dentro del `run()` de la tarea que depende**, no en el `main`: así cada tarea empieza en cuanto terminan **sus** dependencias, y T2 y T3 van en paralelo.
- El `main` solo espera a **T7**: si T7 ha terminado, han terminado todas, porque todas son dependencias suyas, directas o indirectas.
- **Tiempo total ≈ 6 s:** es el **camino crítico** T1 → T2 → T4 → T6 → T7 = 1 + 2 + 1,5 + 1 + 0,5. En secuencial, la suma: 8,5 s.

**C1.3 · el orden inverso** (la pregunta trampa):

```java
private static void esperarA(Thread dependencia) throws InterruptedException {
    while (dependencia.getState() == Thread.State.NEW) {
        Thread.sleep(1);                   // aún no la han arrancado: espero
    }
    dependencia.join();
}
```

`join()` sobre un hilo que aún no ha hecho `start()` (estado **NEW**) **vuelve al instante, sin esperar**. Arrancando de T7 a T1 «funciona» casi siempre, porque el `main` hace los siete `start()` en microsegundos, antes de que T7 llegue a su `join()`. Pero **no está garantizado**: si el `main` se retrasara entre dos `start()`, T7 empezaría antes de tiempo (es una condición de carrera sobre el orden de arranque). `esperarA` lo hace seguro. La alternativa sencilla es arrancar siempre en orden de dependencias.

### C2 · Carrera

**`Meta`:** todos los métodos `synchronized`. En `cruzar`, **añadir a la lista, calcular la posición y anunciarla** es una operación compuesta: sin `synchronized`, dos corredores que llegan a la vez podrían recibir la misma posición, o la `ArrayList` podría perder un elemento, porque no es segura con varios hilos. `getClasificacion()` devuelve una **copia** (`new ArrayList<>(clasificacion)`), para que nadie modifique la lista interna desde fuera.

**`Carrera`, la espera con hora límite común** (como en el ejercicio 2.2 «El examen» de la relación):

```java
long limite = System.currentTimeMillis() + ms;
for (Thread h : hilos) {
    long queda = limite - System.currentTimeMillis();
    if (queda > 0) h.join(queda);          // ¡join(0) = esperar SIN límite!
}
for (Thread h : hilos) if (h.isAlive()) h.interrupt();   // retira a los rezagados
for (Thread h : hilos) h.join();                         // espera a que se retiren de verdad
```

- `join(ms)` **a cada uno** sumaría las esperas: con 4 corredores y 2 s, el último podría tener hasta 8 s.
- `interrupt()` despierta el `sleep()` del corredor con `InterruptedException`, y `Corredor` se retira en su `catch`.
- El último bucle de `join()` es necesario: después de `interrupt()` el hilo todavía tiene que ejecutar su `catch`. Si se mostrara la clasificación sin esperar, los mensajes «se retira» podrían salir después de ella.
- Argumentos: `Integer.parseInt` dentro de `try/catch (NumberFormatException)` y comprobación de rango. Mensaje por `System.err` y `System.exit(1)`.

### C3 · Vocales

**`Recuento.sumar` es `synchronized`.** Comprobado: **sin** `synchronized`, en cinco ejecuciones salió un total de 36 106, 39 974, 34 530, 50 923 y 35 226, en vez de 58 000. `total++` son tres pasos y los hilos se pisan.

**C3.4 · reflexión modelo** (está en la cabecera de `ContarVocales.java`):
- **Sin sincronizar:** condición de carrera; se pierden vocales y el total cambia en cada ejecución.
- **`volatile` no basta:** da visibilidad, no atomicidad.
- **Alternativas correctas:** `AtomicInteger` (`incrementAndGet`, sin cerrojo), o que **cada hilo cuente en una variable local y sume una sola vez al final** dentro de un `synchronized`.
- **Ventaja** de la última: 5 accesos sincronizados en vez de 58 000, mucho más rápida. **Inconveniente:** el recuento solo es correcto al final, no se puede consultar mientras cuentan.

**Lectura del fichero:** `Files.readAllBytes(ruta)` y `new String(…, StandardCharsets.UTF_8).toLowerCase()`. La `IOException` se captura y da el mensaje del enunciado con código 1.

### Lista de comprobación (la del resumen, aplicada)

- [ ] Nombre en la cabecera de **las 7 clases** que completas.
- [ ] `Corredor` sin tocar.
- [ ] Mensajes **idénticos** a los del enunciado.
- [ ] Errores de argumentos por `System.err` y código 1 (C1, C2 y C3).
- [ ] Javadoc en `Meta` y `Carrera` (C2.3) y, mejor, en todas.
- [ ] Reflexiones C1.3 y C3.4 escritas.
- [ ] Ejecutado varias veces con las seis configuraciones.
- [ ] Zip `Apellidos_Nombre.zip` **sin `bin`**.
