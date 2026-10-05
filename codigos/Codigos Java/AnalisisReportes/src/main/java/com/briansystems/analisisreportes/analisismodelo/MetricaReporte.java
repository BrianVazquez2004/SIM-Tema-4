package com.briansystems.analisisreportes.analisismodelo;

public class MetricaReporte {
    private String nombreMetrica;
    private double valorPromedio;
    private double limiteInferior;
    private double limiteSuperior;
    private String estadoEvaluacion;

    public MetricaReporte(String nombreMetrica, double valorPromedio, double limiteInferior, double limiteSuperior, String estadoEvaluacion) {
        this.nombreMetrica = nombreMetrica;
        this.valorPromedio = valorPromedio;
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.estadoEvaluacion = estadoEvaluacion;
    }

    public String getNombreMetrica() { return nombreMetrica; }
    public double getValorPromedio() { return valorPromedio; }
    public double getLimiteInferior() { return limiteInferior; }
    public double getLimiteSuperior() { return limiteSuperior; }
    public String getEstadoEvaluacion() { return estadoEvaluacion; }
}