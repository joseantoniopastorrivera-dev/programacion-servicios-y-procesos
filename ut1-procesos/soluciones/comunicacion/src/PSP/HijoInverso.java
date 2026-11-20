/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.util.Scanner;

/**
 * Hijo del ejercicio 9: devuelve invertida cada línea que recibe por su entrada estándar.
 */
public class HijoInverso {
    /**
     * Lee líneas de la entrada estándar y escribe cada una al revés, hasta el fin de la entrada.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String linea = sc.nextLine();
            String invertida = new StringBuilder(linea).reverse().toString();
            System.out.println(invertida);
        }
        sc.close();
    }
}
