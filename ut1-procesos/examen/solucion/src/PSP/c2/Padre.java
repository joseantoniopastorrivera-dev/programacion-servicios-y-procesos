/*
 * ============================================================
 *  Solución de repaso (no es la entrega de ningún alumno).
 *  El enunciado oficial no venía en el zip: está reconstruido en
 *  ../../../../enunciado-reconstruido.md
 * ============================================================
 */

package PSP.c2;

/*
 * C2.4 · Reflexión: ¿qué cambiaría con hilos en lugar de procesos?
 *
 * Con hilos todo viviría en UN solo proceso (una sola JVM): cada grupo lo calcularía
 * un Thread en vez de un HijoEstadistica lanzado con ProcessBuilder.
 * - Memoria: los hilos COMPARTEN la memoria del proceso. Ya no harían falta tuberías,
 *   redirecciones ni un formato de texto: cada hilo dejaría su suma en una variable
 *   u objeto común. Los procesos no comparten nada y por eso se comunican por stdin/stdout.
 * - Riesgo nuevo: al compartir memoria aparecen las condiciones de carrera. Si dos hilos
 *   hacen suma += x a la vez se pierden datos; habría que sincronizar (synchronized)
 *   o dar a cada hilo su propio resultado y sumarlos al final.
 * - Coste: crear un hilo y cambiar de un hilo a otro es mucho más barato que arrancar
 *   una JVM nueva por cada grupo.
 * - Aislamiento: se pierde. Si un proceso hijo falla, el padre sigue y lo ve en el
 *   código de salida; una excepción no controlada o un System.exit en un hilo
 *   afecta a todo el programa. Tampoco habría códigos de salida ni destroy().
 * - Espera: en vez de waitFor() se usaría join().
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;

/**
 * Ejercicio C2.2: lanza un HijoEstadistica por grupo y calcula la media global.
 */
public class Padre {

    public static void main(String[] args) throws IOException, InterruptedException {

        String[] grupos = {"grupoA", "grupoB", "grupoC"};

        // Misma JVM y mismo classpath que el padre (forma segura de "Java lanza Java").
        // En el examen valía también: new ProcessBuilder("java", "-cp", "bin", "PSP.c2.HijoEstadistica", grupo)
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        // Los tres hijos AÑADEN sus avisos al mismo fichero (appendTo). Lo borro al empezar
        // para que no se mezclen con los de ejecuciones anteriores. Si no existe, delete() no hace nada.
        File errores = new File("errores.log");
        errores.delete();

        // --- 1. Lanzar TODOS los hijos (sin esperar a ninguno todavía) --------
        // Si hiciera start() y waitFor() en el mismo bucle irían en secuencia, uno detrás de otro.
        Process[] hijos = new Process[grupos.length];
        for (int i = 0; i < grupos.length; i++) {
            ProcessBuilder pb = new ProcessBuilder(java, "-cp", classpath, "PSP.c2.HijoEstadistica", grupos[i]);
            // stdin del hijo  <  datos/grupoX.txt   (el hijo lee el fichero como si fuera su teclado)
            pb.redirectInput(new File("datos/" + grupos[i] + ".txt"));
            // stderr del hijo >> errores.log        (añade; con new File(...) cada hijo borraría lo del anterior)
            pb.redirectError(Redirect.appendTo(errores));
            // stdout NO se redirige: se queda en la tubería (PIPE), que es lo que leerá el padre.
            hijos[i] = pb.start();
        }

        // --- 2. Recoger el resultado de cada hijo ------------------------------
        int totalNotas = 0;                               // notas válidas de todos los grupos
        double sumaTotal = 0;                             // suma de todas esas notas

        for (int i = 0; i < grupos.length; i++) {
            Process p = hijos[i];

            // REGLA DE ORO: primero leer la salida del hijo, DESPUÉS waitFor().
            // getInputStream() = lo que ENTRA al padre = lo que SALE del hijo (su stdout).
            String respuesta;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                respuesta = br.readLine();                // el hijo escribe una sola línea (o ninguna si falla)
            }
            int codigo = p.waitFor();                     // espera a que termine y da su código de salida

            if (codigo == 0 && respuesta != null) {
                // Formato acordado con el hijo: nombre;validas;suma;media;maxima;minima
                String[] partes = respuesta.split(";");
                int validas = Integer.parseInt(partes[1]);
                double suma = Double.parseDouble(partes[2]);
                System.out.println(partes[0] + ": " + validas + " notas válidas, media " + partes[3]
                        + ", máxima " + partes[4] + ", mínima " + partes[5]);
                totalNotas += validas;
                sumaTotal += suma;
            } else {
                // Código distinto de 0: ese grupo no aporta nada a la media global.
                System.out.println(grupos[i] + ": sin resultado (código de salida " + codigo + ")");
            }
        }

        // --- 3. Media global ---------------------------------------------------
        // Se divide la suma de TODAS las notas entre el número de notas.
        // Hacer la media de las medias sería un error: los grupos no tienen el mismo tamaño.
        if (totalNotas > 0) {
            System.out.println("MEDIA GLOBAL: " + (sumaTotal / totalNotas) + " (" + totalNotas + " notas)");
        } else {
            System.out.println("MEDIA GLOBAL: no hay notas válidas en ningún grupo");
        }
        System.out.println("Los avisos de los hijos están en " + errores.getName());
    }
}
