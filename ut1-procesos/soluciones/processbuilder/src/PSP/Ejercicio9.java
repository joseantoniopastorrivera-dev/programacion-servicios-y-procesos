/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Ejercicio 9. Tiempo límite: sleep 10, esperar como máximo 3 s y destruirlo.
 *
 *   Linux   : destroy() envía SIGTERM (señal 15) → código de salida 143 = 128 + 15
 *   Windows : destroy() llama a TerminateProcess (no hay señales) → código de salida 1
 */
public class Ejercicio9 {

    /**
     * Lanza sleep 10, lo espera como máximo 3 s con waitFor(tiempo) y, si no ha terminado, lo destruye.
     *
     * @param args no se usan
     * @throws IOException          si no se puede lanzar el proceso
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Process p = new ProcessBuilder(SO.dormir(10)).start();
        // waitFor(tiempo, unidad) NO espera para siempre: devuelve true si terminó, false si se agotó el tiempo.
        boolean terminado = p.waitFor(3, TimeUnit.SECONDS);
        System.out.println("¿Terminó en 3 s?: " + terminado);
        if (!terminado) {
            System.out.println("Antes de destroy(). ¿Vivo?: " + p.isAlive());
            p.destroy();                    // PIDE que termine
            p.waitFor();                    // espera a que termine de verdad (destroy no espera)
            System.out.println("Después de destroy(). ¿Vivo?: " + p.isAlive());
        }
        System.out.println("Código de salida: " + p.exitValue());
    }
}
