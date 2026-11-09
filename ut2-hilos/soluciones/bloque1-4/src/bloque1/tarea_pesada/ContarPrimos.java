/*
 * UT2 · Relación de ejercicios · Ejercicio 1.2 – Tarea pesada sin bloquear
 *
 * El cálculo largo: cuántos números primos hay hasta un límite.
 *
 * SOLUCIÓN. Implementa Runnable para poder ejecutar el cálculo en otro hilo (versión ConHilos).
 * En la versión SinHilos se llama directamente a contar().
 */
package bloque1.tarea_pesada;

public class ContarPrimos implements Runnable {

    private final int limite;

    /**
     * Resultado del cálculo cuando se ejecuta en un hilo (run() no puede devolver nada: es void).
     * El main lo lee DESPUÉS de comprobar que el hilo ha terminado (isAlive() == false / join()),
     * y eso garantiza que ve el valor ya escrito.
     */
    private int resultado;

    public ContarPrimos(int limite) {
        this.limite = limite;
    }

    /** Ya está hecho: no forma parte del ejercicio. */
    static boolean esPrimo(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int d = 3; (long) d * d <= n; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }

    /** Cuenta los primos entre 2 y el límite. */
    public int contar() {
        int cuenta = 0;                         // variable local: es solo de este hilo, no hay nada compartido
        for (int n = 2; n <= limite; n++) {     // recorre todos los números del 2 al límite (incluido)
            if (esPrimo(n)) {
                cuenta++;
            }
        }
        return cuenta;                          // hasta 8.000.000 hay 539.777 primos
    }

    /** Lo que hace el hilo: calcular y guardar el resultado. */
    @Override
    public void run() {
        resultado = contar();
    }

    public int getResultado() {
        return resultado;
    }
}
