import java.util.Scanner;

public class Calculadora {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
int opcion = 0;
double num1, num2;

do {
    System.out.println("---MENÚ CALCULADORA---");
    System.out.println("1. Sumar.");
    System.out.println("2. Restar.");
    System.out.println("3. Multiplicar.");
    System.out.println("4. Dividir.");
    System.out.println("5. Salir.");
    System.out.print("Elige una opción.");

    if(!scanner.hasNextInt()){
        System.out.println("ERROR: Debe introducir un número entero.");
        scanner.next();
        continue;
    }
    opcion = scanner.nextInt();

    if(opcion >= 1 && opcion <= 4){
        System.out.print("Introduce el primer número: ");
                if (!scanner.hasNextDouble()) {
                    System.out.println("Error: Has introducido un carácter no válido. Operación cancelada.");
                    scanner.next(); 
                    continue;       
                }
                num1 = scanner.nextDouble();

               
                System.out.print("Introduce el segundo número: ");
                if (!scanner.hasNextDouble()) {
                    System.out.println("Error: Has introducido un carácter no válido. Operación cancelada.");
                    scanner.next(); 
                    continue;       
                }
                num2 = scanner.nextDouble();

                switch (opcion) {
                    case 1:
                        System.out.println("Resultado: " + (num1 + num2));
                        break;
                    case 2:
                        System.out.println("Resultado: " + (num1 - num2));
                        break;
                    case 3:
                        System.out.println("Resultado: " + (num1 * num2));
                        break;
                    case 4:
                        if (num2 == 0) {
                            System.out.println("Error: No se puede dividir entre cero.");
                        } else {
                            System.out.println("Resultado: " + (num1 / num2));
                        }
                        break;
                }
            } else if (opcion != 5) {
                System.out.println("Opción no válida. Inténtalo de nuevo.");
            }

    }while(opcion!=5);
System.out.println("Saliendo de la calculadora. ¡Hasta pronto!");
        scanner.close();
    }
}
