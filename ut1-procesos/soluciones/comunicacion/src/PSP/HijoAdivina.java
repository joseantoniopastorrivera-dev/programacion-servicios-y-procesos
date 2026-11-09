package PSP;

import java.util.Random;
import java.util.Scanner;

public class HijoAdivina {
    public static void main(String[] args) {
        int secreto;
        if (args.length > 0) {
            secreto = Integer.parseInt(args[0]);   // para probar con un número fijo
        } else {
            secreto = new Random().nextInt(100) + 1;   // de 1 a 100
        }

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            int intento = Integer.parseInt(sc.nextLine().trim());
            if (intento < secreto) {
                System.out.println("mayor");      // el secreto es mayor
            } else if (intento > secreto) {
                System.out.println("menor");      // el secreto es menor
            } else {
                System.out.println("acertado");
                break;
            }
        }
        sc.close();
    }
}
