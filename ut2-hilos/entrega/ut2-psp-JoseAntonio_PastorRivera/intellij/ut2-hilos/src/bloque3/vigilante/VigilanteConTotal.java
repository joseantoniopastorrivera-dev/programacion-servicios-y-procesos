/*
 * UT2 · Relación de ejercicios · Ejercicio 3.3 – El vigilante (pregunta 3)
 *
 * SOLUCIÓN. Varios vigilantes que, además de contar sus rondas, las suman en un TOTAL COMÚN volatile.
 * Sirve para comprobar que volatile NO basta para un contador compartido:
 *   volatile da visibilidad (todos ven el último valor), pero NO atomicidad: total++ sigue siendo
 *   leer + sumar + escribir, y dos vigilantes pueden pisarse igual que las puertas del jardín.
 */
package bloque3.vigilante;

public class VigilanteConTotal extends Thread {

    static volatile boolean turnoTerminado = false;     // aviso común para todos (volatile: se enteran)
    static volatile long totalRondas = 0;               // total común: volatile… pero NO basta

    private long misRondas = 0;                         // rondas de este vigilante (solo las toca él)

    public VigilanteConTotal(String nombre) {
        super(nombre);
    }

    @Override
    public void run() {
        while (!turnoTerminado) {
            misRondas++;
            totalRondas++;                              // condición de carrera: se pierden sumas
        }
    }

    public long getMisRondas() {
        return misRondas;
    }
}
