package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio14 {

    static final int[][] RANGOS = {
        {1, 3000000},
        {3000001, 6000000},
        {6000001, 9000000}
    };

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

    private static Process lanzar(int[] rango) throws IOException {
        return new ProcessBuilder("java", "-cp", "bin", "PSP.HijoPrimos",
                String.valueOf(rango[0]), String.valueOf(rango[1])).start();
    }

    private static int leerResultado(Process p) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        int resultado = Integer.parseInt(br.readLine().trim());
        br.close();
        p.waitFor();
        return resultado;
    }
}
