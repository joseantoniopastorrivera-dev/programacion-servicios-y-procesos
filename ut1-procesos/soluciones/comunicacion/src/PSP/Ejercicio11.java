/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Ejercicio 11. Ordenar notas con sort SIN leer ni escribir los datos desde Java (redirecciones).
 *   Linux   : sort -t, -k2 -rn          (terminal: {@code sort -t, -k2 -rn < datos/notas.csv > datos/notas_ordenadas.csv})
 *   Windows : el sort de Windows no ordena por campos → el de Git: C:/Program Files/Git/usr/bin/sort.exe
 * Reflexión: sin la «n» se ordena como TEXTO: {@code "10" < "5.1"} porque compara carácter a carácter ({@code '1' < '5'}).
 */
public class Ejercicio11 {

    /**
     * Ordena el fichero de notas con sort usando solo redirecciones: Java no lee ni escribe los datos.
     *
     * @param args no se usan
     * @throws IOException          si no se puede lanzar el proceso
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        File entrada = new File("datos/notas.csv");
        File salida = new File("datos/notas_ordenadas.csv");
        String sort = SO.WINDOWS ? SO.git("sort") : "sort";
        ProcessBuilder pb = new ProcessBuilder(sort, "-t,", "-k2", "-rn");
        pb.redirectInput(entrada);       // la entrada del hijo es el fichero (como «< notas.csv»)
        pb.redirectOutput(salida);       // su salida va al fichero       (como «> notas_ordenadas.csv»)
        int codigo = pb.start().waitFor();
        System.out.println("sort terminado con código " + codigo);

        BufferedReader br = new BufferedReader(new FileReader(salida));
        String[] medallas = {"Oro", "Plata", "Bronce"};
        String linea;
        int puesto = 0;
        while (puesto < 3 && (linea = br.readLine()) != null) {
            String[] campos = linea.split(",");
            System.out.println(medallas[puesto] + ": " + campos[0] + " (" + campos[1] + ")");
            puesto++;
        }
        br.close();
        System.out.println("Clasificación completa en " + salida.getPath());
    }
}
