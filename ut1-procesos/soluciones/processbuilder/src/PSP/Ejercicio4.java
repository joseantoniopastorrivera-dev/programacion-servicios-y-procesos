package PSP;

import java.io.IOException;

/**
 * Ejercicio 4. ¿Ha ido bien? mkdir pruebas y su código de salida.
 *
 *   Linux   : mkdir pruebas                 → 1.ª vez 0, 2.ª vez 1 («File exists»)
 *   Windows : cmd /c mkdir pruebas          → 1.ª vez 0, 2.ª vez 1 («Ya existe el subdirectorio»)
 *             (mkdir no es un .exe en Windows: es una orden interna de cmd, por eso «cmd /c»)
 */
public class Ejercicio4 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] comando = SO.comando(
                new String[]{"mkdir", "pruebas"},
                new String[]{"cmd", "/c", "mkdir", "pruebas"});
        Process p = new ProcessBuilder(comando).start();
        int codigo = p.waitFor();                     // espera a que termine y devuelve su código de salida
        System.out.println("Código de salida: " + codigo);
        System.out.println(codigo == 0 ? "Carpeta creada" : "Error al crear la carpeta");
    }
}
