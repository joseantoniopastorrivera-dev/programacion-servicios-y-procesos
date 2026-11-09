package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Ejercicio 7. Calculadora: enviar una expresión al hijo y leer el resultado.
 *   Linux   : bc -l            (lee la expresión por su entrada: «2^10» → 1024)
 *   Windows : no hay bc. Se usa PowerShell leyendo de su entrada: powershell -NoProfile -Command -
 *             OJO: la sintaxis cambia: potencia = [math]::Pow(2,10) (en PowerShell ^ no es potencia).
 */
public class Ejercicio07 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Expresión" + (SO.WINDOWS ? " (sintaxis PowerShell, p. ej. 7/2 o [math]::Pow(2,10))" : "") + ": ");
        String expresion = sc.nextLine();
        String[] comando = SO.comando(
                new String[]{"bc", "-l"},
                new String[]{"powershell", "-NoProfile", "-NonInteractive", "-Command", "-"});
        Process p = new ProcessBuilder(comando).start();

        PrintWriter pw = new PrintWriter(p.getOutputStream());   // getOutputStream = la ENTRADA del hijo
        pw.println(expresion);
        pw.close();                     // close() = fin de la entrada: sin esto bc/PowerShell siguen esperando

        BufferedReader salida = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String resultado = salida.readLine();                    // null si el hijo no escribió nada
        while (resultado != null && resultado.isBlank()) {       // PowerShell puede dejar líneas en blanco
            resultado = salida.readLine();
        }
        salida.close();
        BufferedReader errores = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String error = errores.readLine();
        while (error != null && error.isBlank()) {               // bc empieza con una línea en blanco
            error = errores.readLine();
        }
        errores.close();
        int codigo = p.waitFor();

        if (resultado != null && error == null) {
            System.out.println(expresion + " = " + resultado.trim());
        } else {
            System.out.println("No se puede calcular \"" + expresion + "\": " + error + " (código " + codigo + ")");
        }
        sc.close();
    }
}
