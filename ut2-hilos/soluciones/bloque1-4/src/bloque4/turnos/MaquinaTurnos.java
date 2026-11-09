/*
 * UT2 · Relación de ejercicios · Ejercicio 4.3 – La máquina de turnos
 *
 * La máquina de turnos de la carnicería: ningún número puede repetirse.
 *
 * SOLUCIÓN. Una misma clase con tres modos, para comparar en la salida:
 *   SIN_PROTECCION → siguiente++ a pelo (pregunta 1: salen repetidos)
 *   SYNCHRONIZED   → la solución básica
 *   CAS            → la ampliación: sin synchronized y sin incrementAndGet/getAndIncrement
 */
package bloque4.turnos;

import java.util.concurrent.atomic.AtomicInteger;

public class MaquinaTurnos {

    public enum Modo { SIN_PROTECCION, SYNCHRONIZED, CAS }

    private final Modo modo;
    private int siguiente = 1;                                   // para SIN_PROTECCION y SYNCHRONIZED
    private final AtomicInteger siguienteAtomico = new AtomicInteger(1);   // para CAS

    public MaquinaTurnos(Modo modo) {
        this.modo = modo;
    }

    public int nuevoTurno() {
        switch (modo) {
            case SIN_PROTECCION:
                return siguiente++;                 // leer, sumar, escribir: dos clientes pueden sacar el mismo

            case SYNCHRONIZED:
                synchronized (this) {               // un cliente a la vez en la máquina
                    return siguiente++;             // return dentro de synchronized: el cerrojo se suelta al salir
                }

            case CAS:
            default:
                // Ampliación: compare-and-set (CAS) en un bucle, "a mano".
                while (true) {
                    int actual = siguienteAtomico.get();                    // 1) leo el número actual
                    if (siguienteAtomico.compareAndSet(actual, actual + 1)) {
                        // 2) "si SIGUE valiendo actual, ponlo a actual+1", todo en una operación atómica.
                        //    Si devuelve true, nadie se ha colado entre 1) y 2): el número actual es MÍO.
                        return actual;
                    }
                    // 3) false: otro cliente lo cambió entre medias. No pasa nada: vuelvo a intentarlo.
                    //    Nadie se bloquea; como mucho se repite el bucle (por eso se llama "sin cerrojos").
                }
        }
    }
}
