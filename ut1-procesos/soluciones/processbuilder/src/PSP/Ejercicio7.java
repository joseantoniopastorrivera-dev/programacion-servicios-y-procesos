package PSP;

import java.io.IOException;

/**
 * Ejercicio 7. Secuencial frente a paralelo con sleep 3, sleep 5 y sleep 1.
 *
 *   Linux   : sleep N
 *   Windows : powershell -Command Start-Sleep N   (no hay sleep; «timeout» no sirve sin consola)
 * Secuencial ≈ 3 + 5 + 1 = 9 s. Paralelo ≈ el más lento = 5 s.
 * En Windows sale algo más por lo que tarda en arrancar cada PowerShell.
 */
public class Ejercicio7 {

    static final int[] SEGUNDOS = {3, 5, 1};

    public static void main(String[] args) throws IOException, InterruptedException {
        // ---------- Versión A: secuencial (lanzar, esperar, lanzar el siguiente) ----------
        long inicio = System.currentTimeMillis();
        for (int s : SEGUNDOS) {
            Process p = new ProcessBuilder(SO.dormir(s)).start();
            p.waitFor();                                         // no se lanza el siguiente hasta que este termina
        }
        double tA = (System.currentTimeMillis() - inicio) / 1000.0;
        System.out.printf("Versión A (secuencial): %.2f s%n", tA);

        // ---------- Versión B: paralela (lanzar los tres y después esperar a los tres) ----------
        inicio = System.currentTimeMillis();
        Process[] procesos = new Process[SEGUNDOS.length];
        for (int i = 0; i < SEGUNDOS.length; i++) {
            procesos[i] = new ProcessBuilder(SO.dormir(SEGUNDOS[i])).start();   // los tres corren a la vez
        }
        for (Process p : procesos) {
            p.waitFor();
        }
        double tB = (System.currentTimeMillis() - inicio) / 1000.0;
        System.out.printf("Versión B (paralela):   %.2f s%n", tB);
    }
}
