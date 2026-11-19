/*
 * UT2 · Relación de ejercicios · Ejercicio 3.3 – El vigilante
 *
 * Hace rondas sin parar hasta que le avisan de que su turno ha terminado.
 *
 * SOLUCIÓN (versión del enunciado, CON el fallo): el aviso es un boolean normal (no volatile).
 */
package bloque3.vigilante;

public class Vigilante extends Thread {

    /**
     * El aviso. Lo escribe el jefe (hilo main) y lo lee el vigilante (este hilo).
     * SIN volatile, la JVM no garantiza que el vigilante vea el cambio: el compilador JIT puede leerlo
     * UNA vez, ver que dentro del bucle nadie lo cambia, y convertir el bucle en while (true).
     */
    private boolean turnoTerminado = false;

    private long rondas = 0;

    public Vigilante() {
        super("vigilante");
    }

    /** Lo llama el jefe de seguridad (otro hilo). */
    public void avisarFinDeTurno() {
        turnoTerminado = true;
    }

    public void hacerRondas() {
        while (!turnoTerminado) {       // ¿ve este hilo el true que escribió el main? NO está garantizado
            rondas++;                   // una ronda (muy rápida: solo cuenta)
        }
        System.out.printf("Vigilante: me he enterado del aviso. He hecho %,d rondas.%n", rondas);
    }

    @Override
    public void run() {
        hacerRondas();
    }
}
