/*
 * UT2 · Relación de ejercicios · Ejercicio 1.2 – Tarea pesada sin bloquear
 *
 * Versión con hilos.
 *
 * El cálculo va a un hilo nuevo; el main se queda libre para "atender al usuario" cada medio segundo.
 */
package bloque1.tarea_pesada;

public class ConHilos {

    static final int LIMITE = 8_000_000;

    public static void main(String[] args) throws InterruptedException {
        long inicio = System.currentTimeMillis();

        ContarPrimos calculo = new ContarPrimos(LIMITE);
        Thread hiloCalculo = new Thread(calculo, "calculo");   // 2.º parámetro: nombre del hilo (útil para depurar)
        hiloCalculo.start();                                   // el cálculo empieza en paralelo y el main sigue

        while (hiloCalculo.isAlive()) {                        // mientras el cálculo no haya terminado…
            System.out.println("Sigo atendiendo al usuario...");
            hiloCalculo.join(500);                             // … espera COMO MÁXIMO 500 ms a que termine
        }
        // ¿Por qué join(500) y no Thread.sleep(500)? Los dos esperan medio segundo, pero join(500) se despierta
        // en cuanto el cálculo acaba: el resultado sale sin el retraso de hasta 0,5 s que daría sleep.

        // Aquí el hilo ya ha terminado: es seguro leer su resultado (join garantiza que vemos lo que escribió).
        System.out.printf("Hay %,d primos hasta %,d (%.2f s)%n",
                calculo.getResultado(), LIMITE, (System.currentTimeMillis() - inicio) / 1000.0);
    }
}
