/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 3. Saludo según la hora.
 *   Linux   : date +%H                                         → 00..23
 *   Windows : powershell -NoProfile -Command Get-Date -Format HH → 00..23 (date de cmd no admite formato)
 * Reflexión: en la terminal se escribe date "+%H" porque la SHELL interpreta caracteres especiales;
 * ProcessBuilder no usa shell: cada argumento llega tal cual, así que no hacen falta comillas.
 */
public class Ejercicio03 {

    /**
     * Obtiene la hora del sistema con un proceso hijo y saluda según la franja (buenos días, tardes o noches).
     *
     * @param args opcional: una hora (0-23) para probar todas las franjas sin esperar
     * @throws IOException          si no se puede lanzar el proceso o leer su salida
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int hora;
        if (args.length > 0) {
            hora = Integer.parseInt(args[0]);                    // para probar todas las franjas
        } else {
            String[] comando = SO.comando(
                    new String[]{"date", "+%H"},
                    new String[]{"powershell", "-NoProfile", "-Command", "Get-Date -Format HH"});
            Process p = new ProcessBuilder(comando).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String texto = br.readLine().trim();
            br.close();
            p.waitFor();
            hora = Integer.parseInt(texto);                      // "09" → 9
        }
        System.out.println("Son las " + hora + " horas");
        if (hora >= 6 && hora <= 13) {
            System.out.println("Buenos días");
        } else if (hora >= 14 && hora <= 20) {
            System.out.println("Buenas tardes");
        } else {
            System.out.println("Buenas noches");
        }
    }
}
