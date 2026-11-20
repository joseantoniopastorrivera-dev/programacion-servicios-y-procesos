/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

/** Tarea 4. Sumador: suma los números que recibe por args y escribe el resultado en su salida. */
public class Sumador {

    /**
     * Suma los números que recibe y escribe el resultado por su salida estándar.
     *
     * @param args los números que se suman
     */
    public static void main(String[] args) {
        long suma = 0;
        for (String a : args) {
            suma += Long.parseLong(a);
        }
        System.out.println(suma);
    }
}
