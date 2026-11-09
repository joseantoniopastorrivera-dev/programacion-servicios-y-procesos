/*
 * UT2 · Relación de ejercicios · Ejercicio 3.3 – El vigilante (pregunta 2: corrección)
 *
 * SOLUCIÓN. Idéntico a Vigilante, pero el aviso es volatile.
 * volatile garantiza VISIBILIDAD: toda lectura de la variable va a memoria principal y ve la última
 * escritura de cualquier hilo. El JIT ya no puede "guardarse" el valor y convertir el bucle en infinito.
 */
package bloque3.vigilante;

public class VigilanteVolatile extends Thread {

    private volatile boolean turnoTerminado = false;    // ← LA CORRECCIÓN: una sola palabra

    private long rondas = 0;

    public VigilanteVolatile(String nombre) {
        super(nombre);
    }

    public void avisarFinDeTurno() {
        turnoTerminado = true;
    }

    @Override
    public void run() {
        while (!turnoTerminado) {
            rondas++;
        }
        System.out.printf("%s: me he enterado del aviso. He hecho %,d rondas.%n", getName(), rondas);
    }
}
