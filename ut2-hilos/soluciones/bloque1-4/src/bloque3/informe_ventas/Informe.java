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
 * El cargador y el calculador se lanzan a la vez.
 *
 * SOLUCIÓN. Se ejecuta dos veces:
 *   1) Tal como se describe: los dos hilos a la vez → el calculador suma ventas que aún no existen (ceros).
 *   2) Corregido con join(): el calculador espera a que el cargador termine.
 */
public class Informe {

    /**
     * Genera el informe dos veces: con carga y cálculo a la vez (falla) y con join() (correcto).
     *
     * @param args no se usan
     * @throws InterruptedException si el main es interrumpido mientras espera
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("== 1) Carga y cálculo A LA VEZ (como dice el enunciado)");
        generar(false);
        System.out.println();
        System.out.println("== 2) Corregido: el calculador hace join() sobre el cargador");
        generar(true);
    }

    /**
     * Lanza el cargador y el calculador a la vez, espera a los dos y compara el total con el real.
     *
     * @param corregido true para que el calculador espere al cargador con join()
     * @throws InterruptedException si el main es interrumpido mientras espera
     */
    private static void generar(boolean corregido) throws InterruptedException {
        Ventas ventas = new Ventas();                       // ventas nuevas (todo a 0) en cada informe
        Cargador cargador = new Cargador(ventas);
        Thread hiloCargador = new Thread(cargador, "cargador");

        // Si está corregido, el calculador recibe el hilo del cargador para esperarle.
        Calculador calculador = corregido ? new Calculador(ventas, hiloCargador) : new Calculador(ventas);
        Thread hiloCalculador = new Thread(calculador, "calculador");

        hiloCargador.start();                               // se lanzan a la vez
        hiloCalculador.start();
        hiloCargador.join();                                // el main espera a los dos para mostrar el informe
        hiloCalculador.join();

        System.out.printf("Total facturado: %,d €   Importe medio: %.2f €%n",
                calculador.getTotal(), calculador.getMedia());
        System.out.printf("Total real:      %,d €   → informe %s%n", cargador.getTotalReal(),
                calculador.getTotal() == cargador.getTotalReal() ? "CORRECTO" : "INCORRECTO");
        // El importe medio correcto ronda 50,50 € (media de 1..100). Con el fallo sale mucho menor.
    }
}
