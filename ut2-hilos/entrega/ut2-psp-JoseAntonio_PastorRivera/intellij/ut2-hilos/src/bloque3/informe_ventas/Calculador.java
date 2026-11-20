/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.informe_ventas;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.1 – El informe de ventas
 *
 * Calcula el total facturado y el importe medio de las ventas.
 *
 * SOLUCIÓN. Puede recibir (o no) el hilo del cargador:
 *   - sin él (null): calcula en cuanto arranca → la versión "tal como se describe", que falla.
 *   - con él: primero hace join() sobre el cargador → la versión corregida.
 */
public class Calculador implements Runnable {

    private final Ventas ventas;
    private final Thread esperarA;      // hilo que tiene que terminar antes de calcular (o null)

    private long total;
    private double media;

    /**
     * Calculador que NO espera al cargador (la versión del enunciado, con el fallo).
     *
     * @param ventas ventas compartidas con el cargador
     */
    public Calculador(Ventas ventas) {
        this(ventas, null);             // versión del enunciado: no espera a nadie
    }

    /**
     * Calculador que espera a que termine un hilo antes de calcular (la corrección).
     *
     * @param ventas   ventas compartidas con el cargador
     * @param esperarA hilo que debe terminar antes (el cargador), o null para no esperar
     */
    public Calculador(Ventas ventas, Thread esperarA) {
        this.ventas = ventas;
        this.esperarA = esperarA;
    }

    /**
     * Suma los importes y calcula la media. Si tiene un hilo que esperar, primero hace join() sobre él.
     */
    public void calcular() {
        if (esperarA != null) {
            try {
                esperarA.join();        // CORRECCIÓN: no se lee nada hasta que el cargador haya terminado
            } catch (InterruptedException e) {
                return;
            }
        }
        long suma = 0;
        for (int i = 0; i < Ventas.NUM_VENTAS; i++) {
            suma += ventas.importes[i]; // LEE el dato compartido: si aún no se ha cargado, lee un 0
        }
        total = suma;
        media = (double) suma / Ventas.NUM_VENTAS;   // (double): si no, la división sería entera
    }

    /**
     * Trabajo del hilo: calcular el informe.
     */
    @Override
    public void run() {
        calcular();
    }

    public long getTotal() { return total; }
    public double getMedia() { return media; }
}
