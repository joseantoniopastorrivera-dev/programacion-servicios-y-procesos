/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 5. Buscador con grep: palabra y fichero por args (p. ej. gato datos/texto.txt).
 *   Linux   : {@code grep -n <palabra> <fichero>}    códigos: 0 encontrado · 1 no encontrado · 2 error
 *   Windows : {@code findstr /n <palabra> <fichero>} códigos: 0 encontrado · 1 no encontrado O error
 *             (findstr no distingue «no está» de «fichero inexistente» por el código: se mira stderr)
 * Las dos herramientas sacan «número:línea», así que el resto del programa es igual.
 */
public class Ejercicio05 {

    /**
     * Busca una palabra en un fichero con grep (findstr en Windows) e interpreta su código de salida.
     *
     * @param args la palabra y el fichero
     * @throws IOException          si no se puede lanzar el proceso o leer su salida
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            System.err.println("Uso: java -cp bin PSP.Ejercicio05 <palabra> <fichero>");   // errores por stderr
            System.exit(1);                                                                 // y código distinto de 0
        }
        String palabra = args[0];
        String fichero = args[1];
        String[] comando = SO.comando(
                new String[]{"grep", "-n", palabra, fichero},
                // findstr toma la «/» como inicio de una opción: la ruta va con el separador de Windows.
                new String[]{"findstr", "/n", palabra, fichero.replace('/', File.separatorChar)});
        Process p = new ProcessBuilder(comando).start();

        BufferedReader salida = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String linea;
        int encontradas = 0;
        while ((linea = salida.readLine()) != null) {
            int dosPuntos = linea.indexOf(':');                  // "3:Un gato, un perro..." → número y texto
            System.out.println("Línea " + linea.substring(0, dosPuntos) + ": " + linea.substring(dosPuntos + 1));
            encontradas++;
        }
        BufferedReader errores = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String error = errores.readLine();                       // null si no hubo errores
        int codigo = p.waitFor();

        if (codigo == 0) {
            System.out.println("\"" + palabra + "\" aparece en " + encontradas + " líneas");
        } else if (codigo == 1 && error == null) {
            System.out.println("\"" + palabra + "\" no aparece en " + fichero);
        } else {
            System.out.println("Error (código " + codigo + "): " + error);
        }
    }
}
