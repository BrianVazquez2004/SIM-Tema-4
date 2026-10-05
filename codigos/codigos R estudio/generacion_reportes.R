suppressPackageStartupMessages(library(tcltk))

GenerarEstadisticasResumen <- function(num_replicas) {
  set.seed(42)
  
  promedios_espera <- rnorm(num_replicas, mean = 12.5, sd = 2.1)
  promedios_espera <- pmax(0.5, promedios_espera)
  
  media_global <- mean(promedios_espera)
  sd_global    <- sd(promedios_espera)
  ic_inferior  <- media_global - 1.96 * (sd_global / sqrt(num_replicas))
  ic_superior  <- media_global + 1.96 * (sd_global / sqrt(num_replicas))
  
  return(list(
    "Replicas Analizadas" = sprintf("%d replicas", num_replicas),
    "Tiempo Espera Medio" = sprintf("%.4f min", media_global),
    "Desviacion Estandar" = sprintf("%.4f min", sd_global),
    "Minimo Observado"    = sprintf("%.4f min", min(promedios_espera)),
    "Maximo Observado"    = sprintf("%.4f min", max(promedios_espera)),
    "IC 95%% Inferior"    = sprintf("%.4f min", ic_inferior),
    "IC 95%% Superior"    = sprintf("%.4f min", ic_superior)
  ))
}

crear_interfaz_reportes <- function() {
  tt <- tktoplevel()
  tkwm.title(tt, "Tema 4.4 - Analisis de Resultados y Reportes en R")
  tkwm.geometry(tt, "500x450")
  tkwm.resizable(tt, FALSE, FALSE)
  
  var_replicas <- tclVar("30")
  
  frame_inputs <- tkwidget(tt, "labelframe", text = " Configuracion del Experimento ", padx = 10, pady = 10)
  tkpack(frame_inputs, fill = "x", padx = 15, pady = 10)
  
  lbl_replicas <- tklabel(frame_inputs, text = "Numero de Replicas de Simulacion:")
  ent_replicas <- tkentry(frame_inputs, textvariable = var_replicas, width = 12)
  tkgrid(lbl_replicas, row = 0, column = 0, sticky = "w", padx = 5, pady = 5)
  tkgrid(ent_replicas, row = 0, column = 1, padx = 5, pady = 5)
  
  frame_results <- tkwidget(tt, "labelframe", text = " Reporte Estadistico de Resultados ", padx = 10, pady = 10)
  tkpack(frame_results, fill = "both", expand = TRUE, padx = 15, pady = 10)
  
  listbox_results <- tklistbox(frame_results, height = 7)
  tkpack(listbox_results, fill = "both", expand = TRUE, padx = 5, pady = 5)
  
  ejecutar_reporte <- function() {
    rep_val <- as.integer(tclvalue(var_replicas))
    
    tkdelete(listbox_results, 0, "end")
    
    if (is.na(rep_val) || rep_val < 2) {
      tkmessagebox(type = "ok", icon = "error", message = "Ingresa un numero de replicas entero mayor o igual a 2.")
      return()
    }
    
    tryCatch({
      res <- GenerarEstadisticasResumen(rep_val)
      for (nombre in names(res)) {
        linea <- paste0(nombre, ": ", res[[nombre]])
        tkinsert(listbox_results, "end", linea)
      }
    }, error = function(e) {
      tkmessagebox(type = "ok", icon = "error", message = e$message)
    })
  }
  
  btn_calcular <- tkbutton(tt, text = "Generar Reporte Completo", command = ejecutar_reporte)
  tkpack(btn_calcular, pady = 5)
  
  tcl("update")
  return(tt)
}

ventana <- crear_interfaz_reportes()