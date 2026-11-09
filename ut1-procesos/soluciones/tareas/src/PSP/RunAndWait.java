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

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            System.out.println("Uso: java -cp bin PSP.RunAndWait <fichero.txt> <número> [número...]");
            return;
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

    static String leerPrimeraLinea(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea = br.readLine();
        br.close();
        return linea;
    }
}
