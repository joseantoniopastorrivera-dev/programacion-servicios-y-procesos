/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque2.estados;

import java.util.ArrayList;
import java.util.List;

/**
 * UT2 · Relación de ejercicios · Ejercicio 2.1 – Estados de un hilo
 *
 * Programa principal: observa un hilo mientras se ejecuta y muestra sus estados.
 * Crea en este paquete las clases que necesites.
 *
 * SOLUCIÓN. El main consulta getState() en un bucle muy rápido (sin dormir, para no perderse estados cortos)
 * y apunta el estado SOLO cuando cambia respecto al anterior ("sin repetir estados seguidos").
 * Cuando ve al hilo en WAITING, le despierta con notify() para que pueda seguir y terminar.
 */
public class ObservarEstados {

    /**
     * Arranca el hilo observado, apunta cada cambio de estado con getState() y muestra la secuencia.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        Object cerrojo = new Object();                      // el objeto que comparten para wait()/notify()
        HiloObservado hilo = new HiloObservado(cerrojo);

        List<Thread.State> secuencia = new ArrayList<>();   // estados por los que pasa, en orden
        secuencia.add(hilo.getState());                     // NEW: aún no se ha hecho start()

        hilo.start();

        Thread.State anterior = Thread.State.NEW;
        while (anterior != Thread.State.TERMINATED) {       // hasta verlo terminado
            Thread.State actual = hilo.getState();          // foto del estado en este instante
            if (actual != anterior) {                       // solo si ha cambiado → no se repiten seguidos
                secuencia.add(actual);
                anterior = actual;
            }
            if (actual == Thread.State.WAITING) {           // está esperando un aviso…
                synchronized (cerrojo) {                    // notify() también exige tener el cerrojo
                    cerrojo.notify();                       // … se lo damos: el hilo puede continuar
                }
            }
        }
        // Bucle de espera activa ("busy waiting"): aquí se hace a propósito para no perder estados que duran
        // muy poco. En un programa normal se esperaría con join().

        System.out.println("Secuencia de estados del hilo:");
        System.out.println(String.join(" → ", secuencia.stream().map(Enum::name).toList()));
        // Salida típica:
        // NEW → RUNNABLE → TIMED_WAITING → RUNNABLE → WAITING → BLOCKED → RUNNABLE → TERMINATED
        // El BLOCKED (y el RUNNABLE entre TIMED_WAITING y WAITING) se explican en soluciones-ejercicios-ut2.md.
    }
}
