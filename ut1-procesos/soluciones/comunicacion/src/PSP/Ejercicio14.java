/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 14. Contar primos repartiendo el rango entre tres procesos HijoPrimos:
 * primero en secuencial (lanzar, esperar y lanzar el siguiente) y después en paralelo
 * (lanzar todos y recoger después), comparando los tiempos.
 */
public class Ejercicio14 {

    static final int[][] RANGOS = {
        {1, 3000000},
        {3000001, 6000000},
        {6000001, 9000000}
    };

    /**
     * Cuenta los primos de los tres rangos en secuencial y en paralelo y compara los tiempos.
     *
     * @param args no se usan
     * @throws IOException          si no se puede lanzar un hijo o leer su salida
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        // ---------- Secuencial: lanzar, esperar, lanzar el siguiente ----------
        long inicio = System.currentTimeMillis();
        int total = 0;
        for (int i = 0; i < RANGOS.length; i++) {
            Process p = lanzar(RANGOS[i]);
            total += leerResultado(p);
        }
        long tSecuencial = System.currentTimeMillis() - inicio;
        System.out.println("Secuencial: " + total + " primos en " + tSecuencial + " ms");

        // ---------- Paralelo: lanzar todos, después recoger ----------
        inicio = System.currentTimeMillis();
        Process[] hijos = new Process[RANGOS.length];
        for (int i = 0; i < RANGOS.length; i++) {
            hijos[i] = lanzar(RANGOS[i]);
        }
        total = 0;
        for (int i = 0; i < hijos.length; i++) {
            int parcial = leerResultado(hijos[i]);
            System.out.println("  Hijo " + (i + 1) + " [" + RANGOS[i][0] + "-" + RANGOS[i][1] + "]: " + parcial);
            total += parcial;
        }
        long tParalelo = System.currentTimeMillis() - inicio;
        System.out.println("Paralelo:   " + total + " primos en " + tParalelo + " ms");

        System.out.printf("El paralelo es %.1f veces más rápido%n", (double) tSecuencial / tParalelo);
    }

    /**
     * Lanza un HijoPrimos para un rango de números.
     *
     * @param rango {desde, hasta}
     * @return el proceso del hijo, ya arrancado
     * @throws IOException si no se puede lanzar el proceso
     */
    private static Process lanzar(int[] rango) throws IOException {
        return new ProcessBuilder("java", "-cp", "bin", "PSP.HijoPrimos",
                String.valueOf(rango[0]), String.valueOf(rango[1])).start();
    }

    /**
     * Lee el número de primos que escribe el hijo y espera a que termine (primero leer, después waitFor).
     *
     * @param p proceso del hijo
     * @return número de primos de su rango
     * @throws IOException          si falla la lectura
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    private static int leerResultado(Process p) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        int resultado = Integer.parseInt(br.readLine().trim());
        br.close();
        p.waitFor();
        return resultado;
    }
}
