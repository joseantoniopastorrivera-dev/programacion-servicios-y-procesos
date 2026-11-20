/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque1.tarea_pesada;

/**
 * UT2 · Relación de ejercicios · Ejercicio 1.2 – Tarea pesada sin bloquear
 *
 * SOLUCIÓN B (la que planteó el profesor en clase): la clase HEREDA de Thread.
 *
 * Diferencia con la solución A (ContarPrimos implements Runnable):
 *   A) la clase es solo la TAREA y hay que meterla en un hilo:   new Thread(new ContarPrimos(n)).start();
 *   B) la clase ES el hilo y se arranca directamente:            new ContarPrimosHilo(n).start();
 */
public class ContarPrimosHilo extends Thread {      // extends Thread: este objeto ya es un hilo

    private final int limite;                       // hasta dónde hay que contar

    /**
     * Resultado del cálculo. run() es void y no puede devolver nada, así que se guarda aquí.
     * El main lo lee DESPUÉS de comprobar que el hilo ha terminado (isAlive() == false o join()).
     */
    private int resultado;

    /**
     * Crea el hilo de cálculo, llamado "calculo".
     *
     * @param limite número hasta el que se cuentan los primos (incluido)
     */
    public ContarPrimosHilo(int limite) {
        super("calculo");                           // llama al constructor de Thread que pone nombre al hilo
        this.limite = limite;
    }

    /**
     * Lo que ejecuta el hilo. Se SOBRESCRIBE el run() de Thread (que por defecto no hace nada).
     * Nunca se llama a run() directamente: se llama a start(), y es start() quien crea el hilo
     * nuevo y hace que ese hilo ejecute run().
     */
    @Override
    public void run() {
        int cuenta = 0;                             // variable local: es solo de este hilo
        for (int n = 2; n <= limite; n++) {         // todos los números del 2 al límite (incluido)
            if (ContarPrimos.esPrimo(n)) {          // se reutiliza el esPrimo que ya venía hecho
                cuenta++;
            }
        }
        resultado = cuenta;                         // hasta 8.000.000 hay 539.777 primos
    }

    /**
     * Devuelve cuántos primos se han encontrado. Solo es fiable cuando el hilo ha terminado (tras join()).
     *
     * @return número de primos hasta el límite
     */
    public int getResultado() {
        return resultado;
    }
}
