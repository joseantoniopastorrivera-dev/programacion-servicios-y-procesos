/*
 * ============================================================
 *  Solución de repaso (no es la entrega de ningún alumno).
 *  El enunciado oficial no venía en el zip: está reconstruido en
 *  ../../../../enunciado-reconstruido.md
 * ============================================================
 */

package PSP.c2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio C2.1: proceso hijo que calcula las estadísticas de un grupo.
 *
 * Lee una nota por línea de su ENTRADA ESTÁNDAR (el padre le conecta el fichero).
 * - Por stdout escribe UNA línea con el resultado:  nombre;validas;suma;media;maxima;minima
 * - Por stderr avisa de cada línea que no es una nota válida (0 a 10).
 * - Código de salida: 0 = hay resultado · 1 = ninguna nota válida · 2 = falta el argumento.
 *
 * Argumento: <nombreDelGrupo>
 */
public class HijoEstadistica {

    public static void main(String[] args) throws IOException {

        // --- 1. Comprobar el argumento ---------------------------------------
        if (args.length != 1) {
            System.err.println("Uso: HijoEstadistica <nombreDelGrupo>");
            System.exit(2);
        }
        String grupo = args[0];                           // solo sirve para etiquetar la salida y los avisos

        // --- 2. Acumuladores --------------------------------------------------
        int validas = 0;                                  // cuántas notas correctas llevo
        double suma = 0;                                  // suma de las notas correctas
        double maxima = Double.NEGATIVE_INFINITY;         // cualquier nota real será mayor
        double minima = Double.POSITIVE_INFINITY;         // cualquier nota real será menor
        int numLinea = 0;                                 // para decir en qué línea está el fallo

        // --- 3. Leer la entrada estándar línea a línea ------------------------
        // System.in da bytes; InputStreamReader los pasa a caracteres; BufferedReader añade readLine().
        // El hijo no sabe ni le importa si detrás hay un teclado, una tubería o un fichero.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linea;
        while ((linea = br.readLine()) != null) {         // null = fin de la entrada (se acabó el fichero)
            numLinea++;
            linea = linea.trim();                         // quita espacios y el \r de los ficheros de Windows
            if (linea.isEmpty()) {
                continue;                                 // las líneas en blanco se saltan sin avisar
            }
            try {
                double nota = Double.parseDouble(linea);  // "siete" o "NP" lanzan NumberFormatException
                if (nota < 0 || nota > 10) {              // es un número, pero no una nota posible
                    System.err.println("[" + grupo + "] línea " + numLinea + ": nota fuera de rango: " + linea);
                    continue;
                }
                validas++;
                suma += nota;
                if (nota > maxima) maxima = nota;
                if (nota < minima) minima = nota;
            } catch (NumberFormatException e) {
                // Los avisos van por stderr para NO ensuciar stdout, que es lo que lee el padre.
                System.err.println("[" + grupo + "] línea " + numLinea + ": no es un número: " + linea);
            }
        }

        // --- 4. Sin notas válidas no hay media: se avisa con el código de salida
        if (validas == 0) {
            System.err.println("[" + grupo + "] no hay ninguna nota válida");
            System.exit(1);                               // el padre lo sabrá por waitFor()
        }

        // --- 5. Resultado por stdout, en un formato fácil de trocear ----------
        // Se concatena en vez de usar String.format("%.2f"): en un sistema en español
        // format escribiría "7,25" con coma y el Double.parseDouble del padre fallaría.
        double media = suma / validas;
        System.out.println(grupo + ";" + validas + ";" + suma + ";" + media + ";" + maxima + ";" + minima);
        // Al terminar el main sin System.exit, el código de salida es 0.
    }
}
