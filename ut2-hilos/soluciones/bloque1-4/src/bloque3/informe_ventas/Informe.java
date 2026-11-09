/*
 * UT2 · Relación de ejercicios · Ejercicio 3.1 – El informe de ventas
 *
 * El cargador y el calculador se lanzan a la vez.
 *
 * SOLUCIÓN. Se ejecuta dos veces:
 *   1) Tal como se describe: los dos hilos a la vez → el calculador suma ventas que aún no existen (ceros).
 *   2) Corregido con join(): el calculador espera a que el cargador termine.
 */
package bloque3.informe_ventas;

public class Informe {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("== 1) Carga y cálculo A LA VEZ (como dice el enunciado)");
        generar(false);
        System.out.println();
        System.out.println("== 2) Corregido: el calculador hace join() sobre el cargador");
        generar(true);
    }

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
