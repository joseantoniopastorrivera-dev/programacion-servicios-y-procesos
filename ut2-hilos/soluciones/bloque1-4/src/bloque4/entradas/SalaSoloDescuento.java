/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking (pregunta 1)
 *
 * La propuesta del compañero: proteger SOLO la instrucción que descuenta la entrada.
 * NO FUNCIONA: el descuento ya no pierde restas, pero la COMPROBACIÓN sigue fuera. Cuatro taquillas
 * pueden ver "queda 1" a la vez, y las cuatro descuentan (de forma muy ordenada…) hasta -3.
 */
package bloque4.entradas;

public class SalaSoloDescuento extends Sala {

    @Override
    public boolean vender() {
        if (disponibles > 0) {              // ← la comprobación, SIN proteger: aquí está el fallo
            cobrar();
            synchronized (this) {
                disponibles--;              // protegido, pero ya es tarde: la decisión se tomó arriba
            }
            return true;
        }
        return false;
    }
}
