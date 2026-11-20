/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.vigilante;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.3 – El vigilante (pregunta 2: corrección)
 *
 * SOLUCIÓN. Idéntico a Vigilante, pero el aviso es volatile.
 * volatile garantiza VISIBILIDAD: toda lectura de la variable va a memoria principal y ve la última
 * escritura de cualquier hilo. El JIT ya no puede "guardarse" el valor y convertir el bucle en infinito.
 */
public class VigilanteVolatile extends Thread {

    private volatile boolean turnoTerminado = false;    // ← LA CORRECCIÓN: una sola palabra

    private long rondas = 0;

    /**
     * Crea un vigilante con el aviso volatile.
     *
     * @param nombre nombre del vigilante (también será el del hilo)
     */
    public VigilanteVolatile(String nombre) {
        super(nombre);
    }

    /**
     * Da el aviso de fin de turno. Lo llama el jefe, desde otro hilo.
     */
    public void avisarFinDeTurno() {
        turnoTerminado = true;
    }

    /**
     * Hace rondas hasta ver el aviso (con volatile lo ve siempre) y dice cuántas ha hecho.
     */
    @Override
    public void run() {
        while (!turnoTerminado) {
            rondas++;
        }
        System.out.printf("%s: me he enterado del aviso. He hecho %,d rondas.%n", getName(), rondas);
    }
}
