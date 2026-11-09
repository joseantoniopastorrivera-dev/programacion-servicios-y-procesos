/*
 * UT2 · Relación de ejercicios · Ejercicio 2.2 – El examen (ampliación)
 *
 * Cinco alumnos hacen el examen a la vez, con los mismos 5 segundos para todos.
 *
 * SOLUCIÓN. La clave es una HORA LÍMITE común. Si se hiciera join(5000) a cada alumno, uno detrás de otro,
 * el último podría tener hasta 5 × 5 = 25 s. Por eso a cada join se le da solo el tiempo que QUEDA.
 */
package bloque2.examen;

public class ExamenVariosAlumnos {

    static final long TIEMPO_MS = 5000;

    public static void main(String[] args) throws InterruptedException {
        String[] nombres = {"Ana", "Luis", "Marta", "Pablo", "Sara"};
        Alumno[] alumnos = new Alumno[nombres.length];
        Thread[] hilos = new Thread[nombres.length];

        long limite = System.currentTimeMillis() + TIEMPO_MS;      // hora a la que se recogen TODOS los exámenes
        for (int i = 0; i < nombres.length; i++) {
            alumnos[i] = new Alumno(nombres[i]);
            hilos[i] = new Thread(alumnos[i], nombres[i]);
            hilos[i].start();                                      // los cinco empiezan a la vez
        }

        for (Thread h : hilos) {
            long queda = limite - System.currentTimeMillis();      // tiempo que falta hasta la hora límite
            if (queda > 0) {                                       // ¡importante! join(0) significa "espera SIN límite"
                h.join(queda);
            }
        }
        // Aquí han pasado como mucho 5 s desde el inicio (más unos milisegundos).

        for (Thread h : hilos) {
            if (h.isAlive()) {
                h.interrupt();                                     // se retira el examen a los que no han terminado
            }
        }
        for (Thread h : hilos) {
            h.join();                                              // espera a que todos dejen de escribir
        }

        for (Alumno a : alumnos) {
            System.out.printf("%-6s %2d/%d preguntas  %s%n", a.getNombre(), a.getContestadas(), Alumno.PREGUNTAS,
                    a.terminoATiempo() ? "a tiempo" : "examen retirado");
        }
    }
}
