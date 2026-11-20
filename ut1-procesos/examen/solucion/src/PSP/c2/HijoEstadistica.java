/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Examen RA1 (09/11/2026) · Solución de repaso
 *  Enunciado: ../../../../examen-ut1.md
 * ============================================================
 */

package PSP.c2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Ejercicio C2.1 · Proceso hijo que calcula las estadísticas de un grupo.
 *
 * Lee una nota por línea de su ENTRADA ESTÁNDAR (no sabe si viene de un teclado o de un fichero).
 * <ul>
 *   <li>Cada línea que no es una nota válida: aviso por stderr con el grupo y el número de línea.</li>
 *   <li>Al terminar, por stdout, exactamente: CANTIDAD, MEDIA (2 decimales), MAXIMA y MINIMA.</li>
 *   <li>Sin ninguna nota válida: aviso por stderr, sin resultados y código de salida 2.</li>
 * </ul>
 * Códigos de salida: 0 = correcto · 1 = falta el argumento · 2 = ninguna nota válida.
 *
 * Argumento: {@code <nombreDelGrupo>}
 */
public class HijoEstadistica {

    /**
     * Punto de entrada: lee las notas de stdin, las valida y escribe las cuatro líneas de resultado.
     *
     * @param args nombre del grupo (solo se usa para identificar los avisos)
     * @throws IOException si falla la lectura de la entrada estándar
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Uso: HijoEstadistica <nombreDelGrupo>");
            System.exit(1);
        }
        String grupo = args[0];

        int cantidad = 0;                                // notas válidas
        double suma = 0;
        double maxima = Double.NEGATIVE_INFINITY;        // cualquier nota real será mayor
        double minima = Double.POSITIVE_INFINITY;        // cualquier nota real será menor

        // System.in (bytes) → InputStreamReader (caracteres) → BufferedReader (readLine)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linea;
        int numLinea = 0;
        while ((linea = br.readLine()) != null) {        // null = fin de la entrada (fin del fichero)
            numLinea++;
            Double nota = convertirNota(linea);
            if (nota == null) {
                // Los avisos van por stderr para no ensuciar stdout, que es lo que lee el padre.
                System.err.println(grupo + ", línea " + numLinea + ": no es una nota válida: \"" + linea.trim() + "\"");
                continue;                                // se ignora
            }
            cantidad++;
            suma += nota;
            maxima = Math.max(maxima, nota);
            minima = Math.min(minima, nota);
        }

        if (cantidad == 0) {
            System.err.println(grupo + ": no hay ninguna nota válida");
            System.exit(2);                              // el enunciado pide EXACTAMENTE el código 2
        }

        // Exactamente cuatro líneas. Locale.ROOT: punto decimal siempre ("7.25", no "7,25"),
        // así el padre puede leerla con Double.parseDouble en cualquier sistema.
        System.out.println("CANTIDAD " + cantidad);
        System.out.println("MEDIA " + String.format(Locale.ROOT, "%.2f", suma / cantidad));
        System.out.println("MAXIMA " + maxima);
        System.out.println("MINIMA " + minima);
    }                                                    // fin del main sin exit → código 0

    /**
     * Convierte una línea en nota si es válida: un número con punto decimal opcional entre 0 y 10.
     *
     * @param linea texto leído (puede tener espacios o el \r de un fichero de Windows)
     * @return la nota, o {@code null} si la línea no es una nota válida
     */
    static Double convertirNota(String linea) {
        String texto = linea.trim();
        // Solo dígitos y, opcionalmente, un punto con más dígitos: "7", "6.25", "10".
        // Así se rechazan "siete", "NP", líneas vacías, "-1", "7,5" y cosas que parseDouble
        // aceptaría pero no son notas: "NaN", "1e1", "7d".
        if (!texto.matches("\\d+(\\.\\d+)?")) {
            return null;
        }
        double nota = Double.parseDouble(texto);
        return (nota >= 0 && nota <= 10) ? nota : null;  // "11" es un número, pero no una nota
    }
}
