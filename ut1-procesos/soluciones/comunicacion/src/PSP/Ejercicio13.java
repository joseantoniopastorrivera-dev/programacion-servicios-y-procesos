package PSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 13. Tubería de procesos con ProcessBuilder.startPipeline().
 *   tr -cs 'A-Za-z' '\n' < datos/texto.txt | sort -u | wc -l
 *   Linux   : tr, sort, wc, uniq y head del sistema
 *   Windows : los de Git para Windows (C:/Program Files/Git/usr/bin/...)
 * Reflexión: sale 11 y no 9 porque sort -u distingue mayúsculas: «El»/«el» y «Un»/«un» cuentan doble.
 * El padre solo lee del ÚLTIMO proceso: los demás escriben en la tubería, no en Java.
 */
public class Ejercicio13 {

    public static void main(String[] args) throws IOException, InterruptedException {
        File texto = new File("datos/texto.txt");

        // Parte básica
        ProcessBuilder tr = new ProcessBuilder(h("tr"), "-cs", "A-Za-z", "\n");
        tr.redirectInput(texto);                                  // el primero lee del fichero
        List<ProcessBuilder> tuberia = new ArrayList<ProcessBuilder>();
        tuberia.add(tr);
        tuberia.add(new ProcessBuilder(h("sort"), "-u"));
        tuberia.add(new ProcessBuilder(h("wc"), "-l"));
        List<Process> procesos = ProcessBuilder.startPipeline(tuberia);   // salida de uno → entrada del siguiente
        String distintas = leerTodo(procesos.get(procesos.size() - 1)).trim();
        esperarTodos(procesos);
        System.out.println("Palabras distintas: " + distintas);

        // Ampliación: las 5 más frecuentes sin distinguir mayúsculas
        ProcessBuilder tr2 = new ProcessBuilder(h("tr"), "-cs", "A-Za-z", "\n");
        tr2.redirectInput(texto);
        List<ProcessBuilder> tuberia2 = new ArrayList<ProcessBuilder>();
        tuberia2.add(tr2);
        tuberia2.add(new ProcessBuilder(h("tr"), "A-Z", "a-z"));
        tuberia2.add(new ProcessBuilder(h("sort")));
        tuberia2.add(new ProcessBuilder(h("uniq"), "-c"));
        tuberia2.add(new ProcessBuilder(h("sort"), "-rn"));
        tuberia2.add(new ProcessBuilder(h("head"), "-5"));
        List<Process> procesos2 = ProcessBuilder.startPipeline(tuberia2);
        System.out.println("\nLas 5 palabras más frecuentes:");
        System.out.print(leerTodo(procesos2.get(procesos2.size() - 1)));
        esperarTodos(procesos2);
    }

    /** Nombre de la herramienta: el del sistema en Linux, el de Git en Windows. */
    private static String h(String herramienta) {
        return SO.WINDOWS ? SO.git(herramienta) : herramienta;
    }

    private static String leerTodo(Process p) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea).append("\n");
        }
        br.close();
        return sb.toString();
    }

    private static void esperarTodos(List<Process> procesos) throws InterruptedException {
        for (Process p : procesos) {
            p.waitFor();
        }
    }
}
