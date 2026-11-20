/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque2.autoguardado;

/**
 * UT2 · Relación de ejercicios · Ejercicio 2.3 – Autoguardado
 *
 * Guarda el documento cada segundo. Su bucle no tiene condición de fin.
 *
 * SOLUCIÓN. El bucle es while (true): el autoguardado nunca decide terminar. El que hace que el programa
 * acabe es el Editor, marcando este hilo como DAEMON (ver Editor.java).
 */
public class Autoguardado implements Runnable {

    private final StringBuilder documento;

    /**
     * Crea el autoguardado de un documento.
     *
     * @param documento documento que edita el usuario; lo comparten el editor y el autoguardado
     */
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

    /**
     * Trabajo del hilo: guardar periódicamente hasta que termine el programa (es daemon) o lo interrumpan.
     */
    @Override
    public void run() {
        guardarPeriodicamente();
    }
}
