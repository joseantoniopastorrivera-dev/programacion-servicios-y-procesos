/*
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * Copia en este paquete tu solución del ejercicio 3.2 y corrígela de tres formas distintas.
 *
 * SOLUCIÓN. 4 puertas × 1.000.000 de visitantes con cada una de las tres correcciones, 3 veces cada una,
 * midiendo el tiempo. La primera vuelta de cada una suele ser más lenta (la JVM aún está compilando el código
 * con el JIT): por eso se repite y se compara el mejor tiempo.
 */
package bloque4.jardin;

import java.util.function.Supplier;

public class JardinCorregido {

    static final int PUERTAS = 4;
    static final int POR_PUERTA = 1_000_000;
    static final int REPETICIONES = 3;

    public static void main(String[] args) throws InterruptedException {
        probar("synchronized ", ContadorSynchronized::new);
        probar("ReentrantLock", ContadorLock::new);
        probar("AtomicInteger", ContadorAtomico::new);
    }

    /**
     * Supplier<Contador>: "algo que sabe crear un Contador". ContadorSynchronized::new es una referencia al
     * constructor; así cada repetición empieza con un contador nuevo (a 0).
     */
    private static void probar(String nombre, Supplier<Contador> fabrica) throws InterruptedException {
        long mejor = Long.MAX_VALUE;
        boolean todasBien = true;
        for (int r = 0; r < REPETICIONES; r++) {
            Contador contador = fabrica.get();                     // contador nuevo
            Puerta[] puertas = new Puerta[PUERTAS];

            long inicio = System.nanoTime();                       // nanoTime: el reloj para MEDIR duraciones
            for (int i = 0; i < PUERTAS; i++) {
                puertas[i] = new Puerta("Puerta " + (i + 1), contador, POR_PUERTA);
                puertas[i].start();
            }
            for (Puerta p : puertas) {
                p.join();
            }
            long ms = (System.nanoTime() - inicio) / 1_000_000;

            // Lectura del total cuando YA han terminado todas: join() garantiza que vemos todo lo que
            // escribieron las puertas. Aquí ya no hay concurrencia (pregunta 3).
            int total = contador.total();
            todasBien &= total == PUERTAS * POR_PUERTA;
            mejor = Math.min(mejor, ms);
            System.out.printf("%s  vuelta %d: %,d visitantes en %4d ms%n", nombre, r + 1, total, ms);
        }
        System.out.printf("%s  → %s, mejor tiempo %d ms%n%n", nombre,
                todasBien ? "siempre correcto" : "¡FALLA!", mejor);
    }
}
