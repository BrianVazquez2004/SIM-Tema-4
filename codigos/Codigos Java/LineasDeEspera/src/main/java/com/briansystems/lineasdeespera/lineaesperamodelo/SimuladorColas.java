package com.briansystems.lineasdeespera.lineaesperamodelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorColas {

    public List<Cliente> simular(double tiempoLlegadaProm, double tiempoServicioProm, int totalClientes) {
        List<Cliente> clientes = new ArrayList<>();
        Random rand = new Random();

        double tiempoActual = 0;
        double servidorDisponible = 0;

        for (int i = 1; i <= totalClientes; i++) {
            // Distribución exponencial simple
            double tLlegada = -tiempoLlegadaProm * Math.log(1 - rand.nextDouble());
            double tServicio = -tiempoServicioProm * Math.log(1 - rand.nextDouble());

            tiempoActual += tLlegada;

            double inicioServicio = Math.max(tiempoActual, servidorDisponible);
            double tiempoEspera = inicioServicio - tiempoActual;
            double finServicio = inicioServicio + tServicio;
            double tiempoEnSistema = finServicio - tiempoActual;

            servidorDisponible = finServicio;

            clientes.add(new Cliente(i, tiempoActual, tServicio, inicioServicio, finServicio, tiempoEspera, tiempoEnSistema));
        }

        return clientes;
    }
}