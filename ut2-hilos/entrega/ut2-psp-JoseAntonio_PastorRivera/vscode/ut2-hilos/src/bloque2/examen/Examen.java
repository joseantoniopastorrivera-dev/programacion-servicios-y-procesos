/*
 * UT2 · Relación de ejercicios · Ejercicio 2.2 – El examen
 *
 * El profesor da 5 segundos para el examen.
 */
package bloque2.examen;

public class Examen {

    static final long TIEMPO_MS = 5000;

    public static void main(String[] args) throws InterruptedException {
        Alumno alumno = new Alumno("Ana");
        Thread hilo = new Thread(alumno, alumno.getNombre());
        hilo.start();                           // empieza el examen

        hilo.join(TIEMPO_MS);                   // el profesor espera COMO MÁXIMO 5 s a que termine
        // join(ms) vuelve en dos casos: el alumno ha terminado (antes de 5 s) o se ha agotado el tiempo.
        // Para saber cuál de los dos ha sido, se pregunta isAlive().

        if (hilo.isAlive()) {                   // sigue vivo → no ha terminado en 5 s
            hilo.interrupt();                   // le retira el examen: marca la interrupción y despierta su sleep()
            hilo.join();                        // espera a que el alumno suelte el bolígrafo (termine run())
        }
        // Tras join() es seguro leer contestadas y aTiempo: todo lo que escribió el hilo es visible para el main.

        System.out.printf("%s ha contestado %d de %d preguntas. %s%n",
                alumno.getNombre(), alumno.getContestadas(), Alumno.PREGUNTAS,
                alumno.terminoATiempo() ? "Terminó a tiempo." : "NO terminó a tiempo: se le retiró el examen.");
        // De media son 20 × 400 ms = 8 s > 5 s, así que lo normal es que NO termine (unas 11-13 preguntas).
    }
}
