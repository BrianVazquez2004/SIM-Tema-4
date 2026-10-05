package com.briansystems.analisisreportes.analisismodelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorReporte {

    public List<MetricaReporte> generarAnalisis(int numCorridas, double nivelConfianza) {
        List<MetricaReporte> lista = new ArrayList<>();
        Random rand = new Random();

        double z = (nivelConfianza == 0.99) ? 2.576 : (nivelConfianza == 0.90) ? 1.645 : 1.96;

        lista.add(crearMetrica("Tiempo Promedio en Cola (min)", 12.5, 3.2, numCorridas, z, 15.0));
        lista.add(crearMetrica("Utilización del Sistema (%)", 84.2, 5.1, numCorridas, z, 90.0));
        lista.add(crearMetrica("Costo Operativo Diario ($)", 1450.0, 120.0, numCorridas, z, 2000.0));
        lista.add(crearMetrica("Clientes Atendidos / Hora", 45.8, 4.0, numCorridas, z, 40.0));

        return lista;
    }

    private MetricaReporte crearMetrica(String nombre, double mediaBase, double desviacion, int n, double z, double umbral) {
        Random rand = new Random();
        double suma = 0.0;
        double[] datos = new double[n];

        for (int i = 0; i < n; i++) {
            datos[i] = mediaBase + rand.nextGaussian() * desviacion;
            suma += datos[i];
        }

        double media = suma / n;
        double sumaCuadrados = 0.0;
        for (double val : datos) {
            sumaCuadrados += Math.pow(val - media, 2);
        }
        double s = Math.sqrt(sumaCuadrados / (n - 1));

        double margenError = z * (s / Math.sqrt(n));
        double inf = Math.round((media - margenError) * 100.0) / 100.0;
        double sup = Math.round((media + margenError) * 100.0) / 100.0;
        double mediaRedondeada = Math.round(media * 100.0) / 100.0;

        String estado = (mediaRedondeada <= umbral) ? "Dentro de Parámetro" : "Alerta / Excede Umbral";

        return new MetricaReporte(nombre, mediaRedondeada, inf, sup, estado);
    }
}