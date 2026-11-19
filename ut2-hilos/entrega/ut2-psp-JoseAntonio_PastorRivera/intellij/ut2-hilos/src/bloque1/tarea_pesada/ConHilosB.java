/*
 * UT2 · Relación de ejercicios · Ejercicio 1.2 – Tarea pesada sin bloquear
 *
 * Versión con hilos · SOLUCIÓN B (extends Thread), la que planteó el profesor en clase.
 *
 * Hace lo mismo que ConHilos; solo cambia cómo se crea el hilo.
 */
package bloque1.tarea_pesada;

public class ConHilosB {

    static final int LIMITE = 8_000_000;

    public static void main(String[] args) throws InterruptedException {
        long inicio = System.currentTimeMillis();

        // El objeto YA es un hilo: no hace falta envolverlo en un new Thread(...).
        ContarPrimosHilo hiloCalculo = new ContarPrimosHilo(LIMITE);
        hiloCalculo.start();                                   // start(), no run(): crea el hilo y el main sigue

        while (hiloCalculo.isAlive()) {                        // mientras el cálculo no haya terminado…
            System.out.println("Sigo atendiendo al usuario...");
            hiloCalculo.join(500);                             // … espera COMO MÁXIMO 500 ms a que termine
        }

        // Aquí el hilo ya ha terminado: es seguro leer su resultado.
        System.out.printf("Hay %,d primos hasta %,d (%.2f s)%n",
                hiloCalculo.getResultado(), LIMITE, (System.currentTimeMillis() - inicio) / 1000.0);
    }
}
