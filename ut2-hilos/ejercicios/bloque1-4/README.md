# PSP · UT2 – Relación de ejercicios (bloques 1 a 4)

Proyecto de IntelliJ IDEA para resolver los ejercicios de `Ejercicios_UT2.docx`.

## Abrir el proyecto

En IntelliJ: *File → Open…* y elige la carpeta `bloque1-4`. Necesitas Java 21 o superior. Si IntelliJ no encuentra el JDK, elígelo en *File → Project Structure → Project → SDK*.

## Cómo está organizado

Hay un paquete por ejercicio, con las clases ya creadas **pero sin desarrollar**: los métodos marcados con `// TODO` son los que tienes que programar. Puedes añadir los atributos, métodos y clases que necesites, y decidir cómo se ejecuta cada cosa en su propio hilo.

| Ejercicio | Paquete | Clase con el `main` |
|---|---|---|
| 1.1 Descargas | `bloque1.descargas` | `DescargasSecuencial`, `DescargasConHilos` |
| 1.2 Tarea pesada sin bloquear | `bloque1.tarea_pesada` | `SinHilos`, `ConHilos` |
| 2.1 Estados de un hilo | `bloque2.estados` | `ObservarEstados` |
| 2.2 El examen | `bloque2.examen` | `Examen`, `ExamenVariosAlumnos` (ampliación) |
| 2.3 Autoguardado | `bloque2.autoguardado` | `Editor` |
| 2.4 Taxis | `bloque2.taxis` | `Flota` (la clase `Vehiculo` no se puede modificar) |
| 3.1 El informe de ventas | `bloque3.informe_ventas` | `Informe` |
| 3.2 El jardín ornamental | `bloque3.jardin` | `Jardin` |
| 3.3 El vigilante | `bloque3.vigilante` | `Vigilancia` |
| 3.4 Venta de entradas | `bloque3.entradas` | `Cine` |
| 4.1 Arregla el jardín | `bloque4.jardin` | `JardinCorregido` (parte de tu 3.2) |
| 4.2 Taquillas sin overbooking | `bloque4.entradas` | `CineCorregido` (parte de tu 3.4) |
| 4.3 La máquina de turnos | `bloque4.turnos` | `Carniceria` |
| 4.4 El banco | `bloque4.banco` | `Transferencias` |

Ejecuta cada programa **varias veces**: con hilos, que salga bien una vez no demuestra que esté bien.
