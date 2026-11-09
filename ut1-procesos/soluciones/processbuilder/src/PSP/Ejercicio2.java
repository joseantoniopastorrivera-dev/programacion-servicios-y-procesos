package PSP;

/**
 * Ejercicio 2. ¿Quién es quién? PID del proceso lanzado y PID del propio programa Java.
 *
 * Reflexión (comprobar los PID):
 *   Linux   : ps -ef | grep java      y   ps -ef | grep calc
 *   Windows : tasklist | findstr java y   tasklist | findstr -i calc
 */
public class Ejercicio2 {

    public static void main(String[] args) throws InterruptedException {
        Process calculadora = Ejercicio1.abrir("Calculadora", Ejercicio1.CALCULADORAS_LINUX, "calc.exe");
        if (calculadora != null) {
            System.out.println("PID de la calculadora lanzada: " + calculadora.pid());
        }
        // ProcessHandle.current() representa a ESTE programa Java (el proceso padre).
        System.out.println("PID de este programa Java:     " + ProcessHandle.current().pid());
        System.out.println("Esperando 30 s para que compruebes los PID en la terminal...");
        Thread.sleep(30000);
    }
}
