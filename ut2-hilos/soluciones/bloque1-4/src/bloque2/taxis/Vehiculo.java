/*
 * UT2 · Relación de ejercicios · Ejercicio 2.4 – Taxis
 *
 * Clase que ya existe en la empresa. NO SE PUEDE MODIFICAR.
 */
package bloque2.taxis;

public class Vehiculo {
    protected final String matricula;
    protected int kmRecorridos = 0;

    public Vehiculo(String matricula) { this.matricula = matricula; }
    public void recorrer(int km) { kmRecorridos += km; }
    public int getKmRecorridos() { return kmRecorridos; }
    public String getMatricula() { return matricula; }
}
