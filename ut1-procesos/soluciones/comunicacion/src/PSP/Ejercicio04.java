package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 4. Los 5 ficheros más grandes de una carpeta.
 *   Linux   : ls -S /usr/bin                         (ordenados por tamaño, de mayor a menor)
 *   Windows : cmd /c dir /b /o-s C:\Windows\System32  (/b = solo nombres, /o-s = por tamaño descendente)
 */
public class Ejercicio04 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String carpeta = args.length > 0 ? args[0] : (SO.WINDOWS ? "C:\\Windows\\System32" : "/usr/bin");
        String[] comando = SO.comando(
                new String[]{"ls", "-S", carpeta},
                new String[]{"cmd", "/c", "dir", "/b", "/o-s", "/a-d", carpeta});   // /a-d = sin carpetas
        Process p = new ProcessBuilder(comando).start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        System.out.println("Los 5 ficheros más grandes de " + carpeta + ":");
        int leidas = 0;
        String linea;
        while (leidas < 5 && (linea = br.readLine()) != null) {   // dejamos de leer al llegar a 5
            leidas++;
            System.out.println(leidas + ". " + linea);
        }
        if (p.isAlive()) {                                     // ya no nos interesa el resto
            System.out.println("(el hijo sigue vivo, lo terminamos)");
            p.destroy();
        }
        br.close();
        System.out.println("Código de salida: " + p.waitFor());
    }
}
