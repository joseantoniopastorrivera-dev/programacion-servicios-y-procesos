/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

/**
 * Hijo del ejercicio 14: cuenta los primos de un rango y escribe solo el resultado.
 */
public class HijoPrimos {
    /**
     * Cuenta los primos entre dos números (ambos incluidos) y escribe el total por su salida estándar.
     *
     * @param args el principio y el final del rango
     */
    public static void main(String[] args) {
        if (args.length != 2) {                                  // sin el rango no se puede contar
            System.err.println("Uso: HijoPrimos <desde> <hasta>");
            System.exit(1);
        }
        int desde = Integer.parseInt(args[0]);
        int hasta = Integer.parseInt(args[1]);

        int contador = 0;
        for (int n = desde; n <= hasta; n++) {
            if (esPrimo(n)) {
                contador++;
            }
        }
        System.out.println(contador);   // solo el resultado: el padre lo lee
    }

    /**
     * Indica si un número es primo, probando divisores hasta su raíz cuadrada.
     *
     * @param n número a comprobar
     * @return true si es primo
     */
    private static boolean esPrimo(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
