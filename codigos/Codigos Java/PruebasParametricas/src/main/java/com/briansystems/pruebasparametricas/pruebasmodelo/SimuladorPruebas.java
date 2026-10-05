package com.briansystems.pruebasparametricas.pruebasmodelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorPruebas {

    private List<Double> datosGenerados = new ArrayList<>();

    public List<ResultadoPrueba> generarMuestras(int n, double mediaReal, double desviacionReal) {
        datosGenerados.clear();
        List<ResultadoPrueba> lista = new ArrayList<>();
        Random rand = new Random();

        for (int i = 1; i <= n; i++) {
            double val = mediaReal + rand.nextGaussian() * desviacionReal;
            datosGenerados.add(val);
            lista.add(new ResultadoPrueba(i, Math.round(val * 10000.0) / 10000.0));
        }

        return lista;
    }

    public double calcularMedia() {
        return datosGenerados.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double calcularDesviacionEstandar(double media) {
        if (datosGenerados.size() <= 1) return 0.0;
        double sumaCuadrados = 0.0;
        for (double val : datosGenerados) {
            sumaCuadrados += Math.pow(val - media, 2);
        }
        return Math.sqrt(sumaCuadrados / (datosGenerados.size() - 1));
    }

    public double calcularEstadisticoZ(double mediaMuestral, double mediaHipotetica, double desviacion, int n) {
        if (desviacion == 0) return 0.0;
        return (mediaMuestral - mediaHipotetica) / (desviacion / Math.sqrt(n));
    }
}