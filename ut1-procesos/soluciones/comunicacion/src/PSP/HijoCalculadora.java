/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.util.Scanner;

/**
 * Hijo del ejercicio 10: calculadora con protocolo. Lee líneas «a op b» y contesta el resultado
 * o «ERROR»; termina al recibir «fin».
 */
public class HijoCalculadora {
    /**
     * Atiende operaciones de la entrada estándar hasta recibir «fin» o el fin de la entrada.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String linea = sc.nextLine().trim();
            if (linea.equals("fin")) {
                break;
            }
            System.out.println(calcular(linea));
        }
        sc.close();
    }

    // Recibe "a op b" y devuelve el resultado o "ERROR"
    /**
     * Calcula una operación «a op b».
     *
     * @param operacion texto con dos números y un operador (+, -, *, /) separados por espacios
     * @return el resultado, o «ERROR» si la operación no es válida
     */
    private static String calcular(String operacion) {
        String[] partes = operacion.split("\\s+");
        if (partes.length != 3) {
            return "ERROR";
        }
        try {
            double a = Double.parseDouble(partes[0]);
            double b = Double.parseDouble(partes[2]);
            switch (partes[1]) {
                case "+": return String.valueOf(a + b);
                case "-": return String.valueOf(a - b);
                case "*": return String.valueOf(a * b);
                case "/":
                    if (b == 0) {
                        return "ERROR";
                    }
                    return String.valueOf(a / b);
                default: return "ERROR";
            }
        } catch (NumberFormatException e) {
            return "ERROR";
        }
    }
}
