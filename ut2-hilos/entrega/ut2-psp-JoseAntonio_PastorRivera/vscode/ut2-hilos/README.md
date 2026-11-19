# PSP · UT2 · Relación de ejercicios de hilos (bloques 1 a 4)

**Alumno:** José Antonio Pastor Rivera · Programación de Servicios y Procesos · 2º DAM · IES Isidra de Guzmán

Versión para **Visual Studio Code**. Hay otra versión idéntica para IntelliJ en `../../intellij/ut2-hilos`. Las respuestas a las preguntas, con salidas reales y esquemas, están en `ut2-psp-JoseAntonio_PastorRivera.pdf`, en la raíz del zip.

## Requisitos

- Un **JDK 21 o superior**.
- Extensión **Extension Pack for Java** (Microsoft) en VS Code.

## Ejecutar

**Opción 1 · desde VS Code**

1. *File → Open Folder…* → esta carpeta (`ut2-hilos`).
2. Pestaña *Run and Debug* (Ctrl+Shift+D) → **«Menú UT2 (elegir ejercicio)»** → ▶ (o F5).
3. En la terminal escribe el número del ejercicio (1-18), su código (`3.2`, `1.1b`…), `T` para todos o `0` para salir.

La configuración **«Ejercicio abierto en el editor»** ejecuta directamente la clase que tengas abierta.

**Opción 2 · script** (compila y abre el menú)

- Windows: `powershell -ExecutionPolicy Bypass -File .\ejecutar.ps1`
- Linux: `sh ejecutar.sh`

**Opción 3 · a mano**

```
javac -encoding UTF-8 -d out <todas las fuentes de src>
java -cp out menu.Menu
```

> ⚠️ Si sale `UnsupportedClassVersionError`, el `java` del PATH es más antiguo que el `javac` que compiló (por ejemplo, un Java 8 junto a un JDK 25). Usa los dos del mismo JDK; el script `ejecutar.ps1` ya lo hace.

## El menú

```
  menu.Menu (proceso padre)
     │  ProcessBuilder + inheritIO()
     ▼
  java -cp out bloque3.jardin.Jardin (proceso hijo)
     │  waitFor()
     ▼
  vuelve al menú
```

`menu.Menu` lanza cada ejercicio en un **proceso hijo** (lo de la UT1), con el mismo JDK y el mismo classpath. Así ningún ejercicio deja hilos vivos dentro del menú, y cada uno empieza con una JVM limpia.

## Organización

| Ejercicio | Paquete | Clase con el `main` |
|---|---|---|
| 1.1 Descargas | `bloque1.descargas` | `DescargasSecuencial`, `DescargasConHilos` |
| 1.2 Tarea pesada | `bloque1.tarea_pesada` | `SinHilos`, `ConHilos` (A: `Runnable`), `ConHilosB` (B: `extends Thread`) |
| 2.1 Estados | `bloque2.estados` | `ObservarEstados` |
| 2.2 Examen | `bloque2.examen` | `Examen`, `ExamenVariosAlumnos` |
| 2.3 Autoguardado | `bloque2.autoguardado` | `Editor` |
| 2.4 Taxis | `bloque2.taxis` | `Flota` |
| 3.1 Informe de ventas | `bloque3.informe_ventas` | `Informe` |
| 3.2 Jardín | `bloque3.jardin` | `Jardin` |
| 3.3 Vigilante | `bloque3.vigilante` | `Vigilancia` |
| 3.4 Entradas | `bloque3.entradas` | `Cine` |
| 4.1 Arregla el jardín | `bloque4.jardin` | `JardinCorregido` |
| 4.2 Taquillas | `bloque4.entradas` | `CineCorregido` |
| 4.3 Turnos | `bloque4.turnos` | `Carniceria` |
| 4.4 Banco | `bloque4.banco` | `Transferencias` |
| Menú | `menu` | `Menu` |

Cada clase explica línea a línea en sus comentarios lo que hace y por qué. Ejecuta cada programa **varias veces**: con hilos, que salga bien una vez no demuestra que esté bien.
