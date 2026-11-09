package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 2. ¿Quién y dónde? whoami + hostname.
 * Estos dos comandos existen igual en Linux y en Windows (en Windows whoami da el equipo y el usuario separados por una barra invertida).
 */
public class Ejercicio02 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String usuario = ejecutar("whoami");
        String equipo = ejecutar("hostname");
        System.out.println("Usuario " + usuario + " en el equipo " + equipo);
    }

    /** Lanza un comando y devuelve la primera línea de su salida. */
    static String ejecutar(String comando) throws IOException, InterruptedException {
        Process p = new ProcessBuilder(comando).start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String linea = br.readLine();
        br.close();
        p.waitFor();
        return linea == null ? "" : linea.trim();
    }
}
