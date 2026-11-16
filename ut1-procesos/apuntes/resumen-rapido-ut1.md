# UT1 · Procesos — Resumen rápido para el examen

> **Cómo usarlo:** léelo de principio a fin en unos 40 minutos. Cada apartado tiene la idea, un ejemplo corto y la **⚠️ trampa** que suele caer en el examen.
> **Examen (cómo fue de verdad):** un **test** difícil, con preguntas «a pillar» y muchas de **hilos**; **tres análisis de código**; y **dos ejercicios en el PC** (**Linux** + IntelliJ), C1 y C2. Las preguntas reales están en el apartado 18, el plan de estudio en el 19 y el proyecto en `../examen/`.
> Si quieres más detalle de teoría: `apuntes-ut1-procesos.md`.

---

## 1. Programa, proceso y ejecutable

| Concepto | Qué es | Ejemplo |
|---|---|---|
| **Programa** | Instrucciones + datos **en disco**. Es **estático** | `gnome-calculator`, `Hijo.class` |
| **Proceso** | Un programa **en ejecución**. Es **dinámico**: nace, cambia de estado y muere | La calculadora abierta, con PID 4321 |
| **Ejecutable** | Fichero con lo necesario para que el SO cree el proceso | `.exe` (Windows), ELF (Linux) |

**Contexto de un proceso** (lo que el SO guarda para poder pararlo y reanudarlo): contador de programa (PC), registros, memoria (código, datos, pila y montículo) y recursos (ficheros abiertos, PID, usuario, prioridad).

- Del **mismo programa** puede haber **varios procesos** a la vez. Cada uno tiene su memoria y su PID.
- **Java:** `javac` genera **bytecode** (`.class`), no un `.exe`. Lo ejecuta la **JVM**, que lo traduce al vuelo (JIT). El proceso que ves en `ps` es `java`, no `Hijo`.

> ⚠️ «Un programa puede tener varios procesos» → **verdadero**. «Un proceso es el fichero `.class`» → **falso**.

---

## 2. Multitarea, concurrencia, paralelismo y distribución

- **Multitarea:** varios procesos en el mismo intervalo de tiempo. Con **un núcleo** se **intercalan** (no van a la vez de verdad). Para pasar de uno a otro, el SO hace un **cambio de contexto**, que **cuesta tiempo**.
- **Concurrente:** varios procesos a la vez (reales o intercalados) que **colaboran**: se **comunican** y se **sincronizan**.
- **Paralela:** concurrente en **varios procesadores a la vez** para acabar antes.
- **Distribuida:** procesos en **varios ordenadores** conectados por red.

| Multiprocesador | Memoria |
|---|---|
| **Fuertemente acoplado** | **Compartida** (simétrico: todos iguales · asimétrico: maestro-esclavo) |
| **Débilmente acoplado** | Cada procesador tiene **la suya** (clúster) |
| **Distribuido** | Equipos independientes y heterogéneos en red |

**Ejemplo:** tres hijos que tardan 3, 5 y 1 segundos.
- **En secuencia:** tarda unos **9 s**, la **suma**.
- **En paralelo:** tarda unos **5 s**, lo que tarde **el más lento**.

---

## 3. El kernel y las interrupciones

El **kernel** es el núcleo del SO: pequeño, optimizado y con acceso total al hardware. Funciona con **interrupciones**: para lo que se está ejecutando, atiende otra cosa y luego vuelve. Hay tres causas:

1. **Hardware:** una tecla, un paquete de red, el final de una lectura de disco.
2. **Reloj:** cada pocos milisegundos salta el **planificador a corto plazo**. Esto es lo que hace posible la multitarea.
3. **Llamadas al sistema:** el programa le pide algo al kernel (abrir un fichero, **crear un proceso**…).

> `pb.start()` en Java = llamada al sistema por debajo: `fork`/`exec` en Linux, `CreateProcess` en Windows.

---

## 4. Estados de un proceso y planificación

```text
              admitido            le dan la CPU
   NUEVO ─────────────► LISTO ─────────────────► EN EJECUCIÓN ──── acaba/lo matan ───► TERMINADO
                          ▲  ◄──── fin del quantum ──────┘ │
                          │                                │ espera E/S o un evento
                          └──── llega el dato ──── BLOQUEADO ◄┘
```

| Estado | Significado |
|---|---|
| Nuevo | Se está creando |
| Listo | Podría ejecutarse, **espera su turno** de CPU |
| En ejecución | Usando la CPU **ahora** |
| Bloqueado | Espera algo (E/S, un dato). **No gasta CPU** |
| Terminado | Ha acabado. El SO guarda su **código de salida** para el padre |

> ⚠️ **Bloqueado → En ejecución NO existe.** Al llegar el dato, el proceso vuelve a **Listo** y espera su turno. Tampoco existe **Listo → Bloqueado**: para bloquearse, el proceso tiene que estar ejecutándose.
> ⚠️ Un `br.readLine()` sobre un hijo que aún no ha escrito deja al padre **bloqueado**, no en espera activa.

- **Colas:** de procesos (todos), de **listos** y de **bloqueados**.
- **Planificador a largo plazo:** decide qué procesos nuevos entran en la cola de listos. Se ejecuta poco.
- **Planificador a corto plazo:** decide qué proceso listo pasa a ejecutarse. Se ejecuta muchísimo, así que tiene que ser **muy rápido**.
- **Tipos de planificación:**
  - **Sin desalojo (cooperativa):** el proceso solo suelta la CPU si se bloquea o termina.
  - **Apropiativa (con desalojo):** el SO le quita la CPU. Es la de los SO actuales.
  - **Round Robin:** cada proceso tiene un **quantum**; al agotarlo, vuelve a **Listo**.
- **Cambio de contexto:** el SO guarda el estado, los registros y la información de memoria del proceso que sale, y carga los del que entra.

---

## 5. Árbol de procesos

- Todo proceso lo crea **otro** (su **padre**). En Linux, la raíz es `systemd`/`init`, con **PID 1**.
- **PID:** identificador único mientras el proceso vive.
- **Padre e hijo se ejecutan a la vez** y **NO comparten memoria**. Para comunicarse usan tuberías, ficheros o sockets.
- En Java:
  - `ProcessHandle.current()`: el propio programa.
  - `p.children()`: los hijos **directos**.
  - `p.descendants()`: hijos, nietos… (todo el subárbol).
  - `h.parent()`: el padre (devuelve un `Optional`).

> ⚠️ Un padre con 2 hijos, y uno de ellos con 1 nieto: `children()` = **2** y `descendants()` = **3** (en Linux).

**Comandos de Linux:**
- `ps -ef`: lista los procesos.
- `pstree -p`: muestra el árbol.
- `kill PID`: envía SIGTERM.
- `kill -9 PID`: envía SIGKILL.

---

## 6. Las clases de Java

```text
ProcessBuilder (la receta) ──start()──► Process (el hijo vivo) ──toHandle()──► ProcessHandle (PID, info)
Runtime.getRuntime().exec(...)  → forma ANTIGUA de hacer lo mismo (sin opciones). Usa ProcessBuilder.
```

```java
ProcessBuilder pb = new ProcessBuilder("ls", "-l", "/tmp");   // UN String POR ARGUMENTO
pb.directory(new File("/home/alumno"));                       // carpeta de trabajo del hijo
Process p = pb.start();                                       // crea el proceso y NO espera
System.out.println("PID: " + p.pid());
```

| `ProcessBuilder` | | `Process` | |
|---|---|---|---|
| `command(...)` | cambiar o ver el comando | `pid()` | su PID |
| `directory(File)` | carpeta de trabajo | `waitFor()` | **espera** y devuelve el código de salida |
| `redirectInput/Output/Error` | conectar a ficheros | `waitFor(3, TimeUnit.SECONDS)` | espera como máximo 3 s → `true` si terminó |
| `redirectErrorStream(true)` | mezcla stderr con stdout | `exitValue()` | código de salida (excepción si aún vive) |
| `inheritIO()` | usa la consola del padre | `isAlive()` | ¿sigue vivo? |
| `environment()` | variables de entorno | `destroy()` / `destroyForcibly()` | pedir / obligar a terminar |
| `start()` | lanza el proceso | `getInputStream/OutputStream/ErrorStream` | los canales del hijo |
| `startPipeline(lista)` | tubería de procesos | `info()` | comando, inicio, usuario… (todo `Optional`) |

**Sin shell:** `ProcessBuilder` lanza el programa **directamente**. Por eso no funcionan `|`, `>`, `&&`, `*`, `~` ni `$HOME`.
- Si los necesitas, lanza tú la shell: `new ProcessBuilder("sh", "-c", "ls | wc -l")`.
- Las comillas de la terminal sobran: `date "+%H"` en la terminal se escribe `("date", "+%H")` en Java.

> ⚠️ `new ProcessBuilder("ping -c 1 google.com")` → **IOException: Cannot run program "ping -c 1 google.com"**. Buscaría un programa con ese nombre entero.
> ⚠️ Si el programa no existe, `start()` lanza **IOException**. El proceso **nunca llega a existir**, así que no tiene código de salida.
> ⚠️ `info().command()` devuelve un **Optional**. Se usa `.orElse("-")`.

---

## 7. Esperar, códigos de salida y matar procesos

- **Código de salida:** **0 = bien**; cualquier otro valor = algo falló. Un hijo Java lo fija con `System.exit(n)`; si termina normal, sale 0.
- `grep`: **0** encontrado · **1** no encontrado · **2** error (por ejemplo, el fichero no existe).
- **En Linux el código es 1 byte (0-255)**: el padre recibe `n mod 256`.

| `System.exit(...)` en el hijo | El padre recibe en **Linux** |
|---|---|
| `0`, `5`, `42` | 0, 5, 42 |
| `256` | **0** |
| `300` | **44** (300 − 256) |
| `-1` | **255** |

| | Qué hace en Linux | ¿Se puede ignorar? | Código de salida |
|---|---|---|---|
| `destroy()` | envía **SIGTERM** (15): «por favor, termina» | **Sí** (`trap '' TERM`) | **143** (128 + 15) |
| `destroyForcibly()` | envía **SIGKILL** (9): el kernel lo mata | **No** | **137** (128 + 9) |

```java
Process p = new ProcessBuilder("sleep", "10").start();
if (!p.waitFor(3, TimeUnit.SECONDS)) {   // espera como mucho 3 s
    p.destroy();                          // pide que termine
    if (!p.waitFor(2, TimeUnit.SECONDS)) p.destroyForcibly();   // y si no, lo obliga
}
System.out.println(p.waitFor());          // 143  (destroy() NO espera: por eso hay otro waitFor)
```

> ⚠️ En Windows `destroy()` mata siempre y el código es **1**. **El examen es en Linux:** 143 y 137.
> ⚠️ El código de salida solo sirve para devolver un número pequeño. Para devolver **datos** se usa la **salida estándar**.

---

## 8. Los tres canales: stdin, stdout y stderr

| Canal | Nº | En el hijo | Desde el padre |
|---|---|---|---|
| Entrada (stdin) | 0 | `System.in` / `Scanner` | `p.getOutputStream()` → **escribe** |
| Salida (stdout) | 1 | `System.out` | `p.getInputStream()` → **lee** |
| Error (stderr) | 2 | `System.err` | `p.getErrorStream()` → **lee** |

> ⚠️ **Los nombres engañan.** Piensa siempre **desde el padre**: `getInputStream()` es lo que **entra al padre**, es decir, lo que **sale del hijo**.
> Un hijo lanzado con `ProcessBuilder` **no tiene terminal**: si el padre no lee su salida, nadie la ve (salvo con `inheritIO()`).

**Leer la salida del hijo:**

```java
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
String linea;
while ((linea = br.readLine()) != null)   // null = el hijo ha cerrado su salida
    System.out.println("[HIJO] " + linea);
```

Cada pieza tiene su papel:
- `InputStream` lee **bytes** (`read()` devuelve de 0 a 255, o −1 al final).
- `InputStreamReader` convierte los bytes en **caracteres**.
- `BufferedReader` añade `readLine()`.

**Enviar datos al hijo:**

```java
PrintWriter pw = new PrintWriter(p.getOutputStream());
pw.println("2^10");
pw.flush();   // envía YA y el canal sigue abierto  → para CONVERSAR (mando, leo, mando…)
pw.close();   // envía y CIERRA → el hijo recibe "fin de entrada" → para un ENVÍO ÚNICO (bc, sort, tr)
```

> ⚠️ Sin `flush()`, el dato se queda en el búfer del padre y **el hijo no lo recibe**.
> ⚠️ Sin `close()`, `bc` o `sort` se quedan **esperando más datos para siempre**: el padre se cuelga en `readLine()`.

**Protocolo:** son las reglas que acuerdan padre e hijo para conversar: qué se envía, en qué formato, qué se responde y cómo se termina (por ejemplo, «una operación por línea; `fin` termina»).

---

## 9. Redirecciones y tuberías

Los canales del hijo se pueden conectar a **ficheros**. Así los datos **no pasan por Java**. Se configura **antes de `start()`**.

```java
pb.redirectInput(new File("notas.csv"));                                   //  < notas.csv
pb.redirectOutput(new File("salida.txt"));                                 //  > salida.txt  (sobrescribe)
pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File("log.txt")));  //  >> log.txt    (añade)
pb.redirectError(new File("errores.txt"));                                 //  2> errores.txt
pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);                        //  a la consola del padre
pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);                        //  a /dev/null
pb.inheritIO();                                                            //  los TRES a la consola
```

Valores de `ProcessBuilder.Redirect`:
- **`PIPE`** (por defecto): tubería entre padre e hijo.
- **`INHERIT`**: el mismo canal que el padre.
- **`to(f)`**: a un fichero, sobrescribiendo.
- **`appendTo(f)`**: a un fichero, añadiendo al final.
- **`from(f)`**: lee la entrada de un fichero.
- **`DISCARD`**: tira la salida.

> ⚠️ Si rediriges la salida a un fichero, `getInputStream()` ya **no trae nada**: `readLine()` devuelve `null` enseguida.

**Tubería (pipeline)**, como `ls | sort | wc -l`:

```java
List<ProcessBuilder> lista = List.of(
        new ProcessBuilder("ls"),
        new ProcessBuilder("sort"),
        new ProcessBuilder("wc", "-l"));
List<Process> procs = ProcessBuilder.startPipeline(lista);
Process ultimo = procs.get(procs.size() - 1);    // el padre lee SOLO del último
```

---

## 10. Sincronización y regla de oro

La comunicación por tubería es **unidireccional y bloqueante**:
- `readLine()` bloquea al padre hasta que llega una línea (o `null`).
- `waitFor()` bloquea al padre hasta que el hijo **termina**.

> ### ⚠️ REGLA DE ORO: primero LEER toda la salida, DESPUÉS `waitFor()`
> La tubería tiene un **búfer limitado** (unos 64 KB en Linux). Si el padre está parado en `waitFor()` sin leer:
> 1. El hijo llena el búfer y **se bloquea** esperando a que alguien lea.
> 2. El padre espera a que el hijo **termine**.
>
> Ninguno de los dos avanza: **interbloqueo (deadlock)**. Con poca salida no pasa, porque cabe en el búfer.
> Lo mismo ocurre con **stderr**. Para evitarlo: léelo también, o usa `redirectErrorStream(true)`, o redirígelo a un fichero.

**Diseño de un programa multiproceso:**
1. Descomposición funcional.
2. Partición en procesos (cuanto más **independientes**, mejor, porque sincronizar cuesta).
3. Implementación.

Otras formas de comunicación (no entran en esta UT): sockets, JNI, memoria compartida.

---

## 11. Java lanza Java

```java
new ProcessBuilder("java", "-cp", "bin", "PSP.Hijo", "5", "7").start();
//                  JVM    dónde están    clase con      argumentos
//                         los .class     paquete        de su main (args[0]="5", args[1]="7")
```

- El hijo es **otra JVM**: otro proceso con otra memoria. Solo se comunica por los canales estándar o con el código de salida.
- `bin` es relativa a la **carpeta desde la que ejecutas al padre** (la raíz del proyecto).
- Si falla con `ClassNotFoundException: PSP.Hijo`, hay dos causas posibles: el hijo **no está compilado** o el padre se ejecuta **desde la carpeta equivocada**.

> ⚠️ **En IntelliJ, `-cp bin` puede fallar.** Los proyectos de clase compilan en `bin`, pero un proyecto **nuevo** de IntelliJ compila en `out/production/<NombreProyecto>`. Además, el `java` del PATH de Linux puede no existir o ser otra versión (`UnsupportedClassVersionError`).
> **Forma segura, que funciona en cualquier caso:** usa la misma JVM y el mismo classpath que el padre (es lo que hace tu SumManager):

```java
String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
String cp   = System.getProperty("java.class.path");      // donde IntelliJ ha compilado TUS clases
new ProcessBuilder(java, "-cp", cp, "PSP.Hijo", "5", "7").start();
```

- **Directorio de trabajo en IntelliJ:** por defecto es la **raíz del proyecto**. Ahí se resuelven las rutas relativas como `datos/notas.csv`, y ahí aparecen los ficheros que crees (`salida.txt`).
- Las clases del hijo se compilan solas cuando ejecutas al padre, siempre que estén en el **mismo proyecto**.

**Compilar y ejecutar en la terminal de Linux:**

```bash
javac -d bin src/PSP/*.java
java -cp bin PSP.Padre
```

---

## 12. Entrenamiento «¿Qué sale por pantalla?»

**Antes de leer cada respuesta, tápala y predice la salida.**

**A)** Orden padre-hijo:
```java
ProcessBuilder pb = new ProcessBuilder("echo", "Hijo");
pb.inheritIO();
Process p = pb.start();
System.out.println("Padre");
p.waitFor();
System.out.println("Fin");
```
→ `Padre` y `Hijo` en **orden no garantizado** (lo normal es que salga `Padre` antes, porque arrancar un proceso tarda). `Fin` sale **siempre el último**, gracias a `waitFor()`.

**B)** `exitValue()` con el hijo vivo:
```java
Process p = new ProcessBuilder("sleep", "5").start();
System.out.println(p.exitValue());
```
→ **`IllegalThreadStateException`**: el hijo aún no ha terminado.

**C)** El hijo hace `System.exit(300)` y el padre imprime `p.waitFor()` en Linux.
→ **44**.

**D)** Sin shell:
```java
new ProcessBuilder("echo", "hola", ">", "f.txt").inheritIO().start().waitFor();
```
→ Imprime **`hola > f.txt`** en la consola y **no crea ningún fichero**: `>` es un argumento más para `echo`.

**E)** Redirección y lectura:
```java
ProcessBuilder pb = new ProcessBuilder("ls");
pb.redirectOutput(new File("lista.txt"));
Process p = pb.start();
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
System.out.println("Leído: " + br.readLine());
System.out.println("Código: " + p.waitFor());
```
→ `Leído: null` y `Código: 0`. El listado está en `lista.txt`, no en la tubería.

**F)** Matar al hijo:
```java
Process p = new ProcessBuilder("sleep", "30").start();
p.destroy();
System.out.println(p.waitFor());
```
→ **143** en Linux. Con `destroyForcibly()` sería **137**.

**G)** `grep -q xyz /etc/hostname` (no encuentra nada) → el padre recibe **1**. Si el fichero no existe → **2**.

**H)** Comando inexistente:
```java
try { new ProcessBuilder("comandoInventado").start(); System.out.println("Lanzado"); }
catch (IOException e) { System.out.println("No existe"); }
```
→ **`No existe`**. `Lanzado` no llega a imprimirse.

**I)** Contar hijos (el padre lanza 2 hijos; el primero lanza a su vez un nieto):
```java
System.out.println(ProcessHandle.current().children().count());     // 2
System.out.println(ProcessHandle.current().descendants().count());  // 3
```

### Entrenamiento «Explica estas líneas»

En el examen había que explicar líneas sueltas de un fragmento. Para cada línea, di **tres cosas**: qué hace, por qué está ahí y qué pasaría si faltara o estuviera en otro sitio. **Tapa la columna de la derecha y explícalo en voz alta.**

```java
1  ProcessBuilder pb = new ProcessBuilder("sort", "-n");
2  pb.redirectInput(new File("datos/notas.txt"));
3  pb.redirectErrorStream(true);
4  Process p = pb.start();
5  BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
6  String l;
7  while ((l = br.readLine()) != null) System.out.println(l);
8  int codigo = p.waitFor();
```

| Línea | Qué hay que decir |
|---|---|
| 1 | Prepara el comando, **no lo lanza**. Un String por argumento: sin shell, `"sort -n"` junto daría `IOException` |
| 2 | La entrada del hijo viene del fichero (`< datos/notas.txt`). Va **antes** de `start()`; después ya no tiene efecto. La ruta es relativa a la carpeta desde la que se ejecuta el padre |
| 3 | Mezcla stderr con stdout: los errores llegan por el mismo canal y no queda una tubería sin leer que pueda llenarse |
| 4 | Crea el proceso (llamada al sistema) y **no espera**. Lanza `IOException` si el programa no existe |
| 5 | `getInputStream()` es la **salida** del hijo vista desde el padre. Bytes → caracteres → líneas |
| 7 | Lee hasta `null`, que llega cuando el hijo **cierra su salida**. Mientras no hay línea, el padre está **bloqueado** (no gasta CPU) |
| 8 | Espera a que termine y devuelve el código de salida. Va **después** de leer: si fuera antes y la salida no cupiera en el búfer (64 KB), **interbloqueo** |

Repite el ejercicio con las plantillas P3, P7 y P8: son las que tienen más líneas «con trampa» (`flush`, `close`, `waitFor` con tiempo, `destroy`, `appendTo`).

### Los tres análisis de código que cayeron en el examen

Los corrigió el profesor en clase. El código está **reconstruido** a partir de mis notas (no es el literal del examen), pero la idea de cada uno sí es la suya. Los tres están compilados y ejecutados, también en su versión «rota».

**Análisis 1 · ¿Qué muestra cada `print`?**

```java
Process p = new ProcessBuilder("sleep", "3").start();          // el hijo dura 3 s
System.out.println("A: " + p.waitFor(1, TimeUnit.SECONDS));
System.out.println("B: " + p.isAlive());
p.destroy();
System.out.println("C: " + p.waitFor());
```

| Print | Sale | Por qué |
|---|---|---|
| A | `false` | `waitFor` **con tiempo** espera como mucho 1 s y devuelve un `boolean`: ¿ha terminado? El hijo dura 3 s, así que **no**. El padre no se queda esperando: **sigue** |
| B | `true` | Ha pasado 1 s de los 3: el hijo sigue vivo |
| C | `143` | `destroy()` pide al hijo que termine, pero **no espera**. El `waitFor()` **sin parámetros** sí bloquea hasta que muere y devuelve su **código de salida**, que es **distinto de 0** porque no terminó por sí mismo: 128 + 15 (SIGTERM) en Linux. En Windows sale 1 |

> ⚠️ Los dos `waitFor` no se parecen: con tiempo → `boolean` y no bloquea más que ese tiempo; sin parámetros → `int` (código de salida) y bloquea hasta el final.

**Análisis 2 · El padre envía palabras a `sort` y se queda colgado. ¿Por qué?**

```java
Process p = new ProcessBuilder("sort").start();
PrintWriter pw = new PrintWriter(p.getOutputStream());
pw.println("pera");
pw.println("manzana");
pw.println("uva");
pw.flush();                                                    // ← el fallo está aquí
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
String l;
while ((l = br.readLine()) != null) System.out.println(l);     // se queda parado para siempre
```

- **El problema no es el `flush()`, es que falta el `close()`.** `flush()` envía las palabras, pero deja el canal abierto.
- `sort` no puede ordenar hasta tener **todas** las líneas, y solo sabe que las tiene todas cuando recibe el **fin de la entrada**. Eso ocurre al **cerrar el flujo** (`pw.close()`). Mientras tanto no escribe nada.
- El padre está bloqueado en `readLine()` esperando una salida que no llega, y `sort` está bloqueado esperando más entrada: **interbloqueo**.
- **Arreglo:** cambiar `pw.flush()` por `pw.close()`. Entonces sale `manzana`, `pera`, `uva`.

> ⚠️ Lo que se cierra **no es un fichero**: es el flujo que conecta al padre con la **entrada estándar del hijo**.

**Análisis 3 · El hijo escribe muchísimas líneas y el programa no termina. ¿Por qué?**

```java
Process p = new ProcessBuilder("seq", "1", "100000").start();  // escribe 100 000 líneas
p.waitFor();                                                   // ← mal colocado
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
int n = 0;
while (br.readLine() != null) n++;
System.out.println("Líneas: " + n);
```

- El `waitFor()` está **demasiado arriba**: el padre espera a que el hijo termine **antes de leer** su salida.
- La tubería tiene un **búfer limitado** (unos 64 KB en Linux). La salida no cabe: el hijo lo llena y se **bloquea** hasta que alguien lea.
- El padre espera a que el hijo termine; el hijo espera a que el padre lea. **Interbloqueo.**
- **Arreglo:** bajar el `waitFor()` **después** del bucle de lectura (regla de oro: primero leer, después esperar).

> ⚠️ **No es un problema de tiempo.** No es que «no dé tiempo a leer»: es que ninguno de los dos puede avanzar, por mucho que se espere. Con una salida pequeña, que cabe en el búfer, el mismo código funciona; por eso el fallo engaña.

---

## 13. Plantillas para la parte práctica

Tienes que poder escribirlas **de memoria**. Todas van con `throws IOException, InterruptedException` en el `main`.

**P1 · Lanzar un comando, mostrar su salida y su código de salida**
```java
ProcessBuilder pb = new ProcessBuilder("ls", "-l");
pb.redirectErrorStream(true);                        // errores mezclados con la salida
Process p = pb.start();
try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    String l;
    while ((l = br.readLine()) != null) System.out.println(l);
}
System.out.println("Código de salida: " + p.waitFor());   // DESPUÉS de leer
```

**P2 · Envío único al hijo (por ejemplo, `bc`)**
```java
Process p = new ProcessBuilder("bc", "-l").start();
try (PrintWriter pw = new PrintWriter(p.getOutputStream())) {
    pw.println("2^10");
}                                                    // close() → bc sabe que no hay más
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
System.out.println("Resultado: " + br.readLine());   // 1024
p.waitFor();
```

**P3 · Conversación con un hijo Java (`flush` en cada mensaje)**
```java
// HIJO (PSP/HijoCuadrado.java): lee números y responde con su cuadrado
Scanner sc = new Scanner(System.in);
while (sc.hasNextLine()) {
    String linea = sc.nextLine().trim();
    if (linea.equals("fin")) break;
    System.out.println(Integer.parseInt(linea) * Integer.parseInt(linea));
}

// PADRE
Process p = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoCuadrado").start();
PrintWriter pw = new PrintWriter(p.getOutputStream());
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
for (int n = 1; n <= 3; n++) {
    pw.println(n);
    pw.flush();                                      // sin flush el hijo no recibe nada
    System.out.println(n + "² = " + br.readLine());
}
pw.println("fin");
pw.close();
System.out.println("Código: " + p.waitFor());
```

**P4 · Redirecciones a ficheros**
```java
ProcessBuilder pb = new ProcessBuilder("sort", "-t", ",", "-k2", "-n");
pb.redirectInput(new File("datos/notas.csv"));
pb.redirectOutput(new File("datos/ordenadas.csv"));
pb.redirectError(ProcessBuilder.Redirect.appendTo(new File("errores.log")));
System.out.println("Código: " + pb.start().waitFor());
```

**P5 · Varios hijos en paralelo y esperar a todos**
```java
List<Process> hijos = new ArrayList<>();
for (String n : new String[]{"3", "5", "1"}) {
    hijos.add(new ProcessBuilder("sleep", n).start());      // los lanzo TODOS primero
}
for (Process h : hijos) {
    System.out.println("PID " + h.pid() + " terminó con " + h.waitFor());   // y luego espero
}
```
> ⚠️ Si haces `start()` + `waitFor()` dentro del mismo bucle, los hijos van **en secuencia**, no en paralelo.

**P6 · Leer la salida de varios hijos Java que calculan algo (estilo SumManager/SumWorker)**
```java
// HIJO: suma sus argumentos y escribe el resultado en stdout
int suma = 0;
for (String a : args) suma += Integer.parseInt(a);
System.out.println(suma);

// PADRE
Process p = new ProcessBuilder("java", "-cp", "bin", "PSP.Sumador", "1", "2", "3").start();
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
int resultado = Integer.parseInt(br.readLine().trim());
System.out.println("El hijo devolvió " + resultado + " (código " + p.waitFor() + ")");
```

**P7 · Lanzar con límite de tiempo, salida a un fichero y tres finales (el C1 del examen)**
```java
ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "PSP.c1.Tarea", "8", "0");
File log = new File("tarea.log");
pb.redirectOutput(log);                              // > tarea.log
pb.redirectErrorStream(true);                        // los errores, al mismo fichero
Process p = pb.start();

if (p.waitFor(3, TimeUnit.SECONDS)) {                // true = terminó a tiempo (devuelve boolean, NO el código)
    int codigo = p.exitValue();                      // seguro: ya ha terminado
    System.out.println(codigo == 0 ? "Terminó bien" : "Terminó con error " + codigo);
} else {
    p.destroy();                                     // SIGTERM; no espera
    if (!p.waitFor(2, TimeUnit.SECONDS)) p.destroyForcibly();
    System.out.println("Destruida, código " + p.waitFor());     // 143 en Linux
}

try (BufferedReader br = new BufferedReader(new FileReader(log))) {   // se lee el FICHERO, no la tubería
    String l;
    while ((l = br.readLine()) != null) System.out.println(l);
}
```
> ⚠️ `int c = p.waitFor(3, TimeUnit.SECONDS);` **no compila**: con tiempo devuelve `boolean`.
> ⚠️ Con la salida redirigida, `p.getInputStream()` no trae nada. Hay que abrir el fichero con `FileReader`.

**P8 · Hijo que valida lo que lee y padre con redirecciones (el C2 del examen)**
```java
// HIJO (PSP/c2/HijoEstadistica.java): lee notas por stdin; datos → stdout, avisos → stderr
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
int validas = 0;
double suma = 0;
String linea;
while ((linea = br.readLine()) != null) {            // null = se acabó la entrada
    linea = linea.trim();
    if (linea.isEmpty()) continue;
    try {
        double nota = Double.parseDouble(linea);     // "siete" → NumberFormatException
        if (nota < 0 || nota > 10) { System.err.println("Fuera de rango: " + linea); continue; }
        validas++;
        suma += nota;
    } catch (NumberFormatException e) {
        System.err.println("No es un número: " + linea);
    }
}
if (validas == 0) System.exit(1);                    // sin datos: se avisa con el código de salida
System.out.println(args[0] + ";" + validas + ";" + suma);

// PADRE: un hijo por grupo, TODOS lanzados antes de esperar a ninguno
String[] grupos = {"grupoA", "grupoB", "grupoC"};
Process[] hijos = new Process[grupos.length];
for (int i = 0; i < grupos.length; i++) {
    ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "PSP.c2.HijoEstadistica", grupos[i]);
    pb.redirectInput(new File("datos/" + grupos[i] + ".txt"));                 // < datos/grupoA.txt
    pb.redirectError(Redirect.appendTo(new File("errores.log")));              // 2>> errores.log
    hijos[i] = pb.start();                                                     // stdout sigue en la tubería
}
int total = 0;
double sumaTotal = 0;
for (Process p : hijos) {
    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String respuesta = br.readLine();                // primero leer…
    if (p.waitFor() == 0 && respuesta != null) {     // …después waitFor
        String[] partes = respuesta.split(";");
        total += Integer.parseInt(partes[1]);
        sumaTotal += Double.parseDouble(partes[2]);
    }
}
System.out.println("Media global: " + sumaTotal / total);
```
> ⚠️ **Media global = suma de todas las notas ÷ número de notas.** La media de las medias está mal si los grupos tienen distinto tamaño.
> ⚠️ `String.format("%.2f", 7.25)` escribe **`7,25`** en un sistema en español y el `Double.parseDouble` del padre falla. Concatena, o usa `String.format(Locale.US, …)`.
> ⚠️ Con `redirectError(new File("errores.log"))` cada hijo **borra** lo que escribió el anterior. Para compartir el fichero, `Redirect.appendTo`.
> `Redirect` necesita `import java.lang.ProcessBuilder.Redirect;` (o escribir `ProcessBuilder.Redirect.appendTo`).

**Imports que necesitas:** `java.io.*`, `java.util.*`, `java.util.concurrent.TimeUnit`, `java.lang.ProcessBuilder.Redirect`.

> En P3 y P6, si `-cp bin` no encuentra la clase en IntelliJ, cámbialo por la **forma segura** del apartado 11 (`java.home` + `java.class.path`).

**Verificación:** todo este código se ha compilado y ejecutado con el JDK 25 y los comandos GNU de Git Bash (`ls`, `sort`, `grep`, `sleep`, `wc`, `echo`). Las salidas coinciden con las del resumen, salvo dos cosas que son propias de Windows:
- `destroy()` devuelve **1** (en Linux, **143**).
- El `sort` de Windows no entiende `-t , -k2` (en Linux sí).

`bc` no se ha podido probar en Windows. En Linux viene instalado.

---

## 14. Errores típicos

| Síntoma | Causa → arreglo |
|---|---|
| `Cannot run program "ls -l"` | Todo el comando en un String → un String por argumento |
| `ls \| wc -l` no hace nada útil | No hay shell → `("sh", "-c", "ls \| wc -l")` o `startPipeline` |
| El programa se cuelga | `waitFor()` antes de leer, o falta `close()` con bc/sort → leer primero; cerrar |
| El hijo no recibe el dato | Falta `flush()` |
| `IllegalThreadStateException` | `exitValue()` con el hijo vivo → `waitFor()` antes |
| `ClassNotFoundException: PSP.Hijo` | Hijo sin compilar o ejecutando desde otra carpeta |
| `exit(300)` llega como 44 | Linux guarda 1 byte → devolver datos por stdout |
| `destroy()` no mata al proceso | Captura SIGTERM → `destroyForcibly()` |

---

## 15. Las 7 ideas que no pueden faltar

1. **Programa** = estático, en disco · **proceso** = en ejecución, con PID, memoria y estado.
2. `start()` **no espera**; `waitFor()` **sí** (y devuelve el código de salida).
3. **Un String por argumento** y **sin shell**: nada de `|`, `>` ni comillas.
4. `getInputStream()` = lo que **sale** del hijo · `getOutputStream()` = lo que **entra** al hijo.
5. **Primero leer, después `waitFor()`**. `flush()` para seguir hablando, `close()` para terminar de enviar.
6. `destroy()` = SIGTERM, **143** · `destroyForcibly()` = SIGKILL, **137** · en Linux los códigos van de **0 a 255**.
7. Java lanza Java con `java -cp bin paquete.Clase args` desde la raíz del proyecto.

---

## 16. Procesos frente a hilos

Cayó dos veces: **seis preguntas del test** (apartado 18, de la 10 a la 15) y la reflexión escrita del C2.4 («¿qué cambiaría con hilos en lugar de procesos?»). Programar con hilos es de la UT2, pero **compararlos con los procesos entra en la UT1**. Hay que saber:

| | Procesos | Hilos |
|---|---|---|
| **Memoria** | Cada uno **la suya**; no comparten nada | **Comparten** la del proceso (cada hilo solo tiene su pila) |
| **Comunicación** | Tuberías, ficheros, sockets, código de salida | Variables y objetos comunes, directamente |
| **Riesgo** | Interbloqueo por tuberías llenas | **Condiciones de carrera** → hay que sincronizar (`synchronized`) |
| **Coste** | Crear y cambiar de contexto es **caro** (en Java, una JVM por hijo) | Mucho más **barato** |
| **Aislamiento** | Si un hijo falla, el padre sigue y lo ve en el código de salida | Un fallo grave o un `System.exit` afecta a **todo** el programa |
| **Esperar** | `waitFor()` | `join()` |
| **Terminar a la fuerza** | `destroy()` / `destroyForcibly()` | No hay equivalente seguro: el hilo tiene que cooperar |

**Respuesta modelo (5-10 líneas):** con hilos, todo se ejecutaría en un único proceso y cada grupo lo calcularía un `Thread` en lugar de un hijo lanzado con `ProcessBuilder`. Los hilos comparten la memoria, así que sobrarían las redirecciones, las tuberías y el formato de texto: cada hilo dejaría su resultado en un objeto común. A cambio aparecerían las condiciones de carrera: si dos hilos actualizan a la vez la suma total, se pierden datos, y habría que sincronizar el acceso o sumar al final los resultados de cada hilo. Sería más rápido y ligero, porque no se arranca una JVM por grupo, pero se perdería el aislamiento: no habría códigos de salida y un fallo no controlado afectaría a todo el programa. Para esperar se usaría `join()` en vez de `waitFor()`.

---

## 17. Cómo fue el examen real

No pude asistir. Lo que sigue sale del proyecto que se repartió en el aula y de lo que contaron los compañeros. Está todo en `../examen/`: el proyecto original sin resolver, la solución comentada y `enunciado-reconstruido.md`.

1. **Test**, difícil y «a pillar», con casi la mitad de las preguntas sobre **hilos** → las 15 preguntas reales están en el apartado 18.
2. **Tres análisis de código** (qué muestra cada `print` y por qué se cuelga) → están en el apartado 12.
3. **C1 · `EjecutorTareas <segundos> <codigo> <esperaMaxima>`** → plantilla **P7**. Lanza una clase `Tarea` ya hecha, redirige su salida a `tarea.log`, la espera con límite y distingue tres finales: bien, con error y destruida.
4. **C2 · `HijoEstadistica` + `Padre`** → plantilla **P8**. Un hijo por grupo que lee notas por stdin (con datos no válidos puestos a propósito), redirecciones de entrada y de errores, media global y la **reflexión sobre hilos** (apartado 16).

**Detalles de la entrega que restan puntos si se olvidan:**
- Escribir **nombre y apellidos** en la cabecera de cada clase (había un `TODO` para eso).
- **No modificar** las clases marcadas como ya hechas (`Tarea`).
- Entregar un zip del proyecto **sin la carpeta `bin`**.
- Probar con las **configuraciones de ejecución** que vienen en el desplegable: traen ya los argumentos de cada caso.

**Para repetirlo como simulacro:** copia `../examen/ut1-examen-alumno/` a otra carpeta, ábrela en IntelliJ, resuélvelo en 90 minutos sin mirar y compáralo con `../examen/solucion/`.

---

## 18. Preguntas del test real

Son las 15 preguntas que copié mientras el profesor corregía el examen en clase. **Tapa la respuesta, contesta y luego comprueba.**

Cómo leer las marcas:
- ✅ respuesta **confirmada por el profesor**.
- 🔎 respuesta **mía**: no llegué a copiar la suya.
- Las opciones en *cursiva* las he añadido yo para completar la pregunta; el resto son las del examen, tal como las apunté.

Casi la mitad del test (preguntas 10 a 15) es de **procesos frente a hilos**: repasa el apartado 16.

### Conceptos generales

**1. ¿Cuál es un ámbito de la programación concurrente?**
> **Incompleta:** no copié las opciones ni la respuesta. Lo que hay que saber: la programación concurrente abarca la **multitarea en un solo procesador** (los procesos se intercalan), la programación **paralela** (varios procesadores a la vez) y la **distribuida** (varios ordenadores en red). Apartado 2.

**2. Un proyecto científico reparte el cálculo entre muchos ordenadores. ¿Qué tipo de programación es?**
- a) Paralela en un sistema fuertemente acoplado
- b) Distribuida
- c) *Secuencial*
- d) *Concurrente en un único procesador*

> ✅ **b) Distribuida.** Son equipos independientes conectados por red. «Fuertemente acoplado» es la trampa: significa varios procesadores que **comparten memoria** dentro de una misma máquina.

**3. Si ejecuto con `java` dos veces el mismo programa, ¿cuántos procesos tengo?**
- a) *Uno, porque es el mismo programa*
- b) Dos
- c) *Uno, con dos hilos*
- d) *Ninguno hasta que el programa llame a `start()`*

> 🔎 **b) Dos.** Cada ejecución de `java` arranca su propia JVM: dos procesos, con PID y memoria distintos. Un programa puede tener varios procesos.

**4. ¿Qué ejecuta realmente el código de `Hijo.class`?**
- a) *El sistema operativo, directamente*
- b) La JVM
- c) *El compilador `javac`*
- d) *El procesador, directamente*

> ✅ **b) La JVM.** Un `.class` es bytecode, no un ejecutable. El proceso que ve el sistema operativo es `java`.

### Estados y planificación

**5. Un programa está parado esperando una línea que tiene que escribir el usuario. ¿En qué estado está?**
- a) *Listo*
- b) Bloqueado
- c) *En ejecución*
- d) *Terminado*

> ✅ **b) Bloqueado.** Espera una entrada/salida y no gasta CPU. «Listo» es la trampa: listo es el que **podría** ejecutarse y solo espera su turno.

**6. En un planificador de tiempo compartido, al proceso se le quita la CPU porque se le acaba el tiempo. ¿Qué transición es?**
- a) *De En ejecución a Bloqueado*
- b) De En ejecución a Listo
- c) *De Listo a En ejecución*
- d) *De Bloqueado a Listo*

> 🔎 **b) De En ejecución a Listo.** Agotar el quantum no bloquea al proceso: sigue pudiendo ejecutarse, así que vuelve a la cola de listos.

**7. ¿Qué guarda el sistema operativo al hacer un cambio de contexto?**
> ✅ La opción correcta era siempre la que reunía **el estado del proceso, los registros y la información de memoria**. Las demás opciones no las copié. Las que dejan fuera alguna de las tres cosas, o hablan del código fuente o del `.class`, son falsas.

**8. En la planificación apropiativa…**
- a) Solo se cambia de proceso cuando el que se ejecuta termina o se bloquea
- b) El usuario decide qué proceso se ejecuta
- c) Un proceso más prioritario puede quitarle la CPU al que se está ejecutando
- d) *Los procesos se ejecutan siempre en orden de llegada, hasta terminar*

> ✅ **c).** Apropiativa = con desalojo: el sistema operativo le **quita** la CPU. La a) describe justo lo contrario, la planificación **sin** desalojo (cooperativa).

**9. ¿Quién crea un proceso nuevo?**
- a) El propio proceso, al ejecutarse
- b) El hardware del PC, sin que intervenga el sistema operativo
- c) Siempre el sistema operativo, a petición de otro proceso
- d) El compilador, al generar el `.class`

> ✅ **c).** Todo proceso tiene un padre que lo pide con una llamada al sistema (`fork`/`exec`, `CreateProcess`). En Java, eso es lo que hay debajo de `pb.start()`.

### Procesos frente a hilos

**10. ¿Qué comparten los hilos de un mismo proceso?**
- a) El PID
- b) La pila de llamadas
- c) La memoria del proceso
- d) El código fuente, pero no los datos

> ✅ **c) La memoria del proceso.** La pila es justo lo que **no** comparten: cada hilo tiene la suya. La d) es falsa porque los datos sí se comparten.
> Sobre la a): los hilos de un proceso están todos bajo el mismo PID (pregunta 11), así que apunté esa opción resumida; seguramente estaba redactada de otra forma. La que hay que marcar es la memoria.

**11. Un programa en ejecución tiene tres hilos trabajando. ¿Cuántos PID tiene?**
- a) Uno
- b) *Tres*
- c) *Cuatro: uno del proceso y uno por hilo*
- d) *Ninguno: los hilos no tienen PID*

> ✅ **a) Uno, el del proceso.** El PID identifica al proceso, no a sus hilos.

**12. Comparado con un proceso, un hilo es…**
- a) Más costoso
- b) Más ligero
- c) *Igual de costoso, pero más seguro*
- d) *Más lento de crear, pero más rápido al ejecutarse*

> ✅ **b) Más ligero.** Crearlo y cambiar de uno a otro cuesta menos, porque no hay que crear ni cambiar un espacio de memoria.

**13. Un hilo modifica por error una variable compartida. ¿A quién afecta?**
- a) Solo al hilo que la ha modificado
- b) A todos los hilos del proceso
- c) *A todos los procesos del sistema*
- d) *A nadie: cada hilo tiene su copia*

> ✅ **b) A todos los hilos del proceso.** Comparten la memoria, así que todos ven el valor estropeado. Es el precio de no tener aislamiento.

**14. ¿Por qué los navegadores ejecutan cada pestaña en un proceso distinto?**
- a) Por aislamiento
- b) Porque el sistema operativo no deja crear hilos
- c) *Porque los procesos consumen menos memoria que los hilos*
- d) *Porque así las pestañas comparten los datos más fácilmente*

> 🔎 **a) Por aislamiento.** Si una pestaña falla o se cuelga, las demás siguen funcionando, y una página no puede leer la memoria de otra. Es la pregunta 13 vista al revés. La b) es falsa: el sistema operativo sí deja crear hilos.

**15. ¿Cómo se pasan datos entre procesos y entre hilos?**
- a) Los dos, mediante la memoria compartida del sistema operativo
- b) Los procesos, mediante flujos y tuberías; los hilos, mediante la memoria que comparten
- c) Los dos, mediante el código de salida
- d) *Los dos, mediante variables globales*

> 🔎 **b).** Los procesos no comparten memoria, así que necesitan un canal (stdin/stdout, tuberías, ficheros, sockets). Los hilos leen y escriben directamente las mismas variables. El código de salida solo devuelve un número pequeño, y solo lo tienen los procesos.

### Plantilla de respuestas

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| — | b | b | b | b | b | — | c | c | c | a | b | b | a | b |

---

## 19. Plan de estudio para la recuperación

1. **Test (45 min).** Apartado 18 con las respuestas tapadas. Cada fallo, al apartado que toca: 1-4 → apartados 1, 2 y 6; 5-9 → apartados 3 y 4; 10-15 → apartado 16.
2. **Análisis de código (30 min).** Los tres del apartado 12, en voz alta y sin mirar la explicación. Después, los ejercicios A a I de «¿Qué sale por pantalla?».
3. **Práctica (90 min).** Simulacro con `../examen/ut1-examen-alumno/` y comparación con `../examen/solucion/`.
4. **Reflexión escrita (10 min).** Escribir de memoria la respuesta modelo del apartado 16.
5. **La víspera.** Solo los ⚠️ de todo el documento, el apartado 15 y la plantilla de respuestas.
