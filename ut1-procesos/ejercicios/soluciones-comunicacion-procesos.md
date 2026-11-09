# Soluciones · Comunicación entre procesos (UT1)

> Enunciados: `ejercicios-comunicacion-entre-procesos/ut1-ej-comprocesos.pdf`. Código: [`../soluciones/comunicacion/src/PSP/`](../soluciones/comunicacion/src/PSP/), basado en la solución del profesor (`UT1_Comunicacion_Solucion.zip`) y ampliado con la versión de **Windows**.
> Teoría: [`../apuntes/apuntes-ut1-procesos.md`](../apuntes/apuntes-ut1-procesos.md) (puntos 8 a 10).
>
> Salidas: **Windows = reales**. **Linux = lo esperado**, pendiente de comprobar en la VM «PSP Ubuntu».

**Compilar y ejecutar** (desde `soluciones/comunicacion/`, donde están `bin/` y `datos/`):

```bash
javac -encoding UTF-8 -d bin src/PSP/*.java
java -cp bin PSP.Ejercicio05 gato datos/texto.txt
```

### Recordatorio del enunciado

| Quiero... | Uso |
|---|---|
| Leer la salida del hijo | `getInputStream()` + `BufferedReader` |
| Leer los errores del hijo | `getErrorStream()` o `redirectErrorStream(true)` |
| Enviar datos y terminar | `getOutputStream()` + `PrintWriter` + `close()` |
| Enviar datos y seguir conversando | `getOutputStream()` + `PrintWriter` + `flush()` |
| Redirigir a ficheros | `redirectInput()`, `redirectOutput()`, `Redirect.appendTo()` |
| Encadenar procesos | `ProcessBuilder.startPipeline(lista)` |

> **Regla de oro:** primero se lee la salida del hijo y **después** `waitFor()`.

---

## Nivel 1 · Leer la salida del hijo

### Ejercicio 1. Eco

```java
Process p = new ProcessBuilder("echo", "Hola desde el hijo").start();
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
String linea;
while ((linea = br.readLine()) != null) System.out.println("[HIJO] " + linea);
br.close();
System.out.println("Código de salida: " + p.waitFor());
```

| | Comando |
|---|---|
| Linux | `echo Hola desde el hijo` |
| Windows | `cmd /c echo Hola desde el hijo`, con **cada palabra como argumento aparte** |

Salida real: `[HIJO] Hola desde el hijo · Código de salida: 0`.

**Reflexión:** aparece con el prefijo porque **el hijo no escribe en la pantalla**: escribe en una **tubería** que lee el padre, y es el padre quien decide cómo mostrarlo (con `[HIJO] ` delante).

> Trampa de Windows: con `"Hola desde el hijo"` en un solo argumento, Java lo pone **entre comillas** al construir la línea de órdenes (tiene espacios) y el `echo` de `cmd` imprime las comillas.

### Ejercicio 2. ¿Quién y dónde?

`ejecutar(String comando)` lanza el comando, lee **la primera línea**, espera y la devuelve. `whoami` y `hostname` existen igual en Linux y en Windows.

Salida real (Windows): `Usuario desktop-ivbhl4h\usuario en el equipo DESKTOP-IVBHL4H` (en Linux sale solo el usuario: `Usuario alumno en el equipo pc-aula12`).

### Ejercicio 3. Saludo según la hora

| | Comando |
|---|---|
| Linux | `date +%H` |
| Windows | `powershell -NoProfile -Command "Get-Date -Format HH"` (el `date` de `cmd` no admite formato) |

Si llega una hora por `args`, se usa esa para probar todas las franjas. Salida real: `Son las 13 horas → Buenos días`; con `15` → `Buenas tardes`; con `23` → `Buenas noches`.

**Reflexión:** en la terminal se escribe `date "+%H"` con comillas para que la **shell** no interprete caracteres especiales. `ProcessBuilder` **no usa shell**: cada argumento llega tal cual al programa, así que las comillas sobran (y si las pusieras, llegarían como parte del texto).

### Ejercicio 4. Los 5 más grandes

| | Comando |
|---|---|
| Linux | `ls -S /usr/bin` |
| Windows | `cmd /c dir /b /o-s /a-d C:\Windows\System32` (`/b` solo nombres · `/o-s` por tamaño descendente · `/a-d` sin carpetas) |

```java
while (leidas < 5 && (linea = br.readLine()) != null) { ... }   // deja de leer a las 5
if (p.isAlive()) p.destroy();                                    // el resto ya no interesa
```

Salida real (Windows):

```text
1. MRT.exe
2. OneDriveSetup.exe
3. edgehtml.dll
4. nvidia-pcc.exe
5. mfxplugin64_hw.dll
(el hijo sigue vivo, lo terminamos)
Código de salida: 1
```

El hijo sigue vivo porque aún tiene salida pendiente que nadie lee; si no se terminara, podría quedarse bloqueado con la tubería llena.

---

## Nivel 2 · Errores y códigos de salida

### Ejercicio 5. Buscador con grep

| | Comando | Códigos |
|---|---|---|
| Linux | `grep -n palabra fichero` | 0 encontrado · 1 no encontrado · **2 error** |
| Windows | `findstr /n palabra fichero` | 0 encontrado · 1 no encontrado **o** error → se mira si hubo algo en stderr |

Las dos sacan `número:línea`, así que el análisis es igual: `indexOf(':')` separa el número del texto.

Salida real (Windows):

```text
Línea 1: El gato come. El perro come.
Línea 2: El gato duerme y el perro ladra.
Línea 3: Un gato, un perro y un pez.
"gato" aparece en 3 líneas
"caballo" no aparece en datos/texto.txt
Error (código 1): FINDSTR: No se puede abrir noexiste.txt      ← Linux: código 2, "grep: noexiste.txt: No such file or directory"
```

> Trampa de Windows: `findstr` toma la `/` de `datos/texto.txt` como el inicio de una opción. Se pasa la ruta con `File.separatorChar`.

### Ejercicio 6. Revisor de comandos

```java
pb.redirectErrorStream(true);              // errores y salida por el mismo flujo
try {
    Process p = pb.start();
    String primera = br.readLine();
    while (br.readLine() != null) { }      // consumir el resto
    int codigo = p.waitFor();
    ...[OK] o [FALLO n]...
} catch (IOException e) {
    ...[NO EXISTE]...
}
```

Salida real (Windows):

```text
[OK]        cmd /c date /t
[FALLO 1]   cmd /c dir carpetaQueNoExiste ->  El volumen de la unidad C no tiene etiqueta.
[NO EXISTE] comandoInventado
[FALLO 1]   cmd /c type ficheroQueNoExiste.txt -> El sistema no puede encontrar el archivo especificado.
[OK]        whoami
Correctos: 2 de 5
```

En Linux: `[FALLO 2] ls carpetaQueNoExiste -> ls: cannot access 'carpetaQueNoExiste': No such file or directory` y `[FALLO 1] cat ...`.

**Reflexión:** `comandoInventado` **nunca llega a ser un proceso**: `start()` no encuentra el ejecutable y lanza `IOException`, así que no hay código de salida. `ls carpetaQueNoExiste` **sí** se ejecuta (el programa `ls` existe) y es él quien falla y devuelve 2.

---

## Nivel 3 · Enviar datos al hijo

### Ejercicio 7. Calculadora con bc

```java
Process p = new ProcessBuilder("bc", "-l").start();
PrintWriter pw = new PrintWriter(p.getOutputStream());
pw.println(expresion);
pw.close();                     // sin esto bc sigue esperando expresiones
String resultado = salida.readLine();
```

| | Comando |
|---|---|
| Linux | `bc -l` (`-l`: decimales y funciones matemáticas): `2^10` → `1024`, `7/2` → `3.50000000000000000000` |
| Windows | No hay `bc`. Se usa PowerShell leyendo de su entrada (`powershell -NoProfile -NonInteractive -Command -`), **con otra sintaxis**: `7/2` → `3,5`, potencia `[math]::Pow(2,10)` → `1024` (en PowerShell `^` no es potencia) |

Salida real (Windows): `7/2 = 3,5 · [math]::Pow(2,10) = 1024 · hola + → No se puede calcular...`.

- `bc` escribe los errores por **stderr** (y empieza con una línea en blanco: la solución la salta).
- `close()` es imprescindible: es lo que le dice a `bc` «ya no hay más expresiones».

### Ejercicio 8. Cifrado César con tr

```java
Process p = new ProcessBuilder("tr", "A-Za-z", "N-ZA-Mn-za-m").start();   // Windows: el tr de Git
pw.println(texto); pw.close();
```

Salida real (Windows, con el `tr` de Git):

```text
Texto: Hola Mundo, España
Cifrado:    Ubyn Zhaqb, Rfcnñn
Descifrado: Hola Mundo, España
¿Coincide con el original?: true
```

**Reflexión:** la **ñ** y las tildes **no cambian**: `tr` solo transforma los rangos `A-Z` y `a-z` del alfabeto inglés, y además en UTF-8 esas letras ocupan **2 bytes** que caen fuera de esos rangos. Aplicar ROT13 dos veces devuelve el original porque 13 + 13 = 26, la vuelta completa del alfabeto.

### Ejercicio 9. Hijo inversor

`HijoInverso` lee líneas con `Scanner` y responde cada una al revés (`new StringBuilder(linea).reverse()`). El padre:

```java
haciaHijo.println(frase);
haciaHijo.flush();                      // enviar YA, sin cerrar
String invertida = desdeHijo.readLine();
```

Salida real:

```text
Hijo: anit al aval atina
  ¡Es un palíndromo!
Hijo: odnum aloh
El hijo terminó con código 0
```

**Reflexión:** en los ejercicios 7 y 8 se enviaba **un solo dato** y había que avisar de que no había más (`close()`). Aquí hay una **conversación**: si se cerrara, el hijo vería «fin de entrada» y terminaría tras la primera frase. `flush()` envía lo que hay en el búfer **sin cerrar el canal**. Sin `flush()`, la frase se quedaría en el búfer del `PrintWriter`, el hijo no la recibiría y los dos se quedarían esperando (**interbloqueo**).

### Ejercicio 10. Hijo calculadora

`HijoCalculadora` parte la línea con `split("\\s+")`, comprueba que hay 3 partes, convierte con `Double.parseDouble` y aplica el operador con un `switch`. Responde `ERROR` si el formato es incorrecto, el operador es desconocido o se divide entre cero. Termina con `fin`.

Salida real:

```text
3 + 4  =  7.0
10 / 4  =  2.5
6 * 7  =  42.0
8 / 0  ->  operación no válida
hola + 2  ->  operación no válida
5 ^ 2  ->  operación no válida
20 - 25  =  -5.0
Operaciones no válidas: 3
```

> El enunciado escribe `"20 -25"`, pero la solución usa `"20 - 25"`. Con `"20 -25"` saldrían **2 partes** (`20` y `-25`) y la respuesta sería `ERROR`.

**Reflexión — ¿qué es un protocolo?** Un conjunto de **reglas** que los dos procesos acuerdan para entenderse: formato de los mensajes, orden, respuestas y cómo se termina. Las de este ejercicio son: (1) una petición por línea con `número operador número` separados por espacios; (2) una respuesta por línea, el resultado o `ERROR`; (3) operadores `+ - * /`; (4) `fin` cierra la conversación.

---

## Nivel 4 · Ficheros y tuberías

### Ejercicio 11. Ordenar notas

```java
ProcessBuilder pb = new ProcessBuilder("sort", "-t,", "-k2", "-rn");   // Windows: el sort de Git
pb.redirectInput(new File("datos/notas.csv"));            // < notas.csv
pb.redirectOutput(new File("datos/notas_ordenadas.csv")); // > notas_ordenadas.csv
pb.start().waitFor();
```

Los datos **no pasan por Java**: el SO conecta el fichero con el `sort`. Después Java lee el fichero generado para el podio. Salida real:

```text
Oro: Sara (9.7)
Plata: Marcos (9.2)
Bronce: Elena (8.9)
```

**Reflexión:**
- Comando equivalente: `sort -t, -k2 -rn < datos/notas.csv > datos/notas_ordenadas.csv`.
- Sin la `n` se ordena como **texto**, carácter a carácter: `"10"` va **detrás** de `"5.1"` porque `'1' < '5'`. Un 10 quedaría el último en vez del primero.
- En Windows no vale el `sort` del sistema (no ordena por campos): se usa `C:/Program Files/Git/usr/bin/sort.exe`.

### Ejercicio 12. Diario de actividad

```java
ProcessBuilder fecha = new ProcessBuilder("date", "+=== %d/%m/%Y %H:%M:%S ===");
fecha.redirectOutput(Redirect.appendTo(diario));          // >> diario.log (añadir)
ProcessBuilder ls = new ProcessBuilder("ls", "-l");
ls.redirectOutput(Redirect.appendTo(diario));
```

Windows: `powershell ... Get-Date -Format '=== dd/MM/yyyy HH:mm:ss ==='` y `cmd /c dir`. Salida real (dos ejecuciones):

```text
Entrada añadida: === 09/11/2026 13:15:04 ===
El diario tiene 1 entradas (573 bytes)
Entrada añadida: === 09/11/2026 13:15:05 ===
El diario tiene 2 entradas (1146 bytes)
```

`Redirect.to(f)` sobrescribe en cada ejecución; `Redirect.appendTo(f)` **añade**, que es lo que pide un diario.

### Ejercicio 13. Palabras de un texto con una tubería

```java
List<ProcessBuilder> tuberia = ...;   // tr -cs 'A-Za-z' '\n' (con redirectInput) | sort -u | wc -l
List<Process> procesos = ProcessBuilder.startPipeline(tuberia);
String distintas = leerTodo(procesos.get(procesos.size() - 1));
```

Salida real:

```text
Palabras distintas: 11

Las 5 palabras más frecuentes:
      4 el
      3 un
      3 perro
      3 gato
      2 y
```

| Proceso | Qué aporta |
|---|---|
| `tr -cs 'A-Za-z' '\n'` | Todo lo que no es letra → salto de línea (`-c` complemento, `-s` sin repetidos): una palabra por línea |
| `sort -u` | Ordena y quita duplicados |
| `wc -l` | Cuenta líneas = palabras distintas |

**Reflexión:** sale **11** y no 9 porque `sort -u` **distingue mayúsculas**: `El`/`el` y `Un`/`un` cuentan como palabras distintas (la ampliación lo arregla con `tr 'A-Z' 'a-z'`). El padre lee **solo del último** proceso porque los demás escriben en la tubería hacia el siguiente, no hacia Java.

---

## Nivel 5 · Retos

### Ejercicio 14. Contadores de primos en paralelo

`HijoPrimos` cuenta los primos de su rango (comprobando divisores hasta la raíz: `(long) i * i <= n`) y escribe **solo el número**. El padre reparte 1-9.000.000 en tres hijos.

Salida real (Windows, i7-8750H):

```text
Secuencial: 602489 primos en 5042 ms
  Hijo 1 [1-3000000]: 216816
  Hijo 2 [3000001-6000000]: 196033
  Hijo 3 [6000001-9000000]: 189640
Paralelo:   602489 primos en 2466 ms
El paralelo es 2,0 veces más rápido
```

**Reflexión:**
- **No es 3 veces más rápido** porque: (1) cada hijo arranca una JVM (tiempo fijo que no se reparte); (2) los rangos **no cuestan lo mismo**: comprobar números grandes cuesta más (más divisores hasta la raíz), así que el último hijo tarda más y el total depende del **más lento**; (3) el SO y otros procesos también usan núcleos.
- **Cada hijo encuentra menos primos que el anterior** porque los primos son cada vez **más escasos** al crecer los números (alrededor de n hay aproximadamente 1 primo de cada ln(n) números).
- `(long) i * i` evita el **desbordamiento** de `int` al elevar al cuadrado números grandes.

### Ejercicio 15. Adivina el número

`HijoAdivina` elige el secreto (o lo recibe por `args` para pruebas) y responde `mayor`, `menor` o `acertado`. El padre usa **búsqueda binaria**: prueba siempre el punto medio del intervalo y lo reduce a la mitad.

Salida real (secreto 73):

```text
Intento 5: 71 -> mayor
Intento 6: 73 -> acertado
¡Adivinado en 6 intentos!
```

Con 100 números, la búsqueda binaria acierta **siempre en 7 intentos como máximo** (2⁷ = 128 ≥ 100).

---

## Linux frente a Windows en estos ejercicios

| Ej. | Linux (examen) | Windows (casa) |
|---|---|---|
| 1 | `echo Hola desde el hijo` | `cmd /c echo Hola desde el hijo` (palabras sueltas) |
| 2 | `whoami`, `hostname` | Igual |
| 3 | `date +%H` | `powershell ... Get-Date -Format HH` |
| 4 | `ls -S /usr/bin` | `cmd /c dir /b /o-s /a-d C:\Windows\System32` |
| 5 | `grep -n` (0/1/2) | `findstr /n` (0/1 + stderr) |
| 6 | `date`, `ls`, `cat`, `whoami` | `cmd /c date /t`, `cmd /c dir`, `cmd /c type`, `whoami` |
| 7 | `bc -l` | PowerShell (otra sintaxis) |
| 8, 11, 13 | `tr`, `sort`, `wc`, `uniq`, `head` | Los mismos de Git: `C:/Program Files/Git/usr/bin/` |
| 9, 10, 14, 15 | Java lanza Java | Igual |
| 12 | `date "+..."`, `ls -l` | PowerShell `Get-Date`, `cmd /c dir` |
