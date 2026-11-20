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
import java.lang.ProcessBuilder.Redirect;

/**
 * Ejercicio 12. Diario de actividad: AÑADIR (no sobrescribir) con Redirect.appendTo().
 *   Linux   : date "+=== %d/%m/%Y %H:%M:%S ==="   y   ls -l
 *   Windows : powershell Get-Date -Format "=== dd/MM/yyyy HH:mm:ss ==="   y   cmd /c dir
 */
public class Ejercicio12 {

    /**
     * Añade al diario de actividad la salida de un comando con Redirect.appendTo, sin borrar lo anterior.
     *
     * @param args no se usan
     * @throws IOException          si no se puede lanzar el proceso
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        File diario = new File("diario.log");
        String[] fecha = SO.comando(
                new String[]{"date", "+=== %d/%m/%Y %H:%M:%S ==="},
                new String[]{"powershell", "-NoProfile", "-Command", "Get-Date -Format '=== dd/MM/yyyy HH:mm:ss ==='"});
        String[] listado = SO.comando(new String[]{"ls", "-l"}, new String[]{"cmd", "/c", "dir"});

        ProcessBuilder pbFecha = new ProcessBuilder(fecha);
        pbFecha.redirectOutput(Redirect.appendTo(diario));     // como «>>»: añade al final
        pbFecha.start().waitFor();
        ProcessBuilder pbLs = new ProcessBuilder(listado);
        pbLs.redirectOutput(Redirect.appendTo(diario));
        pbLs.start().waitFor();

        BufferedReader br = new BufferedReader(new FileReader(diario));
        int entradas = 0;
        String ultima = "";
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.startsWith("===")) {
                entradas++;
                ultima = linea;
            }
        }
        br.close();
        System.out.println("Entrada añadida: " + ultima);
        System.out.println("El diario tiene " + entradas + " entradas (" + diario.length() + " bytes)");
    }
}
