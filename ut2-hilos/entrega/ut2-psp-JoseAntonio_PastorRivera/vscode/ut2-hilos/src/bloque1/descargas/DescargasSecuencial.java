/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque1.descargas;

/**
 * UT2 · Relación de ejercicios · Ejercicio 1.1 – Descargas
 *
 * Versión que descarga los ficheros uno detrás de otro.
 */
public class DescargasSecuencial {

    /**
     * Descarga los cinco ficheros uno detrás de otro, todos en el hilo main.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        // Los cinco ficheros del enunciado, en el orden en que aparecen.
        Descarga[] descargas = {
                new Descarga("video.mp4", 40),
                new Descarga("fotos.zip", 15),
                new Descarga("apuntes.pdf", 25),
                new Descarga("musica.mp3", 10),
                new Descarga("juego.iso", 30)
        };

        System.out.println("Descarga secuencial (un único hilo: main)");
        for (Descarga d : descargas) {
            d.descargar();      // llamada NORMAL a un método: el main no sigue hasta que esta descarga termina
        }
        // Resultado: 4 + 1,5 + 2,5 + 1 + 3 = 12 s, y terminan en el orden del array.
        System.out.printf("Todas terminadas a los %.2f s%n",
                (System.currentTimeMillis() - Descarga.INICIO) / 1000.0);
    }
}
