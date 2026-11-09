package PSP;

/** Tarea 4. Sumador: suma los números que recibe por args y escribe el resultado en su salida. */
public class Sumador {

    public static void main(String[] args) {
        long suma = 0;
        for (String a : args) {
            suma += Long.parseLong(a);
        }
        System.out.println(suma);
    }
}
