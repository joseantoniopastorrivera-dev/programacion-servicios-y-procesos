# Soluciones · Ejercicios de ProcessBuilder y Process (UT1)

> Enunciados: `psp-ut1-ejercicios-1.docx`. Código: [`../soluciones/processbuilder/src/PSP/`](../soluciones/processbuilder/src/PSP/). Teoría: [`../apuntes/apuntes-ut1-procesos.md`](../apuntes/apuntes-ut1-procesos.md).
>
> Cada ejercicio funciona en **Linux** (examen) y en **Windows** (casa). En el examen se escribe directamente el comando de Linux: es el **primero** de cada `SO.comando(linux, windows)`.
> Salidas: **Windows = reales**. **Linux = lo esperado** según la teoría, pendiente de comprobar en la VM «PSP Ubuntu» (se marcará ✅ al probarlo).
>
> ⚠️ El enunciado usa comandos de **macOS** (`open -a Calculator`), que **no existen** ni en Linux ni en Windows.

**Compilar y ejecutar** (desde `soluciones/processbuilder/`):

```bash
javac -encoding UTF-8 -d bin src/PSP/*.java
java -cp bin PSP.Ejercicio4
```

---

## Bloque 1 · Lanzar procesos

### Ejercicio 1. Mi primer proceso

| | Calculadora | Editor |
|---|---|---|
| macOS (enunciado) | `open -a Calculator` | `open -a TextEdit` |
| **Linux** | `gnome-calculator` (GNOME) · `kcalc` (KDE) | `gnome-text-editor` / `gedit` (GNOME) · `kate` (KDE) |
| **Windows** | `calc.exe` | `notepad.exe` |

**Versión de examen (Linux, una línea):**

```java
Process p = new ProcessBuilder("gnome-calculator").start();   // o "kcalc" si el escritorio es KDE
```

La solución prueba una lista de calculadoras hasta que una arranca, porque cada escritorio de Linux trae la suya: `start()` lanza `IOException` si el ejecutable no existe, se captura y se prueba el siguiente.

- `start()` **no espera**: el programa Java sigue (y termina) mientras la calculadora queda abierta.
- **Ubuntu Server (la VM) no tiene escritorio**: allí no hay calculadora y el programa lo indica.

### Ejercicio 2. ¿Quién es quién?

```java
System.out.println("PID de la calculadora: " + calculadora.pid());
System.out.println("PID de este programa:  " + ProcessHandle.current().pid());
Thread.sleep(30000);
```

**Reflexión — comprobar los PID:**

| Linux | Windows |
|---|---|
| `ps -ef \| grep java` · `ps -ef \| grep calc` | `tasklist \| findstr java` · `tasklist \| findstr -i calc` |

El PID de Java coincide siempre. El de la calculadora **puede no coincidir**:

- En **Windows 10/11**, `calc.exe` es solo un **lanzador**: abre la Calculadora (`CalculatorApp.exe`, otro proceso) y termina enseguida. El PID que da Java es el del lanzador, que ya no existe.
- En **Linux**, si ya había una `gnome-calculator` abierta, la nueva puede avisar a la existente y cerrarse (aplicación de instancia única).

### Ejercicio 3. Lanzador por argumentos

```java
if (args.length == 0) { /* explicar el uso */ return; }
System.out.println("Comando a lanzar: " + String.join(" ", args));
Process p = new ProcessBuilder(args).start();
```

- `args` ya es un `String[]` con un argumento por posición: es justo lo que necesita `ProcessBuilder`.
- `String.join(" ", args)` solo sirve para **mostrar** el comando.
- Linux: `java -cp bin PSP.Ejercicio3 mkdir carpetaNueva`. Windows: `java -cp bin PSP.Ejercicio3 cmd /c mkdir carpetaNueva` (en Windows `mkdir` es interno de `cmd`).

Salida real sin argumentos:

```text
Uso: java -cp bin PSP.Ejercicio3 <comando> [argumentos...]
Ejemplo: java -cp bin PSP.Ejercicio3 notepad.exe
```

---

## Bloque 2 · Esperar y códigos de salida

### Ejercicio 4. ¿Ha ido bien?

```java
Process p = new ProcessBuilder("mkdir", "pruebas").start();          // Windows: "cmd", "/c", "mkdir", "pruebas"
int codigo = p.waitFor();
System.out.println(codigo == 0 ? "Carpeta creada" : "Error al crear la carpeta");
```

Salida real (dos ejecuciones seguidas):

```text
Código de salida: 0
Carpeta creada
Código de salida: 1
Error al crear la carpeta
```

**Reflexión:** la segunda vez da **1** porque la carpeta **ya existe** y `mkdir` falla (Linux: `mkdir: cannot create directory 'pruebas': File exists`). Es lo mismo en Windows.

### Ejercicio 5. Comprobador de conexión

```java
String[] comando = {"ping", "-c", "1", host};          // Windows: {"ping", "-n", "1", host}
int codigo = new ProcessBuilder(comando).start().waitFor();
System.out.println(codigo == 0 ? host + " responde" : host + " NO responde");
```

Salida real (Windows):

```text
localhost → Código de salida de ping: 0 → localhost responde
noexiste.invalid → Código de salida de ping: 1 → noexiste.invalid NO responde
```

- En Linux `ping` sin `-c` **no termina nunca** (envía paquetes sin parar): el `waitFor()` se quedaría colgado.
- Linux devuelve 2 cuando el nombre no se resuelve y 1 cuando no hay respuesta; en los dos casos es ≠ 0.

### Ejercicio 6. Trabajar en otra carpeta

```java
ProcessBuilder pb = new ProcessBuilder("touch", "fichero.txt");   // Windows: "cmd", "/c", "type nul > fichero.txt"
pb.directory(new File("pruebas"));                                // el hijo se ejecuta DENTRO de pruebas/
pb.start().waitFor();
System.out.println(new File("pruebas", "fichero.txt").exists());   // true
```

- `directory()` cambia la **carpeta de trabajo del hijo**, no la del programa Java. Las rutas relativas del hijo se resuelven desde ahí.
- En Windows funciona el `>` porque se lo damos **a `cmd`** dentro de una sola cadena: es `cmd`, no Java, quien redirige.

Salida real: `Código de salida: 0 · ¿Existe pruebas\fichero.txt?: true`.

---

## Bloque 3 · Varios procesos a la vez

### Ejercicio 7. Secuencial frente a paralelo

```java
// A: secuencial
for (int s : SEGUNDOS) { new ProcessBuilder("sleep", "" + s).start().waitFor(); }
// B: paralelo
for (int i...) procesos[i] = new ProcessBuilder("sleep", "" + SEGUNDOS[i]).start();
for (Process p : procesos) p.waitFor();
```

Salida real (Windows, con PowerShell `Start-Sleep`):

```text
Versión A (secuencial): 10,48 s
Versión B (paralela):    5,60 s
```

**Reflexión:** secuencial = **suma** (3 + 5 + 1 = 9 s); paralelo = el **más lento** (5 s). En Windows sale algo más porque arrancar cada PowerShell cuesta casi medio segundo. En **Linux**, con `sleep`, sale prácticamente 9,0 s y 5,0 s.

### Ejercicio 8. Java lanza Java

```java
// Hijo
System.exit(Integer.parseInt(args[0]));
// Padre
Process hijo = new ProcessBuilder("java", "-cp", "bin", "PSP.Hijo", "42").start();
System.out.println(hijo.waitFor());   // 42
```

Salida real (Windows) y esperada (Linux):

| `System.exit(...)` | Windows (real) | Linux |
|---|---|---|
| 0 · 5 · 42 | 0 · 5 · 42 | 0 · 5 · 42 |
| 300 | 300 | **44** |
| -1 | -1 | **255** |

**Reflexión:** el código de salida es una forma de comunicación **muy limitada**: solo un número, y en Linux solo de **0 a 255** (se guarda 1 byte: 300 mod 256 = 44; −1 → 255). Además se suele reservar para indicar **éxito o error**. Para devolver datos de verdad se usa la **salida estándar** (ejercicios de comunicación).

---

## Bloque 4 · Controlar y terminar procesos

### Ejercicio 9. Tiempo límite

```java
Process p = new ProcessBuilder("sleep", "10").start();
if (!p.waitFor(3, TimeUnit.SECONDS)) {
    System.out.println("Antes: " + p.isAlive());   // true
    p.destroy();
    p.waitFor();                                    // destroy() no espera
    System.out.println("Después: " + p.isAlive());  // false
}
System.out.println(p.exitValue());
```

Salida real (Windows):

```text
¿Terminó en 3 s?: false
Antes de destroy(). ¿Vivo?: true
Después de destroy(). ¿Vivo?: false
Código de salida: 1          ← Linux: 143
```

**Reflexión:** en Linux, `destroy()` envía **SIGTERM** (señal **15**, «terminación»: le pide al proceso que acabe). Cuando un proceso muere por una señal, su código de salida es **128 + nº de señal** → 128 + 15 = **143**. En Windows no hay señales: se usa `TerminateProcess` y el código es 1.

### Ejercicio 10. El proceso rebelde

```java
Process p = new ProcessBuilder("sh", "-c", "trap '' TERM; sleep 30").start();
p.destroy();
if (!p.waitFor(2, TimeUnit.SECONDS)) {       // en Linux NO termina: ignora SIGTERM
    p.destroyForcibly();                     // SIGKILL
    System.out.println(p.waitFor());         // 137
}
```

| | `destroy()` | `destroyForcibly()` | Código |
|---|---|---|---|
| **Linux** | SIGTERM, que el `trap '' TERM` **ignora** → sigue vivo | SIGKILL (9): **no se puede ignorar** | **137** = 128 + 9 |
| **Windows** (real) | Ya lo termina: no hay señales que ignorar | Igual que `destroy()` | 1 |

**Reflexión:** **pedir** (SIGTERM) deja que el proceso decida: puede limpiar, guardar o incluso ignorarlo. **Obligar** (SIGKILL) lo hace el kernel, sin que el proceso pueda hacer nada. Por eso el código es 137 (señal 9) y no 143 (señal 15).

> Detalle: `sh -c "...; sleep 30"` deja el `sleep 30` como **nieto**. Al matar la shell, el `sleep` quedaría huérfano unos segundos; la solución lo termina también con `descendants()`.

### Ejercicio 11. Árbol de procesos

```java
Process p = new ProcessBuilder("sh", "-c", "sh -c 'sleep 60 & wait' & sleep 60 & wait").start();
Thread.sleep(500);
System.out.println(p.children().count() + " / " + p.descendants().count());
Iterator<ProcessHandle> it = p.descendants().iterator();   // sin lambdas
while (it.hasNext()) {
    ProcessHandle h = it.next();
    System.out.println(h.pid() + " " + h.parent().get().pid() + " " + h.info().commandLine().orElse("?"));
}
```

**Linux (esperado):** `children() = 2`, `descendants() = 3`.

```text
sh (padre, PID 100)
├── sh -c 'sleep 60 & wait'   (hijo, 101)
│   └── sleep 60              (nieto, 103)
└── sleep 60                  (hijo, 102)
```

**Windows (real):** como no hay `sh`, se construye el mismo árbol con procesos Java (`NodoArbol`), pero salen `children() = 3` y `descendants() = 7`, porque **cada proceso de consola tiene además un hijo `conhost.exe`** (el anfitrión de consola de Windows).

**Reflexión:** `children()` devuelve solo los hijos **directos**; `descendants()`, **todo el subárbol** (hijos, nietos…). Para destruir el árbol entero hay que usar `descendants()`: si solo matas al padre, los hijos y el nieto **siguen vivos** (quedan huérfanos y los adopta `systemd`/`init`).

---

## Bloque 5 · Reto final

### Ejercicio 12. Mini gestor de procesos

Menú en bucle: lanzar, listar, terminar por PID, **terminar cualquier proceso del sistema (ampliación)** y salir.

| Opción | Claves de la solución |
|---|---|
| Lanzar | `linea.split("\\s+")` → `new ProcessBuilder(partes).start()`; se guarda el `Process` y su comando en dos `ArrayList` con el mismo índice |
| Listar | `isAlive()`; si ya terminó, `exitValue()` (sin `isAlive()` antes lanzaría excepción) |
| Terminar por PID | Busca el `Process` con ese `pid()`; `destroy()` y, si en 2 s no ha terminado, `destroyForcibly()`. Si el PID no es suyo, avisa |
| Ampliación | `ProcessHandle.of(pid)` → `Optional`: vacío si el PID no existe. `destroy()` devuelve `false` si el SO no lo permite (proceso de otro usuario) |
| Salir | Termina todos los que sigan vivos antes de acabar |

Salida real (Windows):

```text
Comando (p. ej. ping -n 100 127.0.0.1): Lanzado con PID 26580
PID 26580 · ping -n 30 127.0.0.1 · vivo
PID a terminar: Aviso: el PID 99999 no es de ningún proceso lanzado por el gestor.
Proceso 26580 terminado con código 1
Hasta luego.
```

Para probar la ampliación en Linux: en otra terminal, `sleep 300 &` y `echo $!` muestra su PID; en el gestor, la opción 4 con ese PID.

> Además, si se cierra la entrada (Ctrl+D en Linux, Ctrl+Z en Windows), el gestor sale ordenadamente en vez de lanzar una excepción.

---

## Chuleta de métodos del enunciado

| Clase | Método | Qué hace |
|---|---|---|
| ProcessBuilder | `new ProcessBuilder(String...)` | Prepara un comando y sus argumentos |
| ProcessBuilder | `command(...)` / `command()` | Cambia o consulta el comando |
| ProcessBuilder | `directory(File)` | Carpeta de trabajo del proceso |
| ProcessBuilder | `start()` | Lanza el proceso y devuelve un `Process` |
| Process | `pid()` | PID del proceso |
| Process | `waitFor()` | Espera a que termine y devuelve el código de salida |
| Process | `waitFor(long, TimeUnit)` | Espera como máximo ese tiempo; `true` si terminó |
| Process | `exitValue()` | Código de salida; excepción si aún no ha terminado |
| Process | `isAlive()` | `true` si sigue en ejecución |
| Process | `destroy()` | Pide que termine (SIGTERM) |
| Process | `destroyForcibly()` | Obliga a terminar (SIGKILL) |
| Process | `children()` / `descendants()` | Hijos directos / todos los descendientes |
| ProcessHandle | `ProcessHandle.current()` | El proceso actual |
| ProcessHandle | `ProcessHandle.of(pid)` | Cualquier proceso del sistema por su PID |
| ProcessHandle | `parent()` | Proceso padre |
| ProcessHandle | `info().commandLine()` | Comando con el que se lanzó |
