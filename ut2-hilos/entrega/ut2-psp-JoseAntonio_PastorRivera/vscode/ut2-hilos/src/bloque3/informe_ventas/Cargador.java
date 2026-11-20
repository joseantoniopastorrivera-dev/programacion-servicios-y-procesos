/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.informe_ventas;

import java.util.concurrent.ThreadLocalRandom;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.1 – El informe de ventas
 *
 * Carga las ventas del día (importes de 1 a 100 €).
 */
public class Cargador implements Runnable {

    private final Ventas ventas;

    /** Total que DEBERÍA salir: lo apunta el propio cargador para poder comprobar el informe. */
    private long totalReal;

    /**
     * Crea el cargador de las ventas del día.
     *
     * @param ventas ventas compartidas con el calculador
     */
    public Cargador(Ventas ventas) {
        this.ventas = ventas;
    }

    /**
     * Rellena el array con importes aleatorios y lleva la suma real para poder comprobar el informe.
     */
    public void cargar() {
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        for (int i = 0; i < Ventas.NUM_VENTAS; i++) {
            int importe = aleatorio.nextInt(1, 101);    // de 1 a 100 €
            ventas.importes[i] = importe;               // ESCRIBE en el dato compartido
            totalReal += importe;
        }
    }

    /**
     * Trabajo del hilo: cargar las ventas.
     */
    @Override
    public void run() {
        cargar();
    }

    /**
     * Suma real de lo cargado, para comprobar si el informe es correcto.
     *
     * @return total facturado de verdad
     */
    public long getTotalReal() {
        return totalReal;
    }
}
