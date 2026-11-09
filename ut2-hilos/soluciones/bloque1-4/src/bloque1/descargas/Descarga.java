/*
 * UT2 · Relación de ejercicios · Ejercicio 1.1 – Descargas
 *
 * Una descarga de un fichero a 10 MB/s.
 *
 * SOLUCIÓN. Descarga implementa Runnable para que la MISMA clase sirva en las dos versiones:
 *   - secuencial: el main llama directamente a descargar() (todo en el hilo main).
 *   - con hilos:  el main hace new Thread(descarga).start() y el hilo nuevo ejecuta run() → descargar().
 */
package bloque1.descargas;

public class Descarga implements Runnable {           // Runnable: "algo que un hilo puede ejecutar"

    static final int MB_POR_SEGUNDO = 10;

    /**
     * Momento en que arrancó el programa (milisegundos desde 1970).
     * Es static: hay UNO para todas las descargas. Lo inicializa la JVM la primera vez que se usa la clase
     * Descarga, que es justo al empezar el main (lo primero que hace el main es crear descargas).
     * Así cada descarga puede calcular "cuánto ha pasado desde que arrancó el programa".
     */
    static final long INICIO = System.currentTimeMillis();

    private final String fichero;
    private final int megas;

    public Descarga(String fichero, int megas) {
        this.fichero = fichero;
        this.megas = megas;
    }

    /** Simula la descarga e indica, al terminar, cuánto tiempo ha pasado desde que arrancó el programa. */
    public void descargar() {
        long duracionMs = megas * 1000L / MB_POR_SEGUNDO;  // 40 MB a 10 MB/s = 4 s = 4000 ms. 1000L: cuenta en long
        try {
            Thread.sleep(duracionMs);                      // "simúlalo": el hilo se queda dormido lo que duraría la descarga
        } catch (InterruptedException e) {                 // sleep obliga a capturarla: alguien nos ha interrumpido
            Thread.currentThread().interrupt();            // se vuelve a marcar la interrupción (buena práctica) …
            return;                                        // … y se abandona la descarga
        }
        long transcurrido = System.currentTimeMillis() - INICIO;   // tiempo desde el arranque del programa
        System.out.printf("%-12s (%2d MB) terminada a los %5.2f s  [%s]%n",
                fichero, megas, transcurrido / 1000.0, Thread.currentThread().getName());
        // Thread.currentThread().getName() muestra QUÉ hilo ha hecho la descarga: "main" en la secuencial,
        // "Thread-0", "Thread-1"… en la de hilos. Es la prueba visible de la diferencia.
    }

    /** Lo que ejecuta el hilo cuando se hace start(). Solo delega en descargar(). */
    @Override
    public void run() {
        descargar();
    }
}
