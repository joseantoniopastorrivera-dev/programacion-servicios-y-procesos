/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

/** Ejercicio 8 (hijo): termina con el código de salida que recibe por args. */
public class Hijo {

    /**
     * Termina con el código de salida que recibe.
     *
     * @param args el código de salida (un entero)
     */
    public static void main(String[] args) {
        if (args.length != 1) {                                  // sin código no hay nada que devolver
            System.err.println("Uso: Hijo <codigoDeSalida>");
            System.exit(1);
        }
        int codigo = Integer.parseInt(args[0]);
        System.exit(codigo);                 // el código de salida es lo que el padre recibe con waitFor()
    }
}
