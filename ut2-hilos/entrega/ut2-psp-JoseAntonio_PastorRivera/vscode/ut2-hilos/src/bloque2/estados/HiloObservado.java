/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque2.estados;

/**
 * UT2 · Relación de ejercicios · Ejercicio 2.1 – Estados de un hilo
 *
 * SOLUCIÓN. El hilo que observa el main. Su run() está diseñado para pasar por cada estado:
 *   NEW            → antes de start() (lo pone la JVM, no hay que hacer nada)
 *   RUNNABLE       → mientras calcula (fase 1 y fase 4)
 *   TIMED_WAITING  → Thread.sleep(300): espera CON tiempo límite
 *   WAITING        → cerrojo.wait(): espera SIN tiempo límite, hasta que otro hilo haga notify()
 *   TERMINATED     → cuando run() termina (lo pone la JVM)
 */
public class HiloObservado extends Thread {        // aquí se usa la otra forma: extender Thread

    /** Objeto que usan los dos hilos para wait()/notify(). Cualquier objeto vale como cerrojo. */
    private final Object cerrojo;

    /** Para que el resultado del cálculo "se use" y el compilador JIT no elimine el bucle. */
    private long basura;

    /**
     * Crea el hilo observado, llamado "observado".
     *
     * @param cerrojo objeto compartido con el main para wait()/notify()
     */
    public HiloObservado(Object cerrojo) {
        super("observado");                         // nombre del hilo
        this.cerrojo = cerrojo;
    }

    /**
     * Recorre los estados: calcula (RUNNABLE), duerme (TIMED_WAITING), espera un notify() (WAITING) y vuelve a calcular.
     */
    @Override
    public void run() {
        calcular(200);                              // 1) RUNNABLE: 200 ms usando CPU

        try {
            Thread.sleep(300);                      // 2) TIMED_WAITING: dormido 300 ms

            synchronized (cerrojo) {                // wait() solo se puede llamar teniendo el cerrojo del objeto
                cerrojo.wait();                     // 3) WAITING: suelta el cerrojo y espera a un notify()
            }                                       //    (al despertar tiene que RECUPERAR el cerrojo: ver BLOCKED)
        } catch (InterruptedException e) {
            return;                                 // si nos interrumpen, terminamos
        }

        calcular(200);                              // 4) RUNNABLE otra vez
    }                                               // 5) al salir de run(): TERMINATED

    /** Gasta CPU durante unos milisegundos (estado RUNNABLE todo el rato). */
    private void calcular(long ms) {
        long fin = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < fin) {
            basura += Math.round(Math.sqrt(basura + 1));    // cálculo sin sentido, solo para ocupar la CPU
        }
    }
}
