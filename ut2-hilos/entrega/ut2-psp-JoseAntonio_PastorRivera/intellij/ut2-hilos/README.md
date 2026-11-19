# PSP · UT2 · Relación de ejercicios de hilos (bloques 1 a 4)

**Alumno:** José Antonio Pastor Rivera · Programación de Servicios y Procesos · 2º DAM · IES Isidra de Guzmán

Versión para **IntelliJ IDEA**. Hay otra versión idéntica para VS Code en `../../vscode/ut2-hilos`. Las respuestas a las preguntas, con salidas reales y esquemas, están en `ut2-psp-JoseAntonio_PastorRivera.pdf`, en la raíz del zip.

## Abrir y ejecutar

1. *File → Open…* → esta carpeta (`ut2-hilos`).
2. Si pide JDK: *File → Project Structure → Project → SDK* → un JDK 21 o superior.
3. Arriba a la derecha elige la configuración **«Menú UT2 (elegir ejercicio)»** y pulsa ▶.
4. En la consola escribe el número del ejercicio (1-18), su código (`3.2`, `1.1b`…), `T` para todos o `0` para salir.

También se puede ejecutar cada ejercicio por separado con el ▶ que hay junto a su `main`.

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
