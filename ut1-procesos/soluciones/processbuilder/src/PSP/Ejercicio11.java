package PSP;

import java.io.IOException;
import java.util.Iterator;

/**
 * Ejercicio 11. Árbol de procesos: un padre, dos hijos y un nieto. Sin lambdas: con Iterator.
 *
 *   Linux   : sh -c "sh -c 'sleep 60 & wait' & sleep 60 & wait"
 *             (el & lanza en segundo plano y wait espera a los hijos)
 *   Windows : java -cp bin PSP.NodoArbol 2   (mismo árbol hecho con procesos Java)
 *
 * children()    = solo los hijos DIRECTOS.
 * descendants() = hijos, nietos, bisnietos... (todo el subárbol).
 */
public class Ejercicio11 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] comando = SO.comando(
                new String[]{"sh", "-c", "sh -c 'sleep 60 & wait' & sleep 60 & wait"},
                new String[]{"java", "-cp", "bin", "PSP.NodoArbol", "2"});
        Process p = new ProcessBuilder(comando).start();
        Thread.sleep(SO.WINDOWS ? 2000 : 500);               // los hijos Java tardan más en arrancar

        System.out.println("Proceso padre: PID " + p.pid());
        System.out.println("children():    " + p.children().count());
        System.out.println("descendants(): " + p.descendants().count());

        // children()/descendants() devuelven un Stream<ProcessHandle>: .iterator() lo recorre sin lambdas.
        Iterator<ProcessHandle> it = p.descendants().iterator();
        while (it.hasNext()) {
            ProcessHandle h = it.next();
            // parent() y info().commandLine() devuelven Optional: puede no haber dato → orElse(...)
            long pidPadre = h.parent().isPresent() ? h.parent().get().pid() : -1;
            System.out.println("  PID " + h.pid() + " · padre " + pidPadre
                    + " · " + h.info().commandLine().orElse(h.info().command().orElse("?")));
        }

        // Destruir: primero los descendientes y después el padre.
        it = p.descendants().iterator();
        while (it.hasNext()) {
            it.next().destroy();
        }
        p.destroy();
        p.waitFor();
        Thread.sleep(500);
        System.out.println("Después de destruir: padre vivo = " + p.isAlive()
                + ", descendientes vivos = " + p.descendants().count());
    }
}
