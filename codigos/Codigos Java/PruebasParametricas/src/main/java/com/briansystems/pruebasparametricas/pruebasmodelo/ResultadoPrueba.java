package com.briansystems.pruebasparametricas.pruebasmodelo;

public class ResultadoPrueba {
    private int idMuestra;
    private double valor;

    public ResultadoPrueba(int idMuestra, double valor) {
        this.idMuestra = idMuestra;
        this.valor = valor;
    }

    public int getIdMuestra() { return idMuestra; }
    public double getValor() { return valor; }
}