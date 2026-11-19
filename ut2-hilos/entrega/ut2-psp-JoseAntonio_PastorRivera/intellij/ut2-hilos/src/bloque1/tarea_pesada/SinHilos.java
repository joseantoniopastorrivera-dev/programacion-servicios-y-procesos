/*
 * UT2 · Relación de ejercicios · Ejercicio 1.2 – Tarea pesada sin bloquear
 *
 * Versión sin hilos.
 *
 * Con un solo hilo (main) es IMPOSIBLE hacer las dos cosas a la vez: mientras el main está dentro de
 * contar(), no puede ejecutar ningún println. El mensaje solo puede salir antes o después del cálculo.
 */
package bloque1.tarea_pesada;

public class SinHilos {

    static final int LIMITE = 8_000_000;          // el _ separa miles: es 8000000 (desde Java 7)

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        System.out.println("Sigo atendiendo al usuario...");     // sale una vez…

        ContarPrimos calculo = new ContarPrimos(LIMITE);
        int primos = calculo.contar();            // … y el main se queda aquí BLOQUEADO varios segundos

        System.out.println("Sigo atendiendo al usuario...");     // … y otra cuando ya es tarde
        System.out.printf("Hay %,d primos hasta %,d (%.2f s)%n",
                primos, LIMITE, (System.currentTimeMillis() - inicio) / 1000.0);
        // Para el usuario el programa ha estado "colgado" todo el cálculo: no ha dado señales de vida.
    }
}
