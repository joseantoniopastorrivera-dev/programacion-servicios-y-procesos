/*
 * UT2 · Relación de ejercicios · Ejercicio 2.4 – Taxis
 *
 * Un taxi de la flota: hace 5 carreras de 2 a 15 km (100 ms por km).
 *
 * SOLUCIÓN. Taxi ya extiende Vehiculo y Java NO permite herencia múltiple: no puede extender también
 * Thread. Por eso implementa la interfaz Runnable (una clase puede extender una clase E implementar
 * interfaces a la vez).
 */
package bloque2.taxis;

import java.util.concurrent.ThreadLocalRandom;

public class Taxi extends Vehiculo implements Runnable {

    static final int CARRERAS = 5;
    static final int MS_POR_KM = 100;

    public Taxi(String matricula) {
        super(matricula);
    }

    /** Hace las 5 carreras del día. */
    public void trabajar() {
        for (int c = 1; c <= CARRERAS; c++) {
            int km = ThreadLocalRandom.current().nextInt(2, 16);   // entre 2 y 15 km (el 16 no entra)
            try {
                Thread.sleep((long) km * MS_POR_KM);                // la carrera dura 100 ms por km
            } catch (InterruptedException e) {
                return;                                             // si nos interrumpen, el taxi se retira
            }
            recorrer(km);   // método heredado de Vehiculo: suma a SU PROPIO kmRecorridos (cada taxi el suyo)
            System.out.printf("%s carrera %d: %2d km%n", matricula, c, km);
        }
    }

    @Override
    public void run() {
        trabajar();
    }
}
