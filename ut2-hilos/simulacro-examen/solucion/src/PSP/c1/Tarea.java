/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c1;

/**
 * Ejercicio C1.1 · Una tarea del despliegue, que se ejecuta en su propio hilo.
 *
 * Antes de empezar espera a que terminen TODAS las tareas de las que depende (join entre hilos),
 * después «trabaja» el tiempo indicado (sleep) y avisa al empezar y al terminar con los
 * milisegundos transcurridos desde el inicio del despliegue.
 */
public class Tarea extends Thread {

    /** Momento en que empieza el despliegue; lo fija el main con {@link #marcarInicio()}. */
    private static long inicio = System.currentTimeMillis();

    /** Duración del trabajo de esta tarea, en milisegundos. */
    private final int ms;

    /** Tareas que tienen que terminar antes de que empiece esta. */
    private final Thread[] dependencias;

    /**
     * Crea la tarea (todavía NO arranca el hilo).
     *
     * @param nombre       nombre de la tarea, por ejemplo "T2 · Compilar el backend" (será el nombre del hilo)
     * @param ms           milisegundos que tarda la tarea
     * @param dependencias tareas que deben terminar antes (varargs: cero, una o varias)
     */
    public Tarea(String nombre, int ms, Thread... dependencias) {
        super(nombre);                       // el nombre del hilo es el de la tarea: getName() lo devuelve
        this.ms = ms;
        this.dependencias = dependencias;
    }

    /** Pone a cero el reloj del despliegue. El main lo llama justo antes de arrancar las tareas. */
    public static void marcarInicio() {
        inicio = System.currentTimeMillis();
    }

    /**
     * Devuelve la duración de la tarea.
     *
     * @return milisegundos que tarda
     */
    public int getMs() {
        return ms;
    }

    /**
     * Trabajo del hilo: esperar a las dependencias, trabajar e informar.
     * Si el hilo es interrumpido, avisa y termina sin hacer su trabajo.
     */
    @Override
    public void run() {
        try {
            for (Thread dependencia : dependencias) {
                esperarA(dependencia);
            }
            System.out.printf("Empieza %s (%d ms)%n", getName(), transcurrido());
            Thread.sleep(ms);                                     // simula el trabajo
            System.out.printf("Termina %s (%d ms)%n", getName(), transcurrido());
        } catch (InterruptedException e) {
            System.out.println(getName() + " interrumpida: no se completa");
        }
    }

    /**
     * Espera a que termine una dependencia, aunque todavía no la hayan arrancado.
     *
     * join() sobre un hilo que aún no ha hecho start() (estado NEW) vuelve AL INSTANTE, sin esperar:
     * si el main arranca las tareas en orden inverso, T7 podría pasar por delante de T6. Por eso,
     * primero se espera (en pasos de 1 ms) a que la dependencia deje de estar en NEW, y después join().
     *
     * @param dependencia tarea que tiene que terminar antes
     * @throws InterruptedException si este hilo es interrumpido mientras espera
     */
    private static void esperarA(Thread dependencia) throws InterruptedException {
        while (dependencia.getState() == Thread.State.NEW) {
            Thread.sleep(1);
        }
        dependencia.join();
    }

    /**
     * Milisegundos transcurridos desde el inicio del despliegue.
     *
     * @return ms desde {@link #marcarInicio()}
     */
    private static long transcurrido() {
        return System.currentTimeMillis() - inicio;
    }
}
