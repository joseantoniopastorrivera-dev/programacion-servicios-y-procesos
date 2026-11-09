/*
 * UT2 · Relación de ejercicios · Ejercicio 2.3 – Autoguardado
 *
 * Guarda el documento cada segundo. Su bucle no tiene condición de fin.
 *
 * SOLUCIÓN. El bucle es while (true): el autoguardado nunca decide terminar. El que hace que el programa
 * acabe es el Editor, marcando este hilo como DAEMON (ver Editor.java).
 */
package bloque2.autoguardado;

public class Autoguardado implements Runnable {

    private final StringBuilder documento;

    public Autoguardado(StringBuilder documento) {
        this.documento = documento;
    }

    /** Guarda el documento cada segundo, sin fin. */
    public void guardarPeriodicamente() {
        int guardado = 0;
        while (true) {                                      // sin condición de fin, como dice el enunciado
            try {
                Thread.sleep(1000);                         // cada segundo
            } catch (InterruptedException e) {
                return;                                     // (la "otra forma" de pararlo: ver Editor)
            }
            String copia;
            synchronized (documento) {                      // el Editor escribe en el MISMO StringBuilder a la vez:
                copia = documento.toString();               // se lee dentro de synchronized para no copiarlo a medio
            }                                               // modificar (se explica en los bloques 3 y 4)
            guardado++;
            System.out.printf("   [autoguardado %d] %d caracteres guardados%n", guardado, copia.length());
        }
    }

    @Override
    public void run() {
        guardarPeriodicamente();
    }
}
