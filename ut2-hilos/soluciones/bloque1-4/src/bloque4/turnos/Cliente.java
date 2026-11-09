/*
 * UT2 · Relación de ejercicios · Ejercicio 4.3 – La máquina de turnos
 *
 * Un cliente que saca 100.000 números de turno.
 */
package bloque4.turnos;

public class Cliente extends Thread {

    static final int TURNOS = 100_000;

    private final MaquinaTurnos maquina;        // la MISMA máquina para los 4 clientes (compartida)

    /**
     * Los números que ha sacado este cliente. NO necesita protección (pregunta 2): cada cliente tiene su
     * propio array y solo él escribe en él. El main lo lee cuando el cliente ya ha terminado (tras join()).
     */
    private final int[] numeros = new int[TURNOS];

    public Cliente(String nombre, MaquinaTurnos maquina) {
        super(nombre);
        this.maquina = maquina;
    }

    public void sacarTurnos() {
        for (int i = 0; i < TURNOS; i++) {
            numeros[i] = maquina.nuevoTurno();
        }
    }

    @Override
    public void run() {
        sacarTurnos();
    }

    public int[] getNumeros() {
        return numeros;
    }
}
