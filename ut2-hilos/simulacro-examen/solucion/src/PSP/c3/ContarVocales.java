/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c3;

/*
 * C3.4 · Reflexión: ¿qué pasaría sin synchronized? ¿Bastaría volatile? ¿Hay una forma mejor?
 *
 * Sin synchronized, los cinco hilos harían total++ a la vez: son tres pasos (leer, sumar, escribir)
 * y se perderían vocales; el total saldría menor y distinto en cada ejecución (condición de carrera).
 * Con textos cortos puede salir bien por casualidad, lo que no demuestra nada. volatile NO basta:
 * da visibilidad, pero no hace atómico el ++. Alternativas correctas: AtomicInteger (incrementAndGet,
 * sin cerrojo) o que cada hilo cuente en una variable LOCAL y sume una sola vez al final.
 * VENTAJA de esta última: solo 5 accesos sincronizados en vez de uno por vocal, mucho más rápido.
 * INCONVENIENTE: el recuento solo es correcto al final; no se puede consultar mientras cuentan.
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ejercicio C3.3 · Cuenta las vocales de un fichero con cinco hilos (uno por vocal) que suman
 * en un recuento común, y comprueba el resultado contando también de forma secuencial.
 *
 * Argumento: {@code <fichero>} (por ejemplo datos/texto.txt)
 */
public class ContarVocales {

    /**
     * Punto de entrada: lee el fichero, lanza los cinco hilos, espera a todos y muestra el recuento.
     *
     * @param args ruta del fichero de texto
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: ContarVocales <fichero>");
            System.exit(1);
        }
        String texto = leerTexto(Paths.get(args[0]));

        Recuento recuento = new Recuento();
        Thread[] hilos = new Thread[Recuento.VOCALES.length];
        for (int i = 0; i < hilos.length; i++) {
            char vocal = Recuento.VOCALES[i];
            hilos[i] = new Thread(new ContadorVocal(texto, vocal, recuento), "vocal-" + vocal);
            hilos[i].start();
        }
        try {
            for (Thread h : hilos) {
                h.join();                    // tras join se ve todo lo que escribieron los hilos
            }
        } catch (InterruptedException e) {
            System.err.println("El main fue interrumpido mientras esperaba a los contadores");
            System.exit(1);
        }

        for (char vocal : Recuento.VOCALES) {
            System.out.println(vocal + ": " + recuento.getCantidad(vocal));
        }
        System.out.println("TOTAL: " + recuento.getTotal());
        int secuencial = contarSecuencial(texto);
        System.out.println("Comprobación secuencial: " + secuencial + " → "
                + (secuencial == recuento.getTotal() ? "CORRECTO" : "INCORRECTO"));
    }

    /**
     * Lee el fichero entero y lo pasa a minúsculas. Si no se puede leer, avisa y termina con código 1.
     *
     * @param fichero ruta del fichero
     * @return el texto en minúsculas
     */
    static String leerTexto(Path fichero) {
        try {
            return new String(Files.readAllBytes(fichero), StandardCharsets.UTF_8).toLowerCase();
        } catch (IOException e) {
            System.err.println("No se puede leer el fichero: " + fichero);
            System.exit(1);
            return null;                     // no se llega
        }
    }

    /**
     * Cuenta las vocales (sin tilde) en un solo hilo, para comprobar el resultado concurrente.
     *
     * @param texto texto en minúsculas
     * @return número de vocales
     */
    static int contarSecuencial(String texto) {
        int n = 0;
        for (int i = 0; i < texto.length(); i++) {
            if ("aeiou".indexOf(texto.charAt(i)) >= 0) {
                n++;
            }
        }
        return n;
    }
}
