/*
 * UT2 · Relación de ejercicios · Ejercicio 3.1 – El informe de ventas
 *
 * Carga las ventas del día (importes de 1 a 100 €).
 */
package bloque3.informe_ventas;

import java.util.concurrent.ThreadLocalRandom;

public class Cargador implements Runnable {

    private final Ventas ventas;

    /** Total que DEBERÍA salir: lo apunta el propio cargador para poder comprobar el informe. */
    private long totalReal;

    public Cargador(Ventas ventas) {
        this.ventas = ventas;
    }

    public void cargar() {
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        for (int i = 0; i < Ventas.NUM_VENTAS; i++) {
            int importe = aleatorio.nextInt(1, 101);    // de 1 a 100 €
            ventas.importes[i] = importe;               // ESCRIBE en el dato compartido
            totalReal += importe;
        }
    }

    @Override
    public void run() {
        cargar();
    }

    public long getTotalReal() {
        return totalReal;
    }
}
