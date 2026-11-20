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
 * Tarea 4 (primera parte). RunAndWait: lanza Sumador con sus números, guarda el resultado en un .txt
 * (redirectOutput, sin leerlo desde Java), espera al hijo y muestra qué comando lanzó y qué devolvió.
 * Igual en Linux y en Windows.
 *
 *   java -cp bin PSP.RunAndWait resultado.txt 3 4 5
 */
public class RunAndWait {

    /**
     * Lanza Sumador con los números recibidos, guarda su salida en un fichero, espera y muestra el resultado.
     *
     * @param args el fichero de resultado y uno o más números
     * @throws IOException          si no se puede lanzar el proceso o leer el fichero
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            System.err.println("Uso: java -cp bin PSP.RunAndWait <fichero.txt> <número> [número...]");
            System.exit(1);
        }
        File salida = new File(args[0]);
        String[] comando = new String[4 + args.length - 1];      // java -cp bin PSP.Sumador + los números
        comando[0] = "java";
        comando[1] = "-cp";
        comando[2] = "bin";
        comando[3] = "PSP.Sumador";
        System.arraycopy(args, 1, comando, 4, args.length - 1);

        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.redirectOutput(salida);                               // la salida del hijo va directa al fichero
        Process p = pb.start();
        int codigo = p.waitFor();                                // el padre ESPERA al hijo

        System.out.println("Comando lanzado: " + String.join(" ", comando));
        System.out.println("Código de salida: " + codigo);
        System.out.println("Resultado (leído de " + salida.getName() + "): " + leerPrimeraLinea(salida));
    }

    /**
     * Lee la primera línea de un fichero.
     *
     * @param f fichero que leer
     * @return su primera línea, o null si está vacío
     * @throws IOException si no se puede leer
     */
    static String leerPrimeraLinea(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea = br.readLine();
        br.close();
        return linea;
    }
}
