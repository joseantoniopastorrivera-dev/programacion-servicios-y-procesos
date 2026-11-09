public class NotasClase {
    public static void main(String[] args) {
        // Declaración e inicialización del array con las notas
        double[] notas = {4.5, 7.0, 8.5, 3.0, 9.5, 5.0, 2.5, 6.0};
        
        int aprobados = 0;
        int suspensos = 0;

        // Recorremos el array evaluando cada nota
        for (double nota : notas) {
            if (nota >= 5.0) {
                aprobados++;
            } else {
                suspensos++;
            }
        }

        // Cálculo del total y del porcentaje
        int totalAlumnos = notas.length;
        // Hacemos un cast a double en la división para no perder los decimales antes de multiplicar por 100
        double porcentajeAprobados = ((double) aprobados / totalAlumnos) * 100;

        // Mostrar resultados por pantalla
        System.out.println("--- ESTADÍSTICAS DE LA CLASE ---");
        System.out.println("Total de alumnos: " + totalAlumnos);
        System.out.println("Alumnos aprobados: " + aprobados);
        System.out.println("Alumnos suspendidos: " + suspensos);
        System.out.println("Porcentaje de aprobados: " + porcentajeAprobados + "%");
    }
}