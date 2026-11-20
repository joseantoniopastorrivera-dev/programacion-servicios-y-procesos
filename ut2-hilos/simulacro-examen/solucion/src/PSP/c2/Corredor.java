package PSP.c2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Corredor del ejercicio C2 (YA HECHO, no hay que modificarlo).
 *
 * Da 10 pasos; cada paso le lleva entre 100 y 400 ms. Al terminar cruza la meta,
 * que es un objeto COMPARTIDO por todos los corredores. Si le interrumpen mientras
 * corre, se retira.
 */
public class Corredor implements Runnable {

    /** Pasos que tiene la carrera. */
    public static final int PASOS = 10;

    private final String nombre;
    private final Meta meta;

    /**
     * @param nombre nombre del corredor, por ejemplo "Corredor 1"
     * @param meta   meta común de la carrera
     */
    public Corredor(String nombre, Meta meta) {
        this.nombre = nombre;
        this.meta = meta;
    }

    @Override
    public void run() {
        try {
            for (int paso = 1; paso <= PASOS; paso++) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 401));
                System.out.println(nombre + " avanza al paso " + paso);
            }
            meta.cruzar(nombre);
        } catch (InterruptedException e) {
            System.out.println(nombre + " se retira: tiempo agotado");
        }
    }
}
