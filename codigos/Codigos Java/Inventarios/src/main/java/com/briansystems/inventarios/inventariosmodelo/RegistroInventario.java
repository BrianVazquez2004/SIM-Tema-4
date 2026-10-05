package com.briansystems.inventarios.inventariosmodelo;

public class RegistroInventario {
    private int dia;
    private int inventarioInicial;
    private int demanda;
    private int inventarioFinal;
    private int faltante;
    private int orden;
    private int diasParaEntrega;

    public RegistroInventario(int dia, int inventarioInicial, int demanda, int inventarioFinal, int faltante, int orden, int diasParaEntrega) {
        this.dia = dia;
        this.inventarioInicial = inventarioInicial;
        this.demanda = demanda;
        this.inventarioFinal = inventarioFinal;
        this.faltante = faltante;
        this.orden = orden;
        this.diasParaEntrega = diasParaEntrega;
    }


    public int getDia() { return dia; }
    public int getInventarioInicial() { return inventarioInicial; }
    public int getDemanda() { return demanda; }
    public int getInventarioFinal() { return inventarioFinal; }
    public int getFaltante() { return faltante; }
    public int getOrden() { return orden; }
    public int getDiasParaEntrega() { return diasParaEntrega; }
}