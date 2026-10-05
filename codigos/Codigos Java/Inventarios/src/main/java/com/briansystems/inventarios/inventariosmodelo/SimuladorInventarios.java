package com.briansystems.inventarios.inventariosmodelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorInventarios {

    public List<RegistroInventario> simular(int invInicial, int reorden, int cantidadOrden, int diasSimulacion, double demandaProm, double leadTimeProm) {
        List<RegistroInventario> registros = new ArrayList<>();
        Random rand = new Random();

        int inventarioActual = invInicial;
        int pedidoEnTransito = 0;
        int diasParaLlegada = 0;

        for (int dia = 1; dia <= diasSimulacion; dia++) {

            if (pedidoEnTransito > 0) {
                diasParaLlegada--;
                if (diasParaLlegada == 0) {
                    inventarioActual += pedidoEnTransito;
                    pedidoEnTransito = 0;
                }
            }

            int invInicioDia = inventarioActual;

            int demanda = (int) Math.max(0, Math.round(demandaProm + rand.nextGaussian() * 2));

            int faltante = 0;
            if (demanda > inventarioActual) {
                faltante = demanda - inventarioActual;
                inventarioActual = 0;
            } else {
                inventarioActual -= demanda;
            }

            int ordenRealizada = 0;
            if ((inventarioActual + pedidoEnTransito) <= reorden && pedidoEnTransito == 0) {
                ordenRealizada = cantidadOrden;
                pedidoEnTransito = cantidadOrden;

                diasParaLlegada = Math.max(1, (int) Math.round(leadTimeProm + (rand.nextDouble() * 2 - 1)));
            }

            registros.add(new RegistroInventario(
                    dia, invInicioDia, demanda, inventarioActual, faltante, ordenRealizada, diasParaLlegada
            ));
        }

        return registros;
    }
}