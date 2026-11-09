/*
 * UT2 · Relación de ejercicios · Ejercicio 3.1 – El informe de ventas
 *
 * Calcula el total facturado y el importe medio de las ventas.
 *
 * SOLUCIÓN. Puede recibir (o no) el hilo del cargador:
 *   - sin él (null): calcula en cuanto arranca → la versión "tal como se describe", que falla.
 *   - con él: primero hace join() sobre el cargador → la versión corregida.
 */
package bloque3.informe_ventas;

public class Calculador implements Runnable {

    private final Ventas ventas;
    private final Thread esperarA;      // hilo que tiene que terminar antes de calcular (o null)

    private long total;
    private double media;

    public Calculador(Ventas ventas) {
        this(ventas, null);             // versión del enunciado: no espera a nadie
    }

    public Calculador(Ventas ventas, Thread esperarA) {
        this.ventas = ventas;
        this.esperarA = esperarA;
    }

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

    @Override
    public void run() {
        calcular();
    }

    public long getTotal() { return total; }
    public double getMedia() { return media; }
}
