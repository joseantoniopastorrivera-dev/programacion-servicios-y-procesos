package PSP;

/** Ejercicio 8 (hijo): termina con el código de salida que recibe por args. */
public class Hijo {

    public static void main(String[] args) {
        int codigo = Integer.parseInt(args[0]);
        System.exit(codigo);                 // el código de salida es lo que el padre recibe con waitFor()
    }
}
