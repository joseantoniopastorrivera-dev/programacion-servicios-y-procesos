/*
 * UT2 · Relación de ejercicios · Ejercicio 2.3 – Autoguardado
 *
 * El usuario escribe durante 4 segundos y cierra el editor.
 *
 * SOLUCIÓN. Un programa Java termina cuando terminan todos sus hilos NO daemon. El autoguardado nunca
 * termina, así que, si fuera un hilo normal, el programa no acabaría nunca. Con setDaemon(true) se le
 * declara "hilo de servicio": la JVM no lo espera y lo corta en cuanto termina el main.
 */
package bloque2.autoguardado;

public class Editor {

    public static void main(String[] args) throws InterruptedException {
        StringBuilder documento = new StringBuilder();

        Thread autoguardado = new Thread(new Autoguardado(documento), "autoguardado");
        autoguardado.setDaemon(true);           // ¡ANTES de start()! Después lanza IllegalThreadStateException
        autoguardado.start();

        String[] palabras = {"Había", "una", "vez", "un", "editor", "que", "guardaba", "cada",
                "segundo", "sin", "que", "nadie", "se", "lo"};
        long fin = System.currentTimeMillis() + 4000;       // el usuario escribe durante 4 s
        int i = 0;
        while (System.currentTimeMillis() < fin) {
            Thread.sleep(300);                              // una palabra cada 300 ms
            synchronized (documento) {                      // mismo cerrojo que el autoguardado
                documento.append(palabras[i % palabras.length]).append(' ');
            }
            i++;
            System.out.println("El usuario escribe: " + palabras[(i - 1) % palabras.length]);
        }

        System.out.println("El usuario cierra el editor (" + i + " palabras). Fin del programa.");
        // Aquí termina el main. Como el único hilo que queda vivo es daemon, la JVM termina y lo corta.

        // OTRA FORMA (sin daemon): autoguardado.interrupt(); → el sleep() del autoguardado lanza
        // InterruptedException y su catch hace return. Esta forma es más limpia porque el hilo termina
        // por sí mismo (por ejemplo, podría hacer un último guardado antes de salir); el daemon se corta de golpe.
    }
}
