/*
 * UT2 · Relación de ejercicios · Ejercicio 1.1 – Descargas
 *
 * Versión que descarga los ficheros a la vez.
 */
package bloque1.descargas;

public class DescargasConHilos {

    public static void main(String[] args) throws InterruptedException {   // join() puede lanzar InterruptedException
        Descarga[] descargas = {
                new Descarga("video.mp4", 40),
                new Descarga("fotos.zip", 15),
                new Descarga("apuntes.pdf", 25),
                new Descarga("musica.mp3", 10),
                new Descarga("juego.iso", 30)
        };

        System.out.println("Descarga con hilos (un hilo por fichero)");
        Thread[] hilos = new Thread[descargas.length];
        for (int i = 0; i < descargas.length; i++) {
            hilos[i] = new Thread(descargas[i]);   // estado NEW: el hilo existe pero aún no hace nada
            hilos[i].start();                      // start() (NO run()): crea el hilo nuevo y vuelve enseguida
        }
        // Error típico: llamar a hilos[i].run(). Eso ejecuta descargar() en el main, uno detrás de otro:
        // volveríamos a tardar 12 s.

        for (Thread h : hilos) {
            h.join();                              // el main espera a que termine cada hilo
        }
        // Las cinco "descargas" (sleep) transcurren a la vez: el total es el de la más larga, video.mp4 (4 s).
        // Terminan por orden de tamaño: musica (1 s), fotos (1,5), apuntes (2,5), juego (3), video (4).
        System.out.printf("Todas terminadas a los %.2f s%n",
                (System.currentTimeMillis() - Descarga.INICIO) / 1000.0);
    }
}
