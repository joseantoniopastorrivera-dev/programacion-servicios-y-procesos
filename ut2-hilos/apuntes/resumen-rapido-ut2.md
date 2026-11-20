# UT2 · Hilos — Resumen rápido para el examen

> **Examen:** lunes 23-11-2026, 8:00 · 2 h 30 min · 50 % del RA2.
> **Formato:** test + 3 ejercicios prácticos en el PC. **Sin análisis de código** (en la UT1 sí lo hubo).
> **Cómo será, según el examen real de la UT1** ([`../../ut1-procesos/examen/examen-ut1.md`](../../ut1-procesos/examen/examen-ut1.md)):
> - **Test de 20 preguntas** con una respuesta correcta: acierto **+1**, error **−1/3**, en blanco **0**. Muchas son **escenarios** («un editor de vídeo usa 8 núcleos…») más que definiciones. → apartado 11.
> - **Práctica sobre un proyecto IntelliJ del profesor**: clases con `// TODO` agrupadas por ejercicio (`PSP.c1`, `PSP.c2`, `PSP.c3`…), alguna clase **ya hecha que no se toca**, configuraciones de ejecución preparadas. El enunciado fija la **salida exacta**. → apartado 9.
> - **Se evalúa:** compila y se ejecuta · requisitos · código **claro y comentado** · mensajes claros y **manejo de errores**. Suele haber un apartado de **documentación** (Javadoc de clases y métodos) y otro de **reflexión** en un comentario.
> - **Entrega:** `Apellidos_Nombre.zip` **sin `bin`**, por el cuestionario del aula. Nombre en la cabecera de cada clase. **Sin Internet ni IA.**
>
> **Entra:** crear hilos, gestionarlos (join, interrupciones, estados, prioridad), compartir información, dependencias, condiciones de carrera, `volatile`, `synchronized` (métodos y bloques) y variables atómicas.
> **No entra (se verá después):** semáforos, productor-consumidor y salas de espera. `wait()`/`notify()` **no salen en los apuntes** de la UT2 (la diapositiva de estados dice que WAITING «lo veremos más adelante»): como mucho, una pregunta de test sobre estados.
>
> Basado en los apuntes `261026-psp-ut2.pdf` (lo que viene de ahí va marcado con 📘), en los ejercicios resueltos de `../soluciones/bloque1-4` y en el formato del examen real de la UT1.

## Índice

1. [Qué es un hilo · procesos frente a hilos](#1-qué-es-un-hilo--procesos-frente-a-hilos)
2. [Crear hilos: Thread o Runnable](#2-crear-hilos-thread-o-runnable)
3. [Estados de un hilo](#3-estados-de-un-hilo)
4. [Métodos de Thread](#4-métodos-de-thread)
5. [join, interrupciones, daemon y prioridad](#5-join-interrupciones-daemon-y-prioridad)
6. [Compartir información](#6-compartir-información)
7. [Problemas: dependencias, Bernstein, carrera, atomicidad, visibilidad](#7-problemas-dependencias-bernstein-carrera-atomicidad-visibilidad)
8. [Sincronización: synchronized y atómicas](#8-sincronización-synchronized-y-atómicas)
9. [Plantillas para la parte práctica](#9-plantillas-para-la-parte-práctica)
10. [Errores típicos](#10-errores-típicos)
11. [Test: simulacro de 20 preguntas y banco de 45](#11-test-simulacro-de-20-preguntas-y-banco-de-45)
12. [Plan de estudio hasta el lunes](#12-plan-de-estudio-hasta-el-lunes)

---

## 1. Qué es un hilo · procesos frente a hilos

- 📘 Un **hilo** (*thread*) es la unidad básica de ejecución del SO: una secuencia de código que se ejecuta **dentro del contexto de un proceso**. Al lanzar un programa Java se crea un hilo que ejecuta el `main`.

```
PROCESO (JVM)  ── memoria propia, PID, recursos del SO
┌──────────────────────────────────────────────┐
│ montón (heap): objetos y static ← COMPARTIDO  │
│ código                          ← COMPARTIDO  │
│ ┌──────────┐ ┌──────────┐ ┌──────────┐        │
│ │ main     │ │ Thread-0 │ │ Thread-1 │        │
│ │ pila     │ │ pila     │ │ pila     │ ← PROPIO│
│ │ registros│ │ registros│ │ registros│        │
│ └──────────┘ └──────────┘ └──────────┘        │
└──────────────────────────────────────────────┘
```

| | Procesos | Hilos |
|---|---|---|
| Memoria | Independiente | **Compartida** con los hilos del mismo proceso |
| Crear | Caro (el SO asigna recursos) | 📘 Mucho más barato: solo pila y registros propios |
| Cambio de contexto | Lento | 📘 Más rápido |
| Comunicarse | Tuberías, ficheros, sockets | 📘 Memoria compartida (pero la sincronización se complica) |
| Un fallo | No afecta a otros procesos | Puede tumbar todo el proceso |

📘 **Ventajas de los hilos:** el programa sigue respondiendo mientras una tarea larga va en otro hilo; aprovechan **varios núcleos** en paralelo; consumen menos memoria. **Desventaja:** al compartir memoria, hay que sincronizar.

📘 **Secuencial frente a hilos (los ratones):** 4 ratones que comen 4, 5, 3 y 6 s. Secuencial: **18 s** (la suma). Con hilos: **6 s** (el más lento). El `main` termina el primero, pero el programa **no acaba hasta que terminan todos los hilos** (no daemon).

## 2. Crear hilos: Thread o Runnable

```java
// A) extends Thread: la clase ES el hilo
public class Raton extends Thread {
    @Override public void run() { /* trabajo */ }
}
new Raton().start();

// B) implements Runnable: la clase es solo la TAREA
public class Raton implements Runnable {
    @Override public void run() { /* trabajo */ }
}
new Thread(new Raton()).start();
new Thread(new Raton(), "Tom").start();          // con nombre

// C) lambda (Runnable es una interfaz funcional)
new Thread(() -> System.out.println("hola")).start();
```

| | `extends Thread` | `implements Runnable` |
|---|---|---|
| Qué es la clase | Un hilo | Una tarea que ejecutará un hilo |
| Lanzar | `tom.start()` | `new Thread(tom).start()` |
| Herencia | 📘 Ya no puede heredar de otra | Puede heredar de otra clase |
| Nombre | `getName()` | `Thread.currentThread().getName()` |
| Compartir datos | Cada hilo es un objeto distinto | 📘 Varios hilos pueden ejecutar **el mismo** objeto y compartir sus atributos |
| Pools | No | `pool.submit(tarea)` |

📘 **Regla práctica:** Runnable salvo en ejemplos muy cortos. 📘 **Analogía:** el `Thread` es el cocinero; el `Runnable`, la receta.

⚠️ **`start()` frente a `run()`:** `start()` crea un hilo nuevo, que ejecuta `run()`. Llamar a `run()` es una llamada normal: lo ejecuta el hilo que llama y **no se crea ningún hilo**. 📘 Ratones con `run()`: 2 + 3 = 5 s; con `start()`: 3 s.
⚠️ Un hilo **solo se arranca una vez**: un segundo `start()` lanza `IllegalThreadStateException`.
⚠️ 📘 El **orden** de los mensajes entre hilos **cambia entre ejecuciones**: lo decide el planificador, no el orden del código.

## 3. Estados de un hilo

📘 `Thread.State`, consultado con `getState()`:

| Estado | Cuándo |
|---|---|
| `NEW` | Creado, sin `start()` |
| `RUNNABLE` | Arrancado: **en ejecución o listo** para ejecutarse (Java no distingue) |
| `BLOCKED` | Esperando un **monitor** (`synchronized`) que tiene otro hilo |
| `WAITING` | Esperando **sin límite** a otro hilo: `join()`, `wait()` |
| `TIMED_WAITING` | Esperando **con límite**: `sleep(ms)`, `join(ms)`, `wait(ms)` |
| `TERMINATED` | `run()` ha terminado |

```
          start()                sleep / join(ms)
  NEW ───────────▶ RUNNABLE ◀──────────────────▶ TIMED_WAITING
                    │  ▲  ▲
   synchronized     │  │  │ join() / wait()
   ocupado          ▼  │  └──────────────▶ WAITING
                 BLOCKED
                    run() termina
  RUNNABLE ──────────────────▶ TERMINATED
```

📘 **Ratón de los apuntes:** NEW → RUNNABLE → TIMED_WAITING (`sleep`) → RUNNABLE → BLOCKED (`synchronized (comedero)` ocupado) → RUNNABLE → TERMINATED.

## 4. Métodos de Thread

| Método | Qué hace |
|---|---|
| `start()` | Crea el hilo, que ejecuta `run()` |
| `run()` | El trabajo del hilo. Llamarlo directamente **no** crea hilo |
| `join()` / `join(ms)` | El hilo **que llama** espera a que termine **ese** hilo (como mucho `ms`) |
| `Thread.sleep(ms)` | **Estático**: duerme el hilo **actual** (no otro) |
| `getName()` / `setName()` | Nombre (`Thread-0`, `Thread-1`… por defecto) |
| `getId()` / `threadId()` | 📘 Identificador; desde Java 19, `threadId()` |
| `getState()` | Estado actual |
| `interrupt()` | Pide la interrupción: activa el **indicador** |
| `Thread.interrupted()` | 📘 **Estático**: ¿está interrumpido el hilo actual? y **limpia** el indicador |
| `isInterrupted()` | ¿Está interrumpido ese hilo? **Sin** limpiar |
| `isAlive()` | `true` entre `start()` y el fin de `run()` |
| `setPriority(n)` | Prioridad 1-10 |
| `setDaemon(true)` | Hilo de servicio; **antes** de `start()` |
| `Thread.currentThread()` | El hilo que está ejecutando esa línea |

📘 **No se puede «matar» un hilo:** `stop()` y `destroy()` están obsoletos o eliminados. Se usan **interrupciones** para que termine ordenadamente (`destroy()` existe, pero para `Process`).

## 5. join, interrupciones, daemon y prioridad

**join:** `h.join()` bloquea al hilo que lo llama hasta que `h` termina. Lanza `InterruptedException` (checked): `throws` o `try/catch`.
- 📘 «Fin» sale demasiado pronto si el `main` no hace `join()` a los hilos.
- **Dependencias entre hilos** (📘 TareaEncadenada, Halcón Milenario, Despliegue): el `join()` va **dentro del `run()`** del hilo que depende, sobre el hilo del que depende.
- `join(0)` = esperar **sin límite**.
- Tras `join()`, todo lo que escribió el hilo es **visible** (*happens-before*).

**Interrupciones** (📘 «una petición; el hilo decide cómo atenderla»):
- Si el hilo está en `sleep`/`join`/`wait`, se despierta con `InterruptedException` (y el indicador se **limpia**).
- Si está calculando, solo se activa el indicador: tiene que comprobar `isInterrupted()` o `Thread.interrupted()` en su bucle.
- Buena práctica en el `catch`: `Thread.currentThread().interrupt();` para volver a marcarlo, o `return`.

**Daemon:** la JVM termina cuando acaban todos los hilos **no daemon**. Un daemon se corta al acabar el último normal. `setDaemon(true)` **antes** de `start()` (si no, `IllegalThreadStateException`).

**Prioridad** 📘:
- `setPriority(n)`, de `MIN_PRIORITY` = **1** a `MAX_PRIORITY` = **10**; por defecto `NORM_PRIORITY` = **5**.
- Un hilo nuevo **hereda** la prioridad del que lo crea.
- Es **solo una indicación**: el planificador del SO (normalmente apropiativo) puede ignorarla. Misma prioridad → tiempo compartido. Con núcleos libres casi no se nota.

## 6. Compartir información

📘 Los atributos **no estáticos** de cada objeto `Thread` son de ese hilo y **no sirven para compartir**. Tres formas de compartir:

1. **Atributos `static`**: uno para todas las instancias.
2. **Referencia a un objeto común** que se pasa a todos los hilos (constructor).
3. **Un único objeto `Runnable`** con atributos de instancia y **varios `Thread`** creados a partir de él.

```java
// 2) objeto común                         // 3) un Runnable, varios Thread
ObjetoComun oc = new ObjetoComun();        Tarea t = new Tarea();
new Hilo(oc).start();                      new Thread(t).start();
new Hilo(oc).start();                      new Thread(t).start();   // comparten t.contador
```

📘 **Recurso compartido:** atributo estático o de un objeto común, método estático o de un objeto común, o un recurso limitado (conexión a BD, socket…).

## 7. Problemas: dependencias, Bernstein, carrera, atomicidad, visibilidad

📘 **Fuentes de errores:** los **recursos compartidos** y el **orden de ejecución**.

📘 **Tipos de dependencias:**
- **De datos:** varios segmentos usan el mismo dato.
- **De flujo:** por instrucciones de control de flujo el orden no se puede determinar (no se ve con un análisis estático).
- **De recursos:** acceden a la vez a recursos del procesador.

📘 **Condiciones de Bernstein.** S1 y S2 son independientes (pueden ir en paralelo) si:

| Condición | Si no se cumple |
|---|---|
| Entradas(S2) ∩ Salidas(S1) = ∅ | **Dependencia de flujo** (S2 lee lo que escribe S1) |
| Entradas(S1) ∩ Salidas(S2) = ∅ | **Antidependencia** (S2 escribe lo que lee S1) |
| Salidas(S1) ∩ Salidas(S2) = ∅ | **Dependencia de salida** (los dos escriben lo mismo) |

Ejemplo: `S1: a = x + y;` `S2: b = a * 2;` → S2 lee `a`, que escribe S1: **dependencia de flujo**, no pueden ir en paralelo; se resuelve con `join()`.

📘 **Condición de carrera:** el resultado depende del orden no controlado de los hilos. `x++` son **3 pasos** (leer, sumar, escribir): se pierden incrementos. 📘 1000 hilos × 10.000 `contador++` dan 9.472.581, 9.594.825… en vez de 10.000.000.

```
paso   Hilo A           Hilo B           x
 1     lee 41                            41
 2                      lee 41           41
 3     escribe 42                        42
 4                      escribe 42       42   ← se pierde uno
```

📘 **Operación atómica:** se ejecuta de una vez, sin que otro hilo vea o cambie el dato a medias. `x++` **no** lo es.

📘 **Inconsistencia de memoria (visibilidad):** un hilo ve un valor **desactualizado** porque el cambio de otro hilo no se ha propagado (caché, registros, optimización del JIT). Ejemplo: `while (!listo) {}` puede no terminar nunca. **Solución:** `volatile`.

📘 **`volatile` garantiza visibilidad, NO atomicidad.** Sirve para una **bandera** que escribe un hilo y leen otros. `x++` sobre un `volatile` sigue perdiendo incrementos.

📘 **Sección crítica:** el bloque que accede a recursos compartidos; solo un hilo a la vez. Garantizarlo es la **exclusión mutua**. Debe terminar en un tiempo acotado y evitar la **inanición** (un hilo que nunca consigue entrar).

**Interbloqueo (deadlock):** 📘 dos hilos se esperan mutuamente y el programa se cuelga. Típico: A tiene el cerrojo 1 y pide el 2; B tiene el 2 y pide el 1. Se evita cogiendo los cerrojos **siempre en el mismo orden**.

## 8. Sincronización: synchronized y atómicas

📘 **Monitor:** todo objeto tiene uno. `synchronized` entra en el monitor: mientras un hilo lo tiene, los demás que quieran el **mismo** monitor esperan (BLOCKED). Al salir se libera y los cambios quedan visibles.

```java
// Método sincronizado: monitor = this (o la clase si el método es static)
public synchronized void incrementar() { valor++; }

// Bloque sincronizado: monitor = el objeto indicado; solo protege lo necesario
private final Object lock = new Object();      // 📘 ATRIBUTO, no variable local
public void incrementar() {
    // ... trabajo no crítico, en paralelo ...
    synchronized (lock) { valor++; }
}
```

| Aspecto 📘 | Método `synchronized` | Bloque `synchronized` |
|---|---|---|
| Alcance | Todo el método | Solo la sección |
| Monitor | `this` (o `Clase.class` si es static) | El objeto del paréntesis |
| Facilidad | Muy sencillo | Hay que elegir bloque y monitor |
| Eficiencia | Menor: bloquea código no crítico | Mayor |
| Uso típico | Métodos cortos y críticos | Métodos largos con partes no compartidas |

- 📘 Un método `synchronized` bloquea **todos los métodos synchronized** del mismo objeto.
- 📘 Para sincronizar, todos los hilos deben usar **el mismo objeto monitor**; con monitores distintos van en paralelo. Por eso el `lock` es un atributo (compartido), no una variable local (uno nuevo en cada llamada).
- 📘 **Dos variables independientes** (`c1` y `c2`, ejercicio CONTAR 2): **dos monitores** distintos (`lock1`, `lock2`) para que `inc1()` e `inc2()` puedan ir en paralelo.
- ⚠️ `synchronized` en `run()` de una clase `extends Thread` **no protege nada**: cada hilo se sincroniza sobre **sí mismo**.
- ⚠️ Datos `static` → monitor común: `synchronized (MiClase.class)` o un `static final Object lock`.
- 📘 **Reentrante:** un hilo que ya tiene un monitor puede volver a entrar en otro método o bloque del mismo monitor sin bloquearse.
- 📘 **Granularidad:** sincronizar de más ralentiza; mejor solo la sección crítica.

📘 **Variables atómicas** (`java.util.concurrent.atomic`): `AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference`. Usan **CAS** (*compare-and-set*), sin cerrojo: más rápidas que `synchronized` para **una sola variable**. Para modificar **varias variables juntas** sigue haciendo falta una sección crítica.

| Método 📘 | Equivale a |
|---|---|
| `get()` / `set(v)` | leer / escribir (visibilidad como `volatile`) |
| `incrementAndGet()` | `++x` atómico (devuelve el nuevo) |
| `getAndIncrement()` | `x++` atómico (devuelve el anterior) |
| `addAndGet(n)` | `x += n` atómico |
| `compareAndSet(esp, nuevo)` | si vale `esp`, pon `nuevo` y devuelve `true`; si no, `false` |

## 9. Plantillas para la parte práctica

**Cómo será** (por el examen real de la UT1): un proyecto IntelliJ con las clases a completar (`// TODO`), los ejercicios en paquetes `PSP.c1`, `PSP.c2`, `PSP.c3`, configuraciones de ejecución con los argumentos ya puestos y, a veces, una clase **ya hecha que no se toca**. En la UT1, el C1 valía 2 puntos y el C2 3,5, repartidos en apartados: programa principal, documentación (0,25) y reflexión (0,5).

**Lista de comprobación antes de entregar** (cada punto es nota):
1. **Nombre y apellidos** en la cabecera de **cada** clase.
2. La salida coincide **carácter a carácter** con la del enunciado (mensajes, formato de líneas, decimales).
3. **Manejo de errores:** argumentos que faltan o no son números → mensaje claro por `System.err` y `System.exit(1)`. `InterruptedException` capturada con un mensaje, no tragada en silencio.
4. **Javadoc** en cada clase y método: qué hace, `@param`, `@return`, `@throws`.
5. **Reflexión** en el comentario que pida (5-10 líneas, con una **ventaja** y un **inconveniente** si lo pide).
6. Ejecutado **varias veces** con las configuraciones preparadas: con hilos, una ejecución buena no demuestra nada.
7. Zip **`Apellidos_Nombre.zip` sin `bin`**.

**Esqueleto de clase al estilo del examen** (cabecera, Javadoc, comprobación de argumentos y métodos pequeños):
```java
/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 * ============================================================
 */
package PSP.c1;

/**
 * Ejercicio C1 · Carrera de corredores.
 * Lanza N hilos Corredor y espera a que lleguen todos a la meta.
 *
 * Argumentos: {@code <numeroDeCorredores>}
 */
public class Carrera {

    /**
     * Punto de entrada: comprueba el argumento, lanza los corredores y espera a todos.
     *
     * @param args número de corredores (entre 3 y 5)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: Carrera <numeroDeCorredores>");
            System.exit(1);
        }
        int n;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("El número de corredores debe ser un entero: " + args[0]);
            System.exit(1);
            return;                          // el compilador no sabe que exit no vuelve
        }
        try {
            Thread[] hilos = lanzar(n);
            esperar(hilos);
            System.out.println("¡Todos los corredores han llegado a la meta!");
        } catch (InterruptedException e) {
            System.err.println("El programa principal fue interrumpido mientras esperaba");
        }
    }

    /**
     * Crea y arranca un hilo por corredor.
     *
     * @param n número de corredores
     * @return los hilos ya arrancados
     */
    static Thread[] lanzar(int n) {
        Thread[] hilos = new Thread[n];
        for (int i = 0; i < n; i++) {
            hilos[i] = new Thread(new Corredor("Corredor " + (i + 1)));
            hilos[i].start();
        }
        return hilos;
    }

    /**
     * Espera a que terminen todos los hilos.
     *
     * @param hilos hilos a esperar
     * @throws InterruptedException si el hilo principal es interrumpido mientras espera
     */
    static void esperar(Thread[] hilos) throws InterruptedException {
        for (Thread h : hilos) h.join();
    }
}
```

Los apuntes proponen estos ejercicios; **lo más probable es que la práctica se parezca a ellos**. Plantillas listas para adaptar:

**A · Lanzar N hilos y esperar a todos** (📘 Corredores, ratones)
```java
public class Corredor implements Runnable {
    private final String nombre;
    public Corredor(String nombre) { this.nombre = nombre; }
    @Override public void run() {
        for (int paso = 1; paso <= 10; paso++) {
            System.out.println(nombre + " avanza al paso " + paso);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 401)); // 100..400 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
// main
Thread[] hilos = new Thread[n];
for (int i = 0; i < n; i++) {
    hilos[i] = new Thread(new Corredor("Corredor " + (i + 1)));
    hilos[i].start();
}
for (Thread h : hilos) h.join();                 // main con throws InterruptedException
System.out.println("¡Todos los corredores han llegado a la meta!");
```

**B · Dependencias entre hilos** (📘 TareaEncadenada, Halcón Milenario, Despliegue)
```java
public class Tarea extends Thread {
    private final int ms;
    private final Thread[] dependencias;
    static final long INICIO = System.currentTimeMillis();

    public Tarea(String nombre, int ms, Thread... dependencias) {   // varargs: 0, 1 o varias
        super(nombre);
        this.ms = ms;
        this.dependencias = dependencias;
    }
    @Override public void run() {
        try {
            for (Thread d : dependencias) d.join();   // espera a TODAS sus dependencias
            System.out.printf("Empieza %s (%d ms)%n", getName(), System.currentTimeMillis() - INICIO);
            Thread.sleep(ms);                         // simula el trabajo
            System.out.printf("Termina %s (%d ms)%n", getName(), System.currentTimeMillis() - INICIO);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
// main: las dependencias se CREAN antes (hay que pasar la referencia), se pueden ARRANCAR en cualquier orden
Tarea t1 = new Tarea("T1", 1000);
Tarea t2 = new Tarea("T2", 2000, t1);
Tarea t3 = new Tarea("T3", 1500, t1);
Tarea t4 = new Tarea("T4", 1500, t2, t3);       // depende de DOS
t1.start(); t2.start(); t3.start(); t4.start();   // en orden de dependencias: lo seguro
t4.join();                                        // el main espera a la última
```
📘 **Ampliación «arráncalos en orden inverso (T7 … T1), ¿sigue funcionando?»** Respuesta: **en la práctica sí, pero no está garantizado.**
- `join()` sobre un hilo **aún no arrancado** (estado NEW, `isAlive()` = `false`) **vuelve al instante, sin esperar**. Comprobado: 0 ms.
- Funciona porque el bucle del `main` arranca todos los hilos en microsegundos, y T7 tarda más en empezar a ejecutarse y llegar a su `join()` que el `main` en hacer el resto de `start()`. Probado 200 veces: nunca falló.
- Pero si el `main` se retrasara entre dos `start()` (por ejemplo, un `sleep` en medio), T7 haría `join()` sobre un hilo NEW, no esperaría y empezaría antes de tiempo. Es una **condición de carrera** sobre el orden de arranque. Lo seguro es arrancar en orden de dependencias, o arrancar todos antes de que ninguno pueda llegar a su `join`.

**C · Interrupción** (📘 Ejercicio 2)
```java
Thread hijo = new Thread(() -> {
    try {
        Thread.sleep(5000);                       // se queda parado 5 s
        System.out.println("Hijo: terminé normal");
    } catch (InterruptedException e) {
        System.out.println("Hijo: me han interrumpido, espero 2 s y acabo");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) { }
    }
});
hijo.start();
hijo.interrupt();                                 // el padre lo interrumpe antes del join
hijo.join();
System.out.println("Padre: el hijo ha terminado");
```

**D · Contador compartido protegido** (📘 CONTAR, VOCALES, COMPARACIÓN)
```java
public class Contadores {
    private int c1, c2;
    private final Object lock1 = new Object();    // un monitor por variable independiente
    private final Object lock2 = new Object();
    public void inc1() { synchronized (lock1) { c1++; } }
    public void inc2() { synchronized (lock2) { c2++; } }
    public int getC1() { synchronized (lock1) { return c1; } }
    public int getC2() { synchronized (lock2) { return c2; } }
}
```

**E · Vocales** (📘 5 hilos, cada uno cuenta una vocal, todos suman en la misma variable)
```java
public class ContarVocal implements Runnable {
    private static int total = 0;                          // dato compartido (static)
    private static final Object LOCK = new Object();       // monitor común (static)
    private final String texto; private final char vocal;
    public ContarVocal(String texto, char vocal) { this.texto = texto; this.vocal = vocal; }
    @Override public void run() {
        for (char c : texto.toLowerCase().toCharArray()) {
            if (c == vocal) {
                synchronized (LOCK) { total++; }           // sección crítica mínima
            }
        }
    }
    public static int getTotal() { return total; }
}
// main: 5 hilos (a, e, i, o, u), start a todos, join a todos, imprimir ContarVocal.getTotal()
```
Mejor aún: cada hilo cuenta en una variable **local** y suma **una sola vez** al final dentro del `synchronized`: menos contención.

**F · Medir tiempos** (📘 COMPARACIÓN método frente a bloque)
```java
long inicio = System.nanoTime();
// lanzar y hacer join a los N hilos
long ms = (System.nanoTime() - inicio) / 1_000_000;
System.out.println("Tiempo: " + ms + " ms");
```
Explicación esperada: el bloque `synchronized` bloquea solo la operación crítica; si el método tiene trabajo no crítico, esa parte va en paralelo y es más rápido. Si el método solo hace `x++`, la diferencia es mínima.

**Reflexiones que pueden pedir** (5-10 líneas, con ventaja e inconveniente):
- *¿Hilos o procesos para esto?* Hilos: más ligeros, comparten memoria (comunicar es directo), `join()`. Inconvenientes: condiciones de carrera (hay que sincronizar) y sin aislamiento (un fallo tumba todo). Procesos: aislados y con código de salida, pero caros y se comunican por flujos.
- *¿Método `synchronized` o bloque?* Bloque: solo protege la sección crítica y deja el resto en paralelo (más rápido); método: más sencillo, pero bloquea todo el método y todos los métodos `synchronized` del objeto.
- *¿`synchronized` o `AtomicInteger`?* Para **una** variable, atómico (CAS, sin cerrojo, más rápido). Para varias variables que cambian juntas, `synchronized`.
- *¿Por qué sale distinto en cada ejecución?* Condición de carrera: el resultado depende del intercalado que decida el planificador.

## 10. Errores típicos

| Síntoma | Causa | Arreglo |
|---|---|---|
| Los hilos van uno detrás de otro | `run()` en vez de `start()` | `start()` |
| «Fin» sale antes que los hilos | Falta `join()` | `join()` a todos antes de imprimir |
| `unreported exception InterruptedException` | `sleep`/`join` lanzan una checked | `throws` o `try/catch` |
| `IllegalThreadStateException` | Segundo `start()`, o `setDaemon` tras `start()` | Hilo nuevo / mover `setDaemon` antes |
| El total cambia en cada ejecución | Carrera en `x++` | `synchronized` / `AtomicInteger` |
| `synchronized` y sigue fallando | Monitores distintos (`synchronized (this)` en cada hilo, `lock` local, `synchronized run()`) | Un monitor **compartido** |
| Un hilo no ve un cambio de bandera | Falta `volatile` | `volatile boolean` |
| El programa no termina | Hilo no daemon en bucle infinito | `interrupt()`, bandera `volatile` o `setDaemon(true)` |
| El programa se cuelga con dos cerrojos | Interbloqueo | Cerrojos siempre en el mismo orden |
| Una dependencia no espera | `join()` sobre un hilo aún no arrancado (vuelve al instante) | Arrancar en orden de dependencias |

---

## 11. Test: simulacro de 20 preguntas y banco de 45

### Simulacro (formato real del examen)

20 preguntas, una correcta. **Acierto +1, error −1/3, en blanco 0.** Nota = (aciertos − errores / 3) / 20 × la puntuación del test. Cronométrate: **20 minutos**. Corrige con la plantilla del final.

**1.** Un gestor de descargas lanza un hilo por fichero y el `main` escribe «Fin» justo después de los `start()`, sin nada más. ¿Qué ocurre?
- a) «Fin» sale siempre lo último
- b) «Fin» puede salir antes de que acaben las descargas, y el programa no termina hasta que acaban
- c) El programa termina al escribir «Fin» y corta las descargas
- d) No compila: falta `join()`

**2.** Un procesador de textos revisa la ortografía en un hilo aparte mientras el usuario escribe. ¿Qué ventaja de los hilos ilustra?
- a) Que el corrector va más rápido que sin hilos
- b) El aislamiento: si el corrector falla, el editor sigue
- c) Que consume menos memoria que el programa sin hilos
- d) La capacidad de respuesta: el programa sigue atendiendo al usuario

**3.** La clase `Coche extends Vehiculo` tiene que ejecutarse en su propio hilo. ¿Qué haces?
- a) `Coche implements Runnable` y `new Thread(coche).start()`
- b) `Coche extends Vehiculo, Thread`
- c) Llamar a `coche.run()` desde el `main`
- d) No se puede: una clase que hereda no puede ser un hilo

**4.** Un alumno escribe `corredor.run()` en lugar de `corredor.start()`. ¿Qué pasa?
- a) No compila
- b) Lanza `IllegalThreadStateException`
- c) El código se ejecuta en el hilo que llama, sin crear ningún hilo nuevo
- d) Se crea el hilo, pero queda en estado NEW

**5.** Un hilo está dentro de `Thread.sleep(2000)`. ¿En qué estado está?
- a) BLOCKED
- b) WAITING
- c) TIMED_WAITING
- d) RUNNABLE

**6.** Un hilo intenta entrar en un método `synchronized` de un objeto mientras otro hilo lo está ejecutando. ¿En qué estado queda?
- a) BLOCKED
- b) WAITING
- c) TIMED_WAITING
- d) NEW

**7.** El `main` ejecuta `t.join()`. ¿Qué ocurre?
- a) `t` espera a que termine el `main`
- b) `t` termina inmediatamente
- c) Los dos hilos pasan a ejecutarse en serie para siempre
- d) El `main` espera a que termine `t`

**8.** Un hilo de copia de seguridad tiene que dejar su trabajo de forma ordenada cuando el usuario cancela. ¿Qué usa el hilo principal?
- a) `stop()`
- b) `destroy()`
- c) `interrupt()`
- d) `System.exit(0)`

**9.** Un hilo que está en `Thread.sleep(5000)` recibe `interrupt()`. ¿Qué ocurre?
- a) Nada hasta que pasen los 5 s
- b) Se despierta con una `InterruptedException`
- c) Termina el programa entero
- d) Pasa a estado BLOCKED

**10.** Un hilo tiene prioridad 10 y otro prioridad 1, y hay núcleos libres. ¿Qué es cierto?
- a) La prioridad es solo una indicación al planificador: puede no notarse
- b) El de prioridad 10 siempre termina antes
- c) El de prioridad 1 no se ejecuta hasta que termina el de 10
- d) La prioridad por defecto de un hilo es 1

**11.** Dos hilos incrementan 1.000.000 de veces cada uno el mismo `int`, sin protección. ¿Qué resultado sale?
- a) Siempre 2.000.000
- b) Siempre 1.000.000
- c) Más de 2.000.000
- d) Normalmente menos de 2.000.000, y distinto en cada ejecución

**12.** ¿Por qué `contador++` pierde incrementos con varios hilos?
- a) Porque el `int` se desborda
- b) Porque son varios pasos (leer, sumar, escribir) que otro hilo puede intercalar
- c) Porque la JVM descarta las operaciones repetidas
- d) Porque falta `join()`

**13.** S1: `total = a + b;` S2: `media = total / 2;`. Según Bernstein, ¿qué hay entre S1 y S2?
- a) Nada: son independientes
- b) Antidependencia
- c) Dependencia de flujo
- d) Dependencia de salida

**14.** Un hilo hace `while (!parar) { }` y el `main` pone `parar = true`, pero el hilo no termina nunca. ¿Cómo se arregla?
- a) Declarando `parar` como `volatile`
- b) Subiendo la prioridad del hilo
- c) Poniendo un `Thread.sleep` en el `main`
- d) Haciendo `parar` `static`

**15.** Cuatro hilos suman en un `static volatile int total`. ¿Qué pasa?
- a) Es correcto: `volatile` hace atómico el `++`
- b) No compila
- c) Es correcto solo con dos hilos
- d) Siguen perdiéndose sumas: `volatile` da visibilidad, no atomicidad

**16.** Un objeto tiene dos métodos `synchronized`, `inc1()` e `inc2()`. Un hilo está dentro de `inc1()` y otro llama a `inc2()` sobre el mismo objeto. ¿Qué pasa?
- a) Los dos se ejecutan a la vez porque son métodos distintos
- b) El segundo espera: los dos métodos usan el mismo monitor (`this`)
- c) Se lanza una excepción
- d) El segundo interrumpe al primero

**17.** `inc1()` e `inc2()` modifican variables distintas y quieres que puedan ejecutarse a la vez sin errores. ¿Qué haces?
- a) Declararlos los dos `synchronized`
- b) Declarar las dos variables `volatile`
- c) Un bloque `synchronized` en cada uno, con un objeto monitor distinto para cada variable
- d) No sincronizar nada

**18.** ¿Qué es la sección crítica?
- a) El bloque que accede a un recurso compartido y solo debe ejecutar un hilo a la vez
- b) El código que más tarda en ejecutarse
- c) El método `main`
- d) El bloque `catch` de una `InterruptedException`

**19.** Quieres un contador compartido por varios hilos, correcto y sin usar `synchronized`. ¿Qué usas?
- a) `volatile int`
- b) `static int`
- c) Un `int` en cada hilo, sin sumar al final
- d) `AtomicInteger` con `incrementAndGet()`

**20.** El hilo A tiene el cerrojo de la cuenta 1 y espera el de la cuenta 2; el hilo B tiene el de la 2 y espera el de la 1. ¿Cómo se llama?
- a) Condición de carrera
- b) Inanición
- c) Interbloqueo
- d) Inconsistencia de memoria

**Hoja de respuestas**

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| | | | | | | | | | | | | | | | | | | | |

<details>
<summary>Plantilla de respuestas del simulacro (ábrela al terminar)</summary>

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| b | d | a | c | c | a | d | c | b | a | d | b | c | a | d | b | c | a | d | c |

Si fallas alguna, su explicación está en el banco de abajo: 1 → 10 y 25 · 2 → 5 · 3 → 8 · 4 → 6 · 5 → 13 · 6 → 14 · 7 → 21 · 8 → 20 · 9 → 23 · 10 → 28 · 11 y 12 → 31 · 13 → 32 · 14 → 38 · 15 → 39 · 16 → 41 · 17 → apartado 8 · 18 → 36 · 19 → 45 y apartado 8 · 20 → apartado 7.
</details>

### Banco de repaso (45 preguntas)

**Tapa la respuesta, contesta y luego comprueba.** Muchas son «a pillar», como en la UT1. Las marcadas con 📘 salen literalmente de los apuntes.

### A · Hilos y procesos

**1.** 📘 ¿Qué comparten los hilos de un mismo proceso?
- a) Nada: cada hilo tiene su memoria
- b) La pila y los registros
- c) La memoria y los recursos del proceso
- d) Solo las variables locales

> **c)** Comparten montón (objetos, `static`) y recursos. Cada hilo tiene **su propia pila y registros** (la trampa es la b).

**2.** 📘 ¿Qué es más rápido?
- a) El cambio de contexto entre procesos
- b) El cambio de contexto entre hilos
- c) Son iguales
- d) Depende de la prioridad

> **b)** Los hilos comparten el espacio de memoria: hay menos que guardar y restaurar.

**3.** Al ejecutar `java Programa`, ¿cuántos hilos hay como mínimo?
- a) Ninguno hasta que se llama a `start()`
- b) Uno, el que ejecuta `main`
- c) Dos, `main` y el del sistema
- d) Tantos como núcleos

> **b)** 📘 «Al lanzar un proceso se crea un Thread que ejecuta el main». (La JVM tiene además hilos internos, como el recolector de basura, pero el de tu código es uno).

**4.** Cuatro tareas de 4, 5, 3 y 6 s, todas con `sleep`. ¿Cuánto tarda la versión con un hilo por tarea?
- a) 18 s
- b) 6 s
- c) 4,5 s
- d) 3 s

> **b)** Lo que tarda la más larga. La secuencial tarda la **suma** (18 s).

**5.** ¿Qué NO consiguen los hilos?
- a) Que el programa siga respondiendo durante una tarea larga
- b) Aprovechar varios núcleos
- c) Aumentar el ancho de banda de una conexión compartida
- d) Solapar tiempos de espera

> **c)** Los hilos no crean recursos: si el cuello de botella es compartido, no se gana nada.

### B · Crear hilos

**6.** ¿Qué ocurre al llamar a `t.run()` en lugar de `t.start()`?
- a) Error de compilación
- b) Se lanza `IllegalThreadStateException`
- c) Se ejecuta el código en el hilo que llama, sin crear un hilo nuevo
- d) Se crea el hilo pero no arranca

> **c)** Compila y funciona, pero en serie.

**7.** ¿Qué ocurre al llamar dos veces a `start()` sobre el mismo objeto?
- a) Se ejecuta dos veces
- b) La segunda se ignora
- c) `IllegalThreadStateException`
- d) `InterruptedException`

> **c)** Un hilo solo arranca una vez.

**8.** Una clase `Taxi extends Vehiculo` debe ejecutarse en un hilo. ¿Qué haces?
- a) `Taxi extends Vehiculo, Thread`
- b) `Taxi extends Vehiculo implements Runnable`
- c) `Taxi implements Thread`
- d) No se puede

> **b)** Java no tiene herencia múltiple de clases; `Thread` es una clase y `Runnable` una interfaz. (La c no compila: `Thread` no es una interfaz.)

**9.** 📘 ¿Qué ventaja tiene `Runnable` para **compartir datos**?
- a) Ninguna
- b) Varios `Thread` pueden ejecutar el mismo objeto `Runnable` y compartir sus atributos
- c) Sus atributos son `static` automáticamente
- d) Sincroniza solo

> **b)** `Tarea t = new Tarea(); new Thread(t).start(); new Thread(t).start();`

**10.** ¿Cuál es el orden de salida de `main { print("A"); hilo.start(); print("B"); }` si el hilo imprime "H"?
- a) Siempre A H B
- b) Siempre A B H
- c) A, y luego H y B en cualquier orden
- d) H siempre el primero

> **c)** A sale seguro primero; después lo decide el planificador.

**11.** ¿Qué método se sobrescribe para indicar el trabajo del hilo?
- a) `start()`
- b) `run()`
- c) `main()`
- d) `execute()`

> **b)** `start()` no se sobrescribe: es el que crea el hilo y llama a `run()`.

### C · Estados y métodos

**12.** 📘 Un hilo creado con `new` pero sin `start()` está en:
- a) RUNNABLE
- b) NEW
- c) WAITING
- d) BLOCKED

> **b)**

**13.** 📘 Un hilo dentro de `Thread.sleep(2000)` está en:
- a) WAITING
- b) BLOCKED
- c) TIMED_WAITING
- d) RUNNABLE

> **c)** Espera con límite de tiempo.

**14.** 📘 Un hilo que intenta entrar en un `synchronized` cuyo monitor tiene otro hilo está en:
- a) WAITING
- b) BLOCKED
- c) TIMED_WAITING
- d) NEW

> **b)** BLOCKED = esperando un monitor.

**15.** Un hilo que hace `otro.join()` (sin tiempo) está en:
- a) BLOCKED
- b) TIMED_WAITING
- c) WAITING
- d) RUNNABLE

> **c)** Espera sin límite. `join(ms)` sería TIMED_WAITING.

**16.** RUNNABLE significa que el hilo…
- a) se está ejecutando en la CPU
- b) está listo o ejecutándose; Java no distingue
- c) ha terminado
- d) espera un monitor

> **b)** Trampa típica.

**17.** 📘 ¿Qué diferencia hay entre `Thread.interrupted()` e `isInterrupted()`?
- a) Ninguna
- b) `interrupted()` es estático, mira el hilo actual y **limpia** el indicador; `isInterrupted()` no lo limpia
- c) `isInterrupted()` interrumpe el hilo
- d) `interrupted()` lanza una excepción

> **b)**

**18.** `Thread.sleep(1000)` llamado desde el `main` sobre un objeto `t` (`t.sleep(1000)`) duerme a…
- a) `t`
- b) el `main`
- c) los dos
- d) ninguno

> **b)** `sleep` es **estático**: duerme siempre al hilo **actual**, aunque se escriba `t.sleep`.

**19.** ¿Qué devuelve `isAlive()` antes de `start()` y después de terminar `run()`?
- a) true / true
- b) false / false
- c) true / false
- d) false / true

> **b)** Solo es `true` entre `start()` y el final de `run()`.

**20.** 📘 ¿Cómo se termina un hilo de forma correcta desde otro?
- a) `stop()`
- b) `destroy()`
- c) `interrupt()` y que el hilo atienda la interrupción
- d) `System.exit()` siempre

> **c)** `stop()` y `destroy()` están obsoletos o eliminados.

### D · join, interrupciones, daemon y prioridad

**21.** `t.join()` hace que…
- a) `t` espere al hilo que llama
- b) el hilo que llama espere a que `t` termine
- c) `t` termine inmediatamente
- d) los dos se ejecuten en serie desde ese momento

> **b)** Trampa: espera **el que llama**, no `t`.

**22.** ¿Qué hace `t.join(0)`?
- a) No espera
- b) Espera 0 ms
- c) Espera sin límite
- d) Error

> **c)** `join(0)` = `join()`.

**23.** Un hilo dormido en `sleep` recibe `interrupt()`. ¿Qué pasa?
- a) Nada hasta que despierte
- b) Se despierta con `InterruptedException` y el indicador queda limpio
- c) Termina inmediatamente
- d) Pasa a BLOCKED

> **b)**

**24.** Un hilo en `while (true) { calcular(); }` sin `sleep` recibe `interrupt()`. ¿Qué pasa?
- a) Lanza `InterruptedException`
- b) Termina
- c) Solo se activa su indicador; sigue calculando si no lo comprueba
- d) Pasa a WAITING

> **c)** Tiene que mirar `isInterrupted()` en el bucle.

**25.** ¿Cuándo termina la JVM?
- a) Cuando termina `main`
- b) Cuando terminan todos los hilos no daemon
- c) Cuando terminan todos los hilos, daemon incluidos
- d) Cuando termina el primer hilo

> **b)**

**26.** `t.setDaemon(true)` después de `t.start()`…
- a) Funciona igual
- b) Lanza `IllegalThreadStateException`
- c) Se ignora
- d) Para el hilo

> **b)**

**27.** 📘 Rango y valor por defecto de la prioridad:
- a) 0-10, 5
- b) 1-10, 5
- c) 1-5, 1
- d) 1-10, 1

> **b)** `MIN_PRIORITY` = 1, `NORM_PRIORITY` = 5, `MAX_PRIORITY` = 10.

**28.** 📘 Un hilo con prioridad 10 frente a otro con prioridad 1…
- a) siempre termina antes
- b) siempre recibe el 100 % de la CPU
- c) puede recibir más CPU, pero es solo una indicación al SO
- d) bloquea al de prioridad 1

> **c)** Con núcleos libres casi no se nota.

**29.** 📘 Un hilo nuevo, ¿qué prioridad tiene?
- a) Siempre 5
- b) La del hilo que lo crea
- c) 1
- d) Aleatoria

> **b)** La hereda (5 si lo crea el `main` sin cambiarla).

### E · Compartir, dependencias y carreras

**30.** 📘 Varios objetos `extends Thread` con un atributo **no static** `contador`, ¿lo comparten?
- a) Sí
- b) No: cada objeto tiene el suyo
- c) Solo si se llama a `join()`
- d) Solo si es `volatile`

> **b)** Para compartir: `static`, un objeto común o un único `Runnable`.

**31.** 📘 ¿Por qué `contador++` con varios hilos pierde incrementos?
- a) Porque `int` no admite valores grandes
- b) Porque son tres pasos (leer, sumar, escribir) y otro hilo puede intercalarse
- c) Porque falta `join()`
- d) Porque la JVM descarta operaciones

> **b)** Condición de carrera.

**32.** 📘 S1: `a = b + c;` S2: `d = a * 2;`. ¿Qué dependencia hay?
- a) Ninguna
- b) De flujo (S2 lee lo que escribe S1)
- c) Antidependencia
- d) De salida

> **b)** Entradas(S2) ∩ Salidas(S1) = {a}.

**33.** 📘 S1: `a = b + 1;` S2: `b = 5;`. ¿Qué dependencia hay?
- a) De flujo
- b) Antidependencia (S2 escribe lo que lee S1)
- c) De salida
- d) Ninguna

> **b)** Entradas(S1) ∩ Salidas(S2) = {b}.

**34.** 📘 S1: `x = 1;` S2: `x = 2;`. ¿Qué dependencia hay?
- a) De flujo
- b) Antidependencia
- c) De salida
- d) Ninguna

> **c)** Los dos escriben `x`: el resultado depende del orden.

**35.** Un programa con una condición de carrera da el resultado correcto en 10 ejecuciones seguidas. ¿Es correcto?
- a) Sí, está probado
- b) No: el fallo depende del intercalado y puede no aparecer
- c) Sí, si se ejecutó en Linux
- d) Solo si tiene `volatile`

> **b)** Una ejecución que sale bien no demuestra nada.

**36.** 📘 ¿Qué es la exclusión mutua?
- a) Que dos hilos no puedan existir a la vez
- b) Garantizar que solo un hilo a la vez ejecute la sección crítica
- c) Que un hilo espere a otro con `join()`
- d) Que los hilos no compartan memoria

> **b)**

**37.** 📘 ¿Qué es la inanición?
- a) Un hilo que nunca consigue entrar en la sección crítica
- b) Dos hilos que se esperan mutuamente
- c) Un hilo sin trabajo
- d) Un hilo daemon

> **a)** La b es interbloqueo.

### F · volatile, synchronized y atómicas

**38.** 📘 `volatile` garantiza…
- a) atomicidad
- b) visibilidad
- c) exclusión mutua
- d) las tres

> **b)** Solo visibilidad.

**39.** `static volatile int total;` y 4 hilos hacen `total++` 1.000.000 de veces cada uno. El resultado es…
- a) siempre 4.000.000
- b) normalmente menor que 4.000.000
- c) mayor que 4.000.000
- d) error de compilación

> **b)** `volatile` no hace atómico el `++`.

**40.** 📘 Un método `public synchronized void m()` usa como monitor…
- a) la clase
- b) `this`
- c) un objeto nuevo en cada llamada
- d) el hilo

> **b)** Si el método es `static`, el monitor es la clase (`Clase.class`).

**41.** 📘 Mientras un hilo ejecuta un método `synchronized` de un objeto, otro hilo…
- a) puede ejecutar cualquier otro método `synchronized` de ese objeto
- b) no puede ejecutar **ningún** método `synchronized` de ese objeto
- c) no puede ejecutar ningún método de ese objeto, aunque no sea synchronized
- d) se interrumpe

> **b)** Los métodos no sincronizados sí puede ejecutarlos (la c es la trampa).

**42.** Dos hilos hacen `synchronized (new Object()) { c++; }`. ¿Está protegido `c`?
- a) Sí
- b) No: cada uno usa un monitor distinto
- c) Solo con 2 hilos
- d) Solo si `c` es `volatile`

> **b)** 📘 Hay que usar **el mismo** objeto monitor (un atributo compartido).

**43.** Una clase `Contador extends Thread` con `public synchronized void run() { total++; }` y `total` static. ¿Está protegido?
- a) Sí
- b) No: cada hilo se sincroniza sobre sí mismo
- c) Sí, si hay `join()`
- d) No compila

> **b)** Monitores distintos. Hace falta `synchronized (Contador.class)` o un lock `static`.

**44.** 📘 ¿Qué significa que `synchronized` sea **reentrante**?
- a) Que se puede interrumpir
- b) Que un hilo que ya tiene el monitor puede volver a entrar en otro bloque o método con ese mismo monitor sin bloquearse
- c) Que varios hilos pueden entrar a la vez
- d) Que se libera solo tras un tiempo

> **b)**

**45.** 📘 ¿Qué hace `atomico.compareAndSet(5, 6)`?
- a) Suma 1
- b) Si vale 5, lo pone a 6 y devuelve `true`; si no, no cambia nada y devuelve `false`
- c) Compara y devuelve el mayor
- d) Lo pone a 6 siempre

> **b)** Base de las variables atómicas (CAS), sin cerrojos.

**Plantilla de respuestas:** 1c · 2b · 3b · 4b · 5c · 6c · 7c · 8b · 9b · 10c · 11b · 12b · 13c · 14b · 15c · 16b · 17b · 18b · 19b · 20c · 21b · 22c · 23b · 24c · 25b · 26b · 27b · 28c · 29b · 30b · 31b · 32b · 33b · 34c · 35b · 36b · 37a · 38b · 39b · 40b · 41b · 42b · 43b · 44b · 45b

---

## 12. Plan de estudio hasta el lunes

| Día | Qué hacer | Tiempo |
|---|---|---|
| **Viernes 20** | Apartados 1-8. **Simulacro de test** (apartado 11) cronometrado en 20 min, con la nota calculada. | 1 h 30 |
| **Sábado 21** | Banco de 45 preguntas (solo los temas que fallaste). Plantillas del apartado 9: escribir **sin mirar** A (corredores), B (despliegue) y D (contadores), con cabecera, Javadoc y comprobación de argumentos. | 2 h 30 |
| **Domingo 22** | **Simulacro práctico completo**: 3 ejercicios en 2 h 30 min, con la lista de comprobación del apartado 9. Repasar fallos con la guía interactiva (modo repaso). | 3 h |
| **Lunes 23** (antes de las 8:00) | Plantilla del simulacro de test, tabla de errores típicos (apartado 10) y lista de comprobación de la práctica. | 15 min |

**Las 8 ideas que no pueden faltar:**
1. `start()` crea el hilo; `run()` no.
2. `join()`: espera **el que llama**. `join(0)` = sin límite.
3. `sleep` es estático y duerme al hilo actual.
4. `interrupt()` es una petición: despierta `sleep`/`join`/`wait`; si no, hay que mirar el indicador.
5. `x++` no es atómico → condición de carrera.
6. `volatile` = visibilidad, **no** atomicidad.
7. `synchronized` solo protege si todos usan **el mismo** monitor.
8. Bernstein: flujo (lee lo que el otro escribe), antidependencia (escribe lo que el otro lee), salida (los dos escriben).
