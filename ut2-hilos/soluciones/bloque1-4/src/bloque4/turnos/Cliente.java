/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque4.turnos;

/**
 * UT2 · Relación de ejercicios · Ejercicio 4.3 – La máquina de turnos
 *
 * Un cliente que saca 100.000 números de turno.
 */
public class Cliente extends Thread {

    static final int TURNOS = 100_000;

    private final MaquinaTurnos maquina;        // la MISMA máquina para los 4 clientes (compartida)

    /**
     * Los números que ha sacado este cliente. NO necesita protección (pregunta 2): cada cliente tiene su
     * propio array y solo él escribe en él. El main lo lee cuando el cliente ya ha terminado (tras join()).
     */
    private final int[] numeros = new int[TURNOS];

    /**
     * Crea un cliente de la carnicería.
     *
     * @param nombre  nombre del cliente (también será el del hilo)
     * @param maquina máquina de turnos común a todos los clientes
     */
    public Cliente(String nombre, MaquinaTurnos maquina) {
        super(nombre);
        this.maquina = maquina;
    }

    /**
     * Saca sus números y los guarda en su propio array (no compartido).
     */
    public void sacarTurnos() {
        for (int i = 0; i < TURNOS; i++) {
            numeros[i] = maquina.nuevoTurno();
        }
    }

    /**
     * Trabajo del hilo: sacar los turnos.
     */
    @Override
    public void run() {
        sacarTurnos();
    }

    /**
     * Números que ha sacado este cliente. Se leen tras join(), así que no necesitan protección.
     *
     * @return los números obtenidos
     */
    public int[] getNumeros() {
        return numeros;
    }
}
