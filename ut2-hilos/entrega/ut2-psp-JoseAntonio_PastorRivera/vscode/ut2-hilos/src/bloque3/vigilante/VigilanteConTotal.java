/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.vigilante;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.3 – El vigilante (pregunta 3)
 *
 * SOLUCIÓN. Varios vigilantes que, además de contar sus rondas, las suman en un TOTAL COMÚN volatile.
 * Sirve para comprobar que volatile NO basta para un contador compartido:
 *   volatile da visibilidad (todos ven el último valor), pero NO atomicidad: total++ sigue siendo
 *   leer + sumar + escribir, y dos vigilantes pueden pisarse igual que las puertas del jardín.
 */
public class VigilanteConTotal extends Thread {

    static volatile boolean turnoTerminado = false;     // aviso común para todos (volatile: se enteran)
    static volatile long totalRondas = 0;               // total común: volatile… pero NO basta

    private long misRondas = 0;                         // rondas de este vigilante (solo las toca él)

    /**
     * Crea un vigilante que suma sus rondas también en un total común.
     *
     * @param nombre nombre del vigilante (también será el del hilo)
     */
    public VigilanteConTotal(String nombre) {
        super(nombre);
    }

    /**
     * Hace rondas hasta el aviso, contando las suyas y sumando en el total común (volatile, sin atomicidad).
     */
    @Override
    public void run() {
        while (!turnoTerminado) {
            misRondas++;
            totalRondas++;                              // condición de carrera: se pierden sumas
        }
    }

    /**
     * Rondas que ha hecho este vigilante (solo él escribe en este contador).
     *
     * @return número de rondas propias
     */
    public long getMisRondas() {
        return misRondas;
    }
}
