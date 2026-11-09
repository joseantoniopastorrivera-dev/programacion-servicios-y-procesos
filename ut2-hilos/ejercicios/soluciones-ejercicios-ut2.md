# UT2 · Soluciones de la relación de ejercicios (bloques 1 a 4)

> Enunciado: [`ejercicios-ut2.docx`](ejercicios-ut2.docx) · Proyecto de la profesora, sin tocar: [`bloque1-4/`](bloque1-4/) · **Soluciones:** [`../soluciones/bloque1-4/`](../soluciones/bloque1-4/)
>
> Cada clase resuelta lleva **la explicación línea a línea en sus comentarios**. Úsala como referencia cuando hagas tú los ejercicios en el proyecto vacío. Aquí están las **respuestas a las preguntas** de cada ejercicio, con **salidas reales** (Windows 11, 12 núcleos, JDK 25 compilando para Java 21, 09/11/2026).

## Índice

- [0. Linux y Windows: compilar y ejecutar](#0-linux-y-windows-compilar-y-ejecutar)
- [Bloque 1 · Hilos y procesos](#bloque-1--hilos-y-procesos): [1.1 Descargas](#11--descargas) · [1.2 Tarea pesada](#12--tarea-pesada-sin-bloquear)
- [Bloque 2 · Crear y gestionar hilos](#bloque-2--crear-y-gestionar-hilos): [2.1 Estados](#21--estados-de-un-hilo) · [2.2 Examen](#22--el-examen) · [2.3 Autoguardado](#23--autoguardado) · [2.4 Taxis](#24--taxis)
- [Bloque 3 · Compartir información](#bloque-3--compartir-información): [3.1 Informe](#31--el-informe-de-ventas) · [3.2 Jardín](#32--el-jardín-ornamental) · [3.3 Vigilante](#33--el-vigilante) · [3.4 Entradas](#34--venta-de-entradas)
- [Bloque 4 · Sincronización](#bloque-4--sincronización): [4.1 Jardín](#41--arregla-el-jardín) · [4.2 Taquillas](#42--taquillas-sin-overbooking) · [4.3 Turnos](#43--la-máquina-de-turnos) · [4.4 Banco](#44--el-banco)
- [Resumen de herramientas](#resumen-de-herramientas)

## 0. Linux y Windows: compilar y ejecutar

**El código es el mismo en los dos sistemas.** En la UT1 había dos versiones porque lanzábamos comandos del sistema operativo (`ls`/`dir`, `sleep`/`timeout`…). En la UT2 todo son **hilos de Java**: `Thread`, `synchronized`, `AtomicInteger`… los gestiona la JVM, que es la misma en Linux y en Windows. Lo único que cambia es **cómo se compila** desde la terminal, y algunos **números** (tiempos, visitantes perdidos), que dependen del procesador y del sistema.

**Con IntelliJ** (igual en los dos): *File → Open…* → carpeta `soluciones/bloque1-4` → botón ▶ junto al `main`.

**Desde la terminal**, dentro de `soluciones/bloque1-4/`:

| | Linux (examen) | Windows (casa, PowerShell) |
|---|---|---|
| Compilar todo | `javac -d out $(find src -name '*.java')` | `javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName` |
| Ejecutar uno | `java -cp out bloque1.descargas.DescargasConHilos` | `java -cp out bloque1.descargas.DescargasConHilos` |
| Ver los hilos de un programa en marcha | `jps` y después `jstack <pid>` (o `top -H -p <pid>`) | `jps` y después `jstack <pid>` (o el Administrador de tareas → Detalles → columna *Subprocesos*) |

- En Windows hace falta `-encoding UTF-8` porque los ficheros llevan tildes y `javac` usaría la codificación de Windows (en Linux ya es UTF-8). Si en la consola de Windows las tildes salen mal, ejecuta con `java -Dstdout.encoding=UTF-8 -cp out …`.
- El nombre de la clase va **con su paquete** (`bloque1.descargas.DescargasConHilos`) y **sin** `.java` ni `.class`.
- Las salidas de abajo usan coma decimal (`4,00 s`) porque el sistema está en español. En una máquina en inglés saldría `4.00 s`.

> ⏳ **Pendiente de comprobar en Linux** (máquina virtual *PSP Ubuntu*). Lo esperable: los ejercicios correctos dan lo mismo, y los del bloque 3 fallan igual, aunque con otros números.

---

## Bloque 1 · Hilos y procesos

### 1.1 · Descargas

**Código:** [`bloque1/descargas`](../soluciones/bloque1-4/src/bloque1/descargas/). `Descarga` implementa `Runnable`: la misma clase sirve para la versión secuencial (el main llama a `descargar()`) y para la de hilos (`new Thread(descarga).start()`).

```text
Descarga secuencial (un único hilo: main)          Descarga con hilos (un hilo por fichero)
video.mp4    (40 MB) terminada a los  4,00 s [main]  musica.mp3   (10 MB) terminada a los  1,00 s [Thread-3]
fotos.zip    (15 MB) terminada a los  5,53 s [main]  fotos.zip    (15 MB) terminada a los  1,51 s [Thread-1]
apuntes.pdf  (25 MB) terminada a los  8,03 s [main]  apuntes.pdf  (25 MB) terminada a los  2,51 s [Thread-2]
musica.mp3   (10 MB) terminada a los  9,03 s [main]  juego.iso    (30 MB) terminada a los  3,00 s [Thread-4]
juego.iso    (30 MB) terminada a los 12,03 s [main]  video.mp4    (40 MB) terminada a los  4,01 s [Thread-0]
Todas terminadas a los 12,03 s                     Todas terminadas a los 4,01 s
```

1. **¿Cuánto tarda cada versión?** La secuencial tarda **12 s**, la **suma** de todas (4 + 1,5 + 2,5 + 1 + 3). La de hilos tarda **4 s**, lo que tarda **la más larga** (video.mp4). Con hilos las cinco esperas transcurren a la vez: mientras un hilo «descarga» (duerme), los demás también avanzan.
2. **¿En qué orden terminan?** En la secuencial, en el **orden del programa**, porque cada una empieza cuando acaba la anterior. Con hilos, **por tamaño**, de menor a mayor: musica, fotos, apuntes, juego, video. Todas empiezan a la vez y acaba antes la que menos tiene que bajar. Fíjate en que el nombre del hilo (`Thread-3`) no sigue el orden de llegada.
3. **¿Y si los 10 MB/s fueran en total?** Entonces **no** compensaría: la conexión se reparte y bajar 120 MB a 10 MB/s cuesta **12 s** se haga como se haga. Los hilos permiten **aprovechar los tiempos de espera** y **repartir el trabajo** entre los núcleos, pero **no crean recursos**: no aumentan el ancho de banda ni la velocidad del disco, y tampoco aceleran nada si el cuello de botella es un recurso compartido. Incluso puede empeorar un poco, por el coste de cambiar de un hilo a otro.

### 1.2 · Tarea pesada sin bloquear

**Código:** [`bloque1/tarea_pesada`](../soluciones/bloque1-4/src/bloque1/tarea_pesada/).

```text
SinHilos                                        ConHilos
Sigo atendiendo al usuario...                   Sigo atendiendo al usuario...
                                                Sigo atendiendo al usuario...
(~2 s sin que salga nada)                       Sigo atendiendo al usuario...
Sigo atendiendo al usuario...                   Sigo atendiendo al usuario...
Hay 539.777 primos hasta 8.000.000 (1,95 s)     Hay 539.777 primos hasta 8.000.000 (2,20 s)
```

1. **¿Qué ocurre en cada versión?** Sin hilos, el main está ocupado **dentro de `contar()`** y no puede ejecutar ningún `println`: el programa parece **colgado** hasta que acaba el cálculo. Con hilos, el cálculo va a otro hilo y el main **sigue respondiendo** cada medio segundo.
2. **¿Qué ventaja ilustra?** La **capacidad de respuesta** (*responsiveness*): un programa puede hacer un trabajo largo **sin dejar de atender** al usuario. Es lo que hace una interfaz gráfica para no congelarse: el trabajo pesado nunca va en el hilo de la interfaz.
3. **¿Tarda menos el cálculo con hilos?** **No.** Tarda lo mismo o un poco más (aquí 2,20 s frente a 1,95 s). El cálculo sigue en **un solo hilo**, y la diferencia es solo que ya no bloquea al main. Lo poco que añade se debe a crear el hilo y al trabajo del main imprimiendo. Para ir más rápido habría que **repartir** el rango de números entre varios hilos, y eso sí aprovecharía varios núcleos.

> En `ConHilos` se espera con `join(500)` y no con `sleep(500)`: los dos esperan medio segundo, pero `join(500)` **vuelve en cuanto el cálculo termina**, así que el resultado no sale con hasta 0,5 s de retraso.

---

## Bloque 2 · Crear y gestionar hilos

### 2.1 · Estados de un hilo

**Código:** [`bloque2/estados`](../soluciones/bloque1-4/src/bloque2/estados/). `HiloObservado` calcula (RUNNABLE), duerme (TIMED_WAITING), espera un `notify()` (WAITING) y vuelve a calcular. El main consulta `getState()` en bucle, guarda solo los cambios y le despierta cuando lo ve en WAITING.

```text
NEW → RUNNABLE → TIMED_WAITING → RUNNABLE → WAITING → BLOCKED → RUNNABLE → TERMINATED   (3 de 3 ejecuciones)
```

1. **¿Qué hace para pasar a TIMED_WAITING? ¿Y a WAITING? Diferencia.**
   - **TIMED_WAITING:** `Thread.sleep(300)`. También valdrían `join(ms)` o `wait(ms)`. Es una espera **con tiempo límite**: el hilo se despierta solo cuando pasa ese tiempo.
   - **WAITING:** `cerrojo.wait()`, sin tiempo. También valdría `join()` sin argumentos. Es una espera **indefinida**: el hilo solo sale si **otro hilo** le avisa (`notify()`/`notifyAll()`) o le interrumpe. Si nadie le avisa, se queda esperando para siempre.
2. **¿Aparece algún estado inesperado?** Sí, dos:
   - **BLOCKED**, entre WAITING y el último RUNNABLE. `wait()` **suelta** el cerrojo mientras espera. Al recibir el `notify()`, el hilo tiene que **volver a cogerlo** para salir del `synchronized`, pero en ese momento lo tiene el main, que está dentro de su propio `synchronized (cerrojo)` haciendo el `notify()`. Durante ese instante el hilo está **bloqueado esperando un cerrojo**: eso es BLOCKED.
   - **RUNNABLE entre TIMED_WAITING y WAITING.** Al despertar del `sleep`, el hilo tiene que ejecutar `synchronized` y `wait()`, y mientras lo hace está RUNNABLE.

   Además, **RUNNABLE no significa «usando la CPU»**. Significa «listo para ejecutarse»: puede estar en un núcleo o esperando turno, y Java no distingue entre los dos casos.

### 2.2 · El examen

**Código:** [`bloque2/examen`](../soluciones/bloque1-4/src/bloque2/examen/).

```text
Examen:               Ana ha contestado 12 de 20 preguntas. NO terminó a tiempo: se le retiró el examen.
ExamenVariosAlumnos:  Ana 13/20 · Luis 13/20 · Marta 12/20 · Pablo 13/20 · Sara 12/20 — examen retirado (los cinco)
```

De media, una pregunta cuesta 400 ms, así que el examen entero son 20 × 400 = 8 s, más que los 5 s disponibles. Lo normal es contestar unas 12 o 13 preguntas.

1. **¿Cómo sabe el profesor si el alumno ha terminado? ¿Cómo se entera el alumno?**
   - El profesor hace `hilo.join(5000)`, que espera **como mucho** 5 s, y después pregunta `hilo.isAlive()`. Si sigue vivo, no ha terminado.
   - Para retirarle el examen hace `hilo.interrupt()`. El alumno pasa casi todo el tiempo dentro de `Thread.sleep()`, y un hilo interrumpido mientras duerme **se despierta al momento** con una `InterruptedException`. En el `catch` deja de escribir, y la pregunta que estaba haciendo no se cuenta.
   - Si el alumno estuviera **calculando** en vez de durmiendo, la interrupción no le despertaría. Tendría que comprobarla él mismo con `Thread.currentThread().isInterrupted()` en su bucle.
2. **¿Por qué no basta con esperar 5 s a cada alumno?** Porque `join(5000)` uno detrás de otro **suma** las esperas. Si el primero no termina, se le esperan 5 s, después otros 5 s al segundo… y el último tendría hasta **25 s**. La solución es una **hora límite común** (`limite = ahora + 5000`) y dar a cada `join` solo el tiempo que **queda**.
   - **Error típico:** si `queda` llega a 0, **no** se debe llamar a `join(0)`, porque `join(0)` significa **esperar sin límite**.

### 2.3 · Autoguardado

**Código:** [`bloque2/autoguardado`](../soluciones/bloque1-4/src/bloque2/autoguardado/).

```text
El usuario escribe: Había
…
   [autoguardado 3] 51 caracteres guardados
…
El usuario cierra el editor (14 palabras). Fin del programa.        ← el programa termina aquí
```

1. **¿Qué ocurre si no haces nada especial con el hilo del autoguardado?** El programa **no termina nunca**. El usuario cierra el editor (acaba el main), pero la JVM **solo termina cuando acaban todos los hilos no daemon**, y el autoguardado tiene un `while (true)`. Se queda guardando cada segundo para siempre, y hay que pararlo a mano (Ctrl+C o el botón ■ de IntelliJ).
2. **¿Cómo has conseguido que termine? ¿Otra forma?**
   - Con `autoguardado.setDaemon(true)` **antes** de `start()`; si se pone después, lanza `IllegalThreadStateException`. Un hilo **daemon** es «de servicio»: la JVM **no lo espera** y lo corta en cuanto terminan los hilos normales.
   - **Otra forma:** `autoguardado.interrupt()` al cerrar el editor. El `sleep(1000)` lanza `InterruptedException` y el `catch` hace `return`. Es más limpia, porque el hilo **termina por sí mismo** y podría hacer un último guardado. El daemon, en cambio, **se corta de golpe**, aunque esté a mitad de guardar.
   - Otras opciones peores: un `volatile boolean abierto` que el bucle comprueba (pero el enunciado dice que el bucle no tiene condición de fin) o `System.exit(0)` (mata todos los hilos sin dejarles terminar).

> El documento (`StringBuilder`) lo usan los dos hilos a la vez. Por eso escribir y copiar se hace dentro de `synchronized (documento)`. Lo verás en los bloques 3 y 4. Sin eso, el autoguardado podría copiar el texto a medio modificar.

### 2.4 · Taxis

**Código:** [`bloque2/taxis`](../soluciones/bloque1-4/src/bloque2/taxis/).

```text
Taxi 1234-ABC:  46 km
Taxi 5678-DEF:  30 km
Taxi 9012-GHI:  40 km
Total de la flota: 116 km
```

1. **¿Cómo trabaja cada taxi en su hilo? ¿Por qué no de la otra forma?** `Taxi extends Vehiculo implements Runnable`, y en `Flota` cada taxi se pasa a un `new Thread(taxi)`. **No puede** extender `Thread` porque ya extiende `Vehiculo`, y Java **no permite herencia múltiple de clases**. `Vehiculo` no se puede modificar, así que tampoco se le puede hacer extender `Thread`. Una clase sí puede extender una clase **e** implementar interfaces a la vez. Por eso `Runnable` es la forma más flexible.
2. **¿Cómo calculas el total? ¿Problema con un contador compartido?**
   - Cada taxi suma **solo en su propio** `kmRecorridos` mientras trabaja: no comparten nada. El main espera a todos con `join()` y **después** suma los tres.
   - Con un **único contador compartido**, `total += km` **no es atómico**: son tres pasos, leer, sumar y escribir. Dos taxis podrían leer el mismo valor y uno de los dos km se **perdería**. Es una **condición de carrera**, justo la que aparece en el bloque 3 (el jardín). Aquí sería rara, porque hay pocas sumas muy separadas en el tiempo, pero **posible**, y que sea rara no la hace correcta.

---

## Bloque 3 · Compartir información

> Norma del enunciado: en este bloque se **encuentra y explica** el problema, sin corregirlo, salvo que el enunciado lo pida (3.1 y 3.3 lo piden). Los fallos se corrigen en el bloque 4.

### 3.1 · El informe de ventas

**Código:** [`bloque3/informe_ventas`](../soluciones/bloque1-4/src/bloque3/informe_ventas/).

```text
== 1) Carga y cálculo A LA VEZ (como dice el enunciado)
Total facturado: 0 €   Importe medio: 0,00 €
Total real:      50.454.228 €   → informe INCORRECTO

== 2) Corregido: el calculador hace join() sobre el cargador
Total facturado: 50.492.808 €   Importe medio: 50,49 €
Total real:      50.492.808 €   → informe CORRECTO
```

1. **¿Es correcto?** **No.** El calculador empieza a sumar **mientras** el cargador todavía está rellenando el array: lee huecos que aún valen **0**. Aquí el calculador incluso terminó antes de que se cargara nada útil (total 0 €). En otras ejecuciones puede salir una parte del total. Nunca sale el real, y el resultado cambia de una ejecución a otra: es **no determinista**.
2. **Condiciones de Bernstein.** Dos tareas pueden ejecutarse a la vez solo si se cumplen las tres:
   - L(Calc) ∩ E(Carg) = ∅
   - E(Calc) ∩ L(Carg) = ∅
   - E(Calc) ∩ E(Carg) = ∅

   Aquí **falla la primera**: el calculador **lee** `importes`, y el cargador **escribe** en `importes`. Hay **dependencia de datos** («leer después de escribir»), así que no pueden ejecutarse concurrentemente: el calculador tiene que ir **después**.
3. **Corrección.** Con lo visto hasta ahora: `join()`. El calculador recibe el hilo del cargador y hace `cargador.join()` antes de sumar. Además, `join()` garantiza que ve **todo** lo que escribió el cargador (relación *happens-before*), así que el array no necesita `volatile` ni `synchronized`. Otra opción equivalente es que el main haga `hiloCargador.join()` antes de lanzar el calculador. En los dos casos se renuncia a la concurrencia entre las dos tareas, porque **no puede haberla**.

### 3.2 · El jardín ornamental

**Código:** [`bloque3/jardin`](../soluciones/bloque1-4/src/bloque3/jardin/).

```text
== 1) Contador compartido (objeto), 1.000.000 por puerta
   contador = 1.258.956  (esperado 2.000.000)  ¡se han perdido 741.044!
   contador = 1.277.242  (esperado 2.000.000)  ¡se han perdido 722.758!
   contador = 1.986.246  (esperado 2.000.000)  ¡se han perdido 13.754!
   contador = 2.000.000  (esperado 2.000.000)  OK
   contador = 2.000.000  (esperado 2.000.000)  OK
== 2) Contador compartido (atributo static), 1.000.000 por puerta
   contador = 1.197.796  …  ¡se han perdido 802.204!
   contador = 1.042.778  …  ¡se han perdido 957.222!
   contador = 2.000.000  …  OK   (×3)
== 3) Solo 1.000 visitantes por puerta
   contador =     2.000  (esperado     2.000)  OK   (×5)
```

1. **¿Qué resultados? ¿Siempre los mismos?** **No.** Casi siempre faltan visitantes, y **cada vez un número distinto**: aquí entre 13.754 y 957.222 perdidos. A veces sale bien. Que salga bien **no** demuestra nada: solo que esa vez los hilos no coincidieron.
   - ¿Por qué en las últimas vueltas sale bien más a menudo? Cuando el bucle se repite mucho, el compilador **JIT** lo optimiza (por ejemplo, sumando en un registro y escribiendo al final), y cada puerta termina en muy poco tiempo, así que casi no coinciden. El código sigue **igual de mal**.
2. **Cómo se pierde un visitante, paso a paso.** `cuenta++` son tres pasos: **leer**, **sumar 1** y **escribir**. Con `cuenta = 41`:

   | Paso | Puerta Norte | Puerta Sur | `cuenta` |
   |---|---|---|---|
   | 1 | lee 41 | | 41 |
   | 2 | | lee 41 | 41 |
   | 3 | calcula 42 | | 41 |
   | 4 | | calcula 42 | 41 |
   | 5 | escribe 42 | | 42 |
   | 6 | | escribe 42 | **42** ← han entrado 2 y solo ha subido 1 |

   Es una **condición de carrera**, y `cuenta++` es la **sección crítica** sin proteger. El planificador puede quitar la CPU a un hilo **entre** cualquiera de esos pasos, o los dos hilos pueden ejecutarse **a la vez** en dos núcleos.
3. **Compartir de otra forma.** Con un atributo **`static`** (`PuertaContadorEstatico.visitantes`): pertenece a la clase y es **uno solo** para todas las puertas. **El resultado no cambia**: siguen perdiéndose visitantes, porque el problema no es **cómo** se comparte el dato sino que `++` **no es atómico**.
4. **Con muchos menos visitantes (1.000):** sale **siempre bien**. Cada puerta tarda microsegundos, así que la primera **termina antes de que la segunda empiece** (crear y arrancar un hilo cuesta más que contar hasta 1.000), y nunca coinciden. **No** significa que sea correcto: el fallo sigue ahí, solo que es muy improbable. Por eso los fallos de concurrencia son tan peligrosos: pasan las pruebas pequeñas y aparecen en producción.

### 3.3 · El vigilante

**Código:** [`bloque3/vigilante`](../soluciones/bloque1-4/src/bloque3/vigilante/).

```text
== 1) Aviso SIN volatile
Jefe: ¡fin de turno!
Jefe: pasados 3 s el vigilante NO se ha enterado del aviso.

== 2) Aviso CON volatile
Jefe: ¡fin de turno!
vigilante: me he enterado del aviso. He hecho 1.495.169.714 rondas.

== 3) Cuatro vigilantes con un total común volatile
Suma de las rondas de cada uno: 87.532.961
Total común (volatile):         29.024.243  → NO cuadra: volatile no hace atómico el ++
```

1. **¿Se entera siempre?** **No**, casi nunca. El aviso es un `boolean` normal. La JVM **no garantiza** que un hilo vea lo que escribe otro (**visibilidad**) si no hay sincronización. En la práctica, el compilador **JIT** ve que dentro del bucle `while (!turnoTerminado) rondas++;` nadie cambia `turnoTerminado`, lo lee **una vez** y transforma el bucle en `while (true)`. El valor también puede quedarse en la caché de un núcleo. El vigilante nunca ve el `true` que escribió el jefe. Por eso es **daemon**: así el programa puede terminar «igualmente», como pide el enunciado.
2. **Corrección:** `private volatile boolean turnoTerminado;` (`VigilanteVolatile`). `volatile` obliga a que cada lectura vea **la última escritura** de cualquier hilo, y prohíbe al JIT guardarse el valor. Con eso se entera **siempre**, al instante.
3. **¿Bastaría con `volatile` para un total común? No.** Con 4 vigilantes sumando en un `static volatile long totalRondas`, el total sale **mucho menor** que la suma de lo que contó cada uno: aquí 29 millones frente a 87 millones.
   - `volatile` solo da **visibilidad**, no **atomicidad**: `totalRondas++` sigue siendo leer, sumar y escribir, y se pisan igual que en el jardín.
   - Para eso hace falta `synchronized`, un `Lock` o un `AtomicLong` (bloque 4).
   - **Resumen:** `volatile` vale para una **bandera** que escribe uno y leen otros. No vale para un **contador** que modifican varios.

### 3.4 · Venta de entradas

**Código:** [`bloque3/entradas`](../soluciones/bloque1-4/src/bloque3/entradas/).

```text
== Sesión 1  Taquillas 40+40+40+40 → Total vendido: 160 de 100   Quedan: 16   ¡OVERBOOKING!
== Sesión 2  Taquillas 35+35+35+35 → Total vendido: 140 de 100   Quedan: -3   ¡OVERBOOKING!
== Sesión 3  Taquillas 33+33+33+34 → Total vendido: 133 de 100   Quedan: -3   ¡OVERBOOKING!
```

1. **¿Se vende el número correcto?** **No.** Se venden **más de 100**. Además, `disponibles` termina en valores imposibles: negativo (−3) o **incoherente** (16 entradas quedan habiendo vendido 160).
2. **¿Qué fragmento lo provoca?** El de `Sala.vender()`:
   ```java
   if (disponibles > 0) {   // 1) comprobar
       cobrar();            // 2) tarda 1 ms
       disponibles--;       // 3) actuar
   ```
   - **Primer fallo, «comprobar y después actuar»** (*check-then-act*), que no es atómico. Las 4 taquillas comprueban, ven que queda alguna, se ponen a cobrar y **todas** descuentan: 4 entradas vendidas cuando quedaba 1. Así sale el −3.
   - **Segundo fallo:** `disponibles--` tampoco es atómico. Se pierden restas como en el jardín, y por eso pueden quedar 16 entradas con 160 vendidas.
3. **¿Si cobrar fuera instantáneo, desaparecería?** **No.** Se haría **mucho menos frecuente**, porque la ventana entre comprobar y descontar sería de nanosegundos en vez de 1 ms, pero seguiría existiendo: la comprobación y el descuento siguen siendo pasos separados. Un fallo raro sigue siendo un fallo, y uno más difícil de detectar.

---

## Bloque 4 · Sincronización

### 4.1 · Arregla el jardín

**Código:** [`bloque4/jardin`](../soluciones/bloque1-4/src/bloque4/jardin/). Hay una interfaz `Contador` y tres implementaciones: `ContadorSynchronized`, `ContadorLock` (ReentrantLock) y `ContadorAtomico` (AtomicInteger). La `Puerta` y el main son los mismos para las tres.

```text
synchronized   → siempre correcto (4.000.000 ×3), mejor tiempo  88 ms   (163, 139, 88)
ReentrantLock  → siempre correcto (4.000.000 ×3), mejor tiempo  97 ms   (99, 97, 101)
AtomicInteger  → siempre correcto (4.000.000 ×3), mejor tiempo  76 ms   (76, 96, 97)
```

1. **¿Son correctas las tres?** **Sí**, las tres dan siempre 4.000.000. Las tres hacen que `cuenta++` sea **indivisible**: las dos primeras con **exclusión mutua** (un hilo a la vez en la sección crítica) y la tercera con una **operación atómica** del procesador (CAS).
2. **¿Cuál es más rápida?**
   - En general, **`AtomicInteger`**: no hay cerrojo, ningún hilo se bloquea ni se duerme, y se usa una sola instrucción del procesador (*compare-and-swap*).
   - `synchronized` y `ReentrantLock` hacen que los hilos que no consiguen el cerrojo **esperen**, y eso cuesta más.
   - Aquí las diferencias son pequeñas (76–97 ms) y **varían entre ejecuciones**: la JVM optimiza mucho `synchronized`, el JIT tarda una vuelta en calentar, y con 4 hilos peleando por el mismo dato la caché del procesador es el cuello de botella para las tres.
   - Por eso se repite y se compara el **mejor** tiempo. Para un **contador simple**, la herramienta adecuada es el atómico.
3. **¿Necesita protección la lectura del total cuando han terminado todas?** **No.** El main lee después de `join()` a todas las puertas, y `join()` garantiza que el hilo que espera **ve todo lo que escribió** el hilo terminado (*happens-before*). Ya no hay nadie escribiendo, así que no hay concurrencia. En el código, `total()` está protegido por coherencia, pero no haría falta. Sí haría falta si se leyera **mientras** las puertas siguen abiertas, por ejemplo para mostrar un marcador en directo.

### 4.2 · Taquillas sin overbooking

**Código:** [`bloque4/entradas`](../soluciones/bloque1-4/src/bloque4/entradas/). Clase base `Sala` y cuatro versiones.

```text
Sin proteger (3.4)          vendidas/quedan: 141/-2  148/-2  114/-3  117/-2  114/-1  → OVERBOOKING,  44 ms por sesión
Solo el descuento (P1)      vendidas/quedan: 102/-2  100/0   103/-3  100/0   100/0   → OVERBOOKING,  35 ms por sesión
vender() synchronized       vendidas/quedan: 100/0 ×5                                  → correcto,    131 ms por sesión
Reserva + cobro fuera (P3)  vendidas/quedan: 100/0 ×5                                  → correcto,     34 ms por sesión
```

**Solución principal:** `SalaSincronizada`, con `public synchronized boolean vender()`. Comprobar, cobrar y descontar se hacen **enteros** con el cerrojo de la sala.

1. **¿Funciona proteger solo el descuento?** **No.** Hay sesiones con 102 y 103 vendidas y −3 en la sala. Proteger `disponibles--` arregla las **restas perdidas** (ya no salen cosas como «quedan 16»), pero la **comprobación** `if (disponibles > 0)` sigue fuera. Las cuatro taquillas pueden ver «queda 1», y las cuatro descuentan, eso sí ordenadamente, hasta −3. **La sección crítica es comprobar + descontar juntos**, no solo el descuento.
2. **¿A qué se debe la diferencia de tiempo?** La versión sin corregir tarda unos **44 ms**, y la sincronizada unos **131 ms**, unas 3 veces más. Con `synchronized`, mientras una taquilla **cobra** (1 ms) tiene el cerrojo y las otras tres **esperan**: las 100 ventas se hacen **de una en una**, unos 100 ms en total. Sin protección, las 4 cobraban a la vez (incorrectamente). Es el precio de la exclusión mutua.
3. **Ampliación: reducir lo que se hace dentro.** `SalaConReserva` comprueba y **reserva** (descuenta) dentro de un `synchronized` mínimo, que es instantáneo, y **cobra fuera**. Nunca vende de más, porque la entrada se descuenta en el mismo bloque en que se comprueba, y las cuatro taquillas cobran a la vez: **34 ms**, tan rápido como sin proteger. Si el cobro pudiera fallar, se devolvería la entrada con otro `synchronized { disponibles++; }`.

> **Regla de oro:** la sección crítica debe ser **lo más pequeña posible** pero **contener entera** la operación que tiene que ser indivisible. Nunca hagas esperas (cobros, E/S, `sleep`) con el cerrojo cogido si puedes evitarlo.

### 4.3 · La máquina de turnos

**Código:** [`bloque4/turnos`](../soluciones/bloque1-4/src/bloque4/turnos/). `MaquinaTurnos` tiene tres modos. Para **demostrar** que no hay repetidos, `Carniceria` marca cada número en un `boolean[]`: si ya estaba marcado, es un repetido.

```text
SIN_PROTECCION  400.000 números, mayor 115.866, repetidos 284.134  → ¡HAY REPETIDOS!
SIN_PROTECCION  400.000 números, mayor 255.656, repetidos 144.344  → ¡HAY REPETIDOS!
SIN_PROTECCION  400.000 números, mayor 400.000, repetidos      0  → ningún número repetido   ← suerte, no corrección
SYNCHRONIZED    400.000 números, mayor 400.000, repetidos      0  → ningún número repetido  (×3)
CAS             400.000 números, mayor 400.000, repetidos      0  → ningún número repetido  (×3)
```

1. **¿Cuántos repetidos sin protección?** Muchísimos y **cada vez distintos**: aquí 284.134 y 144.344, y en otra ejecución ninguno. Cada repetido es un `siguiente++` en el que dos clientes leyeron el mismo valor. Por eso el **mayor** número sale muy por debajo de 400.000. La comprobación se hace con 400.000 números y un array: no basta con mirar el último número.
2. **¿Necesitan protección los números que guarda cada cliente? No.** Cada `Cliente` tiene **su propio** `int[] numeros`: solo él escribe en él, así que **no hay dato compartido**. El main lo lee cuando el cliente ya ha terminado (tras `join()`), con visibilidad garantizada. Solo se protege lo **compartido**, y aquí es solo el contador de la máquina.
3. **Ampliación sin `synchronized` ni métodos que incrementen.** Se hace con `AtomicInteger.compareAndSet(actual, actual + 1)` en un bucle:
   - se lee el valor actual;
   - se intenta cambiarlo a `actual + 1` **solo si sigue valiendo `actual`**, en una sola operación atómica;
   - si devuelve `true`, ese número es **mío**; si devuelve `false`, otro cliente se coló, y se vuelve a intentar.

   Así trabajan por dentro `incrementAndGet()` y compañía. Se llama **sin cerrojos** (*lock-free*) porque nadie espera bloqueado: como mucho, se repite el intento.

### 4.4 · El banco

**Código:** [`bloque4/banco`](../soluciones/bloque1-4/src/bloque4/banco/). `Banco` tiene tres modos. El constructor recibe además el `Modo`, un cambio respecto al esqueleto para poder comparar.

```text
SIN_PROTECCION  inicial 10.000 €  final 75.565 €  → ¡NO CUADRA! (65565 €)   (49 ms)
SIN_PROTECCION  inicial 10.000 €  final 75.269 €  → ¡NO CUADRA! (65269 €)   (38 ms)
SIN_PROTECCION  inicial 10.000 €  final 80.889 €  → ¡NO CUADRA! (70889 €)   (13 ms)
UN_CERROJO      inicial 10.000 €  final 10.000 €  → cuadra   (58, 55, 49 ms)
POR_CUENTA      inicial 10.000 €  final 10.000 €  → cuadra   (65, 64, 66 ms)
```

1. **¿Por qué cambia el dinero total si solo se mueve?**
   - `de.saldo -= cantidad` y `a.saldo += cantidad` son, cada una, leer, operar y escribir. Si dos cajeros tocan **la misma cuenta** a la vez, una de las dos escrituras **pisa** a la otra.
   - Si se pierde una **resta**, **aparece** dinero: aquí sale más del que había, hasta +70.889 €. Si se pierde una **suma**, **desaparece**.
   - Además, el `if (de.saldo >= cantidad)` es «comprobar y después actuar»: dos cajeros pueden comprobar el mismo saldo y dejar la cuenta en negativo.
   - El dinero solo se conserva si cada transferencia es **atómica**: o se hace entera, o no se hace.
2. **Ampliación: ¿qué riesgo aparece y cómo se evita?**
   - Con un cerrojo **por cuenta**, cada transferencia tiene que coger **dos** cerrojos (origen y destino), y aparece el riesgo de **interbloqueo** (*deadlock*): el cajero A transfiere de la 3 a la 7 (coge la 3 y espera la 7), mientras el cajero B transfiere de la 7 a la 3 (coge la 7 y espera la 3). Se esperan **para siempre** y el programa se cuelga.
   - **Solución:** coger los cerrojos **siempre en el mismo orden**, primero la cuenta de menor `id`. Así nunca se forma un ciclo de esperas: se rompe la condición de **espera circular**, una de las cuatro condiciones de Coffman.
   - Otra opción: `ReentrantLock.tryLock()` con tiempo límite, soltando y reintentando.
   - **Comparación de tiempos.** Aquí **no sale más rápida** (unos 65 ms frente a unos 55 ms). Hay solo 10 cuentas, así que dos transferencias al azar comparten cuenta muy a menudo, y la sección crítica es tan corta (dos sumas) que coger dos cerrojos cuesta más que lo que se gana trabajando en paralelo.
   - El cerrojo por cuenta compensa con **muchas cuentas** (pocas colisiones) o cuando cada transferencia **tarda** (accesos a disco o a red). Sería fácil comprobarlo poniendo `CUENTAS = 1000` o un `sleep` dentro de `mover()`.

---

## Resumen de herramientas

| Necesito… | Herramienta | Ejercicio |
|---|---|---|
| Crear un hilo | `extends Thread` o `implements Runnable` + `new Thread(r)`. Usa `Runnable` si la clase ya hereda de otra | 1.1, 2.4 |
| Arrancarlo | `start()` (nunca `run()`: eso no crea hilo) | todos |
| Esperar a que termine | `join()`; con límite, `join(ms)` + `isAlive()` | 1.2, 2.2 |
| Pedirle que pare | `interrupt()` → `InterruptedException` en `sleep`/`wait`/`join`, o `isInterrupted()` | 2.2, 2.3 |
| Que no impida terminar al programa | `setDaemon(true)` antes de `start()` | 2.3, 3.3 |
| Que una tarea vaya después de otra | `join()` (condiciones de Bernstein) | 3.1 |
| Que un hilo vea una bandera de otro | `volatile` (visibilidad, **no** atomicidad) | 3.3 |
| Que una operación compuesta sea indivisible | `synchronized` / `ReentrantLock` (exclusión mutua) | 4.1, 4.2, 4.4 |
| Un contador compartido | `AtomicInteger` / `AtomicLong` (CAS, sin cerrojo) | 4.1, 4.3 |
| Dos cerrojos sin interbloqueo | cogerlos siempre en el mismo orden | 4.4 |
