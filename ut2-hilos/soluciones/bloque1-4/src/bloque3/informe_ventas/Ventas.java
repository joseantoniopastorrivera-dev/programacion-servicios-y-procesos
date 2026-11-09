/*
 * UT2 · Relación de ejercicios · Ejercicio 3.1 – El informe de ventas
 *
 * Las ventas del día.
 *
 * SOLUCIÓN. Un array de int con un hueco por venta. Es el dato COMPARTIDO: el Cargador escribe en él
 * y el Calculador lee de él.
 */
package bloque3.informe_ventas;

public class Ventas {

    static final int NUM_VENTAS = 1_000_000;

    /** importes[i] = euros de la venta i (de 1 a 100). Un 0 significa "esa venta aún no se ha cargado". */
    final int[] importes = new int[NUM_VENTAS];
}
