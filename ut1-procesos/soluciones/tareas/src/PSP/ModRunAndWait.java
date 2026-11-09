package PSP;

import java.io.File;
import java.io.IOException;

/**
 * Tarea 4 (segunda parte). ModRunAndWait: lanza TRES Sumador, cada uno guarda su resultado en un .txt,
 * y el padre lee los tres ficheros y muestra la suma total. Igual en Linux y en Windows.
 *
 * Argumentos: un nombre base y tres grupos de números separados por comas.
 *   java -cp bin PSP.ModRunAndWait parcial 1,2,3 10,20 100,200,300
 *   → crea parcial1.txt, parcial2.txt y parcial3.txt y suma 6 + 30 + 600 = 636
 */
public class ModRunAndWait {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 4) {
            System.out.println("Uso: java -cp bin PSP.ModRunAndWait <nombreBase> <n,n,...> <n,n,...> <n,n,...>");
            return;
        }
        String base = args[0];
        Process[] hijos = new Process[3];
        File[] ficheros = new File[3];

        // 1) Lanzar los tres a la vez (en paralelo)
        for (int i = 0; i < 3; i++) {
            String[] numeros = args[i + 1].split(",");
            String[] comando = new String[4 + numeros.length];
            comando[0] = "java";
            comando[1] = "-cp";
            comando[2] = "bin";
            comando[3] = "PSP.Sumador";
            System.arraycopy(numeros, 0, comando, 4, numeros.length);
            ficheros[i] = new File(base + (i + 1) + ".txt");
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.redirectOutput(ficheros[i]);
            hijos[i] = pb.start();
            System.out.println("Lanzado: " + String.join(" ", comando) + "  >  " + ficheros[i].getName());
        }

        // 2) Esperar a los tres y 3) leer sus ficheros para sumar
        long total = 0;
        for (int i = 0; i < 3; i++) {
            int codigo = hijos[i].waitFor();                 // hay que esperar: si no, el fichero puede estar vacío
            long parcial = Long.parseLong(RunAndWait.leerPrimeraLinea(ficheros[i]).trim());
            System.out.println(ficheros[i].getName() + " = " + parcial + " (código " + codigo + ")");
            total += parcial;
        }
        System.out.println("Suma total: " + total);
    }
}
