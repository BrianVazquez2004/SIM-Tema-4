package com.briansystems.lineasdeespera.lineaesperamodelo;

public class Cliente {
    private int id;
    private double tiempoLlegada;
    private double tiempoServicio;
    private double inicioServicio;
    private double finServicio;
    private double tiempoEspera;
    private double tiempoEnSistema;

    public Cliente(int id, double tiempoLlegada, double tiempoServicio, double inicioServicio, double finServicio, double tiempoEspera, double tiempoEnSistema) {
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoServicio = tiempoServicio;
        this.inicioServicio = inicioServicio;
        this.finServicio = finServicio;
        this.tiempoEspera = tiempoEspera;
        this.tiempoEnSistema = tiempoEnSistema;
    }

    // Getters para la Tabla de JavaFX
    public int getId() { return id; }
    public double getTiempoLlegada() { return tiempoLlegada; }
    public double getTiempoServicio() { return tiempoServicio; }
    public double getInicioServicio() { return inicioServicio; }
    public double getFinServicio() { return finServicio; }
    public double getTiempoEspera() { return tiempoEspera; }
    public double getTiempoEnSistema() { return tiempoEnSistema; }
}