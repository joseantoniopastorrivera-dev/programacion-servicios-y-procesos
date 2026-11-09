package PSP;

public class HijoPrimos {
    public static void main(String[] args) {
        int desde = Integer.parseInt(args[0]);
        int hasta = Integer.parseInt(args[1]);

        int contador = 0;
        for (int n = desde; n <= hasta; n++) {
            if (esPrimo(n)) {
                contador++;
            }
        }
        System.out.println(contador);   // solo el resultado: el padre lo lee
    }

    private static boolean esPrimo(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
